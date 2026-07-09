Feature: Supply Portal AdSetup - HTTPS Default Endpoints and Life-Only Endpoint Toggle

  1. Makes all RTS account endpoints on the AdSetup page render as HTTPS by default, for new and existing accounts across all regions.
  2. Auto-populates the Life-Only MPC bid URLs on the AdSetup page when the Life-Only toggle in Settings puts the account into Life-Only mode, and reverts to standard exchange endpoints when RTB bidding is re-enabled.
  3. Per Ambiguity Interpretation A, "Life-Only toggle set to OFF" means the account IS in Life-Only mode (RTB disabled), matching the QA-1013 verification evidence.
  4. Each scenario walks a single continuous pass through the AdSetup and Settings pages, chaining the checks reachable along that path, per the domain's existing workflow-driven scenario style.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  Scenario: Verify the HTTPS-default and Life-Only endpoint toggle workflow on the AdSetup page
    Given User navigates to Supply Portal AdSetup page for a new RTS account
    Then all displayed endpoints render as HTTPS for US East Coast, US West Coast, and Europe
    Given User navigates to Supply Portal AdSetup page for an existing RTS account
    Then all displayed endpoints render as HTTPS for US East Coast, US West Coast, and Europe
    When User switches the Life-Only toggle in Settings OFF, putting the account into Life-Only mode
    And User navigates to the AdSetup page
    Then the endpoints auto-populate with the Life-Only MPC bid URLs
      | Region              | Endpoint                                               |
      | US East Coast (MA2) | https://ma2-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |
      | US West Coast (SJC) | https://sjc-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |
      | Europe (AMS)        | https://am1-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |
    And the #URL_ALIAS# placeholder is replaced with the account's actual URL alias
    And the US East Coast endpoint hostname is "ma2-mpc.contextweb.com", not the retired "lga-mpc.contextweb.com"
    # Regression anchor: QA-1013 lists the retired lga-mpc hostname and #PUBNAME# placeholder; ET-24252 is the authoritative source (ma2-mpc, #URL_ALIAS#)
    And all three geo endpoints are displayed simultaneously, each clearly labeled with the geo region it corresponds to
    When User uses the copy-to-clipboard action on a displayed endpoint
    Then the copied value is the HTTPS URL, not an HTTP URL
    When User refreshes the AdSetup page
    Then the endpoints remain the Life-Only MPC bid URLs
    When User navigates away from the AdSetup page and returns to it via back navigation
    Then the endpoints remain the Life-Only MPC bid URLs
    When User switches the Life-Only toggle in Settings OFF, then ON, then OFF again in quick succession, then navigates to the AdSetup page
    Then the endpoints displayed match only the final toggle state, with no race condition or stale values
    When User switches the Life-Only toggle in Settings ON, re-enabling RTB bidding
    And User navigates to the AdSetup page
    Then the endpoints revert to the standard exchange endpoints and no longer show the Life-Only MPC bid URLs
    When User inspects the API calls made while viewing the AdSetup page throughout this workflow
    Then the underlying bid routing API calls and the Settings page toggle logic are unchanged by this feature

  @todo
  Scenario: Verify the AdSetup HTTPS and Life-Only changes are correctly scoped to RTS accounts and do not leak across sessions or unrelated changes
    Given the account is not an RTS account
    Then the AdSetup page behavior for that account is unaffected by this feature
    Given two sessions are viewing the same RTS account's AdSetup page
    When one session switches the Life-Only toggle in Settings
    Then the other session sees the updated endpoints after refreshing the AdSetup page
    Then the AdSetup page's endpoint display area renders correctly and is unaffected by the ET-24259 logo update
    # Regression anchor: ET-24259 - PulsePoint logo update shipped under the same parent epic PROD-15374
