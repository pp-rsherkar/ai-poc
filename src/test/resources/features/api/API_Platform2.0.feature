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
      | Content-Type    | application/json                               |
      | Accept          | application/json, text/event-stream            |
      | X-Account-Id    | 561673                                         |
      | X-Advertiser-Id | 5586                                           |
      | X-User-Id       | 69431                                          |
      | Dimension       | <TIME_DIMENSION>, <PROMPT_SPECIFIC_DIMENSIONS> |
      | Filter          | <FILTER_LABEL>, <FILTER_VALUE>                 |
      | Sort            | <SORT_BY>                                      |
      | Limit           | <LIMIT>                                        |
    Then Verify the query is created successfully and returns a query ID
    And User calls the MCP tool to execute the created query with headers:
      | Content-Type    | application/json                    |
      | Accept          | application/json, text/event-stream |
      | X-Account-Id    | 561673                              |
      | X-Advertiser-Id | 5586                                |
      | X-User-Id       | 69431                               |
    Then Verify the query execution response contains the retrieved data "<PROMPT_SPECIFIC_DIMENSIONS>"
    Examples:
      | USER_PROMPT                                                                                                                | PROMPT_SPECIFIC_DIMENSIONS                                                     | TIME_DIMENSION   | FILTER_LABEL    | FILTER_VALUE            | SORT_BY              | LIMIT |
      | What is the trend of Identified NPIs and Total Active Users by Day for the last 8 weeks, including a 7-day moving average? | resolved_measures.distinct_npis, custom_measures_ga4.active_user_count_        | hcp365_core.day  | hcp365_core.day | 56 days ago for 56 days | hcp365_core.day asc  | 500   |
      | Show NPI First Visits vs NPI Returning Visits by Week for the past quarter, with return rate percentages.                  | custom_measures_ga4.npi_first_visits, custom_measures_ga4.npi_returning_visits | hcp365_core.week | hcp365_core.day | 90 days ago for 90 days | hcp365_core.week asc | 13    |