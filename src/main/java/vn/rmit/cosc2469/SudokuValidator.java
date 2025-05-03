package vn.rmit.cosc2469;

public class SudokuValidator {
    public static boolean isValidSudoku(int[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] row = new boolean[10];
            boolean[] col = new boolean[10];
            boolean[] box = new boolean[10];

            for (int j = 0; j < 9; j++) {
                int r = board[i][j];
                if (r != 0 && row[r])
                    return false;
                if (r != 0)
                    row[r] = true;

                int c = board[j][i];
                if (c != 0 && col[c])
                    return false;
                if (c != 0)
                    col[c] = true;

                int rowIndex = 3 * (i / 3) + j / 3;
                int colIndex = 3 * (i % 3) + j % 3;
                int b = board[rowIndex][colIndex];
                if (b != 0 && box[b])
                    return false;
                if (b != 0)
                    box[b] = true;
            }
        }
        return true;
    }
}