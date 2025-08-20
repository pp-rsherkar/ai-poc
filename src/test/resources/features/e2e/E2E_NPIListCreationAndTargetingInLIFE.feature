Feature: End to End workflow for NPI Lists - Attributed and Auto-Imported creation and target it at Tactic level
  It covers below points
  1. Creation of NPI List
  2. Targeting the created Pixel at Tactic level


  Background:
    #1
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    #2
    And User navigates to NPI Lists page
    When User clicks on Create New List
    Then Verify creation of NPI List screen is displayed

  @e2e2
  Scenario Outline: Create a Attribute NPI List and target in 'NPI' targeting at Tactic level
    #3
    And User selects the Attributes List and uploads the file "<FILE_NAME>"
    Then Verify file "<FILE_NAME>" is uploaded successfully
    And User selects the "<COLUMN_NAME>" column and clicks on Next
    And User enters the Attributes list details as "<LIST_NAME>" "<ADVERTISER>"
    When User makes list available in LIFE and HCP365 and clicks on next
    Then Verify the Attributes list is saved successfully
    #4
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    #5
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    #6
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    And User selects the "<CHANNEL>" as channel
    And User add and configure NPI targeting rule
    #7
    Then Verify list is targeted in the tactic successfully
    And User saves the targeting


    Examples:
      | LIST_NAME | ADVERTISER     | FILE_NAME             | COLUMN_NAME | ADVERTISER     | CP_NAME               | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | RULE_TYPE |
      | ATTRIBUTE | 01- Advertiser | NPIAttributeList.xlsx | NPI         | 01- Advertiser | AttributeNPI_Campaign | Regular | 20000     | Line      | 500         | Tactic      | Display Advanced | NPI       |

