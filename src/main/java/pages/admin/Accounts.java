package pages.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class Accounts {
    private final Page page;
    private final Locator SUB_MENU;
    private final Locator ADMINISTRATION;
    private final Locator ACCOUNTS_TAB;
    private final Locator SEARCH_ACCOUNT;
    private final Locator SEARCH_ICON;
    private final Locator SELECT_ACCOUNT;
    private final Locator STUDIO_SETTINGS_BUTTON;
    private final Locator EXPLORER_TOGGLE;
    private final Locator EXPANSION_TOGGLE;
    private final Locator STUDIO_SETTINGS_SAVE;
    private final Locator STUDIO_MENU;
    private final Locator DISABLE_STUDIO_OK_BUTTON;
    private final Locator PULSEPOINT_ICON;
    private final Locator SWITCH_ACCOUNT;
    private final Locator SWITCH_SEARCH_ACCOUNT;
    private final Locator SWITCH_CLICK_ACCOUNT;
    private final Locator STUDIO;
    private final Locator WORKSPACE_NAME;

    public Accounts(Page page) {
        this.page = page;
        this.SUB_MENU = page.locator("span").first();
        this.ADMINISTRATION = page.getByText("Administration", new Page.GetByTextOptions().setExact(true));
        this.ACCOUNTS_TAB = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Accounts"));
        this.SEARCH_ACCOUNT = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.SEARCH_ICON = page.locator(".ui > .iconSprite");
        this.SELECT_ACCOUNT = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("100Plus"));
        this.STUDIO_SETTINGS_BUTTON = page.locator("div:nth-child(6) > .btn > .acc-toggle-wrapper > .icons-32-checkmark");
        this.EXPLORER_TOGGLE = page.locator("app-rightside label").first();
        this.EXPANSION_TOGGLE = page.locator("tr:nth-child(3) > td:nth-child(2) > .toggle-wrapper-withLabel > .toggle > label");
        this.STUDIO_SETTINGS_SAVE = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"));
        this.STUDIO_MENU = page.getByText("Studio").nth(4);
        this.DISABLE_STUDIO_OK_BUTTON = page.locator("sui-dimmer div").filter(new Locator.FilterOptions().setHasText("Ok")).nth(4);
        this.PULSEPOINT_ICON = page.locator(".ui > div:nth-child(6)").first();
        this.SWITCH_ACCOUNT = page.locator(".left > div:nth-child(2)").first();
        this.SWITCH_SEARCH_ACCOUNT = page.getByPlaceholder("Search");
        this.SWITCH_CLICK_ACCOUNT = page.locator("#accountSwitcher").getByText("100Plus");
        this.STUDIO = page.locator("#megamenu div").filter(new Locator.FilterOptions().setHasText("Studio")).nth(3);
        this.WORKSPACE_NAME = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("HCP Audience Expansion");
    }

    public void clickAdministration() {
        ADMINISTRATION.click();
    }

    public void selectAccountsTab() {
        page.waitForLoadState();
        ACCOUNTS_TAB.click();
    }

    public void searchAccount() {
        page.waitForLoadState();
        SELECT_ACCOUNT.click();
    }

    public void enableStudio() {
        STUDIO_SETTINGS_BUTTON.click();
    }

    public void workSpaceSettings() {
        EXPANSION_TOGGLE.click();
    }

    public void saveStudioSettings() {
        STUDIO_SETTINGS_SAVE.click();
    }

    public void disableStudioForAccount() {
        ADMINISTRATION.click();
        ACCOUNTS_TAB.click();
        SEARCH_ACCOUNT.fill("100Plus");
        SEARCH_ICON.click();
        SELECT_ACCOUNT.click();
        STUDIO_SETTINGS_BUTTON.click();
        DISABLE_STUDIO_OK_BUTTON.click();
    }

    public void switchAccount() {
        SWITCH_ACCOUNT.click();
        SWITCH_SEARCH_ACCOUNT.fill("100Plus");
        SWITCH_CLICK_ACCOUNT.click();
    }

    public String verifyWorkspacePermission() {
        WORKSPACE_NAME.waitFor();
        return WORKSPACE_NAME.textContent();
    }

    public void verifyStudioMenu() {
        page.reload();
        PULSEPOINT_ICON.click();
        SUB_MENU.click();
        boolean isVisible = STUDIO_MENU.isVisible();
        if (!isVisible) {
            System.out.println("Studio Menu is not visible");
        }
    }
}