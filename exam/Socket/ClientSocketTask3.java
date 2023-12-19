package Socket;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientSocketTask3 {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the number of contacts: ");
            int n = sc.nextInt();
            dos.writeInt(n);
            Contact[] contacts = new Contact[n];
            for (int i = 0; i < n; i++) {
                System.out.println("Enter the name: ");
                String name = sc.next();
                dos.writeUTF(name);
                System.out.println("Enter the email: ");
                String email = sc.next();
                dos.writeUTF(email);
                System.out.println("Enter the telephone: ");
                String telephone = sc.next();
                dos.writeUTF(telephone);
                contacts[i] = new Contact(name);
                contacts[i].addContactDetail(new ContactDetail("Email", email));
                contacts[i].addContactDetail(new ContactDetail("Telephone", telephone));
            }
            System.out.println("Enter the number of contacts to select: ");
            int number = sc.nextInt();
            dos.writeInt(number);

            // Choose search or sorting option
            System.out.println("Choose an option:\n1. Search by name\n2. Sort by name");
            int option = sc.nextInt();
            dos.writeInt(option);

            switch (option) {
                case 1:
                    System.out.println("Enter the name to search for: ");
                    String searchName = sc.next();
                    dos.writeUTF(searchName);
                    System.out.println("Search results:");
                    for (int i = 0; i < n; i++) {
                        if (dis.readBoolean()) {
                            System.out.println(contacts[i]);
                        }
                    }
                    break;
                case 2:
                    System.out.println("Sorting by name:");
                    for (int i = 0; i < n; i++) {
                        if (dis.readBoolean()) {
                            System.out.println(contacts[i]);
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid option.");
            }

            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

class Contact {
    private String name;
    private List<ContactDetail> contactDetails;

    public Contact(String name) {
        this.name = name;
        this.contactDetails = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ContactDetail> getContactDetails() {
        return contactDetails;
    }

    public void addContactDetail(ContactDetail contactDetail) {
        contactDetails.add(contactDetail);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", contactDetails=" + contactDetails +
                '}';
    }
}

class ContactDetail {
    private String type;
    private String value;

    public ContactDetail(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + ": " + value;
    }
}
