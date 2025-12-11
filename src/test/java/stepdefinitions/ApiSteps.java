package stepdefinitions;

import api.ApiActions;
import api.ApiEndpoints;
import com.microsoft.playwright.APIResponse;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.node.ArrayNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import utils.CommonUtils;
import utils.ConfigReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class ApiSteps {

    APIResponse response;
    JsonNode jsonNode;
    String bearerToken;
    String modifiedName;
    Path path = Paths.get("src/main/resources/apiRequest/request.json");
    ApiActions apiActions = new ApiActions();
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode data;


    @Given("I call the Token API for user {string} and password {string} for authentication")
    public void iCallTheTokenAPIForUserAndPassword(String username, String password) {
        // Headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", ConfigReader.getProperty("basicAuth"));
        // Form Data
        HashMap<String, String> formData = new HashMap<>();
        formData.put("username", username);
        formData.put("password", password);
        formData.put("grant_type", "password");
        response = apiActions.postFormURLEncodedRequest(ConfigReader.getProperty("baseURL"), ApiEndpoints.OAUTH_TOKEN, headers, formData);
    }

    @Then("Verify the Token API response status and presence of a valid bearer token")
    public void theTokenAPIResponseShouldReturnASuccessfulStatusCodeAndContainAValidBearerToken() throws Exception {
        jsonNode = mapper.readTree(response.text());
        Assert.assertEquals(200, response.status());
        Assert.assertTrue("access_token is missing in response", jsonNode.has("access_token") || !jsonNode.get("access_token").isEmpty());
        bearerToken = jsonNode.path("access_token").asText();
    }

    @When("User uses the token to call the GET NPI List API with list ID {string}")
    public void iCallTheGETNPIListAPIToFetchListWithListID(String listId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + bearerToken);
        response = apiActions.getRequestWithoutBody(ConfigReader.getProperty("baseURL"), ApiEndpoints.NPI_LIST_ID + listId, headers);
    }

    @Then("Verify the GET NPI List API response contains the expected NPI block and a successful status code")
    public void theResponseShouldContainTheNPIBlockAndReturnASuccessfulStatusCode() throws Exception {
        jsonNode = mapper.readTree(response.text());
        Assert.assertEquals(200, response.status());
        Assert.assertTrue("NPI key missing in response", jsonNode.has("npis"));
        Assert.assertFalse("'npis' is empty", jsonNode.get("npis").isEmpty());
    }

    @When("User uses the token to call the GET NPI List API with account ID {string}")
    public void userUsesTheTokenToCallTheGETNPIListAPIWithAccountID(String accountID) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + bearerToken);
        response = apiActions.getRequestWithoutBody(ConfigReader.getProperty("baseURL"), ApiEndpoints.NPI_ACCOUNT_ID + accountID, headers);
    }

    @Then("Verify the GET NPI List API response contains the NPI details and a successful status code")
    public void verifyTheGETNPIListAPIResponseContainsTheNPIDetailsAndASuccessfulStatusCode() throws Exception {
        jsonNode = mapper.readTree(response.text());
        Assert.assertEquals(200, response.status());
        Assert.assertFalse("Response is not empty", jsonNode.isEmpty());
    }

    @When("User calls the Create NPI API with account ID {string}, list name {string} and NPIs {string}")
    public void userCallsTheCreateNPIAPIWithAccountIDListNameAndFollowingNPIs(String accountID, String listName, String npis) throws Exception {
        if (!listName.equals("Test_LIST_101") && !listName.isEmpty()) {
            listName = listName + CommonUtils.timeStampCalculation();
        }
        JsonNode fullPayload = mapper.readTree(Files.newBufferedReader(path));
        JsonNode templateNode = fullPayload.path("createNPI");
        ((ObjectNode) templateNode).put("name", listName);
        ArrayNode npiArray = mapper.valueToTree(CommonUtils.parseCommaSeparatedString(npis));
        ((ObjectNode) templateNode).set("npis", npiArray);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + bearerToken);
        String requestBody = templateNode.toString();
        response = apiActions.postRequestWithBody(ConfigReader.getProperty("baseURL"), ApiEndpoints.NPI_ACCOUNT_ID + accountID, headers, requestBody);
    }

    @Then("The API response should have status {string}, errors {string}, and contain the submitted NPI list {string} if applicable")
    public void theAPIResponseShouldHaveStatusErrorsAndContainTheSubmittedNPIListIfApplicable(String statusCode, String errorMessage, String npis) throws Exception {
        jsonNode = mapper.readTree(response.text());
        Assert.assertEquals(Integer.parseInt(statusCode), response.status());
        if (response.status() != 200) {
            List<String> expectedErrorMessages = CommonUtils.parseCommaSeparatedString(errorMessage);
            List<String> actualErrorMessages = new ArrayList<>();
            JsonNode validationErrors = jsonNode.get("validationErrors");
            if (validationErrors != null && validationErrors.isArray()) {
                for (JsonNode error : validationErrors) {
                    actualErrorMessages.add(error.get("errorDescription").asText());
                }
            } else if (jsonNode.has("errorMessage")) {
                actualErrorMessages.add(jsonNode.get("errorMessage").get("errorDescription").asText());
            }
            for (String expectedError : expectedErrorMessages) {
                Assert.assertTrue("Expected error not found: " + expectedError, actualErrorMessages.contains(expectedError));
            }
        }
        if (response.status() == 200) {
            JsonNode returnedNpis = jsonNode.get("npis");
            Assert.assertTrue("Expected 'npis' array in response", returnedNpis != null && returnedNpis.isArray());
            Set<String> submittedNpisSet = new HashSet<>(CommonUtils.parseCommaSeparatedString(npis));
            Set<String> returnedNpisSet = new HashSet<>();
            for (JsonNode npi : returnedNpis) {
                returnedNpisSet.add(npi.asText());
            }
            Assert.assertEquals("Returned NPIs do not match submitted NPIs (after deduplication)", submittedNpisSet, returnedNpisSet);
        }
    }

    @When("User calls the Create NPI API with account ID {string}, list name {string}")
    public void userCallsTheCreateNPIAPIWithAccountIDListName(String accountID, String listName) throws Exception {
        modifiedName = listName + CommonUtils.timeStampCalculation();
        JsonNode fullPayload = mapper.readTree(Files.newBufferedReader(path));
        JsonNode templateNode = fullPayload.path("createNPIWithAttribute");
        ((ObjectNode) templateNode).put("name", modifiedName);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + bearerToken);
        String requestBody = templateNode.toString();
        response = apiActions.postRequestWithBody(ConfigReader.getProperty("baseURL"), ApiEndpoints.NPI_ACCOUNT_ID + accountID + "/attributes", headers, requestBody);
    }

    @Then("Verify the Create NPI API with Attributes API response contains the same list name and a successful status code")
    public void verifyTheCreateNPIAPIWithAttributesAPIResponseContainsTheSameListNameAndASuccessfulStatusCode() throws Exception {
        jsonNode = mapper.readTree(response.text());
        Assert.assertEquals(200, response.status());
        Assert.assertEquals(modifiedName, jsonNode.get("name").asText());
    }

    @And("Add NPIs to the existing NPI list {string} using patch API")
    public void addNPIsToTheExistingNPIListUsingPatchAPI(String listID) throws Exception {
        JsonNode fullPayload = mapper.readTree(Files.newBufferedReader(path));
        JsonNode templateNode = fullPayload.path("addNPIToList");
        ArrayNode npiArray = mapper.createArrayNode();
        for (int i = 0; i < 2; i++) {
            npiArray.add(CommonUtils.generateRandomNumber());
        }
        data = mapper.createArrayNode();
        data.addAll(npiArray);
        System.out.println(data);
        ((ObjectNode) templateNode).set("npis", npiArray);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + bearerToken);
        String requestBody = templateNode.toString();
        response = apiActions.patchRequestWithBody(ConfigReader.getProperty("baseURL"), ApiEndpoints.NPI_LIST_ID + listID, headers, requestBody);
    }

    @And("Verify the API response contains a successful status code")
    public void verifyTheAPIResponseContainsASuccessfulStatusCode() {
        Assert.assertEquals(200, response.status());
    }

    @Then("Verify the NPI block contains the newly added NPIs")
    public void verifyTheNPIBlockContainsTheNewlyAddedNPIs() throws Exception {
        jsonNode = mapper.readTree(response.text());
        JsonNode npisInResponse = jsonNode.path("npis");
        Set<String> responseNpisSet = new HashSet<>();
        for (JsonNode node : npisInResponse) {
            responseNpisSet.add(node.asText());
        }
        for (JsonNode addedNpi : data) {
            String npiValue = addedNpi.asText();
            Assert.assertTrue("Expected NPI not found in response: " + npiValue, responseNpisSet.contains(npiValue));
        }
    }
}
