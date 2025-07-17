Feature: End to End Workflow of Creative Creation and Assignment To Tactic
  It covers below points -
  1. Create a Creative for below types
  Display
  Audio
  Video
  Native Display
  Native Video
  Search Extension
  2. Assign the created Creative to a Tactic


  @e2e
  Scenario Outline: End to End Workflow of Creative Creation with all the creative types and Assignment To Tactic
    # 1
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks Creative Library options present under Activation tab
    Then Verify Creative Library page is displayed
    When User creates and saves "Display" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType | CreativeAttributes                                                                           |
      | Html         | HTMLCode:<html>Auto_Creative</html>, Size:1024x576, DomainLanding:https://www.pulsepoint.com |
    And User creates and saves "Audio" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType | CreativeAttributes                                                                         |
      | Audio URL    | URL:https://www.pulsepoint.com, Durations:60, AdvertiserDomain:pulsepoint.com, IAB:Profane |
    And User creates and saves "Video" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType | CreativeAttributes                                                                                                                                            |
      | Video URL    | URL:https://www.pulsepoint.com, Durations:60, Width:100, Height:100, ClickThroughURL:pulsepoint.com, AdvertiserDomain:https://www.pulsepoint.com, IAB:Profane |
    And User creates and saves "Search Extension" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType | CreativeAttributes                                                                                                                                                           |
      | Search       | Size:300x600, Headline:Test Automation Headline, Description:Test Automation Description, DisplayURL:https://www.pulsepoint.com, ClickThroughURL:pulsepoint.com, IAB:Profane |
    # Native Display
    And User creates and saves "Native Display" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType   | CreativeAttributes                                                                                                                                                                                                                                                  |
      | Native Display | ClickThroughURL:pulsepoint.com, DomainLanding:https://www.pulsepoint.com, IAB:Profane, Headline:Test Automation Headline, SponsoredBy:PulsePoint, Description:Test Automation Description, DisplayURL:https://www.pulsepoint.com, ImageFile:NativeDisplay_Image.jpg |
    And User creates and saves "Native Video" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType | CreativeAttributes                                                                                                                                                                                                                                                                                           |
      | VAST URL     | VASTURL:https://www.pulsepoint.com, Durations:60, Width:100, Height:100, ClickThroughURL:pulsepoint.com, AdvertiserDomain:https://www.pulsepoint.com, IAB:Profane, Headline:Test Automation Headline, SponsoredBy:PulsePoint, Description:Test Automation Description, DisplayURL:https://www.pulsepoint.com |
    #2
    Then Verify the newly created creative is displayed in the Creative Library page
    #3
    And Create and verify a tactic with "<LINE_ITEMS>" line items and other details "<ADVERTISER>" "<CP_NAME>" "<CP_TYPE>" "<CP_BUDGET>" "<LINE_NAME>" "<LINE_BUDGET>" "<TACTIC_NAME>" and assign the created creatives to it


    Examples:
      | ADVERTISER     | CREATIVE_NAME | ADVERTISER_DSA | FINANCER      | CP_NAME          | CP_TYPE | CP_BUDGET | LINE_NAME    | LINE_BUDGET | TACTIC_NAME    | LINE_ITEMS                                                            |
      | 01- Advertiser | Creative      | Auto_DSA       | Auto_Financer | CreativeCampaign | Regular | 20000     | CreativeLine | 500         | CreativeTactic | Display, Audio, Video, Native Display, Native Video, Search Extension |