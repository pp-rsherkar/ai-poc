package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.WaitUtility;

public class BrandExplorerWorkspace {

    private final Page page;
    private final FrameLocator WORKSPACE_FRAME;
    private final Locator BRAND_EXPLORER_CHART;
    private final Locator BRAND_EXPLORER_TABLE;
    private final Locator SAVE_WORKSPACE;
    WaitUtility waitUtility;

    public BrandExplorerWorkspace(Page page) {
        this.page = page;
        this.waitUtility = new WaitUtility(page);
        this.WORKSPACE_FRAME = page.frameLocator("iframe").frameLocator("iframe");
        this.BRAND_EXPLORER_CHART = WORKSPACE_FRAME.locator("//div[@class='recharts-responsive-container']");
        this.BRAND_EXPLORER_TABLE = WORKSPACE_FRAME.locator("//div[contains(@class,'Box')]//table");
        this.SAVE_WORKSPACE = WORKSPACE_FRAME.locator(
                "//button[contains(@data-tour-id,'save-workspace-button')]//div[contains(text(),'Save')]");
    }

    public void waitForDashboardLoad() {
        waitUtility.waitForLocatorVisible(BRAND_EXPLORER_CHART);
        waitUtility.waitForLocatorVisible(BRAND_EXPLORER_TABLE);
    }

    public String getDefaultDimensions(String defaultDimension) {
        Locator locator = WORKSPACE_FRAME.locator(String.format(
                "//table//thead//th[@aria-selected='true']//p[normalize-space()='%s']", defaultDimension));
        waitUtility.waitForLocatorVisible(locator);
        System.out.println("Default Dimension: " + locator.innerText().trim());
        return locator.innerText().trim();
    }

    public String getDefaultMetrics(String defaultMetric) {
        Locator locator = WORKSPACE_FRAME.locator(
                String.format("//table//thead//th[@aria-selected='true']//p[normalize-space()='%s']", defaultMetric));
        waitUtility.waitForLocatorVisible(locator);
        System.out.println("Default Metric: " + locator.innerText().trim());
        return locator.innerText().trim();
    }

    public String getDefaultTimeFrame() {
        Locator locator = WORKSPACE_FRAME.locator("//div[.//p[normalize-space()='Time Frame']]//input[starts-with(@id,'listbox-input-')]");
        waitUtility.waitForLocatorVisible(locator);
        System.out.println("Default TimeFrame: " + locator.innerText().trim());
        return locator.inputValue().trim();
    }

    public void saveBrandExplorerWorkspace() {
        waitForDashboardLoad();
        SAVE_WORKSPACE.first().click();
    }
}
