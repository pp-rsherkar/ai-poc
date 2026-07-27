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
  # Source: ET-24698
  Scenario: Associated Tactics displays new metric columns and an Impressions tooltip
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User navigates to Deal Groups > Associated Tactics for a delivering deal
    Then Columns Impressions, Win Rate, Clearing Price, Total Spend, and Media Spend are present and the Impressions column shows an explanatory tooltip

  @todo
  # Source: ET-24698, GAP-1
  Scenario Outline: The Associated Tactics date range selector recomputes metrics and enforces the 60-day cap
    When User navigates to Deal Groups > Associated Tactics for a delivering deal
    # Framework Gap: Requires step definition for the Custom date-range 60-day boundary in LifeSteps.java
    And User selects date range "<DATE_RANGE>"
    Then "<EXPECTED_RESULT>"
    Examples:
      | DATE_RANGE                      | EXPECTED_RESULT                                                                          |
      | Yesterday                       | Metrics filter to Yesterday and recompute for that window                               |
      | Last 7 Days                     | Metrics filter to Last 7 Days and recompute for that window                             |
      | Last 30 Days                    | Metrics filter to Last 30 Days and recompute for that window                            |
      | Custom range of exactly 60 days | The 60-day range is accepted and metrics recompute for that window                      |
      | Custom range of 75 days         | The range is rejected or clamped to 60 days per the defined rule with a clear indication |

  @todo
  # Source: ET-24698, GAP-2
  Scenario: Associated Tactics supports filter/sort by ID and CSV export scoped per the defined rule
    When User navigates to Deal Groups > Associated Tactics for a delivering deal
    And User enters a Tactic ID, then a Campaign ID, then a Line Item ID
    Then Each filter narrows the Associated Tactics rows to matches
    When User clicks the Impressions sort control
    Then Rows sort by Impressions ascending then descending on toggle
    When User applies a filter and sort, then clicks Export to CSV
    # Framework Gap: Requires step definition for CSV export scope (filtered/sorted view vs full set) in LifeSteps.java
    Then A CSV downloads containing the Associated Tactics rows and the metric columns per the defined export-scope rule

  @todo
  # Source: ET-24698, GAP-3
  # Regression anchor: HT-6125, HT-6049 - unable to access Deal Groups in Tactic UI / deal visibility regressions
  Scenario Outline: Associated Tactics metrics are gated by the Deal-Group-metrics permission and the view remains accessible
    When User navigates to Deal Groups > Associated Tactics as an internal user
    Then The view loads and is accessible
    Given "<PERMISSION_STATE>"
    Then "<EXPECTED_RESULT>"
    Examples:
      | PERMISSION_STATE                               | EXPECTED_RESULT                                                               |
      | User granted the Deal-Group-metrics permission | Columns, date range, filter/sort, and export are all available and functional |
      | User without the Deal-Group-metrics permission | The new columns, date range, filter/sort, and export are not available        |

  @todo
  # Source: ET-24698, AMB-1
  Scenario: The Show Tactics From Other Accounts checkbox is absent, consistent with the descope
    When User navigates to Deal Groups > Associated Tactics for a delivering deal
    Then No "Show Tactics From Other Accounts" checkbox is present

  @todo
  # Source: ET-24697
  Scenario: A PG Workarounds user attaches a PG deal to a non-PG Tactic with no incompatibility warning
    Given User holds the PG Workarounds/PG Deal Management permission
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    And User attaches a PG deal to a non-PG Tactic
    Then No inline Deal Incompatibility warning or tooltip is displayed for this combination
    When User saves the tactic
    Then The PG deal attaches and the tactic saves successfully with no blocking warning

  @todo
  # Source: ET-24697, GAP-1, GAP-2, AMB-1
  Scenario: Other deal incompatibility warnings and permission-gated access remain unaffected
    Given User holds the PG Workarounds/PG Deal Management permission
    When User creates a non-PG incompatible deal/tactic combination
    Then The relevant incompatibility warning and tooltip are still shown for non-PG combinations
    When User attaches a PG deal to a PG-compatible Tactic
    Then No incompatibility warning appears and attachment works normally
    # Framework Gap: Requires step definition for the permission-gated upstream block in LifeSteps.java
    Given User lacks the PG Workarounds/PG Deal Management permission
    Then The PG-deal-on-non-PG-Tactic path is prevented upstream for that user

  @todo
  # Source: ET-24697
  # Regression anchor: HT-6125, HT-6091 - deal-group access and deal spend/visibility regressions
  Scenario: Normal PG deal attachment and deal access are unaffected by the warning removal
    Given User holds the PG Workarounds/PG Deal Management permission
    When User attaches a PG deal to a PG Tactic using the standard flow
    Then PG deals attach and remain accessible with no regression to deal-group/deal access

  @todo
  # Source: ET-24696
  Scenario: A hidden deal is excluded from Deal Group Add Deals but a visible deal remains selectable
    When User opens Deal Group > Add Deals
    Then A deal set to hidden does not appear in the Add Deals selectable list
    And A visible, enabled deal appears and can be added to the deal group

  @todo
  # Source: ET-24696, AMB-1
  # Regression anchor: HT-6071 - hidden deal not spending
  Scenario: A hidden deal already assigned to a group keeps serving and stays visible in the group's management view
    Given A deal group contains a now-hidden deal targeted by a tactic
    Then The tactic continues to serve/spend on the hidden deal
    When User opens the deal group's own management/edit view
    Then The hidden deal is visible there for context, while remaining absent from the Add Deals picker

  @todo
  # Source: ET-24696, GAP-1
  Scenario: Copying a deal group with hidden deals warns and excludes them from the duplicate
    When User copies a deal group containing at least one hidden deal
    Then A warning is displayed stating the hidden deal(s) will not be carried over to the duplicate
    # Framework Gap: Requires step definition for the copy-flow proceed/confirm outcome in LifeSteps.java
    When User completes the copy
    Then The duplicate contains only the visible deals and hidden deals are omitted

  @todo
  # Source: ET-24696, GAP-3
  Scenario Outline: Deal-group name uniqueness is validated on create and copy
    When User attempts to save a deal group with name "<GROUP_NAME>"
    Then "<EXPECTED_RESULT>"
    Examples:
      | GROUP_NAME               | EXPECTED_RESULT                                                             |
      | Existing_Deal_Group_2026 | A validation message indicates the name already exists and save is blocked |
      | New_Unique_Deal_Group    | No duplicate-name message appears and the group saves                      |
