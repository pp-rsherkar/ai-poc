Feature: Life Admin - Milkshake Animation Upload and Scheduling
  1. Provides an Admin UI to upload, preview, schedule and manage milkshake loader animations one at a time.
  2. Serves the active scheduled animation as the Life DSP loader with a safe fallback.
  3. Validates file format, file size and schedule dates for the self-service Design team flow.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24716, PROD-14309
  @todo
  Scenario: Verify the Admin animation upload, preview, schedule and manage workflow
    Given User navigates to the Animation Upload section in the Admin panel
    # Framework Gap: Requires step definitions for the Admin Animation Upload UI in LifeSteps.java
    Then The Animation Upload section is visible with the upload interface displayed
    When User uploads a valid SVG animation file
    Then The file uploads successfully with a confirmation message and appears in the management list
    When User uses the preview function on the uploaded animation
    Then The animation preview auto-plays in the Admin UI with no play button and no pause or resume controls
    When User schedules the uploaded animation for a specific future date
    Then The animation is scheduled with its date shown in the management list and is not active until that date
    When User edits the scheduled date of an existing entry and deletes a different entry
    Then The edit saves with the new date, the deleted entry is removed and both changes persist on reload

  # Source: ET-24716, PROD-14309, GAP-1, GAP-2
  @todo
  Scenario Outline: Verify animation upload validation and scheduling boundary handling
    Given User navigates to the Animation Upload section in the Admin panel
    When User performs "<ACTION>"
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | ACTION                                                    | EXPECTED_RESULT                                                                    |
      | attempting to select multiple files in the upload control | Only one file can be selected or the second file replaces the first                |
      | uploading an unsupported PNG or MP4 file                  | The upload is rejected with an error stating the accepted formats and no partial upload |
      | entering a past date in the scheduler                     | The past date is rejected with a validation error or a warning that it activates immediately |
      | uploading a 5MB or larger SVG file                        | The upload succeeds within 10 seconds or a clear file-size-limit error is shown    |
      | uploading a second animation while the first is scheduled | The second animation is queued and does not automatically overwrite the first      |

  # Source: ET-24716, PROD-14309, GAP-4
  @todo
  Scenario: Verify a Design team non-developer user can complete the upload and schedule flow
    Given User is signed in as a Design team account without developer access
    When User completes the upload and schedule flow end to end
    Then The flow completes successfully with no step requiring developer intervention

  # Source: ET-24716, PROD-14309, GAP-3, AMB-2
  @todo
  Scenario: Verify the scheduled animation is served in Life DSP with correct activation and fallback
    Given An animation is scheduled to activate at 00:00 on a specific date
    When The scheduled activation time is reached
    Then The scheduled SVG animation plays as the milkshake loader in Life DSP and the previous animation deactivates
    When User deletes the currently active animation in Admin
    Then The Life DSP loader shows a default fallback or no animation gracefully with no JavaScript error
    When Two admin accounts upload different animations simultaneously
    Then Both uploads are recorded with no corruption and a clear winner is shown in the list
