Feature: LIFE Regression - Keyword Targeting Search Filter Alignment
  Keyword and Keyword Population targeting search filters the list to show only matching items, aligned with Media Planner.
  Search is case-insensitive, highlights matches, handles empty states, and preserves selection persistence on save.

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

  # Source: ET-24719 (R01, R02, GAP-1)
  @todo
  Scenario Outline: Keyword and Keyword Population search filters the list to matching items only
    When User opens the "<TARGETING_TYPE>" targeting section
    And User types "<TERM>" in the targeting search field
    # Framework Gap: Requires step definitions for keyword search filtering in LifeSteps.java
    Then The list filters to show only items containing "<TERM>" and hides non-matching items
    When User clears the search field
    Then The full unfiltered list is restored with no stale filter state
    Examples:
      | TARGETING_TYPE     | TERM           |
      | Keyword            | hypertension   |
      | Keyword Population | cardiovascular |

  # Source: ET-24719 (R03)
  @todo
  Scenario: Keyword targeting search behavior matches the Media Planner reference implementation
    When User opens the "Keyword" targeting section
    And User types "diabetes" in the targeting search field
    # Framework Gap: Requires step definitions for cross-surface search parity in LifeSteps.java
    Then The filtered list and result count match the Media Planner search for the same term
    And The "diabetes" text is highlighted within each filtered result

  # Source: ET-24719 (GAP-2, R04, edge)
  @todo
  Scenario Outline: Keyword search handles partial, case variation and no-match input
    When User opens the "Keyword" targeting section
    And User types "<TERM>" in the targeting search field
    # Framework Gap: Requires step definitions for search matching behavior in LifeSteps.java
    Then The search result is "<EXPECTED>"
    Examples:
      | TERM       | EXPECTED                                             |
      | card       | all items containing the partial string card         |
      | Hypertension | same results as lowercase hypertension (case-insensitive) |
      | xyzqrstabc | an empty state message with the field still functional |

  # Source: ET-24719 (HT-4185 regression)
  @todo
  Scenario: Regression - items selected via filtered search persist after tactic save
    When User opens the "Keyword" targeting section
    And User filters the list by "diabetes" and selects 5 items from the filtered results
    And User saves the tactic and reloads the tactic
    # Framework Gap: Requires step definitions for keyword selection persistence in LifeSteps.java
    Then Exactly 5 selected items persist and the count does not decrease
    And Health Pages targeting search behavior is unaffected by the Keyword search changes
