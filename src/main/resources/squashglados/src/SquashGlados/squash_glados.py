import argparse
import json
from . import SquashGlados
import sys
from .logging_config import setup_logging

def main():
    logger = setup_logging()
    logger.info("Starting SquashGlados...")
    squash_glados = SquashGlados()

    # get command line argument config file
    config_file = 'squashGladosConfig.json'
    if len(sys.argv) == 1:
        print("Please provide a config file or command line arguments.")  # Suggest using the config file generator web page
        sys.exit(1)
    elif len(sys.argv) > 1 and sys.argv[1].endswith('.json'):
        print("Config file is provided")
        config_file = sys.argv[1]
    else:
        print(f"Command line arguments are provided: {' '.join(sys.argv[1:])}")
        args = setup_argparse()
        # Prepare configuration data from args, ensuring parameter names are consistent with the parameter names of config file.
        # deal with additional args
        additional_args = "-v version: " if args.automation == 'robot' else ""
       
        for custom_var in args.variable:
            var_name, var_value = custom_var.split(':', 1)
            additional_args += " -v " + var_name + ":" + var_value

        # convert status to uppercase
        test_status_filter = [status.upper().replace('FAILED', 'FAILURE') for status in args.test_status_filter]
        if 'ALL' in test_status_filter:
            test_status_filter = ['all']

        # replace underscores with spaces, capitalize each word, and ensure 'Ui' is 'UI'
        test_category = [cat.replace('_', ' ').title().replace('Ui', 'UI') for cat in args.categories]

        # replace underscores with spaces, capitalize each word, and ensure 'Ui' is 'UI'
        test_not_category = [cat.replace('_', ' ').title().replace('Ui', 'UI') for cat in args.notCategories]

        config_data = {
            'squash_server': args.squash_server,
            'environment': args.environment,
            'browser': args.browser,
            'device': args.device,
            'platform': args.platform,
            'automation': args.automation,
            'tests_directory': args.tests_directory,
            'cwd': args.cwd,
            'merge_output': args.merge_output,
            'categories': test_category,
            'notCategories': test_not_category,
            'seleniumGridUrl': args.seleniumGridUrl,
            'additionalArgs': additional_args,
            'developer_mode': args.developer_mode,
            'test_status_filter': test_status_filter,
            'automation_only': args.automation_only,
            'pool_size': args.pool_size,
            'use_skipped_execution_status': args.use_skipped_execution_status,
            'rerun_failed_tests': args.rerun_failed_tests,
            'disable_post_log': args.disable_post_log,
            'pytest_html_report': args.pytest_html_report,
            'pytest_robot_report': args.pytest_robot_report,
        }
        
        if args.iterations is not None:
            config_data['iterations'] = args.iterations
        elif args.test_suites is not None:
            config_data['test_suites'] = args.test_suites

        # Save the configuration to a JSON file
        try:
            with open(config_file, 'w') as json_file:
                json.dump(config_data, json_file, indent=4)
            print(f'Configuration saved to {config_file}')
        except IOError as e:
            print(f'Error saving configuration to JSON: {e}')
            sys.exit(1)

    # Run the SquashGlados with the config file
    squash_glados.run(config_file)


def setup_argparse():
    """Sets up the medium for storing data using the argparse module.

    Notes:
    - Input validation should be done utilizing the argparse module whenever
      possible
    """

    # Arguments
    glados_description = "SquashGlados runs automation tests from a Squash iteration or campaign in parallel and updates the results in Squash."
    glados_lastLine = "SquashGlados is managed by the Internet Brands QA Automation Team"
    parser = argparse.ArgumentParser(prog="SquashGlados", description=glados_description, epilog=glados_lastLine)

    # Help texts
    help_iterations = 'List of Squash iteration IDs. Provide one or more IDs separated by spaces.Example: -i 1 or -i 1 2'
    help_test_suites = 'List of Squash Test Suite IDs. Provide one or more IDs separated by spaces.Example: -t 1 or -t 1 2'
    help_squash_server = 'squash server value: {squash_server}. Must be "prod" or "stg"'
    help_environment = 'Specify the environment (default: ""). Example: -e stg'
    help_browser = 'Browser to use (default: ""). Example: -b chrome'
    help_device = 'Device to use (default: ""). Example: -d iphone'
    help_platform = 'Platform to use (default: ""). Example: -pf windows'
    help_automation = 'Type of automation to use (default: "robot"). Example: -x robot or -x pytest'
    help_tests_directory = 'Relative path for Robot Framework tests (default: "."). Example: -s ./tests'
    help_cwd = 'Relative path used as CWD for pytest (default: "."). Example: -cwd ./'
    help_merge_output = 'If true, merge output files into one (default: False). Example: -m True or -m true'
    help_categories = 'List of categories to run. Provide one or more categories. Example: -c smoke_test or -c smoke_test -c design/ui'
    help_not_categories = 'List of categories to exclude. Provide one or more categories. Example: -n smoke_test or -n smoke_test -n design/ui'
    help_selenium_grid_url = 'URL to the remote grid to run tests on (required if browser is specified). Example: -r http://example.com'
    help_variable = 'Creates a variable in Robot Framework (syntax: name:value). Example: -v programId:None'
    help_developer_mode = 'If true, run tests with automation test name (default: False). Example: -dev True or -dev true'
    help_test_status_filter = 'List of statuses to filter. Provide one or more statuses. Example: -f failed or -f failed -f passed'
    help_automation_only = 'If true, run tests if last execution updated by apiuser (default: False). Example: -a True or -a true'
    help_pool_size = 'Positive integer for how many tests to run at the same time (default: 4). Example: -p 10'
    help_use_skipped_execution_status = 'If true, create execution and update status to "SKIPPED" (default: False). Example: -uskip True -uskip true'
    help_rerun_failed_tests = 'Non-negative integer for rerunning failed tests (default: 0). Example: -rft 1'
    help_disable_post_log = 'If true, will disable zipping and adding log attachment to squash (default: False). Example: -dpl True or -dpl true'
    help_pytest_robot_report = 'If true, will generate pytest-robotframework report (default: False)'
    help_pytest_html_report = 'If true, will generate pytest-html report (default: False)'

    # Required argument
    parser.add_argument('-i', '--iterations', nargs='+', help=help_iterations, action='extend')
    parser.add_argument('-t', '--test_suites', nargs='+', help=help_test_suites, action='extend')

    # Optional arguments
    parser.add_argument('-ss', '--squash_server', nargs='?', choices=['prod', 'stg'], default='prod', help=help_squash_server)
    parser.add_argument('-e', '--environment', nargs='?', default='', help=help_environment)
    parser.add_argument('-b', '--browser', nargs='?', default='', help=help_browser)
    parser.add_argument('-d', '--device', nargs='?', default='', help=help_device)
    parser.add_argument('-pf', '--platform', nargs='?', default='', help=help_platform)
    parser.add_argument('-x', '--automation', nargs='?', choices=['robot', 'pytest'], default='robot', help=help_automation)
    parser.add_argument('-s', '--tests_directory', nargs='?', default='.', help=help_tests_directory)
    parser.add_argument('-cwd', '--cwd', nargs='?', default='.', help=help_cwd)
    parser.add_argument('-m', '--merge_output', nargs='?', default=False, type=lambda x: (str(x).lower() == 'true'), help=help_merge_output)
    parser.add_argument('-c', '--categories', nargs='*', default=[], help=help_categories, action='extend')
    parser.add_argument('-n', '--notCategories', nargs='*', default=[], help=help_not_categories, action='extend')
    parser.add_argument('-r', '--seleniumGridUrl', nargs='?', default='', help=help_selenium_grid_url)
    parser.add_argument('-v', '--variable', help=help_variable, nargs='?', default=[], action='append')
    parser.add_argument('-dev', '--developer_mode', nargs='?', default=False, type=lambda x: (str(x).lower() == 'true'), help=help_developer_mode)
    parser.add_argument('-f', '--test_status_filter', nargs='*', default=[], help=help_test_status_filter, action='extend')
    parser.add_argument('-a', '--automation_only', nargs='?', default=False, type=lambda x: (str(x).lower() == 'true'), help=help_automation_only)
    parser.add_argument('-p', '--pool_size', nargs='?', default=4, help=help_pool_size)
    parser.add_argument('-uskip', '--use_skipped_execution_status', nargs='?', default=False, type=lambda x: (str(x).lower() == 'true'), help=help_use_skipped_execution_status)
    parser.add_argument('-rft', '--rerun_failed_tests', nargs='?', default=0, help=help_rerun_failed_tests)
    parser.add_argument('-dpl', '--disable_post_log', nargs='?', default=False, type=lambda x: (str(x).lower() == 'true'), help=help_disable_post_log)
    parser.add_argument('-prr', '--pytest_robot_report', nargs='?', default=False, type=lambda x: (str(x).lower() == 'true'), help=help_pytest_robot_report)
    parser.add_argument('-phr', '--pytest_html_report', nargs='?', default=False, type=lambda x: (str(x).lower() == 'true'), help=help_pytest_html_report)

    args = parser.parse_args()

    if not args.iterations and not args.test_suites:
        parser.print_usage()
        print("SquashGlados: error: Either iterations or Test Suites is required: -i/--iterations or -t/--test_suites")
        exit(2)

    return args

if __name__ == "__main__":
    main()