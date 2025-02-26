package pages;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

public class Studio_PublishNPIList {

    private final Page page;
    private final Locator megamenu;
    private final Locator studioClick;
    private final Locator searchWorkspace;
    private Locator clickWorkspace;
    private final Locator clickDownloadbutton;
    private final Locator clickPublishNpi;
    private final Locator StaticList;
    private final Locator LiveList;
    private final Locator selectHCP;
    private final Locator selectLIFE;
    private final Locator clickPublishBtn;
    private String workspace;

    public Studio_PublishNPIList(Page page){
        this.page=page;
        this.megamenu= page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("menu"));
        this.studioClick=page.locator("#megamenu div").filter(new Locator.FilterOptions().setHasText("Studio")).nth(3);
        this.searchWorkspace= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));
      // this.clickWorkspace= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.GRIDCELL, new FrameLocator.GetByRoleOptions().setName(workspace)).getByRole(AriaRole.IMG).first();
       // this.clickWorkspace= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("PB_Test_Verify1");
        this.clickDownloadbutton= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().locator(".styles__StyledScheduleStatus-sc-u6f0o3-5 > svg").first();
        this.clickPublishNpi=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Publish NPI List"));
        this.StaticList=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.RADIO, new FrameLocator.GetByRoleOptions().setName("Static List"));
        this.LiveList=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.RADIO, new FrameLocator.GetByRoleOptions().setName("Live List"));
        this.selectHCP=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.CHECKBOX, new FrameLocator.GetByRoleOptions().setName("HCP365"));
        this.selectLIFE= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.CHECKBOX, new FrameLocator.GetByRoleOptions().setName("Life"));
        this.clickPublishBtn=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Publish NPI List"));

    }

    public void menuclick(){

        megamenu.click();
    }
    public void studio(){
        studioClick.click();
        Response reload = page.reload();
    }
    public void searchworksp(String workspace){
        //String name=searchWorkspace.textContent();
        //System.out.println("name is : "+name);
        searchWorkspace.fill(workspace);
        //String name=searchWorkspace.innerText();
       clickWorkspace=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText(workspace);
        //System.out.println("workspace is: "+name);
        clickWorkspace.click();
    }
    public void clickdwnbtn(){
        clickDownloadbutton.click();
    }
    public void clickpubNpi(){
        clickPublishNpi.click();
    }
    public void publish(String listType)
    {
        if(listType.equals("Static"))
        {
            StaticList.click();
        }
        else
        {
            LiveList.click();
        }
    }
    public void hcp(){
        selectHCP.click();
    }
    public void life(){
        selectLIFE.click();
    }
    public void clickpubBtn(){
        clickPublishBtn.click();
    }

}
