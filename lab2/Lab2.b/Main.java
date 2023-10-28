import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    interface Job {
        void doJob(BlockingQueue<Object> in, BlockingQueue<Object> out) throws InterruptedException;
    }

    public static void executePipeline(Job... jobs) {
        BlockingQueue<Object>[] in = new ArrayBlockingQueue[jobs.length];
        for (int i = 0; i < jobs.length; i++) {
            in[i] = new ArrayBlockingQueue<>(100);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(jobs.length);

        for (int i = 0; i < jobs.length; i++) {
            int counter = i;
            executorService.submit(() -> {
                try {
                    if (counter != 0) {
                        jobs[counter].doJob(in[counter - 1], in[counter]);
                    } else {
                        jobs[counter].doJob(in[counter], in[counter]);
                    }
                    in[counter].put("stop");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
        Job storage = (in, out) -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    out.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Job ivanov = (in, out) -> {
            try {
                while (true) {
                    Object val = in.take();
                    Thread.sleep(300);
                    out.put(val);
                    System.out.println("Ivanov took out from storage " + val);
                    if (val.equals("stop")) break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        Job petrov = (in, out) -> {
            try {
                while (true) {
                    Object val = in.take();
                    Thread.sleep(200);
                    out.put(val);
                    System.out.println("Petrov loaded into a truck " + val);
                    if (val.equals("stop")) break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        Job nechiporchuk = (in, out) -> {
            int sum = 0;
            Random random = new Random();
            try {
                while (true) {
                    Object val = in.take();
                    if (val.equals("stop")) {
                        break;
                    }
                    int value = random.nextInt(1000) + 1; // Генеруємо уявну вартість в межах 1-1000
                    sum += value;
                    System.out.println("Nechiporchuk added to the cost of the stolen item " + val + " with a value of " + value + " C.U.");
                }
                System.out.println("Nechiporchuk counted the total cost of the stolen goods: " + sum + " C.U.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Job[] jobs = {storage, ivanov, petrov, nechiporchuk};

        executePipeline(jobs);
    }
}
