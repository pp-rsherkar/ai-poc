package utils;

import com.microsoft.playwright.Locator;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExcelActions {

    public static void uploadFile(Locator fileInput, String fileName) {
        Path filePath = Paths.get("src/test/resources/uploadfiles/" + fileName).toAbsolutePath();
        fileInput.setInputFiles(filePath);
    }
}
