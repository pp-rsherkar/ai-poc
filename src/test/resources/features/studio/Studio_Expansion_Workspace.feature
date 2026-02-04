Feature: HCP Audience Workspace in Studio Application
  1. Verify the workspace creation for HCP Audience Expansion type in Studio application.
  2. Verify the source audience selection and expansion options.
  3. Verify the filters application in HCP Audience Expansion workspace.
  4. Verify the publishing of the expanded audience list to LIFE application.

  #@regression
  Scenario Outline: Create and save an HCP Audience Expansion workspace with specific filters
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User selects the Workspace Type as "HCP Audience Expansion"
    And User selects the advertiser as "<ADVERTISER>"
    Then User selects Source Audience details as "<SOURCE_AUDIENCE>","<OPTIONS>"
    And User selects "<EXPANDED_AUDIENCE>"
    Then User verifies the expanded audience count
    And User clicks on Edit button to rename the workspace to "<WORKSPACE_NAME>"
    And User saves the workspace
    Then Verify the workspace is visible in workspace management page
    Examples:
      | ADVERTISER | WORKSPACE_NAME | SOURCE_AUDIENCE  | OPTIONS      | EXPANDED_AUDIENCE             |
      | Abbvie     | HCP_Expansion  | Studio Workspace | PB_Test      | Expand with Care Team         |
      | Abbvie     | HCP_Expansion  | NPI List         | PB_Test_List | Expand with Affiliation Graph |

    #@e2e
  Scenario Outline: Create and save an HCP Audience Expansion workspace and publish the workspace
      #1
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User selects the Workspace Type as "HCP Audience Expansion"
    And User selects the advertiser as "<ADVERTISER>"
      #2
    Then User selects Source Audience details as "<SOURCE_AUDIENCE>","<OPTIONS>"
    And User selects "<EXPANDED_AUDIENCE>"
      #3
    And User applies the filter and selects option
      | FilterName | Option                          |
      | NPI Age    | 45 to 55, 55 to 65, 65 or Above |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
      #4
    Then Verify the Workspace is saved
    And Download button is enabled to the user
    And User clicks on Publish NPI List
    And User selects publish "<LIST_TYPE>"
    And User select the platform to publish the list
    Then Verify list is published
    And User navigates to NPI Lists page in LIFE
    And User searches the workspace in LIFE and selects it
    And User clicks on the published workspace
    Then User Verify the list is displayed in the Life

    Examples:
      | ADVERTISER | SOURCE_AUDIENCE  | OPTIONS      | EXPANDED_AUDIENCE             | LIST_TYPE |
      | Abbvie     | Studio Workspace | PB_Test      | Expand with Care Team         | Static    |
      | Abbvie     | NPI List         | PB_Test_List | Expand with Affiliation Graph | Live      |