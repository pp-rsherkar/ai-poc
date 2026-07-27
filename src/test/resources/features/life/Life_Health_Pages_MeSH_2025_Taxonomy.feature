Feature: Life Health Pages Targeting - MeSH 2025 Taxonomy Migration

  1. Migrates the Health Pages targeting tree to the MeSH 2025 taxonomy of roughly 13k descriptors.
  2. Applies dynamic margin reduction for deeply nested levels and preserves search-term highlighting across expansion.
  3. Persists descriptor selections accurately when a tactic is saved and reloaded.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24730 (TC_01, TC_03, TC_04, TC_05, TC_13)
  @todo
  Scenario: Verify MeSH 2025 rendering, dynamic margins, highlighting, and parent-child selection in Health Pages targeting
    # Framework Gap: Requires page object + step definitions for the Health Pages MeSH taxonomy tree in LifeTargetingSteps.java
    Given User creates a new tactic and opens the Health Pages targeting section
    Then The Health Pages taxonomy tree loads with the MeSH 2025 descriptors
    And The taxonomy exposes tier levels deeper than 10 on expansion
    When User expands a taxonomy branch to 5 or more levels deep
    Then Each deeper level renders with a smaller left margin increment than its parent level
    When User expands a branch to the maximum available depth
    Then The tree remains visually contained with no horizontal overflow or text clipping
    When User types "cardiovascular" in the Health Pages search field
    Then Matching descriptors display the term highlighted and the highlight persists through expand and collapse
    When User selects a parent node that has 20 or more child descriptors
    Then The targeting selection count includes the parent and all eligible child descriptors

  # Source: ET-24730 (TC_02, TC_06, TC_07, TC_08)
  @todo
  Scenario Outline: Verify Health Pages MeSH 2025 search results, highlight refresh, and empty states
    # Framework Gap: Requires step definitions for Health Pages typeahead search assertions in LifeTargetingSteps.java
    Given User opens the Health Pages targeting section on a new tactic
    When User searches for "<TERM>"
    Then The search result is "<RESULT>"
    Examples:
      | TERM               | RESULT                                                            |
      | Hypertension       | relevant MeSH 2025 descriptors returned within 2 seconds          |
      | oncology           | previous highlights cleared and only new matches highlighted      |
      | xyzabc123          | a no-results empty state with no error and a functional field     |
      | a removed descriptor | no legacy-only descriptor removed in MeSH 2025 is returned       |

  # Source: ET-24730 (TC_09, TC_10, TC_11, TC_12, TC_14, TC_15, TC_16)
  @todo
  Scenario Outline: Verify MeSH 2025 selection persistence, deselection cascade, stability, and cross-targeting regression
    # Framework Gap: Requires step definitions for descriptor persistence and shared targeting-tree regression in LifeTargetingSteps.java
    Given User opens the Health Pages targeting section on a new tactic
    When User selects "<COUNT>" MeSH 2025 descriptors across multiple tree levels and saves the tactic
    And User reopens the saved tactic
    Then Exactly "<COUNT>" descriptors are retained with matching MeSH 2025 labels
    When User deselects a parent node that has 10 child descriptors
    Then All 10 child descriptors are also deselected and the targeting count decreases correctly
    When User expands and collapses multiple branches rapidly
    Then The tree remains stable with no blank nodes, freeze, or console errors
    When User opens an existing tactic saved before the migration containing a legacy descriptor
    Then The tactic loads with no error and the legacy descriptor maps or shows a clear unresolved state
    # Regression anchor: HT-5112 - Health Pops/Health Pops+/Health Pages failed to load simultaneously
    When User opens a tactic that also uses Health Pops and Health Pops+ targeting
    Then Health Pages, Health Pops, and Health Pops+ all load populated with no Milkshake-only state
    # Regression anchor: HT-4185 - Keyword Population items dropped after save
    When User selects Keyword Population items and saves the tactic
    Then The Keyword Population selections persist unchanged after reload
    Examples:
      | COUNT |
      | 10    |
      | 50    |
