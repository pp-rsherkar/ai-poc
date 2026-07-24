Feature: LIFE Regression - AM Settings Login Logout Permission Decoupling
  The Admin AM Settings Login/Logout section is visible to all users, decoupled from the VIEW PINTEREST SIGNIN permission.
  Individual provider sign-in buttons remain gated by their own permissions while the section container is ungated.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24705 (R01, R02, R03, R04)
  @todo
  Scenario Outline: Login/Logout section is always visible while provider buttons follow their own permissions
    When User navigates to Admin AM Settings with permission set "<PERMISSIONS>"
    # Framework Gap: Requires step definitions for AM Settings Login/Logout visibility in LifeSteps.java
    Then The Login/Logout section is visible
    And The Pinterest Sign-in button is "<PINTEREST>"
    And The LinkedIn Sign-in button is "<LINKEDIN>"
    And The TikTok sign-in option is visible with no permission gate
    Examples:
      | PERMISSIONS                              | PINTEREST   | LINKEDIN    |
      | VIEW PINTEREST SIGNIN and LinkedIn signin | visible     | visible     |
      | LinkedIn signin only                      | not visible | visible     |
      | no OAuth permissions                      | not visible | not visible |

  # Source: ET-24705 (R05 restored access)
  @todo
  Scenario: User without Pinterest permission can now access the LinkedIn sign-in button
    When User navigates to Admin AM Settings with permission set "LinkedIn signin only"
    # Framework Gap: Requires step definitions for restored LinkedIn access in LifeSteps.java
    Then The Login/Logout section is visible and the LinkedIn Sign-in button is visible even without the Pinterest permission

  # Source: ET-24705 (GAP-1, section container safety)
  @todo
  Scenario: The Login/Logout section container has no remaining permission gate
    When User navigates to Admin AM Settings with permission set "no OAuth permissions"
    # Framework Gap: Requires step definitions for section container gate checks in LifeSteps.java
    Then The Login/Logout section is visible with an empty or TikTok-only set of sign-in buttons

  # Source: ET-24705 (R10 regression, ET-24726 isolation)
  @todo
  Scenario: Regression - Pinterest OAuth still works and minimum size settings are not gated by Pinterest
    When User navigates to Admin AM Settings with permission set "VIEW PINTEREST SIGNIN and LinkedIn signin"
    And User clicks the Pinterest sign-in button
    # Framework Gap: Requires step definitions for Pinterest OAuth flow in LifeSteps.java
    Then The Pinterest OAuth flow initiates successfully and sign-in works end-to-end
    And Omnichannel minimum size settings behave per their own permissions and not the Pinterest permission
