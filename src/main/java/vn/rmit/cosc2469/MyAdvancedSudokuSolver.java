package vn.rmit.cosc2469;

import java.util.HashSet;
import java.util.Set;

/**
 * A separate class that actually solves Sudoku.
 *
 * If you want a more advanced approach (e.g., hidden singles, naked pairs),
 * you can add that here. But this basic version typically solves "intermediate"
 * puzzles quickly if there's no bug and the puzzle is valid.
 */
public class MyAdvancedSudokuSolver {

    private static final int SIZE = 9;
    private static final int EMPTY = 0;

    private static final long TIME_LIMIT_MS = 2L * 60_000; // 2 minutes

    public void solveInPlace(int[][] board) {
        long startTime = System.currentTimeMillis();
        if (!solveRecursively(board, startTime)) {
            throw new IllegalStateException("No valid assignment found => puzzle unsolvable or timed out.");
        }
    }

    private boolean solveRecursively(int[][] board, long startTime) {
        // 1) Repeated "naked singles" pass
        boolean changed = true;
        while (changed) {
            if (System.currentTimeMillis() - startTime > TIME_LIMIT_MS) {
                throw new RuntimeException("Solver timed out (>2 minutes).");
            }
            changed = fillNakedSingles(board);
            if (isComplete(board)) {
                return isValidCompleted(board);
            }
        }

        // 2) Find a blank cell with the fewest candidates
        int[] cell = findCellWithFewestCandidates(board);
        if (cell == null) {
            // no blanks => puzzle is presumably filled
            return isValidCompleted(board);
        }
        int row = cell[0], col = cell[1];
        Set<Integer> candidates = getCandidates(board, row, col);

        // 3) Backtracking
        for (int val : candidates) {
            board[row][col] = val;
            if (solveRecursively(board, startTime)) {
                return true;
            }
            board[row][col] = EMPTY;
        }
        return false;
    }

    /**
     * fillNakedSingles:
     * If a cell has exactly 1 candidate, fill it. Return true if at least one cell
     * was filled.
     */
    private boolean fillNakedSingles(int[][] board) {
        boolean changed = false;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == EMPTY) {
                    Set<Integer> cands = getCandidates(board, r, c);
                    if (cands.isEmpty()) {
                        throw new IllegalStateException("No candidates => puzzle unsolvable.");
                    }
                    if (cands.size() == 1) {
                        board[r][c] = cands.iterator().next();
                        changed = true;
                    }
                }
            }
        }
        return changed;
    }

    private int[] findCellWithFewestCandidates(int[][] board) {
        int[] best = null;
        int bestSize = Integer.MAX_VALUE;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == EMPTY) {
                    Set<Integer> cands = getCandidates(board, r, c);
                    if (cands.isEmpty()) {
                        // immediate contradiction
                        return new int[] { r, c };
                    }
                    if (cands.size() < bestSize) {
                        bestSize = cands.size();
                        best = new int[] { r, c };
                    }
                }
            }
        }
        return best;
    }

    private Set<Integer> getCandidates(int[][] board, int row, int col) {
        Set<Integer> used = new HashSet<>();
        // Row
        for (int x = 0; x < SIZE; x++) {
            if (board[row][x] != EMPTY) {
                used.add(board[row][x]);
            }
        }
        // Column
        for (int y = 0; y < SIZE; y++) {
            if (board[y][col] != EMPTY) {
                used.add(board[y][col]);
            }
        }
        // 3x3 box
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int rr = 0; rr < 3; rr++) {
            for (int cc = 0; cc < 3; cc++) {
                int val = board[boxRow + rr][boxCol + cc];
                if (val != EMPTY) {
                    used.add(val);
                }
            }
        }

        // all possible digits 1..9 minus used
        Set<Integer> cands = new HashSet<>();
        for (int v = 1; v <= 9; v++) {
            if (!used.contains(v)) {
                cands.add(v);
            }
        }
        return cands;
    }

    private boolean isComplete(int[][] board) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidCompleted(int[][] board) {
        // Check row & col
        for (int i = 0; i < SIZE; i++) {
            Set<Integer> rowVals = new HashSet<>();
            Set<Integer> colVals = new HashSet<>();
            for (int j = 0; j < SIZE; j++) {
                int rv = board[i][j];
                if (rv < 1 || rv > 9 || !rowVals.add(rv)) {
                    return false;
                }
                int cv = board[j][i];
                if (cv < 1 || cv > 9 || !colVals.add(cv)) {
                    return false;
                }
            }
        }
        // Check 3x3 boxes
        for (int boxRow = 0; boxRow < SIZE; boxRow += 3) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += 3) {
                Set<Integer> boxSet = new HashSet<>();
                for (int rr = 0; rr < 3; rr++) {
                    for (int cc = 0; cc < 3; cc++) {
                        int val = board[boxRow + rr][boxCol + cc];
                        if (val < 1 || val > 9 || !boxSet.add(val)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
