import importlib.util
from datetime import datetime
from .squash_api_glados import SquashAPI 
from .squash_constants import STATUS_SKIPPED
from .execution_updater import ExecutionUpdater
import os, logging
import re

""" Filter tests to run """

class FilterTests():
    def __init__(self, input_data: dict):
        self.logger = logging.getLogger('SquashGlados.FilterTests')
        self.runConfig = input_data
        squash_server = self.runConfig.get('squash_server', 'prod')
        self.squash_server = squash_server 
        self.squash_api = SquashAPI(squash_server)
        self._username = 'apiuser'
        self.execution_updater = ExecutionUpdater(squash_server)
        self.not_automated_comment = "Test is not automated. Automated tests should have Automation_Type set to 'Automated' and Automation_Test_Name and Automation_Suite_Name set."
        self.staging_only_comment = "This is a staging test only, but the environment is not staging."
        self.release_only_comment = "This is a release test only, but the environment is not prod or preprod."
        self.mobile_only_comment = "This is a mobile test only, but the device is not mobile."
        self.desktop_only_comment = "This is a desktop test only, but the device is not desktop."
        self.filtered_status_comment = "The test is skipped because the last status is not in test_status_filter"

    # --- get all tests in the campaign tree ---------------------------------
    def get_all_tests_in_campaign_tree(self) -> list:
        """ Walk through the campaign tree and get all the test cases in test plans nested in the campaigns 
            Returns a list of test plan items that are in the campaign test plan and iterations in the campaign """
        self.logger.info("Getting all tests in campaign tree")
        folders = self.runConfig.get('campaign_folders', [])    # list of ids
        campaigns = self.runConfig.get('campaigns', [])
        iterations = self.runConfig.get('iterations', [])
        test_suites = self.runConfig.get('test_suites', [])
        campaign_tests = []
        complete_test_plan = []
        # get campaigns from folders
        if folders:
            for folder_id in folders:
                campaigns.extend(self.squash_api.get_all_campaigns_in_folder(folder_id))
        # for each campaign, get the test cases in the campaign test plan
        if campaigns:
            for campaign_id in campaigns:
                # verify campaign is either an integer or a string that can be converted to an integer
                try:
                    campaign_id = int(campaign_id)
                except ValueError:
                    # self.logger.error("Campaign id must be an integer or a string that can be converted to an integer.")
                    continue
                self.logger.debug("Getting tests in campaign: {}".format(campaign_id))
                tests_in_campaign = self.get_tests_in_campaign(campaign_id)
                self.logger.debug("Tests in campaign: {}".format(len(tests_in_campaign)))
                campaign_tests.extend(tests_in_campaign)
                # get the iterations in the campaign
                iterations_in_campaign = self.squash_api.get_all_iterations_in_campaign(campaign_id)
                self.logger.debug("Iterations in campaign: {}".format(iterations_in_campaign))
                iterations.extend(iterations_in_campaign)
                # if there are tests in campaign_tests that are not in the complete_test_plan, then create a new iteration in the campaign and add the tests to the iteration
                for iteration_id in iterations_in_campaign:
                    self.logger.debug("Getting tests in iteration: {}".format(iteration_id))
                    tests_in_iteration = self.get_tests_in_iteration(iteration_id)
                    for test in tests_in_iteration:
                        test_case_id = test.get('referenced_test_case_id', None)
                        if test_case_id in campaign_tests:
                            campaign_tests.remove(test_case_id)
                    self.logger.debug("Tests in iteration: {}".format(len(tests_in_iteration)))
                if campaign_tests:
                    # create a new iteration in the campaign and add the tests to the iteration
                    self.logger.debug("Creating new iteration in campaign: {}".format(campaign_id))
                    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                    iteration_name = f'New Iteration {timestamp}'
                    new_iteration = self.squash_api.create_squash_iteration(campaign_id, iteration_name)
                    self.logger.debug("New iteration created: {}".format(new_iteration))
                    iteration_id = new_iteration.get('id', None)
                    self.logger.debug("New iteration id: {}".format(iteration_id))
                    for test_case_id in campaign_tests:
                        self.squash_api.add_test_case_to_iteration(iteration_id, test_case_id)
                    iterations.append(iteration_id)
        self.logger.info("The iteration ids: {}".format(iterations))
        # for each iteration, get the test cases in the iteration
        if iterations:
            for iteration_id in iterations:
                # if iteration is not an integer or a string that can be converted to an integer, skip it
                try:
                    iteration_id = int(iteration_id)
                except ValueError:
                    # self.logger.error("Iteration id must be an integer or a string that can be converted to an integer.")
                    continue
                complete_test_plan.extend( self.get_tests_in_iteration(iteration_id) )
        if test_suites:
            for test_suite_id in test_suites:
                complete_test_plan.extend(self.squash_api.get_test_suite_plan(test_suite_id))
        self.logger.info("All tests in campaign tree retrieved. Total tests: {}".format(len(complete_test_plan)))
        return complete_test_plan

    def get_tests_in_campaign(self, campaign_id: int) -> list:
        """ This returns a list of test case ids that are in the campaign test plan (does not include children iterations) """
        tests_in_campaign = self.squash_api.get_test_cases_in_campaign_test_plan(campaign_id)
        return tests_in_campaign

    def get_tests_in_iteration(self, iteration_id: int) -> list:
        """ This returns a list of test plan items that are in the iteration test plan, each item includes the test case id, test case id, iteration id, execution status, and executions """
        iteration_details = self.squash_api.get_test_cases_in_iteration(iteration_id)
        # get_test_cases_in_iteration returns a list of test items with a test_plan_item_id, referenced_test_case_id, execution_status, executions, iteration_id, and automation_test_name, automation_suite_name, etc.
        return iteration_details

    # --- filter tests based on environment, device, etc. ---------------------
    def filter_tests(self, complete_test_plan: list) -> list:
        """ filter tests based on environment, device, etc. 
            Returns a list of test plan items that are filtered based on the run configuration """
        self.logger.info("Filtering tests based on environment, device, etc.")
        updated_test_plan = []
        automation_only = self.runConfig.get('automation_only', False)
        use_skipped_execution_status = self.runConfig.get('use_skipped_execution_status', None)
        useSkippedFiltered = self.runConfig.get('useSkippedFiltered', None)
        if useSkippedFiltered:
            use_skipped_execution_status = useSkippedFiltered 
        if automation_only:
            complete_test_plan = self.filter_only_tests_with_last_execution_status_not_success_by_apiuser(complete_test_plan)    ### (TODO... )
        for test in complete_test_plan:
            self.logger.debug("\n-----\nFiltering test: {}\n-----".format(test))
            execution_info = {}
            execution_status = test.get('execution_status', None)
            test_plan_item_id = test['test_plan_item_id']
            test_case_id = test['referenced_test_case_id']
            self.logger.debug("Filtering test: {}".format(test_case_id))
            # get the test case details
            test_case = self.squash_api.get_test_case(test_case_id)
            self.logger.debug("\n==============\nTest case details: {}\n=========================\n".format(test_case))
            categories = test_case.get('Test_Category', [])
            self.logger.debug("Categories: {}".format(categories))
            # filter tests with no steps, Squash won't let you create an execution for a case with no steps, so don't try
            if test_case['number_of_steps'] == 0:
                self.logger.warning(f"Test \"{test_case['name']}\" has no steps, skipping test")
                continue
            # filter tests with missing automation
            is_automated = self.verify_automation(test_case)
            self.logger.debug("Test is automated: {}".format(is_automated)) 
            if not is_automated:
                self.logger.debug("Test is not automated. Skipping test.")
                if use_skipped_execution_status and execution_status != 'SKIPPED':
                    execution_info = self.create_skipped_execution_for_iteration_test_plan_item(test_plan_item_id, self.not_automated_comment)
                    self.logger.debug(f'Execution info for skipped test id {test_case_id}: {execution_info}')
                    # test["execution_id"] = execution_info.get('id', None)
                    # updated_test_plan.append(test)
                continue
            
            environment_or_device_mismatch_comment = self.filter_environment_device(categories)
            if environment_or_device_mismatch_comment:
                self.logger.debug("environment or device mismatch comment: {}".format(environment_or_device_mismatch_comment))
                if use_skipped_execution_status and execution_status != 'SKIPPED':
                    execution_info = self.create_skipped_execution_for_iteration_test_plan_item(test_plan_item_id, environment_or_device_mismatch_comment)
                    # test["execution_id"] = execution_info.get('id', None)
                    # updated_test_plan.append(test)
                continue
                ### TODO: check if a test already has an execution with status SKIPPED, then don't create a new one... TODO !!!
            category_mismatch_status = self.filter_test_with_categories(categories)
            if not category_mismatch_status:
                continue
            category_mismatch_status = self.filter_test_with_notCategories(categories)
            if not category_mismatch_status:
                continue

            last_status = test['execution_status']
            filter_statuses = self.runConfig.get('test_status_filter', None)
            if isinstance(filter_statuses, list) and filter_statuses is not None and len(filter_statuses) == 1 and filter_statuses[0] in ["all",""]:
                filter_statuses = []
            if filter_statuses and last_status not in filter_statuses:
                self.logger.error(f'skip {test_case_id} because filter_statuses = {filter_statuses} and last_status = {last_status}')
                continue
            
            # (if not filtered) create a new execution for the test    ### I want to include execution_id
            # what if the iteration tet plan item already has an execution, and it has status READY?
            execution_info = None
            executions_for_test = self.squash_api.get_exeutions_for_iteration_test_plan_item(test_plan_item_id)
            if executions_for_test:
                for execution in executions_for_test:
                    if execution['execution_status'] == 'READY':
                        execution_info = execution
            if not execution_info:
                execution_info = self.squash_api.create_squash_execution_for_iteration_test_plan_item(test_plan_item_id)
            self.logger.info(f'Execution created with id: {execution_info["id"]} for test {execution_info['name']}')
            # I want to update this item in complete_test_plan with the execution_info
            if execution_info and '_type' in execution_info and execution_info['_type'] == 'execution':
                test["execution_id"] = execution_info.get('id', None)
                updated_test_plan.append(test)
        # return the list of test cases to run
        self.logger.info("Tests filtered. Total tests to run: {}".format(len(updated_test_plan)))
        return updated_test_plan

    def create_skipped_execution_for_iteration_test_plan_item(self, test_plan_item_id: int, comment: str='Test skipped'):
        """ Create a skipped execution for a test plan item in an iteration """
        execution = self.squash_api.create_squash_execution_for_iteration_test_plan_item(test_plan_item_id)
        res = self.execution_updater.update_execution(execution['id'], STATUS_SKIPPED, comment)
        return execution

    def verify_automation(self, test):
        """ Verify that the test is automated """
        developer_mode = self.runConfig.get('developer_mode', False)
        if not developer_mode and test['Automation_Type'] != 'Automated':
            return False    # filter out manual tests
        if not test['Automation_Test_Name'] and not test['Automation_Suite_Name']:
            return False
        return True

    # TODO filter tests where the test case has no steps !!!     TODO TODO TODO !!!
    # TODO - you can put the same test case in an iteration multiple times, but I don't know why! If both have status READY, then only one should be run

    def filter_environment_device(self, category):
        """ Filter tests based on category """
        # self.logger.info("Filtering test based on category   --- 1 ---")
        aliases = {
            'production': ['production', 'prod', 'prd', 'www', 'preprd2', 'preprd3', 'preprd4', 'ng37'],
            'staging': ['staging', 'stg', 'stage', 'stg1', 'stg2', 'stg3', 'stg4', 'stg5', 'stg6', 'stg7', 'stg8', 'stg9', 'stg10', 'ike', 'ng38'],
            'qa': ['qatest', 'quality assurance', 'qualityassurance', 'bf', 'bug fixes', 'bugfixes', 'bug fix', 'bugfix', 'qa1'],
            'dev': ['developer']
        }
        self.logger.debug(" --- DEBUG ---")
        self.logger.debug(" --- category: {}".format(category))
        self.logger.debug(" --- environment: {}".format(self.runConfig.get('environment', '')))
        self.logger.debug(" --- device: {}".format(self.runConfig.get('device', '')))
        self.logger.debug(" --- prod aliases: {}".format(aliases['production'])) 
        # 
        if 'Staging Test Only' in category and self.runConfig.get('environment', '').lower() in aliases['production']:
            return self.staging_only_comment
        if 'Release Test Only' in category and self.runConfig.get('environment', '').lower()  not in aliases['production']:
            return self.release_only_comment
        if 'Mobile Test Only' in category and self.runConfig.get('device', '') == '':
            return self.mobile_only_comment
        if 'Desktop Test Only' in category and self.runConfig.get('device', '') != '':
            return self.desktop_only_comment
        # self.logger.info("Filtering test based on category   --- 2 --- no errors") 
        return None
    
    def filter_test_with_categories(self, categories): 
        test_cat = self.runConfig.get('categories', [])
        if not test_cat or len(test_cat) == 1 and not test_cat[0]:
            return True
        for cat in test_cat:
            if cat in categories:
                return True
        return False   
    
    def filter_test_with_notCategories(self, notCategories): 
        test_cat = self.runConfig.get('notCategories', [])
        if not test_cat or len(test_cat) == 1 and not test_cat[0]:
            return True
        for cat in test_cat:
            if cat in notCategories:
                return False
        return True 

    def filter_only_tests_with_last_execution_status_not_success_by_apiuser(self, test_plan_items):
        """ Filter tests that have the last execution status not equal to 'SUCCESS' and last_executed_by is apiuser (use this if runConfig['automation_only'] is True) """
        test_cases = []
        for t in test_plan_items:
            if 'executions' in t and len(t['executions']) > 0:
                executions = t['executions']
                # sort by date and get the most recent execution
                latest_execution = executions[0]
                for execution in executions:
                    if 'last_executed_on' in execution and execution['last_executed_on']:
                        execution_time = datetime.fromisoformat(execution['last_executed_on'].replace('Z', '+00:00'))
                        latest_execution_time = datetime.fromisoformat(latest_execution['last_executed_on'].replace('Z', '+00:00'))
                        if execution_time > latest_execution_time:
                            latest_execution = execution
                if 'last_executed_by' in latest_execution and latest_execution['execution_status'] != 'SUCCESS' and latest_execution['last_executed_by'] == self._username:
                    test_cases.append(t)
            #else:
            #    test_cases.append(t)
        return test_cases
    
    def filter_tests_with_status(self, statuses, test_plan_items):
        """ This will return only the test cases from test_plan_items where the most recent status is one of the statuses """
        matching_test_cases = []
        for t in test_plan_items:
            last_execution_status = self.get_last_execution_status_for_test(t)
            if last_execution_status in statuses:
                matching_test_cases.append(t)
        return matching_test_cases
    
    def get_last_execution_status_for_test(self, test):
        last_status = test['execution_status']
        return last_status
    
    def merge_sub_suite_tests(self, test_plan_items):
        """ Merging the sub suite tests into 1 test execution """
        filtered_test_plan_items = []
        sub_suite_test_plan_items = {}
        
        # Split between sub-suite and non sub-suite tests
        for test in test_plan_items:
            automation_sub_suite_name = test.get('automation_sub_suite_name', '')
            browser_device_config = test.get('browser_device_config', '')
            browser_sub_suite_name = automation_sub_suite_name + ' ' + str(browser_device_config)
            self.logger.debug(f"Sub-Suite Setting: {browser_sub_suite_name}")
            if str(automation_sub_suite_name) != '':
                current_list = sub_suite_test_plan_items.get(browser_sub_suite_name, [])
                current_list.append(test)
                sub_suite_test_plan_items[browser_sub_suite_name] = current_list
            else:
                # append non-sub suite tests
                filtered_test_plan_items.append(test)
        
        # Handling sub-suite tests
        automation_cmd = self.runConfig.get('automation', 'robot')    # robot or pytest
        for test_plan_list in sub_suite_test_plan_items.values():
            test_name_list = []
            execution_id_dict = {}
            for test_item in test_plan_list:
                automation_test_name = test_item['automation_test_name']
                test_name_list.append(automation_test_name)
                execution_id_dict[automation_test_name] = test_item['execution_id']
            
            first_test = test_plan_list[0]
            # Only add the test name list and dict if there are more than 1 test
            if len(test_name_list) > 1:
                first_test['automation_test_name_list'] = test_name_list
                first_test['execution_id_dict'] = execution_id_dict
                update_test = self.get_command_for_test(first_test)
                first_test.update(update_test)
            
            filtered_test_plan_items.append(first_test)
        
        return filtered_test_plan_items
            
        
    # --- methods to get the command to run for each test ---------------------
    def get_output_dir_for_test(self, test: dict) -> str:
        """ Create a unique output directory for each test """
        self.logger.debug("Creating output directory for test")
        test_case_id = test.get('referenced_test_case_id', None)
        execution_id = test.get('execution_id', None)
        automation_test_name = self.sanitize_filename(test.get('automation_test_name', ''))
        automation_suite_name = self.sanitize_filename(test.get('automation_suite_name', ''))
        year_month = datetime.now().strftime("%Y_%B")
        year_month_day = datetime.now().strftime("%Y_%m_%d")
        automation_test_name_list = test.get('automation_test_name_list', [])
        if len(automation_test_name_list) > 1:
            automation_test_name = self.sanitize_filename(test.get('automation_sub_suite_name', automation_test_name))
        output_dir = f"logs/{year_month}/{year_month_day}/C{test_case_id}_E{execution_id}_{automation_suite_name}_{automation_test_name}"
        self.logger.debug("Output directory created: {}".format(output_dir))
        return output_dir

    def sanitize_filename(self, name):
        return re.sub(r'[\\/:*?"<>|]', '', str(name))

    def get_tests_to_run(self):
        """ Get the tests to run """
        self.logger.info("Getting tests to run")
        tests = self.get_all_tests_in_campaign_tree()
        filtered_tests = self.filter_tests(tests)
        for test in filtered_tests:
            test['environment'] = self.runConfig.get('environment','')    # Adding environment data to the test info
            update_test = self.get_command_for_test(test)
            test.update(update_test)
        self.logger.info("Tests to run retrieved. Total tests to run: {}".format(len(filtered_tests)))
        self.logger.debug("Tests to run: {}".format(filtered_tests))
        return filtered_tests
    
    def get_command_for_test(self, test: dict) -> list:
        """ Determine if the test command is robot or pytest and construct the command string"""
        # NOTE: TestRunner wants a list of test_info dictionaries, should contain command, log_dir, args, execution_id
        # get commands to run...
        automation_cmd = self.runConfig.get('automation', 'robot')    # robot or pytest
        output_dir = self.get_output_dir_for_test(test)
        test['output_dir'] = output_dir
        test['automation_command'] = automation_cmd
        if automation_cmd == 'robot':
            test['command'] = self.get_robot_command_for_test(test)
        elif automation_cmd == 'pytest':
            test['command'] = self.get_pytest_command_for_test(test)
        if 'command' in test:
            self.logger.debug("Test command: {}".format(test['command']))
        else:
            self.logger.debug("No command found for test: {}".format(test.get('name', 'Unknown')))
        self.logger.debug("Command for test {}: {}".format(test.get('name', 'Unknown'), test.get('command', '')))
        cwd = self.runConfig.get('cwd', '.')
        test['cwd'] = cwd
        
        return test
        
    def get_pytest_command_for_test(self, test: dict) -> str:
        """ return the pytest command to run for the test """
        params = self._get_common_command_params(test)    ### TODO: change this to not include empty args (if there is no browser/device/selenium_grid, don't include it)
        additional_params = self._get_uncommon_additional_params()
        pytest_command = f"{params['automation_cmd']} {params['tests_directory']}/{params['automation_suite_name']}.py::{params['automation_suite_name'].split('/')[-1]}"
        
        if params['automation_test_name_list'] and len(params['automation_test_name_list']) > 0:
            automation_test_name_list_string = " or ".join(params["automation_test_name_list"])
            pytest_command = f'{pytest_command} -k "{automation_test_name_list_string}"'
        else:
            pytest_command = f"{pytest_command}::{params['automation_test_name']}"
        
        if params['execution_id'] and params['execution_id'] != "":
            pytest_command = f'{pytest_command} --execution_id {params["execution_id"]}'
        if params['environment'] and params['environment'] != "":
            pytest_command = f'{pytest_command} --environment {params["environment"]}'
        if params['browser'] and params['browser'] != "":
            pytest_command = f'{pytest_command} --browser {params["browser"]}'
        if params['selenium_grid'] and params['selenium_grid'] != "":
            pytest_command = f'{pytest_command} --remote {params["selenium_grid"]}'
        if params['device'] and params['device'] != "":
            pytest_command = f'{pytest_command} --device {params["device"]}'
        if params['platform'] and params['platform'] != "":
            pytest_command = f'{pytest_command} --platform {params["platform"]}'
        if params['browser_device_config'] and params['browser_device_config'] != "":
            pytest_command = f'{pytest_command} --browser_device_config "{params["browser_device_config"]}"'
        if params['site_config'] and params['site_config'] != "":
            pytest_command = f'{pytest_command} --site_config {params["site_config"]}'
        if params['execution_id_dict'] and len(params['execution_id_dict']) > 0:
            pytest_command = f'{pytest_command} --execution_id_dict "{params["execution_id_dict"]}"'
        pytest_command = f'{pytest_command} --squash_server {self.squash_server}'
        if params['additional_args'] and params['additional_args'] != "":
            additional_args_str = params['additional_args'] 
            # --- the following 5 lines will be deleted soon --- Note: the point of additional_args is it is included in the command line as is, but this code breaks normal pytest args
            if additional_args_str.strip()[0:3] == '-v ' and re.match(r'( ?-v \w+:\w+ ?)+', additional_args_str):
                self.logger.error("DEPRECATION WARNING: if you are using pytest, use pytest style arguments in additional_args, not robot framework")
                # Reformat additional args from "-v key:value" to "--key value"                                                           # TO BE DELETED SOON!!!
                additional_args = [arg.replace(':', ' ', 1).rstrip() for arg in params["additional_args"].split('-v ') if arg.strip()]    # TO BE DELETED SOON!!!
                additional_args_str = '--' + ' --'.join(arg for arg in additional_args if arg).strip()                                    # TO BE DELETED SOON!!!
            # --- the above 5 lines will be deleted soon --- 
            pytest_command = f'{pytest_command} {additional_args_str}'
        for variable, variable_data in additional_params.items():
            pytest_command = f'{pytest_command} --{str(variable)} {variable_data}'

        execution_id = str(params['execution_id'])
        
        # for any vertical that uses pytest framework, if no preference, provide to use default pytest-html report
        pytest_html_report = self.runConfig.get('pytest_html_report', False)
        pytest_robot_report = self.runConfig.get('pytest_robot_report', False)
        if pytest_robot_report:
            if params['execution_id_dict'] and len(params['execution_id_dict']) > 0:
                test_name = test.get('automation_sub_suite_name')
            else:
                test_name = params['automation_test_name']
            # Generate Robot Framework report file names, used for robot pytest framework
            report_name_substring = execution_id + "-" + test_name + "-" + datetime.now().strftime("%m%d%Y_%H%M%S")
            report_name =  report_name_substring + "-report.html"
            log_name = report_name_substring + "-log.html"
            output_name = report_name_substring + "-output.html"

            # use params['cwd'] and params['output_dir'] to get the relative path to the log folder
            cwd = params['cwd']
            output_dir = params['output_dir']
            relative_output_path = os.path.relpath(os.path.abspath(output_dir), os.path.abspath(cwd)) 
            relative_output_path = relative_output_path.replace("\\", "/") 


            pytest_command += (
                f" --robot-outputdir {relative_output_path}"
                f" --robot-loglevel TRACE:INFO"
                f" --robot-log {log_name}"
                f" --robot-output {output_name}"
                f" --robot-report {report_name}"
            )

            # Store report name
            if params['execution_id_dict'] and len(params['execution_id_dict']) > 0:
                for test_name, test_execution_id in params['execution_id_dict'].items():
                    os.environ[str(test_execution_id)] = report_name
            else:
                os.environ[execution_id] = report_name
            
        elif pytest_html_report:
            # pytest-html report as a default fallback report
            report_name = execution_id + "-" + params['automation_test_name'] + "-" + datetime.now().strftime("%m%d%Y_%H%M%S") + "-report.html"
            pytest_use_output_dir_for_html_report = self.runConfig.get('pytest_use_output_dir_for_html_report', False)

            # two options for where the pytest html report file is saved
            if pytest_use_output_dir_for_html_report:
                # use params['cwd'] and params['output_dir'] to get the relative path to the log folder
                cwd = params['cwd']
                output_dir = params['output_dir']
                relative_output_path = os.path.relpath(os.path.abspath(output_dir), os.path.abspath(cwd)) 
                relative_output_path = relative_output_path.replace("\\", "/") 
                html_report_location = f" --html={relative_output_path}/{report_name} --self-contained-html"
            else:
                html_report_location = " --html=html_report/" + report_name + " --self-contained-html"

            pytest_command = pytest_command + html_report_location

            # Store report name
            os.environ[execution_id] = report_name
            
        return pytest_command 

    def get_robot_command_for_test(self, test: dict) -> str:
        """ Return the robot command to run for the test """
        params = self._get_common_command_params(test)
        additional_params = self._get_uncommon_additional_params()
        robot_command = f"{params['automation_cmd']} -d {params['output_dir']}"
        if params['execution_id'] and params['execution_id'] != "":
            robot_command = f'{robot_command} -v execution_id:{params["execution_id"]}'
        if params['environment'] and params['environment'] != "":
            robot_command = f'{robot_command} -v environment:{params["environment"]}'
        if params['browser'] and params['browser'] != "":
            robot_command = f'{robot_command} -v browser:{params["browser"]}'
        if params['selenium_grid'] and params['selenium_grid'] != "":
            robot_command = f'{robot_command} -v remote:{params["selenium_grid"]}'
        if params['device'] and params['device'] != "":
            robot_command = f'{robot_command} -v device:{params["device"]}'
        if params['platform'] and params['platform'] != "":
            robot_command = f'{robot_command} -v platform:{params["platform"]}'
        if params['browser_device_config'] and params['browser_device_config'] != "":
            robot_command = f'{robot_command} -v browser_device_config:"{params["browser_device_config"]}"'
        if params['site_config'] and params['site_config'] != "":
            robot_command = f'{robot_command} -v site_config:"{params["site_config"]}"'
        robot_command = f'{robot_command} -v squash_server:{self.squash_server}'
        if params['execution_id_dict'] and len(params['execution_id_dict']) > 0:
            robot_command = f'{robot_command} -v execution_id_dict:"{params["execution_id_dict"]}"'
        if params['additional_args'] and params['additional_args'] != "":
            robot_command = f'{robot_command} {params["additional_args"]}'
        for variable, variable_data in additional_params.items():
            robot_command = f'{robot_command} -v {str(variable).upper()}:{variable_data}'
        if params['automation_test_name_list'] and len(params['automation_test_name_list']) > 0:
            for test_name in params["automation_test_name_list"]:
                robot_command = f'{robot_command} -t {test_name}'
        else:
            robot_command = f'{robot_command} -t {params["automation_test_name"]}'
        robot_command = f'{robot_command} -s {params["automation_suite_name"]} {params["tests_directory"]}'
        return robot_command

    def _get_common_command_params(self, test: dict) -> dict:
        """ Get common command parameters from the run configuration and test data """
        # Determine browser and device
        default_browser = self.runConfig.get('browser', '')
        default_device = self.runConfig.get('device', '')
        default_platform = self.runConfig.get('platform', '')
        
        return {
            'automation_cmd': self.runConfig.get('automation', ''),
            'browser': test.get('browser', default_browser),
            'device': test.get('device', default_device),
            'platform': test.get('platform', default_platform),
            'selenium_grid': self.runConfig.get('seleniumGridUrl', ''),
            'environment': self.runConfig.get('environment', ''),
            'tests_directory': self.runConfig.get('tests_directory', '.'),
            'cwd': self.runConfig.get('cwd', '.'),
            'output_dir': test.get('output_dir', ''),
            'execution_id': test.get('execution_id', ''),
            'test_case_id': test.get('referenced_test_case_id', ''),
            'automation_test_name': test.get('automation_test_name', ''),
            'automation_test_name_list': test.get('automation_test_name_list', []),
            'execution_id_dict': test.get('execution_id_dict', {}),
            'automation_suite_name': test.get('automation_suite_name', ''),
            'automation_sub_suite_name': test.get('automation_sub_suite_name', ''),
            'browser_device_config': test.get('browser_device_config', ''),
            'site_config': test.get('site_config', ''),
            'additional_args': self.runConfig.get('additionalArgs', '')
        }
    
    def _get_uncommon_additional_params(self) -> dict:
        """ Get the uncommon additional varaible parameters from the run configuration and test data """
        special_variable_list = ['campaign_folders', 'campaigns', 'iterations', 'test_suites', 'automation', 'categories', 'notCategories', 'seleniumGridUrl', 'additionalArgs', 'developer_mode', 'test_status_filter', 'automation_only_mode', 'useSkippedFiltered', 'rerun_failed_tests', 'rerun_statuses', 'pool_size','browser', 'platform','environment','tests_directory', 'merge_output','automation_only','device', 'use_skipped_execution_status', 'cwd', 'squash_server', 'pytest_html_report', 'pytest_robot_report', 'pytest_use_output_dir_for_html_report', 'build_url', 'local_run']
        
        unique_variables_dict = {}
        for variable, variable_data in self.runConfig.items():
            if str(variable) in special_variable_list:
                continue
            unique_variables_dict[variable] = variable_data
        
        return unique_variables_dict
