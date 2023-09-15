import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Slide extends JSlider {
    public Slide() {
        super(0, 100);
    }

    synchronized public void increase(int increment) {
        setValue((int) getValue() + increment);
    }
}

class MyThread extends Thread {
    private Slide mySlider;
    private int count;
    private static final int BOUND = 1000000;
    private int targetPosition;

    public MyThread(Slide mySlider, int increment, int priority, int targetPosition) {
        this.mySlider = mySlider;
        this.targetPosition = targetPosition;
        setPriority(priority);
    }

    @Override
    public void run() {
        while (!interrupted()) {
            int val = (int) mySlider.getValue();
            ++count;
            if (count > BOUND) {
                mySlider.increase((val < targetPosition) ? 1 : -1);
                count = 0;
            }
        }
    }
}

public class Main {
    private static volatile int SEMAPHORE = 0;
    private static Slide mySlider;
    private static MyThread thread1;
    private static MyThread thread2;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        mySlider = new Slide();
        panel.add(mySlider);

        JButton startButton1 = new JButton("ПУСК 1");
        JButton startButton2 = new JButton("ПУСК 2");
        JButton stopButton1 = new JButton("СТОП 1");
        JButton stopButton2 = new JButton("СТОП 2");

        startButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SEMAPHORE == 0) {
                    SEMAPHORE = 1;
                    thread1 = new MyThread(mySlider, 1, Thread.MIN_PRIORITY, 10);
                    thread1.start();
                    startButton1.setEnabled(false);
                    stopButton2.setEnabled(false);
                }
            }
        });

        startButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SEMAPHORE == 0) {
                    SEMAPHORE = 1;
                    thread2 = new MyThread(mySlider, -1, Thread.MAX_PRIORITY, 90);
                    thread2.start();
                    startButton2.setEnabled(false);
                    stopButton1.setEnabled(false);
                }
            }
        });

        stopButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SEMAPHORE = 0;
                thread1.interrupt();
                startButton1.setEnabled(true);
                stopButton2.setEnabled(true);
            }
        });

        stopButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SEMAPHORE = 0;
                thread2.interrupt();
                startButton2.setEnabled(true);
                stopButton1.setEnabled(true);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton1);
        buttonPanel.add(stopButton1);
        buttonPanel.add(startButton2);
        buttonPanel.add(stopButton2);
        panel.add(buttonPanel);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
