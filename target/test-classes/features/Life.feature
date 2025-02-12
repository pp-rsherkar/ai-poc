Feature: LIFE Regression - Create a campaign, Add tactics, generate a report

  Background: Setup test environment for feature execution
    Given This feature will be executed in the "Demo" environment

  @purvesh
  Scenario Outline: Create a Campaign
    Given Life application is logged in as "<USER>"
    And User navigates to the Campaign Dashboard
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    And User selects the "<CHANNEL>" channel, configures the targeting rules, and saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    Then Verify creative details are saved and the campaign is in running state
    Then Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name

    Examples:
      |USER |ADVERTISER               |CP_NAME |CP_TYPE|CP_BUDGET|LINE_NAME  |LINE_BUDGET|TACTIC_NAME|CHANNEL         |CREATIVE       |
      |Admin|01- Advertiser           |Auto    |Regular|20000    |Line       |500        |Tactic     |Standard        |Auto_Creative  |
      #|Admin|00CacheTestAdvertise232n |Test    |Regular|25000    |Line Item_1|1200       |New_Tactic |Display Advanced|Sequential_Auto|

#  @regression
#  Scenario Outline: API Sample Test
#    Given I call "<apiName>" with parameters "<param1>" & "<param2>"
#    Then Verify response have "<statusCode>" & "<expected1>" & "<expected2>"
#    Examples:
#      | apiName | param1 | param2 | statusCode | expected1 | expected2 |
#      | GET     | 1      | 2      | 404        | 1         | 2         |
#      | POST    | 1      | 2      | 404        | 1         | 2         |

#  @regression
#  Scenario Outline: Create a Report Template
#    Given Life application is logged in as "<USER>"
#    And User navigates to the Campaign Dashboard
#    And User navigates to Report Templates page
#    Then Verify the tabs displayed on the Report Templates page
#    When User clicks on New Template
#    Then Verify the tabs displayed on the Create New Template panel
#    When User enters the template details as "<TEMPLATE NAME>" "<DIMENSIONS>" "<METRICS>" -- metrics -- impressions and clicks
#    Then Verify the selected dimensions and metrics under the Template Structure section
#    When User saves the new template
#    Then Verify new template is saved and displayed in the template list -- make sure only 1 row is available and name should be exact match
#    Then Verify new template details -

#    Examples:
#      |USER  |TEMPLATE NAME |DIMENSIONS |METRICS |
#      |Admin |Auto_Template |           |        |
