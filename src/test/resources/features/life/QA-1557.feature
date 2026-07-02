Feature: Deal Groups - Applied Only Tab Reflects Associated Deals
  1. Verify "Applied Only" tab count matches the "APPLIED DEALS" summary panel across deal group sizes
  2. Verify "Applied Only" tab lists deals with metadata consistent with the "All Deals" view
  3. Verify "Applied Only" tab visibility and data by user type and Applied Deals feature flag state
  4. Verify "Applied Only" tab updates immediately when a deal is applied from the "All Deals" tab mid-session
  5. Verify "Applied Only" tab does not show an empty list or an indefinite loader when deals are present (QA-1544 regression)
  6. Verify archived deals within a Deal Group still appear in the "Applied Only" list for existing groups
  7. Verify rapid switching between "All Deals" and "Applied Only" tabs does not break the list or show stale data

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"

  @todo
  Scenario Outline: Verify "Applied Only" tab count matches the "APPLIED DEALS" summary panel for "<DEAL_GROUP>"
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply section # NEW STEP
    And User navigates to Deal Groups page # NEW STEP
    When User selects the "<DEAL_GROUP>" Deal Group # NEW STEP
    Then Deal modal should open with "All Deals" and "Applied Only" tabs and "APPLIED DEALS" summary panel # NEW STEP
    When User clicks "Applied Only" Deals Tab
    Then Verify "Applied Only" tab count matches "<EXPECTED_COUNT>" and the "APPLIED DEALS" summary panel shows the same count # NEW STEP
    Examples:
      | DEAL_GROUP        | EXPECTED_COUNT |
      | Empty_Deal_Group  | 0              |
      | Single_Deal_Group | 1              |
      | Multi_Deal_Group  | 4              |
      | Large_Deal_Group  | 100            |

  @todo
  Scenario: Verify "Applied Only" tab lists deals with metadata consistent with the "All Deals" view
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply section
    And User navigates to Deal Groups page
    When User selects the "Multi_Deal_Group" Deal Group
    Then Deal modal should open with "All Deals" and "Applied Only" tabs and "APPLIED DEALS" summary panel
    When User clicks "Applied Only" Deals Tab
    Then Verify the deal list under "Applied Only" tab displays the following details for each associated deal # NEW STEP
      | Deal Name  | SSP/Exchange | Deal ID | Floor Price | Est. Avails Yst |
      | Deal_Name_ | JW Player    | Deal_   | 230         | 500000          |
    And Verify the "Applied Only" tab metadata matches the metadata shown for the same deals in the "All Deals" tab # NEW STEP

  @todo
  Scenario Outline: Verify "Applied Only" tab visibility and data by user type and Applied Deals feature flag state
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an "<USER_TYPE>" # NEW STEP
    And Applied Deals feature flag is "<FEATURE_STATE>" for the account # NEW STEP
    And User navigates to Supply section
    And User navigates to Deal Groups page
    When User selects the "Multi_Deal_Group" Deal Group
    Then Verify "Applied Only" tab is "<TAB_VISIBILITY>" # NEW STEP
    Examples:
      | USER_TYPE      | FEATURE_STATE | TAB_VISIBILITY                            |
      | internal user  | ON            | visible with the corrected count and list |
      | external user  | ON            | visible with the corrected count and list |
      | internal user  | OFF           | hidden                                    |
      | external user  | OFF           | hidden                                    |

  @todo
  Scenario: Verify "Applied Only" tab updates immediately when a deal is applied from the "All Deals" tab mid-session
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply section
    And User navigates to Deal Groups page
    When User selects the "Single_Deal_Group" Deal Group
    Then Deal modal should open with "All Deals" and "Applied Only" tabs and "APPLIED DEALS" summary panel
    When User clicks "All Deals" Deals Tab
    And User searches the deal and assign it from the deal list
    Then Verify the "APPLIED DEALS" summary panel count increments immediately without closing the modal # NEW STEP
    When User clicks "Applied Only" Deals Tab
    Then Verify the newly applied deal appears in the "Applied Only" list immediately without closing the modal # NEW STEP

  @todo
  Scenario: Verify "Applied Only" tab does not show an empty list or an indefinite loader when deals are present
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply section
    And User navigates to Deal Groups page
    When User selects the "Multi_Deal_Group" Deal Group
    Then Deal modal should open with "All Deals" and "Applied Only" tabs and "APPLIED DEALS" summary panel
    When User clicks "Applied Only" Deals Tab
    Then Verify the tab does not display a loader indefinitely # NEW STEP
    And Verify the deal list is not empty when the "APPLIED DEALS" summary panel shows deals are associated # NEW STEP

  @todo
  Scenario: Verify archived deals within a Deal Group still appear in the "Applied Only" list for existing groups
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply section
    And User navigates to Deal Groups page
    When User selects the "Multi_Deal_Group" Deal Group
    And User clicks 3 dot menu and selects Archive button for the active deal from the deal listing
    When User clicks "Applied Only" Deals Tab
    Then Verify the archived deal still appears in the "Applied Only" list # NEW STEP

  @todo
  Scenario: Verify rapid switching between "All Deals" and "Applied Only" tabs does not break the list or show stale data
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply section
    And User navigates to Deal Groups page
    When User selects the "Multi_Deal_Group" Deal Group
    Then Deal modal should open with "All Deals" and "Applied Only" tabs and "APPLIED DEALS" summary panel
    When User rapidly switches between "All Deals" and "Applied Only" tabs multiple times # NEW STEP
    Then Verify the deal list does not break or show stale data on either tab # NEW STEP
