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

  # Source: ET-24263
  @todo
  Scenario: Campaign landing page loads static columns immediately and defers dynamic metrics until scrolled into view
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    # Framework Gap: Requires step definition to assert immediate static-column render in LifeSteps.java (CampaignDashboardPage)
    Then The static columns "ID, Advertiser, Enabled, Status, Budget" render immediately without waiting on metrics
    # Framework Gap: Requires step definition to assert no metric request fires on initial load in LifeSteps.java
    And No dynamic metric request is issued while the first metric column is outside the viewport
    # Framework Gap: Requires step definition to scroll the first dynamic metric column into view in LifeSteps.java
    When User scrolls the first dynamic metric column into the viewport
    # Framework Gap: Requires step definition to assert the in-cell preloader then rendered values in LifeSteps.java
    Then The metric cells display the preloader and then render the "Spend, Impressions, Clicks, Conversion Rate" values
    # Framework Gap: Requires step definition to scroll a metric column out of and back into view in LifeSteps.java
    When User scrolls the metric column out of the viewport and back into view
    # Framework Gap: Requires step definition to assert the metric caching / re-fetch rule in LifeSteps.java
    Then The metrics render per the defined caching rule with no error and no duplicate fetch

  # Source: ET-24263
  @todo
  Scenario: Reordering a dynamic metric column into view triggers immediate metric loading on reload
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    # Framework Gap: Requires step definition to reorder columns so a dynamic metric is visible and reload in LifeSteps.java
    When User reorders the columns so a dynamic metric column is visible and reloads the page
    # Framework Gap: Requires step definition to assert immediate metric load without a scroll in LifeSteps.java
    Then The dynamic metric loading initiates immediately on load without requiring a scroll

  # Source: ET-24263
  @todo
  Scenario: Active Flight surfaces future-dated entities with Starts-on text and m-dash metrics
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User removes all the filters applied on the Dashboard
    And User clicks "Active Flight" filter
    # Framework Gap: Requires step definition to assert a future-flight entity is listed in Active Flight in LifeSteps.java
    Then The line item "Native Health Pages Keywords" with a future flight start is listed in Active Flight
    # Framework Gap: Requires step definition to assert the Flight column Starts-on text in LifeSteps.java
    And Its Flight column reads "Starts on [month]/[date]" instead of a flight number
    # Framework Gap: Requires step definition to assert the m-dash on non-calculable metric cells in LifeSteps.java
    And Its non-calculable metric columns display an m-dash

  # Source: ET-24263
  @todo
  Scenario: No Grouping option is removed while Campaign and Advertiser grouping remain available
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    # Framework Gap: Requires step definition to open the grouping selector and read available options in LifeSteps.java
    When User opens the grouping selector from the Settings icon
    Then The grouping selector offers the "Group By Campaign" and "Group By Advertiser" options
    And The "No Grouping" option is not available in the grouping selector

  # Source: ET-24263
  @todo
  Scenario: Lifetime is not persisted as the default date mode across sessions
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    # Framework Gap: Requires step definition to reload the landing page and read the active date mode in LifeSteps.java
    When User reloads the campaign landing page
    # Framework Gap: Requires step definition to assert the active date mode after reload in LifeSteps.java
    Then The page does not default back to Lifetime and the Active Flight date mode is applied

  # Source: ET-24263
  @todo
  Scenario: Pagination replaces Show more and scopes bulk operations to the current page
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    # Framework Gap: Requires step definition to assert pagination controls present and Show more absent in LifeSteps.java
    Then The pagination controls are displayed and the "Show more" control is no longer used
    # Framework Gap: Requires step definition to navigate pages via pagination in LifeSteps.java
    When User navigates to the next page using the pagination controls
    Then The next page of campaigns is displayed
    # Framework Gap: Requires step definition to Select all and run a bulk operation while paginated in LifeSteps.java
    When User uses "Select all" and runs a bulk operation while paginated
    Then The bulk operation applies only to the items on the current page
    And The "Select all" selection scope follows the defined rule of the current page

  # Source: HT-6073
  @todo
  Scenario: Campaign landing page loads without regressing to loading all heavy metrics up front
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    # Framework Gap: Requires step definition to observe the metric network fetches on load in LifeSteps.java
    Then Only the static columns load initially and the heavy metrics are deferred
    And The overall landing-page load performance is not regressed
