Feature: Life Admin - Health Markets Updates

  1. Updates the Admin Health Markets section to reflect the post-July 2025 consolidation into Premium Publishers and Medscape.
  2. Supports adding deals through the SSP dropdown with search-by-typing.
  3. Keeps Admin changes scoped so they do not trigger the legacy targeting removal owned by ET-25045.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24704 (TC_01, TC_02, TC_03, TC_04, TC_07)
  @todo
  Scenario: Verify Admin Health Markets loads, SSP deal add with search-by-typing, and deal removal
    # Framework Gap: Requires page object + step definitions for Admin Health Markets SSP deal management in AdminSteps.java
    Given User navigates to the Administrative section and opens Health Markets
    Then The Health Markets section loads and reflects the post-July 2025 consolidated state
    When User starts typing a deal name in the SSP dropdown
    Then The dropdown filters to matching deals in real time as the user types
    When User selects a deal from the SSP dropdown and adds it
    Then The deal is added and appears in the Health Markets deal list with no error
    When User types a term that matches no deal in the SSP dropdown
    Then The dropdown shows an empty state with no error or freeze
    When User removes an existing deal from the Health Markets deal list
    Then The deal is removed and no longer appears in Life DSP targeting for new tactics

  # Source: ET-24704 (TC_05, TC_06, TC_08)
  @todo
  Scenario: Verify legacy option consolidation, scope boundary with ET-25045, and Admin isolation
    Given User navigates to the Administrative section and opens Health Markets
    Then Legacy options Haymarket, Everyday Health, Conde Nast, Vice Media, and AMC are absent or marked deprecated
    # Regression anchor: scope boundary - ET-25045 owns the Life DSP targeting removal
    When User checks the Life DSP tactic targeting for Health Markets legacy options
    Then The legacy targeting options in the tactic UI are unchanged and the ET-25045 removal is not triggered early
    When User navigates through AM Settings and the Life Features table after the Health Markets changes
    Then Both sections load correctly with no regression in other Admin areas
