"""
write_prompt.py  — model-aware, truncation-safe version
Usage: python3 write_prompt.py <jira_id> <domain>

Reads env vars : SUMMARY, LLM_MODEL
Reads files    : jira_description.txt, jira_comments.txt,
                 step_dictionary.txt, scenario_dictionary.txt
Writes         : prompt.txt
"""
import os
import re
import sys
import glob

# ── Args ───────────────────────────────────────────────────────────────────────
if len(sys.argv) < 3:
    sys.exit("Usage: write_prompt.py <jira_id> <domain>")

jira_id     = sys.argv[1]
domain      = sys.argv[2]
domain_text = domain or "general"
feature_dir = os.environ.get("FEATURE_DIR", "src/test/resources/features")
MODEL       = os.environ.get("LLM_MODEL", "claude-sonnet-4-6")

print(f"Model         : {MODEL}")

# ── Model-aware context budgets ────────────────────────────────────────────────
# Official context windows from Anthropic docs (as of June 2026):
#   claude-haiku-4-5   → 200k tokens
#   claude-sonnet-4-6  → 1M tokens
#   claude-opus-4-6    → 1M tokens
#   claude-opus-4-7    → 1M tokens
#   claude-opus-4-8    → 1M tokens
#
# We apply a 25% safety margin, then subtract fixed overhead for prompt
# boilerplate, step dictionary, scenario context, and feature examples.
# 1 token ≈ 4 chars (conservative for English + Gherkin mixed content).

CONTEXT_WINDOWS = {
    "claude-haiku-4-5":  200_000,
    "claude-sonnet-4-6": 1_000_000,
    "claude-opus-4-6":   1_000_000,
    "claude-opus-4-7":   1_000_000,
    "claude-opus-4-8":   1_000_000,
}

# Max output tokens per model (official Anthropic limits, June 2026)
# Used only for logging — the workflow shell step sets max_tokens on the API call.
MAX_OUTPUT_TOKENS = {
    "claude-haiku-4-5":  64_000,
    "claude-sonnet-4-6": 64_000,
    "claude-opus-4-6":   128_000,
    "claude-opus-4-7":   128_000,
    "claude-opus-4-8":   128_000,
}

CHARS_PER_TOKEN   = 4
SAFETY_MARGIN     = 0.75   # use 75% of window to leave room for model response
FIXED_OVERHEAD    = 40_000 # chars reserved for: rules text + step dict + examples + scenario ctx

token_window      = CONTEXT_WINDOWS.get(MODEL, 200_000)  # default to most restrictive if unknown
if MODEL not in CONTEXT_WINDOWS:
    print(f"::warning::Unknown model '{MODEL}' — defaulting to 200k token budget (most restrictive)")

usable_chars      = int(token_window * CHARS_PER_TOKEN * SAFETY_MARGIN) - FIXED_OVERHEAD
DESC_BUDGET       = int(usable_chars * 0.80)   # 80% of jira budget for description
COMMENTS_BUDGET   = int(usable_chars * 0.20)   # 20% of jira budget for comments

print(f"Token window  : {token_window:,} tokens")
print(f"Usable budget : {usable_chars:,} chars total")
print(f"Desc budget   : {DESC_BUDGET:,} chars")
print(f"Comments budget: {COMMENTS_BUDGET:,} chars")

# ── Safe loader — warns loudly, never silently truncates ───────────────────────
def load_with_budget(path, fallback_env, label, budget):
    if os.path.exists(path):
        with open(path, "r", encoding="utf-8") as f:
            content = f.read().strip()
        print(f"{label}: {len(content):,} chars loaded from {path}")
    else:
        content = os.environ.get(fallback_env, "").strip()
        print(f"::warning::{path} not found — falling back to env var ({len(content):,} chars)")

    if not content:
        print(f"::error::{label} is empty — Jira parse step may have failed")
        sys.exit(1)

    if len(content) > budget:
        # Cut at paragraph boundary, not mid-sentence
        truncated = content[:budget].rsplit('\n\n', 1)[0]
        print(f"::warning::{label} is {len(content):,} chars — exceeds {budget:,} char budget for {MODEL}.")
        print(f"::warning::This ticket is unusually large. Truncated to {len(truncated):,} chars at paragraph boundary.")
        print(f"::warning::Consider using claude-sonnet-4-6 or an Opus model for full coverage.")
        return truncated

    print(f"{label}: fits within budget ✓  ({len(content):,} / {budget:,} chars)")
    return content

# ── Load Jira content ──────────────────────────────────────────────────────────
summary_raw   = os.environ.get("SUMMARY", "No Summary")[:300]  # summary is always short
desc_full     = load_with_budget("jira_description.txt", "DESCRIPTION", "Description", DESC_BUDGET)
comments_full = load_with_budget("jira_comments.txt",    "COMMENTS",    "Comments",    COMMENTS_BUDGET)

# ── Feature examples from target domain (up to 40 lines from 2 files) ─────────
example_files = sorted(glob.glob(f"{feature_dir}/{domain_text}/*.feature"))[:2]
feature_examples = ""
for ef in example_files:
    try:
        with open(ef, "r", encoding="utf-8") as f:
            feature_examples += "".join(f.readlines()[:40]) + "\n"
    except Exception:
        pass

# ── Step context: target domain + general ─────────────────────────────────────
steps = []
if os.path.exists("step_dictionary.txt"):
    with open("step_dictionary.txt", "r", encoding="utf-8") as f:
        content = f.read()
    blocks = re.split(r"\n--- Domain: (.*?) ---\n", content)
    for i in range(1, len(blocks), 2):
        d = blocks[i]
        s = blocks[i + 1]
        if d in (domain_text, "general"):
            steps.append(f"--- {d} steps ---\n{s.strip()}")

MAX_LINES_PER_DOMAIN = 120
capped_steps = []
for block in steps:
    lines = block.split("\n")
    if len(lines) > MAX_LINES_PER_DOMAIN:
        lines = lines[:MAX_LINES_PER_DOMAIN]
        print(f"::warning::Step block trimmed to {MAX_LINES_PER_DOMAIN} lines")
    capped_steps.append("\n".join(lines))
step_context = "\n\n".join(capped_steps) or "(none — model must invent all steps)"

if step_context.strip() == "(none — model must invent all steps)":
    print(f"::warning::No steps found for domain '{domain_text}' — model will invent all steps")
else:
    print(f"Step context  : {len(step_context.splitlines())} lines for domain '{domain_text}'")

# ── Scenario context: last 30 titles for this domain ──────────────────────────
scenario_context = ""
if os.path.exists("scenario_dictionary.txt"):
    with open("scenario_dictionary.txt", "r", encoding="utf-8") as f:
        lines = f.readlines()
    domain_scenarios = [l.strip() for l in lines if l.startswith(f"[{domain_text}]")]
    scenario_context = "\n".join(domain_scenarios[-30:])
    print(f"Scenario ctx  : {len(domain_scenarios)} titles, showing last 30")

# ── Build prompt ───────────────────────────────────────────────────────────────
prompt = f"""HARD RULES:
1. Return ONLY raw valid Gherkin. No markdown, no code fences, no explanations.
2. Every Scenario/Scenario Outline must have exactly one @todo tag on its own line above it.
3. NEVER place @todo on the same line as Scenario. NEVER emit two @todo tags before one Scenario.
4. You are STRICTLY FORBIDDEN from inventing new steps unless absolutely necessary. Every step MUST be selected from the STEP DICTIONARY if a functional equivalent exists. If you must invent a new step, mark it with a comment: # NEW STEP
5. Never use placeholder steps. Write the full meaningful step text always.
6. Never use angle bracket tokens like <NAME> unless they are Scenario Outline parameters defined in an Examples table.
7. PERMISSION BOUNDARY RULE — Phrases like "if permitted", "if applicable", "where applicable",
   "if allowed", "if access is granted" attached to ANY user role are NEVER exclusions.
   They are PERMISSION BOUNDARY signals. You MUST write TWO scenarios for each such phrase:
     a. The user HAS permission → verify the expected behaviour occurs correctly.
     b. The user DOES NOT have permission → verify they are blocked, redirected, or shown
        an appropriate error.
   Skipping or merging these into one scenario is a HARD RULE VIOLATION.
   The word "if" defines the test condition — it does not make the scenario optional.
8. FEATURE FILE SPLIT RULE — MAXIMUM 2 Feature blocks per ticket. Hard limit, no exceptions.
   - ONE Feature block: default for all tickets. Use unless the ticket explicitly names two
     completely separate UI components with no shared steps (e.g. a standalone Prescriptions
     panel AND a completely separate Geographic Performance panel described as distinct sections).
   - TWO Feature blocks: only when the ticket is explicitly divided into exactly two named
     components that are functionally independent. Sub-sections, edge cases, label changes,
     regression notes, and permission checks are NOT separate components — fold them into the
     single Feature block using tags (e.g. @edge_case, @regression, @permissions).
   - NEVER produce 3 or more Feature blocks. If in doubt, use ONE.
   - Each Feature block must have its own "Feature: <descriptive name>" line.
   - The pipeline splits Feature blocks into separate .feature files in the PR.

DECISION RULES — SCENARIO OUTLINE AND DATA TABLE ARE MANDATORY IN THESE CASES:
You MUST use Scenario Outline + Examples (not plain Scenario) when ANY of these are true:
  a. The same behaviour is being verified for 2 or more distinct input values, metric types,
     colour states, tab names, column names, or data combinations.
     EXAMPLE: Rx Index colour logic (Green when >1, Grey when <=1, Grey when =1.00) ->
     ONE Scenario Outline with an Examples table, NOT three separate Scenarios.
  b. The same formula or calculation is tested with multiple sets of input/output numbers
     (e.g. Coverage Rate at 0%, 75%, 100%; Avg TRx with different divisor/dividend pairs).
     Collapse into ONE Scenario Outline with numeric columns in Examples.
  c. The same UI column, tab, or metric is tested across multiple named items
     (e.g. NRx row AND NBRx row with same column structure) ->
     ONE Scenario Outline with metric name as an Examples column.

You MUST use a data table when a step verifies 3 or more key-value pairs or column names
in a single assertion (e.g. verifying all columns in a table, all rows in a result set).

Plain Scenario is ONLY correct when the scenario is truly unique and cannot share steps
with any other scenario even after parameterisation. Before writing a plain Scenario, ask:
"Is there another scenario with identical step structure and only different data?" If yes -> Outline.

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
   - HARD RULE 7 APPLIES: Any role qualified with "if permitted", "if applicable", or similar
     conditional language requires TWO scenarios (permitted state + blocked state).
     Do not skip. Do not merge into one.

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
- For every role with conditional language ("if permitted"), have I written both the
  permitted AND blocked scenario? (HARD RULE 7)
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
SUMMARY: {summary_raw}
DESCRIPTION:
{desc_full}

COMMENTS:
{comments_full}

REPOSITORY EXAMPLES (mirror this style exactly):
{feature_examples}

EXISTING SCENARIOS (do not duplicate these):
{scenario_context}

STEP DICTIONARY (use verbatim - invent only if no match exists):
{step_context}

OUTPUT FORMAT:
Feature: <descriptive name — max 2 Feature blocks per ticket>

  @todo
  Scenario: <use ONLY when steps are unique and cannot be parameterised>
    Given ...
    When  ...
    Then  ...

  @todo
  Scenario Outline: <PREFERRED — collapse any 2+ scenarios sharing step structure into ONE Outline>
    Given ...
    When  User views the "<metric>" metric
    Then  "<metric>" displays "<expected_value>"
    And   Color is "<color>"
    Examples:
      | metric        | input_a | input_b | expected_value | color |
      | Coverage Rate | 75      | 100     | 75%            | n/a   |
      | Coverage Rate | 0       | 100     | 0%             | n/a   |
      | Rx Index      | 40%     | 20%     | 2.00           | Green |
      | Rx Index      | 15%     | 30%     | 0.50           | Grey  |
      | Rx Index      | 25%     | 25%     | 1.00           | Grey  |

  @todo
  Scenario: <use a data table when a step must verify 3+ key-value pairs in one assertion>
    Given ...
    When  User views the Prescriptions table
    Then  the table displays the following columns in order:
      | Column             |
      | Metric             |
      | Exposed Rx Share   |
      | Unexposed Rx Share |
      | Rx Index           |

If the ticket covers exactly two distinct components, add a second Feature block:

Feature: <descriptive name for second component>

  @todo
  Scenario Outline: ...

REMINDER BEFORE YOU OUTPUT:
- Count your Feature blocks. MAXIMUM IS 2. If you have 3 or more, merge extras into the nearest Feature.
- Scan every group of plain Scenarios: if two or more share identical step structure with different data values, collapse them into ONE Scenario Outline immediately.
- Is every Scenario/Outline fully complete with at least one Given/When/Then?
- If running low on output space, complete the current Scenario cleanly and stop. Do NOT start one you cannot finish.
"""

with open("prompt.txt", "w", encoding="utf-8") as f:
    f.write(prompt)

size        = os.path.getsize("prompt.txt")
input_toks  = size // CHARS_PER_TOKEN
output_toks = MAX_OUTPUT_TOKENS.get(MODEL, 64_000)
print(f"Prompt size   : {size:,} bytes (~{input_toks:,} input tokens)")
print(f"Window used   : {input_toks / token_window * 100:.1f}% of {MODEL} context window")
print(f"Max output    : {output_toks:,} tokens for {MODEL}")
