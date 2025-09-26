package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class RetargetingPixel {
    private final Page page;
    private final Locator PIXEL_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator SAVE_BUTTON;
    private final Locator SAVE_SUCCESS;

    public RetargetingPixel(Page page) {
        this.page = page;
        this.PIXEL_NAME = page.locator("//input[@placeholder='Pixel Name']");
        this.SEARCH_ADVERTISER = page.locator("//input[contains(@class,'dropdown-search') and @placeholder='Advertisers']");
        this.SELECT_ADVERTISER = page.locator("//div[contains(@class,'item text-truncate')]");
        this.SAVE_BUTTON = page.locator("//button[text()='Save']");
        this.SAVE_SUCCESS = page.locator("//div[contains(@aria-label,'Success!')]");
    }

    public void enterPixelName(String pixelName) {
        PIXEL_NAME.fill(pixelName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
    }

    public void saveRetargetingPixel() {
        SAVE_BUTTON.click();
    }

    public String verifySaveSuccess() {
        String successMessage = SAVE_SUCCESS.innerText();
        SAVE_SUCCESS.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
        return successMessage;
    }
}