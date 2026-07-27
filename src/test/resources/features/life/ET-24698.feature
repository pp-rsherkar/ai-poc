Feature: Life Deal Platform - Deal Groups Associated Tactics Metrics

  1. Adds new permission-gated metric columns, a date range selector, sorting, filtering, and CSV export to the Deal Groups > Associated Tactics view.
  2. Correctly populates all tactics that target a deal group, including deal-group-targeted tactics, with accurate non-inflated metric values.
  3. Keeps the "Show Tactics From Other Accounts" checkbox out of scope and enforces cross-account data isolation.
  4. Each scenario walks a single continuous pass through the Associated Tactics view, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the new metric columns, date range, sorting, filtering, and CSV export with their permission gates
    Given User opens a Deal Group and navigates to the Associated Tactics view with all new metrics permissions enabled
    # Framework Gap: Requires step definitions for the Deal Groups Associated Tactics view in LifeSteps.java
    Then the new metric columns from the design appear in the Associated Tactics table
    # GAP-1: exact column names must be obtained from the PROD-15471 Figma
    When User selects the last 30 days in the date range selector
    Then the metrics update to reflect the selected date range
    When User clicks a sortable metric column header
    Then the tactics reorder ascending or descending by that metric with a sort indicator in the header
    When User applies a filter of spend greater than $100 to a metric column
    Then only tactics matching the filter are shown and the others are hidden
    When User clicks the CSV export button
    Then a CSV downloads containing all visible metric columns and tactic rows matching the UI view
    # GAP-3: CSV format and column order not specified - document the actual output

  @todo
  Scenario: Verify permission gating hides the new columns, date range, and descoped checkbox
    Given User opens the Associated Tactics view without the new metrics permissions
    Then the new metric columns are not visible and only the existing baseline columns appear
    Given User opens the Associated Tactics view without the date range permission
    Then no date range selector is visible and the table shows the default metric period
    Given User opens the Associated Tactics view with all permissions
    Then no "Show Tactics From Other Accounts" checkbox is visible anywhere in the view
    # AMB-1: verify the descoped checkbox is fully absent, not just disabled

  @todo
  Scenario: Verify Associated Tactics populate completely with accurate, non-inflated metric values
    Given User opens a Deal Group with three or more associated tactics
    Then all associated tactics appear in the table with metric values displayed for each
    # Regression anchor: HT-5419 - Associated Tactics not populating for deal-group-targeted tactics
    Given User opens a Deal Group with 16 known deals
    Then the deal count metric shows 16 and is not inflated beyond the group size
    # Regression anchor: HT-5521 - inflated deal ID count (~174 instead of 16)
    Given User opens a Deal Group used by five different tactics
    Then all five deal-group-targeted tactics appear in the view and none are excluded
    # Regression anchor: HT-5419 - deal-group-targeted tactics must be included

  @todo
  Scenario: Verify Deal Groups access, empty states, and cross-account isolation
    Given User has the Deal Groups permission and opens Deal Groups from the Tactic UI
    Then Deal Groups opens without an "Unable to Access" error
    # Regression anchor: HT-6125 (ACTIVE) - User Unable to Access Deal Groups In Tactic UI
    Given User opens a Deal Group with zero associated tactics
    Then an empty-state message is shown with no error and the metric columns are still displayed
    Given User sets the date range to today only
    Then today's metrics are shown and very small values are displayed as 0, not blank or error
    Given User is an Internal Beta user viewing the Associated Tactics
    Then only tactics from the user's own account appear and no cross-account tactic data is exposed
    # Security: cross-account data isolation must be enforced even though R05 is descoped

  @todo
  Scenario: Verify CSV export behavior with filters and deal count accuracy after viewing tactics
    Given User applies a column filter and exports the CSV
    Then the CSV consistently exports either only the filtered rows or all rows in a documented manner
    # GAP-3: CSV export behavior with active filters
    Given User opens the Associated Tactics view for a Deal Group with N deals and returns to the deal list
    Then the deal count still matches N and viewing Associated Tactics did not affect the deal list display
    # Regression anchor: HT-5666 - Applied deals tab count mismatch
