Feature: LIFE Regression - Line Item Management
  Verify all functionalities related to creating, editing, managing, and validating line items within a campaign

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on Create Campaign
    When User enters the campaign details as "01- Advertiser" "Auto" "Regular" "20000" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page

  @regression
  Scenario Outline: Validate that a line item cannot be saved without valid flight details and appropriate budget and date constraints
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
    And Verify if user enters flight budget that exceeds Campaign budget
    Then User should see error message when tries to save line item page
    And User adds the flight details - Flight Start Date, Flight End Date, "<BUDGET>"
    And User adds new flight and enter overlapping flight details - Flight Start Date, Flight End Date, "<BUDGET>"
    And User should see error message when tries to save line item page and dates fields should get highlighted with inline error message
    Examples:
      | LINE_ITEM  | BUDGET |
      | Line_Item_ | 200    |

  @regression
  Scenario Outline: Add multiple and sequential flights to a line item and verify Flights tab after deleting some entries
    When User enters line item details "<LINE_ITEM>"
    And User adds "<NUMBER_OF_FLIGHTS>" flights, fills in the details with "<BUDGET>" for each flight section, and saves the line item
    And User generates sequential flights for the line item using "<BUDGET>" and "<NUMBER_OF_MONTHS>"
    And Verify that Sequential flights should be added based on the start month
    And User fetches all the flight details added
    Then User navigates to the Flights tab and verifies the flight details
    When User deletes some flight entries
    Then User should see the remaining flights listed under the Flights section
    Examples:
      | LINE_ITEM  | BUDGET | NUMBER_OF_FLIGHTS | NUMBER_OF_MONTHS |
      | Line_Item_ | 200    | 3                 | 2                |

  @regression
  Scenario Outline: Create, modify, duplicate, delete, and toggle line items of different types using Bulk Edit Mode
    When User enters the line item details with different line types "<LINE_NAME>" "<LINE_BUDGET>" "<LINE_TYPE>", enables the line item and saves the changes
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
      | LINE_NAME | LINE_BUDGET | LINE_TYPE                                                                   | LINE_ITEM_NOTE | TEMPLATE            |
      | Line      | 500         | Display, Audio, Video, Native Display, Native Video, DOOH, Search Extension | Notes          | Template_Automation |
