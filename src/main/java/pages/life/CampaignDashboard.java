package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.CommonUtils;
import utils.WaitUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CampaignDashboard {
    private final Page page;
    private final Locator CAMPAIGN_PAGE_TEXT;
    private final Locator CAMPAIGN_COMMENT_BOX;
    private final Locator LINE_ITEM_COMMENT_BOX;
    private final Locator TACTIC_NAME_COMMENT_BOX;
    private final Locator COMMENT_BOX;
    private final Locator COMMENT_BOX_OK_BUTTON;
    private final Locator COMMENT_SUCCESS_ALERT;
    private final Locator COMMENT_ICON;
    private final Locator TOOLTIP_TEXT;
    private final Locator LINE_ITEM_TOGGLE_BUTTON;
    private final Locator TACTIC_TOGGLE_BUTTON;
    private final Locator BACK_TO_DASHBOARD;
    private final Locator LINE_ITEM_NAME;
    private final Locator TACTIC_NAME;
    private final Locator CAMPAIGN_PAGE_TITLE;
    private final Locator LINE_ITEM_PAGE_TITLE;
    private final Locator TACTIC_PAGE_TITLE;
    private final Locator DASHBOARD_MENU_ICON;
    private final Locator SELECT_COLUMNS_FROM_ICON;
    private final Locator SHOW_ALL;
    private final Locator HIDE_ALL;
    private final Locator DASHBOARD_MENU_COLUMNS;
    private final Locator FILTER_OK_BUTTON;
    private final Locator SELECTED_FILTER;
    private final Locator RESET_FILTER_BUTTON;
    private final Locator FILTER_ICON;
    private final Locator FAVORITE_ONLY_CHECKBOX;
    private final Locator FAVORITE_CAMPAIGN_LIST;
    private final Locator HIDE_FINISHED_CHECKBOX;
    private final Locator HIDE_FINISHED_LIST;
    private final Locator ACTIVE_FLIGHT_BUTTON;
    private final Locator TODAY_BUTTON;
    private final Locator YESTERDAY_BUTTON;
    private final Locator CUSTOM_BUTTON;
    private final Locator CUSTOM_DATE_TEXTBOX;
    private final Locator INACTIVE_FLIGHTS;
    private final Locator SETTING_ICON;
    private final Locator GROUP_BY_CAMPAIGN_RADIO_BUTTON;
    private final Locator GROUP_BY_ADVERTISER_RADIO_BUTTON;
    private final Locator NO_GROUP_RADIO_BUTTON;
    private final Locator ADVERTISER_COLUMN_NAME;
    private final Locator CAMPAIGN_COLUMN_NAME;
    private final Locator CREATIVE_TOOLTIP;
    private final Locator LIFE_TIME_FILTER;
    private final Locator CLICK_SETTINGS;
    private final Locator SEARCH_CAMPAIGN;
    private final Locator CLICK_CAMPAIGN_SEARCH;
    private final Locator EXPAND_CREATED_LINE_ITEM;
    private final Locator VERIFY_CREATED_TACTIC;
    private final Locator CAMPAIGN_ENTRIES;
    private final Locator SUB_TITLE_AFTER_CAMPAIGN_SEARCH;
    private final Locator FILTER_APPLIED_ICON;
    private final Locator RESET_FILTER_ICON;
    private final Locator PRE_LOADER;
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    String lineItemClassBeforeClick, lineItemClassAfterClick, tacticClassBeforeClick, tacticClassAfterClick;
    boolean flag1, flag2, flag3 = false;

    public CampaignDashboard(Page page) {
        this.page = page;
        this.CAMPAIGN_PAGE_TEXT = page.locator("//span[contains(text(),'Campaigns')]");
        this.CAMPAIGN_COMMENT_BOX = page.locator("//div[@class='camp-data']//span[@class='notesIconEmpty' or @class='notesIconProvided']");
        this.LINE_ITEM_COMMENT_BOX = page.locator("//div[contains(@class,'lineitem-data pointer')]//span[@class='notesIconEmpty' or @class='notesIconProvided']");
        this.TACTIC_NAME_COMMENT_BOX = page.locator("//div[contains(@class,'tactic-data pointer')]//span[@class='notesIconEmpty' or @class='notesIconProvided']");
        this.COMMENT_BOX = page.locator("//textarea[@id='notesId']");
        this.COMMENT_BOX_OK_BUTTON = page.locator("//button[@class='okButton']");
        this.COMMENT_SUCCESS_ALERT = page.locator("//div[@aria-label='Success!']");
        this.COMMENT_ICON = page.locator("span.notesIconProvided");
        this.TOOLTIP_TEXT = page.locator("//span[@class='tooltip-text']");
        this.LINE_ITEM_TOGGLE_BUTTON = page.locator("//div[contains(@class,'lineitem-data pointer')]//sui-checkbox[contains(@class,'ng-valid')]");
        this.TACTIC_TOGGLE_BUTTON = page.locator("//div[contains(@class,'tactic-data pointer')]//sui-checkbox[contains(@class,'ng-valid')]");
        this.BACK_TO_DASHBOARD = page.locator("//div[@class='logo-lists']/img");
        this.LINE_ITEM_NAME = page.locator("//span[contains(@class,'color-black lineitem-name-section')]");
        this.LINE_ITEM_PAGE_TITLE = page.locator("//div[contains(@class,'left truncate lineitem-name')]");
        this.CAMPAIGN_PAGE_TITLE = page.locator("//div[contains(@class,'campaign-name')]");
        this.TACTIC_NAME = page.locator("//span[contains(@class,'color-black tactic-name-section')]");
        this.TACTIC_PAGE_TITLE = page.locator("//div[contains(@class,'left truncate tactic-name')]");
        this.DASHBOARD_MENU_ICON = page.locator("//span[@class='icon-bars']");
        this.SELECT_COLUMNS_FROM_ICON = page.locator("//sui-checkbox[contains(@class,'form-control')]/label");
        this.SHOW_ALL = page.locator("//div[contains(@class,'menuButtons')]//div[@class='show-all']");
        this.HIDE_ALL = page.locator("//div[contains(@class,'menuButtons')]//div[@class='hide-all']");
        this.DASHBOARD_MENU_COLUMNS = page.locator("//div[contains(@class,'data-table-header')]/div[contains(@id,'liHeader') or contains(@class,'align-left')]");
        this.FILTER_OK_BUTTON = page.locator("//button[contains(@class,'ui primary button')]");
        this.SELECTED_FILTER = page.locator("//div[contains(@class,'selected-filters')]//label");
        this.RESET_FILTER_BUTTON = page.locator("//span[contains(text(),'Reset All Filters')]");
        this.FILTER_ICON = page.locator("//div[contains(@class,'filterApplied')]");
        this.FAVORITE_ONLY_CHECKBOX = page.locator("//sui-checkbox[contains(@class,'gaFavoritesOnly')]");
        this.FAVORITE_CAMPAIGN_LIST = page.locator("//i[contains(@class,'star display-inline')]");
        this.HIDE_FINISHED_CHECKBOX = page.locator("//label[contains(text(),'Hide Finished')]/ancestor::sui-checkbox");
        this.HIDE_FINISHED_LIST = page.locator("//span[contains(@class,'display-inline incomplete status-label')]");
        this.ACTIVE_FLIGHT_BUTTON = page.locator("//button[@data-title='Active Flight']");
        this.TODAY_BUTTON = page.locator("//button[@data-title='Today']");
        this.YESTERDAY_BUTTON = page.locator("//button[@data-title='Yesterday']");
        this.CUSTOM_BUTTON = page.locator("//button[@data-title='Custom']");
        this.INACTIVE_FLIGHTS = page.locator("//div[contains(text(),'inactive flight')]");
        this.CUSTOM_DATE_TEXTBOX = page.locator("//input[@name='customDateRange']");
        this.SETTING_ICON = page.locator("//div[contains(@class,'gaGroupingType')]/i");
        this.GROUP_BY_CAMPAIGN_RADIO_BUTTON = page.locator("//i[contains(@class,'gaGroupCampaigns')]");
        this.GROUP_BY_ADVERTISER_RADIO_BUTTON = page.locator("//i[contains(@class,'gaGroupAdvertisers')]");
        this.NO_GROUP_RADIO_BUTTON = page.locator("//i[contains(@class,'gaNoGrouping')]");
        this.ADVERTISER_COLUMN_NAME = page.locator("//div[contains(@id,'liHeaderAdvertiser')]");
        this.CAMPAIGN_COLUMN_NAME = page.locator("//div[contains(@class,'display-inline')]/span[contains(text(),'CAMPAIGN')]");
        this.CREATIVE_TOOLTIP = page.locator("//span[contains(@class,'statusTooltipBackgroundImage')]");
        this.LIFE_TIME_FILTER = page.locator("//button[@data-title='Lifetime']");
        this.CLICK_SETTINGS = page.locator("//i[@class='icon gearIcon']");
        this.SEARCH_CAMPAIGN = page.locator("//input[@placeholder='Search' and contains(@class, 'gaTableSearch')]");
        this.CLICK_CAMPAIGN_SEARCH = page.locator("//div[contains(@class,'gaTableSearchBtn')]");
        this.EXPAND_CREATED_LINE_ITEM = page.locator("//div[contains(@class,'campaignExpand')]/div[contains(@class,'collapsed-thin')]");
        this.VERIFY_CREATED_TACTIC = page.locator("//span[contains(@class,'tactic-name')]");
        this.CAMPAIGN_ENTRIES = page.locator("//div[contains(@class,'name-section-wrapper')]");
        this.SUB_TITLE_AFTER_CAMPAIGN_SEARCH = page.locator("//div[contains(@class,'sub-title') and contains(text(),'1 Line items, 1 Campaigns, 1 Advertisers')]");
        this.FILTER_APPLIED_ICON = page.locator("//div[contains(@class,'filterApplied')]");
        this.RESET_FILTER_ICON = page.locator("//span[contains(text(),'Reset All Filters')]");
        this.PRE_LOADER = page.locator("//div[@class='preloader']");
    }

    public String verifyCampaignDashbaord(String text) {
        waitUtility.waitUntilSpinnerHidden();
        waitUtility.waitUntilPreLoaderHidden();
        page.waitForCondition(() -> CAMPAIGN_PAGE_TEXT.filter(new Locator.FilterOptions().setHasText(text)).count() == 1);
        return CAMPAIGN_PAGE_TEXT.innerText();
    }

    public String verifyCampaignDetails(String campaignID) {
        return page.locator(String.format("//span[contains(text(),'%s')]", campaignID)).innerText();
    }

    public String addCommentsToCampaign(String commentType, List<String> commentValues) {
        page.waitForLoadState(LoadState.LOAD);
        String successAlertText = "";
        switch (commentType) {
            case "Campaign Name":
                for (String val : commentValues) {
                    CAMPAIGN_COMMENT_BOX.isVisible();
                    CAMPAIGN_COMMENT_BOX.click();
                    successAlertText = addComments(val);
                }
                break;
            case "Line Item Name":
                for (String val : commentValues) {
                    LINE_ITEM_COMMENT_BOX.isVisible();
                    LINE_ITEM_COMMENT_BOX.click();
                    successAlertText = addComments(val);
                }
                break;
            case "Tactic Name":
                for (String val : commentValues) {
                    TACTIC_NAME_COMMENT_BOX.isVisible();
                    TACTIC_NAME_COMMENT_BOX.click();
                    successAlertText = addComments(val);
                }
                break;
        }
        return successAlertText;
    }

    public String addComments(String comment) {
        COMMENT_BOX.fill(comment);
        COMMENT_BOX_OK_BUTTON.click();
        waitUtility.waitForLocatorVisible(COMMENT_SUCCESS_ALERT);
        String successAlertText = COMMENT_SUCCESS_ALERT.innerText();
        waitUtility.waitForLocatorHidden(COMMENT_SUCCESS_ALERT);
        return successAlertText;
    }

    public List<String> verifyCommentIconColor() {
        List<String> backgroundImages = new ArrayList<>();
        for (int i = 0; i < COMMENT_ICON.count(); i++) {
            String bgImage = COMMENT_ICON.nth(i).evaluate("element => getComputedStyle(element).backgroundImage").toString();
            backgroundImages.add(bgImage.substring(bgImage.lastIndexOf("/") + 1, bgImage.length() - 2));
        }
        return backgroundImages;
    }

    public List<String> verifyCommentIconText() {
        List<String> returnValues = new ArrayList<>();
        for (int i = 0; i < COMMENT_ICON.count(); i++) {
            COMMENT_ICON.nth(i).hover();
            waitUtility.waitForLocatorVisible(TOOLTIP_TEXT);
            returnValues.add(TOOLTIP_TEXT.innerText());
        }
        return returnValues;
    }

    public void clickLineAndTacticToggleButton() {
        DASHBOARD_MENU_ICON.click();
        SHOW_ALL.click();
        page.keyboard().press("Escape");
        lineItemClassBeforeClick = LINE_ITEM_TOGGLE_BUTTON.getAttribute("class");
        LINE_ITEM_TOGGLE_BUTTON.click();
        waitUtility.waitUntilPreLoaderHidden();
        lineItemClassAfterClick = LINE_ITEM_TOGGLE_BUTTON.getAttribute("class");

        tacticClassBeforeClick = TACTIC_TOGGLE_BUTTON.getAttribute("class");
        TACTIC_TOGGLE_BUTTON.click();
        waitUtility.waitUntilPreLoaderHidden();
        tacticClassAfterClick = TACTIC_TOGGLE_BUTTON.getAttribute("class");
    }

    public boolean verifyLineTacticToggleStatus() {
        return !Objects.equals(lineItemClassBeforeClick, lineItemClassAfterClick) && !Objects.equals(tacticClassBeforeClick, tacticClassAfterClick);
    }

    public void navigateToCampaignLIAndTactic(String campaignID) {
        page.locator(String.format("//span[contains(text(),'%s')]", campaignID)).click();
        waitUtility.waitForLocatorVisible(CAMPAIGN_PAGE_TITLE);
        if (CAMPAIGN_PAGE_TITLE.getAttribute("class").contains("campaign")) flag1 = true;
        BACK_TO_DASHBOARD.click();
        searchCreatedCampaign(campaignID);
        expandCreatedLineItem();
        LINE_ITEM_NAME.click();
        LINE_ITEM_PAGE_TITLE.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        if (LINE_ITEM_PAGE_TITLE.getAttribute("class").contains("lineitem")) flag2 = true;
        BACK_TO_DASHBOARD.click();
        searchCreatedCampaign(campaignID);
        expandCreatedLineItem();
        TACTIC_NAME.click();
        waitUtility.waitForLocatorVisible(TACTIC_PAGE_TITLE);
        if (TACTIC_PAGE_TITLE.getAttribute("class").contains("tactic-name")) flag3 = true;
        BACK_TO_DASHBOARD.click();
        waitUtility.waitUntilPreLoaderHidden();
    }

    public boolean verifyPanelTitleText() {
        return flag1 && flag2 && flag3;
    }

    public void clickMenuTransitionIcon(List<String> columnName) {
        DASHBOARD_MENU_ICON.click();
        HIDE_ALL.click();
        CommonUtils.selectAndClickElement(SELECT_COLUMNS_FROM_ICON, columnName);
        page.keyboard().press("Escape");
    }

    public List<String> fecthDashboardColumns() {
        List<String> columnNames = new ArrayList<>();
        for (int i = 0; i < DASHBOARD_MENU_COLUMNS.count(); i++) {
            columnNames.add(DASHBOARD_MENU_COLUMNS.nth(i).innerText());
        }
        return columnNames;
    }

    public void clickHideAndShowAllOption() {
        DASHBOARD_MENU_ICON.click();
        HIDE_ALL.click();
        page.keyboard().press("Escape");
        if (DASHBOARD_MENU_COLUMNS.count() == 0) flag1 = true;
        DASHBOARD_MENU_ICON.click();
        SHOW_ALL.click();
        page.keyboard().press("Escape");
        if (DASHBOARD_MENU_COLUMNS.count() > 0) flag2 = true;
    }

    public boolean verifyColumnsCount() {
        return flag1 && flag2;
    }

    public void applyFilterOnSelectedColumns(String filterName, List<String> filterValues) {
        String filterXpath = String.format("//span[contains(text(), '%s')]/following-sibling::div//span[contains(@class,'filter')]", filterName);
        page.locator(filterXpath).click();
        for (String value : filterValues) {
            String valueXpath = String.format("//app-overlay[@class='line-item-list-filter']//label[contains(text(),'%s')]", value);
            page.locator(valueXpath).click();
        }
        FILTER_OK_BUTTON.click();
        waitUtility.waitUntilPreLoaderHidden();
    }

    public List<String> verifySelectedFilter() {
        List<String> selectedFilter = new ArrayList<>();
        for (int i = 0; i < SELECTED_FILTER.count(); i++) {
            selectedFilter.add(SELECTED_FILTER.nth(i).innerText());
        }
        return selectedFilter;
    }

    public String verifyFilterIcon() {
        String bgImage = FILTER_ICON.evaluate("element => getComputedStyle(element).backgroundImage").toString();
        RESET_FILTER_BUTTON.click();
        waitUtility.waitUntilPreLoaderHidden();
        return bgImage.substring(bgImage.lastIndexOf("/") + 1, bgImage.length() - 2);
    }

    public void clickFavoriteOnlyCheckbox() {
        verifyCampaignRadioBtnChecked();
        verifyFavoriteCheckbox();
        FAVORITE_ONLY_CHECKBOX.click();
        waitUtility.waitUntilPreLoaderHidden(120000);
    }

    public int verifyCampaignMarkedFavorite() {
        int flag = 0;
        if (!FAVORITE_CAMPAIGN_LIST.first().isVisible()) {
            return flag;
        } else if (FAVORITE_CAMPAIGN_LIST.count() > 0) {
            flag = FAVORITE_CAMPAIGN_LIST.count();
        }
        return flag;
    }

    public void clickHideFinishedCheckbox() {
        verifyCampaignRadioBtnChecked();
        verifyHideFinishedCheckbox();
        HIDE_FINISHED_CHECKBOX.click();
        waitUtility.waitUntilPreLoaderHidden(120000);
    }

    public boolean verifyHideFinishedCampaignList() {
        for (int i = 0; i < HIDE_FINISHED_LIST.count(); i++) {
            String text = HIDE_FINISHED_LIST.nth(i).innerText().trim();
            if (text.equalsIgnoreCase("FINISHED")) {
                return false;
            }
        }
        return true;
    }

    public boolean clickAndVerifyFilterOptionTypeButton() {
        Locator[] filterButtons = {ACTIVE_FLIGHT_BUTTON, TODAY_BUTTON, YESTERDAY_BUTTON};
        for (Locator button : filterButtons) {
            button.click();
            waitUtility.waitUntilPreLoaderHidden(120000);
            if (INACTIVE_FLIGHTS.isVisible()) {
                return false;
            }
        }
        return true;
    }

    public void clickAndVerifyCustomFilterOption() {
        CUSTOM_BUTTON.click();
        CUSTOM_DATE_TEXTBOX.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        CUSTOM_DATE_TEXTBOX.fill("06/01/2025 - 06/20/2025");
        page.keyboard().press("Enter");
    }

    public boolean clickGroupByOptionsAndFilterDashboardData() {
        SETTING_ICON.click();
        GROUP_BY_CAMPAIGN_RADIO_BUTTON.click();
        waitUtility.waitUntilPreLoaderHidden(120000);
        if (ADVERTISER_COLUMN_NAME.isVisible()) flag1 = true;
        SETTING_ICON.click();
        GROUP_BY_ADVERTISER_RADIO_BUTTON.click();
        waitUtility.waitUntilPreLoaderHidden(120000);
        if (CAMPAIGN_COLUMN_NAME.isVisible()) flag2 = true;
        SETTING_ICON.click();
        NO_GROUP_RADIO_BUTTON.click();
        waitUtility.waitUntilPreLoaderHidden();
        if (ADVERTISER_COLUMN_NAME.isVisible() && CAMPAIGN_COLUMN_NAME.isVisible()) flag3 = true;
        return flag1 && flag2 && flag3;
    }

    public boolean fetchCreativeToolTipText() {
        ACTIVE_FLIGHT_BUTTON.click();
        verifyFavoriteCheckbox();
        CREATIVE_TOOLTIP.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        int count = CREATIVE_TOOLTIP.count();
        for (int i = 0; i < count; i++) {
            String tooltipText = CREATIVE_TOOLTIP.nth(i).getAttribute("tooltip-text");
            if (!(tooltipText.contains("No creative assigned") || tooltipText.contains("are pending approval") || tooltipText.contains("are denied"))) {
                return false;
            }
        }
        return true;
    }

    public void navigateToLineItemDetails(){
        LINE_ITEM_NAME.click();
        LINE_ITEM_PAGE_TITLE.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void clickLifetimeFilter() {
        LIFE_TIME_FILTER.click();
        waitUtility.waitUntilPreLoaderHidden();
    }

    public void verifyCampaignRadioBtnChecked() {
        CLICK_SETTINGS.click();
        if (!GROUP_BY_CAMPAIGN_RADIO_BUTTON.getAttribute("class").contains("groupingMenuRadioSelected")) {
            GROUP_BY_CAMPAIGN_RADIO_BUTTON.click();
            waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        }
    }

    public void verifyFavoriteCheckbox() {
        if (FAVORITE_ONLY_CHECKBOX.getAttribute("class").contains("checked")) {
            FAVORITE_ONLY_CHECKBOX.click();
            waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        }
    }

    public void verifyHideFinishedCheckbox() {
        if (HIDE_FINISHED_CHECKBOX.getAttribute("class").contains("checked")) {
            HIDE_FINISHED_CHECKBOX.click();
            waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        }
    }

    public void verifyIfFiltersExist() {
        if (FILTER_APPLIED_ICON.isVisible()) {
            FILTER_APPLIED_ICON.click();
            RESET_FILTER_ICON.click();
            waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        }
    }

    public void searchCreatedCampaign(String createdCampaign) {
        waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        verifyCampaignRadioBtnChecked();
        verifyFavoriteCheckbox();
        verifyHideFinishedCheckbox();
        verifyIfFiltersExist();
        while(!SUB_TITLE_AFTER_CAMPAIGN_SEARCH.isVisible()){
            SEARCH_CAMPAIGN.fill(createdCampaign);
            CLICK_CAMPAIGN_SEARCH.click();
        }
    }

    public String verifyCreatedCampaign(String createdCampaign) {
        String campaignNameXpath = String.format("//span[contains(text(),'%s')]", createdCampaign);
        waitUtility.waitForLocatorVisible(page.locator(campaignNameXpath).first());
        return page.locator(campaignNameXpath).first().innerText();
    }

    public String verifyCreatedLineItem(String lineItemNameRandom) {
        String lineItemNameXpath = String.format("//span[contains(text(),'%s')]", lineItemNameRandom);
        waitUtility.waitForElementVisible(lineItemNameXpath);
        return page.locator(lineItemNameXpath).innerText();
    }

    public void expandCreatedLineItem() {
        if (EXPAND_CREATED_LINE_ITEM.first().isVisible()) {
            EXPAND_CREATED_LINE_ITEM.first().click();
            waitUtility.waitUntilPreLoaderHidden();
        }
    }

    public String verifyCreatedTactic() {
        page.waitForLoadState();
        return VERIFY_CREATED_TACTIC.innerText();
    }
}