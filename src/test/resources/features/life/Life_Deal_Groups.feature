Feature: Life Deal Groups - Verify Applied Deals Tab Count and List Accuracy in Deal (PMP) Modal
  1. Verify Applied Deals tab count and list accuracy for Deal Groups with zero, few, and many applied deals
  2. Verify Applied Deals tab reflects deals added to or removed from a Deal Group after the modal is opened
  3. Verify Applied Deals tab does not retain a previous Deal Group's data when switching between Deal Groups

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Deal Groups page

  @todo
  Scenario Outline: Verify Applied Deals tab count and list accuracy for Deal Group "<DEAL_GROUP>" with "<APPLIED_DEALS_COUNT>" applied deals
    When User opens the Deal (PMP) modal for Deal Group "<DEAL_GROUP>"
    And User clicks Applied Deals tab
    Then Applied Deals tab should display a count of "<APPLIED_DEALS_COUNT>"
    And Applied Deals tab should list "<APPLIED_DEALS_COUNT>" deals
    And the displayed count should match the number of listed deals
    Examples:
      | DEAL_GROUP       | APPLIED_DEALS_COUNT |
      | Zero_Deal_Group  | 0                   |
      | Small_Deal_Group | 3                   |
      | Large_Deal_Group | 50                  |

  @todo
  Scenario Outline: Verify Applied Deals tab reflects deals "<ACTION>" to Deal Group "<DEAL_GROUP>" after the Deal (PMP) modal is opened
    Given User opens the Deal (PMP) modal for Deal Group "<DEAL_GROUP>"
    And User clicks Applied Deals tab
    And Applied Deals tab should display a count of "<BEFORE_COUNT>"
    When a deal is "<ACTION>" to Deal Group "<DEAL_GROUP>" outside the modal
    And User refreshes the Applied Deals tab
    Then Applied Deals tab should display a count of "<AFTER_COUNT>"
    And Applied Deals tab should list "<AFTER_COUNT>" deals
    Examples:
      | DEAL_GROUP       | ACTION  | BEFORE_COUNT | AFTER_COUNT |
      | Small_Deal_Group | added   | 3            | 4           |
      | Small_Deal_Group | removed | 3            | 2           |

  @todo
  Scenario: Verify Applied Deals tab does not retain previous Deal Group data when switching between Deal Groups
    Given User opens the Deal (PMP) modal for Deal Group "Small_Deal_Group"
    And User clicks Applied Deals tab
    And Applied Deals tab should display a count of "3"
    When User closes the Deal (PMP) modal
    And User opens the Deal (PMP) modal for Deal Group "Large_Deal_Group"
    And User clicks Applied Deals tab
    Then Applied Deals tab should display a count of "50"
    And Applied Deals tab should not list any deals from Deal Group "Small_Deal_Group"
