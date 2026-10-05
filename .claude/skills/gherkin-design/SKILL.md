---
name: gherkin-design
description: Convert Netra requirements and test cases into traceable, workflow-consolidated, repository-calibrated Gherkin and a structured test-design handoff. Use after repository and navigation calibration.
---

# Gherkin Design

Transform Netra's validated requirements and test cases into the fewest clear workflow scenarios that preserve complete test-case and requirement traceability.

## Input boundary

Use only the validated `analysis.json` plus repository-calibration and navigation-resolution results. Do not fetch Jira, Confluence, spreadsheets, or documents. Do not reinterpret ticket readiness already decided by Netra.

Preserve all Netra requirement IDs and test-case IDs exactly.

## Triage

Create one triage record for every Netra test case:

```text
testCaseId | requirementIds | automationCandidate | priority |
frameworkReadiness | disposition | rationale | scenarioIds
```

For `duplicate`, also provide:

```json
{
  "coveredBy": {
    "featurePath": "src/test/resources/features/<module>/<file>.feature",
    "scenarioName": "Exact existing scenario name",
    "assertionAnchor": "Exact existing Then/And/But assertion text"
  }
}
```

Allowed automation decisions:

- `yes`: deterministic UI/API behavior, validation, permissions, state changes, navigation, data display, filtering, pagination, accessibility, or user-visible failure handling;
- `no`: purely cosmetic checks, performance measurements, physical hardware, or an external dependency that cannot be controlled;
- `blocked`: Netra marks the test blocked or the expected behavior remains critically ambiguous;
- `duplicate`: repository calibration proves that an existing scenario preserves the case's distinctive condition and exact expected result. Theme-level or requirement-level similarity is not sufficient.

Every duplicate must resolve to one existing repository feature and one unambiguous scenario. Its `assertionAnchor` must exactly match an observable assertion in that scenario. Leave `scenarioIds` empty because no new scenario was authored. If the distinctive condition or result is absent, classify the case as `authored`; if equivalence cannot be determined, block it rather than guessing.

A framework gap does not change `yes` to `no`. Mark readiness `gap` and record the missing Java glue.

Prioritize by risk and requirement importance: `high`, `medium`, or `low`. Base the rationale on Netra evidence; do not invent severity.

## Scenario synthesis

- Parallel variations of the same action become one `Scenario Outline:` with literal `Examples:` rows.
- Treat Netra's `variationGroup` as a lossless contract: all cases in one group must map to the same authored `Scenario Outline`, and every `variationKey` must have exactly one `Examples` data row. Author or defer the whole group; never split it or mark only part of it duplicate.
- Sequential states where one expected result becomes the next precondition become one chained `Scenario:`.
- Preserve source-described state-changing business actions as `When`/`And` actions. Convert them to `Given` only when Netra explicitly identifies the state as pre-existing setup; consolidation must not remove independently testable actions or assertions.
- Never merge unrelated domains merely to reduce scenario count.
- Every contributing test-case assertion remains independently visible after consolidation.
- Every authored scenario records its source ticket, requirement IDs, and test-case IDs.
- Open unresolved gaps or ambiguities do not become asserted steps.
- Use concrete data from Netra. If a required literal value is missing, queue or block the case; do not use placeholders such as `foo` or `test_data`.
- Keep Netra cases with investigative outcomes or vague reproduction fixtures blocked. Never author `Then`/assertive `And`/`But` steps that instruct the executor to document, observe, investigate, confirm whether, determine if, or log a gap.

## Repository Gherkin conventions

- Use two-space indentation.
- Use only the `@todo` tag for new scenarios.
- Add `# Source: <IDs>` immediately above `@todo`.
- Start the first word after every Gherkin keyword with an uppercase letter.
- Match the calibrated target file's phrasing rather than generic textbook wording.
- Before creating a step, search the selected target feature and matching step definitions for an equivalent action or assertion. Reuse the existing wording and workflow sequence exactly when one exists; do not replace an established UI flow with an abstract `Given` step. Create a new step only when no equivalent repository step exists, and mark that missing capability as a framework gap.
- Each step performs or asserts one observable action or result.
- Do not put rationale, analysis, requirement prose, or unresolved questions inside steps.
- Use `<UPPERCASE_PARAMETERS>` only with `Examples:` and double quotes for literal values in ordinary steps.
- Place `Examples:` immediately after the last scenario step with no blank line.
- A feature file has at most one `Background:`. Never modify an existing one.
- Put a two-space-indented `# Framework Gap:` comment immediately above a concrete step that lacks Java support.

## Navigation

Use the resolved navigation path. In an existing file, emit only hops not already established by its `Background:`. For a new file, put common login or landing setup in `Background:` and page-specific hops in scenarios.

## Artifact-only delivery

Write complete staged files under `target/sutra/features/<module>/`. For append targets, copy the original file unchanged and append new content. If the original already fails the feature-format check, normalize that one copy first as `repository-calibration` describes, then append; never create a new file to avoid the failure. Record the intended repository path separately in `test-design-output.json`.

Do not edit `src/test/resources/features/` in artifact-only mode.

## Pull-request delivery

Use this mode only when the user explicitly requests it. Apply the already validated staged changes to their recorded target paths, run project formatting checks, review the diff, and then create the requested branch and pull request. Never merge it.

## Deterministic quality gate

After all staged features and `test-design-output.json` are complete, run one batched quality pass:

1. Invoke `scripts/align-feature-tables.py` once with every staged feature path.
2. Invoke the repository's feature formatter check once with the same paths.
3. Invoke the test-design validator once against the final formatted files.

Confirm append-only diff safety once after formatting. The validator confirms the traceability, duplicate-evidence, blocked-case, variation-group, source-tag, scenario, and Examples contracts. If a gate fails, correct only the reported issue and rerun that gate; do not repeat successful gates or read deterministic script source unless its error output is insufficient.

If the effort budget defers a whole ticket, set its applicable triage records to `queued`. Never split one ticket across runs or silently omit it.
