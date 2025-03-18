Feature: LIFE Regression - Create Static NPI List

  @regression
  Scenario Outline: End to End Workflow of LIFE NPI lists Integration with HCP365
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to Smart actions from the main menu
    When User clicks on Add Smart Action
    Then Verify smart action creation page should be displayed
    And User enters smart action details as "<SMART_ACTION_NAME>" "<ADVERTISER>"
    When User saves the smart action
    Then Verify smart action is saved successfully and navigates to Audience tab
    When User clicks on NPI Lists
    Then Verify NPI list created in LIFE is present
    And User adds NPI list to the smart action
    When User clicks on Ok and Save
    Then Verify data is saved successfully
    When User clicks on Action and enters the details and saves
    Then Verify Action data is saved successfully
    When User clicks on Response and enter the details and creates smart list "<SMART_LIST_NAME>" "<DAYS>" and saves
    Then Verify Response data is saved successfully
    Examples:
      | USER  | ADVERTISER     | SMART_ACTION_NAME | LIST_NAME  | SMART_LIST_NAME | DAYS |
      | Admin | 01- Advertiser | SMART_ACTION      | STATIC_NPI | SMART_LIST      | 5    |