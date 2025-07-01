Feature: Create and Publish HCP Explorer Workspace in Studio and Verify in LIFE
  1. Creation of Workspace in Studio
  2. Publish the workspace as Studio List (Static List or Live List)
  3. Verify the published Studio list in LIFE

  @e2e2 @regression
  Scenario Outline: Create and save HCP Explorer workspace with specific filters
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "UAT_account"
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the "<FILTER>" filter and selects "<OPTION>" option
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    And Download button is enabled to the user
    And User clicks on Publish NPI List
    And User selects publish "<LIST_TYPE>"
    And User select the system to publish the list
    Then Verify list is published
    And User navigates to NPI Lists page in LIFE
    And User searches the workspace in LIFE and selects it
    And User clicks on the published workspace
    Then User Verify the list is displayed in the Life
    Examples:
      | ADVERTISER | FILTER              | OPTION         | WORKSPACE_NAME | LIST_TYPE |
      | Abbvie     | NPI Gender, NPI Age | Male, 25 to 35 | Explorer       | Static    |