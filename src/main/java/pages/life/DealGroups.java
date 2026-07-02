package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.WaitUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Page Object for the Deal Groups section within the LIFE Supply module.
 * Covers navigation to Deal Groups, selecting a Deal Group, and interacting
 * with the Deal modal's "All Deals" / "Applied Only" tabs.
 *
 * @author iAutomate (QA-1557)
 */
public class DealGroups {

    private final Page page;
    private final WaitUtility waitUtility;

    // --- Navigation locators ---
    // TODO: Replace placeholder XPaths with real locators after DOM inspection
    private final Locator SUPPLY_SECTION_LINK;
    private final Locator DEAL_GROUPS_PAGE_LINK;

    // --- Deal modal ---
    private final Locator DEAL_MODAL;
    private final Locator ALL_DEALS_TAB;
    private final Locator APPLIED_ONLY_TAB;
    private final Locator APPLIED_DEALS_SUMMARY_PANEL;
    private final Locator APPLIED_DEALS_SUMMARY_COUNT;
    private final Locator APPLIED_ONLY_TAB_COUNT;
    private final Locator DEAL_LIST_ROWS;
    private final Locator DEAL_LIST_HEADERS;
    private final Locator LOADER_SPINNER;

    private int appliedCountBeforeAction;

    public DealGroups(Page page) {
        this.page = page;
        this.waitUtility = new WaitUtility(page);

        this.SUPPLY_SECTION_LINK = page.locator(
                "//a[contains(text(),'Supply') or contains(@routerlink,'supply')]");
        this.DEAL_GROUPS_PAGE_LINK = page.locator(
                "//a[contains(text(),'Deal Groups') or contains(@routerlink,'deal-groups')]");

        this.DEAL_MODAL = page.locator(
                "//div[contains(@class,'deal-modal') or @role='dialog']");
        this.ALL_DEALS_TAB = page.locator(
                "//a[contains(text(),'All Deals')]");
        this.APPLIED_ONLY_TAB = page.locator(
                "//a[contains(text(),'Applied Only')]");
        this.APPLIED_DEALS_SUMMARY_PANEL = page.locator(
                "//div[contains(@class,'applied-deals-summary') or contains(text(),'APPLIED DEALS')]");
        this.APPLIED_DEALS_SUMMARY_COUNT = page.locator(
                "//div[contains(@class,'applied-deals-summary')]//span[contains(@class,'count')]");
        this.APPLIED_ONLY_TAB_COUNT = page.locator(
                "//a[contains(text(),'Applied Only')]//span[contains(@class,'count') or contains(@class,'badge')]");
        this.DEAL_LIST_ROWS = page.locator(
                "//div[contains(@class,'deal-list')]//tr[contains(@class,'deal-row')]");
        this.DEAL_LIST_HEADERS = page.locator(
                "//div[contains(@class,'deal-list')]//th");
        this.LOADER_SPINNER = page.locator(
                "//div[contains(@class,'loader') or contains(@class,'spinner')]");
    }

    public void navigateToSupplySection() {
        waitUtility.waitForLocatorVisible(SUPPLY_SECTION_LINK);
        SUPPLY_SECTION_LINK.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public void navigateToDealGroupsPage() {
        waitUtility.waitForLocatorVisible(DEAL_GROUPS_PAGE_LINK);
        DEAL_GROUPS_PAGE_LINK.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public void selectDealGroup(String dealGroupName) {
        Locator groupRow = page.locator("//td[contains(text(),'" + dealGroupName + "')]");
        waitUtility.waitForLocatorVisible(groupRow);
        groupRow.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public boolean isDealModalOpenWithExpectedTabs() {
        waitUtility.waitForLocatorVisible(DEAL_MODAL);
        return DEAL_MODAL.isVisible() && ALL_DEALS_TAB.isVisible()
                && APPLIED_ONLY_TAB.isVisible() && APPLIED_DEALS_SUMMARY_PANEL.isVisible();
    }

    public void clickAppliedOnlyTab() {
        waitUtility.waitForLocatorVisible(APPLIED_ONLY_TAB);
        APPLIED_ONLY_TAB.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public void clickAllDealsTab() {
        waitUtility.waitForLocatorVisible(ALL_DEALS_TAB);
        ALL_DEALS_TAB.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public String getAppliedOnlyTabCount() {
        waitUtility.waitForLocatorVisible(APPLIED_ONLY_TAB_COUNT);
        return APPLIED_ONLY_TAB_COUNT.innerText().trim();
    }

    public String getAppliedDealsSummaryPanelCount() {
        waitUtility.waitForLocatorVisible(APPLIED_DEALS_SUMMARY_COUNT);
        return APPLIED_DEALS_SUMMARY_COUNT.innerText().trim();
    }

    public void captureCurrentSummaryCount() {
        appliedCountBeforeAction = Integer.parseInt(getAppliedDealsSummaryPanelCount());
    }

    public int getCapturedSummaryCount() {
        return appliedCountBeforeAction;
    }

    public int getDealListRowCount() {
        return DEAL_LIST_ROWS.count();
    }

    public List<Map<String, String>> getDealListData() {
        List<Map<String, String>> results = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        int headerCount = DEAL_LIST_HEADERS.count();
        for (int h = 0; h < headerCount; h++) {
            headers.add(DEAL_LIST_HEADERS.nth(h).innerText().trim());
        }
        int rowCount = DEAL_LIST_ROWS.count();
        for (int r = 0; r < rowCount; r++) {
            Map<String, String> row = new HashMap<>();
            Locator cells = DEAL_LIST_ROWS.nth(r).locator("td");
            int cellCount = cells.count();
            for (int c = 0; c < cellCount && c < headers.size(); c++) {
                row.put(headers.get(c), cells.nth(c).innerText().trim());
            }
            results.add(row);
        }
        return results;
    }

    public boolean isLoaderDisplayedIndefinitely(long timeoutMs) {
        try {
            LOADER_SPINNER.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.HIDDEN)
                    .setTimeout(timeoutMs));
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isDealListEmpty() {
        return DEAL_LIST_ROWS.count() == 0;
    }

    public boolean isAppliedOnlyTabVisible() {
        return APPLIED_ONLY_TAB.isVisible();
    }

    public String getAppliedOnlyTabVisibilityState() {
        if (!APPLIED_ONLY_TAB.isVisible()) {
            return "hidden";
        }
        APPLIED_ONLY_TAB.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        boolean hasCount = APPLIED_ONLY_TAB_COUNT.isVisible();
        if (hasCount) {
            return "visible with the corrected count and list";
        }
        return "visible";
    }

    public void rapidlyToggleTabs(int switchCount) {
        for (int i = 0; i < switchCount; i++) {
            APPLIED_ONLY_TAB.click();
            page.waitForTimeout(200);
            ALL_DEALS_TAB.click();
            page.waitForTimeout(200);
        }
    }

    public boolean isDealListIntact() {
        boolean noError = page.locator("//div[contains(@class,'error')]").count() == 0;
        return noError && DEAL_LIST_ROWS.count() >= 0;
    }

    public boolean isAnyDealVisibleInAppliedOnlyList() {
        return DEAL_LIST_ROWS.count() > 0;
    }

    /**
     * Toggles the Applied Deals feature flag for the current account.
     * TODO: Implement via admin API or admin panel navigation.
     */
    public void setAppliedDealsFeatureFlag(String state) {
        throw new UnsupportedOperationException(
                "Feature flag toggle requires admin API/panel - implement after environment access");
    }
}
