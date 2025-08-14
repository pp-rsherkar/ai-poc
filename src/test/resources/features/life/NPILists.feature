Feature: LIFE Regression - Create NPI List of following types:
  1. Static NPI List by specifying NPI Numbers
  2. Static NPI List by uploading file with NPI Numbers
  3. Smart NPI List by specifying Type
  4. Attribute NPI List
  5. Auto Import List creation

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
    And User selects the "<LIST_TYPE>" radio button
    And User enters NPI column "Name" "<NPI_COLUMN_NAME>"
    And User selects the "<IMPORT_TYPE>"
    Then User clicks Check File button to verify the file details are correct
    Then User saves the import settings and verifies the data is imported successfully
    And Verify that Token is fetched successfully from URL "BuyerProxy.ashx"
    And Pass token in the API Header and run it to upload the data into the list
    And Verify list data is uploaded successfully
    And Refresh the Browser to view the data uploaded
    And Verify the Total NPI count displayed in Matched NPI section is similar to NPI records present in "<FILE_NAME>"
    Examples:
      | LIST_NAME     | ADVERTISER     | FILE_LOCATION | FILE_PATH                      | FILE_NAME                  | LIST_TYPE            | NPI_COLUMN_NAME | IMPORT_TYPE    |
      | Auto_Imported | 01- Advertiser | 1OurVM        | /home/NPIAutoImport/Automation | AutoImport_Automation1.csv | Plain List           | NPI             | Add new NPIs   |
      | Auto_Imported | 01- Advertiser | 1OurVM        | /home/NPIAutoImport/Automation | AutoImport_Automation1.csv | List with Attributes | NPI             | Import Columns |


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
    And User selects the "<LIST_TYPE>" radio button
    And User enters NPI column "Name" "<NPI_COLUMN_NAME>"
    And User selects the "<IMPORT_TYPE>"
    Then User clicks Check File button to verify the file details are correct
    Then User saves the import settings and verifies the data is imported successfully
    And Verify Reload Now button is available and enabled
    When User clicks on Reload Now button
    Then Verify the file is reloaded successfully
    And Verify the Total NPI count displayed in Matched NPI section is similar to NPI records present in "<FILE_NAME>"
    Examples:
      | LIST_NAME     | ADVERTISER     | FILE_LOCATION | FILE_PATH                      | FILE_NAME                  | LIST_TYPE            | NPI_COLUMN_NAME | IMPORT_TYPE    |
      | Auto_Imported | 01- Advertiser | 1OurVM        | /home/NPIAutoImport/Automation | AutoImport_Automation1.csv | List with Attributes | NPI             | Import Columns |