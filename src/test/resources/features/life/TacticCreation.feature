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
  Scenario Outline: User delete tactic from a line item
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates a new tactic with details "<TACTIC NAME>" "<CHANNEL>" and "<RULE_TYPE>"
    Then User deletes the tactic "<TACTIC NAME>" and verifies it

    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | CHANNEL | RULE_TYPE          | TACTIC NAME    |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Email   | Behavioral Segment | Targeting-0411 |


  @regression
  Scenario Outline: Create tactic and enable those tactics through bulk action.
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates a new tactic with details "<TACTIC NAME>" "<CHANNEL>" and "<RULE_TYPE>"
    And User enables all tactics through bulk action and verifies the status

    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | CHANNEL | RULE_TYPE          | TACTIC NAME    |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Email   | Behavioral Segment | Targeting-0411 |