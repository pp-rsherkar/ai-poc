Feature: Life Deal Platform - Deal Groups Metrics and Hidden Deal Handling
  1. Adds permission-gated metric columns, date range, sorting, filtering and CSV export to the Deal Groups Associated Tactics view.
  2. Filters hidden deals from the Add Deals picker while keeping hidden deals already in a group active for delivery.
  3. Validates duplicate deal group names and keeps deal counts accurate.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24698, PROD-15471
  @todo
  Scenario: Verify the Associated Tactics metrics view with columns, date range, sort, filter and CSV export
    Given User opens a Deal Group and navigates to the Associated Tactics view with all metrics permissions enabled
    # Framework Gap: Requires step definitions for the Deal Groups Associated Tactics metrics view in LifeSteps.java
    Then The new metric columns defined in the PROD-15471 Figma appear in the Associated Tactics table
    And All tactics that target the deal group populate in the view with metric values displayed
    When User selects the last 30 days in the date range selector
    Then The metrics update to reflect the selected date range
    When User clicks a sortable metric column header
    Then The tactics reorder ascending or descending by that metric with a sort indicator shown
    When User applies a filter of spend greater than 100 to a metric column
    Then The table shows only tactics matching the filter and hides the others
    When User clicks the CSV export button
    Then A CSV downloads containing all visible metric columns and tactic rows matching the UI view

  # Source: ET-24698, PROD-15471, AMB-1, GAP-2
  @todo
  Scenario Outline: Verify permission gating and descoped controls in the Associated Tactics view
    Given User opens the Associated Tactics view "<PERMISSION_SETUP>"
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | PERMISSION_SETUP                        | EXPECTED_RESULT                                                       |
      | without the metrics columns permission  | The new metric columns are not visible and only baseline columns appear |
      | without the date range permission       | No date range selector is visible and the table shows the default period |
      | with all permissions enabled            | No Show Tactics From Other Accounts checkbox is present anywhere in the view |

  # Regression anchor: HT-5521 inflated deal counts; HT-5419 Associated Tactics not populating; HT-6125 Deal Groups access; HT-5666 applied deals mismatch
  # Source: ET-24698, HT-5521, HT-5419, HT-6125, HT-5666
  @todo
  Scenario: Verify Deal Groups metric accuracy, population, access and cross-account isolation
    Given A deal group with 16 known deals and 5 tactics targeting it
    When User opens the Associated Tactics view
    Then The deal or tactic count metric matches the actual composition and is not inflated
    And All 5 deal-group-targeted tactics appear in the view and none are excluded
    And No tactics from accounts the user should not access appear in the view
    When User opens Deal Groups from the Tactic UI with the deal groups permission
    Then Deal Groups opens without an Unable to Access error
    When User returns to the deal group deal list after viewing Associated Tactics
    Then The applied deals count still matches the actual number of deals in the group

  # Source: ET-24698, PROD-15471
  @todo
  Scenario Outline: Verify Associated Tactics edge and CSV consistency behaviour
    Given User opens the Associated Tactics view for "<CONDITION>"
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | CONDITION                          | EXPECTED_RESULT                                                          |
      | a deal group with zero tactics     | An empty state message is shown with no error and the metric columns still display |
      | a date range set to today only     | Today metrics are shown and very small or zero values display as 0 not blank |
      | a CSV export while a filter is applied | The CSV exports rows consistently with the applied filter behaviour       |

  # Source: ET-24696, PROD-16021
  @todo
  Scenario: Verify hidden deal filtering, messaging and re-unhide in the Add Deals picker
    Given A deal group management view with at least one deal in the account marked as hidden
    # Framework Gap: Requires step definitions for hidden deal filtering in the Deal Group Add Deals picker in LifeSteps.java
    When User opens the Add Deals view
    Then The hidden deals are not shown in the picker and all non-hidden deals appear normally
    When User opens a deal group that contains at least one hidden deal
    Then The UI shows the messaging from the Figma that the group contains hidden deals
    When User opens a deal group where all deals have been hidden
    Then The UI shows a clear message that all deals in the group are hidden with no crash
    When User unhides a previously hidden deal and reopens the Add Deals picker
    Then The deal reappears in the picker within an acceptable refresh time

  # Source: ET-24696, PROD-16021
  @todo
  Scenario: Verify hidden deals already in a group remain active for delivery
    Given A deal group containing a deal that is later hidden and a tactic that targets the group
    When User verifies tactic delivery
    Then The tactic continues to target and deliver using the deal group and the hidden deal remains active
    And The applied deals count reflects the total deals in the group including the hidden deals

  # Source: ET-24696, PROD-16021, GAP-2
  @todo
  Scenario Outline: Verify duplicate deal group name validation and account-scoped uniqueness
    Given User creates a deal group in the account
    When User creates a deal group named "<NAME_ATTEMPT>"
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | NAME_ATTEMPT                                      | EXPECTED_RESULT                                                          |
      | a name that already exists in the account         | The specific duplicate name error from the Figma appears and the group is not created |
      | the same name in a different letter case          | The documented case-sensitive or case-insensitive behaviour is applied consistently |
      | the same name but in a second separate account    | Both groups are created because name uniqueness is account-scoped not global |

  # Regression anchor: HT-5167 - Deal Groups filter returned no deals for an external user
  # Source: ET-24696, HT-5167, HT-6125
  @todo
  Scenario: Verify the Deal Groups deals filter returns correct deals for a valid account
    Given An external-user account with deals in the Deal Groups Deals view
    When User applies a filter to search for deals
    Then The filter returns the correct non-hidden deals and no deals available error does not appear
