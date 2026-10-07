Feature: HCP Explorer Workspace creation in Studio using filters, AI Configurator, and visualization
  1. Creation of Workspace in Studio
  2. Applying filters to the workspace by clicking Add Filter icon, building audience using AI prompts, and visualizing the audience
  3. Verify the creation of Draft workspace in Studio application.
  4. Verify the presence of Draft workspaces in Workspace Management page for external user.
  5. Verify the editing and publishing of Draft workspaces in Studio application.

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "PP engineering test" and checks Studio permissions
    And User clicks PulsePoint icon to navigate back to Life
    And User navigates to Studio application

  @regression
  Scenario Outline: Create and save HCP Explorer workspace with specific filters
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "<ADVERTISER>"
    And User edits the "HCP Explorer" workspace name as "<WORKSPACE_NAME>"
    And Verify that advertiser field is disabled and displayed in "rgba(34, 34, 34, 0.55)" after saving the workspace
    And User applies the filter and selects option
      | FilterName           | Option                                                                                                                  |
      | NPI Age              | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above                                                           |
      | NPI Gender           | Female, Male, Unknown                                                                                                   |
      | Graduation Year      |                                                                                                               1900-2025 |
      | Net Worth            | Less than $50٫000, $100٫000 to $249٫999, $250٫000 to $499٫999, $500٫000 or above                                        |
      | Number of Patients   | Below 5, 6 to 20, 21 to 50, 51 to 100, 101 to 200, 201 to 300, 301 to 400, 401 to 500, 501 to 1000, 1001 or above       |
      | Patient Age          | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above                                                           |
      | Patient Gender       | Female, Male, Unknown                                                                                                   |
      | Years Practiced      | Below 5, 5 to 10, 10 to 15, 15 to 20, 20 to 25, 25 to 30, 30 to 35, 35 to 40, 40 to 45, 45 to 50, 50 or Above           |
      | Facility Name        | Visiting Nurse Service, Ahs Hospital Corp, Centrastate, Robert Wood Johnson University Hospital, Montclair Hospital Llc |
      | State                | New                                                                                                                     |
      | Profession           | Physician                                                                                                               |
      | Specialty            | Foot & Ankle Surgery, Internal Medicine                                                                                 |
      | Medical School       | New York College                                                                                                        |
      | Patient Facility     | Arizona Autism United٫ Inc.                                                                                             |
      | Prescriptions        | .Insulin Aspart Protamine And Insulin Aspart                                                                            |
      | Prescribing behavior | .Insulin Aspart Protamine And Insulin Aspart                                                                            |
      | Diagnoses            | ABO incompatibility w hemolytic transfs react٫ unsp٫ subs                                                               |
      | Procedures           | Abatacept injection                                                                                                     |
      | IAB                  | Travel                                                                                                                  |
      | MeSH                 | Anatomy                                                                                                                 |
      #| Reachable Audience   | Yes                                                                                                                     |
      #| NPI List Name      | Large file test                                                                                                         |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the "HCP Explorer" workspace
    Then Verify the "HCP Explorer" Workspace is saved
    And Navigate to workspace dashboard
    And User searches the workspace created to perform Actions from More menu
    And User selects the "Delete" option by clicking More Actions menu
    And Verify user is able to delete the workspace
    Examples:
      | ADVERTISER | WORKSPACE_NAME |
      | Abbvie     | HCP_Explorer   |

  @regression
  Scenario Outline: Create and save HCP Explorer workspace by building audience using AI Configurator - <AI_PROMPT>
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "<ADVERTISER>"
    And User edits the "HCP Explorer" workspace name as "<WORKSPACE_NAME>"
    And Verify that advertiser field is disabled and displayed in "rgba(34, 34, 34, 0.55)" after saving the workspace
    And User clicks on AI Configurator and build audience using the AIPrompt "<AI_PROMPT>"
    Then Verify the filter is applied correctly "<PRIMARY_FILTERS>"
    Examples:
      | ADVERTISER | WORKSPACE_NAME | AI_PROMPT                                                                                                                                    | PRIMARY_FILTERS                                  |
      | Abbvie     | HCP_Explorer   | Select Cardiovascular Professionals who are reachable in California state and also exclude net worth Less than $50٫000                       | Net Worth, Specialty Filter, State               |
      | Abbvie     | HCP_Explorer   | Filter doctors by their gender, how long they've been practicing, and then narrow down patients by their age groups.                         | Clinical Recency, NPI Gender, Years Practiced    |
      | Abbvie     | HCP_Explorer   | Look for doctors who graduated within a specific time frame, are located in certain states, and then focus on patient gender.                | Graduation Year, Profession, State               |
      | Abbvie     | HCP_Explorer   | Find doctors within certain age ranges, with specific professions, and narrow by wealth.                                                     | NPI Age, Net Worth, Profession                   |
      | Abbvie     | HCP_Explorer   | Search for doctors from specific medical schools with third-level specialties and certain patient counts.                                    | Clinical Recency, Number of Patients, Profession |
      | Abbvie     | HCP_Explorer   | Focus on patients of specific age groups and genders.                                                                                        | Patient Age, Patient Gender                      |
      | Abbvie     | HCP_Explorer   | Filter doctors by their gender and age, then look for those who graduated from particular medical schools.                                   | NPI Age, NPI Gender, Profession                  |
      | Abbvie     | HCP_Explorer   | Doctors with specific experience, states, wealth level, and patient count.                                                                   | Clinical Recency, Profession, Years Practiced    |
      | Abbvie     | HCP_Explorer   | Select cardiovascular specialist who are graduated before 2004 in New York or California                                                     | Graduation Year, Specialty Filter, State         |
      | Abbvie     | HCP_Explorer   | Decide if you want to reach a broader audience, then focus on patients of specific age groups and genders.                                   | Clinical Recency, Patient Age, Patient Gender    |
      | Abbvie     | HCP_Explorer   | Create a mix of NPIs by choosing profession and specialties in heart and brain surgery.                                                      | Profession, Specialty Filter                     |
      | Abbvie     | HCP_Explorer   | Find doctors based on their years of experience, the states they work in, their wealth level, and how many patients they typically see.      | Clinical Recency, Profession, Years Practiced    |
      | Abbvie     | HCP_Explorer   | Pick the types of NPI lists you want, look for doctors who graduated during a specific period, and then target certain hospitals or clinics. | Graduation Year, Profession                      |

  @regression
  Scenario Outline: Create and save HCP Explorer workspace by applying filters one by one and validating NPI details are refined
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "<ADVERTISER>"
    And User edits the "HCP Explorer" workspace name as "<WORKSPACE_NAME>"
    And Verify that advertiser field is disabled and displayed in "rgba(34, 34, 34, 0.55)" after saving the workspace
    And User applies the following filters one by one and checks that NPI details are refined after each filter:
      | FilterName         | Option                                                                                                                  |
      | NPI Age            | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above                                                           |
      | NPI Gender         | Female, Male, Unknown                                                                                                   |
      | Graduation Year    |                                                                                                               1900-2025 |
      | Net Worth          | Less than $50٫000, $100٫000 to $249٫999, $250٫000 to $499٫999, $500٫000 or above                                        |
      | Number of Patients | Below 5, 6 to 20, 21 to 50, 51 to 100, 101 to 200, 201 to 300, 301 to 400, 401 to 500, 501 to 1000, 1001 or above       |
      | Patient Age        | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above                                                           |
      | Patient Gender     | Female, Male, Unknown                                                                                                   |
      | Years Practiced    | Below 5, 5 to 10, 10 to 15, 15 to 20, 20 to 25, 25 to 30, 30 to 35, 35 to 40, 40 to 45, 45 to 50, 50 or Above           |
      | Facility Name      | Visiting Nurse Service, Ahs Hospital Corp, Centrastate, Robert Wood Johnson University Hospital, Montclair Hospital Llc |
      | State              | New                                                                                                                     |
      | Profession         | Physician                                                                                                               |
      | Specialty          | Foot & Ankle Surgery, Internal Medicine                                                                                 |
      | Medical School     | New York College                                                                                                        |
      #| Reachable Audience | Yes                                                                                                                     |
      #| NPI List Name      | Large file test                                                                                                         |
    And Navigate to workspace dashboard
    And User searches the workspace created to perform Actions from More menu
    And User selects the "Delete" option by clicking More Actions menu
    And Verify user is able to delete the workspace
    Examples:
      | ADVERTISER | WORKSPACE_NAME |
      | Abbvie     | HCP_Explorer   |

  @regression
  Scenario Outline: Create and save HCP Explorer workspace using NPI Cross Filters
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "<ADVERTISER>"
    And User edits the "HCP Explorer" workspace name as "<WORKSPACE_NAME>"
    And Verify that advertiser field is disabled and displayed in "rgba(34, 34, 34, 0.55)" after saving the workspace
    And User applies the filter and selects option
      | FilterName | Option                                                        |
      | NPI Age    | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above |
      | NPI Gender | Female, Male, Unknown                                         |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User hovers over the dashboard filters, selects the region with maximum NPIs and clicks on it
      | NPI Geographic Location    |
      | NPI Facilities Geography   |
      | Patient Age Range          |
      | Patient Gender             |
      | Patient Distribution       |
      | Net Worth                  |
      | NPI Gender                 |
      | NPI Age Range              |
      | Years Practiced            |
      | Top 20 Market Areas        |
      | Top 20 Professions         |
      | Top 20 Specialties         |
      | Top 20 Insurance Providers |
      | Top 20 Prescriptions       |
      | Top 20 Diagnoses           |
      | Top 20 Procedures          |
      | Top 20 MeSH Categories     |
      | Top 20 IAB Categories      |
    And Verify that dashboard filters are displayed correctly in Filter section
    And User saves the "HCP Explorer" workspace
    Then Verify the "HCP Explorer" Workspace is saved
    And Verify dashboard filters are merged with Primary filters
    And Fetch and verify that NPI details are refined
    And Navigate to workspace dashboard
    And User searches the workspace created to perform Actions from More menu
    And User selects the "Delete" option by clicking More Actions menu
    And Verify user is able to delete the workspace
    Examples:
      | ADVERTISER | WORKSPACE_NAME |
      | Abbvie     | HCP_Explorer   |

  @regression
  Scenario Outline: Manage operations on Workspace - Rename, Duplication, and Delete on HCP Explorer workspace
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "<ADVERTISER>"
    And User edits the "HCP Explorer" workspace name as "<WORKSPACE_NAME>"
    And Verify that advertiser field is disabled and displayed in "rgba(34, 34, 34, 0.55)" after saving the workspace
    And User applies the filter and selects option
      | FilterName | Option                |
      | NPI Age    | Below 25, 25 to 35,   |
      | NPI Gender | Female, Male, Unknown |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the "HCP Explorer" workspace
    Then Verify the "HCP Explorer" Workspace is saved
    And User clicks Edit button and updates workspace name to "<WORKSPACE_NAME_EDIT>"
    Then Verify the Workspace is updated with edited name
    And User saves the "HCP Explorer" workspace
    Then Verify the "HCP Explorer" Workspace is saved
    And Navigate to workspace dashboard
    And User searches the workspace created to perform Actions from More menu
    And User selects the "Rename" option by clicking More Actions menu
    And Verify user is able to rename the "HCP Explorer" workspace as "<NEW_WORKSPACE_NAME>"
    And User is able to search the workspace after performing operation - "Rename"
    And User searches the workspace created to perform Actions from More menu
    And User selects the "Duplicate" option by clicking More Actions menu
    And Verify user is able to duplicate the "HCP Explorer" workspace
    And User is able to search the workspace after performing operation - "Duplicate"
    And User searches the workspace created to perform Actions from More menu
    And User selects the "Delete" option by clicking More Actions menu
    And Verify user is able to delete the workspace
    Examples:
      | ADVERTISER | WORKSPACE_NAME | NEW_WORKSPACE_NAME | WORKSPACE_NAME_EDIT |
      | Abbvie     | HCP_Explorer   | New_HCP_Explorer_  | Edit_HCP_Explorer   |

  @regression
  Scenario Outline: Validate Clinical and Contextual Recency filters in HCP Explorer workspace
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "<ADVERTISER>"
    And User edits the "HCP Explorer" workspace name as "<WORKSPACE_NAME>"
    And Verify that advertiser field is disabled and displayed in "rgba(34, 34, 34, 0.55)" after saving the workspace
    And User applies "Clinical" filter, selects filter options as below and verifies the clinical recency filter is updated correctly
      | FilterName           | Option                                                  | Recency  |
      | Prescriptions        |                                 100％ Mineral Sunscreen |  1 Month |
      | Prescribing behavior |           100％ Mineral Broad Spectrum Sunscreen Spf 30 | 3 Months |
      | Diagnoses            | Maternal care for face٫ brow and chin presentation٫ oth | 6 Months |
      | Procedures           | Removal of face wrinkles                                |   1 Year |
    And User applies "Contextual" filter, selects filter options as below and verifies the clinical recency filter is updated correctly
      | FilterName | Option               | Recency |
      | IAB        | Arts & Entertainment |   1 Day |
      | MeSH       | Anatomy              |  1 Week |
    And User saves the "HCP Explorer" workspace
    Then Verify the "HCP Explorer" Workspace is saved
    And Navigate to workspace dashboard
    And User searches the workspace created to perform Actions from More menu
    And User selects the "Delete" option by clicking More Actions menu
    And Verify user is able to delete the workspace
    Examples:
      | ADVERTISER | WORKSPACE_NAME |
      | Abbvie     | HCP_Explorer   |

  @regression
  Scenario Outline: Validate the persistence of applied filters (Workspace Type, Advertiser, Created By, Workspace Name) on the Studio Workspace Details page
    When User selects the workspace type "<WORKSPACE_TYPE>"
    And User selects "<ADVERTISER>" from the Studio Workspace Advertiser dropdown
    And User selects "<CREATED_BY>" from the Studio Workspace Created By dropdown
    And User searches for a workspace by name using the search box on the Workspace Details page
    And User navigates to another page within Studio and then returns to the workspace list page
    Then User verifies that the selected filters, dropdown values, and search input remain persistent unless they are manually deselected or cleared - "<WORKSPACE_TYPE>", "<ADVERTISER>", "<CREATED_BY>"
    And Verify on refresh of the page, the filters are reset and search input is cleared
    Examples:
      | WORKSPACE_TYPE | ADVERTISER | CREATED_BY                     |
      | HCP Explorer   | Abbvie     | ppqa_automation@pulsepoint.com |

  @regression
  Scenario Outline: Create and save a Draft workspace with specific filters and verify visibility with External User
    When User clicks on Create New Workspace
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "<ADVERTISER>"
    And User edits the "HCP Explorer" workspace name as "<WORKSPACE_NAME>"
    Then User applies the filter and selects option
      | FilterName | Option                                                        |
      | NPI Age    | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User selects the Draft option as "<DRAFT_OPTION>"
    And User saves the "HCP Explorer" workspace
    And Internal user logs out from the application
    Given This scenario will be executed in the "Pre-release" environment as a "External User"
    And "Studio" application is logged in successfully with Account "<ACCOUNT_NAME>"
    #And External User switches the "<ACCOUNT_NAME>"account in Studio application -- commiting this step for future changes, if pp engineering test account does not appears in external user account list in studio application
    When External user searches the workspace name in studio application with "<DRAFT_OPTION>" draft option
    Then External user verifies whether the workspace with "<DRAFT_OPTION>" is visible in workspace management page
    Examples:
      | ADVERTISER | DRAFT_OPTION | WORKSPACE_NAME | ACCOUNT_NAME        |
      | Abbvie     | Public       | HCP_Explorer   | PP engineering test |
      | Abbvie     | Private      | HCP_Explorer   | PP engineering test |

  # Source: PROD-14817 (TC_01, TC_03, TC_04, TC_54, TC_65)
  @todo
  Scenario: Diagnoses bulk paste is evaluated one value per line, applied on save and updated when a bulk-added value is removed
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    And User fetches the Identified NPI count from the workspace
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Diagnoses" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value |
      | E119  |
      | I10   |
    # Framework Gap: Requires Bulk Upload close-without-save step in StudioSteps.java
    And User closes the Bulk Upload window without saving
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows no selection under "Diagnoses"
    # Framework Gap: Requires Identified NPI count comparison step in StudioSteps.java
    And Verify the Identified NPI count is unchanged
    When User opens the Bulk Upload window for the "Diagnoses" filter
    And User pastes the following values into the Bulk Upload text area
      | Value                    |
      | Type 2 diabetes mellitus |
      | Essential hypertension   |
      | Asthma                   |
    # Framework Gap: Requires Bulk Upload results list read hook in ExplorerWorkspace.java
    Then Verify the Bulk Upload results list shows 3 input entries
    # Framework Gap: Requires per-entry match status read hook in ExplorerWorkspace.java
    And Verify "Type 2 diabetes mellitus" is matched in the Bulk Upload results
    And Verify "Essential hypertension" is matched in the Bulk Upload results
    And Verify "Asthma" is matched in the Bulk Upload results
    # Framework Gap: Requires per-entry match count read hook in ExplorerWorkspace.java
    And Verify "Asthma" returns more than 1 match in the Bulk Upload results
    When User closes the Bulk Upload window without saving
    And User opens the Bulk Upload window for the "Diagnoses" filter
    And User pastes the following values into the Bulk Upload text area
      | Value |
      | E119  |
      | I10   |
    # Framework Gap: Requires per-entry matched value read hook in ExplorerWorkspace.java
    Then Verify "E119" is matched to "Type 2 diabetes mellitus without complications" in the Bulk Upload results
    And Verify "I10" is matched to "Essential (primary) hypertension" in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    When User saves the Bulk Upload input
    Then Verify the left rail shows 2 selections under "Diagnoses"
    And Verify the Identified NPI count has changed
    And User fetches the Identified NPI count from the workspace
    # Framework Gap: Requires left rail value removal step in StudioSteps.java
    When User removes "I10" from the "Diagnoses" filter in the left rail
    Then Verify the left rail shows 1 selection under "Diagnoses"
    And Verify the Identified NPI count has changed
    And User saves the "HCP Explorer" workspace
    Then Verify the "HCP Explorer" Workspace is saved
    # Framework Gap: Requires browser reload and workspace reopen step in StudioSteps.java
    When User reloads the browser and reopens the saved "HCP Explorer" workspace
    Then Verify the left rail shows 1 selection under "Diagnoses"
    And Navigate to workspace dashboard
    And User searches the workspace created to perform Actions from More menu
    And User selects the "Delete" option by clicking More Actions menu
    And Verify user is able to delete the workspace

  # Source: PROD-14817 (TC_02, TC_37, TC_38), STUD-1482
  @todo
  Scenario: Manually entered bulk values are split only on new lines and trailing commas are stripped on blur
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Diagnoses" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value           |
      | E119,I10,J45909 |
    # Framework Gap: Requires Bulk Upload results list read hook in ExplorerWorkspace.java
    Then Verify the Bulk Upload results list shows 1 input entry
    # Framework Gap: Requires per-entry match status read hook in ExplorerWorkspace.java
    And Verify "E119,I10,J45909" is reported as unmatched in the Bulk Upload results
    # Framework Gap: Requires input entry listing read hook in ExplorerWorkspace.java
    And Verify "E119" is not listed as a separate input entry in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload close-without-save step in StudioSteps.java
    When User closes the Bulk Upload window without saving
    And User opens the Bulk Upload window for the "Diagnoses" filter
    # Framework Gap: Requires Bulk Upload text area typing step in StudioSteps.java
    And User types the following values into the Bulk Upload text area
      | Value |
      | E119, |
      | I10,  |
    # Framework Gap: Requires Bulk Upload text area blur step in StudioSteps.java
    And User clicks outside the Bulk Upload text area
    # Framework Gap: Requires Bulk Upload text area content read hook in ExplorerWorkspace.java
    Then Verify the Bulk Upload text area shows the following values
      | Value |
      | E119  |
      | I10   |
    # Framework Gap: Requires per-entry match count read hook in ExplorerWorkspace.java
    And Verify "E119" returns exactly 1 match in the Bulk Upload results
    And Verify "I10" returns exactly 1 match in the Bulk Upload results
    When User closes the Bulk Upload window without saving
    And User opens the Bulk Upload window for the "Facility Name" filter
    And User pastes the following values into the Bulk Upload text area
      | Value                   |
      | Bellevue Heart Grp, LLC |
    Then Verify the Bulk Upload results list shows 1 input entry
    And Verify "LLC" is not listed as a separate input entry in the Bulk Upload results

  # Source: PROD-14817 (TC_39, TC_40), STUD-1482
  @todo
  Scenario: Uploaded file lines keep look-alike commas inside the value and import only the text before a real comma
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Facility Name" filter
    # Framework Gap: Requires Browse Computer file upload step in StudioSteps.java and fixture file in uploadfiles
    And User uploads the file "facility_lookalike_comma.txt" via Browse Computer in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload results list read hook in ExplorerWorkspace.java
    Then Verify the Bulk Upload results list shows 2 input entries
    # Framework Gap: Requires input entry listing read hook in ExplorerWorkspace.java
    And Verify "Trilogy Healthcare Of Bellevue٫ Llc" is listed as a single input entry in the Bulk Upload results
    # Framework Gap: Requires per-entry match status read hook in ExplorerWorkspace.java
    And Verify "Trilogy Healthcare Of Bellevue٫ Llc" is matched in the Bulk Upload results
    # Framework Gap: Requires per-entry matched value read hook in ExplorerWorkspace.java
    And Verify "Lenox Hill Hosp" is matched to "Lenox Hill Hospital" in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload close-without-save step in StudioSteps.java
    When User closes the Bulk Upload window without saving
    And User opens the Bulk Upload window for the "Facility Name" filter
    And User uploads the file "facility_comma_line.txt" via Browse Computer in the Bulk Upload window
    Then Verify the Bulk Upload results list shows 1 input entry
    And Verify "Bellevue Heart Grp" is listed as a single input entry in the Bulk Upload results
    And Verify "Lenox Hill Hosp" is not listed as a separate input entry in the Bulk Upload results

  # Source: PROD-14817 (TC_05, TC_07, TC_09)
  @todo
  Scenario Outline: Bulk Upload imports one value per line from a <FILE_TYPE> file into the <FILTER> filter
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "<FILTER>" filter
    # Framework Gap: Requires Browse Computer file upload step in StudioSteps.java and fixture file in uploadfiles
    And User uploads the file "<FILE>" via Browse Computer in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload results list read hook in ExplorerWorkspace.java
    Then Verify the Bulk Upload results list shows 3 input entries
    # Framework Gap: Requires input entry order read hook in ExplorerWorkspace.java
    And Verify the Bulk Upload input entries are listed in the order "<VALUE_1>", "<VALUE_2>", "<VALUE_3>"
    # Framework Gap: Requires per-entry match status read hook in ExplorerWorkspace.java
    And Verify "<VALUE_1>" is matched in the Bulk Upload results
    And Verify "<VALUE_2>" is matched in the Bulk Upload results
    And Verify "<VALUE_3>" is matched in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    When User saves the Bulk Upload input
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows 3 selections under "<FILTER>"
    Examples:
      | FILE_TYPE | FILTER             | FILE           | VALUE_1             | VALUE_2              | VALUE_3              |
      | txt       | Insurance Provider | insurers.txt   | Aetna               | Cigna                | Humana               |
      | csv       | Facility Name      | facilities.csv | Lenox Hill Hospital | Mount Sinai Hospital | NYU Langone Hospital |
      | xlsx      | NPI Zip Code       | zips.xlsx      | 07030               | 10021                | 02115                |

  # Source: PROD-14817 (TC_06)
  @todo
  Scenario Outline: Bulk Upload rejects an unsupported <FILE> file and names the allowed file types
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Insurance Provider" filter
    # Framework Gap: Requires Browse Computer file upload step in StudioSteps.java and fixture file in uploadfiles
    And User uploads the file "<FILE>" via Browse Computer in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload validation message read hook in ExplorerWorkspace.java
    Then Verify the Bulk Upload validation message names the allowed file types "txt", "csv" and "xlsx"
    # Framework Gap: Requires Bulk Upload results list read hook in ExplorerWorkspace.java
    And Verify the Bulk Upload results list shows 0 input entries
    # Framework Gap: Requires Bulk Upload close-without-save step in StudioSteps.java
    When User closes the Bulk Upload window without saving
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows no selection under "Insurance Provider"
    Examples:
      | FILE          |
      | insurers.pdf  |
      | insurers.docx |

  # Source: PROD-14817 (TC_08)
  @todo
  Scenario: Bulk Upload of an empty csv file adds no selection
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Facility Name" filter
    # Framework Gap: Requires Browse Computer file upload step in StudioSteps.java and fixture file in uploadfiles
    And User uploads the file "empty.csv" via Browse Computer in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload results list read hook in ExplorerWorkspace.java
    Then Verify the Bulk Upload results list shows 0 input entries
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    When User saves the Bulk Upload input
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows no selection under "Facility Name"

  # Source: PROD-14817 (TC_11, TC_25, TC_29, TC_31, TC_35)
  @todo
  Scenario Outline: Fuzzy-match filter <FILTER> resolves "<VALUE>" to "<MATCH>" and applies it on save
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "<FILTER>" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value   |
      | <VALUE> |
    # Framework Gap: Requires per-entry matched value read hook in ExplorerWorkspace.java
    Then Verify "<VALUE>" is matched to "<MATCH>" in the Bulk Upload results
    # Framework Gap: Requires unmatched warning read hook in ExplorerWorkspace.java
    And Verify no unmatched entry warning is displayed in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    When User saves the Bulk Upload input
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows 1 selection under "<FILTER>"
    Examples:
      | FILTER             | VALUE              | MATCH                |
      | Prescriptions      | Lipitor            | Lipitor              |
      | Prescriptions      | Crestor            | Crestor              |
      | Facility Name      | Lenox Hill Hosp    | Lenox Hill Hospital  |
      | NPI DMA            | New York           | New York             |
      | NPI DMA            | Philadelphia       | Philadelphia         |
      | Insurance Provider | United Health Care | UnitedHealthcare     |
      | Insurance Provider | Aetna              | Aetna                |
      | Patient Facility   | Mount Sinai Hosp   | Mount Sinai Hospital |

  # Source: PROD-14817 (TC_21, TC_23, TC_27)
  @todo
  Scenario Outline: Exact-match filter <FILTER> selects only the pasted values
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "<FILTER>" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value     |
      | <VALUE_1> |
      | <VALUE_2> |
    # Framework Gap: Requires per-entry match count read hook in ExplorerWorkspace.java
    Then Verify "<VALUE_1>" returns exactly 1 match in the Bulk Upload results
    And Verify "<VALUE_2>" returns exactly 1 match in the Bulk Upload results
    # Framework Gap: Requires per-entry matched value read hook in ExplorerWorkspace.java
    And Verify "<VALUE_1>" is matched to "<VALUE_1>" in the Bulk Upload results
    And Verify "<VALUE_2>" is matched to "<VALUE_2>" in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    When User saves the Bulk Upload input
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows 2 selections under "<FILTER>"
    Examples:
      | FILTER                 | VALUE_1    | VALUE_2           |
      | Profession & Specialty | Cardiology | Internal Medicine |
      | State                  | New York   | New Jersey        |
      | NPI Zip Code           | 10021      | 10065             |

  # Source: PROD-14817 (TC_33)
  @todo
  Scenario: Patient State bulk values are applied independently of the NPI State filter
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Patient State" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value      |
      | California |
      | Texas      |
    # Framework Gap: Requires per-entry match count read hook in ExplorerWorkspace.java
    Then Verify "California" returns exactly 1 match in the Bulk Upload results
    And Verify "Texas" returns exactly 1 match in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    When User saves the Bulk Upload input
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows 2 selections under "Patient State"
    And Verify the left rail shows no selection under "State"

  # Source: PROD-14817 (TC_13, TC_18, TC_20, TC_22, TC_24, TC_26, TC_28, TC_30, TC_32, TC_34, TC_36), STUD-406
  @todo
  Scenario Outline: <FILTER> reports "<VALUE>" as unmatched and adds no selection
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "<FILTER>" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value   |
      | <VALUE> |
    # Framework Gap: Requires per-entry match status read hook in ExplorerWorkspace.java
    Then Verify "<VALUE>" is reported as unmatched in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    When User saves the Bulk Upload input
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows no selection under "<FILTER>"
    Examples:
      | FILTER                 | VALUE                     |
      | Prescriptions          | 00014000409               |
      | Prescriptions          | 99999999999               |
      | Diagnoses              | Z99999X                   |
      | Procedures             | Qwxzv procedure           |
      | Profession & Specialty | Cardiolgy                 |
      | State                  | New Yrok                  |
      | Facility Name          | Zzqx Imaginary Clinic 000 |
      | NPI Zip Code           | 1002                      |
      | NPI Zip Code           | 100211                    |
      | NPI Zip Code           | ABCDE                     |
      | NPI DMA                | Atlantis                  |
      | Insurance Provider     | Qqq Insurance 999         |
      | Patient State          | Californa                 |
      | Patient Facility       | Zzqx Imaginary Clinic 000 |

  # Source: PROD-14817 (TC_12, TC_16, TC_53), HT-6337
  @todo
  Scenario Outline: <FILTER> code "<VALUE>" is matched exactly to a single entry
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "<FILTER>" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value   |
      | <VALUE> |
    # Framework Gap: Requires per-entry match count read hook in ExplorerWorkspace.java
    Then Verify "<VALUE>" returns exactly 1 match in the Bulk Upload results
    # Framework Gap: Requires per-entry matched code read hook in ExplorerWorkspace.java
    And Verify no match other than "<VALUE>" is returned for "<VALUE>" in the Bulk Upload results
    Examples:
      | FILTER               | VALUE       |
      | Prescriptions        | 00014000401 |
      | Prescribing Behavior | 00014000401 |
      | Diagnoses            | E119        |

  # Source: PROD-14817 (TC_15, TC_16, TC_53), GAP-1, HT-6337
  @todo
  Scenario Outline: Alternate code format "<VALUE>" in <FILTER> never resolves to more than one entry or a different code
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "<FILTER>" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value   |
      | <VALUE> |
    # Framework Gap: Requires per-entry match count read hook in ExplorerWorkspace.java
    Then Verify "<VALUE>" returns at most 1 match in the Bulk Upload results
    # Framework Gap: Requires per-entry matched code read hook in ExplorerWorkspace.java
    And Verify no match other than "<CANONICAL_CODE>" is returned for "<VALUE>" in the Bulk Upload results
    Examples:
      | FILTER               | VALUE        | CANONICAL_CODE |
      | Prescribing Behavior | 0014000401   | 00014000401    |
      | Prescribing Behavior | 0014-0004-01 | 00014000401    |
      | Diagnoses            | E11.9        | E119           |

  # Source: PROD-14817 (TC_14)
  @todo
  Scenario: Prescribing Behavior accepts a mixed list of a drug name and an NDC-11 code
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Prescribing Behavior" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value       |
      | Lipitor     |
      | 00014000401 |
    # Framework Gap: Requires per-entry matched value read hook in ExplorerWorkspace.java
    Then Verify "Lipitor" is matched to "Lipitor" in the Bulk Upload results
    # Framework Gap: Requires per-entry match count read hook in ExplorerWorkspace.java
    And Verify "00014000401" returns exactly 1 match in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    When User saves the Bulk Upload input
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows 2 selections under "Prescribing Behavior"

  # Source: PROD-14817 (TC_47, TC_57), HT-5921, GAP-8
  @todo
  Scenario Outline: Bulk matching is case-insensitive for "<VALUE>" in <FILTER>
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "<FILTER>" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value   |
      | <VALUE> |
    # Framework Gap: Requires per-entry matched value read hook in ExplorerWorkspace.java
    Then Verify "<VALUE>" is matched to "<MATCH>" in the Bulk Upload results
    # Framework Gap: Requires unmatched warning read hook in ExplorerWorkspace.java
    And Verify no unmatched entry warning is displayed in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    When User saves the Bulk Upload input
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows 1 selection under "<FILTER>"
    Examples:
      | FILTER                 | VALUE      | MATCH      |
      | Prescriptions          | cipro      | Cipro      |
      | Prescriptions          | Cipro      | Cipro      |
      | Prescriptions          | CIPRO      | Cipro      |
      | Profession & Specialty | CARDIOLOGY | Cardiology |

  # Source: PROD-14817 (TC_19, TC_54, TC_55, TC_56), HT-5875, HT-5873, HT-5865, GAP-5
  @todo
  Scenario: Procedures bulk paste resolves names, CPT and HCPCS codes and hybrid input without wrong matches
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Procedures" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value                        |
      | Colonoscopy                  |
      | 45738-Diagonstic Colonoscopy |
      | 45738                        |
      | A9591                        |
    # Framework Gap: Requires per-entry match count read hook in ExplorerWorkspace.java
    Then Verify "Colonoscopy" returns more than 1 match in the Bulk Upload results
    # Framework Gap: Requires per-entry returned value exclusion check in ExplorerWorkspace.java
    And Verify "Anoscopy" is not returned for "Colonoscopy" in the Bulk Upload results
    And Verify "Anoscopy" is not returned for "45738-Diagonstic Colonoscopy" in the Bulk Upload results
    # Framework Gap: Requires per-entry matched value read hook in ExplorerWorkspace.java
    And Verify "45738" is matched to a procedure containing "colonoscopy" in the Bulk Upload results
    And Verify "A9591" is matched to "Fluoroestradiol F 18, diagnostic, 1 millicurie" in the Bulk Upload results

  # Source: PROD-14817 (TC_59), HT-6412
  @todo
  Scenario: Procedures bulk paste resolves HCPCS E0601 and its CPAP name to the same procedure
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Procedures" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value |
      | E0601 |
      | CPAP  |
    # Framework Gap: Requires per-entry matched value read hook in ExplorerWorkspace.java
    Then Verify "E0601" is matched to "Continuous positive airway pressure (CPAP) device" in the Bulk Upload results
    And Verify "CPAP" is matched to "Continuous positive airway pressure (CPAP) device" in the Bulk Upload results

  # Source: PROD-14817 (TC_51), HT-6382
  @todo
  Scenario: Diagnoses file upload does not over-match a diagnosis name to its opposite condition
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Diagnoses" filter
    # Framework Gap: Requires Browse Computer file upload step in StudioSteps.java and fixture file in uploadfiles
    And User uploads the file "diagnoses_overweight_thoracic.csv" via Browse Computer in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload results list read hook in ExplorerWorkspace.java
    Then Verify the Bulk Upload results list shows 2 input entries
    # Framework Gap: Requires per-entry returned value exclusion check in ExplorerWorkspace.java
    And Verify "Underweight" is not returned for "Overweight" in the Bulk Upload results

  # Source: PROD-14817 (TC_45, TC_46, TC_58), STUD-406, HT-5921, HT-5889, GAP-4
  @todo
  Scenario: Prescriptions bulk upload identifies unmatched lines and never renders a truncated value
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    And User fetches the Identified NPI count from the workspace
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Prescriptions" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value   |
      | Xyzqwrt |
      | Qqqzzz  |
    # Framework Gap: Requires per-entry match status read hook in ExplorerWorkspace.java
    Then Verify "Xyzqwrt" is reported as unmatched in the Bulk Upload results
    And Verify "Qqqzzz" is reported as unmatched in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    When User saves the Bulk Upload input
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows no selection under "Prescriptions"
    # Framework Gap: Requires Identified NPI count comparison step in StudioSteps.java
    And Verify the Identified NPI count is unchanged
    When User opens the Bulk Upload window for the "Prescriptions" filter
    And User pastes the following values into the Bulk Upload text area
      | Value   |
      | Lipitor |
      | Crestor |
      | Xyzqwrt |
    # Framework Gap: Requires unmatched warning count read hook in ExplorerWorkspace.java
    Then Verify the unmatched entry warning reports 1 unmatched entry
    # Framework Gap: Requires Show Entries click step in StudioSteps.java
    When User clicks Show Entries in the Bulk Upload window
    # Framework Gap: Requires Show Entries list read hook in ExplorerWorkspace.java
    Then Verify Show Entries lists only "Xyzqwrt"
    # Framework Gap: Requires per-entry matched value read hook in ExplorerWorkspace.java
    And Verify "Lipitor" is matched to "Lipitor" in the Bulk Upload results
    And Verify "Crestor" is matched to "Crestor" in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload close-without-save step in StudioSteps.java
    When User closes the Bulk Upload window without saving
    And User opens the Bulk Upload window for the "Prescriptions" filter
    # Framework Gap: Requires Browse Computer file upload step in StudioSteps.java and fixture file in uploadfiles
    And User uploads the file "prescriptions_misspelled.csv" via Browse Computer in the Bulk Upload window
    Then Verify "Lipitor" is matched to "Lipitor" in the Bulk Upload results
    # Framework Gap: Requires single-character value check in ExplorerWorkspace.java
    And Verify no single-character value "S" is shown in the Bulk Upload results
    When User saves the Bulk Upload input
    # Framework Gap: Requires left rail value read hook in ExplorerWorkspace.java
    Then Verify no single-character value "S" is shown in the left rail under "Prescriptions"
    And User saves the "HCP Explorer" workspace
    Then Verify the "HCP Explorer" Workspace is saved
    # Framework Gap: Requires browser reload and workspace reopen step in StudioSteps.java
    When User reloads the browser and reopens the saved "HCP Explorer" workspace
    Then Verify no single-character value "S" is shown in the left rail under "Prescriptions"
    And Navigate to workspace dashboard
    And User searches the workspace created to perform Actions from More menu
    And User selects the "Delete" option by clicking More Actions menu
    And Verify user is able to delete the workspace

  # Source: PROD-14817 (TC_41, TC_43, TC_44), STUD-1845
  @todo
  Scenario: Semantic search toggle switches Diagnoses bulk matching between meaning and close spelling
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Diagnoses" filter
    # Framework Gap: Requires Semantic search toggle locator in ExplorerWorkspace.java
    Then Verify the Semantic search toggle is displayed in the Bulk Upload window
    # Framework Gap: Requires Semantic search toggle step in StudioSteps.java
    When User turns "On" the Semantic search toggle in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value               |
      | high blood pressure |
    # Framework Gap: Requires per-entry matched value read hook in ExplorerWorkspace.java
    Then Verify "high blood pressure" is matched to "Essential (primary) hypertension" in the Bulk Upload results
    # Framework Gap: Requires Bulk Upload close-without-save step in StudioSteps.java
    When User closes the Bulk Upload window without saving
    And User opens the Bulk Upload window for the "Diagnoses" filter
    And User turns "Off" the Semantic search toggle in the Bulk Upload window
    And User pastes the following values into the Bulk Upload text area
      | Value        |
      | Hypertensoin |
    Then Verify "Hypertensoin" is matched to a diagnosis containing "hypertension" in the Bulk Upload results
    When User closes the Bulk Upload window without saving
    And User opens the Bulk Upload window for the "Diagnoses" filter
    And User turns "Off" the Semantic search toggle in the Bulk Upload window
    And User pastes the following values into the Bulk Upload text area
      | Value               |
      | high blood pressure |
    # Framework Gap: Requires per-entry returned value exclusion check in ExplorerWorkspace.java
    Then Verify "Essential (primary) hypertension" is not returned for "high blood pressure" in the Bulk Upload results

  # Source: PROD-14817 (TC_64)
  @todo
  Scenario: Bulk-added Profession & Specialty values persist after the workspace is saved and reloaded
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Profession & Specialty" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value                |
      | Cardiology           |
      | Internal Medicine    |
      | Foot & Ankle Surgery |
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    And User saves the Bulk Upload input
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows 3 selections under "Profession & Specialty"
    And User fetches the Identified NPI count from the workspace
    And User saves the "HCP Explorer" workspace
    Then Verify the "HCP Explorer" Workspace is saved
    # Framework Gap: Requires browser reload and workspace reopen step in StudioSteps.java
    When User reloads the browser and reopens the saved "HCP Explorer" workspace
    Then Verify the left rail shows 3 selections under "Profession & Specialty"
    # Framework Gap: Requires Identified NPI count comparison step in StudioSteps.java
    And Verify the Identified NPI count is unchanged
    And Navigate to workspace dashboard
    And User searches the workspace created to perform Actions from More menu
    And User selects the "Delete" option by clicking More Actions menu
    And Verify user is able to delete the workspace

  # Source: PROD-14817 (TC_60), HT-6371
  @todo
  Scenario: Consecutive Diagnoses file uploads add every matched value to the left rail and refresh the audience count
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    And User fetches the Identified NPI count from the workspace
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Diagnoses" filter
    # Framework Gap: Requires Browse Computer file upload step in StudioSteps.java and fixture file in uploadfiles
    And User uploads the file "diagnoses_icd10_diabetes.txt" via Browse Computer in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    And User saves the Bulk Upload input
    And User opens the Bulk Upload window for the "Diagnoses" filter
    And User uploads the file "diagnoses_back_pain.csv" via Browse Computer in the Bulk Upload window
    And User saves the Bulk Upload input
    And User clicks on Ok and closes the filter popup
    # Framework Gap: Requires left rail selection read hook in ExplorerWorkspace.java
    Then Verify the left rail shows 7 selections under "Diagnoses"
    # Framework Gap: Requires Identified NPI count comparison step in StudioSteps.java
    And Verify the Identified NPI count has changed

  # Source: PROD-14817 (TC_49), HT-6382
  @todo
  Scenario: Approaching the 6200 character filter limit shows a warning with the actual character total
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Diagnoses" filter
    # Framework Gap: Requires Browse Computer file upload step in StudioSteps.java and a fixture whose matched values total 5000 to 6199 characters
    And User uploads the file "diagnoses_near_char_limit.txt" via Browse Computer in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload save step in StudioSteps.java
    And User saves the Bulk Upload input
    # Framework Gap: Requires character limit warning read hook in ExplorerWorkspace.java
    Then Verify the warning "Approaching filter character limit" is displayed
    And Verify the character limit warning shows the limit "6200"
    # Framework Gap: Requires selected-value character total calculation in StudioSteps.java
    And Verify the warning's current total equals the character total of the selected "Diagnoses" values

  # Source: PROD-14817 (TC_70), HT-5391
  @todo
  Scenario: Prescriptions bulk upload processes every line of a 60 code NDC-11 file
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in ExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Prescriptions" filter
    # Framework Gap: Requires Browse Computer file upload step in StudioSteps.java and a fixture of 60 valid NDC-11 codes
    And User uploads the file "ndcs_60.txt" via Browse Computer in the Bulk Upload window
    # Framework Gap: Requires Bulk Upload results list read hook in ExplorerWorkspace.java
    Then Verify the Bulk Upload results list shows 60 input entries
    # Framework Gap: Requires matched and unmatched count read hook in ExplorerWorkspace.java
    And Verify the matched count plus the unmatched count equals 60
    # Framework Gap: Requires per-entry match count read hook in ExplorerWorkspace.java
    And Verify every matched entry returns exactly 1 match in the Bulk Upload results

  # Source: PROD-14817 (TC_67)
  @todo
  Scenario Outline: Bulk Upload option is <BULK_UPLOAD> for the <FILTER> filter
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "HCP Explorer" workspace
    And User selects the advertiser "Abbvie"
    And User edits the "HCP Explorer" workspace name as "HCP_Explorer"
    # Framework Gap: Requires filtering menu open step in StudioSteps.java
    When User opens the "<FILTER>" filter from the filtering menu
    # Framework Gap: Requires Bulk Upload option visibility check in ExplorerWorkspace.java
    Then Verify the Bulk Upload option is "<BULK_UPLOAD>"
    Examples:
      | FILTER                 | BULK_UPLOAD   |
      | Prescriptions          | displayed     |
      | Prescribing Behavior   | displayed     |
      | Diagnoses              | displayed     |
      | Procedures             | displayed     |
      | Profession & Specialty | displayed     |
      | State                  | displayed     |
      | Facility Name          | displayed     |
      | NPI Zip Code           | displayed     |
      | NPI DMA                | displayed     |
      | Insurance Provider     | displayed     |
      | Patient State          | displayed     |
      | Patient Facility       | displayed     |
      | NPI Age                | not displayed |
      | NPI Gender             | not displayed |
      | Graduation Year        | not displayed |
      | Net Worth              | not displayed |
      | Number of Patients     | not displayed |
      | Patient Age            | not displayed |
      | Patient Gender         | not displayed |
      | Years Practiced        | not displayed |
      | Medical School         | not displayed |
      | IAB                    | not displayed |
      | MeSH                   | not displayed |

  # Source: PROD-14817 (TC_69), HT-6382, HT-6337
  @todo
  Scenario: DTC Explorer Diagnoses bulk upload matches an ICD-10 code exactly like HCP Explorer
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on "DTC Explorer" workspace
    And User selects the advertiser "TAMTESTING ACCOUNT"
    And User edits the "DTC Explorer" workspace name as "DTC_Explorer"
    # Framework Gap: Requires Bulk Upload window open step in StudioSteps.java and Bulk Upload locators in DTCExplorerWorkspace.java
    When User opens the Bulk Upload window for the "Diagnoses" filter
    # Framework Gap: Requires Bulk Upload text area paste step in StudioSteps.java
    And User pastes the following values into the Bulk Upload text area
      | Value |
      | E119  |
    # Framework Gap: Requires per-entry match count read hook in DTCExplorerWorkspace.java
    Then Verify "E119" returns exactly 1 match in the Bulk Upload results
    # Framework Gap: Requires per-entry matched value read hook in DTCExplorerWorkspace.java
    And Verify "E119" is matched to "Type 2 diabetes mellitus without complications" in the Bulk Upload results
