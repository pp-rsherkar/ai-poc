package factory;

import com.microsoft.playwright.*;
import utils.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class DriverFactory {
    public static ThreadLocal<Page> threadLocalDriver = new ThreadLocal<>(); //For Parallel execution
    public static ThreadLocal<BrowserContext> threadLocalContext = new ThreadLocal<>();
    public static ThreadLocal<Browser> threadLocalBrowser = new ThreadLocal<>();
    private static Playwright playwright;
    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);

    public static synchronized Playwright createPlaywright() {
        if (playwright == null) {
            playwright = Playwright.create();
        }
        return playwright;
    }

    public static synchronized Page getPage() {
        return threadLocalDriver.get(); // Will return Initialized Thread Local Driver
    }

    public static synchronized BrowserContext getContext() {
        return threadLocalContext.get();
    }

    public static synchronized Browser getBrowser() {
        return threadLocalBrowser.get();
    }

    //Launches Browser as set by user in config file
    public Page initDriver(String browserName, String remote) {
        BrowserType browserType = null;
        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));
        int delay = Integer.parseInt(ConfigReader.getProperty("delay"));
        playwright = createPlaywright();
        Browser browser = null;
        switch (browserName) {
            case "firefox":
                browserType = playwright.firefox();
                if (remote != null && !remote.trim().isEmpty()) {
                    logger.info("Connecting to remote Firefox browser at: {}", remote);
                    try {
                        browser = browserType.connect(remote);
                        logger.info("Successfully connected to remote Firefox browser");
                    } catch (Exception e) {
                        logger.error("Failed to connect to remote Firefox browser at {}: {}", remote, e.getMessage());
                        logger.error("Falling back to local Firefox browser launch");
                        browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(delay));
                    }
                } else {
                    browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(delay));
                }
                break;
            case "chrome":
                browserType = playwright.chromium();
                if (remote != null && !remote.trim().isEmpty()) {
                    logger.info("Attempting to connect to remote Playwright server at: {}", remote);
                    try {
                        // Enable Playwright debug logging
                        System.setProperty("playwright.debug", "true");
                        browser = browserType.connect(remote);    // java.lang.NullPointerException ???
                        logger.info("Successfully connected to remote Playwright server");
                    } catch (Exception e) {
                        logger.error("Failed to connect to remote server at {}: {}", remote, e.getMessage());
                        throw e;
                        // logger.error("Exception type: {}", e.getClass().getName());
                        // logger.error("Stack trace:");
                        // e.printStackTrace();
                        // if (e.getCause() != null) {
                        //     logger.error("Root cause: {}", e.getCause().getMessage());
                        //     logger.error("Root cause type: {}", e.getCause().getClass().getName());
                        // }
                        // ------------------------------
                    }
                } else {
                    browser = browserType.launch(new BrowserType.LaunchOptions().setChannel("chromium").setHeadless(headless).setArgs(List.of("--start-maximized")).setSlowMo(delay));
                }
                break;
            case "webkit":
                browserType = playwright.webkit();
                if (remote != null && !remote.trim().isEmpty()) {
                    logger.info("Connecting to remote WebKit browser at: {}", remote);
                    try {
                        browser = browserType.connect(remote);
                        logger.info("Successfully connected to remote WebKit browser");
                    } catch (Exception e) {
                        logger.error("Failed to connect to remote WebKit browser at {}: {}", remote, e.getMessage());
                        logger.error("Falling back to local WebKit browser launch");
                        browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(delay));
                    }
                } else {
                    browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(delay));
                }
                break;
        }
        if (null == browserType) throw new IllegalArgumentException("Could not Launch Browser for type" + browserName);
        threadLocalBrowser.set(browser);
        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        //Below line is used to start the trace file
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(false));
        Page page = context.newPage();
        threadLocalDriver.set(page);
        threadLocalContext.set(context);
        return page;
    }
}