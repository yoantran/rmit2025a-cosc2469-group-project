package vn.rmit.cosc2469;

public abstract class RMIT_Sudoku_Solver {
    protected SolverTimer timer;

    public RMIT_Sudoku_Solver() {
        this.timer = new SolverTimer();
    }

    public abstract int[][] solveSudoku(int[][] board);

    public int[][] solve(int[][] puzzle) {
        // Clone puzzle
        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(puzzle[r], 0, board[r], 0, 9);
        }

        timer.start();

        return solveSudoku(board);
    }
}
