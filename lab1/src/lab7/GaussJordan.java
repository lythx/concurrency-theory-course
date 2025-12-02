package lab7;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GaussJordan {

    private final int n;
    private final double[][] matrix;
    private final double[][] multipliers;
    private final double[][] subtractors;
    private final ExecutorService executorService;

    public GaussJordan(double[][] matrix) {
        n = matrix.length - 1;
        this.matrix = matrix;
        this.multipliers = new double[n + 1][n];
        this.subtractors = new double[n + 1][n];

        var threadCount = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    public void run() {

    }



}
