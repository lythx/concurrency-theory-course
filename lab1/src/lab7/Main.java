package lab7;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        double[][] matrix = {
            {2, 1, 3, 6, 10, 100},
            {4, 3, 8, 15, 15, 102},
            {6, 5, 16, 27, 17, 405},
            {5, 7, 12, 13, 56, 12},
            {1, 2, 4, 6, 12, 44}
        };

        var gj = new GaussJordan(matrix);
        gj.run();
        for (var row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

}
