package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import factory.DriverFactory;
import utils.CommonUtils;
import utils.WaitUtility;

import java.util.Collections;
import java.util.List;

public class PMP {
    private final Page page;
    private final Locator VERIFY_TACTIC_SETTINGS_PAGE;
    private final Locator ADD_TARGETING_RULE;
    private final Locator NEW_TARGETING_RULE;
    private final Locator SAVE_TACTIC_SETTINGS;
    private final Locator SEARCH_RULE_TYPE;
    private final Locator SELECT_RULE_TYPE;
    private final Locator ALL_DEALS_PANEL;
    private final Locator PRIVATE_DEALS_TAB;
    private final Locator PREMIUM_DEALS_TAB;
    private final Locator TOOLTIP_TEXT;
    private final Locator OK_BUTTON;
    private final Locator RULE_TYPE_CLOSE;
    private final Locator SUCCESS_ALERT;
    private final Locator APPLIED_DEAL_PANEL_LIST;
    private final Locator DEALS_LIST;
    private final Locator ADD_NEW_DEAL_BUTTON;
    private final Locator DEAL_SEARCH_FILTER;
    private final Locator EXCHANGE_SEARCH_FILTER;
    private final Locator EXCHANGE_DROPDOWN;
    private final Locator ENTER_DEAL_ID;
    private final Locator ENTER_DEAL_NAME;
    private final Locator MEDIA_TYPE_DROPDOWN;
    private final Locator ENTER_PRICE;
    private final Locator ADD_NEW_DEAL_LABEL;
    private final Locator MEDIA_TYPE_VALUE;
    private final Locator DATE_PICKER;
    private final Locator DATE_PICKER_APPLY_BUTTON;
    private final Locator TARGET_APPLIED_DEAL_TOGGLE;
    private final Locator NEW_DEAL_SAVE_BUTTON;
    private final Locator TACTIC_SETTING_TAB;
    private final Locator DEALS_DELETE_ICON;
    private final Locator PRICE_TEXT;
    private final Locator ADD_DEAL_BUTTON;
    private final Locator DEAL_PRICE_TYPE;
    private final Locator MORE_OPTION;
    private final Locator DEAL_TYPE_COLUMN_NAME;
    private final Locator BASE_BID_PRICE;
    private final Locator MAX_BID_PRICE;
    private final Locator SERVE_EVERYWHERE_DIALOG;
    private final Locator SERVE_EVERYWHERE_OK_BUTTON;
    private final Locator ALL_LIFE_MARKET_PLACE;
    private final Locator ALL_PREMIUM_PUBS;
    private final Locator NO_DEAL_TEXT;
    private final Locator ADVERTISER;
    private final Locator ADVERTISER_VALUES;
    private final Locator CURATOR_DROPDOWN;
    private final Locator CURATOR_VALUES;
    private final Locator GA_SURVEY_DIALOG;
    private final Locator GA_SURVEY_CLOSE_BUTTON;
    private final Locator CURATED_MARKETS_AND_DEALS_TITLE;
    private final Locator ALERT;
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    boolean flag1, flag2 = false;

    public PMP(Page page) {
        this.page = page;
        this.VERIFY_TACTIC_SETTINGS_PAGE = page.locator("//div[text()='Bid Strategy']");
        this.ADD_TARGETING_RULE = page.locator("//span[text()='Add Targeting Rule']");
        this.NEW_TARGETING_RULE = page.locator("//span[text()='New Targeting Rule']");
        this.SEARCH_RULE_TYPE = page.locator("//input[@name='search']");
        this.ALL_DEALS_PANEL = page.locator("//div[@class='navbar']//a[contains(@class,'nav-item ui header pointer active')]");
        this.PRIVATE_DEALS_TAB = page.locator("//a[contains(text(),'Private Deals')]");
        this.TOOLTIP_TEXT = page.locator("//div[contains(@class,'ng-tooltip-show')]");
        this.PREMIUM_DEALS_TAB = page.locator("//a[contains(text(),'Life Marketplace Deals')]");
        this.OK_BUTTON = page.locator("//button[contains(@class,'okButton')]");
        this.SAVE_TACTIC_SETTINGS = page.locator("//span[text()='Save']");
        this.SUCCESS_ALERT = page.locator("//div[@aria-label='Success!']");
        this.SELECT_RULE_TYPE = page.locator("(//a[@classname='target-tooltip'])[1]");
        this.APPLIED_DEAL_PANEL_LIST = page.locator("//div[contains(@class,'appliedDealsList')]");
        this.RULE_TYPE_CLOSE = page.locator("//div[contains(@class,'close_icon')]");
        this.DEALS_LIST = page.locator("//span[contains(@class,'dealName')]");
        this.ADD_NEW_DEAL_BUTTON = page.locator("//div[contains(@class,'addNewDealBtn')]");
        this.DEAL_SEARCH_FILTER = page.locator("//input[contains(@class,'searchInp')]");
        this.EXCHANGE_SEARCH_FILTER = page.locator("//input[contains(@placeholder,'Any Exchange')]");
        this.EXCHANGE_DROPDOWN = page.locator("//app-single-select-dropdown[contains(@placeholder,'Select Exchange')]");
        this.ENTER_DEAL_ID = page.locator("//input[@formcontrolname='pubDealId']");
        this.ENTER_DEAL_NAME = page.locator("//input[@formcontrolname='dealName']");
        this.MEDIA_TYPE_DROPDOWN = page.locator("//app-multi-select-checkbox[@placeholder='Select Media Type']");
        this.MEDIA_TYPE_VALUE = page.locator("//div[@class='item']/sui-checkbox/label");
        this.ENTER_PRICE = page.locator("//input[@id='price']");
        this.ADD_NEW_DEAL_LABEL = page.locator("//div[@class='addNewDealLabel']");
        this.DATE_PICKER = page.locator("//div[contains(@class,'form-group')]/div[contains(@class,'forecast-datepicker')]");
        this.DATE_PICKER_APPLY_BUTTON = page.locator("//div[contains(@class,'custom-date')]//button[contains(@class,'applyBtn')]");
        this.TARGET_APPLIED_DEAL_TOGGLE = page.locator("//div[contains(@class,'appliedDeal')]/sui-checkbox");
        this.NEW_DEAL_SAVE_BUTTON = page.locator("//div[contains(@class,'addDealFooter')]//button[contains(@class,'okButton')]");
        this.TACTIC_SETTING_TAB = page.locator("//a[contains(@class,'gaTabSettings')]");
        this.DEALS_DELETE_ICON = page.locator("//span[text()='Curated Markets and Deals']/parent::label//following-sibling::div//div[contains(@title,'delete')]");
        this.PRICE_TEXT = page.locator("//div[contains(@class,'pricingstrategy')]//input[contains(@placeholder,'Price')]");
        this.ADD_DEAL_BUTTON = page.locator("//span[@class='add-action-new-deal']");
        this.DEAL_PRICE_TYPE = page.locator("//button[@name='DealPriceType']");
        this.MORE_OPTION = page.locator("//label[contains(normalize-space(), 'Curated Markets and Deals')]/ancestor::div[contains(@class, 'target-item')]//div[contains(@class, 'rule-options-icon')]");
        this.DEAL_TYPE_COLUMN_NAME = page.locator("//div[translate(normalize-space(text()), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ') = 'DEAL TYPE']");
        this.BASE_BID_PRICE = page.locator("//input[contains(@placeholder,'Base Bid Price')]");
        this.MAX_BID_PRICE = page.locator("//input[contains(@placeholder,'Max Bid Price')]");
        this.SERVE_EVERYWHERE_DIALOG = page.locator("//div[contains(text(),'Serve Everywhere')]");
        this.SERVE_EVERYWHERE_OK_BUTTON = page.locator("//button[contains(text(),'OK')]");
        this.ALL_LIFE_MARKET_PLACE = page.locator("//div[contains(@class,'allPremiumPubs')]");
        this.ALL_PREMIUM_PUBS = page.locator("//div[contains(@class,'premiumPub')]");
        this.NO_DEAL_TEXT = page.locator("//div[contains(@class,'noDealsTxt')]");
        this.ADVERTISER = page.locator("//label[contains(text(),'Advertiser')]/following-sibling::app-multi-select");
        this.ADVERTISER_VALUES = page.locator("//label[contains(text(),'Advertiser')]/following-sibling::app-multi-select//div[@class='menu transition visible']//span");
        this.CURATOR_DROPDOWN = page.locator("//app-single-select-dropdown[@placeholder='Select Curator']");
        this.CURATOR_VALUES = page.locator("//app-single-select-dropdown[@placeholder='Select Curator']//div[contains(@class,'item')]");
        this.GA_SURVEY_DIALOG = page.locator("//div[contains(text(),'Please rate the campaign setup process')]");
        this.GA_SURVEY_CLOSE_BUTTON = page.locator("//div[contains(@class,'btn-close .gaSurveyClose')]");
        this.CURATED_MARKETS_AND_DEALS_TITLE = page.locator("//span[text()='Curated Markets and Deals']");
        this.ALERT = page.locator("//div[@role='alert']");
    }

    public void navigateToTacticSettingTab() {
        waitUtility.waitForLocatorHidden(SUCCESS_ALERT);
        TACTIC_SETTING_TAB.click();
    }

    public void verifyTacticSettingsText() {
        VERIFY_TACTIC_SETTINGS_PAGE.innerText();
    }

    public void addNewTargetingRule() {
        if (ADD_TARGETING_RULE.isVisible()) {
            ADD_TARGETING_RULE.click();
        } else {
            NEW_TARGETING_RULE.click();
        }
    }

    public void searchTargetingRuleAndSelect(String ruleType) {
        SEARCH_RULE_TYPE.clear();
        SEARCH_RULE_TYPE.type(ruleType);
        SELECT_RULE_TYPE.click();
        page.waitForLoadState(LoadState.LOAD);
    }

    public String verifyPMPDealsPanel() {
        waitUtility.waitForLocatorVisible(ALL_DEALS_PANEL);
        return ALL_DEALS_PANEL.innerText();
    }

    public void clickDealsTab(String dealType) {
        if (dealType.contains("Private")) {
            PRIVATE_DEALS_TAB.click();
        } else {
            PREMIUM_DEALS_TAB.click();
        }
    }

    public boolean verifyAsignedDealsList() {
        return APPLIED_DEAL_PANEL_LIST.count() > 0;
    }

    public boolean verifyTargetAppliedDealsToggle(String toggleButton) {
        if (toggleButton.equalsIgnoreCase("ON")) {
            flag1 = TARGET_APPLIED_DEAL_TOGGLE.getAttribute("class").contains("checked");
        } else if (toggleButton.equalsIgnoreCase("OFF")) {
            TARGET_APPLIED_DEAL_TOGGLE.click();
            waitUtility.waitForLocatorVisible(SERVE_EVERYWHERE_DIALOG);
            SERVE_EVERYWHERE_OK_BUTTON.click();
            flag1 = !TARGET_APPLIED_DEAL_TOGGLE.getAttribute("class").contains("checked");
        }
        return flag1;
    }

    public void saveDealsAssigned() {
        OK_BUTTON.click();
        if (RULE_TYPE_CLOSE.isVisible()) RULE_TYPE_CLOSE.click();
        waitUtility.waitUntilSpinnerHidden();
        waitUtility.waitForLocatorVisible(TACTIC_SETTING_TAB);
    }

    public boolean verifyAssignedDealsOnTactic(String dealName) {
        if (MORE_OPTION.isVisible()) MORE_OPTION.click();
        String ellipseXPath = String.format("//span[contains(@class,'target-ellipse') and contains(text(),'%s')]", dealName);
        String textContentXPath = String.format("//span[contains(@class,'text-content') and contains(text(),'%s')]", dealName);
        flag1 = page.locator(ellipseXPath).first().isVisible();
        flag2 = page.locator(textContentXPath).first().isVisible();
        return flag1 && flag2;
    }

    public void saveTacticSettings() {
        SAVE_TACTIC_SETTINGS.click();
    }

    public String verifyTacticIsSaved() {
        return ALERT.innerText();
    }

    public void selectDealFromListAndAssign(String dealName) {
        DEAL_SEARCH_FILTER.fill(dealName);
        waitUtility.waitForElementVisible(String.format("//span[contains(@class,'dealName') and contains(text(),'%s')]", dealName));
        String xpath = String.format("//span[contains(@class,'dealName') and contains(text(),'%s')]/parent::div/preceding-sibling::span", dealName);
        page.locator(xpath).click();
        String assignDealXpath = String.format("//span[contains(@class,'dealName') and contains(text(),'%s')]/ancestor::div[@class='left dealDetails']/following-sibling::div/span[contains(@class,'addDeal')]", dealName);
        String assignedDealXpath = String.format("//span[contains(@class,'dealName') and contains(text(),'%s')]/ancestor::div[@class='left dealDetails']/following-sibling::div/span[contains(@class,'addedDeal')]", dealName);
        if (!page.locator(assignedDealXpath).isVisible()) page.locator(assignDealXpath).click();
    }

    public boolean verifyPrivateDealsFilterPanel() {
        if (ADD_NEW_DEAL_BUTTON.isVisible() && ADD_NEW_DEAL_BUTTON.isEnabled()) flag1 = true;
        if (DEAL_SEARCH_FILTER.isVisible() && EXCHANGE_SEARCH_FILTER.isVisible()) flag2 = true;
        return flag1 && flag2;
    }

    public boolean applyFilter(String key, List<String> value) {
        DEAL_SEARCH_FILTER.clear();
        EXCHANGE_SEARCH_FILTER.clear();
        switch (key) {
            case "SearchByName":
                for (String val : value) {
                    DEAL_SEARCH_FILTER.fill(val);
                }
                break;
            case "SearchByExchange":
                for (String val : value) {
                    EXCHANGE_SEARCH_FILTER.fill(val);
                }
                break;
        }

        waitUtility.waitForLocatorVisible(DEALS_LIST.last());
        return DEALS_LIST.first().isVisible();
    }

    public void clickAddNewDeals() {
        ADD_NEW_DEAL_BUTTON.click();
        waitUtility.waitForLocatorVisible(ADD_NEW_DEAL_LABEL);
        waitUtility.waitUntilSpinnerHidden();
    }

    public String addAndSaveNewDeals(String exchangeType, String dealID, String dealName, List<String> mediaType, String advertiser, String dealPriceType, String price, String curator) {
        EXCHANGE_DROPDOWN.click();
        page.locator(String.format("//div[@menutransition='slide up']/div[contains(text(),'%s')]", exchangeType)).click();
        ENTER_DEAL_ID.fill(dealID);
        ENTER_DEAL_NAME.fill(dealName);
        MEDIA_TYPE_DROPDOWN.click();
        CommonUtils.selectAndClickElement(MEDIA_TYPE_VALUE, mediaType);
        page.keyboard().press("Escape");
        CURATOR_DROPDOWN.click();
        CommonUtils.selectAndClickElement(CURATOR_VALUES, Collections.singletonList(curator));
        page.keyboard().press("Escape");
        ADVERTISER.click();
        CommonUtils.selectAndClickElement(ADVERTISER_VALUES, Collections.singletonList(advertiser));
        page.keyboard().press("Escape");
        DATE_PICKER.click();
        DATE_PICKER_APPLY_BUTTON.click();
        DEAL_PRICE_TYPE.locator("text=" + dealPriceType).click();
        ENTER_PRICE.fill(price);
        NEW_DEAL_SAVE_BUTTON.click();
        String text = ALERT.innerText().trim();
        waitUtility.waitForLocatorHidden(SUCCESS_ALERT);
        return text;
    }

    public boolean isDeleteIconDisabled() {
        DEALS_DELETE_ICON.scrollIntoViewIfNeeded();
        return DEALS_DELETE_ICON.getAttribute("class").contains("disabled");
    }

    public String fetchMessageOnDeleteIconClick() {
        if (GA_SURVEY_DIALOG.isVisible()) GA_SURVEY_CLOSE_BUTTON.click();
        CURATED_MARKETS_AND_DEALS_TITLE.scrollIntoViewIfNeeded();
        DEALS_DELETE_ICON.click(new Locator.ClickOptions().setForce(true));
        return TOOLTIP_TEXT.textContent().trim();
    }

    public void verifyPricingStrategyIsEditable(String dealName, String pricingStrategyType, String value) {
        String xpath = String.format("//span[contains(text(),'%s')]/ancestor::div[contains(@class,'nameWrapper')]/following-sibling::div[@class='detailsScrollWrapper']//div[contains(@class,'data-section')]//div[contains(@class,'pricingstrategy')]//select", dealName);
        page.locator(xpath).first().scrollIntoViewIfNeeded();
        DEAL_TYPE_COLUMN_NAME.evaluate("el => el.scrollIntoView({ inline: 'end', behavior: 'auto' })");
        page.locator(xpath).first().selectOption(new SelectOption().setLabel(pricingStrategyType));
        if (pricingStrategyType.equalsIgnoreCase("Flat")) PRICE_TEXT.fill(value);
        saveTacticSettings();
        verifyTacticIsSaved();
        waitUtility.waitForLocatorHidden(SUCCESS_ALERT);
    }

    public boolean applyDealsFromDealsSection(String dealType, String exchangeType, String dealID, String dealName, List<String> mediaType, String advertiser, String dealPriceType, String price, String curator) {
        ADD_DEAL_BUTTON.click();
        waitUtility.waitUntilSpinnerHidden();
        clickDealsTab(dealType);
        clickAddNewDeals();
        addAndSaveNewDeals(exchangeType, dealID, dealName, mediaType, advertiser, dealPriceType, price, curator);
        selectDealFromListAndAssign(dealName);
        saveDealsAssigned();
        return verifyAssignedDealsOnTactic(dealName);
    }

    public boolean verifyBaseAndMaxPriceIsEditable(String baseBidPrice, String maxBidPrice) {
        BASE_BID_PRICE.scrollIntoViewIfNeeded();
        if (BASE_BID_PRICE.isEditable() && MAX_BID_PRICE.isEditable()) {
            BASE_BID_PRICE.fill(baseBidPrice);
            MAX_BID_PRICE.fill(maxBidPrice);
            flag1 = true;
        }
        return flag1;
    }

    /* Premium Hub is no longer displayed on the Deals panel. This function is being retained in case the functionality is added again in the future. */
    public boolean verifyAllPremiumHubsOnMarketPlace(List<String> premiumHubsList) {
        waitUtility.waitForLocatorVisible(ALL_LIFE_MARKET_PLACE);
        for (int i = 0; i < ALL_PREMIUM_PUBS.count(); i++) {
            String classAttr = ALL_PREMIUM_PUBS.nth(i).getAttribute("class");
            if (classAttr != null) {
                for (String hub : premiumHubsList) {
                    if (classAttr.contains(hub)) {
                        ALL_PREMIUM_PUBS.nth(i).click();

                        boolean dealVisible = false, noDealVisible = false;
                        try {
                            waitUtility.waitForLocatorVisible(DEALS_LIST.first());
                            dealVisible = true;
                        } catch (Exception ignored) {
                        }
                        if (!dealVisible) {
                            try {
                                waitUtility.waitForLocatorVisible(NO_DEAL_TEXT.first());
                                noDealVisible = true;
                            } catch (Exception ignored) {
                            }
                        }
                        if (dealVisible == noDealVisible) {
                            return false;
                        }
                        break;
                    }
                }
            }
        }
        return true;
    }
}