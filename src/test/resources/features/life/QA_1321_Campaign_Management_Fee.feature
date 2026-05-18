Feature: QA-1321 - Campaign management fee inheritance and override

  @regression @qa1321
  Scenario Outline: Verify campaign management fee is inherited and override works on tactic
    Given QA-1321 user logs into Life in the "<ENVIRONMENT>" environment as a "<USER_TYPE>" user for account "<ACCOUNT>"
    When QA-1321 user creates a campaign with details "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" and management fee "<CAMPAIGN_FEE_OPTION>" "<CAMPAIGN_PERCENT>" "<CAMPAIGN_AMOUNT>"
    And QA-1321 user creates a line item with details "<LINE_NAME>" "<LINE_BUDGET>"
    And QA-1321 user verifies management fee "<CAMPAIGN_DISPLAY_VALUE>" is inherited in line item and tactic "<TACTIC_NAME>"
    Then QA-1321 user overrides tactic management fee with the following values
      | Fee Option | Percent | Amount | Expected Display |
      | Percentage | 7.15    |        | + 7.15 %         |
      | CPM        |         | 10.50  | + $10.5          |
      | % + CPM    | 7       | 10     | + 7 % + $10      |
      | Fixed CPM  |         | 11.1   | $11.1            |

    Examples:
      | ENVIRONMENT | USER_TYPE | ACCOUNT               | ADVERTISER     | CP_NAME | CP_TYPE | CP_BUDGET | LINE_NAME | LINE_BUDGET | CAMPAIGN_FEE_OPTION | CAMPAIGN_PERCENT | CAMPAIGN_AMOUNT | CAMPAIGN_DISPLAY_VALUE | TACTIC_NAME |
      | Demo        | User      | automation@pulsepoint | 01- Advertiser | Auto    | Regular | 20000     | Line      | 500         | Percentage          | 5                |                 | + 5 %                  | Tactic      |
