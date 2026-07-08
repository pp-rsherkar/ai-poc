Feature: Campaign Debugger - Tactic Debugger Deal Summary View and Filter by Deal ID
  1. Deal View toggle alongside Tactic View, summarizing per-deal performance for a tactic.
  2. Deal ID selection that filters the sequential and isolated debugger views.
  3. Curated Market Deal ID suppression and permission-gated visibility.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Campaign Debugger for a tactic that targets one or more deals

  @todo
  Scenario: Deal View toggle is available alongside Tactic View for a tactic that targets deals
    When User views the Tactic Debugger for a tactic that targets a deal or curated market
    Then a "Deal View" toggle should be available alongside the existing "Tactic View" toggle

  @todo
  Scenario: Deal View lists every targeted deal with the required columns and a totals row
    When User switches to Deal View
    Then every deal targeted by the tactic should be listed with columns Deal Name, Estimated Avails, Eligible Requests, Placed Bids, Win Rate, Impressions, Spend, and percent of Tactic Spend
    And a final row should display totals across all deals in the tactic

  @todo
  Scenario Outline: Impressions renders as a number for Today/Yesterday and as a sparkline for 7/30 day ranges
    When User switches to Deal View
    And User selects "<DATE_RANGE>" as the date range
    Then the Impressions column should render as "<RENDER_TYPE>"
    Examples:
      | DATE_RANGE   | RENDER_TYPE |
      | Today        | a number    |
      | Yesterday    | a number    |
      | Last 7 Days  | a sparkline |
      | Last 30 Days | a sparkline |

  @todo
  Scenario Outline: Selecting a date range recalculates Deal View metrics
    When User switches to Deal View
    And User selects "<DATE_RANGE>" as the date range
    Then all Deal View metrics should recalculate for the selected period
    Examples:
      | DATE_RANGE   |
      | Today        |
      | Yesterday    |
      | Last 7 Days  |
      | Last 30 Days |

  @todo
  Scenario Outline: Eligible Requests shows Last 24 Hours values and is retitled when Today or Yesterday is selected
    When User switches to Deal View
    And User selects "<DATE_RANGE>" as the date range
    Then the Eligible Requests column should be retitled "Eligible Requests in Last 24 Hours"
    And its values should reflect the last 24 hours rather than the calendar period
    Examples:
      | DATE_RANGE |
      | Today      |
      | Yesterday  |

  @todo
  Scenario: Clicking a deal row filters the sequential and isolated debugger views by that deal
    When User switches to Deal View
    And User clicks on a deal row
    Then the sequential debugger view should filter with respect to that deal
    And the isolated debugger view should filter with respect to that deal

  @todo
  Scenario: Expanding a Deal Group reveals its Deal IDs without navigating into an individual deal
    When User switches to Deal View
    And User clicks the expand/collapse icon on a Deal Group
    Then the Deal Group's individual Deal IDs should be revealed
    And clicking the expand/collapse icon should not navigate into an individual deal

  @todo
  Scenario: Curated Market Deal IDs are never surfaced in the UI
    When User switches to Deal View for a tactic that targets a Curated Market
    Then the Curated Market should not be expandable
    And no individual Deal ID belonging to the Curated Market should be displayed anywhere in the UI

  @todo
  Scenario: No deal is selected by default in Deal View
    When User switches to Deal View
    Then no Deal ID should be selected by default

  @todo
  Scenario: The Explore Deal Across Tactics button appears and links to the Associated Tactics view when a deal is selected
    When User switches to Deal View
    And User filters the debugger by a specific Deal ID
    Then an "Explore Deal Across Tactics" button should be displayed
    And clicking it should link to the Associated Tactics / Deal Performance view for that Deal ID

  @todo
  Scenario: Eligible Requests tooltip and label reflect the renamed terminology
    When User switches to Deal View
    Then the column previously labeled "Matching Targeting" should be labeled "Eligible Requests"
    And its tooltip should read "Requests eligible for bidding after all tactic and deal filters have been applied."

  @todo
  Scenario: Totals tooltip describes totals for all deals and markets assigned to the tactic
    When User switches to Deal View
    And User hovers the tooltip on the Totals row for Deals/Curated Market
    Then the tooltip should read "Totals for all deals and markets assigned to this tactic."

  @todo
  Scenario: Navigating from Standalone PMP Inventory's Associated Tactics opens the debugger pre-filtered by tactic and deal
    Given User is on the Standalone PMP Inventory screen's Associated Tactics section
    When User clicks the link to a specific tactic and deal
    Then the Tactic Debugger should open pre-filtered by that tactic and deal

  @todo
  Scenario: Deal View is disabled with an explanatory tooltip for a tactic that targets no deals or curated markets
    Given the tactic does not target any deal or curated market
    When User views the Tactic Debugger for that tactic
    Then the "Deal View" toggle should be disabled
    And its tooltip should read "Deal View is only available in tactics that have targeted a deal or curated market."

  @todo
  Scenario: Deal View and all references to it are hidden without the Tactic Deal Debugger permission
    Given the user does not have the "Tactic Deal Debugger" permission
    When User views the Tactic Debugger
    Then the "Deal View" toggle and all references to it should be completely hidden

  @todo
  Scenario: Regression - individual-deal debugger view renders the funnel
    When User switches to Deal View
    And User clicks into an individual deal
    Then the debugger funnel view should render under that individual deal

  @todo
  Scenario: Regression - Eligible Requests percentage is non-zero and correctly calculated
    When User switches to Deal View for a tactic with active deals
    Then the Eligible Requests percentage should display a correct non-zero value for each deal, not zero for all deals

  @todo
  Scenario: Regression - isolated view does not show the Tactic View button or Time frame control
    When User switches to Deal View and navigates to the isolated debugger view
    Then the isolated view should not display a "Tactic View" button
    And the isolated view should not display a "Time frame" control

  @todo
  Scenario: Regression - navigating from Deal View sequential mode to isolated view succeeds
    When User switches to Deal View in sequential mode
    And User attempts to navigate to the isolated view
    Then the navigation to the isolated view should succeed

  @todo
  Scenario: Regression - the Explore Deal Across Tactics panel includes a Cancel button
    When User switches to Deal View
    And User opens the "Explore Deal Across Tactics" panel
    Then the panel should include a Cancel button

  @todo
  Scenario: Regression - the no-permission timeframe filter experience matches the permission-gated UI
    Given the user does not have the "Tactic Deal Debugger" permission
    When User views the timeframe filter on the Tactic Debugger
    Then the timeframe filter experience should match the expected no-permission production behavior

  @todo
  Scenario: Regression - debugger state persists after navigating to the Creative Test Page and back
    Given User has an active Deal View filter applied in the Tactic Debugger
    When User navigates to the Creative Test Page and returns to the Tactic Debugger
    Then the previously applied Deal View filter and debugger data should still be present
