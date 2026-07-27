Feature: HCP365 Report Builder - In and Not In Filter Operators
  1. Adds in and not in multi-value filter operators to supported dimensions in the HCP365 Report Builder.
  2. Validates value lists and keeps single-value and combined filters working correctly.
  3. Preserves saved reports, private layouts and Moments and Claims visualisation after deployment.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks on HCP 365 module from main menu

  # Source: ET-24701, PROD-14567
  @todo
  Scenario: Verify in and not in operators filter and combine correctly for supported dimensions
    Given User creates a new filter in the HCP365 Report Builder on a supported dimension
    # Framework Gap: Requires step definitions for the in and not in filter operators in HcpSteps.java
    Then The filter operator dropdown shows both the in and the not in options
    When User adds the filter Advertiser ID in [123456, 789012, 345678] and runs the report
    Then The report returns only rows for Advertisers 123456, 789012 and 345678
    When User adds the filter Advertiser ID not in [123456, 789012] and runs the report
    Then The report excludes Advertisers 123456 and 789012 and shows all others
    When User combines Advertiser ID in [A, B, C] with Token not in [X, Y] in the same report
    Then The report returns rows where the Advertiser ID matches and the Token is not excluded using AND logic
    When User saves the report with the in filter, reloads it and runs it
    Then The saved report loads with the in filter intact and returns the correct results

  # Source: ET-24701, PROD-14567, GAP-1, GAP-2, GAP-3
  @todo
  Scenario Outline: Verify value-list validation and boundary handling for the in and not in operators
    Given User creates a filter in the HCP365 Report Builder
    When User configures "<INPUT>"
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | INPUT                                                  | EXPECTED_RESULT                                                       |
      | an in operator with an empty value list                | A validation error states the value list cannot be empty and the report does not run |
      | Advertiser ID in [abc] with a non-numeric value        | A validation error states the value type is invalid and the report does not run |
      | a dimension not in the supported list                  | The operator dropdown does not include in or not in for that dimension |
      | Advertiser ID in a list of 50 distinct valid IDs       | The report runs and returns results within 30 seconds with no timeout |
      | Advertiser ID in [123456, 123456, 789012] with a duplicate | Duplicates are deduplicated or a warning is shown with no hard error |
      | Advertiser ID in [123456] versus Advertiser ID equals 123456 | Both filters return identical result sets                            |

  # Regression anchor: HT-6148 - HCP365 scheduled reports defaulting to Disabled
  # Source: ET-24701, HT-6148
  @todo
  Scenario: Verify a new scheduled report with an in filter is Enabled by default
    Given User creates a new scheduled report in HCP365 with an in filter
    Then The scheduled report status is set to Enabled by default when created

  # Regression anchor: HT-5720 private layouts missing; HT-3962 Moments and Claims Sev 1; single-value filter regression
  # Source: ET-24701, HT-5720, HT-3962
  @todo
  Scenario: Verify single-value filters, private layouts and Moments and Claims are unaffected after deployment
    Given An existing saved report with single-value equality filters
    When User opens and runs the report after deployment
    Then The report runs correctly and the single-value filters behave identically to before
    When User checks HCP365 Analytics for previously saved private layouts
    Then The private layouts load correctly and are not missing
    When User checks the Moments and Claims visualisation in the studio dashboards with HCP365 enabled
    Then The Moments and Claims sections are visible and populated
