Feature: Create workspace and Publish studio List and Verify in NPI List in LIFE
@nikhil1
  Scenario Outline: Create and save HCP Explorer workspace with specific filters
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the "<FILTER>" filter and selects "<OPTION>" option
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved

    Examples:
      | ADVERTISER | FILTER     | OPTION | WORKSPACE_NAME  |
      | Abbvie     | NPI Gender | Male   |    Explorer     |
  @nikhil1
  Scenario Outline: Publish NPI List as Static List or Live List
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully
    And User searches the "<WORKSPACE_NAME>" and selects it
    And Download button is enabled to the user
    And User clicks on Publish NPI List
    And User selects publish "<LIST_TYPE>"
    And User select the system to publish the list
    Then Verify list is published
    Examples:
      | WORKSPACE_NAME   | LIST_TYPE      |
      | Explorer         |   Live         |
  @nikhil1
  Scenario Outline: Verify the published Studio list in the LIFE
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    And User searches the "<STUDIO_LIST>" in LIFE and selects it
    And User clicks on the published "<STUDIO_LIST>"
    Then User Verify the list is displayed in the Life
    Examples:
      | STUDIO_LIST     |
      | Explorer        |