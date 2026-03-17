package pages.healPageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import pages.life.CampaignDashboard;
import utils.WaitUtility;

import java.time.LocalDateTime;
import java.util.*;

public class HealingPageObject {
    private final Page page;
    public final Locator USERNAME;
    private final Locator PASSWORD;
    private final Locator LOGIN_BUTTON;
    private final Locator LIFE;
    private final Locator SIGNAL;
    private final Locator STUDIO;
    private final Locator SUB_MENU;
    private final Locator ACCOUNT_NAME;
    private final Locator ACCOUNT_SEARCH;
    private final Locator ACCOUNT_ITEM;
    private final Locator STUDIO_TITLE;
    private final Locator CAMPAIGN_PAGE_TEXT;
    private final Locator SPINNER;
    private final Locator OPTIONS_HEADER;
    private final Locator CREATE_CAMPAIGN;
    private final Locator VERIFY_CAMPAIGN_PAGE;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator CAMPAIGN_NAME;
    private final Locator CAMPAIGN_TYPE;
    private final Locator BUDGET;
    private final Locator SAVE_CAMPAIGN;
    private final Locator CAMPAIGN_SUCCESS;
    private final Locator CAMPAIGN_DASHBOARD;
    private final Locator LIFE_TIME_FILTER;
    private final Locator CAMPAIGN_ENTRIES;
    private final Locator VERIFY_LINE_ITEM_PAGE;
    private final Locator LINE_ITEM_NAME;
    private final Locator LINE_ITEM_BUDGET;
    private final Locator ENABLE_LINE_ITEM;
    private final Locator SAVE_LINE_ITEM;
    private final Locator LINE_ITEM_SUCCESS;
    private final Locator PLACEMENT_ID;
    private final Locator VERIFY_TACTIC_CREATIVES_PAGE;
    private final Locator TACTIC_SETTINGS_SUCCESS;
    private final Locator SAVE_TACTIC_SETTINGS;
    private final Locator SEARCH_RULE_TYPE;
    private final Locator SELECT_RULE_TYPE;
    private final Locator SELECT_OPTION;
    private final Locator RULE_TYPE_OK_BUTTON;
    private final Locator RULE_TYPE_CLOSE;
    private final Locator SELECT_CHANNEL;
    private final Locator VERIFY_TACTIC_SETTINGS_PAGE;
    private final Locator TACTIC_DETAILS_SUCCESS;
    private final Locator SAVE_TACTIC_DETAILS;
    private final Locator ADD_CUSTOM_FIELD;
    private final Locator TACTIC_NAME;
    private final Locator VERIFY_TACTIC_DETAILS_PAGE;
    private final Locator SEARCH_CREATIVE;
    private final Locator CLICK_SEARCH;
    private final Locator ASSIGN_CREATIVE_OK_BUTTON;
    private final Locator ENABLE_CREATIVE;
    private final Locator SAVE_TACTIC_CREATIVES;
    private final Locator TACTIC_CREATIVE_SUCCESS;
    private final Locator NAVIGATE_TO_CAMPAIGN_DASHBOARD;
    private final Locator VERIFY_CAMPAIGN_RUNNING;
    private final Locator ASSIGN_CREATIVE_TITLE;
    private final Locator CREATIVE_STATUS;
    private final Locator CLEAR_SEARCH_BOX;
    private final Locator CREATIVES_TABLE;
    private final Locator FAVORITE_ONLY_CHECKBOX;
    private final Locator HIDE_FINISHED_CHECKBOX;
    private final Locator GROUP_BY_CAMPAIGN_RADIO_BUTTON;
    private final Locator CLICK_SETTINGS;
    private final Locator SEARCH_CAMPAIGN;
    private final Locator CLICK_CAMPAIGN_SEARCH;
    private final Locator EXPAND_CREATED_LINE_ITEM;
    private final Locator VERIFY_CREATED_TACTIC;
    private final Locator SUB_TITLE_AFTER_CAMPAIGN_SEARCH;
    private final Locator FILTER_APPLIED_ICON;
    private final Locator RESET_FILTER_ICON;
    private final Locator NO_CAMPAIGN_AVAILABLE_TEXT;
    private final Locator PRE_LOADER;
    Calendar calendar = Calendar.getInstance();
    LocalDateTime currentDateTime = LocalDateTime.now();
    int startDay = currentDateTime.getDayOfMonth();
    int endDay;
    int daysInCurrentMonth = calendar.getActualMaximum(Calendar.DATE);
    WaitUtility waitUtility;
    CampaignDashboard campaignDashboard;
    HealingActions healingActions;

    public HealingPageObject(Page page) {
        if (startDay == daysInCurrentMonth) {
            endDay = 5;
        } else {
            endDay = startDay + 5;
        }
        this.page = page;
        this.healingActions = new HealingActions(page);
        this.waitUtility = new WaitUtility(page);
        this.campaignDashboard = new CampaignDashboard(page);
        this.USERNAME = page.locator("#UserNe");
        this.PASSWORD = page.locator("#Passwd");
        this.LOGIN_BUTTON = page.locator(".loginLabe");
        this.LIFE = page.locator("//span[contains(@class,'buyerPorlLink')]");
        this.SIGNAL = page.getByText("Signal");
        this.STUDIO = page.locator("//div[contains(@class,'genomeMenuItem')]");
        this.SUB_MENU = page.locator("//img[contains(@alt,'menu')]");
        this.ACCOUNT_NAME = page.locator("//div[@class='accountnme']");
        this.ACCOUNT_SEARCH = page.locator("//div[@id='accountSitcher']/input[@placeholder='Search']");
        this.ACCOUNT_ITEM = page.locator("//div[@id='accountSwitcher']//div[@class='item']");
        this.STUDIO_TITLE = page.locator("//div[text()='Studio']");
        this.CAMPAIGN_PAGE_TEXT = page.locator("//span[contains(text(),'Campaigns')]");
        this.SPINNER = page.locator("//div[contains(text(),'Loading...')]");
        this.OPTIONS_HEADER = page.locator("//div[contains(@class, 'options-header')]");
        this.CREATE_CAMPAIGN = page.locator("//button[text()='Create a Campaign']");
        this.VERIFY_CAMPAIGN_PAGE = page.locator("//div[text()='Create New Campaign']");
        this.SEARCH_ADVERTISER = page.locator("//label[text()='Advertiser']/following-sibling::div//input");
        this.SELECT_ADVERTISER = page.getByText("");
        this.CAMPAIGN_NAME = page.locator("//input[@placeholder='Campaign Name']");
        this.CAMPAIGN_TYPE = page.locator("//label[contains(text(),'Campaign Type')]/following-sibling::div//button");
        this.BUDGET = page.locator("//input[@id='budgetcap']");
        this.SAVE_CAMPAIGN = page.locator("//span[text()='Save']");
        this.CAMPAIGN_SUCCESS = page.locator("//div[@aria-label='Success!']/following-sibling::div[@role='alert' and contains(text(),'Campaign')]");
        this.CAMPAIGN_DASHBOARD = page.locator("//span[@class='breadCrumbRoot']");
        this.LIFE_TIME_FILTER = page.locator("//button[@data-title='Lifetime']");
        this.CAMPAIGN_ENTRIES = page.locator("//div[contains(@class,'name-section-wrapper')]");
        this.VERIFY_LINE_ITEM_PAGE = page.locator("//div[text()='New Line Item']");
        this.LINE_ITEM_NAME = page.locator("//input[@placeholder='Line Item Name']");
        this.LINE_ITEM_BUDGET = page.locator("//input[contains(@class,'gaFlightBudget')]");
        this.ENABLE_LINE_ITEM = page.locator("//sui-checkbox[@class='toggle ui checkbox ng-untouched ng-pristine ng-valid']");
        this.SAVE_LINE_ITEM = page.locator("//span[text()='Save']");
        this.LINE_ITEM_SUCCESS = page.locator("//div[@aria-label='Success!']/following-sibling::div[@role='alert' and contains(text(),'Lineitem')]");
        this.PLACEMENT_ID = page.locator("//label[contains(text(),'PlacementId')]/following-sibling::input");
        this.VERIFY_TACTIC_CREATIVES_PAGE = page.locator("//div[text()='Creative(s)']");
        this.TACTIC_SETTINGS_SUCCESS = page.locator("//div[@aria-label='Success!']");
        this.SAVE_TACTIC_SETTINGS = page.locator("//span[text()='Save']");
        this.SEARCH_RULE_TYPE = page.locator("//input[@name='search']");
        this.SELECT_RULE_TYPE = page.locator("(//a[@classname='target-tooltip'])[1]");
        this.SELECT_OPTION = page.locator("(//div[contains(@class,'include-default')])[1]");
        this.RULE_TYPE_OK_BUTTON = page.locator("//button[@class='ui primary button okButton' and normalize-space(text())='Ok']");
        this.RULE_TYPE_CLOSE = page.locator("//div[contains(@class,'close_icon')]");
        this.SELECT_CHANNEL = page.locator("(//div[@id='billingTypeDropdown'])[1]");
        this.VERIFY_TACTIC_SETTINGS_PAGE = page.locator("//div[text()='Bid Strategy']");
        this.TACTIC_DETAILS_SUCCESS = page.locator("//div[@aria-label='Success!']/following-sibling::div[@role='alert' and contains(text(),'Tactic')]");
        this.SAVE_TACTIC_DETAILS = page.locator("//span[text()='Save']");
        this.ADD_CUSTOM_FIELD = page.locator("//span[contains(text(),'Add Custom Field')]");
        this.TACTIC_NAME = page.locator("//input[@placeholder='Tactic Name' or @placeholder='Ad Group Name']");
        this.VERIFY_TACTIC_DETAILS_PAGE = page.locator("//div[text()='New Tactic']");
        this.SEARCH_CREATIVE = page.locator("//input[contains(@class, 'gaTableSearch')]");
        this.CLICK_SEARCH = page.locator("//div[contains(@class,'gaTableSearchBtn')]");
        this.ASSIGN_CREATIVE_OK_BUTTON = page.locator("//button[@class='ui primary button okButton' and normalize-space(text())='Ok']");
        this.ENABLE_CREATIVE = page.locator("//sui-checkbox[@class='toggle ui checkbox ng-untouched ng-pristine ng-valid']");
        this.SAVE_TACTIC_CREATIVES = page.locator("//span[text()='Save']");
        this.TACTIC_CREATIVE_SUCCESS = page.locator("//div[@aria-label='Success!']");
        this.NAVIGATE_TO_CAMPAIGN_DASHBOARD = page.locator("//div[contains(@class,'campaign-tile')]");
        this.VERIFY_CAMPAIGN_RUNNING = page.locator("//span[@class='status-label running']");
        this.ASSIGN_CREATIVE_TITLE = page.locator("//div[contains(text(),'Assign Creatives')]");
        this.CREATIVE_STATUS = page.locator("//td[contains(@class,'status-label')]");
        this.CLEAR_SEARCH_BOX = page.locator("//div[contains(@class,'clear-search-close')]");
        this.CREATIVES_TABLE = page.locator("//div[@id='parentTable']");
        this.FAVORITE_ONLY_CHECKBOX = page.locator("//sui-checkbox[contains(@class,'gaFavoritesOnly')]");
        this.HIDE_FINISHED_CHECKBOX = page.locator("//label[contains(text(),'Hide Finished')]/ancestor::sui-checkbox");
        this.GROUP_BY_CAMPAIGN_RADIO_BUTTON = page.locator("//i[contains(@class,'gaGroupCampaigns')]");
        this.CLICK_SETTINGS = page.locator("//i[@class='icon gearIcon']");
        this.SEARCH_CAMPAIGN = page.locator("//input[@placeholder='Search' and contains(@class, 'gaTableSearch')]");
        this.CLICK_CAMPAIGN_SEARCH = page.locator("//div[contains(@class,'gaTableSearchBtn')]");
        this.EXPAND_CREATED_LINE_ITEM = page.locator("//div[contains(@class,'campaignExpand')]/div[contains(@class,'collapsed-thin')]");
        this.VERIFY_CREATED_TACTIC = page.locator("//span[contains(@class,'tactic-name')]");
        this.SUB_TITLE_AFTER_CAMPAIGN_SEARCH = page.locator("//div[contains(@class,'sub-title') and contains(text(),'Line items, 1 Campaigns')]");
        this.FILTER_APPLIED_ICON = page.locator("//div[contains(@class,'filterApplied')]");
        this.RESET_FILTER_ICON = page.locator("//span[contains(text(),'Reset All Filters')]");
        this.NO_CAMPAIGN_AVAILABLE_TEXT = page.locator("//p[contains(text(), 'No campaigns matching filtering criteria found')]");
        this.PRE_LOADER = page.locator("//div[contains(@class,'preloader')]");
    }

    public void navigateToUrl(String url) {
        this.page.navigate(url); // Standard navigation doesn't usually need healing
    }

    public void enterUsername(String userName) {
        healingActions.safeFill(USERNAME, userName, "The username input field on Login page");
    }

    public void enterPassword(String password) {
        healingActions.safeFill(PASSWORD, password, "The password input field on Login page");
    }

    public void clickLogin() {
        healingActions.safeClick(LOGIN_BUTTON, "The Log In button");
    }

    public void navigateToLife() {
        healingActions.safeClick(LIFE, "The 'Life' link available on the Admin Dashboard");
        healingActions.safeWaitUntilSpinnerHidden(SPINNER, "Loading spinner after clicking Life link");
    }

    public void navigateToHCP() {
        healingActions.safeClick(SIGNAL, "The 'Signal' link for HCP365");
    }

    public void navigateToStudio() {
        healingActions.safeWaitUntilLocatorVisible(SUB_MENU, "The hamburger/side menu icon to access navigation");
        healingActions.safeClick(SUB_MENU, "The hamburger/side menu icon");
        healingActions.safeWaitUntilLocatorVisible(STUDIO, "The Studio menu item in the side menu");
        healingActions.safeClick(STUDIO, "The Studio menu item");
        healingActions.safeWaitUntilLocatorVisible(STUDIO_TITLE, "The Studio page title to confirm navigation");
    }

    public void selectAccount(String account) {
        healingActions.safeWaitUntilLocatorVisible(OPTIONS_HEADER, "The campaign table header options including the filter funnel, gear icons and Lifetime button");
        Locator safeLocator = healingActions.safeLocate(ACCOUNT_NAME, "The account name or username like profile name on top right corner of the page");
        if (safeLocator.innerText().contains("buyer2")) {
            safeLocator.click();
            healingActions.safeFill(ACCOUNT_SEARCH, account, "The Search input field");
            page.waitForLoadState(LoadState.LOAD);
            healingActions.safeClick(ACCOUNT_ITEM, "The search result item containing the text " + account);
        }
        healingActions.safeWaitUntilSpinnerHidden(SPINNER, "Loading spinner after selecting account");
    }

    public String isCampaignDashboardVisibleWithTitle(String text) {
        healingActions.safeWaitUntilSpinnerHidden(SPINNER, "Loading spinner");
        healingActions.safeWaitUntilSpinnerHidden(PRE_LOADER, "Pre Loader spinner");
        Locator safeLocator = healingActions.safeLocate(CAMPAIGN_PAGE_TEXT, "Campaigns page title");
        page.waitForCondition(() -> safeLocator.filter(new Locator.FilterOptions().setHasText(text)).count() == 1);
        return safeLocator.innerText();
    }

    public void createCampaign() {
        CREATE_CAMPAIGN.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public String verifyCampaignText() {
        return VERIFY_CAMPAIGN_PAGE.innerText();
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.fill(advertiser);
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
    }

    public void enterCampaignName(String campaignNameRandom) {
        CAMPAIGN_NAME.fill(campaignNameRandom);
    }

    public void setCampaignType(String campaignType) {
        Locator campaignTypeButton = CAMPAIGN_TYPE.filter(new Locator.FilterOptions().setHasText(campaignType));
        campaignTypeButton.click();
    }

    public void enterBudget(String budget) {
        BUDGET.fill(budget);
        page.keyboard().press("Tab");
    }

    public void saveCampaign() {
        SAVE_CAMPAIGN.click();
    }

    public String campaignSuccess() {
        String successMessage = CAMPAIGN_SUCCESS.innerText().trim();
        waitUtility.waitForLocatorHidden(CAMPAIGN_SUCCESS);
        return successMessage;
    }

    public void navigateToCampaignDashboard() {
        CAMPAIGN_DASHBOARD.click();
        waitUtility.waitForLocatorVisible(CAMPAIGN_DASHBOARD);
        if (LIFE_TIME_FILTER.getAttribute("class").contains("inactive")) {
            LIFE_TIME_FILTER.click();
            waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        }
    }


    public String verifyLineItemText() {
        return VERIFY_LINE_ITEM_PAGE.innerText();
    }

    public void enterLineItemName(String lineItemName) {
        LINE_ITEM_NAME.fill(lineItemName);
    }

    public void enterLineItemBudget(String lineBudget) {
        int count = LINE_ITEM_BUDGET.count();
        LINE_ITEM_BUDGET.nth(count - 1).fill(lineBudget);
    }

    public void enableLineItem() {
        ENABLE_LINE_ITEM.click();
    }

    public void saveLineItem() {
        SAVE_LINE_ITEM.click();
    }

    public String lineItemSuccess() {
        String successMessage = LINE_ITEM_SUCCESS.innerText().trim();
        waitUtility.waitUntilSpinnerHidden();
        waitUtility.waitForLocatorHidden(LINE_ITEM_SUCCESS);
        return successMessage;
    }

    public void isPlacementIdAvailable(String lineItemNameRandom) {
        if (PLACEMENT_ID.isVisible()) {
            PLACEMENT_ID.fill(lineItemNameRandom);
        }
    }

    public String verifyTacticDetailsText() {
        return VERIFY_TACTIC_DETAILS_PAGE.innerText();
    }


    public void enterTacticName(String tacticName) {
        waitUtility.waitForLocatorVisible(ADD_CUSTOM_FIELD);
        TACTIC_NAME.fill(tacticName);
    }

    public void saveTacticDetails() {
        SAVE_TACTIC_DETAILS.click();
    }

    public String tacticDetailsSuccess() {
        String successMessage = TACTIC_DETAILS_SUCCESS.innerText().trim();
        waitUtility.waitUntilSpinnerHidden();
        waitUtility.waitForLocatorHidden(TACTIC_DETAILS_SUCCESS);
        return successMessage;
    }

    public String verifyTacticSettingsText() {
        return VERIFY_TACTIC_SETTINGS_PAGE.innerText();
    }

    public void selectChannel(String channel) {
        page.waitForLoadState(LoadState.LOAD);
        if (SELECT_CHANNEL.isVisible()) {
            SELECT_CHANNEL.click();
            SELECT_CHANNEL.locator("text=" + channel).first().click();
        }
    }

    public void clickOnIcon(String iconName) {
        this.page.getByText(iconName.trim()).click();
    }

    public void selectRuleType(String ruleType) {
        SEARCH_RULE_TYPE.fill(ruleType);
        SEARCH_RULE_TYPE.press("Enter");
        SELECT_RULE_TYPE.click();
        SELECT_OPTION.click();
        clickOk();
        clickClose();
    }

    public void clickOk() {
        RULE_TYPE_OK_BUTTON.click();
    }

    public void clickClose() {
        RULE_TYPE_CLOSE.click();
    }

    public void saveTacticSettings() {
        SAVE_TACTIC_SETTINGS.click();
    }

    public String tacticSettingsSuccess() {
        return TACTIC_SETTINGS_SUCCESS.innerText();
    }

    public String verifyTacticCreativesText() {
        return VERIFY_TACTIC_CREATIVES_PAGE.innerText();
    }

    public void assignCreatives(String creative) {
        Locator selectCreative = page.locator(String.format("//div[contains(text(),'%s')]/ancestor::td/preceding-sibling::td/sui-checkbox", creative));
        ASSIGN_CREATIVE_TITLE.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SEARCH_CREATIVE.fill(creative);
        CLICK_SEARCH.click();
        waitUtility.waitForLocatorVisible(CREATIVES_TABLE);
        if (!selectCreative.first().isVisible()) {
            CLEAR_SEARCH_BOX.click();
            waitUtility.waitForLocatorVisible(CREATIVES_TABLE);
            for (int i = 0; i < CREATIVE_STATUS.count(); i++) {
                if (CREATIVE_STATUS.nth(i).innerText().equalsIgnoreCase("Approved")) {
                    selectCreative = page.locator(String.format("//div[contains(@class,'firsttablewrapper')]//tbody//tr[%d]//sui-checkbox", i + 1));
                    break;
                }
            }
        }
        selectCreative.first().scrollIntoViewIfNeeded();
        if (!selectCreative.first().getAttribute("class").contains("checked")) selectCreative.first().click();
        ASSIGN_CREATIVE_OK_BUTTON.click();
    }

    public void enableCreative() {
        ENABLE_CREATIVE.click();
    }

    public void saveTacticCreatives() {
        SAVE_TACTIC_CREATIVES.click();
    }

    public String tacticCreativesSuccess() {
        return TACTIC_CREATIVE_SUCCESS.innerText();
    }

    public void navigateBackToCampaignDashboard() {
        NAVIGATE_TO_CAMPAIGN_DASHBOARD.click();
    }

    public String verifyCampaignRunning() {
        return VERIFY_CAMPAIGN_RUNNING.innerText();
    }

    public void resetFiltersIfApplied() {
        if (FILTER_APPLIED_ICON.isVisible()) {
            FILTER_APPLIED_ICON.click();
            RESET_FILTER_ICON.click();
            waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        }
    }

    public void searchCreatedCampaign(String createdCampaign) {
        waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        ensureCampaignRadioBtnSelected();
        unselectFavoriteCheckboxIfSelected();
        unselectHideFinishedCheckboxIfSelected();
        resetFiltersIfApplied();
        while (true) {
            SEARCH_CAMPAIGN.fill(createdCampaign);
            CLICK_CAMPAIGN_SEARCH.click();
            waitUtility.waitUntilPreLoaderHidden();
            if (SUB_TITLE_AFTER_CAMPAIGN_SEARCH.isVisible()) {
                break;
            }
            if (NO_CAMPAIGN_AVAILABLE_TEXT.isVisible()) {
                break;
            }
        }
    }

    public void ensureCampaignRadioBtnSelected() {
        CLICK_SETTINGS.click();
        if (!GROUP_BY_CAMPAIGN_RADIO_BUTTON.getAttribute("class").contains("groupingMenuRadioSelected")) {
            GROUP_BY_CAMPAIGN_RADIO_BUTTON.click();
            waitUtility.waitUntilPreLoaderHidden();
            waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        } else {
            page.keyboard().press("Escape");
        }
    }

    public void unselectFavoriteCheckboxIfSelected() {
        if (FAVORITE_ONLY_CHECKBOX.getAttribute("class").contains("checked")) {
            FAVORITE_ONLY_CHECKBOX.click();
            waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
        }
    }

    public void unselectHideFinishedCheckboxIfSelected() {
        if (HIDE_FINISHED_CHECKBOX.getAttribute("class").contains("checked")) {
            HIDE_FINISHED_CHECKBOX.click();
            waitUtility.waitForLocatorVisible(CAMPAIGN_ENTRIES.last());
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
        }
    }

    public String verifyCreatedTactic() {
        page.waitForLoadState();
        return VERIFY_CREATED_TACTIC.innerText();
    }

}
