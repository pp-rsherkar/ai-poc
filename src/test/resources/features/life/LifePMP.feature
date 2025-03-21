Feature: PMP Marketplace Regression - Sanity and Regression

  @regression
  Scenario Outline: Verify premium deal can be assigned to a tactic with Toggle "Only Target Applied Deals" ON
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And User clicks on Deals Targeting
    And User assigns premium deals
    And User clicks on OK button of PMP Modal
    And User saves the changes
    Then Deals should be assigned
    Examples:
      | USER  | tacticPMP                        |
      | Admin | UNIQUE TACTIC FOR PMP 1203 SSS 1 |

  Scenario Outline: Verify private deal can be assigned to a tactic with Toggle "Only Target Applied Deals" ON
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And   User clicks on Deals Targeting
    And  User assigns private deals
    And User clicks on OK button of PMP Modal
    And User saves the changes
    Then Deals should be assigned
    Examples:
      | USER  | tacticPMP                        |
      | Admin | UNIQUE TACTIC FOR PMP 1203 SSS 1 |