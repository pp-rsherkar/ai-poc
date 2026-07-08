Feature: Supply Portal AdSetup - HTTPS Default Endpoints and Life-Only Endpoint Toggle

  1. Makes all RTS account endpoints on the AdSetup page render as HTTPS by default, for new and existing accounts across all regions.
  2. Auto-populates the Life-Only MPC bid URLs on the AdSetup page when the Life-Only toggle in Settings puts the account into Life-Only mode, and reverts to standard exchange endpoints when RTB bidding is re-enabled.
  3. Covers the documented edge cases, a hostname-migration regression anchor (LGA to MA2), and the scope boundary confirming no backend routing changes are introduced.
  4. Per Ambiguity Interpretation A, "Life-Only toggle set to OFF" means the account IS in Life-Only mode (RTB disabled), matching the QA-1013 verification evidence.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply Portal AdSetup page for an RTS account

  @todo
  Scenario Outline: RTS account endpoints render as HTTPS by default without manual intervention
    Given the account is a "<ACCOUNT_TYPE>" RTS account
    Then all displayed endpoints on the AdSetup page render as HTTPS for US East Coast, US West Coast, and Europe
    Examples:
      | ACCOUNT_TYPE |
      | new          |
      | existing     |

  @todo
  Scenario: Activating the Life-Only toggle auto-populates the MPC bid URLs on AdSetup
    Given the Life-Only toggle in Settings is switched OFF, putting the account into Life-Only mode
    When User navigates to the AdSetup page
    Then the endpoints auto-populate with the Life-Only MPC bid URLs
      | Region              | Endpoint                                                |
      | US East Coast (MA2) | https://ma2-mpc.contextweb.com/bid/openrtb/#URL_ALIAS#  |
      | US West Coast (SJC) | https://sjc-mpc.contextweb.com/bid/openrtb/#URL_ALIAS#  |
      | Europe (AMS)        | https://am1-mpc.contextweb.com/bid/openrtb/#URL_ALIAS#  |
    And the #URL_ALIAS# placeholder is replaced with the account's actual URL alias

  @todo
  Scenario: Deactivating the Life-Only toggle reverts AdSetup to standard exchange endpoints
    Given the Life-Only toggle in Settings is switched ON, re-enabling RTB bidding
    When User navigates to the AdSetup page
    Then the endpoints revert to the standard exchange endpoints and no longer show the Life-Only MPC bid URLs

  @todo
  Scenario: Rapidly toggling Life-Only does not leave the AdSetup page in a stale endpoint state
    When User switches the Life-Only toggle OFF, then ON, then OFF again in quick succession
    And User navigates to the AdSetup page
    Then the endpoints displayed match only the final toggle state, with no race condition or stale values

  @todo
  Scenario Outline: Life-Only endpoint state persists across a page refresh and browser navigation
    Given the Life-Only toggle in Settings is switched OFF, putting the account into Life-Only mode
    When User "<NAVIGATION_ACTION>" the AdSetup page
    Then the endpoints remain the Life-Only MPC bid URLs
    Examples:
      | NAVIGATION_ACTION                           |
      | refreshes                                   |
      | navigates away from and returns to via back |

  @todo
  Scenario: All three geo endpoints are displayed simultaneously with clear per-region labeling
    Then the AdSetup page displays the US East Coast, US West Coast, and Europe endpoints at the same time
    And each endpoint is clearly labeled with the geo region it corresponds to

  @todo
  Scenario: The US East Coast endpoint uses the current MA2 hostname, not the retired LGA hostname
    Given the Life-Only toggle in Settings is switched OFF, putting the account into Life-Only mode
    When User navigates to the AdSetup page
    Then the US East Coast endpoint hostname is "ma2-mpc.contextweb.com"
    And the US East Coast endpoint hostname is not "lga-mpc.contextweb.com"
    # Regression anchor: QA-1013 lists the retired lga-mpc hostname and #PUBNAME# placeholder; ET-24252 is the authoritative source (ma2-mpc, #URL_ALIAS#)

  @todo
  Scenario: The AdSetup HTTPS and Life-Only changes do not alter underlying bid routing or Settings toggle logic
    When User inspects the API calls made while viewing the AdSetup page
    Then the underlying bid routing API calls and the Settings page toggle logic are unchanged by this feature

  @todo
  Scenario: Copying an endpoint from AdSetup copies the HTTPS URL
    When User uses the copy-to-clipboard action on a displayed endpoint
    Then the copied value is the HTTPS URL, not an HTTP URL

  @todo
  Scenario: Non-RTS account types are unaffected by the AdSetup HTTPS and Life-Only changes
    Given the account is not an RTS account
    Then the AdSetup page behavior for that account is unaffected by this feature

  @todo
  Scenario: A second concurrent session sees the updated endpoints after a Life-Only toggle change
    Given two sessions are viewing the same account's AdSetup page
    When one session switches the Life-Only toggle in Settings
    Then the other session sees the updated endpoints after refreshing the AdSetup page

  @todo
  Scenario: The sibling PulsePoint logo update does not affect the AdSetup endpoint display area
    Then the AdSetup page's endpoint display area renders correctly and is unaffected by the ET-24259 logo update
    # Regression anchor: ET-24259 - PulsePoint logo update shipped under the same parent epic PROD-15374
