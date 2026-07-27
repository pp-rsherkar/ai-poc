Feature: Life Campaign Landing Page - Lazy-Loaded Metrics and Active Flight Fixes

  1. Defers dynamic metric columns so they load on scroll into view rather than on initial campaign landing page load, showing skeleton states until loaded.
  2. Shows future tactics in "active flight" mode and keeps lazy-loaded metric values consistent with eager-loaded values.
  3. Loads each metric set once per page view and handles lazy-load API errors with a clear error state instead of a stuck skeleton.
  4. Each scenario walks a single continuous pass through the campaign landing page, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify dynamic metrics lazy-load on scroll with skeleton states and no redundant calls
    Given User opens the campaign landing page for a campaign with many tactics
    # Framework Gap: Requires step definitions to observe network activity and metric column states in LifeSteps.java
    Then no dynamic metric API calls fire on initial load and the metric columns show a skeleton loading state
    When User scrolls horizontally to bring the first metric column into view
    Then the dynamic metric API call fires and the metrics populate after the response
    And the skeleton states are replaced by actual metric values within 10 seconds with no perpetual loading state
    When User scrolls back and forth across the metric columns multiple times
    Then the metrics load once and repeated scroll events do not fire additional API calls for the same data

  @todo
  Scenario: Verify metric accuracy, active flight tactics, and page load performance
    Given User opens the campaign landing page for a campaign with 50 or more tactics
    Then the tactic list is shown within 5 seconds, the metric columns show a skeleton state, and rendering is not blocked on the metric APIs
    When User scrolls to trigger the metric load and compares Spend, Impressions, and CTR against a reference report
    Then the lazy-loaded values match the reference report within acceptable rounding
    Given User opens a campaign with both active and future tactics in "active flight" mode
    Then the future tactics are visible in the campaign list

  @todo
  Scenario: Verify lazy-load trigger edge cases for early scroll and custom column layouts
    Given User opens the campaign landing page and immediately scrolls to the metrics area before the page finishes initializing
    Then the metrics still load correctly once the Intersection Observer registers
    # AMB-2: race condition between the scroll and the listener registration
    Given User has moved a dynamic metric column into the first visible position with no scroll required
    When User opens the campaign landing page
    Then the metric loads immediately for the visible column at load time

  @todo
  Scenario: Verify lazy-load error handling and that campaign filtering is not regressed
    Given User opens the campaign landing page and the dynamic metric API fails before the metric columns scroll into view
    Then the metric columns show an error or "Failed to load" state and the skeleton does not persist indefinitely
    # GAP-3: API timeout and error handling for the lazy load
    When User applies a filter to show only active campaigns
    Then the filter works correctly, only matching campaigns are shown, and the metric lazy load still triggers after filtering
    # Regression anchor: the lazy-load implementation must not break campaign list filtering
