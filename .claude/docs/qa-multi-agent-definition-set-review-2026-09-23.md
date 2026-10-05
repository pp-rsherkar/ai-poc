# Simplified Review: QA Multi-Agent Definition Set

**Review date:** 2026-09-23

**Scope:** Orchestrator, Netra, Sutra, their skills, contracts, scripts, MCP boundary, and CI workflows

**Status:** Sound reference architecture; production rollout still has explicit follow-up work

## Executive summary

The structure is appropriate for this QA framework because it separates three different concerns:

1. **Netra understands the work** from Jira and supporting evidence.
2. **Sutra turns that understanding into test design** that fits this repository.
3. **The orchestrator controls the sequence** and accepts only validated handoffs.

The most important rule is:

> **Use AI for judgment, use scripts for repeatable checks, and use schemas between agents.**

This prevents one agent from reading Jira, inventing scenarios, changing automation, and publishing results with unrestricted access in a single opaque run.

## How it relates to our current AI framework

| Current framework concept | New structure | Meaning |
|---|---|---|
| Netra / `iAnalyze` | `.claude/agents/netra.md` plus analysis skills | Netra becomes a bounded worker; detailed methods are reusable skills |
| Sutra / `iDesign` | `.claude/agents/sutra.md` plus design skills | Sutra consumes Netra's validated result instead of repeating Jira analysis |
| Shared prompt instructions | `.claude/skills/<name>/SKILL.md` | Reusable procedures have a defined owner and can include tests/scripts |
| Informal output passed to the next step | `.claude/contracts/*.schema.json` | Handoffs become machine-checkable |
| Manual agent sequencing | `.claude/agents/orchestrator.md` and CI workflows | Order, stopping behavior, permissions, and artifacts are explicit |
| Generated reports beside source files | `target/netra/` and `target/sutra/` | Temporary results are isolated and not committed |

The old `ai-skills/` structure is not removed by this change. Migration is incremental: when an existing capability is refactored, its agent definition moves under `.claude/agents/` and its reusable procedures move under `.claude/skills/`.

## What is good in the design

- **Responsibilities are clear.** Netra analyzes; Sutra designs; the orchestrator coordinates.
- **The agents do not share unnecessary access.** Netra can read Atlassian; Sutra cannot.
- **The handoff is testable.** JSON schemas and validators run before the next stage.
- **Repeatable work is code.** Formatting, report generation, and structural checks are scripts rather than model instructions.
- **Generated output is contained.** CI fails if an artifact-only run changes files outside `target/`.
- **CI is bounded.** Workflows define read-only permissions, timeouts, budgets, pinned tooling, and artifact retention.
- **Humans keep final authority.** The current path does not merge code or write to Jira.

## What this change does not claim to solve

- It does not implement Shakti automation generation or later agents.
- It does not allow agents to merge pull requests.
- It does not remove the need for human review of requirements, test design, or code.
- It does not make free-form AI output reliable by itself; reliability comes from the contracts and validators.
- It does not complete Atlassian service-account rollout merely by defining the MCP configuration.

## Remaining work before normal production use

| Priority | Work | Why it matters |
|---|---|---|
| Required | Verify Netra in CI with the approved Atlassian service account and scoped read token | Confirms the real Jira integration without personal credentials |
| Required | Review and enable Sutra's `workflow_run` job on the default branch | The job is intentionally disabled in the reference implementation |
| Required for code delivery | Implement and review Sutra's explicit pull-request delivery mode | Artifact-only mode currently avoids source changes by design |
| Required before Shakti | Define Shakti's input/output contract, skills, permissions, and validation | Prevents automation generation from bypassing the architecture |
| Operational | Document secrets, local setup, failure handling, and owners | Makes the flow reproducible by another QA engineer |

## Simple go/no-go rule

**Go** for local evaluation and artifact-only Netra/Sutra runs when inputs, credentials, and validators are available.

**Do not enable unattended production delivery yet** unless all of the following are true:

- the service-account integration is verified;
- the produced contracts validate;
- repository boundary checks pass;
- the relevant agent and skills have no blocking review findings;
- a human reviews any proposed source change;
- ownership and rollback/failure handling are documented.

## Team takeaway

Think of the framework as a QA assembly line:

```text
Source evidence -> analysis -> test design -> future automation
       Netra          Sutra          Shakti (future)
```

Each station has one responsibility, accepts a defined input, produces a validated output, and cannot silently take over another station's work. Create a new folder only when it represents a real reusable capability or a real execution boundary—not simply because the prompt became long.
