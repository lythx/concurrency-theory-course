package lab1;

public class IncrementThread extends Thread {

    private final Counter counter;
    private final int iterations;

    public IncrementThread(Counter counter, int iterations) {
        this.counter = counter;
        this.iterations = iterations;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            counter.increment();
        }
    }

}
