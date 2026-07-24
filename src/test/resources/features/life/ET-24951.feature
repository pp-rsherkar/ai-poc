Feature: Life Report Builder - Health System EHR Dimension

  1. Exposes 'Health System EHR' as a new selectable dimension in the Life Report Builder dimension picker, sliced from the flora_ehr flat-file ingestion source.
  2. The dimension is available to all Flora EHR deal clients without a dedicated permission grant, and returns empty/null (never an error) for non-Flora accounts.
  3. The dimension is both groupable and combinable with other dimensions and filters, and is additive-only so existing saved reports keep working.
  4. Each scenario walks a single continuous pass through Report Builder, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the Health System EHR dimension workflow in Life Report Builder for a Flora EHR deal account
    Given User opens Life Report Builder for an account with active Flora EHR deals
    Then the "Health System EHR" dimension is visible in the dimension picker under its category grouping
    And the dimension label reads exactly "Health System EHR"
    When User adds the "Health System EHR" dimension as a row grouping and runs the report
    Then the report generates with one row per EHR platform value and a distinct impression count per platform
    And no special permission is required to access or use the dimension
    When User pulls a report filtered to a known EHR deal "PP_HealthSystemEHR_CBR001"
    Then the EHR platform label matches the platform configured for that deal in the flat-file ingestion source
    When User groups the report by "Date" and "Health System EHR" together
    Then the report generates with hierarchical grouping of Date over EHR Platform without error
    When User adds the filter "Health System EHR = Epic"
    Then the report returns only Epic rows and excludes all other EHR platforms
    When User views impressions delivered by publisher 562529 (Veradigm)
    Then the Veradigm row shows a valid label or a defined fallback value, never a blank that breaks the report
    # GAP-2: exact fallback for the missing Veradigm reporting_value is undefined - document actual result and confirm with team
    # Regression anchor: HT-5466 - Flora deal values correct in backend but rendered incorrectly in the FE

  @todo
  Scenario: Verify the Health System EHR dimension degrades gracefully for non-Flora accounts and preserves existing reports
    Given User opens Life Report Builder for an account with no Flora EHR deals
    When User selects the "Health System EHR" dimension and runs the report
    Then the report generates with empty/null values for the dimension and no error or 403 is thrown
    When User runs the report for a date range with zero Flora EHR delivery
    Then the Report Builder API returns a well-formed response with empty rows and no 400/500 error
    When User opens and runs an existing saved report that does not include the "Health System EHR" dimension
    Then the saved report runs successfully with no unknown-dimension error and the EHR dimension is absent from the output
    Then a Flora EHR CBR deal set to Fixed pricing still displays "Fixed" and not "Floor" in the deal list view
    # Regression anchor: HT-5466 - Flora CBR PG deals showing 'Floor' instead of Fixed
