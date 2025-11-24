Feature: Create and Publish HCP Explorer Workspace in Studio and Verify in LIFE
  1. Creation of Workspace in Studio
  2. Publish the workspace as Studio List (Static List or Live List)
  3. Verify the published Studio list in LIFE

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"

  @e2e @regression
  Scenario Outline: Create and save HCP Explorer workspace with specific filters
    #1
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    #2
    And User applies the filter and selects option
      | FilterName         | Option                                                                                                                  |
      | NPI Age            | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above                                                           |
      | NPI Gender         | Female, Male, Unknown                                                                                                   |
      | Graduation Year    | 1900-2025                                                                                                               |
    And User clicks on Ok and closes the filter popup
    #3
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    And Download button is enabled to the user
    #4
    And User clicks on Publish NPI List
    And User selects publish "<LIST_TYPE>"
    And User select the platform to publish the list
    Then Verify list is published
    #5
    And User navigates to NPI Lists page in LIFE
    And User searches the workspace in LIFE and selects it
    And User clicks on the published workspace
    Then User Verify the list is displayed in the Life
    Examples:
      | ADVERTISER | WORKSPACE_NAME | LIST_TYPE |
      | Abbvie     | Explorer       | Static    |

  @e2e @rs
  Scenario Outline: Create and save HCP Explorer workspace with Engagement filters and verify the Retrofit feature when the Live List is selected with NPI Retention option - "<NPI_RETENTION_OPTION>"
    #1
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    #2
    When User applies the filter and selects option
      | FilterName | Option             |
      | Site       | Associated Device  |
      | Search     | Associated Network |
    And User clicks on Ok and closes the filter popup
    #3
    Then Download button is enabled to the user
    And User clicks on Publish NPI List
     #4
    And User selects publish "<LIST_TYPE>"
    And Verify the Retrofit checkbox is selected
    And User selects "<NPI_RETENTION_OPTION>" as Keep NPIs on the list option
    And User select the platform to publish the list
    Then Verify list is published
    And User clicks on Published button, verifies the "<LIST_TYPE>" and "NPIs engaging" text are displayed
    Examples:
      | WORKSPACE_NAME | ADVERTISER | LIST_TYPE | NPI_RETENTION_OPTION |
      | Explorer       | Abbvie     | Live      |   indefinitely       |
      | Explorer       | Abbvie     | Live      |   remove after       |