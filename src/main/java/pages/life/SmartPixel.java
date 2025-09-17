package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import factory.DriverFactory;
import utils.WaitUtility;

public class SmartPixel {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    private final Page page;
    private final Locator SELECT_ADVERTISER;
    private final Locator INACTIVE_CAMPAIGNS_BUTTON;
    private final Locator ASSOCIATED_CAMPAIGN;
    private final Locator PIXEL_NAME;

    public SmartPixel(Page page) {
        this.page = page;
        this.SELECT_ADVERTISER = page.locator("//div[@class='advertiser']/div[contains(@class,'left')]");
        this.INACTIVE_CAMPAIGNS_BUTTON = page.locator("//button[contains(text(),'Show Inactive Campaigns')]");
        this.ASSOCIATED_CAMPAIGN = page.locator("//sui-checkbox[contains(@class,'checkbox')]/label/span[contains(@class,'camp-name')]");
        this.PIXEL_NAME = page.locator("//input[@placeholder='Pixel Name']");
    }

    public void selectAdvertiser(String advertiser) {
        waitUtility.waitUntilSpinnerHidden();
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
    }

    public void selectAssociatedCampaign() {
        waitUtility.waitUntilSpinnerHidden();
        if (ASSOCIATED_CAMPAIGN.isVisible()) {
            ASSOCIATED_CAMPAIGN.first().click();
        } else {
            INACTIVE_CAMPAIGNS_BUTTON.click();
            ASSOCIATED_CAMPAIGN.first().click();
        }
    }

    public String getPixelName() {
        return PIXEL_NAME.inputValue();
    }
}