  Feature: Enable Studio for an account

  Scenario: Enable Studio for an Account for internal users
    Given user logged in as "<Internal User>"
    When user enable the studio for an account
    Then user should navigate to workspace permission
    Then user selects the workspace types and saves the setting
    Then the studio should be enabled for that account
    Then user should be able to see the enabled workspaces for that account under Studio
    Then user disables the studio permission for an account
    And user should not be able to see the studio permission for that account

