Feature: Life Creative - DCM Script Tag File Validation

  1. Extends DCM bulk upload validation to accept tags that begin with <script as a valid DCM tag format.
  2. Keeps click macro substitution correct for both script and standard DCM tags.
  3. Continues to reject non-DCM content and malformed tags.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24702 (TC_01, TC_02, TC_03, TC_04, TC_05, TC_08)
  @todo
  Scenario: Verify DCM bulk upload accepts script-prefixed tags with correct click macro substitution
    # Framework Gap: Requires page object + step definitions for the DCM bulk upload UI in LifeCreativeSteps.java
    Given User opens the DCM bulk upload UI on a creative
    When User uploads a DCM tag sheet containing tags that start with "<script"
    Then The upload completes with no click macro error and no many-empty-rows error
    And The click macro is correctly substituted in each script tag with no raw placeholder
    When User uploads a standard non-script DCM tag sheet
    Then The standard tags upload successfully with correct click macro substitution
    # Regression anchor: HT-5114 - Klick Agency/Butler Till DCM upload click macro error
    When User uploads the exact DCM tag sheet format that triggered HT-5114
    Then The upload completes with no error and the click macro populates correctly

  # Source: ET-24702 (TC_06, TC_07, TC_10, TC_11, TC_12)
  @todo
  Scenario Outline: Verify DCM validation boundaries for mixed, malformed, and non-DCM content
    Given User opens the DCM bulk upload UI on a creative
    When User uploads "<INPUT>"
    Then The upload result is "<EXPECTED>"
    Examples:
      | INPUT                                              | EXPECTED                                                      |
      | a file mixing standard and script DCM tags         | success with both tag types accepted and click macros correct |
      | a script tag with an unusually long script body    | success with the click macro correct and no content truncation|
      | a tag sheet containing an empty script tag          | the empty script tag rejected while valid tags proceed        |
      | generic JavaScript not structured as a DCM tag      | rejected as the validator distinguishes DCM-structured tags   |
      | non-DCM content such as random HTML                 | rejected with a clear error and not made overly permissive    |

  # Source: ET-24702 (TC_09)
  @todo
  Scenario: Verify DCM click and impression discrepancy is not increased by the validation change
    Given User uploads standard and script DCM tags and runs a test campaign
    # Regression anchor: HT-4137 - Large DCM Discrepancy
    When User compares click and impression counts between DCM and Life DSP
    Then The discrepancy remains within the historical baseline with no new discrepancy introduced
