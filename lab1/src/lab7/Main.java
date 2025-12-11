package lab7;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        /*double[][] matrix = {
            {2, 1, 3, 6, 10, 100},
            {4, 3, 8, 15, 15, 102},
            {6, 5, 16, 27, 17, 405},
            {5, 7, 12, 13, 56, 12},
            {1, 2, 4, 6, 12, 44}
        };*/

        double[][] matrix;

        try {
            matrix = MatrixReader.readMatrixFromFile(args[0].trim());
        } catch (IOException ex) {
            System.out.println("Error reading matrix from file: " + ex.getMessage());
            return;
        }

        var gj = new GaussJordan(matrix);
        gj.run();
        for (var row : matrix) {
            for (int j = 0; j < row.length; j++) {
                System.out.print(row[j]);
                if (j != row.length - 1) {
                    System.out.print(" ");
                } else {
                    System.out.println();
                }
            }
        }

        try {
            MatrixWriter.writeMatrixToFile("result.txt", matrix);
        } catch (IOException ex) {
            System.out.println("Error writing matrix to file: " + ex.getMessage());
        }

    }

}
