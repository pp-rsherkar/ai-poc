# qa-automation

## Overview

This project / repository contains automation code for LIFE, HCP365 & STUDIO applications. Regression & Functional
testing scenario's written using business language for better understanding. Project has modular approach, it uses
features, step definitions, page classes & common functions etc.

## Technology

#### Playwright

#### JAVA

#### Cucumber

#### Maven

## Getting started

1. Clone repository as maven project, ensure all dependencies are resolved correctly. (**Note** - JDK version must be
   set in project properties)
2. Update TestRunner file with feature name / tag values as required to execute scenario's.
3. Create run configuration as shown below & execute.
   <img width="595" alt="image" src="https://github.com/user-attachments/assets/86967dc0-fc6b-47fe-8873-aad598bdff31" />

## AI-assisted CI pipeline

The repository ships an automated loop that runs the Playwright/Cucumber
tests in GitHub Actions and hands failures off to GitHub Copilot (which uses
the GitHub MCP server to read the failing run and propose a fix):

1. **`.github/workflows/e2e-tests.yml`** runs the `@e2e` suite on every PR,
   nightly, and on demand. On failure it uploads Cucumber reports plus
   Playwright traces/screenshots, then opens (or updates) an issue labelled
   `test-failure` and assigns it to `@copilot`.
2. **`.github/workflows/copilot-setup-steps.yml`** pre-installs JDK 21,
   Maven dependencies, and Playwright browsers in the Copilot coding agent's
   sandbox so it can reproduce and verify a fix before pushing.
3. **`.github/copilot-instructions.md`** tells the agent how the repo is laid
   out — JDK 21, Spotless, page-object layering, `@e2e` tag, test commands —
   so the resulting PR follows existing conventions.

To enable the Playwright MCP server for richer browser-driven diagnosis,
register it in **Repository Settings → Copilot → Coding agent → MCP**:

```json
{
  "mcpServers": {
    "playwright": {
      "command": "npx",
      "args": ["-y", "@playwright/mcp@latest", "--headless"]
    }
  }
}
```

## References

For additional details on Approach, Roadmap, Documentation etc. refer to
https://docs.google.com/spreadsheets/d/1_gUs2ZiAKbCh49CFMMxLETey8sO9Ug7VkQhyNF0tZZo/edit?usp=drive_link
