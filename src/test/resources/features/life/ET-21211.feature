Feature: Campaign Debugger - Tactic Debugger Deal View and Filter by Deal ID

  1. Adds a Deal View toggle alongside the existing Tactic View in the Tactic Debugger, showing a per-deal performance table for tactics that target deals or curated markets.
  2. Adds Deal ID drill-in filtering from the Deal View table into the sequential and isolated debugger views, with Curated Market Deal IDs never surfaced.
  3. Covers the Today/Yesterday date-range model (superseding the epic's stale "Last 24 hours" text per the 2025-08-26 comment), permission-gated visibility, and regression anchors drawn from this feature's closed UAT bugs.
  4. This feature already shipped in Nov-2025-portal; this file adds first-time feature-file coverage for it since none previously existed in this repository.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User navigates to Tactic Debugger for a tactic that targets a deal or curated market

  @todo
  Scenario: Deal View toggle is available for tactics that target a deal or curated market
    Then a "Deal View" toggle is displayed alongside the existing "Tactic View" toggle
    When User selects Deal View
    Then Deal View lists every deal targeted in the tactic with columns for Deal Name, Estimated Avails, Eligible Requests, Placed Bids, Win Rate, Impressions, Spend, and % of Tactic Spend
    And a final totals row summarizes the columns across all deals in the tactic

  @todo
  Scenario: Deal View is disabled with an explanatory tooltip for tactics with no targeted deal
    Given the tactic does not target any deal or curated market
    Then the Deal View toggle is disabled
    And hovering the disabled toggle shows the tooltip "Deal View is only available in tactics that have targeted a deal or curated market."

  @todo
  Scenario Outline: Impressions render as a sparkline or a single number depending on the selected date range
    When User selects Deal View
    And User selects the "<DATE_RANGE>" date range
    Then the Impressions column renders as a "<RENDER_MODE>"
    Examples:
      | DATE_RANGE   | RENDER_MODE |
      | Today        | number      |
      | Yesterday    | number      |
      | Last 7 Days  | sparkline   |
      | Last 30 Days | sparkline   |

  @todo
  Scenario Outline: Selecting Today or Yesterday relabels Eligible Requests to Last 24 Hours values
    When User selects Deal View
    And User selects the "<DATE_RANGE>" date range
    Then the Eligible Requests column is retitled "Eligible Requests in Last 24 Hours"
    And its values reflect the last 24 hours, per the 2025-08-26 reconciliation of the epic's date-range model
    Examples:
      | DATE_RANGE |
      | Today      |
      | Yesterday  |

  @todo
  Scenario: Selecting a date range recalculates all Deal View metrics
    When User selects Deal View
    And User changes the selected date range from "Last 7 Days" to "Last 30 Days"
    Then all Deal View metrics, including the totals row, recalculate for the newly selected date range

  @todo
  Scenario: Clicking a deal row filters the debugger to that deal in both sequential and isolated views
    When User selects Deal View
    And User clicks a deal row
    Then the Tactic Debugger's sequential (funnel) view is filtered to that Deal ID
    And the isolated view is also filtered to that Deal ID
    And no Deal ID is selected by default before a row is clicked

  @todo
  Scenario: Expanding a Deal Group reveals its Deal IDs without triggering deal drill-in
    When User selects Deal View
    And User clicks the expand icon on a Deal Group row
    Then the Deal Group expands to reveal its individual Deal IDs
    And the debugger is not filtered to any deal as a result of the expand action

  @todo
  Scenario: Curated Market Deal IDs are never surfaced in the Deal View
    When User selects Deal View
    And a Curated Market is present among the targeted deals
    Then the Curated Market cannot be expanded
    And no individual Deal ID belonging to that Curated Market appears in the UI or in API responses

  @todo
  Scenario: Explore Deal Across Tactics links to the Deal Performance view for the filtered Deal ID
    When User selects Deal View
    And User clicks a deal row to filter by that Deal ID
    Then an "Explore Deal Across Tactics" button is displayed
    When User clicks "Explore Deal Across Tactics"
    Then User is navigated to the Associated Tactics / Deal Performance view for that Deal ID

  @todo
  Scenario: Eligible Requests and Totals tooltips reflect the renamed labels
    When User selects Deal View
    Then hovering the Eligible Requests column header shows the tooltip "Requests eligible for bidding after all tactic and deal filters have been applied."
    And hovering the totals row for Deals/Curated Market shows the tooltip "Totals for all deals and markets assigned to this tactic."

  @todo
  Scenario: Standalone PMP Inventory links into a pre-filtered debugger view
    Given User is on the Associated Tactics section of the Standalone PMP Inventory screen
    When User clicks the link into the debugger for a tactic and deal
    Then the Tactic Debugger opens pre-filtered by that tactic and deal

  @todo
  Scenario: Deal View and all references to it are hidden without the Tactic Deal Debugger permission
    Given User does not have the "Tactic Deal Debugger" permission
    Then the Deal View toggle and all Deal View references are hidden from the Tactic Debugger
    # Regression anchor: PROD-14545 - timeframe filter previously differed for users without this permission

  @todo
  Scenario: Deal View renders correctly under an individual deal drill-in
    When User selects Deal View
    And User clicks a deal row
    Then the debugger view for that individual deal renders correctly
    # Regression anchor: PROD-14540 - debugger view was previously missing under an individual deal

  @todo
  Scenario: Eligible Requests percentage displays a correct non-zero value
    When User selects Deal View
    Then the Eligible Requests percentage for each deal reflects a correct, non-zero calculated value where applicable
    # Regression anchor: PROD-14634 - percentage value was previously displayed as zero for all deals

  @todo
  Scenario: Isolated view controls match the isolated view, not the Tactic View chrome
    When User selects Deal View
    And User clicks a deal row to open the isolated view
    Then the isolated view does not display the Tactic View button or the Time Frame control from the sequential view
    # Regression anchor: PROD-14640 - isolated view previously showed Tactic View button and Time Frame incorrectly

  @todo
  Scenario: Navigating from Deal View's sequential mode to the isolated view succeeds
    When User selects Deal View in sequential mode
    And User attempts to navigate to the isolated view for a filtered deal
    Then the isolated view opens successfully
    # Regression anchor: PROD-14641 - navigation from Deal View sequential mode to isolated view previously failed

  @todo
  Scenario: The Explore Deal Across Tactics panel includes a Cancel control
    When User selects Deal View
    And User clicks a deal row to filter by that Deal ID
    And User opens the "Explore Deal Across Tactics" panel
    Then a Cancel button is present on the panel
    # Regression anchor: PROD-14583 - panel was previously missing a Cancel button

  @todo
  Scenario: Debugger state persists across a visit to the Creative Test Page
    Given User has an active Deal View filter in the Tactic Debugger
    When User navigates to the Creative Test Page and returns to the Tactic Debugger
    Then the previously applied Deal View filter and data are still present
    # Regression anchor: PROD-14515 - debugger data previously disappeared after visiting the Creative Test Page
