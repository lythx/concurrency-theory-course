package lab2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MainLab2 {

    public static void main(String[] args) {
        var nsToTest = List.of(5, 7, 10, 15, 20, 30, 40, 50, 60, 80, 100);

        List<Double> naiveResults = new ArrayList<>();
        List<Double> doubleCheckResults = new ArrayList<>();
        List<Double> asymmetricResults = new ArrayList<>();
        List<Double> arbiterResults = new ArrayList<>();
        for (var n : nsToTest) {
            naiveResults.add(runNaivePhilosopherSimulation(n));
            doubleCheckResults.add(runDoubleCheckPhilosopherSimulation(n));
            asymmetricResults.add(runAsymmetricPhilosopherSimulation(n));
            arbiterResults.add(runArbiterPhilosopherSimulation(n));
        }

        var names = List.of("naive", "double-check", "asymmetric", "arbiter");

        try {
            var file = Paths.get("philosopher-simulation.naive.csv");
            Files.write(file, naiveResults.stream().map(Object::toString).toList());
            file = Paths.get("philosopher-simulation.double-check.csv");
            Files.write(file, naiveResults.stream().map(Object::toString).toList());
            file = Paths.get("philosopher-simulation.double-check.csv");
            Files.write(file, naiveResults.stream().map(Object::toString).toList());
            file = Paths.get("philosopher-simulation.double-check.csv");
            Files.write(file, naiveResults.stream().map(Object::toString).toList());
        }
        catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
    }

    public static double runNaivePhilosopherSimulation(int n) {
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
        return simulation.getAverageWaitTimeMilliseconds();
    }

    public static double runDoubleCheckPhilosopherSimulation(int n) {
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
        return simulation.getAverageWaitTimeMilliseconds();
    }

    public static double runAsymmetricPhilosopherSimulation(int n) {
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
        return simulation.getAverageWaitTimeMilliseconds();
    }

    public static double runArbiterPhilosopherSimulation(int n) {
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
        return simulation.getAverageWaitTimeMilliseconds();
    }

}