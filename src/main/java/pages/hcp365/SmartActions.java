package pages.hcp365;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class SmartActions {

    private final Page page;
    private final Locator SMART_ACTIONS;
    private final Locator ADD_SMART_ACTION;
    private final Locator VERIFY_SMART_ACTION;
    private final Locator SMART_ACTION_NAME;
    private final Locator ADVERTISER;
    private final Locator SAVE_BUTTON;

    private final Locator SMART_ACTION_SUCCESS;
    private final Locator AUDIENCE_TAB;
    private final Locator AUDIENCE_NPI_LISTS_OPTION;
    private final Locator NPI_LIST_SEARCH;
    private final Locator OK_BUTTON;
    private final Locator ACTION_TAB;
    private final Locator ACTION_VISITS_BRAND_PAGE_OPTION;
    private final Locator COLLECTION_COMBO_BOX;
    private final Locator RESPONSE_TAB;
    private final Locator RESPONSE_ADD_NPI_TO_SMART_LIST_OPTION;
    private final Locator SMART_LIST_NAME;
    private final Locator DAYS;
    private final Locator SELECT_ADVERTISER;
    private final Locator NPI_LIST_TARGET;
    private final Locator VERIFY_NPI_LIST_NAME;
    private final Locator SAVED_SUCCESS_MESSAGE;
    private final Locator COLLECTION_OPTION;

    public SmartActions(Page page) {
        this.page = page;
        this.SMART_ACTIONS = page.locator("#megamenu").getByText("Smart Actions");
        this.ADD_SMART_ACTION = page.getByText("Add Smart Action");
        this.VERIFY_SMART_ACTION = page.getByText("Smart Action Properties");
        this.SMART_ACTION_NAME = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Smart Action Name"));
        this.ADVERTISER = page.locator("//*[@id='triggerControlsContainer']/form/div[2]/div/ng-select");
        this.SELECT_ADVERTISER = page.getByText("Z_Automation");
        this.SAVE_BUTTON = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"));
        this.SMART_ACTION_SUCCESS = page.getByText("new Smart Action created successfully");
        this.AUDIENCE_TAB = page.locator("//div[@class='col-sm nav-tab selected']");
        this.AUDIENCE_NPI_LISTS_OPTION = page.getByText("NPI Lists");
        this.NPI_LIST_SEARCH = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.NPI_LIST_TARGET = page.locator("//div[@class='treeviewNode clearfix ng-star-inserted' and not(@hidden)]//div[@title='Target']");
        this.VERIFY_NPI_LIST_NAME = page.locator("//div[@class='treeviewNode clearfix ng-star-inserted' and not(@hidden)]//div[@customclass='primary-tooltip']");
        this.OK_BUTTON = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ok"));
        this.SAVED_SUCCESS_MESSAGE = page.getByText("Data saved successfully");
        this.ACTION_TAB = page.locator("//div[@class='col-sm nav-tab']").first();
        this.ACTION_VISITS_BRAND_PAGE_OPTION = page.getByText("Visits Brand Page");
        this.COLLECTION_COMBO_BOX = page.locator("//div[text()='Collections']/parent::div/following-sibling::ng-select//input[@role='combobox']");
        this.COLLECTION_OPTION = page.locator("//div[@role='option']").first();
        this.RESPONSE_TAB = page.locator("//div[@class='col-sm nav-tab']").nth(1);
        this.RESPONSE_ADD_NPI_TO_SMART_LIST_OPTION = page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Add NPI to the Smart List"));
        this.SMART_LIST_NAME = page.locator("//input[@formcontrolname='smartListName']");
        this.DAYS = page.locator("//input[@formcontrolname='noOfDays']");
    }

    public void responseTabDataEntryAndSave(String smartListName, String days) {
        RESPONSE_TAB.click();
        RESPONSE_ADD_NPI_TO_SMART_LIST_OPTION.click();
        SMART_LIST_NAME.click();
        SMART_LIST_NAME.fill(smartListName);
        DAYS.fill(days);
        SAVE_BUTTON.click();
    }

    public void actionTabDataEntryAndSave() {
        ACTION_TAB.click();
        ACTION_VISITS_BRAND_PAGE_OPTION.click();
        COLLECTION_COMBO_BOX.click();
        COLLECTION_OPTION.click();
        SAVE_BUTTON.click();
    }


    public void enterSmartActionName(String SmartActionName) {
        SMART_ACTION_NAME.fill(SmartActionName);
    }

    public void enterAdvertiser(String advertiser) {
        ADVERTISER.click();
        SELECT_ADVERTISER.click();

    }

    public void saveSmartAction() {
        SAVE_BUTTON.click();
    }

    public String getSmartActionSuccessMessage() {
        return SMART_ACTION_SUCCESS.innerText();
    }

    public void clickSmartActions() {
        SMART_ACTIONS.click();
    }

    public void clickAddSmartAction() {
        ADD_SMART_ACTION.click();
    }

    public String getSmartAction() {
        return VERIFY_SMART_ACTION.innerText();
    }

    public String getAudienceTab() {
        return AUDIENCE_TAB.innerText();
    }

    public void clickNPIListsOption() {
        AUDIENCE_NPI_LISTS_OPTION.click();
    }

    public String searchNPIList(String npiListName) {
        NPI_LIST_SEARCH.click();
        NPI_LIST_SEARCH.fill(npiListName);
        NPI_LIST_SEARCH.press("Enter");
        return VERIFY_NPI_LIST_NAME.innerText();

    }

    public void targetNPIList() {
        NPI_LIST_TARGET.click();
    }

    public void saveNPILists() {
        OK_BUTTON.click();
        SAVE_BUTTON.click();
    }

    public String getSavedMessage() {
        return SAVED_SUCCESS_MESSAGE.innerText();
    }

}
