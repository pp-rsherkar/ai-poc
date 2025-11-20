Feature: LIFE Regression - Verify below scenarios in Tactic creation flow
  1. Create multiple tactics
  2. Verify the availability of three tabs
  3. Verify header section of tactic displays correct status
  4. Verify user is able to add custom field

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @regression
  Scenario Outline: Create multiple tactics and verify its tabs and status
    When User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates below tactics under same line item and verifies it
      | Tactic Name           | Channel  | RuleType           |
      | Targeting Segment     | Email    | Health Population  |
      | Health Populations    | EHR      | NPI                |
      | Audience Group tactic | Standard | Behavioral Segment |
    Then Verify that below tabs gets enabled only after saving tactics
      | Settings  |
      | Creatives |
      | Debugger  |
      | Details   |
    And Verify the status of first tactic under line item is "Incomplete"
    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         |

  @regression
  Scenario Outline: Create new custom field in tactic and delete it
    When User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates below tactics under same line item and verifies it
      | Tactic Name           | Channel  | RuleType           |
      | Targeting Segment     | Email    | Health Population  |
    Then User creates new custom field "<CUSTOM_NAME>" and verifies the same
    And User verifies if new custom field is visible in new and existing tactic
      | Targeting Segment |
    #And User enters value in custom field and verifies if it's not visible in other tactics
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
  Scenario Outline: Verify deletion of Tactic from a Line Item
    When User clicks on create new Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates a new tactic with details "<TACTIC_NAME>" "<CHANNEL>"
    Then User deletes the tactic "<TACTIC_NAME>" and verifies it
    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | CHANNEL | TACTIC_NAME     |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Email   | Targeting-72739 |

  @regression
  Scenario Outline: Create tactic and enable those tactics through bulk action.
    When User clicks on create new Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    Then User creates a new tactic with details "<TACTIC_NAME>" "<CHANNEL>"
    And User enables tactic "<TACTIC_NAME>" through bulk action and verifies the status
    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | CHANNEL | TACTIC_NAME     |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Email   | Targeting-72838 |


  @regression
  Scenario Outline: To verify user is able to add frequency cap for a tactic
    When User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    Then User navigates to campaign
    Then User clicks frequency cap with details "<TIMES_PER>" "<SCOPE>" "<FREQ_VALUE>" and verifies it

#    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
#    Then Verify line item details are saved and user is navigated to the tactic page
#    Then User creates below tactics under same line item and verifies it
#      | Tactic Name       | Channel | RuleType          |
#      | Targeting Segment | Email   | Health Population |
#    Then Verify tactic details are saved and user is navigated to the settings tab
#    add flights in campaign and line item, verify that in tactic, add flight in tactic, verify all the options in flight dropdown
#   Then Frequency Cap section should be visible
#    And "Apply on Tactic Level" checkbox should be displayed and unchecked by default
#    And Frequency value input field should not be visible
#    Then User checks the "Apply on Tactic Level" checkbox
#    And User checks the "Apply on Tactic Level" checkbox
#    And User enters frequency values and clicks on Save button
#    Then Frequency cap values should be saved successfully
#    need to consider different liner item types ?
#    300 x day x Per Person on Campaign Level
#    600 x Time Per 1 hour Per Person on Line Item Level


    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | FREQ_VALUE | TIMES_PER | SCOPE          |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | 80         | week           | Per Household  |
      #| 100- Advertiser | Auto    | Regular | 20000     | Line      | 500         | 90         | hour(s)           | Per Household  |