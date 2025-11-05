package lab4;

import java.util.Random;

public abstract class Philosopher extends Thread {

    protected final Stick leftStick;
    protected final Stick rightStick;

    private final Random random = new Random();

    private double averageWaitTimeMilliseconds = -1;
    private int waitCount = 0;
    private long startWaitTime = -1;
    private boolean isStopped = false;

    private static final int MIN_EATING_TIME = 10;
    private static final int MAX_EATING_TIME = 40;
    private static final int MIN_THINKING_TIME = 10;
    private static final int MAX_THINKING_TIME = 40;


    public Philosopher(Stick leftStick, Stick rightStick) {
        this.leftStick = leftStick;
        this.rightStick = rightStick;
    }

    @Override
    public void run() {
        try {
            while (!isStopped) {
                think();
                startWaitTime = System.nanoTime();
                takeChopsticks();
                updateAverageWaitTime();
                eat();
                releaseChopsticks();
            }
        } catch (InterruptedException e) {
            System.out.println("Philosopher interrupted");
        }
    }

    public void stopPhilosopher() {
        isStopped = true;
    }

    public double getAverageWaitTimeMilliseconds() {
        return averageWaitTimeMilliseconds;
    }

    protected abstract void takeChopsticks() throws InterruptedException;

    protected void releaseChopsticks() {
        leftStick.release();
        rightStick.release();
    }

    private void think() throws InterruptedException {
        Thread.sleep(random.nextInt(MIN_THINKING_TIME, MAX_THINKING_TIME));
    }

    private void eat() throws InterruptedException {
        Thread.sleep(random.nextInt(MIN_EATING_TIME, MAX_EATING_TIME));
    }

    private void updateAverageWaitTime() {
        var waitTimeMilliseconds = (System.nanoTime() - startWaitTime) / 1_000_000;
        if (waitCount == 0) {
            averageWaitTimeMilliseconds = waitTimeMilliseconds;
            waitCount++;
        } else {
            var totalWaitTime = averageWaitTimeMilliseconds * waitCount + waitTimeMilliseconds;
            waitCount++;
            averageWaitTimeMilliseconds = totalWaitTime / waitCount;
        }
    }

}
