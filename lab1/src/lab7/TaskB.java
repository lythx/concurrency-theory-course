package lab7;

public class TaskB implements Runnable {

    private final int i;
    private final int j;
    private final int k;
    private final double[][] matrix;
    private final double[] multipliers;
    private final double[][] valuesToSubtract;

    public TaskB(int i, int j, int k, double[][] matrix, double[] multipliers, double[][] valuesToSubtract) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.matrix = matrix;
        this.multipliers = multipliers;
        this.valuesToSubtract = valuesToSubtract;
    }

    @Override
    public void run() {
        valuesToSubtract[k][j] = matrix[i][j] * multipliers[k];
    }

}
