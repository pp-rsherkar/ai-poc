package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ExplorerWorkspace {
    private final Page page;
    private final Locator WORKSPACE_NAME;
    private final Locator SEARCH_ADVERTISER;
    private final Locator DASHBOARD_CONTENT;
    private final Locator DASHBOARD_ELEMENT;
    private final Locator DASHBOARD_RELOAD_ICON;
    private final Locator ADD_FILTER;
    private final Locator SEARCH_FILTER;
    private final Locator SELECT_FILTER;
    private final Locator FILTER_OK_BUTTON;
    private final Locator FILTER_CLOSE_BUTTON;
    private final Locator APPLIED_FILTER;
    private final Locator APPLIED_FILTER_OPTION;
    private final Locator SAVE_WORKSPACE;
    private final Locator EXPLORER_WORKSPACE_SUCCESS;
    private final FrameLocator WORKSPACE_FRAME;
    private final Locator SAVE_WORKSPACE_NAME;
    private final Locator TO_YEAR;
    private final Locator FROM_YEAR;
    private final Locator REACHABLE_AUDIENCE;
    private final Locator TAB_PANEL_SEARCH;
    private final Locator SELECT_DESELECT_ALL;
    private final Locator AI_CONFIGURATOR_BTN;
    private final Locator AUDIENCE_DESCRIPTION_TEXTAREA;
    private final Locator BUILD_AUDIENCE_BTN;
    private final Locator TRY_ANOTHER_PROMPT_BTN;
    private final Locator FILTER_HEADER_TITLE;
    private final Locator MAP_TOOL_TIP;
    private final Locator DELETE_FILTER;
    private final Locator CAMERA_CONTROL_ICON;
    private final Locator ZOOM_OUT;
    private final Locator MAP_CONTENT;
    private final Locator DASHBOARD_FILTER_TITLE;
    private final Locator MERGED_TEXT;
    private final Locator DASHBOARD_FILTERS;


    public ExplorerWorkspace(Page page) {
        this.page = page;
        this.WORKSPACE_FRAME = page.frameLocator("iframe#iframe0").frameLocator("iframe");
        this.WORKSPACE_NAME = WORKSPACE_FRAME.getByRole(AriaRole.TEXTBOX).nth(1);
        this.SEARCH_ADVERTISER = WORKSPACE_FRAME.locator("input[id^='listbox-input']");
        this.DASHBOARD_CONTENT = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().getByRole(AriaRole.REGION, new FrameLocator.GetByRoleOptions().setName("Dashboard Content"));
        this.DASHBOARD_ELEMENT = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator("(//p[contains(@class,'TextBase-sc')])[1]");
        this.DASHBOARD_RELOAD_ICON = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator("//div[contains(text(),'Reload')]");
        this.ADD_FILTER = WORKSPACE_FRAME.locator("//div[contains(text(),'Add Filter')]");
        this.SEARCH_FILTER = WORKSPACE_FRAME.getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));
        this.SELECT_FILTER = WORKSPACE_FRAME.locator("//div[contains(@class,'styles__StyledIconLabelContainer') or contains(@class,'styles__StyledSubGroupContainer')]");
        this.FILTER_OK_BUTTON = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Ok"));
        this.FILTER_CLOSE_BUTTON = WORKSPACE_FRAME.locator("//h1[contains(text(),'Select Filter')]/following-sibling::button");
        this.APPLIED_FILTER = WORKSPACE_FRAME.locator("//div[contains(@class,'style__FilterTitleContainer-sc-')]");
        this.APPLIED_FILTER_OPTION = WORKSPACE_FRAME.locator("//div[contains(@class,'style__FilterExpression-sc')]");
        this.SAVE_WORKSPACE = WORKSPACE_FRAME.locator("//div[contains(@class,'styles__StyledContainer')]//div[contains(text(),'Save')]");
        this.EXPLORER_WORKSPACE_SUCCESS = WORKSPACE_FRAME.locator("[id=\"\\32 \"] div").filter(new Locator.FilterOptions().setHasText("Workspace managementWorkspace")).nth(2);
        this.SAVE_WORKSPACE_NAME = WORKSPACE_FRAME.locator("//div[contains(@class,'styles__DashboardContainer')]//div[contains(text(),'Save')]");
        this.TAB_PANEL_SEARCH = WORKSPACE_FRAME.locator("//div[@role='tabpanel']//input[@placeholder='Search']");
        this.TO_YEAR = WORKSPACE_FRAME.locator("//input[@data-testid='bi-slider-input-0']");
        this.FROM_YEAR = WORKSPACE_FRAME.locator("//input[@data-testid='bi-slider-input-1']");
        this.REACHABLE_AUDIENCE = WORKSPACE_FRAME.locator("//input[@type='checkbox' and contains(@class,'PrivateSwitchBase-input')]");
        this.SELECT_DESELECT_ALL = WORKSPACE_FRAME.locator("//div[contains(text(),'Select/Deselect All')]");
        this.AI_CONFIGURATOR_BTN = WORKSPACE_FRAME.locator("//p[contains(text(),'AI Configurator')]");
        this.AUDIENCE_DESCRIPTION_TEXTAREA = WORKSPACE_FRAME.locator("//textarea[@placeholder='Describe your Audience']");
        this.BUILD_AUDIENCE_BTN = WORKSPACE_FRAME.locator("//div[text()='Build Audience']");
        this.TRY_ANOTHER_PROMPT_BTN = WORKSPACE_FRAME.locator("//div[text()='Try another prompt']");
        this.FILTER_HEADER_TITLE = WORKSPACE_FRAME.locator("//div[contains(@data-tour-id, 'filters-container')]");
        this.MAP_TOOL_TIP = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator("//div[contains(@class,'MapTooltip')]/div/div/div[text()='Identified NPIs']/following-sibling::div");
        this.DELETE_FILTER = WORKSPACE_FRAME.locator("//div[contains(@class, 'FilterTitleContainer')]//button");
        this.CAMERA_CONTROL_ICON = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator("//button[@title='Map camera controls']");
        this.ZOOM_OUT = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator("//div[@class='gmnoprint']//button[@title='Zoom out' and @class='gm-control-active']");
        this.MAP_CONTENT = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator("div[aria-label='Dashboard Content']");
        this.DASHBOARD_FILTER_TITLE = WORKSPACE_FRAME.locator("//p[contains(text(),'Dashboard Filters')]");
        this.MERGED_TEXT = WORKSPACE_FRAME.locator("//p[contains(text(),'Merged with Primary after Save')]");
        this.DASHBOARD_FILTERS = WORKSPACE_FRAME.locator("//div[contains(@class,'style__PillContainer')]");
    }

    public void enterWorkspaceName(String workspaceName) {
        WORKSPACE_NAME.clear();
        WORKSPACE_NAME.fill(workspaceName);
    }

    public void selectAdvertiser(String advertiser) {
        SEARCH_ADVERTISER.click();
        SEARCH_ADVERTISER.fill(advertiser);
        SEARCH_ADVERTISER.press("ArrowDown");
        SEARCH_ADVERTISER.press("Enter");
        SAVE_WORKSPACE_NAME.click();
        DASHBOARD_CONTENT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DASHBOARD_ELEMENT.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DASHBOARD_RELOAD_ICON.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void clickAddFilter() {
        ADD_FILTER.click();
    }

    public void selectFilter(String filter, List<String> options) {
        SEARCH_FILTER.fill(filter);
        WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]", filter)).first().click();
        switch (filter) {
            case "NPI List Name", "Medical School", "Profession", "Specialty", "State", "Facility Name":
                if (filter.equals("Specialty"))
                    WORKSPACE_FRAME.locator("//span[contains(text(),'All Specialties')]").click();
                for (String option : options) {
                    Locator locator = WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]/preceding-sibling::div/button[@data-testid='bi-include-exclude-check']", option.trim()));
                    TAB_PANEL_SEARCH.fill(option.trim());
                    locator.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                    page.waitForTimeout(1000);
                    SELECT_DESELECT_ALL.click();
                }
                break;
            case "NPI Gender", "NPI Age", "Years Practiced", "Number of Patients", "Patient Age", "Patient Gender":
                for (String option : options) {
                    WORKSPACE_FRAME.locator(String.format("//label[contains(text(),'%s')]", option.trim())).click();
                }
                break;
            case "Site", "Search":
                for (String option : options) {
                    WORKSPACE_FRAME.locator(String.format("//p[contains(text(),'%s')]", option.trim())).click();
                }
                break;
            case "Graduation Year":
                for (String option : options) {
                    String[] array = option.split("-");
                    TO_YEAR.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                    page.waitForTimeout(1000);
                    TO_YEAR.fill(array[0]);
                    FROM_YEAR.fill(array[1]);
                }
                break;
            case "Net Worth":
                for (String option : options) {
                    WORKSPACE_FRAME.locator(String.format("//label[contains(text(),'%s')]", option.trim())).first().click();
                }
                break;
            case "Reachable Audience":
                REACHABLE_AUDIENCE.click();
                break;
        }
        FILTER_OK_BUTTON.click();
        SEARCH_FILTER.clear();
    }

    public void applyFilter() {
        FILTER_CLOSE_BUTTON.click();
    }

    public List<String> getAllSelectedFilters() {
        DASHBOARD_RELOAD_ICON.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return APPLIED_FILTER.allInnerTexts();
    }

    public List<String> getAllSelectedOptions() {
        return APPLIED_FILTER_OPTION.allInnerTexts();
    }

    public void saveExplorerWorkspace() {
        DASHBOARD_ELEMENT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DASHBOARD_RELOAD_ICON.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SAVE_WORKSPACE.first().click();
    }

    public String workspaceSuccess() {
        EXPLORER_WORKSPACE_SUCCESS.waitFor();
        return EXPLORER_WORKSPACE_SUCCESS.innerText();
    }

    public void clickAIConfigurator() {
        AI_CONFIGURATOR_BTN.click();
    }

    public boolean describeAudience(String aiPrompt) {
        AUDIENCE_DESCRIPTION_TEXTAREA.fill(aiPrompt);
        BUILD_AUDIENCE_BTN.click();
        TRY_ANOTHER_PROMPT_BTN.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return FILTER_HEADER_TITLE.isVisible();
    }

    public List<String> checkNPIDetails() {
        DASHBOARD_RELOAD_ICON.waitFor(new Locator.WaitForOptions().setTimeout(240000).setState(WaitForSelectorState.VISIBLE));
        return DASHBOARD_ELEMENT.allInnerTexts();
    }

    public void deleteFilter() {
        DELETE_FILTER.first().click();
    }

    public void hoverOverNPIVisualsIcon(List<String> npiVisualList) {
        for (int i = 0; i < npiVisualList.size(); i++) {
            String visual = npiVisualList.get(i).trim();
            boolean isInView = false;
            switch (npiVisualList.get(i).trim()){
               case "NPI Geographic Location", "NPI Facilities Geography", "NPI ZIP Codes" :
                   Locator MAP_TILE = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator(String.format("//h2[@data-title='%s']/parent::div/following-sibling::div//div[contains(@style,'z-index: 3;')]", visual));
                   isInView = CommonUtils.scrollElementIntoView(MAP_CONTENT, CAMERA_CONTROL_ICON,1000,  100, page);
                   if (!isInView) {
                       continue;
                   }
                   if (npiVisualList.get(i).contains("NPI Facilities Geography")) {
                       ZOOM_OUT.first().click();
                   } else if (npiVisualList.get(i).contains("NPI ZIP Codes")) {
                       ZOOM_OUT.nth(1).click();
                   }

                   BoundingBox box = MAP_TILE.boundingBox();
                   CommonUtils.hoverAndClick(page, box, MAP_TOOL_TIP);
                   break;
               case "Top 20 Market Areas","Top 20 Professions","Top 20 Specialties","Top 20 Insurance Providers", "Top 20 Prescriptions",
                    "Top 20 Diagnoses", "Top 20 Procedures", "Top 20 MeSH Categories", "Top 20 IAB Categories":
                    Locator TOP_ENTITIES = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator(String.format("//h2[@data-title='%s']/parent::div/following-sibling::div//div[@class='ag-center-cols-container']/div", visual));
                    isInView = CommonUtils.scrollElementIntoView(MAP_CONTENT, TOP_ENTITIES,1000,  100, page);
                    if (!isInView) {
                       continue;
                    }
                    if(TOP_ENTITIES.count() > 0){
                       TOP_ENTITIES.first().click();
                    }
                    break;
               case "NPI Age Range", "NPI Gender", "Patient Age Range", "Patient Gender","Net Worth", "Years Practiced", "Patient Distribution":
                   Locator NPI_PATIENT_ENTITIES = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator(String.format("//h2[@data-title='%s']/parent::div/following-sibling::div//*[name()='path' and contains(@class, 'highcharts-point')]", visual));
                   isInView = CommonUtils.scrollElementIntoView(MAP_CONTENT, NPI_PATIENT_ENTITIES, 1000,  100, page);
                   if (!isInView) {
                       continue;
                   }
                   if(NPI_PATIENT_ENTITIES.count() > 0){
                       NPI_PATIENT_ENTITIES.first().click();
                   }
                   break;
           }
            DASHBOARD_RELOAD_ICON.waitFor(new Locator.WaitForOptions().setTimeout(240000).setState(WaitForSelectorState.VISIBLE));
        }
    }

    public boolean areCrossFiltersDisplayed() {
        return DASHBOARD_FILTER_TITLE.isVisible() && MERGED_TEXT.isVisible() && DASHBOARD_FILTERS.count() > 0;
    }

    public List<String> fetchMergedFilters() {
        List<String> mergeFilterName = new ArrayList<>();
        for(String dashboardFilter : DASHBOARD_FILTERS.allInnerTexts()) {
            String[] parts = dashboardFilter.split(":");
            if (parts.length > 0) {
                mergeFilterName.add(parts[0].trim());  // Extract value before colon and trim whitespace
            }
        }
        return mergeFilterName;
    }
}