Feature: HCP365 Report Builder In and Not In Filtering

  The HCP365 Report Builder supports the "in" and "not in" filter operators for supported dimensions.
  Users supply a list of values for a dimension and the builder submits a single query that returns the matching rows.
  Saved and scheduled reports preserve these list based filters so they reload and rerun with the same results.

  Background:
    Given Life application is logged in as "admin11"
    And User clicks on HCP 365 module from main menu
    # Framework Gap: Requires step definitions for navigating to the HCP365 Report Builder in HcpSteps.java
    And User navigates to the HCP365 Report Builder

  # Source: ET-24701
  @todo
  Scenario Outline: Filter operator "<operator>" is available for supported dimensions in the HCP365 Report Builder
    # Framework Gap: Requires step definitions for adding a filter on a dimension in HcpSteps.java
    When User adds a filter on the supported dimension "<dimension>"
    # Framework Gap: Requires step definitions for verifying available filter operators in HcpSteps.java
    Then The filter operator "<operator>" is available for the dimension "<dimension>"

    Examples:
      | dimension     | operator |
      | Advertiser ID | in       |
      | Advertiser ID | not in   |
      | Token         | in       |
      | Token         | not in   |

  # Source: ET-24701
  @todo
  Scenario: Unsupported dimensions do not offer the "in" and "not in" operators
    # Framework Gap: Requires step definitions for adding a filter on an unsupported dimension in HcpSteps.java
    When User adds a filter on the unsupported dimension "Impression Date"
    # Framework Gap: Requires step definitions for verifying an operator is not offered in HcpSteps.java
    Then The filter operator "in" is not offered for the dimension "Impression Date"
    And The filter operator "not in" is not offered for the dimension "Impression Date"

  # Source: ET-24701
  @todo
  Scenario Outline: Report Builder applies the "<operator>" filter and returns the correct Advertiser ID rows
    When User adds a filter on the supported dimension "Advertiser ID"
    # Framework Gap: Requires step definitions for selecting an operator and entering a value list in HcpSteps.java
    And User selects the operator "<operator>" and enters the value list <value_list>
    # Framework Gap: Requires step definitions for running the report in HcpSteps.java
    And User runs the report
    # Framework Gap: Requires step definitions for verifying returned Advertiser ID rows in HcpSteps.java
    Then The report returns only rows where Advertiser ID "<operator>" <value_list>

    Examples:
      | operator | value_list               |
      | in       | [123456, 789012, 345678] |
      | not in   | [123456, 789012]         |

  # Source: ET-24701
  @todo
  Scenario: The "in" operator with a single value returns the same rows as the "equals" operator
    When User adds a filter on the supported dimension "Advertiser ID"
    And User selects the operator "in" and enters the value list [123456]
    And User runs the report
    # Framework Gap: Requires step definitions for capturing report results for comparison in HcpSteps.java
    And User captures the report results as "in-single-value"
    # Framework Gap: Requires step definitions for selecting the equals operator with a single value in HcpSteps.java
    And User selects the operator "equals" and enters the value "123456"
    And User runs the report
    And User captures the report results as "equals-single-value"
    # Framework Gap: Requires step definitions for comparing two captured result sets in HcpSteps.java
    Then The results "in-single-value" and "equals-single-value" are identical

  # Source: ET-24701
  @todo
  Scenario: Combining an "in" filter and a "not in" filter applies AND logic across dimensions
    When User adds a filter on the supported dimension "Advertiser ID"
    And User selects the operator "in" and enters the value list [123456, 789012, 345678]
    # Framework Gap: Requires step definitions for adding a second dimension filter in HcpSteps.java
    And User adds a filter on the supported dimension "Token"
    And User selects the operator "not in" and enters the value list ["tok_alpha", "tok_beta"]
    And User runs the report
    # Framework Gap: Requires step definitions for verifying combined AND filter logic in HcpSteps.java
    Then The report returns only rows where Advertiser ID in [123456, 789012, 345678] and Token not in ["tok_alpha", "tok_beta"]

  # Source: ET-24701
  @todo
  Scenario Outline: Invalid value lists for the "in" operator show a validation error and the report does not run
    When User adds a filter on the supported dimension "Advertiser ID"
    And User selects the operator "in" and enters the value list <value_list>
    And User runs the report
    # Framework Gap: Requires step definitions for verifying a filter validation error in HcpSteps.java
    Then The validation error "<error_message>" is shown
    # Framework Gap: Requires step definitions for verifying the report did not run in HcpSteps.java
    And The report does not run

    Examples:
      | value_list | error_message                           |
      | []         | Enter at least one value for the filter |
      | ["abc"]    | Advertiser ID must be a numeric value   |

  # Source: ET-24701
  @todo
  Scenario: Duplicate values in an "in" list are de-duplicated without a hard error
    When User adds a filter on the supported dimension "Advertiser ID"
    And User selects the operator "in" and enters the value list [123456, 123456, 789012]
    And User runs the report
    # Framework Gap: Requires step definitions for verifying duplicate value handling in HcpSteps.java
    Then The report runs successfully without a hard error
    And The submitted query contains the de-duplicated value list [123456, 789012]

  # Source: ET-24701
  @todo
  Scenario: A report saved with an "in" filter reloads and re-runs correctly
    When User adds a filter on the supported dimension "Advertiser ID"
    And User selects the operator "in" and enters the value list [123456, 789012, 345678]
    # Framework Gap: Requires step definitions for saving a report in HcpSteps.java
    And User saves the report as "Advertiser In-List Report"
    # Framework Gap: Requires step definitions for reloading a saved report in HcpSteps.java
    And User reloads the saved report "Advertiser In-List Report"
    # Framework Gap: Requires step definitions for verifying a persisted filter configuration in HcpSteps.java
    Then The filter shows the operator "in" with the value list [123456, 789012, 345678]
    And User runs the report
    And The report returns only rows where Advertiser ID "in" [123456, 789012, 345678]

  # Source: ET-24701
  @todo
  Scenario: An "in" filter with more than 50 values completes within the performance threshold
    When User adds a filter on the supported dimension "Advertiser ID"
    # Framework Gap: Requires step definitions for entering a generated value list of a given size in HcpSteps.java
    And User selects the operator "in" and enters a value list of 55 Advertiser IDs
    And User runs the report
    # Framework Gap: Requires step definitions for asserting report completion time in HcpSteps.java
    Then The report completes within 30 seconds without a timeout

  # Source: ET-24701
  @todo
  Scenario: Existing single-value filters continue to work in the Report Builder
    When User adds a filter on the supported dimension "Advertiser ID"
    And User selects the operator "equals" and enters the value "123456"
    And User runs the report
    Then The report returns only rows where Advertiser ID equals "123456"

  # Source: ET-24701, HT-5720
  @todo
  Scenario: Private layouts remain accessible in HCP365 Analytics
    # Framework Gap: Requires step definitions for opening HCP365 Analytics in HcpSteps.java
    When User opens HCP365 Analytics
    # Framework Gap: Requires step definitions for verifying private layouts are listed in HcpSteps.java
    Then The private layouts are listed and can be opened

  # Source: ET-24701, HT-3962
  @todo
  Scenario: The HCP365 Moments and Claims visualization renders correctly
    # Framework Gap: Requires step definitions for opening the Moments and Claims visualization in HcpSteps.java
    When User opens the HCP365 Moments and Claims visualization
    # Framework Gap: Requires step definitions for verifying the Moments and Claims visualization renders in HcpSteps.java
    Then The Moments and Claims visualization is displayed with data

  # Source: ET-24701, HT-6148
  @todo
  Scenario: Scheduled reports created with in or not in filters default to Enabled
    When User adds a filter on the supported dimension "Advertiser ID"
    And User selects the operator "in" and enters the value list [123456, 789012, 345678]
    # Framework Gap: Requires step definitions for scheduling a report from the Report Builder in HcpSteps.java
    And User schedules the report as "Scheduled In-List Report"
    # Framework Gap: Requires step definitions for verifying a scheduled report default status in HcpSteps.java
    Then The scheduled report status defaults to "Enabled"
    And The scheduled report status is not "Disabled"
