package vn.rmit.cosc2469;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SolverLogger {
    private final List<String> steps = new ArrayList<>();
    private final String outputFilePath;

    public SolverLogger(String puzzleFilePath) {
        // "test-data/difficult1.csv" -> "difficult1_steps.csv"
        String baseName = new File(puzzleFilePath).getName().replace(".csv", "");
        this.outputFilePath = "output/" + baseName + "_steps.csv";
    }

    public void logStep(String message) {
        steps.add(message);
    }

    public void saveToCSV() {
        File file = new File(outputFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < steps.size(); i++) {
                writer.write(steps.get(i));
                writer.newLine();
            }
            writer.flush();
            System.out.println("✅ Step-by-step log saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("❌ Error writing log file: " + e.getMessage());
        }
    }
}
