Feature: Life Admin - Deprecate Line Item Creative Separation Permission

  1. Removes the Line Item Creative Separation permission from the Life Features table in Admin.
  2. Preserves existing line item configuration workflows for accounts that previously held the permission.
  3. Keeps the Life Features table rendering correct after the row removal.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24713 (TC_01, TC_02, TC_03, TC_05)
  @todo
  Scenario: Verify Line Item Creative Separation permission removal and line item workflow integrity
    # Framework Gap: Requires page object + step definitions for the Life Features permission table in AdminSteps.java
    Given User navigates to the Administrative section and opens the Life Features table
    Then The Line Item Creative Separation row is absent and all other rows are present and intact
    When User toggles a permission row adjacent to where Line Item Creative Separation was
    Then The adjacent toggle works and the table renders with no orphaned rows or gap
    When User creates or edits a line item on an account that previously had the permission enabled
    Then The line item configuration works normally with no permission-related error
    When User opens the Life Features table after both the ET-24713 and ET-24715 deprecations
    Then The table renders with no phantom rows and the row count is reduced by exactly two

  # Source: ET-24713 (TC_04, TC_06, TC_07)
  @todo
  Scenario: Verify existing line item settings, audit logging, and independence from the Creative Name BM removal
    Given User opens an existing line item that had creative separation settings configured
    Then The existing settings are preserved or the UI is clean with no data corruption
    When User checks the Admin audit log after deployment
    Then The log reflects the removal of Line Item Creative Separation with no erroneous entries
    When User verifies both parallel removals in the Life Features table
    Then Neither removal causes the other to reappear or fail and the table is stable
