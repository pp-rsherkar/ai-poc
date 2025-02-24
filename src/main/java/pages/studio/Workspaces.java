package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class Workspaces {
    private final Page page;
    private final Locator WORKSPACE_CONTENT;
    private final Locator CREATE_WORKSPACE;
    private final Locator HCP_EXPLORER;
    private final Locator HCP_EXPANSION;
    private final Locator BACK_TO_WORKSPACE_DASHBOARD;
    static int counter = 0;

    public Workspaces(Page page) {
        this.page = page;
        this.WORKSPACE_CONTENT = page.locator("#extension-root > div > div:nth-child(1) > div > div > div > div > div.Box-sc-5738oh-0.styles__StyledTableContainer-sc-ga32ay-1.czcvII.fTjMUM > table");
        this.CREATE_WORKSPACE = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Create New Workspace"));
        this.HCP_EXPLORER = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("HCP Explorer");
        this.HCP_EXPANSION = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("HCP Audience Expansion");
        this.BACK_TO_WORKSPACE_DASHBOARD = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON);
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

    public void createWorkspace() {
        while (counter < 3) {
            retryCreateWorkspace(false);
        }
    }

    public void createWorkspace(Boolean clickFlag) {
        while (counter < 3) {
            retryCreateWorkspace(clickFlag);
        }
    }

    public void retryCreateWorkspace(boolean clickFlag) {
        {
            CREATE_WORKSPACE.click();
            page.waitForLoadState();
            if (HCP_EXPANSION.isVisible()) {
                page.waitForLoadState();
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