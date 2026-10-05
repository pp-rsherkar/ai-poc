# QA Automation Project Instructions

## Project purpose

- This repository is primarily a Java 21 Maven project for Playwright and Cucumber QA automation.
- Java automation code, Claude agents, reusable skills, and small supporting tools coexist here but have separate ownership boundaries.
- Preserve existing behavior unless the requested change explicitly requires and explains a behavior change.

## QA AI Framework

- This repository is the source of truth for the PulsePoint QA AI framework.
- Use available project agents and skills when they are relevant, and prefer reusing existing capabilities over duplicating them.
- The legacy implementation is being migrated incrementally to the current agent and skill architecture.
- When a required capability is not available in the current architecture, check the legacy implementation before creating a new solution.
- Do not assume that a missing native agent or skill means the capability does not exist in this repository.

## Core working rules

- Keep implementations minimal, simple, and direct. If a simpler solution meets the requirement, use it.
- Do not add abstractions, dependencies, frameworks, wrappers, or configuration without a concrete need.
- Work one logical step at a time. Validate each step before proceeding.
- Read the relevant implementation, tests, and configuration before editing.
- Keep changes within the requested scope. Do not refactor unrelated legacy code opportunistically.
- Prefer small, reviewable changes over broad rewrites.
- Reuse an existing project pattern when it remains correct; improve it only when the change needs improvement.
- Do not report completion until the relevant validation has passed.

## Repository boundaries

- `pom.xml` and `src/` belong to the Java automation project.
- `src/main/java/pages/` contains page objects and UI interactions.
- `src/main/java/utils/` contains shared automation utilities.
- `src/test/java/stepdefinitions/` contains Cucumber step definitions.
- `src/test/resources/features/` contains Gherkin features.
- The root `package.json` and `scripts/format-feature*` belong only to the existing Gherkin formatter.
- Do not add agent or AI tooling dependencies to the root Node project.
- `.github/` contains GitHub-specific workflows and their isolated supporting tools.
- Generated build and agent artifacts belong under `target/` and must not be committed.

## Language ownership

- Use Java for browser automation, API automation, page objects, step definitions, runners, and framework utilities.
- Use Python for deterministic scripts owned by Claude skills, such as schema validation, report generation, or artifact conversion.
- Keep each Python tool's `requirements.txt` with the skill or workflow that owns it.
- Use JavaScript only for the existing Gherkin formatter or when a required platform API has no suitable Java or Python option.
- Do not introduce a second implementation language for a task already handled clearly by the owning language.
- Shell scripts may provide short command wrappers only; business logic belongs in Java or the owning Python tool.

## Java automation conventions

- Target Java 21 and use Maven dependencies from `pom.xml`.
- Keep page interactions in page objects. Keep step definitions thin and focused on scenario orchestration.
- Reuse existing utilities when they express the required wait, configuration, file, database, or API behavior correctly.
- Add new shared utilities only after the same logic is needed in more than one place.
- Prefer `private final Locator` fields initialized in the page-object constructor.
- Use descriptive method names that express user behavior rather than DOM mechanics.
- Do not put credentials, environment-specific secrets, or mutable test data in source files.
- Keep tests independent where practical. Do not rely on execution order unless the suite explicitly requires it.

## Playwright locator policy

Choose the first reliable option in this order:

1. `getByRole()` with an accessible name.
2. `getByLabel()` for form controls.
3. `getByPlaceholder()`, `getByAltText()`, or `getByTitle()` when that attribute is a stable user contract.
4. `getByText()` with an exact value when visible text is the intended contract.
5. `getByTestId()` when semantic locators are unavailable or the element needs an explicit automation contract.
6. A stable ID or stable attribute selector.
7. CSS or XPath only when no reliable semantic or contract-based locator exists.

Additional locator rules:

- Scope locators to a stable parent, then chain or filter to reach the target.
- Every action locator should resolve uniquely under Playwright strictness.
- Avoid DOM-depth XPath, generated class names, layout-dependent selectors, and broad `contains(text(), ...)` expressions.
- Do not use `.first()`, `.last()`, or `.nth()` merely to silence a strictness error. Use them only when position is the real behavior or no stable identity exists.
- Do not concatenate uncontrolled data into XPath or CSS selectors. Prefer semantic locators and `filter()`.
- For new application features, request a stable `data-testid` when no accessible locator can identify the element reliably.
- Do not rewrite unrelated legacy locators while implementing a focused change.

## Waiting and assertions

- Use Playwright locator actions and retrying assertions so Playwright can perform its normal auto-waiting.
- Do not add `page.waitForTimeout()` or `Thread.sleep()` to production tests.
- Wait for an observable application state: a locator state, spinner disappearance, response, URL, or expected UI result.
- Use `WaitUtility` only where the application needs a shared state-specific wait that Playwright does not already provide directly.
- Do not use forced actions to hide an incorrect locator or application-state problem.

## Gherkin and test design

- Write scenarios in business language and test user-visible behavior.
- Keep feature files free of Java, selectors, page-object details, and implementation-specific instructions.
- Reuse an existing step only when its behavior and wording genuinely match.
- Avoid duplicate scenarios that exercise the same behavior without adding coverage.
- Include negative and boundary coverage when the requirement or risk justifies it.
- Preserve the repository's formatter output; do not hand-format against it.

## Claude agents and skills

- Project agents live at `.claude/agents/<agent-name>.md`.
- An agent definition contains identity, model choice, tools, permissions, inputs, outputs, and handoff responsibilities.
- Reusable procedures live at `.claude/skills/<skill-name>/SKILL.md`, exactly one directory below `.claude/skills/`.
- Skill directory names and skill `name` values use lowercase letters, numbers, and hyphens.
- A skill may own `scripts/`, `references/`, `assets/`, tests, and a local dependency file.
- Shared structured handoff schemas live under `.claude/contracts/`.
- Agents reference skills; do not copy the full skill procedure into an agent file.
- `CLAUDE.md` contains only durable project-wide rules. Do not place complete agent workflows here.
- Add a new agent only when it needs its own role, decisions, tools, permissions, model, or execution context.
- Add a new skill when a reusable procedure or body of knowledge can serve one or more agents.
- Validate structured agent handoffs against their schema before a downstream agent consumes them.
- Keep deterministic operations in scripts rather than asking the model to regenerate the same transformation.
- Netra work must not modify other agents or skills unless the request explicitly includes them.

## MCP and external systems

- Project-shareable MCP configuration belongs in `.mcp.json`; local credentials and approvals remain local.
- Never commit API keys, tokens, passwords, OAuth data, cookies, or secret-manager output.
- Read credentials from environment variables or the approved local or CI secret store.
- GitHub Actions must use repository or organization secrets, including `ANTHROPIC_API_KEY` where required.
- Agent and skill files may name required environment variables but must never contain their values.
- External writes must stay within the user's requested task and the responsible agent's declared permissions.

## Change and validation workflow

1. Inspect the relevant files and current working-tree state.
2. State any behavior change before implementing it.
3. Make the smallest complete change.
4. Run the narrowest relevant checks first.
5. Run broader checks when the change can affect shared behavior.
6. Review the final diff for unrelated changes, generated files, and secrets.

Common checks:

- Java style: `mvn -B -ntp checkstyle:check`
- Java formatting: `mvn -B -ntp spotless:check`
- Java tests: run the smallest applicable test or Cucumber scope before a broader suite.
- Formatter tests: `npm run feature:formatter:test`
- Feature formatting: `npm run feature:check`
- Python tools: use the owning tool's local requirements and tests; do not install project packages globally.
- Generated DOCX, XLSX, or HTML artifacts require structural checks and an appropriate rendered or visual review.

## Git and safety

- Preserve unrelated user changes and untracked work.
- Do not modify, delete, or stage files outside the requested scope.
- Do not use destructive Git commands to clean the working tree.
- Do not commit generated output, dependency caches, virtual environments, IDE state, or local Claude settings.
- Do not create or merge a pull request unless the user requests it.

## Official references

- Claude Code project instructions: https://code.claude.com/docs/en/claude-directory
- Claude Agent Skills: https://platform.claude.com/docs/en/agents-and-tools/agent-skills/overview
- Repository skill discovery: https://platform.claude.com/docs/en/managed-agents/skills
- Playwright locators: https://playwright.dev/java/docs/locators
- Playwright auto-waiting: https://playwright.dev/java/docs/actionability
