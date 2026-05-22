Feature: [Automation] JIRA - GitHub Linkage
  Issue: Currently, when users schedule creatives, they might unintentionally leave part of a line item flight with no active creatives

  Scenario: Validate [Automation] JIRA - GitHub Linkage
    Given Issue: Currently, when users schedule creatives, they might unintentionally leave part of a line item flight with no active creatives
    When This results in periods where no creative will run — in most cases, this is an unwanted consequence or mistake from the user’s perspective and can lead to missed delivery
    Then Description: Only check for scheduling gaps if all creatives in the tactic have a schedule applied
