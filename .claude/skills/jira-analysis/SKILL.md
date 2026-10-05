---
name: jira-analysis
description: Analyze Jira tickets, ticket lists, Jira URLs, or fix versions for QA readiness and produce Netra's structured analysis, test coverage, and deterministic review artifacts. Use for requirement analysis before Gherkin or automation work.
---

# Jira QA Analysis

Produce a source-traceable QA analysis without inventing requirements, history, dependencies, or navigation.

## Inputs

Accept exactly one Jira scope:

- One ticket key.
- A comma-separated ticket list.
- A Jira URL that resolves to ticket keys or a Jira filter/release.
- A fix version containing Stories.

Optional settings:

- `detailMode`: `full` by default; `fast` skips linked and parent expansion.
- `productionProjectKey`: `HT` by default.
- `confluenceDetailMode`: `disabled` by default, otherwise `shallow` or `full`.
- `artifactMode`: `compatibility` by default; `analysis_only` skips DOCX, XLSX, and HTML generation.

Ask for the Jira scope only when none can be resolved. If several scopes are supplied, prefer explicit ticket keys over a broader URL or fix version.

## Required resources

- Validate the final JSON against `../../contracts/analysis-output.schema.json`.
- Read `../risk-assessment/SKILL.md` before assigning readiness flags or production-risk ratings.
- Read `../test-coverage/SKILL.md` before producing requirements and test cases.
- Read [references/confluence-search.md](references/confluence-search.md) only when Confluence analysis is enabled.
- Use `scripts/build-deliverables.py` only after the JSON passes schema validation.

## Workflow

1. Resolve the input to Jira keys and record the input type and value.
2. Discover each primary issue using its key, summary, status, issue type, priority, assignee, labels, components, fix versions, resolution, and identifiers for links, comments, attachments, parent, and subtasks. Then hydrate the primary description and only the discovered comments or attachments retained as evidence. Preserve the issue type, status, priority, assignee, components, and fix versions in the ticket's optional `metadata` object. Follow pagination.
3. Start each ticket's `sourceManifest`. Record the primary ticket as fetched, then record every discovered parent, linked issue, subtask, attachment, and comment by its stable identifier. Use `disposition: fetched` only when its full evidence was read. Use `disposition: excluded` with a concrete relevance or detail-mode `reason` when it was not hydrated or used. Never silently drop a discovered source.
4. In `full` mode, deduplicate related keys and fetch their compact metadata in groups of at most 50. Hydrate descriptions and comments only for related issues retained as evidence. In `fast` mode, retain discovered related keys in the source manifest as excluded with the detail-mode reason.
5. If Confluence analysis is enabled, perform the contextual search flow and attach returned pages to the relevant tickets.
6. Query recent production issues using the configured production project, derived components or labels, `created >= -365d`, newest first, and at most 50 results. Request only key, summary, status, components, labels, and dates for discovery. Hydrate descriptions and comments only for issues retained as production-risk evidence; record every other discovered key as excluded with a concrete reason. Use free-text search only when no structured mapping exists.
7. Read `navigation-map/navigation-tree.html` once. Parse only the `GRAPH` object and match each ticket to a node and module. Do not calculate navigation paths.
8. Derive functional requirements, gaps, ambiguities, dependencies, risks, and source references.
9. Apply the risk-assessment skill and then the test-coverage skill.
10. Build the release summary from the completed ticket records. Round `averageTestCasesPerTicket` to two decimal places.
11. Write and validate one `analysis.json`. In `compatibility` mode, run the deterministic builder. In `analysis_only` mode, leave all `compatibilityArtifacts` paths `null` and stop after validation.

## Analysis rules

- Use the ticket, comments, parent, epic, links, subtasks, relevant documentation, production issues, and repository context as evidence.
- Treat missing acceptance criteria as normal. Infer requirements from available context and record confidence.
- Record a gap only when a functional rule, validation, limit, state, permission, error path, or integration contract cannot be determined.
- Record an ambiguity only when competing interpretations materially change behavior.
- Exclude purely cosmetic layout, color, spacing, typography, animation, and copy preferences.
- UI details are in scope only when they expose a functional rule or observable functional outcome.
- Cite every requirement and risk with `sourceRefs`.
- Keep `sourceRefs` focused on evidence actually used, while `sourceManifest` audits both fetched and explicitly excluded inputs.
- Use `contextNote` when the analysis depends mainly on parent, linked, or documentation context.
- A navigation framework gap is a dependency note, not an analysis blocker.
- Represent unresolved observation questions as blocked test cases, never as executable expected results. The blocked case comments must identify the missing behavioral fact or fixture detail.
- Reject defect-name-only fixtures. Regression test data must state the concrete source-backed configuration, state, values, or history required to reproduce the behavior.

## Source discipline

- Use Jira tools or the configured Jira MCP server for Jira data. Do not scrape or guess inaccessible Jira content.
- If a required connector is unavailable, return `blocked` with the missing capability instead of fabricating data.
- Never invent issue keys, URLs, statuses, assignees, comments, documentation, or production history.
- Do not refetch sources after the structured analysis is complete unless validation exposes a specific missing field.
- Keep connector credentials and tokens outside repository files.

## Output

The authoritative output in both artifact modes is `analysis.json` conforming to the shared contract. It includes:

- Run metadata, input, blockers, assumptions, and source references.
- One complete record per ticket.
- Requirements, readiness, risks, dependencies, navigation context, and test coverage.
- Available Jira ticket metadata needed by the human-readable compatibility artifacts.
- Release summary and compatibility artifact paths.

In `compatibility` mode, run the builder with:

```bash
python3 .claude/skills/jira-analysis/scripts/build-deliverables.py analysis.json
```

The builder writes DOCX, XLSX, and HTML compatibility artifacts to `target/netra/` unless `--output-dir` is supplied, verifies them, and records their paths in `analysis.json` under `compatibilityArtifacts`. Content comes from validated JSON; do not recreate formatting or document content in ad hoc scripts. Do not run the builder in `analysis_only` mode.

## Validation

Run the skill tests with:

```bash
python3 -m unittest discover \
  -s .claude/skills/jira-analysis/tests \
  -p 'test_*.py'
```
