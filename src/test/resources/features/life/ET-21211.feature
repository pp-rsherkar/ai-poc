Feature: Campaign Debugger - Tactic Debugger Deal View and Filter by Deal ID

  1. Adds a Deal View toggle alongside the existing Tactic View in the Tactic Debugger, showing a per-deal performance table for tactics that target deals or curated markets.
  2. Adds Deal ID drill-in filtering from the Deal View table into the sequential and isolated debugger views, with Curated Market Deal IDs never surfaced.
  3. Covers the Today/Yesterday date-range model (superseding the epic's stale "Last 24 hours" text per the 2025-08-26 comment) and permission-gated visibility.
  4. This feature already shipped in Nov-2025-portal; this file adds first-time feature-file coverage for it since none previously existed in this repository.
  5. Each scenario walks a single continuous pass through the Tactic Debugger, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User navigates to Tactic Debugger for a tactic that targets a deal or curated market

  @todo
  Scenario: Verify the Deal View table, metrics, and date-range workflow
    Given the tactic does not target any deal or curated market
    Then the Deal View toggle is disabled with the tooltip "Deal View is only available in tactics that have targeted a deal or curated market."
    Given the tactic targets a deal or curated market
    Then a "Deal View" toggle is displayed alongside the existing "Tactic View" toggle
    When User selects Deal View
    Then Deal View lists every deal targeted in the tactic with columns for Deal Name, Estimated Avails, Eligible Requests, Placed Bids, Win Rate, Impressions, Spend, and % of Tactic Spend
    And a final totals row summarizes the columns across all deals in the tactic
    And hovering the Eligible Requests column header shows the tooltip "Requests eligible for bidding after all tactic and deal filters have been applied."
    And hovering the totals row for Deals/Curated Market shows the tooltip "Totals for all deals and markets assigned to this tactic."
    When User selects the "Today" date range
    Then the Impressions column renders as a number, and the Eligible Requests column is retitled "Eligible Requests in Last 24 Hours" with values reflecting the last 24 hours
    When User selects the "Yesterday" date range
    Then the Impressions column renders as a number, and the Eligible Requests column is retitled "Eligible Requests in Last 24 Hours" with values reflecting the last 24 hours
    When User selects the "Last 7 Days" date range
    Then the Impressions column renders as a sparkline
    When User selects the "Last 30 Days" date range
    Then the Impressions column renders as a sparkline
    And all Deal View metrics, including the totals row, recalculate for the newly selected date range
    Then the Eligible Requests percentage for each deal reflects a correct, non-zero calculated value where applicable
    # Regression anchor: PROD-14634 - percentage value was previously displayed as zero for all deals

  @todo
  Scenario: Verify the Deal ID drill-in, navigation, and visibility-scoping workflow
    When User selects Deal View
    And User clicks the expand icon on a Deal Group row
    Then the Deal Group expands to reveal its individual Deal IDs, and the debugger is not filtered to any deal as a result of the expand action
    Given a Curated Market is present among the targeted deals
    Then the Curated Market cannot be expanded, and no individual Deal ID belonging to it appears in the UI or in API responses
    When User clicks a deal row
    Then the Tactic Debugger's sequential (funnel) view is filtered to that Deal ID
    And the debugger view for that individual deal renders correctly
    # Regression anchor: PROD-14540 - debugger view was previously missing under an individual deal
    When User navigates from the sequential view to the isolated view for the filtered deal
    Then the isolated view opens successfully
    # Regression anchor: PROD-14641 - navigation from Deal View sequential mode to isolated view previously failed
    And the isolated view does not display the Tactic View button or the Time Frame control from the sequential view
    # Regression anchor: PROD-14640 - isolated view previously showed Tactic View button and Time Frame incorrectly
    When User clicks "Explore Deal Across Tactics"
    Then a Cancel button is present on the panel
    # Regression anchor: PROD-14583 - panel was previously missing a Cancel button
    When User clicks the "Explore Deal Across Tactics" link
    Then User is navigated to the Associated Tactics / Deal Performance view for that Deal ID
    Given User is on the Associated Tactics section of the Standalone PMP Inventory screen
    When User clicks the link into the debugger for a tactic and deal
    Then the Tactic Debugger opens pre-filtered by that tactic and deal
    Given User does not have the "Tactic Deal Debugger" permission
    Then the Deal View toggle and all Deal View references are hidden from the Tactic Debugger
    # Regression anchor: PROD-14545 - timeframe filter previously differed for users without this permission

  @todo
  Scenario: Verify Deal View state persists across a visit to the Creative Test Page
    Given User has selected Deal View and applied a Deal ID filter
    When User navigates to the Creative Test Page and returns to the Tactic Debugger
    Then the previously applied Deal View filter and data are still present
    # Regression anchor: PROD-14515 - debugger data previously disappeared after visiting the Creative Test Page
