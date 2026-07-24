Feature: LIFE Regression - Tag Manager Navigation and Sorting Refinements
  Tag Manager v1.0 displays channels in the order Site, Search, Email, Media with the Social channel hidden.
  Channel order is consistent across navigation, settings, and tag code views, and existing configurations are preserved.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"

  # Source: ET-24718 (R01, GAP-2)
  @todo
  Scenario Outline: Channel order is consistent across Tag Manager views with Social hidden
    When User opens Tag Manager and views the "<VIEW>"
    # Framework Gap: Requires step definitions for Tag Manager channel order assertions in LifeSteps.java
    Then The channels display in order Site, Search, Email, Media
    And The Social channel is not visible in the "<VIEW>"
    Examples:
      | VIEW              |
      | navigation view   |
      | settings view     |
      | tag code display  |

  # Source: ET-24718 (GAP-1, R04)
  @todo
  Scenario: Tags within a channel section are sorted consistently and stable on reload
    When User opens Tag Manager and opens the Site channel section
    # Framework Gap: Requires step definitions for tag sort order assertions in LifeSteps.java
    Then The tags are sorted consistently and the order is stable on page reload

  # Source: ET-24718 (navigation integrity, AMB-2)
  @todo
  Scenario: Navigating between channels in the new order preserves configuration
    When User opens Tag Manager and switches from Site to Email to Search to Media
    # Framework Gap: Requires step definitions for channel navigation integrity in LifeSteps.java
    Then Each channel section loads correctly with its configuration data intact and unchanged

  # Source: ET-24718 (edge, data preservation)
  @todo
  Scenario Outline: Tag Manager handles single-channel and existing Social configurations without data loss
    When User opens Tag Manager for a "<SETUP>"
    # Framework Gap: Requires step definitions for channel configuration edge cases in LifeSteps.java
    Then The result is "<EXPECTED>" with no error and no data deletion
    Examples:
      | SETUP                                 | EXPECTED                                                   |
      | account with only the Site channel    | Site appears in navigation and other channels are empty     |
      | existing account with Social channel data | Social data is retained even though the Social toggle is hidden |

  # Source: ET-24718 (R01 functional)
  @todo
  Scenario: All four visible channels remain fully functional after the navigation order change
    When User creates or edits a tag in each of the Site, Search, Email and Media channels
    # Framework Gap: Requires step definitions for per-channel tag save in LifeSteps.java
    Then Tags save correctly for all four channels and no channel is broken by the order change
