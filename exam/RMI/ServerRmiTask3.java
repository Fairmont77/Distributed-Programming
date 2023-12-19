package RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ServerRmiTask3 implements ContactServiceRmiTask3 {
    private List<ContactRmiTask3> contacts;

    public ServerRmiTask3() {
        contacts = new ArrayList<>();
    }

    @Override
    public List<ContactRmiTask3> getAllContacts() throws RemoteException {
        return contacts;
    }

    @Override
    public List<ContactRmiTask3> searchContactsByName(String name) throws RemoteException {
        List<ContactRmiTask3> result = new ArrayList<>();
        for (ContactRmiTask3 contact : contacts) {
            if (contact.getName().contains(name)) {
                result.add(contact);
            }
        }
        return result;
    }

    @Override
    public List<ContactRmiTask3> sortContactsByName() throws RemoteException {
        List<ContactRmiTask3> sortedContacts = new ArrayList<>(contacts);
        Collections.sort(sortedContacts, Comparator.comparing(ContactRmiTask3::getName));
        return sortedContacts;
    }

    @Override
    public List<ContactRmiTask3> sortContactsByEmail() throws RemoteException {
        List<ContactRmiTask3> sortedContacts = new ArrayList<>(contacts);
        Collections.sort(sortedContacts, Comparator.comparing(ContactRmiTask3::getName));
        return sortedContacts;
    }

    @Override
    public void addContact(ContactRmiTask3 contact) throws RemoteException {
        contacts.add(contact);
    }

    public static void main(String[] args) {
        try {
            ServerRmiTask3 obj = new ServerRmiTask3();
            ContactServiceRmiTask3 stub = (ContactServiceRmiTask3) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("ContactService", stub);

            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
