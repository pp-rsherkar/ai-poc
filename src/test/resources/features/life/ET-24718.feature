Feature: Tag Manager v1.0 - UI Polish and Navigation Refinements

  1. Enforces a fixed channel order of Site, Search, Email, Media across Tag Manager navigation, settings, and tag-code display, with the Social channel hidden.
  2. Applies consistent, stable sorting to the tags shown within each channel section.
  3. Treats the hidden Social channel as hidden-not-deleted, preserving existing Social configuration data.
  4. Each scenario walks a single continuous pass through Tag Manager, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the channel-order, sorting, and hidden-Social workflow across Tag Manager views
    Given User opens Tag Manager v1.0
    Then the channel navigation renders in the order Site, Search, Email, Media
    And the Social channel is not visible anywhere in the navigation and no Social toggle is shown
    # GAP-3: before/after design screenshots are needed for exact visual validation
    When User opens the Tag Manager Settings view
    Then the Settings view lists the channels in the identical order Site, Search, Email, Media
    When User opens the tag-code display view
    Then the tag-code sections appear in the order Site, Search, Email, Media
    When User opens the Site channel section
    Then the tags within it are sorted consistently and the order is stable on page reload
    # GAP-1: exact sort field (alphabetical / by type / by date) is unspecified - document observed order
    When User switches between Site, Email, Search, and Media in the navigation
    Then each channel section loads correctly with its configuration data intact and unchanged

  @todo
  Scenario: Verify hidden Social data preservation and per-channel functionality after the order change
    Given an existing saved Tag Manager that contains Social channel data
    When User opens it after the channel-order deployment
    Then the existing data is intact and the Social channel data is not deleted even though the Social toggle is hidden
    # AMB-2: hidden must not mean deleted - Social data must survive the UI change
    When User creates or edits a tag in each of the Site, Search, Email, and Media channels
    Then tags save correctly for all four channels and no channel is broken by the order change
    Given a Tag Manager account configured with only the Site channel
    Then Site appears in the navigation and the remaining channels show an empty/placeholder or absent state with no error
