Feature: LIFE Regression - Deprecate Creative Name Bid Multiplier Permission
  The Creative Name BM permission is removed from the Life Features table in Admin.
  Removal leaves the table rendering intact, adjacent permissions functional, and existing tactic workflows unaffected.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24715 (R01, edge search)
  @todo
  Scenario: Creative Name BM permission is removed from the Life Features table
    When User navigates to the Life Features table in the Admin panel
    # Framework Gap: Requires step definitions for the Life Features permission table in LifeSteps.java
    Then The "Creative Name BM" row is absent and all other permission rows are present and intact
    When User searches the Life Features table for "Creative Name"
    Then No results are returned and the search does not error or return stale cached results

  # Source: ET-24715 (R01, R05 regression)
  @todo
  Scenario: Life Features table renders correctly with adjacent permissions functional after removal
    When User navigates to the Life Features table in the Admin panel
    And User toggles a permission adjacent to where Creative Name BM was
    # Framework Gap: Requires step definitions for permission toggle and table render checks in LifeSteps.java
    Then The adjacent permission toggles correctly with no rendering error and the table saves
    And The table shows no blank or phantom rows where Creative Name BM was

  # Source: ET-24715 (R02, GAP-1)
  @todo
  Scenario: Accounts that held Creative Name BM experience no breakage in core tactic workflows
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    # Framework Gap: Requires step definition to assert no Creative Name BM gated UI remains in LifeSteps.java
    And No UI element is gated by Creative Name BM and no permission-related error appears

  # Source: ET-24715 (cross-ticket ET-24713 isolation)
  @todo
  Scenario: Creative Name BM removal does not affect the Line Item Creative Separation removal
    When User navigates to the Life Features table in the Admin panel
    # Framework Gap: Requires step definitions for cross-permission removal isolation in LifeSteps.java
    Then Both the Creative Name BM and Line Item Creative Separation rows are absent with no cross-contamination
