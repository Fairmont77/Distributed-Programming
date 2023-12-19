package org.example.RMI;

import Data.DAO;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.List;

public class Server extends UnicastRemoteObject implements DatabaseService {

    protected Server() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("DatabaseService", server);
            System.out.println("RMI Сервер запущено");
        } catch (Exception e) {
            System.err.println("Помилка сервера RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String createCity(String name, int countryId, int population, boolean isCapital) throws RemoteException {
        try {
            DAO.connect();
            DAO.createCity(name, countryId, population, isCapital);
            return "Місто створено";
        } catch (Exception e) {
            return "Помилка при створенні міста: " + e.getMessage();
        } finally {
            try {
                DAO.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String deleteCity(int cityId) throws RemoteException {
        try {
            DAO.connect();
            DAO.deleteCity(cityId);
            return "Місто видалено";
        } catch (Exception e) {
            return "Помилка при видаленні міста: " + e.getMessage();
        } finally {
            try {
                DAO.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String editCity(int cityId, String newName, int newPopulation, boolean isCapital) throws RemoteException {
        try {
            DAO.connect();
            DAO.editCity(cityId, newName, newPopulation, isCapital);
            return "Місто відредаговано";
        } catch (Exception e) {
            return "Помилка при редагуванні міста: " + e.getMessage();
        } finally {
            try {
                DAO.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<String> getCities(int countryId) throws RemoteException {
        try {
            DAO.connect();
            return DAO.getCities(countryId);
        } catch (Exception e) {
            throw new RemoteException("Помилка при отриманні списку міст: " + e.getMessage());
        } finally {
            try {
                DAO.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<String> getCountries() throws RemoteException {
        try {
            DAO.connect();
            return DAO.getCountries();
        } catch (Exception e) {
            throw new RemoteException("Помилка при отриманні списку країн: " + e.getMessage());
        } finally {
            try {
                DAO.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
