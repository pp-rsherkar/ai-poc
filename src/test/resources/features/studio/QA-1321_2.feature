Feature: HCP2DTC Insights Addressable NPI Geographic Performance Component

  @todo
  Scenario: Verify DMA Alignment Rate displays in correct format
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    Then DMA Alignment Rate displays "12 of 15 (80%)"

  @todo
  Scenario Outline: Verify Addressable NPI Geographic Performance summary metrics display correctly
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    Then the "<metric>" metric displays the value "<expected_value>"
    And the metric is formatted correctly as "<format>"
    Examples:
      | metric                        | expected_value | format           |
      | Total Unique Reach            | 2500           | whole number     |
      | Avg Reach per Addressable NPI | 25.00          | 2 decimal places |

  @todo
  Scenario Outline: Verify Avg Reach per Addressable NPI displays with correct decimal precision
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Avg Reach per Addressable NPI metric
    Then the metric displays "<displayed_value>"
    Examples:
      | input_total_reach | input_seed_npis | displayed_value |
      | 2500              | 100             | 25.00           |
      | 100               | 10              | 10.00           |
      | 303               | 10              | 30.30           |
      | 1                 | 1               | 1.00            |

  @todo
  Scenario: Verify Avg Reach per Addressable NPI displays N/A when no seed NPIs exist
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Avg Reach per Addressable NPI metric with zero seed NPIs
    Then the metric displays "N/A" or "0.00"

  @todo
  Scenario: Verify Aligned Tab contains all required columns
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the Aligned tab
    Then the table displays the following columns in order:
      | Column                  |
      | DMA                     |
      | Addressable NPIs        |
      | Total Reach (New)       |
      | Reach/Addressable NPI   |
      | Addressable NPI Share   |

  @todo
  Scenario: Verify Diagnostics Tab contains all required columns
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the Diagnostics tab
    Then the table displays the following columns in order:
      | Column                |
      | DMA                   |
      | Addressable NPIs      |
      | Reach/Addressable NPI |
      | Addressable NPI Share |

  @todo
  Scenario: Verify Aligned Tab displays DMA with patient reach
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the Aligned tab
    Then the table displays a row with the following values:
      | Column                | Value  |
      | DMA                   | Boston |
      | Addressable NPIs      | 12     |
      | Total Reach (New)     | 450    |
      | Reach/Addressable NPI | 37.50  |
      | Addressable NPI Share | 8%     |

  @todo
  Scenario: Verify Diagnostics Tab displays DMA with zero patient reach
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the Diagnostics tab
    Then the table displays a row with the following values:
      | Column                | Value    |
      | DMA                   | Portland |
      | Addressable NPIs      | 5        |
      | Reach/Addressable NPI | 0.00     |
      | Addressable NPI Share | 3%       |

  @todo
  Scenario Outline: Verify Geographic tables are sortable by metric columns
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the "<tab_name>" tab
    And User clicks the "<column_name>" column header to sort ascending
    Then the table rows are sorted by "<column_name>" in ascending order
    When User clicks the "<column_name>" column header again to sort descending
    Then the table rows are sorted by "<column_name>" in descending order
    Examples:
      | tab_name    | column_name           |
      | Aligned     | Addressable NPIs      |
      | Aligned     | Total Reach (New)     |
      | Aligned     | Reach/Addressable NPI |
      | Aligned     | Addressable NPI Share |
      | Diagnostics | Addressable NPIs      |
      | Diagnostics | Reach/Addressable NPI |
      | Diagnostics | Addressable NPI Share |

  @todo
  Scenario: Verify View All DMAs pop-up opens and displays all DMAs sorted by Addressable NPI Share descending
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks the View All DMAs button
    Then the pop-up opens and displays all DMAs
    And the DMAs are sorted by Addressable NPI Share in descending order

  @todo
  Scenario: Verify View All DMAs pop-up is scrollable for large number of DMAs
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks the View All DMAs button
    Then the pop-up displays a scrollbar
    And User is able to scroll through all DMAs without truncation

  @todo
  Scenario: Verify View All DMAs pop-up displays same columns as Aligned Tab
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks the View All DMAs button
    Then the pop-up displays the following columns in order:
      | Column                |
      | DMA                   |
      | Addressable NPIs      |
      | Total Reach (New)     |
      | Reach/Addressable NPI |
      | Addressable NPI Share |

  @todo
  Scenario: Verify View All DMAs button is disabled when no DMAs are available
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    And no NPI list has been uploaded
    When User views the Addressable NPI Geographic Performance component
    Then the View All DMAs button is disabled

  @todo
  Scenario: Verify View All DMAs pop-up displays No data available message when no DMAs exist
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    And no DMAs have been matched
    When User views the Addressable NPI Geographic Performance component
    And User clicks the View All DMAs button
    Then the pop-up displays "No data available" message

  @todo
  Scenario: Verify rapid tab switching between Aligned and Diagnostics tabs loads data correctly
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Addressable NPI Geographic Performance component
    And User rapidly clicks between the Aligned and Diagnostics tabs multiple times
    Then both tabs display correct data without async race conditions
    And no loading errors or data corruption occurs

  @todo
  Scenario: Verify all instances of Seed NPI have been updated to Addressable NPI across dashboard
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the HCP2DTC insights section
    Then all references use "Addressable NPI" terminology
    And no instances of "Seed NPI" remain in labels, tooltips, or subtitles

  @todo
  Scenario: Verify switching from HCP2DTC factor to Ad Engagement factor removes HCP2DTC insights without artifacts
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    And HCP2DTC insights section is displayed
    When User changes the tactic factor to Ad Engagement
    Then HCP2DTC insights section completely disappears
    And no partial display, orphaned elements, or data from previous factor is visible

  @todo
  Scenario: Verify switching from Ad Engagement factor to HCP2DTC factor displays correct insights
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the Ad Engagement factor
    And HCP2DTC insights section is not displayed
    When User changes the tactic factor to HCP2DTC
    Then HCP2DTC insights section appears with correct data
    And no artifacts from Ad Engagement factor are visible

  @todo
  Scenario Outline: Verify small numeric values display with sufficient decimal precision
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the "<metric>" metric with small numeric value
    Then the metric displays "<displayed_value>"
    Examples:
      | metric                        | input_value | displayed_value |
      | Avg TRx (Exposed)             | 0.05        | 0.05            |
      | Avg Reach per Addressable NPI | 0.01        | 0.01            |

  @todo
  Scenario: Verify Coverage Rate formula has not regressed from Beta to GA
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Coverage Rate metric
    Then the metric displays 75%
    And the formula logic is unchanged from Beta version

  @todo
  Scenario: Verify Rx Index formula has not regressed from Beta to GA
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Rx Index metric
    Then the metric displays 2.00
    And the formula logic is unchanged from Beta version