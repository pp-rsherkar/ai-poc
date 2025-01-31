package pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;


public class NavigateStudio
    {
        private final Locator switchaccount;
        private final Locator searchAccount;
        private final Locator clickAccount;
        private final Locator mainmenu;
        private final Locator studioMenu;
        private final Locator getworkspacename;
        private final Locator disableStudioSetting;
        private final Page page;

        public NavigateStudio(Page page)
        {
            this.page=page;
            this.switchaccount=page.locator(".left > div:nth-child(2)").first();
            this.searchAccount=page.getByPlaceholder("Search");
            this.clickAccount=page.locator("#accountSwitcher").getByText("100Plus");
            this.mainmenu=page.locator(".megaMenuWrapper");
            this.studioMenu=page.locator("#megamenu div").filter(new Locator.FilterOptions().setHasText("Studio")).nth(3);
            //this.getworkspacename = page.locator("iframe[title=\"overview\"]").contentFrame().locator("#iframe").contentFrame().getByText("HCP Audience Expansion");
            this.getworkspacename=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("HCP Audience Expansion");
            this.disableStudioSetting=page.locator("sui-dimmer div").filter(new Locator.FilterOptions().setHasText("Ok")).nth(4);


        }

        public void switchAccount()
        {
            switchaccount.click();
            searchAccount.fill("100Plus");
            clickAccount.click();
            mainmenu.click();


        }
        public void navigateStudio()
        {
            studioMenu.click();

        }
        public String verifyWorkspacePermission()
        {
            System.out.println("workspace locator: "+ getworkspacename.textContent());
            String name=getworkspacename.textContent();
            return name;
        }

    }
