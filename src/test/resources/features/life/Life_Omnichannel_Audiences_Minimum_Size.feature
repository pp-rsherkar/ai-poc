Feature: LIFE Regression - Omnichannel Audiences Minimum Size Permissions
  The Admin interface exposes omnichannel audience minimum sizes gated by dedicated VIEW and EDIT permissions.
  Values are managed at global and account levels with correct inheritance and read-only versus editable enforcement.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24726 (R01, R02, AMB-1)
  @todo
  Scenario Outline: Minimum size settings visibility and edit control by permission
    When User navigates to the global omnichannel minimum size settings with permission "<PERMISSION>"
    # Framework Gap: Requires step definitions for minimum size Admin surface in LifeSteps.java
    Then The minimum size settings section is "<VISIBILITY>"
    And The minimum size fields are "<EDITABILITY>"
    Examples:
      | PERMISSION       | VISIBILITY | EDITABILITY        |
      | VIEW only        | visible    | read-only          |
      | VIEW and EDIT    | visible    | editable           |
      | no permission    | hidden     | not present        |

  # Source: ET-24726 (R02)
  @todo
  Scenario: User with EDIT permission updates and persists a global minimum size value
    When User navigates to the global omnichannel minimum size settings with permission "VIEW and EDIT"
    And User changes the minimum size for one platform and saves
    # Framework Gap: Requires step definitions for minimum size save and persistence in LifeSteps.java
    Then The updated value is reflected in the UI after save and persists on page reload

  # Source: ET-24726 (R04, GAP-2)
  @todo
  Scenario Outline: Account-level minimum size overrides and global inheritance
    When User configures the account-level minimum size as "<ACCOUNT_VALUE>" with global value "<GLOBAL_VALUE>"
    # Framework Gap: Requires step definitions for account vs global precedence in LifeSteps.java
    Then The effective minimum size for the account is "<EFFECTIVE>"
    Examples:
      | ACCOUNT_VALUE | GLOBAL_VALUE | EFFECTIVE     |
      | 5000          | 1000         | 5000          |
      | not set       | 1000         | 1000 (global) |

  # Source: ET-24726 (edge cases)
  @todo
  Scenario Outline: Minimum size boundary values save and display without ambiguity
    When User navigates to the global omnichannel minimum size settings with permission "VIEW and EDIT"
    And User enters the minimum size value "<VALUE>" for a platform and saves
    # Framework Gap: Requires step definitions for minimum size boundary handling in LifeSteps.java
    Then The value "<VALUE>" saves without error and displays as "<DISPLAY>" with no truncation
    Examples:
      | VALUE    | DISPLAY  |
      | 0        | 0        |
      | 10000000 | 10000000 |

  # Source: ET-24726 (ET-24705 cross-permission risk)
  @todo
  Scenario: Minimum size settings are not gated by the VIEW PINTEREST SIGNIN permission
    When User navigates to the global omnichannel minimum size settings with permission "VIEW PINTEREST SIGNIN only"
    # Framework Gap: Requires step definitions for cross-permission isolation checks in LifeSteps.java
    Then The minimum size settings are hidden due to the missing minimum size permission and not due to the Pinterest permission
