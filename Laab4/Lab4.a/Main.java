package src1;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static void main(String[] args) {
        Data data = new Data();
        WorkThread workThread1 = new WorkThread(1, data);
        WorkThread workThread2 = new WorkThread(2, data);
        WorkThread workThread3 = new WorkThread(3, data);

        workThread1.setDaemon(true);
        workThread2.setDaemon(true);
        workThread3.setDaemon(true);

        try {
            workThread1.start();
            Thread.sleep(500);
            workThread2.start();
            Thread.sleep(500);
            workThread3.start();

            workThread1.join();
            workThread2.join();
            workThread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Account {
    private String name;
    private String surname;
    private String phoneNumber;
    private String father;

    public Account(String name, String surname, String father, String number) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = number;
        this.father = father;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    @Override
    public String toString() {
        return "Name: " + name + " " + surname + ", Phone number: " + phoneNumber + ", Father: " + father + "\n";
    }
}

class Data {
    public ArrayList<Account> accounts;
    private ReadWriteLock readWriteLock;

    public Data() {
        accounts = startAccountsInit();
        readWriteLock = new ReentrantReadWriteLock();
    }

    private ArrayList<Account> startAccountsInit() {
        ArrayList<Account> acc = new ArrayList<>();
        acc.add(new Account("Dmytro", "Hushcha", "Serhiy", "+380000000000"));
        acc.add(new Account("Karyna", "Dubovets", "Viktor", "+380111111111"));
        acc.add(new Account("Serhyi", "Hushcha", "Mykola", "+380222222222"));
        acc.add(new Account("Iryna", "Hushcha", "Vasil", "+380333333333"));
        acc.add(new Account("Mykola", "Hushcha", "Serhiy", "+380444444444"));
        acc.add(new Account("Stephen", "Hawking", "William", "paradise"));
        return acc;
    }

    public void lockRead() {
        readWriteLock.readLock().lock();
    }

    public void lockWrite() {
        readWriteLock.writeLock().lock();
    }

    public void unlockRead() {
        readWriteLock.readLock().unlock();
    }

    public void unlockWrite() {
        readWriteLock.writeLock().unlock();
    }
}

class WorkThread extends Thread {
    private int task;
    private Data data;

    public WorkThread(int task, Data data) {
        this.task = task;
        this.data = data;
    }

    @Override
    public void run() {
        switch (task) {
            case 1:
                findPhoneByName();
                break;
            case 2:
                findNameByPhone();
                break;
            case 3:
                RWAccounts();
                break;
            default:
                System.out.println("There is no task number like this.");
        }
    }

    private void findPhoneByName() {
        String nameForFind = "Dmytro";
        data.lockRead();
        try {
            System.out.println("Searching phone by name.");
            for (Account account : data.accounts) {
                if (account.getName().equals(nameForFind))
                    System.out.println("Founded by searching phone by name " + account.toString());
            }
            Thread.sleep(5000);
            System.out.println("Searching phone by name is finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            data.unlockRead();
        }
    }

    private void findNameByPhone() {
        String phoneNumberForFind = "+380111111111";
        data.lockRead();
        try {
            System.out.println("Searching name by phone.");
            for (Account account : data.accounts) {
                if (account.getPhoneNumber().equals(phoneNumberForFind))
                    System.out.println("Founded in searching name by phone " + account.toString());
            }
            Thread.sleep(5000);
            System.out.println("Searching name by phone is finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            data.unlockRead();
        }
    }

    private void RWAccounts() {
        Account a1 = new Account("Stephen", "Hawking", "William", "paradise");
        Account a2 = new Account("Stepan", "Bandera", "Andriy", "1909-1959");
        boolean tt = true;

        data.lockWrite();
        try {
            System.out.println("Writing/Deleting accounts.");
            if (tt) data.accounts.add(a1);
            else data.accounts.remove(a1);
            Thread.sleep(5000);
            System.out.println("Writing/Deleting accounts is finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            data.unlockWrite();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        data.lockWrite();
        try {
            System.out.println("Writing/Deleting accounts.");
            if (tt) data.accounts.add(a2);
            else data.accounts.remove(a2);
            Thread.sleep(5000);
            System.out.println("Writing/Deleting accounts is finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            data.unlockWrite();
        }
    }
}
