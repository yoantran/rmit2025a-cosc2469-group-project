package vn.rmit.cosc2469;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

import vn.rmit.cosc2469.DancingLinks.DancingNode;

public class Examples {
    private static int[][] fromString(String s) {
        int[][] board = new int[9][9];
        for (int i = 0; i < 81; i++) {
            char c = s.charAt(i);
            int row = i / 9;
            int col = i % 9;
            if (c != '.') {
                board[row][col] = c - '0';
            }
        }
        return board;
    }

    private static void printSolution(int[][] result) {
        int N = result.length;
        for (int i = 0; i < N; i++) {
            String ret = "";
            for (int j = 0; j < N; j++) {
                ret += result[i][j] + " ";
            }
            System.out.println(ret);
        }
        System.out.println();
    }

    private static void printStats(List<Long> timings) {
        long min = timings.get(0);
        long max = timings.get(0);

        long sum = 0;
        long sqsum = 0;

        for (long ll : timings) {
            min = Math.min(min, ll);
            max = Math.max(max, ll);
            sum += ll;
        }

        double avg = sum / timings.size();

        for (long ll : timings) {
            sqsum += (ll - avg) * (ll - avg);
        }

        double std = Math.sqrt(sqsum / timings.size());

        System.out.printf("min: %.3f\n", min * 1e-6);
        System.out.printf("max: %.3f\n", max * 1e-6);
        System.out.printf("avg: %.3f\n", avg * 1e-6);
        System.out.printf("std: %.3f\n", std * 1e-6);
    }

    public static void runCoverExample() {
        int[][] example = { { 0, 0, 1, 0, 1, 1, 0 },
                { 1, 0, 0, 1, 0, 0, 1 },
                { 0, 1, 1, 0, 0, 1, 0 },
                { 1, 0, 0, 1, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 0, 1 },
                { 0, 0, 0, 1, 1, 0, 1 }
        };

        DancingLinks DLX = new DancingLinks(example);
        List<DancingNode> result = DLX.runSolver();
        for (DancingNode node : result) {
            System.out.println(node.C.name);
        }
    }

    public static void runSudokuExample() {
        int[][] hardest = {
                { 8, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 3, 6, 0, 0, 0, 0, 0 },
                { 0, 7, 0, 0, 9, 0, 2, 0, 0 },
                { 0, 5, 0, 0, 0, 7, 0, 0, 0 },
                { 0, 0, 0, 0, 4, 5, 7, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0, 3, 0 },
                { 0, 0, 1, 0, 0, 0, 0, 6, 8 },
                { 0, 0, 8, 5, 0, 0, 0, 1, 0 },
                { 0, 9, 0, 0, 0, 0, 4, 0, 0 }
        }; // apparently the hardest sudoku

        SudokuDLX sudoku = new SudokuDLX();
        int[][] result = sudoku.solve(hardest);
        printSolution(result);
    }

    public static void runExample() {

        String[] diffs = { "simple.txt", "easy.txt", "intermediate.txt", "expert.txt" };

        BufferedReader reader = null;
        String text = null;

        for (String diff : diffs) {

            String filename = "src/main/resources/" + diff;

            List<Long> timings = new ArrayList<Long>();

            try {
                reader = new BufferedReader(new FileReader(filename));

                while ((text = reader.readLine()) != null) {

                    int[][] sudoku = fromString(text);

                    SudokuDLX solver = new SudokuDLX();

                    long milis = System.nanoTime();

                    int[][] result = solver.solve(sudoku);

                    long elapsed = System.nanoTime() - milis;

                    timings.add(elapsed);

                    printSolution(result);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("STATS: " + diff + "\n");
            printStats(timings);
        }
    }

    public static void main(String[] args) {
        // runCoverExample();
        // runSudokuExample();

        runExample();
    }

}
