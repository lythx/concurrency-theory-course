package lab7;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MatrixWriter {

    public static void writeMatrixToFile(String filePath, double[][] matrix) throws IOException {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(numCols));
            writer.newLine();

            for (int r = 0; r < numRows; r++) {
                double[] row = matrix[r];

                StringBuilder rowString = new StringBuilder();
                for (int c = 0; c < numCols; c++) {
                    rowString.append(row[c]);
                    if (c < numCols - 1) {
                        rowString.append(" ");
                    }
                }
                writer.write(rowString.toString());
                if (r < numRows - 1) {
                    writer.newLine();
                }
            }
            writer.flush();
        }
    }

}