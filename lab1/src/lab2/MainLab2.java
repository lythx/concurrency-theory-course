package lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MainLab2 {

    public static void main(String[] args) {
        int n = 500;
        runNaivePhilosopherSimulation(n);
        runDoubleCheckPhilosopherSimulation(n);
        runAsymmetricPhilosopherSimulation(n);
        runArbiterPhilosopherSimulation(n);
    }

    public static void runNaivePhilosopherSimulation(int n) {
        List<Stick> sticks = IntStream.range(0, n)
            .mapToObj(i -> new Stick())
            .toList();
        List<Philosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            var leftStick = sticks.get(((i - 1) + n) % n);
            var rightStick = sticks.get((i + 1) % n);
            philosophers.add(new NaivePhilosopher(leftStick, rightStick));
        }
        var simulation = new PhilosopherSimulation(philosophers);
        simulation.run();
    }

    public static void runDoubleCheckPhilosopherSimulation(int n) {
        List<Stick> sticks = IntStream.range(0, n)
            .mapToObj(i -> new Stick())
            .toList();
        List<Philosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            var leftStick = sticks.get(((i - 1) + n) % n);
            var rightStick = sticks.get((i + 1) % n);
            philosophers.add(new DoubleCheckPhilosopher(leftStick, rightStick));
        }
        var simulation = new PhilosopherSimulation(philosophers);
        simulation.run();
    }

    public static void runAsymmetricPhilosopherSimulation(int n) {
        List<Stick> sticks = IntStream.range(0, n)
            .mapToObj(i -> new Stick())
            .toList();
        List<Philosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            var leftStick = sticks.get(((i - 1) + n) % n);
            var rightStick = sticks.get((i + 1) % n);
            philosophers.add(new AsymmetricPhilosopher(leftStick, rightStick, i));
        }
        var simulation = new PhilosopherSimulation(philosophers);
        simulation.run();
    }

    public static void runArbiterPhilosopherSimulation(int n) {
        List<Stick> sticks = IntStream.range(0, n)
            .mapToObj(i -> new Stick())
            .toList();
        var arbiter = new Arbiter(n);
        List<Philosopher> philosophers = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            var leftStick = sticks.get(((i - 1) + n) % n);
            var rightStick = sticks.get((i + 1) % n);
            philosophers.add(new ArbiterPhilosopher(leftStick, rightStick, arbiter));
        }
        var simulation = new PhilosopherSimulation(philosophers);
        simulation.run();
    }

}