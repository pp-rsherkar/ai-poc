Feature: Life Admin - Life Features Permission Deprecations
  1. Removes the Creative Name BM and Line Item Creative Separation permissions from the Life Features admin table.
  2. Keeps the remaining permissions, adjacent toggles and table rendering intact after removal.
  3. Preserves core tactic and line item workflows for accounts that previously held the deprecated permissions.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24715, PROD-15809, ET-24713, PROD-15806
  @todo
  Scenario Outline: Verify deprecated permissions are absent and unsearchable in the Life Features table
    Given User navigates to the Life Features table in Admin
    Then The "<PERMISSION>" row is absent from the table and all other permission rows are present and intact
    When User searches the Life Features table for "<SEARCH_TERM>"
    Then No results are returned and the search does not error or return stale cached results
    Examples:
      | PERMISSION                    | SEARCH_TERM   |
      | Creative Name BM              | Creative Name |
      | Line Item Creative Separation | Line Item Creative Separation |

  # Source: ET-24715, ET-24713
  @todo
  Scenario: Verify the Life Features table renders correctly after both deprecations
    Given User navigates to the Life Features table in Admin
    # Framework Gap: Requires step definition to assert Life Features table row count and adjacent toggle behaviour in LifeSteps.java
    Then The table renders with no blank or phantom rows and the row count is reduced by exactly 2
    When User toggles a permission adjacent to where each deprecated permission was located
    Then The adjacent permission toggles work correctly and the table saves without a rendering error

  # Source: ET-24715, ET-24713, GAP-1, GAP-2
  @todo
  Scenario: Verify accounts that previously held the deprecated permissions retain core workflows
    Given An account that previously had the Creative Name BM and Line Item Creative Separation permissions enabled
    When User creates and saves a tactic and then creates or edits a line item
    Then The tactic and line item save normally with no permission-related errors and no UI element gated by the removed permissions
    And The existing line item creative separation settings are preserved or the UI is clean with no data corruption

  # Source: ET-24715, ET-24713
  @todo
  Scenario: Verify the two deprecations are isolated and reflected in the audit log
    Given User navigates to the Life Features table in Admin after both deprecation deployments
    Then Neither removal causes the other permission to reappear and the table is stable after both deployments
    When User checks the Admin audit log for table changes
    Then The log shows the removal of each deprecated permission with no erroneous entries
