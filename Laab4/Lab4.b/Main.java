import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static void main(String[] args) {
        String[][] garden = new String[5][5];
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Lock readLock = lock.readLock();
        Lock writeLock = lock.writeLock();

        for (int j = 0; j < 5; j++) {
            String[] row = new String[5];
            for (int i = 0; i < 5; i++) {
                row[i] = "0";
            }
            garden[j] = row;
        }

        Thread monitor1 = new Thread(() -> {
            try {
                FileWriter file = new FileWriter("wb.txt");
                while (true) {
                    readLock.lock();
                    try {
                        for (String[] strings : garden) {
                            String line = String.join("", strings);
                            file.write(line + "\n");
                        }
                    } finally {
                        readLock.unlock();
                    }
                    file.write("\n\n\n");
                    file.flush();
                    Thread.sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread monitor2 = new Thread(() -> {
            while (true) {
                readLock.lock();
                try {
                    for (String[] strings : garden) {
                        for (String s : strings) {
                            System.out.print(s);
                        }
                        System.out.println();
                    }
                } finally {
                    readLock.unlock();
                }
                System.out.println();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread nature = new Thread(() -> {
            Random random = new Random();
            while (true) {
                writeLock.lock();
                try {
                    for (int i = 0; i < 2; i++) {
                        garden[random.nextInt(5)][random.nextInt(5)] = "1";
                    }
                } finally {
                    writeLock.unlock();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread gardener = new Thread(() -> {
            while (true) {
                writeLock.lock();
                try {
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (garden[i][j].equals("1")) {
                                garden[i][j] = "0";
                            }
                        }
                    }
                } finally {
                    writeLock.unlock();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        monitor1.start();
        monitor2.start();
        nature.start();
        gardener.start();
    }
}
