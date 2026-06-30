Feature: List Sorting and Record Modification Behavior

  @todo
  Scenario Outline: Verify default sorting by createdOn across all list types
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User views the list in default state
    Then the list displays records sorted by createdOn in ascending order
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify record position remains unchanged after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3 in the createdOn sorted list
    When User modifies the record and saves the changes
    Then the record remains at the same position in the createdOn sorted list
    And the createdOn timestamp of the record is unchanged
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify lastUpdated is not used for default list sorting
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And the list displays records sorted by createdOn
    When User modifies the oldest record to have a newer lastUpdated timestamp
    Then the record remains in its original position based on createdOn
    And the list order is unchanged despite the record having a newer lastUpdated value
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify rapid edits maintain relative order by createdOn
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the positions of records at indices 2, 5, and 8
    When User modifies multiple records in quick succession and saves each change
    Then all records maintain their original positions based on createdOn
    And the relative order of records remains unchanged
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
    When User modifies the record by clearing optional fields and saves the changes
    Then the record remains at position 3 in the createdOn sorted list
    And the createdOn timestamp is unchanged
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
    And User manually sorts the list by the "<SORT_COLUMN>" column
    And User notes the position of a record in the manually sorted list
    When User modifies the record and saves the changes
    Then the record remains in the correct position for the "<SORT_COLUMN>" sort
    And the record does not jump to a position based on createdOn
    Examples:
      | LIST_TYPE       | SORT_COLUMN |
      | NPI List        | Name        |
      | Keyword List    | Value       |
      | Domain/App List | Domain      |
      | IP List         | IP Address  |
      | Email List      | Email       |
      | Pixel List      | Pixel ID    |

  @todo
  Scenario Outline: Verify internal user modification maintains record position
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an internal user
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3
    When User modifies the record and saves the changes
    Then the record remains at position 3 in the createdOn sorted list
    And the record position is consistent with createdOn sort
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify external user with permission can modify record and maintain position
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an external user with modification permissions
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3
    When User modifies the record and saves the changes
    Then the record remains at position 3 in the createdOn sorted list
    And the record position is consistent with createdOn sort
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify external user without permission cannot modify record
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an external user without modification permissions
    And User navigates to the "<LIST_TYPE>" list page
    When User attempts to modify a record
    Then the user is blocked from making the modification
    And an appropriate error message is displayed
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
    When User modifies the record and saves the changes
    Then the record remains on page 2 at its original position
    And the user remains on page 2 after the save operation
    And no unexpected pagination jump occurs
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify record on page 3 does not jump to page 1 after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User navigates to page 3 of the paginated list
    And User notes the position of a record on page 3
    When User modifies the record and saves the changes
    Then the record remains on page 3 at its original position
    And the user remains on page 3 after the save operation
    And no unexpected pagination jump occurs
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify Last Updated column exists and is sortable
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User views the table header
    Then the Last Updated column is visible in the table
    And User clicks the Last Updated column header
    Then the list re-sorts by Last Updated timestamp in ascending order
    And User clicks the Last Updated column header again
    Then the list re-sorts by Last Updated timestamp in descending order
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
    When User modifies a record and saves the changes
    And User navigates to the Audit Log
    Then an Audit Log entry exists for the modification
    And the Audit Log entry contains the correct timestamp within 1 second tolerance
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify list position updates immediately after save without page refresh
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User modifies a record and saves the changes
    Then the list updates immediately in the UI
    And the record position reflects the saved state
    And no manual page refresh is required
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
    When the list contains no records
    Then an empty state message is displayed
    And no sorting issues occur in the empty state
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify loading indicator displays during list refresh after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User modifies a record and saves the changes
    Then a loading indicator appears during the save and refresh process
    And the list updates after the loading completes
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
    Then the list displays records sorted by createdOn by default
    And User navigates to the "<LIST_TYPE_B>" list page
    Then the list displays records sorted by createdOn by default
    And both list types maintain consistent sorting behavior
    Examples:
      | LIST_TYPE_A     | LIST_TYPE_B     |
      | NPI List        | Keyword List    |
      | Domain/App List | IP List         |
      | Email List      | Pixel List      |

  @todo
  Scenario: Verify active sort column is clearly indicated in the UI
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the NPI List page
    When User views the table header
    Then the active sort column is clearly indicated
    And the createdOn column is marked as the default sort
    And users can see that createdOn is the default sort, not lastUpdated

  @todo
  Scenario: Verify NPI data integrity is maintained during list updates
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the NPI List page
    And User notes the NPI data before modification
    When User modifies an NPI record and saves the changes
    Then the NPI data remains intact after modification
    And no data loss or corruption occurs
    And historical data mapping is preserved