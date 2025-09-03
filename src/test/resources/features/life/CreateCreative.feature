Feature: LIFE Regression - Create a Creative Library and verify filters, sort, search options for all creative types including Bulk Upload
  It covers:
  1. Creating creatives via UI for the following types:
  Display
  Video
  Audio
  Native Display
  Native Video
  Search Extension
  2. Bulk uploading creatives for supported types
  3. Verifying available filters, sort, and search functionality on the Creative Library page

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
      | Advertiser           | Amgen        |
      | Associated Campaigns | AutoCampaign |
      | Approval Status      | Approved     |
      | Ad Sizes             | 1024x576     |
      | CreatedBy            | Anand        |
      | Creative Type        | Html, Video  |
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

  @regression
  Scenario: Verify Bulk Creative Upload panel
    Given User clicks Bulk Upload button on Creative Library page
    Then Verify Bulk Upload panel is displayed with following options - Creative Type and Advertiser Dropdown
    And Verify the availability of below Creative Type options and the Default option is "Display"
      | Display |
      | HTML    |
      | Native  |
      | Video   |
    And Verify the Advertiser dropdown is displaying all Advertisers mapped to the logged in account


  @regression
  Scenario Outline: Verify Bulk Creative Upload with Pending Approval, Approved, and Denied statuses
    Given User clicks Bulk Upload button on Creative Library page
    When User selects the "<CREATIVE_TYPE>" creative type
    When The advertiser "<ADVERTISER>" is selected for "Display" creative the following sections are visible
      | Spreadsheet Template and Images |
      | Upload Creatives                |
      | Third Party Tracking Pixel/Tag  |
    And User enters "<ADVERTISER_DSA>", "<FINANCER>" mandatory fields data for Display creative
    And User uploads a valid file "<FILE_NAME>" for "Display" creative and previews the creative details
    And User selects the Approval status "<STATUS>"
    And Verify an appropriate error message when user attempts to click the Preview or OK button without selecting a creative file
    Then Verify the header message for "<STATUS>" status
    And User uploads a valid file "<FILE_NAME>" for "<CREATIVE_TYPE>" creative and previews the creative details
    And User saves the creative
    And Verify the newly created creative is displayed in the Creative Library page
    Examples:
      | CREATIVE_TYPE | ADVERTISER     | ADVERTISER_DSA | FINANCER      | STATUS   | FILE_NAME                      |
      | Display       | 01- Advertiser | Auto_DSA       | Auto_Financer | Pending  | DisplayBulkUploadTemplate.xlsx |
      | Display       | 01- Advertiser | Auto_DSA       | Auto_Financer | Approved | DisplayBulkUploadTemplate.xlsx |
      | Display       | 01- Advertiser | Auto_DSA       | Auto_Financer | Denied   | DisplayBulkUploadTemplate.xlsx |


  @regression
  Scenario Outline: Validate Bulk Upload Functionality and Field Requirements for Display Creatives
    Given User clicks Bulk Upload button on Creative Library page
    When User selects the "Display" creative type
    And The advertiser "<ADVERTISER>" is selected for "Display" creative the following sections are visible
      | Spreadsheet Template and Images |
      | Upload Creatives                |
      | Third Party Tracking Pixel/Tag  |
    And Verify under the "Spreadsheet Template and Images" section the options "Download Blank Template" and "Upload Images to Get Template With URLs" are available
    And User is able to download a blank template using the "Download Blank Template" option
    And Verify user is able to upload images "<IMAGE_FILENAME>" to get a template with URLs
    And Verify under the "Upload Creatives" section the fields "Campaign Restrict", "Spreadsheet", "Approval Status" are available
    And User is able to select a "<CAMPAIGN_NAME>" from the Campaign Restrict dropdown
    And User is able to browse and select a template "<FILE_NAME>" from the system
    And Verify default value of the Approval Status field is "Pending"
    And User selects the Approval status "<STATUS>"
    And Verify under the "Third Party Tracking Pixel/Tag" section the fields Add Third Party Tracking Pixel and Add DoubleVerify Pixel are available
    And User is able to click a third-party tracking pixel and add details "Test"
    And User is able to add a DoubleVerify pixel
    And User is able to delete third-party tracking pixel entries
    And An error message is displayed when a blank template "<BLANK_TEMPLATE>" is uploaded
    And User enters "<ADVERTISER_DSA>", "<FINANCER>" mandatory fields data for Display creative
    And User uploads a valid file "<FILE_NAME>" for "Display" creative and previews the creative details
    And User saves the creative
    And Verify the newly created creative is displayed in the Creative Library page
    Examples:
      | ADVERTISER     | IMAGE_FILENAME    | CAMPAIGN_NAME | FILE_NAME                      | BLANK_TEMPLATE                      | ADVERTISER_DSA | FINANCER      | STATUS   |
      | 01- Advertiser | Display_Image.jpg | Creative      | DisplayBulkUploadTemplate.xlsx | BlankDisplayBulkUploadTemplate.xlsx | Auto_DSA       | Auto_Financer | Approved |


  @regression
  Scenario Outline: Validate Bulk Upload Functionality and Field Requirements for HTML Creatives
    Given User clicks Bulk Upload button on Creative Library page
    When User selects the "HTML" creative type
    And Verify Advertiser field should be mandatory
    And Verify that the Landing Domain field is mandatory when all other required fields, including "<ADVERTISER>" are filled
    And Verify that an appropriate error message is displayed when invalid data "<INVALID_LANDING_DOMAIN>" is entered for the Landing Domain
    And Verify only valid Landing Domain "<LANDING_DOMAIN>" values should be permitted
    And Verify default value of the File field should be "DCM File"
    And Verify default value of the Approval Status field is "Pending"
    And Verify default value of the AdChoices Icon should be "TopRight"
    And Verify default value of the Notes Column field should be "None"
    And Verify Rich Media checkbox should be present and selectable "Up"
    And Verify that the user is able to browse the computer, upload the following file types, and create creatives using details - "<ADVERTISER>", "<ADVERTISER_DSA>", "<FINANCER>", "<LANDING_DOMAIN>", "<STATUS>", "<CREATIVE_NAME>"
      | DCM File | DCM_HTML_BulkUpload.xlsx |
#      | PulsePoint   |
#      | Sizmek       |
#      | DoubleVerify |
#      | Adform       |
#      | Flashtalking |
    And Verify the newly created creative is displayed in the Creative Library page

    Examples:
      | ADVERTISER     | INVALID_LANDING_DOMAIN | LANDING_DOMAIN | ADVERTISER_DSA | FINANCER      | STATUS   | CREATIVE_NAME |
      | 01- Advertiser | test                   | www.google.com | Auto_DSA       | Auto_Financer | Approved | HTML_Creative |


  @regression
  Scenario Outline: Validate Bulk Upload Functionality and Field Requirements for Native Creatives
    Given User clicks Bulk Upload button on Creative Library page
    When User selects the "Native" creative type
    And The advertiser "<ADVERTISER>" is selected for "Native" creative the following sections are visible
      | Spreadsheet Template and Images |
      | Upload Creatives                |
    And Verify under the "Spreadsheet Template and Images" section the options "Download Blank Template" and "Upload Images to Get Template With URLs" are available
    And User is able to download a blank template using the "Download Blank Template" option
    And Verify user is able to upload images "<IMAGE_FILENAME>" to get a template with URLs
    And Verify under the "Upload Creatives" section the fields "Campaign Restrict", "Spreadsheet", "Approval Status" are available
    And Verify only valid Landing Domain "<LANDING_DOMAIN>" values should be permitted
    And Verify user is able to type in "<IAB>" categories
    And Verify default value of the AdChoices Icon should be "TopRight"
    And User is able to select a "<CAMPAIGN_NAME>" from the Campaign Restrict dropdown
    And Verify default value of the Approval Status field is "Pending"
    And User selects the Approval status "<STATUS>"
    And An error message is displayed when a blank template "<BLANK_TEMPLATE>" is uploaded
    And User enters "<ADVERTISER_DSA>", "<FINANCER>" mandatory fields data for Display creative
    And User uploads a valid file "<FILE_NAME>" for "Native" creative and previews the creative details
    And User saves the creative
    And Verify the newly created creative is displayed in the Creative Library page
    Examples:
      | ADVERTISER     | IMAGE_FILENAME          | CAMPAIGN_NAME | FILE_NAME                     | BLANK_TEMPLATE                     | ADVERTISER_DSA | FINANCER      | LANDING_DOMAIN | IAB     | STATUS   |
      | 01- Advertiser | NativeDisplay_Image.jpg | Creative      | NativeBulkUploadTemplate.xlsx | BlankNativeBulkUploadTemplate.xlsx | Auto_DSA       | Auto_Financer | www.google.com | Profane | Approved |

  @regression @e2e2
  Scenario Outline: Validate Bulk Upload Functionality and Field Requirements for Video Creatives
    Given User clicks Bulk Upload button on Creative Library page
    When User selects the "Video" creative type
    And Verify Advertiser field should be mandatory
    And The advertiser "<ADVERTISER>" is selected for "Video" creative the following sections are visible
      | Upload Creatives |
    And Verify that the Clickthrough URL and Landing Domain fields are validated as mandatory when all other required fields are filled
    And Verify only valid Landing Domain "<LANDING_DOMAIN>" values should be permitted
    And Verify only valid Clickthrough URL "<CLICKTHROUGH_URL>" values should be permitted
    And Verify default value of the File field should be "DCM File"
    And Verify default value of the Approval Status field is "Pending"
    And Verify default value of the AdChoices Icon should be "TopRight"
    And Verify default value of the Notes Column field should be "None"
    And Verify that the user is able to browse the computer, upload the following file types, and create creatives using details - "<ADVERTISER>", "<ADVERTISER_DSA>", "<FINANCER>", "<LANDING_DOMAIN>", "<STATUS>", "<CREATIVE_NAME>"
      | DCM File | DCM_HTML_BulkUpload.xlsx |
    And Verify the newly created creative is displayed in the Creative Library page
    Examples:
      | ADVERTISER     | CLICKTHROUGH_URL   | LANDING_DOMAIN | ADVERTISER_DSA | FINANCER      | STATUS   | CREATIVE_NAME  |
      | 01- Advertiser | AutomationTest.com | www.google.com | Auto_DSA       | Auto_Financer | Approved | Video_Creative |
