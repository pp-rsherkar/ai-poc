Feature: Administration - Internal Users Permissions Search (User/Group Filter and Permissions Search)

  1. Adds a user/group filter and an updated main search targeting permissions specifically, to the Administration > User Permissions (Internal Users) table.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Administration > User Permissions page for Internal Users

  @todo
  Scenario: User Filter narrows the permissions table to selected users, matching individual users within groups rather than group titles
    When User activates the User Filter input in the table header
    Then a dropdown allows multi-select of users, or in-dropdown search to narrow a large list
    And groups are collapsed by default, and can be expanded to see individual members
    When User searches for a user who belongs to a group
    Then the filter matches the individual user within the group, not the group title itself, and displays the user plus their parent group name
    When User selects one or more users
    Then the selection remains visibly reflected in the filter control
    And the table refreshes to show only permissions associated with the selection
    Given a user belonging to multiple groups
    Then the correct parent group name(s) display per the search-behavior rule

  @todo
  Scenario: Permissions search filters and highlights matching columns, and "X" resets the search independently of the User Filter
    When User enters a query in the main Permissions search field
    Then only columns matching the search term remain visible, and the matched term is highlighted in the surviving column labels
    When User clicks "X"
    Then the search resets and the table returns to its unfiltered state
    Given a search term matching zero permissions/columns
    Then an empty-state is shown rather than a blank or broken table
    Given the User Filter and the Permissions search are both active simultaneously
    Then the table reflects the intersection of both filters
    # Ambiguity: whether clicking "X" mid-search also clears an active User Filter selection is not stated in the source requirement; confirm with product whether the two filters reset independently or together before treating either behavior as the acceptance bar
