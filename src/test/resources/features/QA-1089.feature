Feature: AO Insights Q1 Improvements on DTC Clinical Insights Dashboard

@todo Scenario: Verify AO Insights section is displayed for AO-enabled tactics
  And "Life" application is logged in successfully with Account "automation@pulsepoint"
  And User navigates to Campaign Dashboard
  And User selects AO-enabled tactic from Select AO Tactic dropdown
  Then AO Insights section is displayed with Waste Reallocation, Spend Distribution by Decile Groups, Media Spend on High Deciles, and Audience Quality metrics
  And AO-specific components are visible only for AO-enabled tactics
  And Tactic selector shows tactics with AO enabled displaying Tactic Name and Tactic ID in brackets

@todo Scenario: Validate Waste Reallocation metric calculation and display
  Given AO-enabled tactic with decile spend data is selected
  When Waste Reallocation metric is calculated as per defined formula
  Then Low Deciles Estimated Control Spend and Low Deciles Estimated Optimized Spend subtitles are displayed correctly
  And Waste Reallocation value is displayed accurately in AO Insights section

@todo Scenario: Validate Spend Distribution by Decile Groups metrics and subtitles
  Given AO-enabled tactic with control and optimized spend data by decile
  When % allocation across decile groups (Low: 1-4, Medium: 5-7, High: 8-10) is calculated for control and optimized scenarios
  Then Spend Distribution by Decile Groups visualization is displayed with correct % allocation and dollar amounts
  And Subtitle shows total spend on Deciles 5 to 10 with % of total spend

@todo Scenario: Validate Media Spend on High Deciles metric and subtitle
  Given AO-enabled tactic with spend data across deciles
  When total spend across deciles 6 to 10 is calculated
  Then Media Spend on High Deciles metric is displayed with % of Total Media Spend subtitle

@todo Scenario: Verify Audience Quality metric and subtitle display
  Given AO-enabled tactic is selected
  Then Audience Quality metric is displayed with Audience Index subtitle in AO Insights section

@todo Scenario: Verify new visualizations with toggles and tactic selector functionality
  Given AO-enabled tactic is selected
  Then Visualization frame with toggles for Spend Bar chart, % Allocation of impressions, Proportion of Total Spend, Win Rate, and CTR is displayed
  And Each bar chart displays correct x-axis (Deciles 1 to 10) and y-axis scales as per specification
  And CPM labels are displayed on each bar based on combined control and optimized spend
  And Number of bids per decile is shown in tooltips for Win Rate chart
  And Tactic selector above visualizations allows focusing AO insights to a single tactic

@todo Scenario: Verify tooltips for Metrics Breakdown table, Tactic Selector, Waste Reallocation, and Media Spend on High Deciles
  Given AO Insights dashboard is displayed
  When user hovers over Metrics Breakdown table, Tactic Selector, Waste Reallocation, and Media Spend on High Deciles components
  Then tooltips display text as per prototype specifications

@todo Scenario: Verify AO Insights section is hidden when no AO-enabled tactics exist
  Given no AO-enabled tactics are available for the account
  Then Tactic selector is hidden
  And AO-specific components are not visible

@todo Scenario: Verify manual enablement of AO Insights dashboard for specific users and accounts
  Given internal PulsePoint user enables AO Insights dashboard for a user and account
  When user logs in to Life application
  Then AO Insights dashboard version is displayed for all enabled campaigns in the account for that user

@todo Scenario: Verify download functionality includes AO Insights visualizations and metrics
  Given AO Insights dashboard is displayed with all new visualizations and metrics
  When user clicks Download Report
  Then downloaded report includes AO Insights section with all new visualizations and metrics accurately represented
