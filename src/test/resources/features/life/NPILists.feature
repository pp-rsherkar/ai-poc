Feature: LIFE Regression - Create NPI List of following types:
  1. Static NPI List by specifying NPI Numbers
  2. Static NPI List by uploading file with NPI Numbers
  3. Smart NPI List by specifying Type
  4. Attribute NPI List
  5. Auto Import List creation
  6. Domain List by entering domain names manually
  7. Domain List by uploading file

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @regression
  Scenario Outline: Create Static NPI List by specifying NPI Numbers.
    And User navigates to NPI Lists page
    When User clicks on Create New List
    Then Verify creation of NPI List screen is displayed
    And User selects Static List
    And User enters the NPI list details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>"
    When User makes list available in LIFE and saves the list
    Then Verify list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | STATIC_NPI |

  @regression
  Scenario Outline: Create Static NPI List by uploading file with NPI Numbers
    And User navigates to NPI Lists page
    When User clicks on Create New List
    Then Verify creation of NPI List screen is displayed
    And User selects Static List
    When User tries to save the list without entering any details, an error message should be displayed
    And User enters the NPI Static list details as "<LIST_NAME>" "<ADVERTISER>"
    And User uploads the file "<FILE_NAME>"
    When User makes list available in LIFE and saves the list
    Then Verify list gets saved successfully
    When User edits the created list
    Then Verify list gets updated successfully
    When User deletes the created list
    Then Verify list gets deleted successfully

    Examples:
      | LIST_NAME  | ADVERTISER     | FILE_NAME          |
      | STATIC_NPI | 01- Advertiser | NPIStaticList.xlsx |


  @regression
  Scenario Outline: Create Smart NPI List by specifying Type.
    And User navigates to NPI Lists page
    When User clicks on Create New List
    Then Verify creation of NPI List screen is displayed
    And User selects Smart List to create NPI list
    And User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for "<Type>" with "<PROFESSION_VALUE>" "<SMART_PIXEL_DROPDOWN_VALUE>" "<NPI_GROUP_VALUE>"
    Then Save and Verify the list gets saved successfully


    Examples:
      | ADVERTISER     | LIST_NAME       | Type       | PROFESSION_VALUE   | SMART_PIXEL_DROPDOWN_VALUE | NPI_GROUP_VALUE                 |
      | 01- Advertiser | SMART_Pixel_NPI | Profession | Nurse Practitioner | AutoCollection889379612    | AutoAdminNPIFileUpload187526255 |

  @regression
  Scenario Outline: Create Attribute NPI List by uploading file with NPI Attributes
    And User navigates to NPI Lists page
    When User clicks on Create New List
    Then Verify creation of NPI List screen is displayed
    And User selects the Attributes List and uploads the file "<FILE_NAME>"
    Then Verify file "<FILE_NAME>" is uploaded successfully
    And User selects the "<COLUMN_NAME>" column and clicks on Next
    When User tries to save the Attribute list without entering any details, an error message should be displayed
    And User enters the Attributes list details as "<LIST_NAME>" "<ADVERTISER>"
    When User makes list available in LIFE and HCP365 and clicks on next
    Then Verify the Attributes list is saved successfully
    When User edits the saved list
    Then Verify the updates are applied successfully
    When User deletes the Attribute list
    Then Verify the list is deleted successfully

    Examples:
      | LIST_NAME | ADVERTISER     | FILE_NAME             | COLUMN_NAME |
      | ATTRIBUTE | 01- Advertiser | NPIAttributeList.xlsx | NPI         |

  @regression
  Scenario Outline: Create Auto-Imported NPI List with "<LIST_TYPE>" by uploading file using API
    And User navigates to NPI Lists page
    When User clicks on Create New List
    Then Verify creation of NPI List screen is displayed
    And User selects the Auto-Imported List
    And Verify if user navigates to the Auto-Imported List page
    Then User tries to save the Auto-Imported list without entering any details, an error message should be displayed
    When User enters the Auto-Imported list details as "<LIST_NAME>" "<ADVERTISER>"
    And User makes list available in LIFE and HCP365 module
    And User clicks Setup Import button to import File details
    And User enters file details "<FILE_LOCATION>" "<FILE_PATH>" "<FILE_NAME>"
    And User selects the List type "<LIST_TYPE>"
    And User enters NPI column "Name" "<NPI_COLUMN_NAME>"
    And User selects the Import type "<IMPORT_TYPE>"
    Then User clicks Check File button to verify the file details are correct
    Then User saves the import settings and verifies the is saved successfully
    And Run API to upload the data into the list
    And Verify list data is uploaded successfully
    And Verify the Total NPI count displayed in Matched NPI section is similar to NPI records present in "<FILE_NAME>"
    Examples:
      | LIST_NAME     | ADVERTISER     | FILE_LOCATION | FILE_PATH           | FILE_NAME                 | LIST_TYPE            | NPI_COLUMN_NAME | IMPORT_TYPE    |
      | Auto_Imported | 01- Advertiser | Our_VM        | /home/NPIAutoImport | AutoImport_Automation.csv | Plain List           | NPI             | Add new NPIs   |
      | Auto_Imported | 01- Advertiser | Our_VM        | /home/NPIAutoImport | AutoImport_Automation.csv | List with Attributes | NPI             | Import Columns |


  @regression
  Scenario Outline: Create Auto-Imported NPI List with "<LIST_TYPE>" by uploading file using Reload Now button
    And User navigates to NPI Lists page
    When User clicks on Create New List
    Then Verify creation of NPI List screen is displayed
    And User selects the Auto-Imported List
    And Verify if user navigates to the Auto-Imported List page
    Then User tries to save the Auto-Imported list without entering any details, an error message should be displayed
    When User enters the Auto-Imported list details as "<LIST_NAME>" "<ADVERTISER>"
    And User makes list available in LIFE and HCP365 module
    And User clicks Setup Import button to import File details
    And User enters file details "<FILE_LOCATION>" "<FILE_PATH>" "<FILE_NAME>"
    And User selects the List type "<LIST_TYPE>"
    And User enters NPI column "Name" "<NPI_COLUMN_NAME>"
    And User selects the Import type "<IMPORT_TYPE>"
    Then User clicks Check File button to verify the file details are correct
    Then User saves the import settings and verifies the is saved successfully
    And Verify Reload Now button is available and enabled
    When User clicks on Reload Now button
    Then Verify the file is reloaded successfully
    And Verify the Total NPI count displayed in Matched NPI section is similar to NPI records present in "<FILE_NAME>"
    Examples:
      | LIST_NAME | ADVERTISER     | FILE_LOCATION | FILE_PATH           | FILE_NAME                 | LIST_TYPE            | NPI_COLUMN_NAME | IMPORT_TYPE    |
      | ATTRIBUTE | 01- Advertiser | Our_VM        | /home/NPIAutoImport | AutoImport_Automation.csv | List With Attributes | NPI             | Import Columns |


  @e2e @regression
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
    And Verify that PulsePoint provided domain lists are denoted with a purple "P" icon
    And Verify that the counter on the left displays the correct value for each list in the navigation panel
    And Verify that the user is able to edit an existing domain name list "<EDITED_DOMAIN_NAMES>"
    And Verify that the user is able to delete an existing domain name list
    Examples:
      | SUB_TABS                   | LIST_NAME | DOMAIN_NAMES                     | EDITED_DOMAIN_NAMES              |
      | Both, Domains, App Bundles | Domain    | domaintest.com, domaintest.co.in | puslepoint.com, pulsepoint.co.in |

  @e2e @regression
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