package factory;

import com.microsoft.playwright.*;
import utils.ConfigReader;

import java.util.List;

public class DriverFactory {

    // ThreadLocal for parallel execution
    public static final ThreadLocal<Playwright> threadLocalPlaywright = new ThreadLocal<>();
    public static final ThreadLocal<Page> threadLocalDriver = new ThreadLocal<>();
    public static final ThreadLocal<BrowserContext> threadLocalContext = new ThreadLocal<>();
    public static final ThreadLocal<Browser> threadLocalBrowser = new ThreadLocal<>();

    /**
     * Create or return Playwright instance for the current thread
     */
    public static Playwright createPlaywright() {
        if (threadLocalPlaywright.get() == null) {
            threadLocalPlaywright.set(Playwright.create());
        }
        return threadLocalPlaywright.get();
    }

    /** Get Page for the current thread */
    public static Page getPage() {
        return threadLocalDriver.get();
    }

    /** Get BrowserContext for the current thread */
    public static BrowserContext getContext() {
        return threadLocalContext.get();
    }

    /** Get Browser for the current thread */
    public static Browser getBrowser() {
        return threadLocalBrowser.get();
    }

    /**
     * Initialize browser and page based on config
     * Thread-safe: Each thread gets its own Playwright, Browser, Context, and Page
     */
    public Page initDriver(String browserName) {
        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));
        int delay = Integer.parseInt(ConfigReader.getProperty("delay"));

        Playwright playwright = createPlaywright();
        Browser browser;

        switch (browserName.toLowerCase()) {
            case "firefox" -> browser = playwright.firefox()
                    .launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(delay));
            case "chrome", "chromium" -> browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setChannel("chromium")
                            .setHeadless(headless)
                            .setArgs(List.of("--start-maximized"))
                            .setSlowMo(delay));
            case "webkit" -> browser = playwright.webkit()
                    .launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(delay));
            default -> throw new IllegalArgumentException("Unsupported browser type: " + browserName);
        }

        // Store browser in ThreadLocal
        threadLocalBrowser.set(browser);

        // Create browser context
        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        // Start tracing
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(false));
        threadLocalContext.set(context);

        // Create Page
        Page page = context.newPage();
        threadLocalDriver.set(page);

        return page;
    }

    /** Clean up all resources for the current thread */
    public void quitDriver() {
        // Close the Page
        Page page = getPage();
        if (page != null) page.close();

        // Close the BrowserContext
        BrowserContext context = getContext();
        if (context != null) context.close();

        // Close the Browser
        Browser browser = getBrowser();
        if (browser != null) browser.close();

        // Close the Playwright instance
        Playwright playwright = threadLocalPlaywright.get();
        if (playwright != null) playwright.close();

        // Remove ThreadLocal references to avoid memory leaks
        threadLocalDriver.remove();
        threadLocalContext.remove();
        threadLocalBrowser.remove();
        threadLocalPlaywright.remove();
    }
}