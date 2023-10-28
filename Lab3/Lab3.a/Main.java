import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class CustomSemaphore {
    private int permits;
    private int fills;
    private int totalHoney;
    private final ReentrantLock lock;
    private final Condition isFull;

    public CustomSemaphore(int permits) {
        this.permits = permits;
        this.fills = 0;
        this.totalHoney = 0;
        this.lock = new ReentrantLock();
        this.isFull = lock.newCondition();
    }

    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            while (permits == 0) {
                isFull.await();
            }
            permits--;
        } finally {
            lock.unlock();
        }
    }

    public void release() {
        lock.lock();
        try {
            permits++;
            fills++;
            isFull.signal();
        } finally {
            lock.unlock();
        }
    }

    public int getFills() {
        return fills;
    }

    public int getTotalHoney() {
        return totalHoney;
    }

    public void increaseTotalHoney() {
        totalHoney++;
    }

    public void resetFills() {
        fills = 0;
    }

    public void resetTotalHoney() {
        totalHoney = 0;
    }
}

class Bee implements Runnable {
    private final CustomSemaphore pot;
    private final int beeId;

    public Bee(CustomSemaphore pot, int beeId) {
        this.pot = pot;
        this.beeId = beeId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                pot.acquire();
                pot.increaseTotalHoney();
                System.out.println("Bee " + beeId + " adding honey. (Total " + pot.getTotalHoney() + ")");
                pot.release();
                Thread.sleep(1000); // Для імітації збору меду
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Bear implements Runnable {
    private final CustomSemaphore pot;
    private final int capacity;

    public Bear(CustomSemaphore pot, int capacity) {
        this.pot = pot;
        this.capacity = capacity;
    }

    @Override
    public void run() {
        while (true) {
            try {
                pot.acquire();
                if (pot.getFills() >= capacity) {
                    System.out.println("Bear wakes up and eats all the honey.");
                    pot.resetFills();
                    pot.resetTotalHoney();
                }
                pot.release();
                Thread.sleep(2000); // Для імітації споживання меду
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        int n = 10; // Кількість бджіл
        int capacity = 50; // Місткість горщика

        CustomSemaphore pot = new CustomSemaphore(capacity); // Ініціалізація семафора

        Thread bearThread = new Thread(new Bear(pot, capacity));
        bearThread.start();

        for (int i = 1; i <= n; i++) {
            Thread beeThread = new Thread(new Bee(pot, i));
            beeThread.start();
        }
    }
}
