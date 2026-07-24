Feature: Admin Health Markets - Curated Deal Management and SSP Deal Selection
  1. Verify the Admin Health Markets page reflects the consolidated state with all curated deals grouped under Premium Publishers and Medscape
  2. Verify deals can be added through the SSP dropdown and appear in the Health Markets deal list
  3. Verify the SSP dropdown supports search by typing to filter deals in real time

  Background:
    Given This scenario will be executed in the "Demo" environment as a "Admin"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to Administrative section
    # Framework Gap: Requires step definitions for navigating to the Admin Health Markets section in LifeSteps.java
    And User navigates to Health Markets section

  # Source: ET-24704
  @todo
  Scenario: Admin Health Markets page loads reflecting the post-July-2025 consolidated state
    # Framework Gap: Requires step definitions for verifying the Health Markets page load in LifeSteps.java
    Then Verify the Health Markets page is displayed
    # Framework Gap: Requires step definitions for verifying curated deals grouping in LifeSteps.java
    And Verify all curated deals are listed under "Premium Publishers" and "Medscape"

  # Source: ET-24704
  @todo
  Scenario: Add a deal through the SSP dropdown and confirm it appears in the deal list
    # Framework Gap: Requires step definitions for opening the Add Deal panel on Health Markets in LifeSteps.java
    When User clicks the Add Deal button on the Health Markets page
    # Framework Gap: Requires step definitions for selecting an SSP from the SSP dropdown in LifeSteps.java
    And User selects "Medscape" from the SSP dropdown
    # Framework Gap: Requires step definitions for selecting a deal under a publisher group in LifeSteps.java
    And User selects the deal "Premium Publishers Deal" and saves the deal
    # Framework Gap: Requires step definitions for verifying a deal in the Health Markets deal list in LifeSteps.java
    Then Verify the deal "Premium Publishers Deal" appears in the Health Markets deal list

  # Source: ET-24704
  @todo
  Scenario Outline: Search by typing in the SSP dropdown filters deals in real time
    # Framework Gap: Requires step definitions for opening the Add Deal panel on Health Markets in LifeSteps.java
    When User clicks the Add Deal button on the Health Markets page
    # Framework Gap: Requires step definitions for typing a search term in the SSP dropdown in LifeSteps.java
    And User types "<SEARCH_TERM>" in the SSP dropdown search field
    # Framework Gap: Requires step definitions for verifying real-time filtered SSP results in LifeSteps.java
    Then Verify the SSP dropdown displays deals matching "<EXPECTED_DEAL>"
    Examples:
      | SEARCH_TERM | EXPECTED_DEAL      |
      | Medscape    | Medscape           |
      | Premium     | Premium Publishers |
      | Med         | Medscape           |

  # Source: ET-24704
  @todo
  Scenario: SSP dropdown with no matching search term shows an empty state gracefully
    # Framework Gap: Requires step definitions for opening the Add Deal panel on Health Markets in LifeSteps.java
    When User clicks the Add Deal button on the Health Markets page
    # Framework Gap: Requires step definitions for typing a search term in the SSP dropdown in LifeSteps.java
    And User types "nonexistentdeal" in the SSP dropdown search field
    # Framework Gap: Requires step definitions for verifying the SSP dropdown empty state in LifeSteps.java
    Then Verify the SSP dropdown shows an empty state message "No deals found"

  # Source: ET-24704
  @todo
  Scenario Outline: Legacy consolidated targeting options are absent or marked deprecated in the SSP dropdown
    # Framework Gap: Requires step definitions for opening the Add Deal panel on Health Markets in LifeSteps.java
    When User clicks the Add Deal button on the Health Markets page
    # Framework Gap: Requires step definitions for typing a search term in the SSP dropdown in LifeSteps.java
    And User types "<LEGACY_OPTION>" in the SSP dropdown search field
    # Framework Gap: Requires step definitions for verifying a legacy option is absent or deprecated in LifeSteps.java
    Then Verify the option "<LEGACY_OPTION>" is either absent or marked as "Deprecated" in the SSP dropdown
    Examples:
      | LEGACY_OPTION   |
      | Haymarket       |
      | Everyday Health |
      | Conde Nast      |
      | Vice Media      |
      | AMC             |

  # Source: ET-24704
  @todo
  Scenario: Health Markets updates do not remove the legacy targeting UI reserved for ET-25045
    # Framework Gap: Requires step definitions for verifying the legacy targeting UI remains available in LifeSteps.java
    Then Verify the legacy targeting UI section remains available on the Health Markets page
    And Verify no legacy targeting controls are disabled by the current release

  # Source: ET-24704
  @todo
  Scenario: Removing a deal updates the Health Markets deal list and excludes it from new tactics
    # Framework Gap: Requires step definitions for removing a deal from the Health Markets list in LifeSteps.java
    When User removes the deal "Premium Publishers Deal" from the Health Markets deal list
    # Framework Gap: Requires step definitions for verifying a deal is absent from the Health Markets list in LifeSteps.java
    Then Verify the deal "Premium Publishers Deal" is no longer shown in the Health Markets deal list
    # Framework Gap: Requires step definitions for verifying deal availability for new tactics in LifeSteps.java
    And Verify the deal "Premium Publishers Deal" is not available when adding it to a new tactic

  # Source: ET-24704
  @todo
  Scenario: Other Admin sections remain unaffected by the Health Markets updates
    # Framework Gap: Requires step definitions for navigating to and verifying the AM Settings section in LifeSteps.java
    When User navigates to the AM Settings section
    Then Verify the AM Settings section loads and functions as expected
    # Framework Gap: Requires step definitions for verifying the Life Features table in LifeSteps.java
    And Verify the Life Features table is displayed with its configured feature rows
