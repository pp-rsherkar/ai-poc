package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.WebActions;

public class Navigation {
    public final Locator USERNAME;
    private final Locator PASSWORD;
    private final Locator LOGIN_BUTTON;
    private final Page page;

    public Navigation(Page page) {
        this.page = page;
        this.USERNAME = page.locator("#UserName");
        this.PASSWORD = page.locator("#Password");
        this.LOGIN_BUTTON = page.locator(".loginLabel");
    }

    public void navigateToUrl() {
        this.page.navigate(WebActions.getProperty("url"));
    }

    public void enterUsername(String userName) {
        USERNAME.fill(WebActions.getProperty("user"));
    }

    public void enterPassword() {
        PASSWORD.fill(WebActions.getProperty("password"));
    }

    public void clickLogin() {
        LOGIN_BUTTON.click();
    }

    public void clickOnIcon(String iconName) {
        this.page.getByText(iconName, new Page.GetByTextOptions().setExact(true)).click();  // Clicks on the Exact text
    }

    public String verifyProfilePage() {
        page.waitForLoadState();
        return this.page.title();
    }
}