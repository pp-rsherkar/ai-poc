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
    And User fetches the logged in username
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
    And Verify default value of Send At fields - Start Time is "06:00" and Timezone is "(GMT+05:30) India Standard Time"
    And Verify user is able to select Time "10:00" and Timezone "<TIME_ZONE>" for Send At fields
    And Verify Report Period field is available with default value "Month to Date"
    And Verify that Report Period field has below options
      | Month to Date   |
      | Week to Date    |
      | Quarter to Date |
      | Flight to Date  |
      | Yesterday       |
      | Last…           |
      | Lifetime        |
    And Verify Report Timing checkbox is available and by default it is unchecked
    And Verify Delivery field has two methods - "Email" and "Custom Destination"
    Examples:
      | REPORT_NAME    | TIME_ZONE                       |
      | ScheduleReport | (GMT+05:30) India Standard Time |

  @regression
  Scenario Outline:Verify fields under Email tab in Schedule Report's Delivery Method section
    And Verify that "Email" tab is selected as Delivery method by default
    When User clicks on "Email" tab as Delivery Method
    Then Verify Deliver to Users field is available
    And Verify that "Deliver to Users" field is pre-populated with logged in user email and user should be able to edit the email address "<USER_EMAIL>"
    And Verify File Name field is available on report panel
    And Verify that Add Emails link is available below Deliver to Users
    When User clicks Add Emails link, Deliver to External Emails field should display
    And User should be able to add multiple emails in Deliver to External Emails field
      | test@pulsepoint.com |
      | test@webmd.com      |
    And Verify that "Notify User for Failures" field is pre-populated with logged in user email and user should be able to edit the email address "<USER_EMAIL>"
    And Verify that user is not able to remove the pre-populated logged in user email from "Notify User for Failures" field
    And Verify the presence of Report Format field and default value - "CSV"
    And Verify the availability of various options of the Report Format field - "<REPORT_FORMATS>"
    And Verify the presence of Text Qualifier checkbox and by default it should be checked
    Examples:
      | USER_EMAIL     | REPORT_FORMATS                                                             |
      | pbalu, pjadhav | CSV, Excel, Pipe Delimited CSV, Pipe Delimited TXT, Tab Delimited TXT, TSV |

  @regression
  Scenario Outline: Verify fields under Custom Destination tab in Schedule Report's Delivery Method section
    When User clicks on "Custom Destination" tab as Delivery Method
    Then Verify Destination dropdown field is available
    And Verify "Add Destination" button is available in Destination dropdown field
    When User clicks "Add Destination" button
    Then Verify Destination Name, Destination Type fields are displayed
    And Verify that Destination Type has values "<DESTINATION_TYPE>"
    And Verify that "Notify User for Failures" field is pre-populated with logged in user email and user should be able to edit the email address "<USER_EMAIL>"
    And Verify that user is not able to remove the pre-populated logged in user email from "Notify User for Failures" field
    And Verify File Path field is available
    And Verify File Name field is available
    And Verify the presence of Report Format field and default value - "CSV"
    And Verify the availability of various options of the Report Format field - "<REPORT_FORMATS>"
    And Verify the presence of Text Qualifier checkbox and by default it should be checked
    And Verify Compression field is available with below options and default value is "None"
      | Zip  |
      | BZip |
      | GZip |
      | None |
    And Verify Control File checkbox is present and by default it should be unchecked
    And Verify the presence of Advanced Export checkbox and by default it should be unchecked
    And Verify Line Coding field is available with below options and default value is "Unix (LF)"
      | Unix (LF)  |
      | DOS (CRLF) |
    Examples:
      | DESTINATION_TYPE   | USER_EMAIL     | REPORT_FORMATS                                                             |
      | FTP, SFTP, S3, GCP | pbalu, pjadhav | CSV, Excel, Pipe Delimited CSV, Pipe Delimited TXT, Tab Delimited TXT, TSV |

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
    And Verify Report Period is selected as "<REPORTING_PERIOD>"
    And User selects start date and end date when Custom Dates option is selected
    And User selects start "12:00" and end time "09:00" when Custom Dates option is selected
    And Validate that the template name matches the file name, and ensure the help text displays the file name in the ".csv" format
    And User clicks the three-dot menu, selects the General variable - "$CampaignName$" and Time variable - "$ReportDateTime[Date/Timestamp]$" with Date-Time format "%Y-%m-%d"
    And User verifies that the help text displays the file name with the value of General and Time variables
    And User clicks Schedule button to generate the report
    And User searches the report and verify the details in report listing page - Template name, "<FREQUENCY_VALUE>", "<REPORTING_PERIOD>" and Generated By
    And Verify the report panel retains the entered data
    Examples:
      | REPORT_NAME    | TEMPLATE       | ADVERTISER     | CAMPAIGN_INITIALS | LINE_ITEM_INITIALS | TACTIC_INITIALS | CREATIVE_INITIALS | FREQUENCY_VALUE | TIME_ZONE                       | REPORTING_PERIOD |
      | ScheduleReport | AutoTemplate20 | 01- Advertiser | CreativeCampaign  | CreativeLine       | CreativeTactic  | Creative          | Once            | (GMT+05:30) India Standard Time | Custom Dates     |

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
    And Verify Report Period is selected as "<REPORTING_PERIOD>"
    And Validate that the template name matches the file name, and ensure the help text displays the file name in the ".csv" format
    And User clicks the three-dot menu, selects the General variable - "$LineItemName$" and Time variable - "$ReportDateTime[Date/Timestamp]$" with Date-Time format "%Y-%m-%d"
    And User verifies that the help text displays the file name with the value of General and Time variables
    And User clicks Schedule button to generate the report
    And User searches the report and verify the details in report listing page - Template name, "<FREQUENCY_VALUE>", "<REPORTING_PERIOD>" and Generated By
    And Verify the report panel retains the entered data
    Examples:
      | REPORT_NAME    | TEMPLATE       | ADVERTISER     | CAMPAIGN_INITIALS | LINE_ITEM_INITIALS | TACTIC_INITIALS | CREATIVE_INITIALS | FREQUENCY_VALUE | TIME_ZONE                       | REPORTING_PERIOD |
      | ScheduleReport | AutoTemplate20 | 01- Advertiser | CreativeCampaign  | CreativeLine       | CreativeTactic  | Creative          | Daily           | (GMT+05:30) India Standard Time | Month to Date    |

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
    And Verify Report Period is selected as "<REPORTING_PERIOD>"
    And Validate that the template name matches the file name, and ensure the help text displays the file name in the ".csv" format
    And User clicks Schedule button to generate the report
    And User searches the report and verify the details in report listing page - Template name, "<FREQUENCY_VALUE>", "<REPORTING_PERIOD>" and Generated By
    And Verify the report panel retains the entered data
    Examples:
      | REPORT_NAME    | TEMPLATE       | ADVERTISER     | CAMPAIGN_INITIALS | LINE_ITEM_INITIALS | TACTIC_INITIALS | CREATIVE_INITIALS | FREQUENCY_VALUE | TIME_ZONE                       | REPORTING_PERIOD |
      | ScheduleReport | AutoTemplate20 | 01- Advertiser | CreativeCampaign  | CreativeLine       | CreativeTactic  | Creative          | Weekly          | (GMT+05:30) India Standard Time | Month to Date    |

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
    And Verify Report Period is selected as "<REPORTING_PERIOD>"
    And Validate that the template name matches the file name, and ensure the help text displays the file name in the ".csv" format
    And User clicks Schedule button to generate the report
    And User searches the report and verify the details in report listing page - Template name, "<FREQUENCY_VALUE>", "<REPORTING_PERIOD>" and Generated By
    And Verify the report panel retains the entered data
    Examples:
      | REPORT_NAME    | TEMPLATE       | ADVERTISER     | CAMPAIGN_INITIALS | LINE_ITEM_INITIALS | TACTIC_INITIALS | CREATIVE_INITIALS | FREQUENCY_VALUE | TIME_ZONE                       | REPORTING_PERIOD |
      | ScheduleReport | AutoTemplate20 | 01- Advertiser | CreativeCampaign  | CreativeLine       | CreativeTactic  | Creative          | Monthly         | (GMT+05:30) India Standard Time | Month to Date    |

  @regression
  Scenario Outline: Verify that user is able to create custom destinations from Scheduled Report panel and generate report
    When Tactic of selected line items should load when user types tactic names initials "<TACTIC_INITIALS>" in "Tactic" field
    Then User should be able to select value from dropdown
    And User should be able to fetch details - Advertiser, Campaign, Line Item, Tactic
    And User should be able to select template "<TEMPLATE>" from the dropdown
    And Verify Report Name field is available and accepts input "<REPORT_NAME>"
    And User clicks "<FREQUENCY_VALUE>" as frequency
    And Verify that user is able to select Schedule start date and Schedule end date
    And Verify that user is able to select Data Timezone field value "<TIME_ZONE>"
    And Verify Send On field is visible and user should be able to select the day "2" of the month
    And Verify user is able to select Time "10:00" and Timezone "<TIME_ZONE>" for Send At fields
    And Verify Report Period is selected as "<REPORTING_PERIOD>"
    When User clicks on "Custom Destination" tab as Delivery Method
    Then Verify Destination dropdown field is available
    And Verify "Add Destination" button is available in Destination dropdown field
    When User clicks "Add Destination" button
    And User enters Destination name "<DESTINATION_NAME>", Destination Type "<DESTINATION_TYPE>" and other required details - "<HOST>", "<PORT>", "<SERVER_PATH>"
    Then User runs the connection test and creates the destination
    And Verify destination created should populate in the Destination dropdown field
    And User clicks Schedule button to generate the report
    And User searches the report and verify the details in report listing page - Template name, "<FREQUENCY_VALUE>", "<REPORTING_PERIOD>" and Generated By
    And Verify the report panel retains the entered data
    Examples:
      | REPORT_NAME    | TEMPLATE       | TACTIC_INITIALS | FREQUENCY_VALUE | TIME_ZONE                       | REPORTING_PERIOD | DESTINATION_NAME | DESTINATION_TYPE | HOST                | PORT | SERVER_PATH                    |
      | ScheduleReport | AutoTemplate20 | CreativeTactic  | Monthly         | (GMT+05:30) India Standard Time | Month to Date    | Run_Destination_ | SFTP             | ma2-qa-automation01 | 22   | /home/NPIAutoImport/Automation |

  @todo
  Scenario Outline: Status indicator dot and tooltip identify each entity's status in the Schedule Report's Campaign, Line Item, and Tactic filter dropdowns
    When Campaign should load for selection when user types campaign initials "<CAMPAIGN_INITIALS>" in "Campaign" field
    Then each listed campaign displays a status indicator dot for one of Incomplete, Denied, Ready, Running, Finished
    When Line Items of selected campaigns should load when user types line items initials "<LINE_ITEM_INITIALS>" in "Line Item" field
    Then each listed line item displays its own status indicator dot
    When Tactic of selected line items should load when user types tactic names initials "<TACTIC_INITIALS>" in "Tactic" field
    Then each listed tactic displays its own status indicator dot
    And hovering a status dot displays a tooltip identifying the current status per the Design System
    Examples:
      | CAMPAIGN_INITIALS | LINE_ITEM_INITIALS | TACTIC_INITIALS |
      | CreativeCampaign   | CreativeLine        | CreativeTactic  |

  # Source: ET-24246
  @todo
  Scenario Outline: Report Period supports a Completed Period option that resolves to the most recently fully completed week at execution time
    Given User selects "Completed Period" as Report Period, with only the "Weekly" sub-option enabled
    When User opens the week-definition dropdown
    Then all seven week-start/end combinations are available
      | Monday-Sunday      |
      | Tuesday-Monday     |
      | Wednesday-Tuesday  |
      | Thursday-Wednesday |
      | Friday-Thursday    |
      | Saturday-Friday    |
      | Sunday-Saturday    |
    Given the report executes on "<EXECUTION_DAY>" for the "<WEEK_COMBINATION>" combination
    Then the resolved date range is "<RESOLVED_RANGE>"
    And a full calendar day must have passed since the week's end day before that week counts as completed, otherwise it resolves to the prior week
    Examples:
      | EXECUTION_DAY | WEEK_COMBINATION | RESOLVED_RANGE          |
      | Tue Jan 7     | Monday-Sunday    | Mon Dec 30 - Sun Jan 5  |
      | Mon Jan 6     | Monday-Sunday    | Mon Dec 30 - Sun Jan 5  |
      | Sun Jan 5     | Monday-Sunday    | Mon Dec 23 - Sun Dec 29 |

  # Source: ET-24250
  @todo
  Scenario: Azure Blob Storage is available as a custom report destination with SAS URL or SAS Connection String authentication and a Test Access validation flow
    When User adds a new destination and selects "Azure Blob Storage" as the destination/storage type
    Then a tab switcher for auth method is shown - "SAS URL" and "SAS Connection String" - each with a "?" tooltip icon
    When User selects "SAS URL"
    Then a "SAS URL" input field, a Path URL field, and a "Test access" button are shown
    When User selects "SAS Connection String"
    Then a "SAS connection string" input field, a Path URL field, and a "Test access" button are shown
    When User runs Test Access with a valid, full-permission SAS credential
    Then the result reads "Connection successful. Permissions for file transfer validated." and the Test Access button is hidden until the SAS field is edited again
    When User runs Test Access with an invalid or malformed SAS credential
    Then the result reads "Connection failed. Please validate the SAS URL / connection string." and the button remains visible to retry
    When User runs Test Access with a valid but read-only SAS credential
    Then both "Connection successful." and "SAS is read-only. Please update permissions to allow file read and write." are shown together, and the button remains available to retry
    When User edits a SAS field after a Success result
    Then the success message clears and Test Access reappears
    Given the Azure Blob Storage destination is available in all 5 configuration locations
      | Life Portal - Account Report    |
      | Life Portal - Reporting         |
      | HCP portal - Custom destination |
      | HCP portal - Run Report         |
      | HCP portal - Schedule Report    |
    Then each location shows the correct, final Azure Blob icon, not a placeholder S3 icon

  # Source: ET-24236
  @todo
  Scenario: SFTP key upload auto-detects a PuTTY (.ppk) key and converts it to OpenSSH format before submission, prompting for a password when the key is encrypted
    When User uploads an OpenSSH/PEM-format SFTP key file
    Then it proceeds through the existing, unmodified upload flow
    When User uploads a PuTTY (.ppk) format SFTP key file
    Then it is auto-converted to OpenSSH format client-side before submission to the API
    And a Password field appears specifically for the PuTTY key, allowing the user to enter the password required to decrypt it
    Given a corrupted or malformed .ppk file
    Then a clear format error is shown rather than a silent conversion failure
    Given a .ppk file using an unsupported or deprecated key algorithm
    Then the existing algorithm-policy validation fires after conversion
    Given a non-key file renamed with a ".ppk" extension
    Then detection is based on file content/structure, not just the file extension
    And all existing file-size-limit validation continues to apply regardless of key format
