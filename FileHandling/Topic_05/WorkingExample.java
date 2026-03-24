package FileHandling.Topic_05;

import java.io.FileWriter;
import java.io.IOException;

public class WorkingExample {
    public static void main(String[] args) {
        try {
            FileWriter fw = new FileWriter("output.txt");
            
            // Imagine disk gets full while writing a huge loop
            for (int i = 0; i < 10_000; i++) { // CAUTION: you can increase the number of iterations, but be CAREFUL, as it may hang your device.
                fw.write("Writing lots of data...\n"); // Disk full here
            }
            fw.close();
            
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
            // Could be: "No space left on device"
        }
    }
}
