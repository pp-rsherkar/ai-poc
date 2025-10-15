package utils;
import com.opencsv.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class FileActions {

    public static List<String[]> readAllDataAtOnce(String file) {
        List<String[]> allData=null;
        try {
            FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReader(filereader);
            allData = csvReader.readAll();
            /*for (String[] row : allData) {
                System.out.println(String.join(", ", row));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allData;
    }

    public static int fetchColumnCountFromCSV(Path filePath, String columnName) throws IOException {
        int rowCount = 0;
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String header = br.readLine();
            if (header == null) return 0;
            String[] headers = header.split(",");
            int index = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase(columnName)) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                throw new IllegalStateException("Column '" + columnName + "' not found in " + filePath.getFileName());
            }
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length > index && !columns[index].trim().isEmpty()) {
                    rowCount++;
                }
            }
        }
        return rowCount;
    }


    public static int fetchColumnCountFromExcel(Path filePath, String columnName) throws IOException {
        try (Workbook wb = new XSSFWorkbook(Files.newInputStream(filePath))) {
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) return 0;

            Row header = sheet.getRow(0);
            if (header == null) return 0;

            int colIndex = -1;
            for (Cell c : header)
                if (c.getStringCellValue().trim().equalsIgnoreCase(columnName))
                    colIndex = c.getColumnIndex();

            if (colIndex == -1)
                throw new IllegalStateException("Column '" + columnName + "' not found in " + filePath.getFileName());

            int count = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Cell cell = sheet.getRow(i) != null ? sheet.getRow(i).getCell(colIndex) : null;
                if (cell != null && !cell.toString().trim().isEmpty()) count++;
            }
            return count;
        }
    }
}