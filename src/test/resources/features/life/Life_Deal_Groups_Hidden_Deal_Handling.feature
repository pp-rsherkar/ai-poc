Feature: LIFE Regression - Deal Groups Hidden Deal Handling
  Hidden deals are filtered from the Deal Group Add Deals picker while remaining active for tactics already using them.
  Users are clearly informed when a group contains hidden deals and duplicate deal group names are validated.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24696 (R01, GAP-3)
  @todo
  Scenario: Hidden deals are excluded from the Add Deals picker while non-hidden deals appear
    When User opens the Deal Group Add Deals view with at least one hidden deal in the account
    # Framework Gap: Requires step definitions for the Deal Group Add Deals picker in LifeSteps.java
    Then The hidden deals are not shown in the picker
    And All non-hidden deals appear normally in the picker

  # Source: ET-24696 (R02, R05 delivery)
  @todo
  Scenario: A hidden deal already in a deal group stays active for tactics targeting that group
    When User opens a deal group that contains a deal which is later hidden and a tactic targets that group
    # Framework Gap: Requires step definitions for hidden deal delivery in deal groups in LifeSteps.java
    Then The tactic continues to target and deliver using the deal group
    And The hidden deal within the group remains active and does not prevent spending

  # Source: ET-24696 (R03, GAP-1, edge all-hidden)
  @todo
  Scenario Outline: User messaging communicates the presence of hidden deals in a group
    When User opens a deal group where "<CONDITION>"
    # Framework Gap: Requires step definitions for hidden deal messaging in LifeSteps.java
    Then The UI shows "<MESSAGE>"
    Examples:
      | CONDITION                     | MESSAGE                                                     |
      | at least one deal is hidden    | an indicator that the group contains hidden deals           |
      | all deals in the group are hidden | a clear message that all deals are hidden with no crash |

  # Source: ET-24696 (R04, GAP-2, R13 scope)
  @todo
  Scenario Outline: Duplicate deal group name validation is account-scoped
    When User creates a deal group named "<NAME>" given an existing "<EXISTING>"
    # Framework Gap: Requires step definitions for deal group name validation in LifeSteps.java
    Then The result is "<EXPECTED>"
    Examples:
      | NAME       | EXISTING                              | EXPECTED                                          |
      | Test Group | Test Group in the same account         | the specific duplicate name error and no creation |
      | Test Group | Test Group only in a different account | the group is created (name uniqueness is account-scoped) |

  # Source: ET-24696 (HT-5167, HT-6125, HT-5666 regression, edge unhide)
  @todo
  Scenario: Regression - deal filter returns deals, counts stay accurate and unhide restores visibility
    When User with an external account opens the Deal Group Deals view and filters to search for deals
    # Framework Gap: Requires step definitions for deal group filter and count regression in LifeSteps.java
    Then The filter returns the correct deals and no No deals available error appears
    And The applied deals count reflects all deals in the group including hidden ones
    And Deal Groups in the Tactic UI loads without an Unable to Access error
    When User unhides a previously hidden deal
    Then The deal reappears in the Add Deals picker within an acceptable refresh time
