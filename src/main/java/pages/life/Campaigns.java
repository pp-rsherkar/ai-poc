package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import factory.DriverFactory;
import utils.CommonUtils;
import utils.WaitUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Campaigns {
    private final Page page;
    private final Locator CREATE_CAMPAIGN;
    private final Locator VERIFY_CAMPAIGN_PAGE;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator CAMPAIGN_NAME;
    private final Locator CAMPAIGN_TYPE;
    private final Locator CAMPAIGN_TYPE_REGULAR;
    private final Locator CAMPAIGN_TYPE_SEQUENTIAL;
    private final Locator BUDGET;
    private final Locator SAVE_CAMPAIGN;
    private final Locator CAMPAIGN_DASHBOARD;
    private final Locator CAMPAIGN_SUCCESS;
    private final Locator LIFE_TIME_FILTER;
    private final Locator CAMPAIGN_ENTRIES;
    private final Locator ADVERTISER_DROPDOWN_VALUES;
    private final Locator MANDATORY_FIELD_ERROR;
    private final Locator SEARCH_DRUG;
    private final Locator CAMPAIGN_DESCRIPTION;
    private final Locator BUDGET_STATUS;
    private final Locator MANAGEMENT_FEE;
    private final Locator MANAGEMENT_FEE_OPTIONS;
    private final Locator DOLLAR_TYPE_FEE_INPUT;
    private final Locator PERCENT_TYPE_FEE_INPUT;
    private final Locator ACTION_ITEM_MENU;
    private final Locator SELECTED_ADVERTISER;
    private final Locator SELECTED_DRUG;
    private final Locator CAMPAIGN_DETAILS_TAB;
    private final Locator CLIENT_DROPDOWN;
    private final Locator CLIENT_DROPDOWN_VALUE;
    private final Locator SELECTED_CLIENT_VALUE;
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());

    public Campaigns(Page page) {
        this.page = page;
        this.CREATE_CAMPAIGN = page.locator("//button[text()='Create a Campaign']");
        this.VERIFY_CAMPAIGN_PAGE = page.locator("//div[text()='Create New Campaign']");
        this.SEARCH_ADVERTISER = page.locator("//label[text()='Advertiser']/following-sibling::div//input");
        this.SELECT_ADVERTISER = page.getByText("");
        this.CAMPAIGN_NAME = page.locator("//input[@placeholder='Campaign Name']");
        this.CAMPAIGN_TYPE = page.locator("//label[contains(text(),'Campaign Type')]/following-sibling::div//button");
        this.CAMPAIGN_TYPE_REGULAR = page.locator("//button[text()='Regular']");
        this.CAMPAIGN_TYPE_SEQUENTIAL = page.locator("//button[text()='Sequential']");
        this.BUDGET = page.locator("//input[@id='budgetcap']");
        this.SAVE_CAMPAIGN = page.locator("//span[text()='Save']");
        this.CAMPAIGN_SUCCESS = page.locator("//div[@aria-label='Success!']"); //div[@aria-label='Success!']/following-sibling::div[@role='alert' and contains(text(),'Campaign')]
        this.CAMPAIGN_DASHBOARD = page.locator("//span[@class='breadCrumbRoot']");
        this.LIFE_TIME_FILTER = page.locator("//button[@data-title='Lifetime']");
        this.CAMPAIGN_ENTRIES = page.locator("//div[contains(@class,'name-section-wrapper')]");
        this.ADVERTISER_DROPDOWN_VALUES = page.locator("//input[@placeholder='Select Advertiser']/following-sibling::div[@class='menu transition visible']//div");
        this.MANDATORY_FIELD_ERROR = page.locator("//div[contains(@class,'errorsWrapper')]//p");
        this.CAMPAIGN_DESCRIPTION = page.locator("//textarea[@placeholder='Description']");
        this.SEARCH_DRUG = page.locator("//input[@placeholder='Drug']");
        this.BUDGET_STATUS = page.locator("//label[contains(text(),'Budget Status')]/following-sibling::div//button | //label[contains(text(),'Approval Status')]/following-sibling::div[contains(@class,'display-inlineBlock')]//button");
        this.MANAGEMENT_FEE = page.locator("//label[contains(text(),'Apply Management Fee')]//parent::sui-checkbox");
        this.MANAGEMENT_FEE_OPTIONS = page.locator("//button[contains(@class,'fee-type-button')]");
        this.PERCENT_TYPE_FEE_INPUT = page.locator("//div[contains(@class,'fee-inputs')]//input[contains(@class,'percent-img')]");
        this.DOLLAR_TYPE_FEE_INPUT = page.locator("//div[contains(@class,'fee-inputs')]//input[contains(@class,'doller-img')]");
        this.ACTION_ITEM_MENU = page.locator("//span[contains(@title,'options')]");
        this.SELECTED_ADVERTISER = page.locator("//label[text()='Advertiser']/following-sibling::div//input//following-sibling::span | //label[text()='Advertiser']/following-sibling::span");
        this.SELECTED_DRUG = page.locator("//div[@id='drugLookup']/following-sibling::div//input");
        this.CAMPAIGN_DETAILS_TAB = page.locator("//a[contains(@class,'gaTabDetails') and contains(text(),'Details')]");
        this.CLIENT_DROPDOWN = page.locator("//label[text()='Client']/following-sibling::div[contains(@class,'dropdown')]");
        this.CLIENT_DROPDOWN_VALUE = page.locator("//label[text()='Client']/following-sibling::div[contains(@class,'dropdown')]//div[@class='menu transition visible']/div");
        this.SELECTED_CLIENT_VALUE = page.locator("//label[text()='Client']/following-sibling::div[contains(@class,'dropdown')]//div[@class='text']");
    }

    public void createCampaign() {
        CREATE_CAMPAIGN.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public String campaignDashboard() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        return this.page.title();
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
        CAMPAIGN_TYPE.locator("text = " + campaignType).click();
//        if ("Regular".equals(campaignType)) {
//            CAMPAIGN_TYPE_REGULAR.click();
//        } else if ("Sequential".equals(campaignType)) {
//            CAMPAIGN_TYPE_SEQUENTIAL.click();
//        }
    }

    public void enterBudget(String budget) {
        BUDGET.fill(budget);
        page.keyboard().press("Tab");
    }

    public void saveCampaign() {
        SAVE_CAMPAIGN.click();
        waitUtility.waitUntilSpinnerHidden();
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

    public List<String> fetchAdvertiserList() {
        waitUtility.waitForLocatorVisible(SEARCH_ADVERTISER);
        SEARCH_ADVERTISER.click();
        return ADVERTISER_DROPDOWN_VALUES.allTextContents();
    }

    public List<String> fetchMandatoryFieldsError() {
        return MANDATORY_FIELD_ERROR.allTextContents();
    }

    public String fetchDefaultCampaignType() {
        return fetchDefaultValue(CAMPAIGN_TYPE);
    }

    public void selectDrug(String drugName) {
        SEARCH_DRUG.click();
        SEARCH_DRUG.fill(drugName);
        Locator option = SEARCH_DRUG.locator(String.format("//following-sibling::div//div[contains(@title,'%s')]", drugName));
        option.first().click();
    }

    public String validateCampaignBudgetNumericInput(String budget) {
        enterBudget(budget);
        page.keyboard().press("Tab");
        return BUDGET.innerText();
    }

    public void enterCampaignDescription(String campaignDescription) {
        CAMPAIGN_DESCRIPTION.fill(campaignDescription);
    }

    public List<String> fetchBudgetStatus() {
        return BUDGET_STATUS.allTextContents();
    }

    public String fetchDefaultBudgetStatus() {
        return fetchDefaultValue(BUDGET_STATUS);
    }

    public String fetchDefaultValue(Locator locator) {
        for(int i = 0; i< locator.count(); i++){
            if(locator.nth(i).getAttribute("class") != null && locator.nth(i).getAttribute("class").contains("active"))
                return locator.nth(i).textContent().trim();
        }
        return "";
    }

    public boolean isManagementFeeAvailable() {
        return MANAGEMENT_FEE.isVisible();
    }

    public void clickManagementFee() {
        MANAGEMENT_FEE.click();
    }

    public List<String> fetchManagementFeeOptions() {
        return MANAGEMENT_FEE_OPTIONS.allTextContents();
    }

    public void clickManagementFeeOptionAndEnterData(String managementFeeOption, String percent, String amount) {
        MANAGEMENT_FEE_OPTIONS.locator("text=" + managementFeeOption).click();
        if(PERCENT_TYPE_FEE_INPUT.isVisible())
            PERCENT_TYPE_FEE_INPUT.fill(percent);
        if(DOLLAR_TYPE_FEE_INPUT.isVisible())
            DOLLAR_TYPE_FEE_INPUT.fill(amount);
    }

    public void clickActionItemMenu() {
        ACTION_ITEM_MENU.scrollIntoViewIfNeeded();
        ACTION_ITEM_MENU.click();
    }

    public boolean isGenerateReportOptionAvailable(String reportOption) {
        Locator reportOptionXpath = page.locator(String.format("//app-icon-lable-link[contains(@title,'%s')]//div", reportOption));
        waitUtility.waitForLocatorVisible(reportOptionXpath);
        return reportOptionXpath.isVisible() && !reportOptionXpath.getAttribute("class").contains("disabled");
    }

    public boolean isDeleteOptionAvailable(String deleteOption) {
        Locator reportOptionXpath = page.locator(String.format("//app-icon-lable-link[contains(@title,'%s')]//div", deleteOption));
        waitUtility.waitForLocatorVisible(reportOptionXpath);
        return reportOptionXpath.isVisible() && reportOptionXpath.getAttribute("class").contains("disabled");
    }

    public List<String> fetchCampaignDetails() {
        List<String> enteredData = new ArrayList<>();
        enteredData.add(SELECTED_ADVERTISER.textContent());
        enteredData.add(CAMPAIGN_NAME.inputValue());
        enteredData.add(fetchDefaultValue(CAMPAIGN_TYPE));
        enteredData.add(SELECTED_CLIENT_VALUE.textContent());
        enteredData.add(SELECTED_DRUG.inputValue());
        enteredData.add(BUDGET.inputValue());
        enteredData.add(CAMPAIGN_DESCRIPTION.inputValue());
        enteredData.add(fetchDefaultValue(BUDGET_STATUS));
        enteredData.add(fetchDefaultValue(MANAGEMENT_FEE_OPTIONS));
        if(PERCENT_TYPE_FEE_INPUT.isVisible())
            enteredData.add(PERCENT_TYPE_FEE_INPUT.inputValue());
        if(DOLLAR_TYPE_FEE_INPUT.isVisible())
            enteredData.add(DOLLAR_TYPE_FEE_INPUT.inputValue());
        return enteredData;
    }

    public void clickSavedCampaign(String campaignName) {
        Locator savedCampaignXpath = page.locator(String.format("//div[contains(@class,'campaign-title') and contains(text(),'%s')]", campaignName));
        savedCampaignXpath.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public void clickCampaignDetailsTab() {
        waitUtility.waitForLocatorVisible(CAMPAIGN_DETAILS_TAB);
        CAMPAIGN_DETAILS_TAB.click();
        waitUtility.waitUntilSpinnerHidden();
        waitUtility.waitForLocatorVisible(SELECTED_ADVERTISER);
    }

    public String verifyClientFieldEnabledOrDisabledBasedOnAccount(String clientName) {
        String state;
        boolean isDisabled = CLIENT_DROPDOWN.getAttribute("class").contains("disabled");
        if (!isDisabled) {
            CLIENT_DROPDOWN.scrollIntoViewIfNeeded();
            CLIENT_DROPDOWN.click();
            CLIENT_DROPDOWN_VALUE.filter(new Locator.FilterOptions().setHasText(clientName)).click();
            state = "Enabled";
        } else {
            state = "Disabled";
        }
        return state;
    }
}