Feature: Supply Portal AdSetup - HTTPS Default Endpoints and Life-Only Endpoint Toggle
  1. All RTS AdSetup endpoints render as HTTPS by default for new and existing accounts.
  2. The Life-Only toggle in Settings auto-populates the correct Life-Only/MPC bid URLs on AdSetup.
  3. Reverting the toggle restores the standard exchange endpoints.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to Supply Portal AdSetup page for an RTS account

  @todo
  Scenario Outline: Existing RTS account endpoints display as HTTPS by default
    Given the RTS account has existing endpoints
    When User views the Endpoints section on the AdSetup page
    Then the "<REGION>" endpoint should display as HTTPS without manual edit
    Examples:
      | REGION              |
      | US East Coast       |
      | US West Coast       |
      | Europe               |

  @todo
  Scenario: A newly created RTS account shows HTTPS endpoints from the start
    When User creates a new RTS account
    And User navigates to the AdSetup page for the new account
    Then all displayed endpoints should be HTTPS from creation, with no manual edit required

  @todo
  Scenario Outline: Activating the Life-Only toggle auto-populates the Life-Only MPC bid URLs
    Given the Life-Only toggle in Settings is set to "OFF"
    When User navigates to the AdSetup page
    Then the "<REGION>" endpoint should auto-populate with "<MPC_URL>"
    Examples:
      | REGION         | MPC_URL                                                |
      | US East Coast  | https://ma2-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |
      | US West Coast  | https://sjc-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |
      | Europe         | https://am1-mpc.contextweb.com/bid/openrtb/#URL_ALIAS# |

  @todo
  Scenario: Deactivating the Life-Only toggle reverts endpoints to the standard exchange endpoints
    Given the Life-Only toggle in Settings is set to "OFF" and AdSetup shows the Life-Only MPC endpoints
    When User switches the Life-Only toggle back to "ON" in Settings
    And User navigates to the AdSetup page
    Then all endpoints should revert to the standard exchange endpoints

  @todo
  Scenario: Rapidly switching the Life-Only toggle does not leave stale endpoint state
    When User toggles Life-Only "OFF", then "ON", then "OFF" in quick succession
    And User navigates to the AdSetup page
    Then the endpoints displayed should reflect only the final toggle state, with no stale or race-condition artifacts

  @todo
  Scenario: Life-Only endpoint state persists across a page refresh
    Given the Life-Only toggle is set to "OFF" and AdSetup shows the Life-Only MPC endpoints
    When User refreshes the AdSetup page
    Then the Life-Only MPC endpoints should still be displayed

  @todo
  Scenario: Life-Only endpoint state is consistent after browser back/forward navigation
    Given the Life-Only toggle is set to "OFF" and AdSetup shows the Life-Only MPC endpoints
    When User navigates away from the AdSetup page and returns using the browser back button
    Then the endpoint state should remain consistent with the current toggle setting

  @todo
  Scenario: The URL_ALIAS placeholder is replaced with the account's actual URL alias
    Given the Life-Only toggle is set to "OFF"
    When User views the Life-Only endpoints on the AdSetup page
    Then the "#URL_ALIAS#" placeholder in each endpoint should be replaced with the account's actual URL alias

  @todo
  Scenario: The Europe endpoint renders correctly with HTTPS default for an EU-region account
    Given the account is configured for the Europe region
    When User views the Endpoints section on the AdSetup page
    Then the "am1-mpc" Europe endpoint should render with HTTPS by default

  @todo
  Scenario: All three geo endpoints are displayed simultaneously with clear region labeling
    When User views the Endpoints section on the AdSetup page
    Then the US East Coast, US West Coast, and Europe endpoints should all be visible at the same time
    And each endpoint should be clearly labeled with the geo region it corresponds to

  @todo
  Scenario: The AdSetup HTTPS/Life-Only change does not alter backend bid routing or the Settings toggle logic
    When User views the Endpoints section on the AdSetup page with either toggle state
    Then the underlying API calls and bid routing behavior should be unaffected by this front-end display change

  @todo
  Scenario: Copying an endpoint via the copy-to-clipboard control copies the HTTPS URL
    When User clicks the copy-to-clipboard control on an endpoint
    Then the copied value should be the HTTPS URL, not an HTTP URL

  @todo
  Scenario: AdSetup behavior for non-RTS account types is unaffected
    Given the account is a non-RTS account type
    When User views the AdSetup page for that account
    Then the endpoint display behavior for non-RTS accounts should be unchanged by this feature

  @todo
  Scenario: A concurrent session sees the updated endpoints after the Life-Only toggle changes
    Given two sessions are viewing the same account's AdSetup page
    When one session toggles Life-Only in Settings
    Then the other session should see the updated endpoints after refreshing

  @todo
  Scenario: Regression - US East Coast endpoint uses the current ma2-mpc hostname and URL_ALIAS placeholder
    Given the Life-Only toggle is set to "OFF"
    When User views the US East Coast endpoint on the AdSetup page
    Then the endpoint should use "ma2-mpc.contextweb.com" and the "#URL_ALIAS#" placeholder
    And the endpoint should not use the stale "lga-mpc.contextweb.com" hostname or "#PUBNAME#" placeholder

  @todo
  Scenario: Regression - the ET-24259 logo update does not affect the AdSetup endpoint display area
    When User views the AdSetup page after the PulsePoint logo update
    Then the Endpoints section layout and content should be unaffected by the logo change
