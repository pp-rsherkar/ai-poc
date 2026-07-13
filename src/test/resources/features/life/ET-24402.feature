Feature: Buyer Portal Feedback - May 2026 UX/Visual Fix Bucket

  1. Monthly rollup story bundling 22 independent, unrelated UX/visual-bug fixes across the Buyer Portal (Deal Groups, Curated Market, NPI table, Debugger, Administration, date pickers, hover states, etc.).
  2. Each subtask is verified independently as a discrete checklist item rather than as a single functional flow, per the recurring monthly bucket pattern established by the prior month's equivalent (ET-23363).

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario Outline: Each resolved visual/UX subtask displays its documented fix, regression-tested at multiple breakpoints and across browsers where relevant
    Given User navigates to "<SURFACE>"
    Then "<EXPECTED_FIX>" is confirmed
    Examples:
      | SUBTASK   | SURFACE                                   | EXPECTED_FIX                                                          |
      | ET-24543  | Destination section                       | auto-scroll present, Delete button properly aligned                  |
      | ET-24544  | Associated Tactics submenu (Bulk Actions) | renders fully inside viewport                                        |
      | ET-24545  | Curated Market                            | visual issues corrected                                              |
      | ET-24546  | Geo Radius targeting panel                | text rendered in gray per design                                     |
      | ET-24548  | Curated Market standalone page            | correct default option set                                           |
      | ET-24552  | Curated Market Table                      | Advertiser column shown by default                                   |
      | ET-24553  | Deal Group                                | visual issues corrected                                              |
      | ET-24554  | Dropdown                                  | border issue corrected                                               |
      | ET-24555  | Platform-wide hover states                | hover background restored                                            |
      | ET-24556  | Deal header                               | no longer jumps                                                      |
      | ET-24557  | NPI table                                 | column width corrected                                               |
      | ET-24558  | NPI page                                  | visual elements updated                                              |
      | ET-24559  | Administration                            | visual elements updated                                              |
      | ET-24560  | Deal panel                                | stray "x" icon removed                                                |
      | ET-24561  | Date picker calendar                      | no longer shifts on scroll                                           |
      | ET-24562  | Memo                                      | no longer shifts on scroll                                           |
      | ET-24563  | Menu header                               | hover background issue corrected                                     |
      | ET-24564  | Creative Table (Tactic level)             | visual issues corrected                                              |
      | ET-24565  | Flight budget popover                     | fixed                                                                 |
      | ET-24566  | Custom Field popover                      | fixed                                                                 |
      | ET-24567  | Debugger (Tactic level)                   | visual issue corrected                                               |

  @todo
  Scenario: Subtask ET-24547 (PLD scroll behavior) remains Open/TBD while the parent story is Resolved, and must not be silently treated as shipped
    Given subtask ET-24547 "TBD: UX - Fix PLD scroll behavior" is Open while parent story ET-24402 is marked Resolved
    Then confirm with the epic owner whether ET-24547 was deliberately descoped from this release or is an outstanding gap being carried forward silently
    # Missing requirement: the parent story carries no description of its own; all requirement detail lives only in the 22 subtasks. Confirm each subtask's own acceptance criteria independently rather than relying on the parent story text.
    # Regression anchor: ET-23363 - prior month's equivalent bucket (Closed); cross-check whether any March issues recurred in May rather than being genuinely new defects.
