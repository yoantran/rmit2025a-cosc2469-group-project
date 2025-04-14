package vn.rmit.algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SudokuSolverHelper {

    /**
     * Reads a 9x9 Sudoku puzzle from the given CSV file path.
     * Returns a 9x9 int array with 0 for blanks.
     */
    public static int[][] readSudokuFromCSV(String csvFilePath) throws IOException {
        int[][] puzzle = new int[9][9];
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < 9) {
                String[] parts = line.split(",");
                if (parts.length != 9) {
                    throw new IOException("Each row must have exactly 9 comma-separated values.");
                }
                for (int col = 0; col < 9; col++) {
                    puzzle[row][col] = Integer.parseInt(parts[col].trim());
                }
                row++;
            }
            if (row < 9) {
                throw new IOException("CSV file has fewer than 9 rows for Sudoku puzzle.");
            }
        }
        return puzzle;
    }

    /**
     * Prints the given 9x9 Sudoku board to stdout.
     */
    public static void printSudoku(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                System.out.print(board[r][c] + (c < 8 ? " " : ""));
            }
            System.out.println();
        }
    }
}
