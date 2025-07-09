Feature: LIFE Regression - Create a Targeting Template for below Line Item Type and import in respective Tactic
  1. Display
  2. Video
  3. Native Display
  4. Audio
  5. Search Extension
  6. DOOH
  7. Native Video

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @e2e2 @regression
  Scenario Outline: Create a Targeting Template and import the template in Tactic
    When User navigates to Targeting template page by clicking the icon from Activation section
    Then Verify New Template button is present above the Search option
    And Verify Targeting template section opens by clicking New Template button
    When User creates Targeting template "<TEMPLATE_NAME>" for the line items "<LINE_ITEMS>" with channel "<CHANNEL>" and Targeting Rules
      | Behavioral Segment | AutoSegment  |
      | Age                | 25-29, 35-39 |
    Then Search for the created targeting template

    Examples:
     | TEMPLATE_NAME | CHANNEL          | LINE_ITEMS              |
     | Template      | Display Advanced | Display, Video, Native Display, Audio, Search Extension, DOOH, Native Video|

