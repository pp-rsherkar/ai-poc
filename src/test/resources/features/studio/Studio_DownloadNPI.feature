Feature: Studio Download NPI List

  @regression
  Scenario Outline: Verify "Download NPI" functionality on studio platform
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User searches the "<WORKSPACE>" and selects it
    Then user clicks on the searched workspace
    Then verify the file content
    #Then verify db result
    Examples:
      | WORKSPACE           |
      | Explorer_c7b72c12-8 |