package lab4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainLab4 {

    public static void main(String[] args) throws IOException {
        var nsToTest = List.of(5, 7, 10, 20, 30, 40, 60, 100, 200, 300);

        List<List<Double>> naiveResults = new ArrayList<>();
        List<List<Double>> doubleCheckResults = new ArrayList<>();
        List<List<Double>> asymmetricResults = new ArrayList<>();
        List<List<Double>> arbiterResults = new ArrayList<>();
        for (var n : nsToTest) {
            naiveResults.add(runNaivePhilosopherSimulation(n));
            doubleCheckResults.add(runDoubleCheckPhilosopherSimulation(n));
            asymmetricResults.add(runAsymmetricPhilosopherSimulation(n));
            arbiterResults.add(runArbiterPhilosopherSimulation(n));
        }

        var names = List.of("naive", "double-check", "asymmetric", "arbiter");
        var results = List.of(naiveResults, doubleCheckResults, asymmetricResults, arbiterResults);

        for (int i = 0; i < names.size(); i++) {
            var name = names.get(i);
            var result = results.get(i);
            var file = Paths.get(String.format("data/philosopher-simulation.%s.csv", name));
            Files.write(
                file,
                result.stream()
                    .map(list ->
                        list.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")))
                    .toList());
        }
    }

    public static List<Double> runNaivePhilosopherSimulation(int n) {
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
        return simulation.getAverageWaitTimesMilliseconds();
    }

    public static List<Double> runDoubleCheckPhilosopherSimulation(int n) {
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
        return simulation.getAverageWaitTimesMilliseconds();
    }

    public static List<Double> runAsymmetricPhilosopherSimulation(int n) {
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
        return simulation.getAverageWaitTimesMilliseconds();
    }

    public static List<Double> runArbiterPhilosopherSimulation(int n) {
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
        return simulation.getAverageWaitTimesMilliseconds();
    }

}