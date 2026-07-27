Feature: Life Admin - Animation Upload UI

  1. Adds an Animation Upload section in the Admin panel where a user can upload a single SVG animation file and preview it before publishing.
  2. Supports scheduling an animation for a future date and managing existing animations, with the scheduled SVG served as the Life DSP loader on its activation date.
  3. Enables the design team to complete the upload and schedule flow self-service, with an auto-playing preview and no pause or resume control (descoped).
  4. Each scenario walks a single continuous pass through the Animation Upload UI, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the Animation Upload section accepts a valid SVG and enforces upload constraints
    Given User navigates to the Animation Upload section in the Admin panel
    # Framework Gap: Requires step definitions for the Admin Animation Upload UI in LifeSteps.java
    Then the Animation Upload section is visible with the upload interface displayed
    When User uploads a valid SVG animation file
    Then the file uploads successfully with a confirmation message and the animation appears in the management list
    When User attempts to select multiple files in the upload control
    Then only one file can be selected as multi-file selection is not supported
    When User attempts to upload an unsupported ".PNG" or ".MP4" file
    Then the upload is rejected with an error message specifying the accepted formats and no partial upload occurs
    # GAP-1: accepted formats are not fully specified - document the error message shown
    When User uploads a 5MB or larger SVG file
    Then the upload either succeeds within 10 seconds or a clear file size error specifies the limit
    # GAP-2: file size limit is not defined - document the limit encountered

  @todo
  Scenario: Verify animation scheduling, preview, and self-service design flow
    Given User has uploaded a valid SVG animation in the Admin panel
    When User sets a future date in the scheduler and confirms scheduling
    Then the animation is scheduled, the scheduled date is shown in the management list, and the animation is not active until that date
    When User enters a past date in the scheduler
    Then the UI rejects the past date with a validation error or warns that the animation will activate immediately
    When User uses the preview function on the uploaded animation
    Then the SVG animation auto-plays in the Admin UI with no pause or resume control shown
    # AMB-1: pause/resume control descoped per the Victor Onazi comment
    Given User is signed in as a non-developer design team member
    When User completes the upload and schedule flow
    Then the flow completes end-to-end with no step requiring developer intervention

  @todo
  Scenario: Verify animation management, queueing, and Life DSP delivery on the scheduled date
    Given User opens the animation management list in the Admin panel
    When User edits the scheduled date of an existing entry and deletes a different entry
    Then the edit saves with the new date, the deleted entry is removed, and both changes persist on reload
    When User uploads a second animation while the first animation is still scheduled
    Then the second animation is added to the queue without automatically overwriting the first
    # GAP-3: default animation fallback and override behavior
    Given the scheduled activation date for an uploaded SVG has arrived, including a midnight activation
    When User opens Life DSP in a logged-in session
    Then the scheduled SVG animation plays as the milkshake loader in Life DSP and the previous animation deactivates
    # AMB-2: confirm timezone handling for the activation, including midnight transitions

  @todo
  Scenario: Verify fallback and concurrency safety for the active animation
    Given User deletes the currently active animation in the Admin panel
    Then Life DSP shows a default fallback animation or no animation gracefully with no JavaScript error
    # GAP-3: fallback behavior when the active animation is deleted
    Given two Admin users upload different animations at the same time
    Then both uploads are recorded with no corruption and a clear winner is shown in the management list
    # GAP-4: concurrency handling for simultaneous uploads
