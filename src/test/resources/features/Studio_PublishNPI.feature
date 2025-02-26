Feature: Studio Publish NPI List

  Scenario Outline: Publish NPI List as Static List or Live List
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully
    And User searches the "<workspace>" and selects it
    And Download button is enabled to the user
    And User clicks on Publish NPI List
    And User selects publish "<listType>"
    And User select the system to publish the list
    Then The list is available in NPI Lists page
    Examples:
      | workspace       | | listType       |
      | PB_Test_Verify1 | |   Static       |
      | PB_Test_Check_3 | |   Live         |