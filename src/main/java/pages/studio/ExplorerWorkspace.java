package pages.studio;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

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
    private final Locator SAVE_EXPLORER_WORKSPACE;
    private final Locator EXPLORER_WORKSPACE_SUCCESS;
    private final FrameLocator WORKSPACE_FRAME;
    private final Locator INCLUDE_CHECKBOX;
    private final Locator TO_YEAR;
    private final Locator FROM_YEAR;
    private final Locator REACHABLE_AUDIENCE;
    private final Locator TAB_PANEL_SEARCH;
    private final Locator SELECT_DESELECT_ALL;
    private final Locator AI_CONFIGURATOR_BTN;
    private final Locator AUDIENCE_DESCRIPTION_TEXTAREA;
    private final Locator BUILD_AUDIENCE_BTN;
    private final Locator TRY_ANOTHER_PROMPT_BTN;


    public ExplorerWorkspace(Page page) {
        this.page = page;
        this.WORKSPACE_FRAME = page.frameLocator("iframe#iframe0").frameLocator("iframe");
        this.WORKSPACE_NAME = WORKSPACE_FRAME.getByRole(AriaRole.TEXTBOX).nth(1);
        this.SEARCH_ADVERTISER = WORKSPACE_FRAME.locator("input[id^='listbox-input']");
        this.DASHBOARD_CONTENT = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().getByRole(AriaRole.REGION, new FrameLocator.GetByRoleOptions().setName("Dashboard Content"));
        this.DASHBOARD_ELEMENT = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().getByRole(AriaRole.REGION, new FrameLocator.GetByRoleOptions().setName("Dashboard Content")).locator("(//p[contains(@class,'TextBase-sc')])[1]");
        this.DASHBOARD_RELOAD_ICON = WORKSPACE_FRAME.locator("#extension-root iframe").contentFrame().locator("//div[contains(text(),'Reload')]");
        this.ADD_FILTER = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Add Filters"));
        this.SEARCH_FILTER = WORKSPACE_FRAME.getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));
        this.SELECT_FILTER = WORKSPACE_FRAME.locator("//div[contains(@class,'styles__StyledIconLabelContainer') or contains(@class,'styles__StyledSubGroupContainer')]");
        this.FILTER_OK_BUTTON = WORKSPACE_FRAME.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Ok"));
        this.FILTER_CLOSE_BUTTON = WORKSPACE_FRAME.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Select Filter$"))).getByRole(AriaRole.BUTTON);
        this.APPLIED_FILTER = WORKSPACE_FRAME.locator("//div[contains(@class,'style__FilterTitle-sc-')]");
        this.APPLIED_FILTER_OPTION = WORKSPACE_FRAME.locator("//div[contains(@class,'style__FilterExpression-sc')]");
        this.SAVE_EXPLORER_WORKSPACE = WORKSPACE_FRAME.locator("//button[contains(@class,'ButtonBase__ButtonOuter')]/div[contains(text(),'Save')]");
        this.EXPLORER_WORKSPACE_SUCCESS = WORKSPACE_FRAME.locator("[id=\"\\32 \"] div").filter(new Locator.FilterOptions().setHasText("Workspace managementWorkspace")).nth(2);
        this.TAB_PANEL_SEARCH = WORKSPACE_FRAME.locator("//div[@role='tabpanel']//input[@placeholder='Search']");
        this.INCLUDE_CHECKBOX = WORKSPACE_FRAME.locator("//button[@data-testid='bi-include-exclude-check']");
        this.TO_YEAR = WORKSPACE_FRAME.locator("//input[@data-testid='bi-slider-input-0']");
        this.FROM_YEAR = WORKSPACE_FRAME.locator("//input[@data-testid='bi-slider-input-1']");
        this.REACHABLE_AUDIENCE = WORKSPACE_FRAME.locator("//input[@type='checkbox' and contains(@class,'PrivateSwitchBase-input')]");
        this.SELECT_DESELECT_ALL = WORKSPACE_FRAME.locator("//div[contains(text(),'Select/Deselect All')]");
        this.AI_CONFIGURATOR_BTN = WORKSPACE_FRAME.locator("//span[text()='AI Configurator']");
        this.AUDIENCE_DESCRIPTION_TEXTAREA = WORKSPACE_FRAME.locator("//textarea[@placeholder='Describe your Audience']");
        this.BUILD_AUDIENCE_BTN = WORKSPACE_FRAME.locator("//div[text()='Build Audience']");
        this.TRY_ANOTHER_PROMPT_BTN = WORKSPACE_FRAME.locator("//button[text()='Try another prompt']");
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
        DASHBOARD_CONTENT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DASHBOARD_ELEMENT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DASHBOARD_RELOAD_ICON.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void clickAddFilter() {
        ADD_FILTER.click();
    }

    public void selectFilter(String filter, List<String> options) {
        SEARCH_FILTER.fill(filter);
        SELECT_FILTER.first().click();
        switch (filter) {
            case "NPI List Name", "Medical School", "Profession", "Specialty", "State", "Facility Name":
                if(filter.equals("Specialty"))
                    WORKSPACE_FRAME.locator("//span[contains(text(),'All Speciality')]").click();
                for (String option : options) {
                    Locator locator = WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]/preceding-sibling::div/button[@data-testid='bi-include-exclude-check']", option.trim()));
                    TAB_PANEL_SEARCH.fill(option);
                    locator.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                    page.waitForTimeout(500);
                    SELECT_DESELECT_ALL.click();
                }
                break;
            case "NPI Gender", "NPI Age", "Years Practiced","Number of Patients", "Patient Age", "Patient Gender":
                for (String option : options) {
                    WORKSPACE_FRAME.locator(String.format("//label[contains(text(),'%s')]", option.trim())).click();
                }
                break;
            case "Site", "Search":
                for (String option : options) {
                    WORKSPACE_FRAME.locator(String.format("//span[contains(text(),'%s')]", option.trim())).click();
                }
                break;
            case "Graduation Year":
                for(String option : options){
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

    public List<String> verifyAllSelectedFilters() {
        DASHBOARD_CONTENT.waitFor();
        return APPLIED_FILTER.allInnerTexts();
    }

    public List<String> verifyAllSelectedOptions() {
        return APPLIED_FILTER_OPTION.allInnerTexts();
    }

    public void saveExplorerWorkspace() {
        DASHBOARD_ELEMENT.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        DASHBOARD_RELOAD_ICON.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        SAVE_EXPLORER_WORKSPACE.first().click();
    }

    public String workspaceSuccess() {
        EXPLORER_WORKSPACE_SUCCESS.waitFor();
        return EXPLORER_WORKSPACE_SUCCESS.innerText();
    }

    public void clickUIConfigurator(String uiPrompt) {
        AI_CONFIGURATOR_BTN.click();
        AUDIENCE_DESCRIPTION_TEXTAREA.fill(uiPrompt);
        BUILD_AUDIENCE_BTN.click();
        TRY_ANOTHER_PROMPT_BTN.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

}