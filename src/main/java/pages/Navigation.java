package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public class Navigation {
    public final Locator USERNAME;
    private final Locator PASSWORD;
    private final Locator LOGIN_BUTTON;
    private final Page page;
    private final Locator LIFE;
    private final Locator SIGNAL;
    private final Locator STUDIO;
    private final Locator SUB_MENU;
    private final Locator RUN_REPORT;
    private final Locator GENERATED_REPORT;
    private final Locator SCHEDULED_REPORT;
    private final Locator REPORT_TEMPLATE;
    private final Locator ACCOUNTNAME;
    private final Locator ACCOUNT_SEARCH;
    private final Locator ACCOUNT_ITEM;
    private final Locator PRE_LOADER;


    public Navigation(Page page) {
        this.page = page;
        this.USERNAME = page.locator("#UserName");
        this.PASSWORD = page.locator("#Password");
        this.LOGIN_BUTTON = page.locator(".loginLabel");
        this.LIFE = page.getByText("Life");
        this.SIGNAL = page.getByText("Signal");
        this.STUDIO = page.getByText("Studio");
        this.RUN_REPORT = page.getByText("Run a Report");
        this.GENERATED_REPORT = page.locator("#megamenu").getByText("Generated Reports");
        this.SCHEDULED_REPORT = page.locator("#megamenu").getByText("Scheduled Reports");
        this.REPORT_TEMPLATE = page.locator("#megamenu").getByText("Report Templates");
        this.SUB_MENU = page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("menu"));
        this.ACCOUNTNAME = page.locator("//div[@class='accountname']");
        this.ACCOUNT_SEARCH = page.locator("//div[@id='accountSwitcher']/input[@placeholder='Search']");
        this.ACCOUNT_ITEM = page.locator("//div[@id='accountSwitcher']//div[@class='item']");
        this.PRE_LOADER = page.locator("//div[@class='preloader']");

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
        this.page.getByText(iconName.trim()).click();
    }

    public String verifyProfilePage() {
        page.waitForLoadState(LoadState.LOAD);
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

    public void selectAccount(String account){
        if(ACCOUNTNAME.innerText().contains("buyer2")){
            ACCOUNTNAME.click();
            ACCOUNT_SEARCH.fill(account);
            page.waitForLoadState(LoadState.LOAD);
            ACCOUNT_ITEM.click();
        }
        PRE_LOADER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void clickSubMenu() {
        SUB_MENU.click();
    }
    public void clickRunReport() {
        RUN_REPORT.click();
    }


    public void clickGeneratedReport() {
        page.waitForTimeout(5000);
        GENERATED_REPORT.click();
    }

    public void clickScheduledReport() {
        SCHEDULED_REPORT.click();

    }
        public void clickReportTemplate()
    {
            REPORT_TEMPLATE.click();
    }
}