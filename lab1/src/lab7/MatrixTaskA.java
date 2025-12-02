package lab7;

import java.util.concurrent.Callable;

public class MatrixTaskA implements Callable<Double> {

    private final int i;
    private final int k;
    private final double[][] matrix;

    public MatrixTaskA(int i, int k, double[][] matrix) {
        this.i = i;
        this.k = k;
        this.matrix = matrix;
    }

    @Override
    public Double call() {
        return matrix[k][i] / matrix[i][i];
    }

}
