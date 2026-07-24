Feature: LIFE Regression - Tag Manager displays advertising channels in the standardized order of Site, Search, Email, and Media.
  The channel order is presented consistently across the main navigation, Settings, and tag code display views.
  Each channel remains fully functional and its tags stay sorted consistently after navigation and page reloads.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    Given User navigates to the "Tag Manager" page

  # Source: ET-24718
  @todo
  Scenario Outline: Channels display in the enforced order across Tag Manager views
    # Framework Gap: Requires step definitions for opening a specific Tag Manager view in LifeSteps.java
    When User opens the "<VIEW>" view in Tag Manager
    # Framework Gap: Requires step definitions for verifying channel display order in LifeSteps.java
    Then Verify the channels are displayed in the following order
      | CHANNEL | POSITION |
      | Site    | 1        |
      | Search  | 2        |
      | Email   | 3        |
      | Media   | 4        |
    # Framework Gap: Requires step definitions for verifying a channel is hidden in LifeSteps.java
    And Verify the "Social" channel is not visible in the "<VIEW>" view
    Examples:
      | VIEW             |
      | main navigation  |
      | Settings         |
      | Tag code display |

  # Source: ET-24718
  @todo
  Scenario: Existing Social channel configuration is retained while the Social channel is hidden
    # Framework Gap: Requires step definitions for loading an existing configuration with Social data in LifeSteps.java
    Given User opens an existing Tag Manager configuration containing "Social" channel data
    # Framework Gap: Requires step definitions for verifying a channel is hidden in LifeSteps.java
    Then Verify the "Social" channel is not visible in the "main navigation" view
    # Framework Gap: Requires step definitions for verifying retained channel data in LifeSteps.java
    And Verify the "Social" channel configuration data is retained and not deleted

  # Source: ET-24718
  @todo
  Scenario: Navigating between channels in the new order preserves data without errors
    # Framework Gap: Requires step definitions for navigating sequentially through channels in LifeSteps.java
    When User navigates through the channels in the order "Site, Search, Email, Media"
    # Framework Gap: Requires step definitions for verifying no navigation errors or data loss in LifeSteps.java
    Then Verify each channel loads without error or data loss

  # Source: ET-24718
  @todo
  Scenario: Tags within each channel are sorted consistently and remain stable after reload
    # Framework Gap: Requires step definitions for verifying tag sort order within a channel in LifeSteps.java
    Then Verify the tags within each channel are displayed in a consistent sort order
    # Framework Gap: Requires step definitions for reloading the Tag Manager page in LifeSteps.java
    When User reloads the Tag Manager page
    # Framework Gap: Requires step definitions for verifying tag sort order within a channel in LifeSteps.java
    Then Verify the tags within each channel retain the same sort order after reload

  # Source: ET-24718
  @todo
  Scenario: A Tag Manager configured with only the Site channel displays correctly without error
    # Framework Gap: Requires step definitions for loading a single-channel configuration in LifeSteps.java
    Given User opens a Tag Manager configuration containing only the "Site" channel
    # Framework Gap: Requires step definitions for verifying channel display order in LifeSteps.java
    Then Verify the "Site" channel is displayed in position "1" without error

  # Source: ET-24718
  @todo
  Scenario Outline: Each channel remains fully functional after the order change
    # Framework Gap: Requires step definitions for opening a specific channel in Tag Manager in LifeSteps.java
    When User opens the "<CHANNEL>" channel in Tag Manager
    # Framework Gap: Requires step definitions for verifying channel functionality in LifeSteps.java
    Then Verify the "<CHANNEL>" channel is fully functional at position "<POSITION>"
    Examples:
      | CHANNEL | POSITION |
      | Site    | 1        |
      | Search  | 2        |
      | Email   | 3        |
      | Media   | 4        |
