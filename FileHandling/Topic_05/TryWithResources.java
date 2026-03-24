package FileHandling.Topic_05;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TryWithResources {
    public static void main(String[] args) {

        // Resources declared here are auto-closed!
        try (
            FileReader fr = new FileReader("data.txt");
            BufferedReader br = new BufferedReader(fr)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }
        // No finally needed! br and fr are closed automatically.
    }
}

/*
Java calls close() on resources in reverse order of how we declared them. So br closes first, then fr. This is important because br wraps fr.
*/