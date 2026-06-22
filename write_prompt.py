"""
write_prompt.py
Usage: python3 write_prompt.py <jira_id> <domain>

Reads env vars: SUMMARY, DESCRIPTION, COMMENTS, FEATURE_DIR
Reads files:    step_dictionary.txt, scenario_dictionary.txt
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

# ── Pull Jira content from env ────────────────────────────────────────────────
summary_trim  = os.environ.get("SUMMARY",      "No Summary")[:300]
desc_trim     = os.environ.get("DESCRIPTION",  "No Description")[:8000]
comments_trim = os.environ.get("COMMENTS",     "No Comments")[:2000]

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
prompt = f"""HARD RULES:
1. Return ONLY raw valid Gherkin. No markdown, no code fences, no explanations.
2. Every Scenario/Scenario Outline must have exactly one @todo tag on its own line above it.
3. NEVER place @todo on the same line as Scenario. NEVER emit two @todo tags before one Scenario.
4. You are STRICTLY FORBIDDEN from inventing new steps unless absolutely necessary. Every step MUST be selected from the STEP DICTIONARY if a functional equivalent exists. If you must invent a new step, mark it with a comment: # NEW STEP
5. Never use placeholder steps. Write the full meaningful step text always.
6. Never use angle bracket tokens like <NAME> unless they are Scenario Outline parameters defined in an Examples table.

DECISION RULES:
- Use plain Scenario by default.
- Use Scenario Outline + Examples ONLY when same steps apply to 2+ distinct data combinations AND Examples has 2+ rows.
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

SCENARIO GENERATION:
1. Read full DESCRIPTION and COMMENTS.
2. Identify every functional scope item.
3. For each item write scenarios covering happy path and negative/validation (only if inferable).
4. Steps: Given / When / Then / And / But order.
5. Business-readable language only. No technical details.
6. One behaviour per scenario. No duplicates.

COVERAGE COMPLETENESS RULES (apply after generating initial scenarios):
After writing your initial scenarios, perform a coverage gap check against these dimensions:

A. USER ROLE COVERAGE
   - If the ticket mentions multiple user types (internal, external, admin, read-only, permissioned),
     each must have at least one scenario.
   - Never assume only one user type unless explicitly stated.
   - If roles are implied but not named, infer from context (e.g. "admin panel" implies admin role).

B. UI INTERACTION COVERAGE
   - For any UI component mentioned (list, table, form, dropdown, modal, panel, search bar):
     * Happy path interaction
     * Interaction after a data-changing action (save, edit, delete, submit)
     * Interaction with another active UI state (filter applied, sort active, search term present)
   - For sorting/ordering specifically:
     * Default sort order
     * Manual sort by each mentioned column
     * Sort behavior after save/edit
   - For forms specifically:
     * Valid submission
     * Invalid/missing required fields
     * Boundary values (min/max length, special characters)

C. DATA STATE COVERAGE
   - For any operation that changes data (create, edit, delete, import, export):
     * Empty state (no records exist)
     * Single record
     * Multiple records / paginated list
     * Record with optional fields left empty
     * Rapid/successive operations (if implied by the ticket)

D. SYSTEM BEHAVIOR COVERAGE
   - For any action that triggers a system response:
     * Immediate feedback (success/error message)
     * State after page refresh (cache/persistence behavior)
     * State after navigation away and back
   - For any background process (sync, sort, filter, search):
     * Expected output when process completes correctly
     * Expected output when input is edge-case (null, empty, special chars)

E. HISTORICAL RISK COVERAGE
   - If COMMENTS or DESCRIPTION reference related tickets, known defects, or regression areas,
     write at least one scenario targeting each risk area called out.
   - Pay special attention to phrases like "regression", "also affects", "related to", "similar issue".

F. AMBIGUITY RESOLUTION
   - If the description contains ambiguous behavior or two possible interpretations,
     write one scenario for EACH interpretation, clearly named to distinguish them.
   - Look for words like "should", "expected to", "may", "depending on" as ambiguity signals.

G. CROSS-COMPONENT COVERAGE
   - If the ticket mentions that a fix/feature affects multiple areas, pages, or modules,
     write at least one scenario per affected area.
   - Do not write one generic scenario and assume it covers all areas.

GAP CHECK INSTRUCTION:
Before finalizing output, mentally verify:
- Have I covered every user role mentioned or implied?
- Have I covered every UI component mentioned (happy path + post-action state)?
- Have I covered empty, single, and multi-record data states where relevant?
- Have I covered system behavior after save/refresh/navigation?
- Have I addressed every historical risk or related ticket mentioned in COMMENTS?
- Have I written scenarios for both interpretations of any ambiguous requirement?
- Have I covered every module/page/area explicitly mentioned in the ticket?
If any answer is NO, add the missing scenarios before outputting.

JIRA:
ID: {jira_id}
TARGET DOMAIN: {domain_text}
SUMMARY: {summary_trim}
DESCRIPTION: {desc_trim}
COMMENTS: {comments_trim}

REPOSITORY EXAMPLES (mirror this style exactly):
{feature_examples}

EXISTING SCENARIOS (do not duplicate these):
{scenario_context}

STEP DICTIONARY (use verbatim - invent only if no match exists):
{step_context}

OUTPUT FORMAT:
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
"""

with open("prompt.txt", "w", encoding="utf-8") as f:
    f.write(prompt)

size  = os.path.getsize("prompt.txt")
words = len(prompt.split())
print(f"Prompt size  : {size} bytes / {words} words (est. {words // 3} tokens)")
