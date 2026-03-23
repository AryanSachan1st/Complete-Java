package FileHandling.Topic_03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReading {
    public static void main(String[] args) {

        // Method-1: Using Scanner for reading files (reads entire file at once)
        try (Scanner sc = new Scanner(new File("testfile.txt"))) {
            while (sc.hasNextLine()) {
                System.out.print(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found: " + e.getMessage());
        }
        System.out.println();

        // Method-2: Using FileReader to read one character at a time [time consuming]
        try (FileReader fr = new FileReader("testfile.txt")) {
            int ch;
            while ((ch = fr.read()) != -1) { // -1 means EOF (end of file)
                System.out.print((char) ch);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        System.out.println();

        // Method-3: Using BufferedReader to read one line at a time [most used]
        try (BufferedReader bfr = new BufferedReader(new FileReader("testfile.txt"))) {
            String line;
            List<String> allLines = new ArrayList<>();

            while ((line = bfr.readLine()) != null) {
                bfr.mark(10); // bookmark
                allLines.add(line);
                bfr.reset(); // set the cursor back to the first bookmark from cursor's current position
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }

        // Method-4: Modern way (Java 8+)
        // Note: It loads entire file into the RAM, so avoid it for very large files
        try {
            List<String> allLines = Files.readAllLines(Paths.get("testfile.txt"));
            allLines.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }
}
