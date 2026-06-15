Feature: Medscape List Match + Consent Workspace in Studio Application
  1.Verify the workspace creation for Medscape List Match + Consent in Studio Application
  2. Verify the saved & published workspace, Explore tab for the valid data
  3. Verify workspace displayed on Dashboard management page with valid data

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "PP engineering test" and checks Studio permissions
    And User clicks PulsePoint icon to navigate back to Life
    And User navigates to Studio application

  Scenario Outline: Create and save Medscape List Match + Consent workspace to verify error msg for mandatory  fields
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on Medscape List Match + Consent
    And User selects the advertiser "<ADVERTISER>"
    And Verify the Workspace is created
    Then User clicks the Save button without selecting values in any fields
    And Verify the in-line error messages are displayed for mandatory fields i.e Source NPI List, Business, Business Vertical, Product
    Then User selects values in each fields as "<Source NPI List>", "<Business>", "<Business Vertical>", "<Product>", "<Pharma>" & "<Brand>"
    And Verify user is able to select multiple brands options
    And Verify user is able to deselect the multiple selected brand options
    And  Verify the in-line error messages is displayed for Brand field
    And User clicks "Select Columns with Required Attributes" & deselects NPI
    And Verify the error displayed as "Please map the following required attributes before submitting: NPI_NUMBER, CUSTOMER_ID"
    Examples:
      | ADVERTISER | Source NPI List                   | Business     | Business Vertical | Product     | Pharma               | Brand                                                     |
      | Medscape   | ND_Prod_Opdivo_11May [ID: #92568] | Medscape (7) | Sponsorship (6)   | MSITE_TOPIC | Bristol-Myers Squibb | GlaxoSmithKline_Global,GSK Anoro Sample Email Suppression |

  Scenario Outline: Create and save Medscape List Match + Consent workspace without Deliverable ID and navigates to Explore Tab
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on Medscape List Match + Consent
    And User selects the advertiser "<ADVERTISER>"
    And Verify the Workspace is created
    Then User selects values in each fields as "<Source NPI List>", "<Business>", "<Business Vertical>", "<Product>", "<Pharma>", "<Brand>" and "<State Exclusion>"
    And User clicks "Select Columns with Required Attributes" & deselects NPI
    And User maps fields as "<Customer ID field>","<Value>" "<NPI_ID>", "<Postal_Code/ZIPCODE>"
    And User updates the workspace name as "<WORKSPACE_NAME>"
    And User saves the workspace
    And Verify the Workspace is saved
    And Verify the Publish button is disabled
    And Verify system navigate on "Explore" tab from "Mapping" tab
    Then Verify the workspace is visible in workspace management page
    And Workspace status is updated & Workspace Definition is displayed as per selected fields
    Examples:
      | ADVERTISER | Source NPI List                      | Business     | Business Vertical | Product           | Pharma                 | Brand                  | State Exclusion | Customer ID field | Value       | NPI_ID     | Postal_Code/ZIPCODE |
      | Medscape   | ND_Prod_Opdivo_11May [ID: #92568]    | Medscape (7) | Sponsorship (6)   | MSITE_TOPIC       | Bristol-Myers Squibb   | BMS Email Suppression  |                 | Compass_ID        | CUSTOMER_ID | NPI_NUMBER | ZIP                 |
      | Medscape   | ND_GSK_LIST_14Apr [ID: #89890]       | MD/alert (3) | Sponsorship (19)  | MD/Alert e-Alerts | GlaxoSmithKline_Global | GlaxoSmithKline_Global | Colorado        | RECORD_ID         | CUSTOMER_ID | NPI_NUMBER |                     |
      | Medscape   | ND_Prod_Eli Lilly_11May [ID: #92567] | MD/alert (3) | Sponsorship (19)  | MD/Alert e-Alerts |                        |                        |                 | RECORD_ID         | CUSTOMER_ID | NPI_NUMBER |                     |

  Scenario Outline: Create and save Medscape List Match + Consent workspace and publish the workspace with unique SF Deliverable ID
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on Medscape List Match + Consent
    And User selects the advertiser "<ADVERTISER>"
    And Verify the Workspace is created
    Then User selects values in each fields as "<Source NPI List>","<Deliverable ID>", "<Business>", "<Business Vertical>", "<Product>", "<Pharma>", "<Brand>" and "<State Exclusion>"
    And Verify for valid Deliverable ID, suggestion is displayed below Pharma & brand
    And User clicks "Select Columns with Required Attributes" & deselects NPI
    And User maps fields as "<Customer ID field>","<Value>" "<NPI_ID>", "<Postal_Code/ZIPCODE>"
    And User updates the workspace name as "<WORKSPACE_NAME>"
    And User saves the workspace
    And Verify the Workspace is saved
    And Verify system navigate on "Explore" tab from "Mapping" tab
    Then User clicks on Publish or download NPI List button
    And User selects Push to Artemis option
    And User selects Push to Artemis button
    And Verify workspace is published successfully only with unique SF Deliverable ID
    And Verify workspace is in Read only mode once published successfully
    Then Verify the workspace is visible in workspace management page
    And Workspace status is updated & Workspace Definition is displayed as per selected fields
    Examples:
      | ADVERTISER | Source NPI List                      | Deliverable ID | Business     | Business Vertical | Product           | Pharma                 | Brand                  | State Exclusion | Customer ID field | Value       | NPI_ID     | Postal_Code/ZIPCODE |
      | Medscape   | ND_Prod_Opdivo_11May [ID: #92568]    | 338482.141     | Medscape (7) | Sponsorship (6)   | MSITE_TOPIC       | Bristol-Myers Squibb   | BMS Email Suppression  |                 | Compass_ID        | CUSTOMER_ID | NPI_NUMBER | ZIP                 |
      | Medscape   | ND_GSK_LIST_14Apr [ID: #89890]       | 338482.141     | MD/alert (3) | Sponsorship (19)  | MD/Alert e-Alerts | GlaxoSmithKline_Global | GlaxoSmithKline_Global | Colorado        | RECORD_ID         | CUSTOMER_ID | NPI_NUMBER |                     |
      | Medscape   | ND_Prod_Eli Lilly_11May [ID: #92567] | 338482.141     | MD/alert (3) | Sponsorship (19)  | MD/Alert e-Alerts |                        |                        |                 | RECORD_ID         | CUSTOMER_ID | NPI_NUMBER |                     |

  Scenario Outline: Create Medscape List Match + Consent workspace and Validate record counts displayed on Explore tab on saving & publishing the workspace
    When User clicks on Create New Workspace
    Then User sees the types of workspaces they have permissions for
    And User clicks on Medscape List Match + Consent
    And User selects the advertiser "<ADVERTISER>"
    And Verify the Workspace is created
    Then User selects values in each fields as "<Source NPI List>","<Deliverable ID>", "<Business>", "<Business Vertical>", "<Product>", "<Pharma>", "<Brand>" and "<State Exclusion>"
    And And User clicks "Select Columns with Required Attributes" & deselects NPI
    And User maps fields as "<Customer ID field>","<Value>" "<NPI_ID>", "<Postal_Code/ZIPCODE>"
    And User updates the workspace name as "<WORKSPACE_NAME>"
    And User saves the workspace
    And Verify the Workspace is saved
    And Verify the values displayed on Explore tab
    Then User clicks on Publish or download NPI List button
    And User selects Push to Artemis option
    And User selects Push to Artemis button
    And Verify workspace is published successfully only with unique SF Deliverable ID
    And Verify the values displayed on Explore tab
    Then Verify the HCP Explorer Workspace is saved
    Examples:
      | ADVERTISER | Source NPI List                      | Deliverable ID | Business     | Business Vertical | Product           | Pharma                 | Brand                  | State Exclusion | Customer ID field | Value       | NPI_ID     | Postal_Code/ZIPCODE |
      | Medscape   | ND_Prod_Opdivo_11May [ID: #92568]    | 338482.141     | Medscape (7) | Sponsorship (6)   | MSITE_TOPIC       |                        |                        |                 | Compass_ID        | CUSTOMER_ID | NPI_NUMBER |                     |
      | Medscape   | ND_Prod_Opdivo_11May [ID: #92568]    | 338482.141     | Medscape (7) | Sponsorship (6)   | MSITE_TOPIC       |                        |                        | Colorado        | Compass_ID        | CUSTOMER_ID | NPI_NUMBER | ZIP                 |
      | Medscape   | ND_Prod_Opdivo_11May [ID: #92568]    | 338482.141     | Medscape (7) | Sponsorship (6)   | MSITE_TOPIC       |                        |                        | Colorado        | Compass_ID        | CUSTOMER_ID | NPI_NUMBER |                     |
      | Medscape   | ND_Prod_Opdivo_11May [ID: #92568]    | 338482.141     | Medscape (7) | Sponsorship (6)   | MSITE_TOPIC       | Bristol-Myers Squibb   | BMS Email Suppression  | Colorado        | Compass_ID        | CUSTOMER_ID | NPI_NUMBER | ZIP                 |
      | Medscape   | ND_Prod_Eli Lilly_11May [ID: #92567] | 338482.141     | MD/alert (3) | Sponsorship (19)  | MD/Alert e-Alerts |                        |                        |                 | RECORD_ID         | CUSTOMER_ID | NPI_NUMBER |                     |
      | Medscape   | ND_GSK_LIST_14Apr [ID: #89890]       | 338482.141     | MD/alert (3) | Sponsorship (19)  | MD/Alert e-Alerts |                        |                        |                 | RECORD_ID         | CUSTOMER_ID | NPI_NUMBER |                     |
      | Medscape   | ND_GSK_LIST_14Apr [ID: #89890]       | 338482.141     | MD/alert (3) | Sponsorship (19)  | MD/Alert e-Alerts | GlaxoSmithKline_Global | GlaxoSmithKline_Global |                 | RECORD_ID         | CUSTOMER_ID | NPI_NUMBER |                     |


