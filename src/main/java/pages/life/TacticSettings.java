package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;

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
    private final Locator RULE_IN_CONDITION_SELECT;
    private final Locator RULE_AGE_OPTION1_SELECT;
    private final Locator RULE_AGE_OPTION2_SELECT;
    private final Locator RULE_HEALTH_PAGES_OPTION_SELECT;
    private final Locator RULE_HEALTH_PAGES_TREATMENTS_TAB;
    private final Locator RULE_HEALTH_PAGES_OTC_TAB;
    private final Locator RULE_POSTAL_CODES_TEXTBOX;
    private final Locator RULE_DEVICE_BLOCK;
    private final Locator RULE_DEVICE_OPTION1_SELECT;
    private final Locator RULE_DEVICE_OPTION2_SELECT;
    private final Locator RULE_LEGAL_POPULATIONS_HOUSEHOLD_TAB;
    private final Locator RULE_LEGAL_POPULATIONS_OPTION_SELECT;
    private final Locator VERIFY_AUDIENCE_ATTRIBUTE;
    private final Locator VERIFY_HEALTH_JOURNEY;
    private final Locator VERIFY_DEMOGRAPHICS;
    private final Locator VERIFY_CONTEXTUAL;
    private final Locator VERIFY_GEOGRAPHY;
    private final Locator VERIFY_MEDIA_SUPPLY;
    private final Locator VERIFY_LEGAL_TARGETINGS;
    private final Locator BEHAVIORAL_SEGMENT_OPTION1;
    private final Locator IN_CONDITION_OPTION1;
    private final Locator AGE_OPTION1;
    private final Locator AGE_OPTION2;
    private final Locator HEALTH_PAGES_OPTION1;
    private final Locator HEALTH_PAGES_OPTION2;
    private final Locator HEALTH_PAGES_OPTION3;
    private final Locator POSTAL_CODES_OPTION1;
    private final Locator POSTAL_CODES_OPTION2;
    private final Locator POSTAL_CODES_OPTION3;
    private final Locator DEVICES_OPTION1;
    private final Locator DEVICES_OPTION2;
    private final Locator LEGAL_POPULATIONS_OPTION1;
    private final Locator TEXT_AUDIENCE_ATTRIBUTE;
    private final Locator NPI_RULE;
    private final Locator NPI_PANEL_SEARCH;
    private final Locator TARGET_OPTION;
    private final Locator VERIFY_NPI;


    String optionTextAudienceAttribute1;
    String optionTextDemographics1;
    String optionTextDemographics2;
    String optionTextMediaSupply1;
    String optionTextMediaSupply2;
    String inCondition1 = "Digestive System Diseases";
    String healthPages1 = "Animal Diseases";
    String healthPages2 = "Equipment and Supplies";
    String healthPages3 = "Cough, Cold, and Allergy";
    String postalCodes1 = "123456";
    String postalCodes2 = "10001";
    String postalCodes3 = "987654";
    String healthPopulations1 = "Adoption";

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
        this.TACTIC_SETTINGS_SUCCESS = page.locator("//*[text()='Success!']");
        this.SEARCH_RULE_OPTION = page.locator("//input[contains(@placeholder,'Search') and contains(@class,'panel-search')]");
        this.RULE_IN_CONDITION_SELECT = page.locator("(//button[@title='Target'])[1]");
        this.RULE_AGE_OPTION1_SELECT = page.locator("(//sui-checkbox[contains(@class,'toggle ui checkbox')])[4]");
        this.RULE_AGE_OPTION2_SELECT = page.locator("(//sui-checkbox[contains(@class,'toggle ui checkbox')])[7]");
        this.RULE_HEALTH_PAGES_OPTION_SELECT = page.locator("//div[@title='Target']");
        this.RULE_HEALTH_PAGES_TREATMENTS_TAB = page.locator("//a[contains(text(),'Treatments')]");
        this.RULE_HEALTH_PAGES_OTC_TAB = page.locator("//a[contains(text(),'OTC')]");
        this.RULE_POSTAL_CODES_TEXTBOX = page.locator("//div[@id='targetedItemsTA']");
        this.RULE_DEVICE_BLOCK = page.locator("//sui-radio-button[contains(@class,'ui radio checkbox')]//label[text()='Block Selected']");
        this.RULE_DEVICE_OPTION1_SELECT = page.locator("(//sui-checkbox[contains(@class,'toggle ui checkbox')])[6]");
        this.RULE_DEVICE_OPTION2_SELECT = page.locator("(//sui-checkbox[contains(@class,'toggle ui checkbox')])[7]");
        this.RULE_LEGAL_POPULATIONS_HOUSEHOLD_TAB = page.locator("//button[normalize-space(text())='Household']");
        this.RULE_LEGAL_POPULATIONS_OPTION_SELECT = page.locator("(//button[@title='Target'])[2]");
        this.VERIFY_AUDIENCE_ATTRIBUTE = page.locator("//label[normalize-space(text())='Behavioral Segment']");
        this.VERIFY_HEALTH_JOURNEY = page.locator("//label[normalize-space(text())='In Condition']");
        this.VERIFY_DEMOGRAPHICS = page.locator("//label[normalize-space(text())='Age']");
        this.VERIFY_CONTEXTUAL = page.locator("//label[normalize-space(text())='Health Pages']");
        this.VERIFY_GEOGRAPHY = page.locator("//label[normalize-space(text())='Postal Codes']");
        this.VERIFY_MEDIA_SUPPLY = page.locator("//label[normalize-space(text())='Device']");
        this.VERIFY_LEGAL_TARGETINGS = page.locator("//label[normalize-space(text())='Legal Populations']");
        this.BEHAVIORAL_SEGMENT_OPTION1 = page.locator("(//span[@class='target-ellipse'])[1]");
        this.IN_CONDITION_OPTION1 = page.locator("(//span[@class='target-ellipse'])[2]");
        this.AGE_OPTION1 = page.locator("(//span[@class='target-ellipse'])[3]");
        this.AGE_OPTION2 = page.locator("(//span[@class='target-ellipse'])[4]");
        this.HEALTH_PAGES_OPTION1 = page.locator("(//span[@class='target-ellipse'])[5]");
        this.HEALTH_PAGES_OPTION2 = page.locator("(//span[@class='target-ellipse'])[6]");
        this.HEALTH_PAGES_OPTION3 = page.locator("(//span[@class='target-ellipse'])[7]");
        this.POSTAL_CODES_OPTION1 = page.locator("(//span[@class='target-ellipse'])[8]");
        this.POSTAL_CODES_OPTION2 = page.locator("(//span[@class='target-ellipse'])[9]");
        this.POSTAL_CODES_OPTION3 = page.locator("(//span[@class='target-ellipse'])[10]");
        this.DEVICES_OPTION1 = page.locator("(//span[@class='target-ellipse'])[11]");
        this.DEVICES_OPTION2 = page.locator("(//span[@class='target-ellipse'])[12]");
        this.LEGAL_POPULATIONS_OPTION1 = page.locator("(//span[@class='target-ellipse'])[13]");
        this.TEXT_AUDIENCE_ATTRIBUTE = page.locator("(//span[contains(@class,'d-block fs-13 lh-20')])[1]");
        this.NPI_RULE = page.locator("a").filter(new Locator.FilterOptions().setHasText("NPIHCP Direct Match"));
        this.NPI_PANEL_SEARCH =page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search..."));
        this.TARGET_OPTION = page.getByTitle("Target");
        this.VERIFY_NPI = page.locator("//label[normalize-space(text())='NPI']");


    }

    public String verifyTacticSettingsText() {
        return VERIFY_TACTIC_SETTINGS_PAGE.innerText();
    }

    public void selectChannel(String channel) {
        SELECT_CHANNEL.click();
        SELECT_CHANNEL.locator("text=" + channel).click();
    }


    public void selectRuleType() {
        SEARCH_RULE_TYPE.fill("Behavioral Segment");
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

    public void selectMultipleRuleTypes(String ruleType) {
        SEARCH_RULE_TYPE.fill(ruleType);
        SEARCH_RULE_TYPE.press("Enter");
        SELECT_RULE_TYPE.click();

        switch (ruleType) {
            case "Behavioral Segment":
                optionTextAudienceAttribute1 = TEXT_AUDIENCE_ATTRIBUTE.innerText();
                SELECT_OPTION.click();
                RULE_TYPE_OK_BUTTON.click();
                break;
            case "In Condition":
                SEARCH_RULE_OPTION.fill(inCondition1);
                RULE_IN_CONDITION_SELECT.click();
                RULE_TYPE_OK_BUTTON.click();
                break;
            case "Age":
                optionTextDemographics1 = RULE_AGE_OPTION1_SELECT.innerText();
                optionTextDemographics2 = RULE_AGE_OPTION2_SELECT.innerText();
                RULE_AGE_OPTION1_SELECT.click();
                RULE_AGE_OPTION2_SELECT.click();
                RULE_TYPE_OK_BUTTON.click();
                break;
            case "Health Pages":
                SEARCH_RULE_OPTION.fill(healthPages1);
                RULE_HEALTH_PAGES_OPTION_SELECT.click();
                RULE_HEALTH_PAGES_TREATMENTS_TAB.click();
                SEARCH_RULE_OPTION.fill(healthPages2);
                RULE_HEALTH_PAGES_OPTION_SELECT.click();
                RULE_HEALTH_PAGES_OTC_TAB.click();
                SEARCH_RULE_OPTION.fill(healthPages3);
                RULE_HEALTH_PAGES_OPTION_SELECT.click();
                RULE_TYPE_OK_BUTTON.click();
                break;
            case "Postal Codes":
                RULE_POSTAL_CODES_TEXTBOX.fill(postalCodes1+"\n"+postalCodes2+"\n"+postalCodes3);
                RULE_TYPE_OK_BUTTON.click();
                break;
            case "Device":
                RULE_DEVICE_BLOCK.click();
                optionTextMediaSupply1 = RULE_DEVICE_OPTION1_SELECT.innerText();
                optionTextMediaSupply2 = RULE_DEVICE_OPTION2_SELECT.innerText();
                RULE_DEVICE_OPTION1_SELECT.click();
                RULE_DEVICE_OPTION2_SELECT.click();
                RULE_TYPE_OK_BUTTON.click();
                break;
            case "Legal Populations":
                RULE_LEGAL_POPULATIONS_HOUSEHOLD_TAB.click();
                SEARCH_RULE_OPTION.fill(healthPopulations1);
                RULE_LEGAL_POPULATIONS_OPTION_SELECT.click();
                RULE_TYPE_OK_BUTTON.click();
                break;



        }
    }

    public void closeRuleTypePanel() {
        RULE_TYPE_CLOSE.click();
    }

    public String verifyAudienceAttributeRule() {
        return VERIFY_AUDIENCE_ATTRIBUTE.innerText();
    }

    public String verifyHealthJourneyRule() {
        return VERIFY_HEALTH_JOURNEY.innerText();
    }

    public String verifyDemographicsRule() {
        return VERIFY_DEMOGRAPHICS.innerText();
    }

    public String verifyContextualRule() {
        return VERIFY_CONTEXTUAL.innerText();
    }

    public String verifyGeographyRule() {
        return VERIFY_GEOGRAPHY.innerText();
    }

    public String verifyMediaSupplyRule() {
        return VERIFY_MEDIA_SUPPLY.innerText();
    }

    public String verifyLegalTargetingsRule() {
        return VERIFY_LEGAL_TARGETINGS.innerText();
    }

    public void verifyAudienceAttributeOption() {
        String displayedTextAudienceAttribute1 = BEHAVIORAL_SEGMENT_OPTION1.innerText();
        assert optionTextAudienceAttribute1.equals(displayedTextAudienceAttribute1);
    }

    public void verifyHealthJourneyOption() {
        String displayedTextHealthJourney1 = IN_CONDITION_OPTION1.innerText();
        assert inCondition1.equals(displayedTextHealthJourney1);
    }

    public void verifyDemographicsOption() {
        String displayedTextDemographics1 = AGE_OPTION1.innerText();
        String displayedTextDemographics2 = AGE_OPTION2.innerText();
        assert optionTextDemographics1.equals(displayedTextDemographics1);
        assert optionTextDemographics2.equals(displayedTextDemographics2);
    }

    public void verifyContextualOption() {
        String displayedTextContextual1 = HEALTH_PAGES_OPTION1.innerText();
        String displayedTextContextual2 = HEALTH_PAGES_OPTION2.innerText();
        String displayedTextContextual3 = HEALTH_PAGES_OPTION3.innerText();
        assert healthPages1.equals(displayedTextContextual1);
        assert healthPages2.equals(displayedTextContextual2);
        assert healthPages3.equals(displayedTextContextual3);
    }

    public void verifyGeographyOption() {
        String displayedTextGeography1 = POSTAL_CODES_OPTION1.innerText();
        String displayedTextGeography2 = POSTAL_CODES_OPTION2.innerText();
        String displayedTextGeography3 = POSTAL_CODES_OPTION3.innerText();
        assert postalCodes1.equals(displayedTextGeography1);
        assert postalCodes2.equals(displayedTextGeography2);
        assert postalCodes3.equals(displayedTextGeography3);
    }

    public void verifyMediaSupplyOption() {
        String displayedTextMediaSupply1 = DEVICES_OPTION1.innerText();
        String displayedTextMediaSupply2 = DEVICES_OPTION2.innerText();
        assert optionTextMediaSupply1.equals(displayedTextMediaSupply1);
        assert optionTextMediaSupply2.equals(displayedTextMediaSupply2);
    }

    public void verifyLegalTargetingsOption() {
        String displayedTextLegalTargetings1 = LEGAL_POPULATIONS_OPTION1.innerText();
        assert healthPopulations1.equals(displayedTextLegalTargetings1);
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




}