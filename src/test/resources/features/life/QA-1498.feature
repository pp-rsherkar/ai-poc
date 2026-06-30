Feature: List Sorting and Record Modification Behavior Across All List Types

  @todo
  Scenario Outline: Verify default sorting by createdOn for all list types
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User views the list in default state
    Then Verify records are sorted by createdOn in ascending order
    And Verify the oldest record appears first and the newest record appears last
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify record position remains unchanged after modification when sorted by createdOn
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3 in the createdOn sorted list
    When User clicks on the record at index 3 to open edit view
    And User modifies a field in the record
    And User saves the record
    Then Verify the record remains at index 3 in the list
    And Verify the createdOn timestamp of the record is unchanged
    And Verify the record position is consistent with createdOn sort order
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify lastUpdated column does not affect default list sorting
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of the first record in the createdOn sorted list
    When User modifies the first record to update its lastUpdated timestamp
    And User saves the record
    Then Verify the first record remains in the first position
    And Verify the list order is unchanged despite the updated lastUpdated value
    And Verify lastUpdated is not used for default list view sorting
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify rapid edits maintain relative order based on createdOn
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the positions of records at index 2, 4, and 6
    When User rapidly modifies record at index 2
    And User rapidly modifies record at index 4
    And User rapidly modifies record at index 6
    And User saves all records
    Then Verify all records maintain their original positions based on createdOn
    And Verify no reordering occurs after rapid edits
    And Verify relative order of records is unchanged
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify clearing optional fields does not change record position
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3
    When User clicks on the record at index 3 to open edit view
    And User clears an optional field in the record
    And User saves the record
    Then Verify the record remains at index 3
    And Verify the createdOn timestamp is unchanged
    And Verify the record position is consistent with createdOn sort order
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify manual sort is respected after record modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User clicks the "<SORT_COLUMN>" column header to manually sort by that column
    And User notes the position of a record in the manually sorted list
    When User clicks on the record to open edit view
    And User modifies a field in the record
    And User saves the record
    Then Verify the record remains in the correct position for the "<SORT_COLUMN>" sort
    And Verify the record does not jump to a position based on createdOn
    And Verify the manual sort order is maintained after save
    Examples:
      | LIST_TYPE    | SORT_COLUMN |
      | NPI List     | Name        |
      | Keyword List | Value       |
      | Domain/App   | Domain      |
      | IP List      | IP Address  |
      | Email List   | Email       |
      | Pixel List   | Pixel ID    |

  @todo
  Scenario Outline: Verify internal user can modify record and position is maintained
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an internal user
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3
    When User clicks on the record at index 3 to open edit view
    And User modifies a field in the record
    And User saves the record
    Then Verify the record remains at index 3
    And Verify the createdOn timestamp is unchanged
    And Verify the record position is consistent with createdOn sort order
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify internal user has permission to modify records
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an internal user
    And User navigates to the "<LIST_TYPE>" list page
    When User clicks on a record to open edit view
    Then Verify the edit form is displayed
    And Verify the Save button is enabled
    And Verify the user can modify and save the record
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify external user is blocked from modifying records when not permitted
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an external user without modify permission
    And User navigates to the "<LIST_TYPE>" list page
    When User clicks on a record to open edit view
    Then Verify the edit form is not displayed or is read-only
    And Verify an appropriate error message is shown
    And Verify the user cannot modify or save the record
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify external user can modify record when permitted and position is maintained
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an external user with modify permission
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3
    When User clicks on the record at index 3 to open edit view
    And User modifies a field in the record
    And User saves the record
    Then Verify the record remains at index 3
    And Verify the createdOn timestamp is unchanged
    And Verify the record position is consistent with createdOn sort order
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify record on page 2 does not jump to page 1 after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User navigates to page 2 of the list
    And User notes the position of a record on page 2
    When User clicks on the record to open edit view
    And User modifies a field in the record
    And User saves the record
    Then Verify the user remains on page 2
    And Verify the record remains on page 2 at its original position
    And Verify the record does not jump to page 1
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify record on page 3 does not jump unexpectedly after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User navigates to page 3 of the list
    And User notes the position of a record on page 3
    When User clicks on the record to open edit view
    And User modifies a field in the record
    And User saves the record
    Then Verify the user remains on page 3
    And Verify the record remains on page 3 at its original position
    And Verify no unexpected pagination jumps occur
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify lastUpdated column exists and is visible in list UI
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User views the table header
    Then Verify the lastUpdated column is visible in the table header
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify clicking lastUpdated column header re-sorts the list
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User clicks the lastUpdated column header
    Then Verify the list re-sorts by lastUpdated in ascending order
    When User clicks the lastUpdated column header again
    Then Verify the list re-sorts by lastUpdated in descending order
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify modification is captured in Audit Log with correct timestamp
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User clicks on a record to open edit view
    And User modifies a field in the record
    And User saves the record
    And User navigates to the Audit Log
    Then Verify an Audit Log entry exists for the modification
    And Verify the Audit Log entry contains the correct timestamp
    And Verify the timestamp is within 1 second tolerance of the save time
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify list position updates immediately after saving without page refresh
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the current list state
    When User clicks on a record to open edit view
    And User modifies a field in the record
    And User saves the record
    Then Verify the list updates immediately in the UI
    And Verify no manual page refresh is required
    And Verify the list reflects the saved state
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify active manual lastUpdated sort is respected after record save
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User clicks the lastUpdated column header to manually sort by lastUpdated
    And User notes the position of a record in the lastUpdated sorted list
    When User clicks on the record to open edit view
    And User modifies a field in the record
    And User saves the record
    Then Verify the record position updates according to the active lastUpdated sort
    And Verify the manual sort is not lost after save
    And Verify the record is repositioned based on its new lastUpdated value
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify all list types use consistent default sorting by createdOn
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "<LIST_TYPE_A>" list page
    Then Verify records are sorted by createdOn in ascending order
    When User navigates to the "<LIST_TYPE_B>" list page
    Then Verify records are sorted by createdOn in ascending order
    And Verify both list types display consistent createdOn sorting behavior
    Examples:
      | LIST_TYPE_A  | LIST_TYPE_B  |
      | NPI List     | Keyword List |
      | NPI List     | Domain/App   |
      | NPI List     | IP List      |
      | Keyword List | Email List   |
      | Domain/App   | Pixel List   |

  @todo
  Scenario Outline: Verify loading indicator displays during list refresh after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User clicks on a record to open edit view
    And User modifies a field in the record
    And User saves the record
    Then Verify a loading indicator appears during the save and refresh process
    And Verify the list updates after loading completes
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify empty state message displays when no records exist
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User filters or searches to display zero records
    Then Verify an empty state message is displayed
    And Verify no sorting issues occur in the empty state
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify NPI data integrity is maintained during list updates
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the NPI List page
    And User notes the NPI data values before modification
    When User clicks on an NPI record to open edit view
    And User modifies a field in the record
    And User saves the record
    Then Verify NPI data remains intact after modification
    And Verify no data loss or corruption occurs
    And Verify historical data mapping is preserved
    Examples:
      | placeholder |
      | value       |

  @todo
  Scenario Outline: Verify search and filter interaction with list sorting
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User enters a search term in the search bar
    And User applies a filter
    Then Verify search results are displayed consistently
    And Verify the filtered list maintains createdOn sort order
    And Verify search and filter logic does not impact list sorting
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App   |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario: Verify active sort column is clearly indicated in the UI
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the NPI List page
    When User views the table header
    Then Verify the active sort column is clearly indicated
    And Verify users can see that createdOn is the default sort
    And Verify users can see that lastUpdated is not the default sort