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

# ── Model capability tiers ─────────────────────────────────────────────────────
# Haiku is fast but has weaker multi-component reasoning.
# For tickets with two distinct functional components, it reliably misses the second.
# We inject an explicit component checklist into the prompt to compensate.
IS_HAIKU = "haiku" in MODEL.lower()
if IS_HAIKU:
    print("::warning::Haiku model selected. Component checklist injection enabled.")
    print("::warning::For tickets with 2 components or 30+ scenarios, consider claude-sonnet-4-6.")

# ── Model-aware context budgets ────────────────────────────────────────────────
# Official context windows from Anthropic docs (as of June 2026):
#   claude-haiku-4-5   → 200k tokens
#   claude-sonnet-4-6  → 1M tokens
#   claude-opus-4-6/7/8 → 1M tokens
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

CHARS_PER_TOKEN = 4
SAFETY_MARGIN   = 0.75   # use 75% of window to leave room for model response
FIXED_OVERHEAD  = 40_000 # chars reserved for: rules text + step dict + examples + scenario ctx

token_window = CONTEXT_WINDOWS.get(MODEL, 200_000)
if MODEL not in CONTEXT_WINDOWS:
    print(f"::warning::Unknown model '{MODEL}' — defaulting to 200k token budget (most restrictive)")

usable_chars    = int(token_window * CHARS_PER_TOKEN * SAFETY_MARGIN) - FIXED_OVERHEAD
DESC_BUDGET     = int(usable_chars * 0.80)
COMMENTS_BUDGET = int(usable_chars * 0.20)

print(f"Token window   : {token_window:,} tokens")
print(f"Usable budget  : {usable_chars:,} chars total")
print(f"Desc budget    : {DESC_BUDGET:,} chars")
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
        truncated = content[:budget].rsplit('\n\n', 1)[0]
        print(f"::warning::{label} is {len(content):,} chars — exceeds {budget:,} char budget for {MODEL}.")
        print(f"::warning::Truncated to {len(truncated):,} chars at paragraph boundary.")
        print(f"::warning::Consider using claude-sonnet-4-6 or an Opus model for full coverage.")
        return truncated

    print(f"{label}: fits within budget ✓  ({len(content):,} / {budget:,} chars)")
    return content

# ── Load Jira content ──────────────────────────────────────────────────────────
summary_raw   = os.environ.get("SUMMARY", "No Summary")[:300]
desc_full     = load_with_budget("jira_description.txt", "DESCRIPTION", "Description", DESC_BUDGET)
comments_full = load_with_budget("jira_comments.txt",    "COMMENTS",    "Comments",    COMMENTS_BUDGET)

# ── Extract component checklist from description ───────────────────────────────
# Scan the description for top-level numbered/bulleted sections and named components.
# This becomes a mandatory checklist injected into the prompt so the model cannot
# skip any component — critical for Haiku which loses track of later components.
def extract_components(description):
    """
    Heuristic: find lines that look like top-level component/section headings.
    Matches patterns like:
      - "Prescriptions Component:"
      - "Addressable NPI Geographic Performance:"
      - "1. Display Logic"
      - "## Summary Metrics"
    Returns a list of component name strings.
    """
    components = []
    patterns = [
        r'^#+\s+(.+)',                          # Markdown headings ## Foo
        r'^\d+\.\s+([A-Z][^:.\n]{5,60}):?',    # Numbered: "1. Display Logic"
        r'^[*\-]\s+\*\*([^*]{5,60})\*\*',      # Bold bullet: **Component Name**
        r'^([A-Z][A-Za-z ]{5,60})(?:\s*Component)?:', # "Prescriptions Component:"
    ]
    for line in description.split('\n'):
        line = line.strip()
        for pat in patterns:
            m = re.match(pat, line)
            if m:
                name = m.group(1).strip().rstrip(':')
                if len(name) > 5 and name not in components:
                    components.append(name)
                break
    return components

components = extract_components(desc_full)
if components:
    print(f"Components detected: {components}")
else:
    print("No distinct components detected — single-component ticket assumed")

# Build the checklist block injected into the prompt
if components:
    checklist_lines = "\n".join(f"  - [ ] {c}" for c in components)
    component_checklist = f"""
MANDATORY COMPONENT CHECKLIST — YOU MUST GENERATE SCENARIOS FOR EVERY ITEM BELOW.
This list was automatically extracted from the ticket description.
DO NOT output anything until you have written at least one scenario for each item.
Tick each off mentally before finalising output:

{checklist_lines}

If ANY item above has zero scenarios written for it, you are in HARD RULE VIOLATION.
Add the missing scenarios before outputting.
"""
else:
    component_checklist = ""

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
4. You are STRICTLY FORBIDDEN from inventing new steps unless absolutely necessary. Every step
   MUST be selected from the STEP DICTIONARY if a functional equivalent exists.
   If you must invent a new step, mark it with a comment: # NEW STEP
5. Never use placeholder steps. Write the full meaningful step text always.
6. Never use angle bracket tokens like <NAME> unless they are Scenario Outline parameters
   defined in an Examples table.
7. PERMISSION BOUNDARY RULE — Phrases like "if permitted", "if applicable", "where applicable",
   "if allowed", "if access is granted" attached to ANY user role are NEVER exclusions.
   They are PERMISSION BOUNDARY signals. You MUST write TWO scenarios for each such phrase:
     a. The user HAS permission → verify the expected behaviour occurs correctly.
     b. The user DOES NOT have permission → verify they are blocked, redirected, or shown
        an appropriate error.
   Skipping or merging these into one scenario is a HARD RULE VIOLATION.
8. FEATURE FILE SPLIT RULE — MAXIMUM 2 Feature blocks per ticket. Hard limit, no exceptions.
   - ONE Feature block: default for all tickets. Use unless the ticket explicitly names two
     completely separate UI components with no shared steps.
   - TWO Feature blocks: only when the ticket is explicitly divided into exactly two named
     components that are functionally independent. Sub-sections, edge cases, label changes,
     regression notes, and permission checks are NOT separate components — fold them into the
     single Feature block using tags (e.g. @edge_case, @regression, @permissions).
   - NEVER produce 3 or more Feature blocks. If in doubt, use ONE.
   - Each Feature block must have its own "Feature: <descriptive name>" line.
   - The pipeline splits Feature blocks into separate .feature files in the PR.
{component_checklist}
DECISION RULES — SCENARIO OUTLINE AND DATA TABLE ARE MANDATORY IN THESE CASES:
You MUST use Scenario Outline + Examples (not plain Scenario) when ANY of these are true:
  a. The same behaviour is verified for 2 or more distinct input values, metric types,
     colour states, tab names, column names, or data combinations.
     EXAMPLE: Rx Index colour logic (Green >1, Grey <=1, Grey =1.00) →
     ONE Scenario Outline, NOT three separate Scenarios.
  b. The same formula/calculation is tested with multiple sets of input/output numbers.
     Collapse into ONE Scenario Outline with numeric columns in Examples.
  c. The same UI column, tab, or metric is tested across multiple named items →
     ONE Scenario Outline with metric name as an Examples column.

You MUST use a data table when a step verifies 3 or more key-value pairs or column names
in a single assertion.

Plain Scenario is ONLY correct when the scenario is truly unique and cannot share steps
with any other scenario even after parameterisation. Before writing a plain Scenario, ask:
"Is there another scenario with identical step structure and only different data?" If yes → Outline.

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

COVERAGE COMPLETENESS RULES:
After writing your initial scenarios, perform a gap check:

A. USER ROLE COVERAGE
   - Every mentioned user type must have at least one scenario.
   - HARD RULE 7: Any role with conditional language ("if permitted") needs TWO scenarios.

B. UI INTERACTION COVERAGE
   - For any UI component (list, table, form, dropdown, modal, panel, search bar):
     * Happy path interaction
     * Interaction after a data-changing action (save, edit, delete, submit)
     * Interaction with another active UI state (filter/sort/search active)
   - For sorting: default order, sort by each mentioned column, sort after save/edit.
   - For tabs: each tab loads correctly, tab switching, rapid tab switching if mentioned.

C. DATA STATE COVERAGE
   - For any data-changing operation: empty state, single record, multiple records.

D. SYSTEM BEHAVIOR COVERAGE
   - For any triggered system response: success/error feedback, state after refresh/navigation.

E. HISTORICAL RISK COVERAGE
   - Every related ticket, known defect, or regression area mentioned → at least one scenario.

F. AMBIGUITY RESOLUTION
   - Two interpretations of any ambiguous requirement → one scenario per interpretation.

G. CROSS-COMPONENT COVERAGE
   - Every module/page/area explicitly mentioned → at least one scenario.

GAP CHECK — BEFORE OUTPUTTING, VERIFY:
- Every item in the MANDATORY COMPONENT CHECKLIST above has at least one scenario.
- Every user role has coverage. Every conditional role has TWO scenarios (HARD RULE 7).
- Every UI component: happy path + post-action state.
- Every mentioned tab has a loading scenario and a tab-switching scenario.
- Every pop-up/modal: open, content, and empty state.
- Every sortable column has a sort scenario.
- Every historical risk ticket has a regression scenario.
- Every formula edge case (zero divisor, zero numerator, boundary values) is covered.
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

FINAL REMINDER BEFORE YOU OUTPUT:
- Count your Feature blocks. MAXIMUM IS 2.
- Go through the MANDATORY COMPONENT CHECKLIST above one more time.
  Every item must have at least one scenario. If any is missing, write it now.
- Scan every group of plain Scenarios: collapse any with identical structure into ONE Outline.
- Every Scenario/Outline must be fully complete with at least one Given/When/Then.
- If running low on output space, complete the current Scenario cleanly and stop.
  Do NOT start a Scenario you cannot finish.
"""

with open("prompt.txt", "w", encoding="utf-8") as f:
    f.write(prompt)

size        = os.path.getsize("prompt.txt")
input_toks  = size // CHARS_PER_TOKEN
output_toks = MAX_OUTPUT_TOKENS.get(MODEL, 64_000)
print(f"Prompt size   : {size:,} bytes (~{input_toks:,} input tokens)")
print(f"Window used   : {input_toks / token_window * 100:.1f}% of {MODEL} context window")
print(f"Max output    : {output_toks:,} tokens for {MODEL}")
print(f"Components    : {len(components)} detected — checklist {'injected' if components else 'skipped'}")
