import re
import socket
import requests
import json
import os
import math
import urllib
import ast
import importlib.util
from . import utility_methods
from . import input_parser
from . import filter_tests
from . import test_runner
from . import execution_updater
from .logging_config import setup_logging
from .squash_constants import STATUS_SUCCESS, STATUS_FAILURE, STATUS_SKIPPED, STATUS_BLOCKED, STATUS_RUNNING, STATUS_UNTESTABLE, STATUS_SETTLED, STATUS_CANCELLED, STATUS_READY

from robot.libraries.BuiltIn import BuiltIn
from robot.libraries.OperatingSystem import OperatingSystem
import robot.api.logger     # this is for Robot Framework logging  --- how do I know if SquashGlados is running a robot or pytest test???
from robot.api.deco import keyword, library
import datetime             # 04-12-2025: Added for timestamped logging

__all__ = ['input_parser', 'filter_tests', 'test_runner', 'execution_updater', 'utility_methods']

@library
class SquashGlados:

    def __init__(self, squash_server='prod'):
        """
        Initializes the SquashGlados library.
        :param squash_server: The Squash server to use, either 'prod' or 'stg'.
        """
        self.logger = setup_logging()
        self.squash_server = squash_server
        if squash_server not in ['prod', 'stg']:
            raise ValueError(f"Invalid squash server value: {squash_server}. Must be 'prod' or 'stg'.")
        self.builtin = None
        try:
            self.builtin = BuiltIn()
        except:
            pass

    def get_execution_updater(self, squash_server='prod'):
        """
        Returns the execution updater instance.
        If it does not exist, it will create one with the default squash server.
        """
        if not hasattr(self, 'execution_updater'):
            try:
                self.squash_server = BuiltIn().get_variable_value('${SQUASH_SERVER}', 'prod')
            except:
                pass
            if self.squash_server not in ['prod', 'stg']:
                raise ValueError(f"Invalid squash server value: {self.squash_server}. Must be 'prod' or 'stg'.")
            self.execution_updater = execution_updater.ExecutionUpdater(squash_server=self.squash_server)
        return self.execution_updater

    def run(self, config_file):
        try:
            # PARSE THE INPUT -----
            self.input_parser = input_parser.InputParser(config_file=config_file)
            json_input = self.input_parser.get_json_input()
            pool_size = self.input_parser.get_pool_size()
            merge_output = json_input.get('merge_output', False)
            squash_server = json_input.get('squash_server', 'prod')
            self.squash_server = squash_server
            if squash_server not in ['prod', 'stg']:
                raise ValueError(f"Invalid squash server value: {squash_server}. Must be 'prod' or 'stg'.")
            self.execution_updater = self.get_execution_updater(squash_server=squash_server)
            
            # Get 'disable_post_log' determines if test should skip posting testrail log file if needed (False by default)
            disable_post_log = self.input_parser.get_variable(variable_name='disable_post_log', default_value=False)
            
            # FILTER THE TESTS -----
            self.filter_tests = filter_tests.FilterTests(json_input)
            ### tests_to_run = self.filter_tests.get_tests_to_run()    # returns a list of tests to run
            all_output_paths = []
            fail_count = 0
            if json_input.get('automation', '').startswith('cucumber'):
                ### 04-12-2025 Read cucumber.json file
                results_path = self.input_parser.get_results_path()
                tests = json_input.get('tests', [])
                is_report_only = results_path and all((not t.get('command') for t in tests))
                if is_report_only:
                    self.logger.info(f"[{datetime.datetime.now().strftime('%d-%m-%Y %H:%M:%S')}] Running in report-only mode: parsing cucumber.json and updating Squash TM.")
                    try:
                        cucumber_results = self.input_parser.parse_cucumber_results()
                    except FileNotFoundError as e:
                        self.logger.error(f"{str(e)}")
                        self.logger.error("Aborting report-only mode due to missing cucumber.json.")
                        return
                    for test in tests:
                        scenario_name = test.get('name', '')
                        normalized_name = self.input_parser.normalize_scenario_name(scenario_name)
                        result = cucumber_results.get(normalized_name, None)
                        iteration_ids = json_input.get('iterations', [])
                        iteration_id = iteration_ids[0] if iteration_ids else None
                        execution_id = None
                        if iteration_id:
                            execution_id = self.execution_updater.get_execution_id_for_test(iteration_id, scenario_name)
                        test['execution_id'] = execution_id
                        if result:
                            # Log the result
                            self.execution_updater.update_test_status(test, result)
                        else:
                            # Log as skipped if not present in cucumber.json
                            self.execution_updater.update_test_status(test, 'skipped')
                    self.logger.info(f"[{datetime.datetime.now().strftime('%d-%m-%Y %H:%M:%S')}] Report-only mode completed.")
                    return

            ### there is an optional item in the config.json called "rerun_failed_tests", if it's > 0, then all the failed tests are rerun that number of times
            rerun_failed_tests = int(json_input.get('rerun_failed_tests', 0))
            rerun_statuses = json_input.get('rerun_statuses', None)
            if rerun_failed_tests > 0 and not rerun_statuses:
                rerun_statuses = ["FAILURE"]
            for x in range(rerun_failed_tests+1):
                self.logger.info(f"----- Filtering tests ----------------")
                tests_to_run = self.filter_tests.get_tests_to_run()    # returns a list of tests to run    # note: if this line is inside the for loop, you get a new execution every time it reruns failed tests, if it's before the for loop, it reuses the same execution when it reruns failures
                if x > 0:
                    self.logger.info(f'RERUN FAILURES {(x-1)}')
                    tests_to_run = self.filter_tests.filter_tests_with_status(rerun_statuses, tests_to_run)
                
                # Handling sub-suite tests
                tests_to_run = self.filter_tests.merge_sub_suite_tests(tests_to_run)
                
                if merge_output:
                    for t in tests_to_run:
                        all_output_paths.append(t['output_dir'])
                if len(tests_to_run) == 0:
                    self.logger.info("No tests to run.")
                    break
                self.logger.info(f"----- Commands to run ----------------")
                ### 04-12-2025: Check command existence before logging
                for t in tests_to_run:
                    if 'command' in t:
                        self.logger.info(f"Command to run: {t['command']}")
                    else:
                        self.logger.error(f"No command found for test: {t.get('name', 'Unknown')}")
                # RUN THE COMMANDS -----                
                self.logger.info(f"----- Running tests ----------------") 
                build_url = self.input_parser.get_variable(variable_name='build_url', default_value=None)
                self.test_runner = test_runner.TestRunner(squash_server, tests_to_run, pool_size=pool_size, build_url=build_url)
                self.logger.info(f"Number of tests to run: {len(tests_to_run)} about to run tests")
                fail_count = self.test_runner.run_tests(disable_post_log=disable_post_log)
                self.logger.info(f"----- All tests completed ----------------")
            if merge_output:
                self.logger.info('Going to merge the output files for these log files: ----------')
                self.logger.info(all_output_paths)
                self.logger.info('-------- end of log files ---------------------')
                utilities = utility_methods.UtilityMethods()
                utilities.run_rebot_merge(all_output_paths)
            if fail_count > 0:
                raise AssertionError("One or more failures occurred.")

            ### 04-12-2025: After running tests, update Squash TM test case status from cucumber.json if applicable
            # if json_input.get('automation', '').startswith('cucumber'):
            #     mapping_mode = self.input_parser.get_mapping_mode()
            #     if mapping_mode == 'scenario_name':
            #         self.test_runner.update_test_case_status_from_cucumber(self.input_parser)
        except Exception as e:
            self.logger.error(f"An unexpected error occurred: {e}")
            raise e


    # methods used in automated tests in verticals outside SquashGlados 
    @keyword
    def update_execution(self, execution_id=None, execution_status=None, comment='', images=[], last_step_started=None, squash_server=None, using_robot=True):
        # If no comment was given will use squash comment with/without log location message
        if len(comment) == 0:
            # Check if we want to include log location in the comment output
            include_squash_log_message = BuiltIn().get_variable_value('${INCLUDE_SQUASH_LOG_MESSAGE}', False)
            squash_comment_format = BuiltIn().get_variable_value('${SQUASH_COMMENT_FORMAT}', False)
            if include_squash_log_message:
                comment = self.prepare_squash_comments()
            elif squash_comment_format:
                method_name = f'prepare_squash_comments_format_{squash_comment_format}'
                method_to_call = getattr(self, method_name)
                comment = method_to_call()
            else:
                comment = BuiltIn().get_variable_value('${SQUASH_COMMENTS}', '')
        
        # For future debugging purposes of what the comment is
        print(comment)
        
        # Get the execution ID from the global variable if no execution id was given
        if execution_id is None:
            execution_id = BuiltIn().get_variable_value('${EXECUTION_ID}', execution_id)

        if execution_id:
            if squash_server is None:
                squash_server = BuiltIn().get_variable_value('${SQUASH_SERVER}', None)
            if not squash_server or squash_server not in ['prod', 'stg']:
                raise ValueError(f"Invalid  or missing squash server value. Must be 'prod' or 'stg'.")
            else:
                self.execution_updater = self.get_execution_updater(squash_server=squash_server)
            self.execution_updater.update_execution(execution_id, execution_status, comment, images, last_step_started, using_robot=using_robot)

    @keyword
    def glados_setup(self):
        if not self.builtin:
            self.builtin = BuiltIn()
        # Determine to use SpartanLibrary or SeleniumLibrary
        self.library = self.determine_platform_library()  
        self.builtin.import_library(self.library)
        self.builtin.set_global_variable('${PLATFORMLIBRARY}', self.library)
        
        # Set the test to not take screenshot for every fail/retry keyword
        driver = self.builtin.get_library_instance(self.library)   
        driver.register_keyword_to_run_on_failure("NOTHING")

        self.utc_time_start = self.builtin.get_time('epoch', 'UTC')
        self.builtin.set_global_variable('${UTC_TIME_START}', self.utc_time_start)

        self.jenkins_url = self.builtin.get_variable_value('${JENKINS_URL}', '')
        self.builtin.set_global_variable('${JENKINS_URL}', self.jenkins_url)

        self.workspace_url = self.builtin.get_variable_value('${WORKSPACE_URL}', '')
        self.builtin.set_global_variable('${WORKSPACE_URL}', self.workspace_url)
        
        self.jenkins_build_number = self.builtin.get_variable_value('${JENKINS_BUILD_NUMBER}', '1')
        self.builtin.set_global_variable('${JENKINS_BUILD_NUMBER}', self.jenkins_build_number)
        
        self.use_jenkins_pipeline = self.builtin.get_variable_value('${USE_JENKINS_PIPELINE}', False)
        self.builtin.set_global_variable('${USE_JENKINS_PIPELINE}', self.use_jenkins_pipeline)
        
        self.browser = self.builtin.get_variable_value('${BROWSER}', 'chrome')
        self.builtin.set_global_variable('${BROWSER}', self.browser)
        
        self.device = self.builtin.get_variable_value('${DEVICE}', '')
        self.builtin.set_global_variable('${DEVICE}', self.device)
        
        self.environment = self.builtin.get_variable_value('${ENVIRONMENT}', 'staging')
        self.builtin.set_global_variable('${ENVIRONMENT}', self.environment)
        
        self.remote = self.builtin.get_variable_value('${REMOTE}', '')
        self.builtin.set_global_variable('${REMOTE}', self.remote)

        squash_server = self.builtin.get_variable_value('${SQUASH_SERVER}', 'prod')
        self.builtin.set_global_variable('${SQUASH_SERVER}', squash_server)
        
        # Handling execution id that is part of the sub-suite format, get the execution id via test name in the dict
        execution_id_dict = self.builtin.get_variable_value('${EXECUTION_ID_DICT}', {})
        if len(execution_id_dict) > 0:
            execution_id_dict = ast.literal_eval(execution_id_dict)
            test_name = self.builtin.get_variable_value('${TEST_NAME}', "")
            execution_id = execution_id_dict.get(test_name, '')
            if execution_id != '':
                self.builtin.set_global_variable('${EXECUTION_ID}', execution_id)
        

    @keyword
    def determine_platform_library(self):
        '''
        Determine if we want to use SpartanLibrary or SeleniumLibrary
        '''
        self.device = self.builtin.get_variable_value('${DEVICE}', '')
        self.use_appium = self.builtin.get_variable_value('${USE_APPIUM}', 'true')
        
        if self.device != '' and self.use_appium == 'true':
            library = 'SpartanLibrary'
        else:
            library = 'SeleniumLibrary'
        return library

    @keyword
    def is_a_browser_open(self):
        """
        Returns True if at least one browser window is open, else returns False
        """
        browser_open = False
        try:
            driver = BuiltIn().get_library_instance("SeleniumLibrary")
            if driver.driver:
                browser_open = True
        except:
            pass

        try:
            from AppiumLibrary import AppiumLibrary
            myappium = BuiltIn().get_library_instance("SpartanLibrary")
            if myappium.get_driver_instance():
                browser_open = True
        except:
            pass
            
        return browser_open

    @keyword
    def is_using_seleniumlibrary(self):
        """
            Returns True if the SeleniumLibrary is in use, False otherwise.
            Useful when needing to determine if SeleniumLibrary is in use instead of Appium.
        """
        try:
            self.builtin.get_library_instance('SeleniumLibrary')
        except RuntimeError:
            return False
        return True

    @keyword
    def get_browser_version(self):
        """
        Returns the browser's version number.

        Since this gets the library instance of the SeleniumLibrary, this will fail if that library is not an import.
        This is the case if Appium is being used.

        If no browser is open when the keyword runs, the browser version will be 'Browser was not open, could not get browser version.'
        """
        # Check to make sure SeleniumLibrary is being used
        is_selenium = self.is_using_seleniumlibrary()

        # Return device if present else version (from SpartanLibrary) if not SeleniumLibrary
        if self.device:
            return self.device

        if not is_selenium:
            return self.version

        # Get Browser Version
        selenium_library = self.builtin.get_library_instance('SeleniumLibrary')
        is_browser_open = self.is_a_browser_open()
        if is_browser_open:
            # Check that browser version variable is Selenium 3 variety
            caps = selenium_library.driver.capabilities
            if 'browserVersion' in caps:
                return caps['browserVersion']
            return caps['version']
        else:
            return 'Browser was not open, could not get browser version.'

    # ----- methods for updating the test status -----
    @keyword
    def override_test_result_to_blocked(self, msg="Test is BLOCKED"):
        """
        If used, Squash will update the result of the test case with "BLOCKED" regardless of Robot Framework result. Then fails the test to stop further execution of test steps.
        """
        #if not self.builtin:
        #    self.builtin = BuiltIn()
        self.builtin.set_test_variable('${OVERRIDE_TEST_RESULT}', STATUS_BLOCKED)
        self.builtin.fail(msg)

    @keyword
    def override_test_result_to_retest(self, msg="Test is RETEST", append=False):
        """
        If used, Squash will update the result of the test case with "Retest" regardless of Robot Framework result. 
        """
        #if not self.builtin:
        #    self.builtin = BuiltIn()
        self.builtin.set_test_variable('${OVERRIDE_TEST_RESULT}', STATUS_SETTLED)
        self.builtin.set_test_message(str(msg), append=append) 

    @keyword
    def override_test_result_to_na(self, msg="Test is N/A or skipped"):
        """
        If used, Squash will update the result of the test case with "SKIPPED" regardless of Robot Framework result. Then fails the test to stop further execution of test steps.
        """
        #if not self.builtin:
        #    self.builtin = BuiltIn()
        self.builtin.set_test_variable('${OVERRIDE_TEST_RESULT}', STATUS_SKIPPED)
        self.builtin.fail(msg)

    def determine_time_elapsed(self):
        """ Returns the time elapsed, time started, and time ended.
            Relies on the global variable 'UTC_TIME_START' to exist which should be set in the 'glados setup' keyword.
        """
        utc_time_end = self.builtin.get_time('epoch', 'UTC')

        time_elapsed = int(utc_time_end) - int(self.utc_time_start)
        time_elapsed = 0.001 if time_elapsed <= 0 else time_elapsed
        self.builtin.log("time_elapsed = " + str(time_elapsed))

        time_started = self.builtin.get_time('timestamp', self.utc_time_start).split(' ')[1]
        time_ended = self.builtin.get_time('timestamp', utc_time_end).split(' ')[1]

        return time_elapsed, time_started, time_ended

    def create_jenkins_url_to_log_folder(self, output_dir):
        """
        To be used in the keyword, 'create url to log folder' to handle creation
        of the URL for Jenkins.
        """
        print(output_dir)
        url_template = "{jenkins_url_ph}/job/{build_name_ph}/ws/{subfolder_ph}/logs/{test_run_ph}/{test_folder_ph}"
        url_pipeline_template = "{jenkins_url_ph}/job/{build_name_ph}/{build_number_ph}/execution/node/3/ws/{subfolder_ph}/logs/{test_run_ph}/{test_folder_ph}"
        split_path = output_dir.split(os.sep + 'logs' + os.sep)

        build_name = split_path[0]
        # Possible beginning of output_dir: "/var/www/jenkins/workspace/", "C:/Jenkins/workspace/"
        # remove everything from the beginning of the string to "/jenkins/workspace/" (case insensitive)
        build_name = re.sub(re.compile("^.*?[/\\\\]jenkins[/\\\\]workspace[/\\\\]", re.I), '', build_name)

        subfolder = ""
        if build_name.count(os.sep) > 0:
            build_name, subfolder = build_name.split(os.sep, 1)

        test_run_folder_parts = split_path[1].split(os.sep)
        # Check if we have at least one part for test_run and one for test_folder
        if len(test_run_folder_parts) < 2:
            raise ValueError(
                "The second part of the split path does not contain enough elements for test_run and test_folder.")
        # Assuming the last part is the test folder
        test_folder = test_run_folder_parts[-1]
        test_run = os.sep.join(test_run_folder_parts[:-1])  # Join all but the last part for test_run
        
        if self.workspace_url != '':
            url_template = "{workspace_url_ph}/logs/{test_run_ph}/{test_folder_ph}"
            log_folder_url = url_template.format(workspace_url_ph=self.workspace_url, test_run_ph=test_run,
                                                 test_folder_ph=test_folder)
        elif self.use_jenkins_pipeline:
            log_folder_url = url_pipeline_template.format(jenkins_url_ph=self.jenkins_url, build_name_ph=build_name,
                                                 build_number_ph=self.jenkins_build_number,
                                                 subfolder_ph=subfolder, test_run_ph=test_run,
                                                 test_folder_ph=test_folder)
        else:
            log_folder_url = url_template.format(jenkins_url_ph=self.jenkins_url, build_name_ph=build_name,
                                                 subfolder_ph=subfolder, test_run_ph=test_run,
                                                 test_folder_ph=test_folder)

        # now do some clean up for special characters
        log_folder_url = urllib.parse.quote(log_folder_url, "/:%")
        
        return log_folder_url

    @keyword
    def create_url_to_log_folder(self, always_local=False):
        """
        If 'always_local' is given, will always return the local location of the file instead of jenkins/workspace url.
        
        Returns: A string that can be pasted into the address bar of the browser that will
                 take the user to log folder of the test case/suite.
        """
        output_dir = self.builtin.get_variable_value('${OUTPUT DIR}')
        # If ran locally, this is the string needed
        local_computer = fr'file://{os.path.normpath(output_dir)}'

        # Creating the string when running from Jenkins takes more work
        from_jenkins = None
        if (self.jenkins_url or self.workspace_url) and not always_local:
            from_jenkins = self.create_jenkins_url_to_log_folder(output_dir)

        # Decide which to use
        url = from_jenkins or local_computer
        self.builtin.log("url to log folder = " + str(url))
        return url

    @keyword
    def prepare_squash_comments(self):
        """ Formats the Squash comment output.

            Returns: The markup string of all Squash comments.
        """
        # Determine time elaspedgit s
        time_elapsed, time_started, time_ended = self.determine_time_elapsed()

        # Get Browser version
        is_browser_open = self.is_a_browser_open()
        browser_version = self.get_browser_version() if is_browser_open else ''

        # Determine the name of the computer the browser was ran on
        computer_name = self.builtin.get_variable_value('${NODE_NAME}', "Node name was not set")

        # Format comments
        squash_comments = self.builtin.get_variable_value('${SQUASH_COMMENTS}', 'No comments were set.')
        test_message = self.builtin.get_variable_value('${TEST MESSAGE}', '')
        test_message = test_message if len(test_message) > 0 else 'No error message from Robot Framework.'
        
        test_status = self.builtin.get_variable_value('${TEST STATUS}')
        
        # Dealing with log location that needs url format for squash
        log_url = self.create_url_to_log_folder()
        if self.jenkins_url != '':
            log_url_format = f'<a href="{log_url}/log.html">log.html</a>'
        else:
            log_url_format = f'{log_url}/log.html'
        
        test_header_info = (
            f'<br><b>The log file can be viewed here</b>: {log_url_format}\n\n<br><br>'
            f'<b>ROBOT FRAMEWORK MESSAGE:</b><br>\n{test_message}\n\n<br>'
            f'<br><b>Test Start:</b> {time_started} UTC/GMT\n<br>'
            f'<b>Test End:</b> {time_ended} UTC/GMT\n\n<br>'
            f'<b>Time Elapsed:</b> {time_elapsed}s\n\n<br>'
            f'<b>Browser Info:</b> {self.browser} - {browser_version}\n\n<br>'
            f'<b>Environment:</b> {self.environment}\n\n<br>'
            f'<br>Test was executed on: {computer_name}\n\n<br><br>'
        )
        #test_header_info = f'![](index.php?/attachments/get/114459)\n{test_header_info}' if test_status == 'FAIL' else test_header_info
        test_header_info = (f'<br><b style="color: red; font-size: 18px;">ALERT - There are failures below!</b><br>\n\n'
                            f'{test_header_info}') if test_status == 'FAIL' else test_header_info
        squash_comments = f'{test_header_info}{squash_comments}'
        return squash_comments

    @keyword
    def prepare_squash_comments_format_02(self):
        squash_comments = self.builtin.get_variable_value('${SQUASH_COMMENTS}', 'No comments were set.')
        test_message = self.builtin.get_variable_value('${TEST MESSAGE}', '')
        test_message = test_message if len(test_message) > 0 else 'No error message from Robot Framework.' 
        test_header_info = (
            f'<b>Test Message:</b><br>{test_message}<br>'
            f'<b>Browser:</b> {self.browser} <br>'
            f'<b>Environment:</b> {self.environment}<br>'
        ) 
        squash_comments = f'{test_header_info}{squash_comments}'
        return squash_comments 

    @keyword
    def set_squash_comment(self, comment, clear_previous=False):
        """ Appends to ${comment} to string ${SQUASH_COMMENTS}.
        If ${SQUASH_COMMENTS} does not exist, it is created as a suite level variable with the value of ${comment}.
        """
        squash_comment = self.builtin.get_variable_value('${SQUASH_COMMENTS}', False)

        # Option 1: If no previous comments, create one
        if squash_comment == False:
            self.builtin.set_test_variable('${SQUASH_COMMENTS}', comment)

        # Option 2: Clear comments before adding
        if squash_comment and clear_previous:
            self.builtin.set_test_variable('${SQUASH_COMMENTS}', comment)

        # Option 3: Add comment to existing
        if squash_comment and not clear_previous:
            self.builtin.set_test_variable('${SQUASH_COMMENTS}', f'{squash_comment}<br/>{comment}')
    
    @keyword
    def upload_squash_image_attachments(self, images, execution_id=None, return_dict=False):
        """ 
            Upload the given list of image attachments to squash test
            If 'return_dict' is set to true will return the data as a dictionary with file name as the key and file url as the value. else will be a list of dictionary.
        """
        
        if images is None or len(images) == 0:
            return None
        
        if not execution_id:
            execution_id = self.builtin.get_variable_value('${EXECUTION_ID}', False)
        
        if execution_id:
            self.execution_updater = self.get_execution_updater()
            image_upload_data = self.execution_updater.update_image_attachments(execution_id, images, return_dict)
            return image_upload_data

    # -----------------------------------------
    # Screenshot Functions
    #   Capture Page Screenshot And Add To Squash Image Attachments
    @keyword
    def capture_page_screenshot_and_add_to_squash_image_attachments(self, image_name=None):
        if not image_name:
            image_name = self.builtin.run_keyword('Capture Page Screenshot')
        else:
            self.builtin.run_keyword('Capture Page Screenshot', image_name)
        execution_id = self.builtin.get_variable_value('${EXECUTION_ID}', False)
        if not execution_id:
            return
        output_dir = self.builtin.get_variable_value('${OUTPUT DIR}')
        self.builtin.log(f'Screenshot filename is: {output_dir}{os.sep}{image_name}')
        img_info = self.upload_squash_image_attachments([f'{output_dir}{os.sep}{image_name}'], return_dict=True)
        self.builtin.log(f'The screenshot was uploaded to the Squash Execution')
        self.builtin.log(img_info)
        self.builtin.log('Going to add the screenshot url in the comment')
        if img_info and type(img_info) == dict:
            comment = "<table>"
            for k,v in img_info.items():
                comment += f'<tr><td>{k}</td><td><img src="{v}" alt="{v}" height="128" width="128" /></td></tr>'
            comment += "</table>"
            self.builtin.log(f'The comment is:')
            self.builtin.log(comment)
            self.set_squash_comment(comment, clear_previous=False)
        self.builtin.log('finished with capture_page_screenshot_and_add_to_squash_image_attachments')


    def capture_page_screenshot_in_sections(self, image_name):
        """
        Since the Chrome and IE browsers only takes screenshots of the viewable portion in a browser window,
        multiple screenshots may be necessary.  
        Depending on the browser height and height of the page content, determines how many screenshots are
        needed to capture the entire height of the pages and takes the screenshots.  
        NOTE: If the browser window is not wide enough to capture all the page content, this keyword will not capture the width that is cut off.  
        Returns a list of the screenshot names
        """
        # Scroll to the top
        self.builtin.run_keyword('Execute Javascript', 'window.scrollTo(0, 0);')
        # Determine length of page content
        pixel_length = self.builtin.run_keyword('Execute Javascript', 'var length = document.body.scrollHeight; return length;')
        #print(f'original pixel_length = {pixel_length}')
        # Determine how much the browser window can display at once
        viewport_height = self.builtin.run_keyword('Execute Javascript', 'var viewport_len = window.innerHeight; return viewport_len;')
        #print(f'original viewport_height = {viewport_height}')
        # Determine number of screenshot needed
        screenshots = math.ceil(float(pixel_length) / float(viewport_height))
        #print(f'original screenshots = {screenshots}')

        # Resize the browser window to Try to avoid overlapping images at the bottom of the page
        height_2 = math.ceil(pixel_length / screenshots)
        #print(f'height_2 = {height_2}')
        resizing_window = False
        if abs(height_2 - viewport_height) > 1: 
            resizing_window = True
            # TODO: resize browser and recalculate pixel_length and viewport_height...
            x,y = self.builtin.run_keyword('Get Window Size')
            #print(f'original window size: {x} X {y}')
            height_3 = height_2 + (y - viewport_height)    # height of viewport plus space between the top of the window and the viewport
            #print(f'height_3 = {height_3}')
            self.builtin.run_keyword('Set Window Size', x, height_3)
            self.builtin.run_keyword('Execute Javascript', 'window.scrollTo(0, 0);')
            pixel_length = self.builtin.run_keyword('Execute Javascript', 'var length = document.body.scrollHeight; return length;')
            viewport_height = self.builtin.run_keyword('Execute Javascript', 'var viewport_len = window.innerHeight; return viewport_len;')
            screenshots = math.ceil(float(pixel_length) / float(viewport_height))
            #print(f'new pixel_length = {pixel_length}')
            #print(f'new viewport_height = {viewport_height}')
            #print(f'new screenshots = {screenshots}')

        y_axis = 0
        output_dir = self.builtin.get_variable_value('${OUTPUT DIR}')
        names = []
        for i in range(screenshots):
            self.builtin.run_keyword('Execute Javascript', f'window.scrollTo(0, {y_axis});')
            self.builtin.sleep('0.5')
            self.builtin.run_keyword('Capture Page Screenshot', f'{image_name}_{i}.png')
            y_axis += viewport_height
            names.append(f'{output_dir}{os.sep}{image_name}_{i}.png')

        # resize the window to the original size
        if resizing_window:
            self.builtin.run_keyword('Set Window Size', x, y)
        x,y = self.builtin.run_keyword('Get Window Size')
        #print(f'final window size: {x} X {y}')

        return names

    @keyword
    def capture_page_screenshot_and_return_url(self, image_name, set_as_comment=True, width="400"):
        """
        Takes a screenshot and returns a list of URLs where the file is hosted.  
        Multiple urls can be given if multiple screenshots had to be taken to capture the whole pages,
        as is the case when using the Chrome browser.  
        An image name needs to be given as an argument. Do not include the extension.  
        By default it will automatically set the TestRail comment using the correct syntax so the image will appear inline on TestRail.
        """ 
        # Take screenshots depending on browser 
        # Create a list for single screenshot browsers
        single_screenshot_browsers = ['internet explorer', 'ie'] 
        # Determine if browser is single screenshot or multi screenshot
        lowercase_browser_name = self.browser.lower()
        only_need_one_screenshot = lowercase_browser_name in single_screenshot_browsers
        if only_need_one_screenshot:
            self.builtin.run_keyword('Captrue Page Screenshot', f'{image_name}.png')
            output_dir = self.builtin.get_variable_value('${OUTPUT DIR}')
            screenshots = [f'{output_dir}{os.sep}{image_name}.png']
        else:
            screenshots = self.capture_page_screenshot_in_sections(f'{image_name}.png')
        img_info = self.upload_squash_image_attachments(screenshots, return_dict=True)
        if img_info and type(img_info) == dict:
            comment = "<table>"
            for k,v in img_info.items():
                comment += f'<tr><td>{k}</td><td><img src="{v}" alt="{v}" width="{width}" /></td></tr>'
            comment += "</table>"
            self.set_squash_comment(comment, clear_previous=False)

        return screenshots

    @keyword
    def get_data_for_execution_case(self, execution_id):
        data = None
        utilities = utility_methods.UtilityMethods()
        data = utilities.get_execution_case_custom_data(execution_id)
        return data

    def get_name_of_execution_case(self, execution_id):
        case_name = None
        utilities = utility_methods.UtilityMethods()
        case_name = utilities.get_execution_case_name(execution_id)
        return case_name

    # -----------------------------------------
    
    def get_mobile_node_name(self, hub_url, session_id):
        """
            Returns the mobile node name that is running the opened browser.
            Returns the IP if the name cannot be resolved.
        """
        # Selenium Grid
        test_session_url = f'{hub_url}/grid/api/testsession?session={session_id}'
        try:
            # Query Hub using Session ID to determine IP
            response = requests.get(test_session_url).json()
            port_ext = ':4750'
            ip = response['proxyId'].replace(port_ext, '').replace('http://', '')
        except:
            return "Selenium Mobile Grid - Node Name was not set"

        # Get name of IP Address
        try:
            node = socket.gethostbyaddr(ip)[0]
        except socket.gaierror:
            node = ip

        return node

    def get_selenium4_node_name(self, hub_url, session_id):
        """
            Returns the Selenium4 node name that is running the opened browser.
            Returns the IP if the name cannot be resolved.
        """
        # Selenium Grid GraphQL
        if hub_url[-1] == "/":
            hub_url = hub_url[0:-1]
        test_session_url = f'{hub_url}/graphql'
        try:
            # Query Hub using Session ID to determine Node Name
            queryTemplate = """{session (id: "<session-id>") { id, uri, nodeUri, slot { stereotype } } }"""

            query = queryTemplate.replace("<session-id>", session_id)

            response = requests.post(test_session_url, json={"query": query})

            if response.status_code != 200:
                return "Selenium 4 Grid - Node Name was not set (No Response)"
            else:
                # The node info is in the stereotype 'nodeId:custom-cap'
                node_stereotype_info = response.json()['data']['session']['slot']['stereotype']
                node_stereotype_json = json.loads(node_stereotype_info)  # Need to transform the stereotype data from string to dictionary for easier access
                node = "Node Name not found in stereotype"
                if 'ib:name' in node_stereotype_json:
                    node = node_stereotype_json['ib:name']  # general QAA and Automotive Selenium 4 grid
                return node
        except:
            return "Selenium 4 Grid - Node Name was not set (exception)"

    def get_desktop_node_name(self, hub_url, session_id):
        """
            Returns the desktop node name that is running the opened browser.
            Or return the message with node name was not set if the name cannot be obtained.
        """
        # Set default ip message
        ip = "Grid - Node Name was not set"

        # Try handling Selenoid Grid
        try:
            selenoid_grid_test_session_url = f'{hub_url}/host/{session_id}'
            response = requests.get(selenoid_grid_test_session_url).json()
            node = response['Name']
            return node
        except:
            pass

        # Try handling Selenium Grid
        try:
            selenium_grid_test_session_url = f'{hub_url}/grid/api/testsession?session={session_id}'
            # Query Hub using Session ID to determine IP
            response = requests.get(selenium_grid_test_session_url).json()
            port_ext = ':4750' if self.library else ':5555'
            ip = response['proxyId'].replace(port_ext, '').replace('http://', '')

            # Get name of IP Address
            try:
                node = socket.gethostbyaddr(ip)[0]
            except socket.gaierror:
                node = ip

            return node
        except:
            pass

        if ip:
            return ip

        # Return node name not set if can't get the node name
        return "Grid - Node Name was not set"

    @keyword
    def get_node_name(self):
        """
            Returns the name of the node that is running the opened browser.
            Returns the IP if the name cannot be resolved.
            Returns a message if the test was ran locally.
        """
        # Determine Session ID
        hostname = socket.gethostname()
        driver_library = self.builtin.get_library_instance(self.library)
        session_id = driver_library._current_application().session_id if self.library == 'SpartanLibrary' else driver_library.driver.session_id

        # Extract Hub URL from remote variable
        remote = self.builtin.get_variable_value('${REMOTE}', '')
        if not remote:
            return hostname

        hub_url = remote
        if '4444' in remote:
            hub_url = remote.split('4444')[0] + '4444'
        elif re.search('http\\:\\/\\/.*\\/', remote):
            hub_url = re.findall('http\\:\\/\\/.*\\/', remote)[0]

        device = self.builtin.get_variable_value('${DEVICE}', '')
        useSelenium4 = self.builtin.get_variable_value('${USE_SELENIUM4}', '')
        if "wd/hub" not in remote:
            useSelenium4 = 'true'

        if str(useSelenium4).lower() == 'true':
            node_name = self.get_selenium4_node_name(hub_url, session_id)
        elif device != '':
            node_name = self.get_mobile_node_name(hub_url, session_id)
        else:
            node_name = self.get_desktop_node_name(hub_url, session_id)

        return node_name
