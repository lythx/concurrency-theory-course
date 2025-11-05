package lab4;

import java.util.List;

public class PhilosopherSimulation {

    private final List<Philosopher> philosophers;
    private List<Double> averageWaitTimesMilliseconds;
    private static final int SIMULATION_TIME_MILLISECONDS = 20_000;

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

        philosophers.forEach(Philosopher::stopPhilosopher);

        for (var philosopher : philosophers) {
            try {
                philosopher.join();
            }
            catch (InterruptedException e) {
                System.out.println("Join interrupted: " + e);
            }
        }

        averageWaitTimesMilliseconds = philosophers.stream()
            .map(Philosopher::getAverageWaitTimeMilliseconds)
            .toList();
    }

    public List<Double> getAverageWaitTimesMilliseconds() {
        return averageWaitTimesMilliseconds;
    }

}
