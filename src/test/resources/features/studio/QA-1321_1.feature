Feature: HCP2DTC Insights Display Logic and Prescriptions Component

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"

  @todo
  Scenario: Verify HCP2DTC insights section is visible when tactic uses HCP2DTC factor
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the insights section
    Then HCP2DTC insights section is displayed on the dashboard
    And Prescriptions component is visible
    And Addressable NPI Geographic Performance component is visible

  @todo
  Scenario: Verify HCP2DTC insights section is hidden when tactic uses non-HCP2DTC factor
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "Ad Engagement" factor
    When User views the insights section
    Then HCP2DTC insights section is completely absent from the dashboard
    And Prescriptions component is not visible
    And Addressable NPI Geographic Performance component is not visible

  @todo
  Scenario Outline: Verify Addressable NPIs count displays matched NPIs from latest uploaded list
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And An NPI list with "<INITIAL_NPI_COUNT>" NPIs has been uploaded
    When User views the Prescriptions component
    Then Addressable NPIs metric displays "<INITIAL_NPI_COUNT>"
    When An updated NPI list with "<UPDATED_NPI_COUNT>" NPIs is uploaded mid-campaign
    And User refreshes the dashboard
    Then Addressable NPIs metric updates to "<UPDATED_NPI_COUNT>"
    Examples:
      | INITIAL_NPI_COUNT | UPDATED_NPI_COUNT |
      | 75                | 85                |
      | 50                | 60                |

  @todo
  Scenario Outline: Verify Coverage Rate formula calculates correctly with various data states
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And "<NPIS_WITH_EXPOSED>" NPIs have at least one exposed patient
    And "<TOTAL_SEED_NPIS>" total seed NPIs exist
    When User views the Coverage Rate metric in the Prescriptions component
    Then Coverage Rate displays "<EXPECTED_COVERAGE_RATE>"
    And Coverage Rate progress bar fills to "<PROGRESS_BAR_PERCENTAGE>"
    And Coverage Rate tooltip displays "Percentage of seed NPIs where at least one patient was reached."
    Examples:
      | NPIS_WITH_EXPOSED | TOTAL_SEED_NPIS | EXPECTED_COVERAGE_RATE | PROGRESS_BAR_PERCENTAGE |
      | 75                | 100             | 75%                    | 75%                     |
      | 100               | 100             | 100%                   | 100%                    |
      | 0                 | 100             | 0%                     | 0%                      |
      | 0                 | 0               | N/A                    | 0%                      |

  @todo
  Scenario Outline: Verify Avg TRx (Exposed) formula calculates and displays with correct decimal precision
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And Total TRx Volume is "<TOTAL_TRX_VOLUME>"
    And Number of Exposed Patients is "<EXPOSED_PATIENTS>"
    When User views the Avg TRx (Exposed) metric in the Prescriptions component
    Then Avg TRx (Exposed) displays "<EXPECTED_AVG_TRX>"
    Examples:
      | TOTAL_TRX_VOLUME | EXPOSED_PATIENTS | EXPECTED_AVG_TRX |
      | 2500             | 100              | 25.00            |
      | 0                | 100              | 0.00             |
      | 303              | 10               | 30.30            |
      | 50               | 10               | 5.00             |
      | 1                | 100              | 0.01             |
      | 0                | 0                | N/A              |

  @todo
  Scenario: Verify Prescription table displays all required columns in correct order
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Prescriptions table
    Then the table displays the following columns in order:
      | Column             |
      | Metric             |
      | Exposed Rx Share   |
      | Unexposed Rx Share |
      | Rx Index           |

  @todo
  Scenario Outline: Verify Prescription table displays NRx and NBRx metrics with correct values and color coding
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Prescriptions table
    Then the table displays "<METRIC>" row with the following data:
      | Field                  | Value                |
      | Metric                 | <METRIC>             |
      | Exposed Rx Share       | <EXPOSED_RX_SHARE>   |
      | Unexposed Rx Share     | <UNEXPOSED_RX_SHARE> |
      | Rx Index               | <RX_INDEX>           |
      | Rx Index Color         | <RX_INDEX_COLOR>     |
    And Exposed Rx Share progress bar fills to "<EXPOSED_PROGRESS>"
    And Unexposed Rx Share progress bar fills to "<UNEXPOSED_PROGRESS>"
    Examples:
      | METRIC | EXPOSED_RX_SHARE | UNEXPOSED_RX_SHARE | RX_INDEX | RX_INDEX_COLOR | EXPOSED_PROGRESS | UNEXPOSED_PROGRESS |
      | NRx    | 35%              | 15%                | 2.33     | Green          | 35%              | 15%                |
      | NBRx   | 28%              | 22%                | 1.27     | Green          | 28%              | 22%                |

  @todo
  Scenario Outline: Verify Rx Index color coding based on threshold values
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And Exposed Rx Share is "<EXPOSED_SHARE>"
    And Unexposed Rx Share is "<UNEXPOSED_SHARE>"
    When User views the Rx Index value in the Prescriptions table
    Then Rx Index displays "<RX_INDEX_VALUE>"
    And Rx Index displays in "<RX_INDEX_COLOR>" color
    Examples:
      | EXPOSED_SHARE | UNEXPOSED_SHARE | RX_INDEX_VALUE | RX_INDEX_COLOR |
      | 40%           | 20%             | 2.00           | Green          |
      | 25%           | 25%             | 1.00           | Grey           |
      | 15%           | 30%             | 0.50           | Grey           |
      | 0%            | 20%             | 0.00           | Grey           |

  @todo
  Scenario Outline: Verify Exposed and Unexposed Rx Share display as percentages with progress bars
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And Total Brand Drug Prescriptions is "<TOTAL_PRESCRIPTIONS>"
    And Exposed Prescriptions is "<EXPOSED_PRESCRIPTIONS>"
    And Unexposed Prescriptions is "<UNEXPOSED_PRESCRIPTIONS>"
    When User views the Prescription table
    Then Exposed Rx Share displays "<EXPOSED_SHARE_PERCENTAGE>"
    And Unexposed Rx Share displays "<UNEXPOSED_SHARE_PERCENTAGE>"
    And Exposed Rx Share progress bar fills to "<EXPOSED_PROGRESS>"
    And Unexposed Rx Share progress bar fills to "<UNEXPOSED_PROGRESS>"
    Examples:
      | TOTAL_PRESCRIPTIONS | EXPOSED_PRESCRIPTIONS | UNEXPOSED_PRESCRIPTIONS | EXPOSED_SHARE_PERCENTAGE | UNEXPOSED_SHARE_PERCENTAGE | EXPOSED_PROGRESS | UNEXPOSED_PROGRESS |
      | 1000                | 300                   | 700                     | 30%                      | 70%                        | 30%              | 70%                |
      | 1000                | 500                   | 500                     | 50%                      | 50%                        | 50%              | 50%                |
      | 0                   | 0                     | 0                       | N/A                      | N/A                        | 0%               | 0%                 |

  @todo
  Scenario: Verify Rx Index displays exactly 2 decimal places for all values
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    When User views the Prescription table
    Then all Rx Index values display with exactly 2 decimal places
    And Rx Index does not display as "3" or "3.0" but as "3.00"

  @todo
  Scenario: Verify small TRx numbers display with sufficient decimal precision
    Given "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the "HCP2DTC" factor
    And Total TRx Volume is "5"
    And Number of Exposed Patients is "100"
    When User views the Avg TRx (Exposed) metric
    Then Avg TRx (Exposed) displays "0.05"
    And the value is not rounded to "0" or "0.1"