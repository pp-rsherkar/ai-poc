Feature: LIFE Regression - Verify below scenarios in Tactic creation flow
  1. Create multiple tactics
  2. Verify the availability of three tabs
  3. Verify header section of tactic displays correct status
  4. Verify user is able to add custom field

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @regression @vp
  Scenario Outline: Create multiple tactics and verify its tabs and status
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates new tactics and verifies it
      | Tactic Name           | Channel  | RuleType           |
      | Audience Group tactic | Standard | Behavioral Segment |
      | Targeting Segment     | Email    | Health Population  |
      | Health Populations    | EHR      | NPI                |
    Then Verify that the tabs gets enabled only after saving tactics
      | Settings  |
      | Creatives |
      | Debugger  |
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
      | Targeting Segment |
    Then User creates new custom field "<CUSTOM_NAME>" and verifies the same
    And User verifies if new custom field is visible in new and existing tactic
      | Targeting Segment |
    And User enters value in custom field and verifies if it's not visible in other tactics
    Then User deletes the custom field
    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | CUSTOM_NAME |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Custom ID   |

  @regression
  Scenario Outline: Verify Base bid price and Max bid price populates correctly for a tactic
    When User clicks on Campaign Settings
    Then Verify user is on default bid settings page
    And  User gets Max Bid and Base Bid values
    And Navigate to Campaign Dashboard and clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    And  Verify Max Bid and Base Bid values on the tactic settings match with Campaign Settings values
    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Tactic      |


  @regression
  Scenario Outline: User delete tactic from a line item
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates a new tactic with details "<TACTIC NAME>" "<CHANNEL>"
    Then User deletes the tactic "<TACTIC NAME>" and verifies it

    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | CHANNEL | TACTIC NAME    |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Email   | Targeting-72838 |

  @regression
  Scenario Outline: Create tactic and enable those tactics through bulk action.
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates a new tactic with details "<TACTIC NAME>" "<CHANNEL>"
    And User enables all tactics through bulk action and verifies the status

    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | CHANNEL  | TACTIC NAME    |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Email    | Targeting-72838 |
