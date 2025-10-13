Feature: Create a Destination in Admin and run below report against it.
  1. Run a Report
  2. Schedule a Report

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to Administrative section and go to Accounts Tab
    And User searches the account "automation@pulsepoint" in which Destination to be created
    And User navigates to Reporting tab
    Then User clicks Add Destination button
    And User enters Destination details - "Auto_Destination_", "SFTP", "ma2-qa-automation01", "22"
    And User clicks Test Connection link to verify if connection happened successfully
    And User saves the custom destination
    And User clicks PulsePoint icon to navigate back to Life

  @e2e
  Scenario Outline: End to End Workflow of create a Destination in Admin and run a Run Report against it
    When User navigates to run report from mega menu of the life application
    And Verify Run Report panel should be opened
    When Tactic of selected line items should load when user types tactic names initials "<TACTIC_INITIALS>" in "Tactic" field
    Then User should be able to select value from dropdown
    And User should be able to fetch details - Advertiser, Campaign, Line Item, Tactic
    And User should be able to select template "<TEMPLATE>" from the dropdown
    When User clicks on "Custom Destination" tab as Delivery Method
    Then User selects destination name created, and other details - "<FILE_PATH>", "<FILE_NAME>"
    And User should be able to generate the report
    Examples:
      | FILE_PATH                      | FILE_NAME                  | TEMPLATE            | TACTIC_INITIALS |
      | /home/NPIAutoImport/Automation | AutoImport_Automation1.csv | Template_Automation | Multiple_Flight |

  @e2e
  Scenario Outline: End to End Workflow of create a Destination in Admin and run a Schedule Report against it
    When User navigates to Schedule report from mega menu of the life application
    And User clicks Schedule Report button
    Then Verify Schedule Report panel should be opened
    When Tactic of selected line items should load when user types tactic names initials "<TACTIC_INITIALS>" in "Tactic" field
    Then User should be able to select value from dropdown
    And User should be able to fetch details - Advertiser, Campaign, Line Item, Tactic
    And User should be able to select template "<TEMPLATE>" from the dropdown
    And Verify Report Name field is available and accepts input "<REPORT_NAME>"
    And User clicks "<FREQUENCY_VALUE>" as frequency
    And Verify that user is able to select Schedule start date and Schedule end date
    And Verify that user is able to select Data Timezone field value "<TIME_ZONE>"
    And Verify user is able to select Time "10:00" and Timezone "<TIME_ZONE>" for Send At fields
    And Verify Report Period is selected as "Month to Date"
    When User clicks on "Custom Destination" tab as Delivery Method
    Then User selects destination name created, and other details - "<FILE_PATH>", "<FILE_NAME>"
    And User clicks Schedule button to generate the report
    Examples:
      | FILE_PATH                      | FILE_NAME                  | TEMPLATE            | TACTIC_INITIALS | REPORT_NAME    | FREQUENCY_VALUE | TIME_ZONE                       |
      | /home/NPIAutoImport/Automation | AutoImport_Automation1.csv | Template_Automation | Multiple_Flight | ScheduleReport | Daily           | (GMT+05:30) India Standard Time |