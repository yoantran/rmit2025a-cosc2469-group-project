package vn.rmit.cosc2469;

/**
 * Main class
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("RMIT Sudoku Solver using Constraint Programming");
        System.out.println("==============================================\n");

        // Create the solver
        RMIT_Sudoku_Solver solver = new RMIT_Sudoku_Solver();

        // Define some sample puzzles
        int[][] easyPuzzle = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        int[][] wildcatJan17 = {
                {0, 0, 0, 2, 6, 0, 7, 0, 1},
                {6, 8, 0, 0, 7, 0, 0, 9, 0},
                {1, 9, 0, 0, 0, 4, 5, 0, 0},
                {8, 2, 0, 1, 0, 0, 0, 4, 0},
                {0, 0, 4, 6, 0, 2, 9, 0, 0},
                {0, 5, 0, 0, 0, 3, 0, 2, 8},
                {0, 0, 9, 3, 0, 0, 0, 7, 4},
                {0, 4, 0, 0, 5, 0, 0, 3, 6},
                {7, 0, 3, 0, 1, 8, 0, 0, 0}
        };

        int[][] wildcatJan18 = {
                {1, 0, 0, 4, 8, 9, 0, 0, 6},
                {7, 3, 0, 0, 0, 0, 0, 4, 0},
                {0, 0, 0, 0, 0, 1, 2, 9, 5},
                {0, 0, 7, 1, 2, 0, 6, 0, 0},
                {5, 0, 0, 7, 0, 3, 0, 0, 8},
                {0, 0, 6, 0, 9, 5, 7, 0, 0},
                {9, 1, 4, 6, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0, 0, 3, 7},
                {8, 0, 0, 5, 1, 2, 0, 0, 4}
        };

        int[][] intermediatePuzzle = {
                {0, 2, 0, 6, 0, 8, 0, 0, 0},
                {5, 8, 0, 0, 0, 9, 7, 0, 0},
                {0, 0, 0, 0, 4, 0, 0, 0, 0},
                {3, 7, 0, 0, 0, 0, 5, 0, 0},
                {6, 0, 0, 0, 0, 0, 0, 0, 4},
                {0, 0, 8, 0, 0, 0, 0, 1, 3},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 9, 8, 0, 0, 0, 3, 6},
                {0, 0, 0, 3, 0, 6, 0, 9, 0}
        };

        int[][] hardPuzzle = {
                {0, 0, 0, 6, 0, 0, 4, 0, 0},
                {7, 0, 0, 0, 0, 3, 6, 0, 0},
                {0, 0, 0, 0, 9, 1, 0, 8, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 5, 0, 1, 8, 0, 0, 0, 3},
                {0, 0, 0, 3, 0, 6, 0, 4, 5},
                {0, 4, 0, 2, 0, 0, 0, 6, 0},
                {9, 0, 3, 0, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0, 1, 0, 0}
        };

        int[][] notFunPuzzle = {
                {0, 2, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 6, 0, 0, 0, 0, 3},
                {0, 7, 4, 0, 8, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 3, 0, 0, 2},
                {0, 8, 0, 0, 4, 0, 0, 1, 0},
                {6, 0, 0, 5, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 7, 8, 0},
                {5, 0, 0, 0, 0, 9, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 4, 0}
        };

        // Solve and display results for easy puzzle
        System.out.println("Solving easy Puzzle:");
        System.out.println("Original puzzle:");
        printBoard(easyPuzzle);

        long startTime1 = System.currentTimeMillis();
        int[][] solution1 = solver.solve(easyPuzzle);
        long endTime1 = System.currentTimeMillis();

        System.out.println("\nSolution (solved in " + (endTime1 - startTime1) + " ms):");
        printBoard(solution1);

        // Solve and display results for wildcatJan17 puzzle
        // Algorithm returns correct solution
        System.out.println("Solving wildcatJan17 Puzzle:");
        System.out.println("Original puzzle:");
        printBoard(wildcatJan17);

        long startTime2 = System.currentTimeMillis();
        int[][] solution2 = solver.solve(wildcatJan17);
        long endTime2 = System.currentTimeMillis();

        System.out.println("\nSolution (solved in " + (endTime2 - startTime2) + " ms):");
        printBoard(solution2);

        // Solve and display results for wildcatJan18 puzzle
        // Algorithm returns correct solution
        System.out.println("\n\nSolving wildcatJan18 Puzzle:");
        System.out.println("Original puzzle:");
        printBoard(wildcatJan18);

        long startTime3 = System.currentTimeMillis();
        int[][] solution3 = solver.solve(wildcatJan18);
        long endTime3 = System.currentTimeMillis();

        System.out.println("\nSolution (solved in " + (endTime3 - startTime3) + " ms):");
        printBoard(solution3);

        // Solve and display results for intermediate puzzle
        // Algorithm returns correct solution
        System.out.println("\n\nSolving intermediate Puzzle:");
        System.out.println("Original puzzle:");
        printBoard(intermediatePuzzle);

        long startTime4 = System.currentTimeMillis();
        int[][] solution4 = solver.solve(intermediatePuzzle);
        long endTime4 = System.currentTimeMillis();

        System.out.println("\nSolution (solved in " + (endTime4 - startTime4) + " ms):");
        printBoard(solution4);

        // Solve and display results for hard puzzle
        // Algorithm returns correct solution
        System.out.println("\n\nSolving hard Puzzle:");
        System.out.println("Original puzzle:");
        printBoard(hardPuzzle);

        long startTime5 = System.currentTimeMillis();
        int[][] solution5 = solver.solve(hardPuzzle);
        long endTime5 = System.currentTimeMillis();

        System.out.println("\nSolution (solved in " + (endTime5 - startTime5) + " ms):");
        printBoard(solution5);


        // Solve and display results for not fun puzzle
        // Algorithm returns wrong solution
        System.out.println("\n\nSolving not fun Puzzle:");
        System.out.println("Original puzzle:");
        printBoard(notFunPuzzle);

        long startTime6 = System.currentTimeMillis();
        int[][] solution6 = solver.solve(notFunPuzzle);
        long endTime6 = System.currentTimeMillis();

        System.out.println("\nSolution (solved in " + (endTime6 - startTime6) + " ms):");
        printBoard(solution6);

    }

    /**
     * Utility method to print a Sudoku board
     */
    private static void printBoard(int[][] board) {
        System.out.println("-------------------------");
        for (int i = 0; i < 9; i++) {
            System.out.print("| ");
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
                if ((j + 1) % 3 == 0) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if ((i + 1) % 3 == 0) {
                System.out.println("-------------------------");
            }
        }
    }
}