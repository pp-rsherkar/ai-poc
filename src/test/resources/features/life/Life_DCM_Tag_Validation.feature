Feature: Life DCM Bulk Upload - Script Tag File Validation
  1. Accepts DCM bulk upload tag sheets whose tags start with the script prefix without a false error.
  2. Keeps click macro substitution correct for script, standard and mixed tag formats.
  3. Still rejects genuinely invalid, empty or non-DCM script content.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24702, PROD-15758
  @todo
  Scenario: Verify a script-prefixed DCM tag sheet uploads without error and populates the click macro
    Given User opens the DCM bulk upload UI
    # Framework Gap: Requires step definitions for DCM bulk upload and click macro inspection in LifeSteps.java
    When User uploads a DCM tag sheet containing JavaScript tags that start with the script prefix
    Then The upload completes successfully with no error message shown
    And No click macro error and no many empty rows error is displayed
    And The click macro is correctly substituted in the script tag with no raw placeholder visible

  # Source: ET-24702, PROD-15758, GAP-1, GAP-2
  @todo
  Scenario Outline: Verify DCM validation accepts valid formats and rejects invalid content
    Given User opens the DCM bulk upload UI
    When User uploads "<FILE>"
    Then The upload result is "<EXPECTED_RESULT>"
    Examples:
      | FILE                                                     | EXPECTED_RESULT                                                          |
      | a standard non-script DCM tag sheet                      | Uploads successfully with the click macro populated correctly           |
      | a sheet mixing standard and script DCM tags              | Uploads successfully with both tag types accepted and click macros correct |
      | a file that is not a valid DCM tag sheet                 | Rejected with a clear error message                                     |
      | a script DCM tag with an unusually long URL or script body | Uploads successfully with the click macro populated and no truncation   |
      | a sheet where one tag is an empty script tag with no content | The empty script tag is rejected with a specific error while other valid tags proceed |
      | a file containing generic JavaScript not structured as a DCM tag | Rejected because the validator distinguishes DCM-formatted script tags from arbitrary JS |

  # Regression anchor: HT-5114 - Klick Agency DCM upload showed a false click macro error; HT-4137 - large DCM discrepancy
  # Source: ET-24702, HT-5114, HT-4137
  @todo
  Scenario: Verify the HT-5114 repro is resolved and DCM discrepancy is not increased
    Given User opens the DCM bulk upload UI
    When User uploads the exact DCM tag sheet format that triggered HT-5114
    Then The upload completes without error and the click macro populates correctly
    When User uploads standard and script DCM tags and runs a test campaign
    Then The click and impression discrepancy remains within the historical baseline with no new discrepancy introduced
