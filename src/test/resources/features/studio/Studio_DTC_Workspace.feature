Feature: DTC Workspace creation, segmentation, and publishing in Studio
  1. Creation of DTC Workspace in Studio
  2. Applying advertiser and filters to the workspace
  3. Verify the workspace is saved successfully
  4. Verify the submission based on Unique Consumers count
  5. Verify the status transition from Requested to Segmented
  6. Verify publishing of workspace after segmentation
  7. Verify the workspace status as Published in Workspace Management page

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "PP engineering test" and checks Studio permissions
    And User clicks PulsePoint icon to navigate back to Life
    And User navigates to Studio application

  @regression
  Scenario: Create, segment and publish DTC workspace based on Unique Consumers
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User selects the Workspace Type as "DTC Workspace"
    And User selects the advertiser "<ADVERTISER>"
    And User updates the workspace name as "<WORKSPACE_NAME>"
    And User applies the filter and selects option
      | FilterName | Option |
      | NPI Gender | Female |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the DTC Workspace is saved
    When User attempts to publish the workspace
    Then User captures the "Unique Consumers" count
    Then Verify whether the "Unique Consumers" count is greater than or equals to 100000
    And User clicks on Submit button
    Then Verify the dialog message as "Your Audience is being processed"
    When User navigates to Workspace Management page
    Then Verify the workspace status as "Requested"
    Then Verify the workspace status is changed from "Requested" to "Segmented" on the second day
    When User clicks on Publish button
    Then Verify the workspace is published successfully
    And Verify the workspace status as "Published"