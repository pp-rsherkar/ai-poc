Feature: External user permission for studio

  @regression1
    Scenario Outline: Verify external user permission for studio application
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    When "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Navigate to administration
    And Click on accounts tab
    And Locate an account "<EXTERNAL_ACCOUNT>" with external user permission and select it
    And Go to users tab and select studio tab
    Then Turn on studio toggle for external users
    Then Verify studio platform is enabled for "External User"

    Examples:
      | EXTERNAL_ACCOUNT |
      | hmt demo      |


