Feature: HCP365 Regression - Dashboard data Validation and Permission

  Scenario Outline: Verify Overview Tabs and its Sub Tabs availability on Dashboard on HCP 365
    Given Life application is logged in as "<USER>"
    And User clicks on HCP 365 module from main menu
    When User land on Overview tab on HCP Dashboard
    Then Verify Tabs and Subtabs of Overview tab on HCP Dashboard
    Examples:
      | USER    | ADVERTISER | TYPE    | CLIENT | ExpectedOutput | User Type |
      | admin11 |            | Regular |        | XYZ            | Internal  |

  Scenario Outline: Verify Site Tabs and its Sub Tabs availability on Dashboard on HCP 365
    Given Life application is logged in as "<USER>"
    And User clicks on HCP 365 module from main menu
    When User clicks on Site tab on HCP Dashboard
    Then Verify Tabs and Subtabs of Site tab on HCP Dashboard
    Examples:
      | USER    | ADVERTISER | TYPE    | CLIENT | ExpectedOutput | User Type |
      | admin11 |            | Regular |        | XYZ            | Internal  |

  Scenario Outline: Verify Search Tabs and its Sub Tabs availability on Dashboard on HCP 365
    Given Life application is logged in as "<USER>"
    And User clicks on HCP 365 module from main menu
    When User clicks on Search tab on HCP Dashboard
    Then Verify Tabs and Subtabs of Search tab on HCP Dashboard
    Examples:
      | USER    | ADVERTISER | TYPE    | CLIENT | ExpectedOutput | User Type |
      | admin11 |            | Regular |        | XYZ            | Internal  |

  Scenario Outline: Verify Media Tabs and its Sub Tabs availability on Dashboard on HCP 365
    Given Life application is logged in as "<USER>"
    And User clicks on HCP 365 module from main menu
    When User clicks on Media tab on HCP Dashboard
    Then Verify Tabs and Subtabs of Media tab on HCP Dashboard
    Examples:
      | USER    | ADVERTISER | TYPE    | CLIENT | ExpectedOutput | User Type |
      | admin11 |            | Regular |        | XYZ            | Internal  |

  Scenario Outline: Verify Email Tabs and its Sub Tabs availability on Dashboard on HCP 365
    Given Life application is logged in as "<USER>"
    And User clicks on HCP 365 module from main menu
    When User clicks on Email tab on HCP Dashboard
    Then Verify Tabs and Subtabs of Email tab on HCP Dashboard
    Examples:
      | USER    | ADVERTISER | TYPE    | CLIENT | ExpectedOutput | User Type |
      | admin11 |            | Regular |        | XYZ            | Internal  |

  Scenario Outline: Verify Social Tabs and its Sub Tabs availability on Dashboard on HCP 365
    Given Life application is logged in as "<USER>"
    And User clicks on HCP 365 module from main menu
    When User clicks on Social tab on HCP Dashboard
    Then Verify Tabs and Subtabs of Social tab on HCP Dashboard
    Examples:
      | USER    | ADVERTISER | TYPE    | CLIENT | ExpectedOutput | User Type |
      | admin11 |            | Regular |        | XYZ            | Internal  |

  Scenario Outline: Verify user access modification of Dashboard on HCP 365 in all tab.
    Given Life application is logged in as "<USER>"
    And User clicks on HCP 365 module from main menu
    When User change settings of HCP Dashboard Display modules
    Then Verify Tabs and Subtabs of shall change on HCP Dashboard
    Examples:
      | USER    | ADVERTISER | TYPE    | CLIENT | ExpectedOutput | User Type |
      | admin11 |            | Regular |        | XYZ            | Internal  |

  Scenario Outline: Verify data after modification of filters in Dashboard on HCP 365 in all tab.
    Given Life application is logged in as "<USER>"
    And User clicks on HCP 365 module from main menu
    When User change filters of HCP Dashboard Overview tabs
    Then Verify data after change in filter of HCP Dashboard
    Examples:
      | USER    | ADVERTISER | TYPE    | CLIENT | ExpectedOutput | User Type |
      | admin11 |            | Regular |        | XYZ            | Internal  |

  @todo
  # Source: ET-24701
  Scenario: Verify HCP365 Run Now filter operator dropdown includes in and not in
    Given Life application is logged in as "admin11"
    And User clicks on HCP 365 module from main menu
    When User navigates to Report Builder Run Now filter operator dropdown
    Then "in" and "not in" appear as selectable operators alongside the existing single-value operators

  @todo
  # Source: ET-24701
  Scenario Outline: HCP365 in/not in filtering generates the correct WHERE clause per dimension
    Given Life application is logged in as "admin11"
    And User clicks on HCP 365 module from main menu
    # Framework Gap: Requires step definitions for the in/not in operator and multi-value filter input in HcpSteps.java
    When User selects dimension "<DIMENSION>", operator "<OPERATOR>", and enters values "<VALUES>"
    And User runs the report
    Then "<EXPECTED_RESULT>"
    Examples:
      | DIMENSION     | OPERATOR | VALUES                     | EXPECTED_RESULT                                                                             |
      | Advertiser ID | in       | 101,102,103                | All three values are accepted and the report returns only advertisers 101, 102, 103         |
      | Advertiser ID | not in   | 101,102                    | The report excludes advertisers 101 and 102 and returns the rest                            |
      | npi           | in       | 1447230388,1447230389      | The report returns rows only for the listed NPIs                                            |
      | Account Name  | in       | Acme Health, Beacon Health | The dropdown-converted dimension accepts multiple values and filters the report accordingly |

  @todo
  # Source: ET-24701, GAP-1, GAP-2, AMB-2
  Scenario: The in/not in filter guards empty values, parses comma-separated input, and handles operator switching
    Given Life application is logged in as "admin11"
    And User clicks on HCP 365 module from main menu
    When User selects dimension "Advertiser ID" and operator "in" with no values entered
    Then Apply is blocked until at least the required values are entered
    # Framework Gap: Requires step definition for the single-value degenerate-list rule in HcpSteps.java
    When User selects operator "in" and enters a single value "101"
    Then Behavior is defined and consistent per the resolved GAP-1 rule and recorded
    # Framework Gap: Requires step definition for comma/whitespace trimming in the value parser in HcpSteps.java
    When User enters values " 101 , 102 " with surrounding whitespace
    Then Values are trimmed and split correctly so the clause uses 101 and 102
    # Framework Gap: Requires step definition for operator-switch value retention in HcpSteps.java
    When User enters values with operator "in" then switches the operator to "equals" and back to "in"
    Then Entered values are cleared or preserved per the defined rule and the applied filter reflects the final operator

  @todo
  # Source: ET-24701
  # Regression anchor: HT-6148, HT-6045 - scheduled reports defaulting to Disabled / rerun template rendering blank
  Scenario: Adding in/not in operators does not regress scheduled or rerun report behavior
    Given Life application is logged in as "admin11"
    And User clicks on HCP 365 module from main menu
    When User runs a report with dimension "Advertiser ID", operator "in", values "101,102"
    And User schedules the report and then reruns it
    Then The report runs, schedules, and reruns without a scheduled-Disabled state or a blank rerun template
