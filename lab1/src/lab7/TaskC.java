package lab7;

public class TaskC implements Runnable {

    private final int j;
    private final int k;
    private final double[][] M;
    private final double[][] n;

    public TaskC(int j, int k, double[][] M, double[][] n) {
        this.j = j;
        this.k = k;
        this.M = M;
        this.n = n;
    }

    @Override
    public void run() {
        M[k][j] -= n[k][j];
    }

}
