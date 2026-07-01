Feature: Deal Groups - Applied Only Tab Count and List Sync
  1. Verify Applied Only tab count and list reflect deals associated with the Deal Group across data volumes
  2. Verify Applied Only list metadata matches the All Deals view and Applied Deals summary panel
  3. Verify Applied Only tab visibility and data by user type and feature flag state
  4. Verify Applied Only count and list update immediately on mid-session association changes
  5. Verify Applied Only tab is resilient to rapid tab switching
  6. Regression: Applied Only tab must not show an empty list or indefinite loader when deals are present (QA-1544)
  7. Verify archived deal visibility within an existing Deal Group's Applied Only list (ET-24696 - open question, see PR notes)

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"

  @todo
  Scenario Outline: Verify Applied Only tab count and list reflect deals associated with the Deal Group for "<DEAL_GROUP_SIZE>" deal group
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply > Deal Groups page # NEW STEP
    When User opens the Deal Group named "<DEAL_GROUP_NAME>" # NEW STEP
    Then Deal modal should open with "All Deals" and "Applied Only" tabs # NEW STEP
    When User clicks "Applied Only" Deals Tab # NEW STEP
    Then "Applied Only" tab should display count "<EXPECTED_COUNT>" matching the "APPLIED DEALS" summary panel # NEW STEP
    And the central list should display exactly "<EXPECTED_COUNT>" deals associated with the Deal Group # NEW STEP
    Examples:
      | DEAL_GROUP_SIZE | DEAL_GROUP_NAME       | EXPECTED_COUNT |
      | Empty           | DealGroup_Empty_Auto  | 0              |
      | Single deal     | DealGroup_Single_Auto | 1              |
      | Multiple deals  | DealGroup_Multi_Auto  | 4              |
      | Large volume    | DealGroup_Large_Auto  | 100            |

  @todo
  Scenario: Verify Applied Only list displays deal metadata consistent with the All Deals view and Applied Deals summary panel
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply > Deal Groups page # NEW STEP
    When User opens the Deal Group named "DealGroup_Multi_Auto" # NEW STEP
    And User clicks "Applied Only" Deals Tab # NEW STEP
    Then each deal row in the Applied Only list should display the following metadata matching the All Deals view # NEW STEP
      | Deal Name | SSP/Exchange | Deal ID | Floor Price | Est. Avails Yst |

  @todo
  Scenario Outline: Verify Applied Only tab shows corrected count and list for "<USER_TYPE>" when the feature is enabled
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" <USER_TYPE>
    And the Applied Only tab feature flag is "ON" for the account # NEW STEP
    And User navigates to Supply > Deal Groups page # NEW STEP
    When User opens the Deal Group named "DealGroup_Multi_Auto" # NEW STEP
    And User clicks "Applied Only" Deals Tab # NEW STEP
    Then "Applied Only" tab should display count "4" matching the "APPLIED DEALS" summary panel # NEW STEP
    And the central list should display exactly "4" deals associated with the Deal Group # NEW STEP
    Examples:
      | USER_TYPE                                          |
      | as an internal user                                |
      | as an external user with modification permissions  |

  @todo
  Scenario: Verify Applied Only tab remains hidden when the feature flag is OFF
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And the Applied Only tab feature flag is "OFF" for the account # NEW STEP
    And User navigates to Supply > Deal Groups page # NEW STEP
    When User opens the Deal Group named "DealGroup_Multi_Auto" # NEW STEP
    Then the "Applied Only" tab should remain hidden in the Deal modal # NEW STEP

  @todo
  Scenario: Verify Applied Only count and list update immediately after adding a deal from the All Deals tab without closing the modal
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply > Deal Groups page # NEW STEP
    When User opens the Deal Group named "DealGroup_Single_Auto" # NEW STEP
    And User clicks "Applied Only" Deals Tab # NEW STEP
    Then "Applied Only" tab should display count "1" matching the "APPLIED DEALS" summary panel # NEW STEP
    When User clicks "All Deals" Deals Tab # NEW STEP
    And User searches the deal and assign it from the deal list
    And User clicks "Applied Only" Deals Tab # NEW STEP
    Then "Applied Only" tab should display count "2" matching the "APPLIED DEALS" summary panel # NEW STEP
    And the central list should display exactly "2" deals associated with the Deal Group # NEW STEP

  @todo
  Scenario: Verify rapid switching between All Deals and Applied Only tabs does not break or show stale data
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply > Deal Groups page # NEW STEP
    When User opens the Deal Group named "DealGroup_Multi_Auto" # NEW STEP
    And User rapidly switches between "All Deals" and "Applied Only" tabs multiple times # NEW STEP
    Then the "Applied Only" tab should display the correct count and list without errors or stale data # NEW STEP

  @todo
  Scenario: Verify Applied Only tab does not show an empty list or an indefinite loader when deals are present
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply > Deal Groups page # NEW STEP
    When User opens the Deal Group named "DealGroup_Multi_Auto" # NEW STEP
    And User clicks "Applied Only" Deals Tab # NEW STEP
    Then the Applied Only list should render deals without displaying an indefinite loader # NEW STEP
    And the "Applied Only" tab should not display an empty list when deals are present # NEW STEP

  @todo
  Scenario: Verify an archived deal within an existing Deal Group still appears in the Applied Only list
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply > Deal Groups page # NEW STEP
    When User opens the Deal Group named "DealGroup_WithArchived_Auto" # NEW STEP
    And User clicks "Applied Only" Deals Tab # NEW STEP
    Then the archived deal should appear in the Applied Only list marked as archived # NEW STEP
    And "Applied Only" tab should display count matching the "APPLIED DEALS" summary panel
