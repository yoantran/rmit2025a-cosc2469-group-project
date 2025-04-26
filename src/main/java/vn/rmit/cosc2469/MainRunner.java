package vn.rmit.cosc2469;

public class MainRunner {
    public static void main(String[] args) {
        String filePath = "test-data/intermediate.csv"; // Change this to try another puzzle

        try {
            int[][] puzzle = SudokuSolverHelper.loadSudokuFromCSV(filePath);

            // Track memory and time
            long startTime = System.nanoTime();
            long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // Prepare logger with filename
            SolverLogger logger = new SolverLogger(filePath);

            // Set up solver and inject logger
            RMIT_Sudoku_Solver solver = new RMIT_Sudoku_Solver();
            solver.setLogger(logger);

            // Solve
            System.out.println("Original Puzzle:");
            printBoard(puzzle);

            int[][] solved = solver.solve(puzzle);

            long endTime = System.nanoTime();
            long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // Output solved puzzle
            System.out.println("\nSolved Puzzle:");
            printBoard(solved);

            // Validate
            boolean isValid = SudokuSolverHelper.isValidSudoku(solved);
            System.out.println("\n✅ Is solution valid? " + isValid);

            // Save step-by-step log
            logger.saveToCSV();

            // Time and memory
            double durationMs = (endTime - startTime) / 1_000_000.0;
            double memoryKB = (endMemory - startMemory) / 1024.0;

            System.out.printf("🕒 Time taken: %.2f ms\n", durationMs);
            System.out.printf("📦 Memory used: %.2f KB\n", memoryKB);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printBoard(int[][] board) {
        for (int row = 0; row < 9; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("------+-------+------");
            }
            for (int col = 0; col < 9; col++) {
                if (col % 3 == 0 && col != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[row][col] == 0 ? ". " : board[row][col] + " ");
            }
            System.out.println();
        }
    }
}
