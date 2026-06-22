"""
write_prompt.py
Usage: python3 write_prompt.py <jira_id> <domain>

Reads env vars: SUMMARY, FEATURE_DIR
Reads files:    jira_description.txt, jira_comments.txt,
                step_dictionary.txt, scenario_dictionary.txt
Writes:         prompt.txt
"""
import os
import re
import sys
import glob

# ── Args ──────────────────────────────────────────────────────────────────────
if len(sys.argv) < 3:
    print("Usage: write_prompt.py <jira_id> <domain>")
    sys.exit(1)

jira_id     = sys.argv[1]
domain      = sys.argv[2]
domain_text = domain if domain else "general"
feature_dir = os.environ.get("FEATURE_DIR", "src/test/resources/features")

# ── Pull Jira content ─────────────────────────────────────────────────────────
summary_trim = os.environ.get("SUMMARY", "No Summary")[:300]

desc_file = "jira_description.txt"
if os.path.exists(desc_file):
    with open(desc_file, "r", encoding="utf-8") as f:
        desc_trim = f.read().strip()[:8000]
    print(f"Description loaded from file: {len(desc_trim)} chars")
else:
    desc_trim = os.environ.get("DESCRIPTION", "No Description")[:8000]
    print("::warning::jira_description.txt not found, falling back to env var")

comments_file = "jira_comments.txt"
if os.path.exists(comments_file):
    with open(comments_file, "r", encoding="utf-8") as f:
        comments_trim = f.read().strip()[:2000]
    print(f"Comments loaded from file: {len(comments_trim)} chars")
else:
    comments_trim = os.environ.get("COMMENTS", "No Comments")[:2000]
    print("::warning::jira_comments.txt not found, falling back to env var")

# ── Feature examples from target domain (up to 40 lines from 2 files) ────────
example_files = sorted(
    glob.glob(f"{feature_dir}/{domain_text}/*.feature")
)[:2]

feature_examples = ""
for ef in example_files:
    try:
        with open(ef, "r", encoding="utf-8") as f:
            lines = f.readlines()[:40]
            feature_examples += "".join(lines) + "\n"
    except Exception:
        pass

# ── Step context: target domain + general ────────────────────────────────────
steps = []
if os.path.exists("step_dictionary.txt"):
    with open("step_dictionary.txt", "r", encoding="utf-8") as f:
        content = f.read()
    blocks = re.split(r"\n--- Domain: (.*?) ---\n", content)
    for i in range(1, len(blocks), 2):
        d = blocks[i]
        s = blocks[i + 1]
        if d == domain_text or d == "general":
            steps.append(f"--- {d} steps ---\n{s.strip()}")

MAX_LINES_PER_DOMAIN = 120
capped_steps = []
for block in steps:
    block_lines = block.split("\n")
    if len(block_lines) > MAX_LINES_PER_DOMAIN:
        block_lines = block_lines[:MAX_LINES_PER_DOMAIN]
        print(f"::warning::Step block trimmed to {MAX_LINES_PER_DOMAIN} lines")
    capped_steps.append("\n".join(block_lines))
step_context = "\n\n".join(capped_steps)

if not step_context.strip():
    print(f"::warning::No steps found for domain '{domain_text}' — model will invent all steps")
else:
    print(f"Step context: {len(step_context.splitlines())} lines for domain '{domain_text}'")

# ── Scenario context: last 30 titles for this domain ─────────────────────────
scenario_context = ""
if os.path.exists("scenario_dictionary.txt"):
    with open("scenario_dictionary.txt", "r", encoding="utf-8") as f:
        lines = f.readlines()
    scenarios = [l.strip() for l in lines if l.startswith(f"[{domain_text}]")]
    scenario_context = "\n".join(scenarios[-30:])
    print(f"Scenario context: {len(scenarios)} titles, showing last 30")

# ── Build prompt ──────────────────────────────────────────────────────────────
prompt = f"""STEP 1 — READ THE TICKET
Read the JIRA ticket below completely before doing anything else.

JIRA:
ID: {jira_id}
TARGET DOMAIN: {domain_text}
SUMMARY: {summary_trim}
DESCRIPTION: {desc_trim}
COMMENTS: {comments_trim}

STEP 2 — EXTRACT (fill every field below before writing any Gherkin)

USER ROLES EXTRACTION — search the ticket for every user type mentioned or implied:
  - Look for words: user, admin, internal, external, permissioned, read-only, viewer, editor, owner
  - Look for phrases: "if permitted", "access level", "logged in as", "role"
  - If a role is implied by context (e.g. "admin panel" → admin user), include it
  - Write every role you found as a comma-separated list
  - If truly none found, write "default authenticated user" — never leave this blank

UI COMPONENTS EXTRACTION — list every UI element mentioned:
  - Look for: list, table, form, dropdown, modal, panel, button, column, filter, search, sort, page, tab
  - Write every component found as a comma-separated list, or "none" if absent

DATA OPERATIONS EXTRACTION — list every data action mentioned:
  - Look for: create, edit, update, delete, save, import, export, modify, clear, reset
  - Write every operation found, or "none" if absent

HISTORICAL RISKS EXTRACTION — list every related ticket or defect pattern mentioned:
  - Look for: ticket IDs (e.g. ET-XXXX, PROD-XXXX), words like "regression", "related to", "similar issue"
  - Write each as "TICKET-ID: risk description", or "none" if absent

AMBIGUITIES EXTRACTION — list every statement with two possible interpretations:
  - Look for: "should", "expected to", "may", "depending on", "based on", unclear scope
  - Write each ambiguity and both interpretations, or "none" if absent

PAGES/MODULES EXTRACTION — list every distinct page or feature area mentioned:
  - Write each area separately, or "none" if absent

STEP 3 — WRITE COVERAGE PLAN
Using your extractions above, fill in this exact block:

<coverage_plan>
- User roles identified: [paste your USER ROLES EXTRACTION result here]
- UI components identified: [paste your UI COMPONENTS EXTRACTION result here]
- Data operations identified: [paste your DATA OPERATIONS EXTRACTION result here]
- Historical risks identified: [paste your HISTORICAL RISKS EXTRACTION result here]
- Ambiguities to resolve: [paste your AMBIGUITIES EXTRACTION result here]
- Pages or modules to cover: [paste your PAGES/MODULES EXTRACTION result here]
- Scenario count planned: [exact number of scenarios you will write — commit to this number]
</coverage_plan>

STEP 4 — GENERATE SCENARIOS
Using the coverage_plan above, generate Gherkin scenarios.
Every single item listed in coverage_plan MUST produce at least one scenario.

HARD RULES:
1. Output ONLY raw valid Gherkin after </coverage_plan>. No markdown, no code fences, no explanations.
2. Every Scenario/Scenario Outline must have exactly one @todo tag on its own line above it.
3. NEVER place @todo on the same line as Scenario. NEVER emit two @todo tags before one Scenario.
4. You are STRICTLY FORBIDDEN from inventing new steps unless absolutely necessary. Every step MUST
   be selected from the STEP DICTIONARY if a functional equivalent exists.
   If you must invent a new step, mark it with a comment: # NEW STEP
5. Never use placeholder steps. Write the full meaningful step text always.
6. Never use angle bracket tokens like <NAME> unless they are Scenario Outline parameters
   defined in an Examples table.

DECISION RULES:
- Use plain Scenario by default.
- Use Scenario Outline + Examples ONLY when same steps apply to 2+ distinct data combinations
  AND Examples has 2+ rows.
- Use a data table when a step needs 3+ key-value pairs or a list.

DESCRIPTION INTERPRETATION RULES:
Style 1 - Explicit acceptance criteria: interpret directly and write scenarios.
Style 2 - Scope list (e.g. "FE: Dropdown, Brand Multi Select"): infer user-facing behaviour per item.
Style 3 - Status notes (e.g. "6/9 - RELEASED"): ignore entirely.
Style 4 - Mixed: process each section independently. UPDATED section is highest priority.

For ANY style:
- Every bullet, numbered item, or named feature must produce at least one scenario.
- Never skip a scope item. Infer behaviour from the name if ACs are missing.
- Use COMMENTS for edge cases and supplementary context.

SCENARIO GENERATION RULES:
1. For each scope item: write happy path + negative/validation (only if inferable).
2. For each USER ROLE listed in coverage_plan: write at least one scenario per role.
3. For each UI COMPONENT listed: cover happy path AND post-action state.
4. For each HISTORICAL RISK listed: write one scenario targeting that specific risk.
5. For each AMBIGUITY listed: write one scenario per interpretation.
6. For each PAGE/MODULE listed: write at least one scenario.
7. Steps order: Given / When / Then / And / But.
8. Business-readable language only. No technical details.
9. One behaviour per scenario. No duplicates.

REPOSITORY EXAMPLES (mirror this Gherkin style exactly):
{feature_examples}

EXISTING SCENARIOS (do not duplicate these):
{scenario_context}

STEP DICTIONARY (use verbatim — invent only if no match exists):
{step_context}

OUTPUT FORMAT — follow exactly:

PART 1: coverage_plan block (mandatory, comes first)
<coverage_plan>
- User roles identified: ...
- UI components identified: ...
- Data operations identified: ...
- Historical risks identified: ...
- Ambiguities to resolve: ...
- Pages or modules to cover: ...
- Scenario count planned: N
</coverage_plan>

PART 2: Gherkin (raw, immediately after </coverage_plan>)
Feature: <name from Jira summary>

  @todo
  Scenario: <actor action expected outcome>
    Given ...
    When  ...
    Then  ...

  @todo
  Scenario Outline: <actor action expected outcome>
    Given ...
    When  ... "<param>"
    Then  ...
    Examples:
      | param  |
      | value1 |
      | value2 |

FINAL ENFORCEMENT — verify before submitting output:
[ ] Every user role in coverage_plan has at least one scenario
[ ] Every UI component in coverage_plan has a happy path and post-action scenario
[ ] Every historical risk in coverage_plan has a dedicated scenario
[ ] Every ambiguity in coverage_plan has one scenario per interpretation
[ ] Every page/module in coverage_plan has at least one scenario
[ ] Total scenario count matches "Scenario count planned" in coverage_plan
[ ] No scenario duplicates existing scenarios listed above
[ ] Every step exists in STEP DICTIONARY or is marked # NEW STEP
If any checkbox would be unchecked, add the missing scenarios before outputting.
"""

with open("prompt.txt", "w", encoding="utf-8") as f:
    f.write(prompt)

size  = os.path.getsize("prompt.txt")
words = len(prompt.split())
print(f"Prompt size  : {size} bytes / {words} words (est. {words // 3} tokens)")
