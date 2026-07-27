Feature: Life Health Pages Targeting - MeSH 2025 13k Taxonomy Migration

  1. Migrates the Health Pages targeting tree to the MeSH 2025 taxonomy (~13k descriptors across ~30k tree positions, 10+ tier levels) in place of the legacy taxonomy.
  2. Applies dynamic margin recalculation for deep nesting and preserves search-term highlighting through expansion and partial matches.
  3. Keeps selection persistence intact so the exact set of descriptors selected survives save and reload across the deeper tree.
  4. Each scenario walks a single continuous pass through the Health Pages targeting UI, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the MeSH 2025 taxonomy tree renders, expands, and recalculates margins for deep nesting
    Given User creates a new tactic and opens Health Pages targeting
    # Framework Gap: Requires step definitions for the Health Pages taxonomy tree in LifeSteps.java
    Then the Health Pages taxonomy tree loads and displays MeSH 2025 descriptors with up to 10 or more tier levels available on expansion
    And the descriptor count reflects the ~13k MeSH 2025 entries, not the legacy ~7.5k
    When User expands a tree branch to 5 or more levels deep
    Then level 5 items render with a smaller left margin than level 4, and level 4 smaller than level 3
    # AMB-1: exact margin formula is unspecified - document the observed margin pattern
    When User expands a descriptor branch to its maximum available depth
    Then the tree remains visually contained with no horizontal overflow or text clipping and items stay legible
    When User rapidly expands and collapses multiple branches
    Then the tree remains stable with no blank nodes, no UI freeze, and no console errors

  @todo
  Scenario: Verify search, highlighting, and typeahead behavior across the MeSH 2025 tree
    Given User opens Health Pages targeting on a new tactic
    When User types "cardiovascular" in the Health Pages search field and expands the matching branches
    Then matching descriptors show the search term highlighted and the highlight persists through expansion
    When User clears the search and types "oncology"
    Then the previous "cardiovascular" highlights are cleared and "oncology" highlights appear correctly
    When User types "Hypertension" in the search field
    Then relevant MeSH 2025 results including "Hypertension" appear within 2 seconds
    When User searches for the descriptor "xyzabc123"
    Then a no-results empty state is shown, no error is thrown, and the search field remains functional
    When User searches for a descriptor confirmed as removed in MeSH 2025
    Then the descriptor is not found and the tree continues to load normally
    # AMB-2 / GAP-2: handling of descriptors removed in MeSH 2025 - reference the 2025 MeSH Highlights page

  @todo
  Scenario Outline: Verify Health Pages descriptor selection persists exactly across save and reload
    Given User opens Health Pages targeting on a new tactic
    When User selects "<COUNT>" MeSH 2025 descriptors across multiple tree levels and saves the tactic
    And User reopens the tactic
    Then exactly "<COUNT>" descriptors are saved and displayed with labels matching the MeSH 2025 selection
    # Regression anchor: HT-4185 - Keyword Populations previously dropped items after save (593 selected, 498 surviving)
    Examples:
      | COUNT |
      | 10    |
      | 50    |

  @todo
  Scenario: Verify parent-node selection cascades correctly across the deep tree
    Given User opens Health Pages targeting on a new tactic
    When User selects a parent node that has 20 or more children and saves
    Then the selection count includes the parent and all eligible children
    When User selects a parent with 10 children and then deselects that parent
    Then all 10 child descriptors are also deselected and the targeting count decreases correctly

  @todo
  Scenario: Verify shared Health targeting components and adjacent targeting types are not regressed
    Given User creates a tactic with Health Pages, Health Pops, and Health Pops+ targeting
    When User views the tactic
    Then all three targeting types load successfully with a populated targeting tree and no Milkshake-only loading state
    # Regression anchor: HT-5112 - Health Pops, Health Pops+, and Health Pages previously failed to load simultaneously
    Given User creates a tactic with Keyword Population targeting
    When User selects descriptors and saves the tactic
    Then Keyword Population targeting is unaffected and its items persist correctly after save
    # Regression anchor: HT-4185 - MeSH 2025 changes must not affect adjacent targeting types

  @todo
  Scenario: Verify a legacy saved tactic with a deprecated MeSH descriptor loads without error
    Given User opens an existing tactic saved before the MeSH migration that contains a legacy descriptor
    Then the tactic loads without error and the legacy descriptor maps to a MeSH 2025 equivalent or is shown as unresolved with a clear UI state
    # GAP-2: backward compatibility for legacy descriptors is undefined - document the actual behavior
