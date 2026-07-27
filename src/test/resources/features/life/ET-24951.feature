Feature: Life Report Builder - Health System EHR Dimension

  1. Adds "Health System EHR" as a new selectable dimension in the Life Report Builder dimension picker, available to all Flora EHR deal clients.
  2. Lets clients group and filter impression delivery by EHR platform (Epic, Cerner, Veradigm) and combine the dimension with existing dimensions such as Date.
  3. Keeps the dimension additive-only so existing saved reports load unchanged, and returns empty rows rather than an error when no Flora EHR delivery exists.
  4. Each scenario walks a single continuous pass through the Report Builder, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the Health System EHR dimension picker, grouping, combination, and filtering workflow
    Given User opens the Life Report Builder for an account with active Flora EHR deals
    # Framework Gap: Requires step definition to open Report Builder and read the dimension picker in LifeSteps.java
    Then the "Health System EHR" dimension is visible in the dimension picker under the correct category grouping
    When User adds the "Health System EHR" dimension as a report row grouping and runs the report
    Then the report generates with one row per EHR platform value and an impression count for each platform
    When User adds "Date" alongside "Health System EHR" as row dimensions and runs the report
    Then the report generates with hierarchical grouping of Date over EHR Platform and no error
    When User applies the filter "Health System EHR = Epic" and runs the report
    Then only Epic rows are returned and other EHR platforms are excluded
    # AMB-2: tests that the dimension is filterable, not only groupable

  @todo
  Scenario: Verify Health System EHR dimension access for a standard Flora client and empty data for non-Flora accounts
    Given User opens the Life Report Builder as a standard Flora EHR deal client without elevated permissions
    Then the "Health System EHR" dimension is visible and usable without requiring a special permission grant
    Given User opens the Life Report Builder for an account with no Flora EHR deals
    When User selects the "Health System EHR" dimension and runs the report
    Then the report generates with empty or null values for the Health System EHR column and no error or 403 is thrown
    # AMB-3: the dimension is unconditionally available; non-Flora accounts see null data, not an error

  @todo
  Scenario: Verify Health System EHR platform values are accurate, complete, and backward compatible
    Given User opens the Life Report Builder for a Flora EHR account with impressions across multiple EHR platforms
    When User groups the report by "Health System EHR"
    Then each EHR platform appears as a separate row with distinct impression counts
    And the EHR platform values in the report match the SSP backend source data exactly
    # Regression anchor: HT-5466 - Flora domain has a history of the backend being correct while the FE renders a stale or incorrect value
    When User filters the report to the known EHR deal "PP_HealthSystemEHR_CBR001"
    Then the EHR platform label matches the platform configured for that deal in the flora_ehr flat file ingestion source
    Given the account has impressions from publisher 562529 (Veradigm) with a missing reporting_value
    Then the Veradigm row shows a valid label or a defined fallback value and does not break the report
    # GAP-2: exact fallback behavior for the missing Veradigm reporting_value is undefined - document the actual result

  @todo
  Scenario: Verify Report Builder stability for saved reports and empty Health System EHR result sets
    Given User opens a saved report template that does not include the "Health System EHR" dimension
    When User runs the saved report
    Then the report runs successfully with no error about unknown dimensions and the EHR dimension is absent from the output
    # GAP-3: the dimension addition must be additive-only and not affect existing report schemas
    Given User selects the "Health System EHR" dimension for a date range with zero Flora EHR delivery
    When User runs the report
    Then the report returns empty rows and no server error (500 or 400) is thrown

  @todo
  Scenario: Verify Flora EHR deal list pricing display is not regressed by the EHR dimension deployment
    Given User opens the Life DSP deal view showing Flora CBR deals for the account
    Then deals configured with Fixed pricing display "Fixed" and not "Floor" in the deal list view
    # Regression anchor: HT-5466 - Flora CBR PG deals previously showed "Floor" when set to Fixed
