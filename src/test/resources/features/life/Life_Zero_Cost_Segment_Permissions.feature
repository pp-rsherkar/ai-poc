Feature: Life Zero Cost Segment Permissions - Verify Global and Account Scope View and Edit Permission Behavior
  1. Verify four $0 Cost Segment permissions (Global View, Global Edit, Account View, Account Edit) are displayed on the Admin Users page
  2. Verify Global Fees and Account Life Settings Fees visibility and editability per permission combination
  3. Verify Global Edit permission grant and revoke is restricted to the named user Jifei
  4. Verify segment classification, tactic editor, and reporting behaviors remain ungated by the new permissions
  5. Verify legacy permission mapping migrates existing users to the correct consolidated permissions

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Administrative section
    And User navigates to Users page

  @todo
  @regression
  Scenario: Verify four dollar zero Cost Segment permissions are displayed on Admin Users page
    When User selects a user to edit permissions
    Then Global View, Global Edit, Account View, and Account Edit permissions should be visible in the permissions list

  @todo
  @regression
  Scenario Outline: Verify Global Fees and Account Life Settings Fees visibility for permission combination Global View "<GLOBAL_VIEW>", Global Edit "<GLOBAL_EDIT>", Account View "<ACCOUNT_VIEW>", Account Edit "<ACCOUNT_EDIT>"
    When User selects a user to edit permissions
    And User sets permissions for the user as Global View "<GLOBAL_VIEW>", Global Edit "<GLOBAL_EDIT>", Account View "<ACCOUNT_VIEW>", Account Edit "<ACCOUNT_EDIT>"
    And User navigates to Administration Setup Global Fees page
    Then Global Fees section visibility should be "<GLOBAL_FEES_STATE>"
    When User navigates to Account Life Settings Fees page for account "<ACCOUNT_NAME>"
    Then Account Fees section visibility should be "<ACCOUNT_FEES_STATE>"
    Examples:
      | ACCOUNT_NAME | GLOBAL_VIEW | GLOBAL_EDIT | ACCOUNT_VIEW | ACCOUNT_EDIT | GLOBAL_FEES_STATE | ACCOUNT_FEES_STATE |
      | 100Plus      | No          | No          | No           | No           | Hidden            | Hidden             |
      | 100Plus      | Yes         | No          | No           | No           | Visible-ReadOnly  | Visible-ReadOnly   |
      | 100Plus      | Yes         | Yes         | No           | No           | Editable          | Visible-ReadOnly   |
      | 100Plus      | No          | No          | Yes          | No           | Hidden            | Visible-ReadOnly   |
      | 100Plus      | No          | No          | Yes          | Yes          | Hidden            | Editable           |
      | 100Plus      | Yes         | Yes         | Yes          | Yes          | Editable          | Editable           |

  @todo
  @regression
  Scenario Outline: Verify Global Edit permission grant and revoke is restricted to the named user Jifei when admin "<ADMIN_USER>" attempts the action
    When "<ADMIN_USER>" attempts to grant Global Edit permission to a user via the Admin UI
    Then the grant action should "<RESULT>"
    When "<ADMIN_USER>" attempts to revoke Global Edit permission from a user via the Admin UI
    Then the revoke action should "<RESULT>"
    Examples:
      | ADMIN_USER   | RESULT     |
      | Jifei        | succeed    |
      | Becky Hallam | be blocked |
      | Andrew Stark | be blocked |

  @todo
  @regression
  Scenario Outline: Verify "<ENTRY_POINT>" behavior remains ungated by the new dollar zero Cost Segment permissions
    Given No dollar zero Cost Segment permissions are granted to the user
    When User navigates to "<ENTRY_POINT>"
    Then "<BEHAVIOR>" should remain unaffected and continue to follow "<GOVERNING_RULE>"
    Examples:
      | ENTRY_POINT                                 | BEHAVIOR                                         | GOVERNING_RULE                                        |
      | Data Segments Zero Dollar Flat Fee dropdown | segment classification editing                   | existing Segment Administration edit permissions      |
      | Tactic Editor Data Cost CPM field           | field visibility for internal and external users | always visible regardless of permission               |
      | Reporting                                   | configured fee values shown in reports           | always reflects configured fees ungated by permission |

  @todo
  @regression
  Scenario Outline: Verify legacy permission mapping migrates users who previously held "<LEGACY_PERMISSION>" to the correct consolidated permissions
    Given A user previously held the legacy "<LEGACY_PERMISSION>" permission under the pre-migration model
    When User reviews that user permissions after migration
    Then the user should now effectively hold "<NEW_PERMISSIONS>" under the consolidated permission model
    Examples:
      | LEGACY_PERMISSION                  | NEW_PERMISSIONS                                      |
      | Direct 1st-party segment View      | Global View, Account View                            |
      | Direct 1st-party segment Edit      | Global Edit, Account Edit                            |
      | dollar zero 3rd-party segment View | Global View, Account View                            |
      | dollar zero 3rd-party segment Edit | Global Edit, Account Edit                            |
      | dollar zero Flat Fee               | Global View, Global Edit, Account View, Account Edit |
