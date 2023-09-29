package com.apinizer.apitest.db;
import org.h2.tools.Server;

import java.sql.*;

public abstract class H2DatabaseInitializer {

    private static String DB_URL = "jdbc:h2:./mydatabase";

    // Database credentials
    private static String DB_USERNAME = "user";
    private static String DB_PASSWORD = "12345";

    private static Connection connection;

    public static void init() {
        // H2 Veritabanı serverini başlat


        try {
            // Register H2 JDBC driver
            Class.forName("org.h2.Driver");

            // Create a connection to the database
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            createTable();

            // Retrieve and display JSON data from the table
            //retrieveJsonData();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS apinizer_apitest ("
                + "id INT PRIMARY KEY,"
                + "json_data JSON"
                + ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
        }
    }

    public static void insertJsonData(String data) throws SQLException {
        String checkIfExistsSQL = "SELECT COUNT(*) FROM apinizer_apitest WHERE id = 1";

        try (PreparedStatement checkIfExistsStatement = connection.prepareStatement(checkIfExistsSQL);
             ResultSet resultSet = checkIfExistsStatement.executeQuery()) {

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // The record with id = 1 exists; update it
                String updateDataSQL = "UPDATE apinizer_apitest SET json_data = ? WHERE id = 1";

                try (PreparedStatement updateStatement = connection.prepareStatement(updateDataSQL)) {
                    // Set the JSON data as a parameter
                    updateStatement.setObject(1, data);

                    // Execute the update statement
                    updateStatement.executeUpdate();
                }
            } else {
                // The record with id = 1 does not exist; insert a new one
                String insertDataSQL = "INSERT INTO apinizer_apitest (id, json_data) VALUES (1, ?)";

                try (PreparedStatement insertStatement = connection.prepareStatement(insertDataSQL)) {
                    // Set the JSON data as a parameter
                    insertStatement.setObject(1, data);

                    // Execute the insert statement
                    insertStatement.executeUpdate();
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return connection;
    }

    public static String retrieveJsonData() {
        String retrieveDataSQL = "SELECT json_data FROM apinizer_apitest WHERE id=1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(retrieveDataSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                // Retrieve JSON data from the result set
                String data = resultSet.getString("json_data");
                return data;
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return null; // Return null if no JSON data is found
    }

}