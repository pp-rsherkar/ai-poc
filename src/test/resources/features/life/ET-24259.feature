Feature: Supply Portal - Update PulsePoint Logo (Icon Removed, Logotype Unchanged)

  1. Removes the leading icon/symbol in front of the PulsePoint logotype on the Supply UI home screen and in outgoing billing emails; the logotype/wordmark itself is unchanged.
  2. Per the source requirement's own title/description mismatch, the billing-email surface is in scope even though the ticket title mentions only "the Supply UI."

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Updated PulsePoint logo (icon removed) renders consistently on the Supply UI home screen and in outgoing billing emails
    Given User navigates to the Supply UI home screen
    Then the updated logo displays with the leading icon/symbol removed and the logotype/wordmark unchanged
    And no leftover spacing/alignment artifact remains where the removed icon used to sit, across different screen sizes/breakpoints
    Given a billing email is sent to a partner
    Then the billing email displays the same updated logo, consistent with the UI
    # Title mismatch: the story title scopes the change to "the Supply UI" only, but the description and its own user story ("As a partner, when I receive a billing email, I want to see the updated PulsePoint logo") explicitly extend the requirement to billing emails; confirm billing-email logo verification is not left untested because the title omits it
    And the billing email logo renders correctly across common email clients and is not served stale from a CDN cache
    Given other UI surfaces that may reference the old logo asset, such as the login page, PDF exports, or the favicon
    Then confirm each was explicitly in or out of scope rather than assumed
    # Regression anchor: ET-24252 - AdSetup HTTPS/Life-Only endpoint changes ship under the same parent epic PROD-15374; the AdSetup page's endpoint display area is unaffected by this logo update
