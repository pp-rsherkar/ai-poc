Feature: Life Deal Groups - Associated Tactics Additional Metrics

  1. Adds permission-gated metric columns, a date range selector, filtering, sorting, and CSV export to the Deal Groups Associated Tactics view.
  2. Populates all tactics that target a deal group with accurate, non-inflated metric values.
  3. Enforces account data isolation for Internal Beta users.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24698 (TC_01, TC_02, TC_03, TC_04, TC_05, TC_06, TC_12)
  @todo
  Scenario Outline: Verify Associated Tactics metric controls are gated by permission
    # Framework Gap: Requires page object + step definitions for the Deal Groups Associated Tactics view in LifeDealGroupsSteps.java
    Given User opens a Deal Group and navigates to the Associated Tactics view with "<PERMISSION>"
    Then The "<CONTROL>" is "<VISIBILITY>"
    Examples:
      | PERMISSION                    | CONTROL              | VISIBILITY  |
      | new metrics permission        | new metric columns   | visible     |
      | without metrics permission    | new metric columns   | not visible |
      | date range permission         | date range selector  | visible     |
      | without date range permission | date range selector  | not visible |
      | sorting permission            | metric column sort   | visible     |
      | filter permission             | metric column filter | visible     |
      | CSV export permission         | CSV export button    | visible     |

  # Source: ET-24698 (TC_03, TC_05, TC_06, TC_13, TC_14, TC_15)
  @todo
  Scenario: Verify date range, sorting, filtering, CSV export, and empty-state on Associated Tactics
    Given User opens a Deal Group Associated Tactics view with all metrics permissions
    When User selects a last-30-days date range
    Then The metric values update to reflect the selected range
    When User sorts by a metric column header
    Then The tactics reorder by that metric with a visible sort indicator
    When User applies a filter of spend greater than "$100"
    Then Only tactics matching the filter are shown
    When User clicks the CSV export button
    Then A CSV downloads containing the visible metric columns and rows matching the UI with consistent filter behavior
    When User opens a deal group with zero associated tactics
    Then An empty-state message is shown with the metric columns still displayed and no error
    When User sets the date range to today only
    Then Today's metrics are shown with small or zero values displayed as 0 and not blank

  # Source: ET-24698 (TC_07, TC_08, TC_09, TC_10, TC_11, TC_16, TC_17)
  @todo
  Scenario: Verify descoped checkbox absence, accurate population, and Deal Groups regression guards
    Given User opens a Deal Group Associated Tactics view
    Then The Show Tactics From Other Accounts checkbox is fully absent from the view
    # Regression anchor: HT-5419 - Associated Tactics not populating for deal-group-targeted tactics
    When User opens a deal group used by 5 tactics
    Then All 5 tactics appear in the Associated Tactics view with metric values
    # Regression anchor: HT-5521 - inflated deal ID count
    When User checks the deal count metric for a deal group with 16 known deals
    Then The metric value equals 16 and is not inflated
    # Regression anchor: HT-6125 (active) - user unable to access Deal Groups in Tactic UI
    When User with the deal groups permission opens Deal Groups from the Tactic UI
    Then The Deal Groups view opens with no unable-to-access error
    # Regression anchor: HT-5666 - applied deals count mismatch
    When User views the Associated Tactics and returns to the deal group deal list
    Then The applied deals count still matches the original deal count
    When User verifies the Associated Tactics view as an Internal Beta user
    Then Only tactics from the user's own account are shown with no cross-account data leakage
