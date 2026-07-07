Feature: AdSetup HTTPS Default and Life-Only Endpoint Toggle for RTS Accounts

  1. Verifies that all RTS AdSetup endpoints render as HTTPS by default for new and existing accounts.
  2. Verifies that the Life-Only toggle auto-populates the correct MPC bid URLs on the AdSetup page and reverts them when toggled back.
  3. Covers placeholder correctness, endpoint hostname regression, and scope-boundary and layout regression anchors.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply Portal
    And User opens an RTS account and navigates to the AdSetup page

  @todo
  Scenario Outline: AdSetup endpoints render as HTTPS by default for "<ACCOUNT_TYPE>" accounts in "<REGION>"
    Then the "<REGION>" endpoint on the AdSetup page should be displayed as HTTPS
    And no manual edit should be required to switch it from HTTP to HTTPS
    Examples:
      | ACCOUNT_TYPE | REGION           |
      | Existing     | US East Coast    |
      | Existing     | US West Coast    |
      | Existing     | Europe           |
      | New          | US East Coast    |
      | New          | US West Coast    |
      | New          | Europe           |

  @todo
  Scenario Outline: Activating the Life-Only toggle auto-populates the "<REGION>" MPC bid URL
    Given User navigates to the Settings page for the RTS account
    When User sets the Life-Only toggle to OFF to activate Life-Only mode
    And User navigates back to the AdSetup page
    Then the "<REGION>" endpoint should be auto-populated with the Life-Only MPC bid URL "<EXPECTED_URL>"
    Examples:
      | REGION        | EXPECTED_URL                                        |
      | US East Coast | https://ma2-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |
      | US West Coast | https://sjc-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |
      | Europe        | https://am1-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |

  @todo
  Scenario Outline: Deactivating the Life-Only toggle reverts the "<REGION>" endpoint to the standard exchange endpoint
    Given the Life-Only toggle is currently set to OFF and Life-Only mode is active
    When User sets the Life-Only toggle to ON to deactivate Life-Only mode
    And User navigates back to the AdSetup page
    Then the "<REGION>" endpoint should revert to the standard exchange endpoint
    Examples:
      | REGION        |
      | US East Coast |
      | US West Coast |
      | Europe        |

  @todo
  @regression
  Scenario: The URL_ALIAS placeholder is correctly replaced and not the stale PUBNAME placeholder
    Given the Life-Only toggle is set to OFF and Life-Only mode is active
    Then the populated Life-Only endpoint should replace the URL_ALIAS placeholder with the account's actual URL alias
    And the endpoint should not contain a PUBNAME placeholder

  @todo
  @regression
  Scenario: The US East Coast Life-Only endpoint uses the migrated ma2-mpc hostname, not the legacy lga-mpc hostname
    Given the Life-Only toggle is set to OFF and Life-Only mode is active
    Then the US East Coast endpoint should use the ma2-mpc contextweb hostname
    And the US East Coast endpoint should not use the legacy lga-mpc hostname

  @todo
  Scenario: Rapid toggling of the Life-Only setting does not leave a stale endpoint state
    Given User navigates to the Settings page for the RTS account
    When User toggles the Life-Only setting ON, OFF, and ON again in quick succession
    And User navigates back to the AdSetup page
    Then the AdSetup endpoints should reflect only the final toggle state with no race condition

  @todo
  Scenario: Life-Only endpoint values persist across a page refresh
    Given the Life-Only toggle is set to OFF and Life-Only mode is active
    When User refreshes the AdSetup page
    Then the endpoints should still display the Life-Only MPC bid URLs

  @todo
  Scenario: Endpoint values remain consistent after browser back and forward navigation
    Given the Life-Only toggle is set to OFF and Life-Only mode is active
    When User navigates away from the AdSetup page and returns using the browser back button
    Then the endpoint values should remain consistent with the current toggle state

  @todo
  Scenario: All three geo endpoints are visible simultaneously with clear region labeling
    Then the US East Coast, US West Coast, and Europe endpoints should all be visible on the AdSetup page at once
    And each endpoint should be clearly labeled with the region it corresponds to

  @todo
  Scenario: The AdSetup HTTPS and Life-Only changes do not alter underlying bid routing or API calls
    Given the Life-Only toggle is set to OFF and Life-Only mode is active
    Then the underlying bid routing and API calls should remain unchanged
    And only the displayed endpoint values on the AdSetup page should differ

  @todo
  Scenario: Non-RTS accounts are unaffected by the HTTPS default and Life-Only toggle changes
    Given User opens a non-RTS account and navigates to its AdSetup page
    Then the endpoint display behavior for that account should be unaffected by this change

  @todo
  Scenario: Copying an endpoint from the AdSetup page copies the HTTPS URL
    When User uses the copy action on an endpoint on the AdSetup page
    Then the copied value should be the HTTPS URL

  @todo
  Scenario: A second concurrent session sees updated endpoints after refreshing following a Life-Only toggle
    Given a second user session has the same RTS account's AdSetup page open
    When the first user sets the Life-Only toggle to OFF and activates Life-Only mode
    And the second session refreshes the AdSetup page
    Then the second session should see the updated Life-Only endpoints

  @todo
  @regression
  Scenario: The PulsePoint logo update did not affect the AdSetup endpoint display layout
    Then the AdSetup endpoint display area should render with its expected layout
    And no visual regression from the sibling logo update should be present
