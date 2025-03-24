Feature: Publish NPI List as Static List or Live List in Studio and Verify in NPI List in LIFE

  Scenario Outline: Publish NPI List as Static List or Live List
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully
    And User searches the "<WORKSPACE>" and selects it
    And Download button is enabled to the user
    And User clicks on Publish NPI List
    And User selects publish "<LIST_TYPE>"
    And User select the system to publish the list
    Then Verify list is published
    Examples:
      | WORKSPACE        | LIST_TYPE      |
      | PB_Test_Check_3  |   Live         |

  Scenario Outline: Verify the published Studio list in the LIFE
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Life" application is logged in successfully
    And User navigates to NPI Lists page
    And User searches the "<STUDIO_LIST>" in LIFE and selects it
    And User clicks on the published "<STUDIO_LIST>"
    Then User Verify the list is displayed in the Life
    Examples:
      | STUDIO_LIST     |
      | PB_Test_Check_3 |

