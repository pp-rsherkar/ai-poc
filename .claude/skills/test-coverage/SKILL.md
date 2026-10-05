---
name: test-coverage
description: Convert source-backed functional requirements into traceable QA coverage and detailed test cases. Use when Netra or another QA workflow needs positive, negative, boundary, permission, integration, or regression coverage without cosmetic UI cases.
---

# Functional Test Coverage

Generate test coverage from the complete supplied context. Formal acceptance criteria are useful but not required.

## Requirement inventory

Create one requirement for each discrete functional behavior found in the ticket, comments, parent or epic, linked issues, subtasks, documentation, implementation notes, or named production regression.

A requirement may cover:

- Business or calculation rules.
- Validation, formats, limits, and boundaries.
- Persistence, transformation, and returned data.
- Permissions and role behavior.
- States and transitions.
- Errors and rejected behavior.
- API or cross-system contracts.

Assign stable IDs as `<TICKET>-R01`, `<TICKET>-R02`, and so on. Attach source references and a confidence level to every requirement.

Exclude cosmetic layout, spacing, color, typography, animation, and copy checks. A UI control belongs only when its state demonstrates a functional rule.

## Coverage calculation

For every requirement, create at least one positive and one negative case. Add a distinct case for each applicable dimension not already covered:

- Secondary happy path.
- Input validation.
- Business-rule or calculation correctness.
- Data integrity or persistence.
- Error state.
- Permission or role.
- State transition.
- Boundary or limit.
- Multi-value or batch behavior.
- Integration or cross-system behavior.
- Relevant production regression.
- Explicit out-of-scope guard.

Use this floor, not a target:

```text
minimumTestCases = requirementCount * 2
                 + uncovered applicable dimensions
                 + regression anchors
```

Do not add redundant cases solely to increase the count.

When two or more cases are parallel, order-independent variations of the same action and assertion shape, assign all of them the same stable `variationGroup` and give each a unique, descriptive `variationKey`. Use both fields together. Do not group sequential state transitions or cases whose expected assertions differ structurally.

## Distinct-risk coverage

Coverage is based on distinct assertions, not parity with a previous test-case count. Before adding a case, confirm that it protects a separate failure mode that is not already asserted by another case.

When supported by the supplied evidence, consider these relationship and state dimensions explicitly:

- Ownership perspective: owner, affected recipient, unaffected recipient, and a user who can switch between account contexts.
- Scoped state reversal: restoring access for one account must not incorrectly clear another account's warning or state.
- Fan-out propagation: the same record must behave consistently in every group, list, or container that references it.
- Operational continuity: retained configuration must also preserve the expected runtime or delivery behavior for unaffected records.
- Discoverability: retained or warned records must remain available through applicable search, filtering, counts, and list views.
- Identity specificity: when the source requires multiple identifiers, such as name and ID, assert each required identifier rather than substituting one for another.

Do not generate a separate case when the assertion is already independently visible in another case. If a dimension would be valuable but its expected behavior is not supported by a source, record a gap instead of inventing the result.

When compatibility with an earlier test inventory is being evaluated, map each earlier assertion to current coverage. Preserve distinct risks and document why any unmatched assertion is intentionally excluded; never add cases merely to reproduce the earlier count.

## Test-case rules

- ID: `TC_<TICKET>_NN`, zero-padded.
- Type: `positive`, `negative`, `edge`, `regression`, or `blocked`.
- Requirement IDs: list every requirement exercised by the case.
- Description: state the behavior and expected decision point without implementation details.
- Test data: provide concrete roles, values, formats, records, permissions, or system states.
- Expected result: state a verifiable functional outcome such as persisted data, computed value, state transition, API value, created or rejected record, or exact enforcing validation.
- Actual result and owner: leave empty before execution.
- Status: `not_executed` unless the case is genuinely blocked.
- Comments: cite the requirement source and any regression or dependency anchor.
- Variation identity: use `variationGroup` and `variationKey` for source-backed combination matrices so downstream design cannot collapse or lose an Examples row.

Use resolved navigation context only to identify the real module or surface. Do not invent a click path.

Expected results must be decidable as pass or fail before execution. Do not use investigative instructions such as “document whether,” “observe,” “confirm whether,” “determine if,” or “log a gap” as outcomes. When the source does not establish the expected behavior, create a blocked case and identify the exact missing fact instead of turning the question into a test.

Test data must state a reproducible fixture, role, record state, value, history, or setup. Phrases such as “configured to reproduce <defect>” and “configured with known traffic” are not concrete test data. Replace them with the source-backed configuration and measurable state; if the source does not provide those details, block the case and name the missing fixture information.

## Blocked coverage

Use one `blocked` case only when no testable behavior can be derived from any supplied source. Set both `type` and `status` to `blocked`, and state the exact missing information in its comments. Do not use blocked status merely because acceptance criteria are absent.

Before returning coverage, verify that every requirement has positive and negative coverage, every regression anchor has a case, IDs are unique, every source-required identifier is asserted, every variation group has at least two uniquely keyed cases, and the produced count meets the calculated floor.
