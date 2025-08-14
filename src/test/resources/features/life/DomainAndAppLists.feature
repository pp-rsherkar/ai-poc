Feature: LIFE Regression - Create Domain and App Bundle Lists of following types:
  1. Domain List by entering domain names manually
  2. Domain List by uploading file


  @regression
  Scenario Outline: Create Domain List by entering domain names manually
    Given User navigates to the Domain List page
    And Verify that the search option is present on the "Domain/App Lists" tab
    And Verify that the sub-tabs "<SUB_TABS>" on the left navigation panel are available and "Both" is selected by default
    And Verify that when the "Domains" tab is selected, only "domain" lists are visible in the panel
    When User clicks on Create New List
    Then Verify that the Create Domain List screen is displayed
    And Verify that an error message is displayed when no list names "<LIST_NAME>" or domain names "<DOMAIN_NAMES>" are specified
    And Verify that if multiple domain names "<DOMAIN_NAMES>" are specified on a single line, a validation error is shown
    And Verify that when domain names are specified manually "<DOMAIN_NAMES>", the option to upload a file disappears
    And Verify that the user is able to create a domain list by specifying domain names manually
    And Verify that PulsePoint provided domain list "Automation_DomainList" is denoted with a purple P icon
    And Verify that the counter on the left displays the correct value for each list in the navigation panel
    And Verify that the user is able to edit an existing domain name list "<EDITED_DOMAIN_NAMES>"
    And Verify that the user is able to delete an existing domain name list
    Examples:
      | SUB_TABS                   | LIST_NAME | DOMAIN_NAMES                     | EDITED_DOMAIN_NAMES              |
      | Both, Domains, App Bundles | Domain    | domaintest.com, domaintest.co.in | puslepoint.com, pulsepoint.co.in |

  @regression
  Scenario Outline: Create Domain List by entering domain names manually
    Given User navigates to the Domain List page
    And Verify that the search option is present on the "Domain/App Lists" tab
    And Verify that the sub-tabs "<SUB_TABS>" on the left navigation panel are available and "Both" is selected by default
    And Verify that when the "Domains" tab is selected, only "domain" lists are visible in the panel
    When User clicks on Create New List
    Then Verify that the Create Domain List screen is displayed
    And Verify that an error message is displayed when no list names is specified and user tries to upload a file "<UPLOAD_FILENAME1>"
    And Verify that when enters "<LIST_NAME>" and upload file "<UPLOAD_FILENAME1>" option is selected, the text area to direct enter the domain names disappears
    And Verify the Uploaded Files section displays the domain count, includes download and delete icons after the file "<UPLOAD_FILENAME1>" is uploaded
    And Verify that the user is able to create a domain list through file upload
    And Verify that the counter on the left displays the correct value after file upload "<UPLOAD_FILENAME1>"
    And Verify that the user is able to edit an existing domain name list by uploading same file "<UPLOAD_FILENAME1>" again and verify the changes
    And Verify that the user is able to edit and save an existing domain name list by uploading another file "<UPLOAD_FILENAME2>" and verify the changes
    And Verify that the counter on the left displays the updated value after new file upload "<UPLOAD_FILENAME2>"
    And Verify that user is able to download the uploaded file "<UPLOAD_FILENAME1>", "<UPLOAD_FILENAME2>"
    And Verify that the user is able to delete the uploaded file "<UPLOAD_FILENAME1>"
    And Verify that the user is able to delete an existing domain name list
    Examples:
      | SUB_TABS                   | LIST_NAME        | UPLOAD_FILENAME1    | UPLOAD_FILENAME2    |
      | Both, Domains, App Bundles | FileUploadDomain | DomainNameFile1.csv | DomainNameFile2.csv |