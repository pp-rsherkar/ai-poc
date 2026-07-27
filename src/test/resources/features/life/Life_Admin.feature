Feature: LIFE Regression - Admin Setup: permissions, Life Marketplace Deals, Animation Upload, and deprecated feature cleanup
  Covers the Admin > Setup surfaces added or changed in the August-2026-portal release: Omnichannel minimum-size permissions, the Animation Upload UI, the Life Marketplace Deals (formerly Health Markets) deal-management view, and the retirement of the Creative Name Bid Multiplier and Line Item Creative Separation permissions.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Administrative section

  @todo
  # Source: ET-24726, GAP-2
  Scenario Outline: Omnichannel minimum audience size visibility and edit rights follow the granted permission
    # Framework Gap: Requires step definitions for the Omnichannel minimum-size view/edit permission pair in LifeSteps.java
    Given User is granted the following Omnichannel permission state: "<PERMISSION_STATE>"
    When User opens the Omnichannel Audiences minimum size settings at Global and Account level
    Then "<VISIBILITY_CHECK>"
    Examples:
      | PERMISSION_STATE                        | VISIBILITY_CHECK                                                                                                     |
      | User holds view permission only         | Minimum sizes for each platform display at Global and Account scope, read-only with save controls disabled or absent |
      | User holds view and edit permissions    | Minimum sizes display and a platform minimum can be changed and persists on reload                                   |
      | User holds neither permission           | Minimum-size fields/section are hidden and no minimum values are exposed anywhere in the UI                          |
      | User holds edit but not view permission | Resulting UI state is defined and consistent per the resolved GAP-2 rule and recorded                                |

  @todo
  # Source: ET-24726, AMB-1
  Scenario: A newly configured platform receives the minimum-size default
    Given User is granted the Omnichannel minimum-size edit permission
    When User opens minimum size settings for a platform without an explicit minimum configured
    Then The platform shows the defined minimum-size default rather than blank or zero

  @todo
  # Source: ET-24726, GAP-1
  Scenario Outline: Global and Account-level minimum sizes are enforced per the defined precedence rule
    Given User is granted the Omnichannel minimum-size edit permission
    When User sets a Global minimum of "<GLOBAL_MIN>" and an Account minimum of "<ACCOUNT_MIN>" for platform "Meta"
    And User attempts to build an omnichannel audience below the effective minimum for platform "Meta"
    Then "<EXPECTED_GOVERNING_VALUE>" and audience creation/push is blocked or warned accordingly
    Examples:
      | GLOBAL_MIN | ACCOUNT_MIN | EXPECTED_GOVERNING_VALUE                                                                                |
      | 50000      | (none)      | Global minimum of 50000 applies to accounts without an account-level override                           |
      | 50000      | 75000       | The defined precedence rule (account overrides global, or the stricter value) governs audience creation |

  @todo
  # Source: ET-24726, GAP-3
  Scenario: Editing a minimum size below the allowed floor is rejected
    Given User is granted the Omnichannel minimum-size edit permission
    When User attempts to save a platform minimum size of "0"
    Then The value is rejected or clamped per the defined floor with a validation message and is not persisted

  @todo
  # Source: ET-24726
  Scenario: Minimum size changes persist correctly and revoking view removes visibility immediately
    Given User is granted the Omnichannel minimum-size edit permission
    When User edits the Account-level minimum for platform "LinkedIn", reloads, switches account and switches back
    Then The saved minimum is stable and scoped to the correct account with no cross-account leakage
    When The Omnichannel minimum-size view permission is revoked for the user
    And User reloads the minimum size settings page
    Then Minimum sizes are no longer visible to that user
    When A user without the edit permission attempts a direct save action on the minimum size field
    Then The change is rejected server-side and the minimum is unchanged

  @todo
  # Source: ET-24716
  Scenario: Uploading a single valid SVG loader shows the preview and only one loader may exist at a time
    # Framework Gap: Requires step definitions for the Animation Upload UI in Admin > Life Permissions in LifeSteps.java
    Given User is granted the "Animation Uploader UI" permission
    When User navigates to the Animation Upload UI
    And User uploads a well-formed SVG loader "milkshake_loader_v1.svg"
    Then An Upload Complete state renders the loader preview and it is listed as the single uploaded item
    When User attempts to upload a second SVG loader "milkshake_loader_v2.svg" without removing the first
    Then The UI prevents a concurrent second loader until the existing one is removed

  @todo
  # Source: ET-24716, GAP-2, GAP-3
  Scenario Outline: Non-SVG, oversized, and unsafe SVG uploads are rejected or sanitized
    Given User is granted the "Animation Uploader UI" permission
    When User navigates to the Animation Upload UI
    # Framework Gap: Requires step definitions for SVG file-type, size, and script-sanitization validation in LifeSteps.java
    And User attempts to upload "<INVALID_INPUT>"
    Then "<EXPECTED_OUTCOME>"
    Examples:
      | INVALID_INPUT                                                             | EXPECTED_OUTCOME                                                                                                  |
      | loader_holiday.png                                                        | Upload rejected with a message stating SVG is the only supported type                                             |
      | loader_holiday.gif                                                        | Upload rejected with a message stating SVG is the only supported type                                             |
      | loader_config.json                                                        | Upload rejected with a message stating SVG is the only supported type                                             |
      | loader_with_script.svg containing a <script> element and an external href | Upload is rejected or the active content is sanitized so no script executes on render                             |
      | loader_oversized.svg far larger than a typical loader                     | Upload is rejected or constrained per a defined limit with a clear message; the UI does not hang or render broken |

  @todo
  # Source: ET-24716
  Scenario: A fresh upload defaults to disabled and removing a loader allows a new one to replace it
    Given User is granted the "Animation Uploader UI" permission
    When User navigates to the Animation Upload UI
    And User uploads a well-formed SVG loader "milkshake_loader_v1.svg"
    Then The Enable/Disable toggle is disabled by default immediately after upload completes
    When User clicks Remove on the uploaded loader
    And User uploads a different SVG loader "milkshake_loader_v3.svg"
    Then The first loader is removed and the second SVG uploads successfully in its place

  @todo
  # Source: ET-24716
  Scenario: Upload progress, failure, and retry states behave correctly
    Given User is granted the "Animation Uploader UI" permission
    When User navigates to the Animation Upload UI
    And User begins uploading SVG loader "milkshake_loader_v4.svg"
    Then A loading indicator is shown during upload and the Enable/Disable toggle remains disabled
    When The upload of "milkshake_loader_v4.svg" is interrupted and fails
    Then An error state appears with a message and both Retry and Remove options are available
    When User clicks Retry with a valid SVG loader "milkshake_loader_v4.svg"
    Then Retry re-initiates the upload and completes to the preview state

  @todo
  # Source: ET-24716
  Scenario Outline: Loader scheduling accepts future dates, blocks past dates, and activates on the scheduled date
    Given User is granted the "Animation Uploader UI" permission
    When User navigates to the Animation Upload UI
    And User uploads a well-formed SVG loader "milkshake_loader_v1.svg"
    # Framework Gap: Requires step definitions for the date-scheduling picker and activation engine in LifeSteps.java
    And User schedules the loader for "<SCHEDULED_DATE>" reflecting the "<SCHEDULE_CASE>" case
    Then "<EXPECTED_RESULT>"
    Examples:
      | SCHEDULE_CASE                       | SCHEDULED_DATE  | EXPECTED_RESULT                                                                             |
      | Future date accepted                | 25/12/2026      | The future date is accepted and the loader is marked scheduled for that date                |
      | Past date disabled                  | 01/01/2020      | Past dates are disabled/unselectable in the date-time picker                                |
      | Scheduled date reached, auto-enable | reached (today) | The loader auto-enables and plays continuously until a newer scheduled loader supersedes it |
      | Not-yet-reached stays disabled      | 31/12/2026      | The loader stays disabled and does not play until its scheduled date is reached             |

  @todo
  # Source: ET-24716, GAP-1
  Scenario: A newer-dated scheduled loader supersedes the active one and same-date scheduling resolves deterministically
    Given User is granted the "Animation Uploader UI" permission
    When User uploads and schedules Loader "milkshake_loader_A.svg" for "25/12/2026" (active)
    And User uploads and schedules Loader "milkshake_loader_B.svg" for "01/01/2027"
    And The current date advances past "01/01/2027"
    Then Loader "milkshake_loader_B.svg" automatically becomes the active loader in place of "milkshake_loader_A.svg"
    # Framework Gap: Requires step definition for the same-date tie-break rule in LifeSteps.java
    When Loader "milkshake_loader_C.svg" and Loader "milkshake_loader_D.svg" are both scheduled for "15/03/2027"
    Then A single active loader is chosen by a deterministic, documented tie-break consistent across reloads

  @todo
  # Source: ET-24716
  Scenario: Animation Upload UI access is gated by the Animation Uploader UI permission
    Given User is granted the "Animation Uploader UI" permission
    When User navigates to the Animation Upload UI
    Then The Animation Upload UI is visible and usable
    Given User is not granted the "Animation Uploader UI" permission
    When User attempts to navigate to the Animation Upload UI
    Then The Animation Upload UI is not accessible or visible

  @todo
  # Source: ET-24716
  # Regression anchor: comment thread (pmahalingam/Victor Onazi) - Play/Pause dropped as infeasible for native SVG
  Scenario: An uploaded SVG loader auto-plays natively with no Play or Pause control rendered
    Given User is granted the "Animation Uploader UI" permission
    When User uploads a well-formed SVG loader "milkshake_loader_v1.svg" and the upload completes
    Then The loader animation plays automatically using native SVG behavior and no Play or Pause control is rendered

  @todo
  # Source: ET-24715
  # Regression anchor: HT-6123 - Creatives tab column defect (creative-admin surface)
  Scenario: Creative Name Bid Multiplier permission is removed from the Life Features table and remains unreachable
    When User navigates to the Life Features table in Admin
    Then The "Creative Name BM" permission row is absent from the Features table and cannot be granted
    # Framework Gap: Requires step definition asserting the deprecated bid-multiplier surface is unreachable in LifeSteps.java
    And The Creative Name Bid Multiplier capability it gated is no longer reachable in the Portal
    # Framework Gap: Requires step definition for pre-existing configured Creative Name Bid Multiplier values in LifeSteps.java
    And A creative that previously had the Creative Name Bid Multiplier configured is handled predictably with no error
    And An account that previously held the permission sees it removed with no error or broken Features table
    And All other permissions in the Life Features table still render and can be toggled

  @todo
  # Source: ET-24713
  # Regression anchor: HT-6123 - Creatives tab column defect (creative-admin surface)
  Scenario: Line Item Creative Separation permission is removed from the Life Features table and remains unreachable
    When User navigates to the Life Features table in Admin
    Then The "Line Item Creative Separation" permission row is absent from the Features table and cannot be granted
    # Framework Gap: Requires step definition asserting the deprecated creative-separation surface is unreachable in LifeSteps.java
    And The Line Item Creative Separation capability it gated is no longer reachable in the Portal
    # Framework Gap: Requires step definition covering an existing line item's Creative Separation setting alongside the ET-24631 Tactic override in LifeSteps.java
    And A line item that previously had Creative Separation configured is handled predictably and consistently with the Tactic-level override
    And An account that previously held the permission sees it removed with no error or broken Features table
    And All other permissions in the Life Features table remain intact

  @todo
  # Source: ET-24704
  Scenario: Admin > Setup is renamed to Life Marketplace Deals and lists Premium Publisher and Medscape deals
    When User navigates to Admin > Setup
    Then The menu item reads "Life Marketplace Deals" and no longer "Health Markets", and opens the new view
    # Framework Gap: Requires step definition for the Life Marketplace Deals table population in LifeSteps.java
    Then Both Premium Publisher and Medscape deals appear in the deals table

  @todo
  # Source: ET-24704
  Scenario Outline: Life Marketplace Deals search filters the table by Deal ID or Name
    When User navigates to Admin > Setup > Life Marketplace Deals
    And User searches by "<SEARCH_BY>" using "<SEARCH_VALUE>"
    Then The table filters to the deal(s) matching "<SEARCH_VALUE>"
    Examples:
      | SEARCH_BY | SEARCH_VALUE |
      | Deal ID   | DEAL_10482   |
      | Name      | Medscape_Q3  |

  @todo
  # Source: ET-24704
  Scenario Outline: Life Marketplace Deals default filters apply on load and respond to Status/Exchange changes
    When User navigates to Admin > Setup > Life Marketplace Deals
    And User performs the following action: "<FILTER_ACTION>"
    Then "<EXPECTED_RESULT>"
    Examples:
      | FILTER_ACTION                       | EXPECTED_RESULT                                                                                                                |
      | Open the view fresh (no changes)    | Defaults are Status=Enabled, Visibility=Visible, Exchange=all, Device Type=any, Floor Price Range=$0-1000+, Est Avails=0-10bn+ |
      | Set Status filter to Disabled       | The table shows deals with Disabled status and excludes enabled ones                                                           |
      | Select a single Exchange (Medscape) | Only deals on the Medscape exchange are shown                                                                                  |

  @todo
  # Source: ET-24704
  Scenario: Life Marketplace Deals table renders all specified columns
    When User navigates to Admin > Setup > Life Marketplace Deals
    Then The deals table renders columns Name, Deal ID, Status, Visibility, Exchange, Deal Type, Media Type, Clearing Price, Floor, and Est. Avails

  @todo
  # Source: ET-24704
  Scenario: Clicking a deal name opens the details modal and the contextual menu offers Edit and Delete
    When User navigates to Admin > Setup > Life Marketplace Deals
    And User clicks a deal Name "DEAL_10482" in the table
    Then The existing deal details modal opens unchanged for that deal
    When User opens the contextual menu on the deal row "DEAL_10482"
    Then Edit Deal and Delete Deal actions are available from the menu

  @todo
  # Source: ET-24704
  Scenario Outline: Add Deal enforces progressive tab enablement and mandatory field validation
    When User navigates to Admin > Setup > Life Marketplace Deals
    And User clicks Add Deal
    Then The panel opens on the Details tab with Assign Accounts and Assigned Only disabled
    When User clicks Next leaving a mandatory Details field "<MANDATORY_FIELD>" empty
    Then Validation errors are displayed and the user cannot proceed to the next tab
    When User completes all mandatory Details fields except "Start Date, End Date, Description" and clicks Next
    Then Assign Accounts and Assigned Only become enabled and the user is moved to the Assign Accounts tab
    Examples:
      | MANDATORY_FIELD |
      | Deal ID         |
      | Deal Name       |
      | Exchange        |

  @todo
  # Source: ET-24704
  Scenario: Editing a deal persists detail, enable/disable, and hide/unhide changes
    When User navigates to Admin > Setup > Life Marketplace Deals
    And User edits deal "DEAL_10482", toggles enable/disable and hide/unhide, and clicks Save
    Then The edited details and status/visibility changes persist after Save

  @todo
  # Source: ET-24704, GAP-1
  Scenario Outline: Deleting a Life Marketplace Deal is allowed only when the deal is not in use
    When User navigates to Admin > Setup > Life Marketplace Deals
    # Framework Gap: Requires step definition for the "in use" scope (active vs paused/ended tactics) in LifeSteps.java
    And User attempts to delete deal "<DEAL_STATE>"
    Then "<EXPECTED_RESULT>"
    Examples:
      | DEAL_STATE                                | EXPECTED_RESULT                                                  |
      | "DEAL_20011" not associated to any tactic | The deal is deleted and no longer appears in the table           |
      | "DEAL_20012" attached to an active tactic | Delete is prevented with a clear message that the deal is in use |

  @todo
  # Source: ET-24704
  # Regression anchor: HT-6071 - hidden deal not spending
  Scenario Outline: Hiding a deal keeps it serving on existing tactics while excluding it from new-tactic targeting
    When User navigates to Admin > Setup > Life Marketplace Deals
    And User performs the following action: "<HIDE_ACTION>"
    Then "<EXPECTED_RESULT>"
    Examples:
      | HIDE_ACTION                                               | EXPECTED_RESULT                                                                                                                         |
      | Hide an enabled, visible deal and hover the Hide checkbox | The deal becomes hidden and the hover tooltip states it stays active for existing tactics but will not appear in targeting for new ones |
      | Attempt to hide a disabled deal row                       | The Hide checkbox is not available for disabled deals                                                                                   |
      | Toggle the Visibility filter to Hidden, then Visible      | The table shows hidden deals then visible deals accordingly                                                                             |

  @todo
  # Source: ET-24704
  # Regression anchor: HT-6071 - hidden deal not spending
  Scenario: A hidden deal keeps serving on an existing tactic but is excluded from a new tactic's deal picker
    Given Deal "DEAL_20013" is already applied on an existing running tactic
    When User hides deal "DEAL_20013" from Admin > Setup > Life Marketplace Deals
    Then The existing tactic keeps the deal and it keeps spending/serving on it
    When User creates a new tactic and opens the deal picker
    Then Deal "DEAL_20013" does not appear in the deal picker for the new tactic

  @todo
  # Source: ET-24704, AMB-1, GAP-3
  Scenario Outline: Medscape Override Fees is gated by exchange type and the Edit Medscape Account Fees permission
    When User navigates to Admin > Setup > Life Marketplace Deals
    And User opens a deal on exchange "<EXCHANGE>"
    Then The Override Fees checkbox is "<OVERRIDE_FEES_CHECKBOX>"
    # Framework Gap: Requires step definition for the Edit Medscape Account Fees permission gate in LifeSteps.java
    Given User holds permission state "<PERMISSION>"
    When User selects Override Fees on a Medscape deal
    Then "<ACCOUNTS_TAB_STATE>"
    Examples:
      | EXCHANGE                | OVERRIDE_FEES_CHECKBOX | PERMISSION                             | ACCOUNTS_TAB_STATE                           |
      | Medscape                | shown                  | Edit Medscape Account Fees granted     | Editable per-account fees                    |
      | Medscape_Email          | shown                  | Edit Medscape Account Fees granted     | Editable per-account fees                    |
      | Pubmatic (non-Medscape) | not shown              | N/A                                    | Accounts tab behaves as a non-Medscape deal  |
      | Medscape                | shown                  | Edit Medscape Account Fees NOT granted | Accounts tab and Override Fees are read-only |

  @todo
  # Source: ET-25052, GAP-4
  Scenario Outline: Import Deals validates each template row against existing Deal ID and Exchange state
    When User navigates to Admin > Setup > Life Marketplace Deals
    And User uploads an Import Deals Excel template row: "<IMPORT_ROW_CASE>"
    Then "<EXPECTED_RESULT>"
    Examples:
      | IMPORT_ROW_CASE                                                                                                                   | EXPECTED_RESULT                                                                            |
      | Deal ID DEAL_30001 does not already exist                                                                                         | A new Life Marketplace Deal is created and reflected in the table and Publisher Deal Table |
      | Deal ID DEAL_10482 already exists as a Private/Curated Marketplace deal                                                           | The row is rejected with an error and no duplicate deal is created                         |
      | Deal ID DEAL_10482 + Exchange Medscape already exists as a Life Marketplace Deal with changed field values and a changed Exchange | Existing values are overridden while the Exchange field is not modified                    |
      | Deal ID DEAL_10482 exists as a Life Marketplace Deal under a different Exchange than the template row                             | An error is thrown for that row and the deal is not created/updated                        |

  @todo
  # Source: ET-25052, GAP-4
  Scenario: An import template mixing valid and invalid rows is processed per the defined commit rule
    When User navigates to Admin > Setup > Life Marketplace Deals
    And User uploads an Import Deals Excel template containing both valid rows and duplicate/invalid rows
    # Framework Gap: Requires step definition for the all-or-nothing vs partial-commit import rule in LifeSteps.java
    Then Processing follows the documented commit rule and the outcome, including which rows applied, is recorded

  @todo
  # Source: ET-24704, GAP-2
  Scenario: The Est. Avails dropdown governs the filterability of clearing price and floor columns
    When User navigates to Admin > Setup > Life Marketplace Deals
    # Framework Gap: Requires step definition for the Est. Avails to clearing-price/floor filter dependency in LifeSteps.java
    And User changes the Est. Avails dropdown selection to "1bn-10bn+"
    Then The clearing price, floor, and est. avails column filters respond per the defined dependency and behavior is recorded

  @todo
  # Source: ET-24704
  # Regression anchor: HT-6029 - deals disappearing from the deals table
  Scenario: Deals do not disappear from the Life Marketplace Deals table after filtering, sorting, or refresh
    When User navigates to Admin > Setup > Life Marketplace Deals
    And User applies filters and sort, then refreshes the view
    Then All matching deals remain listed and none silently drop out of the table
