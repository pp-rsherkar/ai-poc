Feature: PMP Marketplace Regression - Sanity and Regression


  Scenario Outline: Verify premium deal can be assigned to a tactic with Toggle "Only Target Applied Deals" ON
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And User clicks on Deals Targeting
    And User assigns premium deals
    And User clicks on OK button of PMP Modal
    And User saves the changes
    Then Deals should be assigned to both targeting and PMP deals section
    Examples:
      | USER  | tacticPMP         |
      | Admin | TACTIC_0aa5cf1c-0 |

  Scenario Outline: Verify private deal can be assigned to a tactic with Toggle "Only Target Applied Deals" ON
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And   User clicks on Deals Targeting
    And  User assigns private deals
    And User clicks on OK button of PMP Modal
    And User saves the changes
    Then Deals should be assigned to both targeting and PMP deals section
    Examples:
      | USER  | tacticPMP         |
      | Admin | TACTIC_0aa5cf1c-0|

  Scenario Outline: Verify premium deal can be assigned to a tactic with Toggle "Only Target Applied Deals" OFF
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And   User clicks on Deals Targeting
    And  User assigns premium deals
    And User turns the Only Target Applied deals OFF
    And User clicks on OK button of PMP Modal
    And User saves the changes
    Then Deals should be assigned to only PMP deals section
    Examples:
      | USER  | tacticPMP    |
      | Admin | Tactic_0512_SSS |

  Scenario Outline: Verify private deal can be assigned to a tactic with Toggle "Only Target Applied Deals" OFF
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully
    And User has navigated to mentioned tactic "<tacticPMP>"
    When Targeting panel is opened on Tactic Settings tab
    And   User clicks on Deals Targeting
    And  User assigns private deals
    And User turns the Only Target Applied deals OFF
    And User clicks on OK button of PMP Modal
    And User saves the changes
    Then Deals should be assigned to only PMP deals section
    Examples:
      | USER  | tacticPMP   |
      | Admin | Tactic_0512_SSS|