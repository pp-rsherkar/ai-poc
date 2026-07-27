Feature: Life Admin - Deprecate Creative Name Bid Multiplier Permission

  1. Removes the Creative Name BM permission from the Life Features table in Admin.
  2. Preserves existing tactic and creative workflows for accounts that previously held the permission.
  3. Keeps the Life Features table rendering correct after the row removal.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24715 (TC_01, TC_02, TC_03, TC_05, TC_06)
  @todo
  Scenario: Verify Creative Name BM permission removal and Life Features table integrity
    # Framework Gap: Requires page object + step definitions for the Life Features permission table in AdminSteps.java
    Given User navigates to the Administrative section and opens the Life Features table
    Then The Creative Name BM row is absent and all other permission rows are present and intact
    When User toggles a permission adjacent to where Creative Name BM was
    Then The adjacent permission toggles correctly and the table saves with no rendering gap
    When User searches the Life Features table for "Creative Name"
    Then No results are returned and the search does not error
    And The table renders with no blank row and the row count is reduced by exactly one
    When User creates and saves a tactic on an account that previously held Creative Name BM
    Then The tactic saves normally with no permission-related error

  # Source: ET-24715 (TC_04, TC_07)
  @todo
  Scenario: Verify Creative Name BM is inert and its removal is independent of the Line Item Creative Separation removal
    Given User navigates to the Administrative section and opens the Life Features table
    Then No UI element is gated by Creative Name BM and any residual permission check evaluates as denied
    And Both Creative Name BM and Line Item Creative Separation rows are absent with no cross-contamination
