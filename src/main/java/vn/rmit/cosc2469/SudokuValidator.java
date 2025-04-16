package vn.rmit.cosc2469;

import java.util.HashSet;
import java.util.Set;

public class SudokuValidator {
    public static boolean isValidSudoku(int[][] grid) {
        return areRowsValid(grid) && areColsValid(grid) && areBoxesValid(grid);
    }

    private static boolean areRowsValid(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            Set<Integer> seen = new HashSet<>();
            for (int col = 0; col < 9; col++) {
                int num = grid[row][col];
                if (num < 1 || num > 9 || !seen.add(num)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean areColsValid(int[][] grid) {
        for (int col = 0; col < 9; col++) {
            Set<Integer> seen = new HashSet<>();
            for (int row = 0; row < 9; row++) {
                int num = grid[row][col];
                if (num < 1 || num > 9 || !seen.add(num)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean areBoxesValid(int[][] grid) {
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                if (!isBoxValid(grid, boxRow, boxCol)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isBoxValid(int[][] grid, int startRow, int startCol) {
        Set<Integer> seen = new HashSet<>();
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                int num = grid[row][col];
                if (num < 1 || num > 9 || !seen.add(num)) {
                    return false;
                }
            }
        }
        return true;
    }

}
