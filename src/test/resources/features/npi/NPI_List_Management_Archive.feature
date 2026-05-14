Feature: NPI List Management - Archive and Unarchive Lists capability
  Migrating the list archiving capability from the old Life list management UI to the new
  NPI List Management application. Archive and unarchive behaviors must match PROD-2815.

  Scenarios covered:
  1. Archive a list that is NOT tagged to any active tactic / campaign
  2. Verify the archived list moves to the Archived tab and becomes read-only
  3. Unarchive a list and verify it moves back to the Active tab
  4. Attempt to archive a list tagged to an active tactic / campaign - error is displayed
  5. Attempt to archive a list that is active in Signal - error is displayed
  6. Bulk archive and bulk unarchive of multiple lists from the listing page
  7. Verify Archive option availability based on list state and user permissions
  8. Filter, sort and search within the Archived tab
  9. Archive and unarchive a list from the list-detail page (single-list flow)

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "NPI List Management" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to NPI Lists page
    Then Verify NPI List Management dashboard is displayed with "Active" and "Archived" tabs and by default "Active" tab is selected

  @regression
  Scenario Outline: Archive a list which is not tagged to any active tactic - "<LIST_TYPE>"
    When User searches the list "<LIST_NAME>" under "Active" tab and selects the list
    And User clicks 3 dot menu and selects Archive button for the list "<LIST_NAME>"
    Then Verify Archive confirmation pop-up is displayed with list name "<LIST_NAME>"
    When User confirms the archive action
    Then Verify success message "List archived successfully" is displayed
    And Verify the list "<LIST_NAME>" is no longer available under "Active" tab
    When User clicks "Archived" tab from the NPI Lists page
    Then Verify the list "<LIST_NAME>" is displayed under "Archived" tab with archive timestamp and archived-by user
    Examples:
      | LIST_NAME       | LIST_TYPE      |
      | ARCHIVE_STATIC  | Static List    |
      | ARCHIVE_SMART   | Smart List     |
      | ARCHIVE_ATTR    | Attribute List |
      | ARCHIVE_AUTO    | Auto-Imported  |

  @regression
  Scenario: Verify an archived list is read-only and cannot be edited or assigned to a tactic
    Given An archived NPI list "ARCHIVE_READONLY" exists under "Archived" tab
    When User clicks "Archived" tab from the NPI Lists page
    And User opens the archived list "ARCHIVE_READONLY"
    Then Verify the list details page is displayed in read-only mode
    And Verify Edit, Save and Delete actions are disabled for the archived list
    And Verify the archived list "ARCHIVE_READONLY" is NOT available for selection while creating or editing a tactic targeting

  @regression
  Scenario Outline: Unarchive a list and verify it is restored to Active tab - "<LIST_NAME>"
    Given An archived NPI list "<LIST_NAME>" exists under "Archived" tab
    When User clicks "Archived" tab from the NPI Lists page
    And User clicks 3 dot menu and selects Unarchive button for the list "<LIST_NAME>"
    Then Verify Unarchive confirmation pop-up is displayed with list name "<LIST_NAME>"
    When User confirms the unarchive action
    Then Verify success message "List unarchived successfully" is displayed
    And Verify the list "<LIST_NAME>" is no longer available under "Archived" tab
    When User clicks "Active" tab from the NPI Lists page
    Then Verify the list "<LIST_NAME>" is displayed under "Active" tab and is editable
    Examples:
      | LIST_NAME        |
      | UNARCHIVE_STATIC |
      | UNARCHIVE_SMART  |

  @regression
  Scenario: Attempt to archive a list which is tagged to an active tactic / campaign
    Given An NPI list "ACTIVE_TACTIC_LIST" exists which is tagged to an active tactic in an active campaign
    When User searches the list "ACTIVE_TACTIC_LIST" under "Active" tab and selects the list
    And User clicks 3 dot menu and selects Archive button for the list "ACTIVE_TACTIC_LIST"
    And User confirms the archive action
    Then Verify the archive action is blocked and an error message is displayed indicating the list is tagged to active tactic(s)
    And Verify the associated tactics icon is displayed next to the list "ACTIVE_TACTIC_LIST"
    And Verify the list "ACTIVE_TACTIC_LIST" remains under "Active" tab and is not archived
    # Known Issue - PROD-10340: associated tactics link is missing in the error message
    # and the associated tactics icon does not open the detailed pop-up. Once fixed,
    # the following steps should be enabled:
    # And Verify the error message contains a link to the associated tactic(s)
    # When User clicks the associated tactics icon for the list "ACTIVE_TACTIC_LIST"
    # Then Verify the associated tactics detailed pop-up is displayed with campaign and tactic names

  @regression
  Scenario: Attempt to archive a list which is currently active in Signal
    Given An NPI list "SIGNAL_ACTIVE_LIST" exists which is active in Signal
    When User searches the list "SIGNAL_ACTIVE_LIST" under "Active" tab and selects the list
    And User clicks 3 dot menu and selects Archive button for the list "SIGNAL_ACTIVE_LIST"
    And User confirms the archive action
    Then Verify the archive action is blocked and an error message is displayed indicating the list is active in Signal
    And Verify the list "SIGNAL_ACTIVE_LIST" remains under "Active" tab and is not archived

  @regression
  Scenario Outline: Bulk archive multiple lists from the NPI Lists page
    When User clicks "Active" tab from the NPI Lists page
    And User selects multiple lists "<LIST_NAMES>" using the row checkboxes
    And User performs "Bulk Archive" action using "Archive Lists" option on the selected lists
    Then Verify Archive confirmation pop-up is displayed with the count of selected lists
    When User confirms the bulk archive action
    Then Verify success message "<COUNT> lists archived successfully" is displayed
    And Verify the lists "<LIST_NAMES>" are no longer available under "Active" tab
    When User clicks "Archived" tab from the NPI Lists page
    Then Verify the lists "<LIST_NAMES>" are displayed under "Archived" tab
    Examples:
      | LIST_NAMES                              | COUNT |
      | BULK_ARCH_1, BULK_ARCH_2, BULK_ARCH_3   | 3     |

  @regression
  Scenario Outline: Bulk unarchive multiple lists from the Archived tab
    Given Archived NPI lists "<LIST_NAMES>" exist under "Archived" tab
    When User clicks "Archived" tab from the NPI Lists page
    And User selects multiple lists "<LIST_NAMES>" using the row checkboxes
    And User performs "Bulk Unarchive" action using "Unarchive Lists" option on the selected lists
    Then Verify Unarchive confirmation pop-up is displayed with the count of selected lists
    When User confirms the bulk unarchive action
    Then Verify success message "<COUNT> lists unarchived successfully" is displayed
    And Verify the lists "<LIST_NAMES>" are no longer available under "Archived" tab
    When User clicks "Active" tab from the NPI Lists page
    Then Verify the lists "<LIST_NAMES>" are displayed under "Active" tab
    Examples:
      | LIST_NAMES                                    | COUNT |
      | BULK_UNARCH_1, BULK_UNARCH_2, BULK_UNARCH_3   | 3     |

  @regression
  Scenario Outline: Verify Archive option availability based on list state and user permission - "<USER_ROLE>"
    Given User is logged in with role "<USER_ROLE>"
    When User clicks "<TAB>" tab from the NPI Lists page
    And User clicks 3 dot menu for the list "<LIST_NAME>"
    Then Verify the "<EXPECTED_OPTION>" option "<AVAILABILITY>" available in the 3 dot menu
    Examples:
      | USER_ROLE     | TAB      | LIST_NAME           | EXPECTED_OPTION | AVAILABILITY |
      | Admin         | Active   | ACTIVE_LIST_ADMIN   | Archive         | is           |
      | Admin         | Archived | ARCHIVED_LIST_ADMIN | Unarchive       | is           |
      | Read-Only     | Active   | ACTIVE_LIST_RO      | Archive         | is not       |
      | Read-Only     | Archived | ARCHIVED_LIST_RO    | Unarchive       | is not       |

  @regression
  Scenario: Filter, sort and search within the Archived tab
    Given Multiple archived NPI lists exist under "Archived" tab
    When User clicks "Archived" tab from the NPI Lists page
    Then Verify the following filters are available and working under "Archived" tab
      | List Type  |
      | Advertiser |
      | Archived By|
      | Date Range |
    When User searches for an archived list by name "ARCHIVE_SEARCH"
    Then Verify only the lists matching the search term "ARCHIVE_SEARCH" are displayed under "Archived" tab
    When User sorts the archived lists by "Archived On" in descending order
    Then Verify the archived lists are displayed sorted by "Archived On" in descending order
    When User clicks the clear-all button
    Then Verify all the filters and search are cleared and the full archived list is displayed

  @regression
  Scenario Outline: Archive and Unarchive a list from the list-detail page - "<LIST_NAME>"
    When User searches the list "<LIST_NAME>" under "Active" tab and selects the list
    And User opens the list "<LIST_NAME>" details page
    And User checks Archive option is working for the list and verify the list is moved to "Archived" tab
    Then Verify success message "List archived successfully" is displayed
    When User opens the archived list "<LIST_NAME>" from "Archived" tab
    And User checks Unarchive option is working for the list and verify the list is moved to "Active" tab
    Then Verify success message "List unarchived successfully" is displayed
    Examples:
      | LIST_NAME           |
      | DETAIL_PAGE_ARCHIVE |
