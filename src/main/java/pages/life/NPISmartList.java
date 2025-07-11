package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.regex.Pattern;

public class NPISmartList {
    private final Page page;
    private final Locator CLICK_SMART_LIST;
    private final Locator CLICK_SMART_PIXEL;
    private final Locator CLICK_NPI_LIST;
    private final Locator CLICK_SMART_PIXEL_DROPDOWN;
    private final Locator CLICK_SMART_PIXEL_DROPDOWN_VALUE;
    private final Locator CLICK_NPI_GROUP;
    private final Locator CLICK_SPECIALTY;
    private final Locator CLICK_PROFESSION;
    private final Locator CLICK_PRESCRIBEDDRUG;
    private final Locator CLICK_DIAGNOSIS;
    private final Locator CLICK_MEDICAL_PROCEDURE;
    private final Locator CLICK_ENDEMIC_RESEARCH;
    private final Locator CLICK_ENDEMIC_DROPDOWN;
    private final Locator CLICK_ENDEMIC_DROPDOWN_VALUE;
    private final Locator CLICK_PRESCRIPTION_BEHAVIOUR_CHANGE;
    private final Locator CLICK_PRESCRIPTION_DROPDOWN;
    private final Locator CLICK_PRESCRIPTION_DROPDOWN_VALUE;
    private final Locator CLICK_SPECIALTY_DROPDOWN;
    private final Locator SELECT_SPECIALTY_VALUE;

    private final Locator CLICK_PROFESSION_DROPDOWN;
    private final Locator SELECT_PROFESSION_VALUE;
    private final Locator CLICK_NPI_GROUP_VALUE;
    private final Locator CLICK_LIFE_CHECKBOX;
    private final Locator LIST_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator SELECT_ADVERTISER;
    private final Locator PRESCRIBED_DRUG;
    private final Locator SEARCH_DRUG;
    private final Locator SEARCH_DRUG_FILL;
    private final Locator SELECT_DRUG;
    private final Locator VERIFY_DRUG;
    private final Locator LIFE_AVAILABLE_IN;
    private final Locator HCP365_AVAILABLE_IN;
    private final Locator PULSEPOINT_ICON;
    private final Locator CLICK_EXPAND_PRACTICE;

    private final Locator SPINNER;
    private final Locator ADD_DRUG_BTN;

    public NPISmartList(Page page) {
        this.page = page;
        this.CLICK_SMART_LIST = page.getByText("Smart List", new Page.GetByTextOptions().setExact(true));
        this.CLICK_SMART_PIXEL = page.getByText("Smart Pixel", new Page.GetByTextOptions().setExact(true));
        this.CLICK_NPI_LIST = page.getByText("NPI List", new Page.GetByTextOptions().setExact(true));
        this.CLICK_PRESCRIPTION_BEHAVIOUR_CHANGE= page.getByText("Prescription Behavior Change", new Page.GetByTextOptions().setExact(true));
        this.CLICK_SPECIALTY = page.getByText("Specialty", new Page.GetByTextOptions().setExact(true));
        this.CLICK_PROFESSION = page.getByText("Profession", new Page.GetByTextOptions().setExact(true));
        this.CLICK_PRESCRIBEDDRUG = page.getByText("Prescribed Drug", new Page.GetByTextOptions().setExact(true));
        this.CLICK_DIAGNOSIS = page.getByText("Diagnosis", new Page.GetByTextOptions().setExact(true));
        this.CLICK_MEDICAL_PROCEDURE = page.getByText("Medical Procedure", new Page.GetByTextOptions().setExact(true));
        this.CLICK_ENDEMIC_RESEARCH = page.getByText("Endemic Research", new Page.GetByTextOptions().setExact(true));
        this.CLICK_ENDEMIC_DROPDOWN = page.getByText("Select MESH Conditions");
        this.CLICK_ENDEMIC_DROPDOWN_VALUE = page.locator(".pull-left > .iconSprite").first();
        this.CLICK_SPECIALTY_DROPDOWN =  page.getByText("Select Specialities");
        this.SELECT_SPECIALTY_VALUE= page.locator(".pull-left > .iconSprite").first();
        this.CLICK_PRESCRIPTION_DROPDOWN =  page.locator("#prescriptionBehaviorWrapper").getByRole(AriaRole.COMBOBOX);
        this.CLICK_PRESCRIPTION_DROPDOWN_VALUE=  page.getByText("Parastat44911-0034, 44911-");
        this.CLICK_PROFESSION_DROPDOWN =  page.getByText("Select Profession");
        this.SELECT_PROFESSION_VALUE= page.getByText("Nurse Practitioner");
        this.CLICK_EXPAND_PRACTICE=page.getByText("Expand based on Practice and Hospital affiliation", new Page.GetByTextOptions().setExact(true));
        this.CLICK_SMART_PIXEL_DROPDOWN = page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Select Smart Pixel$"))).nth(1);
        this.CLICK_SMART_PIXEL_DROPDOWN_VALUE =  page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("01- Advertiser Smart Pixel 1").setExact(true)).locator("div");
        this.CLICK_NPI_GROUP = page.getByText("Select NPI Groups");
        this.CLICK_NPI_GROUP_VALUE = page.getByText("0_Test_PB_02");
        this.CLICK_LIFE_CHECKBOX = page.locator("#mat-checkbox-4 > .mat-checkbox-layout > .mat-checkbox-inner-container");
        this.LIST_NAME = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("List Name"));
        this.SEARCH_ADVERTISER = page.locator("app-npilists-manager").getByRole(AriaRole.COMBOBOX);
        this.SELECT_ADVERTISER = page.locator("//div[contains(@class,'dropdown-items ng-star-inserted')]");
        this.PRESCRIBED_DRUG = page.locator("//label[contains(@class, 'mat-checkbox-layout') and .//span[contains(text(), 'Prescribed Drug')]]");
        this.SEARCH_DRUG = page.locator("//ng-select[contains(@class, 'drugDropdown ')]");
        this.SEARCH_DRUG_FILL = page.locator("//ng-select[contains(@class, 'drugDropdown ')]//input");
        this.SELECT_DRUG = page.locator("//div[contains(@class,'dropdown-items')]");
        this.VERIFY_DRUG = page.locator("//ng-select[contains(@class, 'drugDropdown ')]//span[contains(@class,'ng-value-label')]");
        this.LIFE_AVAILABLE_IN = page.locator("//span[contains(text(),'Life')]");
        this.HCP365_AVAILABLE_IN = page.locator("//span[contains(text(),'HCP365')]");
        this.PULSEPOINT_ICON = page.locator("//div[@class='logo-lists']/img[@alt='logo']");
        this.SPINNER = page.locator("//div[contains(text(),'Loading...')]");
        this.ADD_DRUG_BTN = page.locator("//span[contains(text(), 'Add Drug')]");

    }

    public void clickSmartList() {
        CLICK_SMART_LIST.click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }
    public void clickSmartPixel() {
        CLICK_SMART_PIXEL.click();
    }
    public void clickNPIList() {
        CLICK_NPI_LIST.click();
    }
    public void clickSmartPixelDropDown() {
        CLICK_SMART_PIXEL_DROPDOWN.click();
    }
    public void clickSmartPixelDropDownValue() {
        CLICK_SMART_PIXEL_DROPDOWN_VALUE.click();
    }
    public void clickNPIGroup() {
        CLICK_NPI_GROUP.click();
    }
    public void clickNPIGroupValue() {
        CLICK_NPI_GROUP_VALUE.click();
    }


    public void clickLifeCheckbox() {
        CLICK_LIFE_CHECKBOX.click();
    }
    public void clickSpecialty() {
        CLICK_SPECIALTY.click();
    }
    public void clickProfession() {
        CLICK_PROFESSION.click();
    }
    public void clickPrescribedDrug() {
        CLICK_PRESCRIBEDDRUG.click();
    }
    public void clickDiagnosis() {
        CLICK_DIAGNOSIS.click();
    }
    public void clickMedicalProcedure() {
        CLICK_MEDICAL_PROCEDURE.click();
    }
    public void clickEndemicResearch() {
        CLICK_ENDEMIC_RESEARCH.click();
    }
    public void clickPrescriptionBehaviorChange() {
        CLICK_PRESCRIPTION_BEHAVIOUR_CHANGE.click();
    }

    public void SelectPrescriptionBehaviorDetails() {
        CLICK_PRESCRIPTION_DROPDOWN.click();
        CLICK_PRESCRIPTION_DROPDOWN.fill("para");
        CLICK_PRESCRIPTION_DROPDOWN_VALUE.click();
    }
    public void SelectEndemicDetails() {
        CLICK_ENDEMIC_DROPDOWN.click();
        CLICK_ENDEMIC_DROPDOWN_VALUE.click();
    }

    public void clickSpecialtyDropdown() {
        CLICK_SPECIALTY_DROPDOWN.click();
    }

    public void selectSpecialtyValue() {
        SELECT_SPECIALTY_VALUE.click();
    }
    public void clickProfessionDropdown() {
        CLICK_PROFESSION_DROPDOWN.click();
    }

    public void selectProfessionValue() {
        SELECT_PROFESSION_VALUE.click();
    }
    public void clickExpandPractice() {
        CLICK_EXPAND_PRACTICE.click();
    }
    public void enterListName(String listName) {
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        LIST_NAME.fill(listName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
        SPINNER.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void selectPrescribedDrug() {
        PRESCRIBED_DRUG.first().scrollIntoViewIfNeeded();
        PRESCRIBED_DRUG.first().check();
        ADD_DRUG_BTN.waitFor(new Locator.WaitForOptions().setTimeout(120000).setState(WaitForSelectorState.VISIBLE));
    }

    public void selectDrug(String drug) {
        ADD_DRUG_BTN.scrollIntoViewIfNeeded();
        ADD_DRUG_BTN.click();
        SEARCH_DRUG.first().click();
        SEARCH_DRUG_FILL.fill(drug);
        SELECT_DRUG.click();
    }

    public String verifyDrug() {
        return VERIFY_DRUG.innerText();
    }

    public void selectProduct() {
        LIFE_AVAILABLE_IN.check();
        HCP365_AVAILABLE_IN.check();
    }

    public void clickPulsepointICon() {
        PULSEPOINT_ICON.click();
    }
}