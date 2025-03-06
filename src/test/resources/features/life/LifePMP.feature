Feature: PMP Marketplace Regression - Sanity and Regression

  Background:Common for all the scenarios under sanity
    Given Life application is logged in as "<USER>"
    And User navigates to a "<CAMPAIGN>" page
    And User navigates to a line item page
    And User navigates to  a "<TACTIC>" page

  Scenario Outline: Verify Deals targeting is available under Media Supply Section
    When  Targeting panel is opened on "<TACTIC>" Settings tab
    Then Verify Deals Targeting is available under Media Supply Section
    Examples:
      | USER    | CAMPAIGN | LINE ITEM | TACTIC | ExpectedOutput                    | User Type |
      | admin11 |          |           |        | Deals targeting should be present | Internal  |

  Scenario Outline: Verify Deals targeting when clicked opens PMP Modal
    When  Targeting panel is opened on "<TACTIC>" Settings tab
    And   User clicks on Deals Targeting option
    Then  PMP Modal should open
    Examples:
      | USER    | TACTIC | ExpectedOutput        | User Type |
      | admin11 |        | PMP Modal should open | Internal  |

  Scenario Outline: Verify premium deal can be assigned to a tactic with Toggle "Only Target Applied Deals" ON
    When Targeting panel is opened on "<TACTIC>" Settings tab
    And   User clicks on Deals Targeting
    And  User navigates to premium deals tab
    And User selects multiple premium deals
    And User ensures the toggle "Only Target Applied Deals" is ON
    And User clicks on OK
    And User clicks on Save
    Then Deals should be assigned to both targeting and PMP deals section
    Examples:
      | USER    | TACTIC | ExpectedOutput                   | User Type |
      | admin11 |        | Premium deals should be assigned | Internal  |

  Scenario Outline: Verify private deal can be assigned to a tactic with Toggle "Only Target Applied Deals" ON
    When Targeting panel is opened on "<TACTIC>" Settings tab
    And   User clicks on Deals Targeting
    And  User navigates to private deals tab
    And User selects multiple private deals
    And User ensures the toggle "Only Target Applied Deals" is ON
    And User clicks on OK
    And User clicks on Save
    Then Deals should be assigned to both targeting and PMP deals section
    Examples:
      | USER    | TACTIC | ExpectedOutput                   | User Type |
      | admin11 |        | Private deals should be assigned | Internal  |