Feature: LIFE Regression - Check below features available on Campaign Dashboard
  1. Add comments to Campaign, Line Items and Tactics
  2. Navigation to Campaign, Line Item and Tactic from dashboard
  3. Modify Dashboard column basis filter
  4. Verify Active, Today, Yesterday, Favorite and Hide Finished filters

  @regression
  Scenario Outline: Verify comments addition on Campaign Dashboard and validate it on Campaign, Line Item and Tactic pages
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    When User enters "<Campaign ID>" and click Search button
    Then Verify Campaigns, line items, tactics names matching the "<Campaign ID>" should display on Dashboard table
    When User add and save comments to Campaign, Line Items and Tactics
      | Campaign Name  | This is Campaign Name comment box  |
      | Line Item Name | This is Line Item Name comment box |
      | Tactic Name    | This is Tactic Name comment box    |
    Then Verify comments, icon should display in bluish-green color "<COLOUR>" and comments should available on individual panel
    And User navigates to campaign, line item and tactic using "<Campaign ID>" and verifies that the comments are displayed in the respective tile comment boxes
    And User verifies the comments in the campaign, line item, and tactic dashboard's comment boxes
    Examples:
      | Campaign ID          | COLOUR                     |
      | Auto_20260528_000342 | 24-note-table-provided.svg |

  @regression
  Scenario Outline: Verify toggle functionality for Line Item and Tactic on Campaign Dashboard and validate it on Line Item and Tactic pages
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    When User enters "<Campaign ID>" and click Search button
    Then Verify Campaigns, line items, tactics names matching the "<Campaign ID>" should display on Dashboard table
    When User toggles the Enabled button for Line Items and Tactics
    Then Verify that Line Items and Tactics reflect the correct enabled or disabled state
    And User fetches the Line Items and Tactics enabled-disabled status from Campaign Dashboard using "<Campaign ID>" and verifies the same status in the respective Line Item and Tactic pages
    Examples:
      | Campaign ID          |
      | Auto_20260531_235701 |

  @regression
  Scenario Outline: Verify filtering and column customization on Campaign Dashboard
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    And User removes all the filters applied on the Dashboard
    When User clicks Menu option and selects column names
      | ADVERTISER    |
      | STATUS        |
      | ENABLED       |
      | ACTIVE FLIGHT |
      | TYPE          |
    Then Verify dashboard is customized and only selected columns are displayed
    And User clicks HideAll option from Menu and verifies Dashboard columns are hidden accordingly
    And User clicks ShowAll option from Menu and verifies Dashboard columns are shown accordingly
    When Navigate to any Dashboard column, select the filter and apply
      | Status  | Incomplete, Pending Approval |
      | Enabled | Enabled                      |
      | Type    | Audio, Display               |
    Then Verify the filter list displays only the selected filter values
    And Verify the Campaign Dashboard data should filter as per the selected filter values
    And Filter icon should display in the column header to which filter is applied and a red bullet "<RED BULLET>" on the filter icon present next to global search
    And User removes all the filters applied on the Dashboard and verifies the data is reset to default state
    Examples:
      | RED BULLET                             |
      | 20-filter-applied.1e22619f2d75d737.svg |

  @regression
  Scenario: Verify Campaign Dashboard displays correct data based on Filters selection
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User removes all the filters applied on the Dashboard
    And User clicks Lifetime filter
    And User verifies that the campaigns displayed on the Dashboard include all past and current flights
    And User clicks "Today" filter
    Then Verify only Current Month's Flights should render on the Dashboard
    And User clicks "Yesterday" filter
    Then Verify only Current Month's Flights should render on the Dashboard
    And User clicks "Active Flight" filter
    Then Verify only Current Month's Flights should render on the Dashboard
    And User clicks "Custom" filter
    And User enters the custom date range from "07/01/2025" to "07/16/2025" and applies the filter
    And Verify only Custom date range Flights from "07/01/2025" to "07/16/2025" should render on the Dashboard if available

  @regression
  Scenario: Verify Campaign Dashboard displays correct data based on Group By Options selection
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks the Settings icon and selects the following group by options and verify dashboard data is grouped accordingly
      | Group By Campaign   |
      | Group By Advertiser |
      | No Grouping         |

  @regression
  Scenario: Verify Campaign Dashboard displays correct data based Favourite and Hide Finished checkbox selection
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    And User removes all the filters applied on the Dashboard
    When User clicks Favorite Only checkbox
    Then Verify the dashboard results should show only campaigns which are marked as favorite
    And User unchecks Favorite Only checkbox
    And Verify the dashboard results should show campaigns which are marked as favorite and nonfavorite
    When User clicks Hide Finished checkbox
    Then Verify the dashboard data should not reflect campaigns with Finished status
    And User unchecks Hide Finished checkbox
    And Verify the dashboard data should reflect campaigns with Finished status


  @regression
  Scenario Outline: Verify navigation to Campaign, Line Item and Tactic pages one by one from Campaign Dashboard
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    When User enters "<Campaign ID>" and click Search button
    Then Verify Campaigns, line items, tactics names matching the "<Campaign ID>" should display on Dashboard table
    When User clicks Campaign "<Campaign ID>", Line Item and Tactic and verify navigation to respective pages
    Examples:
      | Campaign ID          |
      | Auto_20260519_183446 |

  @regression
  Scenario Outline: Verify navigation to Tactic, assign Creatives of "<STATUS>" and check the creative assignment on Campaign Dashboard
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks on Create Campaign
    When User enters the campaign details as "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and saves the campaign
    Then Verify campaign details are saved and user is navigated to the line item page
    When User enters the line item details as "<LINE_NAME>" "<LINE_BUDGET>", enables the line item and saves the changes
    Then Verify line item details are saved and user is navigated to the tactic page
    When User enters the tactic details as "<TACTIC_NAME>" and saves the tactic
    Then Verify tactic details are saved and user is navigated to the settings tab
    When User selects the "<CHANNEL>" as channel
    And User selects "<RULE_TYPE>" as rule type and configures the targeting rules, and saves the settings
    Then Verify settings details are saved and user is navigated to the creatives tab
    Then Verify the newly created campaign details in the campaign list: Campaign name, Line item name and Tactic name
    Then User hover on the image icon for creative in red color and check whether creative is assigned to the campaign
    When User navigates to Tactic and assigns creative of status "<STATUS>" to the Tactic
    And User navigates to Campaign Dashboard
    And User searches the campaign created in the above steps
    And User hover on the image icon for creative in red color and check whether creative is assigned to the campaign
    Examples:
      | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | TACTIC_NAME | CHANNEL          | RULE_TYPE          | STATUS       |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Tactic      | Display Advanced | Behavioral Segment | Approved     |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Tactic      | Display Advanced | Behavioral Segment | Pending Appr |
      | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Tactic      | Display Advanced | Behavioral Segment | Denied       |

  @todo
  # Source: ET-24263
  Scenario: Static columns render immediately while dynamic metrics defer until scrolled into view
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    When User opens the campaign landing page fresh
    Then ID, advertiser, enabled, status, and budget columns display immediately without waiting on metrics
    And No metric fetch occurs on load
    When The first dynamic-metric column becomes partially visible on scroll
    Then All dynamic metrics begin loading and cells show the preloader, then render values on completion
    When User reorders columns so a dynamic metric is visible and reloads the page
    Then Metric loading initiates immediately without requiring a scroll

  @todo
  # Source: ET-24263
  Scenario Outline: Active Flight surfaces future-flight entities with the Starts-on text and m-dash metrics
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    When User switches to Active Flight mode
    Then "<ENTITY_STATE>": "<EXPECTED_RESULT>"
    Examples:
      | ENTITY_STATE                                                             | EXPECTED_RESULT                                              |
      | Line item with a future flight start (e.g. Native Health Pages Keywords) | The entity is shown in Active Flight and is no longer hidden |
      | Future-flight row's Flight column                                        | Reads 'Starts on [month]/[date]' instead of a flight number  |
      | Future-flight row's non-calculable metric columns                        | Display an m-dash instead of a value                         |

  @todo
  # Source: ET-24263
  Scenario: No Grouping is removed while Campaigns and Advertisers grouping remain, and Lifetime is no longer a sticky default
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    When User opens the grouping selector
    Then "No Grouping" is absent from the grouping options
    And Both the Campaigns and Advertisers grouping options are present and functional
    When User sets the date mode to Lifetime, leaves, and returns to the landing page
    Then The page does not default back to Lifetime

  @todo
  # Source: ET-24263
  Scenario: Pagination replaces Show more and bulk operations apply only to the current page
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    Given A landing page with more entities than one page
    Then Pagination controls are shown and navigate pages, and the "Show more" control is no longer used
    # Framework Gap: Requires step definition for the select-all scope under pagination in LifeSteps.java
    When User selects items and runs a bulk operation while paginated
    Then The operation affects only the items displayed on the current page, not the full result set

  @todo
  # Source: ET-24263
  # Regression anchor: HT-6073 - monthly PLD report failure (data-load reliability)
  Scenario: The landing page load performance does not regress to loading all heavy metrics up front
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    When User loads the landing page and observes network/metric fetches
    Then Only static columns load initially, heavy metrics defer, and overall load performance is not regressed
