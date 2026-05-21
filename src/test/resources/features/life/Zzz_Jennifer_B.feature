Feature: LIFE Regression - Navigate to Report Template

  @regression
  Scenario Outline: Navigate to Report Template
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    Then Verify the tabs displayed on the Report Templates page
    Examples:
      | USER  | TEMPLATE NAME | DIMENSIONS      | METRICS     |
      | Admin | Template      | Advertiser Name | Impressions |