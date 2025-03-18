package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseActions {
    private static final String DB_URL = WebActions.getProperty("dbURL");
    private static final String DB_USER = WebActions.getProperty("dbUsername");
    private static final String DB_PASSWORD = WebActions.getProperty("dbPassword");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Database Driver not found", e);
        }
    }

    public static String getData(String query, String expectedValue) throws SQLException {
        String actualValue = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, expectedValue);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    actualValue = resultSet.getString(1);
                }
            }
        }
        return actualValue;
    }

//    public static List<String[]> getCSVData(String query, String expectedValue) throws SQLException {
//        List<String[]> actualValue =  new ArrayList<>();;
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, expectedValue);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                while (resultSet.next()) {
//                    actualValue.add(new String[]{resultSet.getString(1)});
//                }
//            }
//        }
//        return actualValue;
//    }  -- working on this function along with CSVActions utility class
}