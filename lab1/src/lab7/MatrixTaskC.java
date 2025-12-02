package lab7;

public class MatrixTaskC implements Runnable {

    private final int i;
    private final int j;
    private final int k;
    private final double[][] matrix;
    private final double[][] subtractors;

    public MatrixTaskC(int i, int j, int k, double[][] matrix, double[][] subtractors) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.matrix = matrix;
        this.subtractors = subtractors;
    }

    @Override
    public void run() {
        matrix[k][j] -= subtractors[k][i];
    }

}
