Feature: Life Campaign Landing Page - Deferred Metric Loading and Active Flight
  1. Defers dynamic metric loading until the first metric column enters the viewport, showing skeleton states until then.
  2. Keeps lazy-loaded metric values consistent with eager values and improves initial load time.
  3. Surfaces future tactics in active flight mode without regressing existing filtering.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24263, PROD-15367
  @todo
  Scenario: Verify deferred dynamic metric loading with skeleton states and value consistency
    Given User opens the campaign landing page for a campaign with 50 or more tactics
    # Framework Gap: Requires step definitions for viewport-triggered metric loading and skeleton states on the campaign landing page in LifeSteps.java
    Then No dynamic metric API calls fire on initial load and the metric columns show skeleton loading states
    And The tactic list is shown within 5 seconds without blocking on metric APIs
    When User scrolls horizontally to bring the first metric column partially into view
    Then The dynamic metric API call fires and the metrics populate replacing the skeleton states within 10 seconds
    When User compares the lazy-loaded Spend, Impressions and CTR against a reference report
    Then The values match within acceptable rounding

  # Source: ET-24263, PROD-15367, GAP-2, GAP-3, AMB-2, AMB-3
  @todo
  Scenario Outline: Verify metric load triggering edge cases and error handling
    Given User opens the campaign landing page for "<CONDITION>"
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | CONDITION                                                    | EXPECTED_RESULT                                                        |
      | scrolling to the metrics area immediately as the page loads  | The metrics load correctly once the Intersection Observer is registered |
      | a user whose dynamic metric column is in the first visible position | The metric loads immediately on page load without needing a scroll     |
      | scrolling back and forth multiple times after the load       | The metrics load once and repeated scroll events fire no further API calls |
      | a simulated API failure for the dynamic metrics              | The metric columns show an error state and the skeleton does not persist indefinitely |

  # Source: ET-24263, PROD-15367
  @todo
  Scenario: Verify active flight mode shows future tactics
    Given A campaign with both active and future tactics opened in active flight mode
    Then The future tactics are visible in the campaign list in active flight mode

  # Regression anchor: metric lazy load must not regress existing campaign filtering
  # Source: ET-24263, PROD-15367
  @todo
  Scenario: Verify existing campaign filtering is unaffected by the lazy metric load
    Given User opens the campaign landing page
    When User applies a filter to show only active campaigns
    Then The filter works correctly showing only matching campaigns and the metric lazy load still triggers after filtering
