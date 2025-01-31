package pages;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;

public class DisableStudioPermissions
{
    private final Locator returnmenu;
    private final Locator administration;
    private final Locator accountmenu;
    private final Locator searchaccount;
    private final Locator studiosettingbtn;
    private final Locator disableStudioSetting;
    private final Locator PPmenuIcon;
    private final Locator studioMenu;



    public DisableStudioPermissions(Page page)
    {
        this.returnmenu= page.locator("span").first();
        this.administration = page.getByText("Administration", new Page.GetByTextOptions().setExact(true));
        this.accountmenu = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Accounts"));
        this.searchaccount = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("100Plus"));
        this.studiosettingbtn = page.locator("div:nth-child(6) > .btn > .acc-toggle-wrapper > .icons-32-checkmark");
        this.studioMenu=page.getByText("Studio").nth(4);
        this.disableStudioSetting=page.locator("sui-dimmer div").filter(new Locator.FilterOptions().setHasText("Ok")).nth(4);
        this.PPmenuIcon=page.locator(".ui > div:nth-child(6)").first();
    }

    public void disableStudioForAccount()
    {
        returnmenu.click();
        administration.click();
        accountmenu.click();
        searchaccount.click();
        studiosettingbtn.click();
        disableStudioSetting.click();
    }
    public void verifyStudioMenu()
    {
        PPmenuIcon.click();
        returnmenu.click();
        boolean isVisible = studioMenu.isVisible();
        if(!isVisible)
        {
            System.out.println("Studio Menu is not visible");
        }
    }
}
