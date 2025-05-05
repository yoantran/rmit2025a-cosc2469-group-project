package vn.rmit.cosc2469;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Populate {
    private static String folderPath = "test-data/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get the file name without extension
        System.out.print("Enter the input file name (without .txt extension): ");
        String fileName = scanner.nextLine();

        // Get number of puzzles to process
        System.out.print("Enter how many puzzles to process: ");
        int numPuzzles = scanner.nextInt();

        try {
            // Open the input file
            BufferedReader reader = new BufferedReader(new FileReader(folderPath + fileName + ".txt"));

            // Process specified number of puzzles
            for (int i = 1; i <= numPuzzles; i++) {
                String line = reader.readLine();
                if (line == null) {
                    System.out.println("Reached end of file after " + (i - 1) + " puzzles.");
                    break;
                }

                // Process the puzzle and save to CSV
                processPuzzle(line, fileName, i);
                System.out.println("Saved puzzle " + i + " to " + folderPath + fileName + i + ".csv");
            }

            reader.close();
            System.out.println("All puzzles processed successfully!");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }

    // Process a single puzzle line and save as CSV
    private static void processPuzzle(String puzzleLine, String baseName, int puzzleNum) throws IOException {
        // Open output file
        FileWriter writer = new FileWriter(folderPath + baseName + puzzleNum + ".csv");

        // Convert the line to a 9x9 sudoku grid
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int index = row * 9 + col;
                char value = puzzleLine.charAt(index);
                if (value == '.') {
                    value = '0';
                }

                writer.write(value);

                // Add comma except for the last column
                if (col < 8) {
                    writer.write(",");
                }
            }
            // Add newline after each row
            writer.write("\n");
        }

        writer.close();
    }
}
