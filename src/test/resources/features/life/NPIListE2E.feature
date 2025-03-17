Feature: End to End Workflow of NPI Lists

  @regression
  Scenario Outline: End to End Workflow of LIFE NPI lists Integration with HCP365
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    Then Verify creation of NPI List screen is displayed
    And User selects Smart List
    And User enters the NPI list details as "<LIST_NAME>" "<ADVERTISER>"
    When User clicks on Prescribed Drug and enters the drug details "<DRUG_NAME>"
    Then Verify drug details are added
    When User makes list available in LIFE, HCP365 and saves the list
    Then Verify list gets saved successfully
    And  User navigates to Campaign Dashboard
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    #When User selects the "<CHANNEL>" channel, configures the targeting rule with created smart NPI list, and saves the settings
    When User selects the "<CHANNEL>" channel, configure NPI targeting rule
    Then Verify smart list is targeted in the tactic successfully
    And User saves the targeting
    #And User navigates to Smart actions from the main menu
    #When User clicks on Add Smart Action
    #Then Verify smart action creation page is displayed
    #And User enters smart action details as "<SMART_ACTION_NAME"> <"ADVERTISER>"
    #When User saves the smart action
    #Then Verify smart action is saved successfully and navigates to Audience tab
    #When User clicks on NPI Lists
    #Then Verify NPI list created in LIFE is present
    #And User adds NPI list to the smart action
    #When User clicks on Ok and Save
    #Then Verify data is saved successfully
    #When User clicks on Action and enters the details and saves
    #Then Verify data is saved successfully
    #When User clicks on Response and enter the details as "<SMART_LIST_NAME>" and save
    #Then Verify data is saved successfully


    Examples:
      | USER  | ADVERTISER     | LIST_NAME   | DRUG_NAME | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME| CHANNEL          |
      | Admin | 01- Advertiser | Smart List  | Glynase   | Auto    | Regular | 2000      |  Line     | 500         | TACTIC     | Display Advanced |