Feature: Create and Publish HCP Explorer Workspace in Studio and Verify in LIFE
  1. Creation of Workspace in Studio
  2. Publish the workspace as Studio List (Static List or Live List)
  3. Verify the published Studio list in LIFE

  @e2e @regression
  Scenario Outline: Create and save HCP Explorer workspace with specific filters
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
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
      | NPI List Name      | HCP                                                                                                                     |
      | Medical School     | New York College                                                                                                        |
    And User clicks on Ok and closes the filter popup
    Then Verify that the applied filters are displayed correctly
    And User saves the workspace
    Then Verify the HCP Explorer Workspace is saved
    And Download button is enabled to the user
    And User clicks on Publish NPI List
    And User selects publish "<LIST_TYPE>"
    And User select the system to publish the list
    Then Verify list is published
    And User navigates to NPI Lists page in LIFE
    And User searches the workspace in LIFE and selects it
    And User clicks on the published workspace
    Then User Verify the list is displayed in the Life
    Examples:
      | ADVERTISER | WORKSPACE_NAME | LIST_TYPE |
      | Abbvie     | Explorer       | Static    |


  @e2e2 @regression
  Scenario Outline: Create and save HCP Explorer workspace using UI Configurator
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User applies each of the following filters to new HCP Explorer workspaces with advertiser "<ADVERTISER>", workspace prefix "<WORKSPACE_NAME>", and list type "<LIST_TYPE>":
      | Filter doctors by their gender, how long they've been practicing, and then narrow down patients by their age groups.          |
      | Look for doctors who graduated within a specific time frame, are located in certain states, and then focus on patient gender. |
      | Find doctors within certain age ranges, with specific professions, and narrow by wealth.                                      |
      | Search for doctors from specific medical schools with third-level specialties and certain patient counts.                     |
      | Focus on patients of specific age groups and genders.                                                                         |
      | Filter doctors by their gender and age, then filter by medical schools.                                                       |
      | Doctors with specific experience, states, wealth level, and patient count.                                                    |
      | Doctors by graduation period, then target hospitals or clinics.                                                               |
    Then Each filter is applied, workspace is saved, list is published, and verified in LIFE
    Examples:
      | ADVERTISER | WORKSPACE_NAME | LIST_TYPE |
      | Abbvie     | Explorer       | Static    |