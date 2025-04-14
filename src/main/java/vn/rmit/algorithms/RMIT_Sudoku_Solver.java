package vn.rmit.algorithms;

public class RMIT_Sudoku_Solver {

    public int[][] solve(int[][] puzzle) {
        // Copy puzzle to avoid mutating original
        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(puzzle[r], 0, board[r], 0, 9);
        }

        SudokuRelationResidualSolver solver = new SudokuRelationResidualSolver();
        solver.solveInPlace(board);

        return board;
    }
}
