package factory;

import com.microsoft.playwright.*;
import utils.WebActions;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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
        Path userDataDir = Paths.get("C:\\Users\\nparab\\AppData\\Local\\ms-playwright\\chromium-1134\\chrome-win\\").toAbsolutePath();
        BrowserType browserType = null;
        boolean headless = Boolean.parseBoolean(WebActions.getProperty("headless"));
        switch (browserName) {
            case "firefox":
                browserType = Playwright.create().firefox();
                browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            case "chrome":
                browserType = Playwright.create().chromium();
                browser = browserType.launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(headless));
                //browser = browserType.launchPersistentContext(userDataDir,new BrowserType.LaunchPersistentContextOptions().setHeadless(headless));
                break;
            case "webkit":
                browserType = Playwright.create().webkit();
                browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
        }
        if (browserType == null) throw new IllegalArgumentException("Could not Launch Browser for type" + browserType);
        //context = browser.newContext();
        BrowserContext context = browserType.launchPersistentContext(userDataDir, new BrowserType.LaunchPersistentContextOptions().setHeadless(headless).setViewportSize(1366,607));
        //Below line is used to start the trace file
        //context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(false));
        page = context.newPage();
//        int width = (int) page.evaluate("window.innerWidth");
//        int height = (int) page.evaluate("window.innerHeight");
//        System.out.println("Dynamic Viewport Size: " + width + " x " + height);
//        page.setViewportSize(width,height);

        //BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(width,height));

        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(false));

        // Print the viewport size
       // System.out.println("Viewport Size: " + width + "x" + height);
        //page = context.pages().get(0);

        threadLocalDriver.set(page);
        threadLocalContext.set(context);
        return page;

    }
}