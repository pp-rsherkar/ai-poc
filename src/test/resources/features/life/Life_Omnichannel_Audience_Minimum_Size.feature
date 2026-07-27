Feature: Life Omnichannel Audiences - Minimum Size Permissions

  1. Adds internal VIEW and EDIT permissions for Omnichannel audience minimum sizes at the global and account levels.
  2. Surfaces minimum size values in Admin with view-only and edit-enabled states enforced by permission.
  3. Applies the global minimum size as the fallback when no account-level value is set.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24726 (TC_01, TC_02, TC_03, TC_04, TC_08)
  @todo
  Scenario Outline: Verify Omnichannel minimum size visibility and edit rights by permission
    # Framework Gap: Requires page object + step definitions for the Omnichannel minimum size Admin settings in AdminSteps.java
    Given User navigates to the Administrative section
    And User has the "<PERMISSIONS>" permission configuration
    When User opens the global Omnichannel minimum size settings
    Then The minimum size section is "<VISIBILITY>" and editing is "<EDIT_STATE>"
    Examples:
      | PERMISSIONS   | VISIBILITY | EDIT_STATE |
      | VIEW only     | visible    | disabled   |
      | VIEW and EDIT | visible    | enabled    |
      | EDIT only     | visible    | enabled    |
      | none          | hidden     | disabled   |

  # Source: ET-24726 (TC_03, TC_05, TC_06, TC_07, TC_10)
  @todo
  Scenario Outline: Verify global and account-level minimum size edit, inheritance, and boundary values
    # Framework Gap: Requires step definitions for saving and inheriting minimum size values in AdminSteps.java
    Given User navigates to the Administrative section with the EDIT minimum size permission
    When User sets the global minimum size for a platform to "<GLOBAL_VALUE>" and saves
    Then The value "<GLOBAL_VALUE>" is saved and persists on reload
    When User sets an account-level minimum size of "<ACCOUNT_VALUE>" for an account and saves
    Then The account-level value is saved as distinct from the global value
    When User checks an account that has no account-level minimum size configured
    Then The account displays the global minimum size "<GLOBAL_VALUE>" as its effective minimum
    Examples:
      | GLOBAL_VALUE | ACCOUNT_VALUE |
      | 0            | 5000          |
      | 10000000     | 250000        |

  # Source: ET-24726 (TC_09) - adjacent permission-surface risk pattern (ET-24705)
  @todo
  Scenario: Verify the Pinterest permission does not gate the Omnichannel minimum size settings
    Given User navigates to the Administrative section with "VIEW PINTEREST SIGNIN" but without the minimum size VIEW permission
    When User opens the Omnichannel minimum size settings
    Then The minimum size settings are hidden due to the missing minimum size permission and not due to the Pinterest permission
