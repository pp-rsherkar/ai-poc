""" GLaDOS specific Python functions to interact with Squash using the Squash API.  """

# import re
from datetime import datetime, timedelta
import pytz
import json
import requests
from dotenv import load_dotenv
from requests.auth import HTTPBasicAuth
import time
# import mimetypes
#import logging
from .logging_config import setup_logging
import html
import os, sys
from bs4 import BeautifulSoup
from typing import List, Dict
from .squash_constants import (
    STATUS_SUCCESS, STATUS_FAILURE, STATUS_BLOCKED, STATUS_RUNNING,
    STATUS_READY, STATUS_SKIPPED, STATUS_UNTESTABLE, STATUS_SETTLED, STATUS_CANCELLED
)   # SUCCESS and UNTESTABLE are the only statuses that are used in squash_api_glados.py

class SquashAPI:
    """Class to interact with Squash using the Squash API."""
    def __init__(self, server='prod', logger=None, dotenv_path='.env'): 
        missing_env_msg = ""
        load_token = load_dotenv(dotenv_path=dotenv_path)
        if not load_token:
            missing_env_msg = "Warning: The .env file (which stores squash tokens) was not found or is empty. This file needs to be located in the project root directory.\n"
        missing_token_error_msg = f"{missing_env_msg}ERROR: There is no environment variable {server.upper()}_SQUASH_SECRET_API_TOKEN so SquashGlados cannot connect to {server} Squash.\nThe variable can either be added to the OS environment variables or included in the .env file."
            
        if server == 'stg':
            self._base_url = 'https://stg-squash.internetbrands.com/api/rest/latest/' 
            try:
                key = os.environ["STG_SQUASH_SECRET_API_TOKEN"]
            except KeyError:
                sys.exit(missing_token_error_msg)
        else: # Prod
            self._base_url = 'https://squash.internetbrands.com/api/rest/latest/'
            try:
                key = os.environ["PROD_SQUASH_SECRET_API_TOKEN"]
            except KeyError:
                sys.exit(missing_token_error_msg)
        self.auth_header = {"Authorization": "Bearer %s" % key, "Accept": "application/json"}
        if logger:
            self.logger = logger
        else:
            #self.logger = logging.getLogger('SquashGlados.SquashAPI')
            self.logger = setup_logging()
       
    ### UPDATED HTTP REQUESTS HERE
    def _request(self, method: str, url_ending: str, json_data: dict = None, retries: int = 0, retry_delay: int = 2):
        """ General HTTP request method """
        url = f'{self._base_url}{url_ending}'
        self.logger.debug(f'SQUASH API REQUEST ----- HTTP {method} {url}')
        for attempt in range(retries + 1):
            response_json = None
            try:
                self.logger.debug(f'going to send request {url}')
                response = requests.request(method, url, json=json_data, headers=self.auth_header)
                self.logger.debug(f'HTTP status {response.status_code}')
                if response.text:
                    self.logger.debug(f'response text: {response.text}')
                # self.logger.debug(f'HTTP status {response.status_code}')
                if response.status_code == 204:
                    return True
                elif response.status_code >= 400:
                    self.logger.error(f'HTTP error for request {method} {url}')
                    self.logger.error(f'HTTP error {response.status_code}: {response.text}')
                    try:
                        response_json = response.json()
                    except:
                        response_json = "NO JSON FOUND FOR THE RESPONSE"
                    response_json = self.logger.error(f'json response: {response_json}')
                    if response.status_code == 500 and attempt < retries:
                        self.logger.debug(f'Retrying in {retry_delay} seconds...')
                        time.sleep(retry_delay)
                        continue
                    else:
                        self.logger.error(f'Failed after {attempt + 1} attempts')
                        return False
                else:
                    return response.json()
            except requests.exceptions.RequestException as e:
                self.logger.error(f'EXCEPTION Error: {e}')
                if attempt < retries:
                    self.logger.debug(f'Retrying in {retry_delay} seconds...')
                    time.sleep(retry_delay)
                else:
                    self.logger.error(f'Failed after {attempt + 1} attempts')
                    return False

    def _squash_get(self, url_ending: str, retries: int = 1, retry_delay: int = 2):
        """ HTTP GET request """
        return self._request('GET', url_ending, retries=retries, retry_delay=retry_delay)

    def _squash_post(self, url_ending: str, json_data: dict):
        """ HTTP POST request """
        return self._request('POST', url_ending, json_data=json_data)

    def _squash_patch(self, url_ending: str, json_data: dict):
        """ HTTP PATCH request """
        return self._request('PATCH', url_ending, json_data=json_data)

    def _squash_delete(self, url_ending: str):
        """ HTTP DELETE request """
        return self._request('DELETE', url_ending)

    # --- get timestamp --- #
    def get_timestamp(self, offset: str = None, timezone_name: str = 'America/Los_Angeles') -> str:
        """ Get the timestamp in ISO format """
        utc_time = datetime.now(pytz.utc)
        if offset:
            if 'minutes' in offset:
                offset_value = int(offset.split()[0])
                new_time = utc_time + timedelta(minutes=offset_value)
            elif 'hours' in offset:
                offset_value = int(offset.split()[0])
                new_time = utc_time + timedelta(hours=offset_value)
            elif 'days' in offset:
                offset_value = int(offset.split()[0])
                new_time = utc_time + timedelta(days=offset_value)
        else:
            new_time = utc_time
        timezone = pytz.timezone(timezone_name)
        new_time_timezone = new_time.astimezone(timezone)
        return new_time_timezone.isoformat()

    # ---------------------------------------------------------------------------------
    # --- campaign folders --- #
    def get_all_campaigns_in_folder(self, folder_id: int) -> List[int]:
        """ get all the campaigns in the folder with id = folder_id and all the campaigns in subfolders """
        campaign_ids = []
        page = 0
        total_pages = 1
        while page <= total_pages - 1:
            url_ending = f'campaign-folders/{folder_id}/content?page={page}'
            result = self._squash_get(url_ending)
            if '_embedded' in result and 'content' in result['_embedded']:
                for c in result['_embedded']['content']:
                    content_id = c['id']
                    content_type = c['_type']
                    if content_type == 'campaign':
                        campaign_ids.append(content_id)
                    elif content_type == 'campaign-folder':
                        campaign_ids.extend(self.get_all_campaigns_in_folder(content_id))
            if 'page' in result:
                total_pages = result['page']['totalPages']
            page += 1
        return campaign_ids
    # ---------------------------------------------------------------------------------

    # --- functions for Squash campaigns and iterations --- #
    def get_campaign(self, campaign_id: int) -> Dict:
        """ get the information about the campaign with id = campaign_id """
        url_ending = f'campaigns/{campaign_id}'
        result = self._squash_get(url_ending)
        return result

    def get_all_iterations_in_campaign(self, campaign_id:int) -> List[int]:
        """ get all the iterations in the campaign with id = campaign_id """
        iteration_ids = []
        page = 0
        total_pages = 1
        while page <= total_pages - 1:
            url_ending = f'campaigns/{campaign_id}/iterations?page={page}'
            result = self._squash_get(url_ending)
            if '_embedded' in result and 'iterations' in result['_embedded']:
                for i in result['_embedded']['iterations']:
                    iteration_ids.append(i['id'])
            if 'page' in result:
                total_pages = result['page']['totalPages']
            page += 1
        return iteration_ids 

    def get_test_cases_in_campaign_test_plan(self, campaign_id: int) -> List[int]:
        """ given a campaign_id, return a list of test case ids in the campaign's test plan """
        url_ending = f"campaigns/{campaign_id}"
        self.logger.debug(f"(SQUASH_API) Getting test cases in campaign {campaign_id}")
        result = self._squash_get(url_ending)
        self.logger.debug(f"(SQUASH_API) Got test cases in campaign {campaign_id}")
        test_plan = result['test_plan']
        self.logger.debug(f"(SQUASH_API) test_plan: {test_plan}")
        test_case_ids = []
        for t in test_plan:
            reference_test_cases = t['referenced_test_case']
            test_case_ids.append(reference_test_cases["id"])
        self.logger.debug(f"(SQUASH_API) test_case_ids: {test_case_ids}")
        return test_case_ids
    
    def create_squash_iteration(self, campaign_id: int, name: str) -> Dict:
        """ create an iteration for the campaign with id = campaign_id with iteration name = name """
        self.logger.debug(f"(SQUASH_API) Creating iteration {name} for campaign {campaign_id}")
        url_ending = f'campaigns/{campaign_id}/iterations'
        timestamp_start = self.get_timestamp()
        timestamp_end = self.get_timestamp(offset="1 days")
        json_data = {"_type": "iteration", "name": name, "description": f"iteration created by API on {timestamp_start}", "parent": {"_type": "campaign", "id": campaign_id}, "scheduled_start_date": timestamp_start, "scheduled_end_date": timestamp_end, "actual_start_date": timestamp_start, "actual_end_date": timestamp_end}
        result = self._squash_post(url_ending, json_data)
        return result

    # ---------------------------------------------------------------------------------
    

    def create_squash_iteration_test_plan(self, iteration_id: int, test_case_id: int) -> Dict:
        """ create an iteration test plan for iteration_id and with test case: test_case_id """
        try:
            url_ending = f'iterations/{iteration_id}/test-plan'
            json_data = {"_type": "iteration-test-plan-item", "test_case": {"_type": "test-case", "id": test_case_id}}
            result = self._squash_post(url_ending, json_data)
            self.logger.debug(f"(SQUASH_API) RESULT: {result}")
            return result
        except Exception as e:
            self.logger.error(f"Error creating iteration test plan: {e}")

    def add_test_case_to_iteration(self, iteration_id: int, test_case_id: int) -> Dict:
        """ add a test case to the iteration test plan """
        self.logger.debug(f"(SQUASH_API) Adding test case {test_case_id} to iteration {iteration_id}")
        url_ending = f'iterations/{iteration_id}/test-plan'
        json_data = {"_type": "iteration-test-plan-item", "test_case": {"_type": "test-case", "id": test_case_id}}
        result = self._squash_post(url_ending, json_data)
        self.logger.debug(f"(SQUASH_API) RESULT: {result}")
        return result
    
    def get_interation_campaign_data(self, iteration_id: int) -> Dict:
        """ get the campaign id and campaign name associated to the given iteration_id """
        url_ending = f'iterations/{iteration_id}'
        campaign_name = ''
        campaign_id = ''
        
        try:
            iteration_plan = self._squash_get(url_ending)
            campaign_name = iteration_plan['parent']['name']
            campaign_id = iteration_plan['parent']['id']
        except:
            pass
        finally:
            campaign_data = {
                'campaign_name' : campaign_name,
                'campaign_id' : campaign_id
            }
        
        return campaign_data
        
    def get_iteration_browser_data(self, iteration_id: int) -> Dict:
        """ Get the iteration browser data """
        url_ending = f'iterations/{iteration_id}'
        browser_device_dict = {}
        
        try:
            suite_plan = self._squash_get(url_ending)
            
            name = suite_plan['name']
            
            for custom_field in suite_plan['custom_fields']:
                if custom_field['code'] == 'Browser_Device':
                    browser_config = custom_field['value']
                    browser_device_dict['browser_device_config'] = browser_config
                    self.logger.debug(f"(SQUASH_API) Test Suite Browser/Device Info: {browser_config}")
                    # Determine browser
                    browser_device_data  = self.determine_browser_device_data(browser_config)
                    browser_device_dict.update(browser_device_data)
                    break
        except:
            pass
        
        return browser_device_dict
        
    def get_iteration_test_plan(self, iteration_id: int) -> List[Dict]:
        """ get the information about the iteration test plan with id = iteration_id """
        page_number = 0
        plan_items = []
        iter_plan = self._squash_get(f'iterations/{iteration_id}/test-plan?page={page_number}&size=100')
        if iter_plan and '_embedded' in iter_plan and 'test-plan' in iter_plan['_embedded']:
            plan_items = iter_plan['_embedded']['test-plan']
            while '_links' in iter_plan and 'next' in iter_plan['_links']:
                page_number += 1
                iter_plan = self._squash_get(f'iterations/{iteration_id}/test-plan?page={page_number}&size=100')
                if '_embedded' in iter_plan and 'test-plan' in iter_plan['_embedded']:
                    plan_items.extend(iter_plan['_embedded']['test-plan'])
        return plan_items

    def get_test_cases_in_iteration(self, iteration_id: int) -> List[Dict]:
        """ 
        Retrieve test cases in a given iteration.
        Args:    iteration_id (int): The ID of the iteration.
        Returns: List[Dict]: A list of dictionaries containing test case details.
        Test case details includes: 
        a test_plan_item_id, referenced_test_case_id, execution_status, executions, iteration_id,
        automation_type, automation_test_name, automation_suite_name""" 
        iter_plan = self.get_iteration_test_plan(iteration_id)
        iteration_test_suite_map_dict = self.get_all_test_suites_data_in_iterations(iteration_id)
        iteration_browser_data = self.get_iteration_browser_data(iteration_id)
        test_plan_items = []
        for i in iter_plan:
            if 'referenced_test_case' not in i or not i['referenced_test_case']:
                self.logger.debug('skipping test with missing referenced_test_case')
                continue
            test_data = {}
            test_data['test_plan_item_id'] = i['id']
            test_data['referenced_test_case_id'] = i['referenced_test_case']['id']
            test_data['execution_status'] = i['execution_status']
            test_data['executions'] = i['executions']
            test_data['iteration_id'] = i['iteration']['id']
            test_case_details = self.get_test_case(test_data['referenced_test_case_id'])
            test_data['automation_type'] = test_case_details.get('Automation_Type', '')
            test_data['name'] = test_case_details.get('name', '')
            # if there are any space characters in the automation_test_name or automation_suite_name, add quotes around it...
            automation_test_name = test_case_details.get('Automation_Test_Name', '')
            if type(automation_test_name) != str:
                automation_test_name = ''
            automation_test_name = automation_test_name.strip()
            if ' ' in automation_test_name:
                automation_test_name = '"' + automation_test_name + '"'
            test_data['automation_test_name'] = automation_test_name
            automation_suite_name = test_case_details.get('Automation_Suite_Name', '')
            if type(automation_suite_name) != str:
                automation_suite_name = ''
            automation_suite_name = automation_suite_name.strip()
            if ' ' in automation_suite_name:
                automation_suite_name = '"' + automation_suite_name + '"'
            test_data['automation_suite_name'] = automation_suite_name
            test_suite_data = iteration_test_suite_map_dict.get(test_data['test_plan_item_id'], {})
            
            # Sub-suite name
            automation_sub_suite_name = test_case_details.get('Automation_Sub_Suite_Name', '')
            if type(automation_sub_suite_name) != str:
                automation_sub_suite_name = ''
            automation_sub_suite_name = automation_sub_suite_name.strip()
            if automation_sub_suite_name != '' and 'test_suite_id' in test_suite_data:
                # Separate subsuites by squash iteration vs suite
                squash_suite_id = f"SquashSuite{str(test_suite_data['test_suite_id'])}_"
                automation_sub_suite_name = squash_suite_id + automation_sub_suite_name
            if ' ' in automation_sub_suite_name:
                automation_sub_suite_name = '"' + automation_sub_suite_name + '"'
            test_data['automation_sub_suite_name'] = automation_sub_suite_name

            # Handling browser config via test suite or iteration level if no test suite level
            browser_device_config = test_suite_data.get('browser_device_config', '')
            if browser_device_config is not None and str(browser_device_config) != '':
                test_suite_name = test_suite_data.pop('name', None) # Change the test suite key name
                if test_suite_name is not None:
                    test_data['test_suite_name'] = test_suite_name
                test_data.update(test_suite_data)
            else:
                test_data.update(iteration_browser_data)
            test_plan_items.append(test_data)
        return test_plan_items

    def get_all_test_suites_in_iteration(self, iteration_id: int) -> List[int]:
        """ get all the test suites in the iteration with id = iteration_id """
        suite_ids = []
        page = 0
        total_pages = 1
        while page <= total_pages - 1:
            url_ending = f'iterations/{iteration_id}/test-suites?page={page}'
            result = self._squash_get(url_ending)
            if '_embedded' in result and 'test-suites' in result['_embedded']:
                for s in result['_embedded']['test-suites']:
                    suite_ids.append(s['id'])
            if 'page' in result:
                total_pages = result['page']['totalPages']
            page += 1
        return suite_ids
    
    def get_all_test_suites_data_in_iterations(self, iteration_id: int) -> dict:
        """ This gets all the test suite data in the iteration to help map for test plan item id and browser data """
        test_plan_item_dict = {}
        page = 0
        total_pages = 1
        while page <= total_pages - 1:
            url_ending = f'iterations/{iteration_id}/test-suites?page={page}'
            result = self._squash_get(url_ending)
            if '_embedded' in result and 'test-suites' in result['_embedded']:
                for s in result['_embedded']['test-suites']:
                    suite_data = {}
                    suite_data['test_suite_id'] = s['id']
                    try:
                        for custom_field in  s['custom_fields']:
                            if custom_field['code'] == 'Browser_Device':
                                browser_config = custom_field['value']
                                suite_data['browser_device_config'] = browser_config
                                browser_device_data  = self.determine_browser_device_data(browser_config)
                                suite_data.update(browser_device_data)
                                break
                    except:
                        pass
                    test_plan_item_id_list = []
                    for test_plan_item_data in s['test_plan']:
                        test_plan_item_id_list.append(test_plan_item_data['id'])
                        test_plan_item_dict[test_plan_item_data['id']] = suite_data
            if 'page' in result:
                total_pages = result['page']['totalPages']
            page += 1
        return test_plan_item_dict

    def get_test_suite_plan(self, suite_id: int) -> List[Dict]:
        """ get the information about the test suite with id = suite_id """
        url_ending = f'test-suites/{suite_id}/test-plan?size=10000000'
        suite_plan = self._squash_get(url_ending)
        test_plan_items = []
        
        # Get test suite info
        test_suite_info = self.get_test_suite_info(suite_id)
        
        # Get test suite browser/device info
        browser_device_info = test_suite_info.get('browser_device_dict', {})
        
        # Get test suite site info
        site_config = test_suite_info.get('site_config', '')
        
        # Get test suite name
        test_suite_name = test_suite_info.get('name', '')
        
        if suite_plan and '_embedded' in suite_plan and 'test-plan' in suite_plan['_embedded']:
            for i in suite_plan['_embedded']['test-plan']:
                if 'referenced_test_case' not in i or not i['referenced_test_case']:
                    self.logger.debug('skipping test with missing referenced_test_case')
                    continue
                test_data = {}
                test_data['test_suite_name'] = test_suite_name
                test_data['test_suite_id'] = suite_id
                test_data['test_plan_item_id'] = i['id']
                test_data['referenced_test_case_id'] = i['referenced_test_case']['id']
                test_data['execution_status'] = i['execution_status']
                test_data['executions'] = i['executions']
                test_data['iteration_id'] = i['iteration']['id']
                test_data['iteration_name'] = i['iteration']['name']
                test_case_details = self.get_test_case(test_data['referenced_test_case_id'])
                test_data['automation_type'] = test_case_details.get('Automation_Type', '')
                test_data['name'] = test_case_details.get('name', '')
                # if there are any space characters in the automation_test_name or automation_suite_name or automation_sub_suite_name, add quotes around it...
                automation_test_name = test_case_details.get('Automation_Test_Name', '')
                if type(automation_test_name) != str:
                    automation_test_name = ''
                automation_test_name = automation_test_name.strip()
                if ' ' in automation_test_name:
                    automation_test_name = '"' + automation_test_name + '"'
                test_data['automation_test_name'] = automation_test_name
                automation_suite_name = test_case_details.get('Automation_Suite_Name', '')
                if type(automation_suite_name) != str:
                    automation_suite_name = ''
                automation_suite_name = automation_suite_name.strip()
                if ' ' in automation_suite_name:
                    automation_suite_name = '"' + automation_suite_name + '"'
                test_data['automation_suite_name'] = automation_suite_name
                # Sub-suite name
                automation_sub_suite_name = test_case_details.get('Automation_Sub_Suite_Name', '')
                if type(automation_sub_suite_name) != str:
                    automation_sub_suite_name = ''
                automation_sub_suite_name = automation_sub_suite_name.strip()
                if ' ' in automation_sub_suite_name:
                    automation_sub_suite_name = '"' + automation_sub_suite_name + '"'
                test_data['automation_sub_suite_name'] = automation_sub_suite_name
                test_data.update(browser_device_info)
                test_data['site_config'] = site_config
                self.logger.debug(f"(SQUASH_API) DEBUGGING1 TEST DATA Info: {test_data}")
                test_plan_items.append(test_data)
        return test_plan_items
    
    def get_test_suite_info(self, suite_id: int) -> Dict:
        """ Get the test suite information about the test suite with id = suite_id """
        url_ending = f'test-suites/{suite_id}'
        name = ''
        browser_device_dict = {}
        site_config = ''
        
        try:
            suite_plan = self._squash_get(url_ending)
            
            name = suite_plan['name']
            
            for custom_field in suite_plan['custom_fields']:
                if custom_field['code'] == 'Browser_Device':
                    browser_config = custom_field['value']
                    browser_device_dict['browser_device_config'] = browser_config
                    self.logger.debug(f"(SQUASH_API) Test Suite Browser/Device Info: {browser_config}")
                    # Determine browser
                    browser_device_data  = self.determine_browser_device_data(browser_config)
                    browser_device_dict.update(browser_device_data)
                    break
                elif custom_field['code'] == 'Site':
                    site_config = custom_field['value']
                    self.logger.debug(f"(SQUASH_API) Test Suite Site Info: {site_config}")
                    break
        except:
            pass
        finally:
            suite_info = {
                'name' : name,
                'browser_device_dict' : browser_device_dict,
                'site_config' : site_config
                }
        
        return suite_info
    
    def determine_browser_device_data(self, browser_config) -> Dict:
        """ Determine the browser/device data mapping """
        browser_device_dict = {}
        # Determine browser
        if 'chrome' in str(browser_config).lower():
            browser_device_dict['browser'] = 'chrome'
        elif 'safari' in str(browser_config).lower():
            browser_device_dict['browser'] = 'safari'
        # Determine device and platform
        if 'ios' in str(browser_config).lower():
            browser_device_dict['device'] = 'iphone'
            browser_device_dict['platform'] = 'IOS'
        elif 'android' in str(browser_config).lower():
            browser_device_dict['device'] = 'android'
            browser_device_dict['platform'] = 'ANDROID'
        elif 'windows' in str(browser_config).lower():
            browser_device_dict['platform'] = 'windows'
        elif 'mac' in str(browser_config).lower():
            browser_device_dict['platform'] = 'mac'
        
        return browser_device_dict
    
    def get_test_plan_item_info(self, test_plan_item_id) -> Dict:
        """ Get the Test Plan Item info """
        url_ending = f'iteration-test-plan-items/{test_plan_item_id}'
        
        result = self._squash_get(url_ending)
        
        test_plan_data = {}
        test_plan_data['test_plan_item_id'] = result['id']
        test_plan_data['execution_status'] = result['execution_status']
        test_plan_data['iteration_id'] = result['iteration']['id']
        test_plan_data['referenced_test_case_id'] = result['referenced_test_case']['id']
        
        return test_plan_data
    
    # --- functions for Squash executions (moved some stuff to execution_updater.py) --- #
    def create_squash_execution_for_iteration_test_plan_item(self, item_id: int) -> dict:
        """ create an execution for iteration test plan item item_id """
        url_ending = f'iteration-test-plan-items/{item_id}/executions'
        json_data = {}
        result = self._squash_post(url_ending, json_data)
        return result
    
    def DELETE_ME_create_executions_for_campaign(self, campaign_id: int) -> List:    # not sure if I should move to TestFilter?
        """ given a campaign_id, create an iteration, iteration test plan items, and executions """  
        # TODO: if there are no tests in the campaign or iterations in the campaign, there is nothing to execute, so don't create an iteration!
        # this method calls methods above
        timenow = self.get_timestamp()
        name = f'Automation Iteration {timenow}'
        iteration = self.create_squash_iteration(campaign_id, name)
        iteration_id = iteration['id']
        test_cases = self.get_test_cases_in_campaign_test_plan(campaign_id)
        execution_list = []
        for t in test_cases:
            test_plan = self.create_squash_iteration_test_plan(iteration_id, t)
            item_id = test_plan['id']
            execution = self.create_squash_execution_for_iteration_test_plan_item(item_id)
            execution_list.append(execution)
        return execution_list

    # ---------------------------------------------------------------------------------
    # --- attachments --- #
    def upload_attachments(self, owner: str, owner_id: int, files: list, retries: int = 3, retry_delay: int = 2):
        """
        This function will upload one or more files to Squash.
        The owner can be one of the following:
        campaigns, campaign-folders, executions, execution-steps, iterations, projects, requirement-folders, requirement-versions, test-cases, test-case-folders, test-steps, test-suites.
        The owner_id is the id of the owner, for example 
        if you want to upload a file to executions/5 then owner = "executions" and owner_id = 5
        The files parameter is a list of file paths to upload.
        The retries parameter is the number of times to retry the upload if it fails.
        The retry_delay parameter is the number of seconds to wait between retries.

        If the upload is successful, the function will return a list of dictionaries with the following keys:
        filename: the name of the file that was uploaded
        filetype: the file type of the file that was uploaded
        fileurl: the URL to access the file that was uploaded
        If the upload fails, the function will return None.
        """
        url_ending = f'{owner}/{owner_id}/attachments'
        url = f'{self._base_url}{url_ending}'

        ### TODO: first do get url to find out the attachments already uploaded, and do not upload the same file again if the filename is already there...
        files_to_upload = []
        already_uploaded = []
        get_response = self._squash_get(url_ending)
        if get_response and '_embedded' in get_response and 'attachments' in get_response['_embedded']:
            attachments = get_response['_embedded']['attachments']
            previously_uploaded_attachments = {}
            for attached_file in attachments:
                attach_filename = attached_file["name"]
                attachment_api_url = attached_file['_links']['self']['href']
                attachment_content_url = attachment_api_url + '/content'
                file_details = {'filename': attach_filename, 'filetype': attached_file["file_type"], 'fileurl': attachment_content_url}
                previously_uploaded_attachments[attach_filename] = file_details
            #self.logger.info(f'previously_uploaded_attachments = {previously_uploaded_attachments}')
            for filepath in files:
                file_folder,filename = filepath.rsplit(os.sep, 1)
                if filename not in previously_uploaded_attachments.keys():
                    files_to_upload.append(filepath)
                else:
                    already_uploaded.append(previously_uploaded_attachments[filename])
            #self.logger.info(f'files_to_upload = {files_to_upload}')
        else:
            files_to_upload = files
            # TODO: find out if attach_filename is in files, but since files might be abspath, rsplit to get just the filename without path for each f in files...
            # if the file is in files, remove it from list of files to upload
            # but the return dictionary should still contain the filename and it's url
        #  --------------------------------------------------

        files_data = []
        for file_path in files_to_upload:
            try:
                file = open(file_path, 'rb')
                files_data.append(('files', file))
            except Exception as e:
                self.logger.error(f"Exception occurred while opening file {file_path}: {e}")
                return None

        attempt = 0
        while attempt <= retries:
            try:
                response = requests.post(url, headers=self.auth_header, files=files_data)
                if response.status_code == 201:
                    self.logger.debug("Attachments uploaded successfully")
                    response_json = response.json()
                    uploaded_files = []
                    for attachment in response_json['_embedded']['attachments']:
                        # attachment_id = attachment['id']
                        attachment_api_url = attachment['_links']['self']['href']
                        filename = attachment['name']
                        filetype = attachment['file_type']
                        attachment_content_url = attachment_api_url + '/content'
                        uploaded_files.append({'filename': filename, 'filetype': filetype, 'fileurl': attachment_content_url})
                    return uploaded_files
                elif response.status_code in [401, 403]:
                    self.logger.error(f'Authentication error: HTTP STATUS {response.status_code}')
                    self.logger.error(f'json response: {response.json()}')
                    return None
                else:
                    self.logger.error(f'HTTP STATUS: {response.status_code}')
                    self.logger.error(f'json response: {response.json()}')
                    self.logger.error(f"Failed to upload attachments: {response.text}")
                    return None
            except requests.exceptions.ConnectionError as e:
                self.logger.error(f"Connection error occurred: {e}")
            except requests.exceptions.Timeout as e:
                self.logger.error(f"Timeout error occurred: {e}")
            except requests.exceptions.RequestException as e:
                self.logger.error(f"Request exception occurred: {e}")
            except Exception as e:
                self.logger.error(f"Exception occurred while uploading attachments: {e}")
            finally:
                for _, file in files_data:
                    file.close()

            attempt += 1
            if attempt <= retries:
                self.logger.debug(f"Retrying in {retry_delay} seconds...")
                time.sleep(retry_delay)

        self.logger.error("Failed to upload attachments after multiple attempts")
        return None
    
    # --- modify execution (moved to execution_updater.py) --- #
    

    def add_attachments_to_execution(self, execution_id: int, files: list) -> List:
        """ add an attachment to the execution """
        result = self.upload_attachments("executions", execution_id, files)
        # get the old execution comment and add the image to it
        if result:
            execution_info = self.get_execution(execution_id)
            comment = execution_info['comment']
            divStart = '<div style="display:inline-block; margin: 10px; text-align:center">'
            for file in files:
                if any(ext in file for ext in ['png', 'jpg', 'jpeg', 'gif']):
                    comment = f'{comment} {divStart}{file["filename"]} <img alt="{file[""]}" src="{file["fileurl"]}"> </div>'            ### comment = f'<p> <b>Attached Image:</b> </p> <div style="display:inline-block; margin: 10px; text-align:center">{result[0]["filename"]} <img alt="{result[0]["filename"]}" src="{result[0]["fileurl"]}"> </div>'
            if comment:
                self.modify_execution(execution_id, comment=comment)
        return result

    def get_execution_steps(self, execution_id: int) -> list[int]:
        """ get all the steps for the execution with id = execution_id and return a list of the step ids in ascending order """
        execution_steps = []
        page_number = 0
        total_pages = 2
        while page_number < total_pages:
            url_ending = f'executions/{execution_id}/execution-steps?page={page_number}&size=20'
            result = self._squash_get(url_ending)
            result_page = result['page']
            total_pages = result_page['totalPages']
            if '_embedded' in result and 'execution-steps' in result['_embedded']:
                for step in result['_embedded']['execution-steps']:
                    step_id = step['id']
                    execution_steps.append(step_id)
                execution_steps.sort()
            page_number += 1
        return execution_steps
    

    # ----------------------------------------------------------------------------------------------
    def get_exeutions_for_iteration_test_plan_item(self, item_id: int) -> List[Dict]:
        """ get all the executions for the iteration test plan item with id = item_id """
        # note: if you know the iteration id, then get: "{baseurl}iterations/{iteration_id}/test-plan" and find the iteration-test-plan-item id, and use that to get the executions
        # after calling self.get_test_cases_in_iteration, you can get the test_plan_item_id and use that here
        # TODO: need to paginate if there are more than 1 page...
        # TODO: sort by date, get the most recent execution
        # TODO: don't include "?page=0&size=100&sort=last_executed_on,desc" in the url_ending, until you know there are multiple pages...
        url_ending = f'iteration-test-plan-items/{item_id}/executions'     ### ?page=0&size=100&sort=last_executed_on,desc'
        result = self._squash_get(url_ending)
        executions = []
        if '_embedded' in result and 'executions' in result['_embedded']:
            executions = result['_embedded']['executions']
            total_pages = result['page']['totalPages']
            if total_pages > 1:
                for page in range(1, total_pages):
                    url_ending = f'iteration-test-plan-items/{item_id}/executions?page={page}'
                    result = self._squash_get(url_ending)
                    if '_embedded' in result and 'executions' in result['_embedded']:
                        executions.extend(result['_embedded']['executions'])
        return executions

    def get_manually_tested_final_results(self, item_id: int) -> List[str]:
        """ get the final results for the iteration test plan item with id = item_id """
        executions = self.get_exeutions_for_iteration_test_plan_item(item_id)
        final_results = []
        for e in executions:    # I want to get the most recent result and the user who executed it
            # TODO...
            if 'final_result' in e:
                final_results.append(e['final_result'])
        return final_results

    def get_execution_case_custom_data(self, execution_id: int) -> str:
        """ given an execution_id, get execution and return the Data from the test_case_custom_fields or None if not found """
        data = None
        url_ending = f'executions/{execution_id}'
        execution_details = self._squash_get(url_ending)
        for x in execution_details['test_case_custom_fields']:
            if x['code'] == 'Data':
                data = x['value']
        return data
        
    def get_execution_case_name(self, execution_id: int) -> str:
        """ given an execution_id, get execution and return the Data from the test_case_custom_fields or None if not found """
        case_name = None
        url_ending = f'executions/{execution_id}'
        execution_details = self._squash_get(url_ending)
        case_name = execution_details['name']
        return case_name
    
    def get_execution_case_data(self, execution_id: int) -> Dict:
        url_ending = f'executions/{execution_id}'
        execution_data = {}
        try:
            execution_details = self._squash_get(url_ending)
            execution_data['name'] = execution_details['name']
            execution_data['test_plan_item_id'] = execution_details['test_plan_item']['id']
            execution_data['execution_id'] = execution_details['id']
            execution_data['execution_status'] = execution_details['execution_status']
            execution_data['last_executed_on'] = execution_details['last_executed_on']
            execution_data['last_executed_by'] = execution_details['last_executed_by']
            execution_data['importance'] = execution_details['importance']
            execution_data['comment'] = execution_details['comment']
            
            for custom_data in execution_details['test_case_custom_fields']:
                if custom_data['code'] == 'Test_Category':
                    execution_data['test_category'] = custom_data['value']
                elif custom_data['code'] == 'Automation_Type':
                    execution_data['automation_type'] = custom_data['value']
                elif custom_data['code'] == 'Automation_Suite_Name':
                    execution_data['test_suite_name'] = custom_data['value']
                elif custom_data['code'] == 'Automation_Test_Name':
                    execution_data['automation_test_name'] = custom_data['value']
                elif custom_data['code'] == 'YouTrack_Ticket':
                    execution_data['youtrack_ticket'] = custom_data['value']
            
            for execution_custom_data in execution_details['custom_fields']:
                if execution_custom_data['code'] == 'Browser':
                    execution_data['browser'] = execution_custom_data['value']
                elif execution_custom_data['code'] == 'Test_Device':
                    execution_data['test_device'] = execution_custom_data['value']
                elif execution_custom_data['code'] == 'Test_Environment':
                    execution_data['test_environment'] = execution_custom_data['value']
        except:
            pass
        
        return execution_data

    # ### 04-12-2025 Added normalization for scenario names (strip, lower, remove params)
    # def _normalize_scenario_name(self, name):
    #     name = name.strip().lower()
    #     if '[' in name:
    #         name = name.split('[')[0].strip()
    #     return name

    # ### 04-12-2025 Added get_or_create_execution to SquashAPI
    # def get_or_create_execution(self, test_plan_item_id: int) -> int:
    #     """
    #     Returns the execution ID for a test plan item.
    #     If no execution exists, creates one and returns its ID.
    #     """
    #     executions = self.get_exeutions_for_iteration_test_plan_item(test_plan_item_id)
    #     if executions:
    #         # Return the most recent execution ID
    #         return executions[-1]['id']
    #     # No execution exists, create one
    #     result = self.create_squash_execution_for_iteration_test_plan_item(test_plan_item_id)
    #     if result and 'id' in result:
    #         return result['id']
    #     return None
    #
    # ### 04-12-2025: Use normalized scenario name for matching
    # def find_execution_id_by_scenario_name(self, scenario_name, project_id=None):
    #     normalized_input = self._normalize_scenario_name(scenario_name)
    #     url_ending = "executions"
    #     params = {}
    #     if project_id:
    #         params["projectId"] = project_id
    #     try:
    #         result = self._squash_get(url_ending)
    #         if not isinstance(result, dict):
    #             self.logger.error("Squash API call failed or returned non-dict result.")
    #             return None
    #         executions = result.get("_embedded", {}).get("executions", [])
    #         for execution in executions:
    #             exec_name = execution.get("scenarioName")
    #             if exec_name and self._normalize_scenario_name(exec_name) == normalized_input:
    #                 return execution.get("id")
    #         return None
    #     except Exception as e:
    #         self.logger.error(f"Error fetching executions: {e}")
    #         return None

    # ----------------------------------------------------------------------------------------------
    # ----- test cases -----
    def get_test_case(self, test_case_id: int) -> Dict:
        """ This returns all the json from GET /test-cases/{id} """
        url_ending = f'test-cases/{test_case_id}?fields=id,name,custom_fields,steps'
        result = self._squash_get(url_ending)
        self.logger.debug(f'Squash_API_Glados: get_test_case: result: {result}')
        test_case = {
            'test_case_id': result['id'],
            'name': result['name']
        }
        test_case['number_of_steps'] = len(result['steps'])
        if 'custom_fields' in result:
            custom_fields = result['custom_fields']
            for field in custom_fields:
                field_name = field['code']
                field_value = field['value']
                test_case[field_name] = field_value
        return test_case

    def get_test_case_custom_data_section(self, test_case_id: int, return_json: bool = False) -> Dict:
        """ This returns the custom data section from GET /test-cases/{id} """
        test_case = self.get_test_case(test_case_id)
        data = {}
        if 'Data' in test_case and test_case['Data'] is not None:
            if return_json == True:
                data = self.html_to_pretty_json(test_case['Data'])
            else:
                data = self.parse_data_section(test_case['Data'])
        if data is not None:
            return data

        raise Exception(f"No data section exists or it did not contain any data for test case ID: {test_case_id}")

    def html_to_pretty_json(self, html_snippet: str) -> str:
        # 1) Strip out the surrounding <p></p> and replace <br> with newlines
        soup = BeautifulSoup(html_snippet, "html.parser")
        text = soup.get_text(separator="\n")
        # 2) Decode HTML entities (&quot; -> ")
        decoded = html.unescape(text)
        # 3) Make sure it’s valid JSON (we may have an extra closing brace already)
        #    We strip whitespace/newlines and ensure it begins with { and ends with }
        decoded = decoded.strip()
        if not decoded.endswith("}"):
            decoded += "}"
        # 4) Parse + pretty-print
        try:
            obj = json.loads(decoded)
            return json.dumps(obj, indent=2)
        except ValueError:
            return "Data is not JSON format"

    def parse_data_section(self, data_html: str) -> str:
        """ Parses the HTML table from the Data field and formats it as specified. """
        soup = BeautifulSoup(data_html, 'html.parser')
        rows = soup.find_all('tr')

        if not rows:
            return "No data available."

        formatted_data = "|||"

        header_row = rows[0]
        headers = [f":{header.get_text(strip=True)}" for header in header_row.find_all(['th', 'td'])]
        formatted_data += "|".join(headers) + "\n"

        for row in rows[1:]:
            if not row.get_text(strip=True):
                continue
            cols = row.find_all(['th', 'td'])
            if len(cols) >= 2:
                row_data = [col.get_text(strip=True) for col in cols]
                formatted_data += "|| " + " | ".join(row_data) + "\n"

        return formatted_data.replace('\n', '').rstrip()

    ### TODO: I wonder if I should split up this file into multiple files, like test_cases.py, executions.py, etc.
    #### this files should still have all the get/post/patch/delete methods
    ### another file could have methods used with test cases, another with executions, etc.

    ### execution_squash_api methods:
    #     - get_execution,
    #     - get_execution_steps,
    #     - modify_execution,
    #     - modify_execution_step,
    #     - update_execution_with_step_results,
    #     - get_exeutions_for_iteration_test_plan_item,
    #     - get_manually_tested_final_results,

    ### attachment_squash_api methods: upload_attachments, add_attachments_to_execution

    ### test_case_squash_api methods: get_test_case, get_test_cases_in_campaign_test_plan, get_test_cases_in_iteration,

    ### campaign_squash_api methods:
    #     - get_all_campaigns_in_folder
    #     - get_campaign
    #     - get_all_iterations_in_campaign
    #     - get_all_test_suites_in_iteration,
    #     - get_iteration_test_plan,
    #     - get_test_suite_plan,
    #     - create_squash_iteration,
    #     - create_squash_iteration_test_plan,
    #     - add_test_case_to_iteration,
    #     - get_test_cases_in_campaign_test_plan,

    ### other_squash_api methods or utility methods? : get_timestamp,
    ### TODO ----------------------------------------------------------------------------------------------







    ### TODO: need to add more methods for filtering, getting information from Squash, etc...
    def DELETE_ME_get_test_case_automation_information(self, test_case_id):
        """ returns the gitUrl and automation_script for the test case """
        case_info = self.get_test_case(test_case_id)
        gitUrl = case_info['scm_repository_url']
        automation_script = case_info['automated_test_reference']
        return gitUrl, automation_script

    # -----------------------------------------------------------------------------------------------------------
    # hack for code that I didn't write yet...
    def get_other_stuff(self, whatiwant):
        """ This returns all the json from GET /whatiwant  (I might delete this later, but it's here for testing)"""
        result = self._squash_get(whatiwant)
        print(f'result: {result}')
        # useful information in a test case includes: automated_test_reference (where the automation script is), steps, links
        return result





    # -----------------------------------------------------------------------------------------------------------------
    ### everything below is copied from testrail_api_glados.py, need to update for Squash, or remove if not needed

    ### Custom Field info
    ### what are we doing with Browsers, Platforms, Environments, Categories in Squash? These are Custom Variables/fields?
    ### how are they represented in Squash? What are the valid options?

    def get_status_dictionary(self):
        """ returns a dictionary with keys: status code for Squash API, value is the string name of the status """
        pass

    def get_test_categories(self):
        """ returns a dictionary of the custom test categories in Squash, key = Squash code, value = text description """
        pass

    def normalize_input_string(self):
        # copy from testrail_api_glados...
        pass

    def get_results_for_test_case_in_campaign(self, test_id):
        pass

    def get_config_for_campaign(self, test_id):
        # want to copy "get_run_config" from testrail_api_glados, but not sure where the config is saved in Squash...
        pass


    # searching glados.py for "tr_api." to find calls to methods defined in testrail_api_glados, these are used:
    def get_status_dictionary(self):
        pass

    def get_test_categories(self):
        pass

    def extract_run_ids_from_plan_id(self):
        pass

    def get_manually_tested_final_results(self):              ### -> squash_api_glados.get_exeutions_for_iteration_test_plan_item then filter by execution_status
        pass

    def get_tests(self):                    ### -> squash_api_glados.
        pass

    def map_browser_to_testrail(self):       ### do we really need this? can't we just use a string for the browser?
        pass

    def map_test_env_to_testrail(self):      ### not sure, there are multiple possiblities for staging/production, etc. need to add a method to get is_staging, is_production, etc., maybe in FilterTests
        pass

    def map_test_status_to_testrail(self):   ### in Squash, we could just use hard coded values for the status like "SUCCESS", "FAILURE", "BLOCKED", "UNTESTABLE", "RETEST"
        pass

    def add_test_results(self):             ### -> squash_api_glados.update_execution    (you can only modify one execution at at time)
        pass

    def add_test_result(self):              ### -> squash_api_glados.update_execution
        pass

    def post_attachment_to_result(self):    ### ->  squash_api_glados.upload_attachments
        pass

    def get_run(self):                     ### -> squash_api_glados.get_test_cases_in_iteration
        pass

    def get_test_id_from_run_and_case(self):
        pass

    def get_results(self):                 ### -> squash_api_glados.get_exeutions_for_iteration_test_plan_item
        pass
