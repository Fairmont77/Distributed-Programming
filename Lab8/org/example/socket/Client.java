package org.example.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;
        try (Socket socket = new Socket(host, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Введіть команду (або 'exit' для виходу):");

            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                out.writeUTF(input);
                String response = in.readUTF();
                System.out.println("Відповідь сервера: " + response);
            }

        } catch (IOException e) {
            System.out.println("Помилка з'єднання з сервером: " + e.getMessage());
        }
    }
}
