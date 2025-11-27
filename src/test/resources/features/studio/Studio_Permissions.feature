Feature: Enable Studio permissions for an account,advertiser and external users

  1. This Feature verifies the ability to enable Studio permissions for an account and external users.
  2. It includes scenarios for both internal users and external users, ensuring that the Studio permissions can be granted and verified correctly.
  3. It covers logging in, navigating through accounts, advertisers, and users, enabling permissions, and confirming that the external user can see the assigned Studio permissions in the workspace after logging in.
  4. Multiple permission types and advertisers are validated using example data.

  @regression
  Scenario Outline: Enable Studio for an Account for internal users
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User enables the studio for "<ACCOUNT_NAME>" account
    And User navigates to workspace permissions
    When User selects the workspace types and saves the settings
    Then Studio should be enabled for that account
    Then User should be able to see the enabled workspaces for "<ACCOUNT_NAME>" account under Studio
    When User disables the studio permission for "<ACCOUNT_NAME>" account
    Then User should not be able to see the studio permission for that account

    Examples:
      | ACCOUNT_NAME |
      | 100Plus      |

  Scenario Outline: Enable Studio permissions for an External User
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to accounts page and selects the "<ACCOUNT_NAME>" account
    And User navigates to advertisers page under the selected account
    And User enables the "<ADVERTISER_PERMISSIONS>" permission for the "<ADVERTISER_NAME>" advertiser
    And User navigates to users page under the selected account
    And User selects the "<USER_NAME>" external user
    And User enables the "<STUDIO_PERMISSIONS>" permission for the external user
    When External user is logged in successfully in "Studio" application
    And  External user selects the workspace
    Then External user should be able to see the "<STUDIO_PERMISSIONS>" permission in the workspace

    Examples:
      | USER_NAME | STUDIO_PERMISSIONS      | ACCOUNT_NAME | ADVERTISER_PERMISSIONS | ADVERTISER_NAME |
      | hmtdemo   | Moments                 | HMT Demo     | Moments                | Demo W2O        |
      | hmtdemo   | IB health               | HMT Demo     | IB health              | Demo W2O        |
      | hmtdemo   | Claims                  | HMT Demo     | Claims                 | Demo W2O        |
      | hmtdemo   | Download SmartList NPIs | HMT Demo     |                        | Demo W2O        |
      | hmtdemo   | Smart Action Webhook    | HMT Demo     |                        | Demo W2O        |
      | hmtdemo   | NPI Details             | HMT Demo     |                        | Demo W2O        |
      | hmtdemo   | NPI Professions         | HMT Demo     |                        | Demo W2O        |
      | hmtdemo   | Patient Details         | HMT Demo     |                        | Demo W2O        |
