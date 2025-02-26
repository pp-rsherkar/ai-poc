Feature: Studio Platform Permissions and Access


  Scenario: Verify "Download NPI" functionality on studio platform
    Given the admin user is logged into the system as "<user>"
    When user navigates to studio platform
    And search for workspace
    Then user clicks on the searched workspace

