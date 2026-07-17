Feature: Supply Portal AdSetup - HTTPS Default Endpoints and Life-Only Bid URL Toggle
  1. Verify all RTS account endpoints in the Supply Portal AdSetup page render as HTTPS by default, for both new and existing accounts across all three supported regions
  2. Verify that disabling RTB bidding via the Life-Only toggle in Settings auto-populates the AdSetup endpoints with the Life-Only MPC bid URLs, and that re-enabling RTB bidding reverts the endpoints to the standard exchange endpoints
  3. Verify the US East Coast endpoint uses the current ma2-mpc.contextweb.com hostname mapping
  4. Verify the Life-Only endpoint toggle has no effect on non-RTS accounts and does not alter underlying bid routing or the Settings toggle logic itself

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  # Source: ET-24252
  @todo
  Scenario Outline: Verify AdSetup endpoints render as HTTPS by default for <ACCOUNT_TYPE> RTS accounts in <REGION>
    Given User navigates to the Supply Portal AdSetup page for a "<ACCOUNT_TYPE>" RTS account
    Then The "<REGION>" endpoint displayed on the AdSetup page uses the HTTPS scheme by default
    And No manual intervention is required to switch the endpoint from HTTP to HTTPS

    Examples:
      | ACCOUNT_TYPE | REGION        |
      | New          | US East Coast |
      | New          | US West Coast |
      | New          | Europe        |
      | Existing     | US East Coast |
      | Existing     | US West Coast |
      | Existing     | Europe        |

  # Source: ET-24252
  @todo
  Scenario Outline: Verify the Life-Only toggle auto-populates the correct Life-Only MPC bid URL for <REGION>
    Given The account's Life-Only toggle in Settings is switched to disable RTB bidding
    When User navigates to the AdSetup page
    Then The "<REGION>" endpoint auto-populates with "<LIFE_ONLY_ENDPOINT>"
    And The #URL_ALIAS# placeholder is replaced with the account's actual URL alias

    Examples:
      | REGION        | LIFE_ONLY_ENDPOINT                                     |
      | US East Coast | https://ma2-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |
      | US West Coast | https://sjc-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |
      | Europe        | https://am1-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |

  # Source: ET-24252
  @todo
  Scenario: Verify the Life-Only toggle reverts correctly, has no effect outside RTS accounts, and does not alter bid routing behavior
    Given The account's Life-Only toggle in Settings is switched to disable RTB bidding
    And User navigates to the AdSetup page
    Then The endpoints display the Life-Only MPC bid URLs
    When User switches the Life-Only toggle in Settings back to re-enable RTB bidding
    And User navigates to the AdSetup page
    Then The endpoints revert to the standard exchange endpoints
    And The US East Coast endpoint hostname reflects the current "ma2-mpc.contextweb.com" mapping
    Given The account is not an RTS account
    Then The AdSetup page's endpoint display for that account is unaffected by this feature
    When User inspects the underlying bid routing API calls and the Settings toggle logic while completing this workflow
    Then Those calls and that logic are unchanged by the HTTPS-default and Life-Only endpoint display changes
