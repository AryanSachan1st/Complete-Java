package FileHandling.Topic_04;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

// We will be using new I/O API

public class WorkingWithTextFiles {
    public static void main(String[] args) {

        // Reading
        // Method-1: Reading an entire text file all at once (use when file is small)
        Path path = Path.of("testfile.txt");
        try {
            // Reads the entire file into one String — simple and clean
            String content = Files.readString(path, StandardCharsets.UTF_8);
            System.out.println(content);

        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }

        // Method-2: Read line by line (use when reading small csv, log files, etc)
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.forEach(System.out::println);

        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        
        // Method-3: Stream character by character
        try (BufferedReader bfr = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            List<String> lines = new ArrayList<>();
            String line = null;

            while ((line = bfr.readLine()) != null) {
                lines.add(line);
            }
            lines.forEach(System.out::println);

        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }

        // Writing (whereToWrite, whatToWrite, options(standardCharsets, standardOpenOptions))
        Path outputPath = Path.of("testfile.txt");

        // Method-1: Write whole string at once
        String content = "I am writing this from Method-1 of writing in a text file";
        try {
            Files.writeString(outputPath, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        
        // Method-2: Writing multiple lines from list
        List<String> lines = List.of("Alice", "Bob", "Charlie");
        try {
            Files.write(outputPath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        
        // Method-3: Append to an existing file (don't overwrite)
        try {
            Files.writeString(outputPath, "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }

        // Method-4: BufferedWriter for large/streaming writes
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            writer.write("Line 1");
            writer.newLine(); // best approach to write new lines
            writer.write("Line 2");
            writer.newLine();

        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }

        // BOM (Byte Order Mark) Awareness
        // Example: Windows Notepad saves "Hello" as UTF-8 with BOM
        // File bytes: EF BB BF 48 65 6C 6C 6F
        //             ^^^BOM^^^ H  e  l  l  o

        String content1;
        try {
            content1 = Files.readString(path, StandardCharsets.UTF_8);
            @SuppressWarnings("unused")
            String bomRemovedContent = readWithoutBOM(path);

            System.out.println(content1.charAt(0));        // prints: ﻿ (invisible BOM!)
            System.out.println(content1.startsWith("H"));  // false! (BOM is before H)
    
            // ✅ Fix: Strip BOM if present
            if (content1.startsWith("\uFEFF")) {
                content1 = content1.substring(1);  // remove the first character (BOM)
            }

        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }

    // Utility method for BOM removal:
    public static String readWithoutBOM(Path path) throws IOException {
        String content = Files.readString(path, StandardCharsets.UTF_8);
        return content.startsWith("\uFEFF") ? content.substring(1) : content;
    }
}
