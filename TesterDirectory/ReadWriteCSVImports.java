package TesterDirectory;

import java.nio.file.Path;

import FileHandling.Topic_06.CSVHandling.CsvDataHandle;

public class ReadWriteCSVImports {
    public static void main(String[] args) {
        Path path = Path.of("additionals.txt");
        var result = CsvDataHandle.readCSV(path);
        result.forEach(System.out::println);
    }
}
