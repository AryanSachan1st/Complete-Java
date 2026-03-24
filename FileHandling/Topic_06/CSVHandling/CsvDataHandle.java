package FileHandling.Topic_06.CSVHandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CsvDataHandle {
    public static void appendCSV(Path path, String[][] data) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {

            String header = String.join(",", data[0]);
            writer.write(header);
            writer.newLine();

            for (int i=1; i<data.length; i++) {
                String detail = String.join(",", data[i]);
                writer.write(detail);
                writer.newLine();
            }

            System.out.println("Successfully Written CSV file");

        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }
    public static List<List<String>> readCSV(Path path) {
        List<List<String>> result = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = null;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                List<String> detail = List.of(line.split(","));
                result.add(detail);
            }
            System.out.println("Successfully read all details");

        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
        return result;
    }
    public static void main(String[] args) {
        Path path = Path.of("FileHandling/Topic_06/userDataTable.csv");

        // Writing in CSV file
        String[][] data = {
            {"name", "age", "city", "dob"},
            {"Aryan", "21", "Kanpur", "17-11-2004"},
            {"Shivam", "20", "Agra", "23-05-2005"},
            {"Kushagra", "22", "Delhi", "10-08-2003"}
        };
        appendCSV(path, data);

        // Read CSV file
        List<List<String>> result = readCSV(path);
        result.forEach(System.out::println);
    }
}
