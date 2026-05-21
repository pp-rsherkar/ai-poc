package hooks;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import factory.DriverFactory;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigReader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import io.cucumber.core.internal.com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    public DriverFactory driverFactory;
    public Page page;
    private static String featureStatus = "SUCCESS";
    private static String squashSteps = "";

    @BeforeAll
    public static void beforeAllScenarios() {
        featureStatus = "SUCCESS"; // Reset feature status before running all scenarios
        logger.info("Starting feature file execution. Feature status reset to SUCCESS");
        String squash_server = "prod";
        String execution_id = System.getProperty("executionId", "0");
        if (execution_id.equals("0")) {
            logger.warn("No executionId found in system properties. Squash execution update will be skipped.");
            return;
        }
        // If I already have the executionId, I can get the executionSteps and use that to try to guess which executionStep in Squash matches each scenario, and then update the step status in the @After hook for each scenario instead of just updating the execution status at the end of the feature file execution
        // executionSteps = getExecutionSteps(executionId);  // squashGetSteps command will return a JSON string with a list of execution steps and their details, including the step name, so I can try to match the scenario name to the step name to find the corresponding stepId for each scenario, and then use that stepId to update the step status in Squash for each scenario
        String py_command = ConfigReader.getProperty("squashGetSteps");
        py_command = py_command.replace("__SQUASH_SERVER__", squash_server)
                                .replace("__EXECUTION_ID__", execution_id);
        squashSteps = runSquashCommand(py_command);
        logger.info("Squash command output: {}", squashSteps);
        // TODO - parse the squashSteps JSON output and store the step details in a way that can be accessed in the @After hook for each scenario to update the corresponding step status in Squash

    }

    @Before(value = "@e2e or @regression")
    public void launchBrowser(Scenario scenario) {
        try {
            // Clean old traces before starting new scenario
            cleanOldTracesKeepTodayAndYesterday();
            double timeout = Double.parseDouble(ConfigReader.getProperty("timeout"));
            String browserName = ConfigReader.getProperty("browser"); //Fetching browser value from config file
            String remote = ConfigReader.getProperty("remote");
            logger.info("Launching browser: {} with timeout: {} and remote: {}", browserName, timeout, remote);
            driverFactory = new DriverFactory();
            page = driverFactory.initDriver(browserName, remote); // Passing browser name and remote URL to launch the browser
            page.setDefaultTimeout(timeout);
        } catch (Exception e) {
            handleError("Error during browser launch", e, scenario);
            throw e; // Failing scenario explicitly
        }
    }

    //After runs in reverse order so order=1 will run first
    @After(value = "@e2e or @regression", order = 0)
    public void quitBrowser(Scenario scenario) {
        try {
            logger.info("Quitting browser after scenario: {}", scenario.getName());
            if (page != null) page.close();
            if (DriverFactory.getContext() != null) DriverFactory.getContext().close(); // Close context
            if (DriverFactory.getBrowser() != null) DriverFactory.getBrowser().close(); // Close browser
        } catch (Exception e) {
            handleError("Error during browser cleanup", e, scenario);
            throw new RuntimeException("Error during browser cleanup: ", e);
        }
    }

    @After(value = "@e2e or @regression", order = 1)
    public void takeScreenshotAndTrace(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                logger.info("Taking screenshot for failed scenario: {}", scenario.getName());
                String screenshotName = scenario.getName().replaceAll("\\s+", "_");
                byte[] sourcePath = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                scenario.attach(sourcePath, "image/png", screenshotName);  //Attach screenshot to report if scenario fails
                Path tracePath = Paths.get("target/trace_" + scenario.getName().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9._-]", "_") + ".zip");
                // Delete existing file (ensures overwrite)
                Files.deleteIfExists(tracePath);
                DriverFactory.getContext().tracing().stop(new Tracing.StopOptions().setPath(tracePath));
            } catch (Exception e) {
                handleError("Error capturing screenshot or trace", e, scenario);
                throw new RuntimeException(e);
            }
        } else {
            // Stop tracing even if test passed
            try {
                logger.info("Stopping trace for passed scenario: {}", scenario.getName());
                DriverFactory.getContext().tracing().stop();
            } catch (Exception e) {
                logger.warn("Trace stop failed", e);
            }
        }
    }

    /* get the test status (pass/fail) and update the Squash Execution */
    @After(value = "@e2e or @regression", order = 2)
    public void updateSquashStep(Scenario scenario) {
        String execution_status = "SUCCESS";
        if (scenario.isFailed()) {    
            execution_status = "FAILURE";
            featureStatus = "FAILURE"; // Update feature status to FAILURE if any scenario fails
        }
        //execution_status = "RUNNING";
        String squash_comment = "Automated test execution completed for scenario: " + scenario.getName() + " with status: " + execution_status;
        String execution_id = System.getProperty("executionId", "0"); // Get executionId from system property, default to "0" if not set
        if (execution_id.equals("0")) {
            logger.warn("No executionId found in system properties. Squash execution update will be skipped for scenario: {}", scenario.getName());
            return;
        }
        // TODO - use scenario name and squashSteps details to find the corresponding stepId in Squash for this scenario, and then update the step status in Squash using the squashStep command instead of updating the execution status for the entire execution in the @AfterAll hook, which will give more granular visibility into which scenarios passed/failed in Squash
        logger.info("Updating Squash execution with ID {} to status {}", execution_id, execution_status);
        String squash_server = "prod"; // TODO - get squash_server value from config or system property (change to just update the step)
        //String py_command = "python -c \"import SquashGlados; sg = SquashGlados.SquashGlados('" + squash_server + "'); sg.update_execution(" + execution_id + ", execution_status='" + execution_status + "', comment='" + squash_comment + "', squash_server='" + squash_server + "');\" ";
        String py_command = ConfigReader.getProperty("squashStep"); // squashStep
        // can I get the scenario parameters
        Integer stepId = getSquashStepForScenario(scenario.getName());
        // need to replace substrings in py_command: '__SQUASH_SERVER__', '__EXECUTION_ID__', '__EXECUTION_STATUS__', and '__SQUASH_COMMENT__'
        py_command = py_command.replace("__SQUASH_SERVER__", squash_server)
                                .replace("__STEP_ID__", stepId.toString())
                                .replace("__EXECUTION_STATUS__", execution_status);
                                // .replace("__SQUASH_COMMENT__", squash_comment);
        String squashOutput = runSquashCommand(py_command);
        logger.info("Squash command output: {}", squashOutput);
    }

    @AfterAll()
    public static void globalTearDown() {
        logger.info("All scenarios completed. Final feature status: {}", featureStatus);
        // Use featureStatus here for final reporting - it will be SUCCESS if all scenarios passed, FAILURE if any failed
        
        // Update Squash execution with the overall feature file status
        String execution_status = featureStatus; // Use the overall feature status for the Squash execution
        String squash_comment = "Automated test execution completed for feature file with status: " + execution_status;
        String execution_id = System.getProperty("executionId", "0"); // Get executionId from system property, default to "0" if not set
        if (execution_id.equals("0")) {
            logger.warn("No executionId found in system properties. Squash execution update will be skipped for feature file.");
            return;
        }
        String squash_server = "prod"; // TODO - get squash_server value from config or system property
        //String py_command = "python -c \"import SquashGlados; sg = SquashGlados.SquashGlados('" + squash_server + "'); sg.update_execution(" + execution_id + ", execution_status='" + execution_status + "', comment='" + squash_comment + "', squash_server='" + squash_server + "');\" ";
        String py_command = ConfigReader.getProperty("squashExecution");
        // need to replace substrings in py_command: '__SQUASH_SERVER__', '__EXECUTION_ID__', '__EXECUTION_STATUS__', and '__SQUASH_COMMENT__'
        py_command = py_command.replace("__SQUASH_SERVER__", squash_server)
                                .replace("__EXECUTION_ID__", execution_id)
                                .replace("__EXECUTION_STATUS__", execution_status)
                                .replace("__SQUASH_COMMENT__", squash_comment); 
        String squashOutput = runSquashCommand(py_command);
        logger.info("Squash command output: {}", squashOutput);
    }

    // Method to handle and log errors globally
    private void handleError(String message, Exception e, Scenario scenario) {
        logger.error(message, e);
        if (scenario != null) {
            scenario.log(message + ": " + e.getMessage());
        }
    }

    // Clean up old trace files before starting new tests
    private void cleanOldTracesKeepTodayAndYesterday() {
        try {
            File targetDir = new File("target");
            if (targetDir.exists() && targetDir.isDirectory()) {
                File[] files = targetDir.listFiles((dir, name) -> name.startsWith("trace_") && name.endsWith(".zip"));
                if (files != null) {
                    LocalDate today = LocalDate.now();
                    LocalDate yesterday = today.minusDays(1);
                    for (File file : files) {
                        LocalDate fileDate = Files.getLastModifiedTime(file.toPath()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        if (!(fileDate.equals(today) || fileDate.equals(yesterday))) {
                            Files.deleteIfExists(file.toPath());
                            logger.info("Deleted old trace: {}", file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to clean old traces", e);
        }
    }

    // add a method that does ProcessBuilder given a command string, and use that method in the @After hook to update the Squash execution status for each scenario, and in the @AfterAll hook to update the Squash execution status for the entire feature file based on the overall featureStatus (if any scenario failed, featureStatus will be FAILURE, otherwise it will be SUCCESS)
    private static String runSquashCommand(String py_command) {
        try {
            logger.info("Running Squash command: {}", py_command);
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                processBuilder.command("cmd.exe", "/c", py_command);
            } else {
                processBuilder.command("sh", "-c", py_command);
            }
            Process process = processBuilder.start();
            String result = new String(process.getInputStream().readAllBytes());
            // int exitCode = process.waitFor();
            process.waitFor();
            logger.info("Squash command output: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Error updating Squash execution with command: " + py_command, e);
            return e.getMessage();
        }
    }

    private static Integer getSquashStepForScenario(String scenarioName) {
        // TODO - parse the squashSteps JSON to find the step details for the given scenario name, and return the stepId or other relevant details needed to update the step status in Squash
        Integer stepId = -1;
        logger.info("Finding Squash step for scenario: {}", scenarioName);
        // parse squashSteps is a String that contains JSON
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> stepsList = objectMapper.readValue(squashSteps, new TypeReference<List<Map<String, Object>>>() {});
            for (Map<String, Object> step : stepsList) {
                stepId = (Integer) step.get("step_id");
                String executionStatus = (String) step.get("execution_status");
                String action = (String) step.get("action");
                // String comment = (String) step.get("comment"); // This can be null
                logger.info("Checking step ID {} with action: {}", stepId, action);
                
                // Do something with the step data
                System.out.println("Step ID: " + stepId + ", Status: " + executionStatus);
                // I want to search for the scenarioName in the action, but first split action by "<br>",
                // get the first line of the action, it might contain Cucumber variables like "<FILE_NAME>", so I need to replace those with ".*" to create a regex pattern, and then check if the scenarioName matches that pattern, if it does, then also get the variable and see if the variable in the scenarioName is found later in the action, which will help confirm that this is the correct step for this scenario, and then return the stepId for that step so I can update the step status in Squash for this specific step instead of just updating the execution status for the entire execution in the @AfterAll hook
                String[] actionLines = action.split("<br>");
                String firstLine = actionLines[0];
                String actionWoFirstLine = action.replace(firstLine, "");
                logger.info("First line of step action: {}", firstLine);
                // remove regex match "<span.*?</span>" from firstLine
                firstLine = firstLine.replaceAll("<span.*?</span>", "");
                logger.info("First line of step action after removing span tags: {}", firstLine);
                // Replace Cucumber variables with regex patterns
                String firstLine_regex = firstLine.replaceAll("<[^>]+>", "(.*)");
                if (scenarioName.matches(firstLine_regex)) {
                    logger.info("Scenario name matches step action pattern: {}", firstLine);
                    // stepId = (Integer) step.get("step_id");
                    List<String> scenarioParams = new ArrayList<>();
                    Pattern p = Pattern.compile("\"<(.*?)>\"");
                    Matcher m = p.matcher(firstLine);
                    // TODO: I want the substring in scenarioName that corresponds to the <parameter> in the firstLine, so I can check if that substring is found in the rest of the action (actionWoFirstLine) to help confirm that this is the correct step for this scenario, since there might be multiple steps with similar action patterns but different parameters, so I need to check both the pattern match and the parameter value to be more confident in matching the scenario to the correct step in Squash
                    while (m.find()) {
                        String param = m.group(1);
                        scenarioParams.add(param); // group 1 = content between < and >
                        logger.info("Found scenario parameter: {}", param);
                    }
                    // --------
                    List<String> scenarioData = new ArrayList<>();
                    Pattern p2 = Pattern.compile(firstLine_regex);
                    Matcher m2 = p2.matcher(scenarioName);
                    while (m2.find()) {
                        for (int i = 1; i <= m2.groupCount(); i++) {
                            String paramValue = m2.group(i);
                            scenarioData.add(paramValue);
                            logger.info("Found scenario parameter value: {}", paramValue);
                        }
                    }
                    // --------
                    Boolean allParamsFound = true;
                    for (String param : scenarioData) {
                        // check if param value is found in the action
                        if (actionWoFirstLine.contains(param)) {
                            logger.info("Scenario parameter {} found in step action", param);
                            // allParamsFound = true;
                            // stepId = (Integer) step.get("step_id");
                        } else {
                            allParamsFound = false;
                        }
                    }
                    if (allParamsFound) {
                        stepId = (Integer) step.get("step_id");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing Squash steps JSON", e);
        }
        return stepId;
    }
}
