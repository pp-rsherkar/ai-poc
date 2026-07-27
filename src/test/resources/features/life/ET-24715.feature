Feature: Life Admin - Deprecate Creative Name Bid Multiplier Permission

  1. Removes the Creative Name BM permission row from the Life Features table in Admin while leaving all other permission rows intact.
  2. Ensures users who previously held the permission experience no breakage in core tactic and creative workflows.
  3. Keeps the Life Features table rendering correctly after the row removal, with the deprecated permission absent from search results.
  4. Each scenario walks a single continuous pass through the Life Features table, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the Creative Name BM permission is removed and the Life Features table remains stable
    Given User navigates to the Life Features table in Admin
    # Framework Gap: Requires step definitions for the Life Features permission table in LifeSteps.java
    Then the Creative Name BM row is absent while all other permission rows are present and intact
    When User toggles a permission adjacent to where Creative Name BM was
    Then the adjacent permission toggles correctly with no rendering error and the table saves without issue
    Then the table renders with no blank rows where Creative Name BM was and the row count matches the expected pre-minus-one total
    When User searches the Life Features table for "Creative Name"
    Then no results are returned and the search does not error or return stale cached results

  @todo
  Scenario: Verify deprecating Creative Name BM does not break existing tactic workflows
    Given User is on an account that previously had Creative Name BM permission enabled
    When User creates and saves a tactic
    Then the tactic creates and saves normally with no permission-related errors related to Creative Name BM
    Then no UI element is gated by Creative Name BM, and any remaining permission check evaluates as denied with no UI shown
    # GAP-1: scope of deletion (permission row only vs underlying feature code) to be confirmed

  @todo
  Scenario: Verify Creative Name BM removal is independent of the parallel Line Item Creative Separation removal
    Given User navigates to the Life Features table in Admin
    Then both the Creative Name BM and Line Item Creative Separation rows are absent with no cross-contamination between the two removals
    # AMB-1: parallel deprecations ET-24715 and ET-24713 must be independent of each other
