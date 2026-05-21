pipeline {
    agent any
    environment {
        PROD_SQUASH_SECRET_API_TOKEN = credentials('prod_squash_secret_api_token')
        STG_SQUASH_SECRET_API_TOKEN = credentials('stg_squash_secret_api_token')
    }

    parameters {
        choice(name: 'test_id_type', choices: ['iteration', 'test-suite'], description: 'Select which type of Squash IDs you are providing')
        string(name: 'IDS', description: 'Enter iteration/test-suite IDs separated by commas', defaultValue: '19425') 
        string(name: 'BRANCH', description: 'Enter Git branch to run tests from', defaultValue: 'QA-1192')
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
                
                checkout scmGit(branches: [[name: params.BRANCH]], extensions: [], userRemoteConfigs: [[credentialsId: '23c71168-01de-4537-ae73-62fb01c818ad', url: 'https://github.com/pulsepointinc/qa-automation.git']])
                
                // Create and activate virtual environment
                bat '''
        "C:\\Users\\rsherkar\\AppData\\Local\\Programs\\Python\\Python314\\python.exe" -m venv .venv

        call .venv\\Scripts\\activate.bat

        python -m pip install --upgrade pip

        pip install -r requirements.txt

        pip list
        '''
            }
        }

        stage('Generate Config') {
            steps {
                script {
                    // Prepare data for config.json
                    def config = [
                        'automation': 'mvn',
                        'tests_directory': 'src/test/resources/features',
                        'squash_server': params.Squash_Server ? params.Squash_Server : 'stg',    // 'stg' or 'prod',
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
