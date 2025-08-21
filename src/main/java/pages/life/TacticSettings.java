package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class TacticSettings {
    private final Page page;
    private final Locator VERIFY_TACTIC_SETTINGS_PAGE;
    private final Locator SELECT_CHANNEL;
    private final Locator SEARCH_RULE_TYPE;
    private final Locator SELECT_RULE_TYPE;
    private final Locator SELECT_OPTION;
    private final Locator RULE_TYPE_OK_BUTTON;
    private final Locator RULE_TYPE_CLOSE;
    private final Locator SAVE_TACTIC_SETTINGS;
    private final Locator TACTIC_SETTINGS_SUCCESS;
    private final Locator SEARCH_RULE_OPTION;
    private final Locator RULE_POSTAL_CODES_TEXTBOX;
    private final Locator RULE_DEVICE_BLOCK;
    private final Locator RULE_LEGAL_POPULATIONS_HOUSEHOLD_TAB;
    private final Locator NPI_RULE;
    private final Locator NPI_PANEL_SEARCH;
    private final Locator TARGET_OPTION;
    private final Locator VERIFY_NPI;
    private final Locator FETCH_TARGET_RULETYPES;
    private final Locator FETCH_TARGET_RULEOPTIONS;
    private final Locator TARGET_CATEGORY_NAME;
    private final Locator SPINNER;
    private final Locator PERSON_TAB;
    private final Locator HOUSEHOLD_TAB;
    private final Locator HOUSEHOLD_IP_TAB;
    private final Locator HEALTH_POPULATIONS_TREATMENTS_OPTION;
    private final Locator RULE_INDIVIDUAL_KEYWORDS_OPTION;
    private final Locator KEYWORD_POPULATIONS_TEXTBOX;
    private final Locator GEO_TARGETS_BULK_UPLOAD;
    private final Locator GEO_TARGETS_UPLOAD_BUTTON;
    private final Locator GEO_TARGETS_TEXTBOX;
    private final Locator AUTHENTIC_BRAND_SUITABILITY_SEGMENT_ID;
    private final Locator RULE_APP_BUNDLES_LISTS_OPTION;
    private final Locator VIEWABILITY_PERCENTAGE_BOX;
    private final Locator KEYWORDS_TEXTBOX;
    private final Locator GEO_RADIUS_ADD_POINT;
    private final Locator GEO_RADIUS_LAT;
    private final Locator GEO_RADIUS_LONG;
    private final Locator GEO_RADIUS_DISTANCE;
    private final Locator GEO_RADIUS_POINT_NAME;
    private final Locator GEO_RADIUS_SAVE;

    List<Object> ruleTypes;
    List<Object> ruleOptions;

    public TacticSettings(Page page) {
        this.page = page;
        this.VERIFY_TACTIC_SETTINGS_PAGE = page.locator("//div[text()='Bid Strategy']");
        this.SELECT_CHANNEL = page.locator("(//div[@id='billingTypeDropdown'])[1]");
        this.SEARCH_RULE_TYPE = page.locator("//input[@name='search']");
        this.SELECT_RULE_TYPE = page.locator("(//a[@classname='target-tooltip'])[1]");
        this.SELECT_OPTION = page.locator("(//div[contains(@class,'include-default')])[1]");
        this.RULE_TYPE_OK_BUTTON = page.locator("//button[@class='ui primary button okButton' and normalize-space(text())='Ok']");
        this.RULE_TYPE_CLOSE = page.locator("//div[contains(@class,'close_icon')]");
        this.SAVE_TACTIC_SETTINGS = page.locator("//span[text()='Save']");
        this.TACTIC_SETTINGS_SUCCESS = page.locator("//div[@aria-label='Success!']");
        this.SEARCH_RULE_OPTION = page.locator("//input[contains(@placeholder,'Search') and contains(@class,'panel-search')]");
        this.RULE_POSTAL_CODES_TEXTBOX = page.locator("//div[@id='targetedItemsTA']");
        this.RULE_DEVICE_BLOCK = page.locator("//sui-radio-button[contains(@class,'ui radio checkbox')]//label[text()='Block Selected']");
        this.RULE_LEGAL_POPULATIONS_HOUSEHOLD_TAB = page.locator("//button[normalize-space(text())='Household']");
        this.NPI_RULE = page.locator("a").filter(new Locator.FilterOptions().setHasText("NPIHCP Direct Match"));
        this.NPI_PANEL_SEARCH = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search..."));
        this.TARGET_OPTION = page.getByTitle("Target");
        this.VERIFY_NPI = page.locator("//label[normalize-space(text())='NPI']");
        this.FETCH_TARGET_RULETYPES = page.locator("//label[contains(@class,'target-item__label')]");
        this.FETCH_TARGET_RULEOPTIONS = page.locator("//span[contains(@class,'target-ellipse')]");
        this.TARGET_CATEGORY_NAME = page.locator("//div[contains(@class,'targetCategoryName')]");
        this.SPINNER = page.locator("//div[contains(text(),'Loading...')]");
        this.PERSON_TAB = page.locator("//button[normalize-space(text())='Person']");
        this.HOUSEHOLD_TAB = page.locator("//button[normalize-space(text())='Household']");
        this.HOUSEHOLD_IP_TAB = page.locator("//button[normalize-space(text())='Household IP']");
        this.HEALTH_POPULATIONS_TREATMENTS_OPTION = page.locator("//div[contains(@class,'vertical-tab')]//a[contains(text(),'Treatments')]");
        this.RULE_INDIVIDUAL_KEYWORDS_OPTION = page.locator("//div[contains(@class,'vertical-tab')]//a[contains(text(),'Individual Keywords')]");
        this.KEYWORD_POPULATIONS_TEXTBOX = page.locator("//div[contains(@class,'keywordContainer')]//textarea");
        this.GEO_TARGETS_BULK_UPLOAD = page.locator("//span[text()='Bulk Upload']");
        this.GEO_TARGETS_UPLOAD_BUTTON = page.locator("//button[normalize-space()='Upload']");
        this.GEO_TARGETS_TEXTBOX = page.locator("//textarea[@id='geotargetedItemsTA']");
        this.AUTHENTIC_BRAND_SUITABILITY_SEGMENT_ID = page.locator("//div[@class='input']/input[@type='text']");
        this.RULE_APP_BUNDLES_LISTS_OPTION = page.locator("//div[contains(@class,'vertical-tab')]//a[contains(text(),'App Bundles Lists')]");
        this.VIEWABILITY_PERCENTAGE_BOX = page.locator("//div[contains(@class, 'rightLabel')]//input[contains(@class, 'form-control-percent-mini-right')]");
        this.KEYWORDS_TEXTBOX = page.locator("//div[contains(@class,'text-area-container')]//textarea");
        this.GEO_RADIUS_ADD_POINT = page.locator("//div[normalize-space()='Add Point' and contains(@class,'semi-bold')]");
        this.GEO_RADIUS_LAT = page.locator("//tr[contains(@class,'geopointRowInEdit')]//input[@formcontrolname='latitude']");
        this.GEO_RADIUS_LONG = page.locator("//tr[contains(@class,'geopointRowInEdit')]//input[@formcontrolname='longitude']");
        this.GEO_RADIUS_DISTANCE = page.locator("//tr[contains(@class,'geopointRowInEdit')]//input[@formcontrolname='distance']");
        this.GEO_RADIUS_POINT_NAME = page.locator("//tr[contains(@class,'geopointRowInEdit')]//input[@formcontrolname='name']");
        this.GEO_RADIUS_SAVE = page.locator("(//div[@title='Save' and contains(@class,'saveGeoPtButton')])[1]");
    }

    public String verifyTacticSettingsText() {
        return VERIFY_TACTIC_SETTINGS_PAGE.innerText();
    }

    public void selectChannel(String channel) {
        SELECT_CHANNEL.click();
        SELECT_CHANNEL.locator("text=" + channel).first().click();
    }

    public void selectRuleType(String ruleType) {
        SEARCH_RULE_TYPE.fill(ruleType);
        SEARCH_RULE_TYPE.press("Enter");
        SELECT_RULE_TYPE.click();
        SELECT_OPTION.click();
        RULE_TYPE_OK_BUTTON.click();
        RULE_TYPE_CLOSE.click();
    }

    public void saveTacticSettings() {
        SAVE_TACTIC_SETTINGS.click();
    }

    public String tacticSettingsSuccess() {
        return TACTIC_SETTINGS_SUCCESS.innerText();
    }

    public void selectMultipleRuleTypes(String ruleType, List<String> ruleValues) {
        SEARCH_RULE_TYPE.clear();
        SEARCH_RULE_TYPE.type(ruleType);
        if(SELECT_RULE_TYPE.isVisible()){
            SELECT_RULE_TYPE.click();
            SPINNER.waitFor(new Locator.WaitForOptions().setTimeout(120000).setState(WaitForSelectorState.HIDDEN));

            switch (ruleType) {
                case "Behavioral Segment":
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("(//span[contains(text(), '%s')]/ancestor::div[contains(@class, 'segmentname')]/preceding-sibling::div[contains(@class, 'iconsWrapper')]//div[contains(@class, 'include-default')])[1]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "NPI":
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("(//mark[contains(text(), '%s')]/ancestor::div[contains(@class, 'npilist-itemWrapper')]//div[contains(@class, 'include-default')])[1]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "HCP by Specialty":
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("(//mark[text()='%s']/ancestor::div[contains(@class,'treeviewNode')]//div[contains(@class,'include-default')])[1]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Health Populations":
                    HOUSEHOLD_IP_TAB.click();
                    HEALTH_POPULATIONS_TREATMENTS_OPTION.click();
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("(//mark[text()='%s']/ancestor::div[contains(@class,'treeviewNode')]//div[contains(@class,'include-default')])[1]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Keyword Populations":
                    PERSON_TAB.click();
                    RULE_INDIVIDUAL_KEYWORDS_OPTION.click();
                    for (String val : ruleValues) {
                        KEYWORD_POPULATIONS_TEXTBOX.type(val.trim());
                        KEYWORD_POPULATIONS_TEXTBOX.press("Enter");
                        page.waitForLoadState(LoadState.LOAD);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Practice Staff":
                    HOUSEHOLD_TAB.click();
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("(//span[contains(text(), '%s')]/ancestor::div[contains(@class, 'itemWrapper')]//div[contains(@class, 'include-default')])[1]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "IP Address":
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("(//div[contains(text(), '%s')]/ancestor::div[contains(@class, 'left cliptext')]/preceding-sibling::div[contains(@class, 'left iconsWrapper')]//div[contains(@class, 'include-default')])[1]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "In Condition":
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("//mark[contains(text(), '%s')]/ancestor::div[contains(@class, 'left name-icon')]/preceding-sibling::div[contains(@class,'left targetBlockIcons')]/div[@title='Target']", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Age", "Browser":
                    for (String val : ruleValues) {
                        String xpath = String.format("//label[contains(text(),'%s')]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Health Pages":
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.first().fill(val);
                        String xpath = String.format("//span[contains(text(),'%s')]/ancestor::div[contains(@class,'left name-icon')]/preceding-sibling::div[contains(@class,'left targetBlockIcons')]/div[@title='Target']", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Keywords":
                    RULE_INDIVIDUAL_KEYWORDS_OPTION.click();
                    for (String val : ruleValues) {
                        KEYWORDS_TEXTBOX.type(val.trim());
                        KEYWORDS_TEXTBOX.press("Enter");
                        page.waitForLoadState(LoadState.LOAD);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Endemics":
                    clickRuleTypeOkButton();
                    break;
                case "Geo Targets":
                    GEO_TARGETS_BULK_UPLOAD.click();
                    GEO_TARGETS_TEXTBOX.click();
                    for (String val : ruleValues) {
                        GEO_TARGETS_TEXTBOX.type(val.trim());
                        GEO_TARGETS_TEXTBOX.press("Enter");
                        page.waitForLoadState(LoadState.LOAD);
                    }
                    GEO_TARGETS_UPLOAD_BUTTON.click();
                    clickRuleTypeOkButton();
                    break;
                case "Geo Radius":
                    String pointName = "GeoPointName";
                    String latitude = ruleValues.get(0).trim();
                    String longitude = ruleValues.get(1).trim();
                    String distance = ruleValues.get(2).trim();
                    GEO_RADIUS_ADD_POINT.click();
                    GEO_RADIUS_LAT.fill(latitude);
                    GEO_RADIUS_LONG.fill(longitude);
                    GEO_RADIUS_DISTANCE.fill(distance);
                    GEO_RADIUS_POINT_NAME.fill(pointName);
                    GEO_RADIUS_SAVE.click();
                    clickRuleTypeOkButton();
                    break;
                case "Postal Codes":
                    RULE_POSTAL_CODES_TEXTBOX.click();
                    for (String val : ruleValues) {
                        RULE_POSTAL_CODES_TEXTBOX.type(val.trim());
                        RULE_POSTAL_CODES_TEXTBOX.press("Enter");
                        page.waitForLoadState(LoadState.LOAD);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Weather Signals":
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("(//mark[contains(text(),'%s')]/ancestor::div[contains(@class,'treeviewNode')]//div[contains(@class,'include-default')])[1]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Authentic Brand Suitability":
                    String segmentID = ruleValues.get(0);
                    AUTHENTIC_BRAND_SUITABILITY_SEGMENT_ID.fill(segmentID);
                    page.locator("body").click();
                    clickRuleTypeOkButton();
                    break;
                case "Brand Safety & Suitability":
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("//mark[contains(text(),'%s')]/ancestor::div[@class='left name-icon ng-star-inserted']/preceding-sibling::div[contains(@class,'custom_checkbox')]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Device", "Operating System":
                    RULE_DEVICE_BLOCK.click();
                    for (String val : ruleValues) {
                        String xpath = String.format("//label[contains(text(),'%s')]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Domains/Apps":
                    RULE_APP_BUNDLES_LISTS_OPTION.click();
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("//span[text()='%s']/ancestor::div[@class='cliptext']/preceding-sibling::div[@class='target_icon h-20']//div[contains(@class,'include-default')]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Inventory Source":
                    for (String val : ruleValues) {
                        String xpath = String.format("//span[text()='%s']/ancestor::div[contains(@class,'name-icon')]/preceding-sibling::div[contains(@class,'targetBlockIcons')]//div[contains(@class,'include-default')]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Viewability":
                    String percent = ruleValues.get(0);
                    VIEWABILITY_PERCENTAGE_BOX.fill(percent);
                    clickRuleTypeOkButton();
                    break;
                case "Legal Pages":
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("//mark[text()='%s']/ancestor::div[contains(@class,'treeviewNode')]//button[contains(@class,'include-default')]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
                case "Legal Populations":
                    RULE_LEGAL_POPULATIONS_HOUSEHOLD_TAB.click();
                    for (String val : ruleValues) {
                        SEARCH_RULE_OPTION.fill(val);
                        String xpath = String.format("//span/mark[contains(text(), '%s')]/ancestor::div[contains(@class, 'left name-icon')]/preceding-sibling::div[contains(@class, 'left targetBlockIcons')]//button[contains(@class, 'include-default')]", val);
                        isElementVisible(xpath);
                    }
                    clickRuleTypeOkButton();
                    break;
            }
        }
    }

    public void isElementVisible(String xpath){
        Locator locator = page.locator(xpath);
        boolean visible = false;
        for (int i = 0; i < 5; i++) {
            if (locator.isVisible()) {
                page.waitForTimeout(1000);
                visible = true;
                break;
            }
            page.waitForTimeout(1000);
        }
        if (visible) {
            locator.scrollIntoViewIfNeeded();
            locator.click(new Locator.ClickOptions().setForce(true));
            page.waitForLoadState(LoadState.LOAD);
        }
    }

    public void clickRuleTypeOkButton(){
        if(RULE_TYPE_OK_BUTTON.isEnabled())
            RULE_TYPE_OK_BUTTON.click();
        page.waitForLoadState(LoadState.LOAD);
    }

    public void closeRuleTypePanel() {
        RULE_TYPE_CLOSE.click();
    }

    public List<Object> fetchRulesTypes(){
        ruleTypes = new ArrayList<>();
        FETCH_TARGET_RULETYPES.first().waitFor();
        for (int i = 0; i < FETCH_TARGET_RULETYPES.count(); i++) {
            String text = FETCH_TARGET_RULETYPES.nth(i).innerText().replaceAll("\\s*\\(\\d+\\)", "").trim();
            ruleTypes.add(text);
        }
        return ruleTypes;
    }

    public void fetchRulesTypesCount(int expectedCount) {
        FETCH_TARGET_RULETYPES.nth(expectedCount - 1).waitFor();
    }

    public List<Object> fetchRuleOptions(){
        ruleOptions = new ArrayList<>();
        for (int i = 0; i < FETCH_TARGET_RULEOPTIONS.count(); i++) {
            String text = FETCH_TARGET_RULEOPTIONS.nth(i).innerText();
            text = text.replaceAll("≥", "").trim();
            ruleOptions.add(text);
        }
        return ruleOptions;
    }

    public void selectNPIRule(String listname) {
        NPI_RULE.click();
        NPI_PANEL_SEARCH.click();
        NPI_PANEL_SEARCH.fill(listname);
        NPI_PANEL_SEARCH.press("Enter");
    }

    public void clickTarget() {
        TARGET_OPTION.click();
    }

    public void clickOk() {
        RULE_TYPE_OK_BUTTON.click();
    }

    public void clickClose() {
        RULE_TYPE_CLOSE.click();
    }

    public String verifyNPIRule() {
        return VERIFY_NPI.innerText();
    }

    /*Roshani Sherkar
    * 01-07-2025*/
    public boolean fetchAndVerifyTargetCategoryName(List<String> targetCategoryList){
        List<String> actualCategories = new ArrayList<>();
        int count = TARGET_CATEGORY_NAME.count();
        for (int i = 0; i < count; i++) {
            String text = TARGET_CATEGORY_NAME.nth(i).innerText().trim();
            actualCategories.add(text);
        }
        return new HashSet<>(targetCategoryList).containsAll(actualCategories);
    }

    public List<String> getTargetTypesForCategory(String key) {
        TARGET_CATEGORY_NAME.first().waitFor();
        int categoryCount = TARGET_CATEGORY_NAME.count();
        for (int i = 0; i < categoryCount; i++) {
            String categoryText = TARGET_CATEGORY_NAME.nth(i).innerText().trim();

            if (categoryText.contains(key)) {
                String xpathString = String.format("//div[contains(@class,'targetCategoryName') and contains(text(),'%s')]/following-sibling::div//span[1]", key);
                Locator categoryItems = page.locator(xpathString);
                List<String> actualValues = new ArrayList<>();
                for (int j = 0; j < categoryItems.count(); j++) {
                    categoryItems.nth(j).scrollIntoViewIfNeeded();
                    actualValues.add(categoryItems.nth(j).innerText().trim());
                }
                return actualValues;
            }
        }
        return Collections.emptyList();
    }
}