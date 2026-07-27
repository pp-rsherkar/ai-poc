Feature: Life Admin - Animation Upload UI

  1. Provides an Admin UI to upload, preview, schedule, and manage milkshake loader animations one at a time.
  2. Serves the scheduled animation as the Life DSP loader on its activation date.
  3. Enforces single-file upload and supported-format validation with self-service access for the Design team.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24716 (TC_01, TC_02, TC_05, TC_07, TC_08, TC_09, TC_11, TC_14)
  @todo
  Scenario: Verify the Animation Upload, preview, schedule, management, and Life DSP delivery workflow
    # Framework Gap: Requires page object + step definitions for the Admin Animation Upload UI in AdminSteps.java
    Given User navigates to the Administrative section and opens the Animation Upload section
    Then The Animation Upload section is displayed with the upload interface
    When User uploads a valid SVG animation file
    Then The file uploads successfully and appears in the management list with a confirmation
    When User previews the uploaded animation
    Then The SVG animation auto-plays in the Admin preview with no pause or resume control
    When User schedules the animation for a future date and confirms
    Then The scheduled date is shown in the management list and the animation is not yet active
    When User edits the scheduled date of an existing entry and deletes a different entry
    Then The edit saves with the new date and the deleted entry is removed after reload
    When A non-developer Design team user completes the upload and schedule flow
    Then The flow completes end-to-end with no step requiring developer intervention
    When User opens Life DSP on or after the scheduled activation date
    Then The scheduled SVG animation plays as the milkshake loader

  # Source: ET-24716 (TC_03, TC_04, TC_06, TC_10)
  @todo
  Scenario Outline: Verify Animation Upload validation for file count, format, date, and size
    Given User opens the Animation Upload section in the Administrative section
    When User attempts to upload "<INPUT>"
    Then The result is "<EXPECTED>"
    Examples:
      | INPUT                                  | EXPECTED                                                       |
      | two files selected at once             | only one file is accepted and multi-file selection is prevented |
      | a .PNG or .MP4 file                    | upload rejected with an accepted-format error message           |
      | a valid SVG scheduled for a past date  | past date rejected or warned it will activate immediately       |
      | a 5MB SVG file                         | upload succeeds within 10 seconds or a clear size-limit error   |

  # Source: ET-24716 (TC_12, TC_13, TC_15, TC_16)
  @todo
  Scenario: Verify default fallback, concurrency, and midnight activation for scheduled animations
    Given User opens the Animation Upload section with one animation already scheduled
    When User uploads a second animation while the first is still scheduled
    Then The second animation is queued and does not automatically overwrite the first without explicit configuration
    When User deletes the currently active animation
    Then The Life DSP loader shows a default fallback or no animation gracefully with no JavaScript error
    When Two Admin users upload different animations simultaneously
    Then Both uploads are recorded with a clear winner and no list corruption
    When An animation is scheduled for exactly 00:00 on a date
    Then The animation activates at the scheduled time and the previous animation deactivates
