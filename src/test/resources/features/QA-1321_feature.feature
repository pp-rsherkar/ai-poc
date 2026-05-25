Feature: JIRA - GitHub Linkage

  As an internal user,
  I want to link JIRA issues to GitHub repositories,
  So that development tracking and automation are streamlined.

  Scenario: Internal user links a JIRA issue to a GitHub repository
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And Internal User is able to view "<WORKSPACE_NAME>" in workspace management page
    And User clicks the Settings icon and selects the following group by options and verify dashboard data is grouped accordingly
    And User clicks on accounts tab
    And User clicks on create new Campaign
    And User clicks the three-dot menu and verifies that "Generate Report" is enabled and "Delete" is disabled
    And User clicks Test Connection link to verify if connection happened successfully
    And User clicks Setup Import button to import File details
    And User clicks on Add Targeting Rule
    And User configures targeting rules as below
    And User clicks the comments icon in the tactic "header" section and add "<HEADER_COMMENT>"
    And User clicks the comments icon in the tactic "navigation" section and add "<NAV_COMMENT>"
    And User clicks Download NPI option

  Scenario: Internal user verifies JIRA issue is linked to GitHub repository
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And Internal User is able to view "<WORKSPACE_NAME>" in workspace management page
    And User clicks on accounts tab
    And User clicks on create new Campaign
    And User clicks the three-dot menu and verifies that "Generate Report" is enabled and "Delete" is disabled
    And User clicks Test Connection link to verify if connection happened successfully
    And User clicks Setup Import button to import File details
    And User clicks on Add Targeting Rule
    And User configures targeting rules as below
    And User clicks the comments icon in the tactic "header" section and add "<HEADER_COMMENT>"
    And User clicks the comments icon in the tactic "navigation" section and add "<NAV_COMMENT>"
    And User clicks Download NPI option
    And Confirms that the report panel retains the entered data
