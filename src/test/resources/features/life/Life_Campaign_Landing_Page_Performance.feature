Feature: LIFE Regression - Campaign Landing Page Performance Optimization
  The campaign landing page lazily loads dynamic metrics on horizontal scroll into view, showing skeleton states until data arrives.
  Active flight mode surfaces future tactics and lazily loaded metric values match eager-loaded values.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24263 (R01, R02, AMB-1)
  @todo
  Scenario: Dynamic metrics defer loading until the first metric column scrolls into view
    When User opens the campaign landing page for a campaign with many tactics
    # Framework Gap: Requires step definitions for deferred metric loading and network assertions in LifeSteps.java
    Then No dynamic metric API calls fire on initial load and the metric columns show a skeleton loading state
    When User scrolls horizontally to bring the first metric column partially into view
    Then The dynamic metrics API call fires and the metrics populate after the response
    And The skeleton states are replaced by actual metric values within 10 seconds

  # Source: ET-24263 (R03)
  @todo
  Scenario: Active flight mode shows future tactics on the campaign landing page
    When User opens a campaign with active and future tactics in active flight mode
    # Framework Gap: Requires step definitions for active flight mode tactic listing in LifeSteps.java
    Then The future tactics are visible in the campaign list in active flight mode

  # Source: ET-24263 (R04, R05 data and performance)
  @todo
  Scenario: Lazy-loaded metric values match the reference report and improve initial load
    When User opens the campaign landing page for a campaign with 50 or more tactics
    # Framework Gap: Requires step definitions for metric consistency and load timing in LifeSteps.java
    Then The tactic list shows within 5 seconds while metric columns show skeleton and do not block on metric APIs
    When User triggers the metric load and compares Spend, Impressions and CTR against a reference report
    Then The lazy-loaded values match the reference report within acceptable rounding

  # Source: ET-24263 (AMB-2, AMB-3, GAP-2, GAP-3 edge cases)
  @todo
  Scenario Outline: Deferred metric loading handles early scroll, custom column position and API errors
    When User opens the campaign landing page for the case "<CASE>"
    # Framework Gap: Requires step definitions for Intersection Observer edge cases in LifeSteps.java
    Then The metric loading behaves as "<EXPECTED>"
    Examples:
      | CASE                                                 | EXPECTED                                                     |
      | user scrolls to metrics before the page initializes   | metrics load correctly once the observer registers           |
      | a dynamic metric column moved to the first position   | metrics load immediately on load without needing to scroll   |
      | an API failure during the lazy metric load            | an error state is shown and the skeleton does not persist    |

  # Source: ET-24263 (R12 regression, no repeat calls)
  @todo
  Scenario: Regression - filtering works and metrics load once per view
    When User applies a filter to show only active campaigns on the landing page
    # Framework Gap: Requires step definitions for landing page filtering and load-once behavior in LifeSteps.java
    Then The filter works correctly and the metric lazy load still triggers correctly after filtering
    When User scrolls back and forth multiple times after the metrics have loaded
    Then The metrics load once and additional scroll events do not fire repeated API calls for the same data
