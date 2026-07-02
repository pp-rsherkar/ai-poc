Read the feature file $ARGUMENTS from the repository and generate the complete test automation for it.

Follow CLAUDE.md for the full workflow — all 5 phases:
1. Locate and parse the feature file, catalog every step, identify the domain
2. Scan existing step definitions, page objects, and utilities — reuse first, never duplicate
3. Navigate the live application for real locators, then generate step definitions and page object classes
4. Run the feature file with Maven and iterate until all scenarios pass with zero failures
5. Create a new branch, commit all changes, and push
