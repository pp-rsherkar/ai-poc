Feature: LIFE Regression - Check various features available on Campaign Dashboard

  @regression
  Scenario Outline: Verify Campaign Dashboard's features
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    When User enters "<Campaign ID>" and click Search button
    Then Verify Campaigns, line items, tactics names matching the "<Campaign ID>" should display on Dashboard table
    When User add comments to Campaign, Line Items and Tactics
      | Campaign Name  | This is Campaign Name comment box  |
      | Line Item Name | This is Line Item Name comment box |
      | Tactic Name    | This is Tactic Name comment box    |
    Then Verify comments are saved successfully, icon should display in bluish-green color "<COLOUR>" and comments should available on individual panel
    When User toggles Enabled button for Line Items and Tactic from dashboard
    Then Verify Line Items and Tactics are enabled, disabled accordingly
    When User clicks Campaign "<Campaign ID>", Line Item and Tactic one by one
    Then verify user should navigate to respective panel
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
      | STATUS  | Incomplete, Pending Approval |
      | ENABLED | Enabled                      |
      | TYPE    | Audio, Native Display        |
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
      | Auto_20250625_123023 | 24-note-table-provided.svg | 20-filter-applied.1e22619f2d75d737.svg |