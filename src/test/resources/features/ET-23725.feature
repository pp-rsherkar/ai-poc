Feature: Life: Remove Inactive Inventory Sources (100% Passback Accounts)

  As an internal user,
  I want to remove inactive inventory sources,
  So that passback accounts are cleaned up and data remains accurate.

  @todo
  Scenario: Verify removal of inactive inventory sources
    Given "Internal User" is logged in successfully
    And User navigates to the Inventory Sources management page
    When User filters inventory sources by status "Inactive"
    And User selects all filtered inactive sources
    And User clicks the "Remove" button
    Then Confirm removal prompt appears
    When User confirms the removal
    Then The selected inactive inventory sources are removed successfully
    And A success message "Inactive sources removed" is displayed

  @todo
  Scenario: Validate error when attempting removal without selection
    Given "Internal User" is logged in successfully
    And User navigates to the Inventory Sources management page
    When User clicks the "Remove" button without selecting any sources
    Then An error message "Please select sources to remove" is displayed

  @todo
  Scenario: Verify permission restrictions for removal
    Given "Read-Only User" is logged in successfully
    And User navigates to the Inventory Sources management page
    When User filters inventory sources by status "Inactive"
    And User attempts to select sources and click "Remove"
    Then The "Remove" button is disabled
    And User sees an alert "Insufficient permissions to remove sources"

  @todo
  Scenario: Remove inactive sources with associated campaigns
    Given "Internal User" is logged in successfully
    And User navigates to the Inventory Sources management page
    When User filters sources with active campaigns associated
    And User selects sources with campaigns
    And User clicks "Remove"
    Then A warning "Sources with active campaigns cannot be removed" is displayed
    And No sources are removed

  @todo
  Scenario: Verify removal process logs activity
    Given "Internal User" is logged in successfully
    And User navigates to the Inventory Sources management page
    When User filters and selects inactive sources
    And User removes selected sources
    Then The system logs the removal activity with timestamp and user details
    And The activity log contains entries for each removal action
