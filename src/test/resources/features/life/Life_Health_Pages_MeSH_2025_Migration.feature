Feature: LIFE Regression - Health Pages MeSH 2025 Taxonomy Migration
  Health Pages targeting renders the MeSH 2025 13k taxonomy tree with dynamic margin handling for deep nesting.
  Search highlighting, selection persistence, and parent/child selection behave correctly across the larger taxonomy.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "500", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab

  # Source: ET-24730 (GAP-1, AMB-2)
  @todo
  Scenario: Health Pages targeting renders the MeSH 2025 taxonomy and excludes legacy-only descriptors
    When User opens the Health Pages targeting section
    # Framework Gap: Requires step definitions for MeSH 2025 tree rendering assertions in LifeSteps.java
    Then The Health Pages taxonomy tree loads the MeSH 2025 descriptors with 10 or more tier levels available on expansion
    When User searches for a descriptor removed in MeSH 2025
    Then The removed descriptor is not returned and no error is thrown

  # Source: ET-24730 (AMB-1, GAP-3)
  @todo
  Scenario: Deep tree expansion applies dynamic margin reduction without layout overflow
    When User opens the Health Pages targeting section
    And User expands a MeSH 2025 branch to 8 or more levels deep
    # Framework Gap: Requires step definitions for margin measurement and overflow checks in LifeSteps.java
    Then Each deeper level has a smaller left margin increment than its parent level
    And The tree remains visually contained with no horizontal overflow or text clipping

  # Source: ET-24730 (R02)
  @todo
  Scenario Outline: Search term highlighting persists and updates across expansion states
    When User opens the Health Pages targeting section
    And User types "<TERM>" in the Health Pages search field
    # Framework Gap: Requires step definitions for search highlight assertions in LifeSteps.java
    Then Matching descriptors show "<TERM>" visually highlighted and the highlight persists on parent expansion
    When User clears the search field and types "<NEW_TERM>"
    Then Highlights for "<TERM>" are cleared and "<NEW_TERM>" highlights appear correctly
    Examples:
      | TERM           | NEW_TERM |
      | cardiovascular | oncology |

  # Source: ET-24730 (R05)
  @todo
  Scenario Outline: Health Pages typeahead returns results or an empty state within acceptable time
    When User opens the Health Pages targeting section
    And User types "<QUERY>" in the Health Pages search field
    # Framework Gap: Requires step definitions for typeahead timing and empty state in LifeSteps.java
    Then The search returns "<OUTCOME>" within 2 seconds and the field remains functional
    Examples:
      | QUERY       | OUTCOME                          |
      | Hypertension | relevant MeSH 2025 results       |
      | xyzabc123    | a no-results empty state message |

  # Source: ET-24730 (HT-4185, R06, R07)
  @todo
  Scenario Outline: Health Pages selection persists exactly after save and reload
    When User opens the Health Pages targeting section
    And User selects "<COUNT>" MeSH 2025 descriptors across multiple tree levels
    And User saves the tactic and reopens the tactic
    # Framework Gap: Requires step definitions for Health Pages selection persistence in LifeSteps.java
    Then Exactly "<COUNT>" descriptors are saved and displayed with their MeSH 2025 labels intact
    Examples:
      | COUNT |
      | 10    |
      | 50    |

  # Source: ET-24730 (parent/child selection)
  @todo
  Scenario: Parent node selection and deselection updates child descriptor counts correctly
    When User opens the Health Pages targeting section
    And User selects a parent node that has 20 or more children
    # Framework Gap: Requires step definitions for parent/child selection counts in LifeSteps.java
    Then The selection count includes the parent and all eligible children
    When User deselects the parent node
    Then All child descriptors are deselected and the targeting count decreases correctly

  # Source: ET-24730 (HT-5112 regression)
  @todo
  Scenario: Regression - Health Pages, Health Pops and Health Pops+ all load after MeSH 2025 migration
    When User adds Health Pages, Health Pops and Health Pops+ targeting to the tactic
    # Framework Gap: Requires step definitions for shared health targeting load checks in LifeSteps.java
    Then All three targeting types load successfully with populated trees and no Milkshake-only loading state
    And Keyword Population targeting selections persist correctly after save and are unaffected
