package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class WaitUtility {
    private final Page page;
    private final Locator SPINNER;
    private final Locator PRE_LOADER;

    public WaitUtility(Page page) {
        this.page = page;
        this.SPINNER = page.locator("//div[contains(text(),'Loading...')]");
        this.PRE_LOADER = page.locator("//div[contains(@class,'preloader')]");
    }

    public void waitUntilSpinnerHidden(){
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void waitUntilPreLoaderHidden(){
        PRE_LOADER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void waitUntilPreLoaderHidden(double timeout){
        PRE_LOADER.waitFor(new Locator.WaitForOptions().setTimeout(timeout).setState(WaitForSelectorState.HIDDEN));
    }

    public void waitForLocatorVisible(Locator locator){
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void waitForLocatorVisible(Locator locator, double timeout){
        locator.waitFor(new Locator.WaitForOptions().setTimeout(timeout).setState(WaitForSelectorState.VISIBLE));
    }

    public void waitForLocatorHidden(Locator locator){
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void waitForLocatorHidden(Locator locator, double timeout){
        locator.waitFor(new Locator.WaitForOptions().setTimeout(timeout).setState(WaitForSelectorState.HIDDEN));
    }

    public void waitForLocatorDetached(Locator locator){
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
    }

    public void waitForElementVisible(String xpath) {
        page.waitForSelector(xpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void waitForElementVisible(String xpath, double timeout) {
        page.waitForSelector(xpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeout));
    }

    public void waitForElementHidden(String xpath) {
        page.waitForSelector(xpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN));
    }
}
