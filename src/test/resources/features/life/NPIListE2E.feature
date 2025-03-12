Feature: End to End Workflow of NPI Lists

  @Raj_Run
  Scenario Outline: End to End Workflow of LIFE NPI lists Integration with HCP365
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    Then Verify creation of NPI List screen is displayed
    And User selects Smart List
    And User enters the NPI list details as "<LIST_NAME>" "<ADVERTISER>"
    When User clicks on Prescribed Drug and enters the drug details
    Then Verify drug details are added
    When User makes list available in LIFE, HCP365 and saves the list
    Then Verify list gets saved successfully
    #When User navigates to Tactic targeting and add NPI targeting by selecting the created smart list
    #And User clicks on OK and Save
    #Then Verify smart list is targeted in the tactic successfully
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
      | USER  | ADVERTISER     | LIST_NAME   |
      | Admin | 01- Advertiser | Smart List  |