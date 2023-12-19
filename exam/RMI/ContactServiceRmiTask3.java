package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ContactServiceRmiTask3 extends Remote {
    List<ContactRmiTask3> getAllContacts() throws RemoteException;
    List<ContactRmiTask3> searchContactsByName(String name) throws RemoteException;
    List<ContactRmiTask3> sortContactsByName() throws RemoteException;
    List<ContactRmiTask3> sortContactsByEmail() throws RemoteException;
    void addContact(ContactRmiTask3 contact) throws RemoteException;
}
