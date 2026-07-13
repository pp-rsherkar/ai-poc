Feature: Deal Groups - Yesterday's Available Impressions Column

  1. Adds an "Est. Avails Yst" column to the Deal Group view, reusing the existing Est. Avails 30d data source scoped to a 1-day date range, with no new API call.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Deal Group view

  # Source: ET-24249
  @todo
  Scenario: Est. Avails Yst column shows yesterday's available impressions, reusing the Est. Avails 30d data source and null-handling convention
    Then a new column "Est. Avails Yst" appears immediately to the right of the existing "Est. Avails 30d" column
    And its values are sourced from the same data source as Est. Avails 30d, scoped via a 1-day date-range parameter, with no new API call
    And data refreshes on the same cadence as Est. Avails 30d
    Given a deal group with zero avails yesterday
    Then it renders "--", matching Est. Avails 30d's existing null-handling convention
    Given a deal group with no avails data at all, having never had traffic
    Then it also renders "--", not 0 or a blank cell
    Given a deal group created only today, with no "yesterday" to report on
    Then it renders "--" gracefully rather than an error
    Given the date/timezone boundary at midnight
    Then "yesterday" is computed using the same calendar-day/timezone convention as Est. Avails 30d, not a rolling 24-hour window
    And very large avails numbers format consistently with the existing Est. Avails 30d column's number formatting
