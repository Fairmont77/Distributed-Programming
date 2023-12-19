package Socket;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerSocketTask3 {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            Socket socket = serverSocket.accept();
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            int n = inputStream.readInt();
            Contact[] contacts = new Contact[n];
            for (int i = 0; i < n; i++) {
                String name = inputStream.readUTF();
                String email = inputStream.readUTF();
                String telephone = inputStream.readUTF();
                contacts[i] = new Contact(name);
                contacts[i].addContactDetail(new ContactDetail("Email", email));
                contacts[i].addContactDetail(new ContactDetail("Telephone", telephone));
            }
            int number = inputStream.readInt();
            int option = inputStream.readInt();

            switch (option) {
                case 1: // Search by name
                    String searchName = inputStream.readUTF();
                    for (int i = 0; i < n; i++) {
                        if (contacts[i].getName().equalsIgnoreCase(searchName)) {
                            outputStream.writeBoolean(true);
                        } else {
                            outputStream.writeBoolean(false);
                        }
                    }
                    break;
                case 2: // Sort by name
                    Arrays.sort(contacts, Comparator.comparing(Contact::getName));
                    for (int i = 0; i < n; i++) {
                        outputStream.writeBoolean(true);
                    }
                    break;
                case 3: // Sort by surname
                    // You can add sorting by surname logic here if needed
                    break;
                default:
                    System.out.println("Invalid option.");
            }

            for (int i = 0; i < n; i++) {
                outputStream.writeBoolean(i < number);
            }

            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
