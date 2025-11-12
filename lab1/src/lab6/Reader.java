package lab6;

import java.util.Random;

public class Reader extends Thread {
    private final SynchronizedList list;
    private final int MAX_VALUE = 1000;
    private double averageWaitTimeMilliseconds = -1;
    private int waitCount = 0;
    private long startWaitTime = -1;

    public Reader(SynchronizedList list) {
        this.list = list;
    }

    @Override
    public void run() {
        Random rand = new Random();
        while (!Thread.currentThread().isInterrupted()) {
            // Czyta losowy element (z dużym prawdopodobieństwem, że go znajdzie)
            int valueToFind = rand.nextInt(MAX_VALUE / 2); // Wyszukuje wartości z zakresu początkowego
            startWaitTime = System.nanoTime();
            try {
                list.contains(valueToFind);
            } catch (InterruptedException e) {
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