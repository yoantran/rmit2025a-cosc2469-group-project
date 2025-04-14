package vn.rmit.algorithms;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class RMIT_Sudoku_SolverTest {

    @Test
    public void testEasiest1() throws IOException {
        int[][] puzzle = SudokuSolverHelper.readSudokuFromCSV("test-data/easiest1.csv");
        RMIT_Sudoku_Solver solver = new RMIT_Sudoku_Solver();
        int[][] solution = solver.solve(puzzle);

        // Check no zeros
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                assertTrue(solution[r][c] != 0);
            }
        }
    }
    // Add more tests for other CSVs if you wish
}
