Feature: Life Tag Manager - Channel Navigation Order and UI Refinements

  1. Orders the Tag Manager channel navigation as Site, Search, Email, Media across the main navigation, Settings, and tag code display views.
  2. Hides the Social channel from navigation without deleting existing Social configuration data.
  3. Sorts tags consistently within each channel section and keeps all four visible channels fully functional.
  4. Each scenario walks a single continuous pass through the Tag Manager, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the channel navigation order and Social visibility across all views
    Given User opens Tag Manager v1.0
    # Framework Gap: Requires step definitions for the Tag Manager navigation and channel views in LifeSteps.java
    Then the channel navigation renders in order Site, Search, Email, Media and Social is not visible
    When User opens the Tag Manager Settings view
    Then the Settings view lists the channels in the same order Site, Search, Email, Media
    When User opens the tag code display for a configured Tag Manager
    Then the tag code sections appear in order Site, Search, Email, Media
    When User checks all navigation views for a Social toggle
    Then Social does not appear anywhere in the navigation

  @todo
  Scenario: Verify navigating channels and tag sorting preserve data and functionality
    Given User opens Tag Manager v1.0
    When User switches from Site to Email to Search to Media
    Then each channel section loads correctly and its configuration data is intact and unchanged
    When User opens the Site channel section
    Then the tags are sorted consistently and the order is stable on page reload
    # GAP-1: exact sort field not specified - document the observed sort order
    When User creates or edits a tag in each of the four channels
    Then tags save correctly for Site, Search, Email, and Media with no channel broken by the order change

  @todo
  Scenario: Verify existing configurations and sparse setups are handled without data loss
    Given User opens an existing saved Tag Manager that contains Social channel data after deployment
    Then the existing data is intact and the Social channel data is not deleted even though the Social toggle is hidden
    # AMB-2: hidden is not deleted - Social data must survive the UI change
    Given User opens a Tag Manager account with only the Site channel configured
    Then Site appears in the navigation and the other channels are shown as empty or absent with no error
