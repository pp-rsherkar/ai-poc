Feature: LIFE Regression - Check below features available on Campaign Dashboard
  1. Add comments to Campaign, Line Items and Tactics
  2. Navigation to Campaign, Line Item and Tactic from dashboard
  3. Modify Dashboard column basis filter
  4. Verify Active, Today, Yesterday, Favorite and Hide Finished filters

  @regression
  Scenario Outline: Verify Campaign Dashboard's features
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
    When User toggles the Enabled button for Line Items and Tactics
    Then Verify that Line Items and Tactics reflect the correct enabled or disabled state
    When User clicks Campaign "<Campaign ID>", Line Item and Tactic
    Then Verify user should navigate to Campaign, Line Item and Tactic
    When User clicks Menu option and selects column names
      | ADVERTISER    |
      | STATUS        |
      | ENABLED       |
      | ACTIVE FLIGHT |
      | TYPE          |
    Then Verify dashboard is customized and only selected columns are displayed
    When User clicks HideAll and ShowAll options from Menu
    Then Dashboard columns should be hidden and shown accordingly
    When Navigate to any Dashboard column, select the filter and apply
      | Status  | Incomplete, Pending Approval |
      | Enabled | Enabled                      |
      | Type    | Audio, Native Display        |
    Then Verify the data should filter as per the selected filter values
    And Filter icon should display in the column header to which filter is applied and a red bullet "<RED BULLET>" on the filter icon present next to global search
    When User clicks Active Flights, Today and Yesterday filter option type
    Then Verify only Active Flights should render on the Dashboard
    #When User clicks Custom filter option type and selects date
    #Then Not clear on verification point
    When User clicks the Settings icon and selects the following group by options
    Then Verify the Dashboard data is grouped by the selected options
    When User clicks Favorite star icon on few campaigns and checks Favorite Only checkbox
    Then Verify the dashboard results should show only campaigns which are marked as favorite
    When User clicks Hide Finished checkbox
    Then Verify the dashboard data should not reflect campaigns with Finished status
    When User hover on the image icon for creative in red color
    Then Tool tip whether creative is assigned to the campaign or not should be reflected

    Examples:
      | Campaign ID          | COLOUR                     | RED BULLET                             |
      | Auto_20250821_213314 | 24-note-table-provided.svg | 20-filter-applied.1e22619f2d75d737.svg |