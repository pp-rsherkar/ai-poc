Feature: LIFE Regression - Audience Manager sign-in access and Meta audience push
  1. AM Settings Login/Logout section visibility is decoupled from the Pinterest permission
  2. Provider sign-in buttons are gated by their own permissions
  3. Meta audience push creates only the Customer File Custom Audience

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24705
  @todo
  Scenario Outline: Verify AM Settings Login/Logout visibility and provider sign-in gating for permission "<PERMISSION>"
    # Framework Gap: Requires step definitions for Admin > AM Settings Login/Logout section in LifeSteps.java
    When User opens Admin AM Settings with permission "<PERMISSION>"
    Then The Login/Logout section is always visible regardless of the Pinterest permission
    And The Pinterest sign-in button visibility is "<PINTEREST_BTN>" and the LinkedIn sign-in button visibility is "<LINKEDIN_BTN>"
    And The TikTok sign-in option is visible to all users with no permission gate
    And A user previously blocked from LinkedIn by the missing Pinterest permission can now access LinkedIn sign-in
    And The AM Settings section container carries no remaining permission gate
    # Regression anchor: Pinterest OAuth must remain functional for permitted accounts
    And The Pinterest OAuth sign-in flow still works end-to-end for accounts with the permission
    Examples:
      | PERMISSION                       | PINTEREST_BTN | LINKEDIN_BTN |
      | VIEW PINTEREST SIGNIN + LinkedIn | visible       | visible      |
      | LinkedIn only                    | hidden        | visible      |
      | no OAuth permissions             | hidden        | hidden       |

  # Source: ET-24706
  @todo
  Scenario: Verify Audience Manager Meta push creates only the Customer File Custom Audience and no pixel audience
    # Framework Gap: Requires step definitions for the Audience Manager Meta push flow in LifeSteps.java
    When User pushes an Audience Manager audience to Meta for a Meta-connected account
    Then The push completes successfully and the Customer File Custom Audience is created in the client Meta account
    And No new Website Custom Audience (pixel audience) is created and pre-existing pixel audiences are left unchanged
    And Repeated pushes and an account with no Meta Pixel configured never create a pixel audience
    And LinkedIn and TikTok audience pushes are unaffected and cluster service logs contain no missing-pixel-signal errors
    And Meta push completion time is not increased by removing the pixel signal
    And A failed Customer File Custom Audience signal is reported as a push failure with no phantom success state
