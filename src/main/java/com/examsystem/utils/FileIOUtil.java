package com.examsystem.utils;

import com.examsystem.entity.Student;
import java.io.*;
import java.util.Map;

/**
 * Utility class for File I/O operations.
 * Demonstrates BufferedReader/Writer for CSV file handling.
 */
public class FileIOUtil {
    
    /**
     * Saves exam results to CSV file.
     * Demonstrates BufferedWriter usage and file I/O.
     * 
     * @param filePath Output CSV file path
     * @param results Map of Student to score
     * @throws IOException if I/O error occurs
     */
    public static void saveResultsToCSV(String filePath, Map<Student, Integer> results) 
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write CSV header
            writer.write("ID,Name,Username,Score");
            writer.newLine();
            
            // Write student results
            for (Map.Entry<Student, Integer> entry : results.entrySet()) {
                Student student = entry.getKey();
                Integer score = entry.getValue();
                
                String line = String.format("%d,%s,%s,%d",
                    student.getId(),
                    escapeCsv(student.getName()),
                    escapeCsv(student.getUsername()),
                    score
                );
                
                writer.write(line);
                writer.newLine();
            }
            
            System.out.println("[FILE I/O] Results saved to CSV: " + filePath);
        }
    }
    
    /**
     * Escapes CSV special characters.
     */
    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    /**
     * Reads exam results from CSV file.
     * Demonstrates BufferedReader usage.
     * 
     * @param filePath Input CSV file path
     * @throws IOException if I/O error occurs
     */
    public static void readResultsFromCSV(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            
            System.out.println("[FILE I/O] Reading results from: " + filePath);
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    System.out.println("Header: " + line);
                } else {
                    System.out.println("Row " + lineNumber + ": " + line);
                }
            }
        }
    }
    
    /**
     * Appends log entry to file.
     * Demonstrates file append mode.
     */
    public static void appendLog(String filePath, String logEntry) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath, true))) { // true = append mode
            writer.write(logEntry);
            writer.newLine();
        }
    }
}
