# Copilot instructions for `qa-automation`

This repository is a Java + Maven + Playwright + Cucumber UI/API automation
suite. When Copilot is asked to fix a failing scenario (typically through an
issue auto-filed by the `E2E Playwright Tests` workflow), follow these rules.

## Tech stack

- **JDK 21** (see `pom.xml` `maven.compiler.source/target`).
- **Maven** build; tests are executed via the JUnit 4 Cucumber runner
  `src/test/java/testrunner/TestRunner.java`.
- **Playwright for Java** `1.50.0`.
- **Cucumber JVM** `7.20.1`.
- **Spotless** (Palantir Java Format) enforces formatting; CI runs
  `spotless:check` and a weekly auto-fix workflow runs `spotless:apply`.

## How to run the tests

```bash
# Full @e2e suite
mvn -B -Dtest=TestRunner test

# Filter to a specific tag while debugging
mvn -B -Dtest=TestRunner -Dcucumber.filter.tags="@e2e and @login" test

# Re-run only previously failed scenarios
mvn -B -Dtest=FailedTestRunner test
```

Artifacts produced on every run:

- `target/cucumber-reports/report.html` — Cucumber HTML report.
- `target/cucumber-reports/cucumber.json` — JSON report.
- `target/failed_scenarios.txt` — re-run file consumed by `FailedTestRunner`.
- `target/trace_<scenario>.zip` — Playwright trace, written by `hooks.Hooks`
  when a scenario fails. Open with `npx playwright show-trace`.

## Before opening a PR

1. **Format**: run `mvn spotless:apply` (the project uses Palantir Java Format
   with `ratchetFrom=origin/main`, so only changed lines are reformatted).
2. **Verify**: run the failing scenario locally with the tag filter shown
   above and make sure it passes.
3. **Scope**: only modify files needed to fix the failing scenario. Typical
   layers, in order of preference:
   - **Locator / selector drift** → fix in `src/main/java/pages/{life,hcp,studio,admin}/...`.
   - **Flaky waits / timing** → use Playwright auto-waiting APIs in the page
     class, not `Thread.sleep` in step definitions.
   - **New product behaviour** → update the feature in
     `src/test/resources/features/...` and add a matching step in
     `src/test/java/stepdefinitions/...`.
   - Avoid touching `src/main/java/factory/DriverFactory.java` or
     `src/test/java/hooks/Hooks.java` unless the failure is clearly in the
     browser bootstrap or tracing.
4. **PR**: reference the auto-filed issue with `Fixes #<n>` so the issue
   closes when the PR merges.

## Conventions

- One page class per page under `src/main/java/pages/<product>/`. Locators
  live in the page class, never in step definitions.
- Step definitions are thin — they call page objects and assert.
- Use `utils.ConfigReader` for environment values; never hard-code URLs or
  credentials. Secrets are encrypted (see `EncryptionDecryption`).
- Tags: `@e2e`, `@regression`, plus per-product/page tags. The `Before`/`After`
  hooks in `hooks.Hooks` only fire for `@e2e or @regression`.
- Headless is configured via `headless=true` in
  `src/test/resources/config/config.properties`; CI always runs headless.

## What not to do

- Do not introduce new linting/formatting tools.
- Do not bypass Spotless (`ratchetFrom=origin/main` means brand-new files must
  fully conform — YAML uses 2-space indent, trailing whitespace trimmed,
  files end with newline).
- Do not commit screenshots, traces, or anything under `target/`.
- Do not weaken assertions to make a scenario pass; fix the locator or
  product expectation instead.
