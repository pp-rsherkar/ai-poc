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

  # Source: ET-24245
  @todo
  Scenario Outline: HCP365 Report Builder's Report Format dropdown supports all six delimiter formats, mirroring Life's layout
    Given Life application is logged in as "<USER>"
    And User clicks on HCP 365 module from main menu
    And User navigates to Report Builder
    When User opens the Report Format dropdown
    Then all six formats are available, matching Life's Report Format UI layout
      | CSV (Comma-delimited) |
      | Tab Delimited (TSV)   |
      | Pipe Delimited CSV    |
      | Pipe Delimited TXT    |
      | Tab Delimited TXT     |
      | Excel                 |
    When User selects "<FORMAT>" and generates a scheduled or on-demand report containing a value with the delimiter character itself, with Text Qualifier enabled
    Then the delimiter is correctly applied and Text Qualifier/escaping prevents column misalignment for that value
    Given a report scheduled before this change shipped with a legacy format setting
    Then it continues to generate correctly post-deployment with no forced-migration break
    Examples:
      | USER    | FORMAT                |
      | admin11 | CSV (Comma-delimited) |
      | admin11 | Tab Delimited (TSV)   |
      | admin11 | Pipe Delimited CSV    |
      | admin11 | Pipe Delimited TXT    |
      | admin11 | Tab Delimited TXT     |
