""" This is where Glados updates an execution with execution status, comment, images  """
import datetime

from .squash_api_glados import SquashAPI
from .squash_constants import STATUS_SUCCESS, STATUS_FAILURE, STATUS_SKIPPED, STATUS_BLOCKED, STATUS_RUNNING, STATUS_UNTESTABLE, STATUS_READY, STATUS_SETTLED, STATUS_CANCELLED
from .logging_config import setup_logging
import robot.api.logger as robotlogger     # this is for Robot Framework logging  --- how do I know if SquashGlados is running a robot or pytest test???
from robot.libraries.BuiltIn import BuiltIn
from robot.api.deco import keyword, library

### NOTE TODO: if a test is rerun, then create a new execution, instead of updating the existing one...

@library
class ExecutionUpdater:
    ROBOT_AUTO_KEYWORDS = False

    def __init__(self, squash_server="prod", include_all_images=True, only_images_in_comment=[], max_images=-1):
        self.logger = setup_logging()
        self.builtin = None
        self.squash_api_glados = SquashAPI(squash_server, self.logger) 

        self.comment = ''
        self.include_all_images = include_all_images
        self.only_images_in_comment = only_images_in_comment
        self.max_images = max_images

        ### TODO: I need a way to know if the test is using Robot Framework or Pytest
        ### if it is Robot Framework, then import some robot stuff like:
        ### from robot.libraries.BuiltIn import BuiltIn
        ### if Pytest, do Python stuff instead
        # maybe there should be a different SquashGlados module that is imported for Robot Framework or Pytest
        # then there is one file with Robot Framework keywords for updating the execution result and overriding the test status
        # and a different file with similar methods for pytest
        # Let's make this file the Robot Framework one, and later copy,paste,&update it for Pytest


    @keyword
    def update_execution(self, execution_id, execution_status=STATUS_RUNNING, comment='', images=[], last_step_started=None, using_robot=True):
        """ Update execution in Squash
        Args: execution_id: int, execution_status: str, comment: str, images: list
        execution_status: 'SUCCESS', 'FAILURE', 'BLOCKED', 'READY', 'RUNNING' """

        if not execution_id:
            return None
        
        self.logger.debug(f'Starting update_execution for execution_id {execution_id} and execution_status {execution_status}')

        retest = False
        timestamp = self.squash_api_glados.get_timestamp()

        if using_robot:
            try:
                self.builtin = BuiltIn()
            except:
                pass
            try:
                if self.builtin:
                    test_status = self.builtin.get_variable_value('${TEST_STATUS}', None)
                    self.logger.debug(f'test_status = {test_status}')
                    override_test_result = self.builtin.get_variable_value('${OVERRIDE_TEST_RESULT}', None)
                    self.logger.debug(f'override_test_result = {override_test_result}')
                    if override_test_result and override_test_result == STATUS_BLOCKED:
                        execution_status = STATUS_BLOCKED
                    elif override_test_result and override_test_result.upper() in ("RETEST", STATUS_UNTESTABLE, STATUS_SETTLED) and 'pass' in test_status.lower():
                        retest = True
                        execution_status = STATUS_SETTLED
                    elif override_test_result and override_test_result == STATUS_SKIPPED:
                        execution_status = STATUS_SKIPPED
                    else:
                        if 'pass' in test_status.lower():
                            execution_status = STATUS_SUCCESS
                        elif 'fail' in test_status.lower():
                            execution_status = STATUS_FAILURE
                        elif 'skip' in test_status.lower():
                            execution_status = STATUS_SKIPPED
                        else:
                            execution_status = STATUS_READY
            except Exception as e:
                pass
                # self.logger.error(f"Error trying to get test status to update the execution {execution_id}: {e}")
    
            comment = f'Updating the execution to status {execution_status}<br/>' + str(comment)
            test_message = ''
            if self.builtin:
                try:
                    test_message = self.builtin.get_variable_value('${TEST MESSAGE}', '')
                except:
                    pass
            if test_message:
                test_message = "<br/>" + test_message
                comment += test_message

        self.logger.debug(f"Updating execution {execution_id} with status {execution_status}")
        if not execution_id:
            raise Exception("Execution ID is required.")
        if not execution_status and not comment and not images:
            raise Exception("At least one of execution_status, comment, or images is required.")
        if not self.include_all_images:
            #if self.only_images_in_comment:    # TODO: not sure about this
            #    images = [image for image in images if image in self.only_images_in_comment]
            if self.max_images > 0:
                images = images[:self.max_images]
        ### TODO: if execution status is not in squash_constants.py, update it 'FAIL' -> 'FAILURE',...
        self.update_squash_execution(execution_id, execution_status, comment, images, last_step_started, retest)

        ### 04-12-2025 Update status and comment
        self.modify_execution(execution_id, status=execution_status, comment=comment, retest=retest)
        ### 04-12-2025 Upload images if provided
        if images:
            self.update_image_attachments(execution_id, images)
        ### 04-12-2025 Update step results if last_step_started is provided
        if last_step_started is not None:
            self.update_execution_with_step_results(execution_id, execution_status, last_step_started)

    def update_custom_field_in_execution(self, execution_id, field_name, field_value):
        """ This updates the given custome field with the given value in an execution """
        url_ending = f'executions/{execution_id}'
        json_data = {"_type": "execution"}
        custom_field_data = {'code': field_name, 'value': field_value}
        json_data['custom_fields'] = [custom_field_data]
        result = self.squash_api_glados._squash_patch(url_ending, json_data)
    
    def update_elapsed_time_in_execution(self, execution_id, elapsed_time):
        """ This updates the elapsed time in an execution """
        elapsed_text = f"{elapsed_time:.2f} seconds"
        self.update_custom_field_in_execution(execution_id, 'Elapsed', elapsed_time)
    
    def update_browser_in_execution(self, execution_id, browser):
        """ This updates the browser name used in an execution """
        self.update_custom_field_in_execution(execution_id, 'Browser', str(browser).title())
    
    def update_device_in_execution(self, execution_id, device):
        """ This updates the device name used in an execution """
        if str(device).lower() == 'ios':
            device = 'iOS'
        elif device and str(device) != '':
            device = str(device).title()
        else:
            device = ''
        
        self.update_custom_field_in_execution(execution_id, 'Test_Device', device)
    
    def update_environment_in_execution(self, execution_id, environment):
        """ This updates the environment used in an execution """
        if str(environment).lower() in ['stg','staging']:
            environment = 'Staging'
        elif str(environment).lower() in ['prd','prod','production']:
            environment = 'Production'
        elif str(environment).lower() in ['qatest','qa']:
            environment = 'QA'
        elif str(environment).lower() in ['dev']:
            environment = 'Dev'
        elif str(environment).lower() in ['other']:
            environment = 'Other'
        else:
            environment = ''
        
        self.update_custom_field_in_execution(execution_id, 'Test_Environment', environment)
    
    def upload_attachments_to_execution(self, execution_id, attachments):
        """ This uploads the attachments to the execution with id execution_id and attachments is a list of strings of paths to files to upload """
        result = self.squash_api_glados.upload_attachments('executions', execution_id, attachments)

    def _format_comments(self, execution_data):    # this is not used, still thinking about it
        """ Format comments for execution update 
            execution_data can contain: elapsed_time, browser, browser_version, device, environment, ... 
            I want to make it so differnt users can format the comments differently
        """
        test_info_table = "<table>"
        for key, value in execution_data.items():
            test_info_table += f"<tr><td>{key}</td><td>{value}</td></tr>"
        test_info_table += "</table>"
        return f"{test_info_table}{self.comment}"

    # ---- copied from squash_api_glados.py ----
    def modify_execution(self, execution_id: int, status: str=None, comment: str=None, retest=False) -> dict:
        """ update the execution to have the status and comment """
        url_ending = f'executions/{execution_id}'
        ### json_data = {"_type": "execution", "last_executed_on": self.squash_api_glados.get_timestamp(), "last_executed_by": 'apiuser'}
        json_data = {"_type": "execution"}
        retest_custom_field = {'code': 'Retest', 'value': retest}
        json_data['custom_fields'] = [retest_custom_field]
        if status:
            # if status = 'retest', then execution_status = 'SETTLED' and retest = True
            if status.lower() == 'retest':
                status = STATUS_SETTLED
                retest_custom_field = {'code': 'Retest', 'value': retest}
                json_data['custom_fields'] = [retest_custom_field]
            json_data["execution_status"] = status
        if comment:
            json_data["comment"] = f'<div>{comment}</div>'
        result = self.squash_api_glados._squash_patch(url_ending, json_data)
        return result
    
    ### Note: I'm thinking about adding different possibilities for formatting comments, especially with images attached...
    # Maybe some users don't want the images to be in the comment, but only attached to the execution
    # Maybe some users want the images to be in the comment, but only the first image
    # Maybe users want to format the comment with HTML themselves? but not sure how to handle the images then...
    # maybe there can be different possible sizes for images in comments, or a max number of images in comments
    # possible variables to add: include_all_images, only_images_in_comment, max_images, comment_format

    def update_squash_execution(self, execution_id: int, execution_status: str, comment: str = '', images: list = [], last_step_started: int = None, retest=False):
        """ update the execution with the execution_status, comment, and images """
        self.logger.debug(f"Updating squash execution {execution_id} with status {execution_status}")
        try:
            self.modify_execution(execution_id, status=execution_status, retest=retest)
            if not last_step_started:
                last_step_started = -1
            self.update_execution_with_step_results(execution_id, execution_status, int(last_step_started))
            if images:
                imgsUploaded = self.squash_api_glados.upload_attachments("executions", execution_id, images)
                comment = f'{comment}<br/><b>Attached Images:</b><table>'
                # divStart = '<div style="display:inline-block; margin: 10px; text-align:center">'
                for img in imgsUploaded:
                    if any(ext in img['filetype'] for ext in ['png', 'jpg', 'jpeg', 'gif']):
                        comment = f'{comment} <tr><td>{img['filename']}</td><td><img alt="{img['filename']}" src="{img['fileurl']}" height="128" width="128"></td></tr>'
                comment = f'{comment}</table>'
            if comment:
                update_time = self.squash_api_glados.get_timestamp()
                old_comments = self.get_execution(execution_id)['comment']
                if not old_comments or str(old_comments) == 'None':
                    old_comments = ''
                else:
                    old_comments = f'<b>Older Comments:</b>{old_comments}'
                comment = f'<table border="0" cellpadding="5" cellspacing="1"><tr><td>Updated by apiuser at {update_time}<br>{comment}</td></tr></table>{old_comments}'
                self.logger.debug(f"Updating execution {execution_id} with comment: {comment}")
                self.modify_execution(execution_id, status=execution_status, comment=comment, retest=retest)
            robotlogger.info(f"Updating execution {execution_id} with status {execution_status} and comment: {comment}", html=True)
        except Exception as e:
            self.logger.error(f"Error updating execution {execution_id}: {e}")
            raise

    def get_execution(self, execution_id: int) -> dict:
        """ get the information about the execution with id = execution_id """
        url_ending = f'executions/{execution_id}'
        result = self.squash_api_glados._squash_get(url_ending)
        return result

    def modify_execution_step(self, execution_id: int, status: str) -> dict:
        """ update the execution step with the status """
        url_ending = f'execution-steps/{execution_id}/execution-status/{status}'
        result = self.squash_api_glados._squash_patch(url_ending, {})
        self.logger.debug(f'modify_execution_step - result: {result}')
        return result

    def get_execution_steps(self, execution_id: int) -> list[int]:
        """ get all the steps for the execution with id = execution_id and return a list of the step ids in ascending order """
        steps = self.squash_api_glados.get_execution_steps(execution_id)
        return steps

    def update_execution_with_step_results(self, execution_id: int, execution_status: str, last_step_started: int):
        """ update the execution with the step results """
        step_results = self.get_execution_steps(execution_id)
        step_number = 1
        for step_id in step_results:
            if last_step_started == -1:
                self.modify_execution_step(step_id, execution_status)
            else:
                status = STATUS_SUCCESS
                if step_number == last_step_started:
                    status = execution_status
                elif step_number > int(last_step_started):
                    break    # don't update the steps that haven't started yet
                    # status = STATUS_BLOCKED
                self.modify_execution_step(step_id, status)
            step_number += 1

    def update_image_attachments(self, execution_id: int, images: list, return_dict: bool = False):
        """ 
            Upload the given list of image attachments to squash test
            If 'return_dict' is set to true will return the data as a dictionary with file name as the key and file url as the value. else will be a list of dictionary.
        """
        
        imgsUploaded = self.squash_api_glados.upload_attachments("executions", execution_id, images)
        comment = ''
        image_upload_list = []
        image_upload_dict = {}
        
        for img in imgsUploaded:
            image_dict = {}
            if return_dict:
                image_upload_dict[img['filename']] = img['fileurl']
            else:
                image_dict["File Name"] = img['filename']
                image_dict["File Url"] = img['fileurl']
                image_upload_list.append(image_dict)
        
        if return_dict:
            return image_upload_dict
        else:
            return image_upload_list


    def update_execution_comment(self, execution_id, comment, replace_comment=False):
        """ either combine the new comment with the old comment or replace the comment in the execution """
        if not replace_comment:
            update_time = self.squash_api_glados.get_timestamp()
            old_comments = self.get_execution(execution_id)['comment']
            if not old_comments or str(old_comments) == 'None':
                old_comments = ''
            else:
                old_comments = f'{old_comments}'
            comment = f'<table border="0" cellpadding="10" cellspacing="1"><tr><td>Updated by apiuser at {update_time}<br>{comment}</td></tr></table>{old_comments}'
            self.logger.debug(f"Updating execution {execution_id} with comment: {comment}")
        url_ending = f'executions/{execution_id}'
        json_data = {"_type": "execution"}
        json_data["comment"] = comment
        result = self.squash_api_glados._squash_patch(url_ending, json_data)
        return result

    ### 04-12-2025 Added method to update Squash TM test status from cucumber.json results
    def update_test_status(self, test, result):
        """
        Update Squash TM test case execution status based on cucumber.json result
        Args:
            test: dict, test case info from config.json
            result: str, status from cucumber.json ('passed', 'failed', 'skipped', 'unknown')
        """
        test_name = test.get('name', '')
        execution_id = test.get('execution_id', None)
        # Map cucumber status to Squash status
        status_map = {
            'passed': STATUS_SUCCESS,
            'failed': STATUS_FAILURE,
            'skipped': STATUS_BLOCKED,
            'unknown': STATUS_UNTESTABLE
        }
        squash_status = status_map.get(result, STATUS_UNTESTABLE)
        self.logger.info(f' Updating test "{test_name}" with result "{result}" (Squash status: "{squash_status}") from cucumber.json.')
        if not execution_id:
            self.logger.error(f' No execution_id found for test "{test_name}". Cannot update status.')
            return
        # Call update_execution with Squash status
        comment = f' Status updated from cucumber.json result: {result}'
        self.update_execution(execution_id, execution_status=squash_status, comment=comment)

    # 04-12-2025 Enhancement: Fetch execution_id for a test case using iteration_id and test_name
    def get_execution_id_for_test(self, iteration_id, test_name):
        """
        Given an iteration_id and test_name, fetch the execution_id from Squash TM.
        Args:
            iteration_id: str or int, the Squash TM iteration folder ID
            test_name: str, the normalized test name
        Returns:
            execution_id: int or None
        """
        timestamp = self.squash_api_glados.get_timestamp()
        self.logger.info(f"Fetching execution_id for test '{test_name}' in iteration '{iteration_id}' at {timestamp}")
        # Get all test cases in the iteration
        test_cases = self.squash_api_glados.get_test_cases_in_iteration(iteration_id)
        normalized_test_name = test_name.strip().lower()
        for tc in test_cases:
            tc_name = tc.get('name', '').strip().lower()
            if normalized_test_name == tc_name:
                # 04-12-2025 Get test plan item id for this test case
                test_plan_item_id = tc.get('test_plan_item_id')
                if not test_plan_item_id:
                    self.logger.error(f"No test_plan_item_id found for test '{test_name}' in iteration '{iteration_id}'")
                    continue
                # 04-12-2025 Check for existing executions
                executions = self.squash_api_glados.get_exeutions_for_iteration_test_plan_item(test_plan_item_id)
                if executions:
                    # Use the most recent execution
                    execution_id = executions[-1].get('id')
                    self.logger.info(f"Found existing execution_id '{execution_id}' for test '{test_name}' in iteration '{iteration_id}'")
                    return execution_id
                else:
                    # 04-12-2025 No execution exists, create one
                    execution = self.squash_api_glados.create_squash_execution_for_iteration_test_plan_item(test_plan_item_id)
                    execution_id = execution.get('id') if execution else None
                    self.logger.info(f"Created new execution_id '{execution_id}' for test '{test_name}' in iteration '{iteration_id}'")
                    return execution_id
        self.logger.error(f"No matching test case found for '{test_name}' in iteration '{iteration_id}'")
        return None

    # def update_statuses_from_cucumber(self, iteration_id, cucumber_scenarios):
    #     """Update Squash executions based on cucumber.json scenarios, skipping and logging missing cases."""
    #     squash_tests = self.squash_api_glados.get_test_cases_in_iteration(iteration_id)
    #     cucumber_names = set([s['name'].strip() for s in cucumber_scenarios])
    #     valid_statuses = {
    #         'passed': STATUS_SUCCESS,
    #         'failed': STATUS_FAILURE,
    #         'skipped': STATUS_SKIPPED,
    #         'blocked': STATUS_BLOCKED,
    #         'untestable': STATUS_UNTESTABLE,
    #         'retest': STATUS_SETTLED,
    #         'cancelled': STATUS_CANCELLED,
    #         'ready': STATUS_READY,
    #         'running': STATUS_RUNNING
    #     }
    #     for test in squash_tests:
    #         test_name = test.get('name', '').strip()
    #         if test_name not in cucumber_names:
    #             self.logger.info(f' SKIPPED: Test case "{test_name}" in iteration {iteration_id} not found in cucumber.json, skipping status update.')
    #             continue
    #         # Find cucumber scenario result
    #         cucumber_result = next((s for s in cucumber_scenarios if s['name'].strip() == test_name), None)
    #         if not cucumber_result:
    #             self.logger.info(f' SKIPPED: Test case "{test_name}" in iteration {iteration_id} not found in cucumber.json, skipping status update.')
    #             continue
    #         raw_status = cucumber_result.get('status', '').lower()
    #         squash_status = valid_statuses.get(raw_status, STATUS_READY)
    #         execution_id = self.get_execution_id_for_test(iteration_id, test_name)
    #         if not execution_id:
    #             self.logger.info(f' SKIPPED: No execution_id found for test "{test_name}" in iteration {iteration_id}.')
    #             continue
    #         self.logger.info(f' Updating test "{test_name}" (execution_id {execution_id}) to status "{squash_status}".')
    #         comment = f'<div> Updating the execution to status {squash_status}<br/></div>'
    #         self.modify_execution(execution_id, status=squash_status, comment=comment)