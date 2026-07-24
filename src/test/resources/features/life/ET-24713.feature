Feature: Admin - Deprecate Line Item Creative Separation Permission

  1. Removes the 'Line Item Creative Separation' permission row from the Life Features table in the Admin panel so its gating logic is no longer visible or enforced.
  2. Ensures existing line items and the accounts that held the permission keep working, with no configuration data corrupted or reset.
  3. Coordinates cleanly with the parallel ET-24715 deprecation so the two removals stay independent and the table renders correctly after both.
  4. Each scenario walks a single continuous pass through the Admin Life Features table, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24713
  @todo
  Scenario: Verify the Line Item Creative Separation permission removal and Life Features table integrity workflow
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given User is an Admin user viewing the Life Features table
    Then the "Line Item Creative Separation" row is absent and all other rows are present and intact
    When User toggles a permission row adjacent to where Line Item Creative Separation was
    Then the adjacent toggle works, the table renders correctly, and there are no orphaned rows or rendering gaps
    When User opens an Admin audit log after deployment, if audit logging covers table changes
    Then the log shows the removal of the Line Item Creative Separation permission with no erroneous entries

  # Source: ET-24713, GAP-2, AMB-1, ET-24715
  @todo
  Scenario: Verify the deprecation preserves existing line items and stays isolated from ET-24715
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given an account that previously had the Line Item Creative Separation permission enabled
    When User creates or edits a line item with that account
    Then the line item configuration works normally with no permission-related error
    When User opens an existing line item that had creative separation settings configured
    Then the existing settings are preserved if the feature still exists, or the UI is clean if the feature is removed, with no data corruption
    # GAP-2: whether existing configuration data is retained or reset is undefined - document actual behaviour
    When User opens the Life Features table after both the ET-24713 and ET-24715 deprecations are deployed
    Then the table renders without errors or phantom rows and the row count is reduced by exactly two
    When User verifies both removals are isolated
    Then neither removal causes the other to reappear or fail and the table is stable after both deployments
    # AMB-1: parallel deprecations ET-24713 and ET-24715 must be independent of each other
