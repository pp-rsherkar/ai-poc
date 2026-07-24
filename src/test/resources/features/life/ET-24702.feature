Feature: Creative Bulk Upload - Accept DCM Tags Starting with <script

  1. Extends DCM bulk-upload file validation to accept JavaScript DCM tags that begin with "<script" without showing a false-positive error.
  2. Keeps click-macro substitution working identically for <script tags and standard DCM tags, and suppresses the spurious "many empty rows" error for valid files.
  3. Preserves rejection of genuinely invalid or non-DCM content so the validator does not become overly permissive.
  4. Each scenario walks a single continuous pass through the DCM bulk-upload flow, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify valid <script DCM tags upload successfully with correct click-macro substitution
    Given User has a DCM tag sheet containing JavaScript tags that start with "<script" in the Klick Agency format
    When User uploads the tag sheet via the DCM bulk-upload UI
    Then the upload completes successfully with no error message (no click-macro error and no "many empty rows" error)
    # Regression anchor: HT-5114 - Klick Agency/Butler Till unable to upload DCM tag sheet; click macro was populating despite the error
    When User inspects an uploaded <script tag in the creative
    Then the click macro is correctly substituted with no raw placeholder visible
    When User uploads a standard (non-script) DCM tag sheet
    Then the standard tags upload successfully with correct click-macro substitution and no error
    When User uploads a DCM tag sheet containing both standard and <script tags
    Then the mixed file uploads successfully with both tag types accepted and click macros correct in both
    When User uploads a <script tag with an unusually long URL or script body
    Then the upload succeeds, the click macro populates, and the tag content is not truncated

  @todo
  Scenario: Verify the validation change keeps rejecting invalid content and does not regress DCM accuracy
    Given User is on the DCM bulk-upload UI
    When User uploads a file that is not a valid DCM tag sheet
    Then the upload is rejected with a clear error and the validator does not become too permissive
    When User uploads a DCM tag sheet where one tag is "<script></script>" with no content
    Then the empty script tag is rejected with a specific error while other valid tags proceed normally
    When User uploads a file of generic JavaScript prefixed with "<script" that is not DCM-structured
    Then the upload is rejected, distinguishing DCM-formatted <script tags from arbitrary JavaScript
    # GAP-2: only DCM-structured <script tags should be accepted - the format boundary must hold
    When User uploads standard and <script DCM tags, runs a test campaign, and compares click/impression counts in DCM vs Life DSP
    Then the discrepancy stays within the historical baseline and no new discrepancy is introduced
    # Regression anchor: HT-4137 - Large DCM Discrepancy; click-macro accuracy must be unaffected
