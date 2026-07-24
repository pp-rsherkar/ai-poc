Feature: LIFE Regression - DCM Script Tag Upload Validation
  DCM bulk upload accepts tag sheets containing JavaScript tags that start with <script and populates the click macro correctly.
  Standard DCM tags continue to validate, and genuinely invalid content is still rejected.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24702 (R01, R02, R03, R04, HT-5114)
  @todo
  Scenario: DCM bulk upload accepts a valid <script tag sheet with the click macro populated
    When User uploads a DCM tag sheet containing JavaScript tags that start with "<script" via the DCM bulk upload UI
    # Framework Gap: Requires step definitions for DCM bulk upload validation in LifeSteps.java
    Then The upload completes successfully with no click macro error and no many empty rows error
    And The click macro is correctly substituted in the <script tag with no raw placeholder visible

  # Source: ET-24702 (R05, R07, GAP-2)
  @todo
  Scenario Outline: DCM validation accepts standard and mixed tag formats
    When User uploads a "<FILE>" via the DCM bulk upload UI
    # Framework Gap: Requires step definitions for DCM tag format validation in LifeSteps.java
    Then The upload result is "<EXPECTED>" with correct click macro substitution
    Examples:
      | FILE                                      | EXPECTED                                  |
      | standard non-script DCM tag sheet          | uploads successfully                      |
      | mixed standard and <script DCM tag sheet   | uploads successfully with both types accepted |

  # Source: ET-24702 (negative validation, GAP-1, edge)
  @todo
  Scenario Outline: DCM validation still rejects invalid and malformed content
    When User uploads "<FILE>" via the DCM bulk upload UI
    # Framework Gap: Requires step definitions for DCM rejection validation in LifeSteps.java
    Then The upload is "<EXPECTED>"
    Examples:
      | FILE                                          | EXPECTED                                            |
      | a file that is not a valid DCM tag sheet       | rejected with a clear error message                 |
      | a DCM sheet with an empty <script></script> tag | rejected for the empty tag while other tags proceed |
      | generic JavaScript not structured as a DCM tag  | rejected as the validator distinguishes it from DCM |

  # Source: ET-24702 (HT-5114, HT-4137 regression, edge long tag)
  @todo
  Scenario: Regression - discrepancy stays within baseline and long <script tags are handled
    When User uploads standard and <script DCM tags and runs a test campaign
    # Framework Gap: Requires step definitions for click and impression discrepancy checks in LifeSteps.java
    Then The click and impression discrepancy between DCM and Life DSP remains within the historical baseline under 10 percent
    When User uploads a <script DCM tag with an unusually long URL or script body
    Then The upload succeeds with the click macro populated and no truncation of the tag content
