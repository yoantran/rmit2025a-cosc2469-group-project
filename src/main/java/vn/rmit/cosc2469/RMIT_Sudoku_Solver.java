package vn.rmit.cosc2469;

public abstract class RMIT_Sudoku_Solver {
    protected static final long TIME_LIMIT = 2 * 60 * 1000; // 2 minutes
    protected long startTime;

    public abstract int[][] solveSudoku(int[][] board);

    public int[][] solve(int[][] puzzle) {
        // Clone puzzle
        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(puzzle[r], 0, board[r], 0, 9);
        }

        return solveSudoku(board);
    }
}
