package vn.rmit.cosc2469;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Sudoku_Tabu_Search_SolverTest {
    RMIT_Sudoku_Solver solver;

    @BeforeEach
    void setUp() {
        solver = new RMIT_Sudoku_Solver();
    }

    @AfterEach
    void tearDown() {
        solver = null;
    }

    @Test
    void solve() {
        String filePath = "test-data/intermediate.csv"; // Change this to try another puzzle

    }
}