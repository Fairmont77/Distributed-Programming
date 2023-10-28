import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    private final AtomicBoolean isWorkFinished;
    private final AtomicBoolean isBarberBusy;
    private final Object barberMonitor;
    private final Object queueMonitor;
    private final Queue<Client> queue;

    Main() {
        barberMonitor = new Object();
        queueMonitor = new Object();
        isWorkFinished = new AtomicBoolean(false);
        isBarberBusy = new AtomicBoolean(false);
        queue = new ConcurrentLinkedQueue<>();
    }

    private class Barber implements Runnable {
        @Override
        public void run() {
            synchronized (barberMonitor) {
                System.out.println(Thread.currentThread().getName() + ": day is started");

                while (!isWorkFinished.get() || !queue.isEmpty()) {
                    if (queue.isEmpty()) {
                        try {
                            System.out.println(Thread.currentThread().getName() + ": barber falling asleep");
                            isBarberBusy.set(false);
                            barberMonitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Client client = queue.remove();
                        System.out.println(Thread.currentThread().getName() + ": getting next client " + client.getClientName());
                        client.startShaving();
                    }
                }
                System.out.println(Thread.currentThread().getName() + ": day is finished");
            }
        }
    }

    private class Client extends Thread {
        private boolean isShaved;
        private final String clientName;

        Client(String name) {
            isShaved = false;
            this.clientName = name;
        }

        public String getClientName() {
            return clientName;
        }

        @Override
        public void run() {
            synchronized (queueMonitor) {
                System.out.println(Thread.currentThread().getName() + ": " + clientName + " now in the queue");
                while (!isShaved) {
                    if (!isBarberBusy.get()) {
                        synchronized (barberMonitor) {
                            barberMonitor.notifyAll();
                            isBarberBusy.set(true);
                            System.out.println(Thread.currentThread().getName() + ": " + clientName + " awaking barber");
                        }
                    } else {
                        try {
                            System.out.println(Thread.currentThread().getName() + ": " + clientName + " falling asleep");
                            queueMonitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public void startShaving() {
            System.out.println(Thread.currentThread().getName() + ": " + clientName + " shaving started");
            try {
                Random random = new Random();
                Thread.sleep(random.nextInt(3000) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isShaved = true;
            synchronized (queueMonitor) {
                queueMonitor.notifyAll();
            }
            System.out.println(Thread.currentThread().getName() + ": " + clientName + " shaving finished");
        }
    }

    public void barbershopWork() {
        Thread barber = new Thread(new Barber());
        barber.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 3; i++) {
            Client client = new Client("Client" + (i + 1));
            queue.add(client);
            System.out.println(Thread.currentThread().getName() + ": " + client.getClientName() + " comes to barbershop");
            client.start();
        }
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Client client = new Client("Client" + (4));
        queue.add(client);
        System.out.println(Thread.currentThread().getName() + ": " + client.getClientName() + " comes to barbershop");

        client.start();

        isWorkFinished.set(true);
    }

    public static void main(String[] args) {
        Main bArBeRsHoP = new Main();
        bArBeRsHoP.barbershopWork();
    }
}
