package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.CommonUtils;
import utils.WaitUtility;

import java.util.List;

public class PMP {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    private final Page page;
    private final Locator VERIFY_TACTIC_SETTINGS_PAGE;
    private final Locator ADD_TARGETING_RULE;
    private final Locator NEW_TARGETING_RULE;
    private final Locator SAVE_TACTIC_SETTINGS;
    private final Locator SEARCH_RULE_TYPE;
    private final Locator SELECT_RULE_TYPE;
    private final Locator ALLDEALS_PANEL;
    private final Locator PRIVATE_DEALS_TAB;
    private final Locator PREMIUM_DEALS_TAB;
    private final Locator TOOLTIP_TEXT;
    private final Locator OK_BUTTON;
    private final Locator RULE_TYPE_CLOSE;
    private final Locator SUCCESS_ALERT;
    private final Locator APPLIEDDEAL_PANELLIST;
    private final Locator DEALS_LIST;
    private final Locator ADD_NEWDEAL_BUTTON;
    private final Locator DEAL_SEARCHFILTER;
    private final Locator EXCHANGE_SEARCHFILTER;
    private final Locator EXCHANGE_DROPDOWN;
    private final Locator ENTER_DEALID;
    private final Locator ENTER_DEALNAME;
    private final Locator MEDIATYPE_DROPDOWN;
    private final Locator ENTER_PRICE;
    private final Locator ADDNEWDEAL_LABEL;
    private final Locator MEDIATYPE_VALUE;
    private final Locator DATE_PICKER;
    private final Locator DATE_PICKER_APPLYBTN;
    private final Locator TARGET_APPLIED_DEAL_TOGGLE;
    private final Locator NEWDEAL_SAVEBTN;
    private final Locator TACTICSETTING_TAB;
    private final Locator DELETE_ICON;
    private final Locator PRICING_STRATEGY_DROPDOWN;
    private final Locator PRICE_TEXT;
    private final Locator PERCENTAGE_TEXT;
    private final Locator ADDDEAL_BUTTON;
    private final Locator DEALPRICE_TYPE;
    private final Locator MORE_OPTION;
    private final Locator CLEARING_CPM_COLNAME;
    private final Locator BASE_BIDPRICE;
    private final Locator MAX_BIDPRICE;
    private final Locator SERVE_EVERYWHERE_DAILOG;
    private final Locator SERVE_EVERYWHERE_OKBTN;
    private final Locator ALL_LIFEMARKETPLACE;
    private final Locator ALL_PREMIUMPUBS;
    private final Locator NO_DEAL_TEXT;
    private final Locator SPINNER;

    boolean flag1, flag2 = false;

    public PMP(Page page) {
        this.page = page;
        this.VERIFY_TACTIC_SETTINGS_PAGE = page.locator("//div[text()='Bid Strategy']");
        this.ADD_TARGETING_RULE = page.locator("//span[text()='Add Targeting Rule']");
        this.NEW_TARGETING_RULE = page.locator("//span[text()='New Targeting Rule']");
        this.SEARCH_RULE_TYPE = page.locator("//input[@name='search']");
        this.ALLDEALS_PANEL = page.locator("//div[@class='navbar']//a[contains(@class,'nav-item ui header pointer active')]");
        this.PRIVATE_DEALS_TAB = page.locator("//a[contains(text(),'Private Deals')]");
        this.TOOLTIP_TEXT = page.locator("//div[contains(@class,'ng-tooltip-show')]");
        this.PREMIUM_DEALS_TAB = page.locator("//a[contains(text(),'Life Marketplace Deals')]");
        this.OK_BUTTON = page.locator("//button[contains(@class,'okButton')]");
        this.SAVE_TACTIC_SETTINGS = page.locator("//span[text()='Save']");
        this.SUCCESS_ALERT = page.locator("//div[@aria-label='Success!']");
        this.SELECT_RULE_TYPE = page.locator("(//a[@classname='target-tooltip'])[1]");
        this.APPLIEDDEAL_PANELLIST = page.locator("//div[contains(@class,'appliedDealsList')]");
        this.RULE_TYPE_CLOSE = page.locator("//div[contains(@class,'close_icon')]");
        this.DEALS_LIST = page.locator("//span[contains(@class,'dealName')]");
        this.ADD_NEWDEAL_BUTTON = page.locator("//div[contains(@class,'addNewDealBtn')]");
        this.DEAL_SEARCHFILTER = page.locator("//input[contains(@class,'searchInp')]");
        this.EXCHANGE_SEARCHFILTER = page.locator("//input[contains(@placeholder,'Any Exchange')]");
        this.EXCHANGE_DROPDOWN = page.locator("//app-single-select-dropdown[contains(@placeholder,'Select Exchange')]");
        this.ENTER_DEALID = page.locator("//input[@formcontrolname='pubDealId']");
        this.ENTER_DEALNAME = page.locator("//input[@formcontrolname='dealName']");
        this.MEDIATYPE_DROPDOWN = page.locator("//app-multi-select-checkbox[@placeholder='Select Media Type']");
        this.MEDIATYPE_VALUE = page.locator("//div[@class='item']/sui-checkbox/label");
        this.ENTER_PRICE = page.locator("//input[@id='price']");
        this.ADDNEWDEAL_LABEL = page.locator("//div[@class='addNewDealLabel']");
        this.DATE_PICKER = page.locator("//div[contains(@class,'form-group')]/div[contains(@class,'forecast-datepicker')]");
        this.DATE_PICKER_APPLYBTN = page.locator("//div[contains(@class,'custom-date')]//button[contains(@class,'applyBtn')]");
        this.TARGET_APPLIED_DEAL_TOGGLE = page.locator("//div[contains(@class,'appliedDeal')]/sui-checkbox");
        this.NEWDEAL_SAVEBTN = page.locator("//button[contains(text(),'Save and Add')]");
        this.TACTICSETTING_TAB = page.locator("//a[contains(@class,'gaTabSettings')]");
        this.DELETE_ICON = page.locator("//div[contains(@title,'delete')]");
        this.PRICING_STRATEGY_DROPDOWN = page.locator("//div[contains(@class,'menu transition visible')]/div[contains(@class,'item')]");
        this.PRICE_TEXT = page.locator("//div[contains(@class,'pricingstrategy')]//input[contains(@placeholder,'Price')]");
        this.PERCENTAGE_TEXT = page.locator("//div[contains(@class,'pricingstrategy')]//input[contains(@placeholder,'percentage')]");
        this.ADDDEAL_BUTTON = page.locator("//span[@class='add-action-new-deal']");
        this.DEALPRICE_TYPE = page.locator("//button[@name='DealPriceType']");
        this.MORE_OPTION = page.locator("//label[contains(normalize-space(), 'Curated Markets and Deals')]/ancestor::div[contains(@class, 'target-item')]//div[contains(@class, 'rule-options-icon')]");
        this.CLEARING_CPM_COLNAME = page.locator("//div[text()='CLEARING PRICE']");
        this.BASE_BIDPRICE = page.locator("//input[contains(@placeholder,'Base Bid Price')]");
        this.MAX_BIDPRICE = page.locator("//input[contains(@placeholder,'Max Bid Price')]");
        this.SERVE_EVERYWHERE_DAILOG = page.locator("//div[contains(text(),'Serve Everywhere')]");
        this.SERVE_EVERYWHERE_OKBTN = page.locator("//button[contains(text(),'OK')]");
        this.ALL_LIFEMARKETPLACE = page.locator("//div[contains(@class,'allPremiumPubs')]");
        this.ALL_PREMIUMPUBS = page.locator("//div[contains(@class,'premiumPub')]");
        this.NO_DEAL_TEXT = page.locator("//div[contains(@class,'noDealsTxt')]");
        this.SPINNER = page.locator("//div[contains(text(),'Loading...')]");
    }

    public void navigateToTacticSettingTab() {
        waitUtility.waitForLocatorHidden(SUCCESS_ALERT);
        TACTICSETTING_TAB.click();
    }

    public void verifyTacticSettingsText() {
        VERIFY_TACTIC_SETTINGS_PAGE.innerText();
    }

    public void addNewTargetingRule() {
        if(ADD_TARGETING_RULE.isVisible()){
            ADD_TARGETING_RULE.click();
        }else{
            NEW_TARGETING_RULE.click();
        }
    }

    public void searchTargetingRuleAndSelect(String ruleType) {
        SEARCH_RULE_TYPE.clear();
        SEARCH_RULE_TYPE.type(ruleType);
        SELECT_RULE_TYPE.click();
        page.waitForLoadState(LoadState.LOAD);
    }

    public String verifyPMPDealsPanel(){
        waitUtility.waitForLocatorVisible(ALLDEALS_PANEL);
        return ALLDEALS_PANEL.innerText();
    }

    public void clickDealsTab(String dealType) {
        if(dealType.contains("Private")){
            PRIVATE_DEALS_TAB.click();
        }else{
            PREMIUM_DEALS_TAB.click();
        }
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    public boolean verifyAsignedDealsList() {
        return APPLIEDDEAL_PANELLIST.count() > 0;
    }

    public boolean verifyTargetAppliedDealsToggle(String toggleButton){
        if(toggleButton.equalsIgnoreCase("ON")){
            flag1 = TARGET_APPLIED_DEAL_TOGGLE.getAttribute("class").contains("checked");
        }else if(toggleButton.equalsIgnoreCase("OFF")){
            TARGET_APPLIED_DEAL_TOGGLE.click();
            waitUtility.waitForLocatorVisible(SERVE_EVERYWHERE_DAILOG);
            SERVE_EVERYWHERE_OKBTN.click();
            flag1 = !TARGET_APPLIED_DEAL_TOGGLE.getAttribute("class").contains("checked");
        }
            return flag1;
    }

    public void saveDealsAssigned() {
        OK_BUTTON.click();
        if(RULE_TYPE_CLOSE.isVisible())
            RULE_TYPE_CLOSE.click();
        waitUtility.waitForLocatorVisible(TACTICSETTING_TAB);
    }

    public boolean verifyAssignedDealsOnTactic(String dealName, String toggleButton) {
        if(MORE_OPTION.isVisible())
            MORE_OPTION.click();
        if(toggleButton.equalsIgnoreCase("ON")){
            flag1 = page.locator(String.format("//span[contains(@class,'target-ellipse') and contains(text(),'%s')]", dealName)).isVisible();
            flag2 = page.locator(String.format("//span[contains(@class,'text-content') and contains(text(),'%s')]", dealName)).isVisible();
        } else if (toggleButton.equalsIgnoreCase("OFF")) {
            flag1 = !page.locator(String.format("//span[contains(@class,'target-ellipse') and contains(text(),'%s')]", dealName)).isVisible();
            flag2 = page.locator(String.format("//span[contains(@class,'text-content') and contains(text(),'%s')]", dealName)).isVisible();
        }
        return flag1 && flag2;
    }


    public void saveTacticSettings() {
        SAVE_TACTIC_SETTINGS.click();
    }

    public String verifyTacticIsSaved() {
        return SUCCESS_ALERT.innerText();
    }

    public void selectDealFromListAndAssign(String dealName) {
        DEAL_SEARCHFILTER.fill(dealName);
        waitUtility.waitForElementVisible(String.format("//span[contains(@class,'dealName') and contains(text(),'%s')]", dealName));
        String xpath = String.format("//span[contains(@class,'dealName') and contains(text(),'%s')]/parent::div/preceding-sibling::span", dealName);
        page.locator(xpath).click();
        String assignDealXpath = String.format("//span[contains(@class,'dealName') and contains(text(),'%s')]/ancestor::div[@class='left dealDetails']/following-sibling::div/span[contains(@class,'addDeal')]", dealName);
        String assignedDealXpath = String.format("//span[contains(@class,'dealName') and contains(text(),'%s')]/ancestor::div[@class='left dealDetails']/following-sibling::div/span[contains(@class,'addedDeal')]", dealName);
        if(!page.locator(assignedDealXpath).isVisible())
            page.locator(assignDealXpath).click();
    }

    public boolean verifyPrivateDealsFilterPanel() {
        if(ADD_NEWDEAL_BUTTON.isVisible() && ADD_NEWDEAL_BUTTON.isEnabled())
            flag1 = true;
        if(DEAL_SEARCHFILTER.isVisible() && EXCHANGE_SEARCHFILTER.isVisible())
            flag2 = true;
        return flag1 && flag2;
    }

    public boolean applyFilter(String key, List<String> value) {
        DEAL_SEARCHFILTER.clear();
        EXCHANGE_SEARCHFILTER.clear();
        switch (key){
            case "SearchByName" :
                for(String val : value) {
                    DEAL_SEARCHFILTER.fill(val);
                }
                break;
            case "SearchByExchange" :
                for(String val : value){
                    EXCHANGE_SEARCHFILTER.fill(val);
                }
                break;
        }
        waitUtility.waitForLocatorVisible(DEALS_LIST.first());
        return DEALS_LIST.first().isVisible();
    }

    public void clickAddNewDeals(){
        ADD_NEWDEAL_BUTTON.click();
        waitUtility.waitForLocatorVisible(ADDNEWDEAL_LABEL);
        waitUtility.waitUntilSpinnerHidden();
    }

    public String addAndSaveNewDeals(String exchangeType, String dealID, String dealName, List<String> mediaType, String dealPriceType, String price) {
        EXCHANGE_DROPDOWN.click();
        page.locator(String.format("//div[@menutransition='slide up']/div[contains(text(),'%s')]", exchangeType)).click();
        ENTER_DEALID.fill(dealID);
        ENTER_DEALNAME.fill(dealName);
        MEDIATYPE_DROPDOWN.click();
        CommonUtils.selectAndClickElement(MEDIATYPE_VALUE, mediaType);
        page.keyboard().press("Escape");
        DATE_PICKER.click();
        DATE_PICKER_APPLYBTN.click();
        DEALPRICE_TYPE.locator("text=" + dealPriceType).click();
        ENTER_PRICE.fill(price);
        NEWDEAL_SAVEBTN.click();
        String text = SUCCESS_ALERT.innerText().trim();
        waitUtility.waitForLocatorHidden(SUCCESS_ALERT);
        return text;
    }

    public boolean verifyDeleteIconAndMessage(String message) {
        for(int i=0; i<DELETE_ICON.count(); i++){
            if(DELETE_ICON.nth(i).getAttribute("class").contains("disabled")) {
                flag1 = true;
                DELETE_ICON.nth(i).scrollIntoViewIfNeeded();
                DELETE_ICON.nth(i).click(new Locator.ClickOptions().setForce(true));
                String tooltipText = TOOLTIP_TEXT.innerText();
                if(message.equals(tooltipText))
                    flag2 = true;
            }
        }
        return flag1 && flag2;
    }

    public void verifyPricingStrategyIsEditable(String dealName, String key, List<String> pricingStrategyType) {
        String xpath = String.format("//span[contains(text(),'%s')]/ancestor::div[contains(@class,'nameWrapper')]/following-sibling::div[@class='detailsScrollWrapper']//div[contains(@class,'data-section')]//div[contains(@class,'pricingstrategy')]/div",dealName);
        page.locator(xpath).first().scrollIntoViewIfNeeded();
        CLEARING_CPM_COLNAME.evaluate("el => el.scrollIntoView({ inline: 'end', behavior: 'auto' })");
        page.locator(xpath).first().click();
        PRICING_STRATEGY_DROPDOWN.locator("text=" + key).click();
        if (key.equalsIgnoreCase("Flat")) {
            PRICE_TEXT.fill(pricingStrategyType.get(0));
        } else if (key.equalsIgnoreCase("% above floor")) {
            PERCENTAGE_TEXT.fill(pricingStrategyType.get(0));
        }
        saveTacticSettings();
        verifyTacticIsSaved();
        waitUtility.waitForLocatorHidden(SUCCESS_ALERT);
    }

    public boolean applyDealsFromDealsSection(String dealType, String exchangeType, String dealID, String dealName, List<String> mediaType, String dealPriceType, String price, String toggleButton) {
        ADDDEAL_BUTTON.click();
        waitUtility.waitUntilSpinnerHidden();
        clickDealsTab(dealType);
        clickAddNewDeals();
        addAndSaveNewDeals(exchangeType, dealID, dealName, mediaType, dealPriceType, price);
        selectDealFromListAndAssign(dealName);
        saveDealsAssigned();
        if(toggleButton.equalsIgnoreCase("ON"))
            MORE_OPTION.scrollIntoViewIfNeeded();
        return verifyAssignedDealsOnTactic(dealName, toggleButton);
    }

    public boolean verifyBaseAndMaxPriceIsEditable(String baseBidPrice, String maxBidPrice) {
        BASE_BIDPRICE.scrollIntoViewIfNeeded();
        if(BASE_BIDPRICE.isEditable() && MAX_BIDPRICE.isEditable()){
            BASE_BIDPRICE.fill(baseBidPrice);
            MAX_BIDPRICE.fill(maxBidPrice);
            flag1 = true;
        }
        return flag1;
    }

    public boolean verifyAllPremiumHubsOnMarketPlace(List<String> premiumHubsList) {
        waitUtility.waitForLocatorVisible(ALL_LIFEMARKETPLACE);
        for (int i = 0; i < ALL_PREMIUMPUBS.count(); i++) {
            String classAttr = ALL_PREMIUMPUBS.nth(i).getAttribute("class");
            if (classAttr != null) {
                for (String hub : premiumHubsList) {
                    if (classAttr.contains(hub)) {
                        ALL_PREMIUMPUBS.nth(i).click();

                        boolean dealVisible = false, noDealVisible = false;
                        try {
                            waitUtility.waitForLocatorVisible(DEALS_LIST.first());
                            dealVisible = true;
                        } catch (Exception ignored) {}
                        if (!dealVisible) {
                            try {
                                waitUtility.waitForLocatorVisible(NO_DEAL_TEXT.first());
                                noDealVisible = true;
                            } catch (Exception ignored) {}
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

