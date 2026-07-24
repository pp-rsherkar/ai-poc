Feature: Admin AM Settings - Decouple Login/Logout Section from VIEW PINTEREST SIGNIN

  1. Removes the misapplied VIEW PINTEREST SIGNIN gate from the AM Settings Login/Logout section so the section is visible to all users.
  2. Keeps the Pinterest Sign-in button gated by VIEW PINTEREST SIGNIN and the LinkedIn Sign-in button gated by its own permission, both unchanged.
  3. Restores LinkedIn/TikTok sign-in access for users who were previously blocked by the Pinterest-specific gate, without over-correcting the section container.
  4. Each scenario walks a single continuous pass through the Admin AM Settings, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24705
  @todo
  Scenario: Verify the Login/Logout section visibility and per-button gating workflow after the permission decoupling
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given an account without the VIEW PINTEREST SIGNIN permission
    When User navigates to Admin > AM Settings
    Then the Login/Logout section is visible and the Pinterest Sign-in button within it is not visible
    Given an account with the VIEW PINTEREST SIGNIN permission
    When User navigates to Admin > AM Settings
    Then the Login/Logout section is visible and the Pinterest Sign-in button is visible within it
    Given an account with the LinkedIn sign-in permission
    When User navigates to Admin > AM Settings
    Then the Login/Logout section is visible and the LinkedIn Sign-in button is visible within it
    Given an account without the LinkedIn sign-in permission
    Then the Login/Logout section is visible but the LinkedIn Sign-in button is not visible within it
    Given an account without VIEW PINTEREST SIGNIN but with the LinkedIn sign-in permission
    Then the Login/Logout section is visible and the LinkedIn Sign-in button is now accessible (previously blocked by the Pinterest gate)
    Given any user account
    Then the TikTok sign-in option is visible in the Login/Logout section with no permission gate

  # Source: ET-24705, GAP-2, AMB-1, ET-24726
  @todo
  Scenario: Verify the section is not over-gated and the Pinterest OAuth flow and adjacent permissions still work
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given an account with no special permissions
    When User navigates to Admin > AM Settings
    Then the Login/Logout section is visible with no remaining permission gate on the section container
    # GAP-2: over-correction risk - the section container must not be gated by any other permission
    Given an account with no Pinterest, LinkedIn, or TikTok permissions
    Then the Login/Logout section is visible showing an empty state or TikTok-only, with no error
    # AMB-1: define what the section looks like when all gated buttons are hidden
    Given an account with the VIEW PINTEREST SIGNIN permission
    When User clicks the Pinterest Sign-in button in AM Settings
    Then the Pinterest OAuth flow initiates and sign-in works end-to-end
    When User checks the omnichannel minimum size settings (ET-24726) without VIEW PINTEREST SIGNIN
    Then those settings behave per their own permissions and are not blocked by the Pinterest permission
    # Regression anchor: permission-surface confusion is a release-wide pattern (ET-24726)
