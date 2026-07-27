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

  # Source: ET-24701
  @todo
  Scenario Outline: Verify HCP365 Report Builder "in" and "not in" multi-value filtering
    Given Life application is logged in as "<USER>"
    And User clicks on HCP 365 module from main menu
    # Framework Gap: Requires step definitions for the HCP365 Report Builder in/not in filter operators in HcpSteps.java
    When User creates a filter on a supported dimension in HCP365 Report Builder
    Then The operator dropdown shows "in" and "not in" for supported dimensions and hides them for unsupported dimensions
    And An "Advertiser ID in <IN_LIST>" filter returns only those advertisers and an "Advertiser ID not in <NOT_IN_LIST>" filter excludes them
    And An empty value list and a non-numeric value show a validation error and duplicate values are deduplicated or accepted with no hard error
    And A single-value "in" filter behaves identically to an equals filter and combining "in" and "not in" across dimensions applies AND logic
    And An "in" filter with 50 or more values completes within 30 seconds with no timeout
    And A report saved with an "in" filter reloads and re-runs correctly and a new scheduled report is Enabled by default
    # Regression anchor: HT-5720 - Private Layouts previously missing in HCP365 Analytics
    And Existing single-value filters still work and private layouts in HCP365 Analytics remain accessible
    # Regression anchor: HT-3962 - HCP365 Moments/Claims Sev 1 visualization bug
    And Moments and Claims visualizations display correctly for an account with HCP365 enabled
    Examples:
      | USER    | IN_LIST                  | NOT_IN_LIST      |
      | admin11 | [123456, 789012, 345678] | [123456, 789012] |
