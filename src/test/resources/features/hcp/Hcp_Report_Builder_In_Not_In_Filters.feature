Feature: HCP365 Regression - Report Builder in and not in Filter Operators
  HCP365 Report Builder supports the in and not in operators for multi-value inclusion and exclusion filtering on supported dimensions.
  The operators validate input, persist in saved reports, and combine with existing single-value filters.

  Background:
    Given Life application is logged in as "admin11"
    And User clicks on HCP 365 module from main menu

  # Source: ET-24701 (R01, R02, R03, R04)
  @todo
  Scenario Outline: in and not in operators filter multi-value inclusion and exclusion
    When User creates a filter on the supported dimension "Advertiser ID" with operator "<OPERATOR>" and values "<VALUES>"
    And User runs the report
    # Framework Gap: Requires step definitions for HCP365 Report Builder filter operators in HcpSteps.java
    Then The operator "<OPERATOR>" is available in the filter operator dropdown
    And The report returns rows "<EXPECTED>"
    Examples:
      | OPERATOR | VALUES                 | EXPECTED                                        |
      | in       | 123456, 789012, 345678 | only for advertisers 123456, 789012 and 345678  |
      | not in   | 123456, 789012         | excluding advertisers 123456 and 789012          |

  # Source: ET-24701 (R03 validation, GAP-3)
  @todo
  Scenario Outline: in filter validates the value list
    When User creates a filter on "Advertiser ID" with operator "in" and values "<VALUES>"
    And User runs the report
    # Framework Gap: Requires step definitions for filter value validation in HcpSteps.java
    Then The result is "<EXPECTED>"
    Examples:
      | VALUES  | EXPECTED                                                  |
      | (empty) | a validation error that the value list cannot be empty     |
      | abc     | a validation error for the invalid non-numeric value       |
      | 123456, 123456, 789012 | duplicates deduplicated or warned with no hard error |

  # Source: ET-24701 (R05, R06, GAP-1)
  @todo
  Scenario: Unsupported dimensions hide the operators and saved in filters reload correctly
    When User selects a dimension that is not supported for in and not in
    # Framework Gap: Requires step definitions for dimension operator support in HcpSteps.java
    Then The filter operator dropdown does not include in or not in for the unsupported dimension
    When User saves a report with an in filter and reloads the saved report
    Then The saved report loads with the in filter intact and re-runs returning correct results

  # Source: ET-24701 (R07, edge equivalence and combination)
  @todo
  Scenario Outline: in filter performance, equals equivalence and combined operators
    When User runs a report for the case "<CASE>"
    # Framework Gap: Requires step definitions for filter performance and combination in HcpSteps.java
    Then The report behaves as "<EXPECTED>"
    Examples:
      | CASE                                              | EXPECTED                                              |
      | Advertiser ID in a list of 50 distinct valid IDs   | returns results within 30 seconds with no timeout      |
      | Advertiser ID in a single value versus equals      | returns an identical result set to the equals filter   |
      | Advertiser ID in a list and Token not in a list    | returns rows matching the AND of both filter conditions |

  # Source: ET-24701 (HT-6148, HT-5720, HT-3962 regression)
  @todo
  Scenario: Regression - scheduled report defaults, private layouts and Moments/Claims are intact
    When User creates a new scheduled report in HCP365 with an in filter
    # Framework Gap: Requires step definitions for scheduled report and layout regression checks in HcpSteps.java
    Then The scheduled report status is set to Enabled by default
    And Existing single-value filters still run correctly after the in and not in introduction
    And Private layouts in HCP365 Analytics load correctly and are not missing
    And Moments and Claims visualizations display in the studio dashboards
