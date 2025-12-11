package lab7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MatrixReader {

    public static double[][] readMatrixFromFile(String filePath) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = reader.readLine();

            int n = Integer.parseInt(firstLine.trim());

            double[][] matrix = new double[n][n + 1];
            String line;
            int j = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                for (int i = 0; i < n; i++) {
                    matrix[i][j] = Double.parseDouble(parts[i]);
                }
                j++;
            }
            return matrix;

        }
    }
}
