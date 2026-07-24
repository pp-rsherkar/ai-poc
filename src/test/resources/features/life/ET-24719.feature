Feature: Life Keyword Targeting - Align Search Behaviour with Media Planner

  1. Changes Keyword and Keyword Population targeting search so that typing a term filters the list to matching items instead of only highlighting them.
  2. Aligns the matching logic, result count, and behaviour with the Media Planner reference implementation across both targeting types.
  3. Retains highlighting within the filtered results and preserves selection persistence when a tactic is saved.
  4. Each scenario walks a single continuous pass through the targeting search, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24719, GAP-1, AMB-1
  @todo
  Scenario: Verify the filter-and-highlight search workflow for Keyword and Keyword Population targeting
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given User creates a new tactic and opens Keyword targeting
    When User types "hypertension" in the search field
    Then the list filters to show only items containing "hypertension" and hides non-matching items
    And the term "hypertension" is highlighted within each visible result
    When User clears the search term
    Then the full unfiltered keyword list is restored with no stale filter state
    When User types the partial string "card"
    Then items containing "cardiovascular", "cardiology", and "cardiac" all appear in the filtered list
    # AMB-1 / GAP-1: substring-vs-prefix and full-text-vs-code matching logic must be confirmed against Media Planner
    When User types "Hypertension" and then "hypertension"
    Then both searches return the same filtered list, confirming case-insensitive matching
    When User types the non-existent term "xyzqrstabc"
    Then an appropriate empty state is shown, the field stays functional, and the list does not fall back to all items
    # GAP-2: exact empty-state message is unspecified - document actual result
    When User opens Keyword Population targeting and searches for "cardiovascular"
    Then the Keyword Population list filters identically to Keyword targeting
    When User runs the same search term in Life DSP and in Media Planner
    Then both surfaces filter with the same matching logic and the same result count

  # Source: ET-24719, GAP-2, HT-4185
  @todo
  Scenario: Verify selection persistence and adjacent-targeting regression after search-driven selection
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given User creates a new tactic and opens Keyword targeting
    When User filters the list by "diabetes", selects 5 items from the filtered results, and saves the tactic
    And User reopens the tactic
    Then exactly 5 selected items persist and the count does not decrease
    When User selects 50 or more Keyword Population items and saves the tactic
    And User reopens the tactic
    Then the reloaded count equals the count at selection time with no items silently dropped
    # Regression anchor: HT-4185 - Keyword Populations disappearing items after saving tactic
    When User opens Health Pages targeting and searches for a descriptor
    Then Health Pages search behaviour is unchanged by the Keyword targeting filter implementation
