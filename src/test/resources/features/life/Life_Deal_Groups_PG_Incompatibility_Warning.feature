Feature: LIFE Regression - PG Deal Incompatibility Warning Removal
  Adding a PG deal to a non-PG tactic no longer displays the Deal Incompatibility warning or tooltip.
  Deal delivery, pricing, fees, and unrelated incompatibility warnings remain correct after the removal.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "Line" "500", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "Tactic" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab

  # Source: ET-24697 (R01, R02, R03)
  @todo
  Scenario: Adding a PG deal to a non-PG tactic shows no incompatibility warning or tooltip
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User with the PG Workaround permission adds a PG deal to the non-PG tactic
    # Framework Gap: Requires step definitions for PG deal incompatibility warning checks in LifeSteps.java
    Then No Deal Incompatibility warning appears as a modal, toast or inline message
    And No deal incompatibility tooltip appears on hover over the deal

  # Source: ET-24697 (R04, GAP-2, AMB-2)
  @todo
  Scenario Outline: Baseline deal combinations remain warning-free and documented
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User adds a "<DEAL>" to a "<TACTIC>" with permission "<PERMISSION>"
    # Framework Gap: Requires step definitions for deal-tactic combination behavior in LifeSteps.java
    Then The behavior is "<EXPECTED>"
    Examples:
      | DEAL         | TACTIC     | PERMISSION            | EXPECTED                                   |
      | non-PG deal  | non-PG tactic | no PG Workaround   | deal added with no warning (unchanged)     |
      | PG deal      | PG tactic     | PG tactic setup    | deal added with no incompatibility message |
      | PG deal      | non-PG tactic | no PG Workaround   | documented actual behavior confirmed with team |

  # Source: ET-24697 (R06, R07, R10, HT-5051, HT-3601 regression)
  @todo
  Scenario: Regression - PG deal pricing, fees, delivery and persistence are correct after removal
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User with the PG Workaround permission adds a PG deal to the non-PG tactic and saves the tactic
    # Framework Gap: Requires step definitions for PG deal pricing, fee and delivery checks in LifeSteps.java
    Then The PG deal shows the correct pricing type Fixed or Floor as configured with no pricing display error
    And Only the Deal Fee is charged with no Platform Fee double-charge
    And The tactic bids and delivers on the PG deal with no bidding loss
    When User reloads the tactic
    Then The tactic loads with the PG deal present and no residual or stale warning state

  # Source: ET-24697 (R09 isolation)
  @todo
  Scenario: Unrelated incompatibility warnings still appear after the PG warning removal
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User sets up a genuinely incompatible deal scenario unrelated to PG on non-PG
    # Framework Gap: Requires step definitions for unrelated incompatibility warnings in LifeSteps.java
    Then Other incompatibility warnings still appear and only the PG-on-non-PG warning is removed
