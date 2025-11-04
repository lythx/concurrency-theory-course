package lab2;

import java.util.List;

public class PhilosopherSimulation {

    private final List<Philosopher> philosophers;
    private double averageWaitTimeMilliseconds = -1;
    private static final int SIMULATION_TIME_MILLISECONDS = 10_000;

    public PhilosopherSimulation(List<Philosopher> philosophers) {
        this.philosophers = philosophers;
    }

    public void run() {
        philosophers.forEach(Philosopher::start);
        try {
            Thread.sleep(SIMULATION_TIME_MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("Simulation sleep interrupted: " + e);
        }

        var waitTimes = philosophers.stream().map(Philosopher::getAverageWaitTimeMilliseconds).toList();
        philosophers.forEach(Thread::interrupt);
        averageWaitTimeMilliseconds = philosophers.stream()
            .mapToDouble(Philosopher::getAverageWaitTimeMilliseconds)
            .average()
            .orElse(-1);
    }

    public double getAverageWaitTimeMilliseconds() {
        return averageWaitTimeMilliseconds;
    }
}
