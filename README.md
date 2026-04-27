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

## Code formatting

Formatting is enforced by the [Spotless](https://github.com/diffplug/spotless) Maven plugin
(Palantir Java Format for `.java`, plus rules for `pom.xml`, Markdown, and YAML).

- Run `mvn spotless:apply` before committing to auto-format your changes.
- Run `mvn spotless:check` to verify formatting without modifying files.

A scheduled GitHub Actions workflow (`.github/workflows/format-check.yml`) runs every
Sunday, executes `mvn spotless:apply`, and opens an auto-fix pull request if anything
in the repo needs reformatting. A second workflow (`.github/workflows/ci-format.yml`)
runs `spotless:check` on every pull request.

## References

For additional details on Approach, Roadmap, Documentation etc. refer to
https://docs.google.com/spreadsheets/d/1_gUs2ZiAKbCh49CFMMxLETey8sO9Ug7VkQhyNF0tZZo/edit?usp=drive_link
