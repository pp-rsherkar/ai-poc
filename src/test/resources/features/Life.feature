Feature: LIFE Regression - Create a campaign, Add tactics, generate a report

  @sanity
  Scenario Outline: Create a campaign
    Given Life application is logged in as "<USER>"
    And I click create campaign
    When I fill "<ADVERTISER>" "<TYPE>" "<CLIENT>"campaign details
    And I click on save
    Then Verify campaign is created successfully
    Examples:
      | USER       | ADVERTISER | TYPE       | CLIENT | ExpectedOutput | Type     |
      | Admin      |            | Regular    |        | XYZ            | Positive |
      | SuperAdmin |            | Sequential |        | ABC            | Negative |

  @regression
  Scenario Outline: API Sample Test
    Given I call "<apiName>" with parameters "<param1>" & "<param2>"
    Then Verify response have "<statusCode>" & "<expected1>" & "<expected2>"
    Examples:
      | apiName | param1 | param2 | statusCode | expected1 | expected2 |
      | GET     | 1      | 2      | 404        | 1         | 2         |
      | POST    | 1      | 2      | 404        | 1         | 2         |
