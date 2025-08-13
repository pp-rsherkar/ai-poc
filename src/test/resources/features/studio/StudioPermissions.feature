Feature: Enable Studio for an account

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

  @regression
  Scenario Outline: Enable Studio permissions for an External User
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And user navigates to accounts page and selects the "<ACCOUNT_NAME>" account
    And user navigates to advertisers page under the selected account
    And user enables the <ADVERTISER_PERMISSIONS> permission for the "<ADVERTISER_NAME>" advertiser
    And user navigates to users page under the selected account
    And User selects the <USER_NAME> external user
    And User enables the <STUDIO_PERMISSIONS> permission for the external user
    When External user is logged in successfully in "Studio" application
    And External user selects the workspace
    Then External user should be able the see the <STUDIO_PERMISSIONS> permissiosn in the workspace

    Examples:
      |USER_NAME |STUDIO_PERMISSIONS         |ACCOUNT_NAME |ADVERTISER_PERMISSIONS|ADVERTISER_NAME|
      | hmtdemo  |Moments                    |HMT Demo     |Moments               |Demo W2O       |
      | hmtdemo  |IB health                  |HMT Demo     |IB health             |Demo W2O       |
      | hmtdemo  |Claims                     |HMT Demo     |Claims                |Demo W2O       |
      | hmtdemo  |Download SmartList NPIs    |HMT Demo     |                      |Demo W2O       |
      | hmtdemo  |Smart Action Webhook       |HMT Demo     |                      |Demo W2O       |
      | hmtdemo  |NPI Details                |HMT Demo     |                      |Demo W2O       |
      | hmtdemo  |NPI Professions            |HMT Demo     |                      |Demo W2O       |
      | hmtdemo  |Patient Details            |HMT Demo     |                      |Demo W2O       |
