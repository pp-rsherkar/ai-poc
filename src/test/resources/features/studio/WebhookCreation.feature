Feature: Webhook Creation Regression - Verify below features
  1. Request method selection - GET and POST
  2. Adding Macros to the URL and Body
  3. Verify error messages on wrong input
  4. Webhook setup

  @e2e @regression
  Scenario Outline: Create a webhook and verify macros are appended to the request URL and body
    #1
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    #2
    And Verify Webhook panel is disabled before applying filters
    When User applies the filter and selects option
      | FilterName | Option             |
      | Site       | Associated Device  |
      | Search     | Associated Network |
    And User clicks on Ok and closes the filter popup
    Then Verify Webhook panel is enabled after applying engagement filters
    #3
    When User clicks "GET" request method
    Then Verify inline error message for the invalid webhook entries "<INVALID_WEBHOOK_DATA>"
    And Verify error message when webhook setup is failed using "<ERROR_DATA>"
    #4
    When User adds valid URL and append Macros with "<PARAM>" to the "URL" as follow
      | NPI        |
      | URL        |
      | Channel    |
      | Param 1..5 |
    Then Verify if Macros Appended to the URL
    When User saves the webhook setup
    Then Check that the success message appears once the webhook is successfully created
    #5
    When User clicks "POST" request method
    And User selects content type "<CONTENT_TYPE>"
    Then Verify inline error message for the invalid webhook entries "<INVALID_WEBHOOK_DATA>"
    And Verify error message when webhook setup is failed using "<ERROR_DATA>"
    When User adds valid URL and append Macros with "<PARAM>" to the "URL" as follow
      | NPI        |
      | URL        |
      | Channel    |
      | Param 1..5 |
    Then Verify if Macros Appended to the URL
    #6
    When User adds valid body "<BODY>" and append Macros with "<PARAM>" to the "Body" as follow
      | NPI        |
      | URL        |
      | Channel    |
      | Param 1..5 |
    Then Verify if Macros Appended to the Body "<BODY>"
    When User saves the webhook setup
    Then Check that the success message appears once the webhook is successfully created
    #7
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    Then Check the webhook icon is highlighted in green color
    #8
    When User tries to delete the workspace associated with active webhook from the workspace list
    Then Verify user receives a warning when attempting to delete a workspace with an active webhook

    Examples:
      | ADVERTISER | WORKSPACE_NAME | PARAM   | BODY             | CONTENT_TYPE | INVALID_WEBHOOK_DATA | ERROR_DATA                             |
      | Abbvie     | Explorer       | Param 4 | WebhookData.json | JSON         | Test                 | https:www.google.com, WebhookData.json |