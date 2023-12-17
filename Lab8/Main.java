package org.example;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            DAO.connect();
            Scanner scanner = new Scanner(System.in);
            String str = "";
            while (!str.equals("exit")) {
                System.out.println("Enter command:");
                str = scanner.nextLine();
                String[] command = str.split(" ");
                try {
                    switch (command[0]) {
                        case "createCity":
                            // Expected format: createCity cityName countryId population isCapital
                            DAO.createCity(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]), Boolean.parseBoolean(command[4]));
                            break;
                        case "deleteCity":
                            // Expected format: deleteCity cityId
                            DAO.deleteCity(Integer.parseInt(command[1]));
                            break;
                        case "editCity":
                            // Expected format: editCity cityId newName newPopulation isCapital
                            DAO.editCity(Integer.parseInt(command[1]), command[2], Integer.parseInt(command[3]), Boolean.parseBoolean(command[4]));
                            break;
                        case "getCities":
                            // Expected format: getCities countryId
                            List<String> cities = DAO.getCities(Integer.parseInt(command[1]));
                            cities.forEach(System.out::println);
                            break;
                        case "getCountries":
                            // No additional arguments expected
                            List<String> countries = DAO.getCountries();
                            countries.forEach(System.out::println);
                            break;
                        // Add more cases for other operations
                        default:
                            System.out.println("Unknown command");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error processing command: " + e.getMessage());
                }
            }
            DAO.disconnect();
        } catch (SQLException e) {
            System.out.println("Error connecting to or disconnecting from the database: " + e.getMessage());
        }
    }
}
