Feature: LIFE Regression - Create Attribute NPI List

  @regression
  Scenario Outline: Create Attribute NPI List by uploading file with NPI Attributes
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
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
      | LIST_NAME  | ADVERTISER     | FILE_NAME             | COLUMN_NAME |
      | ATTRIBUTE  | 01- Advertiser | NPIAttributeList.xlsx | NPI         |