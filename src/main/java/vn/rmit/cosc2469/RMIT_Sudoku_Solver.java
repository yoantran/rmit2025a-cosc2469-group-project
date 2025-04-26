package vn.rmit.cosc2469;

import java.util.*;

public class RMIT_Sudoku_Solver {
    private long startTime;
    private static final long TIME_LIMIT = 2 * 60 * 1000; // 2 minutes
    private SolverLogger logger;
    private int stepCounter = 0;

    public void setLogger(SolverLogger logger) {
        this.logger = logger;
    }

    public int[][] solve(int[][] puzzle) {
        startTime = System.currentTimeMillis();

        // Clone puzzle
        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(puzzle[r], 0, board[r], 0, 9);
        }

        if (solveSudoku(board)) {
            return board;
        } else {
            throw new RuntimeException("No solution found within time limit.");
        }
    }

    private boolean solveSudoku(int[][] board) {
        if (System.currentTimeMillis() - startTime > TIME_LIMIT) {
            throw new RuntimeException("Time limit exceeded.");
        }

        int[] cell = findCellWithFewestOptions(board);
        if (cell == null)
            return true;

        int row = cell[0];
        int col = cell[1];
        List<Integer> candidates = getCandidates(board, row, col);

        for (int num : candidates) {
            if (logger != null) {
                stepCounter++;
                String reason = explainConflicts(board, row, col, num);
                logger.logStep(stepCounter + ". (" + (row + 1) + "," + (col + 1) + ") = " + num + ", " + reason);
            }

            board[row][col] = num;
            if (solveSudoku(board))
                return true;
            board[row][col] = 0; // backtrack
        }

        return false;
    }

    private String explainConflicts(int[][] board, int row, int col, int num) {
        List<String> reasons = new ArrayList<>();

        // Row conflict
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                reasons.add("row conflict with (" + (row + 1) + "," + (i + 1) + ")");
            }
        }

        // Column conflict
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                reasons.add("col conflict with (" + (i + 1) + "," + (col + 1) + ")");
            }
        }

        // 3x3 box conflict
        int boxStartRow = row - row % 3;
        int boxStartCol = col - col % 3;
        for (int i = boxStartRow; i < boxStartRow + 3; i++) {
            for (int j = boxStartCol; j < boxStartCol + 3; j++) {
                if (board[i][j] == num) {
                    reasons.add("3x3 conflict with (" + (i + 1) + "," + (j + 1) + ")");
                }
            }
        }

        if (reasons.isEmpty()) {
            return "no conflicts, trying value";
        }

        return String.join("; ", reasons);
    }

    private int[] findCellWithFewestOptions(int[][] board) {
        int minOptions = 10;
        int[] result = null;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    int options = getCandidates(board, row, col).size();
                    if (options < minOptions) {
                        minOptions = options;
                        result = new int[] { row, col };
                        if (minOptions == 1)
                            return result;
                    }
                }
            }
        }

        return result;
    }

    private List<Integer> getCandidates(int[][] board, int row, int col) {
        boolean[] used = new boolean[10];

        for (int i = 0; i < 9; i++) {
            used[board[row][i]] = true;
            used[board[i][col]] = true;
        }

        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                used[board[boxRow + i][boxCol + j]] = true;
            }
        }

        List<Integer> candidates = new ArrayList<>();
        for (int num = 1; num <= 9; num++) {
            if (!used[num]) {
                candidates.add(num);
            }
        }
        return candidates;
    }
}
