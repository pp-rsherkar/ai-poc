Feature: Deal Debugger - Data Source Corrections and Scope Expansion

  1. Post Targeting Eligible Requests is sourced from day-bounded Today/Yesterday data instead of a rolling Last-24-Hours window (ET-24480).
  2. Bids is sourced from the same DPD Adv Stats endpoint as Impressions/Clicks/Win Rate/CTR, resolving a cross-source inconsistency (ET-24243).
  3. Deal View becomes accessible for tactics targeting both Deals and the Open Exchange together, scoped to only the explicitly targeted deals (ET-24239).

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Tactic Debugger for an existing tactic

  @todo
  Scenario Outline: Post Targeting Eligible Requests uses day-bounded Today/Yesterday data instead of a rolling 24-hour window
    Given User selects "<TIMEFRAME>" as the Debugger timeframe
    Then Post Targeting Eligible Requests is populated from the new "<TIMEFRAME>"-scoped data source, not the old rolling Last-24-Hours source
    And comparing Bids vs. Post Targeting Eligible Requests for this timeframe shows Requests greater than or equal to Bids, not inverted
    Given the "Yesterday" timeframe option
    Then it is unhidden now that its data source is correct
    Given a deal debugged right at the midnight day-boundary rollover
    Then "Today" data does not leak into "Yesterday" or vice versa, since both now use day-bounded, not rolling, sources
    Examples:
      | TIMEFRAME |
      | Today     |
      | Yesterday |

  @todo
  Scenario: Bids in the Tactic Debugger is sourced from the same DPD Adv Stats endpoint as Impressions, resolving the Win Rate mismatch
    Given User views the Tactic Debugger metrics header
    Then Bids is sourced from the DPD Adv Stats endpoint, the same source already used for Impressions, Clicks, Win Rate, and CTR
    When User manually recomputes Win Rate as Impressions divided by Bids using the displayed values
    Then the recomputed value matches the displayed Win Rate
    # Regression anchor: BRAIN-2339 - root-cause ticket for the Win Rate mismatch; confirm the exact originally reported discrepancy is closed, not just the general metric source
    # Regression anchor: HT-5259 - root-cause ticket for "more impressions than bids"; cross-check the exact originally reported scenario now shows correct behavior
    Given any filter/breakdown view within the Debugger beyond the top-level metrics header
    Then the Bids source change applies consistently across all such breakdowns

  @todo
  Scenario: Deal View is accessible for tactics targeting Deals and the Open Exchange together, showing only explicitly targeted deals
    Given a tactic has "Selected + Open Exchange" selected in the Curated Markets and Deals section
    Then the Deal View is accessible for that tactic, not just for pure-deal-targeting tactics
    And Deal View data is captured only for deals/curated marketplaces explicitly targeted on the tactic
    Given the tactic also bids on open-exchange deals it did not explicitly target
    Then those non-targeted deals are not surfaced in Deal View
    Given the tactic's radio selection is switched between "Selected + Open Exchange", pure deal-only, and pure open-exchange-only
    Then Deal View access/visibility toggles correctly for each state
