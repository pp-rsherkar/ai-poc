package hooks;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ConfigReader;

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hooks {
    private static final Logger logger = Logger.getLogger(Hooks.class.getName());
    public DriverFactory driverFactory;
    public Page page;

//    @Before(value = "@e2e or @regression", order = 0)
//    public void runPythonSetup() {
//        logger.info("Starting Python Setup Script...");
//        PythonRunner.runPythonScript();
//    }

    @Before(value = "@e2e or @regression", order = 1)
    public void launchBrowser(Scenario scenario) {
        try {
            double timeout = Double.parseDouble(ConfigReader.getProperty("timeout"));
            String browserName = ConfigReader.getProperty("browser");  //Fetching browser value from config file
            driverFactory = new DriverFactory();
            page = driverFactory.initDriver(browserName); // Passing browser name to launch the browser
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
                String screenshotName = scenario.getName().replaceAll("\\s+", "_"); //Replace all space in scenario name with underscore
                byte[] sourcePath = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                scenario.attach(sourcePath, "image/png", screenshotName);  //Attach screenshot to report if scenario fails
                DriverFactory.getContext().tracing().stop(new Tracing.StopOptions().setPath(Paths.get("target/trace_" + scenario.getName().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9._-]", "_") + ".zip")));
            }
            catch (Exception e) {
                handleError("Error capturing screenshot or trace", e, scenario);
                throw new RuntimeException("Error during failure capture: ", e);
            }
        } else {
            // Stop tracing even if test passed
            try {
                DriverFactory.getContext().tracing().stop();
            } catch (Exception e) {
                logger.warning("Trace stop failed: " + e.getMessage());
            }
        }
    }

    // Method to handle and log errors globally
    private void handleError(String message, Exception e, Scenario scenario) {
        String errorDetails = message + ": " + e.getMessage();
        logger.log(Level.SEVERE, errorDetails, e);
        if (scenario != null) {
            scenario.log(errorDetails);
        }
    }
}