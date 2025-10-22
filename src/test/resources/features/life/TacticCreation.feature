Feature: LIFE Regression - Verify below scenarios in Tactic creation flow
  1. Create multiple tactics
  2. Verify the availability of three tabs
  3. Verify header section of tactic displays correct status
  4. Verify user is able to add custom field

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign

  @regression
  Scenario Outline: Create multiple tactic and verify its tabs and status
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates new tactics and verifies it
      | Audience Group tactic |
      | Targeting Segment     |
      | Health Populations    |
    Then Verify that the tabs gets enabled only after saving tactics
      |Settings|
      |Creatives|
      |Debugger |
    And Verify the status of saved tactic


    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         |

  @regression
  Scenario Outline: Create new custom field in tactic and delete it
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates new tactics and verifies it
      | Targeting Segment  |
    Then User creates new custom field "<CUSTOM_NAME>" and verifies the same
    And User verifies if new custom field is visible in new and existing tactic
      | Targeting Segment  |
    #And User enters value in custom field and verifies if its not visible in other tactics
    Then User deletes the custom field

    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | CUSTOM_NAME |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Custom ID |