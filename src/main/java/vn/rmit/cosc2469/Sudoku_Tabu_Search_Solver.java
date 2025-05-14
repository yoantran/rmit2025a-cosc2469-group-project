package vn.rmit.cosc2469;

import java.util.*;

import static vn.rmit.cosc2469.SudokuSolverHelper.isValidSudoku;

/**
 * Sudoku_Tabu_Search_Solver implements a local search heuristic, Tabu Search,
 * to solve standard 9x9 Sudoku puzzles. It initializes the grid with an
 * Alldifferent constraint within rows and iteratively reduces conflicts.
 *
 * <h2>Algorithm Type</h2>
 * Local Search (Tabu Search) with Alldifferent Initial Constraint.
 * <h2>Time and Space Complexity (Practical Considerations for n=9)</h2>
 * <ul>
 * <li>Worst-case time complexity: O(MAX_ITERATIONS * n^3), where n is the grid size (9), and MAX_ITERATIONS is the maximum allowed iterations.</li>
 * <li>Best-case: O(n^2), if no or few iterations are needed</li>
 * <li>Space complexity: O(n^2) to store the Sudoku grid and O(m) for the tabu list, where m is the maximum number of stored moves (tabu tenure).</li>
 * </ul>
 */
public class Sudoku_Tabu_Search_Solver {
    private static final int SIZE = 9;  // size of sudoku grid
    private static final int BOX = 3;   // size of each 3x3 sub-box
    private static final int MAX_ITERATIONS = 10000;    // the maximum number of iterations for the Tabu Search
    private static final int TIME_LIMIT_MS = 120000;    // 2 minutes
    private final Random random = new Random();         // random generator for initial filling
    private int stepCount = 0;
    private long startTime = -1;
    private boolean timerStarted = false;

    private void startTimer() {
        if (!timerStarted) {
            startTime = System.currentTimeMillis();
            timerStarted = true;
        } else {
            timeElapsed();
        }
    }

    private long timeElapsed() {
        return timerStarted ? System.currentTimeMillis() - startTime : 0;
    }

    private boolean isTimedOut() {
        return timerStarted && timeElapsed() > TIME_LIMIT_MS;
    }

    private void checkTimeout() {
        if (isTimedOut()) {
            throw new RuntimeException("❗Timeout: Could not solve puzzle within time limit.");
        }
    }

    // Restarts the timer from 0 and sets timerStarted to true
    public void resetTimer() {
        startTime = System.currentTimeMillis();
        timerStarted = true;
        System.out.println("🔄 Timer reset");
    }

    /**
     * Attempts to solve the given Sudoku puzzle using a Tabu Search algorithm.
     *
     * @param puzzle The initial Sudoku puzzle to solve.
     * @return A completed 9x9 Sudoku grid if solvable within limits; otherwise, the best attempt.
     */
    public int[][] solve(int[][] puzzle) {
        startTimer();

        // Fixed cells
        boolean[][] fixed = new boolean[SIZE][SIZE];
        int[][] current = new int[SIZE][SIZE];

        // Step 1: Preprocess with AllDifferent (initialize rows with all 1–9 without repetition)
        for (int row = 0; row < SIZE; row++) {
            List<Integer> available = new ArrayList<>();

            // create a list of numbers 1 to SIZE for the current row
            for (int i = 1; i <= SIZE; i++) available.add(i);

            // copy values from the puzzle to the current solution
            // then mark fixed positions while removing them from available numbers
            for (int col = 0; col < SIZE; col++) {
                current[row][col] = puzzle[row][col];
                if (puzzle[row][col] != 0) {
                    fixed[row][col] = true;     // mark as fixed
                    available.remove((Integer) puzzle[row][col]);       // remove fixed number
                }
            }

            // Fill the remaining cells randomly with available values
            for (int col = 0; col < SIZE; col++) {
                if (!fixed[row][col]) {
                    current[row][col] = available.remove(random.nextInt(available.size()));
                }
            }
        }


        int[][] best = deepCopy(current);               // store best solution found so far
        int bestCost = calculateConflicts(best);        // cost (number of conflicts) of current board

        // Tabu list with tenure
        Map<String, Integer> tabuList = new HashMap<>();    // map to store recently performed moves and their remaining tenure
        int tabuTenure = 7; // move will be forbidden for 7 iterations

        // step 2: tabu search to improve solution
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            if (timeElapsed() > TIME_LIMIT_MS) {
                checkTimeout();
            }


            if (bestCost == 0) break;   // puzzle is solved if no conflicts remain

            // Decrease remaining tenure for each tabu move
            List<String> expiredMoves = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : tabuList.entrySet()) {
                int remaining = entry.getValue() - 1;   // decrease the remaining tenure
                if (remaining <= 0) {
                    expiredMoves.add(entry.getKey());       // collect expired moves to remove later
                } else {
                    tabuList.put(entry.getKey(), remaining);    // update remaining tenure
                }
            }
            // remove expired tabu moves from the tabu list
            for (String move : expiredMoves) {
                tabuList.remove(move);
            }

            // track best solution in this iteration
            int[][] bestCandidate = null;
            int bestCandidateCost = Integer.MAX_VALUE;
            String bestMove = "";

            // explore neighbors by swapping non-fixed values in the same row
            for (int row = 0; row < SIZE; row++) {
                for (int col1 = 0; col1 < SIZE; col1++) {
                    for (int col2 = col1 + 1; col2 < SIZE; col2++) {
                        if (fixed[row][col1] || fixed[row][col2]) continue; // skip fixed

                        // create a candidate solution by swapping two values in the row
                        int[][] candidate = deepCopy(current);
                        swap(candidate[row], col1, col2);

                        // generate a move identifier (for use in tabu list)
                        String move = row + "-" + col1 + "<->" + col2;
                        int cost = calculateConflicts(candidate); // evaluate number of conflicts

                        boolean isTabu = tabuList.containsKey(move); // check if move is tabu
                        boolean isAspiration = cost < bestCost; // allow tabu if its better than the best so far

                        // if move is not tabu
                        if (!isTabu || isAspiration) {
                            if (cost < bestCandidateCost) {
                                bestCandidate = candidate;
                                bestCandidateCost = cost;
                                bestMove = move;
                            }
                        }
                    }
                }
            }

            // apply best move found in this iteration
            if (bestCandidate != null) {
                current = bestCandidate;
                stepCount++; // Count the accepted move
                // update best
                if (bestCandidateCost <= bestCost) {
                    best = deepCopy(bestCandidate);
                    bestCost = bestCandidateCost;
                }

                // Add the selected move to the tabu list with its tenure
                tabuList.put(bestMove, tabuTenure);
            }
        }
        return best;
    }

    /**
     * Deep copies a 2D array to avoid mutating original reference.
     *
     * @param board The 2D integer array to copy.
     * @return A new 2D integer array with the same contents as the input.
     */
    private int[][] deepCopy(int[][] board) {
        int[][] copy = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, board[0].length);
        }
        return copy;
    }

    /**
     * Swaps two elements in a row of the Sudoku board.
     *
     * @param row The integer array representing the row.
     * @param i   The index of the first element to swap.
     * @param j   The index of the second element to swap.
     */
    private void swap(int[] row, int i, int j) {
        int temp = row[i];
        row[i] = row[j];
        row[j] = temp;
    }

    /**
     * Calculates the total number of conflicts in the grid.
     * Conflicts are counted in:
     * - Columns (duplicate numbers)
     * - 3x3 boxes (duplicate numbers)
     *
     * @param board The Sudoku board to evaluate for conflicts.
     * @return The total number of conflicts found in the board.
     */
    private int calculateConflicts(int[][] board) {
        int conflicts = 0;

        // Check for duplicate values in rows
        for (int row = 0; row < SIZE; row++) {
            Set<Integer> seen = new HashSet<>();
            for (int col = 0; col < SIZE; col++) {
                int val = board[row][col];
                if (val != 0 && !seen.add(val)) {
                    conflicts++;
                }
            }
        }

        // Check for duplicate values in columns
        for (int col = 0; col < SIZE; col++) {
            Set<Integer> seen = new HashSet<>();
            for (int row = 0; row < SIZE; row++) {
                int val = board[row][col];
                if (val != 0 && !seen.add(val)) {
                    conflicts++;
                }
            }
        }

        // Check for duplicate values in 3x3 sub-boxes
        for (int boxRow = 0; boxRow < SIZE; boxRow += BOX) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += BOX) {
                Set<Integer> seen = new HashSet<>();
                for (int i = 0; i < BOX; i++) {
                    for (int j = 0; j < BOX; j++) {
                        int val = board[boxRow + i][boxCol + j];
                        if (val != 0 && !seen.add(val)) {
                            conflicts++;
                        }
                    }
                }
            }
        }
        return conflicts;
    }

    /**
     * Repeatedly calls the solve method until a valid Sudoku solution is found.
     *
     * @param puzzle The initial Sudoku puzzle to solve.
     * @return A valid completed 9x9 Sudoku grid.
     */
    public int[][] solveUntilValid(int[][] puzzle) {
        startTimer();

        int attempt = 0;

        while (true) {
            attempt++;
            int[][] result = solve(puzzle);

            if (isValidSudoku(result)) {
                System.out.println("✅ Found a valid solution on attempt " + attempt +
                        " in " + timeElapsed() + " ms");
                resetTimer();
                return result;
            }

            if (isTimedOut()) {
                checkTimeout();
                return null; // Will never reach here due to the exception, but for compilation
            }

            System.out.println("❌ Attempt " + attempt + " failed. Retrying...");
        }
    }

    /**
     * Converts a 2D Sudoku board to a readable string format.
     *
     * @param board The Sudoku board to convert to a string.
     * @return A string representation of the Sudoku board.
     */
    public String toString(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(board[i][j]).append(" ");
                if ((j + 1) % 3 == 0 && j != SIZE - 1) sb.append("| ");
            }
            sb.append("\n");
            if ((i + 1) % 3 == 0 && i != SIZE - 1) sb.append("------+-------+------\n");
        }
        return sb.toString();
    }
}
