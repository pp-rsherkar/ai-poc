Feature: HCP365 Report Builder - Support 'in' and 'not in' Filter Operators

  1. Adds 'in' and 'not in' as multi-value filter operators in the HCP365 Report Builder filter builder for the supported dimensions.
  2. Accepts a list of values, validates malformed and empty input, and returns correctly included/excluded rows within acceptable time for large lists.
  3. Keeps existing single-value filters, saved reports, private layouts, and scheduled-report defaults working after the change.
  4. Each scenario walks a single continuous pass through the HCP365 Report Builder filter builder, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "HCP" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24701, GAP-1, GAP-2, AMB-1
  @todo
  Scenario: Verify the 'in' and 'not in' multi-value filter workflow in HCP365 Report Builder
    # Framework Gap: new step definitions required in stepdefinitions/HcpSteps.java (Background steps reused verbatim)
    Given User opens HCP365 Report Builder and creates a new filter on a supported dimension
    Then the operator dropdown shows both "in" and "not in" as options
    # GAP-1: the set of supported dimensions is defined in Lisa Choo's Google Sheet and must be obtained
    When User adds the filter "Advertiser ID in [123456, 789012, 345678]" and runs the report
    Then the report returns only rows for those advertiser IDs and excludes all others
    When User adds the filter "Advertiser ID not in [123456, 789012]" and runs the report
    Then the report excludes rows for those advertisers and includes all others
    When User adds an "in" filter of 50 distinct valid IDs and runs the report
    Then the report completes within an acceptable time with no timeout or "too large" error
    # GAP-2: maximum list size is not defined
    When User enters the filter "Advertiser ID in [123456, 123456, 789012]" with a duplicate value
    Then duplicates are silently deduplicated or a non-blocking warning is shown, with no hard error
    When User builds an "in" filter with a single value and compares it with an equals filter on the same value
    Then both filters return identical result sets
    When User combines "Advertiser ID in [A, B, C]" with "Token not in [X, Y]" in the same report
    Then the report applies the filters with AND logic and returns the correctly intersected rows
    # AMB-1: multi-filter combination behaviour

  # Source: ET-24701, AMB-2, HT-5720, HT-6148, HT-3962
  @todo
  Scenario: Verify input validation and backward-compatibility for existing HCP365 reporting artifacts
    # Framework Gap: new step definitions required in stepdefinitions/HcpSteps.java (Background steps reused verbatim)
    Given User opens HCP365 Report Builder
    When User adds an "in" filter with an empty value list and runs the report
    Then a validation error states the value list cannot be empty and the report does not run
    When User adds the filter "Advertiser ID in [abc]" with a non-numeric value
    Then a validation error flags the invalid value type and the report does not run with malformed values
    When User selects a dimension not in the supported list
    Then the operator dropdown does not offer "in" or "not in" for that dimension
    When User saves a report with an "in" filter, reloads it, and re-runs it
    Then the saved report loads with the "in" filter intact and returns correct results
    When User opens an existing saved report that uses single-value equality filters
    Then the report runs correctly and the single-value filters behave identically to before
    # AMB-2: saved-report migration to the new operator format
    When User checks previously saved private layouts in HCP365 Analytics
    Then the private layouts load correctly and are not missing
    # Regression anchor: HT-5720 - Private Layouts Missing in HCP365 Analytics
    When User creates a new scheduled report in HCP365 with an "in" filter
    Then the scheduled report is Enabled by default, not Disabled
    # Regression anchor: HT-6148 (ACTIVE) - HCP365 scheduled reports defaulting to Disabled
    When User checks the HCP365 Moments and Claims visualization after deployment
    Then the Moments and Claims sections are visible and populated
    # Regression anchor: HT-3962 - HCP365 toggle / Moments & Claims Sev 1 bug
