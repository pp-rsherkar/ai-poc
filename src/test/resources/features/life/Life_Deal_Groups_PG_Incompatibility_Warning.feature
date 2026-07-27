Feature: Life Deal Groups - Remove PG Deal Incompatibility Warning

  1. Removes the Deal Incompatibility warning and tooltip when a PG deal is added to a non-PG tactic.
  2. Keeps unrelated incompatibility warnings and standard deal-tactic behavior unchanged.
  3. Preserves PG deal delivery, pricing, and fee correctness.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24697 (TC_01, TC_02, TC_03, TC_04, TC_08)
  @todo
  Scenario: Verify Deal Incompatibility warning and tooltip are removed for PG deals on non-PG tactics
    # Framework Gap: Requires page object + step definitions for tactic deal selection warnings in LifeTacticSteps.java
    Given User creates a non-PG tactic on an account with the PG Workaround permission
    When User adds a PG deal to the non-PG tactic
    Then No Deal Incompatibility warning appears as a modal, toast, or inline message
    And No incompatibility tooltip appears when hovering over the deal
    When User adds a standard non-PG deal to a non-PG tactic
    Then The deal is added with no warning as standard behavior
    When User adds a PG deal to a PG tactic
    Then The deal is added with no incompatibility message
    When User saves the tactic and reopens it
    Then The tactic loads with the PG deal present and no residual warning state

  # Source: ET-24697 (TC_05, TC_06, TC_07, TC_09, TC_10)
  @todo
  Scenario: Verify targeted warning removal, delivery, pricing, and fee regression for PG deals
    Given User adds a PG deal to a non-PG tactic without the PG Workaround permission
    Then The actual behavior for non-PG-Workaround users is recorded per GAP-1
    When User sets up a genuinely incompatible non-PG-related deal scenario
    Then Other valid incompatibility warnings still appear and only the PG-on-non-PG warning is removed
    # Regression anchor: HT-5466 - Flora CBR PG deals showing Floor instead of Fixed
    When User checks the pricing display of the PG deal on the non-PG tactic
    Then The pricing type shows correctly as configured with no display error
    # Regression anchor: HT-3601 - PMP incurring Deal Fee and Platform Fee
    When User checks the fees for the PG deal in reporting
    Then Only the applicable Deal Fee is charged with no double Platform Fee
    # Regression anchor: HT-5051 - PG Adx deal bidding loss
    When User verifies bidding and delivery for the PG deal in staging
    Then The tactic bids and delivers on the PG deal with no bidding loss
