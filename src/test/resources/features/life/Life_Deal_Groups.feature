Feature: Life Deal Groups - Associated Tactics Metrics and Hidden Deal Handling
  Deal Groups let users bundle private deals and target them from a tactic.
  Users can review per-tactic delivery metrics in the Associated Tactics view of a deal group.
  Deal group membership, metrics, and user messaging stay accurate as deals are added, hidden, or unhidden.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    # Framework Gap: Requires step definitions for navigating to the Deal Groups module from the Tactic targeting UI in LifeSteps.java
    When User opens the Deal Groups module from the Tactic targeting UI
    # Framework Gap: Requires step definitions for opening a named deal group in LifeSteps.java
    And User opens the deal group named "Test Group"

  # Source: ET-24698
  @todo
  Scenario Outline: Associated Tactics enhancements are visible only with the required permission
    # Framework Gap: Requires step definitions for opening the Associated Tactics view of a deal group in LifeSteps.java
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for asserting permission-gated Associated Tactics enhancements in LifeSteps.java
    # Open Question: the exact permission string that gates these enhancements is undocumented and must be confirmed
    Then The Associated Tactics "<enhancement>" is "<visibility>" for a user whose permission is "<permission>"
    Examples:
      | enhancement         | permission | visibility |
      | metric columns      | granted    | visible    |
      | metric columns      | absent     | hidden     |
      | date range selector | granted    | visible    |
      | date range selector | absent     | hidden     |
      | CSV export button   | granted    | visible    |
      | CSV export button   | absent     | hidden     |

  # Source: ET-24698
  @todo
  Scenario Outline: Selecting a date range recalculates the Associated Tactics metrics
    When User opens the Associated Tactics view for the deal group
    # Reuses existing custom date range step from LifeSteps.java
    And User enters the custom date range from "<start_date>" to "<end_date>" and applies the filter
    # Framework Gap: Requires step definitions for asserting recalculated Associated Tactics metric values in LifeSteps.java
    # Open Question: exact metric column labels are defined in Figma and must be confirmed
    Then The Associated Tactics metric "Spend" returns a "<result_type>" value and not blank
    Examples:
      | start_date  | end_date | result_type        |
      | 30_days_ago | today    | populated non-zero |
      | today       | today    | meaningful zero    |

  # Source: ET-24698
  @todo
  Scenario Outline: Sorting a metric column reorders the tactics and shows a sort indicator
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for sorting an Associated Tactics metric column in LifeSteps.java
    # Open Question: exact metric column labels are defined in Figma; "Spend" used here as a placeholder
    And User sorts the metric column "Spend" in "<direction>" order
    # Framework Gap: Requires step definitions for asserting tactic sort order and sort indicator in LifeSteps.java
    Then The tactics are reordered by "Spend" in "<direction>" order
    And A sort indicator is displayed on the "Spend" column header
    Examples:
      | direction  |
      | ascending  |
      | descending |

  # Source: ET-24698
  @todo
  Scenario: CSV export downloads a file that matches the current Associated Tactics view
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for triggering the Associated Tactics CSV export in LifeSteps.java
    And User clicks the CSV export button in the Associated Tactics view
    # Framework Gap: Requires step definitions for validating a downloaded CSV against the on-screen view in LifeSteps.java
    # Open Question: the CSV file format and column order are unspecified and must be confirmed
    Then A CSV file is downloaded whose rows and columns match the displayed Associated Tactics view

  # Source: ET-24698
  @todo
  Scenario: The 'Show Tactics From Other Accounts' checkbox is not present in the Associated Tactics view
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for asserting absence of the descoped checkbox in LifeSteps.java
    Then The "Show Tactics From Other Accounts" checkbox is not displayed in the Associated Tactics view

  # Source: ET-24698
  @todo
  Scenario: Associated Tactics view populates the tactics that target the deal group
    Given The deal group "Test Group" is targeted by the following tactics
      | tactic_name |
      | Tactic A    |
      | Tactic B    |
      | Tactic C    |
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for asserting the tactics listed in Associated Tactics in LifeSteps.java
    Then The Associated Tactics view lists all 3 targeting tactics

  # Source: ET-24698
  # HT-5521
  @todo
  Scenario: Metric values in Associated Tactics are not inflated by duplicate joins
    Given The deal group "Test Group" is applied to 16 deals
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for asserting a non-inflated applied deals metric in LifeSteps.java
    Then The applied deals metric shows "16" and not the inflated value "174"

  # Source: ET-24698
  # HT-5419
  @todo
  Scenario: All tactics targeting the deal group appear in Associated Tactics
    Given The deal group "Test Group" is targeted by 5 tactics
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for asserting the count of populated tactics in LifeSteps.java
    Then The Associated Tactics view lists all 5 tactics and none are missing

  # Source: ET-24698
  # HT-6125
  @todo
  Scenario: A user with the Deal Groups permission can open a deal group from the Tactic UI
    # Framework Gap: Requires step definitions for asserting Deal Groups access for a permissioned user in LifeSteps.java
    Given The current user has the Deal Groups permission granted
    When User opens the Deal Groups module from the Tactic targeting UI
    Then The deal group "Test Group" is accessible and its Associated Tactics view can be opened

  # Source: ET-24698
  @todo
  Scenario: Filtering Associated Tactics by a metric shows only matching tactics
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for filtering Associated Tactics by a metric threshold in LifeSteps.java
    And User filters the Associated Tactics where "Spend" is greater than "$100"
    # Framework Gap: Requires step definitions for asserting the filtered Associated Tactics result set in LifeSteps.java
    Then Only tactics whose "Spend" is greater than "$100" are displayed

  # Source: ET-24698
  @todo
  Scenario: A deal group with zero targeting tactics shows an empty state rather than an error
    Given The deal group "test group" is targeted by 0 tactics
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for asserting the Associated Tactics empty state in LifeSteps.java
    Then The Associated Tactics view shows an empty state message and no error

  # Source: ET-24698
  @todo
  Scenario: CSV export row set stays consistent with the applied Associated Tactics filter
    When User opens the Associated Tactics view for the deal group
    And User filters the Associated Tactics where "Spend" is greater than "$100"
    And User clicks the CSV export button in the Associated Tactics view
    # Framework Gap: Requires step definitions for asserting CSV rows against the active filter in LifeSteps.java
    # Open Question: whether CSV export returns filtered rows or all rows must be confirmed and documented
    Then The exported CSV rows match the filtered Associated Tactics view consistently

  # Source: ET-24698
  # HT-5666
  @todo
  Scenario: Applied deals count stays accurate after viewing Associated Tactics
    Given The deal group "Test Group" is applied to 16 deals
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for returning to the deal group applied deals view in LifeSteps.java
    And User returns to the deal group applied deals view
    # Framework Gap: Requires step definitions for asserting the applied deals count in LifeSteps.java
    Then The applied deals count still shows "16"

  # Source: ET-24698
  @todo
  Scenario: The metrics view does not expose tactic data from other accounts
    When User opens the Associated Tactics view for the deal group
    # Framework Gap: Requires step definitions for asserting account-scoped tactic data in LifeSteps.java
    Then Only tactics from the current account "automation@pulsepoint" are displayed
    And No tactic data from other accounts is exposed in the metrics or the CSV export

  # Source: ET-24696
  @todo
  Scenario: Hidden deals are filtered out of the Add Deals picker while non-hidden deals remain
    # Framework Gap: Requires step definitions for opening the Add Deals picker of a deal group in LifeSteps.java
    When User opens the Add Deals picker for the deal group
    # Framework Gap: Requires step definitions for asserting deal visibility in the Add Deals picker in LifeSteps.java
    Then The Add Deals picker shows the deals as follows
      | deal_name | is_hidden | shown_in_picker |
      | Deal 01   | no        | yes             |
      | Deal 02   | no        | yes             |
      | Deal 03   | yes       | no              |
      | Deal 04   | yes       | no              |

  # Source: ET-24696
  @todo
  Scenario: A hidden deal already in the group stays active for tactics
    Given The deal group "Test Group" already contains the hidden deal "Deal 03"
    # Framework Gap: Requires step definitions for asserting a hidden deal remains active in a deal group in LifeSteps.java
    When User reviews the deals applied to the deal group
    Then The hidden deal "Deal 03" is still active and available for tactic targeting

  # Source: ET-24696
  @todo
  Scenario: The user sees clear messaging when the deal group contains hidden deals
    Given The deal group "Test Group" contains 3 hidden deals and 7 visible deals
    When User reviews the deals applied to the deal group
    # Framework Gap: Requires step definitions for asserting the hidden-deals messaging in LifeSteps.java
    # Open Question: exact hidden-deals message text is defined in Figma and must be confirmed
    Then A clear message informs the user that the group contains hidden deals

  # Source: ET-24696
  @todo
  Scenario: Creating a deal group with an existing name shows a specific duplicate-name error and is not created
    Given A deal group named "Test Group" already exists in the account
    # Framework Gap: Requires step definitions for creating a deal group with a given name in LifeSteps.java
    When User attempts to create a deal group named "Test Group"
    # Framework Gap: Requires step definitions for asserting the duplicate-name error message in LifeSteps.java
    # Open Question: exact duplicate-name error text is defined in Figma and must be confirmed
    Then A specific duplicate-name error message is shown
    And The deal group is not created

  # Source: ET-24696
  @todo
  Scenario Outline: Duplicate deal group name check case sensitivity is documented
    Given A deal group named "Test Group" already exists in the account
    When User attempts to create a deal group named "<new_name>"
    # Framework Gap: Requires step definitions for asserting case-sensitive duplicate-name handling in LifeSteps.java
    # Open Question: whether the uniqueness check is case sensitive must be confirmed and documented
    Then The creation result is "<expected_result>"
    Examples:
      | new_name   | expected_result      |
      | Test Group | blocked as duplicate |
      | test group | documented behaviour |

  # Source: ET-24696
  @todo
  Scenario: A tactic targeting a group with hidden deals keeps spending after a hidden deal is added
    Given A tactic targets the deal group "Test Group" and is spending
    # Framework Gap: Requires step definitions for adding a hidden deal to a deal group in LifeSteps.java
    When A hidden deal "Deal 03" is added to the deal group
    # Framework Gap: Requires step definitions for asserting tactic delivery continuity in LifeSteps.java
    Then The tactic continues to spend against the deal group without interruption

  # Source: ET-24696
  # HT-5167
  @todo
  Scenario: The Add Deals picker returns the correct deals with no false 'no deals available' message
    Given The account has deals that are eligible for the deal group
    When User opens the Add Deals picker for the deal group
    # Framework Gap: Requires step definitions for asserting the Add Deals picker result set in LifeSteps.java
    Then The Add Deals picker lists the eligible non-hidden deals
    And No false "no deals available" message is shown

  # Source: ET-24696
  # HT-5666
  @todo
  Scenario: The applied deals count is accurate when the group contains hidden deals
    Given The deal group "Test Group" contains 3 hidden deals and 7 visible deals
    When User reviews the deals applied to the deal group
    # Framework Gap: Requires step definitions for asserting the applied deals count with hidden deals in LifeSteps.java
    Then The applied deals count shows "10" covering 3 hidden and 7 visible deals

  # Source: ET-24696
  # HT-6125
  @todo
  Scenario: Deal Groups are accessible in the Tactic UI for eligible users
    Given The current user is eligible to access Deal Groups
    When User opens the Deal Groups module from the Tactic targeting UI
    # Framework Gap: Requires step definitions for asserting Deal Groups accessibility in the Tactic UI in LifeSteps.java
    Then The deal group "Test Group" can be opened from the Tactic targeting UI

  # Source: ET-24696
  @todo
  Scenario: A deal group where all deals are hidden shows a clear message and does not crash
    Given The deal group "test group" contains only hidden deals
    When User reviews the deals applied to the deal group
    # Framework Gap: Requires step definitions for asserting the all-hidden deal group message in LifeSteps.java
    # Open Question: exact all-hidden message text is defined in Figma and must be confirmed
    Then A clear message is shown that all deals in the group are hidden
    And The deal group view does not crash

  # Source: ET-24696
  @todo
  Scenario: Re-unhiding a deal makes it reappear in the Add Deals picker
    Given The deal "Deal 03" is currently hidden and excluded from the Add Deals picker
    # Framework Gap: Requires step definitions for unhiding a deal in LifeSteps.java
    When User unhides the deal "Deal 03"
    And User opens the Add Deals picker for the deal group
    Then The deal "Deal 03" reappears in the Add Deals picker

  # Source: ET-24696
  @todo
  Scenario: Deal group name uniqueness is enforced per account and not globally
    Given A deal group named "Test Group" exists in another account
    When User attempts to create a deal group named "Test Group" in the current account
    # Framework Gap: Requires step definitions for asserting account-scoped name uniqueness in LifeSteps.java
    # Open Question: whether hidden scope and name uniqueness are account-scoped or global must be confirmed
    Then The deal group is created successfully because uniqueness is account-scoped
