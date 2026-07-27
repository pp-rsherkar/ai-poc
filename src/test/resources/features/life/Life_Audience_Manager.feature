Feature: LIFE Regression - Audience Manager Meta push scope and AM Settings permission gating
  Verifies that a Meta push from Audience Manager creates only a Customer File Custom Audience (no Website Custom Audience), and that Admin > AM Settings Login/Logout/Audit Log controls are visible to internal users independent of the Pinterest, LinkedIn, and TikTok sign-in permissions, while the audience-panel Sign-in buttons remain permission-gated.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"

  @todo
  # Source: ET-24706
  Scenario: A Meta push creates only a Customer File Custom Audience and no Website Custom Audience
    And User navigates to Audience Manager
    # Framework Gap: Requires step definition asserting the outbound Meta push payload omits the web-audience signal in LifeSteps.java
    When User pushes an audience never previously pushed to Meta
    Then No payload instructing creation of a Website Custom Audience is sent to the cluster service
    And Push completes, AM status updates to Synced/Active, and only a Customer File Custom Audience is created in the client Meta Ad Manager account
    And The Customer File Custom Audience is populated with the pushed identifiers exactly as before the change

  @todo
  # Source: ET-24706, AMB-1
  Scenario Outline: Meta push status, match counts, and non-Meta platforms are unaffected by the removed web-audience signal
    And User navigates to Audience Manager
    When "<REGRESSION_CHECK>"
    Then "<EXPECTED_RESULT>"
    Examples:
      | REGRESSION_CHECK                                 | EXPECTED_RESULT                                                                                  |
      | Re-push a previously created Meta audience       | All statuses remain accurate with no errors or regressions in push state                         |
      | Compare match counts before and after the change | Match counts are consistent with the pre-deploy baseline                                         |
      | Force a push failure using an expired Meta token | The failure is reported with an appropriate status/error; the push is not reported as successful |
      | Push to a non-Meta platform after the change     | Non-Meta pushes behave exactly as before the removed signal                                      |

  @todo
  # Source: ET-24706, GAP-1, GAP-2
  Scenario: In-flight pushes and cluster backward-compatibility are unaffected by the signal removal
    And User navigates to Audience Manager
    # Framework Gap: Requires step definition for an in-flight push started immediately before deploy in LifeSteps.java
    When A Meta push is initiated just before the web-audience signal is removed
    Then The in-flight push completes without creating an orphaned or partial web audience
    When A Meta push is run against the current cluster service without the web-audience signal
    Then The push succeeds without the signal and no failure arises from its absence

  @todo
  # Source: ET-24705, GAP-1
  Scenario Outline: Admin > AM Settings Login/Logout/Audit Log is visible on the Pinterest, LinkedIn, and TikTok cards regardless of sign-in permission
    And User navigates to route "/Buyer/#/account-new/account-list/{accountId}/amsettings"
    Given "<PERMISSION_STATE>"
    When User views the "<CARD>" AM Settings card
    Then "<EXPECTED_RESULT>"
    Examples:
      | CARD      | PERMISSION_STATE                                      | EXPECTED_RESULT                                                                            |
      | Pinterest | Internal user lacking VIEW PINTEREST SIGNIN           | Login, Logout and Audit Log controls are visible on the Pinterest card                     |
      | Pinterest | Internal user WITH VIEW PINTEREST SIGNIN              | Login/Logout/Audit Log remain visible; card visibility is decoupled from the permission    |
      | LinkedIn  | Internal user lacking the LinkedIn sign-in permission | Login, Logout and Audit Log are visible on the LinkedIn card                               |
      | TikTok    | Internal user (any)                                   | Login, Logout and Audit Log are visible on the TikTok card with no permission gate applied |

  @todo
  # Source: ET-24705, AMB-1
  Scenario Outline: The audience-panel Sign-in buttons remain gated by their existing permissions, unchanged
    And User navigates to Audience Manager and opens the Audience create/edit panel
    Given "<PANEL_PERMISSION_STATE>"
    Then "<EXPECTED_SIGNIN_BUTTON_STATE>"
    Examples:
      | PANEL_PERMISSION_STATE                       | EXPECTED_SIGNIN_BUTTON_STATE                                                |
      | User WITH VIEW PINTEREST SIGNIN              | The Pinterest Sign-in button is visible in the Audience create/edit panel   |
      | User WITHOUT VIEW PINTEREST SIGNIN           | The Pinterest Sign-in button is not shown in the Audience create/edit panel |
      | User WITH the LinkedIn sign-in permission    | The LinkedIn Sign-in button is visible in the Audience create/edit panel    |
      | User WITHOUT the LinkedIn sign-in permission | The LinkedIn Sign-in button is not shown in the Audience create/edit panel  |

  @todo
  # Source: ET-24705, AMB-2, GAP-2
  # Regression anchor: HT-6094, HT-6134 - permissioning churn and Audience Manager not loading
  Scenario: The Audit Log sub-control is specifically ungated and other AM Settings controls are unaffected
    And User navigates to route "/Buyer/#/account-new/account-list/{accountId}/amsettings"
    Given Internal user without VIEW PINTEREST SIGNIN
    When User opens Audit Log on the Pinterest card
    Then The Audit Log opens and is usable, confirming all three sub-controls are ungated
    And All other Admin > AM Settings controls behave as before with no unrelated control changed
    # Framework Gap: Requires step definition documenting external-user visibility of the AM Settings cards in LifeSteps.java
    Given An external user views Admin > AM Settings
    Then External-user visibility of the cards follows the defined rule and is recorded for product confirmation
