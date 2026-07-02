Feature: LIFE Regression - Test Jackpot Validation

  @todo
  Scenario Outline: Verify Campaign Dashboard loads successfully for different roles
    Given This scenario will be executed in the "<ENVIRONMENT>" environment as a "User"
    And "Life" application is logged in successfully with Account "<USER>"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    Then Verify the campaign list is displayed with at least one entry
    And Verify the dashboard filter options are visible

    Examples:
      | ENVIRONMENT | USER                       |
      | Demo        | automation@pulsepoint      |
      | Demo        | admin@pulsepoint           |

  @todo
  Scenario: Verify Campaign Dashboard displays correct column headers
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    Then Verify the following column headers are displayed on the Campaign Dashboard
      | Column Header   |
      | Campaign Name   |
      | Status          |
      | Start Date      |
      | End Date        |
