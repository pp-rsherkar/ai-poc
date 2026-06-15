Feature: Brand Explorer Workspace creation in Studio

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "PP engineering test" and checks Studio permissions
    And User clicks PulsePoint icon to navigate back to Life
    And User navigates to Studio application

  @regression
  Scenario Outline: Create and save Brand Explorer workspace with default selections
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "Brand Explorer" workspace
    And User selects the advertiser "<ADVERTISER>"
    And User edits the workspace name as "<WORKSPACE_NAME>"
    Then Verify that advertiser field is disabled and displayed in "rgba(34, 34, 34, 0.55)" after saving the workspace
    Then Verify Dimension "Day" and Metric "Identified NPIs" are selected by default in the workspace
    Then Verify Time Frame is selected as "Last 7 Days" by default in the workspace
    And User saves the "Brand Explorer" workspace
    Then Verify the "Brand Explorer" Workspace is saved
    Examples:
      | ADVERTISER         | WORKSPACE_NAME |
      | TAMTESTING ACCOUNT | Brand_Explorer |