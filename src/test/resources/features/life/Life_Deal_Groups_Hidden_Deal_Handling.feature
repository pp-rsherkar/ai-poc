Feature: Life Deal Groups - Hidden Deal Handling

  1. Filters hidden deals out of the Deal Group Add Deals picker while keeping hidden deals already in a group active.
  2. Communicates clearly when a deal group contains hidden deals.
  3. Validates duplicate deal group names with an account-scoped uniqueness check.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24696 (TC_01, TC_02, TC_03, TC_04, TC_12)
  @todo
  Scenario: Verify hidden deal filtering, in-group activity, messaging, and hide-unhide cycle
    # Framework Gap: Requires page object + step definitions for Deal Group Add Deals and hidden deal handling in LifeDealGroupsSteps.java
    Given User opens the Deal Group Add Deals picker with at least one deal marked hidden
    Then The hidden deal is not shown in the picker while non-hidden deals appear normally
    When User views a tactic that targets a deal group containing a hidden deal
    Then The tactic continues to deliver and the hidden deal within the group remains active
    When User opens a deal group that contains at least one hidden deal
    Then The UI shows a clear message that the group contains hidden deals
    When User unhides a previously hidden deal
    Then The deal reappears in the Add Deals picker within an acceptable refresh time

  # Source: ET-24696 (TC_05, TC_06, TC_13)
  @todo
  Scenario Outline: Verify duplicate deal group name validation and account-scoped uniqueness
    Given User creates a deal group in the account
    When User creates a deal group named "<NAME>" under condition "<CONDITION>"
    Then The result is "<EXPECTED>"
    Examples:
      | NAME       | CONDITION                                | EXPECTED                                               |
      | Test Group | a group named Test Group already exists  | the specific duplicate-name error and no group created |
      | test group | a group named Test Group already exists  | behavior recorded for case sensitivity per GAP-2       |
      | Test Group | created in a second different account    | the group is created as uniqueness is account-scoped   |

  # Source: ET-24696 (TC_07, TC_08, TC_09, TC_10, TC_11)
  @todo
  Scenario: Verify spend continuity, picker filter, count accuracy, access, and all-hidden state
    # Regression anchor: HT-5043 - deal group tactic not spending
    Given User has a tactic targeting a deal group and adds a hidden deal to that group
    Then The tactic continues to deliver and the hidden deal does not prevent spending
    # Regression anchor: HT-5167 - Deal Groups filter showing no deals available
    When An external-user account filters deals in the Deal Group Deals view
    Then The filter returns the correct non-hidden deals with no no-deals-available error
    # Regression anchor: HT-5666 - applied deals count mismatch
    When User views the applied deals count for a group of 10 deals with 3 hidden
    Then The applied deals count correctly reflects all 10 deals
    # Regression anchor: HT-6125 (active) - unable to access Deal Groups in Tactic UI
    When User with the deal groups permission opens Deal Groups from the Tactic UI
    Then The Deal Groups section loads with no unable-to-access error
    When User opens a deal group where all deals are hidden
    Then A clear message explains that all deals in the group are hidden with no crash
