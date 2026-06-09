"""
Stale PR Reminder Bot - V1
"""

from __future__ import annotations

import os
from datetime import datetime, timezone
from typing import List, Tuple

import requests
from github import Github
from github.PullRequest import PullRequest

STALE_MINUTES = int(os.getenv("STALE_MINUTES", "30"))
SPAM_PREVENTION_MINUTES = int(os.getenv("SPAM_PREVENTION_MINUTES", "10"))
REQUIRED_APPROVALS = int(os.getenv("REQUIRED_APPROVALS", "2"))
MERGER = os.getenv("MERGER", "pp-pmitra")

# Per-status guidance shown in the reminder comment.
STATUS_ACTIONS = {
    "Ready To Merge": "All required approvals are in. Please merge when ready.",
    "No Reviewers Assigned": "Please assign reviewers to get this moving.",
    "Waiting For Review": "Please review this PR.",
    "Changes Requested": "Please address the unresolved review threads.",
    "Review Discussion In Progress": (
        "**Author:** Please address any outstanding review feedback "
        "if any remains unresolved.\n\n"
        "**Reviewers:** Please review any updates that may have been "
        "provided. If satisfied, please approve the PR. Otherwise, "
        "request additional changes."
    ),
    "Comments Received": (
        "**Author:** Please review and address comments if needed.\n\n"
        "**Reviewers:** Please approve if satisfied or provide "
        "additional feedback."
    ),
    "Awaiting Additional Approval": (
        "One or more additional approvals are still needed."
    ),
}


def get_now() -> datetime:
    return datetime.now(timezone.utc)


def get_pr_age_minutes(pr: PullRequest) -> float:
    return (get_now() - pr.created_at).total_seconds() / 60


def get_last_activity_days(pr: PullRequest) -> float:
    return (get_now() - pr.updated_at).total_seconds() / 86400


def get_requested_reviewers(pr: PullRequest) -> List[str]:
    try:
        users, _teams = pr.get_review_requests()
        return [user.login for user in users if user.type != "Bot"]
    except Exception as exc:
        print(f"Unable to fetch reviewers for PR #{pr.number}: {exc}")
        return []

def get_issue_commenters(pr: PullRequest) -> List[str]:
    commenters = set()
    for comment in pr.get_issue_comments():
        if (
            comment.user
            and comment.user.type != "Bot"
        ):
            commenters.add(comment.user.login)

    return list(commenters)


def analyze_reviews(
    pr: PullRequest,
) -> Tuple[List[str], List[str], List[str], int]:
    """Reduce a PR's review history to effective per-user verdicts.

    A COMMENTED review never overrides a standing APPROVED / CHANGES_REQUESTED
    verdict (this matches GitHub), so an approve-then-comment still counts as an
    approval. Returns (approved_by, changes_requested_by, commented_by,
    review_count).
    """
    decision = {}      # login -> APPROVED / CHANGES_REQUESTED / DISMISSED
    commented = set()  # logins that left a COMMENTED review
    seen = set()       # any login that submitted any review

    for review in pr.get_reviews():
        if not review.user or review.user.type == "Bot":
            continue
        login = review.user.login
        seen.add(login)
        if review.state in ("APPROVED", "CHANGES_REQUESTED", "DISMISSED"):
            decision[login] = review.state
        elif review.state == "COMMENTED":
            commented.add(login)

    approved_by = [u for u, s in decision.items() if s == "APPROVED"]
    changes_requested_by = [
        u for u, s in decision.items() if s == "CHANGES_REQUESTED"
    ]
    commented_by = [
        u
        for u in commented
        if decision.get(u) not in ("APPROVED", "CHANGES_REQUESTED")
    ]
    return approved_by, changes_requested_by, commented_by, len(seen)

def get_review_feedback(pr: PullRequest) -> List[str]:

    feedback = []

    for review in pr.get_reviews():

        if not review.user or review.user.type == "Bot":
            continue

        body = (review.body or "").strip()

        if not body:
            continue

        if review.state in ("CHANGES_REQUESTED", "COMMENTED"):

            feedback.append(
                f"- @{review.user.login} [{review.state}]\n  {body}"
            )

    return feedback

def get_discussion_comments(pr: PullRequest) -> List[str]:

    comments = []

    for comment in pr.get_issue_comments():

        if (
            not comment.user
            or comment.user.type == "Bot"
        ):
            continue

        body = (comment.body or "").strip()

        if not body:
            continue

        comments.append(
            f"- @{comment.user.login}\n  {body}"
        )
    return comments


def get_unresolved_review_threads(
    repository_name: str,
    pr_number: int,
) -> Tuple[List[str], str]:

    owner, repo = repository_name.split("/")

    query = """
    query($owner:String!, $repo:String!, $number:Int!) {
      repository(owner:$owner, name:$repo) {
        pullRequest(number:$number) {
          reviewThreads(first:100) {
            nodes {
              isResolved
              comments(first:20) {
                nodes {
                  body
                  path
                  author {
                    login
                  }
                }
              }
            }
          }
        }
      }
    }
    """

    response = requests.post(
        "https://api.github.com/graphql",
        headers={"Authorization": f"Bearer {os.environ['GITHUB_TOKEN']}"},
        json={
            "query": query,
            "variables": {
                "owner": owner,
                "repo": repo,
                "number": pr_number,
            },
        },
        timeout=30,
    )

    response.raise_for_status()

    threads = (
        response.json()["data"]["repository"]["pullRequest"]
        ["reviewThreads"]["nodes"]
    )

    unresolved_reviewers = set()
    summary_lines = []

    for thread in threads:

        if thread["isResolved"]:
            continue

        comments = thread["comments"]["nodes"]

        if not comments:
            continue

        first_comment = comments[0]
        reviewer = first_comment["author"]["login"]

        unresolved_reviewers.add(reviewer)

        summary_lines.append(
            f"- **{first_comment['path']}** "
            f"(Reviewer: @{reviewer}) "
            f"{first_comment['body']}"
        )

    if not summary_lines:
        summary = "_No unresolved review items_"
    else:
        summary = "\n".join(summary_lines)

    return list(unresolved_reviewers), summary


def determine_status(
    pr: PullRequest,
    unresolved_reviewers: List[str],
    issue_commenters: List[str],
) -> Tuple[str, str, List[str]]:

    approved_by, changes_requested_by, commented_by, review_count = (
        analyze_reviews(pr)
    )
    requested_reviewers = get_requested_reviewers(pr)
    approval_count = len(approved_by)
    author = pr.user.login

    # 1. Open unresolved review threads always win
    if unresolved_reviewers:
        return "Changes Requested", author, approved_by

    # 1. Ready To Merge - only when nothing is blocking. A standing
    # CHANGES_REQUESTED review blocks merge on GitHub even with enough
    # approvals, so it takes precedence over Ready (handled in 4 & 5 below).
    if approval_count >= REQUIRED_APPROVALS and not changes_requested_by:
        return "Ready To Merge", MERGER, approved_by

    # 2 & 3. Nothing has been reviewed yet
    if review_count == 0:
        if requested_reviewers:
            responsible = ", ".join(f"@{r}" for r in requested_reviewers)
            return "Waiting For Review", responsible, approved_by
        return "No Reviewers Assigned", author, approved_by

    # 4 & 5. A change was requested
    if changes_requested_by:

        participants = sorted(
                            set([author]
                                + changes_requested_by
                                + commented_by
                                + requested_reviewers
                                + issue_commenters
                            )
                            - set(approved_by)
                        )
    
        responsible = ", ".join(
            f"@{user}" for user in participants
        )
    
        return "Review Discussion In Progress", responsible, approved_by
        
    # 6. Comments only, no block (changes already handled above)
    if commented_by:
        participants = sorted(
                            set([author]
                                + commented_by
                                + requested_reviewers
                                + issue_commenters
                            )
                            - set(approved_by)
                        )
        
        responsible = ", ".join(
            f"@{user}" for user in participants
        )
        
        return "Comments Received", responsible, approved_by

    # 7. Some approvals, still short of the bar
    if approval_count > 0:
        return (
            "Awaiting Additional Approval",
            "@reviewers",
            approved_by,
        )

    # Fallback: only dismissed/withdrawn reviews remain.
    if requested_reviewers:
        responsible = ", ".join(f"@{r}" for r in requested_reviewers)
        return "Waiting For Review", responsible, approved_by
    return "No Reviewers Assigned", author, approved_by


def recently_reminded(pr: PullRequest) -> bool:

    comments = list(pr.get_issue_comments())

    for comment in reversed(comments):

        if (
            comment.user
            and comment.user.type == "Bot"
            and "Stale PR Reminder" in comment.body
        ):

            age_minutes = (
                get_now() - comment.created_at
            ).total_seconds() / 60

            return age_minutes < SPAM_PREVENTION_MINUTES

    return False


def build_comment(
    pr: PullRequest,
    status: str,
    responsible: str,
    approved_by: List[str],
    pr_age_minutes: float,
    last_activity_days: float,
    review_summary: str,
) -> str:

    pr_age_days = pr_age_minutes / 1440
    stale_days = STALE_MINUTES / 1440

    approvals = ", ".join(approved_by) if approved_by else "None"

    responsible_display = responsible
    if not responsible.startswith("@"):
        responsible_display = f"@{responsible}"

    action = STATUS_ACTIONS.get(status, "Please take a look.")

    return f"""
## 🤖 Stale PR Reminder

### PR Details

| Metric | Value |
|----------|----------|
| PR | #{pr.number} |
| Author | @{pr.user.login} |
| PR Age | {pr_age_days:.2f} days |
| Last Activity | {last_activity_days:.1f} days ago |
| Status | {status} |
| Action Item On | {responsible_display} |

### Next Steps

{action}

### Approvals

Required Approvals: {REQUIRED_APPROVALS}

Current Approvals: {len(approved_by)}

Approved By:
{approvals}

### PR Feedback

{review_summary}

---

This PR has been open for more than {stale_days:.0f} days.
"""


def main() -> None:

    github_client = Github(os.environ["GITHUB_TOKEN"])

    repository = github_client.get_repo(os.environ["GITHUB_REPOSITORY"])

    print(f"Scanning repository {repository.full_name}")

    for pr in repository.get_pulls(state="open"):

        if pr.draft:
            continue

        pr_age_minutes = get_pr_age_minutes(pr)

        if pr_age_minutes < STALE_MINUTES:
            continue

        if recently_reminded(pr):
            continue

        unresolved_reviewers, review_summary = get_unresolved_review_threads(
            repository.full_name,
            pr.number,
        )

        issue_commenters = get_issue_commenters(pr)

        review_feedback = get_review_feedback(pr)
        
        discussion_comments = get_discussion_comments(pr)

        feedback_sections = []
        
        if review_summary != "_No unresolved review items_":
            feedback_sections.append(
                "### Code Review Threads\n\n" + review_summary
            )
        
        if review_feedback:
            feedback_sections.append(
                "### Review Feedback\n\n" +
                "\n\n".join(review_feedback)
            )
        
        if discussion_comments:
            feedback_sections.append(
                "### Discussion Comments\n\n" +
                "\n\n".join(discussion_comments)
            )
        
        review_summary = (
            "\n\n".join(feedback_sections)
            if feedback_sections
            else "_No feedback found_"
        )

        status, responsible, approved_by = determine_status(
            pr,
            unresolved_reviewers,
            issue_commenters,
        )

        comment = build_comment(
            pr=pr,
            status=status,
            responsible=responsible,
            approved_by=approved_by,
            pr_age_minutes=pr_age_minutes,
            last_activity_days=get_last_activity_days(pr),
            review_summary=review_summary,
        )

        pr.create_issue_comment(comment)

        print(f"Reminder posted for PR #{pr.number}")


if __name__ == "__main__":
    main()
