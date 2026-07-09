Read the feature file $ARGUMENTS from the repository and generate the complete test automation for it.
Only work on scenarios tagged with @todo — skip all other scenarios in the feature file.

Follow CLAUDE.md for the full workflow — all 5 phases:
1. Locate and parse the feature file, catalog every step but only for @todo-tagged scenarios, identify the domain
2. Scan existing step definitions, page objects, and utilities — reuse first, never duplicate
3. Navigate the live application for real locators, then generate step definitions and page object classes (only for steps needed by @todo scenarios)
4. Run the @todo scenarios with Maven (use `-Dcucumber.filter.tags="@todo"`) and iterate until all pass with zero failures
5. Create a new branch, commit all changes, and push
