Feature: Line Item Dashboard - AQ Index Metric Rename and Raw Value Display
  1. Renames the "AQ" metric to "AQ Index" on the Line Item dashboard Overview tab and associated graph or chart.
  2. Changes the metric display from a percentage to a raw index value sourced from contextadrpt.dbo.LineitemFlightAQScore, removing the "%" sign.
  3. Updates the AQ metric tooltip to explain that values above 1 indicate above-average performance.
  4. Verifies the rename and value format change are applied consistently across all UI surfaces referencing this metric, including saved and cached views.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter

  @todo
  Scenario: Verify AQ Index label, raw value format, tooltip, and graph on Line Item Dashboard Overview tab
    When User searches for a campaign that contains a Line Item with AQ data on the Campaign Dashboard
    And User clicks the Line Item name to navigate to the Line Item dashboard
    And User selects the Overview tab on the Line Item dashboard
    Then The metric previously labeled "AQ" is now displayed as "AQ Index"
    And The "AQ Index" value is displayed as a raw numeric value without a "%" sign
    And The displayed "AQ Index" value uses a consistent number of decimal places
    And Hovering over the "AQ Index" metric shows the tooltip "Compares entity's audience quality to baseline. Values above 1 indicate above-average performance."
    When User locates the graph or chart that displays the AQ metric on the Overview tab
    Then The graph label reads "AQ Index" and its data points are raw index values without percentage conversion
    When User navigates back to the Campaign Dashboard and searches for a Line Item where the AQ Index value is below 1
    And User clicks the Line Item name and selects the Overview tab
    Then The AQ Index value is rendered as a raw number below 1 with no "%" sign and the tooltip is displayed
    When User navigates back to the Campaign Dashboard and searches for a Line Item where the AQ Index value is above 1
    And User clicks the Line Item name and selects the Overview tab
    Then The AQ Index value is rendered as a raw number above 1 with no "%" sign and the tooltip is displayed
    And All observed AQ Index values use the same decimal precision format regardless of their magnitude

  @todo
  Scenario: Verify AQ Index rename consistency across Campaign Dashboard, reports, and cached views
    When User checks the Campaign Dashboard columns and filters for any reference to the old "AQ" label
    Then All instances of the old "AQ" label on the Campaign Dashboard are replaced with "AQ Index"
    And Any "AQ Index" value on the Campaign Dashboard is displayed as a raw number without a "%" sign
    When User opens a previously saved dashboard view or report that referenced the old "AQ" label and percentage format
    Then The saved view displays "AQ Index" with the raw value format and no "%" sign
    And No cached or stale version of the old "AQ" percentage display persists after a page refresh
    When User exports or downloads a report that includes the AQ metric
    Then The exported report reflects "AQ Index" as the label with the raw value and no percentage formatting