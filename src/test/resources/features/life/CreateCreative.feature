Feature: LIFE Regression - Create a Creative Library for below creative types and verify filters, sort, search options available on the page
  1. Display
  2. Video
  3. Audio
  4. Native Display
  5. Native Video
  6. Search Extension

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And User clicks Creative Library options present under Activation tab
    Then Verify Creative Library page is displayed

  @regression
  Scenario: Verify filters under Active and Archived activity, clear-all button, sort, and search options on the Creative Library page
    And Check Activity buttons "Active" and verify following filters are available and working
      | Advertiser           | 01- Advertiser           |
      | Associated Campaigns | AutoCampaign123367676211 |
      | Approval Status      | Approved                 |
      | Ad Sizes             | 1025x800                 |
      | CreatedBy            | Anand                    |
      | Creative Type        | Image, Video             |
    And Check Activity buttons "Archived" and verify following filters are available and working
      | Advertiser           | Amgen                      |
      | Associated Campaigns | AutoCampaign               |
      | Approval Status      | Approved                   |
      | Ad Sizes             | 1024x576                   |
      | CreatedBy            | Anand                      |
      | Creative Type        | Html, Video                |
    And Verify the following sort options are available and working
      | Name-Asc          |
      | Name-Desc         |
      | ID-Asc            |
      | ID-Desc           |
      | Last Updated-Asc  |
      | Last Updated-Desc |
    And Verify Search Box is available and working
      | 291264           |
      | VideoURLCreative |
      | Manual           |
      | BulkUpload       |
      | 120x600          |
    And Verify Copy option is available and working


  @regression
  Scenario Outline: Create new Creative Library entries for multiple Creative Types
    # Display
    When User creates and saves "Display" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType | CreativeAttributes                                                                                                               |
      | Html         | HTMLCode:<html>Auto_Creative</html>, Size:1024x576, DomainLanding:https://www.pulsepoint.com                                     |
      | Image        | ImageFile:Display_Image.jpg, Size:1024x576, ClickThroughURL:https://www.pulsepoint.com, DomainLanding:https://www.pulsepoint.com |
       #| Html5        | ArchiveFile:Creative_Archive, Size:1024x576, DomainLanding:https://www.pulsepoint.com |

    # Audio
    And User creates and saves "Audio" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType | CreativeAttributes                                                                             |
      #|Upload        | FileName:Creative_Audio.mp3, Durations:60, AdvertiserDomain:pulsepoint.com, IAB:Profane |
      | Audio URL    | URL:https://www.pulsepoint.com, Durations:60, AdvertiserDomain:pulsepoint.com, IAB:Profane     |
      | VAST URL     | VASTURL:https://www.pulsepoint.com, Durations:60, AdvertiserDomain:pulsepoint.com, IAB:Profane |
      | VAST XML     | VASTXML:https://www.pulsepoint.com, Durations:60, AdvertiserDomain:pulsepoint.com, IAB:Profane |

    # Video
    And User creates and saves "Video" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType | CreativeAttributes                                                                                                                                                |
      #|Upload        | FileName:Creative_Audio.mp3, Durations:60, Width:100, Height:100, ClickThroughURL:pulsepoint.com, AdvertiserDomain:https://www.pulsepoint.com, IAB:Profane |
      | Video URL    | URL:https://www.pulsepoint.com, Durations:60, Width:100, Height:100, ClickThroughURL:pulsepoint.com, AdvertiserDomain:https://www.pulsepoint.com, IAB:Profane     |
      | VAST URL     | VASTURL:https://www.pulsepoint.com, Durations:60, Width:100, Height:100, ClickThroughURL:pulsepoint.com, AdvertiserDomain:https://www.pulsepoint.com, IAB:Profane |
      | VAST XML     | VASTXML:https://www.pulsepoint.com, Durations:60, Width:100, Height:100, ClickThroughURL:pulsepoint.com, AdvertiserDomain:https://www.pulsepoint.com, IAB:Profane |

    # Search Extension
    And User creates and saves "Search Extension" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType | CreativeAttributes                                                                                                                                                           |
      | Search       | Size:300x600, Headline:Test Automation Headline, Description:Test Automation Description, DisplayURL:https://www.pulsepoint.com, ClickThroughURL:pulsepoint.com, IAB:Profane |

    # Native Display
    And User creates and saves "Native Display" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType   | CreativeAttributes                                                                                                                                                                                                                                                  |
      | Native Display | ClickThroughURL:pulsepoint.com, DomainLanding:https://www.pulsepoint.com, IAB:Profane, Headline:Test Automation Headline, SponsoredBy:PulsePoint, Description:Test Automation Description, DisplayURL:https://www.pulsepoint.com, ImageFile:NativeDisplay_Image.jpg |

    # Native Video
    And User creates and saves "Native Video" creative using details "<ADVERTISER>" as Advertiser, "<CREATIVE_NAME>" as Creative Name, "<ADVERTISER_DSA>", "<FINANCER>" and below Creative attributes
      | CreativeType | CreativeAttributes                                                                                                                                                                                                                                                                                           |
      #|Upload        | FileName:Creative_Audio.mp3, Durations:60, Width:100, Height:100, ClickThroughURL:pulsepoint.com, AdvertiserDomain:https://www.pulsepoint.com, IAB:Profane |
      | Video URL    | URL:https://www.pulsepoint.com, Durations:60, Width:100, Height:100, ClickThroughURL:pulsepoint.com, AdvertiserDomain:https://www.pulsepoint.com, IAB:Profane, Headline:Test Automation Headline, SponsoredBy:PulsePoint, Description:Test Automation Description, DisplayURL:https://www.pulsepoint.com     |
      | VAST URL     | VASTURL:https://www.pulsepoint.com, Durations:60, Width:100, Height:100, ClickThroughURL:pulsepoint.com, AdvertiserDomain:https://www.pulsepoint.com, IAB:Profane, Headline:Test Automation Headline, SponsoredBy:PulsePoint, Description:Test Automation Description, DisplayURL:https://www.pulsepoint.com |
      | VAST XML     | VASTXML:https://www.pulsepoint.com, Durations:60, Width:100, Height:100, ClickThroughURL:pulsepoint.com, AdvertiserDomain:https://www.pulsepoint.com, IAB:Profane, Headline:Test Automation Headline, SponsoredBy:PulsePoint, Description:Test Automation Description, DisplayURL:https://www.pulsepoint.com |

    Then Verify the newly created creative is displayed in the Creative Library page

    Examples:
      | ADVERTISER     | CREATIVE_NAME | ADVERTISER_DSA | FINANCER      |
      | 01- Advertiser | Creative      | Auto_DSA       | Auto_Financer |