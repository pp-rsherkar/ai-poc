Feature: Life PMP Regression - Verify Private and Life MarketPlace Deals Creation and Assignment
  1. Verify Private Deals Tab
  2. Verify Life Marketplace Deals Tab
  3. Addition of Private Deals and assigned to a tactic when Only Target Applied Deals toggle is ON
  4. Addition of Private Deals and assigned to a tactic when Only Target Applied Deals toggle is OFF

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
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
    When User enters below details in respective search field, verify that the deal list appears based on the selected filters
      | SearchByName     | Deal           |
      | SearchByExchange | Pubmatic       |

  @e2e @regression
  Scenario Outline: Add New Private Deals and assigned to a tactic when Only Target Applied Deals toggle is ON
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "<EXCHANGE_TYPE>", "<DEAL_ID>", "<DEAL_NAME>", "<MEDIA_TYPE>", "<ADVERTISER>", "<DEALPRICE_TYPE>", "<PRICE>", "<CURATOR>"
    When User searches the deal and assign it from the deal list
    Then Selected Deals should appear in Applied Deals panel
    #And Verify Target Applied Deals Toggle button is available with default value as "ON"
    When User clicks on OK button
    Then Deal details should appear on Tactic Settings tab under Targeting section, Curated Market and Deals section depending on toggle button "ON"
    And Verify Delete icon is disabled and error message "Go to the Curated Markets & Deals section to remove the market."
    And Verify Pricing Strategy is editable for Deals present in Curated Market and Deals section
      | Flat          | 35 |
      | % above floor | 60 |
    And Verify user can add new "Private" deals by clicking Add Deal button present in Curated Market and Deals section using details "<EXCHANGE_TYPE>", "<DEAL_ID>", "<DEAL_NAME>", "<MEDIA_TYPE>", "<ADVERTISER>", "<DEALPRICE_TYPE>", "<PRICE>", "<CURATOR>" with toggle "ON"
    And Verify Base Bid Price "<BASE_BIDPRICE>" and Max Bid Price "<MAX_BIDPRICE>" fields are editable when deals are targeted
    When User clicks Save button from Tactic Setting tab
    Then Deals should get assigned to the Tactic
    Examples:
      | EXCHANGE_TYPE | DEAL_ID | DEAL_NAME  | MEDIA_TYPE                 | DEALPRICE_TYPE | PRICE | BASE_BIDPRICE | MAX_BIDPRICE | ADVERTISER     | CURATOR                          |
      | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | Floor          | 230   | 34            | 60           | 01- Advertiser | PulsePoint (Direct Integrations) |

  @regression
  Scenario Outline: Add New Private Deals and assigned to a tactic when Only Target Applied Deals toggle is OFF
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "<EXCHANGE_TYPE>", "<DEAL_ID>", "<DEAL_NAME>", "<MEDIA_TYPE>", "<ADVERTISER>", "<DEALPRICE_TYPE>", "<PRICE>", "<CURATOR>"
    When User searches the deal and assign it from the deal list
    Then Selected Deals should appear in Applied Deals panel
    #And Verify Target Applied Deals Toggle button is available with default value as "OFF"
    When User clicks on OK button
    Then Deal details should appear on Tactic Settings tab under Targeting section, Curated Market and Deals section depending on toggle button "OFF"
    And Verify Pricing Strategy is editable for Deals present in Curated Market and Deals section
      | Flat          | 35 |
      | % above floor | 60 |
    And Verify user can add new "Private" deals by clicking Add Deal button present in Curated Market and Deals section using details "<EXCHANGE_TYPE>", "<DEAL_ID>", "<DEAL_NAME>", "<MEDIA_TYPE>", "<ADVERTISER>", "<DEALPRICE_TYPE>", "<PRICE>", "<CURATOR>" with toggle "OFF"
    And Verify Base Bid Price "<BASE_BIDPRICE>" and Max Bid Price "<MAX_BIDPRICE>" fields are editable when deals are targeted
    When User clicks Save button from Tactic Setting tab
    Then Deals should get assigned to the Tactic
    Examples:
      | EXCHANGE_TYPE | DEAL_ID | DEAL_NAME  | MEDIA_TYPE                 | DEALPRICE_TYPE | PRICE | BASE_BIDPRICE | MAX_BIDPRICE | ADVERTISER     | CURATOR                          |
      | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | Floor          | 230   | 34            | 60           | 01- Advertiser | PulsePoint (Direct Integrations) |