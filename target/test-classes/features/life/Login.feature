Feature: LIFE Regression - Verify Login Flow

  @regression
  Scenario: Verify successful login and landing on Campaign Dashboard
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    Then Verify Campaign Dashboard is displayed with title "Campaigns"
