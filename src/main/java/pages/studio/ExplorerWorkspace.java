package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;
import java.util.regex.Pattern;

public class ExplorerWorkspace {
    private final Page page;
    private final Locator WORKSPACE_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator DASHBOARD_CONTENT;
    private final Locator DASHBOARD_ELEMENT;
    private final Locator DASHBOARD_RELOAD_ICON;
    private final Locator ADD_FILTER;
    private final Locator SEARCH_FILTER;
    private final Locator SELECT_FILTER;
    private final Locator FILTER_OK_BUTTON;
    private final Locator FILTER_CLOSE_BUTTON;
    private final Locator APPLIED_FILTER;
    private final Locator APPLIED_FILTER_OPTION;
    private final Locator SAVE_WORKSPACE;
    private final Locator EXPLORER_WORKSPACE_SUCCESS;
    private final FrameLocator WORKSPACE_FRAME;
    private final Locator SAVE_WORKSPACE_NAME;

    public ExplorerWorkspace(Page page) {
        this.page = page;
        this.WORKSPACE_FRAME = page.frameLocator("iframe#iframe0").frameLocator("iframe");
        this.WORKSPACE_NAME = WORKSPACE_FRAME.getByRole(AriaRole.TEXTBOX).nth(1);
        this.SEARCH_ADVERTISER = WORKSPACE_FRAME.locator("input[id^='listbox-input']");
        this.DASHBOARD_CONTENT = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().getByRole(AriaRole.REGION, new FrameLocator.GetByRoleOptions().setName("Dashboard Content"));
        this.DASHBOARD_ELEMENT = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().getByRole(AriaRole.REGION, new FrameLocator.GetByRoleOptions().setName("Dashboard Content")).locator("(//p[contains(@class,'TextBase-sc')])[1]");
        this.DASHBOARD_RELOAD_ICON = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator("//div[contains(text(),'Reload')]");
        this.ADD_FILTER = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Add Filters"));
        this.SEARCH_FILTER = WORKSPACE_FRAME.getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));
        this.SELECT_FILTER = WORKSPACE_FRAME.locator("//div[contains(@class,'styles__StyledIconLabelContainer') or contains(@class,'styles__StyledSubGroupContainer')]");
        this.FILTER_OK_BUTTON = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Ok"));
        this.FILTER_CLOSE_BUTTON = WORKSPACE_FRAME.locator("//h1[contains(text(),'Select Filter')]/following-sibling::button");
        this.APPLIED_FILTER = WORKSPACE_FRAME.locator("//div[contains(@class,'style__FilterTitleContainer-sc-')]");
        this.APPLIED_FILTER_OPTION = WORKSPACE_FRAME.locator("//div[contains(@class,'style__FilterExpression-sc')]");
        this.SAVE_WORKSPACE = WORKSPACE_FRAME.locator("//div[contains(@class,'styles__StyledContainer')]//div[contains(text(),'Save')]");
        this.EXPLORER_WORKSPACE_SUCCESS = WORKSPACE_FRAME.locator("[id=\"\\32 \"] div").filter(new Locator.FilterOptions().setHasText("Workspace managementWorkspace")).nth(2);
        this.SAVE_WORKSPACE_NAME = WORKSPACE_FRAME.locator("//div[contains(@class,'styles__DashboardContainer')]//div[contains(text(),'Save')]");
    }

    public void enterWorkspaceName(String workspaceName) {
        WORKSPACE_NAME.clear();
        WORKSPACE_NAME.fill(workspaceName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SEARCH_ADVERTISER.fill(advertiser);
        SEARCH_ADVERTISER.press("ArrowDown");
        SEARCH_ADVERTISER.press("Enter");
        SAVE_WORKSPACE_NAME.click();
        DASHBOARD_CONTENT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DASHBOARD_ELEMENT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void clickAddFilter(){
        ADD_FILTER.click();
    }

    public void selectFilter(String filter, String option) {
        SEARCH_FILTER.fill(filter);
        WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]", filter)).click();
        if(filter.contains("Site") || filter.contains("Search")){
            WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]", option)).click();
        }else{
            WORKSPACE_FRAME.locator(String.format("//label[contains(text(),'%s')]", option)).click();
        }
        FILTER_OK_BUTTON.click();
        SEARCH_FILTER.clear();
    }

    public void applyFilter() {
        FILTER_CLOSE_BUTTON.click();
    }

    public List<String> verifyAllSelectedFilters() {
        DASHBOARD_RELOAD_ICON.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return APPLIED_FILTER.allInnerTexts();
    }

    public List<String> verifyAllSelectedOptions() {
        return APPLIED_FILTER_OPTION.allInnerTexts();
    }

    public void saveExplorerWorkspace() {
        DASHBOARD_ELEMENT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DASHBOARD_RELOAD_ICON.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SAVE_WORKSPACE.first().click();
    }

    public String workspaceSuccess() {
        EXPLORER_WORKSPACE_SUCCESS.waitFor();
        return EXPLORER_WORKSPACE_SUCCESS.innerText();
    }
}