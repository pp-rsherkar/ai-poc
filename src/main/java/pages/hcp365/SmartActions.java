package pages.hcp365;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;

public class SmartActions {

    private final Page page;
    private final Locator CLICK_SMARTACTIONS;
    private final Locator CLICK_ADDSMARTACTION;
    private final Locator VERIFY_SMARTACTION;
    private final Locator SMARTACTION_NAME;
    private final Locator ADVERTISER;
    private final Locator SAVE_BUTTON;

    private final Locator SMARTACTION_SUCCESS;
    private final Locator AUDIENCE_TAB;
    private final Locator AUDIENCE_NPILISTS_OPTION;
    private final Locator NPILIST_SEARCH;
    private final Locator OK_BUTTON;
    private final Locator ACTION_TAB;
       private final Locator ACTION_VISITSBRANDPAGE_OPTION;
    private final Locator COLLECTION_COMBOBOX;
    private final Locator RESPONSE_TAB;
    private final Locator RESPONSE_ADDNPITOSMARTLIST_OPTION;
    private final Locator SMARTLISTNAME;
    private final Locator DAYS;
    private final Locator SELECTADVERTISER;
    private final Locator NPILIST_TARGET;
    private final Locator VERIFY_NPILISTNAME;
    private final Locator SAVEDSUCCESSMSG;
    private final Locator COLLECTION_OPTION;



    public SmartActions (Page page, String npiListName) {
        this.page = page;
        this.CLICK_SMARTACTIONS = page.locator("#megamenu").getByText("Smart Actions");
        this.CLICK_ADDSMARTACTION = page.getByText("Add Smart Action");
        this.VERIFY_SMARTACTION = page.getByText("Smart Action Properties");
        this.SMARTACTION_NAME = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Smart Action Name"));
        this.ADVERTISER = page.locator("//*[@id='triggerControlsContainer']/form/div[2]/div/ng-select");
        this.SELECTADVERTISER = page.getByText("01- Advertiser");
        this.SAVE_BUTTON = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"));
        this.SMARTACTION_SUCCESS = page.getByText("new Smart Action created successfully");
        this.AUDIENCE_TAB = page.locator("//div[@class='col-sm nav-tab selected']");
        this.AUDIENCE_NPILISTS_OPTION = page.getByText("NPI Lists");
        this.NPILIST_SEARCH = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search"));
        this.NPILIST_TARGET = page.locator("//div[@title='"+npiListName+"']/preceding-sibling::div[@class='float-left targetIcons ng-star-inserted']/div[@title='Target']");
        this.VERIFY_NPILISTNAME = page.locator("//div[@title='"+npiListName+"']");
        this.OK_BUTTON = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("ok"));
        this.SAVEDSUCCESSMSG = page.getByText("Data saved successfully");
        this.ACTION_TAB = page.locator("//div[@class='col-sm nav-tab']").first();
       // this.ACTION_TAB_SELECTED = page.locator("//div[@class='col-sm nav-tab selected'][text()='Action']");
        this.ACTION_VISITSBRANDPAGE_OPTION = page.getByText("Visits Brand Page");
        this.COLLECTION_COMBOBOX = page.locator("//div[text()='Collections']/parent::div/following-sibling::ng-select//input[@role='combobox']");
        this.COLLECTION_OPTION=page.locator("//div[@role='option']").first();
        this.RESPONSE_TAB = page.locator("//div[@class='col-sm nav-tab']").nth(1);
        this.RESPONSE_ADDNPITOSMARTLIST_OPTION =page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Add NPI to the Smart List"));
        this.SMARTLISTNAME = page.locator("//input[@formcontrolname='smartListName']");
        this.DAYS = page.locator("//input[@formcontrolname='noOfDays']");
    }

    public void responseTabDataEntryAndSave(String smartListName, String days){
        RESPONSE_TAB.click();
        RESPONSE_ADDNPITOSMARTLIST_OPTION.click();
        SMARTLISTNAME.click();
        SMARTLISTNAME.fill(smartListName);
        DAYS.fill(days);
        SAVE_BUTTON.click();
    }

    public void actionTabDataEntryAndSave(){
        ACTION_TAB.click();
      //  ACTION_TAB_SELECTED.click();
        ACTION_VISITSBRANDPAGE_OPTION.click();
        COLLECTION_COMBOBOX.click();
        COLLECTION_OPTION.click();
        SAVE_BUTTON.click();
    }


    public void enterSmartActionName(String SmartActionName) {
        SMARTACTION_NAME.fill(SmartActionName);
    }
    public void enterAdvertiser(String advertiser) {
      //  this.advertiser=advertiser;
        ADVERTISER.click();
      SELECTADVERTISER.click();

    }
    public void saveSmartAction() {
        SAVE_BUTTON.click();
    }

    public String verifySmartActionSuccessMsg() {return SMARTACTION_SUCCESS.innerText(); }
    public void clickSmartActions() {
        CLICK_SMARTACTIONS.click();
    }
    public void clickAddSmartAction() {
        CLICK_ADDSMARTACTION.click();
    }
    public String verifySmartAction() {return VERIFY_SMARTACTION.innerText(); }

    public String verifyAudienceTab(){
        return AUDIENCE_TAB.innerText();
    }

    public void clickNPIListsOption(){
        AUDIENCE_NPILISTS_OPTION.click();
    }

    public String searchNPIList(String npiListName){
        NPILIST_SEARCH.click();
        NPILIST_SEARCH.fill(npiListName);
        NPILIST_SEARCH.press("Enter");
        return VERIFY_NPILISTNAME.innerText();

    }

    public void targetNPIList(){
        NPILIST_TARGET.click();
    }

    public void saveNPILists(){
        OK_BUTTON.click();
        SAVE_BUTTON.click();
    }
    public String SavedMsg(){
        return SAVEDSUCCESSMSG.innerText();
    }

}
