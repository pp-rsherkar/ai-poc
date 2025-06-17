package hooks;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.ConfigReader;
import utils.WebActions;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Hooks {
    public DriverFactory driverFactory;
    public Page page;

    @Before
    public void launchBrowser() {
        String browserName = ConfigReader.getProperty("browser");  //Fetching browser value from config file
        driverFactory = new DriverFactory();
        page = driverFactory.initDriver(browserName); // Passing browser name to launch the browser
    }

    //After runs in reverse order so order=1 will run first
    @After(order = 0)
    public void quitBrowser() {
        try {
            if (page != null) {
                page.close();
            }
            if (DriverFactory.context != null) {
                DriverFactory.context.close(); // Close context
            }
            if (DriverFactory.browser != null) {
                DriverFactory.browser.close(); // Close browser
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during browser cleanup: ", e);
        }
    }

    @After(order = 1)
    public void takeScreenshotAndTrace(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                String screenshotName = scenario.getName().replaceAll("\\s+", "_"); //Replace all space in scenario name with underscore
                byte[] sourcePath = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                scenario.attach(sourcePath, "image/png", screenshotName);  //Attach screenshot to report if scenario fails
                DriverFactory.context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("target/" + screenshotName + ".zip")));
            } catch (Exception e) {
                throw new RuntimeException("Error during failure capture: ", e);
            }
        }
    }
}