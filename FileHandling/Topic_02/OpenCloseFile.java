package FileHandling.Topic_02;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/* Important Docs for this file-
1. Java uses Stream based architecture, data flows through the stream between the program and the file. [Program <-> Stream <-> File on disc]
2. Byte Stream [FileInputStream, FileOutputStream]: used for images, videos, binary files
3. Character Stream [FileReader, FileWriter]: used for text files, .txt, .csv, etc
4. Closing the file is very important to negate the memory wastage
*/

public class OpenCloseFile {
    public static void main(String[] args) {
        BufferedReader file = null;
        try {
            file = new BufferedReader(new FileReader("test0102.txt")); // faster
            System.out.println("File Openend successfully");

            // BufferedReader reads big chunk of data from the disc and stores it in the RAM buffer [faster], rather than just FileReader which reads char by char.

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try { // manually closing the file
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                System.out.println("Error in closing file: " + e.getMessage());
            }
        }

        // alternative [better] approach: auto closing through try_with_resources block
        // internally it implements AutoClosable Interface, any class implementing that interface can be used with try_with_resource block.
        try (BufferedReader file2 = new BufferedReader(new FileReader("test0102.txt"))) {
            System.out.println("File read successfully");
        } catch (FileNotFoundException e) {
            System.out.println("File Not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO exception: " + e.getMessage());
        }
    }
}