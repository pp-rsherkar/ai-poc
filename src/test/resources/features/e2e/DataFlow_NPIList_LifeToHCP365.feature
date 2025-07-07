Feature: End to End Workflow of NPI Lists.
  It covers below points
  1. Create a Smart and Static List from LIFE and to be available in LIFE & HCP365.
  2. Create Campaign in LIFE with smart list targeting.
  3. Create a Smart action from HCP365 and navigate to Audience tab.
  4. Verify NPI list created in LIFE is available.
  5. Add Smart list created in LIFE as the Audience.
  6. Add action as Visits Brand page.
  7. Add response to the smart action as Add NPI to the smart list.
  8. Verify Smart action is saved successfully.

  @e2e @regression
  Scenario Outline: End to End Workflow of LIFE NPI Smart lists Integration with HCP365
    # 1
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to NPI Lists page
    When User clicks on Create New List
    Then Verify creation of NPI List screen is displayed
    And User selects Smart List
    And User enters the NPI list details as "<LIST_NAME>" "<ADVERTISER>"
    When User clicks on Prescribed Drug and enters the drug details "<DRUG_NAME>"
    Then Verify drug details are added
    When User makes list available in LIFE, HCP365 and saves the list
    Then Verify list gets saved successfully
    # 2
    And  User navigates to Campaign Dashboard
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "<CHANNEL>" channel, configure NPI targeting rule
    Then Verify smart list is targeted in the tactic successfully
    And User saves the targeting
    # 3
    And User navigates to Smart actions from the main menu
    When User clicks on Add Smart Action
    Then Verify smart action creation page is displayed
    And User enters smart action details as "<SMART_ACTION_NAME>" "<ADVERTISER>"
    When User saves the smart action
    Then Verify smart action is saved successfully and navigates to Audience tab
    # 4 & 5
    When User clicks on NPI Lists
    Then Verify NPI list created in LIFE is present
    And User adds NPI list to the smart action
    When User clicks on Ok and Save
    Then Verify data is saved successfully
    # 6
    When User clicks on Action and enters the details and saves
    Then Verify data is saved successfully
    # 7 & 8
    When User clicks on Response and enter the details and creates smart list "<SMART_LIST_NAME>" "<DAYS>" and saves
    Then Verify data is saved successfully

    Examples:
      | ADVERTISER   | LIST_NAME  | DRUG_NAME | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | SMART_ACTION_NAME | SMART_LIST_NAME | DAYS |
      | Z_Automation | Smart List | Glynase   | Auto    | Regular | 2000      | Line      | 500         | TACTIC      | Display Advanced | SMART_ACTION      | SMART_LIST      | 5    |


  @e2e2 @regression
  Scenario Outline: End to End Workflow of LIFE Static NPI lists Integration with HCP365
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to NPI Lists page
    When User clicks on Create New List
    Then Verify creation of NPI List screen is displayed
    And User selects Static List
    And User enters the NPI list details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>"
    When User makes list available in LIFE, HCP365 and saves the list
    Then Verify list gets saved successfully
    # 2
    And  User navigates to Campaign Dashboard
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "<CHANNEL>" channel, configure NPI targeting rule
    Then Verify smart list is targeted in the tactic successfully
    And User saves the targeting
    # 3
    And User navigates to Smart actions from the main menu
    When User clicks on Add Smart Action
    Then Verify smart action creation page is displayed
    And User enters smart action details as "<SMART_ACTION_NAME>" "<ADVERTISER>"
    When User saves the smart action
    Then Verify smart action is saved successfully and navigates to Audience tab
    # 4 & 5
    When User clicks on NPI Lists
    Then Verify NPI list created in LIFE is present
    And User adds NPI list to the smart action
    When User clicks on Ok and Save
    Then Verify data is saved successfully
    # 6
    When User clicks on Action and enters the details and saves
    Then Verify data is saved successfully
    Examples:
      | ADVERTISER   | LIST_NAME  | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | SMART_ACTION_NAME |  NPI_NUMBER    |
      | Z_Automation | STATIC_NPI | Auto    | Regular | 2000      | Line      | 500         | TACTIC      | Display Advanced | SMART_ACTION      |  1234567890    |

