Feature: Life Keyword Targeting - Search Filters List to Match Media Planner

  1. Changes Keyword and Keyword Population targeting search so it filters the list to matching items instead of only highlighting them.
  2. Aligns the search matching logic with the Media Planner reference implementation and keeps the matched term highlighted within the filtered results.
  3. Preserves selection so items chosen from a filtered result set persist after the tactic is saved.
  4. Each scenario walks a single continuous pass through the targeting search UI, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify Keyword targeting search filters, highlights, resets, and handles edge cases
    Given User creates a new tactic and opens Keyword targeting
    # Framework Gap: Requires step definitions for Keyword targeting search in LifeSteps.java
    When User types "hypertension" in the Keyword targeting search field
    Then the list filters to show only items containing "hypertension" and non-matching items are hidden
    And the "hypertension" text is highlighted within each filtered result
    When User types "card" in the search field
    Then items containing "cardiovascular", "cardiology", and "cardiac" all appear in the filtered list
    # AMB-1: confirm substring versus prefix match behavior
    When User types "Hypertension" and then "hypertension"
    Then both searches return the same filtered list, confirming the search is case-insensitive
    When User searches for "xyzqrstabc"
    Then an empty-state message is shown, the field remains functional, and the full list is not displayed
    # GAP-2: exact empty-state message is not specified
    When User clears the search field
    Then the full unfiltered keyword list is restored with no stale filter state

  @todo
  Scenario: Verify Keyword Population search mirrors Keyword targeting and matches Media Planner
    Given User creates a new tactic and opens Keyword Population targeting
    When User types "cardiovascular" in the Keyword Population search field
    Then the list filters to show only Keyword Population items matching "cardiovascular", mirroring Keyword targeting
    When User runs the same keyword search in Life DSP and in Media Planner for the same term
    Then both surfaces filter identically with the same matching logic and the same result count

  @todo
  Scenario: Verify items selected from a filtered search persist after tactic save
    Given User creates a new tactic and opens Keyword targeting
    When User filters the list by "diabetes", selects 5 items from the filtered results, and saves the tactic
    And User reopens the tactic
    Then exactly 5 selected items persist and the count does not decrease
    When User selects 50 or more Keyword Population items and saves the tactic
    And User reopens the tactic
    Then the reloaded selection count equals the count at selection time with no items silently dropped
    # Regression anchor: HT-4185 - Keyword Populations previously disappeared after saving a tactic

  @todo
  Scenario: Verify Health Pages targeting search is unaffected by the Keyword targeting search change
    Given User opens Health Pages targeting on a new tactic
    When User searches for a descriptor
    Then Health Pages search behavior is unchanged with no regression from the Keyword targeting filter implementation
