package org.example.RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            DatabaseService service = (DatabaseService) registry.lookup("DatabaseService");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Введіть команду (або 'exit' для виходу):");

            while (true) {
                String input = scanner.nextLine();
                if ("exit".equalsIgnoreCase(input)) {
                    break;
                }

                String[] parts = input.split(" ");
                if (parts.length == 0) {
                    continue;
                }

                String command = parts[0];
                try {
                    switch (command) {
                        case "createCity":
                            if (parts.length < 5) {
                                System.out.println("Недостатньо аргументів для команди createCity");
                                break;
                            }
                            System.out.println(service.createCity(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Boolean.parseBoolean(parts[4])));
                            break;
                        case "deleteCity":
                            if (parts.length < 2) {
                                System.out.println("Недостатньо аргументів для команди deleteCity");
                                break;
                            }
                            System.out.println(service.deleteCity(Integer.parseInt(parts[1])));
                            break;
                        case "editCity":
                            if (parts.length < 5) {
                                System.out.println("Недостатньо аргументів для команди editCity");
                                break;
                            }
                            System.out.println(service.editCity(Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]), Boolean.parseBoolean(parts[4])));
                            break;
                        case "getCities":
                            if (parts.length < 2) {
                                System.out.println("Недостатньо аргументів для команди getCities");
                                break;
                            }
                            service.getCities(Integer.parseInt(parts[1])).forEach(System.out::println);
                            break;
                        case "getCountries":
                            service.getCountries().forEach(System.out::println);
                            break;
                        default:
                            System.out.println("Невідома команда");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Помилка при виконанні команди: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Помилка з'єднання з RMI сервером: " + e.getMessage());
        }
    }
}
