Feature: Verify Draft Workspace Functionality in Studio Application
  1. Verify the creation of Draft workspace in Studio application.
  2. Verify the presence of Draft workspaces in Workspace Management page for external user.
  3. Verify the editing and publishing of Draft workspaces in Studio application.

  @regression
  Scenario Outline: Create and save a Draft workspace with specific filters
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User selects the Workspace Type as "HCP Explorer"
    And User selects the advertiser as "<ADVERTISER>"
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User selects the Draft option as "<DRAFT_OPTION>"
    Then User applies the filter and selects option
      | FilterName | Option                                                        |
      | NPI Age    | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Given This scenario will be executed in the "Pre-release" environment as a "External User"
    And "Studio" application is logged in successfully with Account "<ACCOUNT_NAME>"
    When External user Searches the "<WORKSPACE_NAME>"
    Then External user Verifies whether the "<WORKSPACE_NAME>" is visible in workspace management page

    Examples:

      | ADVERTISER |  | DRAFT_OPTION |  | WORKSPACE_NAME |
      | Abbvie     |  | Public       |  | Explorer       |
      | Abbvie     |  | Private      |  | Explorer       |