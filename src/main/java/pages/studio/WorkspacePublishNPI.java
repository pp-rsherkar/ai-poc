package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class WorkspacePublishNPI {

    private final Page page;
    private final Locator STUDIO_CLICK;
    private final Locator SEARCH_WORKSPACE;
    private final Locator DOWNLOAD_BUTTON;
    private final Locator PUBLISH_NPI;
    private final Locator PUBLISHED_NPI;
    private final Locator STATIC_LIST;
    private final Locator LIVE_LIST;
    private final Locator SELECT_HCP;
    private final Locator SELECT_LIFE;
    private final Locator PUBLISH_BUTTON;
    private final FrameLocator WORKSPACE_FRAME;
    private final Locator WORK_SPACECREATED_ALERT;

    public WorkspacePublishNPI(Page page) {
        this.page = page;
        this.WORKSPACE_FRAME = page.frameLocator("iframe#iframe0").frameLocator("iframe");
        this.STUDIO_CLICK = page.locator("#megamenu div").filter(new Locator.FilterOptions().setHasText("Studio")).nth(3);
        this.SEARCH_WORKSPACE = WORKSPACE_FRAME.getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));
        this.DOWNLOAD_BUTTON = WORKSPACE_FRAME.locator(".styles__StyledScheduleStatus-sc-u6f0o3-5 > svg").first();
        this.PUBLISH_NPI = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Publish NPI List"));
        this.PUBLISHED_NPI = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Published NPI List"));
        this.STATIC_LIST = WORKSPACE_FRAME.getByRole(AriaRole.RADIO, new FrameLocator.GetByRoleOptions().setName("Static List"));
        this.LIVE_LIST = WORKSPACE_FRAME.getByRole(AriaRole.RADIO, new FrameLocator.GetByRoleOptions().setName("Live List"));
        this.SELECT_HCP = WORKSPACE_FRAME.getByRole(AriaRole.CHECKBOX, new FrameLocator.GetByRoleOptions().setName("HCP365"));
        this.SELECT_LIFE = WORKSPACE_FRAME.getByRole(AriaRole.CHECKBOX, new FrameLocator.GetByRoleOptions().setName("Life"));
        this.PUBLISH_BUTTON = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Publish"));
        this.WORK_SPACECREATED_ALERT = WORKSPACE_FRAME.locator("//span[contains(@class,'TextBase-sc')]");

    }

    public void studio() {
        STUDIO_CLICK.click();
        page.reload();
    }

    public void searchWorkspace(String workspace) {
        // WORKSPACE = WORKSPACE + '_' + UUID.randomUUID().toString().substring(0, 10);
        SEARCH_WORKSPACE.fill(workspace);
        Locator clickWorkspace = WORKSPACE_FRAME.getByText(workspace);
        clickWorkspace.click();
    }

    public void clickDownbutton() {
        DOWNLOAD_BUTTON.click();
    }

    public void clickPublishNpi() {
        PUBLISH_NPI.click();
    }

    public void publish(String listType) {
        if (listType.equals("Static")) {
            STATIC_LIST.click();
        } else {
            LIVE_LIST.click();
        }
    }

    public void hcp() {
        SELECT_HCP.click();
    }

    public void life() {
        SELECT_LIFE.click();
    }

    public void clickPublish() {
        PUBLISH_BUTTON.click();
        WORK_SPACECREATED_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

    }

    public String verifyPublishedNpi() {
        return PUBLISHED_NPI.innerText();
    }

    public void waitTillWorkspaceAlertHide(){
        WORK_SPACECREATED_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));;
    }

}