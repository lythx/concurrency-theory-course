package lab7;

import java.util.concurrent.Callable;

public class MatrixTaskB implements Callable<Double> {

    private final int i;
    private final int j;
    private final int k;
    private final double[][] matrix;
    private final double[][] multipliers;

    public MatrixTaskB(int i, int j, int k, double[][] matrix, double[][] multipliers) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.matrix = matrix;
        this.multipliers = multipliers;
    }

    @Override
    public Double call() {
        return matrix[i][j] * multipliers[k][i];
    }

}
