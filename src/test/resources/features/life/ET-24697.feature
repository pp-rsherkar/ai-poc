Feature: Deal Platform - Remove Deal Incompatibility Warning for PG Deals on non-PG Tactics

  1. Removes the 'Deal Incompatibility' warning and its tooltip when a PG deal is added to a non-PG tactic, so the intentional PG Workaround use case proceeds silently.
  2. Keeps the warning removal targeted so genuinely incompatible scenarios and the underlying deal-association logic are unchanged.
  3. Guards against the Deal Platform's history of PG pricing, fee, and bidding regressions.
  4. Each scenario walks a single continuous pass through the tactic deal-selection UI, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the incompatibility warning and tooltip are removed for the PG-on-non-PG workflow
    Given an account with the PG Workaround permission
    When User creates a non-PG tactic and adds a PG deal to it
    Then the deal is added with no Deal Incompatibility warning, modal, toast, or inline message
    # AMB-2: confirm the exact permission name "PG Workaround" with the team
    When User hovers over the added PG deal in the selection
    Then no incompatibility tooltip appears
    When User saves the tactic and reopens it
    Then the tactic loads with the PG deal in the deal list and no residual or stale warning state
    Given an account without the PG Workaround permission
    When User adds a standard non-PG deal to a non-PG tactic
    Then the deal is added without a warning (standard behaviour is unchanged)
    When User attempts to add a PG deal to a non-PG tactic without the PG Workaround permission
    Then the actual behaviour is documented (warning still shown, action blocked, or silent)
    # GAP-1: behaviour for non-PG-Workaround users is unspecified - document actual behaviour

  @todo
  Scenario: Verify the warning removal is targeted and PG delivery, pricing, fees, and bidding are not regressed
    Given an account with a PG tactic setup
    When User adds a PG deal to a PG tactic
    Then the deal is added with no incompatibility message (the correct PG-on-PG scenario is unaffected)
    When User sets up a genuinely incompatible deal scenario elsewhere in the UI
    Then other non-PG-related incompatibility warnings still appear as expected
    Given an account with the PG Workaround permission and a PG deal on a non-PG tactic
    Then the PG deal shows its correct pricing type (Fixed or Floor as configured) with no pricing display error
    # Regression anchor: HT-5466 - Flora CBR PG deals showing 'Floor' when configured as Fixed
    And only one fee type is charged for the PMP/PG deal with no Platform Fee double-charge
    # Regression anchor: HT-3601 - PMP incurring Deal Fee AND Platform Fee
    And the tactic bids and delivers on the PG deal with no bidding loss from the UI change
    # Regression anchor: HT-5051 - PG Adx deals having deal pricing bidding loss
