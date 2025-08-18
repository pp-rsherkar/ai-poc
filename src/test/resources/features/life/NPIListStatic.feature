Feature: LIFE Regression - Create Static NPI List

  @regression
  Scenario Outline: Create Static NPI List by specifying NPI Numbers.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to NPI Lists page
    When User clicks on Create New List
    And User selects Smart List to create NPI list
    And User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for "<Type>" with "<PROFESSION_VALUE>" "<SMART_PIXEL_DROPDOWN_VALUE>" "<NPI_GROUP_VALUE>"
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | LIST_NAME       | Type       | PROFESSION_VALUE   | SMART_PIXEL_DROPDOWN_VALUE | NPI_GROUP_VALUE                 |
      | 01- Advertiser | SMART_Pixel_NPI | Profession | Nurse Practitioner | AutoCollection889379612    | AutoAdminNPIFileUpload187526255 |


  @regression
  Scenario Outline: Verify Testcases and Assertions for Smart List NPI creation.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to NPI Lists page
    When User clicks on Create New List
    And User selects Smart List to create NPI list
    And User verify Smart List options present to create NPI list
    And User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for Smart Pixel with "<SMART_PIXEL_DROPDOWN_VALUE>"
    And User verify all fields under Smart pixel options
    And User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for NPI List with "<NPI_GROUP_VALUE>"
    And User verify all fields under NPI List options
    And User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for Speciality
    And User verify all fields under Speciality options
    And User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for Profession with "<PROFESSION_VALUE>"
    And User verify all fields under Profession options
    When User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for Prescribed drug
    And User verify all fields under Prescribed drug options
    When User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for Diagnosis
    And User verify all fields under Diagnosis options
    When User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for Medical Procedure
    And User verify all fields under Medical Procedure options
    When User enters the Smart NPI list details as "<LIST_NAME>" "<ADVERTISER>" for Expand based on Practice and Hospital affiliation
    And User verify all fields under Expand based on Practice and Hospital affiliation options
    Then Save and Verify the list gets saved successfully

    Examples:
      | ADVERTISER     | LIST_NAME       | PROFESSION_VALUE   | SMART_PIXEL_DROPDOWN_VALUE | NPI_GROUP_VALUE                 |
      | 01- Advertiser | SMART_Pixel_NPI  | Nurse Practitioner | AutoCollection889379612    | AutoAdminNPIFileUpload187526255 |