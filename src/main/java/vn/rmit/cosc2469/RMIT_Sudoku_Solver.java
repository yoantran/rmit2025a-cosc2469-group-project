package vn.rmit.cosc2469;

import java.util.*;

import static vn.rmit.cosc2469.SudokuValidator.isValidSudoku;


/**
 * A Sudoku solver that applies local search heuristics combined with a tabu search strategy
 * to solve standard 9x9 Sudoku puzzles. The solver uses:
 * <ul>
 *   <li>Heuristic 1: Basic row/column/box elimination to fill initial values</li>
 *   <li>Tabu search: Iteratively reduces conflicts by swapping non-fixed values within rows</li>
 *   <li>Cost evaluation: Conflict calculation in columns and boxes</li>
 * </ul>
 *
 * If a solution is not found within a given iteration limit or time limit (2 minutes),
 * the best attempt is returned.
 *
 * @param puzzle int[][]
 * @return A completed 9x9 Sudoku grid if solvable within limits; otherwise, the best attempt
 */
public class RMIT_Sudoku_Solver {
    private static final int SIZE = 9;
    private static final int BOX = 3;   // size of each 3x3 sub-box
    private static final int MAX_ITERATIONS = 10000;
    private static final int TIME_LIMIT_MS = 120000; // 2 minutes
    private final Random random = new Random();         // random generator for initial filling
    private int stepCount = 0;


    public int[][] solve(int[][] puzzle) {
        SolverTimer.startTimer();

//        long startTime = System.currentTimeMillis();    // record start time for timeout check

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
        Map<String, Integer> tabuList = new HashMap<>();
        int tabuTenure = 7; // move will be forbidden for 7 iterations

        // step 2: tabu search to improve solution
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            if (SolverTimer.timeElapsed() > TIME_LIMIT_MS) {
                SolverTimer.checkTimeout();
            }


            if (bestCost == 0) break;   // puzzle is solved if no conflicts remain

            // Decrease remaining tenure for each tabu move
            List<String> expiredMoves = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : tabuList.entrySet()) {
                int remaining = entry.getValue() - 1;
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

                        // create a candiate solution by swapping two values in the row
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
     * Deep copies a 2D array to avoid mutating original reference
     */
    private int[][] deepCopy(int[][] board) {
        int[][] copy = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    /**
     * swaps two elements in a row
     */
    private void swap(int[] row, int i, int j) {
        int temp = row[i];
        row[i] = row[j];
        row[j] = temp;
    }

    /**
     * Calculates the total number of conflicts in the grid.
     * Conflicts are counted in:
     *   - Columns (duplicate numbers)
     *   - 3x3 boxes (duplicate numbers)
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

    public int[][] solveUntilValid(int[][] puzzle) {
//        long startTime = System.currentTimeMillis();
        SolverTimer.startTimer();

        int attempt = 0;

        while (true) {
            attempt++;
            int[][] result = solve(puzzle);

            if (isValidSudoku(result)) {
                System.out.println("✅ Found a valid solution on attempt " + attempt);
                SolverTimer.resetTimer();
                return result;
            }

            if (SolverTimer.timeElapsed() > TIME_LIMIT_MS) {
                SolverTimer.checkTimeout();
            }

            System.out.println("❌ Attempt " + attempt + " failed. Retrying...");
        }
    }

    public int getStepCount() {
        return stepCount;
    }

    /**
     * Converts a 2D Sudoku board to a readable string format.
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