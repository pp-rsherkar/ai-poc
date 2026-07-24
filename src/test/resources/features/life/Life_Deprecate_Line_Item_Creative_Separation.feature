Feature: LIFE Regression - Deprecate Line Item Creative Separation Permission
  The Line Item Creative Separation permission is removed from the Life Features table in Admin.
  Removal preserves existing line item configuration, keeps the table rendering intact, and isolates from the parallel Creative Name BM deprecation.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24713 (R01)
  @todo
  Scenario: Line Item Creative Separation permission is removed from the Life Features table
    When User navigates to the Life Features table in the Admin panel
    And User toggles a permission adjacent to where Line Item Creative Separation was
    # Framework Gap: Requires step definitions for the Life Features permission table in LifeSteps.java
    Then The "Line Item Creative Separation" row is absent and all other rows are present and intact
    And The adjacent permission toggles correctly with no orphaned rows or rendering gap

  # Source: ET-24713 (R02, GAP-2)
  @todo
  Scenario: Line item configuration works and existing settings are preserved after deprecation
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "500", enables the line item and saves the changes
    # Framework Gap: Requires step definition to assert line item creative separation settings integrity in LifeSteps.java
    Then The line item configures without permission-related errors and existing settings are not lost or corrupted

  # Source: ET-24713 (R05 regression, ET-24715 isolation)
  @todo
  Scenario: Life Features table renders correctly after both deprecations with row count reduced by two
    When User navigates to the Life Features table in the Admin panel
    # Framework Gap: Requires step definitions for permission table row-count validation in LifeSteps.java
    Then The table renders with no blank or phantom rows and the row count is reduced by exactly two
    And Neither removal causes the other to reappear or fail

  # Source: ET-24713 (GAP-1, edge audit)
  @todo
  Scenario: Admin audit log reflects the removal of Line Item Creative Separation
    When User opens the Admin audit log after deployment
    # Framework Gap: Requires step definitions for Admin audit log assertions in LifeSteps.java
    Then The log shows the removal of the Line Item Creative Separation permission with no erroneous entries
