Feature: Audience Manager - Deprecate Meta Web (Pixel) Audience Creation

  1. Modifies the Meta audience push flow so the FE sends only the Customer File Custom Audience signal and suppresses the legacy Website Custom Audience (pixel) signal.
  2. Ensures no new pixel audience is created going forward while pre-existing pixel audiences in client Meta accounts remain untouched.
  3. Keeps the overall push flow, non-Meta platform pushes, and error handling intact after the signal is removed.
  4. Each scenario walks a single continuous pass through the Audience Manager Meta push flow, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24706
  @todo
  Scenario: Verify the Meta audience push workflow creates only the Customer File Custom Audience and no pixel audience
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given an account configured for Meta audience push with an active Meta Pixel
    When User creates and pushes an AM audience to Meta
    Then the push completes successfully with a confirmation and no pixel-audience API error in the AM UI
    And the Customer File Custom Audience appears in the client Meta account and is usable for Meta campaigns
    And no new Website Custom Audience (pixel audience) is created in the client Meta account
    When User pushes AM audiences to Meta three times in a row
    Then none of the three pushes creates a pixel audience and a Customer File CA is created for each
    Given a client Meta account with pre-existing Website Custom Audiences created before the deprecation
    Then those pre-existing pixel audiences remain unchanged after the push (forward-only change, no backfill deletion)

  # Source: ET-24706, GAP-1, GAP-2
  @todo
  Scenario: Verify push resilience, non-Meta isolation, and error handling after the pixel signal removal
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given an account with Meta integration but no Meta Pixel configured
    When User pushes an AM audience to Meta
    Then the push completes successfully with no missing-Pixel error and the Customer File CA is created
    Given an account with LinkedIn and TikTok audience push configured
    When User pushes audiences to LinkedIn and TikTok
    Then those non-Meta flows are unaffected by the Meta pixel change
    When the Customer File CA creation signal fails
    Then the AM UI reports the push failure with a relevant message and no phantom success state
    # GAP-1: the removed signal's fire-and-forget vs awaited behaviour affects push latency/success condition
    When User inspects the cluster service logs after an AM Meta push
    Then no error indicates the cluster service expected the now-suppressed pixel audience signal
    # GAP-2: cluster service coordination for the absent signal must be confirmed
    When User pushes audiences from three different Meta-connected accounts
    Then all three succeed, each creates a Customer File CA, and none creates a pixel audience
