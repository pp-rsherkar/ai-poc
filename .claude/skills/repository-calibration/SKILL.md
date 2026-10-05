---
name: repository-calibration
description: Calibrate Sutra test design against this repository's feature organization, step vocabulary, page objects, duplicate coverage, and append-versus-create conventions. Use before authoring or placing Gherkin.
---

# Repository Calibration

Inspect the checked-out repository before drafting Gherkin. Repository evidence overrides generic BDD phrasing.

## Search boundary

Search recursively across:

- `src/test/resources/features/`
- `src/test/java/stepdefinitions/`
- `src/main/java/pages/`
- relevant utilities under `src/main/java/utils/`

Do not search only the feature root; feature files live in module subdirectories. Search results are discovery evidence, not an instruction to read every matching directory or file.

## Calibration procedure

1. Identify the ticket's module and parent functional domain from Netra's analysis.
2. Search existing feature files recursively for the domain, requirement concepts, ticket key, and `# Source:` references.
3. Check the current branch and available local refs for an existing change covering the same ticket. Do not make network calls unless the selected delivery mode explicitly requires remote Git operations.
4. Compare the case's distinctive precondition, action, and expected result, not only ticket IDs or broad requirement themes. Classify overlap as:
   - `duplicate`: one existing scenario contains the distinctive condition and an exact observable assertion covering the expected result;
   - `partial`: author only uncovered requirements;
   - `new`: no matching behavior exists.
   Record duplicate evidence as the repository-relative feature path, exact scenario name, and exact `Then`/assertive `And`/`But` step. If any element cannot be resolved, the case is not a duplicate.
5. Choose an existing parent-domain feature file whenever one exists. Create a new file only when no parent-domain file is suitable.
6. New target paths must be `src/test/resources/features/<module>/<Domain>_<Module>.feature`. Never create a ticket-named feature or write directly under the feature root.
7. Read the complete selected target feature once. Preserve its `Feature:` header, description, `Background:`, tags, scenarios, and byte content. Reuse that read for target selection, phrasing, overlap, and append decisions; do not reopen the full target during authoring. Do not fully read other feature files unless no suitable target can be selected from search evidence.
8. Extract the target file's dominant step phrasing. If creating a file, sample at least eight search-matched existing steps per keyword across the module when available.
9. Inventory reusable `@Given`, `@When`, and `@Then` annotations through search. Read only the matching step-definition methods and the page-object methods they directly call. Expand to adjacent implementation files only when a concrete unresolved symbol or behavior requires it.

## Calibration result

Return for each ticket:

- target repository path;
- action: `append`, `create`, `duplicate`, or `blocked`;
- overlap evidence, including exact feature, scenario, and assertion anchors for duplicates;
- keyword phrasing profile;
- reusable step definitions;
- missing step definitions or page-object methods;
- framework readiness: `ready` or `gap`.

## Diff safety

When appending, create a staged copy and append after exactly two newline characters. Do not reformat or rewrite existing content. Before handoff, compare the staged copy with the source and confirm all pre-existing lines remain unchanged.

If existing content was deleted or modified, stop and restore the staged file before continuing. Do not hide the failure by producing a new file.

## Pre-existing format violations

A selected parent file that already fails the feature-format check is never a reason to create a new file. `mvn spotless:apply` does not cover `.feature` files.

1. Run `npm run feature:format -- <staged copy of the parent file>` on that one file only. Never run the formatter without a path, because it rewrites every feature file.
2. Treat the formatted copy as the unchanged original for the diff-safety comparison, then append after it.
3. Confirm the formatter changed whitespace only, and record the normalization in the handoff `assumptions`.
4. In pull-request delivery, commit the normalization separately before the appended scenarios.

Other open branches and pull requests do not change the append target. Treat every run as a real change.
