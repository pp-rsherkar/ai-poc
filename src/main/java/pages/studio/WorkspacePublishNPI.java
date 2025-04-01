package pages.studio;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import java.util.UUID;

public class WorkspacePublishNPI
{

    private final Page page;
    private final Locator studioClick;
    private final Locator searchWorkspace;
//    private Locator clickWorkspace;
    private final Locator clickDownloadbutton;
    private final Locator clickPublishNpi;
    private final Locator staticList;
    private final Locator liveList;
    private final Locator selectHCP;
    private final Locator selectLIFE;
    private final Locator clickPublishbutton;
    //private String WORKSPACE;
    private final Locator verifyPopupMessage;

    public WorkspacePublishNPI(Page page){
        this.page=page;
        this.studioClick=page.locator("#megamenu div").filter(new Locator.FilterOptions().setHasText("Studio")).nth(3);
        this.searchWorkspace= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.TEXTBOX, new FrameLocator.GetByRoleOptions().setName("Search"));
        this.clickDownloadbutton= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().locator(".styles__StyledScheduleStatus-sc-u6f0o3-5 > svg").first();
        this.clickPublishNpi=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Publish NPI List"));
        this.staticList=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.RADIO, new FrameLocator.GetByRoleOptions().setName("Static List"));
        this.liveList =page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.RADIO, new FrameLocator.GetByRoleOptions().setName("Live List"));
        this.selectHCP=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.CHECKBOX, new FrameLocator.GetByRoleOptions().setName("HCP365"));
        this.selectLIFE= page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.CHECKBOX, new FrameLocator.GetByRoleOptions().setName("Life"));
        this.clickPublishbutton = page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Publish"));
        this.verifyPopupMessage=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText("Workspace saved and ready to");
    }

    public void studio(){
        studioClick.click();
        Response reload = page.reload();
    }
    public void searchWorkspace(String WORKSPACE){
       // WORKSPACE = WORKSPACE + '_' + UUID.randomUUID().toString().substring(0, 10);
        searchWorkspace.fill(WORKSPACE);
        Locator clickWorkspace=page.locator("iframe[title=\"overview\"]").contentFrame().locator("iframe").contentFrame().getByText(WORKSPACE);
        clickWorkspace.click();
    }
    public void clickDownbutton(){

        clickDownloadbutton.click();
    }
    public void clickPublishNpi(){

        clickPublishNpi.click();
    }
    public void publish(String listType)
    {
        if(listType.equals("Static"))
        {
            staticList.click();
        }
        else
        {
            liveList.click();
        }
    }
    public void hcp(){

        selectHCP.click();
    }
    public void life(){

        selectLIFE.click();
    }
    public void clickPublish(){

        clickPublishbutton.click();
        page.waitForTimeout(5000);
    }
    public String verifyToast(){
        String msg=verifyPopupMessage.textContent();
        System.out.println("Toast msg is : "+msg);
        return msg;
    }

}
