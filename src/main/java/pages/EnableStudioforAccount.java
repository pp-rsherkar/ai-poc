package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class EnableStudioforAccount {
    private final Locator menu;
    private final Locator administration;
    private final Locator accountmenu;
    private final Locator search;
    private final Locator searchIcon;
    private final Locator searchaccount;
    private final Locator studiosettingbtn;
    private final Locator explorerworkspace;
    private final Locator expansionworkspace;
    private final Locator savestudiosetting;
    private final Page page;

    public EnableStudioforAccount(Page page) {
        this.page = page;
        this.menu = page.locator(".megaMenuWrapper");
        this.administration = page.locator("#megamenu div").filter(new Locator.FilterOptions().setHasText("Administration")).nth(4);
        this.accountmenu = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Accounts"));
        this.search = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.searchIcon=page.locator(".ui > .iconSprite");
        this.searchaccount = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("100Plus"));
        this.studiosettingbtn = page.locator("div:nth-child(6) > .btn > .acc-toggle-wrapper > .icons-32-checkmark");
        this.explorerworkspace = page.locator("app-rightside label").first();
        this.expansionworkspace = page.locator("app-rightside label").nth(2);
        this.savestudiosetting=page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"));
    }

    public void clickMenu() {
        menu.click();
    }

    public void clickAdministration() {
        administration.click();
    }

    public void accountMenuClick()
    {
        accountmenu.click();
    }
    public void searchAccount()
    {
        searchaccount.click();
    }
    public void enableStudio()
    {
        studiosettingbtn.click();
    }
    public void workSpaceSettings() {
        expansionworkspace.click();
        //explorerworkspace.click();
    }
    public void Savestudiosettings()
    {
        savestudiosetting.click();
    }
}
