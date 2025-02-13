package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

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

    public void navigateToUrl(String url) {
        this.page.navigate(url);
    }

    public void enterUsername(String userName) {
        USERNAME.fill(userName);
    }

    public void enterPassword(String password) {
        PASSWORD.fill(password);
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