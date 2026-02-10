package hooks;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ConfigReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hooks {
    private static final Logger logger = Logger.getLogger(Hooks.class.getName());
    private static final long MAX_VIDEO_SIZE = 50 * 1024 * 1024; // 50MB
    public DriverFactory driverFactory;
    public Page page;

    @Before(value = "@e2e or @regression")
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

    // After runs in reverse order so order = -1 runs LAST
    @After(value = "@e2e or @regression", order = -1)
    public void renameAndAttachVideo(Scenario scenario) {
        if (page.video() == null) {
            return;
        }
        try {
            Path videoPath = page.video().path();
            if (!Files.exists(videoPath)) {
                return;
            }
            String scenarioName = "Video - " + scenario.getName().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9._-]", "_");
            if (scenario.isFailed()) {
                Path renamed = videoPath.getParent().resolve(scenarioName + ".webm");
                Files.move(videoPath, renamed, StandardCopyOption.REPLACE_EXISTING);
                long videoSize = Files.size(renamed);
                if (videoSize > MAX_VIDEO_SIZE) {
                    scenario.attach(("Video file too large (" + videoSize + " bytes). See: " + renamed.toAbsolutePath()).getBytes(), "text/plain", scenarioName);
                } else {
                    scenario.attach(Files.readAllBytes(renamed), "video/webm", scenarioName);
                }
                Files.deleteIfExists(renamed);
            } else {
                Files.deleteIfExists(videoPath);
            }
        } catch (Exception e) {
            logger.warning("Video handling failed: " + e);
        }
    }


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
                String screenshotName = "Screenshot - " + scenario.getName().replaceAll("\\s+", "_"); //Replace all space in scenario name with underscore
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