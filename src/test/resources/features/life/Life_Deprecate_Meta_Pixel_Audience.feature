Feature: LIFE Regression - Deprecate Meta Web Pixel Audience Creation
  Pushing an Audience Manager audience to Meta creates only the Customer File Custom Audience and suppresses the Website Custom Audience pixel signal.
  Existing pixel audiences are untouched and non-Meta audience pushes are unaffected.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24706 (R01, R02, R03)
  @todo
  Scenario: AM Meta push creates only the Customer File Custom Audience and no pixel audience
    When User pushes an Audience Manager audience to Meta for a Meta-connected account with an active Meta Pixel
    # Framework Gap: Requires step definitions for the Audience Manager Meta push flow in LifeSteps.java
    Then The push completes successfully with a confirmation in the AM UI
    And A Customer File Custom Audience is created in the client Meta account
    And No new Website Custom Audience pixel audience is created in the client Meta account

  # Source: ET-24706 (R05, edge no-pixel)
  @todo
  Scenario Outline: Meta push behaves correctly across pixel configurations and repeated pushes
    When User pushes an Audience Manager audience to Meta for a "<ACCOUNT>"
    # Framework Gap: Requires step definitions for Meta push edge cases in LifeSteps.java
    Then The result is "<EXPECTED>"
    Examples:
      | ACCOUNT                              | EXPECTED                                                     |
      | account with pre-existing pixel audiences | pre-existing pixel audiences remain unchanged               |
      | account with no Meta Pixel configured     | push completes with no missing-pixel error and a Customer File CA |
      | account pushed three times in a row        | no pixel audiences created and a Customer File CA each time  |

  # Source: ET-24706 (GAP-2, HT cluster regression)
  @todo
  Scenario: Cluster service reports no missing-pixel errors and push failure is surfaced correctly
    When User pushes an Audience Manager audience to Meta and inspects the cluster service logs
    # Framework Gap: Requires step definitions for cluster service log assertions in LifeSteps.java
    Then No error logs indicate a missing pixel audience signal
    When The Customer File Custom Audience creation signal fails
    Then The push failure is reported in the AM UI with a relevant message and no phantom success state

  # Source: ET-24706 (R05 regression, non-Meta)
  @todo
  Scenario: Regression - LinkedIn and TikTok audience pushes and multi-account Meta pushes are unaffected
    When User pushes audiences to LinkedIn and TikTok
    # Framework Gap: Requires step definitions for non-Meta audience push checks in LifeSteps.java
    Then The LinkedIn and TikTok audience creation flows succeed with no regression
    When User pushes audiences from three different Meta-connected accounts
    Then All three pushes succeed with a Customer File CA each and no pixel audiences created
