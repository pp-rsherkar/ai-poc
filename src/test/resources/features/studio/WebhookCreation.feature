Feature: Webhook Creation Regression - Verify below features
  1. Request method selection - GET and POST
  2. Adding Macros to the URL and Body
  3. Verify error messages on wrong input
  4. Webhook setup

  @regression
  Scenario Outline: Create a webhook and verify macros are appended to the request URL and body
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And Verify Webhook panel is disabled before applying filters
    When User applies the "<FILTER>" filter and selects "<OPTION>" option
    And User clicks on Ok and closes the filter popup
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    Then Verify Webhook panel is enabled after applying filters
    When User clicks "GET" request method
    And User adds URL "<URL>" and Macros with "<PARAM>" to the URL as follow
    | NPI        |
    | URL        |
    | Channel    |
    | Param 1..5 |
    Then Verify if Macros Appended to the URL "<URL>"
    When User clicks "POST" request method
    And User selects content type "<CONTENT_TYPE>"
    And User adds URL "<URL>" and append Macros with "<PARAM>" to the URL as follow
      | NPI        |
      | URL        |
      | Channel    |
      | Param 1..5 |
    And User adds body "<BODY>" and append Macros to the body as follow
      | NPI        |
      | URL        |
      | Channel    |
      | Param 1..5 |

    Examples:
      | ADVERTISER | FILTER       | OPTION                                | WORKSPACE_NAME | PARAM  | URL                                                       | BODY             | CONTENT_TYPE |
      | Abbvie     | Site, Search | Associated Device, Associated Network | Explorer       | Param4 | https://webhook.site/4312c282-2efc-486e-bedf-3cd385a0c3da | WebhookData.json | JSON         |
