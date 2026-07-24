Feature: LIFE Regression - Flora Health System EHR Reporting Dimension
  Life Report Builder exposes the Health System EHR dimension so clients can slice Flora EHR deal impressions by EHR platform.
  The dimension is available to all Flora EHR deal accounts and combines with existing Report Builder dimensions and filters.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24951 (GAP-1, AMB-1)
  @todo
  Scenario: Health System EHR dimension is discoverable and groupable in Life Report Builder
    When User navigates to run report from mega menu of the life application
    And Verify Run Report panel should be opened
    And User clicks on "Pick Dimensions/Metrics" link
    # Framework Gap: Requires step definitions for EHR dimension picker assertions in LifeSteps.java
    Then The "Health System EHR" dimension is displayed in the dimension picker under the correct category grouping
    And The "Health System EHR" dimension can be added as a report row grouping
    When User generates the report grouped by "Health System EHR"
    Then The report generates with one row per EHR platform value and an impression count per platform
    And The EHR platform values match the flora_ehr flat file ingestion source exactly with no stale labels

  # Source: ET-24951 (AMB-3)
  @todo
  Scenario Outline: Health System EHR dimension access and empty-data behavior by account type
    When User navigates to run report from mega menu of the life application
    And Verify Run Report panel should be opened
    And User selects the "Health System EHR" dimension for a "<ACCOUNT_TYPE>" account
    And User generates the report grouped by "Health System EHR"
    # Framework Gap: Requires step definitions for account-type scoped dimension results in LifeSteps.java
    Then The dimension is "<VISIBILITY>" without requiring a special permission grant
    And The report shows "<RESULT>" and no error is thrown
    Examples:
      | ACCOUNT_TYPE       | VISIBILITY | RESULT                    |
      | Flora EHR deal     | usable     | distinct EHR platform rows |
      | Non-Flora EHR deal | usable     | empty or null EHR values   |

  # Source: ET-24951 (AMB-2)
  @todo
  Scenario: Health System EHR dimension combines with other dimensions and filters
    When User navigates to run report from mega menu of the life application
    And Verify Run Report panel should be opened
    And User selects "Date" and "Health System EHR" as row dimensions
    And User generates the report
    Then The report generates with hierarchical grouping "Date > EHR Platform" and no error
    When User adds a filter "Health System EHR = Epic" and generates the report
    # Framework Gap: Requires step definitions for dimension-level filtering in LifeSteps.java
    Then The report returns only "Epic" rows and excludes all other EHR platforms

  # Source: ET-24951 (GAP-2, GAP-3)
  @todo
  Scenario Outline: Health System EHR dimension edge cases for missing data and saved reports
    When User navigates to run report from mega menu of the life application
    And Verify Run Report panel should be opened
    And User runs the "<CASE>" scenario for the Health System EHR dimension
    # Framework Gap: Requires step definitions for missing-value fallback and saved-report load in LifeSteps.java
    Then The report handles it gracefully with result "<EXPECTED>" and no server error
    Examples:
      | CASE                                          | EXPECTED                                  |
      | publisher 562529 Veradigm missing reporting_value | valid fallback label (Veradigm or Unknown) |
      | saved report template without the EHR dimension   | report runs with EHR dimension absent      |
      | date range with zero Flora EHR delivery           | empty rows and no 500 or 400 error         |

  # Source: ET-24951 (HT-5466)
  @todo
  Scenario: Regression - Flora CBR PG deal pricing displays correctly after EHR dimension deployment
    And User searches the Flora EHR deal account and opens the Life DSP deal view
    # Framework Gap: Requires step definition to assert deal pricing type in the deal list in LifeSteps.java
    Then The CBR deal "PP_HealthSystemEHR_CBR004" with Fixed pricing displays "Fixed" and not "Floor"
