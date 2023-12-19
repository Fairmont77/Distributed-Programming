package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class ClientRmiTask3 {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ContactServiceRmiTask3 contactService = (ContactServiceRmiTask3) registry.lookup("ContactService");

            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("Оберіть опцію:");
                System.out.println("1. Отримати всі контакти");
                System.out.println("2. Пошук за ім'ям");
                System.out.println("3. Сортування за ім'ям");
                System.out.println("4. Сортування за поштою");
                System.out.println("5. Додати новий контакт");
                System.out.println("0. Вийти");

                int option = sc.nextInt();
                sc.nextLine(); // Очистка буфера

                switch (option) {
                    case 1:
                        List<ContactRmiTask3> allContacts = contactService.getAllContacts();
                        for (ContactRmiTask3 contact : allContacts) {
                            System.out.println(contact);
                        }
                        break;
                    case 2:
                        System.out.println("Введіть ім'я для пошуку:");
                        String name = sc.nextLine();
                        List<ContactRmiTask3> contactsByName = contactService.searchContactsByName(name);
                        for (ContactRmiTask3 contact : contactsByName) {
                            System.out.println(contact);
                        }
                        break;
                    case 3:
                        List<ContactRmiTask3> contactsSortedByName = contactService.sortContactsByName();
                        for (ContactRmiTask3 contact : contactsSortedByName) {
                            System.out.println(contact);
                        }
                        break;
                    case 4:
                        List<ContactRmiTask3> contactsSortedBySurname = contactService.sortContactsByEmail();
                        for (ContactRmiTask3 contact : contactsSortedBySurname) {
                            System.out.println(contact);
                        }
                        break;
                    case 5:
                        System.out.println("Введіть ім'я:");
                        String newName = sc.nextLine();
                        System.out.println("Введіть електронну пошту:");
                        String newEmail = sc.nextLine();
                        System.out.println("Введіть номер телефону:");
                        String newTelephone = sc.nextLine();
                        ContactRmiTask3 newContact = new ContactRmiTask3(newName, newEmail, newTelephone);
                        contactService.addContact(newContact);
                        System.out.println("Контакт додано.");
                        break;
                    case 0:
                        System.out.println("Програма завершила роботу.");
                        System.exit(0);
                    default:
                        System.out.println("Невірний вибір опції.");
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
