Feature: HCP2DTC Insights Display Logic and Prescriptions Component

  @todo
  Scenario: Verify HCP2DTC insights section is visible when tactic uses HCP2DTC factor
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    Then HCP2DTC insights section is displayed on the dashboard
    And Prescriptions component is visible
    And Addressable NPI Geographic Performance component is visible

  @todo
  Scenario: Verify HCP2DTC insights section is completely hidden when tactic uses non-HCP2DTC factor
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the AO Insights dashboard
    And User selects a tactic configured with the Ad Engagement factor
    Then HCP2DTC insights section is completely absent from the dashboard
    And No partial display or disabled state is shown

  @todo
  Scenario Outline: Verify Prescriptions component summary metrics display correctly
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Prescriptions component
    Then the "<metric>" metric displays the value "<expected_value>"
    And the metric is formatted correctly as "<format>"
    Examples:
      | metric                    | expected_value | format                |
      | Addressable NPIs (Count)  | 75             | whole number          |
      | Coverage Rate             | 75%            | percentage with bar   |
      | Avg TRx (Exposed)         | 25.00          | 2 decimal places      |

  @todo
  Scenario: Verify Coverage Rate progress bar fills to correct percentage
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Coverage Rate metric
    Then the progress bar fills to exactly 60% of its total width
    And the metric displays 60%

  @todo
  Scenario: Verify Coverage Rate tooltip displays correct specification text
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User hovers over the Coverage Rate metric
    Then tooltip displays "Percentage of seed NPIs where at least one patient was reached."

  @todo
  Scenario Outline: Verify Avg TRx (Exposed) displays with correct decimal precision
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Avg TRx (Exposed) metric
    Then the metric displays "<displayed_value>"
    Examples:
      | input_trx_volume | input_patients | displayed_value |
      | 500              | 20             | 25.00           |
      | 100              | 10             | 10.00           |
      | 303              | 10             | 30.30           |
      | 5                | 1              | 5.00            |

  @todo
  Scenario: Verify Avg TRx (Exposed) displays N/A when no exposed patients exist
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Avg TRx (Exposed) metric with zero exposed patients
    Then the metric displays "N/A" or "0.00"

  @todo
  Scenario: Verify Prescription table contains all required columns in correct order
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Prescriptions table
    Then the table displays the following columns in order:
      | Column             |
      | Metric             |
      | Exposed Rx Share   |
      | Unexposed Rx Share |
      | Rx Index           |

  @todo
  Scenario Outline: Verify Prescription table displays NRx and NBRx metric rows with correct values
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Prescriptions table
    Then the "<metric>" row displays the following values:
      | Column             | Value           |
      | Metric             | <metric>        |
      | Exposed Rx Share   | <exposed_share> |
      | Unexposed Rx Share | <unexposed_share> |
      | Rx Index           | <rx_index>      |
    Examples:
      | metric | exposed_share | unexposed_share | rx_index |
      | NRx    | 35%           | 15%             | 2.33     |
      | NBRx   | 28%           | 22%             | 1.27     |

  @todo
  Scenario Outline: Verify Exposed and Unexposed Rx Share displays as percentage with progress bar
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the "<share_type>" in the Prescriptions table
    Then the metric displays "<percentage_value>"
    And the progress bar fills to "<bar_width>"
    Examples:
      | share_type         | percentage_value | bar_width |
      | Exposed Rx Share   | 30%              | 30%       |
      | Exposed Rx Share   | 45%              | 45%       |
      | Unexposed Rx Share | 20%              | 20%       |

  @todo
  Scenario Outline: Verify Rx Index color coding based on value threshold
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Rx Index metric with value "<rx_index_value>"
    Then the metric displays "<rx_index_value>" in "<color>" color
    And the metric displays exactly 2 decimal places
    Examples:
      | rx_index_value | color |
      | 2.00           | Green |
      | 0.50           | Grey  |
      | 1.00           | Grey  |
      | 3.75           | Green |

  @todo
  Scenario: Verify Rx Index displays N/A when Unexposed Share is zero
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    When User views the Rx Index metric with zero unexposed share
    Then the metric displays "N/A" or "∞"

  @todo
  Scenario: Verify Addressable NPIs count updates when NPI list is updated mid-campaign
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    And the Addressable NPIs metric displays 75
    When User uploads a new NPI list with 85 NPIs
    And User refreshes the dashboard
    Then the Addressable NPIs metric updates to 85

  @todo
  Scenario: Verify external users can see HCP2DTC section without internal-only permission toggles
    Given This scenario will be executed in the "Pre-release" environment as a "External User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the AO Insights dashboard
    And User selects a tactic configured with the HCP2DTC factor
    Then HCP2DTC insights section is fully visible and accessible
    And no internal-only permission toggles are present