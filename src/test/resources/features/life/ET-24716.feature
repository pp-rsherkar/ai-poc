Feature: Admin - Milkshake Animation Upload UI

  1. Adds an Admin section that lets the Design team upload, preview, schedule, and manage milkshake loader animations without a developer ticket.
  2. Supports uploading one animation at a time, scheduling it for a future date, and previewing SVG animations inline with auto-play.
  3. Serves the active scheduled animation as the Life DSP loader on its scheduled date, with graceful fallback when the active animation is deleted.
  4. Each scenario walks a single continuous pass through the Animation Upload UI, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24716, GAP-1, GAP-2, AMB-1, AMB-3
  @todo
  Scenario: Verify the upload, validation, preview, and scheduling workflow in the Admin Animation Upload UI
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given User navigates to the Animation Upload section in the Admin panel
    Then the Animation Upload section and its upload interface are displayed
    # GAP-4: the specific permission gating Admin access is not defined
    When User uploads a valid SVG animation file
    Then the file uploads successfully, a confirmation is shown, and the animation appears in the management list
    When User attempts to select more than one file at a time
    Then multi-file selection is prevented or the second file replaces the first (one-at-a-time constraint)
    When User attempts to upload a .PNG or .MP4 file
    Then the upload is rejected with an error naming the accepted formats and no partial upload occurs
    # GAP-1: accepted formats are not fully specified - document the error message shown
    When User uploads a 5MB or larger SVG file
    Then the upload either completes within an acceptable time or a clear file-size-limit error is shown
    # GAP-2: maximum file size is not defined - document the limit encountered
    When User uses the preview function on an uploaded SVG animation
    Then the animation preview auto-plays inline in the Admin UI with no pause/resume control shown
    # AMB-1 / AMB-3: pause control descoped (Victor Onazi comment); confirm inline vs separate-window preview
    When User schedules the animation for a future date and confirms
    Then the animation is scheduled, the scheduled date is shown in the management list, and it is not yet active
    When User enters a past date in the scheduler
    Then the past date is rejected with a validation error or a warning that the animation will activate immediately

  # Source: ET-24716, GAP-3, AMB-2
  @todo
  Scenario: Verify animation management, activation, and Life DSP loader delivery with fallback
    # Framework Gap: new step definitions required in stepdefinitions/LifeSteps.java (Background steps reused verbatim)
    Given a valid animation is scheduled in the Admin Animation Upload UI
    When User edits the scheduled date of an existing entry and deletes a different entry
    Then the edit saves with the new date, the deleted entry is removed, and the changes persist on reload
    When User uploads a second animation while the first is still scheduled
    Then the second animation is added to the queue and does not automatically overwrite the first
    # GAP-3: default-animation fallback/override behaviour is undefined
    When the scheduled activation date is reached and User opens Life DSP in a logged-in session
    Then the scheduled SVG animation plays as the milkshake loader
    # AMB-2: schedule timezone (server vs user vs configurable) must be confirmed, including the midnight transition
    When User deletes the currently active animation
    Then the Life DSP loader shows a default fallback or no animation gracefully with no JavaScript error
