package vn.rmit.cosc2469;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RMIT_Sudoku_SolverTest {
    // Test cases for easy puzzle
    @Test
    public void testEasy() {
        int[][] puzzle = {
                { 0, 0, 0, 2, 6, 0, 7, 0, 1 },
                { 6, 8, 0, 0, 7, 0, 0, 9, 0 },
                { 1, 9, 0, 0, 0, 4, 5, 0, 0 },
                { 8, 2, 0, 1, 0, 0, 0, 4, 0 },
                { 0, 0, 4, 6, 0, 2, 9, 0, 0 },
                { 0, 5, 0, 0, 0, 3, 0, 2, 8 },
                { 0, 0, 9, 3, 0, 0, 0, 7, 4 },
                { 0, 4, 0, 0, 5, 0, 0, 3, 6 },
                { 7, 0, 3, 0, 1, 8, 0, 0, 0 }
        };
        RMIT_Sudoku_Solver solver = new FullBacktrackingSolver();
        int[][] solved = solver.solve(puzzle);

        boolean isValid = SudokuValidator.isValidSudoku(solved);
        assertTrue(isValid, "Solution is not valid");
    }

    // Test case for intermediate puzzle
    @Test
    public void testIntermediate() {
        int[][] puzzle = {
                { 0, 2, 0, 6, 0, 8, 0, 0, 0 },
                { 5, 8, 0, 0, 0, 9, 7, 0, 0 },
                { 0, 0, 0, 0, 4, 0, 0, 0, 0 },
                { 3, 7, 0, 0, 0, 0, 5, 0, 0 },
                { 6, 0, 0, 0, 0, 0, 0, 0, 4 },
                { 0, 0, 8, 0, 0, 0, 0, 1, 3 },
                { 0, 0, 0, 0, 2, 0, 0, 0, 0 },
                { 0, 0, 9, 8, 0, 0, 0, 3, 6 },
                { 0, 0, 0, 3, 0, 6, 0, 9, 0 },
        };
        RMIT_Sudoku_Solver solver = new FullBacktrackingSolver();
        int[][] solved = solver.solve(puzzle);

        boolean isValid = SudokuValidator.isValidSudoku(solved);
        assertTrue(isValid, "Solution is not valid");
    }

    // Test case for difficult puzzle
    @Test
    public void testDifficult() {
        int[][] puzzle = {
                { 0, 0, 0, 6, 0, 0, 4, 0, 0 },
                { 7, 0, 0, 0, 0, 3, 6, 0, 0 },
                { 0, 0, 0, 0, 9, 1, 0, 8, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 5, 0, 1, 8, 0, 0, 0, 3 },
                { 0, 0, 0, 3, 0, 6, 0, 4, 5 },
                { 0, 4, 0, 2, 0, 0, 0, 6, 0 },
                { 9, 0, 3, 0, 0, 0, 0, 0, 0 },
                { 0, 2, 0, 0, 0, 0, 1, 0, 0 },
        };
        RMIT_Sudoku_Solver solver = new FullBacktrackingSolver();
        int[][] solved = solver.solve(puzzle);

        boolean isValid = SudokuValidator.isValidSudoku(solved);
        assertTrue(isValid, "Solution is not valid");
    }

    // Test case for not fun puzzle
    @Test
    public void testNotFun() {
        int[][] puzzle = {
                { 0, 2, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 6, 0, 0, 0, 0, 3 },
                { 0, 7, 4, 0, 8, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 3, 0, 0, 2 },
                { 0, 8, 0, 0, 4, 0, 0, 1, 0 },
                { 6, 0, 0, 5, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 7, 8, 0 },
                { 5, 0, 0, 0, 0, 9, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 4, 0 },
        };
        RMIT_Sudoku_Solver solver = new FullBacktrackingSolver();
        int[][] solved = solver.solve(puzzle);

        boolean isValid = SudokuValidator.isValidSudoku(solved);
        assertTrue(isValid, "Solution is not valid");
    }

    // Test case for empty puzzle
    @Test
    public void testEmptyPuzzle() {
        int[][] puzzle = {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 }
        };
        RMIT_Sudoku_Solver solver = new FullBacktrackingSolver();
        int[][] solved = solver.solve(puzzle);
        assertTrue(SudokuValidator.isValidSudoku(solved));
    }

    // Test case for unsolvable puzzle
    @Test
    public void testUnsolvablePuzzle() {
        int[][] puzzle = {
                { 5, 1, 6, 8, 4, 9, 7, 3, 2 },
                { 3, 0, 7, 6, 0, 5, 0, 0, 0 },
                { 8, 0, 9, 7, 0, 0, 0, 6, 5 },
                { 1, 3, 5, 0, 6, 0, 9, 0, 7 },
                { 4, 7, 2, 5, 9, 1, 0, 0, 6 },
                { 9, 6, 8, 3, 7, 0, 0, 5, 0 },
                { 2, 5, 3, 1, 8, 6, 0, 7, 4 },
                { 6, 8, 4, 2, 0, 7, 5, 0, 0 },
                { 7, 9, 1, 0, 5, 0, 6, 0, 8 }
        };
        RMIT_Sudoku_Solver solver = new FullBacktrackingSolver();
        // Expect exception or null result
        assertThrows(Exception.class, () -> solver.solve(puzzle));
    }

    // Test case for a puzzle containing invalid input value
    @Test
    public void testInvalidInputValues() {
        int[][] puzzle = {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 10, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 }
        };
        boolean isValid = SudokuValidator.isValidSudoku(puzzle);
        assertFalse(isValid, "Puzzle must contain invalid values");
    }

    // Test case for a puzzle containing duplicate values in a row
    @Test
    public void testInvalidSudoku() {
        int[][] puzzle = {
                { 2, 2, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 6, 0, 0, 0, 0, 3 },
                { 0, 7, 4, 0, 8, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 3, 0, 0, 2 },
                { 0, 8, 0, 0, 4, 0, 0, 1, 0 },
                { 6, 0, 0, 5, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 1, 0, 7, 8, 0 },
                { 5, 0, 0, 0, 0, 9, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 4, 0 },
        };
        boolean isValid = SudokuValidator.isValidSudoku(puzzle);
        assertFalse(isValid, "Puzzle must contain duplicate values in a row");
    }

    // Test case to ensure the input values are preserved in the solved puzzle
    @Test
    public void testInputPreservation() {
        int[][] puzzle = {
                { 0, 2, 0, 6, 0, 8, 0, 0, 0 },
                { 5, 8, 0, 0, 0, 9, 7, 0, 0 },
                { 0, 0, 0, 0, 4, 0, 0, 0, 0 },
                { 3, 7, 0, 0, 0, 0, 5, 0, 0 },
                { 6, 0, 0, 0, 0, 0, 0, 0, 4 },
                { 0, 0, 8, 0, 0, 0, 0, 1, 3 },
                { 0, 0, 0, 0, 2, 0, 0, 0, 0 },
                { 0, 0, 9, 8, 0, 0, 0, 3, 6 },
                { 0, 0, 0, 3, 0, 6, 0, 9, 0 }
        };

        RMIT_Sudoku_Solver solver = new FullBacktrackingSolver();
        int[][] solved = solver.solve(puzzle);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (puzzle[i][j] != 0) {
                    assertEquals(puzzle[i][j], solved[i][j]);
                }
            }
        }
    }
}
