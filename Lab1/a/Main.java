import javax.swing.*;

class Slide extends JSlider {
    public Slide() {
        super(0, 100);
    }

    synchronized public void Increase(int increment) {
        setValue((int) getValue() + increment);
    }
}

class MyThread extends Thread {
    private Slide mySlider;
    private int count;
    private static int BOUND = 1000000;
    private static int THREAD_COUNTER = 0;
    private int curNum;
    private int targetPosition;

    public MyThread(Slide mySlider, int increment, int priority, int targetPosition) {
        this.mySlider = mySlider;
        this.targetPosition = targetPosition;
        curNum = ++THREAD_COUNTER;
        setPriority(priority);
    }

    @Override
    public void run() {
        while (!interrupted()) {
            int val = (int) (mySlider.getValue());
            ++count;
            if (count > BOUND) {
                mySlider.Increase((val < targetPosition) ? 1 : -1);
                count = 0;
            }
        }
    }

    public JPanel GetJPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Thread #" + curNum + ", Target = " + targetPosition);
        SpinnerModel sModel = new SpinnerNumberModel(getPriority(), Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1);
        JSpinner spinner = new JSpinner(sModel);
        spinner.addChangeListener(e -> {
            setPriority((int) spinner.getValue());
        });
        panel.add(label);
        panel.add(spinner);
        return panel;
    }

    public int getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(int targetPosition) {
        this.targetPosition = targetPosition;
    }
}

public class Main {
    public static void main(String[] args) {
        JFrame Lab1_DP = new JFrame();
        Lab1_DP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Lab1_DP.setSize(500, 500);
        Slide mySlider = new Slide();

        int initialTargetPosition1 = 10;
        int initialTargetPosition2 = 90;
        MyThread thread1 = new MyThread(mySlider, +1, Thread.NORM_PRIORITY, initialTargetPosition1);
        MyThread thread2 = new MyThread(mySlider, -1, Thread.NORM_PRIORITY, initialTargetPosition2);

        JButton startButton = new JButton("ПУСК");
        startButton.addActionListener(e -> {
            thread1.start();
            thread2.start();
            startButton.setEnabled(false);
        });
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(thread1.GetJPanel());
        panel.add(thread2.GetJPanel());
        panel.add(mySlider);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        panel.add(buttonPanel);

        Lab1_DP.setContentPane(panel);
        Lab1_DP.setVisible(true);
    }
}
