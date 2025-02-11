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
    private final Locator disableStudioSetting;
    private final Locator PPmenuIcon;
    private final Locator studioMenu;
    private final Page page;

    public EnableStudioforAccount(Page page) {
        this.page = page;
        this.menu = page.locator("span").first();
        this.administration = page.getByText("Administration", new Page.GetByTextOptions().setExact(true));
        this.accountmenu = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Accounts"));
        this.search = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.searchIcon=page.locator(".ui > .iconSprite");
        this.searchaccount = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("100Plus"));
        this.studiosettingbtn = page.locator("div:nth-child(6) > .btn > .acc-toggle-wrapper > .icons-32-checkmark");
        this.explorerworkspace = page.locator("app-rightside label").first();
        this.expansionworkspace = page.locator(("tr:nth-child(3) > td:nth-child(2) > .toggle-wrapper-withLabel > .toggle > label"));
        this.savestudiosetting=page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"));
        this.studioMenu=page.getByText("Studio").nth(4);
        this.disableStudioSetting=page.locator("sui-dimmer div").filter(new Locator.FilterOptions().setHasText("Ok")).nth(4);
        this.PPmenuIcon=page.locator(".ui > div:nth-child(6)").first();
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

    public void disableStudioForAccount()
    {
        menu.click();
        administration.click();
        accountmenu.click();
        searchaccount.click();
        studiosettingbtn.click();
        disableStudioSetting.click();
    }
    public void verifyStudioMenu()
    {
        PPmenuIcon.click();
        menu.click();
        boolean isVisible = studioMenu.isVisible();
        if(!isVisible)
        {
            System.out.println("Studio Menu is not visible");
        }
    }
}
