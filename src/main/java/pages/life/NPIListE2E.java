package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

public class NPIListE2E {
    private final Page page;
    private final Locator CLICK_NPILISTS;
    private final Locator CLICK_ADDLIST;
    private final Locator VERIFY_NPILIST;
    private final Locator CLICK_SMARTLIST;
    private final Locator LIST_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator PRESCRIBED_DRUG;
    private final Locator CLICK_ADDDRUG;
    private final Locator SEARCH_DRUG;
    private final Locator SELECT_DRUG;
    private final Locator VERIFY_DRUG;
    private final Locator LIFE_AVAILABLEIN;
    private final Locator HCP365_AVAILABLEIN;
    private final Locator SAVE_BUTTON;
    private final Locator LIST_SUCCESS;
    //private final Locator CLICK_MAINMENU;
    //private final Locator CLICK_CAMPAIGNS;
   // private final Locator CLICK_SETTINGS;
    //private final Locator SELECT_GROUPBYCAMPAIGN;
   // private final Locator CLICK_ADVERTISERFILTER;
    //private final Locator SEARCH_FILTER;
    //private final Locator SELECT_FILTER;
    //private final Locator CLICK_APPLY;
    //private final Locator CLICK_CAMPAIGN;
    //private final Locator CLICK_LINEITEM;
    //private final Locator CLICK_TACTIC:
    //private final Locator VERIFY_SETTINGS;
    //private final Locator CLICK_TARGETINGRULE;
    //private final Locator CLICK_NPI;
    //private final Locator SEARCH_NPI;
    //private final Locator SELECT_NPI;
    //private final Locator CLICK_OK;
    //private final Locator CLICK_SAVE;
    //private final Locator Verify_TACTICTARGETING;


    public NPIListE2E (Page page) {
        this.page = page;
        this.CLICK_NPILISTS = page.locator("#megamenu").getByText("NPI Lists");
        this.CLICK_ADDLIST = page.locator("app-icon-lable-link").filter(new Locator.FilterOptions().setHasText("Add List")).getByRole(AriaRole.IMG);
        this.VERIFY_NPILIST = page.getByText("Create New NPI List");
        this.CLICK_SMARTLIST = page.getByText("Smart List");
        this.LIST_NAME = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("List Name"));
        this.SEARCH_ADVERTISER = page.locator("app-npilists-manager").getByRole(AriaRole.COMBOBOX);
        this.SELECT_ADVERTISER = page.getByText("- Advertiser");
        this.PRESCRIBED_DRUG = page.locator("#mat-checkbox-12 > .mat-checkbox-layout > .mat-checkbox-inner-container");
        this.CLICK_ADDDRUG = page.getByText("Add Drug");
        this.SEARCH_DRUG = page.getByRole(AriaRole.LISTBOX).filter(new Locator.FilterOptions().setHasText(Pattern.compile("^$"))).getByRole(AriaRole.COMBOBOX);
        this.SELECT_DRUG = page.getByText("Glynase0009-0352, 0009-0341, 0009-");
       // this.VERIFY_DRUG = page.getByRole('listbox').filter({hasText:'×Glynase' }).getByRole('combobox');
        this.VERIFY_DRUG = page.getByRole(AriaRole.LISTBOX).filter(new Locator.FilterOptions().setHasText("×Glynase")).getByRole(AriaRole.COMBOBOX);
        this.LIFE_AVAILABLEIN = page.locator("#mat-checkbox-4 > .mat-checkbox-layout > .mat-checkbox-inner-container");
        this.HCP365_AVAILABLEIN = page.locator("#mat-checkbox-5 > .mat-checkbox-layout > .mat-checkbox-inner-container");
        this.SAVE_BUTTON = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save"));
        this.LIST_SUCCESS = page.getByText("× NPI list created");

    }

        public void clickNPILists() {
            CLICK_NPILISTS.click();
        }

        public void clickAddlist() {
            CLICK_ADDLIST.click();

        }

        public String verifyNPIList() {
            return VERIFY_NPILIST.innerText();
        }

        public void clickSmartList() {
            CLICK_SMARTLIST.click();
        }

        public void enterListName(String npiListName) {
                LIST_NAME.fill(npiListName);
        }

        public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
         }

         public void selectPrescribedDrug() {
         PRESCRIBED_DRUG.check();
         }

         public void clickAddDrug() {
        CLICK_ADDDRUG.click();
         }

         public void selectDrug(String Drug) {
            SEARCH_DRUG.click();
            SEARCH_DRUG.fill("nas");
            SELECT_DRUG.locator(page.getByText("Glynase0009-0352, 0009-0341, 0009-")).click();
         }

         public String verifyDrug() {
        return VERIFY_DRUG.innerText();
         }

         public void selectProduct(){
             LIFE_AVAILABLEIN.check();
             HCP365_AVAILABLEIN.check();
         }

         public void saveList() {
            SAVE_BUTTON.click();
         }

         public String saveListSuccess() {
            return LIST_SUCCESS.innerText();
         }


}
