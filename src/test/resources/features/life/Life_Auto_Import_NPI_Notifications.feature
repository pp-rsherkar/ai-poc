Feature: Auto Import NPI Notifications - Success-Only Client Delivery

  1. Auto-import job failure notifications route to internal AM recipients only; success reports continue to all configured recipients including clients.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Auto-Import NPI notification configuration

  # Source: ET-24237
  @todo
  Scenario Outline: Auto-import failure notifications route to internal recipients only; success reports route to all configured recipients
    Given the configured recipient list contains a mix of internal AM and client addresses
    When an auto-import cron job "<OUTCOME>"
    Then the notification is delivered to "<RECIPIENTS>"
    Examples:
      | OUTCOME  | RECIPIENTS                                   |
      | fails    | internal AM recipients only, not clients     |
      | succeeds | all configured recipients, including clients |

  # Source: ET-24237
  @todo
  Scenario: A failed job that is re-run and later succeeds delivers only the eventual success email to the client
    Given an auto-import job fails and is then re-run
    When the re-run succeeds
    Then the client receives only the eventual success email, not a stray failure email from the earlier failed attempt
