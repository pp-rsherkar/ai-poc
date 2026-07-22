Feature: DTC Clinical Insights - HCP2DTC AO Insights and Seed NPI Geographic Performance

  1. Conditionally displays the AO Insights section (Prescriptions + Seed NPI Geographic Performance) on DTC Clinical Insights only for tactics with the HCP2DTC factor enabled, keyed off the latest uploaded NPI list version.
  2. Adds Seed NPI Geographic Performance metrics: Seed NPIs, Coverage Rate, Avg TRx per Exposed Audience, NRx/NBRx Exposed and Unexposed Rx Share, Rx Index, DMA Alignment Rate, Total Unique Reach, and Avg Reach per NPI.
  3. Adds Aligned and Diagnostics DMA tabs plus a "View All DMAs" pop-up, and extends the existing dashboard download to include the new widget data.
  4. Per Comment 512359, AQ Test and Control tiles are hidden in the production build post-deploy; Time Frame selector behavior on the new tiles and the View All DMAs download control are out of scope for this cycle.
  5. Each scenario walks a single continuous pass through the DTC Clinical Insights dashboard, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: TC_QA-1553_1, TC_QA-1553_2, TC_QA-1553_3
  @todo
  Scenario: Verify AO Insights section conditional display and HCP2DTC factor toggle workflow
    # Framework Gap: Requires a DTC Clinical Insights navigation step in LifeSteps.java and a DtcClinicalInsights page object under pages/life (LineItemMediaInsights.java currently has no navigation or assertion methods)
    Given User selects a DTC tactic with the HCP2DTC factor enabled and the latest NPI list applied
    When User opens DTC Clinical Insights for the tactic
    Then the AO Insights section is displayed with the Prescriptions and Seed NPI Geographic Performance components
    Given User opens DTC Clinical Insights for a tactic without the HCP2DTC factor
    Then the AO Insights section and its widgets are not displayed
    Given User adds the HCP2DTC factor to a tactic and reloads DTC Clinical Insights
    Then the AO Insights section is displayed
    When User removes the HCP2DTC factor from the tactic and reloads DTC Clinical Insights
    Then the AO Insights section is hidden and no stale widgets remain
    # Regression anchor: HT-5067 / HT-4704 - conditional display of AO Insights section on factor toggle

  # Source: TC_QA-1553_4, TC_QA-1553_5, TC_QA-1553_7, TC_QA-1553_8, TC_QA-1553_10, TC_QA-1553_12, TC_QA-1553_13, TC_QA-1553_14, TC_QA-1553_15, TC_QA-1553_18, TC_QA-1553_19, TC_QA-1553_20
  @todo
  Scenario: Verify Seed NPI Geographic Performance summary metrics formulas and tooltips
    Given User opens DTC Clinical Insights for a DTC tactic with the HCP2DTC factor enabled and an uploaded NPI list
    # Framework Gap: Requires metric-card getter methods (Seed NPIs, Coverage Rate, Avg TRx, Rx Share, Rx Index, DMA Alignment Rate, Total Unique Reach, Avg Reach per NPI) in the DtcClinicalInsights page object
    Then the Seed NPIs count equals the matched addressable NPIs from the latest NPI list version
    When User uploads a newer NPI list version for the same tactic and reloads DTC Clinical Insights
    Then the metrics use the latest NPI list version and the historical version is not used
    Then the Coverage Rate equals the NPIs with at least one exposed patient divided by total seed NPIs times 100, with the progress bar reflecting that percentage
    When User hovers over the Coverage Rate metric
    Then the tooltip reads "Percentage of seed NPIs where at least one patient was reached."
    Then the Avg TRx per Exposed Audience equals Total TRx divided by exposed patients, with tooltip and subtitle present
    Then the Metrics Breakdown table displays NRx and NBRx rows
    When User hovers over the NRx and NBRx rows
    Then the NRx tooltip reads "new prescriptions" and the NBRx tooltip reads "new-to-brand prescriptions"
    Then the Exposed Rx Share equals exposed patients with NRx or NBRx divided by exposed_trx times 100, with the bar scaled to the exposed total
    Then the Unexposed Rx Share equals unexposed patients with NRx or NBRx divided by unexposed_trx times 100, with the bar scaled to the unexposed total
    Then the Rx Index equals the Exposed Rx Share divided by the Unexposed Rx Share, with a baseline value of 1
    And an Rx Index above 1 renders green
    Then the DMA Alignment Rate displays as covered DMAs of total DMAs with the rounded percentage in parentheses
    Then the Total Unique Reach equals the distinct patients of seed NPIs reached
    Then the Avg Reach per NPI equals the Total Unique Reach divided by seed NPIs

  # Source: TC_QA-1553_6, TC_QA-1553_9, TC_QA-1553_11, TC_QA-1553_16, TC_QA-1553_17, TC_QA-1553_21
  @todo
  Scenario Outline: Verify Seed NPI Geographic Performance boundary and divide-by-zero handling for "<Metric>"
    Given User opens DTC Clinical Insights for a DTC tactic with "<Test Data>"
    # Framework Gap: Boundary and zero-state assertions require dedicated verification methods per metric in the DtcClinicalInsights page object
    Then "<Metric>" renders as "<Expected Behavior>"
    Examples:
      | Metric               | Test Data                         | Expected Behavior                                  |
      | Seed NPIs            | zero matched seed NPIs            | Zero-state handled with no crash or NaN            |
      | Coverage Rate 0%     | zero NPIs reached                 | Renders 0% with an empty bar fill                  |
      | Coverage Rate 100%   | all NPIs reached                  | Renders 100% with a full bar fill                  |
      | Avg TRx              | zero exposed patients             | Divide-by-zero handled with no NaN or Infinity     |
      | Rx Index color at 1  | exposed and unexposed share equal | Renders grey at baseline (index equal to 1)        |
      | Rx Index denominator | unexposed Rx Share equal to zero  | Division handled gracefully with a defined display |
      | DMA Alignment Rate   | totalNpiDmas equal to zero        | No divide error; a defined zero-state is shown     |

  # Source: TC_QA-1553_22, TC_QA-1553_23, TC_QA-1553_24, TC_QA-1553_25, TC_QA-1553_26, TC_QA-1553_27
  @todo
  Scenario: Verify Aligned and Diagnostics DMA tab workflow and the View All DMAs pop-up
    Given User opens DTC Clinical Insights for a DTC tactic with covered and zero-reach DMAs
    # Framework Gap: Requires Aligned tab, Diagnostics tab, and View All DMAs pop-up methods in the DtcClinicalInsights page object
    When User clicks the Aligned tab
    Then the Aligned tab shows only covered DMAs with columns DMA, Seed NPIs, Reach per NPI, and Seed NPI Share
    And the Reach per NPI equals the rounded value of unique DMA reach divided by seed NPIs
    And the Seed NPI Share equals DMA NPIs divided by total NPIs times 100
    When User clicks the Diagnostics tab
    Then the Diagnostics tab lists zero-reach DMAs with Reach per NPI equal to 0
    And a high Seed NPI Share DMA with zero reach is flagged for investigation priority
    And a DMA with 1 seed NPI and very low but nonzero reach is classified per the implemented rule
    # Ambiguity: classification boundary between "very low" and "zero reach" is not fully specified; record which tab the low-reach DMA appears in
    When User clicks "View All DMAs"
    Then the pop-up columns match the Aligned tab, the pop-up is scrollable, and rows are sorted descending by Seed NPI Share

  # Source: TC_QA-1553_29, TC_QA-1553_30
  @todo
  Scenario: Verify Seed NPI Geographic Performance widget download workflow including lite-access users
    Given User opens DTC Clinical Insights with the AO Insights section and Seed NPI Geographic Performance widget rendered
    # Framework Gap: Requires a download-trigger step and downloaded-file content assertion in the DtcClinicalInsights page object
    When User clicks Download Report
    Then the downloaded file contains the Aligned and Diagnostic results along with the new widget data
    Given User logs in as an NPI-list "lite" access user
    When User clicks Download Report on DTC Clinical Insights
    Then the download succeeds with no permission error
    # Regression anchor: HT-6140 (OPEN) - lite-access user download permission

  # Source: TC_QA-1553_31, TC_QA-1553_32
  @todo
  Scenario: Verify widget first-load population and cross-check of new metrics against source data
    Given User opens DTC Clinical Insights for a valid HCP2DTC tactic with data
    Then the AO Insights section and Seed NPI Geographic Performance widget load and populate on first load with no blank or no-data state
    # Regression anchor: recurring not-loading defect pattern (29-ticket history)
    When User independently computes the expected Coverage Rate, Rx Index, and DMA and reach metrics from source data
    Then the displayed metrics match the source data with no zero, low, or discrepancy
    # Regression anchor: HT-5956 / HT-5955 / HT-4860 - metric discrepancy against source data

  # Source: TC_QA-1553_33
  @todo
  Scenario: Verify AQ Test and Control tiles are hidden in the production build
    Given the DTC Clinical Insights change is deployed to the production build
    # Framework Gap: Requires a post-deploy tile-visibility assertion method in the DtcClinicalInsights page object
    When User opens DTC Clinical Insights for a tactic in the production build
    Then the AQ Test and Control tiles are removed or hidden as agreed
