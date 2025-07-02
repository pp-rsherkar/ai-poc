package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;

public class Workspace {

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
    private final Locator WEBHOOK_ICON;
    private final Locator WEBHOOK_TOGGLE_BUTTON;
    private final Locator WEBHOOK_PANEL_TITLE;
    private final Locator WEBHOOK_CANCEL_BUTTON;
    private final Locator GET_REQUEST;
    private final Locator POST_REQUEST;
    private final Locator URL_TEXTAREA;
    private final Locator MACROS;
    private final Locator PARAM;

    public Workspace(Page page) {
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
        this.WEBHOOK_ICON = WORKSPACE_FRAME.locator("(//div[contains(@class,'styles__StyledScheduleStatus')])[3]"); //no unique identifier is available hence index needs to be provided
        this.WEBHOOK_TOGGLE_BUTTON = WORKSPACE_FRAME.locator("//span[contains(@class,'MuiButtonBase-root')]");
        this.WEBHOOK_PANEL_TITLE = WORKSPACE_FRAME.locator("//h1[contains(text(),'Webhook')]");
        this.WEBHOOK_CANCEL_BUTTON = WORKSPACE_FRAME.locator("//button[@type='button']/div[contains(text(),'Cancel')]");
        this.GET_REQUEST = WORKSPACE_FRAME.locator("//button[@value='GET']");
        this.POST_REQUEST = WORKSPACE_FRAME.locator("//button[@value='POST']");
        this.URL_TEXTAREA = WORKSPACE_FRAME.locator("//textarea[@name='url']");
        this.MACROS = WORKSPACE_FRAME.locator("//span[contains(@class,'styles__StyledBIChip')]/span/div");
        this.PARAM = WORKSPACE_FRAME.locator("//p[contains(@cursor,'pointer')]");
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

    public void clickWebhookIcon(){
        WEBHOOK_ICON.click();
    }

    public String verifyWebhookToggleButton() {
        WEBHOOK_PANEL_TITLE.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        if(WEBHOOK_TOGGLE_BUTTON.getAttribute("class").contains("Mui-disabled")){
            return "Disabled";
        }else {
            WEBHOOK_TOGGLE_BUTTON.click();
            if(WEBHOOK_TOGGLE_BUTTON.getAttribute("class").contains("Mui-checked"))
                return "Enabled";
        }
        return " ";
    }

    public void closeWebhookPanel(){
        WEBHOOK_CANCEL_BUTTON.click();
    }

    public void clickRequestMethod(String requestType) {
        if(requestType.contains("GET"))
            GET_REQUEST.click();
        POST_REQUEST.click();
    }

    public void addURLAndMacros(String url, String param, List<String> macrosList) {
        URL_TEXTAREA.fill(url);
        for(int i=0; i<MACROS.count(); i++){
            MACROS.nth(i).locator("text="+macrosList.get(i)).click();
            if(PARAM.isVisible())
                PARAM.locator("text="+param).click();
        }
    }

    public String verifyMacrosAppendedToURL() {
        return URL_TEXTAREA.innerText();
    }

    public void selectAndClickContentType(String contentType) {
        page.locator(String.format("//button[contains(@value,'%s')]",contentType)).click();
    }
}