Feature: Life Deal Platform - Hidden Deal Handling in Deal Groups

  1. Hides hidden deals from the Deal Group > Add Deals picker while keeping hidden deals already inside a deal group active for tactics that target the group.
  2. Shows clear messaging when a deal group contains hidden deals and a specific duplicate-name error when a deal group name already exists.
  3. Keeps the applied deals count accurate with mixed hidden and visible deals and scopes deal group name uniqueness to the account.
  4. Each scenario walks a single continuous pass through the Deal Groups management UI, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify hidden deals are filtered from the Add Deals picker while visible deals remain
    Given User opens the Deal Group Add Deals view with at least one deal in the account marked as hidden
    # Framework Gap: Requires step definitions for the Deal Groups Add Deals picker in LifeSteps.java
    Then the hidden deals are not shown in the picker while non-hidden deals appear normally
    When User marks a visible deal as hidden and then unhides it
    Then the deal disappears from the picker when hidden and reappears after being unhidden within acceptable refresh time
    # GAP-3: confirm whether hidden is account-level or global

  @todo
  Scenario: Verify hidden deals inside a deal group stay active for targeting tactics
    Given User has a deal group containing a deal that is later hidden and a tactic targeting that group
    When User verifies tactic delivery
    Then the tactic continues to target and deliver using the deal group and the hidden deal within the group remains active
    Given User adds a hidden deal to a deal group that a tactic already targets
    Then the tactic continues to deliver and the hidden deal within the group does not prevent spending
    # Regression anchor: HT-5043 - Deal Group Bug where the tactic was not spending

  @todo
  Scenario: Verify hidden-deal messaging and duplicate deal group name handling
    Given User opens a deal group that contains at least one hidden deal
    Then the UI shows an indicator or message that the group contains hidden deals
    # GAP-1: exact message text must be obtained from the Figma (Victor Onazi comment)
    When User creates a deal group with a name that already exists in the account
    Then the specific duplicate-name error from the design is shown and the deal group is not created
    # GAP-2: exact error message from the Figma is required
    When User creates a deal group named "Test Group" and then attempts to create "test group"
    Then the case sensitivity of the duplicate-name check is documented as either an error or a second group created
    Given User opens a deal group where all deals have been hidden
    Then the UI clearly messages that all deals in the group are hidden with no crash or unexplained empty state

  @todo
  Scenario: Verify deal count accuracy, account-scoped naming, and Deal Groups access are not regressed
    Given User opens a deal group with 10 deals where 3 are hidden and 7 are visible
    Then the applied deals count reflects the total of 10 deals with hidden deals counted even when not shown in the picker
    # Regression anchor: HT-5666 - Applied deals tab count and list mismatch
    Given User creates a deal group named "Test Group" in two different accounts
    Then both groups are created as name uniqueness is account-scoped, not global
    # GAP-3: name uniqueness scope
    Given User on an external account opens the Deal Group Deals view and applies a filter to search for deals
    Then the filter returns the correct deals and no "no deals available" error appears for a valid account with deals
    # Regression anchor: HT-5167 - Deal Groups filter showing "no deals available"
    Given User with the Deal Groups permission opens Deal Groups from the Tactic UI
    Then the Deal Groups section loads without an "Unable to Access" error
    # Regression anchor: HT-6125 (ACTIVE) - User Unable to Access Deal Groups In Tactic UI
