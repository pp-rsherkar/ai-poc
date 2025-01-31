  Feature: Enable Studio for an account

  Scenario Outline: Enable Studio for an Account
    Given user logged in as "<Internal User>"
    When user enable the studio for an account
    Then user should navigate to workspace permission
    Then user selects the workspace types and saves the setting
    Then the studio should be enabled for that account
    And user should be able to see the enabled workspaces for that account under Studio

    Examples:
      | Internal User |
      | nparab        |