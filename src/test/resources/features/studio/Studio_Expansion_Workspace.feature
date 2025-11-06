Feature: HCP Audience Workspace in Studio Application
  1. Verify the workspace creation for HCP Audience Expansion type in Studio application.
  2. Verify the source audience selection and expansion options.

  Scenario Outline: Create and save an HCP Audience Expansion workspace with specific filters
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User clicks on Create New Workspace
    And User selects the Workspace Type as "HCP Audience Expansion"
    And User selects the advertiser as "<ADVERTISER>"
    Then User selects Source Audience details as "<SOURCE_AUDIENCE>","<OPTIONS>"
    And User selects "<EXPANDED_AUDIENCE>"
    Then User applies filters to the workspace
    And User clicks on Edit button to rename the workspace to "<WORKSPACE_NAME>"
    And User saves the workspace
    Then Verify the workspace in workspace management page
    Examples:
      | ADVERTISER | WORKSPACE_NAME | SOURCE_AUDIENCE  | OPTIONS      | EXPANDED_AUDIENCE             |
      | Abbvie     | HCP_Expansion  | Studio Workspace | PB_Test      | Expand with Care Team         |
      | Abbvie     | HCP_Expansion  | NPI List         | PB_Test_List | Expand with Affiliation Graph |