Feature: HCP2DTC Insights Addressable NPI Geographic Performance Component

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"

  @todo
  Scenario Outline: Verify DMA Alignment Rate displays in correct format with coverage metrics
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And "<COVERED_DMAS>" DMAs have patient reach
    And "<TOTAL_NPI_DMAS>" total DMAs exist for seed NPIs
    When User views the Addressable NPI Geographic Performance component
    Then DMA Alignment Rate displays "<COVERED_DMAS> of <TOTAL_NPI_DMAS> (<ALIGNMENT_RATE_PCT>%)"
    Examples:
      | COVERED_DMAS | TOTAL_NPI_DMAS | ALIGNMENT_RATE_PCT |
      | 12           | 15             | 80                 |
      | 8            | 20             | 40                 |
      | 20           | 20             | 100                |

  @todo
  Scenario Outline: Verify Total Unique Reach and Avg Reach per Addressable NPI metrics calculate correctly
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And Total Unique Reach is "<TOTAL_UNIQUE_REACH>"
    And Count of Seed NPIs is "<SEED_NPI_COUNT>"
    When User views the Addressable NPI Geographic Performance component
    Then Total Unique Reach displays "<TOTAL_UNIQUE_REACH>"
    And Avg Reach per Addressable NPI displays "<EXPECTED_AVG_REACH>"
    Examples:
      | TOTAL_UNIQUE_REACH | SEED_NPI_COUNT | EXPECTED_AVG_REACH |
      | 2500               | 100            | 25.00              |
      | 0                  | 100            | 0.00               |
      | 303                | 10             | 30.30              |
      | 100                | 10             | 10.00              |
      | 1                  | 100            | 0.01               |
      | 0                  | 0              | N/A                |

  @todo
  Scenario: Verify Aligned Tab displays all required columns in correct order
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the "Aligned" tab
    Then the Aligned Tab table displays the following columns in order:
      | Column                    |
      | DMA                       |
      | Addressable NPIs          |
      | Total Reach (New)         |
      | Reach/Addressable NPI     |
      | Addressable NPI Share     |

  @todo
  Scenario: Verify Diagnostics Tab displays all required columns in correct order
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the "Diagnostics" tab
    Then the Diagnostics Tab table displays the following columns in order:
      | Column                    |
      | DMA                       |
      | Addressable NPIs          |
      | Reach/Addressable NPI     |
      | Addressable NPI Share     |

  @todo
  Scenario Outline: Verify Aligned Tab displays DMAs with patient reach and correct decimal precision
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the "Aligned" tab
    Then the Aligned Tab displays the following DMA data:
      | DMA     | Addressable NPIs | Total Reach | Reach/Addressable NPI | Addressable NPI Share |
      | <DMA>   | <NPI_COUNT>      | <REACH>     | <REACH_PER_NPI>       | <NPI_SHARE>           |
    And Reach/Addressable NPI displays exactly 2 decimal places
    Examples:
      | DMA     | NPI_COUNT | REACH | REACH_PER_NPI | NPI_SHARE |
      | Boston  | 12        | 450   | 37.50         | 8%        |
      | Chicago | 15        | 600   | 40.00         | 10%       |

  @todo
  Scenario Outline: Verify Diagnostics Tab displays DMAs with zero reach
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the "Diagnostics" tab
    Then the Diagnostics Tab displays the following DMA data:
      | DMA       | Addressable NPIs | Reach/Addressable NPI | Addressable NPI Share |
      | <DMA>     | <NPI_COUNT>      | <REACH_PER_NPI>       | <NPI_SHARE>           |
    And Reach/Addressable NPI displays "<REACH_PER_NPI>" for zero reach DMAs
    Examples:
      | DMA      | NPI_COUNT | REACH_PER_NPI | NPI_SHARE |
      | Portland | 5         | 0.00          | 3%        |
      | Denver   | 8         | 0.00          | 5%        |

  @todo
  Scenario Outline: Verify Geographic tables are sortable by metric columns in ascending and descending order
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the "Aligned" tab
    And User clicks on the "<COLUMN_NAME>" column header to sort ascending
    Then the table sorts by "<COLUMN_NAME>" in ascending order
    When User clicks on the "<COLUMN_NAME>" column header again to sort descending
    Then the table sorts by "<COLUMN_NAME>" in descending order
    Examples:
      | COLUMN_NAME           |
      | Addressable NPIs      |
      | Total Reach (New)     |
      | Reach/Addressable NPI |
      | Addressable NPI Share |

  @todo
  Scenario: Verify View All DMAs pop-up opens and displays all DMAs sorted by Addressable NPI Share descending
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the "View All DMAs" button
    Then the View All DMAs pop-up opens
    And the pop-up displays all DMAs sorted by Addressable NPI Share in descending order
    And the pop-up displays the same columns as the Aligned Tab

  @todo
  Scenario: Verify View All DMAs pop-up is scrollable for large number of DMAs
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And the dashboard contains 100 DMAs
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the "View All DMAs" button
    Then the View All DMAs pop-up opens
    And the pop-up displays a scrollbar
    And User can scroll through all 100 DMAs without truncation

  @todo
  Scenario: Verify Reach/Addressable NPI displays 2 decimal places for whole number values
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Addressable NPI Geographic Performance component
    And User clicks on the "Aligned" tab
    Then all Reach/Addressable NPI values display with exactly 2 decimal places
    And values display as "10.00" not "10" or "10.0"

  @todo
  Scenario: Verify Avg Reach per Addressable NPI displays 2 decimal places for whole number values
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Addressable NPI Geographic Performance component
    Then Avg Reach per Addressable NPI displays with exactly 2 decimal places
    And the value displays as "10.00" not "10" or "10.0"

  @todo
  Scenario: Verify small Reach/NPI values display with sufficient decimal precision
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And Total Unique Reach is "1"
    And Count of Seed NPIs is "100"
    When User views the Avg Reach per Addressable NPI metric
    Then Avg Reach per Addressable NPI displays "0.01"
    And the value is not rounded to "0" or "0.0"

  @todo
  Scenario: Verify rapidly switching between Aligned and Diagnostics tabs loads data correctly
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Addressable NPI Geographic Performance component
    And User rapidly clicks between the "Aligned" and "Diagnostics" tabs multiple times
    Then both tabs display correct data without async race conditions
    And no loading errors or data corruption occurs
    And all metrics display accurately after rapid switching

  @todo
  Scenario: Verify NPI list updates mid-campaign reflect in all geographic performance metrics
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And an NPI list with 50 NPIs has been uploaded
    And the dashboard displays DMA Alignment Rate based on the initial list
    When an updated NPI list with 60 NPIs is uploaded mid-campaign
    And User refreshes the dashboard
    Then DMA Alignment Rate updates to reflect the new NPI list
    And Total Unique Reach updates accordingly
    And Avg Reach per Addressable NPI recalculates with the new data

  @todo
  Scenario: Verify Rx Index at exactly 1.00 boundary renders in Grey color
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And Exposed Rx Share is "25%"
    And Unexposed Rx Share is "25%"
    When User views the Prescription table
    Then Rx Index displays "1.00"
    And Rx Index displays in "Grey" color

  @todo
  Scenario: Verify external users can see HCP2DTC section without internal-only permission toggles
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User is an external user
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the insights section
    Then HCP2DTC insights section is fully visible and accessible
    And no internal-only permission toggles are present
    And Prescriptions component is displayed
    And Addressable NPI Geographic Performance component is displayed

  @todo
  Scenario: Verify HCP2DTC section is completely hidden when switching from HCP2DTC to other factors
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And HCP2DTC insights section is visible
    When User switches the tactic factor to "Ad Engagement"
    Then HCP2DTC insights section is completely absent from the dashboard
    And no partial display or orphaned elements remain
    And no data from the previous HCP2DTC factor is visible

  @todo
  Scenario: Verify HCP2DTC section displays correctly when switching from other factors to HCP2DTC
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "Ad Engagement" factor
    And HCP2DTC insights section is not visible
    When User switches the tactic factor to "HCP2DTC"
    Then HCP2DTC insights section appears with correct data
    And no artifacts from the Ad Engagement factor are visible
    And all metrics display accurately

  @todo
  Scenario: Verify all instances of "Seed NPI" terminology have been updated to "Addressable NPI"
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the HCP2DTC insights section
    Then all labels use "Addressable NPI" terminology
    And all tooltips use "Addressable NPI" terminology
    And all subtitles use "Addressable NPI" terminology
    And no instances of "Seed NPI" remain in the interface

  @todo
  Scenario: Verify Coverage Rate formula has not regressed from Beta to GA
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And 75 NPIs have at least one exposed patient
    And 100 total seed NPIs exist
    When User views the Coverage Rate metric
    Then Coverage Rate displays "75%"
    And the formula logic is unchanged from Beta version

  @todo
  Scenario: Verify Rx Index formula has not regressed from Beta to GA
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And Exposed Rx Share is "40%"
    And Unexposed Rx Share is "20%"
    When User views the Rx Index value
    Then Rx Index displays "2.00"
    And the formula logic is unchanged from Beta version

  @todo
  Scenario: Verify View All DMAs pop-up search functionality filters DMAs correctly
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And the dashboard contains 20 DMAs
    When User clicks on the "View All DMAs" button
    And User enters a search term in the pop-up search field
    Then the pop-up filters to show only matching DMAs
    When User clears the search text
    Then all 20 DMAs are restored and visible without requiring additional clicks

  @todo
  Scenario: Verify View All DMAs pop-up displays "No data available" message when no DMAs exist
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And no NPI list has been uploaded
    When User views the Addressable NPI Geographic Performance component
    Then the "View All DMAs" button is disabled
    Or the View All DMAs pop-up displays "No data available" message