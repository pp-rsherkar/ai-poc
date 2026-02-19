package hooks;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigReader;

import java.nio.file.Paths;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    public DriverFactory driverFactory;
    public Page page;

    @Before(value = "@e2e or @regression")
    public void launchBrowser(Scenario scenario) {
        try {
            double timeout = Double.parseDouble(ConfigReader.getProperty("timeout"));
            String browserName = ConfigReader.getProperty("browser"); //Fetching browser value from config file
            logger.info("Launching browser: {} with timeout: {}", browserName, timeout);
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
                DriverFactory.getContext().tracing().stop(new Tracing.StopOptions().setPath(Paths.get("target/trace_" + scenario.getName().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9._-]", "_") + ".zip")));
            }
            catch (Exception e) {
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

    // Method to handle and log errors globally
    private void handleError(String message, Exception e, Scenario scenario) {
        logger.error(message, e);
        if (scenario != null) {
            scenario.log(message + ": " + e.getMessage());
        }
    }
}
