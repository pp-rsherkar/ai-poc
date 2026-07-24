Feature: Life Admin AM Settings - Login/Logout Section Visibility and Provider Sign-In Permission Gating

  1. The AM Settings Login/Logout section is visible to every internal user regardless of the OAuth sign-in permissions they hold.
  2. The Pinterest sign-in button stays gated by the VIEW PINTEREST SIGNIN permission, the LinkedIn sign-in button is gated by its own dedicated permission, and the TikTok sign-in button is available to all users.
  3. Internal users who hold the LinkedIn permission can access LinkedIn sign-in even when they do not hold the Pinterest permission.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    # Framework Gap: Requires step definitions for navigating to the Admin > AM Settings page in LifeSteps.java
    And User navigates to Admin > AM Settings

  # Source: ET-24705
  @todo
  Scenario Outline: Verify Login/Logout section visibility and provider sign-in button permission gating
    # Framework Gap: Requires step definitions for setting an internal user permission grant state in LifeSteps.java
    Given Internal user permission "<permission>" is set to "<permission_held>"
    # Framework Gap: Requires step definitions for viewing the AM Settings Login/Logout section in LifeSteps.java
    When User views the AM Settings Login/Logout section
    # Framework Gap: Requires step definitions for verifying the Login/Logout section visibility in LifeSteps.java
    Then The "Login/Logout" section is visible
    # Framework Gap: Requires step definitions for verifying a provider sign-in button visibility in LifeSteps.java
    And The "<provider>" sign-in button is "<button_visible>"

    Examples:
      | provider  | permission                                                             | permission_held | button_visible |
      | Pinterest | VIEW PINTEREST SIGNIN                                                   | granted         | visible        |
      | Pinterest | VIEW PINTEREST SIGNIN                                                   | revoked         | hidden         |
      | LinkedIn  | AUDIENCE MANAGER PERMISSION FOR INTERNAL USERS TO VIEW LINKED-IN SIGNIN | granted         | visible        |
      | LinkedIn  | AUDIENCE MANAGER PERMISSION FOR INTERNAL USERS TO VIEW LINKED-IN SIGNIN | revoked         | hidden         |
      | TikTok    | NONE                                                                   | none            | visible        |

  # Source: ET-24705
  @todo
  Scenario: Restore LinkedIn sign-in access for an internal user who lacks the Pinterest permission
    # Framework Gap: Requires step definitions for setting an internal user permission grant state in LifeSteps.java
    Given Internal user permission "VIEW PINTEREST SIGNIN" is set to "revoked"
    # Framework Gap: Requires step definitions for setting an internal user permission grant state in LifeSteps.java
    And Internal user permission "AUDIENCE MANAGER PERMISSION FOR INTERNAL USERS TO VIEW LINKED-IN SIGNIN" is set to "granted"
    # Framework Gap: Requires step definitions for viewing the AM Settings Login/Logout section in LifeSteps.java
    When User views the AM Settings Login/Logout section
    # Framework Gap: Requires step definitions for verifying the Login/Logout section visibility in LifeSteps.java
    Then The "Login/Logout" section is visible
    # Framework Gap: Requires step definitions for verifying a provider sign-in button visibility in LifeSteps.java
    And The "Pinterest" sign-in button is "hidden"
    # Framework Gap: Requires step definitions for verifying a provider sign-in button visibility in LifeSteps.java
    And The "LinkedIn" sign-in button is "visible"
    # Framework Gap: Requires step definitions for starting a provider sign-in flow in LifeSteps.java
    And User can start the "LinkedIn" sign-in flow

  # Source: ET-24705
  @todo
  Scenario: View the Login/Logout section for an account holding no OAuth sign-in permissions
    # Framework Gap: Requires step definitions for setting an internal user permission grant state in LifeSteps.java
    Given Internal user permission "VIEW PINTEREST SIGNIN" is set to "revoked"
    # Framework Gap: Requires step definitions for setting an internal user permission grant state in LifeSteps.java
    And Internal user permission "AUDIENCE MANAGER PERMISSION FOR INTERNAL USERS TO VIEW LINKED-IN SIGNIN" is set to "revoked"
    # Framework Gap: Requires step definitions for viewing the AM Settings Login/Logout section in LifeSteps.java
    When User views the AM Settings Login/Logout section
    # Framework Gap: Requires step definitions for verifying the Login/Logout section visibility in LifeSteps.java
    Then The "Login/Logout" section is visible and not gated by any remaining permission check
    # Framework Gap: Requires step definitions for verifying a provider sign-in button visibility in LifeSteps.java
    And The "Pinterest" sign-in button is "hidden"
    # Framework Gap: Requires step definitions for verifying a provider sign-in button visibility in LifeSteps.java
    And The "LinkedIn" sign-in button is "hidden"
    # Framework Gap: Requires step definitions for verifying a provider sign-in button visibility in LifeSteps.java
    And The "TikTok" sign-in button is "visible"

  # Source: ET-24705
  @todo
  Scenario: Complete the Pinterest OAuth sign-in flow for an account holding the Pinterest permission
    # Framework Gap: Requires step definitions for setting an internal user permission grant state in LifeSteps.java
    Given Internal user permission "VIEW PINTEREST SIGNIN" is set to "granted"
    # Framework Gap: Requires step definitions for viewing the AM Settings Login/Logout section in LifeSteps.java
    When User views the AM Settings Login/Logout section
    # Framework Gap: Requires step definitions for verifying a provider sign-in button visibility in LifeSteps.java
    Then The "Pinterest" sign-in button is "visible"
    # Framework Gap: Requires step definitions for completing a provider OAuth sign-in flow in LifeSteps.java
    When User completes the "Pinterest" OAuth sign-in flow
    # Framework Gap: Requires step definitions for verifying a provider sign-in completion in LifeSteps.java
    Then The "Pinterest" account is signed in successfully

  # Source: ET-24705, ET-24726
  @todo
  Scenario: Confirm Omnichannel minimum-size settings are independent of the Pinterest permission
    # Framework Gap: Requires step definitions for setting an internal user permission grant state in LifeSteps.java
    Given Internal user permission "VIEW PINTEREST SIGNIN" is set to "revoked"
    # Framework Gap: Requires step definitions for viewing the AM Settings Omnichannel minimum-size settings in LifeSteps.java
    When User views the AM Settings Omnichannel minimum-size settings
    # Framework Gap: Requires step definitions for verifying the Omnichannel minimum-size settings in LifeSteps.java
    Then The Omnichannel minimum-size settings are visible and editable
