package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class Pixels {
    private final Page page;
    private final Locator PIXELS_MENU_ITEM;
    private final Locator ADD_PIXEL_BUTTON;
    private final Locator CREATE_NEW_PIXEL_LABEL;
    private final Locator RETARGETING_PIXEL;
    private final Locator SMART_PIXEL;
    private final Locator CONVERSION_PIXEL;
    private final Locator SEARCH_BOX;

    public Pixels(Page page) {
        this.page = page;
        this.PIXELS_MENU_ITEM = page.locator("//div[contains(@class,'pull-left menuLabel') and text()='Pixels']");
        this.ADD_PIXEL_BUTTON = page.locator("//span[normalize-space(text())='Add Pixel']");
        this.CREATE_NEW_PIXEL_LABEL = page.locator("//div[@class='createNewPixel']/div[@class='heading-wrapper']");
        this.RETARGETING_PIXEL = page.locator("//span[text()='Retargeting Pixel']");
        this.SMART_PIXEL = page.locator("//span[text()='Smart Pixel']");
        this.CONVERSION_PIXEL = page.locator("//span[text()='Conversion Pixel']");
        this.SEARCH_BOX = page.locator("//input[@placeholder='Search' and contains(@class,'search icon')]");
    }

    public void clickPixelsMenuItem() {
        PIXELS_MENU_ITEM.click();
    }

    public void clickAddPixelButton() {
        ADD_PIXEL_BUTTON.click();
    }

    public String verifyCreateNewPixelLabel() {
        return CREATE_NEW_PIXEL_LABEL.innerText();
    }

    public String verifyRetargetingPixel() {
        return RETARGETING_PIXEL.innerText();
    }

    public String verifySmartPixel() {
        return SMART_PIXEL.innerText();
    }

    public String verifyConversionPixel() {
        return CONVERSION_PIXEL.innerText();
    }

    public void clickRetargetingPixel() {
        RETARGETING_PIXEL.click();
    }

    public void searchSavedPixel(String pixelName) {
        SEARCH_BOX.fill(pixelName);
        SEARCH_BOX.press("Enter");
    }

    public String verifyCreatedPixel(String pixelName) {
        String createdPixelXpath = String.format("//div[contains(text(),'%s')]", pixelName);
        page.waitForSelector(createdPixelXpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(createdPixelXpath).innerText();
    }

}
