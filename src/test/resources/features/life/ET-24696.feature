Feature: Deal Platform - Hidden Deal Handling in Deal Groups

  1. Filters hidden deals out of the Deal Group > Add Deals picker while keeping already-assigned hidden deals active for tactics that target the group.
  2. Communicates clearly when a deal group contains hidden deals and validates duplicate deal group names with an account-scoped check.
  3. Guards against the Deal Platform's history of picker-filter, count-accuracy, delivery, and access regressions.
  4. Each scenario walks a single continuous pass through the Deal Groups UI, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify hidden deal filtering, in-group persistence, and user messaging workflow in Deal Groups
    Given an account with at least one deal marked as hidden
    When User opens the Deal Group > Add Deals picker
    Then the hidden deal is not shown while all non-hidden deals appear normally
    # GAP-3: confirm whether "hidden" is scoped at the account level or globally
    Given a deal group that already contains a deal which is later hidden
    When a tactic targets that deal group
    Then the tactic continues to deliver and the hidden deal within the group stays active
    When User opens a deal group that contains at least one hidden deal
    Then the UI shows a clear indicator or message that the group contains hidden deals
    # GAP-1: exact hidden-deal message text is in the Figma (Victor Onazi comment) and must be obtained
    When User marks a deal hidden, confirms it disappears from the picker, then unhides it
    Then the deal reappears in the picker within an acceptable refresh time
    Given a deal group where all deals have been hidden
    When User opens the group
    Then a clear message explains that all deals are hidden with no crash or unexplained empty state

  @todo
  Scenario: Verify duplicate-name validation and Deal Groups regression guards
    Given User is creating a deal group
    When User creates a deal group with a name that already exists in the account
    Then the specific duplicate-name error message is shown and the deal group is not created
    # GAP-2: exact duplicate-name error text is in the Figma and must be obtained
    When User creates "Test Group" and then attempts to create "test group" with different casing
    Then the case-sensitivity behaviour is documented (error shown if case-insensitive, or created if case-sensitive)
    When User creates a deal group named "Test Group" in two different accounts
    Then both groups are created, confirming name uniqueness is account-scoped and not global
    Given an external-user account with deals
    When User opens Deal Group > Deals and applies a filter to search for deals
    Then the filter returns the correct deals and no "no deals available" error appears
    # Regression anchor: HT-5167 - Deal Groups filter showing 'no deals available'
    Given a deal group with 10 deals where 3 are hidden and 7 are visible
    Then the applied deals count reflects all 10 deals, counting hidden deals even though they are not shown in the picker
    # Regression anchor: HT-5666 - Applied deals tab count/list mismatch
    When User opens Deal Groups from the Tactic UI with a deal-groups-permitted account
    Then Deal Groups loads without an "Unable to Access" error
    # Regression anchor: HT-6125 (ACTIVE) - User unable to access Deal Groups in Tactic UI
