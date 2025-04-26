package vn.rmit.cosc2469;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SudokuSolverHelper {

    public static int[][] loadSudokuFromCSV(String filePath) {
        int[][] puzzle = new int[9][9];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < 9) {
                String[] tokens = line.split(",");
                for (int col = 0; col < tokens.length && col < 9; col++) {
                    puzzle[row][col] = Integer.parseInt(tokens[col].trim());
                }
                row++;
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            throw new RuntimeException("Failed to load Sudoku puzzle from file.");
        }

        return puzzle;
    }

    public static boolean isValidSudoku(int[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] row = new boolean[10];
            boolean[] col = new boolean[10];
            boolean[] box = new boolean[10];

            for (int j = 0; j < 9; j++) {
                // Row check
                int r = board[i][j];
                if (r != 0 && row[r])
                    return false;
                if (r != 0)
                    row[r] = true;

                // Column check
                int c = board[j][i];
                if (c != 0 && col[c])
                    return false;
                if (c != 0)
                    col[c] = true;

                // Box check
                int rowIndex = 3 * (i / 3) + j / 3;
                int colIndex = 3 * (i % 3) + j % 3;
                int b = board[rowIndex][colIndex];
                if (b != 0 && box[b])
                    return false;
                if (b != 0)
                    box[b] = true;
            }
        }
        return true;
    }

}
