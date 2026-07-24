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
  Scenario Outline: Verify dynamic metric columns lazy-load and show skeletons until the "<Metric Column>" column is scrolled into the viewport
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    When User enters "<Campaign ID>" and click Search button
    Then Verify Campaigns, line items, tactics names matching the "<Campaign ID>" should display on Dashboard table
    # Framework Gap: Requires step definitions for verifying metric API calls are not fired on initial page load in LifeSteps.java
    Then Verify no metric API request is fired for the "<Metric Column>" column on initial page load
    # Framework Gap: Requires step definitions for asserting skeleton placeholder states in LifeSteps.java
    And Verify the "<Metric Column>" column shows skeleton loading placeholders and not blank or error content
    # Framework Gap: Requires step definitions for scrolling a column into the viewport to trigger the Intersection Observer in LifeSteps.java
    When User scrolls the "<Metric Column>" column into view
    # Framework Gap: Requires step definitions for verifying metric API calls fire on viewport entry in LifeSteps.java
    Then Verify the metric API request fires and populates the "<Metric Column>" column
    # Framework Gap: Requires step definitions for asserting skeletons are replaced within a time budget in LifeSteps.java
    And Verify the skeleton placeholders are replaced by "<Metric Column>" values within "10" seconds and are not stuck
    # Framework Gap: Requires step definitions for comparing lazy-loaded values against an eager-load reference in LifeSteps.java
    And Verify the lazy-loaded "<Metric Column>" values match the eager-load reference values
    Examples:
      | Campaign ID          | Metric Column |
      | Auto_20260528_000342 | Spend         |
      | Auto_20260528_000342 | Impressions   |
      | Auto_20260528_000342 | Clicks        |
      | Auto_20260528_000342 | CTR           |

  # Source: ET-24263
  @todo
  Scenario: Verify initial Campaign Dashboard load time is improved for campaigns with 50 or more tactics
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    When User enters "Auto_20260528_000342" and click Search button
    Then Verify Campaigns, line items, tactics names matching the "Auto_20260528_000342" should display on Dashboard table
    # Framework Gap: Requires step definitions for asserting a minimum tactic count in the Dashboard table in LifeSteps.java
    And Verify the Dashboard table renders "50" or more tactics
    # Framework Gap: Requires step definitions for measuring Dashboard list render time in LifeSteps.java
    Then Verify the Dashboard tactics list renders within "5" seconds
    # Framework Gap: Requires step definitions for asserting the list render does not block on metric API calls in LifeSteps.java
    And Verify the tactics list render does not block on metric API calls

  # Source: ET-24263
  @todo
  Scenario: Verify an immediate scroll before the Intersection Observer registers still triggers metric loading
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    When User enters "Auto_20260528_000342" and click Search button
    Then Verify Campaigns, line items, tactics names matching the "Auto_20260528_000342" should display on Dashboard table
    # Framework Gap: Requires step definitions for scrolling immediately before Observer registration completes in LifeSteps.java
    When User scrolls the "Spend" column into view immediately before the Intersection Observer is registered
    # Framework Gap: Requires step definitions for verifying the Intersection Observer fires after registration in LifeSteps.java
    Then Verify the Intersection Observer fires after registration and the metric API request is triggered
    # Framework Gap: Requires step definitions for asserting skeletons resolve to values in LifeSteps.java
    And Verify the "Spend" skeleton placeholders are replaced by values within "10" seconds

  # Source: ET-24263
  @todo
  Scenario Outline: Verify a metric API error during lazy load shows an error state and does not leave skeletons stuck for "<Error Condition>"
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    When User enters "Auto_20260528_000342" and click Search button
    Then Verify Campaigns, line items, tactics names matching the "Auto_20260528_000342" should display on Dashboard table
    # Framework Gap: Requires step definitions for simulating a metric API failure condition in LifeSteps.java
    When The metric API responds with "<Error Condition>" as the "Spend" column is scrolled into view
    # Framework Gap: Requires step definitions for asserting an error state is rendered in the metric column in LifeSteps.java
    Then Verify the "Spend" column displays an error state and the skeleton placeholders are not stuck
    Examples:
      | Error Condition       |
      | HTTP 500 server error |
      | request timeout       |
      | slow connection       |

  # Source: ET-24263
  @todo
  Scenario: Verify a custom column configuration with a metric in the first visible position loads that metric on page load
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    When User clicks Menu option and selects column names
      | SPEND |
    # Framework Gap: Requires step definitions for positioning a metric column as the first visible column in a custom config in LifeSteps.java
    And User configures the "Spend" metric column to the first visible position
    When User enters "Auto_20260528_000342" and click Search button
    Then Verify Campaigns, line items, tactics names matching the "Auto_20260528_000342" should display on Dashboard table
    # Framework Gap: Requires step definitions for verifying a first-visible metric loads on initial load without scrolling in LifeSteps.java
    Then Verify the "Spend" metric API fires on initial load and the values populate without scrolling

  # Source: ET-24263
  @todo
  Scenario: Verify metric values load once per page view and do not reload on every scroll
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    When User enters "Auto_20260528_000342" and click Search button
    Then Verify Campaigns, line items, tactics names matching the "Auto_20260528_000342" should display on Dashboard table
    # Framework Gap: Requires step definitions for scrolling a column into view in LifeSteps.java
    When User scrolls the "Spend" column into view
    # Framework Gap: Requires step definitions for verifying metric API fires on viewport entry in LifeSteps.java
    Then Verify the metric API request fires and populates the "Spend" column
    # Framework Gap: Requires step definitions for repeated scroll interactions on a column in LifeSteps.java
    When User scrolls the "Spend" column out of and back into view multiple times
    # Framework Gap: Requires step definitions for asserting the metric API is called only once per page view in LifeSteps.java
    Then Verify the "Spend" metric API request is not fired again and the values remain populated

  # Source: ET-24263
  @todo
  Scenario: Verify Active Flight mode surfaces future tactics on the Campaign Dashboard
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User removes all the filters applied on the Dashboard
    And User clicks "Active Flight" filter
    # Framework Gap: Requires step definitions for verifying future-dated tactics are surfaced in Active Flight mode in LifeSteps.java
    Then Verify tactics with future flight dates are surfaced and not hidden on the Dashboard

  # Source: ET-24263
  @todo
  Scenario Outline: Verify existing campaign filtering is unaffected and lazy metric loading still triggers after filtering by "<Filter Type>"
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User clicks Lifetime filter
    And User removes all the filters applied on the Dashboard
    When Navigate to any Dashboard column, select the filter and apply
      | <Filter Type> | <Filter Value> |
    Then Verify the filter list displays only the selected filter values
    And Verify the Campaign Dashboard data should filter as per the selected filter values
    # Framework Gap: Requires step definitions for scrolling a column into view after a filter is applied in LifeSteps.java
    When User scrolls the "Spend" column into view
    # Framework Gap: Requires step definitions for verifying lazy metric loading still triggers post-filter in LifeSteps.java
    Then Verify the metric API request fires and populates the "Spend" column
    Examples:
      | Filter Type | Filter Value                 |
      | Status      | Incomplete, Pending Approval |
      | Enabled     | Enabled                      |
      | Type        | Audio, Display               |
