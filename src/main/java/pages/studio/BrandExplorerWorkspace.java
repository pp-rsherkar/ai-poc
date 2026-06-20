package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import utils.WaitUtility;

public class BrandExplorerWorkspace {

    private final Page page;
    private final FrameLocator WORKSPACE_FRAME;
    private final Locator BRAND_EXPLORER_CHART;
    private final Locator BRAND_EXPLORER_TABLE;
    private final Locator SAVE_WORKSPACE;
    private final Locator DATE_RANGE_SELECTOR;
    WaitUtility waitUtility;

    public BrandExplorerWorkspace(Page page) {
        this.page = page;
        this.waitUtility = new WaitUtility(page);
        this.WORKSPACE_FRAME = this.page.frameLocator("iframe").frameLocator("iframe");
        this.BRAND_EXPLORER_CHART = WORKSPACE_FRAME.locator("//div[@class='recharts-responsive-container']");
        this.BRAND_EXPLORER_TABLE = WORKSPACE_FRAME.locator("//div[contains(@class,'Box')]//table");
        this.SAVE_WORKSPACE = WORKSPACE_FRAME.locator(
                "//button[contains(@data-tour-id,'save-workspace-button')]//div[contains(text(),'Save')]");
        this.DATE_RANGE_SELECTOR = WORKSPACE_FRAME.locator(
                "//p[normalize-space()='Time Frame']/following-sibling::div//input[starts-with(@id,'listbox-input-')]");
    }

    public void waitForDashboardLoad() {
        waitUtility.waitForLocatorVisible(BRAND_EXPLORER_CHART);
        waitUtility.waitForLocatorVisible(BRAND_EXPLORER_TABLE);
    }

    public String getDefaultDimensions(String defaultDimension) {
        Locator locator = WORKSPACE_FRAME.locator(String.format(
                "//table//thead//th[@aria-selected='true']//p[normalize-space()='%s']", defaultDimension));
        waitUtility.waitForLocatorVisible(locator);
        return locator.innerText().trim();
    }

    public String getDefaultMetrics(String defaultMetric) {
        Locator locator = WORKSPACE_FRAME.locator(
                String.format("//table//thead//th[@aria-selected='true']//p[normalize-space()='%s']", defaultMetric));
        waitUtility.waitForLocatorVisible(locator);
        return locator.innerText().trim();
    }

    public String getDefaultTimeFrame() {
        Locator locator = WORKSPACE_FRAME.locator(
                "//p[normalize-space()='Time Frame']/following-sibling::div//input[starts-with(@id,'listbox-input-')]");
        waitUtility.waitForLocatorVisible(locator);
        return locator.inputValue().trim();
    }

    public void saveBrandExplorerWorkspace() {
        waitForDashboardLoad();
        SAVE_WORKSPACE.first().click();
    }

    public void clickTimeFrameSelector() {
        waitUtility.waitForLocatorVisible(DATE_RANGE_SELECTOR);
        DATE_RANGE_SELECTOR.click();
    }

    public List<String> getTimeFrameOptions() {
        Locator options = WORKSPACE_FRAME.locator("//div[@role='dialog']//li[@role='option']");
        waitUtility.waitForLocatorVisible(options.first());
        List<String> optionlabels = new ArrayList<>();
        int count = options.count();
        for (int i = 0; i < count; i++) {
            optionlabels.add(options.nth(i).innerText().trim());
        }
        return optionlabels;
    }

    public void selectTimeFramePreset(String timeFrame) {
        Locator option = WORKSPACE_FRAME.locator(
                String.format("//div[@role='dialog']//li[@role='option']/span[normalize-space()='%s']", timeFrame));
        waitUtility.waitForLocatorVisible(option);
        option.click();
        page.keyboard().press("Escape");
        waitForDashboardLoad();
    }

    public String getSelectedTimeFrameAfterUpdate() {
        waitForDashboardLoad();
        return getDefaultTimeFrame();
    }

    public List<String> getTableDates(int days) {
        Locator dateCells = WORKSPACE_FRAME.locator(
                "//div[contains(@class,'Box')]//table//tbody//tr//td[1][@aria-colindex]");

        waitUtility.waitForLocatorVisible(dateCells.first());

        Set<String> seenDates = new LinkedHashSet<>();
        String previousLastDate = "";

        while (seenDates.size() < days) {

            int visibleRowCount = dateCells.count();

            // Capture all currently visible dates
            for (int i = 0; i < visibleRowCount; i++) {
                String date = dateCells.nth(i).innerText().trim();

                if (!date.isEmpty()) {
                    seenDates.add(date);
                }
            }

            if (seenDates.size() >= days) {
                break;
            }

            String currentLastDate = dateCells.last().innerText().trim();

            // Hover over table before scrolling
            dateCells.last().hover();

            // Scroll down
            page.mouse().wheel(0, 75);

            // Wait for virtualized rows to refresh
            page.waitForTimeout(1000);

            String newLastDate = dateCells.last().innerText().trim();

            // No new data loaded
            if (currentLastDate.equals(newLastDate)
                    || currentLastDate.equals(previousLastDate)) {
                break;
            }

            previousLastDate = currentLastDate;
        }

        return seenDates.stream()
                .limit(days)
                .collect(Collectors.toList());
    }
}
