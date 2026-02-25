Feature: POC on healing locators if it gives exceptions and to find the alternate locators for the elements which are failing due to stale element exception or any other exception.


  @e2e
  Scenario: Verify comments addition on Campaign Dashboard and validate it on Campaign, Line Item and Tactic pages
    Given This scenario is executing in the "Demo" environment as aa "User"
    And "Life" application is logged in withh Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayedd with title as "Campaigns"