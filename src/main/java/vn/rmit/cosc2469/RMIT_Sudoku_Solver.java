package vn.rmit.cosc2469;

import java.util.*;

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

    /**
     * Validates that the board is a correct Sudoku solution.
     * Each row, column, and 3x3 box must contain digits 1 through 9 without duplication.
     */
    public boolean isValidSudoku(int[][] board) {
        // Check rows
        for (int row = 0; row < SIZE; row++) {
            boolean[] seen = new boolean[SIZE + 1];
            for (int col = 0; col < SIZE; col++) {
                int val = board[row][col];
                if (val < 1 || val > 9 || seen[val]) return false;
                seen[val] = true;
            }
        }

        // Check columns
        for (int col = 0; col < SIZE; col++) {
            boolean[] seen = new boolean[SIZE + 1];
            for (int row = 0; row < SIZE; row++) {
                int val = board[row][col];
                if (val < 1 || val > 9 || seen[val]) return false;
                seen[val] = true;
            }
        }

        // Check 3x3 boxes
        for (int boxRow = 0; boxRow < SIZE; boxRow += BOX) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += BOX) {
                boolean[] seen = new boolean[SIZE + 1];
                for (int i = 0; i < BOX; i++) {
                    for (int j = 0; j < BOX; j++) {
                        int val = board[boxRow + i][boxCol + j];
                        if (val < 1 || val > 9 || seen[val]) return false;
                        seen[val] = true;
                    }
                }
            }
        }

        return true; // valid board
    }


    public int[][] solve(int[][] puzzle) {
        long startTime = System.currentTimeMillis();    // record start time for timeout check

        // Fixed cells
        boolean[][] fixed = new boolean[SIZE][SIZE];
        int[][] current = new int[SIZE][SIZE];

        // Step 1: Preprocess with AllDifferent (initialize rows with all 1–9 without repetition)
        // TODO: fine-tune, check row and col
        for (int row = 0; row < SIZE; row++) {
            List<Integer> available = new ArrayList<>();        // available numbers for this row
            for (int i = 1; i <= SIZE; i++) available.add(i);   // add 1 to 9

            for (int col = 0; col < SIZE; col++) {
                current[row][col] = puzzle[row][col];           // copy original puzzle value
                if (puzzle[row][col] != 0) {                    // if cell is pre-filled
                    fixed[row][col] = true;                     // mark as fixed
                    available.remove((Integer) puzzle[row][col]);   // remove value from availability
                }
            }

            for (int col = 0; col < SIZE; col++) {
                if (!fixed[row][col]) {
                    // fill empty cells randomly with remaining available numbers
                    current[row][col] = available.remove(random.nextInt(available.size()));
                }
            }
        }

        int[][] best = deepCopy(current);               // store best solution found so far
        int bestCost = calculateConflicts(best);        // cost (number of conflicts) of current board
        Set<String> tabuList = new LinkedHashSet<>();   // tabu list to avoid repeating bad moves

        // step 2: tabu search to improve solution
        // TODO: fine tune
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            if (System.currentTimeMillis() - startTime > TIME_LIMIT_MS) {
                throw new RuntimeException("Timeout: Couldn't solve puzzle within 2 minutes.");
            }

            if (bestCost == 0) break;   // puzzle is solved if no conflicts remain

            int[][] bestCandidate = null;
            int bestCandidateCost = Integer.MAX_VALUE;
            String bestMove = "";

            // try swapping two non-fixed cells in each row to create candidate neighbors
            for (int row = 0; row < SIZE; row++) {
                for (int col1 = 0; col1 < SIZE; col1++) {
                    for (int col2 = col1 + 1; col2 < SIZE; col2++) {
                        if (fixed[row][col1] || fixed[row][col2]) continue; // skip fixed cells

                        int[][] candidate = deepCopy(current);      // copy current board
                        swap(candidate[row], col1, col2);           // swap two cells

                        String move = row + "-" + col1 + "<->" + col2;      // encode the move
                        int cost = calculateConflicts(candidate);           // evaluate candidate

                        // only consider moves not in tabu list or better than current best
                        if (!tabuList.contains(move) || cost < bestCost) {
                            if (cost < bestCandidateCost) {
                                bestCandidate = candidate;
                                bestCandidateCost = cost;
                                bestMove = move;
                            }
                        }
                    }
                }
            }

            // accept the best neighbor move
            if (bestCandidate != null) {
                current = bestCandidate;
                if (bestCandidateCost <= bestCost) {
                    best = deepCopy(bestCandidate);     // update best solution
                    bestCost = bestCandidateCost;
                    tabuList.add(bestMove);             // add to tabu list
                    if (tabuList.size() > 500) {
                        // maintain max tabu list size
                        Iterator<String> it = tabuList.iterator();
                        it.next();
                        it.remove();
                    }
                }
            }
            stepCount++; // increment step count
        }

        return best;        // return the best solution found
    }

    /**
    * Deep copies a 2D array to avoid mutating original reference
     */
    private int[][] deepCopy(int[][] board) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) copy[i] = board[i].clone(); // clone each row
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

        // check for duplicate values in columns
        for (int col = 0; col < SIZE; col++) {
            boolean[] seen = new boolean[SIZE + 1]; // index 1-9
            for (int row = 0; row < SIZE; row++) {
                int val = board[row][col];
                if (seen[val]) conflicts++;         // count duplicate
                else seen[val] = true;
            }
        }

        // check for the duplicate values in 3x3 sub-boxes
        for (int boxRow = 0; boxRow < SIZE; boxRow += BOX) {
            for (int boxCol = 0; boxCol < SIZE; boxCol += BOX) {
                boolean[] seen = new boolean[SIZE + 1];     // index 1-9
                for (int i = 0; i < BOX; i++) {
                    for (int j = 0; j < BOX; j++) {
                        int val = board[boxRow + i][boxCol + j];
                        if (seen[val]) conflicts++;     // count duplicate
                        else seen[val] = true;
                    }
                }
            }
        }

        return conflicts;
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
