Feature: Life Admin - Omnichannel Audiences Minimum Size Permissions

  1. Adds an internal VIEW permission that surfaces the configured minimum audience size per platform at the global and account levels in Admin.
  2. Adds an internal EDIT permission that lets staff modify those minimum sizes at the global and account levels and persist the change.
  3. Applies the global minimum size as the fallback when an account has no account-level value configured.
  4. Each scenario walks a single continuous pass through the Admin minimum size settings, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario Outline: Verify the VIEW minimum size permission gates the settings visibility at the global level
    Given User is an internal admin "<VIEW_PERMISSION>" the VIEW minimum size permission
    When User navigates to the global omnichannel minimum size settings
    Then the minimum size settings section is "<VISIBILITY>"
    # Framework Gap: Requires step definitions for the omnichannel minimum size settings in LifeSteps.java
    Examples:
      | VIEW_PERMISSION | VISIBILITY                                   |
      | with            | visible with read-only values per platform   |
      | without         | not visible, hidden gracefully without error |

  @todo
  Scenario: Verify the EDIT permission allows saving minimum size values and VIEW-only cannot edit
    Given User is an internal admin with the EDIT minimum size permission
    When User navigates to the global minimum size settings and changes a platform value
    Then the value saves successfully, is reflected after save, and persists on page reload
    Given User is an internal admin with the VIEW minimum size permission only
    When User attempts to edit a minimum size field
    Then the edit controls are disabled or absent and the field remains read-only
    # GAP-2: precedence rule between global and account level is not defined

  @todo
  Scenario: Verify account-level minimum size overrides and inheritance from global
    Given User is an internal admin with the EDIT minimum size permission
    When User sets an account-level platform value different from the global value
    Then the account-level value saves successfully and is shown as distinct from the global value
    Given an account has no account-level minimum size configured
    Then the account displays the global minimum size as its effective minimum
    # GAP-2: global is the fallback when account-level is not set

  @todo
  Scenario Outline: Verify minimum size boundary values are handled without error
    Given User is an internal admin with the EDIT minimum size permission
    When User sets a platform minimum size to "<VALUE>"
    Then the value "<VALUE>" saves without error and displays correctly without truncation
    Examples:
      | VALUE    |
      | 0        |
      | 10000000 |

  @todo
  Scenario: Verify VIEW and EDIT permissions are independent and unrelated permissions do not gate the settings
    Given User is an internal admin with the EDIT minimum size permission but not the VIEW permission
    When User navigates to the minimum size settings
    Then the observed behavior for an EDIT-only user is documented
    # AMB-1: inter-permission dependency between VIEW and EDIT is not specified
    Given User has the VIEW PINTEREST SIGNIN permission but not the minimum size VIEW permission
    When User navigates to the minimum size settings
    Then the minimum size settings are not visible, blocked only by the missing minimum size permission
    # Regression anchor: permission-surface confusion is a recurring theme in this release (see ET-24705)
