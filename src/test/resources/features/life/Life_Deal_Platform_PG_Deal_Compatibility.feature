Feature: Life Deal Platform - PG Deal Compatibility Warning Removal
  1. Removes the Deal Incompatibility warning and tooltip when a PG deal is added to a non-PG tactic.
  2. Keeps standard deal, PG-on-PG and unrelated incompatibility behaviour unchanged.
  3. Preserves PG deal pricing, fees and bidding after the warning removal.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24697, PROD-12423
  @todo
  Scenario: Verify no incompatibility warning or tooltip when a PG deal is added to a non-PG tactic
    Given An account with the PG Workaround permission on a non-PG tactic
    # Framework Gap: Requires step definition to assert absence of the Deal Incompatibility warning and tooltip in LifeSteps.java
    When User adds a PG deal to the non-PG tactic
    Then The deal is added with no Deal Incompatibility modal, toast or inline warning shown
    And No deal incompatibility tooltip appears on hover over the deal in the selection
    When User saves the tactic and reloads it
    Then The tactic loads with the PG deal in the list and no residual or stale warning state
    When User adds a standard non-PG deal to a non-PG tactic and a PG deal to a PG tactic
    Then Both additions proceed with no incompatibility warning as the unchanged baseline

  # Source: ET-24697, PROD-12423, GAP-1, AMB-2
  @todo
  Scenario Outline: Verify warning removal is scoped correctly across permission and other scenarios
    Given "<SETUP>"
    When User adds the deal to the tactic
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | SETUP                                                       | EXPECTED_RESULT                                                       |
      | An account without the PG Workaround permission on a non-PG tactic | The documented behaviour for the missing permission is confirmed with the team |
      | A genuinely incompatible non-PG-related deal scenario       | Other incompatibility warnings still appear and only the PG-on-non-PG warning is removed |

  # Regression anchor: HT-5051 PG pricing bidding loss; HT-3601 PMP incurring both Deal Fee and Platform Fee
  # Source: ET-24697, HT-5051, HT-3601
  @todo
  Scenario: Verify PG deal pricing, fees and bidding are unaffected by the warning removal
    Given An account with the PG Workaround permission and a PG deal on a non-PG tactic
    When User checks the deal pricing display, fees in reporting and bidding in staging
    Then The PG deal shows the correct pricing type with no display error
    And Only a single Deal Fee is charged with no Platform Fee double-charge
    And The tactic bids and delivers on the PG deal with no bidding loss from the UI change
