Feature: LIFE Regression - Tag Manager v1.0 channel navigation, sorting, and plug-in link
  Verifies the Tag Manager v1.0 channel ordering in settings and tag-code display, the hidden Social channel toggle, descending timestamp sorting of tag collections, the fixed Site tag-type order, and the corrected Chrome plug-in link.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Tag Manager

  @todo
  # Source: ET-24718
  Scenario: Channel navigation order is Site, Search, Email, Media in both settings and tag-code display, with Social hidden
    When User opens Tag Manager settings
    Then Channels render top-to-bottom as "Site, Search, Email, Media"
    When User opens the tag-code display view
    Then The channel order matches settings exactly, with parity between the two surfaces
    And No Social channel toggle or option is present in either surface

  @todo
  # Source: ET-24718, GAP-1, AMB-1
  Scenario Outline: Tag collections sort descending by timestamp with a deterministic tie-break
    Given "<TIMESTAMP_CASE>"
    # Framework Gap: Requires step definition for the timestamp tie-break rule in LifeSteps.java
    When User views the Tag Manager collections list
    Then "<EXPECTED_ORDER_CHECK>"
    Examples:
      | TIMESTAMP_CASE                                            | EXPECTED_ORDER_CHECK                                                                   |
      | Multiple tag collections with distinct created timestamps | List orders collections newest-to-oldest by timestamp                                  |
      | Two tag collections with identical timestamps             | A consistent, repeatable tie-break is applied so ordering is not random across reloads |

  @todo
  # Source: ET-24718
  Scenario: Site tag types are ordered Google, Tealium, Adobe, Generic Javascript, Generic Image
    When User opens the Site channel tag-type picker
    Then Tag types appear in exactly the order "Google, Tealium, Adobe, Generic Javascript, Generic Image" with Google first

  @todo
  # Source: ET-24718
  Scenario: The Chrome plug-in link points to the corrected chromewebstore URL
    When User clicks the debugger plug-in link in Tag Manager
    Then The link target is "https://chromewebstore.google.com/detail/pulsepoint-debugger/oblmhmjodoldhjcogdganmlgleeejfbc" and resolves to the extension page
    And The deprecated "https://chrome.google.com/webstore" URL is not present

  @todo
  # Source: ET-24718, GAP-2
  Scenario: Existing Social-channel tags are handled predictably once the Social toggle is hidden
    Given An account with pre-existing Social-channel tags
    # Framework Gap: Requires step definition for the Social-channel legacy-tag handling path in LifeSteps.java
    When User views the Tag Manager collections list
    Then Prior Social tags follow a defined path (migrated, hidden, or read-only) with no orphaned or broken tag-code output
