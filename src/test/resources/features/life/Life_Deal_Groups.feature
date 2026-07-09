Feature: Life Deal Groups - Verify Applied Deals Tab Count and List Accuracy on the Deal PMP Modal
  1. Verify Applied Deals tab displays accurate count and list for Deal Groups with varying applied deal volumes
  2. Verify Applied Deals tab reflects deals added or removed from a Deal Group after the modal was first opened
  3. Verify Applied Deals tab does not retain data from a previously viewed Deal Group when switching between Deal Groups

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Deal Groups page

  @todo
  @regression
  Scenario Outline: Verify Applied Deals tab count and list accuracy for Deal Group "<DEAL_GROUP_NAME>" with "<DEAL_COUNT>" applied deals
    When User creates a new Deal Group named "<DEAL_GROUP_NAME>"
    And User opens the Deal PMP modal for the Deal Group
    And User applies "<DEAL_COUNT>" deals to the Deal Group
    And User clicks the Applied Deals tab
    Then Applied Deals tab should display a count of "<DEAL_COUNT>"
    And Applied Deals tab should list all applied deals matching the displayed count
    Examples:
      | DEAL_GROUP_NAME  | DEAL_COUNT |
      | Zero_Deal_Group  | 0          |
      | Small_Deal_Group | 3          |
      | Large_Deal_Group | 50         |

  @todo
  @regression
  Scenario: Verify Applied Deals tab reflects deals added or removed from a Deal Group after the modal was first opened
    When User creates a new Deal Group named "Sync_Deal_Group"
    And User opens the Deal PMP modal for the Deal Group
    And User applies "2" deals to the Deal Group
    And User clicks the Applied Deals tab
    Then Applied Deals tab should display a count of "2"
    When User removes "1" deal from the Deal Group while the modal remains open
    Then Applied Deals tab should update the count to "1" without requiring a page reload
    When User applies "1" additional deal to the Deal Group while the modal remains open
    Then Applied Deals tab should update the count to "2" reflecting the current state

  @todo
  @regression
  Scenario: Verify Applied Deals tab does not retain data from a previously viewed Deal Group when switching between Deal Groups
    When User creates a new Deal Group named "DealGroup_A"
    And User opens the Deal PMP modal for the Deal Group
    And User applies "3" deals to the Deal Group
    And User clicks the Applied Deals tab
    Then Applied Deals tab should display a count of "3"
    When User closes the Deal PMP modal
    And User creates a new Deal Group named "DealGroup_B"
    And User opens the Deal PMP modal for the Deal Group
    And User clicks the Applied Deals tab
    Then Applied Deals tab should display a count of "0" and not retain data from Deal Group "DealGroup_A"
