Feature: Fetch data using MCP Tools by retrieving metadata, creating query and executing query using dimensions and metrics
  1. STG Token
  2. MCP Initialize
  3. MCP Tools List
  4. MCP Prompts List
  5. MCP Prompt Get
  6. MCP Tool Call - Looker Get Explore Metadata
  7. MCP Tool Call - Create Query
  8. MCP Tool Call - Execute Query

  @api @todo
  Scenario: Retrieve data using dimensions and metrics through MCP workflow
    Given I call the Token API using the API key for authentication with configuration:
      | Content-Type | application/x-www-form-urlencoded |
      | grant_type   | client_credentials                |
      | client_id    | platform-twenty-dpd               |
      | audience     | studio-api                        |
    Then Verify the Token API response status and presence of a valid access token
    When User initializes the MCP server using the access token with headers:
      | Content-Type  | application/json                    |
      | Accept        | application/json, text/event-stream |
    Then Verify the MCP server initialization response is successful
