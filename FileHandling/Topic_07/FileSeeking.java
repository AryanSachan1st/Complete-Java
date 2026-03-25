package FileHandling.Topic_07;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileSeeking {
    public static void main(String[] args) {
        String path = "FileHandling/Topic_07/demoFile.txt";

        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            raf.seek(raf.length()); // move pointer to end of file before appending

            raf.writeBytes("\nThis is the new line 1 added by raf"); // append a new line of text at the end

            raf.seek(7); // move pointer to byte position 7

            System.out.println("After seek(7), pointer: " + raf.getFilePointer()); // print current pointer position after seek

            byte[] buffer = new byte[4];
            raf.read(buffer); // read 4 bytes starting from the current pointer position and store it in buffer array

            System.out.println(new String(buffer)); // read the buffer array as a string

            System.out.println("After reading, pointer: " + raf.getFilePointer()); // print pointer position after the read

            raf.seek(0); // rewind pointer back to the beginning of the file

            System.out.println("After rewind, pointer: " + raf.getFilePointer()); // print pointer position after rewind

            byte[] all = new byte[(int) raf.length()];
            raf.read(all); // read the entire file content into the buffer
            
            System.out.println(new String(all)); // print the full file content as a string

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        }
    }
}