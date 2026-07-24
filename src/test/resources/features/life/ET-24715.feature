Feature: Admin - Deprecate Creative Name Bid Multiplier Permission

  1. Removes the 'Creative Name BM' permission row from the Life Features table in the Admin panel so it is no longer visible, selectable, or enforceable.
  2. Ensures the removal does not break adjacent permission rows or the workflows of accounts that previously held the permission.
  3. Coordinates cleanly with the parallel ET-24713 deprecation so the two removals stay independent.
  4. Each scenario walks a single continuous pass through the Admin Life Features table, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the Creative Name BM permission removal and Life Features table integrity workflow
    Given User is an Admin user viewing the Life Features table
    Then the "Creative Name BM" row is absent and all other permission rows are present and intact
    # GAP-1: whether the underlying feature code is also deleted (vs. only the permission row) is an unresolved open question
    When User toggles a permission row adjacent to where Creative Name BM was
    Then the adjacent toggle works, the table renders without error, and it saves without issue
    When User opens the Life Features table after deployment
    Then the table renders with no blank row where Creative Name BM was and the row count equals the prior count minus one
    When User searches or filters the Life Features table for "Creative Name"
    Then no results are returned and the search does not error or return stale cached results

  @todo
  Scenario: Verify the deprecation does not break existing workflows and stays isolated from ET-24713
    Given an account that previously had the Creative Name BM permission enabled
    When User creates and saves a tactic with that account
    Then the tactic saves normally with no permission-related error tied to Creative Name BM
    When User inspects whether any UI element remains gated by Creative Name BM
    Then no UI element is gated by it, and any residual permission check evaluates as denied with no UI shown
    # GAP-1: depends on resolution of the code-deletion open question
    When User verifies the Line Item Creative Separation permission (ET-24713) is also removed
    Then both the Creative Name BM and Line Item Creative Separation rows are absent with no cross-contamination between the two removals
    # AMB-1: parallel deprecations ET-24715 and ET-24713 must be independent of each other
