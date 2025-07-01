Feature: Enable Studio for an account

  @regression
  Scenario Outline: Enable Studio for an Account for internal users
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Life" application is logged in successfully with Account "UAT_account"
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