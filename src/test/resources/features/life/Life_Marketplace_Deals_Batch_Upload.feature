Feature: Life Marketplace Deals Batch Upload - Verify Import Panel, Preview, Validation, Table and Filters
  1. Verify the Import Deals side panel components and template download from the Life Marketplace Deals page
  2. Verify uploading a valid template, previewing deals with Deal ID and Exchange read-only, and saving new deals
  3. Verify existing deals are overridden, and both Indirect and Direct deals are batch uploaded correctly
  4. Verify the new table columns, MPC Deal Type and Curator filters, column sorting, and copy actions
  5. Verify error states for duplicate, private, PG and exchange-change deals during import
  6. Verify partial batch save, empty and large batch handling, account scoping and targeting visibility

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to Administrative section
    And User navigates to Setup Tab
    And User navigates to Life Marketplace Deals page

  @regression
  Scenario: Verify Import Deals side panel components and download the Life Marketplace template
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    Then Verify Import Deals side panel opens with Download template component, Upload component and Preview button disabled
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    And User downloads the Life Marketplace template
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify the downloaded template is in Excel format with correct columns and sub-tabs listing acceptable values for constrained fields
    And Verify all mandatory fields marked with asterisk are present in the template

  @regression
  Scenario: Upload a valid template, preview read-only enforcement and save new deals to the table
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    Then Verify Import Deals side panel opens with Download template component, Upload component and Preview button disabled
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    And User uploads a completed Life Marketplace template with valid new deals
      | DEAL_ID   | EXCHANGE  | DEAL_NAME  | MEDIA_TYPE | CURATOR | PRICING_TYPE | MPC_DEAL_TYPE                     |
      | New_Deal_ | JW Player | Deal_Name_ | Display    | Client  | Fixed        | Deals that can run on any adgroup |
      | New_Deal_ | ADX       | Deal_Name_ | Video      | Client  | Floor        | Deals that can run on any adgroup |
      | New_Deal_ | JW Player | Deal_Name_ | Display    | Client  | Fixed        | Deals that can run on any adgroup |
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify the file state displays as Uploaded and the Preview button becomes enabled
    And Verify the uploaded file can be removed and re-uploaded
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Preview button in the Import Deals side panel
    Then Verify Preview screen displays all imported deal information with Deal ID and Exchange read-only and all other fields editable
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Save button in the Preview screen
    Then Verify toast message displays the correct count of deals added to Life Marketplace Deals
    And Verify all uploaded deals appear in the Life Marketplace Deals table with correct field values

  @regression
  Scenario: Existing Deal ID and Exchange values are overridden on save without changing the Exchange
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a completed Life Marketplace template with valid new deals
      | DEAL_ID        | EXCHANGE  | DEAL_NAME     | MEDIA_TYPE | CURATOR | PRICING_TYPE | MPC_DEAL_TYPE                     |
      | Existing_Deal_ | JW Player | Updated_Name_ | Display    | Client  | Fixed        | Deals that can run on any adgroup |
      | Existing_Deal_ | ADX       | Updated_Name_ | Video      | Client  | Floor        | Deals that can run on any adgroup |
    When User clicks Preview button in the Import Deals side panel
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify Exchange field is read-only in Preview for existing deals
    When User clicks Save button in the Preview screen
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify existing deals are updated with the new deal details and the Exchange value is unchanged

  @regression
  Scenario: Both Indirect and Direct deals are batch uploaded and reflected in SSP Backend and Ad Manager
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a completed Life Marketplace template with valid new deals
      | DEAL_ID   | EXCHANGE  | DEAL_NAME  | MEDIA_TYPE | CURATOR | PRICING_TYPE | MPC_DEAL_TYPE                     | DEAL_INTEGRATION |
      | New_Deal_ | JW Player | Deal_Name_ | Display    | Client  | Fixed        | Deals that can run on any adgroup | Indirect         |
      | New_Deal_ | ADX       | Deal_Name_ | Video      | Client  | Floor        | Deals that can run on any adgroup | Direct           |
    When User clicks Preview button in the Import Deals side panel
    And User clicks Save button in the Preview screen
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify both Indirect and Direct deals are created in the Life Marketplace Deals table
    And Verify the Exchange deals are reflected in SSP Backend and Ad Manager with the correct exchange association

  @regression
  Scenario: Multi-value Media Types and Account Assignments accept comma-separated input
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with comma-separated Media Types and Account Assignments for a deal
    When User clicks Preview button in the Import Deals side panel
    And User clicks Save button in the Preview screen
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify the deal is created with all specified Media Types and Account Assignments correctly parsed and stored

  @regression
  Scenario: New table columns are visible in the Life Marketplace Deals table after upload
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify the Life Marketplace Deals table displays columns Pricing Type, MPC Deal Type, % Margin, $ Margin Cap, Curator and Created/Last Updated Date

  @regression
  Scenario Outline: Filter the Life Marketplace Deals table by "<FILTER_NAME>" returns only matching deals
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User filters the Life Marketplace Deals table by "<FILTER_NAME>" with value "<FILTER_VALUE>"
    Then Verify the Life Marketplace Deals table shows only deals matching "<FILTER_NAME>" value "<FILTER_VALUE>"
    Examples:
      | FILTER_NAME   | FILTER_VALUE                      |
      | MPC Deal Type | Deals that can run on any adgroup |
      | Curator       | Client                            |

  @regression
  Scenario Outline: Sort the Life Marketplace Deals table on "<SORT_COLUMN>" in "<ORDER>" order
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User sorts the Life Marketplace Deals table on column "<SORT_COLUMN>" in "<ORDER>" order
    Then Verify the Life Marketplace Deals table rows are sorted correctly on "<SORT_COLUMN>" in "<ORDER>" order
    Examples:
      | SORT_COLUMN   | ORDER    |
      | Name          | A-Z      |
      | Name          | Z-A      |
      | Curator       | A-Z      |
      | MPC Deal Type | Z-A      |
      | % Margin      | Low-High |
      | % Margin      | High-Low |
      | $ Margin Cap  | Low-High |
      | $ Margin Cap  | High-Low |

  @regression
  Scenario: Deal ID exceeding 20 characters is truncated in the table and copy actions copy full values
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify a Deal ID exceeding 20 characters is visually truncated in the Life Marketplace Deals table column
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks the quick-copy action for the Deal ID in the table
    Then Verify the full Deal ID is copied to the clipboard
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Copy Deal Name from the More menu for a deal row
    Then Verify the full Deal Name is copied to the clipboard

  @regression
  Scenario: Most recently uploaded file appears at the top of the file list in the Import Deals panel
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads two files sequentially in the Import Deals side panel
    Then Verify the most recently uploaded file appears at the top of the file list in the Import Deals panel

  @regression
  Scenario: Preview button remains disabled until a file is uploaded
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    Then Verify the Preview button is disabled and cannot be clicked when no file is uploaded

  @regression
  Scenario: Remove and re-upload a different file in the Import Deals panel
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a completed Life Marketplace template with valid new deals
      | DEAL_ID   | EXCHANGE  | DEAL_NAME  | MEDIA_TYPE | CURATOR | PRICING_TYPE | MPC_DEAL_TYPE                     |
      | New_Deal_ | JW Player | Deal_Name_ | Display    | Client  | Fixed        | Deals that can run on any adgroup |
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    And User removes the uploaded file from the Import Deals side panel
    Then Verify the file is removed and the panel state resets
    And User uploads a completed Life Marketplace template with valid new deals
      | DEAL_ID   | EXCHANGE | DEAL_NAME  | MEDIA_TYPE | CURATOR | PRICING_TYPE | MPC_DEAL_TYPE                     |
      | New_Deal_ | ADX      | Deal_Name_ | Video      | Client  | Floor        | Deals that can run on any adgroup |
    Then Verify the file state displays as Uploaded and the Preview button becomes enabled
    When User clicks Preview button in the Import Deals side panel
    Then Verify the Preview screen shows the second file data

  @regression
  Scenario Outline: Deal ID with an existing "<CONFLICT_TYPE>" is flagged with an error state in Preview
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with a Deal ID that has conflict "<CONFLICT_TYPE>"
    When User clicks Preview button in the Import Deals side panel
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify the Deal ID field is highlighted red with a warning icon and the tooltip "<TOOLTIP>"
    And Verify the editable fields are disabled and the remove icon is shown for the affected deal
    Examples:
      | CONFLICT_TYPE       | TOOLTIP                                            |
      | Curated Market deal | This Deal ID already exists in the curated market. |
      | Private Deal        | This Deal ID already exists as a private deal.     |

  @regression
  Scenario: Changing the Exchange for an existing Deal ID shows an error state in Preview
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with an existing Deal ID where the Exchange is changed to a different value
    When User clicks Preview button in the Import Deals side panel
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify the Deal ID field shows the error tooltip "Exchange cannot be modified for existing Deal IDs"
    And Verify the editable fields are disabled and the remove icon is shown for the affected deal

  @regression
  Scenario: Duplicate Deal IDs within the same upload are flagged in Preview
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with two rows containing the same Deal ID
    When User clicks Preview button in the Import Deals side panel
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify both duplicate rows are flagged with a red warning icon and the tooltip "This Deal ID is a duplicate."
    And Verify the table message displays "2 deals have duplicate IDs and won't be added."
    And Verify the editable fields are disabled and the remove icon is shown for the affected deals

  @regression
  Scenario: PG deal uploaded without PG Workaround permission is flagged in Preview
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with a PG deal row as a user without PG Workaround permission
    When User clicks Preview button in the Import Deals side panel
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify the Deal ID field is highlighted red with the tooltip "PG deals cannot be added as Life Marketplace deals."
    And Verify the editable fields are disabled and the remove icon is shown for the affected deal

  @regression
  Scenario: Multiple error types in one upload show a combined table message with per-row tooltips
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with one Curated Market duplicate, one internal duplicate and one PG deal without permission
    When User clicks Preview button in the Import Deals side panel
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify the table message displays the total error count as "3 deals have errors and won't be added."
    And Verify each error row displays its specific tooltip

  @regression
  Scenario: Partial batch save adds only the valid deals and keeps error deals flagged
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with 3 valid new deals, 1 Curated Market duplicate and 1 internal duplicate
    When User clicks Preview button in the Import Deals side panel
    And User clicks Save button in the Preview screen
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify the 3 valid deals are saved and the 2 error deals remain flagged and are not added
    And Verify the toast message reflects only the count of successfully added deals

  @regression
  Scenario: Empty template with only headers is handled gracefully
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with header row only and no data rows
    Then Verify the empty upload is handled gracefully with a validation message or the Preview button remains disabled

  @regression
  Scenario: Large batch upload of 40 or more valid deals is created accurately with correct count
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with 40 or more valid new deals
    When User clicks Preview button in the Import Deals side panel
    And User clicks Save button in the Preview screen
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify all 40 or more deals are created in the Life Marketplace Deals table with an accurate count and no timeout or partial creation

  @regression
  Scenario: Deal ID existing as a Life Marketplace deal under a different Exchange is rejected
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with a Deal ID existing under Exchange B while specifying Exchange A
    When User clicks Preview button in the Import Deals side panel
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify an error is thrown, the deal is not created and the Deal ID field is highlighted with an error indicator

  @regression
  Scenario: Batch-uploaded deal scoped to one account is not accessible from another account
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a Life Marketplace template with a deal having Account Assignments set to Account A only
    When User clicks Preview button in the Import Deals side panel
    And User clicks Save button in the Preview screen
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify the batch-uploaded deal is not searchable, targetable or visible from Account B

  @regression
  Scenario: Batch-uploaded deals appear in targeting template search and the Deals tab
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    When User clicks Import Deals button on Life Marketplace Deals page
    And User uploads a completed Life Marketplace template with valid new deals
      | DEAL_ID   | EXCHANGE  | DEAL_NAME  | MEDIA_TYPE | CURATOR | PRICING_TYPE | MPC_DEAL_TYPE                     |
      | New_Deal_ | JW Player | Deal_Name_ | Display    | Client  | Fixed        | Deals that can run on any adgroup |
      | New_Deal_ | ADX       | Deal_Name_ | Video      | Client  | Floor        | Deals that can run on any adgroup |
      | New_Deal_ | JW Player | Deal_Name_ | Display    | Client  | Fixed        | Deals that can run on any adgroup |
    When User clicks Preview button in the Import Deals side panel
    And User clicks Save button in the Preview screen
    # Framework Gap: Requires step definition implementation in stepdefinitions/
    Then Verify all batch-uploaded deals appear in targeting template search by Deal ID or Name
    And Verify all batch-uploaded deals appear in the Deals tab
