Feature: Life Admin - AM Settings Login/Logout Section Decoupled from Pinterest Permission

  1. Shows the Admin > AM Settings Login/Logout section independently of the VIEW PINTEREST SIGNIN permission, so the section is no longer hidden by it.
  2. Keeps each platform sign-in button gated by its own permission: Pinterest and LinkedIn by their respective permissions, TikTok ungated.
  3. Restores access to LinkedIn sign-in for users who were previously blocked only because they lacked the Pinterest permission.
  4. Each scenario walks a single continuous pass through the Admin AM Settings page, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the Login/Logout section is visible regardless of the Pinterest permission and gates the Pinterest button correctly
    Given User navigates to Admin > AM Settings without the VIEW PINTEREST SIGNIN permission
    # Framework Gap: Requires step definitions for the AM Settings Login/Logout section in LifeSteps.java
    Then the Login/Logout section is visible and the Pinterest Sign-in button is not visible
    Given User navigates to Admin > AM Settings with the VIEW PINTEREST SIGNIN permission
    Then the Login/Logout section is visible and the Pinterest Sign-in button is visible within it

  @todo
  Scenario: Verify LinkedIn and TikTok sign-in gating within the decoupled section
    Given User navigates to Admin > AM Settings with the LinkedIn sign-in permission
    Then the Login/Logout section is visible and the LinkedIn Sign-in button is visible within it
    Given User navigates to Admin > AM Settings without the LinkedIn sign-in permission
    Then the Login/Logout section is visible and the LinkedIn Sign-in button is not visible within it
    Given User navigates to Admin > AM Settings without the VIEW PINTEREST SIGNIN permission but with the LinkedIn sign-in permission
    Then the LinkedIn Sign-in button is visible, no longer blocked by the missing Pinterest permission
    # This is the core user-impact validation of the fix
    Given User navigates to Admin > AM Settings as any user
    Then the TikTok sign-in option is visible within the Login/Logout section without a permission gate

  @todo
  Scenario: Verify the section container is not gated and the empty state is handled with no OAuth permissions
    Given User navigates to Admin > AM Settings with no special permissions
    Then the Login/Logout section is visible with no remaining permission gate on the section container
    # GAP-2: overcorrection risk - verify no other permission gates the section container
    Given User navigates to Admin > AM Settings with no Pinterest, LinkedIn, or TikTok permissions
    Then the Login/Logout section is visible with an empty state or only the ungated TikTok option shown
    # AMB-1: appearance of the section when all gated buttons are hidden

  @todo
  Scenario: Verify the Pinterest OAuth flow and adjacent permission surfaces are not regressed
    Given User navigates to Admin > AM Settings with the VIEW PINTEREST SIGNIN permission
    When User clicks the Pinterest Sign-in button
    Then the Pinterest OAuth flow initiates and sign-in works end-to-end
    # Regression anchor: the fix must not break the Pinterest sign-in OAuth flow for authorized users
    Given User navigates to any omnichannel minimum size settings without the VIEW PINTEREST SIGNIN permission
    Then the minimum size settings behave per their own permissions and are not blocked by the Pinterest permission
    # Regression anchor: adjacent permission-surface confusion pattern in this release (see ET-24726)
