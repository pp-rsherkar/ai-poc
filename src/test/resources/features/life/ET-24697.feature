Feature: Life Deal Platform - Remove Deal Incompatibility Warning for PG Deals on Non-PG Tactics

  1. Removes the Deal Incompatibility warning and its tooltip when a PG deal is added to a non-PG tactic for users with the PG Workaround permission.
  2. Keeps the warning removal targeted, so standard deal flows and genuinely incompatible scenarios are unchanged.
  3. Preserves correct PG deal delivery, pricing, fees, and bidding after the warning is removed.
  4. Each scenario walks a single continuous pass through the tactic deal selection, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify no incompatibility warning or tooltip appears when adding a PG deal to a non-PG tactic
    Given User has the PG Workaround permission and edits a non-PG tactic
    # Framework Gap: Requires step definitions for tactic deal selection and warning checks in LifeSteps.java
    When User adds a PG deal to the non-PG tactic
    Then the deal is added with no Deal Incompatibility warning shown as a modal, toast, or inline message
    When User hovers over the PG deal in the selection
    Then no tooltip related to deal incompatibility appears
    # AMB-2: confirm the exact permission name "PG Workaround" with the team
    When User saves the tactic and reopens it
    Then the tactic loads with the PG deal in the deal list and no residual or stale warning state

  @todo
  Scenario: Verify the warning removal is targeted to the PG-on-non-PG scenario
    Given User adds a standard non-PG deal to a non-PG tactic
    Then the deal is added without a warning, unchanged from standard behavior
    Given User adds a PG deal to a PG tactic
    Then the deal is added without a warning as PG on PG is fully supported
    Given User without the PG Workaround permission adds a PG deal to a non-PG tactic
    Then the actual behavior is documented, whether a warning is shown, the action is blocked, or it is silent
    # GAP-1: behavior for non-PG-Workaround users is not specified - document the actual behavior
    Given a genuinely incompatible non-PG-related deal scenario exists
    Then its incompatibility warning still appears, confirming the fix is targeted and not a global suppressor

  @todo
  Scenario: Verify PG deal delivery, pricing, fees, and bidding are not regressed
    Given User with the PG Workaround permission has added a PG deal to a non-PG tactic
    Then the PG deal shows the correct pricing type of Fixed or Floor as configured with no pricing display error
    # Regression anchor: HT-5466 - Flora CBR PG deals showing "Floor" when configured as Fixed
    Then only the Deal Fee is charged for the PMP or PG deal with no Platform Fee double-charge
    # Regression anchor: HT-3601 - PMP incurring Deal Fee AND Platform Fee
    When User verifies spend and bidding in staging
    Then the tactic bids and delivers on the PG deal with no bidding loss from the UI change
    # Regression anchor: HT-5051 - PG Adx deals having deal pricing bidding loss
