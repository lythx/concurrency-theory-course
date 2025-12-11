package lab7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GaussJordan {

    private final int size;
    private final double[][] matrix;
    private final double[] multipliers;
    private final double[][] valuesToSubtract;
    private final ExecutorService executorService;

    public GaussJordan(double[][] matrix) {
        size = matrix.length;
        this.matrix = matrix;
        this.multipliers = new double[size];
        this.valuesToSubtract = new double[size][size + 1];

        var threadCount = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    public void run() {
        try {
            List<Future<?>> results = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                for (int k = i + 1; k < size; k++) {
                    results.add(executorService.submit(new TaskA(i, k, matrix, multipliers)));
                }
                for (var result : results) {
                    result.get();
                }
                results.clear();


                for (int k = i + 1; k < size; k++) {
                    for (int j = i; j < size + 1; j++) {
                        results.add(executorService.submit(new TaskB(i, j, k, matrix, multipliers, valuesToSubtract)));
                    }
                }
                for (var result : results) {
                    result.get();
                }
                results.clear();

                for (int k = i + 1; k < size; k++) {
                    for (int j = i; j < size + 1; j++) {
                        results.add(executorService.submit(new TaskC(j, k, matrix, valuesToSubtract)));
                    }
                }
                for (var result : results) {
                    result.get();
                }
                results.clear();
            }
        } catch (ExecutionException | InterruptedException ex) {
            System.out.println("GaussJordan error: " + ex.getMessage());
        }
        finally {
            executorService.shutdown();
        }
    }

}
