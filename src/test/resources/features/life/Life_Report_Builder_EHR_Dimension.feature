Feature: Life Report Builder - Health System EHR Dimension
  1. Exposes Health System EHR as a selectable dimension in the Life Report Builder dimension picker for Flora EHR deal accounts.
  2. Groups and filters impression delivery by EHR platform sourced from the flora_ehr flat file ingestion.
  3. Preserves existing saved reports and returns well-formed responses when EHR delivery is absent.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24951, PROD-15494
  @todo
  Scenario: Verify Health System EHR dimension selection, grouping and combination in Life Report Builder
    Given User opens Life Report Builder for an account with active Flora EHR deals
    # Framework Gap: Requires step definition to locate the Health System EHR dimension in the RB dimension picker in LifeSteps.java
    Then The Health System EHR dimension is visible in the dimension picker under the correct category grouping
    And The Health System EHR dimension is available without any special permission grant
    # Framework Gap: Requires step definition to add a dimension as a report row grouping in LifeSteps.java
    When User adds the Health System EHR dimension as a report row grouping
    Then The report generates with one row per EHR platform value and an impression count for each platform
    And Each EHR platform value is distinct per row for delivery across "Epic" and "Cerner"
    # Framework Gap: Requires step definition to validate report values against the flora_ehr ingestion source in LifeSteps.java
    When User runs the report filtered to the known EHR deal "PP_HealthSystemEHR_CBR001"
    Then The EHR platform label matches the platform configured for that deal in the flat file ingestion source
    When User adds the Date dimension alongside the Health System EHR dimension
    Then The report generates with hierarchical grouping of Date over EHR Platform with no error
    When User applies the filter "Health System EHR = Epic"
    Then The report returns only Epic rows and excludes all other EHR platforms
    When User opens a saved report template that does not include the Health System EHR dimension
    Then The saved report runs successfully with no unknown-dimension error and the EHR dimension is absent from the output

  # Source: ET-24951, PROD-15494, GAP-2, AMB-3
  @todo
  Scenario Outline: Verify Health System EHR dimension data integrity, fallback and empty-state handling
    Given User opens Life Report Builder for a "<ACCOUNT_TYPE>" account
    When User runs a report with the Health System EHR dimension for "<CONDITION>"
    Then The report result is "<EXPECTED_RESULT>" with no server error
    Examples:
      | ACCOUNT_TYPE  | CONDITION                                    | EXPECTED_RESULT                                            |
      | Non-Flora EHR | any date range                               | Empty or null EHR values displayed, not an error          |
      | Flora EHR     | comparison against backend EHR source data   | EHR platform values match the backend source exactly      |
      | Flora EHR     | impressions from publisher 562529 (Veradigm) | Veradigm row shows a valid label or defined fallback value |
      | Flora EHR     | a date range with zero EHR deal delivery     | Empty rows returned with no 500 or 400 server error       |

  # Regression anchor: HT-5466 - Flora CBR PG deals showed 'Floor' instead of 'Fixed'; the EHR dimension deployment must not regress deal pricing display
  # Source: ET-24951, HT-5466
  @todo
  Scenario: Verify Flora EHR deal list pricing display is correct after the EHR dimension deployment
    Given User opens the Life DSP deal view showing Flora EHR CBR deals
    When User inspects the deal "PP_HealthSystemEHR_CBR004" configured with Fixed pricing
    Then The deal list displays "Fixed" pricing and not "Floor"
