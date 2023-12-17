package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name of the database: ");
        String dbName = in.nextLine();
        System.out.println("Enter the name of the table: ");
        String tableName = in.nextLine();
        System.out.println("Enter the name of the user: ");
        String userName = in.nextLine();
        System.out.println("Enter the password: ");
        String password = in.nextLine();
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, userName, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(resultSetMetaData.getColumnName(i) + " ");
        }
        System.out.println();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + " ");
            }
            System.out.println();
        }
        resultSet.close();
        statement.close();
        connection.close();
    }
}
