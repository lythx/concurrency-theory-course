package lab2;

import java.util.Random;

public abstract class Philosopher extends Thread {

    protected final Stick leftStick;
    protected final Stick rightStick;

    private final Random random = new Random();

    private long averageWaitTimeMilliseconds = -1;
    private int waitCount = 0;

    private static final int MIN_EATING_TIME = 1000;
    private static final int MAX_EATING_TIME = 5000;
    private static final int MIN_THINKING_TIME = 1000;
    private static final int MAX_THINKING_TIME = 5000;


    public Philosopher(Stick leftStick, Stick rightStick) {
        this.leftStick = leftStick;
        this.rightStick = rightStick;
    }

    @Override
    public void run() {
        while (true) {
            think();
            var startTime = System.nanoTime();
            takeChopsticks();
            var endTime = System.nanoTime();
            updateAverageWaitTime(startTime, endTime);
            releaseChopsticks();
            eat();
        }
    }

    public long getAverageWaitTimeMilliseconds() {
        return averageWaitTimeMilliseconds;
    }

    protected abstract void takeChopsticks();

    protected abstract void releaseChopsticks();

    protected void think() {
        try {
            Thread.sleep(random.nextInt(MIN_THINKING_TIME, MAX_THINKING_TIME));
        } catch (InterruptedException e) {
            System.out.println("Philosopher interrupted when thinking: " + e);
        }
    }

    protected void eat() {
        try {
            Thread.sleep(random.nextInt(MIN_EATING_TIME, MAX_EATING_TIME));
        } catch (InterruptedException e) {
            System.out.println("Philosopher interrupted when eating: " + e);
        }
    }

    private void updateAverageWaitTime(long startTime, long endTime) {
        var waitTimeMilliseconds = (endTime - startTime) / 1_000_000;
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
