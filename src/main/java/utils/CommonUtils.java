package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
}
