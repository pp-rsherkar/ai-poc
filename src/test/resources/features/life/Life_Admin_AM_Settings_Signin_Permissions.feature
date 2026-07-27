Feature: Life Admin - AM Settings Login/Logout Sign-in Permission Decoupling
  1. Shows the AM Settings Login/Logout section to all users regardless of the VIEW PINTEREST SIGNIN permission.
  2. Keeps each platform sign-in button gated by its own permission, with TikTok ungated.
  3. Preserves the Pinterest OAuth flow for permitted accounts.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24705, PROD-15754
  @todo
  Scenario Outline: Verify the Login/Logout section and platform sign-in buttons follow their own permissions
    Given User has the "<PERMISSION_SETUP>" configuration
    When User navigates to Admin > AM Settings
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | PERMISSION_SETUP                                   | EXPECTED_RESULT                                                                       |
      | without VIEW PINTEREST SIGNIN                      | The Login/Logout section is visible and the Pinterest Sign-in button is not visible    |
      | with VIEW PINTEREST SIGNIN                         | The Login/Logout section is visible and the Pinterest Sign-in button is visible within it |
      | with the LinkedIn sign-in permission              | The Login/Logout section is visible and the LinkedIn Sign-in button is visible within it |
      | without the LinkedIn sign-in permission           | The Login/Logout section is visible and the LinkedIn Sign-in button is not visible     |
      | without VIEW PINTEREST SIGNIN but with LinkedIn   | The Login/Logout section is visible and the previously blocked LinkedIn button is now accessible |
      | any account for TikTok sign-in                    | The Login/Logout section is visible and the TikTok sign-in option is visible with no permission gate |
      | no OAuth permissions at all                       | The Login/Logout section is visible with an empty state or only the ungated TikTok option |

  # Source: ET-24705, PROD-15754
  @todo
  Scenario: Verify the Login/Logout section container itself is not gated by any permission
    Given User has an account with no special permissions
    # Framework Gap: Requires step definition to assert the AM Settings Login/Logout section container visibility in LifeSteps.java
    When User navigates to Admin > AM Settings
    Then The Login/Logout section is visible with no remaining permission gate on the section container

  # Regression anchor: fix must not overcorrect; Pinterest OAuth must still work and minimum size settings must not be Pinterest-gated
  # Source: ET-24705, PROD-15754, ET-24726
  @todo
  Scenario: Verify Pinterest OAuth still works and minimum size settings are not Pinterest-gated
    Given An account with the VIEW PINTEREST SIGNIN permission
    When User navigates to Admin > AM Settings and clicks the Pinterest Sign-in button
    Then The Pinterest OAuth flow initiates and sign-in works end to end
    When User without VIEW PINTEREST SIGNIN navigates to any omnichannel minimum size settings
    Then The minimum size settings behave per their own permissions and are not blocked by the Pinterest permission
