Feature: Life Tag Manager - Navigation Order and Sorting Refinements
  1. Enforces the channel order Site, Search, Email, Media across navigation, settings and tag code display with Social hidden.
  2. Keeps tags within each channel sorted consistently and stable across reload.
  3. Preserves existing configuration and channel functionality after the order change.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24718, PROD-15837
  @todo
  Scenario: Verify Tag Manager channel order, hidden Social, sorting and channel functionality
    Given User opens Tag Manager v1.0
    # Framework Gap: Requires step definition to read the Tag Manager channel navigation order in LifeSteps.java
    Then The channel navigation renders in the order Site, Search, Email, Media and Social is not visible
    And The Social channel does not appear in the main navigation, settings or tag code display views
    When User opens the Tag Manager Settings view
    Then The Settings view lists the channels in the order Site, Search, Email, Media
    When User opens the tag code display for a configured Tag Manager
    Then The tag code sections appear in the order Site, Search, Email, Media
    When User switches from Site to Email to Search to Media in the navigation
    Then Each channel section loads correctly with its configuration data intact and unchanged
    When User opens the Site channel section and observes tag sorting
    Then The tags are sorted consistently and the order is stable on page reload
    When User creates or edits a tag in each of the Site, Search, Email and Media channels
    Then The tag saves correctly for every channel and no channel is broken by the order change

  # Source: ET-24718, PROD-15837, GAP-2
  @todo
  Scenario Outline: Verify Tag Manager edge configurations after the navigation order change
    Given User opens Tag Manager v1.0 for a "<CONFIG>"
    Then The result is "<EXPECTED_RESULT>"
    Examples:
      | CONFIG                                        | EXPECTED_RESULT                                                                        |
      | Tag Manager account with only the Site channel configured | Site appears in navigation and the other channels are empty or absent with no error        |
      | existing saved Tag Manager containing Social channel data | The existing data is intact and Social channel data is not deleted even though the toggle is hidden |
