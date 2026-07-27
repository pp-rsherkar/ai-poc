Feature: Life Audience Manager - Deprecate Meta Web Pixel Audience Creation

  1. Stops creating a Website Custom Audience (pixel audience) in the client Meta account when an Audience Manager audience is pushed to Meta.
  2. Keeps the Customer File Custom Audience creation working so the Meta push still completes successfully.
  3. Leaves pre-existing pixel audiences untouched and does not affect audience pushes to LinkedIn or TikTok.
  4. Each scenario walks a single continuous pass through the Audience Manager Meta push flow, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the Meta push completes and creates a Customer File audience but no pixel audience
    Given User has an account configured for Meta audience push with an active Meta Pixel
    # Framework Gap: Requires step definitions for the Audience Manager Meta push flow in LifeSteps.java
    When User creates and pushes an Audience Manager audience to Meta
    Then the push completes without error and a success confirmation is shown in the AM UI
    And a Customer File Custom Audience is created in the client Meta account
    And no new Website Custom Audience is created in the client Meta account
    When User pushes audiences to Meta three times in a row
    Then none of the three pushes creates a pixel audience and each creates the Customer File audience correctly

  @todo
  Scenario: Verify pre-existing pixel audiences and non-Meta pushes are unaffected
    Given a client Meta account has Website Custom Audiences created before the deprecation
    When User pushes an Audience Manager audience to Meta
    Then the pre-existing pixel audiences remain in the client Meta account unchanged
    Given User has an account with LinkedIn and TikTok audience push configured
    When User pushes audiences to LinkedIn and TikTok
    Then the LinkedIn and TikTok audience creation flows are unaffected with no regression

  @todo
  Scenario: Verify Meta push edge cases and error handling after the pixel signal removal
    Given User has an account with Meta integration but no Meta Pixel configured
    When User pushes an Audience Manager audience to Meta
    Then the push completes successfully with no error about a missing Pixel and the Customer File audience is created
    Given the Customer File Custom Audience creation call fails
    When User pushes an audience to Meta
    Then the push failure is reported in the AM UI with a relevant error and no phantom success state
    # GAP-1: error handling when the remaining signal fails must still work
    When User inspects the cluster service logs after a Meta push
    Then no error logs indicate the cluster service expected a pixel audience signal that was not received
    # GAP-2: cluster service coordination after the missing signal

  @todo
  Scenario: Verify Meta push consistency and performance across multiple accounts
    Given User has three different client accounts with Meta integration
    When User pushes audiences from each account to Meta
    Then all three pushes succeed, a Customer File audience is created for each, and no pixel audience is created in any
    And the Meta push completion time is not noticeably longer after removing the pixel signal
    # Removing a fire-and-forget signal should not add latency
