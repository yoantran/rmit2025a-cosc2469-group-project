package vn.rmit.algorithms;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainRunner {

    public static void main(String[] args) {
        // We'll store puzzle files in a list
        List<String> puzzleFiles = Arrays.asList(
                "test-data/easiest1.csv",
                "test-data/easiest2.csv",
                "test-data/intermediate.csv",
                "test-data/difficult1.csv",
                "test-data/difficult2.csv",
                "test-data/notfun.csv"
        );

        // Print a menu
        System.out.println("Choose a puzzle to solve (1..6):");
        for (int i = 0; i < puzzleFiles.size(); i++) {
            System.out.println((i+1) + ". " + puzzleFiles.get(i));
        }
        System.out.println("Or 0 to solve all in sequence.");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        sc.close();

        if (choice == 0) {
            // solve all
            for (String filePath : puzzleFiles) {
                solveAndPrint(filePath);
            }
        } else if (choice >= 1 && choice <= puzzleFiles.size()) {
            // solve chosen
            String filePath = puzzleFiles.get(choice - 1);
            solveAndPrint(filePath);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void solveAndPrint(String filePath) {
        System.out.println("\nSolving: " + filePath);
        try {
            int[][] puzzle = SudokuSolverHelper.readSudokuFromCSV(filePath);

            RMIT_Sudoku_Solver solver = new RMIT_Sudoku_Solver();
            int[][] solution = solver.solve(puzzle);

            System.out.println("Solution for " + filePath + ":");
            SudokuSolverHelper.printSudoku(solution);
        } catch (IOException e) {
            System.out.println("I/O Error reading puzzle: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed to solve " + filePath + ": " + e.getMessage());
        }
    }
}
