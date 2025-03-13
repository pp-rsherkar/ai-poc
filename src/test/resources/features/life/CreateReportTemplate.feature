Feature: LIFE Regression - Create a Report Template

  @jenkins
  Scenario Outline: Create a Report Template
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to Report Templates page
    Then Verify the tabs displayed on the Report Templates page
    When User clicks on New Template
    Then Verify the tabs displayed on the Create New Template panel
    When User enters the template details as "<TEMPLATE NAME>" "<DIMENSIONS>" "<METRICS>"
    Then Verify the selected dimensions and metrics under the Template Structure section
    When User saves the new template
    Then Verify new template is saved and displayed in the template list

    Examples:
      | USER  | TEMPLATE NAME | DIMENSIONS      | METRICS     |
      | Admin | Template      | Advertiser Name | Impressions |
