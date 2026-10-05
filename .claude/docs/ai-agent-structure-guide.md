# QA AI Agent Structure Guide

## Table of contents

- [The change in one minute](#the-change-in-one-minute)
- [Why the folders exist](#why-the-folders-exist)
- [What belongs inside skills and contracts](#what-belongs-inside-skills-and-contracts)
- [How one request moves through the framework](#how-one-request-moves-through-the-framework)
- [Where to change current Netra and Sutra behavior](#where-to-change-current-netra-and-sutra-behavior)
- [Where should new logic go?](#where-should-new-logic-go)
- [How to add a stage correctly](#how-to-add-a-stage-correctly)
- [Why this design is useful beyond PR #337](#why-this-design-is-useful-beyond-pr-337)
- [Practical checks before opening a PR](#practical-checks-before-opening-a-pr)
- [Current state](#current-state)

## The change in one minute

PR #337 introduces a safe way to build a multi-stage AI workflow inside the existing QA automation repository.

The main idea is simple:

> **An agent owns a job. A skill explains how to do part of that job. A contract defines the handoff. A script verifies repeatable rules. A workflow runs the job safely in CI.**

The current implemented flow is:

```text
QA request
   |
   v
Orchestrator  -- chooses the required stage and checks handoffs
   |
   +--> Netra -- reads Jira and produces validated QA analysis
   |       |
   |       `--> analysis.json + optional DOCX/XLSX/HTML
   |
   `--> Sutra -- consumes analysis.json and designs Gherkin
           |
           `--> test-design-output.json + staged .feature files

Stop: automation generation is not implemented in this change.
```

This is more than a fix for one ticket. It establishes a repeatable pattern for adding future stages without creating one large agent with broad access and unclear responsibilities.

## Why the folders exist

This structure uses Claude Code's standard project locations so the framework is automatically available when a
developer opens the repository locally and starts Claude Code from the repository root. Claude Code discovers project
agents from `.claude/agents/`, reusable skills from `.claude/skills/`, project instructions from `CLAUDE.md`, and shared
MCP server declarations from `.mcp.json`. Developers do not need to copy these definitions into a personal Claude
configuration.

The `contracts/`, `docs/`, and `reviews/` directories are repository conventions used by this framework; they are not
Claude Code discovery locations. Agents and skills reference those files when they need schemas, guidance, or review
evidence.

Official references:

- [Explore the `.claude` directory](https://code.claude.com/docs/en/claude-directory)
- [Create custom subagents](https://code.claude.com/docs/en/sub-agents)
- [Extend Claude with skills](https://code.claude.com/docs/en/skills)

```text
.claude/
├── agents/                 WHO performs each independent job
│   ├── orchestrator.md     Coordinates stages; does no domain work
│   ├── netra.md            Owns Jira-backed QA analysis
│   └── sutra.md            Owns Gherkin test design
├── skills/                 HOW reusable work is performed
│   ├── jira-analysis/
│   ├── risk-assessment/
│   ├── test-coverage/
│   ├── repository-calibration/
│   ├── navigation-resolution/
│   └── gherkin-design/
├── contracts/              EXACT DATA passed between stages
│   ├── analysis-output.schema.json
│   └── test-design-output.schema.json
├── docs/                   HUMAN-FRIENDLY architecture guidance
└── reviews/                TRACEABLE agent/skill review records

.github/workflows/          WHEN and under what CI permissions agents run
.mcp.json                   WHICH shared external connectors are declared
target/                     GENERATED output; never commit it
```

The existing `ai-skills/` folder is the legacy framework. It is migrated incrementally. New or refactored Claude definitions use `.claude/` so the two structures are not mixed.

## What belongs inside skills and contracts

| Location | Put this here | Why |
|---|---|---|
| `skills/<name>/SKILL.md` | The reusable procedure: when it applies, required inputs, decisions, steps, output, and failure behavior | Claude loads it as working instructions. Keep it focused and **under 500 lines**. [Official skill guidance](https://code.claude.com/docs/en/skills#add-supporting-files) |
| `skills/<name>/references/` | Detailed domain rules, examples, lookup material, or external-system guidance needed only for some runs | References keep `SKILL.md` small and are loaded only when needed. They are optional, but `SKILL.md` must say exactly when to read them. [Official supporting-file guidance](https://code.claude.com/docs/en/skills#add-supporting-files) |
| `skills/<name>/scripts/` | Deterministic work such as schema validation, formatting, conversion, or calculations | Code should perform repeatable checks; the skill should retain decisions that require judgment. [Official skill script example](https://code.claude.com/docs/en/skills#add-supporting-files) |
| `skills/<name>/requirements.txt` | Only pinned Python packages required by that skill's scripts | Dependencies stay with their owner and make local and CI execution reproducible. This is a project convention using the standard [pip requirements-file format](https://pip.pypa.io/en/stable/reference/requirements-file-format/). |
| `contracts/<name>.schema.json` | The exact machine-readable input or output exchanged between agents: required fields, allowed values, version, and rejected extras | Contracts stop a downstream agent from accepting incomplete or incompatible output. This is our framework convention built on [JSON Schema](https://json-schema.org/learn/getting-started-step-by-step). Claude does not auto-discover this folder. |
| `skills/<name>/tests/` | Fixtures and tests for scripts, validators, and important failure paths | Tests prove that deterministic gates accept valid artifacts and reject invalid ones. Use the owning runtime's standard test framework, such as [Python `unittest`](https://docs.python.org/3/library/unittest.html). |

`SKILL.md` should point to every supporting reference or script it expects Claude to use. A file placed in a skill
folder without a clear instruction may never be loaded or executed.

### Example: one automation-fix skill used by two agents

If Shakti and Kavach both need to repair failing automation, do not copy the repair procedure into both agent files.
Create one shared skill:

```text
.claude/skills/automation-fix/
├── SKILL.md                 Shared diagnosis and safe-fix procedure
├── references/              Locator, retry, and framework repair guidance
├── scripts/                 Deterministic validation helpers
└── tests/                   Skill-owned regression tests
```

Then preload the same skill in both agents:

```yaml
# shakti.md and kavach.md
skills:
  - automation-fix
```

- **Shakti** uses it when generated or existing automation needs a verified implementation fix.
- **Kavach** uses it after failure analysis identifies an automation defect that is safe to repair.
- The skill contains the common repair method; each agent still owns its own trigger, permissions, decisions, and
  handoff.

This pattern applies to any shared capability, such as navigation resolution, locator repair, repository calibration,
or evidence collection. One canonical skill prevents duplicated instructions and behavior drift. Claude officially
supports preloading the same project skill into multiple subagents through the `skills` field. See
[Preload skills into subagents](https://code.claude.com/docs/en/sub-agents#preload-skills-into-subagents).

## How one request moves through the framework

| Stage | Receives | Does | Produces | Must not do |
|---|---|---|---|---|
| Orchestrator | User scope or an existing analysis path | Chooses Netra-only or Netra-to-Sutra; validates handoffs | Final artifact locations and status | Query Jira, redesign tests, or modify source files |
| Netra | Jira key, URL, list, or fix version | Analyzes requirements, risk, and test coverage | Validated `analysis.json`; optional human-readable reports in compatibility mode | Write to Jira or design Gherkin |
| Sutra | Validated `analysis.json` | Calibrates against this repo and designs traceable Gherkin | Validated `test-design-output.json` and staged feature files | Re-query Jira or implement Java automation |

The handoff file is the boundary. Sutra does not repeat Netra's research; it trusts only a schema-valid `analysis.json`. This reduces conflicting interpretations, duplicated cost, and hidden context.

## Where to change current Netra and Sutra behavior

Before editing, identify the activity that owns the behavior. Change its primary owner first, then update only the listed dependent files when the behavior changes their contract or deterministic checks. Do not place domain rules in an agent or the orchestrator merely because that is where the problem was observed.

| When the requested change concerns... | Primary owner | Also check when affected |
|---|---|---|
| Netra's Jira discovery, selective source retrieval, source manifest, analysis workflow, or artifact mode | `.claude/skills/jira-analysis/SKILL.md` | `.claude/agents/netra.md` for input or handoff behavior; `.claude/agents/orchestrator.md` for pipeline defaults |
| Netra's readiness flags or production-risk classification | `.claude/skills/risk-assessment/SKILL.md` | Analysis contract and validator if the output fields or allowed values change |
| Netra's requirements, test cases, expected results, fixtures, coverage dimensions, or variation groups | `.claude/skills/test-coverage/SKILL.md` | Analysis contract, validator, fixtures, and tests if the output structure or enforceable rules change |
| The fields accepted in or emitted by `analysis.json` | `.claude/contracts/analysis-output.schema.json` | `.claude/skills/jira-analysis/scripts/validate-analysis.py`, its fixtures and tests, Netra's producer instructions, and Sutra's consumer assumptions |
| Semantic validation of Netra output | `.claude/skills/jira-analysis/scripts/validate-analysis.py` | `.claude/skills/jira-analysis/tests/`; change the schema too only when the data shape changes |
| Netra's DOCX, XLSX, or HTML content and formatting | `.claude/skills/jira-analysis/scripts/build-deliverables.py` | Builder tests and fixtures; do not put report formatting rules in `netra.md` |
| Netra's identity, tools, permissions, stopping rules, or handoff | `.claude/agents/netra.md` | The orchestrator when caller or handoff behavior changes |
| Sutra's selection of an existing feature, duplicate evidence, reusable steps, page objects, or framework gaps | `.claude/skills/repository-calibration/SKILL.md` | Gherkin validator and tests only when a new deterministic output rule is introduced |
| Sutra's module or navigation-path resolution | `.claude/skills/navigation-resolution/SKILL.md` | Navigation source data if its structure changes |
| Sutra's triage, scenario consolidation, Scenario Outlines, traceability, Gherkin conventions, or quality-gate sequence | `.claude/skills/gherkin-design/SKILL.md` | Test-design contract, validator, formatter, fixtures, and tests when applicable |
| The fields accepted in or emitted by `test-design-output.json` | `.claude/contracts/test-design-output.schema.json` | `.claude/skills/gherkin-design/scripts/validate-test-design.py`, its fixtures and tests, and the orchestrator's handoff checks |
| Gherkin table alignment | `.claude/skills/gherkin-design/scripts/align-feature-tables.py` | Gherkin-design tests and the skill command instructions |
| Semantic validation of Sutra output and staged feature files | `.claude/skills/gherkin-design/scripts/validate-test-design.py` | `.claude/skills/gherkin-design/tests/`; change the schema too only when the data shape changes |
| Sutra's identity, tools, permissions, delivery boundary, stopping rules, or handoff | `.claude/agents/sutra.md` | The orchestrator when caller or handoff behavior changes |
| Stage order, delegation, pipeline defaults, or cross-agent handoff verification | `.claude/agents/orchestrator.md` | The affected agent definitions and contracts |
| CI triggers, permissions, runtime setup, budgets, or uploaded artifacts | `.github/workflows/netra.yml` or `.github/workflows/sutra.yml` | The invoked agent, dependency files, and artifact paths |

Use these rules when a change crosses layers:

1. A judgment or domain-behavior change starts in the owning `SKILL.md`.
2. A handoff data-shape change updates the schema, producer, consumer, fixtures, validator, and tests together.
3. A repeatable enforcement or formatting change belongs in the owning script with a regression test; the skill should state when to run it.
4. Agent files stay focused on role, tools, permissions, boundaries, and handoff. The orchestrator coordinates stages but does not own Netra or Sutra domain logic.
5. Update this guide only when ownership or architecture changes, not for every rule added inside a skill.

For example, if Sutra loses a row from a Scenario Outline, start with `gherkin-design/SKILL.md`. If the loss should be rejected automatically, update `validate-test-design.py` and its tests. Change the contract only if a new field is required. Do not add the Scenario Outline rule to `sutra.md` or `orchestrator.md`.

## Where should new logic go?

Use this decision guide before creating a folder:

| If the new capability needs... | Add or change... | Example |
|---|---|---|
| Its own role, decisions, permissions, tools, or execution context | `.claude/agents/<name>.md` | A future Shakti agent that writes automation code |
| A reusable method or domain procedure | `.claude/skills/<name>/SKILL.md` | Locator selection or failure classification |
| A machine-checkable handoff between stages | `.claude/contracts/<name>.schema.json` | Automation-plan output consumed by another agent |
| The same repeatable result every time | A script inside the owning skill | JSON validation, formatting, or report generation |
| CI execution, secrets, budgets, triggers, or artifacts | `.github/workflows/<name>.yml` | Run Netra manually and publish its artifact |
| A project-wide rule that applies everywhere | `CLAUDE.md` | Never commit credentials or generated artifacts |
| Explanation for people | `.claude/docs/` | This guide |

### Do not create a new agent when

- an existing agent can perform the work with one additional reusable skill;
- the task is a deterministic transformation that belongs in a script;
- the only difference is a new prompt or output format;
- the proposed agent has no distinct tools, authority, or handoff.

### Create a new agent when

- it owns a separate business stage;
- it needs a different tool or permission boundary;
- it can stop, block, or make decisions independently;
- another stage consumes its validated output.

## How to add a stage correctly

Suppose the next stage is **Shakti**, responsible for implementing automation from Sutra's design.

1. **Define the boundary first.** State Shakti's input, output, allowed tools, write scope, stopping rules, and downstream consumer.
2. **Create the contract.** Add a schema for the automation result before connecting another stage to it.
3. **Create only the reusable skills needed.** Keep coding conventions, locator selection, and implementation procedures outside the agent definition.
4. **Add deterministic checks.** Validate paths, schema, formatting, compilation, and tests with scripts or existing build commands.
5. **Keep permissions narrow.** Grant repository write access only if implementation requires it; do not pass Jira credentials from Netra.
6. **Stage output safely.** Prefer `target/<agent>/` until the delivery behavior is reviewed. Require explicit permission before creating a branch or pull request.
7. **Connect the orchestrator last.** Add the stage only after its input and output can be validated independently.
8. **Add CI safeguards.** Pin runtime versions, set time and cost limits, use secrets, validate the artifact, and fail if files outside the allowed boundary change.
9. **Review the definition.** Run agent and skill reviews, then keep human approval for consequential changes.

## Why this design is useful beyond PR #337

- **Clear ownership:** Jira interpretation, test design, and future automation are separate responsibilities.
- **Safer access:** only Netra can read Atlassian; Sutra receives data, not credentials.
- **Reliable handoffs:** JSON schemas catch missing or malformed data before the next stage runs.
- **Lower AI variability:** formatting, validation, and report generation use deterministic code.
- **Easier debugging:** a failure belongs to a named stage and leaves a specific artifact.
- **Reusable knowledge:** multiple agents can reuse a skill without copying its instructions.
- **Controlled growth:** future agents can extend the pipeline without turning the orchestrator into a large all-purpose agent.

## Practical checks before opening a PR

- The agent definition says what it owns and what it must not do.
- Reusable procedure text is in a skill, not duplicated in the agent.
- Every downstream handoff has a schema and validator.
- Generated files stay under `target/` unless delivery was explicitly requested.
- External systems are read-only unless the use case explicitly requires a write.
- Credentials come from approved local or CI secret storage.
- CI has minimal permissions, a timeout, a cost limit, and artifact retention.
- The changed agent and every changed skill have been reviewed.
- A human still approves repository or external-system changes.

## Current state

- Netra analysis and deterministic artifact generation are implemented.
- Sutra artifact-only test design is implemented and validated.
- The orchestrator can run the Netra-to-Sutra path locally.
- Sutra's automatic CI job is intentionally disabled until the reference implementation is reviewed and merged.
- Shakti and later stages are intentionally out of scope; they must follow the same boundary-first pattern when separately approved.
