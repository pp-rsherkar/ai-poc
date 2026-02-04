import pytest
import requests
import json
import string
import random
from _pytest.runner import runtestprotocol
from SquashGlados import execution_updater

def pytest_addoption(parser):
    parser.addoption("--execution_id", action="store", help="Squash execution id")
    parser.addoption("--env", action="store", default="prod", help="Testing environment")               # is the environment 'env' or 'environment'???
    parser.addoption("--environment", action="store", default="prod", help="Testing environment")
    parser.addoption("--squash_server", action="store", default="prod", help="Squash server prod or stg")
    parser.addoption("--robot-outputdir", action="store", default="tests/logs", help="Directory to save robot log files")
    parser.addoption("--browser", action="store", default="chrome", help="Which browser to use")
    parser.addoption("--device", action="store", default="", help="Which device to use")
    parser.addoption("--jenkins_url", action="store", default="", help="The Jenkins URL")
    parser.addoption("--workspace_url", action="store", default="", help="The Jenkins Workspace URL")
    parser.addoption("--remote", action="store", default="", help="The remote selenium grid")
    parser.addoption("--output_dir", action="store", default="", help="The output directory")

@pytest.fixture
def browser(request):
    return request.config.getoption("--browser")

@pytest.fixture
def execution_id(request):
    """Fixture to get the execution ID from command line options."""
    return request.config.getoption("--execution_id")

def pytest_runtest_setup(item):
    cmd_env = item.config.getoption('--env')
    env_names = item.get_closest_marker(name='env')
    list_of_env_matches = {'prod': ['prod', 'preprd2', 'preprd3', 'preprd4'],
        'stg': ['stg1', 'stg2', 'stg3', 'stg4', 'stg5', 'stg6', 'stg7', 'stg8', 'stg9', 'stg10', 'qa1']}
    if env_names:
        matches_env = False
        for env_name in env_names.args:
            if cmd_env in list_of_env_matches[env_name]:
                matches_env = True
                break
        if not matches_env:
            pytest.skip("Test requires environments in %r" % env_names)

@pytest.fixture(scope='class')
def base_setup(request):
    '''https://testrail.internetbrands.com/testrail/index.php?/cases/view/19022947'''
    base = dict()
    base['env'] = request.config.getoption('--env')
    if 'stg' in base['env'] or 'qa' in base['env']:
        base['url'] = 'http://profile-services.' + base['env'] + '.lawyers.com'
    elif 'preprd' in base['env']:
        base['url'] = 'http://profile-services' + base['env'].replace('preprd', '') + '.lawyers.com'
    else:
        base['url'] = 'http://profile-services.lawyers.com'

    if request.cls is not None:
        request.cls.base = base
    request.cls.base['squash_comments'] = []
    return base

def pytest_runtest_protocol(item, nextitem):
    reports = runtestprotocol(item, nextitem=nextitem)
    squash_api = execution_updater.ExecutionUpdater()
    for report in reports:
        if not hasattr(item.cls, 'base'):
            continue

        if 'elapsed_time' not in item.cls.base:
            item.cls.base['elapsed_time_msg'] = str(report.when) + ": " + str(report.duration)
            item.cls.base['elapsed_time'] = report.duration
        else:
            item.cls.base['elapsed_time_msg'] += " + " + str(report.when) + ": " + str(report.duration)
            item.cls.base['elapsed_time'] += report.duration

        
        if report.when == 'setup':
            if report.outcome == 'failed':
                item.cls.base['test_status'] = 'BLOCKED'            # Change retest to blocked here???
                item.cls.base['squash_comments'].append("The setup failed.")
        if report.when == 'call':
            if report.outcome == 'failed':
                item.cls.base['test_status'] = 'FAILURE'            # Squash status FAILURE
                item.cls.base['squash_comments'].append("The test " + item.name + " failed.")
                item.cls.base['squash_comments'].append("ERROR: " + str(report.longrepr))
        if report.when == 'teardown':
            # tr_status_dict = tr_api.get_status_dictionary()
            item.cls.base['elapsed_time_msg'] += " = total: " + str(item.cls.base['elapsed_time'])
            # ---------------------------------------------------------------------------------
            print("item attributes: " + str(item.__dict__))
            if hasattr(item,'test_status') and item.test_status == 'BLOCKED':
                item.cls.base['squash_comments'].append("The test " + item.name + " is blocked.")
                item.cls.base['elapsed_time_msg'] += " = total: " + str(item.cls.base['elapsed_time'])
                print(item.cls.base['squash_comments'])
                print("Elapsed time: " + str(item.cls.base['elapsed_time_msg']))
                execution_id = item.config.getoption("--execution_id")
                squash_api.update_execution(execution_id, item.test_status, item.cls.base['squash_comments'])   #, squash_server=item.config.getoption("--squash_server"))
                return True
            elif hasattr(item,'test_status') and item.test_status == 'SETTLED':
                item.cls.base['squash_comments'].append("The test " + item.name + " is settled.")
                item.cls.base['elapsed_time_msg'] += " = total: " + str(item.cls.base['elapsed_time'])
                print(item.cls.base['squash_comments'])
                print("Elapsed time: " + str(item.cls.base['elapsed_time_msg']))
                execution_id = item.config.getoption("--execution_id")
                squash_api.update_execution(execution_id, item.test_status, item.cls.base['squash_comments'])   #, squash_server=item.config.getoption("--squash_server"))
                return True
            # ---------------------------------------------------------------------------------
            if not 'test_status' in item.cls.base or item.cls.base['test_status'] == 1:
                item.cls.base['squash_comments'].append("The test " + item.name + " passed.")
                item.cls.base['test_status'] = 'SUCCESS'            # Squash status SUCCESS
            print(item.cls.base['squash_comments'])
            item.cls.base['squash_comments'].append("Environment: " + str(item.cls.base['env']))
            print("Elapsed time: " + str(item.cls.base['elapsed_time_msg']))
            print(item)
            #update_testrail(item)
            execution_id = item.config.getoption("--execution_id")
            squash_api.update_execution(execution_id, item.cls.base['test_status'], item.cls.base['squash_comments'])    #, squash_server=item.config.getoption("--squash_server"))    # where is the execution_id saved?
    return True

def pytest_generate_tests(metafunc):
    option_value = metafunc.config.option.environment
    if 'environment' in metafunc.fixturenames and option_value is not None:
        metafunc.parametrize("environment", [option_value])
