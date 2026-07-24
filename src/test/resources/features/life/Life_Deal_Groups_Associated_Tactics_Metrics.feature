Feature: LIFE Regression - Deal Groups Associated Tactics Metrics
  The Deal Groups Associated Tactics view exposes permission-gated metric columns, a date range selector, filtering, sorting, and CSV export.
  Metric values reflect actual deal group composition and the descoped Show Tactics From Other Accounts control is absent.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24698 (R01, R06, GAP-2)
  @todo
  Scenario Outline: New metric columns are gated by the required permission
    When User opens a Deal Group and navigates to the Associated Tactics view with permission "<PERMISSION>"
    # Framework Gap: Requires step definitions for the Deal Groups Associated Tactics view in LifeSteps.java
    Then The new metric columns are "<VISIBILITY>"
    Examples:
      | PERMISSION            | VISIBILITY  |
      | all metrics enabled   | visible     |
      | no metrics permission | not visible |

  # Source: ET-24698 (R02, R03, R04, GAP-1, GAP-3)
  @todo
  Scenario: Date range, sorting, filtering and CSV export operate on the metrics view
    When User opens a Deal Group and navigates to the Associated Tactics view with permission "all metrics enabled"
    And User selects a date range of the last 30 days
    # Framework Gap: Requires step definitions for date range, sort, filter and CSV export in LifeSteps.java
    Then The metrics update to reflect the selected date range
    When User sorts by a metric column and filters by spend greater than 100
    Then The tactics reorder by the metric and only tactics matching the filter are shown
    When User clicks the CSV export button
    Then A CSV downloads containing the visible metric columns and rows matching the UI view

  # Source: ET-24698 (R05 descoped)
  @todo
  Scenario: The descoped Show Tactics From Other Accounts checkbox is not present
    When User opens a Deal Group and navigates to the Associated Tactics view with permission "all metrics enabled"
    # Framework Gap: Requires step definition to assert absence of the descoped control in LifeSteps.java
    Then No Show Tactics From Other Accounts checkbox is visible anywhere in the view

  # Source: ET-24698 (R07, HT-5521, HT-5419, HT-5372 regression)
  @todo
  Scenario: Regression - Associated Tactics populates correctly with non-inflated metrics
    When User opens a Deal Group with 16 known deals used by 5 tactics
    # Framework Gap: Requires step definitions for deal group composition assertions in LifeSteps.java
    Then All 5 deal-group-targeted tactics appear in the Associated Tactics view
    And The metric values match the actual deal count of 16 and are not inflated beyond the group size

  # Source: ET-24698 (HT-6125 regression, AMB-2 access, edge)
  @todo
  Scenario: Regression - deal group access, cross-account isolation and empty state
    When User with the Deal Groups permission opens Deal Groups from the Tactic UI
    # Framework Gap: Requires step definitions for deal group access and isolation in LifeSteps.java
    Then Deal Groups opens without an Unable to Access error
    And The Associated Tactics view shows only tactics from the user's own account
    When User opens a Deal Group with zero associated tactics
    Then An empty state message is shown with no error and the metric columns still display
