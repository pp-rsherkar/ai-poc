package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import factory.DriverFactory;
import utils.WaitUtility;

public class Campaigns {
    private final Page page;
    private final Locator CREATE_CAMPAIGN;
    private final Locator VERIFY_CAMPAIGN_PAGE;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator CAMPAIGN_NAME;
    private final Locator CAMPAIGN_TYPE_REGULAR;
    private final Locator CAMPAIGN_TYPE_SEQUENTIAL;
    private final Locator BUDGET;
    private final Locator SAVE_CAMPAIGN;
    private final Locator CAMPAIGN_DASHBOARD;
    private final Locator CAMPAIGN_SUCCESS;
    private final Locator LIFE_TIME_FILTER;
    private final Locator CAMPAIGN_ENTRIES;
    private final Locator SELECT_CAMPAIGN;
    private final Locator DETAILS_TAB;
    private final Locator TIMES_PER_DROPDOWN;
    private final Locator SCOPE_DROPDOWN;
    private final Locator FREQUENCY_CAP_VALUE;
    private final Locator CUSTOM_FIELD;
    private final Locator GET_FREQUENCY_CAP_TEXT;
    private final Locator SELECT_LINE_ITEM;
    private final Locator FREQUENCY_CAP;
    private final Locator TIMES_PER_HRS_VALUE;
    static WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());

    public Campaigns(Page page) {
        this.page = page;
        this.CREATE_CAMPAIGN = page.locator("//button[text()='Create a Campaign']");
        this.VERIFY_CAMPAIGN_PAGE = page.locator("//div[text()='Create New Campaign']");
        this.SEARCH_ADVERTISER = page.locator("(//input[@placeholder='Select Advertiser'])[1]");
        this.SELECT_ADVERTISER = page.getByText("");
        this.CAMPAIGN_NAME = page.locator("//input[@placeholder='Campaign Name']");
        this.CAMPAIGN_TYPE_REGULAR = page.locator("//button[text()='Regular']");
        this.CAMPAIGN_TYPE_SEQUENTIAL = page.locator("//button[text()='Sequential']");
        this.BUDGET = page.locator("//input[@id='budgetcap']");
        this.SAVE_CAMPAIGN = page.locator("//span[text()='Save']");
        this.CAMPAIGN_SUCCESS = page.locator("//div[@aria-label='Success!']");
        this.CAMPAIGN_DASHBOARD = page.locator("//span[@class='breadCrumbRoot']");
        this.LIFE_TIME_FILTER = page.locator("//button[@data-title='Lifetime']");
        this.CAMPAIGN_ENTRIES = page.locator("//div[contains(@class,'name-section-wrapper')]");
        this.SELECT_CAMPAIGN = page.locator("//div[@class='item-details']");
        this.CUSTOM_FIELD = page.locator("//label[contains(@class,'cmp-form-label')]");
        this.DETAILS_TAB = page.locator("//a[contains(text(),'Details')]");
        this.TIMES_PER_DROPDOWN = page.locator("//div[contains(@class,'dropdown-wrapper ui field noMargin')]");
        this.SCOPE_DROPDOWN = page.locator("//div[contains(@class,'crossDevice-dropdown')]");
        this.FREQUENCY_CAP_VALUE = page.locator("//input[@id='windowLimit' or @id='freqWindowLimit']");
        this.GET_FREQUENCY_CAP_TEXT = page.locator("//p[contains(@class,'display-block')]");
        this.SELECT_LINE_ITEM = page.locator("//div[contains(@class,'listitembox')]");
        this.FREQUENCY_CAP = page.locator("//label[contains(text(),'Frequency Cap')]/following-sibling::div//sui-checkbox");
        this.TIMES_PER_HRS_VALUE = page.locator("//input[@formcontrolname='hour']");
    }

    public void createCampaign() {
        CREATE_CAMPAIGN.click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public void selectCampaign() {
        SELECT_CAMPAIGN.first().click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public  void clickLineItem()  {
        SELECT_LINE_ITEM.first().click();
        waitUtility.waitForElementVisible("//div[contains(@class, 'data-rangeSlider-container')]");
    }

    public  boolean isFrequencyCapDisabled() {
        return FREQUENCY_CAP.getAttribute("class").contains("checked");
    }

    public void clickDetailsTab() {
        DETAILS_TAB.click();
    }

    public void addFrequencyCap(String level, String FREQ_VALUE,String TIMES_PER, String SCOPE)  {
        Locator TIMES_PER_OPTION = page.locator(String.format("//div[contains(text(),'%s')]", TIMES_PER));
        Locator FREQUENCY_CAP_SCOPE = page.locator(String.format("//div[contains(text(),'%s')]", SCOPE));
        if(level.contains("Campaign") | level.contains("Line Item")){
        waitUtility.waitForLocatorVisible(CUSTOM_FIELD.first());}
        if(!FREQUENCY_CAP.getAttribute("class").contains("checked")){
        FREQUENCY_CAP.click();}
        FREQUENCY_CAP_VALUE.fill(FREQ_VALUE);
        TIMES_PER_DROPDOWN.click();
        TIMES_PER_OPTION.first().click();
        if(TIMES_PER.contains("hour(s)")){
            TIMES_PER_HRS_VALUE.fill(FREQ_VALUE);
        }
        SCOPE_DROPDOWN.click();
        FREQUENCY_CAP_SCOPE.click();
        SAVE_CAMPAIGN.click();
        waitUtility.waitForElementVisible("//div[@role='alert']");
    }

    public boolean getFrequencyCapState(){
        return FREQUENCY_CAP.first().getAttribute("class").contains("checked");
    }


    public String getSavedFrequencyCap(String level) {
        String frequencyCapValue = GET_FREQUENCY_CAP_TEXT.first().innerText().trim().toUpperCase();
        if (level.contains("Line Item")) {
            frequencyCapValue = GET_FREQUENCY_CAP_TEXT.filter(new Locator.FilterOptions().setHasText("Line item")).innerText().trim().toUpperCase();
        }
        return frequencyCapValue;
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
        if ("Regular".equals(campaignType)) {
            CAMPAIGN_TYPE_REGULAR.click();
        } else if ("Sequential".equals(campaignType)) {
            CAMPAIGN_TYPE_SEQUENTIAL.click();
        }
    }

    public void enterBudget(String budget) {
        BUDGET.fill(budget);
    }

    public void saveCampaign() {
        SAVE_CAMPAIGN.click();
    }

    public String campaignSuccess() {
        String successMessage = CAMPAIGN_SUCCESS.innerText().trim();
        waitUtility.waitUntilSpinnerHidden();
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
}