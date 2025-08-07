package utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.playwright.Locator;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExcelActions {

    //Read CSV file and return column values starting from index 1
    public static List<String> readCsvExcludingFirstColumn(String filePath) throws Exception {
        List<String> actualHeaders = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String firstLine = reader.readLine(); // Read the first line (header)
            if (firstLine != null) {
                String[] columns = firstLine.split(",", -1); // Split by comma
                for (int i = 1; i < columns.length; i++) { // Skip the first column
                    String cleaned = columns[i]
                            .trim()
                            .replaceAll("^\"|\"$", "") // Remove leading/trailing quotes
                            .replaceAll("\\s+", "")     // Remove all whitespace
                            .toLowerCase();             // Normalize case
                    actualHeaders.add(cleaned);
                }
            }
        }

        return actualHeaders;
    }


    public static void uploadFile(Locator fileInput, String fileName) {
        Path filePath = Paths.get("src/main/resources/uploadfiles/" + fileName).toAbsolutePath();
        fileInput.setInputFiles(filePath);
    }

    public static int countCsvRecords(String filePath) throws IOException, CsvValidationException {
        int rowCount = 0;
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext();
            while (reader.readNext() != null) {
                rowCount++;
            }
        }
        return rowCount;
    }

}
