Feature: HCP365 Report Builder - In and Not In Filter Operators

  1. Adds 'in' and 'not in' multi-value filter operators to the HCP365 Report Builder for supported dimensions.
  2. Validates list input, returns correctly filtered results, and persists saved reports.
  3. Keeps existing single-value filters and HCP365 analytics working.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on HCP 365 module from main menu

  # Source: ET-24701 (TC_01, TC_02, TC_03, TC_04, TC_07, TC_08, TC_14)
  @todo
  Scenario: Verify in and not in operators, multi-value filtering, and saved report persistence
    # Framework Gap: Requires page object + step definitions for the HCP365 Report Builder filter operators in HcpReportBuilderSteps.java
    Given User opens the HCP365 Report Builder and creates a filter on a supported dimension
    Then The operator dropdown includes both "in" and "not in"
    When User applies the filter "Advertiser ID in [123456, 789012, 345678]" and runs the report
    Then The report returns only rows for those three advertisers and excludes all others
    When User applies the filter "Advertiser ID not in [123456, 789012]" and runs the report
    Then The report excludes those two advertisers and includes all others
    When User selects a dimension not in the supported list
    Then The operator dropdown does not include "in" or "not in"
    When User applies "Advertiser ID in [123456]" and compares it against "Advertiser ID equals 123456"
    Then Both filters return identical result sets
    When User saves a report with an "in" filter and reloads it
    Then The saved report loads with the "in" filter intact and re-runs correctly

  # Source: ET-24701 (TC_05, TC_06, TC_10, TC_16)
  @todo
  Scenario Outline: Verify in and not in input validation and combined-filter behavior
    Given User opens the HCP365 Report Builder and adds a filter using the "in" operator
    When User enters "<INPUT>"
    Then The result is "<EXPECTED>"
    Examples:
      | INPUT                                            | EXPECTED                                                 |
      | an empty value list                              | a validation error and the report does not run           |
      | a non-numeric value abc in an ID field           | an invalid-value-type error and the report does not run  |
      | Advertiser ID in [123456, 123456, 789012]        | duplicates deduplicated or a warning with no hard error  |
      | Advertiser ID in [A,B,C] and Token not in [X,Y]  | combined AND logic returning matching, non-excluded rows |

  # Source: ET-24701 (TC_09, TC_11, TC_12, TC_13, TC_15)
  @todo
  Scenario: Verify large-list performance and HCP365 regression guards
    Given User opens the HCP365 Report Builder
    When User applies an "in" filter with 50 distinct valid Advertiser IDs and runs the report
    Then The report returns results within 30 seconds with no timeout or too-large error
    When User opens an existing saved report with single-value equality filters after deployment
    Then The single-value filters behave identically to before
    # Regression anchor: HT-5720 - Private Layouts missing in HCP365 Analytics
    When User checks previously saved private layouts in HCP365 Analytics
    Then The private layouts load correctly and are not missing
    # Regression anchor: HT-3962 - HCP365 Moments and Claims Sev 1 bug
    When User checks the Moments and Claims sections in the studio dashboards
    Then The Moments and Claims sections are visible and populated
    # Regression anchor: HT-6148 (active) - scheduled reports default to Disabled
    When User creates a new scheduled report with an "in" filter
    Then The scheduled report is set to Enabled by default
