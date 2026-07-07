Feature: RTS AdSetup HTTPS Default and Life-Only Endpoint Toggle
  1. All RTS AdSetup endpoints (US East Coast, US West Coast, Europe) render as HTTPS by default, for both existing and newly created accounts, with no manual edit required.
  2. When an RTS account's Life-Only toggle is set to OFF (RTB bidding disabled / Life-Only mode active), AdSetup auto-populates the Life-Only MPC bid URLs for all three regions; switching back to ON reverts the endpoints to the standard exchange URLs.
  3. Assumption (Ambiguity Detected 1, ET-24252): "Life-Only toggle set to OFF" is read as Interpretation A - OFF means RTB is disabled and the account operates in Life-Only/MPC mode - matching the QA-1013 verification screenshots. See PR body for the open clarification question.
  4. Assumption (Contradicting Requirement Detected, ET-24252): the US East Coast hostname and URL placeholder use ET-24252 own values (ma2-mpc.contextweb.com, URL_ALIAS) as source of truth, not QA-1013's stale lga-mpc/PUBNAME values. See PR body.
  5. No backend routing, bidding, or Settings-page toggle logic changes are in scope - this covers the AdSetup FE display only.

  @todo
  Scenario Outline: Verify RTS AdSetup endpoints render as HTTPS by default for "<ACCOUNT_TYPE>" accounts in "<REGION>"
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AdSetup page for a "<ACCOUNT_TYPE>" RTS account in "<REGION>" # NEW STEP
    Then The endpoint displayed for "<REGION>" should use the "https" protocol with no manual edit required # NEW STEP
    Examples:
      | ACCOUNT_TYPE | REGION                    |
      | Existing     | US East Coast (Virginia)  |
      | Existing     | US West Coast (SJC)       |
      | Existing     | Europe (AMS)              |
      | New          | US East Coast (Virginia)  |
      | New          | US West Coast (SJC)       |
      | New          | Europe (AMS)              |

  @todo
  Scenario Outline: Verify Life-Only toggle set to OFF auto-populates the MPC bid URL for "<REGION>" on AdSetup
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Settings page for an RTS account and sets the Life-Only toggle to "OFF" # NEW STEP
    When User navigates to the AdSetup page for that account # NEW STEP
    Then The "<REGION>" endpoint should auto-populate with "<MPC_ENDPOINT>" # NEW STEP
    Examples:
      | REGION                   | MPC_ENDPOINT                                           |
      | US East Coast (Virginia) | https://ma2-mpc.contextweb.com/bid/openrtb/#URL_ALIAS#  |
      | US West Coast (SJC)      | https://sjc-mpc.contextweb.com/bid/openrtb/#URL_ALIAS#  |
      | Europe (AMS)             | https://am1-mpc.contextweb.com/bid/openrtb/#URL_ALIAS#  |

  @todo
  Scenario Outline: Verify Life-Only toggle set to ON reverts the "<REGION>" endpoint to the standard exchange URL on AdSetup
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Settings page for an RTS account with the Life-Only toggle currently "OFF" # NEW STEP
    When User sets the Life-Only toggle to "ON" # NEW STEP
    And User navigates to the AdSetup page for that account
    Then The "<REGION>" endpoint should revert to the standard exchange endpoint # NEW STEP
    Examples:
      | REGION                   |
      | US East Coast (Virginia) |
      | US West Coast (SJC)      |
      | Europe (AMS)             |

  @todo
  Scenario: Verify the #URL_ALIAS# placeholder in Life-Only endpoints is replaced with the account's actual URL alias
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Settings page for an RTS account and sets the Life-Only toggle to "OFF"
    When User navigates to the AdSetup page for that account
    Then Each populated Life-Only endpoint should have "#URL_ALIAS#" replaced with the account's actual URL alias, and not display a "#PUBNAME#" token # NEW STEP

  @todo
  Scenario: Regression - verify the US East Coast Life-Only endpoint uses the migrated ma2-mpc hostname and not the legacy lga-mpc hostname
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Settings page for an RTS account and sets the Life-Only toggle to "OFF"
    When User navigates to the AdSetup page for that account
    Then The US East Coast endpoint should display the "ma2-mpc.contextweb.com" hostname and not the legacy "lga-mpc.contextweb.com" hostname # NEW STEP

  @todo
  Scenario: Verify rapid Life-Only toggle switching does not leave AdSetup endpoints in a stale state
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Settings page for an RTS account
    When User toggles Life-Only "ON", "OFF" and "ON" in quick succession # NEW STEP
    And User navigates to the AdSetup page for that account
    Then The endpoints displayed should reflect only the final toggle state with no stale or duplicated values # NEW STEP

  @todo
  Scenario: Verify Life-Only endpoint state persists on AdSetup after a page refresh
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Settings page for an RTS account and sets the Life-Only toggle to "OFF"
    And User navigates to the AdSetup page for that account
    When User refreshes the AdSetup page # NEW STEP
    Then The Life-Only MPC endpoints should still be displayed for all three regions # NEW STEP

  @todo
  Scenario: Verify AdSetup endpoint state is consistent after browser back and forward navigation following a Life-Only toggle change
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Settings page for an RTS account and sets the Life-Only toggle to "OFF"
    And User navigates to the AdSetup page for that account
    When User navigates away from the AdSetup page and then navigates back using the browser back button # NEW STEP
    Then The AdSetup page should display a consistent endpoint state, either preserved or cleanly reset # NEW STEP

  @todo
  Scenario: Verify all three geo endpoints are displayed simultaneously on AdSetup with clear region labeling
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AdSetup page for an RTS account
    Then The US East Coast, US West Coast, and Europe endpoints should all be visible at once, each clearly labeled with its corresponding region # NEW STEP

  @todo
  Scenario: Verify toggling Life-Only does not alter the underlying bid routing or API calls
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the Settings page for an RTS account
    When User toggles the Life-Only setting # NEW STEP
    Then The AdSetup endpoint display should update, but the underlying bid routing and API calls should remain unaffected # NEW STEP

  @todo
  Scenario: Verify AdSetup behavior for non-RTS account types is unaffected by the HTTPS default and Life-Only toggle changes
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AdSetup page for a non-RTS account # NEW STEP
    Then The AdSetup page for the non-RTS account should render unaffected by the RTS HTTPS default and Life-Only toggle changes # NEW STEP

  @todo
  Scenario: Verify copying an AdSetup endpoint via the copy-to-clipboard action copies the HTTPS URL
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AdSetup page for an RTS account
    When User uses the copy-to-clipboard action on a displayed endpoint # NEW STEP
    Then The copied value should be the HTTPS endpoint URL and not an HTTP URL # NEW STEP

  @todo
  Scenario: Verify a second concurrent session sees updated endpoints after a Life-Only toggle change on refresh
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Another session has the same RTS account's AdSetup page open # NEW STEP
    When User sets the Life-Only toggle to "OFF" for that account in the first session # NEW STEP
    And The second session refreshes the AdSetup page # NEW STEP
    Then The second session should display the updated Life-Only endpoints # NEW STEP

  @todo
  Scenario: Regression - verify the PulsePoint logo update from ET-24259 did not affect the AdSetup endpoint display layout
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User navigates to the AdSetup page for an RTS account
    Then The endpoint display area layout should render correctly and be unaffected by the logo update delivered under ET-24259 # NEW STEP
