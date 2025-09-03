package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.WaitUtility;

public class CampaignListing {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    private final Page page;
    private final Locator CLICK_SETTINGS;
    private final Locator SEARCH_CAMPAIGN;
    private final Locator CLICK_CAMPAIGN_SEARCH;
    private final Locator EXPAND_CREATED_LINE_ITEM;
    private final Locator VERIFY_CREATED_TACTIC;
    private final Locator NOTE_ICON;
    private final Locator GROUPBYCAMPAIGN_RADIOBUTTON;
    private final Locator SUB_TITLE_AFTERCAMPAIGNSEARCH;
    private final Locator FAVORITE_ONLY_CHECKBOX;
    private final Locator HIDE_FINISHED_CHECKBOX;
    private final Locator FILTER_APPLIED_ICON;
    private final Locator RESET_FILTER_ICON;
    private final Locator PRE_LOADER;

    public CampaignListing(Page page) {
        this.page = page;
        this.CLICK_SETTINGS = page.locator("//i[@class='icon gearIcon']");
        this.SEARCH_CAMPAIGN = page.locator("//input[@placeholder='Search' and contains(@class, 'gaTableSearch')]");
        this.CLICK_CAMPAIGN_SEARCH = page.locator("//div[contains(@class,'gaTableSearchBtn')]");
        this.EXPAND_CREATED_LINE_ITEM = page.locator("//div[contains(@class,'campaignExpand')]/div[contains(@class,'collapsed-thin')]");
        this.VERIFY_CREATED_TACTIC = page.locator("//span[contains(@class,'tactic-name')]");
        this.NOTE_ICON = page.locator("//div[contains(@class,'notes-icon-col')]");
        this.GROUPBYCAMPAIGN_RADIOBUTTON = page.locator("//i[contains(@class,'gaGroupCampaigns')]");
        this.SUB_TITLE_AFTERCAMPAIGNSEARCH = page.locator("//div[contains(@class,'sub-title') and contains(text(),'1 Line items, 1 Campaigns, 1 Advertisers')]");
        this.FAVORITE_ONLY_CHECKBOX = page.locator("//sui-checkbox[contains(@class,'gaFavoritesOnly')]");
        this.HIDE_FINISHED_CHECKBOX = page.locator("//label[contains(text(),'Hide Finished')]/ancestor::sui-checkbox");
        this.FILTER_APPLIED_ICON = page.locator("//div[contains(@class,'filterApplied')]");
        this.RESET_FILTER_ICON = page.locator("//span[contains(text(),'Reset All Filters')]");
        this.PRE_LOADER = page.locator("//div[@class='preloader']");
    }

    public void verifyCampaignRadioBtnChecked() {
        CLICK_SETTINGS.click();
        if (!GROUPBYCAMPAIGN_RADIOBUTTON.getAttribute("class").contains("groupingMenuRadioSelected")) {
            GROUPBYCAMPAIGN_RADIOBUTTON.click();
            waitUtility.waitUntilPreLoaderHidden();
        }
    }

    public void verifyFavoriteCheckbox() {
        if (FAVORITE_ONLY_CHECKBOX.getAttribute("class").contains("checked")) {
            FAVORITE_ONLY_CHECKBOX.click();
            waitUtility.waitUntilPreLoaderHidden(120000);
        }
    }

    public void verifyHideFinishedCheckbox(){
        if (HIDE_FINISHED_CHECKBOX.getAttribute("class").contains("checked")) {
            HIDE_FINISHED_CHECKBOX.click();
            waitUtility.waitUntilPreLoaderHidden(120000);
        }
    }

    public void verifyIfFiltersExist(){
        if(FILTER_APPLIED_ICON.isVisible()){
            FILTER_APPLIED_ICON.click();
            RESET_FILTER_ICON.click();
            waitUtility.waitUntilPreLoaderHidden(120000);
        }
    }

    public void searchCreatedCampaign(String createdCampaign) {
        waitUtility.waitForLocatorVisible(NOTE_ICON.last());
        verifyCampaignRadioBtnChecked();
        verifyFavoriteCheckbox();
        verifyHideFinishedCheckbox();
        verifyIfFiltersExist();
        SEARCH_CAMPAIGN.fill(createdCampaign);
        if(PRE_LOADER.isVisible())
            waitUtility.waitUntilPreLoaderHidden(120000);
        CLICK_CAMPAIGN_SEARCH.click();
        SUB_TITLE_AFTERCAMPAIGNSEARCH.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
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
        EXPAND_CREATED_LINE_ITEM.click();
        waitUtility.waitUntilPreLoaderHidden();
    }

    public String verifyCreatedTactic() {
        page.waitForLoadState();
        return VERIFY_CREATED_TACTIC.innerText();
    }
}