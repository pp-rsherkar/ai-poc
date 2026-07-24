Feature: LIFE Regression - Admin Health Markets Updates
  The Admin Health Markets section reflects the current product state where curated deals reside under Premium Publishers and Medscape.
  The SSP dropdown supports search-by-typing for adding deals, and legacy targeting cleanup remains out of scope.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24704 (R01, GAP-1)
  @todo
  Scenario: Admin Health Markets section loads reflecting the current product state
    When User navigates to Admin Health Markets
    # Framework Gap: Requires step definitions for the Admin Health Markets section in LifeSteps.java
    Then The Health Markets section loads without error with curated deals under Premium Publishers and Medscape

  # Source: ET-24704 (R02, R04, GAP-2)
  @todo
  Scenario Outline: SSP dropdown supports adding a deal and search-by-typing
    When User navigates to Admin Health Markets
    And User types "<QUERY>" in the SSP dropdown
    # Framework Gap: Requires step definitions for the SSP dropdown search in LifeSteps.java
    Then The dropdown result is "<EXPECTED>"
    Examples:
      | QUERY        | EXPECTED                                             |
      | Premium      | matching deals filter in real-time as the user types |
      | zzz_no_match | an empty state or no-results message with no freeze  |

  # Source: ET-24704 (R04 add deal)
  @todo
  Scenario: Adding and removing a deal via the SSP dropdown updates the Health Markets deal list
    When User navigates to Admin Health Markets
    And User adds a deal via the SSP dropdown
    # Framework Gap: Requires step definitions for Health Markets deal management in LifeSteps.java
    Then The deal is added and appears in the Health Markets deal list with no error
    When User removes an existing deal from the deal list
    Then The deal is removed and no longer shows for new tactics targeting Health Markets

  # Source: ET-24704 (GAP-3, DEP ET-25045 isolation)
  @todo
  Scenario: Admin Health Markets changes do not trigger the ET-25045 legacy targeting removal
    When User navigates to Admin Health Markets
    # Framework Gap: Requires step definitions for legacy targeting state checks in LifeSteps.java
    Then Legacy consolidated targeting options are absent or clearly marked as deprecated in Admin
    And The Life DSP tactic targeting for Health Markets legacy options is unchanged and the ET-25045 removal is not triggered early

  # Source: ET-24704 (R08 regression)
  @todo
  Scenario: Regression - other Admin sections are unaffected by Health Markets changes
    When User navigates through AM Settings and the Life Features table after the Health Markets deployment
    # Framework Gap: Requires step definitions for adjacent Admin section checks in LifeSteps.java
    Then Both sections load correctly with no regression
