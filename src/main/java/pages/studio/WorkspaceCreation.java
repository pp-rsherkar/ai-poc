package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;

public class WorkspaceCreation {

    ExplorerWorkspace explorerWorkspace = new ExplorerWorkspace(DriverFactory.getPage());
    private final Page page;
    private final Locator CREATE_WORKSPACE;
    private final Locator HCP_EXPLORER;
    private final Locator HCP_EXPANSION;
    private final Locator BACK_TO_WORKSPACE_DASHBOARD;
    private final Locator WORK_SPACECREATED_ALERT;
    private final Locator MENU_ICON;
    private final FrameLocator WORKSPACE_FRAME;
    private final Locator WORKSPACE_TYPE;
    private final Locator MORE_ACTION_DIALOG;
    private final Locator DELETE_BUTTON;
    private final Locator REMOVAL_CONFIRMATION_POPUP;
    private final Locator REMOVAL_CONFIRMATION_TEXT;
    private final Locator REMOVE_BUTTON;
    private final Locator WORKSPACE_ARCHIVAL_ALERT;
    private final Locator OUTER_FRAME;
    private final Locator RENAME_BUTTON;
    private final Locator RENAME_WORKSPACE_POPUP;
    private final Locator UPDATE_BUTTON;
    private final Locator RENAME_WORKSPACE_ALERT;
    private final Locator SEARCH_WORKSPACE;
    private final Locator DUPLICATE_BUTTON;
    private final Locator DUPLICATE_WORKSPACE_POPUP;
    private final Locator DUPLICATE_BUTTON_FROM_POPUP;
    private final Locator DUPLICATE_WORKSPACE_ALERT;
    private final Locator DUPLICATE_WORKSPACE_NAME;
    int counter = 0;

    public WorkspaceCreation(Page page) {
        this.page = page;
        this.WORKSPACE_FRAME = page.frameLocator("iframe#iframe0").frameLocator("iframe");
        this.CREATE_WORKSPACE = WORKSPACE_FRAME.locator("//div[text()='Create New Workspace' or contains(text(),'Open New Workspace')]");
        this.HCP_EXPLORER = WORKSPACE_FRAME.locator("//p[contains(text(),'HCP Explorer')]");
        this.HCP_EXPANSION = WORKSPACE_FRAME.locator("//label[contains(text(),'HCP Audience Expansion')]");
        this.BACK_TO_WORKSPACE_DASHBOARD = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON);
        this.WORK_SPACECREATED_ALERT = WORKSPACE_FRAME.locator("//h3[contains(text(),'Saving workspace') or contains(text(),'Creating workspace')]/following-sibling::span");
        this.MENU_ICON = page.locator("//img[contains(@class,'menu-icon')]");
        this.WORKSPACE_TYPE = WORKSPACE_FRAME.locator("//p[text()='Workspace Type']");
        this.MORE_ACTION_DIALOG = WORKSPACE_FRAME.locator("//div[@role='dialog']");
        this.DELETE_BUTTON = WORKSPACE_FRAME.locator("//div[contains(text(),'Delete')]");
        this.REMOVAL_CONFIRMATION_POPUP = WORKSPACE_FRAME.locator("//h3[contains(text(),'Removal Confirmation')]");
        this.REMOVAL_CONFIRMATION_TEXT = WORKSPACE_FRAME.locator("//div[contains(text(),'You are trying to delete the workspace')]");
        this.REMOVE_BUTTON = WORKSPACE_FRAME.locator("//div[text()='Remove']");
        this.WORKSPACE_ARCHIVAL_ALERT = WORKSPACE_FRAME.locator("//span[contains(text(),'Workspace deleted successfully')]");
        this.OUTER_FRAME = page.frameLocator("iframe#iframe0").locator("//div[@data-testid='chatty-frame']");
        this.RENAME_BUTTON = WORKSPACE_FRAME.locator("//div[contains(text(),'Rename')]");
        this.RENAME_WORKSPACE_POPUP = WORKSPACE_FRAME.locator("//h3[contains(text(),'Rename Workspace')]");
        this.UPDATE_BUTTON = WORKSPACE_FRAME.locator("//div[contains(text(),'Update')]");
        this.RENAME_WORKSPACE_ALERT = WORKSPACE_FRAME.locator("//span[contains(text(),'Workspace renamed successfully')]");
        this.SEARCH_WORKSPACE = WORKSPACE_FRAME.locator("//input[contains(@placeholder,'Search')]");
        this.DUPLICATE_BUTTON = WORKSPACE_FRAME.locator("//div[contains(text(),'Duplicate')]");
        this.DUPLICATE_WORKSPACE_POPUP = WORKSPACE_FRAME.locator("//h3[contains(text(),'Duplicate Workspace')]");
        this.DUPLICATE_BUTTON_FROM_POPUP = WORKSPACE_FRAME.locator("//h3[contains(text(),'Duplicate Workspace')]/parent::header/following-sibling::footer//div[contains(text(),'Duplicate')]");
        this.DUPLICATE_WORKSPACE_ALERT = WORKSPACE_FRAME.locator("//span[contains(text(),'Workspace duplicated successfully')]");
        this.DUPLICATE_WORKSPACE_NAME = WORKSPACE_FRAME.locator("//span/b[starts-with(text(), 'Copy of Explorer')]");
    }

    public String studioDashboard() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        return this.page.title();
    }

    public String verifyHCPExplorer() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
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
       while(!CREATE_WORKSPACE.first().isEnabled()){
                MENU_ICON.click();
                page.keyboard().press("Escape");
       }
       CREATE_WORKSPACE.first().click();
       page.waitForCondition(() -> WORKSPACE_TYPE.filter(new Locator.FilterOptions().setHasText("Workspace Type")).count() == 1);
    }

    public void verifyStudioWorkspaceFrame(){
        OUTER_FRAME.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        CREATE_WORKSPACE.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
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

    public void openMoreActionsForWorkspace(String workspaceName){
        WORKSPACE_FRAME.locator(String.format("//td[contains(@id,'%s')]//button",workspaceName)).first().click();
    }

    public void deleteWorkspace() {
        MORE_ACTION_DIALOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DELETE_BUTTON.scrollIntoViewIfNeeded();
        DELETE_BUTTON.click();
        REMOVAL_CONFIRMATION_POPUP.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public String verifyDeletePopUp() {
       return REMOVAL_CONFIRMATION_TEXT.innerText();
    }

    public String deleteWorkspaceWithActiveWebhook() {
        String text = " ";
        REMOVE_BUTTON.click();
        text = WORKSPACE_ARCHIVAL_ALERT.innerText();
        WORKSPACE_ARCHIVAL_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        return text;
    }

    public void selectRenameWorkspaceFromOptions(){
        MORE_ACTION_DIALOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        RENAME_BUTTON.scrollIntoViewIfNeeded();
        RENAME_BUTTON.click();
        RENAME_WORKSPACE_POPUP.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public String renameWorkspaceName(String oldWorkspaceName, String newWorkspace) {
        WORKSPACE_FRAME.locator(String.format("//input[contains(@value,'%s')]", oldWorkspaceName)).fill(newWorkspace);
        UPDATE_BUTTON.click();
        return RENAME_WORKSPACE_ALERT.innerText();
    }

    public boolean searchWorkspaceName(String workspaceName) {
        SEARCH_WORKSPACE.fill(workspaceName);
        page.keyboard().press("Enter");
        return WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]", workspaceName)).isVisible();
    }

    public void selectDuplicateWorkspaceFromOptions(){
        MORE_ACTION_DIALOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DUPLICATE_BUTTON.scrollIntoViewIfNeeded();
        DUPLICATE_BUTTON.click();
        DUPLICATE_WORKSPACE_POPUP.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public String fetchDuplicateWorkspaceName(){
        return DUPLICATE_WORKSPACE_NAME.innerText();
    }

    public String clickDuplicateButton() {
        DUPLICATE_BUTTON_FROM_POPUP.click();
        return DUPLICATE_WORKSPACE_ALERT.innerText();
    }

    public void clickWorkspace(String workspaceName){
        WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]", workspaceName)).click();
    }

    public boolean navigateToWorkspace(String workspace) {
        return WORKSPACE_FRAME.locator(String.format("//h1[contains(text(),'%s')]", workspace)).isVisible();
    }
}