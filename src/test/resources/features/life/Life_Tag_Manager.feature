Feature: LIFE Regression - Tag Manager channel navigation order and sorting
  1. Channel navigation renders in the order Site, Search, Email, Media with Social hidden
  2. Channel order is consistent across navigation, settings and tag code display views
  3. Tags within each channel are sorted consistently and existing data is preserved

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User opens Tag Manager

  # Source: ET-24718
  @todo
  Scenario: Verify Tag Manager channel order, hidden Social channel and consistent sorting across views
    # Framework Gap: Requires step definitions for Tag Manager navigation and settings in LifeSteps.java
    When User views the Tag Manager channel navigation
    Then The channels render in the order Site, Search, Email, Media and the Social channel is not visible anywhere
    And The Settings view and the tag code display view list channels in the same Site, Search, Email, Media order
    And Tags within each channel section are sorted consistently and the order is stable on reload
    And Switching between channels preserves each channel's configuration with no data loss
    And Existing Tag Manager configurations including hidden Social channel data are preserved and not deleted
    And Creating or editing a tag in each of Site, Search, Email and Media saves correctly
    And A Tag Manager with only one configured channel still shows the correct navigation order with no error
