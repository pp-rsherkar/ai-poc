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

  # Source: ET-24733
  @todo
  Scenario: PG Tactic auto-derives Base/Max Bid Price and Media Optimization from targeted PG deal terms, and PG Line Item flight pacing always shows ASAP
    Given User adds multiple PG deals priced "$2", "$5", "$6" as targeting on a PG Tactic
    When an Admin user views the Tactic Settings tab
    Then Base Bid Price and Max Bid Price both display, disabled, as "$6", the maximum price across all targeted PG deals
    And Media Optimization displays, disabled, as "Flat"
    And a tooltip reading "Bid price is fixed (flat) at the corresponding PG deal price. Base bid price shown for reference only." renders next to the Base Bid Price value, not next to the field label
    Given an External user views the same PG Tactic
    Then the Base Bid Price, Max Bid Price, and Media Optimization fields are not rendered at all
    When an Admin user creates or saves a PG Line Item
    Then Flight Pacing always persists and displays, disabled, as "ASAP"
    Given an External user views the same PG Line Item
    Then the Flight Pacing field is not rendered at all
    Given a single PG deal is targeted instead of multiple
    Then Base Bid Price and Max Bid Price both display as that single deal's price
    Given a PG deal's price is updated after the Tactic is already saved
    Then User confirms with product whether Base/Max Bid Price are expected to recompute, since this is not settled in the source requirement
    Given a Tactic is converted from PG to non-PG and back to PG again
    Then the Bid Price and Media Optimization fields re-lock and re-populate correctly rather than retaining stale disabled values
    Given a targeted PG deal's price exceeds the account-level Max Bid Price cap
    Then the account cap is honored and the Tactic saves without error, with no warning UI shown in this release

  # Source: ET-24238
  @todo
  Scenario: Media Spend column on Deal > Associated Tactics view is visible only to users with the permission-gated soft launch enabled
    Given a user without the Media Spend gating permission views the Deal > Associated Tactics view
    Then the Media Spend column is fully absent from the view, not merely disabled
    Given a user with the Media Spend gating permission enabled views the same Deal > Associated Tactics view
    Then the Media Spend column is visible and its values match the underlying DPD-sourced data
    And toggling the gating permission on and off for a test account correctly shows and hides the column
