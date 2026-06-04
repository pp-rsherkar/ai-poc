Feature: LIFE Regression - Create a Report Template

  @regression
  Scenario Outline: Create a Report Template
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User navigates to Report Templates page
    Then Verify the tabs displayed on the Report Templates page
    When User clicks on New Template
    Then Verify the tabs displayed on the Create New Template panel
    And Verify the delete button is disabled on the Create New Template panel
    When User enters the template details as "<TEMPLATE NAME>" "<DIMENSIONS>" "<METRICS>"
    Then Verify the selected dimensions and metrics under the Template Structure section
    When User saves the new template
    Then Verify new template is saved and displayed in the template list
    And Verify the details of the created template
    And Verify the delete button is enabled on the Edit Template panel
    Examples:
      | TEMPLATE NAME | DIMENSIONS      | METRICS     |
      | Template      | Advertiser Name | Impressions |

  @regression
  Scenario Outline: Add multiple dimensions and metrics from multiple categories during template creation
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User navigates to Report Templates page
    Then Verify the tabs displayed on the Report Templates page
    When User clicks on New Template
    Then Verify the tabs displayed on the Create New Template panel
    And Verify the delete button is disabled on the Create New Template panel
    When User enters the template details as "<TEMPLATE NAME>" "<DIMENSIONS>" "<METRICS>"
    Then Verify the selected dimensions and metrics under the Template Structure section
    When User saves the new template
    Then Verify new template is saved and displayed in the template list
    And Verify the details of the created template
    And Verify the delete button is enabled on the Edit Template panel
    Examples:
      | TEMPLATE NAME         | DIMENSIONS                                                                                                           | METRICS                                                          |
      | MultiCategoryTemplate | Advertiser Name, Campaign Name, NPI First Name, Device Type, Area code, Keywords, Deal Name, Age, Current Step Name, | Impressions, Clicks, Platform Fee, Complete Views, Midpoint Rate |
