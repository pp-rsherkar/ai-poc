package factory;

import com.microsoft.playwright.*;
import utils.WebActions;

public class DriverFactory {
    public static BrowserContext context;
    public static Page page;
    public static ThreadLocal<Page> threadLocalDriver = new ThreadLocal<>(); //For Parallel execution
    public static ThreadLocal<BrowserContext> threadLocalContext = new ThreadLocal<>();
    private static Playwright playwright;
    public Browser browser;

    public static Playwright createPlaywright() {
        if (playwright == null) {
            playwright = Playwright.create();
        }
        return playwright;
    }

    public static synchronized Page getPage() {
        return threadLocalDriver.get(); // Will return Initialized Thread Local Driver
    }

    public static synchronized BrowserContext getContext() {
        return threadLocalContext.get(); // Will return Initialized Thread Local Context
    }

    //Launches Browser as set by user in config file
    public Page initDriver(String browserName) {
        BrowserType browserType = null;
        boolean headless = Boolean.parseBoolean(WebActions.getProperty("headless"));
        switch (browserName) {
            case "firefox":
                browserType = Playwright.create().firefox();
                browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(1000));
                break;
            case "chrome":
                browserType = Playwright.create().chromium();
                browser = browserType.launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(headless).setSlowMo(2000));
                break;
            case "webkit":
                browserType = Playwright.create().webkit();
                browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(1000));
                break;
        }
        if (browserType == null) throw new IllegalArgumentException("Could not Launch Browser for type" + browserType);
        context = browser.newContext();
        //Below line is used to start the trace file
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(false));
        page = context.newPage();
        threadLocalDriver.set(page);
        threadLocalContext.set(context);
        return page;
    }
}