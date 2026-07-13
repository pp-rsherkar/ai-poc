Feature: Auto Import NPI Notifications - Success-Only Client Delivery

  1. Auto-import job failure notifications route to internal AM recipients only; success reports continue to all configured recipients including clients.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Auto-Import NPI notification configuration

  @todo
  Scenario Outline: Auto-import failure notifications route to internal recipients only; success reports route to all configured recipients
    Given the configured recipient list contains a mix of internal AM and client addresses
    When an auto-import cron job "<OUTCOME>"
    Then the notification is delivered to "<RECIPIENTS>"
    Examples:
      | OUTCOME  | RECIPIENTS                                 |
      | fails    | internal AM recipients only, not clients   |
      | succeeds | all configured recipients, including clients |

  @todo
  Scenario: A failed job that is re-run and later succeeds delivers only the eventual success email to the client
    Given an auto-import job fails and is then re-run
    When the re-run succeeds
    Then the client receives only the eventual success email, not a stray failure email from the earlier failed attempt
    # Missing requirement: the source epic poses an unresolved open question - whether "success only for clients" requires separate internal/client recipient fields with different trigger conditions, or a per-entry "success only" flag; confirm which mechanism actually shipped, since the two approaches produce different configuration UIs and edge-case behavior
    # Title mismatch: the story title references an Excel file format for the success email, but the parent epic's description and requirements are entirely about recipient routing on success/failure and never mention file format; confirm the actual source of the Excel-format requirement before treating it as in-scope for this story
