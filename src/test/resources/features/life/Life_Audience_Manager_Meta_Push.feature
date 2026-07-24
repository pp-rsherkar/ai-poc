Feature: LIFE Audience Manager - Meta Push Sends Customer File Custom Audience

  1. In an Audience Manager Meta push, the platform sends the Customer File Custom Audience signal to the connected client Meta account and creates that audience there.
  2. The Meta push completes successfully and stays consistent across consecutive pushes and multiple Meta-connected accounts.
  3. LinkedIn and TikTok pushes continue to work exactly as before, and the cluster service and Meta integration remain fully intact.

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    # Framework Gap: Requires step definitions for opening Audience Manager with Meta integration configured in LifeSteps.java
    And Audience Manager is open with Meta integration configured

  # Source: ET-24706
  @todo
  Scenario: Verify a Meta push sends only the Customer File Custom Audience signal and creates it in the client Meta account
    # Framework Gap: Requires step definitions for initiating an Audience Manager Meta push in LifeSteps.java
    When User initiates a Meta push for advertiser "Meta_Account_A"
    # Framework Gap: Requires step definitions for verifying Meta push completion status in LifeSteps.java
    Then Verify the Meta push completes successfully
    # Framework Gap: Requires step definitions for Meta account Custom Audience verification in LifeSteps.java
    And Verify the Customer File Custom Audience "CFCA_Automation" is created in the client Meta account
    # Framework Gap: Requires step definitions for verifying the suppressed Website Custom Audience pixel signal in LifeSteps.java
    And Verify no new Website Custom Audience pixel "WebsiteCA_Pixel_Automation" is created after the push
    # Framework Gap: Requires step definitions for verifying existing pixel audiences remain unchanged in LifeSteps.java
    And Verify the existing pixel audience "Existing_Pixel_Audience_2024" is not deleted or modified
    # Framework Gap: Requires step definitions for measuring Meta push latency against the baseline in LifeSteps.java
    And Verify the Meta push latency is the same or better than the previous baseline
    # Framework Gap: Requires step definitions for cluster service log verification in LifeSteps.java
    And Verify the cluster service logs contain no missing-pixel-signal errors

  # Source: ET-24706
  @todo
  Scenario Outline: Verify three consecutive Meta pushes each create the Customer File Custom Audience and produce no pixel audiences
    # Framework Gap: Requires step definitions for initiating a numbered Audience Manager Meta push in LifeSteps.java
    When User initiates Meta push number "<PUSH_NUMBER>" for advertiser "Meta_Account_A"
    Then Verify the Meta push completes successfully
    And Verify the Customer File Custom Audience "<CUSTOMER_FILE_AUDIENCE>" is created in the client Meta account
    # Framework Gap: Requires step definitions for verifying no pixel audience is created after a numbered push in LifeSteps.java
    And Verify no new Website Custom Audience pixel "<PIXEL_AUDIENCE>" is created after push number "<PUSH_NUMBER>"

    Examples:
      | PUSH_NUMBER | CUSTOMER_FILE_AUDIENCE | PIXEL_AUDIENCE             |
      | 1           | CFCA_Automation        | WebsiteCA_Pixel_Automation |
      | 2           | CFCA_Automation        | WebsiteCA_Pixel_Automation |
      | 3           | CFCA_Automation        | WebsiteCA_Pixel_Automation |

  # Source: ET-24706
  @todo
  Scenario Outline: Verify Meta push behaviour is consistent across multiple Meta-connected accounts
    # Framework Gap: Requires step definitions for initiating a Meta push against a specific Meta account id in LifeSteps.java
    When User initiates a Meta push for advertiser "<META_ACCOUNT>" with Meta account id "<META_ACCOUNT_ID>"
    Then Verify the Meta push completes successfully
    And Verify the Customer File Custom Audience "<CUSTOMER_FILE_AUDIENCE>" is created in the client Meta account
    And Verify no new Website Custom Audience pixel "WebsiteCA_Pixel_Automation" is created after the push

    Examples:
      | META_ACCOUNT   | META_ACCOUNT_ID | CUSTOMER_FILE_AUDIENCE |
      | Meta_Account_A | act_10001       | CFCA_Automation        |
      | Meta_Account_B | act_10002       | CFCA_Automation        |
      | Meta_Account_C | act_10003       | CFCA_Automation        |

  # Source: ET-24706
  @todo
  Scenario Outline: Verify LinkedIn and TikTok pushes are unaffected by the Meta pixel signal change
    # Framework Gap: Requires step definitions for initiating a channel-specific audience push in LifeSteps.java
    When User initiates a "<CHANNEL>" push for advertiser "Meta_Account_A"
    # Framework Gap: Requires step definitions for verifying a channel-specific push completion status in LifeSteps.java
    Then Verify the "<CHANNEL>" push completes successfully
    # Framework Gap: Requires step definitions for verifying a channel-specific pushed audience in LifeSteps.java
    And Verify the "<CHANNEL>" pushed audience "<PUSHED_AUDIENCE>" is created in the client account

    Examples:
      | CHANNEL  | PUSHED_AUDIENCE |
      | LinkedIn | CFCA_Automation |
      | TikTok   | CFCA_Automation |

  # Source: ET-24706
  @todo
  Scenario: Verify a Meta push still succeeds for an account with no Meta Pixel configured
    # Framework Gap: Requires step definitions for asserting an advertiser has no Meta Pixel configured in LifeSteps.java
    Given The advertiser "Meta_Account_B" has no Meta Pixel configured
    When User initiates a Meta push for advertiser "Meta_Account_B"
    Then Verify the Meta push completes successfully
    And Verify the Customer File Custom Audience "CFCA_Automation" is created in the client Meta account
    And Verify no new Website Custom Audience pixel "WebsiteCA_Pixel_Automation" is created after the push

  # Source: ET-24706
  @todo
  Scenario: Verify a Meta push reports failure correctly when the Customer File Custom Audience signal fails
    # Framework Gap: Requires step definitions for simulating a Customer File Custom Audience signal failure in LifeSteps.java
    Given The Customer File Custom Audience signal is configured to fail for advertiser "Meta_Account_C"
    When User initiates a Meta push for advertiser "Meta_Account_C"
    # Framework Gap: Requires step definitions for verifying a Meta push failure status in LifeSteps.java
    Then Verify the Meta push reports failure correctly
    # Framework Gap: Requires step definitions for asserting a Meta push does not report a phantom success in LifeSteps.java
    And Verify the Meta push does not report a phantom success
    And Verify the cluster service logs contain no missing-pixel-signal errors
