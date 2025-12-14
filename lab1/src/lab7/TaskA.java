package lab7;

public class TaskA implements Runnable {

    private final int i;
    private final int k;
    private final double[][] matrix;
    private final double[] multipliers;

    public TaskA(int i, int k, double[][] matrix, double[] multipliers) {
        this.i = i;
        this.k = k;
        this.matrix = matrix;
        this.multipliers = multipliers;
    }

    @Override
    public void run() {
        multipliers[k] = matrix[k][i] / matrix[i][i];
    }

}
