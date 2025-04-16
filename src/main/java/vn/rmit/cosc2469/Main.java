package vn.rmit.cosc2469;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        // Define a Sudoku puzzle (0 represents empty cells)
        int[][] puzzle1 = {
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
        int[][] puzzle2 = {
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
        int[][] puzzle3 = {
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
        int[][] puzzle4 = {
                {0, 0, 0, 6, 0, 0, 4, 0, 0},
                {7, 0, 0, 0, 0, 3, 6, 0, 0},
                {0, 0, 0, 0, 9, 1, 0, 8, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 5, 0, 1, 8, 0, 0, 0, 3},
                {0, 0, 0, 3, 0, 6, 0, 4, 5},
                {0, 4, 0, 2, 0, 0, 0, 6, 0},
                {9, 0, 3, 0, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0, 1, 0, 0},
        };
        int[][] puzzle5 = {
                {2, 0, 0, 3, 0, 0, 0, 0, 0},
                {8, 0, 4, 0, 6, 2, 0, 0, 3},
                {0, 1, 3, 8, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 2, 0, 3, 9, 0},
                {5, 0, 7, 0, 0, 0, 6, 2, 1},
                {0, 3, 2, 0, 0, 6, 0, 0, 0},
                {0, 2, 0, 0, 0, 9, 1, 4, 0},
                {6, 0, 1, 2, 5, 0, 8, 0, 9},
                {0, 0, 0, 0, 0, 1, 0, 0, 2},
        };
        int[][] puzzle6 = {
                {0, 2, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 6, 0, 0, 0, 0, 3},
                {0, 7, 4, 0, 8, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 3, 0, 0, 2},
                {0, 8, 0, 0, 4, 0, 0, 1, 0},
                {6, 0, 0, 5, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 7, 8, 0},
                {5, 0, 0, 0, 0, 9, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 4, 0},
        };

        // Create a solver instance
        RMIT_Sudoku_Solver solver = new RMIT_Sudoku_Solver();

        // Solve the puzzle using Tabu Search
        int[][] solution1 = solver.solve(puzzle1);
        int[][] solution2 = solver.solve(puzzle2);
        int[][] solution3 = solver.solve(puzzle3);
        int[][] solution4 = solver.solve(puzzle4);
        int[][] solution5 = solver.solve(puzzle5);
        int[][] solution6 = solver.solve(puzzle6);

        System.out.println( "wildcatjan17 took " + solver.getStepCount() + " steps: " );
        System.out.println(solver.toString(solution1));
        if (SudokuValidator.isValidSudoku(solution1)) {
            System.out.println("The solution is valid.");
        } else {
            System.out.println("The solution is INVALID.");
        }
        System.out.println( "wildcatjan18 took " + solver.getStepCount() + " steps: " );
        System.out.println(solver.toString(solution2));
        if (SudokuValidator.isValidSudoku(solution2)) {
            System.out.println("The solution is valid.");
        } else {
            System.out.println("The solution is INVALID.");
        }
        System.out.println( "dtfeb19 took " + solver.getStepCount() + " steps: " );
        System.out.println(solver.toString(solution3));
        if (SudokuValidator.isValidSudoku(solution3)) {
            System.out.println("The solution is valid.");
        } else {
            System.out.println("The solution is INVALID.");
        }
        System.out.println( "v2155141 took " + solver.getStepCount() + " steps: " );
        System.out.println(solver.toString(solution4));
        if (SudokuValidator.isValidSudoku(solution4)) {
            System.out.println("The solution is valid.");
        } else {
            System.out.println("The solution is INVALID.");
        }
        System.out.println( "challenge2 took " + solver.getStepCount() + " steps: " );
        System.out.println(solver.toString(solution5));
        if (SudokuValidator.isValidSudoku(solution5)) {
            System.out.println("The solution is valid.");
        } else {
            System.out.println("The solution is INVALID.");
        }
        System.out.println( "challenge1 took " + solver.getStepCount() + " steps: " );
        System.out.println(solver.toString(solution6));
        if (SudokuValidator.isValidSudoku(solution6)) {
            System.out.println("The solution is valid.");
        } else {
            System.out.println("The solution is INVALID.");
        }
    }
}