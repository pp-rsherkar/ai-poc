Feature: Deal Sharing to Accounts within Account Group

  Background:
    Given This scenario will be executed in the "Pre-release" environment as a "User"
    And "Studio" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Administrative section
    And User navigates to Accounts Tab
    And User searches the account "PP engineering test" and checks Studio permissions
    And User clicks PulsePoint icon to navigate back to Life
    And User navigates to Studio application

  @todo
  Scenario: Share deal to all accounts in an account group
    When User clicks on Add New Deal button
    And User selects an account group containing multiple accounts
    And User selects "All Affiliated Accounts" option to share the deal
    And User saves the deal
    Then All accounts in the selected group should have access to the deal

  @todo
  Scenario: Share deal to a subset of accounts within an account group
    When User clicks on Add New Deal button
    And User selects an account group containing multiple accounts
    And User selects specific accounts within the group to share the deal
    And User saves the deal
    Then Only the selected accounts should have access to the deal

  @todo
  Scenario: User views tooltip on "All Affiliated Accounts" option
    When User clicks on Add New Deal button
    And User selects an account group containing multiple accounts
    And User hovers over "All Affiliated Accounts"
    Then User sees a tooltip explaining the option

  @todo
  Scenario: Validation error when no account is selected for deal sharing
    When User clicks on Add New Deal button
    And User selects an account group containing multiple accounts
    And User does not select any account for sharing
    And User saves the deal
    Then An error message is displayed indicating at least one account must be selected

  @todo
  Scenario: User without permission cannot share deal to account group
    Given This scenario will be executed in the "Pre-release" environment as a "External User"
    And "Studio" application is logged in successfully with Account "external@pulsepoint"
    When User clicks on Add New Deal button
    And User selects an account group containing multiple accounts
    Then User does not see the option to share deal to account group

  @todo
  Scenario Outline: Share deal to accounts with invalid selection
    When User clicks on Add New Deal button
    And User selects an account group containing multiple accounts
    And User selects "<INVALID_ACCOUNT>" not belonging to the group
    And User saves the deal
    Then An error message is displayed indicating invalid account selection
    Examples:
      | INVALID_ACCOUNT |
      | UnaffiliatedAccount |
      | DisabledAccount     |
