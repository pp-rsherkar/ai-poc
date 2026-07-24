Feature: Life Admin Life Features Permission Table

  1. The Admin Life Features table lists the permissions that govern Life account capabilities, with each permission shown as a labeled, toggleable row.
  2. Administrators can view, search, and toggle the supported Life Features permissions, and each change saves and takes effect for the selected account.
  3. The Life Features table renders consistently across accounts and reflects the current set of supported permissions.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to Administrative section
    # Framework Gap: Requires step definitions for opening the Life Features permission table in LifeSteps.java
    And User opens the "Life Features" permission table

  # Source: ET-24715, ET-24713
  @todo
  Scenario Outline: Deprecated permission row is removed while adjacent Life Features rows stay intact
    # Framework Gap: Requires step definitions for asserting a permission row is absent from the Life Features table in LifeSteps.java
    Then The Life Features table does not display a row for "<permission>"
    # Framework Gap: Requires step definitions for asserting a permission is not selectable in LifeSteps.java
    And The permission "<permission>" is not available to select or enable
    # Framework Gap: Requires step definitions for verifying the remaining permission rows are intact in LifeSteps.java
    And All other Life Features permission rows remain present and intact
    # Framework Gap: Requires step definitions for toggling an adjacent permission row in LifeSteps.java
    When User toggles an adjacent permission row in the Life Features table
    # Framework Gap: Requires step definitions for verifying an adjacent permission toggle saves in LifeSteps.java
    Then The adjacent permission row toggles and saves successfully
    # Framework Gap: Requires step definitions for verifying the table has no orphaned or blank rows in LifeSteps.java
    And The Life Features table shows no orphaned or blank rows

    Examples:
      | permission                    |
      | Creative Name BM              |
      | Line Item Creative Separation |

  # Source: ET-24715
  @todo
  Scenario: Creative Name BM is inert with no gated UI and account tactics still save
    # Framework Gap: Requires step definitions for asserting no UI element is gated by a permission in LifeSteps.java
    Then No UI element in the Life application is gated by the "Creative Name BM" permission
    # Framework Gap: Requires step definitions for asserting a deprecated permission string is inert in LifeSteps.java
    And The "Creative Name BM" permission string is inert and enforces nothing
    # Framework Gap: Requires step definitions for opening an account that previously had a permission enabled in LifeSteps.java
    When User opens an account that previously had "Creative Name BM" enabled
    # Framework Gap: Requires step definitions for creating and saving a tactic for the selected account in LifeSteps.java
    And User creates and saves a tactic for that account
    # Framework Gap: Requires step definitions for verifying a tactic saves without error in LifeSteps.java
    Then The tactic is created and saved without error

  # Source: ET-24715
  @todo
  Scenario: Searching the Life Features table for Creative Name returns no results without error
    # Framework Gap: Requires step definitions for searching within the Life Features table in LifeSteps.java
    When User searches the Life Features table for "Creative Name"
    # Framework Gap: Requires step definitions for asserting an empty Life Features search result in LifeSteps.java
    Then The Life Features table returns no matching permission rows
    # Framework Gap: Requires step definitions for asserting no error is shown for an empty search in LifeSteps.java
    And No error is displayed for the empty search

  # Source: ET-24713
  @todo
  Scenario: Line Item Creative Separation removal preserves existing line item configuration
    # Framework Gap: Requires step definitions for asserting no UI element is gated by a permission in LifeSteps.java
    Then No UI element in the Life application is gated by the "Line Item Creative Separation" permission
    # Framework Gap: Requires step definitions for opening an account that previously had a permission enabled in LifeSteps.java
    When User opens an account that previously had "Line Item Creative Separation" enabled
    # Framework Gap: Requires step definitions for configuring a line item for the selected account in LifeSteps.java
    And User configures a line item for that account
    # Framework Gap: Requires step definitions for verifying a line item saves without error in LifeSteps.java
    Then The line item is configured and saved without error
    # Framework Gap: Requires step definitions for verifying existing line item settings are unchanged in LifeSteps.java
    And Existing line item settings are unchanged and not lost after the deprecation

  # Source: ET-24713
  @todo
  Scenario: Admin audit log reflects the Line Item Creative Separation removal
    # Framework Gap: Requires step definitions for opening the Admin audit log for the Life Features table in LifeSteps.java
    When User opens the Admin audit log for the Life Features table
    # Framework Gap: Requires step definitions for asserting an audit log entry for a permission removal in LifeSteps.java
    Then The audit log records the removal of the "Line Item Creative Separation" permission

  # Source: ET-24715, ET-24713
  @todo
  Scenario: Life Features table renders correctly after both permission deprecations
    # Framework Gap: Requires step definitions for verifying the Life Features table renders without blank or duplicated rows in LifeSteps.java
    Then The Life Features table renders with no blank or duplicated rows
    # Framework Gap: Requires step definitions for verifying the table row count is reduced by an exact amount in LifeSteps.java
    And The Life Features table row count is reduced by exactly 2 from the pre-deprecation count
    # Framework Gap: Requires step definitions for asserting specific permissions are absent from the table in LifeSteps.java
    And Neither "Creative Name BM" nor "Line Item Creative Separation" appears in the table
    # Framework Gap: Requires step definitions for verifying one permission removal does not affect the other in LifeSteps.java
    And The removal of "Creative Name BM" does not interfere with the removal of "Line Item Creative Separation"
    # Framework Gap: Requires step definitions for verifying no cross-contaminated leftover permission state in LifeSteps.java
    And Neither removal leaves cross-contaminated or leftover permission state
