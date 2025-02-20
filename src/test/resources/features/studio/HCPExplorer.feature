Feature: HCP Explorer Workspace in Studio Application

  @regression
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
      | ADVERTISER | FILTER     | OPTION | WORKSPACE_NAME |
      | Abbvie     | NPI Gender | Male   | Explorer       |