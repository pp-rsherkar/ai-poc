Feature: List Sorting and Record Modification Behavior

  @todo
  Scenario Outline: Verify default sorting by createdOn across all list types
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "<LIST_TYPE>" list page
    Then Verify records are displayed sorted by createdOn in ascending order
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3 in the createdOn sorted list
    When User clicks on the record at index 3 to open it
    And User modifies a field in the record
    And User saves the record
    Then Verify the record remains at index 3 in the createdOn sorted list
    And Verify the createdOn timestamp of the record is unchanged
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify lastUpdated column is not used for default list sorting
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of the first record in the default createdOn sorted list
    When User modifies the first record to update its lastUpdated timestamp
    And User saves the record
    Then Verify the record remains in the first position based on createdOn
    And Verify the list order is unchanged despite the record having a newer lastUpdated timestamp
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the positions of records at index 2, 4, and 6 in the createdOn sorted list
    When User modifies the record at index 2 and saves it
    And User modifies the record at index 4 and saves it
    And User modifies the record at index 6 and saves it
    Then Verify all three records maintain their original positions in the createdOn sorted list
    And Verify the relative order of all records remains unchanged
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3 in the createdOn sorted list
    When User clicks on the record at index 3 to open it
    And User clears an optional field in the record
    And User saves the record
    Then Verify the record remains at index 3 in the createdOn sorted list
    And Verify the createdOn timestamp of the record is unchanged
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User clicks the "<SORT_COLUMN>" column header to manually sort by that column
    And User notes the position of a record in the manually sorted list
    When User clicks on the record to open it
    And User modifies a field in the record
    And User saves the record
    Then Verify the record remains in the correct position for the "<SORT_COLUMN>" manual sort
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
  Scenario Outline: Verify internal user modification maintains record position
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3 in the createdOn sorted list
    When User clicks on the record at index 3 to open it
    And User modifies a field in the record
    And User saves the record
    Then Verify the record remains at index 3 in the createdOn sorted list
    And Verify the createdOn timestamp of the record is unchanged
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User has permission to modify records in the "<LIST_TYPE>" list
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of record at index 3 in the createdOn sorted list
    When User clicks on the record at index 3 to open it
    And User modifies a field in the record
    And User saves the record
    Then Verify the record remains at index 3 in the createdOn sorted list
    And Verify the createdOn timestamp of the record is unchanged
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User does not have permission to modify records in the "<LIST_TYPE>" list
    And User navigates to the "<LIST_TYPE>" list page
    When User attempts to click on a record to open it
    Then Verify the record cannot be opened or modified
    And Verify an appropriate error message or access denied notification is displayed
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User navigates to page 2 of the paginated list
    And User notes the position of a record on page 2
    When User clicks on the record to open it
    And User modifies a field in the record
    And User saves the record
    Then Verify the user remains on page 2 after the save
    And Verify the record maintains its position on page 2 based on createdOn sort
    And Verify the record does not jump to page 1
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify record on page 3 does not cause unexpected pagination jumps
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User navigates to page 3 of the paginated list
    And User notes the position of a record on page 3
    When User clicks on the record to open it
    And User modifies a field in the record
    And User saves the record
    Then Verify the user remains on page 3 after the save
    And Verify the record maintains its position on page 3 based on createdOn sort
    And Verify no unexpected jump to page 1 or other pages occurs
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify lastUpdated column exists and is visible in list UI
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "<LIST_TYPE>" list page
    Then Verify the "Last Updated" column is visible in the table header
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario Outline: Verify clicking lastUpdated column header re-sorts the list
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User clicks the lastUpdated column header to manually sort by lastUpdated
    Then Verify the list re-sorts by Last Updated timestamp in ascending order
    When User clicks the lastUpdated column header again
    Then Verify the list re-sorts by Last Updated timestamp in descending order
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User clicks on a record to open it
    And User modifies a field in the record
    And User saves the record
    Then Verify an Audit Log entry exists for the modification
    And Verify the Audit Log entry contains the correct timestamp within 1 second tolerance
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User clicks on a record to open it
    And User modifies a field in the record
    And User saves the record
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
  Scenario Outline: Verify search and filter interaction does not impact list sorting
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User applies a filter
    Then Verify the filtered list is displayed sorted by createdOn
    When User modifies a record in the filtered list
    And User saves the record
    Then Verify the record maintains its position in the filtered createdOn sorted list
    And Verify the filter logic does not impact list sorting
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "<LIST_TYPE_A>" list page
    Then Verify records are displayed sorted by createdOn in ascending order
    When User navigates to the "<LIST_TYPE_B>" list page
    Then Verify records are displayed sorted by createdOn in ascending order
    And Verify both list types use the same default sorting behavior
    Examples:
      | LIST_TYPE_A  | LIST_TYPE_B     |
      | NPI List     | Keyword List    |
      | Domain/App List | IP List      |
      | Email List   | Pixel List      |

  @todo
  Scenario Outline: Verify loading indicator displays during list refresh after modification
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User clicks on a record to open it
    And User modifies a field in the record
    And User saves the record
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
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "<LIST_TYPE>" list page with no records
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
  Scenario Outline: Verify NPI data integrity is maintained during list updates
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "NPI List" list page
    When User clicks on an NPI record to open it
    And User modifies a field in the record
    And User saves the record
    Then Verify NPI data remains intact after modification
    And Verify no data loss or corruption occurs
    And Verify historical data mapping is preserved
    Examples:
      | LIST_TYPE |
      | NPI List  |

  @todo
  Scenario Outline: Verify active sort column is clearly indicated in list UI
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "<LIST_TYPE>" list page
    Then Verify the active sort column is clearly indicated in the UI
    And Verify users can see that createdOn is the default sort, not lastUpdated
    Examples:
      | LIST_TYPE    |
      | NPI List     |
      | Keyword List |
      | Domain/App List |
      | IP List      |
      | Email List   |
      | Pixel List   |

  @todo
  Scenario: Verify NPI List sorting behavior matches confirmed behavior from ET-20452
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "NPI List" list page
    Then Verify the list displays records sorted by createdOn in ascending order
    And Verify lastUpdated is not used for default sort
    And Verify the sorting behavior matches the confirmed behavior from ET-20452

  @todo
  Scenario: Verify UI consistency across all list types when popup displays with black background
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "NPI List" list page
    When User opens a record detail popup
    Then Verify a black background displays behind the popup
    When User navigates to the "Keyword List" list page
    And User opens a record detail popup
    Then Verify a black background displays behind the popup
    And Verify UI consistency across all list types

  @todo
  Scenario: Verify search behavior and list display behavior are unified across all list panels
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "NPI List" list page
    When User applies a search filter
    Then Verify search results are displayed consistently
    And Verify search/filter logic does not impact list sorting
    When User navigates to the "Keyword List" list page
    And User applies a search filter
    Then Verify search results are displayed consistently across all list types