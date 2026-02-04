# SquashGlados



## Introduction

SquashGlados is similar to the old Glados, but designed to work with Squash (instead of Testrail).
There are a lot of changes from the old Glados, but the general idea is the same. 
The purpose of SquashGlados is to run the automated tests in Squash iteration(s), create Squash executions for each test that is run and update the executions with the test status, comments, and attachments.
SquashGlados runs tests in parallel with the pool size specified.


## Installation

You can check out this git repo and install with pip like this:

```
pip install .
```

Or you can install from git like this:

```
python -m pip install git+https://git.internetbrands.com/QAAutomation/squashglados
```

This is on the QAA pip server. You can find the latest version [here](http://stg-qa-selenium1.internetbrands.com:8080/simple/ib-squashglados/). 
You can pip install by using a requirements.txt file that includes this: 

```
--extra-index-url http://stg-qa-selenium1.internetbrands.com:8080/ --trusted-host stg-qa-selenium1.internetbrands.com
ib-squashglados==0.8.7
```
and running `pip install -r requirements.txt`

You can also pip install on the command line like this:
```
pip install ib-squashglados==0.8.7 --extra-index-url http://stg-qa-selenium1.internetbrands.com:8080/ --trusted-host stg-qa-selenium1.internetbrands.com 
```


## Changes to your automation code

Import SquashGlados like this:

```
Library           SquashGlados.execution_updater.ExecutionUpdater
```

Update your teardown to include "update execution" like this:

```
update execution    ${execution_id}
```

Note, when SquashGlados creates the commands to run, it includes the "execution_id" as one of the command line variables.

The update_execution() method has optional arguments for comment, images, and last_step_started. 
The comment can include rich text (HTML), with some limitations (must be properly formatted, a lot of css styles are not supported). 
There is some functionality for uploading attachments to an execution, but needs some testing.
If your test fails and last_step_started is a positive integer, then steps before last_step_started have status SUCCESS, last_step_started will have status FAILURE, and the steps after last_step_started are READY.
For example, if last_step_started = 3 and the test has 5 steps, then steps 1 and 2 are SUCCESS, step 3 is FAILURE, steps 3 and 4 are READY.

Remove lines that use the old Glados like:

```
Library    Glados
```


## How To Run SquashGlados

First you need to create an iteration in Squash containing 1 or more automated tests you want to run.
Next you need to create a config.json file, which will contain all the input variables for SquashGlados, including the iteration id.
There is a tool to help you create the config.json file [here](http://stg-qa-selenium1.internetbrands.com/squash_glados_tools/glados_config_file_creator.html).
You can run SquashGlados on your local computer by running this command:

```
python -m SquashGlados config.json
```

Note: the config.json file can be named differently, just make sure to replace "config.json" in the SquashGlados command with the name of your config file.

If you want to run SquashGlados on Jenkins, first create a Jenkins pipeline job and use the pipeline script in tools/pipeline_jenkins_squash_glados.txt.
Click "Build" to run the job once. The first time you run a Jenkins pipeline script, it will set up the parameters in the pipeline script. 
After the first run, if you refresh the page, you now have the option to "Build with Parameters".  Click "Build with Parameters".
Fill out the form with your iteration id, and other variables and click "Build".
You can click the running build number and click "Console Output" to see the console output.
You can go back to the Squash web page and refresh the page with your iteration's execution plan to see the statuses update.
When a test starts running, it's status will go from READY to RUNNING. 
If your automated tests are importing SquashGlados and include "update execution" in the teardown, then the Squash execution will be updated with the status, comments, and possibly attachments.


## Config file variables

Every Config file should have either a list of iteration ids or a list of test-suite ids
 - "iterations" or "test_suites": list of strings of ids
 - "automation": "robot" or "pytest", whichever you use to run your automated tests
 - "squash_server": specifies if you want to use stg-squash or prod squash, aoptions are "prod" or "stg"
 - "environment": the environment you want your tests to use and added to robot or pytest commands
 - "browser": the web browser you want to use, if your tests use a web browser
 - "device": for mobile, which device to use ("iphone", "android"), or for desktop, either use empty string or leave it out of the config file
 - "platform": for mobile, which operating system ("IOS", "Windows")
 - "categories": list of categories to filter, or empty list, if not empty, it only runs tests where the categories matches of of the test cases categories 
 - "notCategories": list of categories to filter, or empty list, it only runs tests where the categories are NOT in any of of the test cases categories 
 - "seleniumGridUrl": string url of the remote grid to use, if running Selenium or mobile tests
 - "additionalArgs": string of arguments you want to add to the end of the command line, for robot: "-v a:b -v c:d" or pytest "--a b --c d" (replace a,c with variable names, and b,d with variable values)
 - "developer_mode": default: false, set to true if you want to run tests that have Automation Test Name and Automation Suite Name, but the Automation Type is not Automated
 - "automation_only": default: false, set to true if you only want tests if the last time the test in this test plan ran, it was run by the API User and the status was not SUCCESS
 - "test_status_filter": list can contain zero or more strings that are valid Squash execution statuses, where you only want to run tests if the last execution in this test plan for this case had a status matching one in the test_status_filter list
 - "pool_size": string with integer specifying how many commands (robot, pytest) do you want to run at the same time
 - "merge_output": default: false, set to true if you want to merge all the Robot Framework logs into one big log file
 - "tests_directory": string name of the folder (relative to the current directory) where the tests are located
 - "cwd": if using pytest, use this instead of tests_directory to specify the pytest commands should be run from that as the current working directory
 - "use_skipped_execution_status": default: false, set to true if you want it to create a SKIPPED execution for any test cases that are not run because of any filter reasons (mismatched environment, status, category, etc)
 - "rerun_failed_tests": default: "0", change to the number of times you want to rerun the tests that fail, or ignore this if your Jenkins pipeline has multiple stages for rerunning failures
 - "rerun_statuses": list of Squash execution statuses you consider failure, default is ["FAILURE"]
 - "build_url": if running on Jenkins, the URL to the workspace, used to create path to log files in execution result comments
 - "pytest_html_report": default: false, set to true if using pytest and you want to use pytest-html report log files
 - "pytest_robot_report": default: false, set to true if using pytest and you want to use Robot Framework style log files
 - "pytest_use_output_dir_for_html_report": default: false, set to true if using pytest-html and you want to use the same output folder for the pytest-html report as other output files

## Project status

This project is in development.
