package lab6;

import java.util.Random;

public class Writer extends Thread {
    private final SynchronizedList list;
    private final int MAX_VALUE = 1000;
    private double averageWaitTimeMilliseconds = -1;
    private int waitCount = 0;
    private long startWaitTime = -1;

    public Writer(SynchronizedList list) {
        this.list = list;
    }

    @Override
    public void run() {
        Random rand = new Random();
        while (!Thread.currentThread().isInterrupted()) {
            int action = rand.nextInt(2);
            int value = rand.nextInt(MAX_VALUE);
            startWaitTime = System.nanoTime();
            try {
                if (action == 0) {
                    // Pół na pół: dodawanie i usuwanie
                    list.add(value);
                } else {
                    list.remove(value);
                }
            }
            catch (InterruptedException e) {
                break;
            }
            updateAverageWaitTime();
        }
    }

    public long getAverageWaitTimeMilliseconds() {
        return (long) averageWaitTimeMilliseconds;
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