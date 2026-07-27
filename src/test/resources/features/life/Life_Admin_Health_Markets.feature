Feature: Life Admin - Health Markets SSP Deal Management
  1. Loads the Admin Health Markets section reflecting the current Premium Publishers and Medscape product state.
  2. Supports adding and removing SSP deals with search-by-typing in the SSP dropdown.
  3. Keeps the transitional Admin state without triggering the separate legacy targeting removal.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24704, PROD-14706
  @todo
  Scenario: Verify the Health Markets Admin section loads and supports SSP deal add, search and remove
    Given User navigates to Admin > Health Markets
    Then The Health Markets section loads with content reflecting the post-July 2025 state under Premium Publishers and Medscape
    # Framework Gap: Requires step definition for the Health Markets SSP dropdown add and search-by-typing in LifeSteps.java
    When User adds a deal using the SSP dropdown
    Then The deal is added and appears in the Health Markets deal list with no error
    When User starts typing a deal name in the SSP dropdown
    Then The dropdown filters to matching deals in real time as the user types
    When User types a term that matches no deal in the SSP dropdown
    Then The dropdown shows an empty or no-results state without erroring or freezing
    When User removes an existing deal from the Health Markets deal list
    Then The deal is removed and no longer appears for new tactics in Life DSP targeting

  # Source: ET-24704, PROD-14706, GAP-3, AMB-2
  @todo
  Scenario: Verify legacy targeting handling and scope isolation from ET-25045
    Given User navigates to Admin > Health Markets
    Then The legacy options Haymarket, Everyday Health, Conde Nast, Vice Media and AMC are absent or clearly marked as deprecated
    When User checks the Life DSP tactic targeting for Health Markets legacy options
    Then The legacy targeting options in the tactic UI are unchanged and the ET-25045 removal has not been triggered early

  # Regression anchor: Admin Health Markets changes must not affect other Admin sections
  # Source: ET-24704
  @todo
  Scenario: Verify other Admin sections are unaffected by the Health Markets changes
    Given User navigates through AM Settings and the Life Features table after the Health Markets deployment
    Then Both sections load correctly with no regression in other Admin areas
