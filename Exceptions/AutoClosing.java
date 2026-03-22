package Exceptions;

import java.io.FileReader;

// No need to close the connection of that object whose class extends AutoClosable interface, if you are using try_resources block.

public class AutoClosing {
    private static void getFile() {
        try (FileReader fileReader = new FileReader("console.txt")) { // FileReader implements AutoClosable
            // business logic
        } catch (Exception e) {
            System.out.println("Exceptio: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        
    }
}
