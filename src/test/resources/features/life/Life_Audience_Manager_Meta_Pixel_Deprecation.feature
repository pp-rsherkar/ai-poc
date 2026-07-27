Feature: Life Audience Manager - Deprecate Meta Web Pixel Audience Creation

  1. Removes the FE trigger that creates the Website Custom (pixel) audience during an Audience Manager Meta push.
  2. Continues to create the Customer File Custom Audience and leaves pre-existing pixel audiences untouched.
  3. Keeps LinkedIn and TikTok audience pushes unaffected.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24706 (TC_01, TC_02, TC_03, TC_04, TC_05, TC_06)
  @todo
  Scenario: Verify Meta audience push creates only the Customer File audience and no pixel audience
    # Framework Gap: Requires page object + step definitions for the Audience Manager Meta push flow in LifeAudienceManagerSteps.java
    Given User opens Audience Manager for an account with Meta integration and an active Meta Pixel
    When User creates and pushes an AM audience to Meta
    Then The push completes successfully with a confirmation and no pixel-related error
    And The Customer File Custom Audience is created in the client Meta account
    And No new Website Custom Audience is created in the client Meta account
    And Any pre-existing pixel audiences in the client Meta account remain unchanged
    When User pushes AM audiences to Meta three times in a row
    Then None of the pushes create a pixel audience and each creates a Customer File audience
    When User pushes an AM audience for an account that has no Meta Pixel configured
    Then The push completes successfully with the Customer File audience created and no missing-pixel error

  # Source: ET-24706 (TC_07, TC_08, TC_09, TC_10, TC_11)
  @todo
  Scenario: Verify non-Meta pushes, error handling, latency, and cross-account consistency after pixel signal removal
    Given User opens Audience Manager for an account with LinkedIn and TikTok audience push configured
    When User pushes audiences to LinkedIn and TikTok
    Then The LinkedIn and TikTok audience creation flows are unaffected with no regression
    When The Customer File audience creation signal fails during a Meta push
    Then The push reports failure with a relevant error and no phantom success state
    When User measures Meta push completion time after the pixel signal removal
    Then The completion time is the same or better than the pre-deprecation benchmark
    When User inspects the cluster service logs after a Meta push
    Then No error is logged about a missing pixel audience signal
    When User pushes audiences from three different Meta-connected accounts
    Then All pushes succeed with Customer File audiences created and no pixel audiences in any account
