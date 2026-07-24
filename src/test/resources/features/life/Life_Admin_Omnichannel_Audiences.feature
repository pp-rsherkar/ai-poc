Feature: Admin Omnichannel Audiences - Minimum Audience Size Permissions
  1. Verify internal users with the view permission can see the minimum audience size configured for each platform at the global and account level
  2. Verify internal users with the edit permission can modify and persist the minimum audience size at the global and account level
  3. Verify the view and edit permissions independently gate access to the Omnichannel Audiences minimum size settings

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    When User navigates to Administrative section

  # Source: ET-24726
  @todo
  Scenario Outline: View and edit permissions gate the Omnichannel Audiences minimum size settings independently
    # Framework Gap: Requires step definitions for minimum audience size permission assignment in LifeSteps.java
    Given Internal user is granted "<VIEW_PERMISSION>" view minimum audience size permission and "<EDIT_PERMISSION>" edit minimum audience size permission
    # Framework Gap: Requires step definitions for Omnichannel Audiences minimum size settings navigation in LifeSteps.java
    When User navigates to the Omnichannel Audiences minimum size settings at the "Global" level
    # Framework Gap: Requires step definitions for minimum audience size settings visibility verification in LifeSteps.java
    Then Minimum audience size settings visibility is "<SETTINGS_VISIBLE>"
    # Framework Gap: Requires step definitions for minimum audience size control edit-state verification in LifeSteps.java
    And Minimum audience size controls edit state is "<CONTROLS_STATE>"
    Examples:
      | VIEW_PERMISSION | EDIT_PERMISSION | SETTINGS_VISIBLE | CONTROLS_STATE |
      | granted         | granted         | visible          | editable       |
      | granted         | revoked         | visible          | read-only      |
      | revoked         | revoked         | hidden           | not-applicable |
      | revoked         | granted         | hidden           | not-applicable |

  # Source: ET-24726
  @todo
  Scenario: Internal user without the view permission cannot see the minimum size settings and no error is shown
    # Framework Gap: Requires step definitions for minimum audience size permission assignment in LifeSteps.java
    Given Internal user is granted "revoked" view minimum audience size permission
    # Framework Gap: Requires step definitions for Omnichannel Audiences minimum size settings navigation in LifeSteps.java
    When User navigates to the Omnichannel Audiences minimum size settings at the "Global" level
    # Framework Gap: Requires step definitions for minimum audience size settings visibility verification in LifeSteps.java
    Then Minimum audience size settings visibility is "hidden"
    # Framework Gap: Requires step definitions for graceful error-state verification in LifeSteps.java
    And No error message is displayed

  # Source: ET-24726
  @todo
  Scenario: Internal user with edit permission updates the global minimum audience size and the value persists after reload
    # Framework Gap: Requires step definitions for minimum audience size permission assignment in LifeSteps.java
    Given Internal user is granted edit minimum audience size permission
    # Framework Gap: Requires step definitions for Omnichannel Audiences minimum size settings navigation in LifeSteps.java
    When User navigates to the Omnichannel Audiences minimum size settings at the "Global" level
    # Framework Gap: Requires step definitions for setting a platform minimum audience size value in LifeSteps.java
    And User sets the minimum audience size for platform "Facebook" to "50000"
    # Framework Gap: Requires step definitions for saving minimum audience size settings in LifeSteps.java
    And User saves the minimum audience size settings
    # Framework Gap: Requires step definitions for reloading the minimum audience size settings in LifeSteps.java
    And User reloads the Omnichannel Audiences minimum size settings
    # Framework Gap: Requires step definitions for verifying a platform minimum audience size value in LifeSteps.java
    Then The minimum audience size for platform "Facebook" at the "Global" level is "50000"

  # Source: ET-24726
  @todo
  Scenario: Account-level minimum audience size is set independently of the global value
    # Framework Gap: Requires step definitions for minimum audience size permission assignment in LifeSteps.java
    Given Internal user is granted edit minimum audience size permission
    # Framework Gap: Requires step definitions for setting a platform minimum audience size value by level in LifeSteps.java
    When User sets the minimum audience size for platform "Facebook" to "50000" at the "Global" level
    # Framework Gap: Requires step definitions for setting a platform minimum audience size value by account in LifeSteps.java
    And User sets the minimum audience size for platform "Facebook" to "75000" at the "Account" level for account "automation@pulsepoint"
    # Framework Gap: Requires step definitions for saving minimum audience size settings in LifeSteps.java
    And User saves the minimum audience size settings
    # Framework Gap: Requires step definitions for verifying a platform minimum audience size value by account in LifeSteps.java
    Then The minimum audience size for platform "Facebook" at the "Account" level for account "automation@pulsepoint" is "75000"
    # Framework Gap: Requires step definitions for verifying a platform minimum audience size value by level in LifeSteps.java
    And The minimum audience size for platform "Facebook" at the "Global" level is "50000"

  # Source: ET-24726
  @todo
  Scenario: Account without an explicit minimum audience size inherits the global value
    # Framework Gap: Requires step definitions for minimum audience size permission assignment in LifeSteps.java
    Given Internal user is granted edit minimum audience size permission
    # Framework Gap: Requires step definitions for seeding a global platform minimum audience size value in LifeSteps.java
    And The global minimum audience size for platform "Facebook" is "50000"
    # Framework Gap: Requires step definitions for viewing an account-level minimum audience size with no explicit value in LifeSteps.java
    When User views the minimum audience size for platform "Facebook" at the "Account" level for account "automation@pulsepoint" with no account-level value set
    # Framework Gap: Requires step definitions for verifying inherited minimum audience size value in LifeSteps.java
    Then The displayed minimum audience size for platform "Facebook" is inherited from the "Global" level as "50000"

  # Source: ET-24726
  @todo
  Scenario Outline: Boundary minimum audience size values are saved and displayed without misinterpretation
    # Framework Gap: Requires step definitions for minimum audience size permission assignment in LifeSteps.java
    Given Internal user is granted edit minimum audience size permission
    # Framework Gap: Requires step definitions for Omnichannel Audiences minimum size settings navigation in LifeSteps.java
    When User navigates to the Omnichannel Audiences minimum size settings at the "Global" level
    # Framework Gap: Requires step definitions for setting a platform minimum audience size value in LifeSteps.java
    And User sets the minimum audience size for platform "Facebook" to "<MIN_SIZE>"
    # Framework Gap: Requires step definitions for saving minimum audience size settings in LifeSteps.java
    And User saves the minimum audience size settings
    # Framework Gap: Requires step definitions for reloading the minimum audience size settings in LifeSteps.java
    And User reloads the Omnichannel Audiences minimum size settings
    # Framework Gap: Requires step definitions for verifying a platform minimum audience size value in LifeSteps.java
    Then The minimum audience size for platform "Facebook" at the "Global" level is "<MIN_SIZE>"
    # Framework Gap: Requires step definitions for verifying minimum audience size value interpretation in LifeSteps.java
    And The minimum audience size value "<MIN_SIZE>" is treated as "<INTERPRETATION>"
    Examples:
      | MIN_SIZE | INTERPRETATION      |
      | 0        | explicitly-set-zero |
      | 10000000 | exact-no-truncation |

  # Source: ET-24726
  # Source: ET-24705
  @todo
  Scenario Outline: The Pinterest sign-in permission neither grants nor blocks the Omnichannel Audiences minimum size settings
    # Framework Gap: Requires step definitions for combined Pinterest sign-in and minimum audience size permission assignment in LifeSteps.java
    Given Internal user is granted "<PINTEREST_SIGNIN>" Pinterest sign-in permission and "<VIEW_PERMISSION>" view minimum audience size permission
    # Framework Gap: Requires step definitions for Omnichannel Audiences minimum size settings navigation in LifeSteps.java
    When User navigates to the Omnichannel Audiences minimum size settings at the "Global" level
    # Framework Gap: Requires step definitions for minimum audience size settings visibility verification in LifeSteps.java
    Then Minimum audience size settings visibility is "<SETTINGS_VISIBLE>"
    Examples:
      | PINTEREST_SIGNIN | VIEW_PERMISSION | SETTINGS_VISIBLE |
      | granted          | revoked         | hidden           |
      | revoked          | granted         | visible          |
