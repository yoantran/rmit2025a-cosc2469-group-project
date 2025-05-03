package vn.rmit.cosc2469;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RMIT_Sudoku_SolverTest {
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

}
