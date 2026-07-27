Feature: LIFE Regression - Admin panel permissions, animation management and Health Markets configuration
  1. Omnichannel Audiences minimum size view and edit permissions at global and account level
  2. Milkshake loader animation upload, scheduling, preview and management
  3. Deprecation of Life Features permissions from the Admin table
  4. Admin Health Markets SSP deal management and search-by-typing

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User navigates to the Administrative section

  # Source: ET-24726
  @todo
  Scenario Outline: Verify Omnichannel Audiences minimum size visibility and edit control at "<LEVEL>" level for permission "<PERMISSION>"
    # Framework Gap: Requires step definitions for Admin > Omnichannel minimum size settings in LifeSteps.java
    When User opens the Omnichannel Audiences minimum size settings at "<LEVEL>" level with permission "<PERMISSION>"
    Then The settings visibility is "<VISIBILITY>" and the edit controls are "<EDIT_STATE>"
    And A "<PLATFORM>" minimum size value of "<VALUE>" saves without error and persists on reload
    And An account with no explicit account-level value inherits the global minimum size
    And The "VIEW PINTEREST SIGNIN" permission does not inadvertently gate the minimum size settings
    Examples:
      | LEVEL   | PERMISSION | VISIBILITY | EDIT_STATE | PLATFORM | VALUE    |
      | Global  | VIEW only  | visible    | read-only  | Meta     | 100      |
      | Global  | EDIT       | visible    | editable   | Meta     | 0        |
      | Global  | none       | hidden     | none       | Meta     | 100      |
      | Account | EDIT       | visible    | editable   | TikTok   | 10000000 |

  # Source: ET-24716
  @todo
  Scenario Outline: Verify milkshake animation upload, scheduling, preview and management in Admin for file "<FILE>"
    # Framework Gap: Requires step definitions for Admin > Animation Upload UI in LifeSteps.java
    When User uploads a single animation file "<FILE>" via the Animation Upload UI
    Then The upload result is "<RESULT>" and only one file can be selected at a time
    And A valid animation previews inline and auto-plays with no pause or resume control
    And A valid animation schedules for the future date "<SCHEDULE_DATE>" and a past date is rejected with a validation error
    And The scheduled animation is served as the Life DSP loader on its activation date and a midnight rollover activates it without delay
    And An existing scheduled animation can be edited and deleted and deleting the active animation falls back gracefully with no JavaScript error
    And A non-developer Design team user completes the upload and schedule flow without developer assistance
    And A 5MB SVG either uploads within acceptable time or shows a clear file size error and concurrent uploads by two Admin users do not corrupt the list
    Examples:
      | FILE                  | RESULT   | SCHEDULE_DATE |
      | milkshake_holiday.svg | accepted | 2026-12-25    |
      | milkshake_promo.png   | rejected | 2026-12-25    |

  # Source: ET-24715, ET-24713
  @todo
  Scenario Outline: Verify deprecated permission "<PERMISSION>" is removed from the Life Features table without side effects
    # Framework Gap: Requires step definitions for Admin > Life Features permission table in LifeSteps.java
    When User opens the Life Features table in the Admin panel
    Then The "<PERMISSION>" permission row is absent and searching for "<PERMISSION>" returns no results
    And Permissions adjacent to the removed row still toggle correctly and the table renders with no blank or phantom rows
    And An account that previously held "<PERMISSION>" can still complete "<WORKFLOW>" with no permission error
    And After both deprecations the table row count is reduced by exactly 2 with no cross-contamination between the two removals
    Examples:
      | PERMISSION                    | WORKFLOW                |
      | Creative Name BM              | tactic create and save  |
      | Line Item Creative Separation | line item configuration |

  # Source: ET-24704
  @todo
  Scenario Outline: Verify Admin Health Markets SSP deal management and search-by-typing
    # Framework Gap: Requires step definitions for Admin > Health Markets SSP deal management in LifeSteps.java
    When User opens Admin Health Markets and adds a deal via the SSP dropdown using search term "<SEARCH>"
    Then The SSP dropdown filters deals in real time and an unmatched term "<NO_MATCH>" shows an empty state with no error
    And The selected deal is added to the deal list and the section reflects the post-July 2025 Premium Publishers and Medscape state
    And Legacy targeting options "<LEGACY>" are not shown as active and the Life DSP tactic targeting UI is left unchanged for ET-25045
    And Removing a deal updates the deal list and other Admin sections such as AM Settings and the Life Features table remain unaffected
    Examples:
      | SEARCH     | NO_MATCH   | LEGACY                                                  |
      | PulsePoint | zzznomatch | Haymarket, Everyday Health, Condé Nast, Vice Media, AMC |
