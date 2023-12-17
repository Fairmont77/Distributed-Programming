package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class Server {
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        int port = 12345;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Сервер запущено на порту " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.out.println("Помилка обробки клієнта: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка сервера: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            while (true) {
                String commandLine = in.readUTF();
                if ("exit".equalsIgnoreCase(commandLine)) {
                    break;
                }

                String[] command = commandLine.split(" ");
                String response = executeCommand(command);
                out.writeUTF(response);
            }
        } catch (IOException e) {
            System.out.println("Помилка з'єднання з клієнтом: " + e.getMessage());
        }
    }

    private static String executeCommand(String[] command) {
        try {
            DAO.connect();
            switch (command[0]) {
                case "createCity":
                    if (command.length < 5) return "Недостатньо аргументів для команди createCity";
                    DAO.createCity(command[1], Integer.parseInt(command[2]), Integer.parseInt(command[3]), Boolean.parseBoolean(command[4]));
                    return "Місто створено";
                case "deleteCity":
                    if (command.length < 2) return "Недостатньо аргументів для команди deleteCity";
                    DAO.deleteCity(Integer.parseInt(command[1]));
                    return "Місто видалено";
                case "editCity":
                    if (command.length < 5) return "Недостатньо аргументів для команди editCity";
                    DAO.editCity(Integer.parseInt(command[1]), command[2], Integer.parseInt(command[3]), Boolean.parseBoolean(command[4]));
                    return "Місто відредаговано";
                case "getCities":
                    if (command.length < 2) return "Недостатньо аргументів для команди getCities";
                    List<String> cities = DAO.getCities(Integer.parseInt(command[1]));
                    return String.join("\n", cities);
                case "getCountries":
                    List<String> countries = DAO.getCountries();
                    return String.join("\n", countries);
                default:
                    return "Невідома команда";
            }
        } catch (Exception e) {
            return "Помилка: " + e.getMessage();
        } finally {
            try {
                DAO.disconnect();
            } catch (SQLException e) {
                System.out.println("Помилка відключення від бази даних: " + e.getMessage());
            }
        }
    }

}
