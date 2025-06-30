package utils;

import com.microsoft.playwright.Locator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CommonUtils {

    public static String timeStampCalculation(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    public static List<String> normalize(List<String> list) {
        return list.stream()
                .map(s -> s.replaceAll("\\s+", " ").trim()) // replaces multiple spaces and trims
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


}
