Feature: LIFE Regression – Generate IP Address Lists in the following ways:
  1. Manually input ip address to create a list
  2. Upload a file to generate a id address list automatically

  Background:
    Given This scenario will be executed in the "Demo" environment as a "User"
    And "Life" application is logged in successfully with Account "automation@pulsepoint"
    And Verify Campaign Dashboard is displayed with title "Campaigns"
    And User navigates to the "IP Address Lists" page
    And Verify that the search option is present on the "IP Lists" tab
    When User clicks on Create New List
    Then Verify that the Create New List screen is displayed

  @regression
  Scenario Outline: Manage an IP Address List by manually adding, updating, and removing IP addresses
    And Verify that an error message is displayed when no listname "<LIST_NAME>" or "IP Address" names are specified
    And Verify that if multiple "IP Address" are specified on a single line, a validation error is shown
      | 203.45.67.89 |
      | 67.89.123.45 |
    And Verify that if multiple "<IP_ADDRESS_ACROSS_LINES>" are specified across multiple lines, an error is shown
    And Verify that when "IP Address" names are specified manually, the option to upload a file disappears
      | 11.22.33.44                             |
      | 55.66.77.88                             |
      | 99.88.77.66                             |
      | 14.25.36.47                             |
      | 78.90.12.34                             |
      | 156.23.45.67                            |
      | 201.34.56.78                            |
      | 34.56.78.90                             |
      | 89.123.45.6                             |
      | 145.67.89.10                            |
      | 23.45.67.89                             |
      | 76.54.32.10                             |
      | 111.222.33.44                           |
      | 135.246.57.68                           |
      | 98.76.54.32                             |
      | 2001:0DB8:85A3:0000:0000:8A2E:0370:7334 |
      | 2001:0DB8:1234:5678:9ABC:DEF0:1111:2222 |
      | 3FFE:1900:FE21:4545:0000:0000:0000:0001 |
      | 2001:ABCD:1234:5678:90AB:CDEF:1234:5678 |
      | FE80:0000:0000:0000:021C:42FF:FE00:0001 |
      | 2606:4700:4700:0000:0000:0000:0000:1111 |
      | 1234:5678:9ABC:DEF0:1234:5678:9ABC:DEF0 |
      | ABCD:EF01:2345:6789:ABCD:EF01:2345:6789 |
      | 1111:2222:3333:4444:5555:6666:7777:8888 |
      | AAAA:BBBB:CCCC:DDDD:EEEE:FFFF:0000:1111 |
    And User retrieves all the entered data before saving the list details "<LIST_NAME>"
    And Verify that the user is able to create a "IP Address" list by specifying names manually
    And Verify that the counter on the left displays the correct value for each list in the navigation panel
    And User verifies that saved details for list "<LIST_NAME>" match the input data
    And Verify that the user is able to edit an existing "IP Address" list with below details
      | 204.102.153.51                          |
      | 215.107.161.54                          |
      | 226.113.169.57                          |
      | 237.118.177.60                          |
      | 246.123.185.63                          |
      | 25.50.75.100                            |
      | 36.72.108.144                           |
      | 48.96.144.192                           |
      | 59.118.177.236                          |
      | 70.140.210.24                           |
      | 81.162.243.69                           |
      | 92.184.20.112                           |
      | 103.206.51.154                          |
      | 114.228.82.196                          |
      | 125.250.113.225                         |
      | AAAA:BBBB:CCCC:DDDD:EEEE:FFFF:1111:2222 |
      | 1357:2468:369A:48AC:5BDE:6CEF:7D01:8E23 |
      | 2468:1357:ACE0:BDF1:2345:6789:ABCD:EF01 |
      | 1000:2000:3000:4000:5000:6000:7000:8000 |
      | 1234:0000:5678:0000:9ABC:0000:DEF0:1111 |
      | AAAA:0001:BBBB:0002:CCCC:0003:DDDD:0004 |
      | FEED:BEEF:CAFE:BABE:1234:5678:9ABC:DEF0 |
      | DEAD:BEEF:0000:1111:2222:3333:4444:5555 |
      | FACE:B00C:1234:5678:9ABC:DEF0:1111:2222 |
      | C001:D00D:ABCD:1234:5678:9ABC:DEF0:1111 |
      | BEEF:1234:5678:9ABC:DEF0:AAAA:BBBB:CCCC |
      | CAFE:1234:5678:ABCD:EF01:2345:6789:AAAA |
      | D00D:1111:2222:3333:4444:5555:6666:7777 |
    And Verify that the counter on the left displays the correct value for each list in the navigation panel
    And User verifies that saved details for list "<LIST_NAME>" match the input data
    And Verify that the user is able to delete an existing "IP Address" name list
    And Verify the deleted list is no longer displayed in the left panel
    Examples:
      | LIST_NAME  | IP_ADDRESS_ACROSS_LINES                                                |
      | IP_Address | 123.46.7.5, 123.46.7.7 :: 123.46.7.0, 684D:1111:222:3333:4444:5555:6:9 |

  @regression
  Scenario Outline: Manage an IP Address List by uploading a file to create, update, and delete IP addresses
    And Verify that an error message is displayed when no list names is specified and user tries to upload a file "<UPLOAD_FILENAME1>"
    And Verify that when enters "<LIST_NAME>" and upload file "<UPLOAD_FILENAME1>" option is selected, the text area to direct enter the names disappears
    And Verify the Uploaded Files section displays the entries count, includes download and delete icons after the file "<UPLOAD_FILENAME1>" is uploaded
    And Verify that the user is able to create a "IP Address" list through file upload
    And Verify that the counter on the left displays the correct value after file upload "<UPLOAD_FILENAME1>"
    And Verify that the user is able to edit an existing list by uploading same file "<UPLOAD_FILENAME1>" again and verify the changes
    And Verify that the user is able to edit and save an existing "IP Address" list by uploading another file "<UPLOAD_FILENAME2>" and verify the changes
    And Verify that the counter on the left displays the updated value after new file upload "<UPLOAD_FILENAME2>"
    And Verify that user is able to download the uploaded file "<UPLOAD_FILENAME1>", "<UPLOAD_FILENAME2>" and fetches the count of the downloaded files
    And Verify that the count of the downloaded files "<UPLOAD_FILENAME1>", "<UPLOAD_FILENAME2>" matches with the count displayed in the Uploaded Files section and left side panel
    And Verify that the user is able to delete the uploaded file "<UPLOAD_FILENAME1>" and verify the counter on the left displays the updated value after file deletion
    And Verify that the user is able to delete an existing "IP Address" name list
    And Verify the deleted list is no longer displayed in the left panel
    Examples:
      | LIST_NAME            | UPLOAD_FILENAME1   | UPLOAD_FILENAME2   |
      | IPAddress_FileUpload | IPAddressFile1.csv | IPAddressFile2.csv |
