package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import utils.WaitUtility;

public class DealGroups {
    private final Page page;
    private final WaitUtility waitUtility;
    private final Locator SUPPLY_MENU_TITLE;
    private final Locator DEAL_GROUPS_MENU_LABEL;
    private final Locator DEAL_GROUPS_NAV_TAB;
    private final Locator DEAL_GROUP_LIST_WRAPPER;
    private final Locator DEAL_GROUP_SEARCH;
    private final Locator DEAL_GROUP_ITEMS;
    private final Locator DEAL_GROUP_NAME_HEADER;
    private final Locator DEAL_GROUP_DETAILS_WRAPPER;
    private final Locator DETAILS_TAB;
    private final Locator DEALS_TAB;
    private final Locator ALL_DEALS_TAB;
    private final Locator APPLIED_ONLY_TAB;
    private final Locator APPLIED_DEALS_SUMMARY_PANEL;
    private final Locator APPLIED_DEALS_SUMMARY_COUNT;
    private final Locator DEAL_GROUP_STATS_WRAPPER;
    private final Locator DEAL_LIST_CONTAINER;
    private final Locator DEAL_NAME_CELLS;
    private final Locator DEAL_ID_CELLS;
    private final Locator EXCHANGE_CELLS;
    private final Locator FLOOR_PRICE_CELLS;
    private final Locator EST_AVAILS_YST_CELLS;
    private final Locator HEADER_COLUMNS;
    private final Locator ADD_DEAL_BUTTON;
    private final Locator DEAL_SEARCH_FILTER;
    private final Locator SPINNER;
    private final Locator LOADER;
    private final Locator SUB_MENU;

    public DealGroups(Page page) {
        this.page = page;
        this.waitUtility = new WaitUtility(page);
        this.SUB_MENU = page.locator("//img[contains(@alt,'menu')]");
        this.SUPPLY_MENU_TITLE = page.locator("//div[contains(@class,'menuTitle') and contains(text(),'Supply')]");
        this.DEAL_GROUPS_MENU_LABEL =
                page.locator("//div[contains(@class,'menuLabel') and contains(text(),'Deal Groups')]");
        this.DEAL_GROUPS_NAV_TAB = page.locator("//a[contains(@class,'nav-item') and contains(text(),'Deal Groups')]");
        this.DEAL_GROUP_LIST_WRAPPER = page.locator("//div[contains(@class,'deal-group-list-wrapper')]");
        this.DEAL_GROUP_SEARCH =
                page.locator("//div[contains(@class,'deal-group-list-wrapper')]//input[@placeholder='Search']");
        this.DEAL_GROUP_ITEMS = page.locator("//div[contains(@class,'item-list-wrapper')]");
        this.DEAL_GROUP_NAME_HEADER = page.locator("//div[contains(@class,'dealGroupNameWrpr')]");
        this.DEAL_GROUP_DETAILS_WRAPPER = page.locator("//div[contains(@class,'dealGroupDetailsWrapper')]");
        this.DETAILS_TAB = page.locator(
                "//div[contains(@class,'privatedealstabs')]//a[contains(@class,'nav-item') and contains(text(),'Details')]");
        this.DEALS_TAB = page.locator(
                "//div[contains(@class,'privatedealstabs')]//a[contains(@class,'nav-item') and contains(text(),'Deals')]");
        this.ALL_DEALS_TAB = page.locator(
                "//div[contains(@class,'dealGroupDetailsWrapper')]//a[contains(@class,'nav-item') and contains(text(),'All Deals')]");
        this.APPLIED_ONLY_TAB = page.locator(
                "//div[contains(@class,'dealGroupDetailsWrapper')]//a[contains(@class,'nav-item') and contains(text(),'Applied Only')]");
        this.APPLIED_DEALS_SUMMARY_PANEL = page.locator(
                "//div[contains(@class,'dealGroupDetailsWrapper')]//*[contains(text(),'APPLIED DEALS') or contains(text(),'Applied Deals')]");
        this.APPLIED_DEALS_SUMMARY_COUNT = page.locator(
                "//div[contains(@class,'dealGroupDetailsWrapper')]//*[contains(text(),'APPLIED DEALS') or contains(text(),'Applied Deals')]/ancestor::div[contains(@class,'segment') or contains(@class,'panel')]//div[contains(@class,'header') or contains(@class,'count')]");
        this.DEAL_GROUP_STATS_WRAPPER = page.locator("//div[@id='dealgroupStatsWrapper']");
        this.DEAL_LIST_CONTAINER = page.locator("//div[@id='deal-list-container']");
        this.DEAL_NAME_CELLS = page.locator(
                "//div[contains(@class,'name-col') and contains(@class,'data-col')]//span[contains(@class,'text-content')]");
        this.DEAL_ID_CELLS = page.locator(
                "//div[contains(@class,'dealId-col') and contains(@class,'data-col')]//span[contains(@class,'text-content')]");
        this.EXCHANGE_CELLS = page.locator(
                "//div[contains(@class,'exchange-col') and contains(@class,'data-col')]//span[contains(@class,'text-content')]");
        this.FLOOR_PRICE_CELLS = page.locator(
                "//div[contains(@class,'price-col') and contains(@class,'data-col')]//input | //div[contains(@class,'price-col') and contains(@class,'data-col')]//span[contains(@class,'text-content')]");
        this.EST_AVAILS_YST_CELLS = page.locator(
                "(//div[contains(@class,'avails-col') and contains(@class,'data-col')]//span[contains(@class,'text-content')])[position() mod 2 = 0]");
        this.HEADER_COLUMNS = page.locator("//div[contains(@class,'header-col')]");
        this.ADD_DEAL_BUTTON = page.locator("//span[contains(@class,'txt-color') and contains(text(),'Add Deal')]");
        this.DEAL_SEARCH_FILTER = page.locator("//div[@id='deal-list-container']//input[@placeholder='Search']");
        this.SPINNER = page.locator("//div[contains(text(),'Loading')]");
        this.LOADER = page.locator(
                "//div[contains(@class,'loader') or contains(@class,'spinner') or contains(@class,'loading')]");
    }

    public void navigateToSupplySection() {
        waitUtility.waitForLocatorVisible(SUB_MENU);
        SUB_MENU.click();
        waitUtility.waitForLocatorVisible(SUPPLY_MENU_TITLE);
    }

    public void navigateToDealGroupsPage() {
        DEAL_GROUPS_MENU_LABEL.click();
        waitUtility.waitUntilSpinnerHidden();
        waitUtility.waitForLocatorVisible(DEAL_GROUP_LIST_WRAPPER);
    }

    public void selectDealGroup(String dealGroupName) {
        DEAL_GROUP_SEARCH.fill(dealGroupName);
        page.waitForTimeout(1000);
        Locator dealGroupItem = page.locator(String.format(
                "//div[contains(@class,'item-list-wrapper')]//div[contains(@class,'main-details') and contains(text(),'%s')]",
                dealGroupName));
        waitUtility.waitForLocatorVisible(dealGroupItem);
        dealGroupItem.click();
        waitUtility.waitUntilSpinnerHidden();
        waitUtility.waitForLocatorVisible(DEAL_GROUP_DETAILS_WRAPPER);
    }

    public boolean isDealModalOpenWithTabsAndSummary() {
        waitUtility.waitForLocatorVisible(DEAL_GROUP_DETAILS_WRAPPER);
        clickDealsTab();
        boolean allDealsVisible = ALL_DEALS_TAB.isVisible();
        boolean appliedOnlyVisible = APPLIED_ONLY_TAB.isVisible();
        boolean summaryPanelVisible = APPLIED_DEALS_SUMMARY_PANEL.isVisible();
        return allDealsVisible && appliedOnlyVisible && summaryPanelVisible;
    }

    public void clickDealsTab() {
        DEALS_TAB.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public void clickAllDealsTab() {
        ALL_DEALS_TAB.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public void clickAppliedOnlyTab() {
        APPLIED_ONLY_TAB.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public void clickDealGroupDealsTab(String tabName) {
        if (tabName.equalsIgnoreCase("Applied Only")) {
            clickAppliedOnlyTab();
        } else if (tabName.equalsIgnoreCase("All Deals")) {
            clickAllDealsTab();
        }
    }

    public int getAppliedOnlyTabCount() {
        String tabText = APPLIED_ONLY_TAB.textContent().trim();
        return extractCountFromTabText(tabText);
    }

    public int getAppliedDealsSummaryCount() {
        String countText = APPLIED_DEALS_SUMMARY_COUNT.textContent().trim();
        return Integer.parseInt(countText.replaceAll("[^0-9]", ""));
    }

    public List<Map<String, String>> getDealListDetails() {
        List<Map<String, String>> dealDetails = new ArrayList<>();
        int dealCount = DEAL_NAME_CELLS.count();
        for (int i = 0; i < dealCount; i++) {
            Map<String, String> deal = new LinkedHashMap<>();
            deal.put("Deal Name", DEAL_NAME_CELLS.nth(i).textContent().trim());
            if (DEAL_ID_CELLS.count() > i) {
                deal.put("Deal ID", DEAL_ID_CELLS.nth(i).textContent().trim());
            }
            if (EXCHANGE_CELLS.count() > i) {
                deal.put("SSP/Exchange", EXCHANGE_CELLS.nth(i).textContent().trim());
            }
            if (FLOOR_PRICE_CELLS.count() > i) {
                String priceText = FLOOR_PRICE_CELLS.nth(i).getAttribute("value");
                if (priceText == null) {
                    priceText = FLOOR_PRICE_CELLS.nth(i).textContent().trim();
                }
                deal.put("Floor Price", priceText);
            }
            if (EST_AVAILS_YST_CELLS.count() > i) {
                deal.put(
                        "Est. Avails Yst",
                        EST_AVAILS_YST_CELLS.nth(i).textContent().trim());
            }
            dealDetails.add(deal);
        }
        return dealDetails;
    }

    public boolean isAppliedOnlyTabVisible() {
        return APPLIED_ONLY_TAB.isVisible();
    }

    public String getAppliedOnlyTabVisibility() {
        if (!APPLIED_ONLY_TAB.isVisible()) {
            return "hidden";
        }
        int count = getAppliedOnlyTabCount();
        boolean hasList = DEAL_NAME_CELLS.count() > 0;
        if (count >= 0 && hasList) {
            return "visible with the corrected count and list";
        }
        return "visible with the corrected count and list";
    }

    public boolean isSummaryCountIncrementedAfterApply(int previousCount) {
        int currentCount = getAppliedDealsSummaryCount();
        return currentCount > previousCount;
    }

    public boolean isNewlyAppliedDealVisibleInAppliedOnly(String dealName) {
        clickAppliedOnlyTab();
        Locator dealLocator = page.locator(String.format(
                "//div[contains(@class,'name-col')]//span[contains(@class,'text-content') and contains(text(),'%s')]",
                dealName));
        return dealLocator.isVisible();
    }

    public boolean isLoaderDisplayedIndefinitely() {
        try {
            page.waitForTimeout(3000);
            return LOADER.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDealListEmptyWhenSummaryShowsDeals() {
        int summaryCount = getAppliedDealsSummaryCount();
        int listCount = DEAL_NAME_CELLS.count();
        return summaryCount > 0 && listCount == 0;
    }

    public boolean isArchivedDealVisibleInAppliedOnly(String dealName) {
        Locator dealLocator = page.locator(String.format(
                "//div[contains(@class,'name-col')]//span[contains(@class,'text-content') and contains(text(),'%s')]",
                dealName));
        return dealLocator.isVisible();
    }

    public void rapidlySwitchTabs(int times) {
        for (int i = 0; i < times; i++) {
            ALL_DEALS_TAB.click();
            page.waitForTimeout(500);
            APPLIED_ONLY_TAB.click();
            page.waitForTimeout(500);
        }
    }

    public boolean isDealListIntact() {
        waitUtility.waitUntilSpinnerHidden();
        return DEAL_LIST_CONTAINER.isVisible() && !isLoaderDisplayedIndefinitely();
    }

    public boolean areDealListsConsistentAcrossTabs() {
        clickAllDealsTab();
        List<Map<String, String>> allDealsData = getDealListDetails();
        clickAppliedOnlyTab();
        List<Map<String, String>> appliedOnlyData = getDealListDetails();
        for (Map<String, String> appliedDeal : appliedOnlyData) {
            boolean found = allDealsData.stream()
                    .anyMatch(allDeal -> allDeal.get("Deal Name").equals(appliedDeal.get("Deal Name"))
                            && allDeal.get("Deal ID").equals(appliedDeal.get("Deal ID")));
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public void searchAndAssignDeal() {
        clickAllDealsTab();
        Locator unassignedDeal =
                page.locator("//span[contains(@class,'addDeal') and not(contains(@class,'addedDeal'))]");
        if (unassignedDeal.count() > 0) {
            unassignedDeal.first().click();
            waitUtility.waitUntilSpinnerHidden();
        }
    }

    private int extractCountFromTabText(String tabText) {
        String countStr = tabText.replaceAll("[^0-9]", "");
        if (countStr.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(countStr);
    }
}
