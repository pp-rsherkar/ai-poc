Feature: Internal Users Permissions Search - User Filter and Permissions Column Search

  1. Adds a searchable multi-select User Filter to the Internal Users Permissions table header, allowing admins to narrow the matrix to selected users or groups.
  2. Converts the main search bar into a permissions-column filter that hides non-matching columns and highlights the matched text in remaining column labels.
  3. Covers the happy-path behavior for both filters independently and combined, the documented edge cases and negative states, and two regression anchors from prior permission-save and permission-labeling defects.
  4. User Filter search matches on user name only, not group title (Ambiguity Interpretation A), and the Permissions Search coexists alongside the existing search bar rather than replacing it (Ambiguity Interpretation A).

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Administrative section
    And User navigates to Internal Users Permissions page

  @todo
  Scenario Outline: Filter the Internal Users Permissions table using the User Filter dropdown
    When User activates the User Filter input in the table header
    Then User Filter dropdown displays available users grouped by their organizational groups, collapsed by default
    When User selects "<SELECTED_USERS>" from the User Filter dropdown
    Then Permissions table refreshes to show only rows for "<SELECTED_USERS>"
    When User clears all selections from the User Filter
    Then Permissions table returns to its unfiltered state
    Examples:
      | SELECTED_USERS          |
      | a single user           |
      | multiple users          |

  @todo
  Scenario Outline: User Filter search matches user names within groups and does not match group titles
    When User activates the User Filter input in the table header
    And User types "<SEARCH_TERM>" in the User Filter search box
    Then User Filter dropdown shows "<EXPECTED_RESULT>"
    Examples:
      | SEARCH_TERM              | EXPECTED_RESULT                                    |
      | an existing user's name  | the matching user displayed with its parent group  |
      | an existing group's name | no results, since group titles are not searchable  |

  @todo
  Scenario: Selected users in the User Filter are displayed as removable chips
    When User activates the User Filter input in the table header
    And User selects multiple users from the User Filter dropdown
    Then the selected users are displayed as chips in the User Filter input
    When User removes one chip from the User Filter input
    Then Permissions table refreshes to exclude the removed user and retains the remaining selected users

  @todo
  Scenario: User Filter search with no matching user shows an empty state and leaves the table unchanged
    When User activates the User Filter input in the table header
    And User types a user name that does not exist in the User Filter search box
    Then User Filter dropdown displays an empty, no-results state
    And Permissions table remains unchanged

  @todo
  Scenario: A group with no members appears in the User Filter dropdown without error
    When User activates the User Filter input in the table header
    And User expands a group that has no members
    Then the group is displayed as empty or expandable without a broken state or a JavaScript error

  @todo
  Scenario: A user belonging to multiple groups appears under each group without duplicating filtered table rows
    When User activates the User Filter input in the table header
    And User searches for a user that belongs to more than one organizational group
    Then the user is listed under each of their parent groups in the User Filter dropdown
    When User selects that user from the User Filter dropdown
    Then Permissions table displays exactly one row for that user, not one row per group

  @todo
  Scenario Outline: Permissions Search filters the table to matching permission columns and highlights the match
    When User types "<SEARCH_TERM>" in the main search bar
    Then only permission columns whose labels match "<SEARCH_TERM>" are displayed
    And the matched text is highlighted within the visible column labels
    When User clicks "X" on the main search bar
    Then all permission columns are restored
    Examples:
      | SEARCH_TERM                    |
      | an existing permission label    |

  @todo
  Scenario: Permissions Search with no matching permission name shows a meaningful empty state
    When User types a permission name that does not exist in the main search bar
    Then all permission columns are hidden
    And the table displays a meaningful empty state instead of a broken layout

  @todo
  Scenario: User Filter and Permissions Search can be applied together
    When User selects one or more users from the User Filter dropdown
    And User types a matching permission name in the main search bar
    Then Permissions table shows only the selected users' rows with only the matching permission columns

  @todo
  Scenario Outline: Special characters entered in the filter inputs are sanitized without errors
    When User types "<SPECIAL_CHARACTERS>" in the "<FILTER_INPUT>"
    Then the input is sanitized, no JavaScript error occurs, and no unescaped script executes
    Examples:
      | FILTER_INPUT           | SPECIAL_CHARACTERS |
      | User Filter search box | <, >, &, "          |
      | main search bar        | <, >, &, "          |

  @todo
  Scenario: Rapid typing in the filter inputs does not cause flicker or race conditions
    When User rapidly types and edits text in the User Filter search box and the main search bar in quick succession
    Then Permissions table does not flicker and reflects only the final entered search values, with no stale results from earlier keystrokes

  @todo
  Scenario: Filtering performance is maintained with a large number of users and permission columns
    Given the account has 100 or more internal users and 50 or more permission columns
    When User applies a User Filter selection and a Permissions Search term
    Then Permissions table updates without noticeable performance degradation

  @todo
  Scenario: Filter inputs are fully operable via keyboard
    When User tabs into the User Filter input and types a search term
    And User navigates the dropdown options using arrow keys and selects one with Enter
    Then the selected user filters the Permissions table as if selected with a mouse

  @todo
  Scenario: Saving a permission change while a filter is active updates the correct user and permission
    Given a User Filter selection is applied to the Permissions table
    When User toggles a permission checkbox for a visible user and saves the change
    Then the save request includes the correct user and permission data for that user, regardless of the active filter
    # Regression anchor: ET-24931 - toggling internal user permissions previously sent an empty POST payload

  @todo
  Scenario: Permissions Search highlights current permission labels, not stale ones
    When User types a permission name in the main search bar that matches a recently renamed permission
    Then the column for the current, up-to-date permission label is displayed and highlighted
    # Regression anchor: ET-25029 - permission column labels were previously generic instead of feature-specific
