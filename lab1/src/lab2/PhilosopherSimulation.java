package lab2;

import java.util.List;

public class PhilosopherSimulation {

    private final List<Philosopher> philosophers;

    public PhilosopherSimulation(List<Philosopher> philosophers) {
        this.philosophers = philosophers;
    }

    public void run() {
        philosophers.forEach(Philosopher::start);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            System.out.println("Simulation sleep interrupted: " + e);
        }

        var waitTimes = philosophers.stream().map(Philosopher::getAverageWaitTimeMilliseconds).toList();
        System.out.println(waitTimes);
        philosophers.forEach(Thread::interrupt);

    }

}
