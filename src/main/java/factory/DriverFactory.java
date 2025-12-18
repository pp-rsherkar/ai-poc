package factory;

import com.microsoft.playwright.*;
import utils.ConfigReader;

import java.util.List;

public class DriverFactory {
    public static BrowserContext context;
    public static Page page;
    public static ThreadLocal<Page> threadLocalDriver = new ThreadLocal<>(); //For Parallel execution
    public static ThreadLocal<BrowserContext> threadLocalContext = new ThreadLocal<>();
    public static ThreadLocal<Browser> threadLocalBrowser = new ThreadLocal<>();
    public static Browser browser;
    private static Playwright playwright;

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
        return threadLocalContext.get();
    }

    public static synchronized Browser getBrowser() {
        return threadLocalBrowser.get();
    }

    //Launches Browser as set by user in config file
    public Page initDriver(String browserName) {
        BrowserType browserType = null;
        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));
        int delay = Integer.parseInt(ConfigReader.getProperty("delay"));
        playwright = createPlaywright();
        switch (browserName) {
            case "firefox":
                browserType = playwright.firefox();
                browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(delay));
                break;
            case "chrome":
                browserType = playwright.chromium();
                browser = browserType.launch(new BrowserType.LaunchOptions().setChannel("chromium").setHeadless(headless).setArgs(List.of("--start-maximized")).setSlowMo(delay));
                break;
            case "webkit":
                browserType = playwright.webkit();
                browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(delay));
                break;
        }
        if (null == browserType) throw new IllegalArgumentException("Could not Launch Browser for type" + browserName);
        threadLocalBrowser.set(browser);
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        //Below line is used to start the trace file
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(false));
        page = context.newPage();
        threadLocalDriver.set(page);
        threadLocalContext.set(context);
        return page;
    }
}