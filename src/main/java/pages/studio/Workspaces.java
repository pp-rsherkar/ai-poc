package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.regex.Pattern;

public class Workspaces {
    private final Page page;
    private final Locator WORKSPACE_CONTENT;
    private final Locator CREATE_WORKSPACE;
    private final Locator HCP_EXPLORER;
    private final Locator HCP_EXPANSION;

    public Workspaces(Page page) {
        this.page = page;
        this.WORKSPACE_CONTENT = page.locator("#extension-root > div > div:nth-child(1) > div > div > div > div > div.Box-sc-5738oh-0.styles__StyledTableContainer-sc-ga32ay-1.czcvII.fTjMUM > table");
        this.CREATE_WORKSPACE = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Create New Workspace"));
        this.HCP_EXPLORER = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("HCP Explorer");
        this.HCP_EXPANSION = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("HCP Audience Expansion");
    }

    public String studioDashboard() {
        page.waitForLoadState();
        return this.page.title();
    }

    public void createWorkspace() {
        CREATE_WORKSPACE.click();
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
}