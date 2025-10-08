Feature: HCP Explorer Workspace creation in Studio using filters, AI Configurator, and visualization
  1. Creation of Workspace in Studio
  2. Applying filters to the workspace by clicking Add Filter icon, building audience using AI prompts, and visualizing the audience

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"

  @regression
  Scenario Outline: Create and save HCP Explorer workspace with specific filters
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the filter and selects option
      | FilterName         | Option                                                                                                                  |
      | NPI Age            | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above                                                           |
      | NPI Gender         | Female, Male, Unknown                                                                                                   |
      | Graduation Year    | 1900-2025                                                                                                               |
      | Net Worth          | Less than $50٫000, $100٫000 to $249٫999, $250٫000 to $499٫999, $500٫000 or above                                        |
      | Number of Patients | Below 5, 6 to 20, 21 to 50, 51 to 100, 101 to 200, 201 to 300, 301 to 400, 401 to 500, 501 to 1000, 1001 or above       |
      | Reachable Audience | Yes                                                                                                                     |
      | Patient Age        | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above                                                           |
      | Patient Gender     | Female, Male, Unknown                                                                                                   |
      | Years Practiced    | Below 5, 5 to 10, 10 to 15, 15 to 20, 20 to 25, 25 to 30, 30 to 35, 35 to 40, 40 to 45, 45 to 50, 50 or Above           |
      | Facility Name      | Visiting Nurse Service, Ahs Hospital Corp, Centrastate, Robert Wood Johnson University Hospital, Montclair Hospital Llc |
      | State              | New                                                                                                                     |
      | Profession         | Physician                                                                                                               |
      | Specialty          | Foot & Ankle Surgery, Internal Medicine                                                                                 |
      | NPI List Name      | Large file test                                                                                                         |
      | Medical School     | New York College                                                                                                        |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    Examples:
      | ADVERTISER | WORKSPACE_NAME |
      | Abbvie     | Explorer       |


  @regression
  Scenario Outline: Create and save HCP Explorer workspace by building audience using AI Configurator - <AI_PROMPT>
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User clicks on AI Configurator and build audience using the AIPrompt "<AI_PROMPT>"
    Then Verify the filter is applied correctly "<PRIMARY_FILTERS>"
#    And User saves the workspace
#    Then Verify the HCP Explorer Workspace is saved
    Examples:
      | ADVERTISER | WORKSPACE_NAME | AI_PROMPT                                                                                                                                    | PRIMARY_FILTERS                                  |
      | Abbvie     | Explorer       | Select Cardiovascular Professionals who are reachable in California state and also exclude net worth Less than $50٫000                       | Net Worth, Specialty Filter, State               |
      | Abbvie     | Explorer       | Filter doctors by their gender, how long they've been practicing, and then narrow down patients by their age groups.                         | Clinical Recency, NPI Gender, Years Practiced    |
      | Abbvie     | Explorer       | Look for doctors who graduated within a specific time frame, are located in certain states, and then focus on patient gender.                | Graduation Year, Profession, State               |
      | Abbvie     | Explorer       | Find doctors within certain age ranges, with specific professions, and narrow by wealth.                                                     | NPI Age, Net Worth, Profession                   |
      | Abbvie     | Explorer       | Search for doctors from specific medical schools with third-level specialties and certain patient counts.                                    | Clinical Recency, Number of Patients, Profession |
      | Abbvie     | Explorer       | Focus on patients of specific age groups and genders.                                                                                        | Patient Age, Patient Gender                      |
      | Abbvie     | Explorer       | Filter doctors by their gender and age, then look for those who graduated from particular medical schools.                                   | NPI Age, NPI Gender, Profession                  |
      | Abbvie     | Explorer       | Doctors with specific experience, states, wealth level, and patient count.                                                                   | Clinical Recency, Profession, Years Practiced    |
      | Abbvie     | Explorer       | Select cardiovascular specialist who are graduated before 2004 in New York or California                                                     | Graduation Year, Specialty Filter, State         |
      | Abbvie     | Explorer       | Decide if you want to reach a broader audience, then focus on patients of specific age groups and genders.                                   | Clinical Recency, Patient Age, Patient Gender    |
      | Abbvie     | Explorer       | Create a mix of NPIs by choosing profession and specialties in heart and brain surgery.                                                      | Profession, Specialty Filter                     |
      | Abbvie     | Explorer       | Find doctors based on their years of experience, the states they work in, their wealth level, and how many patients they typically see.      | Clinical Recency, Profession, Years Practiced    |
      | Abbvie     | Explorer       | Pick the types of NPI lists you want, look for doctors who graduated during a specific period, and then target certain hospitals or clinics. | Graduation Year, Profession                      |


  @regression
  Scenario Outline: Create and save HCP Explorer workspace by applying filters one by one and validating NPI details are refined
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the following filters one by one and checks that NPI details are refined after each filter:
      | FilterName         | Option                                                                                                                  |
      | NPI Age            | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above                                                           |
      | NPI Gender         | Female, Male, Unknown                                                                                                   |
      | Graduation Year    | 1900-2025                                                                                                               |
      | Net Worth          | Less than $50٫000, $100٫000 to $249٫999, $250٫000 to $499٫999, $500٫000 or above                                        |
      | Number of Patients | Below 5, 6 to 20, 21 to 50, 51 to 100, 101 to 200, 201 to 300, 301 to 400, 401 to 500, 501 to 1000, 1001 or above       |
      | Reachable Audience | Yes                                                                                                                     |
      | Patient Age        | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above                                                           |
      | Patient Gender     | Female, Male, Unknown                                                                                                   |
      | Years Practiced    | Below 5, 5 to 10, 10 to 15, 15 to 20, 20 to 25, 25 to 30, 30 to 35, 35 to 40, 40 to 45, 45 to 50, 50 or Above           |
      | Facility Name      | Visiting Nurse Service, Ahs Hospital Corp, Centrastate, Robert Wood Johnson University Hospital, Montclair Hospital Llc |
      | State              | New                                                                                                                     |
      | Profession         | Physician                                                                                                               |
      | Specialty          | Foot & Ankle Surgery, Internal Medicine                                                                                 |
      | NPI List Name      | Large file test                                                                                                         |
      | Medical School     | New York College                                                                                                        |
    Examples:
      | ADVERTISER | WORKSPACE_NAME |
      | Abbvie     | Explorer       |

  @e2e @regression
  Scenario Outline: Create and save HCP Explorer workspace using NPI Cross Filters
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the filter and selects option
      | FilterName | Option                                                        |
      | NPI Age    | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above |
      | NPI Gender | Female, Male, Unknown                                         |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User hovers over the dashboard filters, selects the region with maximum NPIs and clicks on it
      | NPI Geographic Location    |
      | NPI Facilities Geography   |
      | Top 20 Market Areas        |
      | Top 20 Professions         |
      | Top 20 Specialties         |
      | Top 20 Insurance Providers |
      | Top 20 Prescriptions       |
      | Top 20 Diagnoses           |
      | Top 20 Procedures          |
      | Top 20 MeSH Categories     |
      | Top 20 IAB Categories      |
      | NPI Age Range              |
      | NPI Gender                 |
      | Patient Age Range          |
      | Patient Gender             |
      | Net Worth                  |
      | Years Practiced            |
      | Patient Distribution       |
    And Verify that dashboard filters are displayed correctly in Filter section
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    And Verify dashboard filters are merged with Primary filters
    And Fetch and verify that NPI details are refined
    Examples:
      | ADVERTISER | WORKSPACE_NAME |
      | Abbvie     | Explorer       |

  @e2e @regression
  Scenario Outline: Create and save HCP Explorer workspace by building audience using AI Configurator and applying primary filters
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the filter and selects option
      | FilterName | Option                                                        |
      | NPI Age    | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above |
      | NPI Gender | Female, Male, Unknown                                         |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And Fetch and verify that NPI details are refined
    And User clicks on AI Configurator and build audience using the AIPrompt "<AI_PROMPT>"
    Then Verify the filter is applied correctly "<PRIMARY_FILTERS>"
    And Verify after adding ai prompt filter selected manually should be overwritten
    And Fetch and verify that NPI details are refined
    And User applies the filter and selects option
      | FilterName      | Option                                                        |
      | Patient Age     | Below 25, 25 to 35, 35 to 45, 45 to 55, 55 to 65, 65 or Above |
      | Patient Gender  | Female, Male, Unknown                                         |
      | Graduation Year | 1900-2025                                                     |
    And User clicks on Ok and closes the filter popup
    And Fetch and verify that NPI details are refined
    Then Delete the filter
    And Fetch and verify that NPI details are refined
    Examples:
      | ADVERTISER | WORKSPACE_NAME | AI_PROMPT                                                                                | PRIMARY_FILTERS |
      | Abbvie     | Explorer       | Find doctors within certain age ranges, with specific professions, and narrow by wealth. | Profession      |

  @regression
  Scenario Outline: Rename HCP Explorer workspace
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the filter and selects option
      | FilterName | Option                |
      | NPI Age    | Below 25, 25 to 35,   |
      | NPI Gender | Female, Male, Unknown |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    And Navigate to workspace dashboard and search the workspace created
    And User selects the Rename option by clicking More Actions button
    And Verify user is able to rename the workspace as "<NEW_WORKSPACE_NAME>"
    And User is able to search the workspace with the new name
    Examples:
      | ADVERTISER | WORKSPACE_NAME | NEW_WORKSPACE_NAME |
      | Abbvie     | Explorer       | New_Explorer       |

  @regression
  Scenario Outline: Delete HCP Explorer workspace
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the filter and selects option
      | FilterName | Option                |
      | NPI Age    | Below 25, 25 to 35,   |
      | NPI Gender | Female, Male, Unknown |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    And Navigate to workspace dashboard and search the workspace created
    And User selects the Delete option by clicking More Actions button
    And Verify user is able to delete the workspace
    Examples:
      | ADVERTISER | WORKSPACE_NAME |
      | Abbvie     | Explorer       |

  @regression
  Scenario Outline: Duplicate HCP Explorer workspace
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on HCP Explorer workspace
    And User adds the workspace name as "<WORKSPACE_NAME>" and selects the advertiser "<ADVERTISER>"
    And User applies the filter and selects option
      | FilterName | Option                |
      | NPI Age    | Below 25, 25 to 35,   |
      | NPI Gender | Female, Male, Unknown |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    And Navigate to workspace dashboard and search the workspace created
    And User selects the Duplicate option by clicking More Actions button
    And Verify user is able to duplicate the workspace
    And User is able to search the workspace with the new name
    Examples:
      | ADVERTISER | WORKSPACE_NAME |
      | Abbvie     | Explorer       |