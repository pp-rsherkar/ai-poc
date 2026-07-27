Feature: Life Keyword Targeting - Search Filter Alignment with Media Planner
  1. Filters the Keyword and Keyword Population targeting lists to matching items as the user types, matching Media Planner behaviour.
  2. Highlights matches within the filtered results and restores the full list when the search is cleared.
  3. Persists items selected via filtered search on tactic save.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24719, PROD-15106
  @todo
  Scenario: Verify Keyword targeting search filters, highlights and aligns with Media Planner
    Given User opens Keyword targeting on a new tactic
    # Framework Gap: Requires step definition asserting the keyword list filters to matching items on search in LifeSteps.java
    When User types "hypertension" in the Keyword targeting search field
    Then The list filters to show only keywords containing "hypertension" and non-matching items are hidden
    When User clears the Keyword targeting search field
    Then The full unfiltered keyword list is restored with no stale filter state
    When User types "diabetes" in the Keyword targeting search field
    Then The filtered list shows only diabetes-related items and the "diabetes" text is highlighted within each result
    When User types "card" in the Keyword targeting search field
    Then The filtered list includes items such as cardiovascular, cardiology and cardiac
    When User types "Hypertension" and then "hypertension" in the Keyword targeting search field
    Then Both searches return the same case-insensitive filtered list
    When User compares the same search term in Life DSP Keyword targeting and in the Media Planner Keyword targeting
    Then Both surfaces filter identically with the same matching logic and the same result count

  # Source: ET-24719, PROD-15106, GAP-2
  @todo
  Scenario: Verify Keyword Population targeting search filters identically and persists selections on save
    Given User opens Keyword Population targeting on a new tactic
    When User types "cardiovascular" in the Keyword Population search field
    Then The list filters to show only Keyword Population items matching "cardiovascular" mirroring Keyword targeting
    When User filters by "diabetes", selects 5 items from the filtered results and saves the tactic
    Then Exactly 5 selected items persist after tactic reload and the count does not decrease

  # Regression anchor: HT-4185 - Keyword Populations disappearing items after tactic save
  # Source: ET-24719, HT-4185
  @todo
  Scenario: Verify Keyword Population selection count does not decrease after save
    Given User opens Keyword Population targeting on a new tactic
    When User selects 50 or more Keyword Population items and saves the tactic
    And User reloads the tactic
    Then The selection count on reload equals the count at selection time with no items silently dropped

  # Source: ET-24719, GAP-1
  @todo
  Scenario: Verify Health Pages targeting search is unaffected by the Keyword targeting filter change
    Given User opens Health Pages targeting on a new tactic
    When User searches for a descriptor in Health Pages targeting
    Then The Health Pages search behaviour is unchanged with no regression from the Keyword targeting filter implementation
