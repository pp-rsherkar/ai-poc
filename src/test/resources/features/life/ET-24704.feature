Feature: Admin - Updates to Health Markets

  1. Updates the Admin > Health Markets section to reflect the post-July-2025 state where curated deals reside under Premium Publishers and Medscape.
  2. Supports SSP deal management including search-by-typing in the SSP dropdown when adding a deal, with a graceful zero-result state.
  3. Keeps ET-24704 scoped to the Admin layer so it does not trigger the legacy targeting removal owned by ET-25045, and stays isolated from other Admin sections.
  4. Each scenario walks a single continuous pass through Admin > Health Markets, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the Admin Health Markets SSP deal management and search-by-typing workflow
    Given User is an Admin user
    When User navigates to Admin > Health Markets
    Then the Health Markets section loads without error and reflects the post-July-2025 state (curated deals under Premium Publishers / Medscape)
    # GAP-1: parent epic PROD-14706 has no description - the ticket is the primary requirements source
    When User uses the SSP dropdown to add a deal
    Then the deal is added and appears in the Health Markets deal list with no error
    When User starts typing a deal name in the SSP dropdown
    Then the dropdown filters to matching deals in real time as User types
    # GAP-2 / AMB-2: search-by-typing was raised mid-development (Douglas Richards) - confirm it is in scope for this ticket
    When User types a term that matches no deal in the SSP dropdown
    Then the dropdown shows an empty/no-results state without erroring or freezing
    When User removes an existing deal from the Health Markets deal list
    Then the deal is removed and Life DSP targeting for Health Markets no longer shows that deal for new tactics

  @todo
  Scenario: Verify Admin Health Markets changes stay within scope and do not affect targeting UI or other Admin sections
    Given User is an Admin user on the Health Markets section
    Then legacy targeting options now consolidated (Haymarket, Everyday Health, Conde Nast, Vice Media, AMC) are absent or clearly marked deprecated
    When User checks Life DSP tactic targeting for Health Markets legacy options
    Then the legacy targeting options in the tactic UI are unchanged and the ET-25045 removal has not been triggered early
    # Scope boundary: ET-24704 is the Admin layer; ET-25045 owns the Life DSP targeting removal (not in this release)
    When User navigates through AM Settings and the Life Features table after the Health Markets deployment
    Then both sections load correctly with no regression in other Admin areas
