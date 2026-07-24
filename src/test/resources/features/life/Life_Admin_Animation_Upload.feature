Feature: LIFE Admin - Manage Milkshake Loader Animations
  The Admin panel provides a dedicated Animation Upload section where the Design team uploads, previews, schedules and manages milkshake loader animations one at a time.
  Uploaded SVG animations auto-play in preview and can be scheduled to serve as the Life DSP loader on a chosen date.
  Design team members manage the full animation lifecycle themselves without raising developer tickets.

  Background:
    Given This scenario will be executed in the "Demo" environment as an "Admin"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    # Framework Gap: Requires step definitions for navigating to the Admin Animation Upload section in LifeSteps.java
    And User navigates to the Animation Upload section in the Admin panel

  # Source: ET-24716
  @todo
  Scenario: Animation Upload section is available in the Admin panel
    # Framework Gap: Requires step definitions for verifying the Animation Upload section in LifeSteps.java
    Then Verify the Animation Upload section is displayed in the Admin panel
    # Framework Gap: Requires step definitions for verifying the animation management list in LifeSteps.java
    And Verify the animation management list is displayed

  # Source: ET-24716
  @todo
  Scenario: Upload, preview and schedule a valid SVG animation end-to-end
    # Framework Gap: Requires step definitions for selecting an animation file in LifeSteps.java
    When User selects the animation file "milkshake_loader.svg"
    # Framework Gap: Requires step definitions for uploading the selected animation in LifeSteps.java
    And User uploads the selected animation
    # Framework Gap: Requires step definitions for verifying the upload confirmation in LifeSteps.java
    Then Verify the upload confirmation message is displayed
    # Framework Gap: Requires step definitions for verifying an animation entry in the management list in LifeSteps.java
    And Verify the animation "milkshake_loader.svg" appears in the management list
    # Framework Gap: Requires step definitions for opening the animation preview in LifeSteps.java
    When User opens the preview for the animation "milkshake_loader.svg"
    # Framework Gap: Requires step definitions for verifying the SVG preview auto-plays in LifeSteps.java
    Then Verify the SVG animation auto-plays in the preview without a play button
    # Framework Gap: Requires step definitions for scheduling an animation date in LifeSteps.java
    When User schedules the animation for the date "2026-09-01"
    # Framework Gap: Requires step definitions for verifying a scheduled entry in the list in LifeSteps.java
    Then Verify the animation "milkshake_loader.svg" is shown in the list with schedule date "2026-09-01"
    # Framework Gap: Requires step definitions for verifying scheduled-but-not-active state in LifeSteps.java
    And Verify the animation is not active before the scheduled date

  # Source: ET-24716
  @todo
  Scenario: Design-team member completes the upload and schedule journey without developer help
    # Framework Gap: Requires step definitions for selecting an animation file in LifeSteps.java
    When User selects the animation file "spring_loader.svg"
    # Framework Gap: Requires step definitions for uploading the selected animation in LifeSteps.java
    And User uploads the selected animation
    # Framework Gap: Requires step definitions for verifying the upload confirmation in LifeSteps.java
    Then Verify the upload confirmation message is displayed
    # Framework Gap: Requires step definitions for scheduling an animation date in LifeSteps.java
    When User schedules the animation for the date "2026-10-15"
    # Framework Gap: Requires step definitions for verifying a scheduled entry in the list in LifeSteps.java
    Then Verify the animation "spring_loader.svg" is shown in the list with schedule date "2026-10-15"
    # Framework Gap: Requires step definitions for verifying the journey completes without a developer ticket in LifeSteps.java
    And Verify the upload and schedule journey completes without raising a developer ticket

  # Source: ET-24716
  @todo
  Scenario Outline: Reject animation uploads in unsupported formats
    # Framework Gap: Requires step definitions for selecting an animation file in LifeSteps.java
    When User selects the animation file "<FILE_NAME>"
    # Framework Gap: Requires step definitions for uploading the selected animation in LifeSteps.java
    And User uploads the selected animation
    # Framework Gap: Requires step definitions for verifying an unsupported-format error in LifeSteps.java
    Then Verify an error is displayed rejecting the unsupported file format
    # Framework Gap: Requires step definitions for verifying the accepted-formats list in the error in LifeSteps.java
    And Verify the error lists the accepted animation file formats
    # Framework Gap: Requires step definitions for verifying an animation is absent from the list in LifeSteps.java
    And Verify the animation "<FILE_NAME>" does not appear in the management list
    Examples:
      | FILE_NAME  |
      | banner.png |
      | promo.mp4  |

  # Source: ET-24716
  @todo
  Scenario: Reject selecting multiple animation files at once
    # Framework Gap: Requires step definitions for selecting multiple animation files in LifeSteps.java
    When User attempts to select the animation files "loader_one.svg" and "loader_two.svg" together
    # Framework Gap: Requires step definitions for verifying multi-file selection is blocked in LifeSteps.java
    Then Verify multiple file selection is not supported and only one animation can be uploaded at a time

  # Source: ET-24716
  @todo
  Scenario: Upload a large SVG animation of 5MB or more
    # Framework Gap: Requires step definitions for selecting an animation file in LifeSteps.java
    When User selects the animation file "large_loader_5mb.svg"
    # Framework Gap: Requires step definitions for uploading the selected animation in LifeSteps.java
    And User uploads the selected animation
    # Framework Gap: Requires step definitions for verifying large-file upload outcome within a time threshold in LifeSteps.java
    Then Verify the large animation either uploads successfully within 10 seconds or a clear file-size error is displayed

  # Source: ET-24716
  @todo
  Scenario Outline: Handle scheduling edge cases for animation dates
    # Framework Gap: Requires step definitions for selecting an animation file in LifeSteps.java
    When User selects the animation file "milkshake_loader.svg"
    # Framework Gap: Requires step definitions for uploading the selected animation in LifeSteps.java
    And User uploads the selected animation
    # Framework Gap: Requires step definitions for scheduling an animation date and time in LifeSteps.java
    And User schedules the animation for the date "<SCHEDULE_DATE>" at time "<SCHEDULE_TIME>"
    # Framework Gap: Requires step definitions for verifying scheduling outcomes in LifeSteps.java
    Then Verify the scheduling result is "<EXPECTED_RESULT>"
    Examples:
      | SCHEDULE_DATE | SCHEDULE_TIME | EXPECTED_RESULT                                        |
      | 2026-01-01    | 09:00         | past date is rejected with a warning and not activated |
      | 2026-09-01    | 00:00         | animation transitions to active at midnight            |

  # Source: ET-24716
  @todo
  Scenario: Preview offers no pause or resume control
    # Framework Gap: Requires step definitions for selecting an animation file in LifeSteps.java
    When User selects the animation file "milkshake_loader.svg"
    # Framework Gap: Requires step definitions for uploading the selected animation in LifeSteps.java
    And User uploads the selected animation
    # Framework Gap: Requires step definitions for opening the animation preview in LifeSteps.java
    And User opens the preview for the animation "milkshake_loader.svg"
    # Framework Gap: Requires step definitions for verifying absence of pause and resume controls in LifeSteps.java
    Then Verify the preview provides no pause or resume control

  # Source: ET-24716
  @todo
  Scenario: Scheduled SVG serves as the Life DSP loader on its scheduled date
    # Framework Gap: Requires step definitions for confirming an animation is scheduled for a date in LifeSteps.java
    Given The animation "milkshake_loader.svg" is scheduled for the date "2026-09-01"
    # Framework Gap: Requires step definitions for simulating the scheduled date in LifeSteps.java
    When The scheduled date "2026-09-01" is reached
    # Framework Gap: Requires step definitions for verifying the served Life DSP loader animation in LifeSteps.java
    Then Verify the animation "milkshake_loader.svg" is served as the Life DSP loader

  # Source: ET-24716
  @todo
  Scenario: Second upload does not auto-replace the currently scheduled animation
    # Framework Gap: Requires step definitions for confirming an animation is scheduled for a date in LifeSteps.java
    Given The animation "milkshake_loader.svg" is scheduled for the date "2026-09-01"
    # Framework Gap: Requires step definitions for selecting an animation file in LifeSteps.java
    When User selects the animation file "new_loader.svg"
    # Framework Gap: Requires step definitions for uploading the selected animation in LifeSteps.java
    And User uploads the selected animation
    # Framework Gap: Requires step definitions for verifying the scheduled animation is unchanged without explicit config in LifeSteps.java
    Then Verify the scheduled animation remains "milkshake_loader.svg" until it is explicitly reconfigured

  # Source: ET-24716
  @todo
  Scenario: Concurrent uploads by two admins do not corrupt the management list
    # Framework Gap: Requires step definitions for a second admin uploading concurrently in LifeSteps.java
    When Two admins upload the animations "loader_admin_a.svg" and "loader_admin_b.svg" concurrently
    # Framework Gap: Requires step definitions for verifying both concurrent entries in the list in LifeSteps.java
    Then Verify both animations appear in the management list without corruption

  # Source: ET-24716
  @todo
  Scenario: Edit a scheduled date and delete an entry persist after reload
    # Framework Gap: Requires step definitions for confirming an animation is scheduled for a date in LifeSteps.java
    Given The animation "milkshake_loader.svg" is scheduled for the date "2026-09-01"
    # Framework Gap: Requires step definitions for editing a scheduled animation date in LifeSteps.java
    When User edits the scheduled date of "milkshake_loader.svg" to "2026-09-10"
    # Framework Gap: Requires step definitions for reloading the Animation Upload section in LifeSteps.java
    And User reloads the Animation Upload section
    # Framework Gap: Requires step definitions for verifying a persisted scheduled date in LifeSteps.java
    Then Verify the animation "milkshake_loader.svg" shows the schedule date "2026-09-10"
    # Framework Gap: Requires step definitions for deleting an animation entry in LifeSteps.java
    When User deletes the animation "milkshake_loader.svg" from the management list
    # Framework Gap: Requires step definitions for reloading the Animation Upload section in LifeSteps.java
    And User reloads the Animation Upload section
    # Framework Gap: Requires step definitions for verifying an animation is absent from the list in LifeSteps.java
    Then Verify the animation "milkshake_loader.svg" does not appear in the management list

  # Source: ET-24716
  @todo
  Scenario: Deleting an active animation falls back gracefully without errors
    # Framework Gap: Requires step definitions for confirming an animation is active in LifeSteps.java
    Given The animation "milkshake_loader.svg" is currently active as the Life DSP loader
    # Framework Gap: Requires step definitions for deleting an animation entry in LifeSteps.java
    When User deletes the animation "milkshake_loader.svg" from the management list
    # Framework Gap: Requires step definitions for verifying graceful fallback loader behaviour in LifeSteps.java
    Then Verify the loader falls back gracefully with no JavaScript error displayed
