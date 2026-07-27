Feature: Life Health Pages Targeting - MeSH 2025 13k Taxonomy Migration
  1. Renders the MeSH 2025 taxonomy tree in Health Pages targeting with deeper nesting and dynamic margin reduction.
  2. Keeps search highlighting and typeahead accurate against the larger taxonomy.
  3. Persists descriptor selections across save and reload without dropping items.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24730, PROD-15817
  @todo
  Scenario: Verify MeSH 2025 taxonomy tree rendering, dynamic margins, highlighting and search in Health Pages targeting
    Given User creates a new tactic and opens Health Pages targeting
    # Framework Gap: Requires step definition to load and inspect the MeSH 2025 Health Pages taxonomy tree in LifeSteps.java
    Then The Health Pages taxonomy tree loads the MeSH 2025 descriptors with 10 or more tier levels available on expansion
    When User expands a MeSH 2025 tree branch to 5 or more levels deep
    Then Each deeper level shows a smaller left-margin increment than its parent level and the layout does not overflow or clip
    When User expands a MeSH 2025 descriptor tree to its maximum available depth of 8 or more levels
    Then The tree remains visually contained with no horizontal overflow and items remain legible
    When User types "cardiovascular" in the Health Pages search field and expands the matching branches
    Then The matching descriptors have the search term highlighted and the highlight persists across expansion
    When User clears the search and types "oncology"
    Then The previous cardiovascular highlights are cleared and the oncology matches are highlighted correctly
    When User rapidly expands and collapses multiple branches of the MeSH 2025 tree
    Then The tree remains stable with no blank nodes, no freeze and no console errors

  # Source: ET-24730, PROD-15817, GAP-3
  @todo
  Scenario Outline: Verify Health Pages typeahead search returns correct results within acceptable time
    Given User opens Health Pages targeting on a new tactic
    When User types "<SEARCH_TERM>" in the Health Pages search field
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | SEARCH_TERM | EXPECTED_RESULT                                                          |
      | Hypertension | Relevant MeSH 2025 results appear within 2 seconds including Hypertension |
      | xyzabc123   | An empty no-results state is shown with no error and the field stays usable |
      | LegacyOnlyDescriptor | The removed legacy-only descriptor is not found and the tree loads normally |

  # Source: ET-24730, PROD-15817, GAP-2
  @todo
  Scenario Outline: Verify MeSH 2025 descriptor selection persistence and parent-child selection behaviour
    Given User opens Health Pages targeting on a new tactic
    When User performs "<ACTION>" and saves the tactic
    And User reopens the tactic
    Then The selection result is "<EXPECTED_RESULT>"
    Examples:
      | ACTION                                                       | EXPECTED_RESULT                                                        |
      | Selecting 10 descriptors across multiple tree levels         | Exactly 10 descriptors are saved and displayed with matching labels    |
      | Selecting 50 descriptors                                     | The selection count on reload equals 50 with no items silently dropped |
      | Selecting a parent node that has 20 or more children         | The count includes the parent plus all eligible children              |
      | Selecting a parent with 10 children then deselecting the parent | All 10 children are deselected and the targeting count decreases correctly |

  # Regression anchor: HT-5112 - Health Pops/Pops+/Pages failed to load together; HT-4185 - Keyword Populations disappearing after save
  # Source: ET-24730, HT-5112, HT-4185
  @todo
  Scenario: Verify Health targeting family loads and Keyword Population is unaffected after MeSH 2025 migration
    Given User creates a tactic with Health Pages, Health Pops and Health Pops+ targeting
    Then All three targeting types load successfully with a populated tree and no Milkshake-only loading state
    When User adds Keyword Population targeting, selects descriptors and saves the tactic
    Then The Keyword Population selections persist correctly and are unaffected by the Health Pages changes

  # Source: ET-24730, GAP-2
  @todo
  Scenario: Verify a saved tactic with a legacy MeSH descriptor loads without error after migration
    Given User opens an existing tactic saved before the MeSH migration that contains a legacy descriptor
    Then The tactic loads without error and the legacy descriptor either maps to a MeSH 2025 equivalent or is shown in a clear unresolved state
