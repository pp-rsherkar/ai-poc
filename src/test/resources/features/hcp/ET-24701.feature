Feature: HCP365 Report Builder - In and Not In Filtering Operators

  1. Adds "in" and "not in" filter operators to the HCP365 Report Builder for supported dimensions, letting users filter reports by multiple values.
  2. Validates value lists, applies AND logic across multiple filters, and persists the operators in saved and scheduled reports.
  3. Keeps existing single-value filters, private layouts, and dashboard visualizations working after the change.
  4. Each scenario walks a single continuous pass through the HCP365 Report Builder, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given Life application is logged in as "admin11"
    And User clicks on HCP 365 module from main menu

  @todo
  Scenario: Verify the in and not in operators appear only for supported dimensions and filter results correctly
    Given User creates a new filter in the HCP365 Report Builder on a supported dimension
    # Framework Gap: Requires step definitions for the HCP365 Report Builder filter operators in HcpSteps.java
    Then the filter operator dropdown shows both "in" and "not in" as options
    # GAP-1: the supported dimension list must be obtained from the Lisa Choo Google Sheet
    When User selects a dimension not supported by the operators
    Then the dropdown does not include "in" or "not in" for that dimension
    When User adds the filter "Advertiser ID in [123456, 789012, 345678]" and runs the report
    Then the report returns only rows for Advertisers 123456, 789012, and 345678
    When User adds the filter "Advertiser ID not in [123456, 789012]" and runs the report
    Then the report excludes Advertisers 123456 and 789012 and returns all others

  @todo
  Scenario: Verify value-list validation and duplicate handling for the in operator
    Given User creates a filter with the "in" operator in the HCP365 Report Builder
    When User leaves the value field empty and runs the report
    Then a validation error states the value list cannot be empty and the report does not run
    When User enters the non-numeric Advertiser ID "abc" in the value list and runs the report
    Then a validation error for the invalid value type is shown and the report does not run
    When User enters "Advertiser ID in [123456, 123456, 789012]" with a duplicate value
    Then the duplicates are silently deduplicated or a non-blocking warning is shown with no hard error

  @todo
  Scenario: Verify saved report persistence, single-value equivalence, and multi-filter combination
    Given User saves a report with an "in" filter and reloads the saved report
    When User runs the reloaded report
    Then the "in" filter is intact and the report returns correct results
    When User compares "Advertiser ID in [123456]" against "Advertiser ID equals 123456"
    Then both filters return identical result sets
    When User adds "Advertiser ID in [A, B, C]" and "Token not in [X, Y]" in the same report and runs it
    Then the report returns rows where Advertiser ID matches AND Token is not in the excluded set, confirming AND logic
    # AMB-1: multi-filter combination behavior

  @todo
  Scenario: Verify a large in filter with 50 or more values completes within acceptable time
    Given User creates a filter with the "in" operator in the HCP365 Report Builder
    When User adds 50 distinct valid Advertiser IDs and runs the report
    Then the report returns results within 30 seconds with no timeout or "too large" error
    # GAP-2: boundary size limit is not defined

  @todo
  Scenario: Verify existing HCP365 filters, layouts, dashboards, and scheduled reports are not regressed
    Given User opens an existing saved report with single-value equality filters after deployment
    When User runs the report
    Then the report runs correctly and single-value filters behave identically to before
    # Regression anchor: backward compatibility for existing filter configurations
    Given User opens HCP365 Analytics
    Then previously saved private layouts load correctly and the layout switcher is present
    # Regression anchor: HT-5720 - Private Layouts Missing in HCP365 Analytics
    Given User has HCP365 enabled at both account and advertiser level
    Then the Moments and Claims sections are visible and populated in the studio dashboards
    # Regression anchor: HT-3962 - HCP365 toggle / Moments and Claims Sev 1 bug
    When User creates a new scheduled report in HCP365 with an "in" filter
    Then the scheduled report status is set to Enabled by default
    # Regression anchor: HT-6148 (ACTIVE) - Scheduled reports default to Disabled
