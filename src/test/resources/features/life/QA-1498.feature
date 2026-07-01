Feature: List Sorting and Record Modification Behavior

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"

  @todo
  Scenario Outline: Verify default sorting by createdOn across all list types
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "<LIST_TYPE>" list page
    Then Verify the list displays records sorted by createdOn in ascending order by default
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify record position remains consistent after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the current position of record at index 3
    When User modifies the record at index 3
    And User saves the changes
    Then Verify the record remains at index 3 in the createdOn sorted list
    And Verify the createdOn timestamp of the record is unchanged
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify lastUpdated column does not affect default list sorting
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the position of the oldest record (Record A) and newest record by lastUpdated (Record B)
    When User modifies Record A to have a newer lastUpdated timestamp
    Then Verify Record A remains in its original position based on createdOn
    And Verify Record B remains in its original position based on createdOn
    And Verify the list order is unchanged despite Record A having a newer lastUpdated
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify manual sort persistence after record modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User manually sorts the list by "<SORT_COLUMN>"
    And User notes the position of a record in the manually sorted list
    When User modifies that record
    And User saves the changes
    Then Verify the record remains in the correct position for the "<SORT_COLUMN>" sort
    And Verify the record does not jump to a position based on createdOn
    Examples:
      | LIST_TYPE   | SORT_COLUMN |
      | NPI List    | Name        |
      | Keyword     | Value       |
      | Domain/App  | Domain      |
      | IP          | IP Address  |
      | Email       | Email       |
      | Pixel       | Pixel ID    |

  @todo
  Scenario Outline: Verify rapid edits maintain relative order based on createdOn
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the positions of records at index 2, 5, and 8
    When User modifies the record at index 2
    And User modifies the record at index 5
    And User modifies the record at index 8 in quick succession
    Then Verify all records maintain their original positions based on createdOn
    And Verify no reordering occurs among the modified records
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify clearing optional fields does not change record position
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the current position of record at index 3
    When User modifies the record at index 3 by clearing optional fields
    And User saves the changes
    Then Verify the record remains at index 3 in the createdOn sorted list
    And Verify the createdOn timestamp is unchanged
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify pagination behavior after record modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User navigates to page 2 of the list
    And User notes the position of a record on page 2
    When User modifies that record
    And User saves the changes
    Then Verify the record remains on page 2 at its original position
    And Verify the user remains on page 2 after save
    And Verify no unexpected pagination jumps occur
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify internal user can modify records and maintain position
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an internal user
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the current position of record at index 3
    When User modifies the record at index 3
    And User saves the changes
    Then Verify the record remains at index 3 in the createdOn sorted list
    And Verify the modification is captured in the Audit Log with correct timestamp
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify external user with permission can modify records and maintain position
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an external user with modification permission
    And User navigates to the "<LIST_TYPE>" list page
    And User notes the current position of record at index 3
    When User modifies the record at index 3
    And User saves the changes
    Then Verify the record remains at index 3 in the createdOn sorted list
    And Verify the modification is captured in the Audit Log with correct timestamp
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify external user without permission cannot modify records
    Given "Life" application is logged in successfully with Account "automation@pulsepoint" as an external user without modification permission
    And User navigates to the "<LIST_TYPE>" list page
    When User attempts to modify a record
    Then Verify the user is blocked from modifying the record
    And Verify an appropriate error message or access denied notification is displayed
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify lastUpdated column exists and is sortable
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User views the table column headers
    Then Verify the "Last Updated" column is visible in the table
    When User clicks the "Last Updated" column header
    Then Verify the list re-sorts by Last Updated timestamp in ascending order
    When User clicks the "Last Updated" column header again
    Then Verify the list re-sorts by Last Updated timestamp in descending order
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify list updates immediately after saving without manual refresh
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User modifies a record
    And User saves the changes
    Then Verify the list position updates immediately in the UI
    And Verify no manual page refresh is required
    And Verify the list reflects the saved state
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify modification is captured in Audit Log with correct timestamp
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User modifies a record
    And User saves the changes
    Then Verify an Audit Log entry exists for the modification
    And Verify the Audit Log timestamp matches the modification time within 1 second tolerance
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify consistent sorting behavior across all list types
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to the "NPI List" list page
    Then Verify the list displays records sorted by createdOn in ascending order
    When User navigates to the "Keyword" list page
    Then Verify the list displays records sorted by createdOn in ascending order
    When User navigates to the "Domain/App" list page
    Then Verify the list displays records sorted by createdOn in ascending order
    When User navigates to the "IP" list page
    Then Verify the list displays records sorted by createdOn in ascending order
    When User navigates to the "Email" list page
    Then Verify the list displays records sorted by createdOn in ascending order
    When User navigates to the "Pixel" list page
    Then Verify the list displays records sorted by createdOn in ascending order

  @todo
  Scenario Outline: Verify active sort column is clearly indicated in UI
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User views the table column headers
    Then Verify the createdOn column header displays a visual indicator showing it is the active sort
    And Verify the sort direction (ascending) is clearly indicated
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify loading indicator displays during list refresh after modification
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When User modifies a record
    And User saves the changes
    Then Verify a loading indicator appears during the save and refresh process
    And Verify the list updates after loading completes
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify empty state message displays when no records exist
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    When the list contains no records
    Then Verify an empty state message is displayed
    And Verify no sorting issues occur in the empty state
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |

  @todo
  Scenario Outline: Verify no unexpected pagination jumps when modifying records on page 3
    Given "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the "<LIST_TYPE>" list page
    And User navigates to page 3 of the list
    And User notes the current page number
    When User modifies a record on page 3
    And User saves the changes
    Then Verify the user remains on page 3 after save
    And Verify no unexpected jump to page 1 or other pages occurs
    And Verify the record position is consistent with createdOn sort
    Examples:
      | LIST_TYPE   |
      | NPI List    |
      | Keyword     |
      | Domain/App  |
      | IP          |
      | Email       |
      | Pixel       |