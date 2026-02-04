# import logging
from .logging_config import setup_logging
import multiprocessing
import re
import subprocess
import os
import time
import shutil
from pathlib import Path
import shlex
import sys
from .execution_updater import ExecutionUpdater
from .squash_constants import STATUS_RUNNING, STATUS_FAILURE    # STATUS_COMPLETED

class TestRunner:
    def __init__(self, squash_server="prod", tests_to_run=None, pool_size=4, build_url=None):
        self.pool_size = int(pool_size)
        self.tests = tests_to_run or []
        self.execution_updater = ExecutionUpdater(squash_server)
        squash_env = ''
        if squash_server == 'stg':
            squash_env = 'stg-'
        self.squash_case_url_template = f'https://{squash_env}squash.internetbrands.com/test-case-workspace/test-case/__CASE_ID__/content?anchor=information'
        self.squash_test_suite_url_template = f'https://{squash_env}squash.internetbrands.com/campaign-workspace/test-suite/__TEST_SUITE_ID__/content?anchor=plan-exec'
        self.squash_iteration_url_template = f'https://{squash_env}squash.internetbrands.com/campaign-workspace/iteration/__ITERATION_ID__/test-plan?anchor=plan-exec'
        self.build_url = build_url
        #self.logger = logging.getLogger('SquashGlados.TestRunner')
        self.logger = setup_logging()
        self.logger.info(f"TestRunner initialized with pool size {self.pool_size}")
        self.logger.info(f"Number of tests to run: {len(self.tests)}")

    def add_test_to_list(self, test_info):
        self.tests.append(test_info)

    def run_tests(self, disable_post_log=False):
        self.logger.debug(f"inside method run_tests -- Running tests with pool size {self.pool_size}")
        total_failures = 0
        results = []

        with multiprocessing.Pool(int(self.pool_size)) as pool:
            for test_info in self.tests:
                result = pool.apply_async(self.run_test, args=(test_info,disable_post_log,))
                results.append(result)
                time.sleep(1)    # Wait 1 second between each process spawn

            pool.close()
            pool.join()

        self.logger.debug(f"Finished the multiprocessing pool running all tests")

        # Collect results and count failures
        all_error_messages = {}
        for result in results:
            test_result, errmsg = result.get()
            if test_result == 0:
                continue
            total_failures = total_failures + 1
            if errmsg in all_error_messages:
                all_error_messages[errmsg] += 1
            else:
                all_error_messages[errmsg] = 1
        print(" ----------")
        print("Here is a summary of the unique errors and the sum of tests with each error:")
        print(f'count   error')
        print(f'-----   -----')
        for k, v in all_error_messages.items():
            tempmsg = list(filter(lambda y: y != '', k.split('\n')))
            if len(tempmsg) > 0:
                print(f'  {v}     {tempmsg[0][0:200]}')

        results_message = ("\nFINAL RESULT:  {failure_count_ph}/{test_count_ph} tests failed.\n")
        results_message = results_message.format(
            failure_count_ph=total_failures,
            test_count_ph=len(self.tests))
        print(results_message)

        return total_failures

    def run_test(self, test_info, disable_post_log=False):
        #logging.basicConfig(level=logging.INFO)  # Configure logging level for each process
        #logger = logging.getLogger('SquashGlados.TestRunner')
        logger = setup_logging(log_file='squash_glados_executions.log')

        ### 04-12-2025 Sanitize test name for directory/file usage
        def sanitize_filename(name):
            sanitized = re.sub(r'[\\/:*?"<>|]', '', name)
            return sanitized[:100]  # Truncate to 100 chars for Windows compatibility

        combined_test_name = test_info['automation_test_name'] + f"({test_info['name']})"
        sanitized_test_name = sanitize_filename(test_info['automation_test_name']) + f"({sanitize_filename(test_info['name'])})"
        logger.info(f"Starting test: {combined_test_name}")
        start_time = None
        end_time = None
        ### 04-12-2025 Initialize to None for error handling
        execution_id = None
        try:
            time.sleep(5)
            test_command = test_info.get('command') 
            automation_command = test_info.get('automation_command', 'pytest') 
            log_dir = test_info.get('output_dir')
            ### 04-12-2025 Use sanitized and truncated test name in log_dir if needed
            if log_dir:
                log_dir = os.path.join(log_dir, sanitized_test_name)
            else:
                log_dir = sanitized_test_name
            ### 04-12-2025 Truncate full path for Windows compatibility
            log_dir = log_dir[:150]
            os.makedirs(log_dir, exist_ok=True)
            execution_id = test_info.get('execution_id')
            cwd = test_info.get('cwd', '.')
            self.execution_updater.update_execution(execution_id, STATUS_RUNNING, 'Test is running', last_step_started=1)
            # add a comment in the execution with a link to the test case
            case_id = test_info.get('referenced_test_case_id', '')
            squash_case_url = self.squash_case_url_template.replace('__CASE_ID__', str(case_id))
            comment = f'<br><ul><li><b>Referenced Test Case:</b> <a href="{squash_case_url}">{squash_case_url}</a></li>'
            # add a comment in the execution with a link to the test suite/iterration execution
            test_suite_id = test_info.get('test_suite_id', '')
            iteration_id = test_info.get('iteration_id', '')
            if str(test_suite_id) != '':
                squash_test_suite_url = self.squash_test_suite_url_template.replace('__TEST_SUITE_ID__', str(test_suite_id))
                comment = comment + f'<li><b>Test Suite Execution Link:</b> <a href="{squash_test_suite_url}">{squash_test_suite_url}</a></li></ul>'
            elif str(iteration_id) != '':
                squash_iteration_url = self.squash_iteration_url_template.replace('__ITERATION_ID__', str(iteration_id))
                comment = comment + f'<li><b>Iteration Execution Link:</b> <a href="{squash_iteration_url}">{squash_iteration_url}</a></li></ul>'
            else:
                comment = comment + '</ul>'
            self.execution_updater.update_execution_comment(execution_id, comment, replace_comment=False) 
            # Adding custom test field data (environemnt, browser, device)
            test_environment = test_info.get('environment','') 
            self.execution_updater.update_environment_in_execution(execution_id, str(test_environment))
            test_browser = test_info.get('browser', '') 
            self.execution_updater.update_browser_in_execution(execution_id, test_browser)
            test_device = test_info.get('device', '') 
            self.execution_updater.update_device_in_execution(execution_id, test_device)
            # Check for missing command
            if not test_command:
                logger.error(f"No command found for test: {test_info.get('name', 'Unknown')}")
                self.execution_updater.update_execution(execution_id, STATUS_FAILURE, 'No command found for test')
                return 2, 'No command found for test'
            # Construct the command list directly - sys.executable is the python interpreter, the first word in test_command is "robot" or "pytest"
            command_list = [sys.executable, '-m'] + shlex.split(test_command)
            start_time = time.time()
            result = subprocess.run(command_list, check=True, cwd=cwd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
            end_time = time.time()
            elapsed_time = end_time - start_time
            self.execution_updater.update_elapsed_time_in_execution(execution_id, elapsed_time)
            if log_dir:
                self._write_output_files(log_dir, result.stdout, result.stderr)
                if str(disable_post_log).lower() != 'true':
                    self._zip_and_upload_logs(log_dir, execution_id, automation_command)
            else:
                logger.warning(f"Log directory not found for test {test_info['automation_test_name']}")
            if "| FAIL |" in result.stdout.decode():
                logger.warning(f"Finished test: {combined_test_name} with FAIL test status and returncode: {result.returncode} ")
            else:
                msg = f"Finished test: {combined_test_name} with returncode: {result.returncode}"
                if result.returncode == 0:
                    msg += "  (PASSED)"
                logger.info(msg)
            return 0, None    # 0 indicates success, no error message 
        except subprocess.CalledProcessError as e:
            if start_time and not end_time:
                end_time = time.time()
                elapsed_time = end_time - start_time
                self.execution_updater.update_elapsed_time_in_execution(execution_id, elapsed_time)
            logger.error(f"Test {combined_test_name} failed")
            # ---
            try:
                decoded_output = e.stdout.decode('utf-8')
            except UnicodeDecodeError:
                decoded_output = e.stdout.decode('latin-1')
            try:
                decoded_error = e.stderr.decode('utf-8')
            except UnicodeDecodeError:
                decoded_error = e.stderr.decode('latin-1')
            # ---
            logger.error(f"Test {combined_test_name} STDOUT output: \n{decoded_output}")
            logger.error(f"Test {combined_test_name} STDERR: {decoded_error}{os.linesep}")
            # ---
            if log_dir:
                self._write_output_files(log_dir, e.stdout, e.stderr, fail=True)
                if str(disable_post_log).lower() != 'true':
                    self._zip_and_upload_logs(log_dir, execution_id, automation_command)
            execution_info = self.execution_updater.get_execution(execution_id)
            if execution_info['execution_status'] == STATUS_RUNNING:
                self.execution_updater.update_execution(execution_id, STATUS_FAILURE, 'EXCEPTION CalledProcessError: ' + str(e) + "<br/>" +  str(e.stderr.decode()) )

            # read the stdout file and look for an error message
            error_message = ""
            lines = decoded_output.split('\n')
            if 'robot' in command_list:
                # get lines between "FAIL" and "----" as error message
                errmsg = False
                for line in lines:
                    if '-' * 20 in line:
                        break
                    if 'FAIL' in line:
                        errmsg = True
                    elif errmsg:
                        error_message += line
            else:
                error_message = ""
                short_summary_found = False
                exception_found = False

                # First, try to extract Exception message anywhere in the log (prioritized)
                for index, line in enumerate(lines):
                    # Look for lines that start with "Exception:" or contain "Exception:"
                    if 'Exception:' in line:
                        # Extract message after "Exception:" and strip spaces
                        parts = line.split('Exception:', 1)
                        if len(parts) > 1:
                            if parts[1].strip() != '':
                                error_message = parts[1].strip()
                            else:
                                error_message = lines[index + 1] if index + 1 < len(lines) else 'no error message found'
                            exception_found = True
                            break

                # If no Exception found, fallback to your original parsing after "short test summary info"
                if not exception_found:
                    for line in lines:
                        if short_summary_found:
                            if '-' in line:
                                errmsg = '-'.join(line.split('-')[1:]).strip()
                                error_message += errmsg
                            else:
                                error_message += line.strip()
                            break
                        if 'short test summary info' in line.lower():
                            short_summary_found = True

            error_message = error_message.rstrip('\n')
            return 1, error_message  # 1 indicates failure, return error message

        except Exception as e:
            if start_time and not end_time:
                end_time = time.time()
                elapsed_time = end_time - start_time
                if execution_id:
                    self.execution_updater.update_elapsed_time_in_execution(execution_id, elapsed_time)
            logger.error(f"An unexpected error occurred while running test {combined_test_name}: {e}")
            if execution_id:
                self.execution_updater.update_execution(execution_id, STATUS_FAILURE,'OTHER EXCEPTION from run_test: ' + str(e))
            return 2, str(e)    # 2 indicates failure, return error message
        
    def _write_output_files(self, log_dir, stdout, stderr, fail=False):
        try:
            stdout_file = Path(log_dir) / ("stdout_fail.txt" if fail else "stdout.txt")
            stderr_file = Path(log_dir) / ("stderr_fail.txt" if fail else "stderr.txt")
            with open(stdout_file, 'w') as f:
                f.write(stdout.decode())
            with open(stderr_file, 'w') as f:
                f.write(stderr.decode())
        except Exception as e:
            self.logger.error(f"Error writing output files: {e}")

    def _zip_and_upload_logs(self, log_dir, execution_id, automation_command='pytest'):
        log_file_path = Path(log_dir)
        if log_file_path.exists():
            image_files = [f'{log_dir}{os.sep}{f}' for f in os.listdir(log_dir) if f.endswith('.png')]
            normalized_path = os.path.normpath(log_dir)
            last_folder = os.path.basename(normalized_path) 
            outputFolder = "output_" + last_folder 
            output_zip_path = log_file_path.parent.absolute() / outputFolder
            shutil.make_archive(output_zip_path, "zip", log_file_path.absolute())
            list_of_files_to_upload = []
            list_of_files_to_upload.append(os.fspath(output_zip_path.with_suffix(".zip")))
            list_of_files_to_upload.extend(image_files)
            self.execution_updater.upload_attachments_to_execution(execution_id, attachments=list_of_files_to_upload)
            output_zip_path.with_suffix(".zip").unlink()
            # update the execution comment with a URL to the log file
            if self.build_url:
                log_url = self.build_url + log_dir
                if automation_command == 'pytest':
                    comment = f'The log file can be found here: <a href="{log_url}">{log_url}</a>'
                elif automation_command == 'robot':
                    comment = f'The log file can be found here: <a href="{log_url}/log.html">{log_url}/log.html</a>'
                result = self.execution_updater.update_execution_comment(execution_id, comment=comment, replace_comment=False)
        else:
            self.logger.warning(f"Log file {log_file_path} not found for test.")

    ### 04-12-2025 Added logic to map cucumber.json results to Squash TM test cases and update status
    # def update_test_case_status_from_cucumber(self, input_parser):
    #     """
    #     Parse cucumber.json and update Squash TM test case status for each scenario.
    #     """
    #     scenario_results = input_parser.parse_cucumber_results()
    #     mapping_mode = input_parser.get_mapping_mode()
    #     for test_info in self.tests:
    #         # [20-11-2025] Normalize scenario name for robust mapping
    #         scenario_name = input_parser._normalize_scenario_name(test_info.get('name', ''))
    #         matched_status = None
    #         self.logger.info(f"Checking Squash TM test case '{scenario_name}' for mapping.")
    #         for parsed_name, status in scenario_results.items():
    #             self.logger.info(f"Comparing with parsed scenario '{parsed_name}' from cucumber.json.")
    #             if scenario_name == parsed_name:
    #                 matched_status = status
    #                 break
    #         execution_id = test_info.get('execution_id')
    #         if not execution_id:
    #             self.logger.error(f"No execution_id found for test '{scenario_name}'. Status cannot be updated.")
    #         if matched_status and execution_id:
    #             if matched_status == 'passed':
    #                 self.execution_updater.update_execution_status(execution_id, 'SUCCESS')
    #             elif matched_status == 'failed':
    #                 self.execution_updater.update_execution_status(execution_id, 'FAILURE')
    #             elif matched_status == 'skipped':
    #                 self.execution_updater.update_execution_status(execution_id, 'SKIPPED')
    #             else:
    #                 self.execution_updater.update_execution_status(execution_id, 'UNKNOWN')
    #             self.logger.info(f"Updated Squash TM test case '{scenario_name}' with status '{matched_status}' from cucumber.json.")
    #         elif matched_status:
    #             self.logger.warning(f"Matching scenario found but no execution_id for '{scenario_name}'.")
    #         else:
    #             self.logger.warning(f"No matching scenario found in cucumber.json for Squash TM test case '{scenario_name}'.")
    #

