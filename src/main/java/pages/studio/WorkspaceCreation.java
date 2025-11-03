package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.WaitUtility;

public class WorkspaceCreation {

    private final Page page;
    private final Locator CREATE_WORKSPACE;
    private final Locator HCP_EXPLORER;
    private final Locator HCP_EXPANSION;
    private final Locator BACK_TO_WORKSPACE_DASHBOARD;
    private final Locator WORKSPACE_CREATED_ALERT;
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
    private final Locator DASHBOARD_RELOAD_ICON;
    private final Locator PAGINATION;
    private final Locator DEPENDENT_WORKSPACE_TEXT;
    private final Locator REMOVE_WORKSPACE_BUTTON;
    private final Locator DELETE_WORKSPACE_ERROR_ALERT_BOX;
    private final Locator DELETE_WORKSPACE_ERROR_TEXT;
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    int counter = 0;

    public WorkspaceCreation(Page page) {
        this.page = page;
        this.WORKSPACE_FRAME = page.frameLocator("iframe#iframe0").frameLocator("iframe");
        this.CREATE_WORKSPACE = WORKSPACE_FRAME.locator("//div[text()='Create New Workspace' or contains(text(),'Open New Workspace')]");
        this.HCP_EXPLORER = WORKSPACE_FRAME.locator("//p[contains(text(),'HCP Explorer')]");
        this.HCP_EXPANSION = WORKSPACE_FRAME.locator("//label[contains(text(),'HCP Audience Expansion')]");
        this.BACK_TO_WORKSPACE_DASHBOARD = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON);
        this.WORKSPACE_CREATED_ALERT = WORKSPACE_FRAME.locator("//p[contains(text(),'Workspace created successfully') or contains(text(),'Workspace saved successfully')]");
        this.MENU_ICON = page.locator("//img[contains(@class,'menu-icon')]");
        this.WORKSPACE_TYPE = WORKSPACE_FRAME.locator("//p[text()='Workspace Type']");
        this.MORE_ACTION_DIALOG = WORKSPACE_FRAME.locator("//div[@role='dialog']");
        this.DELETE_BUTTON = WORKSPACE_FRAME.locator("//div[contains(text(),'Delete')]");
        this.REMOVAL_CONFIRMATION_POPUP = WORKSPACE_FRAME.locator("//h3[contains(text(),'Removal Confirmation')]");
        this.REMOVAL_CONFIRMATION_TEXT = WORKSPACE_FRAME.locator("//div[contains(text(),'You are trying to delete the workspace')]");
        this.REMOVE_BUTTON = WORKSPACE_FRAME.locator("//div[text()='Remove']");
        this.WORKSPACE_ARCHIVAL_ALERT = WORKSPACE_FRAME.locator("//p[contains(text(),'Workspace deleted successfully')]");
        this.OUTER_FRAME = page.frameLocator("iframe#iframe0").locator("//div[@data-testid='chatty-frame']");
        this.RENAME_BUTTON = WORKSPACE_FRAME.locator("//div[contains(text(),'Rename')]");
        this.RENAME_WORKSPACE_POPUP = WORKSPACE_FRAME.locator("//h3[contains(text(),'Rename Workspace')]");
        this.UPDATE_BUTTON = WORKSPACE_FRAME.locator("//div[contains(text(),'Update')]");
        this.RENAME_WORKSPACE_ALERT = WORKSPACE_FRAME.locator("//p[contains(text(),'Workspace renamed successfully')]");
        this.SEARCH_WORKSPACE = WORKSPACE_FRAME.locator("//input[contains(@placeholder,'Search')]");
        this.DUPLICATE_BUTTON = WORKSPACE_FRAME.locator("//div[contains(text(),'Duplicate')]");
        this.DUPLICATE_WORKSPACE_POPUP = WORKSPACE_FRAME.locator("//h3[contains(text(),'Duplicate Workspace')]");
        this.DUPLICATE_BUTTON_FROM_POPUP = WORKSPACE_FRAME.locator("//h3[contains(text(),'Duplicate Workspace')]/parent::header/following-sibling::footer//div[contains(text(),'Duplicate')]");
        this.DUPLICATE_WORKSPACE_ALERT = WORKSPACE_FRAME.locator("//p[contains(text(),'Workspace duplicated successfully')]");
        this.DUPLICATE_WORKSPACE_NAME = WORKSPACE_FRAME.locator("//span/b[starts-with(text(), 'Copy of')]");
        this.DASHBOARD_RELOAD_ICON = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator("//div[contains(text(),'Reload')]");
        this.PAGINATION = WORKSPACE_FRAME.locator("(//div[contains(@class, 'PaginationContainer')]//span)[1]");
        this.DEPENDENT_WORKSPACE_TEXT = WORKSPACE_FRAME.locator("//div[contains(text(),'Checking for dependent workspaces...')]");
        this.REMOVE_WORKSPACE_BUTTON = WORKSPACE_FRAME.locator("//button/div[text()='Remove']");
        this.DELETE_WORKSPACE_ERROR_ALERT_BOX = WORKSPACE_FRAME.locator("//h3[contains(text(),'Deleting workspace')]");
        this.DELETE_WORKSPACE_ERROR_TEXT = WORKSPACE_FRAME.locator("//p[contains(text(),\"Deletion blocked by Life. Message: This list can't be deleted\")]");
    }

    public String studioDashboard() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        return this.page.title();
    }

    public String getHCPExplorerText() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        return HCP_EXPLORER.innerText();
    }

    public String getHCPAudienceExpansionText() {
        return HCP_EXPANSION.innerText();
    }

    public void clickHCPExplorerWorkspace() {
        HCP_EXPLORER.click();
    }

    public String getWorkspaceCreationText() {
        String text = WORKSPACE_CREATED_ALERT.innerText();
        waitUtility.waitForLocatorHidden(WORKSPACE_CREATED_ALERT);
        return text;
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

    public void clickMoreActionsMenu(String workspaceName){
        if(WORKSPACE_FRAME.locator(String.format("//td[contains(@id,'%s')]//button",workspaceName)).first().isVisible())
            WORKSPACE_FRAME.locator(String.format("//td[contains(@id,'%s')]//button",workspaceName)).first().click();
    }

    public void deleteWorkspace() {
        MORE_ACTION_DIALOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DELETE_BUTTON.scrollIntoViewIfNeeded();
        DELETE_BUTTON.click();
        REMOVAL_CONFIRMATION_POPUP.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void performActionOnWorkspace(String actionName){
        MORE_ACTION_DIALOG.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        switch(actionName){
            case "Duplicate":
                DUPLICATE_BUTTON.scrollIntoViewIfNeeded();
                DUPLICATE_BUTTON.click();
                DUPLICATE_WORKSPACE_POPUP.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                break;
            case "Delete":
                DELETE_BUTTON.scrollIntoViewIfNeeded();
                DELETE_BUTTON.click();
                REMOVAL_CONFIRMATION_POPUP.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                break;
            case "Rename":
                RENAME_BUTTON.scrollIntoViewIfNeeded();
                RENAME_BUTTON.click();
                RENAME_WORKSPACE_POPUP.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                break;
        }
    }

    public String getDeletePopUpText() {
        waitUtility.waitForLocatorHidden(DEPENDENT_WORKSPACE_TEXT);
       return REMOVAL_CONFIRMATION_TEXT.innerText();
    }

    public String deleteWorkspaceWithActiveWebhook() {
        String text = " ";
        REMOVE_BUTTON.click();
        text = WORKSPACE_ARCHIVAL_ALERT.innerText();
        WORKSPACE_ARCHIVAL_ALERT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        return text;
    }

    public String renameWorkspaceName(String oldWorkspaceName, String newWorkspace) {
        WORKSPACE_FRAME.locator(String.format("//h3[text()='Rename Workspace']/parent::header/following-sibling::div//input[@value='%s']", oldWorkspaceName)).fill(newWorkspace);
        if(!UPDATE_BUTTON.isEnabled())
            page.waitForTimeout(2000);
        UPDATE_BUTTON.click();
        String text = RENAME_WORKSPACE_ALERT.innerText();
        waitUtility.waitForLocatorHidden(RENAME_WORKSPACE_ALERT);
        return text;
    }

    public boolean searchWorkspaceName(String workspaceName) {
        while(!CREATE_WORKSPACE.first().isEnabled()){
           MENU_ICON.click();
           page.keyboard().press("Escape");
        }
        page.waitForCondition(() -> SEARCH_WORKSPACE.filter().count() == 1);
        if(WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]", workspaceName)).isVisible()){
            return true;
        }else {
            SEARCH_WORKSPACE.fill(workspaceName);
            page.keyboard().press("Enter");
            waitUtility.waitForLocatorVisible(PAGINATION);
            return WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]", workspaceName)).isVisible();
        }
    }

    public String fetchDuplicateWorkspaceName(){
        return DUPLICATE_WORKSPACE_NAME.innerText();
    }

    public String clickDuplicateButton() {
        DUPLICATE_BUTTON_FROM_POPUP.click();
        String text = DUPLICATE_WORKSPACE_ALERT.innerText();
        waitUtility.waitForLocatorHidden(DUPLICATE_WORKSPACE_ALERT);
        return text;
    }

    public void clickWorkspace(String workspaceName){
        WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]", workspaceName)).click();
    }

    public boolean navigateToWorkspace(String workspace) {
        DASHBOARD_RELOAD_ICON.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return WORKSPACE_FRAME.locator(String.format("//h1[contains(text(),'%s')]", workspace)).isVisible();
    }

    public void selectMoreActionsMenu(String workspaceName) {
        Locator moreActionsButton = WORKSPACE_FRAME.locator(String.format("//td[contains(@id,'%s')]//button",workspaceName)).first();
        waitUtility.waitForLocatorVisible(moreActionsButton);
        moreActionsButton.click();
    }

    public void clickRemoveWorkspaceButton() {
        REMOVE_WORKSPACE_BUTTON.click();
    }

    public String getDeleteWorkspaceErrorMessage() {
        waitUtility.waitForLocatorVisible(DELETE_WORKSPACE_ERROR_ALERT_BOX);
        waitUtility.waitForLocatorVisible(DELETE_WORKSPACE_ERROR_TEXT);
        String errorText = DELETE_WORKSPACE_ERROR_TEXT.innerText();
        waitUtility.waitForLocatorHidden(DELETE_WORKSPACE_ERROR_ALERT_BOX);
        return errorText;
    }
}