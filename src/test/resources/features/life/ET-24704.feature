Feature: Life Admin - Health Markets Updates and SSP Deal Management

  1. Aligns the Admin > Health Markets section with the current product state, with curated deals consolidated under Premium Publishers / Medscape.
  2. Supports adding and removing deals via the SSP dropdown, including search-by-typing to filter matching deals.
  3. Keeps Admin-side Health Markets changes scoped to Admin, without triggering the separate Life DSP legacy targeting removal.
  4. Each scenario walks a single continuous pass through the Admin Health Markets section, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the Health Markets section loads, adds a deal, and searches the SSP dropdown
    Given User navigates to Admin > Health Markets
    # Framework Gap: Requires step definitions for the Admin Health Markets section in LifeSteps.java
    Then the Health Markets section loads without error and reflects the post-July 2025 state with curated deals under Premium Publishers / Medscape
    When User uses the SSP dropdown to add a deal
    Then the deal is added successfully and appears in the Health Markets deal list
    When User starts typing a deal name in the SSP dropdown
    Then the dropdown filters to show matching deals as User types
    # GAP-2 / AMB-2: confirm search-by-typing is in scope for this story (Douglas Richards comment)
    When User types a term that matches no deal in the SSP dropdown
    Then the dropdown shows an empty or "no results" state without an error or freeze

  @todo
  Scenario: Verify legacy Health Markets options are consolidated and removing a deal propagates to targeting
    Given User navigates to Admin > Health Markets
    Then legacy options Haymarket, Everyday Health, Conde Nast, Vice Media, and AMC are absent or clearly marked as deprecated
    When User removes an existing deal from the Health Markets deal list
    Then the deal is removed from the list and Life DSP targeting for Health Markets no longer shows that deal for new tactics

  @todo
  Scenario: Verify Admin Health Markets changes are scoped and do not regress other Admin sections
    Given User checks Life DSP tactic targeting for Health Markets legacy options
    Then the legacy targeting options in the tactic UI are unchanged and the ET-25045 removal has not been triggered early
    # Regression anchor: scope boundary - ET-24704 (Admin) is separate from ET-25045 (Life DSP targeting removal)
    When User navigates through AM Settings and the Life Features table after the Health Markets deployment
    Then both sections load correctly with no regression in other Admin areas
