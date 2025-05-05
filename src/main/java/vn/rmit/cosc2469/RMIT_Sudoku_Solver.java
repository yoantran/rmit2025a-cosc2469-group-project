package vn.rmit.cosc2469;

import java.util.*;

/**
 * RMIT_Sudoku_Solver is a backtracking-based Sudoku solver that uses
 * constraint-based heuristics (Minimum Remaining Value) to optimize search.
 *
 * <h2>Algorithm Type</h2>
 * Backtracking (DFS) with MRV heuristic.
 *
 * <h2>Time and Space Complexity</h2>
 * <ul>
 *     <li>Worst-case time complexity: O(9^n), where n is the number of empty cells.</li>
 *     <li>Best-case: Much less due to pruning on nearly-filled boards.</li>
 *     <li>Space complexity: O(n) in recursion depth (maximum 81 levels).</li>
 * </ul>
 */
public class RMIT_Sudoku_Solver {
    private long startTime;
    private static final long TIME_LIMIT = 2 * 60 * 1000; // 2 minutes
    private SolverLogger logger;
    private int stepCounter = 0;

    /**
     * Injects a logger instance to capture solving steps.
     * @param logger the SolverLogger object
     */
    public void setLogger(SolverLogger logger) {
        this.logger = logger;
    }

    /**
     * Entry point to solve a given Sudoku puzzle.
     *
     * @param puzzle A 9x9 grid with 0 representing empty cells.
     * @return A solved 9x9 Sudoku board.
     * @throws RuntimeException if the time limit is exceeded or no solution is found.
     */
    public int[][] solve(int[][] puzzle) {
        startTime = System.currentTimeMillis();

        // Deep copy the puzzle to avoid modifying the original input.
        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(puzzle[r], 0, board[r], 0, 9);
        }

        if (solveSudoku(board)) return board;
        else throw new RuntimeException("No solution found within time limit.");
    }

    /**
     * Recursive backtracking function that uses the MRV (Minimum Remaining Values) heuristic.
     * It selects the empty cell with the fewest candidates, then tries each candidate in turn.
     * If a candidate leads to a dead-end (i.e. no solution in recursion), it backtracks.
     *
     * Explicit logging is used to trace the decision process.
     */
    private boolean solveSudoku(int[][] board) {
        // Timeout check.
        if (System.currentTimeMillis() - startTime > TIME_LIMIT) {
            throw new RuntimeException("Time limit exceeded.");
        }

        // Collect all empty cells along with their candidate lists.
        List<CellOptions> allOptions = new ArrayList<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    List<Integer> candidates = getCandidates(board, r, c);
                    allOptions.add(new CellOptions(r, c, candidates));
                }
            }
        }

        // If there are no empty cells, the board is solved.
        if (allOptions.isEmpty()) return true;

        // Use MRV: select the empty cell with the fewest candidate options.
        allOptions.sort(Comparator.comparingInt(o -> o.candidates.size()));
        CellOptions chosen = allOptions.get(0);

        // Log the evaluation of all empty cells and the decision.
        if (logger != null) {
            logger.logStep("Evaluating all empty cells:");
            for (CellOptions option : allOptions) {
                logger.logStep("- Cell (" + (option.row + 1) + "," + (option.col + 1) + ") has options: " + option.candidates);
            }
            logger.logStep("→ Choosing cell (" + (chosen.row + 1) + "," + (chosen.col + 1) +
                    ") because it has the fewest options: " + chosen.candidates);
        }

        int row = chosen.row;
        int col = chosen.col;
        List<Integer> candidates = chosen.candidates;

        // Try each candidate for the chosen cell.
        for (int num : candidates) {
            // Log the candidate attempt.
            if (logger != null) {
                stepCounter++;
                String reason = explainConflicts(board, row, col, num);
                logger.logStep(stepCounter + ". Trying (" + (row + 1) + "," + (col + 1) + ") = " + num
                        + " → " + reason + " | Options: " + candidates);
            }

            board[row][col] = num;
            if (solveSudoku(board)) return true;

            // Log that candidate 'num' leads to a dead-end and backtracking is occurring.
            if (logger != null) {
                logger.logStep("Candidate " + num + " at (" + (row + 1) + "," + (col + 1) +
                        ") leads to dead-end. Backtracking.");
            }
            board[row][col] = 0; // Backtrack.
        }

        // If none of the candidates led to a solution, log the dead-end condition at this cell.
        if (logger != null) {
            logger.logStep("All candidates for cell (" + (row + 1) + "," + (col + 1) + ") exhausted " +
                    candidates + ". Dead-end reached, backtracking to previous decision.");
        }

        return false;
    }

    /**
     * Checks and explains conflicts when trying to place a number in a cell.
     * Returns a string explaining if there are row, column, or box conflicts.
     * If there are no conflicts, returns "no conflicts, trying value".
     */
    private String explainConflicts(int[][] board, int row, int col, int num) {
        List<String> reasons = new ArrayList<>();

        // Check row conflicts.
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                reasons.add("row conflict with (" + (row + 1) + "," + (i + 1) + ")");
            }
        }
        // Check column conflicts.
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                reasons.add("col conflict with (" + (i + 1) + "," + (col + 1) + ")");
            }
        }
        // Check 3x3 box conflicts.
        int boxStartRow = row - row % 3;
        int boxStartCol = col - col % 3;
        for (int i = boxStartRow; i < boxStartRow + 3; i++) {
            for (int j = boxStartCol; j < boxStartCol + 3; j++) {
                if (board[i][j] == num) {
                    reasons.add("3x3 conflict with (" + (i + 1) + "," + (j + 1) + ")");
                }
            }
        }
        return reasons.isEmpty() ? "no conflicts, trying value" : String.join("; ", reasons);
    }

    /**
     * Returns a list of valid candidates (numbers 1–9) for a given cell,
     * considering the Sudoku rules (row, column, and 3x3 box constraints).
     */
    private List<Integer> getCandidates(int[][] board, int row, int col) {
        boolean[] used = new boolean[10]; // Index 0 is unused.

        // Mark numbers used in the current row and column.
        for (int i = 0; i < 9; i++) {
            used[board[row][i]] = true;
            used[board[i][col]] = true;
        }
        // Mark numbers used in the 3x3 box.
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                used[board[boxRow + i][boxCol + j]] = true;
            }
        }
        // Collect numbers that are not used.
        List<Integer> candidates = new ArrayList<>();
        for (int num = 1; num <= 9; num++) {
            if (!used[num]) {
                candidates.add(num);
            }
        }
        return candidates;
    }

    /**
     * Helper class to store an empty cell's coordinates along with its candidate options.
     */
    private static class CellOptions {
        int row, col;
        List<Integer> candidates;

        CellOptions(int row, int col, List<Integer> candidates) {
            this.row = row;
            this.col = col;
            this.candidates = candidates;
        }
    }
}
