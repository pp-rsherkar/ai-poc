package api;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import factory.DriverFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ApiActions {

    private final Playwright playwright = DriverFactory.createPlaywright();
    APIResponse response;
    private APIRequestContext request;

    public APIResponse getRequestWithoutBody(String baseURL, String endpointPath, HashMap<String, String> headers) {
        request = playwright.request().newContext(new APIRequest.NewContextOptions().setBaseURL(baseURL).setExtraHTTPHeaders(headers));
        response = request.get(endpointPath);
        return response;
    }

    public APIResponse postRequestWithoutBody(String baseURL, String endpointPath, HashMap<String, String> headers) {
        request = playwright.request().newContext(new APIRequest.NewContextOptions().setBaseURL(baseURL).setExtraHTTPHeaders(headers));
        response = request.post(endpointPath);
        return response;
    }

    public APIResponse postFormURLEncodedRequest(String baseURL, String endpointPath, HashMap<String, String> headers, HashMap<String, String> formData) {
        request = playwright.request().newContext(new APIRequest.NewContextOptions().setBaseURL(baseURL).setExtraHTTPHeaders(headers));
        StringBuilder formBody = new StringBuilder();
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            if (!formBody.isEmpty()) formBody.append("&");
            String encodedKey = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
            String encodedValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            formBody.append(encodedKey).append("=").append(encodedValue);
        }
        return request.post(endpointPath, RequestOptions.create().setHeader("Content-Type", "application/x-www-form-urlencoded").setData(formBody.toString()));
    }

    public APIResponse postRequestWithBody(String baseURL, String endpointPath, HashMap<String, String> headers, String requestBody) {
        request = playwright.request().newContext(new APIRequest.NewContextOptions().setBaseURL(baseURL).setExtraHTTPHeaders(headers));
        return request.post(endpointPath, RequestOptions.create().setHeader("Content-Type", "application/json").setData(requestBody));
    }

    public APIResponse patchRequestWithBody(String baseURL, String endpointPath, HashMap<String, String> headers, String requestBody) {
        request = playwright.request().newContext(new APIRequest.NewContextOptions().setBaseURL(baseURL).setExtraHTTPHeaders(headers));
        return request.patch(endpointPath, RequestOptions.create().setHeader("Content-Type", "application/json").setData(requestBody));
    }
}