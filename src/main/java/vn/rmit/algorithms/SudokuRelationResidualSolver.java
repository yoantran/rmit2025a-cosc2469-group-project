package vn.rmit.algorithms;

import java.util.*;

/**
 * A robust "Relation & Residual" Sudoku solver with advanced constraint propagation
 * (naked singles, hidden singles, naked pairs) plus a fallback backtracking approach (MRV).
 *
 * Usage in your RMIT_Sudoku_Solver:
 *   SudokuRelationResidualSolver solver = new SudokuRelationResidualSolver();
 *   solver.solveInPlace(board);
 *
 * This version includes `isValidCompleted(...)` so you won't get
 * "Cannot resolve method" errors anymore.
 */
public class SudokuRelationResidualSolver {

    private static final int SIZE = 9;
    private static final int EMPTY = 0;

    /**
     * Public method: modifies the `board` in-place to solve Sudoku.
     * Throws IllegalStateException if puzzle is unsolvable or if it times out.
     */
    public void solveInPlace(int[][] board) {
        long startTime = System.currentTimeMillis();
        final long MAX_TIME_MS = 20L * 60_000; // 2 minutes; adjust if needed

        boolean solved = solveRecursively(board, startTime, MAX_TIME_MS);
        if (!solved) {
            throw new IllegalStateException("No valid assignment found => puzzle unsolvable or timed out.");
        }
    }

    /**
     * The main recursive solver that:
     * 1) Does repeated advanced constraint logic,
     * 2) Falls back to backtracking with the MRV heuristic.
     */
    private boolean solveRecursively(int[][] board, long startTime, long maxTime) {
        // Repeated advanced constraint propagation
        boolean changed = true;
        while (changed) {
            // Check time limit
            if (System.currentTimeMillis() - startTime > maxTime) {
                throw new RuntimeException("Solver timed out (over 2 minutes).");
            }
            changed = false;

            // (a) Naked Singles
            boolean singleFilled = fillNakedSingles(board);
            if (singleFilled) changed = true;

            // (b) Hidden Singles
            boolean hiddenFilled = fillHiddenSingles(board);
            if (hiddenFilled) changed = true;

            // (c) Naked Pairs
            boolean pairsReduced = applyNakedPairs(board);
            if (pairsReduced) changed = true;

            // Early check if complete
            if (isComplete(board)) {
                // If fully filled, verify validity
                return isValidCompleted(board);
            }
        }

        // If still incomplete, pick a blank cell with the fewest candidates => MRV
        int[] bestCell = findCellWithFewestCandidates(board);
        if (bestCell == null) {
            // No blanks => puzzle presumably filled
            return isValidCompleted(board);
        }
        int row = bestCell[0];
        int col = bestCell[1];
        Set<Integer> candidates = getCandidates(board, row, col);

        // Try each candidate
        for (int val : candidates) {
            board[row][col] = val;
            if (solveRecursively(board, startTime, maxTime)) {
                return true;
            }
            // backtrack
            board[row][col] = EMPTY;
        }

        // No candidate worked => unsolvable from this path
        return false;
    }

    // -------------------------------------------------------------------
    //   A) Constraint Propagation Methods
    // -------------------------------------------------------------------

    /**
     * 1) Naked Singles: If a cell has exactly 1 candidate, fill it in right away.
     */
    private boolean fillNakedSingles(int[][] board) {
        boolean changed = false;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == EMPTY) {
                    Set<Integer> cand = getCandidates(board, r, c);
                    if (cand.isEmpty()) {
                        throw new IllegalStateException("No candidates => unsolvable puzzle.");
                    } else if (cand.size() == 1) {
                        // Exactly one candidate => fill it
                        board[r][c] = cand.iterator().next();
                        changed = true;
                    }
                }
            }
        }
        return changed;
    }

    /**
     * Returns a list of [row, col] cell coordinates for the entire row 'row'.
     */
    private List<int[]> getRowCells(int row) {
        List<int[]> cells = new ArrayList<>();
        for (int c = 0; c < 9; c++) {
            cells.add(new int[] { row, c });
        }
        return cells;
    }

    /**
     * Returns a list of [row, col] cell coordinates for the entire column 'col'.
     */
    private List<int[]> getColCells(int col) {
        List<int[]> cells = new ArrayList<>();
        for (int r = 0; r < 9; r++) {
            cells.add(new int[] { r, col });
        }
        return cells;
    }

    /**
     * Returns a list of [row, col] cell coordinates for the 3x3 box whose
     * top-left corner is at (boxRow, boxCol). Typically boxRow, boxCol
     * are multiples of 3, like (0,0), (0,3), etc.
     */
    private List<int[]> getBoxCells(int boxRow, int boxCol) {
        List<int[]> cells = new ArrayList<>();
        for (int rr = 0; rr < 3; rr++) {
            for (int cc = 0; cc < 3; cc++) {
                cells.add(new int[] { boxRow + rr, boxCol + cc });
            }
        }
        return cells;
    }


    /**
     * 2) Hidden Singles: If a digit can only appear in exactly one cell in a row/col/box, fill it.
     */
    private boolean fillHiddenSingles(int[][] board) {
        boolean changed = false;

        // For each row
        for (int row = 0; row < SIZE; row++) {
            changed |= findHiddenSinglesInUnit(board, getRowCells(row));
        }

        // For each column
        for (int col = 0; col < SIZE; col++) {
            changed |= findHiddenSinglesInUnit(board, getColCells(col));
        }

        // For each 3x3 box
        for (int boxRow = 0; boxRow < SIZE; boxRow += 3) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += 3) {
                changed |= findHiddenSinglesInUnit(board, getBoxCells(boxRow, boxCol));
            }
        }

        return changed;
    }

    /**
     * 3) Naked Pairs: If two cells in the same row/col/box share the same 2-candidate set,
     * remove those candidates from other cells in that unit.
     */
    private boolean applyNakedPairs(int[][] board) {
        boolean changed = false;

        // Rows
        for (int row = 0; row < SIZE; row++) {
            changed |= findNakedPairsInUnit(board, getRowCells(row));
        }

        // Columns
        for (int col = 0; col < SIZE; col++) {
            changed |= findNakedPairsInUnit(board, getColCells(col));
        }

        // 3x3 boxes
        for (int boxRow = 0; boxRow < SIZE; boxRow += 3) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += 3) {
                changed |= findNakedPairsInUnit(board, getBoxCells(boxRow, boxCol));
            }
        }

        return changed;
    }

    /**
     * Helper for hidden singles in a row/col/box.
     * If a digit is only possible in exactly 1 cell, fill it.
     */
    private boolean findHiddenSinglesInUnit(int[][] board, List<int[]> cells) {
        boolean changed = false;

        // digit -> list of possible cells
        Map<Integer, List<int[]>> candidateMap = new HashMap<>();
        for (int d = 1; d <= 9; d++) {
            candidateMap.put(d, new ArrayList<>());
        }

        // Collect possible positions for each digit
        for (int[] cell : cells) {
            int r = cell[0];
            int c = cell[1];
            if (board[r][c] == EMPTY) {
                Set<Integer> cands = getCandidates(board, r, c);
                for (int d : cands) {
                    candidateMap.get(d).add(cell);
                }
            }
        }

        // If a digit can only go to exactly 1 cell, fill it
        for (int d = 1; d <= 9; d++) {
            List<int[]> positions = candidateMap.get(d);
            if (positions.size() == 1) {
                int[] uniqueCell = positions.get(0);
                int rr = uniqueCell[0];
                int cc = uniqueCell[1];
                if (board[rr][cc] == EMPTY) {
                    board[rr][cc] = d;
                    changed = true;
                }
            }
        }

        return changed;
    }

    /**
     * Helper for naked pairs in a row/col/box.
     * If two cells have exactly the same 2-candidate set, remove them from other cells in that unit.
     */
    private boolean findNakedPairsInUnit(int[][] board, List<int[]> cells) {
        boolean changed = false;
        Map<String, List<int[]>> pairsMap = new HashMap<>();

        // gather 2-candidate cells
        for (int[] cell : cells) {
            int r = cell[0], c = cell[1];
            if (board[r][c] == EMPTY) {
                Set<Integer> cands = getCandidates(board, r, c);
                if (cands.size() == 2) {
                    // sort to get stable key
                    List<Integer> pairList = new ArrayList<>(cands);
                    Collections.sort(pairList);
                    String key = pairList.get(0) + "," + pairList.get(1);
                    pairsMap.computeIfAbsent(key, k -> new ArrayList<>()).add(cell);
                }
            }
        }

        // For each pair that occurs exactly in 2 cells, remove them from other cells
        for (Map.Entry<String, List<int[]>> entry : pairsMap.entrySet()) {
            String key = entry.getKey();
            List<int[]> pairCells = entry.getValue();
            if (pairCells.size() == 2) {
                // We have a naked pair
                String[] split = key.split(",");
                int v1 = Integer.parseInt(split[0]);
                int v2 = Integer.parseInt(split[1]);

                for (int[] cell : cells) {
                    if (pairCells.contains(cell)) continue; // skip the pair
                    int rr = cell[0], cc = cell[1];
                    if (board[rr][cc] == EMPTY) {
                        Set<Integer> cands = getCandidates(board, rr, cc);
                        if (cands.contains(v1) || cands.contains(v2)) {
                            cands.remove(v1);
                            cands.remove(v2);
                            if (cands.isEmpty()) {
                                throw new IllegalStateException("No candidates left => puzzle unsolvable.");
                            }
                            // If it becomes a single, fill it
                            if (cands.size() == 1) {
                                board[rr][cc] = cands.iterator().next();
                            }
                            changed = true;
                        }
                    }
                }
            }
        }

        return changed;
    }

    // -------------------------------------------------------------------
    //   B) Backtracking Helpers
    // -------------------------------------------------------------------

    /**
     * Finds a blank cell with the fewest candidates (MRV). Returns null if none is blank.
     */
    private int[] findCellWithFewestCandidates(int[][] board) {
        int[] bestCell = null;
        int bestSize = Integer.MAX_VALUE;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == EMPTY) {
                    Set<Integer> cands = getCandidates(board, r, c);
                    if (cands.isEmpty()) {
                        // Contradiction
                        return new int[]{r, c};
                    }
                    if (cands.size() < bestSize) {
                        bestSize = cands.size();
                        bestCell = new int[]{r, c};
                    }
                }
            }
        }
        return bestCell;
    }

    /**
     * Returns the set of possible digits [1..9] that can fit board[row][col] (given the current fill).
     */
    private Set<Integer> getCandidates(int[][] board, int row, int col) {
        Set<Integer> used = new HashSet<>();

        // row
        for (int cc = 0; cc < SIZE; cc++) {
            if (board[row][cc] != EMPTY) {
                used.add(board[row][cc]);
            }
        }
        // column
        for (int rr = 0; rr < SIZE; rr++) {
            if (board[rr][col] != EMPTY) {
                used.add(board[rr][col]);
            }
        }
        // 3x3 box
        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;
        for (int rr = 0; rr < 3; rr++) {
            for (int cc = 0; cc < 3; cc++) {
                int val = board[boxRowStart + rr][boxColStart + cc];
                if (val != EMPTY) {
                    used.add(val);
                }
            }
        }

        // candidates = 1..9 minus used
        Set<Integer> candidates = new HashSet<>();
        for (int v = 1; v <= 9; v++) {
            if (!used.contains(v)) {
                candidates.add(v);
            }
        }
        return candidates;
    }

    // -------------------------------------------------------------------
    //   C) Utility Methods (isComplete, isValidCompleted, etc.)
    // -------------------------------------------------------------------

    /**
     * Returns true if there are no EMPTY cells.
     */
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

    /**
     * Checks if the board is a valid, completed Sudoku solution:
     * - No zeros
     * - Rows/columns/3x3 boxes contain digits 1..9 exactly once each
     */
    private boolean isValidCompleted(int[][] board) {
        // 1) Check each row & column
        for (int i = 0; i < SIZE; i++) {
            Set<Integer> rowVals = new HashSet<>();
            Set<Integer> colVals = new HashSet<>();
            for (int j = 0; j < SIZE; j++) {
                // row check
                int rowCell = board[i][j];
                if (rowCell < 1 || rowCell > 9 || !rowVals.add(rowCell)) {
                    return false;
                }
                // col check
                int colCell = board[j][i];
                if (colCell < 1 || colCell > 9 || !colVals.add(colCell)) {
                    return false;
                }
            }
        }

        // 2) Check each 3x3 box
        for (int rowBlock = 0; rowBlock < SIZE; rowBlock += 3) {
            for (int colBlock = 0; colBlock < SIZE; colBlock += 3) {
                Set<Integer> boxVals = new HashSet<>();
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        int val = board[rowBlock + r][colBlock + c];
                        if (val < 1 || val > 9 || !boxVals.add(val)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    // -------------------------------------------------------------------
    // End of class
    // -------------------------------------------------------------------
}
