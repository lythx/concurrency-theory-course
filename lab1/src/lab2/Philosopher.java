package lab2;

import java.util.Random;

public abstract class Philosopher extends Thread {

    protected final Stick leftStick;
    protected final Stick rightStick;

    private final Random random = new Random();

    private long averageWaitTimeMilliseconds = -1;
    private int waitCount = 0;
    private long startWaitTime = -1;

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
        while (true) {
            think();
            startWaitTime = System.nanoTime();
            try {
                takeChopsticks();
            } catch (InterruptedException e) {
                updateAverageWaitTime();
                break;
            }
            updateAverageWaitTime();
            eat();
            releaseChopsticks();
        }
    }

    public long getAverageWaitTimeMilliseconds() {
        return averageWaitTimeMilliseconds;
    }

    protected abstract void takeChopsticks() throws InterruptedException;

    protected abstract void releaseChopsticks();

    protected void think() {
        try {
            Thread.sleep(random.nextInt(MIN_THINKING_TIME, MAX_THINKING_TIME));
        } catch (InterruptedException ignored) {}
    }

    protected void eat() {
        try {
            Thread.sleep(random.nextInt(MIN_EATING_TIME, MAX_EATING_TIME));
        } catch (InterruptedException ignored) {}
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
