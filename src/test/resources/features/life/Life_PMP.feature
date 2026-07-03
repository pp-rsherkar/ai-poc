Feature: Life PMP Regression - Verify Private and Life MarketPlace Deals Creation and Assignment
  1. Verify Private Deals Tab
  2. Verify Life Marketplace Deals Tab
  3. Addition of Private Deals and assigned to a tactic when Only Target Applied Deals toggle is ON
  4. Addition of Private Deals and assigned to a tactic when Only Target Applied Deals toggle is OFF

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
    And User selects the "Display Advanced" as channel
    And User selects "Behavioral Segment" as rule type and configures the targeting rules, and saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab

  @regression
  Scenario: Verify Private Deals Tab
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    Then User should see Add New Deal button, filters such as Exchange, Search
    And Verify that "Active" and "Archived" buttons are available and by default "Active" button is selected
    When User enters below details in respective search field, verify that the deal list appears based on the selected filters
      | SearchByName     | Deal                  |
      | SearchByExchange | PulsePoint, JW Player |

  @regression
  Scenario: Verify Life Marketplace Deals Tab
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Life Marketplace Deals" Deals Tab
    And Verify Edit icon availability for the deals listed under "Life Marketplace" Deals tab
    And Verify that "Premium Publisher" should not display in deals listing under Life Marketplace Deals tab
    When User enters below details in respective search field, verify that the deal list appears based on the selected filters
      | SearchByName     | Deal     |
      | SearchByExchange | Pubmatic |

  @regression
  Scenario Outline: Add New Private Deals with deal price type "<DEAL_PRICE_TYPE>", pricing strategy "<PRICING_STRATEGY>" and assign to a tactic
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    And Verify Deal Type field is available with default value as "PMP"
    And Verify Curator field is available with default value as "Client"
    And Verify Pricing Type field is available with default value as "Floor"
    Then New Deal panel should open and user should be able to add new deal with details "<EXCHANGE_TYPE>", "<DEAL_ID>", "<DEAL_NAME>", "<MEDIA_TYPE>", "<ADVERTISER>", "<DEAL_PRICE_TYPE>", "<PRICE>", "<CURATOR>"
    When User searches the deal and assign it from the deal list
    Then Verify Edit icon availability for the deals listed under "Private" Deals tab
    And Verify Clearing Price field is available and fetch the tool-tip details on hover for the field
    Then Selected Deals should appear in Applied Deals panel
    When User clicks on OK button
    Then Deal details should appear on Tactic Settings tab under Targeting section, Curated Markets and Deals section depending on toggle button status
    And Verify Pricing Strategy is editable and update it with "<PRICING_STRATEGY>" and "<VALUE>" for Deals present in Curated Markets and Deals section
    And Verify user can add new "Private" deals by clicking Add Deal button present in Curated Markets and Deals section using details "<EXCHANGE_TYPE>", "<DEAL_ID>", "<DEAL_NAME>", "<MEDIA_TYPE>", "<ADVERTISER>", "<DEAL_PRICE_TYPE>", "<PRICE>", "<CURATOR>"
    And Verify Base Bid Price "<BASE_BID_PRICE>" and Max Bid Price "<MAX_BID_PRICE>" fields are editable when deals are targeted
    When User clicks Save button from Tactic Setting tab
    Then Deals should get assigned to the Tactic
    Examples:
      | EXCHANGE_TYPE | DEAL_ID | DEAL_NAME  | MEDIA_TYPE                 | DEAL_PRICE_TYPE | PRICE | BASE_BID_PRICE | MAX_BID_PRICE | ADVERTISER     | CURATOR                          | PRICING_STRATEGY | VALUE |
      | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | Fixed           | 230   | 34             | 60            | 01- Advertiser | PulsePoint (Direct Integrations) | Flat             | 35    |
      | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | Floor           | 230   | 34             | 60            | 01- Advertiser | PulsePoint (Direct Integrations) | Floor+           |       |
      | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | Fixed           | 230   | 34             | 60            | 01- Advertiser | PulsePoint (Direct Integrations) | Default          |       |

  @regression
  Scenario Outline: Verify active deal moves to archived while campaign is not running state
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    And Verify Deal Type field is available with default value as "PMP"
    And Verify Curator field is available with default value as "Client"
    And Verify Pricing Type field is available with default value as "Floor"
    Then New Deal panel should open and user should be able to add new deal with details "<EXCHANGE_TYPE>", "<DEAL_ID>", "<DEAL_NAME>", "<MEDIA_TYPE>", "<ADVERTISER>", "<DEAL_PRICE_TYPE>", "<PRICE>", "<CURATOR>"
    When User searches the deal and assign it from the deal list
    And User clicks 3 dot menu and selects Archive button for the active deal from the deal listing
    And Verify Archive option is available based on the campaign state
    And User clicks "Archived" button from the search section of deal listing page
    Then Verify that the deal is moved to archived deal section
    Examples:
      | EXCHANGE_TYPE | DEAL_ID | DEAL_NAME  | MEDIA_TYPE                 | DEAL_PRICE_TYPE | PRICE | ADVERTISER     | CURATOR                          |
      | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | Fixed           | 230   | 01- Advertiser | PulsePoint (Direct Integrations) |

  @regression
  Scenario Outline: Verify active deal should not be deleted while campaign is running state
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "<EXCHANGE_TYPE>", "<DEAL_ID>", "<DEAL_NAME>", "<MEDIA_TYPE>", "<ADVERTISER>", "<DEAL_PRICE_TYPE>", "<PRICE>", "<CURATOR>"
    When User searches the deal and assign it from the deal list
    When User clicks on OK button
    Then Deal details should appear on Tactic Settings tab under Targeting section, Curated Markets and Deals section depending on toggle button status
    And User saves the settings
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User searches the deal and assign it from the deal list
    And User clicks 3 dot menu and selects Archive button for the active deal from the deal listing
    And Verify Archive option is available based on the campaign state
    And Verify the Tactic Link is available in the confirmation pop-up
    And Verify the Tactic Link is clickable and navigates to the respective tactic page
    Examples:
      | EXCHANGE_TYPE | DEAL_ID | DEAL_NAME  | MEDIA_TYPE                 | DEAL_PRICE_TYPE | PRICE | ADVERTISER     | CURATOR                          | CREATIVE      |
      | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | Fixed           | 230   | 01- Advertiser | PulsePoint (Direct Integrations) | Auto_Creative |

  @regression
  Scenario Outline: Verify that after deleting an active deal from targeting, the user is able to delete the deal while the campaign is in a running state
    And User assigns the existing creative named "<CREATIVE>", enables the tactic and saves the changes
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "<EXCHANGE_TYPE>", "<DEAL_ID>", "<DEAL_NAME>", "<MEDIA_TYPE>", "<ADVERTISER>", "<DEAL_PRICE_TYPE>", "<PRICE>", "<CURATOR>"
    When User searches the deal and assign it from the deal list
    When User clicks on OK button
    Then Deal details should appear on Tactic Settings tab under Targeting section, Curated Markets and Deals section depending on toggle button status
    And User saves the settings
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User searches the deal and assign it from the deal list
    And User clicks 3 dot menu and selects Archive button for the active deal from the deal listing
    And Verify Archive option is available based on the campaign state
    And Verify the Tactic Link is available in the confirmation pop-up
    And Verify the Tactic Link is clickable and navigates to the respective tactic page
    When User searches the deal and assign it from the deal list
    And User unassigns active deal from the applied deals section of All Deals tab
    When User clicks on OK button
    And User saves the settings
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User searches the deal and assign it from the deal list
    And User clicks 3 dot menu and selects Archive button for the active deal from the deal listing
    And Verify Archive option is available based on the campaign state
    And User clicks "Archived" button from the search section of deal listing page
    Then Verify that the deal is moved to archived deal section
    Examples:
      | EXCHANGE_TYPE | DEAL_ID | DEAL_NAME  | MEDIA_TYPE                 | DEAL_PRICE_TYPE | PRICE | ADVERTISER     | CURATOR                          | CREATIVE      |
      | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | Fixed           | 230   | 01- Advertiser | PulsePoint (Direct Integrations) | Auto_Creative |

  @todo
  Scenario Outline: Verify "Applied Only" tab count matches the "APPLIED DEALS" summary panel for "<DEAL_GROUP>"
    When User navigates to Supply section # NEW STEP
    And User navigates to Deal Groups page # NEW STEP
    And User selects the "<DEAL_GROUP>" Deal Group # NEW STEP
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
    When User navigates to Supply section
    And User navigates to Deal Groups page
    And User selects the "Multi_Deal_Group" Deal Group
    Then Deal modal should open with "All Deals" and "Applied Only" tabs and "APPLIED DEALS" summary panel
    When User clicks "Applied Only" Deals Tab
    Then Verify the deal list under "Applied Only" tab displays the following details for each associated deal # NEW STEP
      | Deal Name  | SSP/Exchange | Deal ID | Floor Price | Est. Avails Yst |
      | Deal_Name_ | JW Player    | Deal_   | 230         | 500000          |
    And Verify the "Applied Only" tab metadata matches the metadata shown for the same deals in the "All Deals" tab # NEW STEP

  @todo
  Scenario Outline: Verify "Applied Only" tab visibility and data by user type and Applied Deals feature flag state
    Given This scenario will be executed in the "Demo" environment as a "<USER_TYPE>" # NEW STEP
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Applied Deals feature flag is "<FEATURE_STATE>" for the account # NEW STEP
    And User navigates to Supply section
    And User navigates to Deal Groups page
    When User selects the "Multi_Deal_Group" Deal Group
    Then Verify "Applied Only" tab is "<TAB_VISIBILITY>" # NEW STEP
    Examples:
      | USER_TYPE      | FEATURE_STATE | TAB_VISIBILITY                            |
      | Internal User  | ON            | visible with the corrected count and list |
      | External User  | ON            | visible with the corrected count and list |
      | Internal User  | OFF           | hidden                                    |
      | External User  | OFF           | hidden                                    |

  @todo
  Scenario: Verify "Applied Only" tab updates immediately when a deal is applied from the "All Deals" tab mid-session
    When User navigates to Supply section
    And User navigates to Deal Groups page
    And User selects the "Single_Deal_Group" Deal Group
    Then Deal modal should open with "All Deals" and "Applied Only" tabs and "APPLIED DEALS" summary panel
    When User clicks "All Deals" Deals Tab
    And User searches the deal and assign it from the deal list
    Then Verify the "APPLIED DEALS" summary panel count increments immediately without closing the modal # NEW STEP
    When User clicks "Applied Only" Deals Tab
    Then Verify the newly applied deal appears in the "Applied Only" list immediately without closing the modal # NEW STEP

  @todo
  Scenario: Verify "Applied Only" tab does not show an empty list or an indefinite loader when deals are present
    When User navigates to Supply section
    And User navigates to Deal Groups page
    And User selects the "Multi_Deal_Group" Deal Group
    Then Deal modal should open with "All Deals" and "Applied Only" tabs and "APPLIED DEALS" summary panel
    When User clicks "Applied Only" Deals Tab
    Then Verify the tab does not display a loader indefinitely # NEW STEP
    And Verify the deal list is not empty when the "APPLIED DEALS" summary panel shows deals are associated # NEW STEP

  @todo
  Scenario: Verify archived deals within a Deal Group still appear in the "Applied Only" list for existing groups
    When User navigates to Supply section
    And User navigates to Deal Groups page
    And User selects the "Multi_Deal_Group" Deal Group
    And User clicks 3 dot menu and selects Archive button for the active deal from the deal listing
    When User clicks "Applied Only" Deals Tab
    Then Verify the archived deal still appears in the "Applied Only" list # NEW STEP

  @todo
  Scenario: Verify rapid switching between "All Deals" and "Applied Only" tabs does not break the list or show stale data
    When User navigates to Supply section
    And User navigates to Deal Groups page
    And User selects the "Multi_Deal_Group" Deal Group
    Then Deal modal should open with "All Deals" and "Applied Only" tabs and "APPLIED DEALS" summary panel
    When User rapidly switches between "All Deals" and "Applied Only" tabs multiple times # NEW STEP
    Then Verify the deal list does not break or show stale data on either tab # NEW STEP