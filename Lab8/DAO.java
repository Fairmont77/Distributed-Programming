package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private static final String URL = "jdbc:mysql://localhost:3306/MAP?serverTimezone=Europe/Moscow&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "SlavA2702";
    private static Connection connection;

    public static void connect() throws SQLException {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static void createCity(String name, int countryId, int population, boolean isCapital) throws SQLException {
        String sql = "INSERT INTO CITIES (ID_CO, NAME, COUNT, ISCAPITAL) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, countryId);
            pstmt.setString(2, name);
            pstmt.setInt(3, population);
            pstmt.setBoolean(4, isCapital);
            pstmt.executeUpdate();
        }
    }

    public static void deleteCity(int cityId) throws SQLException {
        String sql = "DELETE FROM CITIES WHERE ID_CI = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cityId);
            pstmt.executeUpdate();
        }
    }

    public static void editCity(int cityId, String newName, int newPopulation, boolean isCapital) throws SQLException {
        String sql = "UPDATE CITIES SET NAME = ?, COUNT = ?, ISCAPITAL = ? WHERE ID_CI = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newPopulation);
            pstmt.setBoolean(3, isCapital);
            pstmt.setInt(4, cityId);
            pstmt.executeUpdate();
        }
    }

    public static List<String> getCities(int countryId) throws SQLException {
        List<String> cities = new ArrayList<>();
        String sql = "SELECT * FROM CITIES WHERE ID_CO = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, countryId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    cities.add(String.format("ID: %d, Name: %s, Population: %d, Capital: %b",
                            resultSet.getInt("ID_CI"),
                            resultSet.getString("NAME"),
                            resultSet.getInt("COUNT"),
                            resultSet.getBoolean("ISCAPITAL")));
                }
            }
        }
        return cities;
    }

    public static List<String> getCountries() throws SQLException {
        List<String> countries = new ArrayList<>();
        String sql = "SELECT * FROM COUNTRIES";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                countries.add(String.format("ID: %d, Name: %s",
                        resultSet.getInt("ID_CO"),
                        resultSet.getString("NAME")));
            }
        }
        return countries;
    }
}
