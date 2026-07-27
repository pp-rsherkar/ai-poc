Feature: Life Tag Manager - UI Polish and Navigation Refinements

  1. Enforces the channel order Site, Search, Email, Media across Tag Manager navigation, settings, and tag code views.
  2. Hides the Social channel from all views without deleting existing Social data.
  3. Sorts tags consistently within each channel section.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24718 (TC_01, TC_02, TC_03, TC_04, TC_06, TC_09)
  @todo
  Scenario: Verify Tag Manager channel ordering, Social hidden, and consistent sorting across views
    # Framework Gap: Requires page object + step definitions for Tag Manager navigation and settings in LifeTagManagerSteps.java
    Given User opens Tag Manager v1.0
    Then The channel navigation renders in the order Site, Search, Email, Media
    And The Social channel is not visible in the navigation, settings, or tag code views
    When User opens the Tag Manager Settings view
    Then The Settings view lists channels in the order Site, Search, Email, Media
    When User opens the tag code display view
    Then The tag code sections appear in the order Site, Search, Email, Media
    When User opens the Site channel section
    Then The tags within the section are sorted consistently and the order is stable on reload

  # Source: ET-24718 (TC_05, TC_07, TC_08, TC_10)
  @todo
  Scenario: Verify channel navigation integrity, sparse configuration, and Social data preservation
    Given User opens Tag Manager v1.0
    When User navigates between the Site, Email, Search, and Media channels in sequence
    Then Each channel section loads correctly with its configuration intact and no data loss
    When User creates or edits a tag in each of the Site, Search, Email, and Media channels
    Then The tag saves correctly in every channel with no channel broken by the order change
    When User opens a Tag Manager account configured with only the Site channel
    Then The navigation shows Site correctly and the remaining channels show an empty state with no error
    # Regression anchor: hidden is not deleted
    When User opens an existing Tag Manager that previously held Social channel data
    Then The existing Social data is preserved even though the Social toggle is hidden
