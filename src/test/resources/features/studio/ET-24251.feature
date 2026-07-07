Feature: Internal Users Permissions Search - User Filter and Permissions Column Search

  1. Verifies the new searchable multi-select User Filter added to the Internal Users Permissions table header.
  2. Verifies the Permissions Search behavior, which converts the main search bar into a permissions-column filter.
  3. Covers combined use of both filters, edge cases, accessibility, and regression anchors tied to prior permission save and labeling defects.

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Administrative section
    And User navigates to Internal Users tab under Permissions section
    Then User should see the Internal Users Permissions table with the User Filter and Permissions Search controls in the header

  @todo
  Scenario Outline: User Filter narrows the permissions table to selected users
    When User selects "<SELECTED_USERS>" in the User Filter
    Then the permissions table should show only rows for "<SELECTED_USERS>"
    Examples:
      | SELECTED_USERS          |
      | Single Internal User    |
      | Multiple Internal Users |

  @todo
  Scenario: Clearing all User Filter selections returns the table to its unfiltered state
    Given User has selected one or more users in the User Filter
    When User clears all selections from the User Filter
    Then the permissions table should return to its unfiltered state

  @todo
  Scenario: Selected users appear as chips in the User Filter input
    When User selects a user from the User Filter dropdown
    Then the selected user should appear as a chip in the User Filter input

  @todo
  Scenario: User Filter groups are collapsed by default and expand to show individual users
    When User activates the User Filter input
    Then groups should be collapsed by default in the User Filter dropdown
    When User expands a group in the User Filter dropdown
    Then the individual users under that group should be visible

  @todo
  Scenario: User Filter search matches user names only and does not match group titles
    When User searches for a group name within the User Filter dropdown
    Then the User Filter dropdown should show no matching results for that group name

  @todo
  Scenario: User Filter search with no matching user shows an empty state and leaves the table unchanged
    When User searches for a user name that does not exist in the User Filter dropdown
    Then the User Filter dropdown should show an empty no-results state
    And the permissions table should remain unchanged

  @todo
  Scenario: A user who belongs to multiple groups appears under each group without duplicating filtered rows
    Given a user belongs to more than one group
    When User activates the User Filter input
    Then the user should appear under each of their groups in the User Filter dropdown
    When User selects that user and applies the filter
    Then the user should appear only once in the filtered permissions table

  @todo
  Scenario: A group with no members does not error in the User Filter dropdown
    Given a group has no members
    When User activates the User Filter input
    Then the empty group should appear as empty or expandable without errors

  @todo
  Scenario Outline: Permissions Search filters and highlights columns by label match
    When User enters "<QUERY>" in the Permissions Search bar
    Then only permission columns whose labels match "<QUERY>" should be visible
    And the matched text should be highlighted in the visible column headers
    Examples:
      | QUERY |
      | Edit  |
      | View  |

  @todo
  Scenario: Permissions Search with no matching permission hides all columns and shows a meaningful empty state
    When User enters a permission name that does not exist in the Permissions Search bar
    Then all permission columns should be hidden
    And a message indicating no permissions match the search should be displayed

  @todo
  Scenario: Clearing the Permissions Search via the X icon restores all columns
    Given User has entered a query in the Permissions Search bar
    When User clicks the X icon on the Permissions Search bar
    Then all permission columns should be restored

  @todo
  Scenario: Combining the User Filter and Permissions Search narrows rows and columns simultaneously
    When User selects a user in the User Filter
    And User enters a query in the Permissions Search bar
    Then the permissions table should show only the selected users rows with only the matching permission columns

  @todo
  Scenario Outline: Special characters entered in either filter are sanitized without errors
    When User enters "<SPECIAL_CHARS>" in the "<FILTER>" field
    Then no script should execute and no application error should be thrown
    And the input should be treated as plain text
    Examples:
      | FILTER              | SPECIAL_CHARS                |
      | User Filter         | script tag payload            |
      | Permissions Search  | quote and ampersand payload   |

  @todo
  Scenario Outline: Rapid typing in either filter does not cause flicker or stale results
    When User types rapidly in the "<FILTER>" field
    Then the permissions table should not flicker or show stale results
    Examples:
      | FILTER             |
      | User Filter        |
      | Permissions Search |

  @todo
  Scenario: Large-volume accounts filter without noticeable performance degradation
    Given the account has 100 or more internal users and 50 or more permission columns
    When User applies the User Filter and the Permissions Search together
    Then the table should filter without noticeable performance degradation

  @todo
  Scenario: User Filter dropdown is fully operable using keyboard only
    When User navigates the User Filter dropdown using Tab, Arrow keys and Enter only
    Then User should be able to select a user without using the mouse

  @todo
  @regression
  Scenario: Toggling a permission while a filter is active saves against the correct user
    Given a filter is active on the permissions table
    When User toggles a permission checkbox for a visible user and saves
    Then the save request should be sent for the correct user and permission with a properly populated payload

  @todo
  @regression
  Scenario: Permissions Search highlights the current permission label and not a stale or generic name
    When User enters a permission name in the Permissions Search bar
    Then the matching column header should display the current, feature-specific permission label
    And no stale or generic permission label should be highlighted
