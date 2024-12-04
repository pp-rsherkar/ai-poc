package utils;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import factory.DriverFactory;

import java.util.HashMap;

public class ApiActions {

    private final Playwright playwright = DriverFactory.createPlaywright();
    APIResponse response;
    private APIRequestContext request;

    public APIResponse getResponse(HashMap<String, String> headers) {
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                // All requests we send go to this API endpoint.
                .setBaseURL("https://api.github.com")
                .setExtraHTTPHeaders(headers));
        response = request.post("");
        return response;
    }

    public APIResponse postResponse(HashMap<String, String> headers) {
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                // All requests we send go to this API endpoint.
                .setBaseURL("https://api.github.com")
                .setExtraHTTPHeaders(headers));
        response = request.post("");
        return response;
    }
}
