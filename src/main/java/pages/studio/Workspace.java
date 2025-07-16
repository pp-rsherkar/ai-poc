package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.CommonUtils;

import java.io.IOException;
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
    private final Locator URL_TEXTAREA;
    private final Locator BODY_TEXTAREA;
    private final Locator PARAM;
    private final Locator WEBHOOK_BUTTONS;
    private final Locator WEBHOOK_SUCCESS_ALERT;
    private final Locator WEBHOOK_SAVE_BUTTON;
    private final Locator INLINE_ERROR_MESSAGE;
    private final Locator ERROR_ALERT;
    private final Locator CREATE_WORKSPACE;
    private final Locator GO_TO_WORKSPACE_LIST;
    private final Locator BEFORE_YOU_LEAVE_DAILOG;
    private final Locator EXIT_BUTTON;
    private Locator MACROS;


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
        this.URL_TEXTAREA = WORKSPACE_FRAME.locator("//textarea[@name='url']");
        this.BODY_TEXTAREA = WORKSPACE_FRAME.locator("//textarea[@name='body']");
        this.PARAM = WORKSPACE_FRAME.locator("//p[contains(@cursor,'pointer')]");
        this.WEBHOOK_BUTTONS = WORKSPACE_FRAME.locator("//button[contains(@class,'ButtonItem-sc')]");
        this.WEBHOOK_SUCCESS_ALERT = WORKSPACE_FRAME.locator("//span[contains(text(),'Webhook setup successfully')]");
        this.WEBHOOK_SAVE_BUTTON = WORKSPACE_FRAME.locator("//button[@type='submit']");
        this.INLINE_ERROR_MESSAGE = WORKSPACE_FRAME.locator(" //label[contains(@class,'FieldLabel')]/following-sibling::div/div[contains(@class,'ValidationMessage')]");
        this.ERROR_ALERT = WORKSPACE_FRAME.locator(" //h3[contains(text(),'Error occurred while saving workspace or editing webhook')]");
        this.CREATE_WORKSPACE = WORKSPACE_FRAME.locator("//div[text()='Create New Workspace']");
        this.GO_TO_WORKSPACE_LIST = WORKSPACE_FRAME.locator("//div[contains(text(),'Go back to workspaces list')]");
        this.BEFORE_YOU_LEAVE_DAILOG = WORKSPACE_FRAME.locator("//h3[contains(text(),'Before you leave')]");
        this.EXIT_BUTTON = WORKSPACE_FRAME.locator("//div[contains(text(),'Yes, Exit')]");
    }

    public void studio() {
        STUDIO_CLICK.click();
        page.reload();
    }

    public void searchWorkspace(String workspace) {
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
        WORK_SPACECREATED_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
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

    public void clickRequestOrContentButton(String buttonName) {
        for (int i = 0; i < WEBHOOK_BUTTONS.count(); i++) {
            if (WEBHOOK_BUTTONS.nth(i).innerText().contains(buttonName) && WEBHOOK_BUTTONS.nth(i).getAttribute("aria-pressed").contains("true")) {
                break;
            }else if(WEBHOOK_BUTTONS.nth(i).innerText().contains(buttonName)){
                WEBHOOK_BUTTONS.nth(i).click();
                break;
            }
        }
    }

    public void addURL(String url) {
        URL_TEXTAREA.fill(url);
    }

    public String verifyMacrosAppendedToURL() {
        return URL_TEXTAREA.inputValue();
    }

    public void addBody(String jsonFile) throws IOException {
        BODY_TEXTAREA.fill(CommonUtils.readJsonTestDataFile(jsonFile));
    }

    public void addMacros(String textType, String param, List<String> macrosList){
        String xpath = String.format("//label[text()='%s']/ancestor::div[contains(@class, 'StyledCustomTextAreaContainer')]//span[contains(@class,'styles__StyledBIChip')]//span", textType);
        MACROS = WORKSPACE_FRAME.locator(xpath);
        for(int i = 0; i< MACROS.count(); i++){
            MACROS.nth(i).locator("text=" + macrosList.get(i)).click();
            if(PARAM.nth(i).isVisible()){
                for (int j = 0; j < PARAM.count(); j++) {
                    if (PARAM.nth(j).innerText().contains(param)) {
                        PARAM.nth(j).click();
                        page.keyboard().press("Escape");
                        break;
                    }
                }
            }
        }
    }

    public void saveWebhookSetup(){
        WEBHOOK_SAVE_BUTTON.click();
    }

    public String verifyWebhookCreationIsSuccess() {
        String alert = WEBHOOK_SUCCESS_ALERT.innerText().trim();
        WEBHOOK_SUCCESS_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        return alert;
    }

    public String verifyInlineErrorMessage(String invalidData) {
        String error = " ";
        URL_TEXTAREA.fill(invalidData);
        if(BODY_TEXTAREA.isVisible())
            BODY_TEXTAREA.fill(invalidData);
        WEBHOOK_SAVE_BUTTON.click();
        INLINE_ERROR_MESSAGE.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        if(INLINE_ERROR_MESSAGE.count()>0) {
            for (int i = 0; i < INLINE_ERROR_MESSAGE.count(); i++) {
                error = error + INLINE_ERROR_MESSAGE.nth(i).innerText();
            }
        }
        return error.trim();
    }

    public String verifyErrorMsgWhenAPIFailed(List<String> mediaTypeList) throws IOException {
        String alert = " ";
        URL_TEXTAREA.fill(mediaTypeList.get(0));
        if(BODY_TEXTAREA.isVisible()){
            String file = mediaTypeList.get(1).trim();
            BODY_TEXTAREA.fill(CommonUtils.readJsonTestDataFile(file));
        }
        WEBHOOK_SAVE_BUTTON.click();
        alert = ERROR_ALERT.innerText();
        ERROR_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        return alert;
    }

    public String verifyMacrosAppendedToBody() {
        return BODY_TEXTAREA.inputValue();
    }

    public String checkBackgroundColorOfWebhookIcon() {
        return WEBHOOK_ICON.evaluate("element => getComputedStyle(element).backgroundColor").toString();
    }

    public void goToWorkspaceList() {
        GO_TO_WORKSPACE_LIST.click();
        BEFORE_YOU_LEAVE_DAILOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        EXIT_BUTTON.click();
        CREATE_WORKSPACE.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }
}