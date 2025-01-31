package pages;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

import static factory.DriverFactory.page;

public class ExpansionWorkspace {
    private final Page page;
    private final Locator HCP_AUDIENCEEXP;
    private final Locator ADVT_DROPDOWN;
    private final Locator SELECT_ADVT;
    private final Locator SRC_AUDIENCE;
    private final Locator SELECT_SRCAUDIENCE;
    private final Locator EXPAND_CARETEAM;
    private final Locator EXPAND_AFFGRAPH;
    private final Locator ADD_FILTER;
    private final Locator NPI_AGE;
    private final Locator Below_25;
    private final Locator blank;
    private final Locator OK_FILTER;
    private final Locator SAVE;
    private final Locator WRKSP_NM;
    private final Locator Enter_MYPG;
    private final Locator x;
    public ExpansionWorkspace(Page page) {
        this.page = page;

        this.HCP_AUDIENCEEXP =page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.IMG).nth(2);
      this.ADVT_DROPDOWN =   page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.TEXTBOX);
      this.SELECT_ADVT =page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.OPTION, new FrameLocator.GetByRoleOptions().setName("Abbvie"));


this.blank= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("Create New WorkspaceAdvertiserSource AudienceNPI ListExtend audience of");

    this.SRC_AUDIENCE =page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Studio Workspace Extend"));
        this.SELECT_SRCAUDIENCE =page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("My playground");
        this.Enter_MYPG=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));

        this.EXPAND_CARETEAM =page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("Expand With Care Team");
        this.EXPAND_AFFGRAPH=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("Expand With Affiliation Graph");
        this.ADD_FILTER =page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Add Filters"));

       this.NPI_AGE =      page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^NPI Age$"))).nth(3);
        this.Below_25 =page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByLabel("Below");
        this.OK_FILTER =page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Ok"));
         this.SAVE = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Save"));
        this.x=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^Select Filter$"))).getByRole(AriaRole.BUTTON);
        this.WRKSP_NM=  page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.TEXTBOX).nth(2);

    }

    public void HCP_AUDIENCE_EXP() {
        page.waitForTimeout(6000);
        page.waitForLoadState();
        HCP_AUDIENCEEXP.click();
    }
    public void ADVT_DRPDWN() {

        ADVT_DROPDOWN.click();
        ADVT_DROPDOWN.fill("Abbvie");
        page.waitForTimeout(3000);
          SELECT_ADVT.click();
        page.waitForTimeout(6000);

       // page.waitForTimeout(2000);
        //  blank.click();
       // ADVT_DROPDOWN.click();
        //ADVT_DROPDOWN.selectOption("Advie");

    }
//    public void ADVT_SELECT() {
//
//        SELECT_ADVT.click();
//    }
    public void SRCAUDI_SELECT() {
        page.waitForLoadState();
        page.waitForTimeout(3000);
        SRC_AUDIENCE.click();
        page.waitForTimeout(6000);
        Enter_MYPG.fill("My playground");
        page.waitForTimeout(3000);

        SELECT_SRCAUDIENCE.click();
        page.waitForTimeout(3000);
    }
    public void EXPAND_CARETEAM() {

        page.waitForLoadState();
        page.waitForTimeout(3000);
        EXPAND_CARETEAM.click();

        page.waitForTimeout(6000);

    }
    public void EXPAND_AFFGRAPH() {
        EXPAND_AFFGRAPH.click();
        page.waitForTimeout(3000);
    }
    public void ADD_FILTER() {
        page.waitForLoadState();
        page.waitForTimeout(3000);
        ADD_FILTER.click();
        NPI_AGE.click();
        Below_25.click();
        //OK_FILTER.scrollIntoViewIfNeeded();
        page.waitForTimeout(2000);
        OK_FILTER.click();
        page.waitForTimeout(4000);
        x.click();
        page.waitForTimeout(3000);

    }

    public void SAVE_EXP() {
        SAVE.click();
    }
    public void RENAME_EXP() {
        WRKSP_NM.click();
        WRKSP_NM.click();
        page.waitForTimeout(3000);
        WRKSP_NM.fill("Playwright_Test1");
        page.waitForTimeout(3000);
    }
//    public String VERIFY_WRSP() {
//        page.waitForLoadState();
//        return this.HCP_AUDIENCEEXP.innerText();
//    }
}