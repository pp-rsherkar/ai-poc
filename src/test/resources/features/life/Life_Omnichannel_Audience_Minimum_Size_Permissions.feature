Feature: Life Omnichannel Audiences - Minimum Size View and Edit Permissions
  1. Surfaces omnichannel minimum audience sizes in Admin gated by dedicated view and edit permissions.
  2. Supports independent global and account-level minimum size values with global inheritance.
  3. Handles boundary values and keeps the permissions isolated from unrelated permission checks.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24726, PROD-15858
  @todo
  Scenario Outline: Verify view and edit permission gating for omnichannel minimum size settings
    Given User is an internal admin with the "<PERMISSION>" configuration
    When User navigates to the global omnichannel minimum size settings
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | PERMISSION            | EXPECTED_RESULT                                                                 |
      | VIEW minimum size     | Minimum size values are visible per platform and are read-only                  |
      | no minimum size       | The minimum size settings section is not visible and hidden gracefully          |
      | EDIT minimum size     | Values can be modified, saved and persist on reload                             |
      | VIEW only, no EDIT    | Edit controls are disabled or absent and the field is read-only                 |
      | EDIT only, no VIEW    | Behaviour is defined and documented for the EDIT-without-VIEW combination       |

  # Source: ET-24726, PROD-15858, GAP-2
  @todo
  Scenario: Verify account-level minimum size values are independent of global with inheritance fallback
    Given User is an internal admin with the EDIT minimum size permission
    # Framework Gap: Requires step definitions for account-level omnichannel minimum size settings in LifeSteps.java
    When User sets an account-level platform minimum size that differs from the global value
    Then The account-level value saves successfully and is shown as distinct from the global value
    When User checks an account that has no explicit account-level minimum size configured
    Then The account displays the global minimum size as its effective minimum

  # Source: ET-24726, PROD-15858
  @todo
  Scenario Outline: Verify boundary minimum size values are stored and displayed without error
    Given User is an internal admin with the EDIT minimum size permission
    When User sets a platform minimum size to "<VALUE>"
    Then The value "<VALUE>" saves without error and displays without truncation or ambiguity with not-set
    Examples:
      | VALUE    |
      | 0        |
      | 10000000 |

  # Regression anchor: ET-24705 permission-surface misapplication pattern in this release
  # Source: ET-24726, ET-24705
  @todo
  Scenario: Verify VIEW PINTEREST SIGNIN permission does not inadvertently gate minimum size settings
    Given User has the VIEW PINTEREST SIGNIN permission but not the minimum size VIEW permission
    When User navigates to the omnichannel minimum size settings
    Then The minimum size settings are hidden due to the missing minimum size permission and not due to the Pinterest permission
