Feature: Life Campaign Landing Page - Performance and UX Optimization

  1. Defers dynamic metric loading on the campaign landing page until the first metric column scrolls into view.
  2. Shows skeleton loading states until metrics load and surfaces future tactics in active flight mode.
  3. Keeps lazy-loaded metric values consistent with eager-loaded values.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24263 (TC_01, TC_02, TC_03, TC_04, TC_05, TC_06, TC_07)
  @todo
  Scenario: Verify deferred metric loading, skeleton states, active flight, and data consistency
    # Framework Gap: Requires page object + step definitions for campaign landing page lazy metric loading in LifeCampaignSteps.java
    Given User opens the campaign landing page for a campaign with 50 or more tactics
    Then No dynamic metric API call fires on initial load and the metric columns show skeleton loading states
    And The tactic list is visible within 5 seconds without blocking on metric APIs
    When User scrolls horizontally to bring the first metric column partially into view
    Then The dynamic metric API call fires and the metrics populate, replacing the skeletons within 10 seconds
    When User compares the lazy-loaded metric values against a reference report
    Then The values match within acceptable rounding
    When User opens the campaign landing page in active flight mode with active and future tactics
    Then The future tactics are visible in the campaign list

  # Source: ET-24263 (TC_08, TC_09, TC_10, TC_11, TC_12)
  @todo
  Scenario: Verify early-scroll trigger, error state, custom columns, no redundant reloads, and filter regression
    Given User opens the campaign landing page
    When User immediately scrolls to the metrics area before the page finishes initializing
    Then The metrics still load correctly once the Intersection Observer is registered
    When The metric API fails during the lazy load
    Then The metric columns show an error state and the skeleton does not persist indefinitely
    When User has moved a dynamic metric column to the first visible position with no scroll needed
    Then The metrics load immediately on page load for the visible column
    When User scrolls back and forth multiple times after the metrics have loaded
    Then The metrics load once and no redundant metric API calls fire
    When User applies a campaign filter by status
    Then The filter works correctly and the metric lazy load still triggers after filtering
