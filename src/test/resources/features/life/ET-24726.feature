Feature: Admin - Omnichannel Audiences Minimum Size View and Edit Permissions

  1. Introduces two internal permissions that surface omnichannel audience minimum sizes in Admin: a VIEW permission (read-only visibility) and an EDIT permission (modify and persist values).
  2. Minimum sizes are configurable at both global and account level, with the global value acting as the fallback when no account-level value is set.
  3. The permission gates hide the settings gracefully rather than throwing errors, and are decoupled from unrelated permissions such as VIEW PINTEREST SIGNIN.
  4. Each scenario walks a single continuous pass through the Admin minimum-size settings, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24726, GAP-1, GAP-2
  @todo
  Scenario: Verify the VIEW and EDIT permission workflow for global and account-level omnichannel minimum sizes
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given User is an internal admin with the VIEW minimum size permission
    When User navigates to the global omnichannel minimum size settings
    Then the minimum size value for each configured platform is visible and read-only
    # GAP-1: exact platform names are defined in PROD-13185 and must be confirmed
    Given User is an internal admin with the EDIT minimum size permission
    When User changes a platform's global minimum size value and saves
    Then the value saves successfully, is reflected in the UI, and persists on page reload
    When User sets an account-level minimum size that differs from the global value
    Then the account-level value saves and is shown as distinct from the global value
    When User checks an account that has no account-level minimum size configured
    Then that account displays the global minimum size as its effective minimum
    # GAP-2: global-vs-account precedence rule is undefined - document actual behaviour
    When User sets a platform minimum size to 0
    Then the value 0 saves without error and is displayed distinctly from a "not set" state
    When User sets a platform minimum size to 10000000
    Then the value saves and displays without truncation or error

  # Source: ET-24726, AMB-1, ET-24705
  @todo
  Scenario: Verify the permission gates hide the minimum size settings without leaking access across permissions
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given User is an internal admin without the VIEW minimum size permission
    When User navigates to the global omnichannel minimum size settings
    Then the minimum size settings section is not visible and no access or permission error is shown
    Given User is an internal admin with the VIEW permission but not the EDIT permission
    When User attempts to edit a minimum size field
    Then the edit controls are disabled or absent, the field is read-only, and saving is blocked
    Given User holds VIEW PINTEREST SIGNIN but not the minimum size VIEW permission
    When User navigates to the omnichannel minimum size settings
    Then the settings remain hidden, blocked only by the missing minimum size permission and not by the Pinterest permission
    # Regression anchor: ET-24705 - permission-surface misapplication is a release-wide risk pattern
    Given User holds the EDIT permission only
    Then the resulting view/edit behaviour is documented
    # AMB-1: whether EDIT implicitly grants VIEW is unspecified - document actual behaviour
