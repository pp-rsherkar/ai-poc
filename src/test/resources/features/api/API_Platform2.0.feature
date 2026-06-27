Feature: Fetch data using MCP Tools by retrieving metadata, creating query and executing query using dimensions and metrics
  1. STG Token
  2. MCP Initialize
  3. MCP Tools List
  4. MCP Prompts List
  5. MCP Prompt Get
  6. MCP Tool Call - Looker Get Explore Metadata
  7. MCP Tool Call - Create Query
  8. MCP Tool Call - Execute Query

  @regression
  Scenario Outline: Validate NPI and Active User Analytics Across Multiple Business Use Cases - "<USER_PROMPT>"
    Given I call the Token API using the API key for authentication with configuration:
      | Content-Type | application/x-www-form-urlencoded |
      | grant_type   | client_credentials                |
      | client_id    | platform-twenty-dpd               |
      | audience     | studio-api                        |
    Then Verify the Token API response status and presence of a valid access token
    When User initializes the MCP server using the access token with headers:
      | Content-Type | application/json                    |
      | Accept       | application/json, text/event-stream |
    Then Verify the MCP server initialization response is successful
    And User calls the MCP tool to create a query for the user prompt using dimensions and metrics with headers:
      | Content-Type    | application/json                    |
      | Accept          | application/json, text/event-stream |
      | X-Account-Id    | <ACCOUNT_ID>                        |
      | X-Advertiser-Id | <ADVERTISER_ID>                     |
      | X-User-Id       | <USER_ID>                           |
      | Dimension       | <PROMPT_SPECIFIC_DIMENSIONS>        |
      | FilterLabel     | <FILTER_LABEL>                      |
      | FilterValue     | <FILTER_VALUE>                      |
      | Sort            | <SORT_BY>                           |
      | Limit           | <LIMIT>                             |
    Then Verify the query is created successfully and returns a query ID
    And User calls the MCP tool to execute the created query with headers:
      | Content-Type    | application/json                    |
      | Accept          | application/json, text/event-stream |
      | X-Account-Id    | <ACCOUNT_ID>                        |
      | X-Advertiser-Id | <ADVERTISER_ID>                     |
      | X-User-Id       | <USER_ID>                           |
    Then Verify the query execution response contains the retrieved data "<PROMPT_SPECIFIC_DIMENSIONS>"
    Examples:
      | ACCOUNT_ID | ADVERTISER_ID | USER_ID | USER_PROMPT                                                                                                                          | PROMPT_SPECIFIC_DIMENSIONS                                                                                                           | FILTER_LABEL                             | FILTER_VALUE              | SORT_BY                                   | LIMIT |
      | 561973     | 5590          | 69431   | What is the trend of Identified NPIs and Total Active Users by Day for the last 8 weeks, including a 7-day moving average?           | resolved_measures.distinct_npis, custom_measures_ga4.active_user_count_, hcp365_core.day                                             | hcp365_core.day                          | 56 days ago for 56 days   | hcp365_core.day asc                       | 500   |
      | 561673     | 5586          | 69431   | Show NPI First Visits vs NPI Returning Visits by Week for the past quarter, with return rate percentages.                            | custom_measures_ga4.npi_first_visits, custom_measures_ga4.npi_returning_visits, hcp365_core.week                                     | hcp365_core.day                          | 90 days ago for 90 days   | hcp365_core.week asc                      | 13    |
      | 561973     | 5590          | 69431   | Show NPI Pageviews by Page URL grouped by Primary Specialty for the last 45 days to assess topic affinity.                           | hcp365_core.normalizedurl, npi_reference.primary_specialty,resolved_measures.npi_pageviews                                           | hcp365_core.day                          | last 45 days              | resolved_measures.npi_pageviews desc      | 500   |
      | 561973     | 5590          | 69431   | Show UTM Term performance (Identified NPIs and NPI Events) by Campaign Name for brand-level paid search in Q1.                       | hcp365_core.utm_term, resolved_dimensions.campaign_name, resolved_measures.distinct_npis, resolved_measures.npi_events               | hcp365_core.day, hcp365_core.source_type | this year Q1, PAID SEARCH | resolved_measures.distinct_npis desc      | 500   |
      | 561973     | 5590          | 69431   | Show NPI Social Impressions and NPI Social Clicks by Channel and Week for the past 8 weeks.                                          | hcp365_core.tokentype, hcp365_core.week, resolved_measures.npi_social_impressions, resolved_measures.npi_social_clicks               | hcp365_core.day                          | 56 days ago for 56 days   | hcp365_core.week desc                     | 500   |
      | 561973     | 5590          | 69431   | Which Collection ID values have Identified NPIs / Total Events ratio < 20% but high Total Events volume, grouped by Advertiser Name? | resolved_dimensions.token, resolved_dimensions.advertiser_name, resolved_measures.distinct_npis, resolved_measures.total_events      |                                          |                           | resolved_measures.total_events desc       | 500   |
      | 561973     | 5590          | 69431   | Provide HCP First Visits vs HCP Returning Visits by User Type and Day of Week.                                                       | resolved_dimensions.user_type, hcp365_core.dayofweek, custom_measures_ga4.hcp_first_visits, custom_measures_ga4.hcp_returning_visits |                                          |                           | custom_measures_ga4.hcp_first_visits desc | 500   |
