Feature: Life Marketplace Deals Batch Upload - Verify Import, Validation, Table Management, and Deal Discoverability
  1. Verify the Import Deals side panel: template download, upload, preview, and save
  2. Verify new and existing Life Marketplace deals are created and updated via batch upload
  3. Verify per-deal validation error states, aggregate messaging, and partial batch handling
  4. Verify the Life Marketplace Deals table columns, filters, sorting, and row quick actions
  5. Verify account scoping and deal discoverability after batch upload

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to Administrative section
    And User navigates to Setup Tab
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    And User navigates to Life Marketplace Deals page

  # Source: TC_ET-25052_01, TC_ET-25052_02, TC_ET-25052_03, TC_ET-25052_04, TC_ET-25052_05, TC_ET-25052_17, TC_ET-25052_24, TC_ET-25052_25
  @todo @regression
  Scenario: Import Deals happy path - open panel, download template, upload, preview, and save new deals
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And Verify Download template component is at the top and Upload component is below it
    And Verify Preview button is disabled before any file is uploaded
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    And User downloads the Life Marketplace Deals template
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    And Verify the template contains all mandatory fields marked with "*" and sub-tabs of acceptable values
    And User fills the template with deal details and uploads the template
      | DEAL_NAME | EXCHANGE  | MEDIA_TYPE | CURATOR | DEAL_PRICE | PRICING_TYPE | MPC_DEAL_TYPE                     |
      | LMP_Deal  | JW Player | Display    | Client  | 230        | Fixed        | Deals that can run on any adgroup |
      | LMP_Deal  | ADX       | Video      | Client  | 123        | Floor        | Deals that can run on any adgroup |
      | LMP_Deal  | Pubmatic  | Display    | Client  | 321        | Fixed        | Deals that can run on any adgroup |
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    Then Verify the file state displays as "Uploaded" and the Preview button becomes enabled
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    And Verify the most recently uploaded file appears at the top of the file list
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    When User removes the uploaded file and re-uploads the template
    Then Verify the previous file is cleared and the re-uploaded file state displays as "Uploaded"
    When User clicks Preview button
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    Then Verify all imported deal fields are editable except "Deal ID" and "Exchange" which are read-only
    When User clicks Save button
    # Framework Gap: Requires step definition implementation in LifeSteps.java (regression: HT-5039 count, HT-5142 visibility)
    Then Verify a toast message displays the count of deals added to Life Marketplace Deals
    And Verify all 3 uploaded deals appear in the Life Marketplace Deals table with correct field values

  # Source: TC_ET-25052_06
  @todo @regression
  Scenario: Existing Deal ID and Exchange are overridden on save while Exchange stays read-only
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And User fills the template with deal details and uploads the template
      | DEAL_NAME     | EXCHANGE  | MEDIA_TYPE | CURATOR | DEAL_PRICE | PRICING_TYPE | MPC_DEAL_TYPE                     |
      | LMP_Existing1 | JW Player | Display    | Client  | 250        | Fixed        | Deals that can run on any adgroup |
      | LMP_Existing2 | ADX       | Video      | Client  | 140        | Floor        | Deals that can run on any adgroup |
    When User clicks Preview button
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    Then Verify the "Exchange" field is read-only for existing deals in the Preview screen
    When User clicks Save button
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    Then Verify the existing deals are updated with the new details and the Exchange value is unchanged

  # Source: TC_ET-25052_07
  @todo @regression
  Scenario Outline: Batch uploaded "<DEAL_HANDLING>" deal is created and reflected in SSP Backend and Ad Manager
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And User fills the template with deal details and uploads the template
      | DEAL_NAME | EXCHANGE   | MEDIA_TYPE | CURATOR | DEAL_PRICE | PRICING_TYPE | DEAL_HANDLING   |
      | LMP_Deal  | <EXCHANGE> | Display    | Client  | 230        | Fixed        | <DEAL_HANDLING> |
    When User clicks Preview button
    And User clicks Save button
    # Framework Gap: Requires step definition implementation in LifeSteps.java (per ET-19617 Indirect/Direct precedent - GAP-4)
    Then Verify the "<DEAL_HANDLING>" deal is created in the Life Marketplace Deals table
    # Framework Gap: Requires cross-system verification hooks (SSP Backend / Ad Manager) in LifeSteps.java
    And Verify the "<DEAL_HANDLING>" Exchange deal is reflected in the SSP Backend and Ad Manager
    Examples:
      | DEAL_HANDLING | EXCHANGE  |
      | Indirect      | JW Player |
      | Direct        | ADX       |

  # Source: TC_ET-25052_08
  @todo @regression
  Scenario: Multi-value Media Types and Account Assignments are parsed from comma-separated cells
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And User fills the template with deal details and uploads the template
      | DEAL_NAME     | EXCHANGE  | MEDIA_TYPE                 | CURATOR | DEAL_PRICE | PRICING_TYPE | ACCOUNT_ASSIGNMENTS                    |
      | LMP_MultiDeal | JW Player | Display (All), Video (All) | Client  | 230        | Fixed        | automation@pulsepoint, secondary@pulse |
    When User clicks Preview button
    And User clicks Save button
    # Framework Gap: Requires step definition implementation in LifeSteps.java (GAP-5: invalid item in list unconfirmed)
    Then Verify the deal is created with all specified Media Types and Account Assignments correctly parsed and stored

  # Source: TC_ET-25052_09, TC_ET-25052_12, TC_ET-25052_13, TC_ET-25052_14
  @todo @regression
  Scenario: Life Marketplace Deals table shows new columns and supports row quick actions
    # Framework Gap: Requires step definition implementation in LifeSteps.java (regression: HT-4010 % Margin - GAP-2)
    Then Verify the table displays columns "Pricing Type", "MPC Deal Type", "% Margin", "$ Margin Cap", "Curator" and "Created/Last Updated Date"
    # Framework Gap: Requires step definition implementation in LifeSteps.java (AMB-2)
    And Verify a Deal ID exceeding 20 characters is visually truncated in the table column
    # Framework Gap: Requires step definition implementation in LifeSteps.java (AMB-2)
    When User copies the Deal ID using the quick-copy action in the table
    Then Verify the full Deal ID is copied to the clipboard and not the truncated display value
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    When User copies the Deal Name using the More menu in the table
    Then Verify the Deal Name is copied to the clipboard

  # Source: TC_ET-25052_10, TC_ET-25052_11
  @todo @regression
  Scenario Outline: Filtering the Life Marketplace Deals table by "<FILTER>" returns only matching deals
    # Framework Gap: Requires step definition implementation in LifeSteps.java (regression: HT-4357)
    When User filters the Life Marketplace Deals table by "<FILTER>" with value "<VALUE>"
    Then Verify the table shows only deals matching the selected "<FILTER>"
    Examples:
      | FILTER        | VALUE                             |
      | MPC Deal Type | Deals that can run on any adgroup |
      | Curator       | Client                            |

  # Source: TC_ET-25052_15, TC_ET-25052_16
  @todo @regression
  Scenario Outline: Sorting the Life Marketplace Deals table by "<COLUMN>" orders rows "<ORDER>"
    # Framework Gap: Requires step definition implementation in LifeSteps.java (regression: HT-4010 for numeric)
    When User sorts the Life Marketplace Deals table by "<COLUMN>" in "<ORDER>" order
    Then Verify all rows are correctly ordered by "<COLUMN>" in "<ORDER>" order
    Examples:
      | COLUMN        | ORDER    |
      | Name          | A-Z      |
      | Name          | Z-A      |
      | MPC Deal Type | A-Z      |
      | % Margin      | Low-High |
      | % Margin      | High-Low |
      | $ Margin Cap  | Low-High |
      | $ Margin Cap  | High-Low |

  # Source: TC_ET-25052_18, TC_ET-25052_19, TC_ET-25052_20, TC_ET-25052_21, TC_ET-25052_22, TC_ET-25052_29
  @todo @regression
  Scenario Outline: Per-deal upload validation flags "<ERROR_CASE>" with the correct error state
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And User uploads a template containing a deal in the "<ERROR_CASE>" condition
    When User clicks Preview button
    # Framework Gap: Requires step definition implementation in LifeSteps.java (AMB-3: confirm tooltip wording vs Figma)
    Then Verify the Deal ID field is highlighted with a warning icon and the tooltip reads "<TOOLTIP>"
    And Verify the editable fields are disabled and the remove icon is shown for the affected deal
    Examples:
      | ERROR_CASE                                               | TOOLTIP                                             |
      | exists in Curated Market                                 | This Deal ID already exists in the curated market.  |
      | exists as a Private Deal                                 | This Deal ID already exists as a private deal.      |
      | existing Deal ID with changed Exchange                   | Exchange cannot be modified for existing Deal IDs   |
      | duplicate within the same upload                         | This Deal ID is a duplicate.                        |
      | PG deal without PG Workaround permission                 | PG deals cannot be added as Life Marketplace deals. |
      | existing Life Marketplace deal under a different Exchange | This Deal ID already exists under a different exchange. |

  # Source: TC_ET-25052_23
  @todo @regression
  Scenario: Multiple error types in one upload show a combined aggregate table message
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And User uploads a template with 1 Curated Market duplicate, 1 internal duplicate and 1 PG deal without permission
    When User clicks Preview button
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    Then Verify the table message reads "3 deals have errors and won't be added."
    And Verify each error row displays its own specific tooltip

  # Source: TC_ET-25052_26
  @todo @regression
  Scenario: Partial batch saves only the valid deals and holds the error deals
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And User uploads a template with 3 valid deals, 1 Curated Market duplicate and 1 internal duplicate
    When User clicks Preview button
    And User clicks Save button
    # Framework Gap: Requires step definition implementation in LifeSteps.java (GAP-1: confirm partial-success behavior with BA/Dev)
    Then Verify the 3 valid deals are saved and appear in the Life Marketplace Deals table
    And Verify the 2 error deals remain flagged and are not added
    And Verify the toast message reflects only the count of successfully added deals

  # Source: TC_ET-25052_27
  @todo @regression
  Scenario: Empty template upload is handled gracefully
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And User uploads the Life Marketplace Deals template containing headers only with no data rows
    # Framework Gap: Requires step definition implementation in LifeSteps.java
    Then Verify a validation message is shown or the Preview button remains disabled

  # Source: TC_ET-25052_28
  @todo @regression
  Scenario: Large batch of 40+ deals uploads accurately
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And User uploads a template containing "40" valid new deals
    When User clicks Preview button
    And User clicks Save button
    # Framework Gap: Requires step definition implementation in LifeSteps.java (regression: HT-5039 count accuracy)
    Then Verify all "40" deals are created in the table with an accurate count and no partial creation

  # Source: TC_ET-25052_30
  @todo @regression
  Scenario: Account assignment scoping isolates a batch-uploaded deal from other accounts
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And User uploads a template with a deal whose Account Assignments is set to "Account A" only
    When User clicks Preview button
    And User clicks Save button
    # Framework Gap: Requires step definition implementation in LifeSteps.java (regression: HT-4020 account isolation)
    Then Verify the deal is not searchable, targetable, or visible from "Account B"

  # Source: TC_ET-25052_31
  @todo @regression
  Scenario: Batch-uploaded deals are discoverable in targeting-template search and the Deals tab
    When User clicks Import Deals button
    Then Verify Import Deal panel is opened
    And User uploads a template containing "3" valid new deals
    When User clicks Preview button
    And User clicks Save button
    # Framework Gap: Requires step definition implementation in LifeSteps.java (regression: HT-5142 discoverability)
    Then Verify all "3" deals appear in the targeting template search by Deal ID or Name
    And Verify all "3" deals appear in the Deals tab
