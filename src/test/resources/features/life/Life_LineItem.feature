Feature: LIFE Regression - Line Item Management
  Verify all functionalities related to creating, editing, managing, and validating line items within a campaign

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  @regression
  Scenario Outline: Validate that a line item cannot be saved without valid flight details and appropriate budget and date constraints
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    And Verify Line Item page has below tabs
      | Overview       |
      | Details        |
      | Flights        |
      | Conversion     |
      | Tactics        |
      | Media Insights |
    And Verify status of line item is Incomplete when there are no tactics under the line item
    When User fills in required details "<LINE_ITEM>" except for flight information and save
    Then User should see an error message to add flight details
    And User clicks Add Flight button
    And User tries to save the line item without entering any flight details
    Then User should see error message "Invalid budget" when tries to save line item page
    And Verify if user enters flight budget that exceeds Campaign budget
    Then User should see error message when tries to save line item page
    And User adds the flight details - Flight Start Date, Flight End Date, "<BUDGET>"
    And User adds new flight and enter overlapping flight details - Flight Start Date, Flight End Date, "<BUDGET>"
    And User should see error message when tries to save line item page and dates fields should get highlighted with inline error message
    And Verify "Apply Impression Cap for This Flight" and "Apply Daily Impression Cap" checkboxes are available for each flight entry
    And User should be able to check the "Apply Impression Cap for This Flight" and "Apply Daily Impression Cap" checkboxes
    And Verify error message if user fails to add impression cap value when the checkboxes are selected and tries to save the line item page
    Examples:
      | LINE_ITEM  | BUDGET |
      | Line_Item_ |    200 |

  @regression
  Scenario Outline: Add multiple flights to a line item and verify Flights tab after deleting some entries
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters line item name "<LINE_ITEM>" on details page
    And User adds "<NUMBER_OF_FLIGHTS>" flights, fills in the details with "<BUDGET>" for each flight section, and saves the line item
    And User fetches all the flight details added
    Then User navigates to the Flights tab and verifies the flight details
    When User deletes some flight entries
    Then User should see the remaining flights listed under the Flights section
    Examples:
      | LINE_ITEM  | BUDGET | NUMBER_OF_FLIGHTS |
      | Line_Item_ |    200 |                 3 |

  @regression
  Scenario Outline: Add sequential flights to a line item and verify Flights tab after deleting some entries
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters line item name "<LINE_ITEM>" on details page
    And User generates sequential flights for the line item using "<BUDGET>" and "<NUMBER_OF_MONTHS>"
    And Verify that Sequential flights should be added based on the start month and verify start date of the month for each flight entry
    And Verify end date of the month for each flight entry
    And User fetches all the flight details added
    Then User navigates to the Flights tab and verifies the flight details
    When User deletes some flight entries
    Then User should see the remaining flights listed under the Flights section
    Examples:
      | LINE_ITEM  | BUDGET | NUMBER_OF_MONTHS |
      | Line_Item_ |    200 |                4 |

  @regression
  Scenario Outline: Perform create, modify, duplicate, delete, and toggle operations on line items using Bulk Edit Mode and verify data integrity after duplication
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User creates line items with below line types and other details, enables the line item and saves the changes
      | LINE_TYPE        | LINE_ITEM_DETAILS                                                                                                                          |
      | Display          | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Priority, PacingMode:Even, PacingPercentage:60    |
      | Audio            | LineName:LineItem, LineBudget:500, CostModel:CPM, CPMAmount:50, BudgetDistribution:Dollars, PacingMode:ASAP, PacingPercentage:60           |
      | Video            | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Percentage, PacingMode:Ahead, PacingPercentage:60 |
      | Native Display   | LineName:LineItem, LineBudget:500, CostModel:CPM, CPMAmount:50, BudgetDistribution:Priority, PacingMode:Even, PacingPercentage:60          |
      | Native Video     | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Priority, PacingMode:Ahead, PacingPercentage:60   |
      | DOOH             | LineName:LineItem, LineBudget:500, CostModel:CPM, CPMAmount:50, BudgetDistribution:Dollars, PacingMode:ASAP, PacingPercentage:60           |
      | Search Extension | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Percentage, PacingMode:Ahead, PacingPercentage:60 |
    And Verify Bulk Edit Mode successfully "disables" multiple selected line items
    And Verify that each selected line item is "Disabled"
    And Verify Bulk Edit Mode successfully "enables" multiple selected line items
    And Verify that each selected line item is "Enabled"
    Then User adds Comments or Notes "<LINE_ITEM_NOTE>" to each line item
    And Verify the notes added to each line item
    And Verify user is able to create a copy of the line items using "Duplicate" option
    And Verify "Delete" is available for each item, and deleted items are removed from the Left menu
    And Verify "Generate Report" option opens the Run report screen for user and run the report for "<TEMPLATE>"
    And Verify that the reports generated on the Line Item page are available on the Generate Report page
    Examples:
      | LINE_ITEM_NOTE | TEMPLATE            |
      | Notes          | Template_Automation |

  @regression
  Scenario: Verify data integrity after creating line items with different line types and details
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User creates line items with below line types and other details and verifies the details after saving the line item
      | LINE_TYPE        | LINE_ITEM_DETAILS                                                                                                                          |
      | Display          | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Priority, PacingMode:Ahead, PacingPercentage:60   |
      | Audio            | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Dollars, PacingMode:ASAP, PacingPercentage:60     |
      | Video            | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Percentage, PacingMode:Ahead, PacingPercentage:60 |
      | Native Display   | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Priority, PacingMode:Even, PacingPercentage:60    |
      | Native Video     | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Priority, PacingMode:Ahead, PacingPercentage:60   |
      | DOOH             | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Dollars, PacingMode:ASAP, PacingPercentage:60     |
      | Search Extension | LineName:LineItem, LineBudget:500, CostModel:Fixed CPM, CPMAmount:50, BudgetDistribution:Percentage, PacingMode:Ahead, PacingPercentage:60 |

  @regression
  Scenario Outline: Verify addition of Line items to the existing campaign and validate the data persistence after updating the line item details
    When User clicks on the existing campaign to open the campaign details page
    And User clicks on Add Line Item button
    And User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify that the new line item is added to the existing campaign and displayed in the left menu under the campaign
    And User updates line item details such as "<UPDATED_LINE_BUDGET>" and flight dates and saves the line item
    Then Verify that the line item details are updated successfully and reflected on the Line Item page
    Examples:
      | LINE_NAME | LINE_BUDGET | UPDATED_LINE_BUDGET |
      | Line      |         500 |              505.00 |

  @regression
  Scenario Outline: Create new custom field in Line Item details page and verify its availability in all the new and existing line items under all Campaigns of the selected account
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters line item name "<LINE_ITEM>" on details page
    Then User creates new custom field "<CUSTOM_NAME>" and verifies the same in the line item details page
    And User enters details in "<LINE_BUDGET>" enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    And User navigates to Campaign Dashboard
    And User clicks on the existing campaign to open the campaign details page
    Then Verify the custom field created in line item details page is available for all line items under the campaign
    Then User deletes the custom field and verify its removed from new "line item"
    Examples:
      | LINE_ITEM  | CUSTOM_NAME  | LINE_BUDGET |
      | Line_Item_ | Custom_Field |          50 |

  # Source: ET-25081
  @todo
  Scenario: Create a line item using the new Open AI line item type
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
  # Framework Gap: Requires an "Open AI" entry in the line item type list in LifeSteps.java / pages/life (new line item type, not yet implemented)
    When User opens the line item type list and selects "Open AI"
    And User enters the line item name "OAI Test LI 01", a flight of 2026-10-01 to 2026-10-31 and a flight budget of 3000
    And User saves the line item
    Then The line item is saved with type Open AI and reloads showing type Open AI after the page is refreshed
  # Framework Gap: Requires a non-Open-AI line item regression check confirming Even is unaffected outside this line item type
    Then Verify a non-Open-AI line item is unaffected by the new Even budget distribution option

  # Source: ET-25040, TC_ET-25040_33, TC_ET-25040_34, TC_ET-25040_36, TC_ET-25040_37, QA-2102
  @todo
  Scenario: Line Item Conversion tab shows the no-pixel message until a pixel created from the line item is associated with it
    And User searches and selects the campaign "C1"
    # Framework Gap: Requires a step definition to open a named line item from the campaign details page in LifeSteps.java / pages/life/Campaigns.java
    When User opens the "LI-N" Line Item from the campaign details page
    # Framework Gap: Requires page-object hooks for the Line Item Conversion tab in pages/life/LineItemConversions.java
    And User opens the Conversion tab of the line item
    # Framework Gap: Requires page-object hooks for the Line Item Conversion tab empty state in pages/life/LineItemConversions.java
    Then Verify the message "To enable tracking for Line Item's Tactics, you must associate the pixel with that Line Item." is displayed on the Conversion tab
    And Verify the empty-state illustration is displayed on the Conversion tab
    # Framework Gap: Requires a step definition for the Add New Pixel action on the Line Item Conversion tab in LifeSteps.java
    When User clicks Add New Pixel on the Conversion tab
    And User enters the pixel details as "QA LI-Create" "100Advertiser" "Person" "Submit Application"
    # Framework Gap: Requires a step definition and page-object hook for the "Associate this pixel with line item" footer checkbox in pages/life/ConversionPixel.java
    And User ticks the "Associate this pixel with line item" checkbox
    And User saves the pixel
    # Framework Gap: Requires page-object hooks for the pixel list on the Line Item Conversion tab in pages/life/LineItemConversions.java
    Then Verify the created pixel is ticked on the Conversion tab
    And Verify the message "To enable tracking for Line Item's Tactics, you must associate the pixel with that Line Item." is not displayed on the Conversion tab
    And Verify the empty-state illustration is not displayed on the Conversion tab
    When User clicks Add New Pixel on the Conversion tab
    And User enters the pixel details as "QA LI-Create-2" "100Advertiser" "Person" "Submit Application"
    And User saves the pixel
    Then Verify the created pixel is not ticked on the Conversion tab
    And User navigates to Pixels page
    # Framework Gap: Requires a step definition to open a created Conversion Pixel detail page in LifeSteps.java / pages/life/Pixels.java
    When User opens the created Conversion Pixel from the pixel list
    # Framework Gap: Requires page-object hooks for the Associated Line Items section in pages/life/ConversionPixel.java
    Then Verify the Associated Line Items section title counter shows "0"
    And Verify the warning "LI must be associated for pixel to work" is displayed in the Associated Line Items section
    And User navigates to Pixels page
    # Framework Gap: Requires a step definition to open a Conversion Pixel by name from the pixel list in LifeSteps.java
    When User opens the Conversion Pixel "QA LI-Create" from the pixel list
    # Framework Gap: Requires page-object hooks for the Associated Line Items list in pages/life/ConversionPixel.java
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-N |
    And Verify the Associated Line Items section title counter shows "1"

  # Source: ET-25040, TC_ET-25040_01, TC_ET-25040_02, TC_ET-25040_39, TC_ET-25040_40, HT-5380
  @todo
  Scenario: Associations made on the pixel page and on the Line Item Conversion tab stay in sync
    And User navigates to Pixels page
    # Framework Gap: Requires a step definition to open a Conversion Pixel by name from the pixel list in LifeSteps.java
    When User opens the Conversion Pixel "4221" from the pixel list
    # Framework Gap: Requires page-object hooks for the Associated Line Items list in pages/life/ConversionPixel.java
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A |
      | LI-B |
    And Verify the Associated Line Items section title counter shows "2"
    # Framework Gap: Requires step definitions for the Add Line Items panel in LifeSteps.java
    When User clicks Add in the Associated Line Items section
    And User expands campaign "C2" in the Add Line Items panel
    And User ticks line item "LI-C" in the Add Line Items panel
    And User confirms the Add Line Items panel selection
    # Framework Gap: Requires a generic page reload step in LifeSteps.java
    And User reloads the pixel page
    Then Verify the Associated Line Items section title counter shows "3"
    And User navigates to Campaign Dashboard
    And User searches and selects the campaign "C2"
    # Framework Gap: Requires a step definition to open a named line item from the campaign details page in LifeSteps.java / pages/life/Campaigns.java
    When User opens the "LI-C" Line Item from the campaign details page
    # Framework Gap: Requires page-object hooks for the Line Item Conversion tab in pages/life/LineItemConversions.java
    And User opens the Conversion tab of the line item
    # Framework Gap: Requires page-object hooks for the pixel list on the Line Item Conversion tab in pages/life/LineItemConversions.java
    Then Verify pixel "4221" is ticked on the Conversion tab
    And Verify the message "To enable tracking for Line Item's Tactics, you must associate the pixel with that Line Item." is not displayed on the Conversion tab
    # Framework Gap: Requires a step definition to untick a pixel on the Line Item Conversion tab in LifeSteps.java
    When User unticks pixel "4221" on the Conversion tab
    # Framework Gap: Requires a step definition to save the Line Item Conversion tab in LifeSteps.java
    And User saves the Conversion tab of the line item
    And User navigates to Pixels page
    And User opens the Conversion Pixel "4221" from the pixel list
    Then Verify the Associated Line Items section lists exactly the below line items
      | LI-A |
      | LI-B |
    And Verify the Associated Line Items section title counter shows "2"

  # Source: ET-25040, TC_ET-25040_41, TC_ET-25040_42, PROD-15912
  @todo
  Scenario: A duplicated line item keeps its pixel associations and removing the copy leaves the original associated
    And User searches and selects the campaign "C1"
    # Framework Gap: Requires a step definition to open a named line item from the campaign details page in LifeSteps.java / pages/life/Campaigns.java
    When User opens the "LI-A" Line Item from the campaign details page
    # Framework Gap: Requires a step definition to duplicate a single named line item in LifeSteps.java / pages/life/LineItemDetails.java
    And User duplicates the line item as "LI-A-copy"
    # Framework Gap: Requires page-object hooks for the Line Item Conversion tab in pages/life/LineItemConversions.java
    And User opens the Conversion tab of the line item
    # Framework Gap: Requires page-object hooks for the pixel list on the Line Item Conversion tab in pages/life/LineItemConversions.java
    Then Verify pixel "4221" is ticked on the Conversion tab
    And User navigates to Pixels page
    # Framework Gap: Requires a step definition to open a Conversion Pixel by name from the pixel list in LifeSteps.java
    When User opens the Conversion Pixel "4221" from the pixel list
    # Framework Gap: Requires page-object hooks for the Associated Line Items list in pages/life/ConversionPixel.java
    Then Verify the Associated Line Items section lists the line item "LI-A"
    And Verify the Associated Line Items section lists the line item "LI-A-copy" with its new line item ID
    And Verify the Associated Line Items section title counter increased by "1"
    # Framework Gap: Requires step definitions for removing a line item from the Associated Line Items section in LifeSteps.java
    When User removes line item "LI-A-copy" from the Associated Line Items section
    And User saves the pixel
    # Framework Gap: Requires a generic page reload step in LifeSteps.java
    And User reloads the pixel page
    Then Verify the Associated Line Items section does not list the line item "LI-A-copy"
    And Verify the Associated Line Items section lists the line item "LI-A"
    And User navigates to Campaign Dashboard
    And User searches and selects the campaign "C1"
    When User opens the "LI-A" Line Item from the campaign details page
    And User opens the Conversion tab of the line item
    Then Verify pixel "4221" is ticked on the Conversion tab
