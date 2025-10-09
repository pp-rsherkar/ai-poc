package pages.life;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import factory.DriverFactory;
import utils.CommonUtils;
import utils.WaitUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CreateCreatives {
    private final Page page;
    private final Locator CREATIVE_PAGE_TITLE;
    private final Locator UNARCHIVED_BUTTON;
    private final Locator ARCHIVED_BUTTON;
    private final Locator SELECT_ADVERTISER;
    private final Locator DROPDOWN_VALUES;
    private final Locator CLEAR_ALL_BUTTON;
    private final Locator SELECT_CAMPAIGN;
    private final Locator SELECT_AD_SIZE;
    private final Locator SELECT_CREATIVE_TYPE;
    private final Locator APPROVAL_STATUS;
    private final Locator CREATIVE_TYPE_ICON;
    private final Locator CREATED_BY;
    private final Locator SORT_DROPDOWN;
    private final Locator SORT_BY_UP;
    private final Locator SORT_BY_DOWN;
    private final Locator SEARCH_BOX;
    private final Locator DATA_CONTENT_PANEL;
    private final Locator COPY_ICON;
    private final Locator CREATIVE_HEADER;
    private final Locator OK_BUTTON;
    private final Locator SUCCESS_ALERT;
    private final Locator ARCHIVE_DIALOG;
    private final Locator ARCHIVE_BUTTON;
    private final Locator NEW_CREATIVE_BUTTON;
    private final Locator ADVERTISER_DROPDOWN;
    private final Locator ADVERTISER_DROPDOWN_VALUES;
    private final Locator CREATIVE_NAME;
    private final Locator ADVERTISER_DSA;
    private final Locator FINANCER;
    private final Locator CREATIVE_TYPE;
    private final Locator CREATIVE_HTML_CODE;
    private final Locator MACROS_CHECKBOX;
    private final Locator CREATIVE_AD_SIZE;
    private final Locator CREATIVE_AD_SIZE_VALUE;
    private final Locator DOMAIN_LANDING;
    private final Locator CLICK_THROUGH_URL;
    private final Locator DURATION;
    private final Locator URL;
    private final Locator VAST_XML_TEXTAREA;
    private final Locator IAB_CATEGORY;
    private final Locator IAB_CATEGORY_VALUE;
    private final Locator WIDTH;
    private final Locator HEIGHT;
    private final Locator HEADLINE;
    private final Locator DESCRIPTION;
    private final Locator DISPLAY_URL;
    private final Locator SPONSORED_BY;
    private final Locator PRODUCT_DESCRIPTION;
    private final Locator CREATIVE_NAME_TEXT;
    WaitUtility waitUtility = new WaitUtility(DriverFactory.getPage());
    List<String> creativesList = new ArrayList<>();
    String imageTextLocator = "//span[contains(text(),'%s')]";

    public CreateCreatives(Page page) {
        this.page = page;
        this.CREATIVE_PAGE_TITLE = page.locator("//div[contains(text(),'Creatives')]");
        this.UNARCHIVED_BUTTON = page.locator("//img[@title='unarchive']");
        this.ARCHIVED_BUTTON = page.locator("//img[@title='archive']");
        this.SELECT_ADVERTISER = page.locator("//app-multi-select[contains(@placeholder,'Select Advertisers')]/div/span/input");
        this.DROPDOWN_VALUES = page.locator("//div[@class='menu transition visible']//div[@class='item']/span");
        this.CLEAR_ALL_BUTTON = page.locator("//div[contains(text(),'Clear All')]");
        this.SELECT_CAMPAIGN = page.locator("//app-multi-select[contains(@placeholder,'Select Campaigns')]/div/span/input");
        this.SELECT_AD_SIZE = page.locator("//app-multi-select[contains(@placeholder,'Select Ad Sizes')]/div/span/input");
        this.SELECT_CREATIVE_TYPE = page.locator("//app-multi-select[contains(@placeholder,'Select Type')]/div/span/input");
        this.APPROVAL_STATUS = page.locator("//app-multi-select[contains(@placeholder,'Select Status')]/div/span/input");
        this.CREATED_BY = page.locator("//app-multi-select[contains(@placeholder,'Select Created By')]/div/span/input");
        this.CREATIVE_TYPE_ICON = page.locator("//div[contains(@class,'video') or contains(@class,'native') or contains(@class,'image') or contains(@class,'expandablerichmedia') or contains(@class,'search') or contains(@class,'generichtml') or contains(@class,'audio')]");
        this.SORT_DROPDOWN = page.locator("//div[contains(@class, 'sort-option-dropdown')]");
        this.SORT_BY_UP = page.locator("//span[contains(text(),'Sort by')]/following-sibling::i[contains(@class,'up')]");
        this.SORT_BY_DOWN = page.locator("//span[contains(text(),'Sort by')]/following-sibling::i[contains(@class,'down')]");
        this.SEARCH_BOX = page.locator("//div[contains(@class,'search-field')]/input[@placeholder='Search']");
        this.DATA_CONTENT_PANEL = page.locator("//div[contains(@class,'content-section left')]");
        this.COPY_ICON = page.locator("//div[contains(@class,'approved')]/following-sibling::div/span[contains(@title,'copy')]");
        this.CREATIVE_HEADER = page.locator("//div[contains(@class,'rightPanelHeader1')]");
        this.OK_BUTTON = page.locator("//button[contains(@class,'okButton')]");
        this.SUCCESS_ALERT = page.locator("//div[contains(@aria-label,'Success!')]");
        this.ARCHIVE_DIALOG = page.locator("//div[contains(text(),'Archive Creative?')]");
        this.ARCHIVE_BUTTON = page.locator("//div[contains(@class,'approveButtonText')]");
        this.NEW_CREATIVE_BUTTON = page.locator("//button[contains(text(),'New Creative')]");
        this.ADVERTISER_DROPDOWN = page.locator("//app-single-select-dropdown[contains(@placeholder,'Select Advertiser')]/div/div/input");
        this.ADVERTISER_DROPDOWN_VALUES = page.locator("//app-single-select-dropdown[contains(@placeholder,'Select Advertiser')]//input/following-sibling::div/div");
        this.CREATIVE_NAME = page.locator("//input[contains(@formcontrolname,'creativeName')]");
        this.ADVERTISER_DSA = page.locator("//input[contains(@placeholder,'Advertiser per DSA')]");
        this.FINANCER = page.locator("//input[contains(@placeholder,'Financer')]");
        this.CREATIVE_TYPE = page.locator("//app-creativetype/div//following-sibling::div");
        this.CREATIVE_HTML_CODE = page.locator("//textarea[contains(@formcontrolname,'htmlCode')]");
        this.MACROS_CHECKBOX = page.locator("//sui-checkbox[contains(@class,'multi_click_macros_check ')]");
        this.CREATIVE_AD_SIZE = page.locator("//app-single-select-dropdown//input[contains(@placeholder,'Select Ad Size')]");
        this.CREATIVE_AD_SIZE_VALUE = page.locator("//input[contains(@placeholder,'Select Ad Size')]/following-sibling::div/div");
        this.DOMAIN_LANDING = page.locator("//input[contains(@formcontrolname,'landingDomain')]");
        this.CLICK_THROUGH_URL = page.locator("//input[contains(@formcontrolname,'clickThruUrl')]");
        this.DURATION = page.locator("//input[contains(@formcontrolname,'audioDuration') or contains(@formcontrolname,'duration')]");
        this.URL = page.locator("//input[contains(@placeholder,'url')]");
        this.VAST_XML_TEXTAREA = page.locator("//textarea[contains(@formcontrolname,'vastDoc') or contains(@formcontrolname,'vastAudioDoc')]");
        this.IAB_CATEGORY = page.locator("//div[contains(@id,'iabcreativeLookup')]/div/input");
        this.IAB_CATEGORY_VALUE = page.locator("//div[contains(@id,'iabcreativeLookup')]/div/div/div");
        this.WIDTH = page.locator("//input[contains(@formcontrolname,'width')]");
        this.HEIGHT = page.locator("//input[contains(@formcontrolname,'height')]");
        this.HEADLINE = page.locator("//input[contains(@formcontrolname,'headline') or contains(@formcontrolname,'tittle')]");
        this.DESCRIPTION = page.locator("//input[contains(@formcontrolname,'description')]");
        this.DISPLAY_URL = page.locator("//input[contains(@formcontrolname,'srchDisplayUrl') or contains(@formcontrolname,'displayUrl')]");
        this.SPONSORED_BY = page.locator("//input[contains(@formcontrolname,'sponsoredText')]");
        this.PRODUCT_DESCRIPTION = page.locator("//textarea[contains(@formcontrolname,'productDescription')]");
        this.CREATIVE_NAME_TEXT = page.locator("//div[contains(@role,'alert')]");
    }

    public String verifyCreativeLibraryPageTitle() {
        waitUtility.waitUntilPreLoaderHidden();
        return CREATIVE_PAGE_TITLE.innerText().trim();
    }

    public void clickActivityButton(String buttonType) {
        Locator button = page.locator(String.format("//div[contains(text(), '%s')]/parent::button", buttonType));
        String classAttr = button.getAttribute("class");
        if (classAttr == null || !classAttr.contains("active")) {
            button.click();
            waitUtility.waitUntilPreLoaderHidden();
        }
    }

    public void clickClearAllButton() {
        CLEAR_ALL_BUTTON.scrollIntoViewIfNeeded();
        CLEAR_ALL_BUTTON.click();
        waitUtility.waitUntilPreLoaderHidden(120000);
    }

    public boolean verifyArchiveUnarchiveButtonsPresent(String buttonType) {
        boolean flag = false;
        if (buttonType.contains("Active") && ARCHIVED_BUTTON.first().isVisible()) {
            waitUtility.waitUntilPreLoaderHidden();
            flag = true;
        } else if (buttonType.contains("Archived") && UNARCHIVED_BUTTON.first().isVisible()) {
            waitUtility.waitUntilPreLoaderHidden();
            flag = true;
        }
        return flag;
    }

    public boolean clickArchiveUnarchiveButtons() {
        boolean flag = false;
        if (ARCHIVED_BUTTON.first().isVisible()) {
            ARCHIVED_BUTTON.first().click();
            if (ARCHIVE_DIALOG.isVisible()) ARCHIVE_BUTTON.click();
            waitUtility.waitUntilPreLoaderHidden();
            flag = true;
        } else if (UNARCHIVED_BUTTON.first().isVisible()) {
            UNARCHIVED_BUTTON.first().click();
            waitUtility.waitUntilPreLoaderHidden();
            flag = true;
        }
        return flag;
    }

    public boolean verifyFilterOptions(String key, List<String> values) {
        boolean flag = false;
        switch (key) {
            case "Advertiser":
                selectDropdownAndFill(SELECT_ADVERTISER, values);
                for (String value : values) {
                    flag = page.locator(String.format("//span[@class='label']/following-sibling::span[contains(text(),'%s')]", value)).first().isVisible();
                }
                clickClearAllButton();
                break;
            case "Associated Campaigns":
                selectDropdownAndFill(SELECT_CAMPAIGN, values);
                for (String value : values) {
                    flag = page.locator(String.format("//span[@class='label']/following-sibling::span[contains(text(),'%s')]", value)).first().isVisible();
                }
                clickClearAllButton();
                break;
            case "Approval Status":
                selectDropdownAndFill(APPROVAL_STATUS, values);
                for (String value : values) {
                    flag = page.locator(String.format("//span[contains(text(),'%s')]", value)).first().isVisible();
                }
                clickClearAllButton();
                break;
            case "Ad Sizes":
                selectDropdownAndFill(SELECT_AD_SIZE, values);
                for (String value : values) {
                    flag = page.locator(String.format("//span[@class='label']/following-sibling::span[contains(text(),'%s')]", value)).first().isVisible();
                }
                clickClearAllButton();
                break;
            case "Creative Type":
                selectDropdownAndFill(SELECT_CREATIVE_TYPE, values);
                flag = CREATIVE_TYPE_ICON.first().isVisible();
                clickClearAllButton();
                break;
            case "CreatedBy":
                selectDropdownAndFill(CREATED_BY, values);
                for (String value : values) {
                    flag = page.locator(String.format("//span[@class='label']/following-sibling::span[contains(text(),'%s')]", value)).first().isVisible();
                }
                clickClearAllButton();
                break;
        }
        return flag;
    }

    private void selectDropdownAndFill(Locator dropdown, List<String> values) {
        dropdown.click();
        for (String value : values) {
            dropdown.fill(value);
            DROPDOWN_VALUES.first().click();
        }
        page.keyboard().press("Escape");
        waitUtility.waitUntilPreLoaderHidden();
    }

    public boolean verifySortOptions(List<String> sortOptionsList) {
        boolean flag = false;
        for (String sortOption : sortOptionsList) {
            SORT_DROPDOWN.click();
            String[] options = sortOption.split("-");
            if (options.length < 2) continue;
            String direction = options[1].equalsIgnoreCase("Asc") ? "up" : "down";
            page.locator(String.format("//span[contains(text(),'%s')]/following-sibling::i[contains(@class,'%s')]", options[0], direction)).click();
            waitUtility.waitUntilPreLoaderHidden();
            if ((direction.equals("up") && SORT_BY_UP.isVisible()) || (direction.equals("down") && SORT_BY_DOWN.isVisible())) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean searchByValues(List<String> searchValuesList) {
        boolean flag = false;
        for (String searchValue : searchValuesList) {
            searchCreative(searchValue);
            flag = DATA_CONTENT_PANEL.first().isVisible();
        }
        return flag;
    }

    public void searchCreative(String searchValue) {
        SEARCH_BOX.fill(searchValue);
        page.keyboard().press("Enter");
        waitUtility.waitUntilPreLoaderHidden();
    }

    public String copyCreative() {
        COPY_ICON.first().click();
        waitUtility.waitForLocatorVisible(CREATIVE_HEADER);
        return saveCreative();
    }

    public void clickNewCreativeButton() {
        NEW_CREATIVE_BUTTON.click();
        waitUtility.waitForLocatorVisible(CREATIVE_HEADER);
    }

    public void enterCreativeDetails(String advertiser, String creativeName, String advertiserDSA, String financer) {
        ADVERTISER_DROPDOWN.fill(advertiser);
        ADVERTISER_DROPDOWN_VALUES.first().click();
        CREATIVE_NAME.fill(creativeName);
        ADVERTISER_DSA.scrollIntoViewIfNeeded();
        ADVERTISER_DSA.fill(advertiserDSA);
        FINANCER.scrollIntoViewIfNeeded();
        FINANCER.fill(financer);
        waitUtility.waitUntilPreLoaderHidden();
    }

    public void selectCreativeType(String creativeType) {
        for (int i = 0; i < CREATIVE_TYPE.count(); i++) {
            if (CREATIVE_TYPE.nth(i).innerText().trim().equalsIgnoreCase(creativeType)) {
                CREATIVE_TYPE.nth(i).click();
                break;
            }
        }
    }

    public void fillAttributes(String type, Map<String, String> attributeMap) {
        switch (type) {
            case "Html", "Html5", "Image":
                page.locator(String.format("//button[text()='%s']", type)).click();
                if (type.equals("Html5")) {
                    CommonUtils.uploadFile(page, 0, imageTextLocator, attributeMap.get("ArchiveFile"));
                }
                if (type.contains("Image")) {
                    CommonUtils.uploadFile(page, 0, imageTextLocator, attributeMap.get("ImageFile"));
                } else {
                    CREATIVE_HTML_CODE.fill(attributeMap.get("HTMLCode"));
                    MACROS_CHECKBOX.click();
                }
                CREATIVE_AD_SIZE.click();
                CommonUtils.selectAndClickElement(CREATIVE_AD_SIZE_VALUE, Collections.singletonList(attributeMap.get("Size")));
                if (CLICK_THROUGH_URL.isEditable()) {
                    CLICK_THROUGH_URL.fill(attributeMap.get("ClickThroughURL"));
                }
                DOMAIN_LANDING.fill(attributeMap.get("DomainLanding"));
                break;

            case "Upload", "Audio URL", "VAST URL", "VAST XML", "Video URL":
                page.locator(String.format("//button[text()='%s']", type)).click();
                if (type.contains("Upload"))
                    CommonUtils.uploadFile(page, 0, imageTextLocator, attributeMap.get("FileName"));
                else if (type.contains("Audio URL") || type.contains("Video URL")) URL.fill(attributeMap.get("URL"));
                else if (type.contains("VAST URL")) URL.fill(attributeMap.get("VASTURL"));
                else VAST_XML_TEXTAREA.fill(attributeMap.get("VASTXML"));
                DURATION.fill(attributeMap.get("Durations"));
                if (WIDTH.isVisible() && HEIGHT.isVisible() && CLICK_THROUGH_URL.isVisible()) {
                    WIDTH.fill(attributeMap.get("Width"));
                    HEIGHT.fill(attributeMap.get("Height"));
                    CLICK_THROUGH_URL.fill(attributeMap.get("ClickThroughURL"));
                }
                DOMAIN_LANDING.fill(attributeMap.get("AdvertiserDomain"));
                IAB_CATEGORY.fill(attributeMap.get("IAB"));
                IAB_CATEGORY_VALUE.first().click();
                if (HEADLINE.isVisible()) HEADLINE.fill(attributeMap.get("Headline"));
                if (SPONSORED_BY.isVisible()) SPONSORED_BY.fill(attributeMap.get("SponsoredBy"));
                if (PRODUCT_DESCRIPTION.isVisible()) PRODUCT_DESCRIPTION.fill(attributeMap.get("Description"));
                if (DISPLAY_URL.isVisible()) DISPLAY_URL.fill(attributeMap.get("DisplayURL"));
                break;

            case "Search":
                CREATIVE_AD_SIZE.click();
                CommonUtils.selectAndClickElement(CREATIVE_AD_SIZE_VALUE, Collections.singletonList(attributeMap.get("Size")));
                HEADLINE.fill(attributeMap.get("Headline"));
                DESCRIPTION.fill(attributeMap.get("Description"));
                DISPLAY_URL.fill(attributeMap.get("DisplayURL"));
                CLICK_THROUGH_URL.fill(attributeMap.get("ClickThroughURL"));
                IAB_CATEGORY.fill(attributeMap.get("IAB"));
                IAB_CATEGORY_VALUE.first().click();
                break;

            case "Native Display":
                CommonUtils.uploadFile(page, 0, imageTextLocator, attributeMap.get("ImageFile"));
                CLICK_THROUGH_URL.fill(attributeMap.get("ClickThroughURL"));
                DOMAIN_LANDING.fill(attributeMap.get("DomainLanding"));
                IAB_CATEGORY.fill(attributeMap.get("IAB"));
                IAB_CATEGORY_VALUE.first().click();
                HEADLINE.fill(attributeMap.get("Headline"));
                SPONSORED_BY.fill(attributeMap.get("SponsoredBy"));
                PRODUCT_DESCRIPTION.fill(attributeMap.get("Description"));
                DISPLAY_URL.fill(attributeMap.get("DisplayURL"));
        }
    }

    public String saveCreative() {
        OK_BUTTON.click();
        return SUCCESS_ALERT.innerText().trim();
    }

    public List<String> fetchCreatives() {
        String creativeName = CREATIVE_NAME_TEXT.innerText().replaceAll("\\b(Creative|created)\\b\\p{Punct}?", "").trim().replaceAll(" +", " ");
        creativesList.add(creativeName);
        waitUtility.waitForLocatorHidden(SUCCESS_ALERT);
        return creativesList;
    }

    public boolean verifyCreativesInLibrary(String name) {
        searchCreative(name);
        Locator locator = page.locator(String.format("//div[@title='%s']", name));
        return locator.innerText().equalsIgnoreCase(name);
    }
}