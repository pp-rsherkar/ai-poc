package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CampaignListing {
    private final Page page;
    private final Locator CLICK_SETTINGS;
    private final Locator SEARCH_CAMPAIGN;
    private final Locator CLICK_CAMPAIGN_SEARCH;
    private final Locator EXPAND_CREATED_LINE_ITEM;
    private final Locator VERIFY_CREATED_TACTIC;
    private final Locator NOTE_ICON;
    private final Locator PRE_LOADER;
    private final Locator GROUPBYCAMPAIGN_RADIOBUTTON;
    private final Locator SUB_TITLE_AFTERCAMPAIGNSEARCH;
    private final Locator FAVORITE_ONLY_CHECKBOX;
    private final Locator HIDE_FINISHED_CHECKBOX;

    public CampaignListing(Page page) {
        this.page = page;
        this.CLICK_SETTINGS = page.locator("//i[@class='icon gearIcon']");
        this.SEARCH_CAMPAIGN = page.locator("//input[@placeholder='Search' and contains(@class, 'gaTableSearch')]");
        this.CLICK_CAMPAIGN_SEARCH = page.locator("//div[@class='iconSprite search1']");
        this.EXPAND_CREATED_LINE_ITEM = page.locator("//div[contains(@class,'campaignExpand')]/div[contains(@class,'collapsed-thin')]");
        this.VERIFY_CREATED_TACTIC = page.locator("//span[contains(@class,'tactic-name')]");
        this.NOTE_ICON = page.locator("//div[contains(@class,'notes-icon-col')]");
        this.PRE_LOADER = page.locator("//div[@class='preloader']");
        this.GROUPBYCAMPAIGN_RADIOBUTTON = page.locator("//i[contains(@class,'gaGroupCampaigns')]");
        this.SUB_TITLE_AFTERCAMPAIGNSEARCH = page.locator("//div[contains(@class,'sub-title') and contains(text(),'1 Line items, 1 Campaigns, 1 Advertisers')]");
        this.FAVORITE_ONLY_CHECKBOX = page.locator("//sui-checkbox[contains(@class,'gaFavoritesOnly')]");
        this.HIDE_FINISHED_CHECKBOX = page.locator("//label[contains(text(),'Hide Finished')]/ancestor::sui-checkbox");
    }

    public void verifyCampaignRadioBtnChecked() {
        CLICK_SETTINGS.click();
        if (!GROUPBYCAMPAIGN_RADIOBUTTON.getAttribute("class").contains("groupingMenuRadioSelected")) {
            GROUPBYCAMPAIGN_RADIOBUTTON.click();
            PRE_LOADER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        }
    }

    public void verifyFavoriteCheckbox() {
        if (FAVORITE_ONLY_CHECKBOX.getAttribute("class").contains("checked")) {
            FAVORITE_ONLY_CHECKBOX.click();
            PRE_LOADER.waitFor(new Locator.WaitForOptions().setTimeout(120000).setState(WaitForSelectorState.HIDDEN));
        }
    }

    public void verifyHideFinishedCheckbox(){
        if (HIDE_FINISHED_CHECKBOX.getAttribute("class").contains("checked")) {
            HIDE_FINISHED_CHECKBOX.click();
            PRE_LOADER.waitFor(new Locator.WaitForOptions().setTimeout(120000).setState(WaitForSelectorState.HIDDEN));
        }
    }

    public void searchCreatedCampaign(String createdCampaign) {
        NOTE_ICON.last().waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        verifyCampaignRadioBtnChecked();
        verifyFavoriteCheckbox();
        verifyHideFinishedCheckbox();
        SEARCH_CAMPAIGN.fill(createdCampaign);
        CLICK_CAMPAIGN_SEARCH.click();
        SUB_TITLE_AFTERCAMPAIGNSEARCH.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public String verifyCreatedCampaign(String createdCampaign) {
        String campaignNameXpath = String.format("//span[contains(text(),'%s')]", createdCampaign);
        page.waitForSelector(campaignNameXpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(campaignNameXpath).innerText();
    }

    public String verifyCreatedLineItem(String lineItemNameRandom) {
        String lineItemNameXpath = String.format("//span[contains(text(),'%s')]", lineItemNameRandom);
        page.waitForSelector(lineItemNameXpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(lineItemNameXpath).innerText();
    }

    public void expandCreatedLineItem() {
        EXPAND_CREATED_LINE_ITEM.click();
        PRE_LOADER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public String verifyCreatedTactic() {
        page.waitForLoadState();
        return VERIFY_CREATED_TACTIC.innerText();
    }
}