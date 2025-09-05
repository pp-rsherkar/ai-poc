package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;
import utils.Constants;
import utils.WaitUtility;

import java.util.regex.Pattern;

public class NPISmartList {
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    private final Page page;
    private final Locator CLICK_SMART_LIST;
    private final Locator CLICK_SMART_PIXEL;
    private final Locator CLICK_NPI_LIST;
    private final Locator CLICK_SMART_PIXEL_DROPDOWN;
    private final Locator CLICK_ADD_VISITED_URL;
    private final Locator CLICK_ADD_IGNORED_URL;
    private final Locator INSERT_VISITED_URL;
    private final Locator INSERT_IGNORED_URL;
    private final Locator CLICK_NPI_GROUP;
    private final Locator CLICK_SPECIALTY;
    private final Locator CLICK_PROFESSION;
    private final Locator CLICK_PRESCRIBEDDRUG;
    private final Locator CLICK_DIAGNOSIS;
    private final Locator CLICK_MEDICAL_PROCEDURE;
    private final Locator CLICK_ENDEMIC_RESEARCH;
    private final Locator CLICK_ENDEMIC_DROPDOWN;
    private final Locator CLICK_ENDEMIC_DROPDOWN_VALUE;
    private final Locator CLICK_ENDEMIC_SAVE;
    private final Locator CLICK_PRESCRIPTION_BEHAVIOUR_CHANGE;
    private final Locator CLICK_PRESCRIPTION_DROPDOWN;
    private final Locator CLICK_PRESCRIPTION_DROPDOWN_VALUE;
    private final Locator CLICK_SPECIALTY_DROPDOWN;
    private final Locator SELECT_SPECIALTY_VALUE;
    private final Locator CLICK_PROFESSION_DROPDOWN;
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
    private final Locator ADD_DRUG_BUTTON;
    private final Locator OBSERVED_WITHIN_LAST_DAYS;
    private final Locator DAYS_FIELD;
    private final Locator VISITED_MORE_THAN;
    private final Locator PAGES_FIELD;
    private final Locator SEARCH_KEYWORD_CHECKBOX_ENGAGEDVIASEARCH;
    private final Locator SEARCH_KEYWORD_CHECKBOX_ENGAGEDANYWHERE;
    private final Locator PRESENT_BUTTON;
    private final Locator ABSENT_BUTTON;
    private final Locator KEYWORD_TEXTFIELD;
    private final Locator NPI_CHECKBOX;
    private final Locator PROFESSIONAL_POPUP;
    private final Locator PRESCRIBED_DRUG_POPUP;
    private final Locator NPI_PRESCRIBED_DRUG;
    private final Locator NPI_ADD_DRUG;
    private final Locator NPI_FILL_DRUG;
    private final Locator NPI_DRUG_SELECT;
    private final Locator RECENCY_FIELD;
    private final Locator RECENCY_POPUP;
    private final Locator DECILE_RANGE;
    private final Locator MEDICAL_DECILE_RANGE;
    private final Locator  DISEASE_POPUP;
    private final Locator  DISEASE_VALUE;
    private final Locator  DIAGNOSIS_RECENCY_POPUP;
    private final Locator NPI_DISEASE;
    private final Locator DISEASE_SELECT;
    private final Locator DRUG_FIELD;
    private final Locator DRUG_VALUE;
    private static Locator ENGAGED_ONSITE_OPTIONS = null;
    private static Locator ENGAGED_VIA_SEARCH_OPTIONS = null;
    private static Locator ENGAGED_ANYWHERE_OPTIONS = null;
    private static Locator SMART_NPI_OPTIONS = null;

    public NPISmartList(Page page) {
        this.page = page;
        this.CLICK_SMART_LIST = page.getByText("Smart List", new Page.GetByTextOptions().setExact(true));
        this.CLICK_SMART_PIXEL = page.getByText("Smart Pixel", new Page.GetByTextOptions().setExact(true));
        this.CLICK_NPI_LIST = page.getByText("NPI List", new Page.GetByTextOptions().setExact(true));
        this.CLICK_PRESCRIPTION_BEHAVIOUR_CHANGE = page.getByText("Prescription Behavior Change", new Page.GetByTextOptions().setExact(true));
        this.CLICK_SPECIALTY = page.getByText("Specialty", new Page.GetByTextOptions().setExact(true));
        this.CLICK_PROFESSION = page.getByText("Profession", new Page.GetByTextOptions().setExact(true));
        this.CLICK_PRESCRIBEDDRUG = page.getByText("Prescribed Drug", new Page.GetByTextOptions().setExact(true));
        this.CLICK_DIAGNOSIS = page.getByText("Diagnosis", new Page.GetByTextOptions().setExact(true));
        this.CLICK_MEDICAL_PROCEDURE = page.getByText("Medical Procedure", new Page.GetByTextOptions().setExact(true));
        this.CLICK_ENDEMIC_RESEARCH = page.getByText("Endemic Research", new Page.GetByTextOptions().setExact(true));
        this.CLICK_ENDEMIC_DROPDOWN = page.getByText("Select MESH Conditions");
        this.CLICK_ENDEMIC_DROPDOWN_VALUE = page.locator(".pull-left > .iconSprite").first();
        this.CLICK_ENDEMIC_SAVE = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ok"));
        this.CLICK_SPECIALTY_DROPDOWN = page.getByText("Select Specialities");
        this.SELECT_SPECIALTY_VALUE = page.locator(".pull-left > .iconSprite").first();
        this.CLICK_PRESCRIPTION_DROPDOWN = page.locator("#prescriptionBehaviorWrapper").getByRole(AriaRole.COMBOBOX);
        this.CLICK_PRESCRIPTION_DROPDOWN_VALUE = page.getByText(Constants.PRESCRIPTION_DROPDOWN_VALUE);
        this.CLICK_PROFESSION_DROPDOWN = page.getByText("Select Profession");
        this.CLICK_EXPAND_PRACTICE = page.getByText("Expand based on Practice and Hospital affiliation", new Page.GetByTextOptions().setExact(true));
        this.CLICK_SMART_PIXEL_DROPDOWN = page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Select Smart Pixel$"))).nth(1);
        this.CLICK_ADD_VISITED_URL = page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("altText?altText:''")).nth(2);
        this.CLICK_ADD_IGNORED_URL = page.getByText("Add URL").nth(1);
        this.INSERT_IGNORED_URL = page.locator("#smartlistDetailsContainer").getByRole(AriaRole.TEXTBOX).nth(2);
        this.INSERT_VISITED_URL = page.locator("#smartlistDetailsContainer").getByRole(AriaRole.TEXTBOX).nth(2);
        this.CLICK_NPI_GROUP = page.getByText("Select NPI Groups");
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
        this.ADD_DRUG_BUTTON = page.locator("//span[contains(text(), 'Add Drug')]");
        this.OBSERVED_WITHIN_LAST_DAYS =page.locator("#mat-radio-3 > .mat-radio-label > .mat-radio-container > .mat-radio-outer-circle");
        this.DAYS_FIELD = page.locator("#recencyControls").getByRole(AriaRole.TEXTBOX);
        this.VISITED_MORE_THAN = page.locator("(//div[@class='mat-checkbox-inner-container'])[2]");
        this.PAGES_FIELD =  page.getByRole(AriaRole.TEXTBOX).nth(4);
        this.SEARCH_KEYWORD_CHECKBOX_ENGAGEDVIASEARCH=page.locator("(//div[@class='mat-checkbox-inner-container'])[2]");
        this.SEARCH_KEYWORD_CHECKBOX_ENGAGEDANYWHERE=page.locator("(//div[@class='mat-checkbox-inner-container'])[3]");
        this.PRESENT_BUTTON = page.locator("#mat-radio-8 > .mat-radio-label > .mat-radio-container > .mat-radio-outer-circle");
        this.ABSENT_BUTTON = page.locator("(//div[@class='mat-radio-outer-circle'])[4]");
        this.KEYWORD_TEXTFIELD =page.locator("textarea");
        this.NPI_CHECKBOX = page.locator("(//div[@class='mat-checkbox-inner-container'])[3]");
        this.PROFESSIONAL_POPUP = page.getByText("× Select Professions");
        this.PRESCRIBED_DRUG_POPUP = page.locator("//div[@role='alertdialog' and contains(., 'Invalid drug selection at row number 1')]");
        this.NPI_PRESCRIBED_DRUG = page.getByRole(AriaRole.LISTBOX).filter(new Locator.FilterOptions().setHasText(Pattern.compile("^$"))).locator("span").first();
        this.NPI_ADD_DRUG = page.getByText("Add Drug");
        this.NPI_FILL_DRUG =  page.locator("(//input[@role='combobox'])[8]");
        this.NPI_DRUG_SELECT =  page.locator("(//div[contains(@title,'40032-089')])[1]");
        this.RECENCY_FIELD =page.locator("#smartlistDetailsContainer").getByRole(AriaRole.TEXTBOX);
        this.RECENCY_POPUP =page.getByText("Prescribed Drug recency can not be less than 30 days");
        this.DIAGNOSIS_RECENCY_POPUP =page.getByText("Diagnosis recency can not be less than 30 days");
        this.DECILE_RANGE =page.locator("(//div[@class='text ng-star-inserted'][normalize-space()='1-10 decile'])[1]");
        this.MEDICAL_DECILE_RANGE =page.locator("(//div[@class='text ng-star-inserted'][normalize-space()='1-10 decile'])[2]");
        this.DISEASE_POPUP = page.getByText("Please select a diagnosis");
        this.DISEASE_VALUE =  page.locator("(//input[@role='combobox'])[9]");
        this.NPI_DISEASE = page.getByText("Select Diagnosis");
        this.DISEASE_SELECT =   page.getByText("A920: Chikungunya virus");
        this.DRUG_FIELD = page.locator("#prescriptionBehaviorWrapper").getByRole(AriaRole.COMBOBOX);
        this.DRUG_VALUE =  page.getByText("Parastat44911-0487, 44911-");
        ENGAGED_ONSITE_OPTIONS = page.getByText("Select Smart Pixel Engagement");
        ENGAGED_VIA_SEARCH_OPTIONS = page.getByText("Engaged via Search");
        ENGAGED_ANYWHERE_OPTIONS = page.getByText("Engaged Anywhere");
        SMART_NPI_OPTIONS =page.locator("#smartlistDetailsContainer");

    }

    public void clickSmartList() {
        CLICK_SMART_LIST.click();
        waitUtility.waitUntilSpinnerHidden();
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

    public void clickSmartPixelDropDownValue(String smartPixelDropdownValue) {
        page.getByText(smartPixelDropdownValue).click();
    }

    public void insertVisitedURL() {
        CLICK_ADD_VISITED_URL.click();
        INSERT_VISITED_URL.fill(Constants.VISITED_URL_1);
        CLICK_ADD_VISITED_URL.click();
        INSERT_VISITED_URL.fill(Constants.VISITED_URL_2);
    }


    public void insertIgnoredURL() {
        CLICK_ADD_IGNORED_URL.click();
        INSERT_IGNORED_URL.fill("www.google.com");
    }

    public static String verifyOptionNPISmartList() {
        return SMART_NPI_OPTIONS.innerText();
    }
    public static String verifyOptionEngagedOnsite() {
        return ENGAGED_ONSITE_OPTIONS.innerText();
    }
    public static String verifyOptionEngagedViaSearch() {
        return ENGAGED_VIA_SEARCH_OPTIONS.innerText();
    }
    public static String verifyOptionEngagedAnywhere() {
        return ENGAGED_ANYWHERE_OPTIONS.innerText();
    }
    public void clickNPIGroup() {
        CLICK_NPI_GROUP.click();
    }

    public void clickNPIGroupValue(String npiGroupValue) {
        page.getByText(npiGroupValue).click();
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
        CLICK_ENDEMIC_SAVE.click();
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

    public void selectProfessionValue(String professionValue) {
        page.getByText(professionValue).click();
    }

    public void clickExpandPractice() {
        CLICK_EXPAND_PRACTICE.click();
    }

    public void enterListName(String listName) {
        waitUtility.waitUntilSpinnerHidden();
        LIST_NAME.fill(listName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SELECT_ADVERTISER.locator("text=" + advertiser).click();
        waitUtility.waitUntilSpinnerHidden();
    }

    public void selectPrescribedDrug() {
        PRESCRIBED_DRUG.first().scrollIntoViewIfNeeded();
        PRESCRIBED_DRUG.first().check();
        ADD_DRUG_BUTTON.waitFor(new Locator.WaitForOptions().setTimeout(120000).setState(WaitForSelectorState.VISIBLE));
    }

    public void selectDrug(String drug) {
        ADD_DRUG_BUTTON.scrollIntoViewIfNeeded();
        ADD_DRUG_BUTTON.click();
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

    public void clickPulsepointIcon() {
        PULSEPOINT_ICON.click();
    }

    public void selectObservedWithinLastValues(String days) {
        OBSERVED_WITHIN_LAST_DAYS.click();
        DAYS_FIELD.fill(days);




    }

    public void selectSiteActivities(String pages) {
        VISITED_MORE_THAN.click();
        PAGES_FIELD.fill(pages);
    }

    public void addVisitedUrl(String visitedUrl) {

        CLICK_ADD_VISITED_URL.click();
        INSERT_VISITED_URL.fill(visitedUrl);
    }

    public void addIgnoredUrl(String ignoredUrl) {
        CLICK_ADD_IGNORED_URL.click();
        INSERT_IGNORED_URL.fill(ignoredUrl);
    }

    public void addSearchKeyword(String keywordValue) {


        KEYWORD_TEXTFIELD.fill(keywordValue);
    }


    public void clickSeachKeywordCheckboxEngagedViaSearch() {

        SEARCH_KEYWORD_CHECKBOX_ENGAGEDVIASEARCH.click();


    }

    public void clickPresentButton() {

        PRESENT_BUTTON.click();
    }
    public void clickAbsentButton() {

        ABSENT_BUTTON.click();
    }

    public void clickEngagedViaSearch() {

        ENGAGED_VIA_SEARCH_OPTIONS.click();

    }
    public void clickEngagedAnywhere() {

        ENGAGED_ANYWHERE_OPTIONS.click();

    }

    public void clickSeachKeywordCheckboxEngagedAnywhere() {

        SEARCH_KEYWORD_CHECKBOX_ENGAGEDANYWHERE.click();
    }

    public boolean verifyNPIOptionDisabled() {

        return NPI_CHECKBOX.isDisabled();
    }
    public boolean verifyNPIOptionEnabled() {

        return NPI_CHECKBOX.isEnabled();
    }


    public boolean verifyNPIErrorMessage() {

        return page.locator("text=Select HCP target list").isVisible();
    }

    public boolean verifySpecialtyErrorMessage() {

        return page.locator("text=Select Specialties").isVisible();


    }

    public boolean verifyProfessionErrorMessage() {
        return PROFESSIONAL_POPUP.isVisible();

    }

    public boolean verifyPrescribedDrugErrorMessage() {
        return PRESCRIBED_DRUG_POPUP.isVisible();

    }

    public void selectNPIPrescribedDrug(String drug) {

//        NPI_ADD_DRUG.click();
        NPI_PRESCRIBED_DRUG.click();
        NPI_FILL_DRUG.fill(drug);
        NPI_DRUG_SELECT.click();
    }

    public void enterRecency(String recency) {
        RECENCY_FIELD.fill(recency);
    }

    public void verifyRecencyValidation() {

        RECENCY_FIELD.fill("29");

    }

    public boolean verifyRecencyErrorMessage() {
        return RECENCY_POPUP.isVisible();

    }

    public void selectDecileRange(String decileRange) {

        DECILE_RANGE.click();
        String decile_value = String.format("//span[@class='ngx-slider-span ngx-slider-tick-legend ng-star-inserted'][normalize-space()='%s']", decileRange);
        page.locator(decile_value).click();
        page.keyboard().press("Escape");

    }
    public void selectMedicalDecileRange(String decileRange) {

        MEDICAL_DECILE_RANGE.click();
        String decile_value = String.format("//span[@class='ngx-slider-span ngx-slider-tick-legend ng-star-inserted'][normalize-space()='%s']", decileRange);
        page.locator(decile_value).click();
        page.keyboard().press("Escape");

    }

    public boolean verifyDiseaseErrorMessage() {

        return DISEASE_POPUP.isVisible();

    }
    public boolean verifyDiagnosisRecencyErrorMessage() {
        return DIAGNOSIS_RECENCY_POPUP.isVisible();

    }

    public void selectDisease(String diseaseValue) {

        NPI_DISEASE.click();
        DISEASE_VALUE.fill(diseaseValue);
        DISEASE_SELECT.click();
    }

    public boolean verifyProcedureErrorMessage() {
        return page.locator("text=Please select a treatment").isVisible();
    }

    public boolean verifyMedicalProcedureRecencyErrorMessage() {
        return page.locator("text=Medical Procedure recency can not be less than 30 days").isVisible();
    }

    public void selectMedicalProcedure(String procedureValue) {
        page.getByText("Select Medical Procedure").click();
        page.locator("#smartlistDetailsContainer").getByRole(AriaRole.COMBOBOX).fill(procedureValue);
        page.getByText(procedureValue).click();
    }

    public void clickExpandBasedOnPracticeAndHospitalAffiliation() {
        CLICK_EXPAND_PRACTICE.click();
    }

    public boolean verifyPracticeAndHospitalErrorMessage() {
        return page.locator("text=Select one or more List Population Options").isVisible();
    }

    public boolean verifyPrescriptionTypeErrorMessage() {
        return page.locator("Please select a drug for").isVisible();
    }

    public void selectPrescriptionType(String prescriptionType) {

        if (prescriptionType.equals("Droppers")) {

        } else if (prescriptionType.equals("New Prescribers")) {
            page.getByText("New to Brand").click();
        }

    }

    public void selectDrugs(String drugs) {
        DRUG_FIELD.click();
        DRUG_FIELD.fill(drugs);
        DRUG_VALUE.click();
    }

    public void selectTopPercent(String topPercent) {
        Locator slider = page.locator("ngx-slider")
                .filter(new Locator.FilterOptions().setHasText(topPercent))
                .getByLabel("ngx-slider");
        slider.click();
    }

    public void selectTimeFrame(String timeFrame) {
        String timeFrameValue = String.format("//span[@class='ngx-slider-span ngx-slider-tick-legend ng-star-inserted'][normalize-space()='%s']", timeFrame);
        page.locator(timeFrameValue).click();
    }
}
