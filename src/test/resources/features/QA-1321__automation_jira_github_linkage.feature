```gherkin
Feature: Creative Scheduling Gap Warning for Line Item Flights

  As a user scheduling creatives for a tactic,
  I want to be warned if there are periods within a line item flight where no creative is active,
  So that I can avoid missed delivery due to unintended scheduling gaps.

  Background:
    Given a tactic with one or more line item flights
    And the creative table displays all creatives associated with the tactic

  Scenario: No warning when at least one creative is unscheduled
    Given at least one creative in the tactic has no schedule applied
    When the user views the creative scheduling table
    Then no scheduling gap warning is displayed above the creative table

  Scenario: Warning is shown when all creatives are scheduled and there is a gap
    Given all creatives in the tactic have schedules applied
    And the combined scheduled dates of all creatives do not fully cover all line item flight dates
    When the user views the creative scheduling table
    Then a single scheduling gap warning is displayed above the creative table

  Scenario: No warning when all creatives are scheduled and there are no gaps
    Given all creatives in the tactic have schedules applied
    And the combined scheduled dates of all creatives fully cover all line item flight dates
    When the user views the creative scheduling table
    Then no scheduling gap warning is displayed above the creative table

  Scenario: Warning applies to tactic, not individual creatives
    Given all creatives in the tactic have schedules applied
    And there is a scheduling gap within any line item flight
    When the user views the creative scheduling table
    Then a single scheduling gap warning is displayed above the creative table
    And no individual creative is marked with a warning
```
