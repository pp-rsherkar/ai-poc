Feature: POC on healing locators if it gives exceptions and to find the alternate locators for the elements which are failing due to stale element exception or any other exception.


  @e2e
  Scenario Outline: Verify comments addition on Campaign Dashboard and validate it on Campaign, Line Item and Tactic pages
    Given This scenario is executing in the "Demo" environment as aa "User"
    And "Life" application is logged in with Account as "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with a title as "Campaigns"
    And User clicks on Create Campaign button
    When User enters the campaign details such as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the Line Item page
    When User enters the line item details such as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the Tactic page
    When User enters the tactic details such as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the Settings tab
    And User selects the "<CHANNEL>" as channel type
    And User selects "<RULE_TYPE>" as rule type and configures the Targeting rules, and saves the settings
    Then Verify settings details are saved and user is navigated to the Creatives tab
    And User assigns the Existing creative named "<CREATIVE>", enables the tactic and saves the changes
    Then Verify creative details are saved and the Campaign is in running state
    Then Verify the newly created campaign details in the Campaign list: Campaign name, Line item name and Tactic name
    Examples:
      | USER  | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | CREATIVE            | REPORT_TEMPLATE | TEMPLATE NAME | DIMENSIONS                    | METRICS            | RULE_TYPE          |
      | Admin | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Tactic      | Display Advanced | Automation_Creative | E2E Report      | Template      | Advertiser Name,Campaign Name | Impressions,Clicks | Behavioral Segment |