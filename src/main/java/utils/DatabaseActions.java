package utils;

import java.sql.*;
import java.util.*;

public class DatabaseActions {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(ConfigLoader.getDbURL(), ConfigLoader.getDbUsername(), ConfigLoader.getDbPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Database Driver not found", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public static List<Map<String, String>> getDataAsMap(String query, String expectedValue) throws SQLException {
        List<Map<String, String>> actualValue = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, expectedValue);
            try (ResultSet resultSet = statement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (resultSet.next()) {
                    Map<String, String> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), resultSet.getString(i));
                    }
                    actualValue.add(row);
                }
            }
        }
        return actualValue;
    }

    public static List<Object> getDataAsList(String query, String expectedValue) {
        List<Object> resultList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, expectedValue);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    resultList.add(metaData.getColumnName(i));
                }
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        resultList.add(resultSet.getObject(i));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}