package poohSearch;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BeeSearchMain {
    private int[][] forest;
    private final AtomicBoolean found;
    private final AtomicInteger currentRow;
    private final int forestSize;
    private final int threadCount;
    private Thread[] threads;

    private class Bees extends Thread {
        public Bees() {
        }

        public void run() {
            while (!found.get() && currentRow.get() < forestSize) {
                checkRow(currentRow.get());
                currentRow.set(currentRow.get() + 1);
            }
        }
    }

    public BeeSearchMain(int forestSize) {
        this.forestSize = forestSize;
        this.threadCount = (int) Math.sqrt(forestSize);
        this.threads = new Thread[threadCount];
        forest = new int[forestSize][forestSize];
        initializeForest();
        found = new AtomicBoolean(false);
        currentRow = new AtomicInteger(0);
    }

    private void initializeForest() {
        for (int i = 0; i < forestSize; i++) {
            for (int j = 0; j < forestSize; j++) {
                forest[i][j] = 0;
            }
        }
        int column = (int) (Math.random() * forestSize);
        int row = (int) (Math.random() * forestSize);
        System.out.println("Pooh is in row: " + row + " column: " + column);
        forest[row][column] = 1;
    }

    private void checkAllForest() {
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Bees();
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkRow(int row) {
        if (found.get()) {
            return;
        }
        System.out.println(Thread.currentThread().getName() + " group of bees in row: " + row);
        for (int i = 0; i < forestSize; i++) {
            if (forest[row][i] == 1) {
                System.out.println(Thread.currentThread().getName() + " pooh was found in row: " + row);
                found.set(true);
                break;
            }
        }
    }

    public static void main(String[] args) {
        BeeSearchMain beesFindingWinniePooh = new BeeSearchMain(100);
        beesFindingWinniePooh.checkAllForest();
    }
}
