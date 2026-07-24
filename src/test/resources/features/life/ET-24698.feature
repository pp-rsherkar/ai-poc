Feature: Deal Platform - Additional Metrics in Deal Groups Associated Tactics (Internal Beta)

  1. Adds permission-gated metric columns, a date-range selector, sorting, filtering, and CSV export to the Deal Groups > Associated Tactics view.
  2. Confirms the descoped 'Show Tactics From Other Accounts' checkbox is absent and enforces cross-account data isolation.
  3. Guards against the Deal Platform's history of populated-count and access regressions in the Associated Tactics area.
  4. Each scenario walks a single continuous pass through the Deal Groups Associated Tactics view, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24698, GAP-1, GAP-3, AMB-1
  @todo
  Scenario: Verify the permission-gated metrics, date range, sort, filter, and CSV export workflow in Associated Tactics
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given an account with all new metrics permissions enabled
    When User opens a Deal Group and navigates to the Associated Tactics view
    Then the new metric columns appear in the Associated Tactics table
    # GAP-1: exact column names and metric definitions are in the PROD-15471 Figma and must be obtained
    When User selects the last 30 days in the date-range selector
    Then the metrics update to reflect the selected range and values change appropriately
    When User clicks a sortable metric column header
    Then the tactics reorder ascending/descending by that metric with a visible sort indicator
    When User applies a filter to a metric column such as spend greater than 100
    Then the table shows only tactics matching the filter condition
    When User clicks the CSV export button
    Then a CSV downloads containing the visible metric columns and tactic rows matching the UI view
    # GAP-3: CSV field order/headers/date format are unspecified - document actual output and behaviour with active filters
    Then no "Show Tactics From Other Accounts" checkbox is present anywhere in the view
    # AMB-1: the checkbox is descoped (Pavan Dasari) - verify it is fully absent, not merely disabled

  # Source: ET-24698, HT-5419, HT-5521, HT-6125, HT-5666
  @todo
  Scenario: Verify permission gating, data accuracy, and Deal Groups regression guards in Associated Tactics
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given an account without the new metrics permissions
    When User opens Deal Group > Associated Tactics
    Then the new metric columns and the date-range selector are not visible and only baseline columns appear
    Given a deal group with three or more associated tactics
    When User opens the Associated Tactics view
    Then all associated tactics appear with metric values displayed for each
    # Regression anchor: HT-5419 - Associated Tactics not populating for deal-group-targeted tactics
    Given a deal group with 16 known deals
    Then the deal/tactic count metric matches the actual deal count and is not inflated
    # Regression anchor: HT-5521 - inflated deal ID count (~174 instead of 16)
    When User opens Deal Groups from the Tactic UI with a deal-groups-permitted account
    Then Deal Groups opens without an "Unable to Access" error
    # Regression anchor: HT-6125 (ACTIVE) - User unable to access Deal Groups in Tactic UI
    When User views the Associated Tactics view and then returns to the deal group deal list
    Then the applied deals count still matches the group composition
    # Regression anchor: HT-5666 - Applied deals tab count/list mismatch
    Given an Internal Beta user
    Then the Associated Tactics view shows only tactics from the user's own account with no cross-account data leakage
    Given a deal group with zero associated tactics
    Then an empty-state message is shown with no error and the metric columns still render
