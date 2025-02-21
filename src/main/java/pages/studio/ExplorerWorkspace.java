package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

public class ExplorerWorkspace {
    private final Page page;
    private final Locator WORKSPACE_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator DASHBOARD_CONTENT;
    private final Locator ADD_FILTER;
    private final Locator SEARCH_FILTER;
    private final Locator SELECT_FILTER;
    private final Locator SELECT_FILTER_OPTION;
    private final Locator FILTER_OK_BUTTON;
    private final Locator FILTER_CLOSE_BUTTON;
    private final Locator APPLIED_FILTER;
    private final Locator APPLIED_FILTER_OPTION;
    private final Locator SAVE_EXPLORER_WORKSPACE;
    private final Locator EXPLORER_WORKSPACE_SUCCESS;

    public ExplorerWorkspace(Page page) {
        this.page = page;
        this.WORKSPACE_NAME = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.TEXTBOX).nth(1);
        this.SEARCH_ADVERTISER = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.COMBOBOX, new FrameLocator.GetByRoleOptions().setName("undefined combobox")).getByRole(AriaRole.TEXTBOX);
        this.SELECT_ADVERTISER = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.LISTBOX);
        this.DASHBOARD_CONTENT = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().locator("#extension-root iframe").contentFrame().getByRole(AriaRole.REGION, new FrameLocator.GetByRoleOptions().setName("Dashboard Content"));
        this.ADD_FILTER = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Add Filters"));
        this.SEARCH_FILTER = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));
        this.SELECT_FILTER = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("NPI Gender");
        this.SELECT_FILTER_OPTION = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("Male", new FrameLocator.GetByTextOptions().setExact(true));
        this.FILTER_OK_BUTTON = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Ok"));
        this.FILTER_CLOSE_BUTTON = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Select Filter$"))).getByRole(AriaRole.BUTTON);
        this.APPLIED_FILTER = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("NPI Gender").nth(1);
        this.APPLIED_FILTER_OPTION = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("Male").first();
        //this.SAVE_EXPLORER_WORKSPACE = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Save"));
        this.SAVE_EXPLORER_WORKSPACE = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().locator(".styles__StyledResetUpdate-sc-njp72g-0 > button:nth-child(2)");
        this.EXPLORER_WORKSPACE_SUCCESS = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().locator("[id=\"\\32 \"] div").filter(new Locator.FilterOptions().setHasText("Workspace managementWorkspace")).nth(2);
    }

    public void enterWorkspaceName(String workspaceName) {
        WORKSPACE_NAME.clear();
        WORKSPACE_NAME.fill(workspaceName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.fill(advertiser);
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
        DASHBOARD_CONTENT.waitFor();
    }

    public void selectFilter(String filter, String option) {
        ADD_FILTER.click();
        SEARCH_FILTER.fill(filter);
        SELECT_FILTER.click();
        SELECT_FILTER_OPTION.click();
    }

    public void applyFilter() {
        FILTER_OK_BUTTON.click();
        FILTER_CLOSE_BUTTON.click();
    }

    public String verifySelectedFilter() {
        DASHBOARD_CONTENT.waitFor();
        return APPLIED_FILTER.innerText();
    }

    public String verifySelectedOption() {
        return APPLIED_FILTER_OPTION.innerText();
    }

    public void saveExplorerWorkspace() {
        page.waitForLoadState();
        SAVE_EXPLORER_WORKSPACE.waitFor();
        SAVE_EXPLORER_WORKSPACE.click();
    }

    public String workspaceSuccess() {
        EXPLORER_WORKSPACE_SUCCESS.waitFor();
        return EXPLORER_WORKSPACE_SUCCESS.innerText();
    }
}