Feature: HCP Audience Workspace in Studio Application

  @regression
  Scenario Outline: Create and save an HCP Audience Expansion workspace with specific filters
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "UAT_account"
    When User clicks on Create New Workspace For Expansion
    And the user selects the advertiser "<ADVERTISER>"
    Then the user selects Source Audience "<SOURCE_AUDIENCE>"
    And the user selects Expand With Care Team or Expand With Affiliation Graph and selects the value
    Then the filters should be applied to the workspace
    And the user renames the workspace to "<WORKSPACE_NAME>"
    Then the user saves the workspace and check the workspace is Saved

    Examples:
      | ADVERTISER | WORKSPACE_NAME | SOURCE_AUDIENCE  |
      | Abbvie     | Expansion      | Studio Workspace |