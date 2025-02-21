package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class Navigation {
    public final Locator USERNAME;
    private final Locator PASSWORD;
    private final Locator LOGIN_BUTTON;
    private final Page page;
    private final Locator LIFE;
    private final Locator SIGNAL;
    private final Locator STUDIO;
    private final Locator SUB_MENU;

    public Navigation(Page page) {
        this.page = page;
        this.USERNAME = page.locator("#UserName");
        this.PASSWORD = page.locator("#Password");
        this.LOGIN_BUTTON = page.locator(".loginLabel");
        this.LIFE = page.getByText("Life");
        this.SIGNAL = page.getByText("Signal");
        this.STUDIO = page.getByText("Studio");
        this.SUB_MENU = page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("menu"));
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

    public void navigateToLife() {
        LIFE.click();
    }

    public void navigateToHCP() {
        SIGNAL.click();
    }

    public void navigateToStudio() {
        page.waitForLoadState();
        SUB_MENU.click();
        page.waitForLoadState();
        STUDIO.click();
        page.waitForLoadState();
    }

    public void clickSubMenu() {
        SUB_MENU.click();
    }
}