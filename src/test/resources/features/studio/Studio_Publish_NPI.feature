Feature: Studio Publish NPI List

  @regression
  Scenario Outline: Publish NPI List as Static List or Live List
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User searches the "<WORKSPACE>" and selects it
    And Download button is enabled to the user
    And User clicks on Publish NPI List
    And User selects publish "<LIST_TYPE>"
    And User select the system to publish the list
    Then Verify list is published
    Examples:
      | WORKSPACE           | LIST_TYPE |
      | Explorer_c7b72c12-8 | Static    |
      | Explorer_eb41551a-c | Live      |