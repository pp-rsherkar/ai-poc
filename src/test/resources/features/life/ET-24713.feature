Feature: Life Admin - Deprecate Line Item Creative Separation Permission

  1. Removes the Line Item Creative Separation permission row from the Life Features table in Admin while leaving all other rows intact.
  2. Ensures accounts that previously had the permission can still configure line items without error and existing settings are not corrupted.
  3. Keeps the Life Features table rendering correctly after the row removal, alongside the parallel Creative Name BM deprecation.
  4. Each scenario walks a single continuous pass through the Life Features table, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the Line Item Creative Separation permission is removed and the table remains stable
    Given User navigates to the Life Features table in Admin
    # Framework Gap: Requires step definitions for the Life Features permission table in LifeSteps.java
    Then the Line Item Creative Separation row is absent while all other rows are present and intact
    When User toggles a permission adjacent to where Line Item Creative Separation was
    Then the adjacent permission toggles correctly with no orphaned rows or rendering gap
    Then the table renders after both the ET-24713 and ET-24715 deprecations with no blank or phantom rows and the row count reduced by exactly two
    # Regression anchor: combined table rendering regression for both parallel deprecations

  @todo
  Scenario: Verify deprecating Line Item Creative Separation does not break existing line item workflows
    Given User is on an account that previously had Line Item Creative Separation enabled
    When User creates or edits a line item
    Then the line item configuration works normally with no permission-related errors
    Given User opens an existing line item that had creative separation settings configured
    Then the existing settings are preserved or the UI is clean with no data corruption
    # GAP-2: whether existing configuration data is retained or reset is undefined

  @todo
  Scenario: Verify Line Item Creative Separation removal is isolated from the Creative Name BM removal and audited
    Given User navigates to the Life Features table in Admin
    Then neither removal causes the other to reappear or fail and the table is stable after both deployments
    # AMB-1: parallel removals must be independent
    Given User opens the Admin audit log after deployment
    Then the log shows the removal of Line Item Creative Separation with no erroneous entries
