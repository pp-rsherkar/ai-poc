Feature: Life Admin - AM Settings Login/Logout Permission Decoupling

  1. Decouples the AM Settings Login/Logout section visibility from the VIEW PINTEREST SIGNIN permission.
  2. Keeps the Pinterest and LinkedIn sign-in buttons gated by their own permissions.
  3. Leaves TikTok sign-in ungated and visible to all users.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24705 (TC_01, TC_02, TC_03, TC_04, TC_05, TC_06, TC_07)
  @todo
  Scenario Outline: Verify AM Settings Login/Logout section and sign-in button visibility by permission
    # Framework Gap: Requires page object + step definitions for Admin AM Settings Login/Logout in AdminSteps.java
    Given User navigates to Admin AM Settings with the "<PERMISSIONS>" permission configuration
    Then The Login/Logout section is visible
    And The "<BUTTON>" sign-in button is "<BUTTON_VISIBILITY>"
    Examples:
      | PERMISSIONS                                    | BUTTON    | BUTTON_VISIBILITY |
      | without VIEW PINTEREST SIGNIN                  | Pinterest | not visible       |
      | with VIEW PINTEREST SIGNIN                     | Pinterest | visible           |
      | with LinkedIn signin permission                | LinkedIn  | visible           |
      | without LinkedIn signin permission             | LinkedIn  | not visible       |
      | without Pinterest but with LinkedIn permission | LinkedIn  | visible           |
      | any account                                    | TikTok    | visible           |

  # Source: ET-24705 (TC_08, TC_09, TC_10, TC_11)
  @todo
  Scenario: Verify no residual gate on the section, empty state, and Pinterest OAuth regression
    Given User navigates to Admin AM Settings with no special permissions
    Then The Login/Logout section is visible with no remaining permission gate on the section container
    When User navigates to Admin AM Settings with no Pinterest, LinkedIn, or TikTok permissions
    Then The Login/Logout section is visible showing an empty state or TikTok-only options
    # Regression anchor: OAuth flow must still work for authorized users
    When User with VIEW PINTEREST SIGNIN clicks the Pinterest sign-in button
    Then The Pinterest OAuth flow initiates and completes successfully
    When User without VIEW PINTEREST SIGNIN opens the Omnichannel minimum size settings
    Then The minimum size settings behave per their own permission and are not blocked by the Pinterest permission
