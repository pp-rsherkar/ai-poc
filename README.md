# PulsePoint QA Automation

Java 21 test-automation framework for PulsePoint LIFE, HCP365/Signal, and Studio. The repository combines
Playwright browser automation, Cucumber/Gherkin scenarios, API and database checks, and project-scoped Claude agents
and skills for QA workflows.

## Technology stack

| Technology | Purpose |
| --- | --- |
| Java 21 | Primary automation language |
| Maven | Build and dependency management |
| Playwright for Java | Browser automation |
| Cucumber and JUnit | BDD scenarios and test execution |
| Apache POI and OpenCSV | Spreadsheet and CSV test data |
| SQL Server JDBC | Database validation |
| Node.js and `gherkin-utils` | Feature-file formatting only |
| Claude Code | Project agents and reusable QA skills |

Versions are controlled by `pom.xml`, `package-lock.json`, and the relevant GitHub Actions workflows.

## Repository layout

```text
qa-automation/
├── src/main/java/
│   ├── api/                         API automation
│   ├── factory/                     Playwright lifecycle
│   ├── pages/                       Page objects by application
│   └── utils/                       Shared automation utilities
├── src/test/java/
│   ├── hooks/                       Cucumber lifecycle hooks
│   ├── stepdefinitions/             Step implementations
│   └── testrunner/                  Main and failed-test runners
├── src/test/resources/
│   ├── config/                      Local execution configuration
│   └── features/                    API, E2E, HCP, LIFE, and Studio features
├── .claude/
│   ├── agents/                      Claude agent definitions
│   ├── contracts/                   Structured handoff schemas
│   └── skills/                      Reusable skills using canonical SKILL.md files
├── .github/workflows/               CI and repository automation
├── config/checkstyle/               Java static-analysis configuration
├── scripts/                         Gherkin formatting scripts
├── CLAUDE.md                        Project-wide Claude instructions
├── pom.xml                          Maven configuration
└── package.json                     Gherkin formatter dependencies only
```

`target/` contains generated reports, traces, videos, build output, and agent artifacts. It must not be committed.

The existing `ai-skills/` directory contains legacy AI definitions that are being migrated incrementally. New or
refactored Claude agents and skills must use `.claude/agents/` and `.claude/skills/`.

## Prerequisites

- Git
- JDK 21
- Maven 3.9 or later
- Node.js 24 and npm for Gherkin formatting
- Access to the required PulsePoint test environments
- Approved local test configuration and credentials
- Claude Code only when running the repository's agents or skills

Confirm the main tools before setup:

```bash
java -version
mvn -version
node --version
npm --version
```

## Local setup

1. Clone the repository and enter its root directory:

   ```bash
   git clone <repository-url>
   cd qa-automation
   ```

2. Enable the version-controlled pre-commit hook:

   ```bash
   git config core.hooksPath .githooks
   ```

3. Download Java dependencies and compile without running the environment-dependent test suite:

   ```bash
   mvn -B -ntp -DskipTests compile
   ```

4. Install the Playwright browser binaries required by the configured browser:

   ```bash
   mvn -B -ntp exec:java \
     -Dexec.mainClass=com.microsoft.playwright.CLI \
     -Dexec.args="install"
   ```

5. Install the pinned Node.js formatter dependencies:

   ```bash
   npm ci
   ```

6. Obtain the approved values for `src/test/resources/config/config.properties`. Do not place plaintext credentials
   in Git or commit personal configuration changes.

## Configuration and credentials

The Java framework currently reads browser, environment, API, database, and test-account settings from
`src/test/resources/config/config.properties`. Sensitive values used by the existing framework must follow the
team-approved encryption and secret-distribution process.

Rules for all contributors:

- Never commit plaintext passwords, API keys, tokens, cookies, private keys, or OAuth data.
- Keep Claude and MCP credentials in environment variables, GitHub Secrets, or an approved secret manager.
- Use `ANTHROPIC_API_KEY` only through the approved local or CI secret source.
- Keep local Claude approvals and authentication outside Git.
- Review the staged diff for secrets before every commit.

If a credential is exposed, revoke or rotate it immediately and notify the repository owners. Removing it from the
latest commit is not sufficient when it has entered Git history.

## Running tests

The main runner currently selects scenarios tagged `@e2e`.

```bash
mvn -B -ntp test -Dtest=TestRunner
```

Use a Cucumber tag expression to narrow the scope without editing the runner:

```bash
mvn -B -ntp test \
  -Dtest=TestRunner \
  -Dcucumber.filter.tags="@e2e and not @wip"
```

Re-run scenarios recorded by the previous execution:

```bash
mvn -B -ntp test -Dtest=FailedTestRunner
```

Test execution requires valid environment access and configuration. Run the smallest relevant tag or feature scope
before a broader suite.

## Test artifacts

Generated artifacts are written under `target/`, including:

| Artifact | Location |
| --- | --- |
| Cucumber HTML report | `target/cucumber-reports/report.html` |
| Cucumber JSON report | `target/cucumber-reports/cucumber.json` |
| Cucumber JUnit report | `target/cucumber-reports/Cucumber.xml` |
| Failed-scenario list | `target/failed_scenarios.txt` |
| Failure traces | `target/trace_<scenario>.zip` |
| Optional videos | `target/videos/` during execution |
| Netra artifacts | `target/netra/` |
| Sutra staged Gherkin and handoff | `target/sutra/` |

Failure screenshots are attached to the Cucumber scenario report. Open a Playwright trace with the matching
Playwright tooling when diagnosing a UI failure.

## Quality checks

Run the checks relevant to your change before opening or updating a pull request:

```bash
# Java static analysis
mvn -B -ntp checkstyle:check

# Java, POM, Markdown, and YAML formatting verification
mvn -B -ntp spotless:check

# Apply configured formatting
mvn -B -ntp spotless:apply

# Test the project-specific Gherkin formatter
npm run feature:formatter:test

# Verify all feature files
npm run feature:check

# Format all feature files
npm run feature:format
```

The pre-commit hook runs Checkstyle and checks staged `.feature` files. Pull requests to `main` also run the repository
code-quality workflow.

Checkstyle suppressions cover identified legacy violations. Do not add new suppressions merely to make a change pass;
remove a file's suppression after its violations are corrected.

## Claude agents and skills

Git is the source of truth for project AI definitions:

- Agents: `.claude/agents/<agent-name>.md`
- Skills: `.claude/skills/<skill-name>/SKILL.md`
- Structured contracts: `.claude/contracts/`
- Shared MCP declarations: `.mcp.json`
- Project-wide rules: `CLAUDE.md`

For the folder logic, extension rules, and a concise architecture review, start with
[the QA AI framework documentation](.claude/docs/README.md).

An agent owns identity, tools, permissions, decisions, inputs, outputs, and handoffs. A skill contains a reusable
procedure or body of knowledge. Do not duplicate the full procedure in both places.

### Run locally

Start Claude Code from the repository root so it can discover the project configuration:

```bash
claude
```

Developers can authenticate with their Claude.ai Team/Enterprise account or load an approved Anthropic API key from a
secure local secret manager. API keys must never be written into repository files or shell history.

The project MCP configuration declares shared server details, but authentication, OAuth consent, and first-run trust
approval remain local to each user or execution environment.

Netra CI uses the official Rovo MCP endpoint with Basic authentication constructed at runtime from the GitHub Actions
secrets `JIRA_EMAIL` and `JIRA_API_TOKEN`. `JIRA_BASE_URL` identifies the intended Atlassian site when the account can
access more than one site. These secrets must belong to the same dedicated automation account and are never written to
the repository. The Atlassian organization must enable Rovo MCP API-token authentication, and the token must include
the required read/search `agent-interface` scopes.

Run the orchestrator as the main Claude Code agent so its Netra-and-Sutra delegation allowlist is enforced:

```bash
claude --agent orchestrator "Analyze ET-25077 and create the Gherkin test design."
```

The orchestrator delegates Jira analysis to Netra, validates `analysis.json`, passes that file to Sutra, and validates
Sutra's staged feature files and `test-design-output.json`. It stops before automation generation because Shakti is not
implemented in this reference branch.

Netra can also be run directly when orchestration is not required:

```text
Run Netra to perform a complete QA analysis of ET-25077.
```

Netra validates `analysis.json` and generates its DOCX, XLSX, and HTML deliverables deterministically under
`target/netra/`.

Sutra can also run directly from an existing Netra artifact without Jira or MCP access:

```bash
claude --agent sutra \
  "Create an artifact-only test design from target/netra/analysis.json under target/sutra/."
```

In GitHub Actions, Netra and Sutra have separate workflows. A successful `Netra QA analysis` run triggers Sutra through
GitHub's `workflow_run` event. Sutra receives the exact Netra run ID from the event, downloads the `netra-analysis`
artifact with read-only Actions permission, validates `analysis.json`, and uploads
`sutra-test-design-<netra-run-id>`. By default, Sutra also applies its validated staged feature files and opens a pull
request against the branch analyzed by Netra. Set the repository variable `SUTRA_DELIVERY_MODE` to `artifact_only` to
retain artifact delivery without creating a pull request. PR delivery uses the `QA_AUTOMATION` secret. No secret or
mutable "latest run" pointer is needed for the Netra handoff. This follows GitHub's official
[cross-workflow artifact pattern](https://docs.github.com/en/actions/reference/workflows-and-actions/events-that-trigger-workflows#workflow_run)
and does not require Sutra to reconnect to Atlassian. Netra is enabled for manual dispatch.

### AI definition review

Changes to Claude agents, skills, contracts, settings, MCP configuration, AI workflows, or `CLAUDE.md` trigger the AI
definition review workflow after that workflow is present on `main`. It uses the repository's `agent-reviewer` and
`skill-reviewer` skills and updates one consolidated pull-request comment.

The workflow is read-only, does not use Atlassian MCP, and requires `ANTHROPIC_API_KEY` in GitHub Actions secrets.
Human reviewers remain responsible for approval and merge decisions.

## Contribution workflow

1. Pull the latest `main` branch and create a focused feature branch.
2. Make the smallest complete change; avoid unrelated cleanup.
3. Add or update the relevant tests, scenarios, contracts, or fixtures.
4. Run the applicable quality checks and targeted tests.
5. Review the final diff for generated files, unrelated changes, and secrets.
6. Open a pull request that explains the behavior change, validation performed, and any known limitation.
7. Address automated and human review findings before merge.

Do not commit generated output, dependency caches, virtual environments, IDE settings, or local Claude settings.

## Additional documentation

- [Framework overview](FRAMEWORK_OVERVIEW.md)
- [Project instructions](CLAUDE.md)
- [Maven standard directory layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout)
- [Playwright Java browser installation](https://playwright.dev/java/docs/browsers)
- [Claude Code project directory](https://code.claude.com/docs/en/claude-directory)
- [QA automation roadmap and supporting material](https://docs.google.com/spreadsheets/d/1_gUs2ZiAKbCh49CFMMxLETey8sO9Ug7VkQhyNF0tZZo/edit?usp=drive_link)
