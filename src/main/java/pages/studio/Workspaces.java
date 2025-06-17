package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class Workspaces {
    private final Page page;
    private final Locator CREATE_WORKSPACE;
    private final Locator HCP_EXPLORER;
    private final Locator HCP_EXPANSION;
    private final Locator BACK_TO_WORKSPACE_DASHBOARD;
    private final Locator WORK_SPACECREATED_ALERT;
    private final Locator MENU_ICON;
    private final FrameLocator WORKSPACE_FRAME;
    int counter = 0;

    public Workspaces(Page page) {
        this.page = page;
        this.WORKSPACE_FRAME = page.frameLocator("iframe#iframe0").frameLocator("iframe");
        this.CREATE_WORKSPACE = WORKSPACE_FRAME.locator("text=Create New Workspace");
        this.HCP_EXPLORER = WORKSPACE_FRAME.locator("//label[contains(text(),'HCP Explorer')]");
        this.HCP_EXPANSION = WORKSPACE_FRAME.locator("//label[contains(text(),'HCP Audience Expansion')]");
        this.BACK_TO_WORKSPACE_DASHBOARD = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON);
        this.WORK_SPACECREATED_ALERT = WORKSPACE_FRAME.locator("//span[contains(@class,'TextBase-sc')]");
        this.MENU_ICON = page.locator("//img[contains(@class,'menu-icon')]");
    }

    public String studioDashboard() {
        page.waitForLoadState();
        return this.page.title();
    }

    public String verifyHCPExplorer() {
        page.waitForLoadState();
        return HCP_EXPLORER.innerText();
    }

    public String verifyHCPAudienceExpansion() {
        return HCP_EXPANSION.innerText();
    }

    public void clickHCPExplorerWorkspace() {
        HCP_EXPLORER.click();
    }

    public String verifyWorkspaceCreation() {
        return WORK_SPACECREATED_ALERT.innerText();
    }

    public void createWorkspace() {
        while (counter < 3) {
            retryCreateWorkspace(false);
        }
    }

    public void createStudioWorkspace() {
        /*while (counter < 3) {
            retryCreateWorkspace(clickFlag);
        }*/
        for (int i = 0; i < 1000; i++) {
            if (CREATE_WORKSPACE.isEnabled()) {
                CREATE_WORKSPACE.click();
                break;
            }else{
                MENU_ICON.click();
                page.keyboard().press("Escape");
            }
        }
    }

    public void verifyStudioWorkspaceFrame(){
        page.frameLocator("iframe#iframe0")
                .frameLocator("iframe")
                .locator("text=Create New Workspace")
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void retryCreateWorkspace(boolean clickFlag) {
        {
            page.waitForTimeout(5000);
            CREATE_WORKSPACE.click();
            page.waitForLoadState();
            if (HCP_EXPANSION.isVisible()) {
                page.waitForLoadState();
                counter = 4;
//                if (clickFlag) {
//                    HCP_EXPLORER.click();
//                }
            } else {
                counter++;
                BACK_TO_WORKSPACE_DASHBOARD.click();
                page.waitForLoadState();
            }
        }
    }

    public void createWorkspaceExpansion(Boolean expansionFlag) {
        while (counter < 3) {
            retryCreateWorkspaceExpansion(expansionFlag);
        }
    }

    public void retryCreateWorkspaceExpansion(boolean expansionFlag) {
        {
            CREATE_WORKSPACE.click();
            page.waitForLoadState();
            if (HCP_EXPANSION.isVisible()) {

                page.waitForLoadState();
                counter = 4;
                if (expansionFlag) {
                    HCP_EXPANSION.click();
                }
            } else {
                counter++;
                //page.waitForTimeout(3000);
                BACK_TO_WORKSPACE_DASHBOARD.click();
                page.waitForLoadState();
            }
        }
    }

    public void createWorkspace_downloadnpi() {
        while (counter < 3) {
            retryCreateWorkspace_downloadnpi(false);
        }
    }

    public void retryCreateWorkspace_downloadnpi(boolean clickFlag) {
        {
            if (CREATE_WORKSPACE.isVisible()) {
                CREATE_WORKSPACE.click();
            } else {
                page.waitForTimeout(10000);
                CREATE_WORKSPACE.click();
            }
            page.waitForLoadState();
            if (HCP_EXPANSION.isVisible()) {
                page.waitForLoadState();
                BACK_TO_WORKSPACE_DASHBOARD.click();
                counter = 4;
                if (clickFlag) {
                    HCP_EXPLORER.click();
                }
            } else {
                counter++;
                BACK_TO_WORKSPACE_DASHBOARD.click();
                page.waitForLoadState();
            }
        }
    }
}