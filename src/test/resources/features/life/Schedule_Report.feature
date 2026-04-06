Feature: LIFE Regression - Schedule Report fields verification and report generation
  It ensures the correct loading, configuration, default behavior, and interaction of key elements in the Schedule Report tab, including:
  1. Report Name field input
  2. Frequency options (Once, Daily, Weekly, Monthly) and default selection
  3. Schedule Start/End dates and Timezone configuration
  4. Send On and Send At behavior for scheduled delivery
  5. Email and Custom Destination (FTP, SFTP, F3, GCP) delivery setup
  6. Compression and Control File options
  7. Successful report scheduling and delivery verification

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    When User navigates to Schedule report from mega menu of the life application
    And User clicks Schedule Report button
    Then Verify Schedule Report panel should be opened

  @regression
  Scenario Outline: Verify the fields under the Schedule Report tab on the Reports page
    And Verify Report Name field is available and accepts input "<REPORT_NAME>"
    And Verify availability of frequency field with options below
      | Once    |
      | Daily   |
      | Weekly  |
      | Monthly |
    And Verify default value of the Frequency field is "Weekly"
    And Verify that user is able to select Schedule start date and Schedule end date
    And Verify default value of Data Timezone is "(GMT-04:00) Eastern Time"
    And Verify that user is able to select Data Timezone field value "<TIME_ZONE>"
    And The Send On field should contain all days of the week when "Weekly" is selected as Frequency
      | Sun |
      | Mon |
      | Tue |
      | Wed |
      | Thu |
      | Fri |
      | Sat |
    And Verify default value of Send On is "Sun"
    And Verify Send At field is available with Start Time and Timezone fields
    And Verify default value of Send At fields - Start Time is "06:00" and Timezone is "(GMT-05:00) Eastern Time"
    And Verify user is able to select Time "10:00" and Timezone "<TIME_ZONE>" for Send At fields
    And Verify Delivery field has two methods - "Email" and "Custom Destination"
    Examples:
      | REPORT_NAME    | TIME_ZONE                       |
      | ScheduleReport | (GMT+05:30) India Standard Time |

  @regression
  Scenario:Verify fields under Email tab in Schedule Report's Delivery Method section
    When User clicks on "Email" tab as Delivery Method
    Then Verify Deliver to Users field is available
    And User should be able to specify multiple users in Deliver to Users field
      | pbalu   |
      | pjadhav |
    And Verify that Add Emails link is available below Deliver to Users
    When User clicks Add Emails link, Deliver to External Emails field should display
    And User should be able to add multiple emails in Deliver to External Emails field
      | test@pulsepoint.com |
      | test@webmd.com      |

  @regression
  Scenario Outline: Verify fields under Custom Destination tab in Schedule Report's Delivery Method section
    When User clicks on "Custom Destination" tab as Delivery Method
    Then Verify Destination dropdown field is available
    And Verify "Add Destination" button is available in Destination dropdown field
    When User clicks "Add Destination" button
    Then Verify Destination Name, Destination Type fields are displayed
    And Verify that Destination Type has values "<DESTINATION_TYPE>"
    And Verify File Path field is available
    And Verify File Name field is available
    And Verify Compression field is available with below options and default value is "None"
      | Zip  |
      | BZip |
      | GZip |
      | None |
    And Verify Control File checkbox is present and by default it should be unchecked
    Examples:
      | DESTINATION_TYPE   |
      | FTP, SFTP, S3, GCP |

  @regression
  Scenario Outline: Verify a Scheduled Report generation with Frequency value - Once and using Email delivery method
    And User should be able to select template "<TEMPLATE>" from the dropdown
    And User should be able to select advertiser as "<ADVERTISER>"
    When Campaign should load for selection when user types campaign initials "<CAMPAIGN_INITIALS>" in "Campaign" field
    Then User should be able to select value from dropdown
    When Line Items of selected campaigns should load when user types line items initials "<LINE_ITEM_INITIALS>" in "Line Item" field
    Then User should be able to select value from dropdown
    When Tactic of selected line items should load when user types tactic names initials "<TACTIC_INITIALS>" in "Tactic" field
    Then User should be able to select value from dropdown
    When Creative of selected tactic should load when user types creative names initials "<CREATIVE_INITIALS>" in "Creative" field
    Then User should be able to select value from dropdown
    And Verify Report Name field is available and accepts input "<REPORT_NAME>"
    And User clicks "<FREQUENCY_VALUE>" as frequency
    And User selects Send On date
    And Verify that user is able to select Data Timezone field value "<TIME_ZONE>"
    And Verify user is able to select Time "10:00" and Timezone "<TIME_ZONE>" for Send At fields
    And Verify Report Period is selected as "Custom Dates"
    And User selects start date and end date when Custom Dates option is selected
    And User selects start "12:00" and end time "09:00" when Custom Dates option is selected
    And User clicks Schedule button to generate the report
    And User searches the report and checks the report panel retains the entered data
    Examples:
      | REPORT_NAME    | TEMPLATE       | ADVERTISER     | CAMPAIGN_INITIALS | LINE_ITEM_INITIALS | TACTIC_INITIALS | CREATIVE_INITIALS | FREQUENCY_VALUE | TIME_ZONE                       |
      | ScheduleReport | AutoTemplate20 | 01- Advertiser | CreativeCampaign  | CreativeLine       | CreativeTactic  | Creative          | Once            | (GMT+05:30) India Standard Time |

  @regression
  Scenario Outline: Verify a Scheduled Report generation with Frequency value - Daily and using Email delivery method
    And User should be able to select template "<TEMPLATE>" from the dropdown
    And User should be able to select advertiser as "<ADVERTISER>"
    When Campaign should load for selection when user types campaign initials "<CAMPAIGN_INITIALS>" in "Campaign" field
    Then User should be able to select value from dropdown
    When Line Items of selected campaigns should load when user types line items initials "<LINE_ITEM_INITIALS>" in "Line Item" field
    Then User should be able to select value from dropdown
    When Tactic of selected line items should load when user types tactic names initials "<TACTIC_INITIALS>" in "Tactic" field
    Then User should be able to select value from dropdown
    When Creative of selected tactic should load when user types creative names initials "<CREATIVE_INITIALS>" in "Creative" field
    Then User should be able to select value from dropdown
    And Verify Report Name field is available and accepts input "<REPORT_NAME>"
    And User clicks "<FREQUENCY_VALUE>" as frequency
    And Verify that user is able to select Schedule start date and Schedule end date
    And Verify that user is able to select Data Timezone field value "<TIME_ZONE>"
    And Verify user is able to select Time "10:00" and Timezone "<TIME_ZONE>" for Send At fields
    And Verify Report Period is selected as "Month to Date"
    And User clicks Schedule button to generate the report
    And User searches the report and checks the report panel retains the entered data
    Examples:
      | REPORT_NAME    | TEMPLATE       | ADVERTISER     | CAMPAIGN_INITIALS | LINE_ITEM_INITIALS | TACTIC_INITIALS | CREATIVE_INITIALS | FREQUENCY_VALUE | TIME_ZONE                       |
      | ScheduleReport | AutoTemplate20 | 01- Advertiser | CreativeCampaign  | CreativeLine       | CreativeTactic  | Creative          | Daily           | (GMT+05:30) India Standard Time |

  @regression
  Scenario Outline: Verify a Scheduled Report generation with Frequency value - Weekly and using Email delivery method
    And User should be able to select template "<TEMPLATE>" from the dropdown
    And User should be able to select advertiser as "<ADVERTISER>"
    When Campaign should load for selection when user types campaign initials "<CAMPAIGN_INITIALS>" in "Campaign" field
    Then User should be able to select value from dropdown
    When Line Items of selected campaigns should load when user types line items initials "<LINE_ITEM_INITIALS>" in "Line Item" field
    Then User should be able to select value from dropdown
    When Tactic of selected line items should load when user types tactic names initials "<TACTIC_INITIALS>" in "Tactic" field
    Then User should be able to select value from dropdown
    When Creative of selected tactic should load when user types creative names initials "<CREATIVE_INITIALS>" in "Creative" field
    Then User should be able to select value from dropdown
    And Verify Report Name field is available and accepts input "<REPORT_NAME>"
    And User clicks "<FREQUENCY_VALUE>" as frequency
    And Verify that user is able to select Schedule start date and Schedule end date
    And Verify that user is able to select Data Timezone field value "<TIME_ZONE>"
    And Verify that user is able to select Send On day "Tue"
    And Verify user is able to select Time "10:00" and Timezone "<TIME_ZONE>" for Send At fields
    And Verify Report Period is selected as "Month to Date"
    And User clicks Schedule button to generate the report
    And User searches the report and checks the report panel retains the entered data
    Examples:
      | REPORT_NAME    | TEMPLATE       | ADVERTISER     | CAMPAIGN_INITIALS | LINE_ITEM_INITIALS | TACTIC_INITIALS | CREATIVE_INITIALS | FREQUENCY_VALUE | TIME_ZONE                       |
      | ScheduleReport | AutoTemplate20 | 01- Advertiser | CreativeCampaign  | CreativeLine       | CreativeTactic  | Creative          | Weekly          | (GMT+05:30) India Standard Time |

  @regression
  Scenario Outline: Verify a Scheduled Report generation with Frequency value - Monthly and using Email delivery method
    And User should be able to select template "<TEMPLATE>" from the dropdown
    And User should be able to select advertiser as "<ADVERTISER>"
    When Campaign should load for selection when user types campaign initials "<CAMPAIGN_INITIALS>" in "Campaign" field
    Then User should be able to select value from dropdown
    When Line Items of selected campaigns should load when user types line items initials "<LINE_ITEM_INITIALS>" in "Line Item" field
    Then User should be able to select value from dropdown
    When Tactic of selected line items should load when user types tactic names initials "<TACTIC_INITIALS>" in "Tactic" field
    Then User should be able to select value from dropdown
    When Creative of selected tactic should load when user types creative names initials "<CREATIVE_INITIALS>" in "Creative" field
    Then User should be able to select value from dropdown
    And Verify Report Name field is available and accepts input "<REPORT_NAME>"
    And User clicks "<FREQUENCY_VALUE>" as frequency
    And Verify that user is able to select Schedule start date and Schedule end date
    And Verify that user is able to select Data Timezone field value "<TIME_ZONE>"
    And Verify Send On field is visible and user should be able to select the day "2" of the month
    And Verify user is able to select Time "10:00" and Timezone "<TIME_ZONE>" for Send At fields
    And Verify Report Period is selected as "Month to Date"
    And User clicks Schedule button to generate the report
    And User searches the report and checks the report panel retains the entered data
    Examples:
      | REPORT_NAME    | TEMPLATE       | ADVERTISER     | CAMPAIGN_INITIALS | LINE_ITEM_INITIALS | TACTIC_INITIALS | CREATIVE_INITIALS | FREQUENCY_VALUE | TIME_ZONE                       |
      | ScheduleReport | AutoTemplate20 | 01- Advertiser | CreativeCampaign  | CreativeLine       | CreativeTactic  | Creative          | Monthly         | (GMT+05:30) India Standard Time |