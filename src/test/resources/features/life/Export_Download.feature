Feature: LIFE Regression - This feature verifies the export/download functionality for various list types created through file uploads:
  1. Keyword List
  2. Domain List
  3. App Bundle List
  4. IP Address List
  5. Static NPI List

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @regression
  Scenario Outline: Verify user is able to export Keyword list created by uploading a file
    Given User navigates to the "Keyword Lists" page
    And Verify that the search option is present on the "Keyword Lists" tab
    When User clicks on Create New List
    Then Verify that the Create New List screen is displayed
    And Verify that when enters "<LIST_NAME>" and upload file "<UPLOAD_FILENAME1>" option is selected, the text area to direct enter the names disappears
    And Verify the Uploaded Files section displays the entries count, includes download and delete icons after the file "<UPLOAD_FILENAME1>" is uploaded
    And Verify that the user is able to create a "Keywords" list through file upload
    And Verify that the counter on the left displays the correct value after file upload "<UPLOAD_FILENAME1>"
    And Verify that user is able to download the uploaded "Keyword" list
    And Verify the count of items in the downloaded "Keyword" list
    Examples:
      | LIST_NAME          | UPLOAD_FILENAME1  |
      | Keyword_FileUpload | KeywordsFile1.csv |

  @regression
  Scenario Outline: Verify user is able to export Domain list created by uploading a file
    Given User navigates to the "Domain & App Lists" page
    When User clicks on Create New List
    And User selects the "Domains" radio button from create new list page
    Then Verify that the Create New List screen is displayed
    And Verify that when enters "<LIST_NAME>" and upload file "<UPLOAD_FILENAME1>" option is selected, the text area to direct enter the names disappears
    And Verify the Uploaded Files section displays the entries count, includes download and delete icons after the file "<UPLOAD_FILENAME1>" is uploaded
    And Verify that the user is able to create a "Domains" list through file upload
    And Verify that the counter on the left displays the correct value after file upload "<UPLOAD_FILENAME1>"
    And Verify that user is able to download the uploaded "Domain" list
    And Verify the count of items in the downloaded "Domain" list
    Examples:
      | LIST_NAME         | UPLOAD_FILENAME1    |
      | Domain_FileUpload | DomainNameFile1.csv |

  @regression
  Scenario Outline: Verify user is able to export App Bundle list created by uploading a file
    Given User navigates to the "Domain & App Lists" page
    When User clicks on Create New List
    And User selects the "App Bundles" radio button from create new list page
    Then Verify that the Create New List screen is displayed
    And Verify that an error message is displayed when no list names is specified and user tries to upload a file "<UPLOAD_FILENAME1>"
    And Verify that when enters "<LIST_NAME>" and upload file "<UPLOAD_FILENAME1>" option is selected, the text area to direct enter the names disappears
    And Verify the Uploaded Files section displays the entries count, includes download and delete icons after the file "<UPLOAD_FILENAME1>" is uploaded
    And Verify that the user is able to create a "AppBundle" list through file upload
    And Verify that the counter on the left displays the correct value after file upload "<UPLOAD_FILENAME1>"
    And Verify that user is able to download the uploaded "App Bundle" list
    And Verify the count of items in the downloaded "App Bundle" list
    Examples:
      | LIST_NAME            | UPLOAD_FILENAME1   |
      | AppBundle_FileUpload | AppBundleFile1.csv |

  @regression
  Scenario Outline: Verify user is able to export IP list created by uploading a file
    And User navigates to the "IP Address Lists" page
    When User clicks on Create New List
    Then Verify that the Create New List screen is displayed
    And Verify that when enters "<LIST_NAME>" and upload file "<UPLOAD_FILENAME1>" option is selected, the text area to direct enter the names disappears
    And Verify the Uploaded Files section displays the entries count, includes download and delete icons after the file "<UPLOAD_FILENAME1>" is uploaded
    And Verify that the user is able to create a "IP Address" list through file upload
    And Verify that the counter on the left displays the correct value after file upload "<UPLOAD_FILENAME1>"
    And Verify that user is able to download the uploaded "IP" list
    And Verify the count of items in the downloaded "IP" list
    Examples:
      | LIST_NAME            | UPLOAD_FILENAME1   |
      | IPAddress_FileUpload | IPAddressFile1.csv |

  @regression
  Scenario Outline: Verify user is able to export the Regular NPI list created by uploading a file
    And User navigates to NPI Lists page
    When User clicks on Create New List
    Then Verify creation of NPI List screen is displayed
    And User selects Static List
    And User enters the NPI Static list details as "<LIST_NAME>" "<ADVERTISER>"
    And User uploads the file "<FILE_NAME>"
    When User makes list available in LIFE and saves the list
    Then Verify list gets saved successfully
    And Verify that user is able to download the uploaded "NPI" list
    And Verify the count of items in the downloaded "NPI" list
    Examples:
      | LIST_NAME  | ADVERTISER     | FILE_NAME          |
      | STATIC_NPI | 01- Advertiser | NPIStaticList.xlsx |
