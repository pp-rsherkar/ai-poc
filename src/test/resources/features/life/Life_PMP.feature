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

  # Source: ET-24697
  @todo
  Scenario Outline: Adding a deal to a tactic shows no Deal Incompatibility warning
    # Framework Gap: Requires step definitions for granting the "PG Workaround" permission to the user in LifeSteps.java
    Given The "<PERMISSION>" permission is applied for the user
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    # Framework Gap: Requires step definitions for configuring the tactic type as PG or non-PG in LifeSteps.java
    And User configures the tactic type as "<TACTIC_TYPE>"
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "<EXCHANGE_TYPE>", "<DEAL_ID>", "<DEAL_NAME>", "<MEDIA_TYPE>", "<ADVERTISER>", "<DEAL_PRICE_TYPE>", "<PRICE>", "<CURATOR>"
    # Framework Gap: Requires step definitions for selecting the deal type as "PG" or "PMP" during deal creation in LifeSteps.java
    And User selects the deal type as "<DEAL_TYPE>"
    When User searches the deal and assign it from the deal list
    Then Selected Deals should appear in Applied Deals panel
    # Framework Gap: Requires step definitions for verifying absence of the "Deal Incompatibility" warning in LifeSteps.java
    And No "Deal Incompatibility" warning is displayed for the applied deal
    Examples:
      | PERMISSION    | DEAL_TYPE | TACTIC_TYPE | EXCHANGE_TYPE | DEAL_ID | DEAL_NAME  | MEDIA_TYPE                 | ADVERTISER     | DEAL_PRICE_TYPE | PRICE | CURATOR                          |
      | PG Workaround | PG        | Display     | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | 01- Advertiser | Fixed           | 230   | PulsePoint (Direct Integrations) |
      | PG Workaround | PMP       | Display     | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | 01- Advertiser | Fixed           | 230   | PulsePoint (Direct Integrations) |
      | PG Workaround | PG        | PG          | JW Player     | Deal_   | Deal_Name_ | Display (All), Video (All) | 01- Advertiser | Fixed           | 230   | PulsePoint (Direct Integrations) |

  # Source: ET-24697
  @todo
  Scenario: No Deal Incompatibility tooltip appears on hover for a PG deal on a non-PG tactic
    # Framework Gap: Requires step definitions for granting the "PG Workaround" permission to the user in LifeSteps.java
    Given The "PG Workaround" permission is applied for the user
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "JW Player", "Deal_", "Deal_Name_", "Display (All), Video (All)", "01- Advertiser", "Fixed", "230", "PulsePoint (Direct Integrations)"
    # Framework Gap: Requires step definitions for selecting the deal type as "PG" during deal creation in LifeSteps.java
    And User selects the deal type as "PG"
    When User searches the deal and assign it from the deal list
    Then Selected Deals should appear in Applied Deals panel
    # Framework Gap: Requires step definitions for hovering over the applied deal on the tactic in LifeSteps.java
    When User hovers over the applied PG deal on the non-PG tactic
    # Framework Gap: Requires step definitions for verifying absence of the "Deal Incompatibility" tooltip in LifeSteps.java
    Then No "Deal Incompatibility" tooltip is displayed on hover

  # Source: ET-24697
  @todo
  Scenario: User without the PG Workaround permission adds a PG deal to a non-PG tactic
    # Framework Gap: Requires step definitions for ensuring the "PG Workaround" permission is not applied for the user in LifeSteps.java
    Given The "PG Workaround" permission is not applied for the user
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "JW Player", "Deal_", "Deal_Name_", "Display (All), Video (All)", "01- Advertiser", "Fixed", "230", "PulsePoint (Direct Integrations)"
    # Framework Gap: Requires step definitions for selecting the deal type as "PG" during deal creation in LifeSteps.java
    And User selects the deal type as "PG"
    When User searches the deal and assign it from the deal list
    # Framework Gap: Requires step definitions for capturing and documenting the PG-on-non-PG deal behavior without the permission in LifeSteps.java
    Then The system behavior for the PG deal on the non-PG tactic is documented

  # Source: ET-24697, HT-5051
  @todo
  Scenario Outline: PG deal pricing is displayed correctly with pricing type "<DEAL_PRICE_TYPE>" after warning removal
    # Framework Gap: Requires step definitions for granting the "PG Workaround" permission to the user in LifeSteps.java
    Given The "PG Workaround" permission is applied for the user
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "JW Player", "Deal_", "Deal_Name_", "Display (All), Video (All)", "01- Advertiser", "<DEAL_PRICE_TYPE>", "<PRICE>", "PulsePoint (Direct Integrations)"
    # Framework Gap: Requires step definitions for selecting the deal type as "PG" during deal creation in LifeSteps.java
    And User selects the deal type as "PG"
    When User searches the deal and assign it from the deal list
    Then Selected Deals should appear in Applied Deals panel
    # Framework Gap: Requires step definitions for verifying the displayed PG deal price and pricing type in LifeSteps.java
    And The PG deal price "<PRICE>" is displayed with pricing type "<DEAL_PRICE_TYPE>"
    Examples:
      | DEAL_PRICE_TYPE | PRICE |
      | Fixed           | 230   |
      | Floor           | 230   |

  # Source: ET-24697, HT-3601
  @todo
  Scenario: PG deal on a non-PG tactic applies only the Deal Fee and no additional Platform Fee
    # Framework Gap: Requires step definitions for granting the "PG Workaround" permission to the user in LifeSteps.java
    Given The "PG Workaround" permission is applied for the user
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "JW Player", "Deal_", "Deal_Name_", "Display (All), Video (All)", "01- Advertiser", "Fixed", "230", "PulsePoint (Direct Integrations)"
    # Framework Gap: Requires step definitions for selecting the deal type as "PG" during deal creation in LifeSteps.java
    And User selects the deal type as "PG"
    When User searches the deal and assign it from the deal list
    Then Selected Deals should appear in Applied Deals panel
    # Framework Gap: Requires step definitions for verifying the applied fee composition in LifeSteps.java
    And Only the "Deal Fee" is applied and no additional "Platform Fee" is charged for the PG deal

  # Source: ET-24697
  @todo
  Scenario: Saved and reloaded tactic with a PG deal shows no residual Deal Incompatibility warning
    # Framework Gap: Requires step definitions for granting the "PG Workaround" permission to the user in LifeSteps.java
    Given The "PG Workaround" permission is applied for the user
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "JW Player", "Deal_", "Deal_Name_", "Display (All), Video (All)", "01- Advertiser", "Fixed", "230", "PulsePoint (Direct Integrations)"
    # Framework Gap: Requires step definitions for selecting the deal type as "PG" during deal creation in LifeSteps.java
    And User selects the deal type as "PG"
    When User searches the deal and assign it from the deal list
    When User clicks on OK button
    When User clicks Save button from Tactic Setting tab
    Then Deals should get assigned to the Tactic
    # Framework Gap: Requires step definitions for reloading the saved tactic settings page in LifeSteps.java
    When User reloads the tactic settings page
    # Framework Gap: Requires step definitions for verifying absence of a residual "Deal Incompatibility" warning in LifeSteps.java
    Then No residual "Deal Incompatibility" warning is displayed for the PG deal

  # Source: ET-24697
  @todo
  Scenario: Non-PG incompatibility warnings still appear after the PG deal warning removal
    # Framework Gap: Requires step definitions for granting the "PG Workaround" permission to the user in LifeSteps.java
    Given The "PG Workaround" permission is applied for the user
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "JW Player", "Deal_", "Deal_Name_", "Display (All), Video (All)", "01- Advertiser", "Fixed", "230", "PulsePoint (Direct Integrations)"
    # Framework Gap: Requires step definitions for configuring a deal that triggers a non-PG incompatibility in LifeSteps.java
    And User configures a deal that triggers a non-PG incompatibility condition
    When User searches the deal and assign it from the deal list
    # Framework Gap: Requires step definitions for verifying the non-PG incompatibility warning is still displayed in LifeSteps.java
    Then The non-PG incompatibility warning is still displayed for the applied deal

  # Source: ET-24697
  @todo
  Scenario: Tactic with a PG deal continues to bid and deliver after the warning removal
    # Framework Gap: Requires step definitions for granting the "PG Workaround" permission to the user in LifeSteps.java
    Given The "PG Workaround" permission is applied for the user
    And User assigns the existing creative named "Auto_Creative", enables the tactic and saves the changes
    When User clicks Tactic Setting tab
    Then User should navigate to respective Tactic Setting tab
    When User add new targeting rule for Rule Type "Deals"
    Then user should navigate to PMP Deals Panel
    When User clicks "Private" Deals Tab
    And User clicks on Add New Deal button
    Then New Deal panel should open and user should be able to add new deal with details "JW Player", "Deal_", "Deal_Name_", "Display (All), Video (All)", "01- Advertiser", "Fixed", "230", "PulsePoint (Direct Integrations)"
    # Framework Gap: Requires step definitions for selecting the deal type as "PG" during deal creation in LifeSteps.java
    And User selects the deal type as "PG"
    When User searches the deal and assign it from the deal list
    When User clicks on OK button
    When User clicks Save button from Tactic Setting tab
    Then Deals should get assigned to the Tactic
    # Framework Gap: Requires step definitions for verifying the tactic bids and delivers on the assigned PG deal in LifeSteps.java
    Then The tactic bids and delivers on the assigned PG deal
