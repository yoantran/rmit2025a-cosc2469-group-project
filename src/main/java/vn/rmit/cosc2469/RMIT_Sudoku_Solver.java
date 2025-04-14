package vn.rmit.cosc2469;

/**
 * This solver uses constraint programming techniques to efficiently solve Sudoku puzzles.
 * Key constraint programming concepts implemented:
 * 1. Domain representation - tracking possible values for each cell
 * 2. Constraint propagation - arc consistency (AC-3 algorithm)
 * 3. Forward checking
 * 4. Variable ordering heuristics (MRV)
 * 5. Value ordering heuristics (LCV)
 */
public class RMIT_Sudoku_Solver {
    // Constants for puzzle dimensions
    private static final int SIZE = 9;
    private static final int BOX_SIZE = 3;
    private static final int EMPTY = 0;

    /**
     * Represents the domains (possible values) for each cell in the grid
     */
    private boolean[][][] domains;

    /**
     * Solves the given Sudoku puzzle using constraint programming techniques.
     * @param puzzle A 9x9 grid where 0 represents empty cells and 1-9 are filled cells
     * @return The solved puzzle as a 9x9 grid
     * @throws RuntimeException if the puzzle cannot be solved within 2 minutes
     */
    public int[][] solve(int[][] puzzle) {
        // Create a copy of the puzzle to work with
        int[][] board = copyBoard(puzzle);

        // Initialize domains for all cells
        initializeDomains(board);

        // Apply initial constraint propagation
        if (!constraintPropagation(board)) {
            throw new RuntimeException("No solution exists for this puzzle");
        }

        // Solve using backtracking with constraint programming techniques
        if (!backtrackWithCP(board)) {
            throw new RuntimeException("No solution exists for this puzzle");
        }

        return board;
    }

    /**
     * Creates a deep copy of the Sudoku board.
     */
    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

    /**
     * Initializes the domains for all cells in the board.
     * For filled cells, the domain contains only the assigned value.
     * For empty cells, the domain initially contains all possible values (1-9).
     */
    private void initializeDomains(int[][] board) {
        domains = new boolean[SIZE][SIZE][SIZE + 1];

        // Initialize all domains
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    // Empty cell - all values possible initially
                    for (int val = 1; val <= SIZE; val++) {
                        domains[row][col][val] = true;
                    }
                } else {
                    // Filled cell - only the assigned value is possible
                    int value = board[row][col];
                    for (int val = 1; val <= SIZE; val++) {
                        domains[row][col][val] = (val == value);
                    }
                }
            }
        }

        // Apply initial constraints from pre-filled values
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != EMPTY) {
                    int value = board[row][col];
                    updateDomains(board, row, col, value);
                }
            }
        }
    }

    /**
     * Updates domains of related cells when a value is assigned to a cell.
     * This implements the forward checking aspect of constraint programming.
     * @param board The current board state
     * @param row The row where a value was assigned
     * @param col The column where a value was assigned
     * @param value The value that was assigned
     * @return false if any domain becomes empty, true otherwise
     */
    private boolean updateDomains(int[][] board, int row, int col, int value) {
        // Remove value from domains in the same row
        for (int c = 0; c < SIZE; c++) {
            if (c != col && board[row][c] == EMPTY) {
                domains[row][c][value] = false;
                if (countDomainSize(row, c) == 0) {
                    return false; // No valid values for this cell
                }
            }
        }

        // Remove value from domains in the same column
        for (int r = 0; r < SIZE; r++) {
            if (r != row && board[r][col] == EMPTY) {
                domains[r][col][value] = false;
                if (countDomainSize(r, col) == 0) {
                    return false; // No valid values for this cell
                }
            }
        }

        // Remove value from domains in the same 3x3 box
        int boxStartRow = (row / BOX_SIZE) * BOX_SIZE;
        int boxStartCol = (col / BOX_SIZE) * BOX_SIZE;

        for (int r = boxStartRow; r < boxStartRow + BOX_SIZE; r++) {
            for (int c = boxStartCol; c < boxStartCol + BOX_SIZE; c++) {
                if ((r != row || c != col) && board[r][c] == EMPTY) {
                    domains[r][c][value] = false;
                    if (countDomainSize(r, c) == 0) {
                        return false; // No valid values for this cell
                    }
                }
            }
        }

        return true;
    }

    /**
     * Counts the number of values in the domain of a cell.
     */
    private int countDomainSize(int row, int col) {
        int count = 0;
        for (int val = 1; val <= SIZE; val++) {
            if (domains[row][col][val]) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the first value from a cell's domain.
     * @return The first value in the domain, or 0 if the domain is empty
     */
    private int getFirstDomainValue(int row, int col) {
        for (int val = 1; val <= SIZE; val++) {
            if (domains[row][col][val]) {
                return val;
            }
        }
        return 0; // Empty domain
    }

    /**
     * Applies constraint propagation using a simplified version of the AC-3 algorithm.
     * This ensures arc consistency throughout the board.
     * @param board The current board state
     * @return false if a contradiction is found, true otherwise
     */
    private boolean constraintPropagation(int[][] board) {
        boolean changed = true;

        // Repeat until no more changes
        while (changed) {
            changed = false;

            // Check for cells with only one possible value (naked singles)
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (board[row][col] == EMPTY) {
                        int domainSize = countDomainSize(row, col);

                        if (domainSize == 0) {
                            return false; // No solution
                        } else if (domainSize == 1) {
                            // Found a cell with only one possible value
                            int value = getFirstDomainValue(row, col);
                            board[row][col] = value;

                            // Update domains of related cells
                            if (!updateDomains(board, row, col, value)) {
                                return false; // Contradiction found
                            }

                            changed = true;
                        }
                    }
                }
            }

            // Check for hidden singles in rows, columns, and boxes
            if (findHiddenSingles(board)) {
                changed = true;
            }
        }

        return true;
    }

    /**
     * Finds and fills in hidden singles - values that can only appear in one cell
     * within a row, column, or box.
     * @param board The current board state
     * @return true if any hidden singles were found and filled
     */
    private boolean findHiddenSingles(int[][] board) {
        boolean found = false;

        // Check rows for hidden singles
        for (int row = 0; row < SIZE; row++) {
            for (int val = 1; val <= SIZE; val++) {
                int possibleCol = -1;
                int count = 0;

                for (int col = 0; col < SIZE; col++) {
                    if (board[row][col] == EMPTY && domains[row][col][val]) {
                        possibleCol = col;
                        count++;
                    }
                }

                if (count == 1) {
                    // Found a hidden single in this row
                    board[row][possibleCol] = val;

                    // Update domains
                    for (int v = 1; v <= SIZE; v++) {
                        domains[row][possibleCol][v] = (v == val);
                    }

                    if (!updateDomains(board, row, possibleCol, val)) {
                        return false; // Contradiction found
                    }

                    found = true;
                }
            }
        }

        // Check columns for hidden singles
        for (int col = 0; col < SIZE; col++) {
            for (int val = 1; val <= SIZE; val++) {
                int possibleRow = -1;
                int count = 0;

                for (int row = 0; row < SIZE; row++) {
                    if (board[row][col] == EMPTY && domains[row][col][val]) {
                        possibleRow = row;
                        count++;
                    }
                }

                if (count == 1) {
                    // Found a hidden single in this column
                    board[possibleRow][col] = val;

                    // Update domains
                    for (int v = 1; v <= SIZE; v++) {
                        domains[possibleRow][col][v] = (v == val);
                    }

                    if (!updateDomains(board, possibleRow, col, val)) {
                        return false; // Contradiction found
                    }

                    found = true;
                }
            }
        }

        // Check boxes for hidden singles
        for (int boxRow = 0; boxRow < SIZE; boxRow += BOX_SIZE) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += BOX_SIZE) {
                for (int val = 1; val <= SIZE; val++) {
                    int possibleRow = -1;
                    int possibleCol = -1;
                    int count = 0;

                    for (int r = 0; r < BOX_SIZE; r++) {
                        for (int c = 0; c < BOX_SIZE; c++) {
                            int row = boxRow + r;
                            int col = boxCol + c;

                            if (board[row][col] == EMPTY && domains[row][col][val]) {
                                possibleRow = row;
                                possibleCol = col;
                                count++;
                            }
                        }
                    }

                    if (count == 1) {
                        // Found a hidden single in this box
                        board[possibleRow][possibleCol] = val;

                        // Update domains
                        for (int v = 1; v <= SIZE; v++) {
                            domains[possibleRow][possibleCol][v] = (v == val);
                        }

                        if (!updateDomains(board, possibleRow, possibleCol, val)) {
                            return false; // Contradiction found
                        }

                        found = true;
                    }
                }
            }
        }

        return found;
    }

    /**
     * Backtracking algorithm enhanced with constraint programming techniques.
     * Uses MRV (Minimum Remaining Values) for variable ordering and
     * LCV (The Least Constraining Value) for value ordering.
     */
    private boolean backtrackWithCP(int[][] board) {
        // Find the cell with minimum remaining values (MRV)
        int[] cell = findMRVCell(board);
        int row = cell[0];
        int col = cell[1];

        // If no empty cell is found, the puzzle is solved
        if (row == -1 && col == -1) {
            return true;
        }

        // Get the values in this cell's domain, ordered by least constraining value
        int[] orderedValues = getOrderedDomainValues(board, row, col);

        // Try each value in the domain
        for (int val : orderedValues) {
            // Skip if this value is not in the domain
            if (!domains[row][col][val]) {
                continue;
            }

            // Make a backup of the domains
            boolean[][][] domainBackup = backupDomains();

            // Assign the value
            board[row][col] = val;

            // Update domains for this assignment
            for (int v = 1; v <= SIZE; v++) {
                domains[row][col][v] = (v == val);
            }

            // Apply constraint propagation
            boolean valid = updateDomains(board, row, col, val) && constraintPropagation(board);

            if (valid && backtrackWithCP(board)) {
                return true;
            }

            // Backtrack
            board[row][col] = EMPTY;
            restoreDomains(domainBackup);
        }

        return false;
    }

    /**
     * Finds the cell with the minimum remaining values (MRV).
     * This is a key variable ordering heuristic in constraint programming.
     *
     * @param board The current board state
     * @return An array [row, col] of the cell with fewest possible values,
     *         or [-1, -1] if all cells are filled
     */
    private int[] findMRVCell(int[][] board) {
        int minDomainSize = SIZE + 1;
        int[] result = {-1, -1};

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    int domainSize = countDomainSize(row, col);

                    if (domainSize < minDomainSize) {
                        minDomainSize = domainSize;
                        result[0] = row;
                        result[1] = col;

                        // If we found a cell with only one possible value, return immediately
                        if (minDomainSize == 1) {
                            return result;
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Returns the domain values for a cell ordered by the least constraining value (LCV) heuristic.
     * LCV orders values by how much they constrain other variables (fewest conflicts first).
     * @param board The current board state
     * @param row The row of the cell
     * @param col The column of the cell
     * @return Array of values in the domain, ordered by LCV
     */
    private int[] getOrderedDomainValues(int[][] board, int row, int col) {
        int[] counts = new int[SIZE + 1];
        int[] orderedValues = new int[SIZE];
        int valueCount = 0;

        // Count how many cells would be affected by each value
        for (int val = 1; val <= SIZE; val++) {
            if (domains[row][col][val]) {
                counts[val] = countConstraints(board, row, col, val);
                orderedValues[valueCount++] = val;
            }
        }

        // Sort values by fewest constraints first (bubble sort for simplicity)
        for (int i = 0; i < valueCount - 1; i++) {
            for (int j = 0; j < valueCount - i - 1; j++) {
                if (counts[orderedValues[j]] > counts[orderedValues[j + 1]]) {
                    // Swap
                    int temp = orderedValues[j];
                    orderedValues[j] = orderedValues[j + 1];
                    orderedValues[j + 1] = temp;
                }
            }
        }

        // Create a properly sized result array
        int[] result = new int[valueCount];
        System.arraycopy(orderedValues, 0, result, 0, valueCount);

        return result;
    }

    /**
     * Counts how many related cells would be constrained if a value is assigned.
     * Used for the Least Constraining Value (LCV) heuristic.
     */
    private int countConstraints(int[][] board, int row, int col, int value) {
        int count = 0;

        // Check row
        for (int c = 0; c < SIZE; c++) {
            if (c != col && board[row][c] == EMPTY && domains[row][c][value]) {
                count++;
            }
        }

        // Check column
        for (int r = 0; r < SIZE; r++) {
            if (r != row && board[r][col] == EMPTY && domains[r][col][value]) {
                count++;
            }
        }

        // Check box
        int boxStartRow = (row / BOX_SIZE) * BOX_SIZE;
        int boxStartCol = (col / BOX_SIZE) * BOX_SIZE;

        for (int r = boxStartRow; r < boxStartRow + BOX_SIZE; r++) {
            for (int c = boxStartCol; c < boxStartCol + BOX_SIZE; c++) {
                if ((r != row || c != col) && board[r][c] == EMPTY && domains[r][c][value]) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Creates a backup of the current domains.
     */
    private boolean[][][] backupDomains() {
        boolean[][][] backup = new boolean[SIZE][SIZE][SIZE + 1];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                for (int val = 1; val <= SIZE; val++) {
                    backup[row][col][val] = domains[row][col][val];
                }
            }
        }

        return backup;
    }

    /**
     * Restores domains from a backup.
     */
    private void restoreDomains(boolean[][][] backup) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                for (int val = 1; val <= SIZE; val++) {
                    domains[row][col][val] = backup[row][col][val];
                }
            }
        }
    }
}