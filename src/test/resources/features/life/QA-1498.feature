Feature: List Sorting and Record Modification Behavior

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"

  @todo
  Scenario Outline: Verify default sorting by createdOn across all list types
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "<LIST_TYPE>" list page
    Then Verify the list displays records sorted by createdOn in ascending order
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify record position remains unchanged after modification when sorted by createdOn
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3 in the createdOn sorted list
    When User modifies the record at index 3
    And User saves the changes
    Then Verify the record remains at index 3 in the list
    And Verify the createdOn timestamp remains unchanged
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify lastUpdated column is not used for default list view sorting
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of the oldest record by createdOn
    When User modifies the oldest record to update its lastUpdated timestamp
    And User saves the changes
    Then Verify the record remains in its original position based on createdOn
    And Verify the list order is not affected by the updated lastUpdated timestamp
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify rapid edits maintain relative order based on createdOn
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the positions of records at index 2, 5, and 8
    When User modifies record at index 2
    And User saves the changes
    And User modifies record at index 5
    And User saves the changes
    And User modifies record at index 8
    And User saves the changes
    Then Verify all records maintain their original positions based on createdOn
    And Verify the relative order between modified records remains unchanged
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify clearing optional fields does not change record position
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3
    When User modifies the record at index 3 by clearing an optional field
    And User saves the changes
    Then Verify the record remains at index 3
    And Verify the createdOn timestamp remains unchanged
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify manual sort is respected after record modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User clicks the "<SORT_COLUMN>" column header to manually sort by that column
    And User notes the position of a record in the manually sorted list
    And User modifies that record
    And User saves the changes
    Then Verify the record remains in the correct position for the "<SORT_COLUMN>" sort
    And Verify the record does not jump to a position based on createdOn
    Examples:
      | LIST_TYPE       | SORT_COLUMN |
      | NPI List        | Name        |
      | Keyword List    | Value       |
      | Domain/App List | Domain      |
      | IP List         | IP Address  |
      | Email List      | Email       |
      | Pixel List      | Pixel ID    |

  @todo
  Scenario Outline: Verify internal user can modify records and position is maintained
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an internal user
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3
    When User modifies the record at index 3
    And User saves the changes
    Then Verify the record remains at index 3 in the createdOn sorted list
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify external user with permission can modify records and position is maintained
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an external user with modification permissions
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3
    When User modifies the record at index 3
    And User saves the changes
    Then Verify the record remains at index 3 in the createdOn sorted list
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify external user without permission cannot modify records
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an external user without modification permissions
    And User navigates to the "<LIST_TYPE>" list page
    When User attempts to modify a record
    Then Verify the user is blocked or shown an appropriate error message
    And Verify the record is not modified
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify record on page 2 does not jump to page 1 after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User navigates to page 2 of the paginated list
    And User notes the position of a record on page 2
    When User modifies that record
    And User saves the changes
    Then Verify the record remains on page 2
    And Verify the user remains on page 2 after save
    And Verify the record position is consistent with createdOn sort
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify record on page 3 does not jump unexpectedly after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User navigates to page 3 of the paginated list
    And User notes the position of a record on page 3
    When User modifies that record
    And User saves the changes
    Then Verify the user remains on page 3 after save
    And Verify the record position is consistent with createdOn sort
    And Verify no unexpected pagination jumps occur
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify lastUpdated column exists and is sortable
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User verifies the lastUpdated column is visible in the table header
    And User clicks the lastUpdated column header to manually sort by lastUpdated
    Then Verify the list re-sorts by lastUpdated in ascending order
    When User clicks the lastUpdated column header again
    Then Verify the list re-sorts by lastUpdated in descending order
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify modification is captured in Audit Log with correct timestamp
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User modifies a record
    And User saves the changes
    Then Verify the modification is captured in the Audit Log
    And Verify the Audit Log entry contains the correct timestamp
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify list position updates immediately after saving without manual refresh
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User modifies a record
    And User saves the changes
    Then Verify the record position updates immediately in the UI
    And Verify no manual page refresh is required
    And Verify the list reflects the saved state
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify all list types use consistent default sorting by createdOn
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "<LIST_TYPE_A>" list page
    Then Verify the list displays records sorted by createdOn by default
    When User navigates to the "<LIST_TYPE_B>" list page
    Then Verify the list displays records sorted by createdOn by default
    And Verify both list types use the same default sorting column
    Examples:
      | LIST_TYPE_A     | LIST_TYPE_B     |
      | NPI List        | Keyword List    |
      | NPI List        | Domain/App List |
      | NPI List        | IP List         |
      | NPI List        | Email List      |
      | NPI List        | Pixel List      |
      | Keyword List    | Domain/App List |
      | Keyword List    | IP List         |
      | Keyword List    | Email List      |
      | Keyword List    | Pixel List      |

  @todo
  Scenario Outline: Verify loading indicator displays during list refresh after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User modifies a record
    And User saves the changes
    Then Verify a loading indicator appears during the save and refresh process
    And Verify the list updates after loading completes
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify empty state message displays when no records exist
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User filters or searches such that no records are returned
    Then Verify an empty state message is displayed
    And Verify no sorting issues occur in the empty state
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario: Verify NPI data integrity is maintained during list updates and record modifications
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the NPI List page
    When User modifies an NPI record
    And User saves the changes
    Then Verify NPI data remains intact after modification
    And Verify no data loss or corruption occurs
    And Verify historical data mapping is preserved

  @todo
  Scenario: Verify active sort column is clearly indicated in the UI
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the NPI List page
    Then Verify the active sort column is clearly indicated in the table header
    And Verify users can see that createdOn is the default sort, not lastUpdated

  @todo
  Scenario Outline: Verify search and filter behavior does not impact list sorting
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User applies a filter or search
    Then Verify the filtered results are sorted by createdOn
    And Verify search and filter logic does not impact the default sorting behavior
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |