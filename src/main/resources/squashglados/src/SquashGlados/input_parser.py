import json
import logging
import os


class InputParser():
    def __init__(self, config_file: str):
        self.logger = logging.getLogger('SquashGlados.InputParser')
        self.config_file = config_file
        try:
            self.config_data = self._load_config()
        except Exception as e:
            self.logger.error(f"Error loading config file: {e}")
            raise     # Exception(f"Error loading config file: {e}")

    def _validate_config(self, json_data: dict) -> None:
        """ Validate the config file """
        automation = json_data.get('automation', None)
        valid_automation_types = ['robot', 'pytest', 'cucumber-java-bdd-playwright-maven']
        if not automation or automation not in valid_automation_types:
            self.logger.error(
                f"*** WARNING *** automation is required in the SquashGlados config json file and valid options are {valid_automation_types}")
        campaigns = json_data.get('campaigns', None)
        iterations = json_data.get('iterations', None)
        campaign_folders = json_data.get('campaign_folders', None)
        test_suites = json_data.get('test_suites', None)
        if not any([campaigns, iterations, campaign_folders, test_suites]):
            raise ValueError("ERROR: At least one of the following keys is required: iterations, test_suites")
        browser = json_data.get('browser', None)
        seleniumGridUrl = json_data.get('seleniumGridUrl', None)
        local_run = json_data.get('local_run', None)
        if browser and not seleniumGridUrl and not local_run:
            raise ValueError("ERROR: If browser is provided, seleniumGridUrl must be provided.")

    def _load_config(self) -> dict:
        """ Load the config file """
        self.logger.info(f"Loading config file {self.config_file}")
        try:
            with open(self.config_file, 'r') as file:
                config_data = json.load(file)
                self.logger.debug(f"Config file {self.config_file} loaded successfully.")
                self._validate_config(config_data)
                self.logger.debug(f"Config file {self.config_file} validated successfully.")
                return config_data
        except FileNotFoundError:
            self.logger.error(f"Config file {self.config_file} not found.")
            raise     # Exception(f"Config file {self.config_file} not found.")
        except json.JSONDecodeError:
            self.logger.error(f"Config file {self.config_file} is not a valid JSON.")
            raise     # Exception(f"Config file {self.config_file} is not a valid JSON.")
        except Exception as e:
            self.logger.error(f"Unexpected error loading config file {self.config_file}: {e}")
            raise    # Exception(f"Unexpected error loading config file {self.config_file}: {e}")

    def get_json_input(self) -> dict:
        return self.config_data 

    def get_campaign_ids(self) -> list:
        return self.config_data.get('campaigns', [])
    def get_pool_size(self) -> int:
        return self.config_data.get('pool_size', 4)

    def get_browser(self) -> str:
        return self.config_data.get('browser', '')

    def get_seleniumGridUrl(self) -> list:
        return self.config_data.get('seleniumGridUrl', [])

    def is_development_mode(self) -> bool:
        return self.config_data.get('development_mode', False)

    def get_variable(self, variable_name: str, default_value=None):
        return self.config_data.get(variable_name, default_value)

    def get_filter_rules(self) -> dict:
        """ create json variable including browser, environment, category, etc. """
        rules = {}
        for key in ['browser', 'environment', 'category']:
            if key in self.config_data:
                rules[key] = self.config_data[key]
        return rules 

    ### 04-12-2025: New methods for Cucumber BDD results parsing - fetch results path and mapping mode
    def get_results_path(self) -> str:
        return self.config_data.get('results_path', '')

    # def get_mapping_mode(self) -> str:
    #     return self.config_data.get('mapping_mode', 'test_name')

    ### 04-12-2025 Enhanced normalization and edge case handling for scenario names
    def normalize_scenario_name(self, name: str) -> str:
        # Remove extra whitespace, lowercase, and strip special characters
        import re
        name = name.strip().lower()
        name = re.sub(r'[^a-z0-9 ]', '', name)
        name = re.sub(r'\s+', ' ', name)
        return name

    ### 04-12-2025 Improved parse_cucumber_results to handle scenario outlines and missing steps
    def parse_cucumber_results(self) -> dict:
        # [21-11-2025] Error handling for missing cucumber.json
        results_path = self.get_results_path()
        scenario_results = {}
        if not os.path.exists(results_path):
            self.logger.error(f"cucumber.json file not found at path: {results_path}")
            # [21-11-2025] Stop further processing if file is missing
            raise FileNotFoundError(f"cucumber.json file not found at path: {results_path}")
        try:
            with open(results_path, 'r', encoding='utf-8') as f:
                cucumber_data = json.load(f)
                for feature in cucumber_data:
                    if 'elements' in feature:
                        for scenario in feature['elements']:
                            name = scenario.get('name', '').strip()
                            normalized_name = self.normalize_scenario_name(name)
                            status = 'unknown'
                            # Find scenario status from steps
                            if 'steps' in scenario and scenario['steps']:
                                step_statuses = [step['result']['status'] for step in scenario['steps'] if 'result' in step]
                                if step_statuses and all(s == 'passed' for s in step_statuses):
                                    status = 'passed'
                                elif step_statuses and any(s == 'failed' for s in step_statuses):
                                    status = 'failed'
                                elif step_statuses and any(s == 'skipped' for s in step_statuses):
                                    status = 'skipped'
                                else:
                                    status = 'unknown'
                            else:
                                # [21-11-2025] Handle missing steps as unknown
                                status = 'unknown'
                            scenario_results[normalized_name] = status
            return scenario_results
        except Exception as e:
            self.logger.error(f"Error parsing cucumber.json: {e}")
            return {}