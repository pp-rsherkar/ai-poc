Feature: LIFE Regression - Create Static NPI List

  @jenkins
  Scenario Outline: Create Static NPI List by specifying NPI Numbers.
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    When User clicks on Add List
    Then Verify creation of NPI List screen is displayed
    And User selects Static List
    And User enters the NPI list details as "<LIST_NAME>" "<ADVERTISER>" "<NPI_NUMBER>"
    When User makes list available in LIFE and saves the list
    Then Verify list gets saved successfully

    Examples:
      | ADVERTISER     | NPI_NUMBER | LIST_NAME  |
      | 01- Advertiser | 1478523698 | STATIC_NPI |









