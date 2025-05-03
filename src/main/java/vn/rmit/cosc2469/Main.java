package vn.rmit.cosc2469;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        String filePath = "test-data/easy.csv"; // Change this to try another puzzle

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null && !line.equals("")) {
                int[][] puzzle = new int[9][9];
                int row = 0;

                String[] firstLineTokens = line.split(",");
                for (int col = 0; col < firstLineTokens.length && col < 9; col++) {
                    puzzle[row][col] = Integer.parseInt(firstLineTokens[col].trim());
                }
                row++;

                while ((line = br.readLine()) != null && !line.equals("")) {
                    String[] tokens = line.split(",");
                    for (int col = 0; col < tokens.length && col < 9; col++) {
                        puzzle[row][col] = Integer.parseInt(tokens[col].trim());
                    }
                    row++;
                }

                // Prepare logger with filename
                SolverLogger logger = new SolverLogger(filePath);

                // Set up solver and inject logger
                RMIT_Sudoku_Solver solver = new FullBacktrackingSolver();
                ((FullBacktrackingSolver) solver).setLogger(logger);

                // Solve
                System.out.println("Original Puzzle:");
                printBoard(puzzle);

                // Validate
                boolean originalIsValid = SudokuValidator.isValidSudoku(puzzle);
                System.out.println("\n✅ Is original puzzle valid? " + originalIsValid);

                if (!originalIsValid) {
                    System.out.println("❌ Original puzzle is invalid, skipping...");
                    continue;
                }

                // Start measurements here
                System.gc();
                long startTime = System.nanoTime();
                long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                int[][] solved = solver.solve(puzzle);

                // End measurements immediately after solving
                long endTime = System.nanoTime();
                long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                // Output solved puzzle
                System.out.println("\nSolved Puzzle:");
                printBoard(solved);

                // Validate
                boolean isValid = SudokuValidator.isValidSudoku(solved);
                System.out.println("\n✅ Is solution valid? " + isValid);

                // Save step-by-step log
                logger.saveToCSV();

                // Time and memory
                double durationMs = (endTime - startTime) / 1_000_000.0;
                double memoryKB = (endMemory - startMemory) / 1024.0;

                System.out.printf("🕒 Time taken: %.2f ms\n", durationMs);
                System.out.printf("📦 Memory used: %.2f KB\n\n", memoryKB);
            }
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
