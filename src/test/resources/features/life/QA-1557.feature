Feature: Deal Groups - Applied Only Tab Data Synchronization

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"

  @todo
  Scenario Outline: Verify Applied Only tab count matches Applied Deals summary panel for varying deal group sizes
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    # NEW STEP
    And User navigates to Supply, Deal Groups section
    # NEW STEP
    When User opens the Deal modal for a Deal Group containing "<DEAL_COUNT>" deals
    # NEW STEP
    Then Applied Only tab should display the count as "<EXPECTED_LABEL>"
    # NEW STEP
    And Applied Only tab list should display "<DEAL_COUNT>" deals matching the count shown in the Applied Deals summary panel
    Examples:
      | DEAL_COUNT | EXPECTED_LABEL     |
      | 0          | Applied Only (0)   |
      | 4          | Applied Only (4)   |
      | 100        | Applied Only (100) |

  @todo
  Scenario: Verify Applied Only tab list renders without performance lag for a Deal Group with a high volume of deals
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply, Deal Groups section
    When User opens the Deal modal for a Deal Group containing 100 or more deals
    Then Applied Only tab should display the correct deal count
    And Applied Only tab list should render without noticeable performance lag

  @todo
  Scenario: Verify Applied Only tab list displays deal metadata consistent with the All Deals view
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply, Deal Groups section
    When User opens the Deal modal for a Deal Group with applied deals
    And User clicks Applied Only tab
    Then the deal list displays the following columns in order:
      | Column             |
      | Deal Name          |
      | SSP/Exchange        |
      | Deal ID             |
      | Floor Price         |
      | Est. Avails Yst     |
    And Verify the displayed metadata matches the All Deals view for the same deals

  @todo
  Scenario: Verify Applied Only tab count and list update immediately after adding a deal from All Deals tab without closing the modal
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply, Deal Groups section
    And User opens the Deal modal for an existing Deal Group
    When User clicks All Deals tab and adds a new deal to the Deal Group
    And User clicks Applied Only tab without closing the modal
    Then Applied Only tab count should update immediately to reflect the newly added deal
    And Applied Only tab list should display the newly added deal without requiring a manual refresh

  @todo
  Scenario: Verify behavior of archived or hidden deals within the Applied Only tab for existing Deal Groups
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply, Deal Groups section
    And User opens the Deal modal for a Deal Group that contains an archived or hidden deal
    When User clicks Applied Only tab
    Then Verify whether the archived or hidden deal appears in the Applied Only list per current defined behavior
    And Verify the Applied Only count reflects the same treatment of archived or hidden deals as the Applied Deals summary panel

  @todo
  Scenario Outline: Verify Applied Only tab visibility and data based on user type and feature flag state
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an "<USER_TYPE>"
    And User navigates to Supply, Deal Groups section
    When User opens the Deal modal for a Deal Group with applied deals with feature flag "<FEATURE_FLAG>"
    Then Verify the Applied Only tab behavior is "<EXPECTED_BEHAVIOR>"
    Examples:
      | USER_TYPE     | FEATURE_FLAG | EXPECTED_BEHAVIOR                                   |
      | Internal User | ON           | tab is visible with correct count and deal list     |
      | External User | ON           | tab is visible with correct count and deal list     |
      | Internal User | OFF          | tab remains hidden per interim logic                |

  @todo
  Scenario: Verify Applied Only tab does not display an empty list or an indefinite loader when deals are present
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply, Deal Groups section
    And User opens the Deal modal for a Deal Group with applied deals
    When User clicks Applied Only tab
    Then Verify the deal list loads and displays the applied deals without showing an empty state
    And Verify no indefinite loading indicator is displayed once the list has loaded

  @todo
  Scenario: Verify rapid switching between All Deals and Applied Only tabs does not break the list or display stale data
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply, Deal Groups section
    And User opens the Deal modal for a Deal Group with applied deals
    When User rapidly switches between All Deals and Applied Only tabs multiple times
    Then Verify each tab displays its correct list without errors
    And Verify no stale data is displayed after rapid tab switching
