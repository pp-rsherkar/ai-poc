package utils;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CommonUtils {

    public static String timeStampCalculation(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    public static String randomNumberGeneration(){
        return UUID.randomUUID().toString().substring(0, 10);
    }

    public static List<String> normalize(List<String> list) {
        return list.stream()
                .map(s -> s.replaceAll("\\s+", " ").trim()) // replaces multiple spaces and trims
                .collect(Collectors.toList());
    }

    public static List<String> normalizeObjectList(List<Object> list) {
        return list.stream()
                .map(Object::toString)
                .map(s -> s.replaceAll("\\s+", " ").trim())
                .collect(Collectors.toList());
    }

    public static Map<String, List<String>> processDataTable(Map<String, String> map) {
        Map<String, List<String>> result = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().trim();
            String keyValue = entry.getValue().trim();
            List<String> values = keyValue.contains(",")
                    ? Arrays.stream(keyValue.split(",")).map(String::trim).collect(Collectors.toList())
                    : Collections.singletonList(keyValue);
            result.put(key, values);
        }
        return result;
    }

    public static void selectAndClickElement(Locator locator, List<String> values){
        for (int i = 0; i < locator.count(); i++) {
            String text = locator.nth(i).innerText().trim();
            for (String value : values) {
                if (text.equalsIgnoreCase(value.trim())){
                    locator.nth(i).click();
                    break;
                }
            }
        }
    }

    public static String readJsonTestDataFile(String jsonFileName) throws IOException {
        InputStream is = CommonUtils.class.getClassLoader().getResourceAsStream(jsonFileName);
        if (is == null) {
            throw new IllegalArgumentException("File not found in resources folder: " + jsonFileName);
        }
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static List<String> convertStringToList(String stringData){
        return Arrays.stream(stringData.split(","))
                .map(String::trim)
                .toList();
    }

    public static void uploadFile(Locator fileInput, String fileName) {
        Path filePath = Paths.get("src/main/resources/uploadfiles/" + fileName).toAbsolutePath();
        fileInput.setInputFiles(filePath);
    }

    public static void uploadFileThroughSystemDialog(Page page, String fileName) {
        Locator fileInput = page.locator("input[type='file']").first();
        fileInput.setInputFiles(Paths.get("src/main/resources/uploadfiles/" + fileName));
        ElementHandle fileInputHandle = fileInput.elementHandle();
        page.evaluate("element => element.dispatchEvent(new Event('change', { bubbles: true }))", fileInputHandle);
    }

}
