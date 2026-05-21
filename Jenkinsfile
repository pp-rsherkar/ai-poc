pipeline {
    agent any
    environment {
        PROD_SQUASH_SECRET_API_TOKEN = credentials('prod_squash_secret_api_token')
        STG_SQUASH_SECRET_API_TOKEN = credentials('stg_squash_secret_api_token')
    }

    parameters {
        choice(name: 'test_id_type', choices: ['iteration', 'test-suite'], description: 'Select which type of Squash IDs you are providing')
        string(name: 'IDS', description: 'Enter iteration/test-suite IDs separated by commas', defaultValue: '') 
        string(name: 'BRANCH', description: 'Enter Git branch to run tests from', defaultValue: 'main')
        choice(name: 'ENVIRONMENT', choices: ['ike', 'stg1', 'stg2', 'stg3', 'prod'], description: 'Select the target environment')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'safari'], description: 'Select the browser')
        activeChoice(choiceType: 'PT_MULTI_SELECT', filterLength: 1, filterable: false, name: 'INCLUDE_CATEGORIES', script: groovyScript(fallbackScript: [classpath: [], oldScript: '', sandbox: true, script: ''], script: [classpath: [], oldScript: '', sandbox: true, script: '[\'Design/UI\', \'Smoke Test\', \'Library Case\', \'Release Test Only\', \'Compatibility\', \'Logic\', \'Performance\', \'Security\', \'Single Code Source\', \'SEO\', \'Tracking Code/Metrics\', \'User Scenarios\', \'No PreProd/Canary\', \'Revenue Impacting\', \'Build Verification\', \'Desktop Test\', \'Mobile Test\', \'Responsive Test\', \'Desktop Required\', \'Functional\', \'Sanity Test\', \'Staging Test Only\', \'Data/Content\', \'Light Regression\', \'Full Regression\'] ']) )
        activeChoice(choiceType: 'PT_MULTI_SELECT', filterLength: 1, filterable: false, name: 'EXCLUDE_CATEGORIES', script: groovyScript(fallbackScript: [classpath: [], oldScript: '', sandbox: true, script: ''], script: [classpath: [], oldScript: '', sandbox: true, script: '[\'Design/UI\', \'Smoke Test\', \'Library Case\', \'Release Test Only\', \'Compatibility\', \'Logic\', \'Performance\', \'Security\', \'Single Code Source\', \'SEO\', \'Tracking Code/Metrics\', \'User Scenarios\', \'No PreProd/Canary\', \'Revenue Impacting\', \'Build Verification\', \'Desktop Test\', \'Mobile Test\', \'Responsive Test\', \'Desktop Required\', \'Functional\', \'Sanity Test\', \'Staging Test Only\', \'Data/Content\', \'Light Regression\', \'Full Regression\'] ']) )
        choice(name: 'GRID', choices: ['ws://stg-qa-selenium1.internetbrands.com:8090'], description: 'Select the Grid where the browser runs')
        string(name: 'ADDITIONAL_ARGS', description: 'Enter any additional arguments', defaultValue: '')
        activeChoice(choiceType: 'PT_MULTI_SELECT', filterLength: 1, filterable: false, name: 'STATUS_FILTER', script: groovyScript(fallbackScript: [classpath: [], oldScript: '', sandbox: true, script: ''], script: [classpath: [], oldScript: '', sandbox: true, script: '[\'READY\', \'RUNNING\', \'SUCCESS\', \'FAILURE\', \'BLOCKED\', \'SETTLED\', \'UNTESTABLE\', \'SKIPPED\', \'CANCELLED\'] ']) )
        booleanParam(name: 'developer_mode', description: 'developer mode allows you to run tests with Automation Test Name and Automation Suite Name (even if the Automation Type has not been updated yet)', defaultValue: false)
        booleanParam(name: 'automation_only_mode', description: 'automation_only_mode will only run tests if the most recent execution was run by the apiuser', defaultValue: false)
        booleanParam(name: 'useSkippedFiltered', description: 'check this if you want an execution to have skipped status for the filtered cases', defaultValue: false)
        string(name: 'pool', description: 'how many tests do you want to run at the same time?', defaultValue: '4')
        string(name: 'rerun_failed_tests', description: 'If you want to rerun failed tests, how many times do you want to rerun failures?', defaultValue: '0')
        activeChoice( choiceType: 'PT_MULTI_SELECT', filterLength: 1, filterable: false, name: 'RERUN_STATUSES', description: 'If you rerun failed tests, which statuses do you want to rerun?', script: groovyScript( fallbackScript: [classpath: [], oldScript: '', sandbox: true, script: ''], script: [ classpath: [], oldScript: '', sandbox: true, script: '[\'READY\', \'RUNNING\', \'SUCCESS\', \'FAILURE:selected\', \'BLOCKED:selected\', \'SETTLED\', \'UNTESTABLE\', \'SKIPPED\', \'CANCELLED\'] ']) )
        choice(name: 'Squash_Server', choices: ['prod', 'stg'], description: 'Select the Squash environment')

    }

    stages {
        stage('Prepare Environment') {
            steps {
                script{
                    if (env.IDS == null || env.IDS.isEmpty()) {
                        currentBuild.result = 'ABORTED'
                        error("No iteration or test-suite IDs provided.")
                    }
                }
                
                checkout scmGit(branches: [[name: params.BRANCH]], extensions: [], userRemoteConfigs: [[credentialsId: 'GitQatestUser', url: 'git@git.internetbrands.com:QAAutomation/pulsepoint_automation.git']])
                
                // Create and activate virtual environment
                sh '''
                python3 -m venv ./.venv
                source ./.venv/bin/activate
                python3 -m pip install --upgrade pip 
                python3 -m pip install -r requirements.txt
                python3 -m pip list
                '''
            }
        }

        stage('Generate Config') {
            steps {
                script {
                    // Prepare data for config.json
                    def config = [
                        'environment': params.ENVIRONMENT,
                        'browser': params.BROWSER,
                        'automation': 'mvn',
                        'tests_directory': 'src/test/resources/features',
                        'categories': params.INCLUDE_CATEGORIES ? params.INCLUDE_CATEGORIES.split(',') : [],
                        'notCategories': params.EXCLUDE_CATEGORIES ? params.EXCLUDE_CATEGORIES.split(',') : [],
                        'grid': params.GRID,
                        'additionalArgs': params.ADDITIONAL_ARGS,
                        'developer_mode': params.developer_mode,
                        'test_status_filter': params.STATUS_FILTER ? params.STATUS_FILTER.split(',') : [],
                        'automation_only_mode': params.automation_only_mode,
                        'use_skipped_execution_status': params.useSkippedFiltered,
                        'pool_size': params.pool,
                        'squash_server': params.Squash_Server ? params.Squash_Server : 'stg',    // 'stg' or 'prod',
                        'rerun_failed_tests': params.rerun_failed_tests,
                        'rerun_statuses': params.RERUN_STATUSES ? params.RERUN_STATUSES.split(',') : [],
                        'build_url': env.BUILD_URL + 'execution/node/3/ws/'
                    ]
                    if (params.test_id_type == 'test-suite') {
                        config['test_suites'] = params.IDS.split(',')
                    } else if (params.test_id_type == 'iteration') {
                        config['iterations'] = params.IDS.split(',')
                    }

                    // Write config to JSON file
                    writeJSON file: 'config.json', json: config

                    // Print config.json
                    def cleanConfig = readJSON file: 'config.json'
                    def formattedConfig = groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(cleanConfig))
                    echo formattedConfig
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh '''
                    source ./.venv/bin/activate
                    # Create an exec-capable temp directory inside the workspace for Playwright/Java
                    TMP_DIR="$PWD/tmp/playwright-$(date +%s%N)"
                    mkdir -p "$TMP_DIR"
                    chmod 0777 "$TMP_DIR"
                    export TMPDIR="$TMP_DIR"
                    # Ensure Java uses the same temp directory (preserve existing options)
                    export JAVA_TOOL_OPTIONS="-Djava.io.tmpdir=$TMP_DIR ${JAVA_TOOL_OPTIONS:-}"
                    # Run the test runner
                    python3 -m SquashGlados config.json
                    # Cleanup the temporary directory when finished
                    rm -rf "$TMP_DIR"
                    '''
                }
            }
        }

    }

}
