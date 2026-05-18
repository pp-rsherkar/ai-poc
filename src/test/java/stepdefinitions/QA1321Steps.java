package stepdefinitions;

import factory.DriverFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import pages.Navigation;
import pages.life.Campaigns;
import pages.life.LineItemDetails;
import pages.life.TacticDetails;
import pages.life.TacticSettings;
import utils.CommonUtils;
import utils.ConfigReader;

import java.util.List;
import java.util.Map;

public class QA1321Steps {
    private static final String INTERNAL_USER_TYPE = "user";

    private final Navigation navigation = new Navigation(DriverFactory.getPage());
    private final Campaigns campaigns = new Campaigns(DriverFactory.getPage());
    private final LineItemDetails lineItemDetails = new LineItemDetails(DriverFactory.getPage());
    private final TacticDetails tacticDetails = new TacticDetails(DriverFactory.getPage());
    private final TacticSettings tacticSettings = new TacticSettings(DriverFactory.getPage());

    private String userType;
    private String url;
    private String username;
    private String password;
    private String campaignNameRandom;
    private String lineItemNameRandom;
    private String tacticNameRandom;

    @Given("QA-1321 user logs into Life in the {string} environment as a {string} user for account {string}")
    public void qa1321_user_logs_into_life(String environment, String user, String account) throws Exception {
        userType = user;

        if (environment.equals("Demo")) {
            url = ConfigReader.getProperty("demoURL");
            if (user != null && user.toLowerCase().contains("external") && ConfigReader.getProperty("demoExternalUser") != null) {
                username = ConfigReader.getExternalDemoUsername();
                password = ConfigReader.getExternalDemoPassword();
            } else {
                username = ConfigReader.getInternalDemoUsername();
                password = ConfigReader.getInternalDemoPassword();
            }
        } else if (environment.equals("Pre-release")) {
            url = ConfigReader.getProperty("preReleaseURL");
            if (user != null && user.toLowerCase().contains("external")) {
                username = ConfigReader.getExternalPreReleaseUsername();
                password = ConfigReader.getExternalPreReleasePassword();
            } else {
                username = ConfigReader.getInternalPreReleaseUsername();
                password = ConfigReader.getInternalPreReleasePassword();
            }
        } else {
            throw new IllegalArgumentException("Unsupported environment: " + environment);
        }

        navigation.navigateToUrl(url);
        navigation.enterUsername(username);
        navigation.enterPassword(password);
        navigation.clickLogin();
        navigation.selectAndClickExternalUserApplicationType();
        if (userType != null && INTERNAL_USER_TYPE.equalsIgnoreCase(userType)) {
            navigation.navigateToLife();
            navigation.selectAccount(account);
        } else {
            navigation.selectExternalUserAccount(account);
        }
    }

    @When("QA-1321 user creates a campaign with details {string} {string} {string} {string} and management fee {string} {string} {string}")
    public void qa1321_user_creates_campaign_with_management_fee(String advertiser, String campaignName, String campaignType, String campaignBudget,
                                                                  String managementFeeOption, String managementFeePercent, String managementFeeAmount) {
        campaignNameRandom = campaignName + '_' + CommonUtils.timeStampCalculation();

        campaigns.createCampaign();
        Assert.assertEquals("Create New Campaign", campaigns.verifyCampaignText());

        campaigns.selectAdvertiser(advertiser);
        campaigns.enterCampaignName(campaignNameRandom);
        campaigns.setCampaignType(campaignType);
        campaigns.enterBudget(campaignBudget);

        Assert.assertTrue("Campaign management fee checkbox is not visible", campaigns.isManagementFeeAvailable());
        campaigns.clickManagementFee();
        campaigns.clickManagementFeeOptionAndEnterData(managementFeeOption, managementFeePercent, managementFeeAmount);

        campaigns.saveCampaign();
        Assert.assertEquals("Campaign " + campaignNameRandom + " created.", campaigns.campaignSuccess());
        Assert.assertEquals("New Line Item", lineItemDetails.verifyLineItemText());
    }

    @And("QA-1321 user creates a line item with details {string} {string}")
    public void qa1321_user_creates_line_item(String lineItemName, String lineItemBudget) {
        lineItemNameRandom = lineItemName + '_' + CommonUtils.timeStampCalculation();

        lineItemDetails.enterLineItemName(lineItemNameRandom);
        navigation.clickOnIcon("Add Flight");
        lineItemDetails.enterLineItemBudget(lineItemBudget);
        lineItemDetails.isPlacementIdAvailable(lineItemNameRandom);
        lineItemDetails.enableLineItem();
        lineItemDetails.saveLineItem();

        Assert.assertEquals("Lineitem " + lineItemNameRandom + " created.", lineItemDetails.lineItemSuccess());
        Assert.assertTrue(tacticDetails.verifyTacticDetailsText().contains("New Tactic") || tacticDetails.verifyTacticDetailsText().contains("New Ad Group"));
    }

    @And("QA-1321 user verifies management fee {string} is inherited in line item and tactic {string}")
    public void qa1321_user_verifies_inherited_management_fee(String expectedFeeDisplayValue, String tacticName) {
        tacticNameRandom = tacticName + '_' + CommonUtils.timeStampCalculation();
        tacticDetails.createTactic(tacticNameRandom);
        tacticDetails.clickSettingsTab();
        Assert.assertEquals(expectedFeeDisplayValue, tacticSettings.fetchDisplayedManagementFeeValue());

        campaigns.clickLineItemTile();
        lineItemDetails.clickDetailsTab();
        Assert.assertEquals(expectedFeeDisplayValue, lineItemDetails.fetchDisplayedManagementFeeValue());

        campaigns.clickTacticTile();
        tacticDetails.clickSettingsTab();
    }

    @Then("QA-1321 user overrides tactic management fee with the following values")
    public void qa1321_user_overrides_tactic_management_fee(DataTable dataTable) {
        List<Map<String, String>> feeDetails = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> feeRow : feeDetails) {
            String feeOption = feeRow.get("Fee Option");
            String percent = feeRow.get("Percent");
            String amount = feeRow.get("Amount");
            String expectedDisplay = feeRow.get("Expected Display");

            tacticDetails.selectManagementFeeOptionAndEnterData(feeOption, percent, amount, expectedDisplay);
            Assert.assertEquals(expectedDisplay, tacticSettings.fetchDisplayedManagementFeeValue());
        }
    }
}
