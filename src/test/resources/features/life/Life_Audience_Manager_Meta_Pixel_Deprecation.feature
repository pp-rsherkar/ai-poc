Feature: Life Audience Manager - Meta Web Pixel Audience Deprecation
  1. Sends only the Customer File Custom Audience signal on a Meta push and suppresses the Website Custom Audience pixel signal.
  2. Leaves existing pixel audiences and non-Meta pushes unaffected.
  3. Reports push failures correctly without adding latency after removing the pixel signal.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24706, PROD-15770
  @todo
  Scenario: Verify a Meta push creates only the Customer File Custom Audience and no pixel audience
    Given An account with a Meta integration and an active Meta Pixel
    # Framework Gap: Requires step definition to trigger an Audience Manager push to Meta and inspect the client Meta account in LifeSteps.java
    When User creates and pushes an Audience Manager audience to Meta
    Then The push completes with a success confirmation and no pixel-related API error
    And A Customer File Custom Audience is created in the client Meta account and is usable for Meta campaigns
    And No new Website Custom Audience is created in the client Meta account
    And Pre-existing Website Custom Audiences in the client Meta account remain unchanged
    And The push completion time is not noticeably longer than the pre-deprecation benchmark

  # Source: ET-24706, PROD-15770, GAP-1, GAP-2
  @todo
  Scenario Outline: Verify Meta push behaviour across repeat, no-pixel, failure and log conditions
    Given "<SETUP>"
    When User pushes an Audience Manager audience to Meta
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | SETUP                                                | EXPECTED_RESULT                                                              |
      | The same audience is pushed to Meta three times in a row | No push creates a pixel audience and a Customer File Custom Audience is created each time |
      | An account with a Meta integration but no Meta Pixel | The push completes with no missing-pixel error and a Customer File CA is created |
      | The Customer File CA creation API call fails         | The push reports failure in the AM UI with a relevant error and no phantom success |
      | The cluster service logs are inspected after a push  | No error logs indicate an expected pixel audience signal was missing        |

  # Regression anchor: removing a signal from the multi-step AM push must not regress non-Meta pushes or multi-account pushes
  # Source: ET-24706, PROD-15770
  @todo
  Scenario: Verify non-Meta pushes and multi-account Meta pushes are unaffected
    Given An account configured for LinkedIn and TikTok audience push
    When User pushes audiences to LinkedIn and TikTok
    Then The LinkedIn and TikTok audience creation flows are unaffected with no regression
    When User pushes audiences from three different Meta-connected accounts
    Then All three pushes succeed with a Customer File CA created for each and no pixel audiences created in any
