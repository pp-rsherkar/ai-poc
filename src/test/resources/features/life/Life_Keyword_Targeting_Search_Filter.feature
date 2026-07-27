Feature: Life Keyword Targeting - Search Filter Alignment with Media Planner

  1. Aligns Keyword and Keyword Population targeting search with Media Planner by filtering the list to matching items.
  2. Highlights the matched term within the filtered results and restores the full list when the search is cleared.
  3. Persists items selected via filtered search when the tactic is saved.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24719 (TC_01, TC_02, TC_03, TC_04, TC_05)
  @todo
  Scenario: Verify Keyword and Keyword Population search filters, highlights, and matches Media Planner behavior
    # Framework Gap: Requires page object + step definitions for keyword targeting search and filter in LifeTargetingSteps.java
    Given User creates a new tactic and opens the Keyword targeting section
    When User types "hypertension" in the Keyword search field
    Then The list filters to show only items containing "hypertension" and hides non-matching items
    And The term "hypertension" is highlighted within each filtered result
    When User clears the Keyword search field
    Then The full unfiltered keyword list is restored with no stale filter state
    When User opens the Keyword Population targeting section and types "cardiovascular"
    Then The Keyword Population list filters identically to Keyword targeting
    When User compares the Life DSP search result for a term against the Media Planner result for the same term
    Then Both surfaces filter with identical matching logic and the same result count

  # Source: ET-24719 (TC_06, TC_07, TC_10)
  @todo
  Scenario Outline: Verify keyword search matching, case-insensitivity, and empty-state handling
    Given User opens the Keyword targeting section on a new tactic
    When User searches for "<TERM>"
    Then The filtered result is "<RESULT>"
    Examples:
      | TERM         | RESULT                                                          |
      | card         | all items containing card such as cardiovascular and cardiac    |
      | Hypertension | the same items as the lowercase search hypertension             |
      | xyzqrstabc   | a no-results empty state with the list not showing all items    |

  # Source: ET-24719 (TC_08, TC_09, TC_11)
  @todo
  Scenario: Verify filtered-search selections persist on save and adjacent targeting is unaffected
    Given User opens the Keyword targeting section on a new tactic
    When User filters the list by "diabetes", selects 5 items, and saves the tactic
    And User reopens the saved tactic
    Then Exactly 5 selected items persist after reload
    # Regression anchor: HT-4185 - Keyword Population items dropped after save
    When User selects 50 Keyword Population items and saves the tactic
    Then The Keyword Population selection count on reload equals 50
    When User opens the Health Pages targeting section and searches for a descriptor
    Then The Health Pages search behavior is unchanged by the Keyword targeting filter implementation
