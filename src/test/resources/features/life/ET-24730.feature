Feature: Life Health Pages Targeting - Migrate to MeSH 2025 13k Taxonomy

  1. Replaces the legacy MeSH taxonomy tree in Health Pages targeting with the new MeSH 2025 taxonomy (~13k descriptors, ~30k tree positions, 10+ tier levels).
  2. Applies dynamic margin recalculation so deeper tree levels use progressively smaller margin increments while staying legible and contained.
  3. Preserves search-term highlighting across expand/collapse and keeps selection persistence intact when a tactic is saved and reloaded.
  4. Each scenario walks a single continuous pass through the Health Pages targeting tree, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24730, AMB-1
  @todo
  Scenario: Verify the MeSH 2025 taxonomy rendering, deep-nesting margins, and search highlighting workflow in Health Pages targeting
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given User creates a new tactic and opens Health Pages targeting
    Then the Health Pages taxonomy tree loads and displays the MeSH 2025 descriptors (~13k unique entries, not the legacy ~7.5k)
    When User expands a tree branch to 5 or more levels deep
    Then each deeper level uses a smaller left-margin increment than its parent level and remains legible
    # AMB-1: exact margin-reduction formula/floor is not specified - document the observed pattern and confirm with dev
    When User expands a descriptor tree to its maximum available depth (8+ levels)
    Then the tree stays visually contained with no horizontal overflow or text clipping
    When User types "cardiovascular" in the Health Pages search field and expands matching branches
    Then the matching descriptors are highlighted and the highlight persists through expand and collapse
    When User clears the search and types "oncology"
    Then the previous highlights are cleared and only the new matches are highlighted
    When User searches for a descriptor confirmed removed in MeSH 2025
    Then the descriptor is not found, no error is thrown, and the tree stays functional
    When User searches for the gibberish string "xyzabc123"
    Then a no-results empty state is shown, no error is thrown, and the search field stays functional
    When User rapidly expands and collapses multiple branches
    Then the tree stays stable with no blank nodes, UI freeze, or console errors

  # Source: ET-24730, GAP-2, AMB-2, HT-4185, HT-5112
  @todo
  Scenario: Verify MeSH 2025 selection persistence, cascade behaviour, and cross-targeting regression on tactic save
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given User creates a new tactic and opens Health Pages targeting
    When User selects 50 MeSH 2025 descriptors across multiple tree levels and saves the tactic
    And User reopens the tactic
    Then exactly 50 descriptors are saved and displayed and no items are silently dropped
    # Regression anchor: HT-4185 - Keyword Populations: 593 selected reduced to 498 after tactic save
    When User selects a parent node that has 20 or more children and saves
    Then the selection count includes the parent plus all eligible children
    When User deselects a parent node that has 10 children
    Then all 10 children are also deselected and the targeting count decreases correctly
    When User views a tactic that also has Health Pops and Health Pops+ targeting
    Then Health Pages, Health Pops, and Health Pops+ all load with populated trees and no Milkshake-only loading state
    # Regression anchor: HT-5112 - Health Pops/Health Pops+/Health Pages failed to load simultaneously (shared tree component)
    When User creates a tactic with Keyword Population targeting and saves it
    Then Keyword Population targeting is unaffected by the MeSH 2025 migration and its items persist after save
    When User opens an existing tactic saved before the migration that contains a legacy descriptor
    Then the tactic loads without error and the legacy descriptor either maps to a MeSH 2025 equivalent or is shown in a clear unresolved state
    # GAP-2 / AMB-2: backward-compatibility handling for legacy-only descriptors is undefined - document actual behaviour
