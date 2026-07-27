Feature: Life Report Builder - Health System EHR Dimension

  1. Exposes 'Health System EHR' as a selectable dimension in the Life Report Builder for Flora EHR deal accounts.
  2. Supports grouping and filtering impression delivery by EHR platform and combining it with other report dimensions.
  3. Makes the dimension available to all Flora EHR deal clients without a special permission grant.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24951 (TC_01, TC_02, TC_05, TC_06, TC_07, TC_09)
  @todo
  Scenario: Verify the Health System EHR dimension end-to-end grouping workflow in Life Report Builder
    # Framework Gap: Requires page object + step definitions for the Life Report Builder dimension picker in LifeReportBuilderSteps.java
    Given User opens Life Report Builder for an account with active Flora EHR deals
    Then The "Health System EHR" dimension is displayed in the dimension picker under its category grouping
    When User adds the "Health System EHR" dimension as a report row grouping and runs the report
    Then The report generates without error with one row per EHR platform value
    And Each EHR platform such as Epic and Cerner appears as a distinct row with its own impression count
    And The EHR platform values match the SSP backend flora_ehr source exactly
    When User adds "Date" as an additional row dimension above "Health System EHR" and runs the report
    Then The report generates with hierarchical grouping of Date over EHR platform without error

  # Source: ET-24951 (TC_03, TC_04, TC_13)
  @todo
  Scenario Outline: Verify Health System EHR dimension availability and empty-data handling across account types
    # Framework Gap: Requires step definitions to assert dimension availability by account type in LifeReportBuilderSteps.java
    Given User opens Life Report Builder for a "<ACCOUNT_TYPE>" account
    When User selects the "Health System EHR" dimension and runs the report for "<DATE_RANGE>"
    Then The dimension is usable and the report returns "<RESULT>" with no server error
    Examples:
      | ACCOUNT_TYPE                | DATE_RANGE               | RESULT               |
      | Flora EHR deal client       | last 30 days             | EHR platform rows    |
      | Standard non-elevated Flora | last 30 days             | EHR platform rows    |
      | Non-Flora EHR account       | last 30 days             | empty or null values |
      | Flora EHR deal client       | range with zero delivery | empty rows           |

  # Source: ET-24951 (TC_08, TC_10, TC_11, TC_12)
  @todo
  Scenario: Verify Health System EHR filtering, Veradigm fallback, backward compatibility, and deal pricing regression
    Given User opens Life Report Builder for an account with Flora EHR deals
    # Framework Gap: Requires step definitions for dimension-level filtering in LifeReportBuilderSteps.java
    When User applies the filter "Health System EHR = Epic" and runs the report
    Then The report returns only Epic rows and excludes all other EHR platforms
    When User runs a report including impressions from publisher "562529" (Veradigm)
    Then The Veradigm row displays a valid fallback label and does not break the report
    When User opens an existing saved report that does not include the "Health System EHR" dimension
    Then The saved report runs successfully with no unknown-dimension error
    # Regression anchor: HT-5466 - Flora CBR PG deals rendered 'Floor' instead of 'Fixed'
    When User opens the Life DSP deal list for a Flora EHR CBR deal configured as "Fixed"
    Then The deal pricing displays as "Fixed" and not "Floor"
