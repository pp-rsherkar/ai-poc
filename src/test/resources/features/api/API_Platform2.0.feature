Feature: Fetch data using MCP Tools by retrieving metadata, creating query and executing query using dimensions and metrics
  1. STG Token
  2. MCP Initialize
  3. MCP Tools List
  4. MCP Prompts List
  5. MCP Prompt Get
  6. MCP Tool Call - Looker Get Explore Metadata
  7. MCP Tool Call - Create Query
  8. MCP Tool Call - Execute Query

  @e2e @todo
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
    And User requests the list of available MCP prompts with headers:
      | Content-Type | application/json                    |
      | Accept       | application/json, text/event-stream |
    Then Verify the MCP prompts list is fetched successfully
    And User retrieves specific MCP prompt details with headers:
      | Content-Type | application/json |
      | Accept       | application/json, text/event-stream |
    Then Verify the MCP prompt details are retrieved successfully
    And User calls the MCP tool to get Looker explore metadata with headers:
      | X-Account-Id    | 123                                 |
      | X-Advertiser-Id | 456                                 |
      | X-User-Id       | 789                                 |
      | Content-Type    | application/json                    |
      | Accept          | application/json, text/event-stream |
    Then Verify the Looker explore metadata response is successful
    And User calls the MCP tool to create a query using dimensions and metrics with headers:
      | Content-Type    | application/json |
      | Accept          | application/json |
      | X-Account-Id    | 12345            |
      | X-Advertiser-Id | 67890            |
      | X-User-Id       | your-user-uuid   |
    Then Verify the query is created successfully and returns a query ID
    And User calls the MCP tool to execute the created query with headers:
      | Content-Type    | application/json                    |
      | Accept          | application/json, text/event-stream |
      | X-Account-Id    | 12345                               |
      | X-Advertiser-Id | 67890                               |
      | X-User-Id       | your-user-uuid                      |
    Then Verify the query execution response contains the retrieved data