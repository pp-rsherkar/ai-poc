Feature: LIFE Regression - Admin Animation Upload
  The Admin panel provides a self-service UI for the Design team to upload, preview, schedule, and manage milkshake loader animations.
  Uploads are one at a time, schedulable to future dates, previewable inline, and the active animation serves as the Life DSP loader.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24716 (R01, R02, R04, R06, GAP-1)
  @todo
  Scenario: Design user uploads, previews and confirms a valid SVG animation
    When User navigates to the Animation Upload section in the Admin panel
    # Framework Gap: Requires step definitions for the Animation Upload UI in LifeSteps.java
    Then The Animation Upload section is displayed with an upload interface
    When User uploads a valid SVG animation file
    Then The file uploads successfully with a confirmation message and appears in the management list
    And The animation preview auto-plays inline in the Admin UI with no pause or resume control shown

  # Source: ET-24716 (R02, GAP-1, GAP-2)
  @todo
  Scenario Outline: Animation upload validates file type, count and size
    When User navigates to the Animation Upload section in the Admin panel
    And User attempts to upload "<INPUT>"
    # Framework Gap: Requires step definitions for upload validation in LifeSteps.java
    Then The result is "<EXPECTED>"
    Examples:
      | INPUT                          | EXPECTED                                                    |
      | multiple files at once         | only one file can be selected (one at a time only)          |
      | a .PNG or .MP4 file            | upload is rejected with an error listing accepted formats   |
      | a 5MB SVG file                 | upload succeeds within 10 seconds or shows a file size error |

  # Source: ET-24716 (R03, AMB-2)
  @todo
  Scenario Outline: Animation scheduling validates the target date
    When User navigates to the Animation Upload section in the Admin panel
    And User uploads a valid SVG and sets the schedule date to "<DATE>"
    # Framework Gap: Requires step definitions for animation scheduling in LifeSteps.java
    Then The scheduling result is "<EXPECTED>"
    Examples:
      | DATE        | EXPECTED                                                       |
      | 2026-12-25  | animation is scheduled and shown as not active until that date  |
      | 2026-01-01  | past date is rejected or warns the animation will activate now  |

  # Source: ET-24716 (R06 self-service)
  @todo
  Scenario: Design team user completes the upload and schedule flow without developer assistance
    When User signs in as a Design team account and completes the upload plus schedule flow
    # Framework Gap: Requires step definitions for Design role upload flow in LifeSteps.java
    Then The flow completes end-to-end with no step requiring developer intervention

  # Source: ET-24716 (management, GAP-3, HT scheduling risk)
  @todo
  Scenario: Scheduled animations can be managed and a deleted active animation falls back gracefully
    When User navigates to the Animation Upload section in the Admin panel
    And User edits the scheduled date of an existing entry and deletes a different entry
    # Framework Gap: Requires step definitions for animation management in LifeSteps.java
    Then The edit saves with the new date and the deleted entry is removed, persisting on reload
    When User deletes the currently active animation
    Then The Life DSP loader shows a default fallback animation gracefully with no JavaScript error

  # Source: ET-24716 (R06 activation, edge midnight)
  @todo
  Scenario: The scheduled animation activates on its date and serves as the Life DSP loader
    When The scheduled activation date is reached for an uploaded SVG animation
    # Framework Gap: Requires step definitions for loader activation verification in LifeSteps.java
    Then The scheduled SVG animation plays as the milkshake loader in Life DSP
    And An animation scheduled for 00:00 transitions to active at the scheduled time and the previous animation deactivates
