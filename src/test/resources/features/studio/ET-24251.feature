Feature: Internal Users Permissions Search - User Filter and Permissions Column Search
  1. Searchable multi-select User Filter in the Internal Users Permissions table header, grouped by organizational group.
  2. Permissions Search that converts the main search bar into a permissions-column filter with label highlighting.
  3. Combined use of both filters and their edge/negative behavior.

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Administrative section
    And User navigates to Internal Users Permissions page

  @todo
  Scenario Outline: User Filter narrows the permissions table to the selected user(s)
    When User activates the User Filter input in the permissions table header
    Then a dropdown listing available users grouped by organizational group should be displayed
    When User selects "<USER_SELECTION>" from the User Filter dropdown
    Then the permissions table should refresh to show only rows for "<USER_SELECTION>"
    Examples:
      | USER_SELECTION      |
      | a single user       |
      | multiple users      |

  @todo
  Scenario: Clearing all User Filter selections restores the unfiltered permissions table
    When User selects one or more users from the User Filter dropdown
    And User clears all selections from the User Filter input
    Then the permissions table should return to its unfiltered state showing all users

  @todo
  Scenario: Selected users are displayed as removable chips in the User Filter input
    When User selects a user from the User Filter dropdown
    Then the selected user should appear as a chip in the User Filter input
    And User should be able to remove the selection by dismissing its chip

  @todo
  Scenario: Groups are collapsed by default and expand to reveal individual users
    When User activates the User Filter input in the permissions table header
    Then organizational groups should be displayed collapsed by default
    When User expands a group
    Then the individual users within that group should be displayed

  @todo
  Scenario: User Filter search matches user names only and does not match group titles
    When User types a user's name in the User Filter search field
    Then matching users should be displayed along with their parent group name
    When User types a group name in the User Filter search field
    Then no results should be displayed since group titles are not searched

  @todo
  Scenario: Searching for a user name with no match shows an empty results state and leaves the table unchanged
    When User types a user name that does not exist in the User Filter search field
    Then the User Filter dropdown should display an empty/no-results state
    And the permissions table should remain unchanged

  @todo
  Scenario: A user who belongs to multiple groups appears under each group without duplicating filtered rows
    When User activates the User Filter input in the permissions table header
    Then a user who belongs to multiple groups should appear under each of those groups
    When User selects that user from the User Filter dropdown
    Then the permissions table should show exactly one row for that user, not one row per group

  @todo
  Scenario: A group with no members does not error and appears as an empty expandable group
    When User activates the User Filter input in the permissions table header
    And User expands a group that has no members
    Then the group should display as empty without any error

  @todo
  Scenario Outline: Permissions Search filters and highlights matching permission columns
    When User types "<SEARCH_TERM>" in the main search bar
    Then only permission columns whose labels match "<SEARCH_TERM>" should be displayed
    And the matching text should be highlighted within the visible column labels
    Examples:
      | SEARCH_TERM |
      | View        |
      | Edit        |

  @todo
  Scenario: Permissions Search with no matching permission name hides all columns and shows a meaningful empty state
    When User types a permission name that does not exist in the main search bar
    Then all permission columns should be hidden
    And a meaningful empty state message should be displayed instead of a broken layout

  @todo
  Scenario: Clearing Permissions Search via the X control restores all columns
    When User types a value in the main search bar
    And User clicks the "X" control on the main search bar
    Then all permission columns should be restored

  @todo
  Scenario: Combining User Filter and Permissions Search narrows rows and columns together
    When User selects one or more users from the User Filter dropdown
    And User types a matching term in the main search bar
    Then the permissions table should show only the selected users' rows with only the matching permission columns

  @todo
  Scenario Outline: Special characters entered in either filter are sanitized without errors
    When User types "<SPECIAL_CHARACTERS>" in the "<FILTER>"
    Then the input should be sanitized with no JavaScript errors and no unescaped script execution
    Examples:
      | SPECIAL_CHARACTERS | FILTER                     |
      | <script>alert(1)</script> | User Filter search field |
      | "><&                | main search bar            |

  @todo
  Scenario Outline: Rapid typing in either filter does not cause flicker or stale results
    When User rapidly types and edits text in the "<FILTER>"
    Then the table should not flicker
    And no race condition between successive filter API calls should occur
    Examples:
      | FILTER                    |
      | User Filter search field  |
      | main search bar           |

  @todo
  Scenario: Filtering performs correctly against a large number of users and permission columns
    Given the account has 100 or more internal users and 50 or more permission columns
    When User applies the User Filter and the Permissions Search
    Then the table should filter correctly without noticeable performance degradation

  @todo
  Scenario: The User Filter dropdown is fully operable using keyboard only
    When User tabs into the User Filter input using the keyboard
    And User types a search term and navigates the dropdown options using arrow keys
    And User presses Enter to select an option
    Then the selection should be applied using only keyboard navigation

  @todo
  Scenario: Toggling a permission while a filter is active saves against the correct user
    When User applies a User Filter selection
    And User toggles a permission checkbox for the filtered user and saves
    Then the save should apply to the correct user and permission
    And the save request payload should include the correct permission data, not an empty payload

  @todo
  Scenario: Permissions Search highlights the current permission label, not a stale one
    When User types a search term matching a recently renamed, feature-specific permission label
    Then the Permissions Search should highlight the current feature-specific label
    And no stale or generic permission label should be matched instead
