Feature: Internal Users Permissions Search - User Filter and Permissions Column Search

  1. Adds a searchable multi-select User Filter to the Internal Users Permissions table header, allowing admins to narrow the matrix to selected users or groups.
  2. Converts the main search bar into a permissions-column filter that hides non-matching columns and highlights the matched text in remaining column labels.
  3. User Filter search matches on user name only, not group title (Ambiguity Interpretation A), and the Permissions Search coexists alongside the existing search bar rather than replacing it (Ambiguity Interpretation A).
  4. Each scenario below walks a single continuous pass through the Internal Users Permissions page, chaining the checks that are reachable without leaving that page or resetting state, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Administrative section
    And User navigates to Internal Users Permissions page

  @todo
  Scenario: Verify the User Filter workflow - grouped dropdown, search, multi-select, chips, and edge cases
    When User activates the User Filter input in the table header
    Then User Filter dropdown displays available users grouped by their organizational groups, collapsed by default
    When User expands a group that has members
    Then the group's individual users are revealed
    When User expands a group that has no members
    Then the group is displayed as empty or expandable without a broken state or a JavaScript error
    When User searches for an existing user's name in the User Filter search box
    Then the matching user is displayed along with its parent group name
    When User searches for an existing group's name in the User Filter search box
    Then no results are returned, since group titles are not searchable
    When User searches for a user name that does not exist
    Then User Filter dropdown displays an empty, no-results state and the Permissions table remains unchanged
    When User searches for a user that belongs to more than one organizational group
    Then the user is listed under each of their parent groups in the dropdown
    When User selects that user along with one other user from the dropdown
    Then the selected users are displayed as chips in the User Filter input
    And Permissions table refreshes to show exactly one row per selected user, not one row per group
    When User removes one chip from the User Filter input
    Then Permissions table refreshes to exclude the removed user and retains the remaining selected user
    When User clears all selections from the User Filter
    Then Permissions table returns to its unfiltered state
    When User selects a user from the User Filter and toggles a permission checkbox for that user, then saves the change
    Then the save request includes the correct user and permission data for that user, regardless of the active filter
    # Regression anchor: ET-24931 - toggling internal user permissions previously sent an empty POST payload

  @todo
  Scenario: Verify the Permissions Search workflow - column filtering, highlighting, empty state, and combination with the User Filter
    When User types an existing permission label in the main search bar
    Then only permission columns whose labels match the search term are displayed
    And the matched text is highlighted within the visible column labels
    When User clicks "X" on the main search bar
    Then all permission columns are restored
    When User types a permission name that does not exist in the main search bar
    Then all permission columns are hidden and the table displays a meaningful empty state instead of a broken layout
    When User clears the main search bar, then selects one or more users from the User Filter dropdown and types a matching permission name in the main search bar
    Then Permissions table shows only the selected users' rows with only the matching permission columns
    When User types a permission name in the main search bar that matches a recently renamed permission
    Then the column for the current, up-to-date permission label is displayed and highlighted, not a stale one
    # Regression anchor: ET-25029 - permission column labels were previously generic instead of feature-specific

  @todo
  Scenario Outline: Verify special characters entered in the filter inputs are sanitized without errors
    When User types "<SPECIAL_CHARACTERS>" in the "<FILTER_INPUT>"
    Then the input is sanitized, no JavaScript error occurs, and no unescaped script executes
    Examples:
      | FILTER_INPUT           | SPECIAL_CHARACTERS |
      | User Filter search box | <, >, &, "          |
      | main search bar        | <, >, &, "          |

  @todo
  Scenario: Verify the filter inputs remain stable and performant under rapid typing and a large dataset
    Given the account has 100 or more internal users and 50 or more permission columns
    When User rapidly types and edits text in the User Filter search box and the main search bar in quick succession
    Then Permissions table does not flicker and reflects only the final entered search values, with no stale results from earlier keystrokes
    And Permissions table updates without noticeable performance degradation

  @todo
  Scenario: Verify the User Filter and Permissions Search are fully operable via keyboard
    When User tabs into the User Filter input and types a search term
    And User navigates the dropdown options using arrow keys and selects one with Enter
    Then the selected user filters the Permissions table as if selected with a mouse
