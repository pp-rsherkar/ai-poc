Feature: Webhook Creation Regression - Verify below features
  1. Request method selection - GET and POST
  2. Adding Macros to the URL and Body
  3. Verify error messages on wrong input
  4. Webhook setup

  @e2e @regression
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
    Then Verify Webhook panel is enabled after applying engagement filters
    When User clicks "GET" request method
    Then Verify inline error message for the invalid webhook entries "<INVALID_WEBHOOK_DATA>"
    And Verify error message when webhook setup is failed using "<ERROR_DATA>"
    When User adds valid URL "<URL>" and append Macros with "<PARAM>" to the "URL" as follow
    | NPI        |
    | URL        |
    | Channel    |
    | Param 1..5 |
    Then Verify if Macros Appended to the URL "<URL>"
    When User saves the webhook setup
    Then Check that the success message appears once the webhook is successfully created
    When User clicks "POST" request method
    And User selects content type "<CONTENT_TYPE>"
    Then Verify inline error message for the invalid webhook entries "<INVALID_WEBHOOK_DATA>"
    And Verify error message when webhook setup is failed using "<ERROR_DATA>"
    When User adds valid URL "<URL>" and append Macros with "<PARAM>" to the "URL" as follow
      | NPI        |
      | URL        |
      | Channel    |
      | Param 1..5 |
    Then Verify if Macros Appended to the URL "<URL>"
    When User adds valid body "<BODY>" and append Macros with "<PARAM>" to the "Body" as follow
      | NPI        |
      | URL        |
      | Channel    |
      | Param 1..5 |
    Then Verify if Macros Appended to the Body "<BODY>"
    When User saves the webhook setup
    Then Check that the success message appears once the webhook is successfully created
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    Then Check the webhook icon is highlighted in green color
    When When User tries to delete the workspace associated with active webhook from the workspace list
    Then Verify user receives a warning when attempting to delete a workspace with an active webhook

    Examples:
      | ADVERTISER | FILTER       | OPTION                                | WORKSPACE_NAME | PARAM   | URL                                                       | BODY             | CONTENT_TYPE | INVALID_WEBHOOK_DATA | ERROR_DATA                              |
      | Abbvie     | Site, Search | Associated Device, Associated Network | Explorer       | Param 4 | https://webhook.site/4312c282-2efc-486e-bedf-3cd385a0c3da | WebhookData.json | JSON         | Test                 | https:www.google.com, WebhookData.json  |