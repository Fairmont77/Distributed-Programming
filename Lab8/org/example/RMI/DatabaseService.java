package org.example.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DatabaseService extends Remote {
    String createCity(String name, int countryId, int population, boolean isCapital) throws RemoteException;
    String deleteCity(int cityId) throws RemoteException;
    String editCity(int cityId, String newName, int newPopulation, boolean isCapital) throws RemoteException;
    List<String> getCities(int countryId) throws RemoteException;
    List<String> getCountries() throws RemoteException;
}
