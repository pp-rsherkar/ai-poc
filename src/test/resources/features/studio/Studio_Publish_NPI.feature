Feature: Studio Publish NPI List

  @regression
  Scenario Outline: Publish NPI List as Static List and Live List with platforms - LIFE and HCP365
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the filter and selects option
      | FilterName | Option                |
      | NPI Age    | Below 25, 25 to 35,   |
      | NPI Gender | Female, Male, Unknown |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    Then Download button is enabled to the user
    And User clicks on Publish NPI List
    And User selects publish "<LIST_TYPE>"
    And User select the platform to publish the list
    Then Verify list is published
    And Check the Download icon is highlighted in green color
    Examples:
      | WORKSPACE_NAME | ADVERTISER | LIST_TYPE |
      | Explorer       | Abbvie     | Static    |
      | Explorer       | Abbvie     | Live      |

  @regression
  Scenario Outline: Publish NPI List as Static List and Live List without selecting any platform
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the filter and selects option
      | FilterName | Option                |
      | NPI Age    | Below 25, 25 to 35,   |
      | NPI Gender | Female, Male, Unknown |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    Then Download button is enabled to the user
    And User clicks on Publish NPI List
    And User selects publish "<LIST_TYPE>"
    Then Verify list is published
    And Check the Download icon is highlighted in green color
    And User navigates to NPI Lists page in LIFE
    And User searches the workspace in LIFE and selects it
    And User clicks on the published workspace
    And Verify the list should be available for LIFE platform by default
    Examples:
      | WORKSPACE_NAME | ADVERTISER | LIST_TYPE |
      | Explorer       | Abbvie     | Static    |
      | Explorer       | Abbvie     | Live      |