package vn.rmit.cosc2469;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Sudoku_Tabu_Search_SolverTest {
    Sudoku_Tabu_Search_Solver solver;

    @BeforeEach
    void setUp() {
        solver = new Sudoku_Tabu_Search_Solver();
    }

    @AfterEach
    void tearDown() {
        solver = null;
    }

    @Test
    void solve() {
        // Easiest 1
        int[][] expected1 = {
                {4, 3, 5, 2, 6, 9, 7, 8, 1},
                {6, 8, 2, 5, 7, 1, 4, 9, 3},
                {1, 9, 7, 8, 3, 4, 5, 6, 2},
                {8, 2, 6, 1, 9, 5, 3, 4, 7},
                {3, 7, 4, 6, 8, 2, 9, 1, 5},
                {9, 5, 1, 7, 4, 3, 6, 2, 8},
                {5, 1, 9, 3, 2, 6, 8, 7, 4},
                {2, 4, 8, 9, 5, 7, 1, 3, 6},
                {7, 6, 3, 4, 1, 8, 2, 5, 9}
        };

        int[][] puzzle1 = SudokuSolverHelper.loadSudokuFromCSV("test-data/easiest1.csv");
        int[][] result1 = solver.solveUntilValid(puzzle1);
        System.out.println("Easiest Puzzle 1 solution:");
        System.out.println(solver.toString(result1));
        assertArrayEquals(expected1, result1);


        // Easiest 2
        int[][] expected2 = {
                {1, 5, 2, 4, 8, 9, 3, 7, 6},
                {7, 3, 9, 2, 5, 6, 8, 4, 1},
                {4, 6, 8, 3, 7, 1, 2, 9, 5},
                {3, 8, 7, 1, 2, 4, 6, 5, 9},
                {5, 9, 1, 7, 6, 3, 4, 2, 8},
                {2, 4, 6, 8, 9, 5, 7, 1, 3},
                {9, 1, 4, 6, 3, 7, 5, 8, 2},
                {6, 2, 5, 9, 4, 8, 1, 3, 7},
                {8, 7, 3, 5, 1, 2, 9, 6, 4}
        };
        int[][] puzzle2 = SudokuSolverHelper.loadSudokuFromCSV("test-data/easiest2.csv");
        int[][] result2 = solver.solveUntilValid(puzzle2);
        System.out.println("Easiest Puzzle 2 solution:");
        System.out.println(solver.toString(result2));
        assertArrayEquals(expected2, result2);


        // Intermidate
        int[][] expected3 = {
                {1, 2, 3, 6, 7, 8, 9, 4, 5},
                {5, 8, 4, 2, 3, 9, 7, 6, 1},
                {9, 6, 7, 1, 4, 5, 3, 2, 8},
                {3, 7, 2, 4, 6, 1, 5, 8, 9},
                {6, 9, 1, 5, 8, 3, 2, 7, 4},
                {4, 5, 8, 7, 9, 2, 6, 1, 3},
                {8, 3, 6, 9, 2, 4, 1, 5, 7},
                {2, 1, 9, 8, 5, 7, 4, 3, 6},
                {7, 4, 5, 3, 1, 6, 8, 9, 2}
        };
        int[][] puzzle3 = SudokuSolverHelper.loadSudokuFromCSV("test-data/intermediate.csv");
//        int[][] result3 = solver.solveUntilValid(puzzle3);
//        System.out.println("Intermediate Puzzle solution:");
//        System.out.println(solver.toString(result3));
//        assertArrayEquals(expected3, result3);


        // difficult 1
        int[][] expected4 = {
                {5, 8, 1, 6, 7, 2, 4, 3, 9},
                {7, 9, 2, 8, 4, 3, 6, 5, 1},
                {3, 6, 4, 5, 9, 1, 7, 8, 2},
                {4, 3, 8, 9, 5, 7, 2, 1, 6},
                {2, 5, 6, 1, 8, 4, 9, 7, 3},
                {1, 7, 9, 3, 2, 6, 8, 4, 5},
                {8, 4, 5, 2, 1, 9, 3, 6, 7},
                {9, 1, 3, 7, 6, 8, 5, 2, 4},
                {6, 2, 7, 4, 3, 5, 1, 9, 8},
        };
        int[][] puzzle4 = SudokuSolverHelper.loadSudokuFromCSV("test-data/difficult1.csv");
//        int[][] result4 = solver.solveUntilValid(puzzle4);
//        System.out.println("Difficult Puzzle 1 solution:");
//        System.out.println(solver.toString(result4));
//        assertArrayEquals(expected4, result4);


        // difficult 2
        int[][] expected5 = {
                {2, 7, 6, 3, 1, 4, 9, 5, 8},
                {8, 5, 4, 9, 6, 2, 7, 1, 3},
                {9, 1, 3, 8, 7, 5, 2, 6, 4},
                {4, 6, 8, 1, 2, 7, 3, 9, 5},
                {5, 9, 7, 4, 3, 8, 6, 2, 1},
                {1, 3, 2, 5, 9, 6, 4, 8, 7},
                {3, 2, 5, 7, 8, 9, 1, 4, 6},
                {6, 4, 1, 2, 5, 3, 8, 7, 9},
                {7, 8, 9, 6, 4, 1, 5, 3, 2},
        };
        int[][] puzzle5 = SudokuSolverHelper.loadSudokuFromCSV("test-data/difficult2.csv");
//        int[][] result5 = solver.solveUntilValid(puzzle5);
//        System.out.println("Difficult Puzzle 2 solution:");
//        System.out.println(solver.toString(result5));
//        assertArrayEquals(expected5, result5);

        // Not Fun
        int[][] expected6 = {
                {1, 2, 6, 4, 3, 7, 9, 5, 8},
                {8, 9, 5, 6, 2, 1, 4, 7, 3},
                {3, 7, 4, 9, 8, 5, 1, 2, 6},
                {4, 5, 7, 1, 9, 3, 8, 6, 2},
                {9, 8, 3, 2, 4, 6, 5, 1, 7},
                {6, 1, 2, 5, 7, 8, 3, 9, 4},
                {2, 6, 9, 3, 1, 4, 7, 8, 5},
                {5, 4, 8, 7, 6, 9, 2, 3, 1},
                {7, 3, 1, 8, 5, 2, 6, 4, 9},
        };
        int[][] puzzle6 = SudokuSolverHelper.loadSudokuFromCSV("test-data/notfun.csv");
//        int[][] result6 = solver.solveUntilValid(puzzle6);
//        System.out.println("Not Fun Puzzle solution:");
//        System.out.println(solver.toString(result6));
//        assertArrayEquals(expected6, result6);
    }
}