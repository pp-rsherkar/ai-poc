Feature: LIFE Campaign Trend Chart - Verify Weekly x-axis label displays calendar start-of-week dates
  1. Weekly granularity x-axis labels show calendar start-of-week dates (MM/DD/YY) instead of ISO week numbers
  2. Weekly label behavior across partial-week and long/yearly date ranges, with underlying chart data unchanged
  3. Daily, Monthly, and Hourly granularities remain unaffected, including under rapid granularity switching

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User navigates to the Campaign trend report chart

  # Source: ET-24260
  @todo
  Scenario Outline: Verify Weekly granularity displays calendar start-of-week x-axis labels across date range variations
    When The user views the Campaign trend chart with "Weekly" granularity applied to the "<DATE_RANGE_TYPE>" date range from "<START_DATE>" to "<END_DATE>"
    Then The x-axis labels display calendar dates in MM/DD/YY format instead of ISO week numbers
    And Each label reflects the Sunday that begins its week
    And The week-start label behavior for that range matches "<EXPECTED_BEHAVIOR>"
    And The underlying chart data points and shape remain unchanged from before the label format update
    Examples:
      | DATE_RANGE_TYPE      | START_DATE | END_DATE   | EXPECTED_BEHAVIOR                                                                            |
      | Multi-week range     | 03/01/2026 | 03/29/2026 | the first label displays as 03/01/26 for the week that begins on that date                   |
      | Mid-week start range | 03/04/2026 | 03/29/2026 | the first label reflects the true week-start Sunday date rather than the mid-week start date |
      | Mid-week end range   | 03/01/2026 | 03/25/2026 | the final label reflects the correct week-start date for the trailing partial week           |
      | Yearly range         | 11/17/2025 | 06/29/2026 | all weekly labels remain legible with no overlapping text                                    |

  # Source: ET-24260
  @todo
  Scenario: Verify non-Weekly granularities remain unaffected and rapid granularity switching does not break the x-axis labels
    When The user switches the Campaign trend chart to "Daily" granularity
    Then The x-axis shows individual day labels, unchanged from prior behavior
    When The user switches the Campaign trend chart to "Monthly" granularity
    Then The x-axis shows month labels, unchanged from prior behavior
    When The user switches the Campaign trend chart to "Hourly" granularity, if available in this view
    Then The x-axis shows hourly labels, unchanged from prior behavior
    When The user rapidly toggles the granularity from "Daily" to "Weekly" to "Monthly" to "Weekly"
    Then No rendering errors occur and correct labels appear at each step
