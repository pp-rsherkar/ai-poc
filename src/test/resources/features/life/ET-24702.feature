Feature: Life Creative - DCM File Type Validation Accepts script Tags

  1. Updates DCM bulk upload validation to accept JavaScript DCM tag sheets whose tags start with "<script", which previously failed to upload.
  2. Ensures the click macro populates correctly in accepted <script tags and no click-macro or empty-rows error is shown for valid files.
  3. Keeps standard DCM tag formats working and continues to reject genuinely invalid or non-DCM content.
  4. Each scenario walks a single continuous pass through the DCM bulk upload UI, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify a valid <script DCM tag sheet uploads with correct click macro and no error
    Given User prepares a DCM tag sheet containing JavaScript tags that start with "<script" in the Klick Agency format
    # Framework Gap: Requires step definitions for the DCM bulk upload UI in LifeSteps.java
    When User uploads the <script DCM tag sheet via the DCM bulk upload UI
    Then the upload completes successfully with no error message and the tags are accepted
    And the click macro is correctly substituted in the <script tags with no raw placeholder visible
    And no "many empty rows" error and no click macro error appear for the valid <script tag sheet
    # AMB-1: verify both error types are suppressed for valid <script tags

  @todo
  Scenario: Verify the exact HT-5114 repro is resolved and standard formats still validate
    Given User prepares the exact DCM tag sheet format that triggered HT-5114
    When User uploads the DCM tag sheet via the DCM bulk upload UI
    Then the upload completes without error and the click macro populates correctly
    # Regression anchor: HT-5114 - Butler Till / Klick Agency were unable to upload a DCM tag sheet
    When User uploads a standard non-JavaScript DCM tag sheet
    Then the standard tags upload successfully with the click macro populated and no error
    When User uploads a DCM tag sheet mixing standard and <script tags
    Then the mixed file uploads successfully with both tag types accepted and correct click macros in both
    # GAP-2: mixed-format handling

  @todo
  Scenario: Verify the validator remains strict and rejects invalid or non-DCM content
    Given User opens the DCM bulk upload UI
    When User uploads a file that is not a valid DCM tag sheet
    Then the upload is rejected with a clear error message
    When User uploads a file of generic JavaScript prefixed with "<script" that is not structured as a DCM tag
    Then the upload is rejected because only DCM-structured <script tags are accepted
    # GAP-2: <script acceptance must not open a gate to arbitrary content
    When User uploads a DCM tag sheet where one tag is an empty "<script></script>" with no content
    Then the empty script tag is rejected with a specific error while other valid tags in the file proceed
    When User uploads a <script DCM tag with an unusually long URL or script body
    Then the upload succeeds and the click macro populates correctly with no truncation of the tag content

  @todo
  Scenario: Verify tracking discrepancy is not increased by the validation change
    Given User uploads standard and <script DCM tags and runs a test campaign
    When User compares click and impression counts in DCM against Life DSP
    Then the discrepancy remains within the historical baseline under 10 percent with no new discrepancy introduced
    # Regression anchor: HT-4137 - Large DCM Discrepancy; the validation change must not affect click macro accuracy
