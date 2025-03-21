package utils;
import com.opencsv.*;
import java.io.*;
import java.util.*;

public class CSVActions {

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


}