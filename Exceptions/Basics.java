package Exceptions;

/**
 * BASICS.java — Exception Handling Demo
 * =======================================
 * Demonstrates core Java exception handling concepts:
 *  1. Arithmetic & Null Pointer exceptions with try-catch-finally
 *  2. Checked exceptions (IOException) with manual resource cleanup
 *  3. Custom exceptions (CustomException) for domain-specific errors
 *  4. Re-throwing exceptions to propagate them to the caller
 *
 * NOTE: A parent Exception class will always catch child Exception types.
 *       e.g., catching Exception will also catch ArithmeticException, NullPointerException, etc.
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

// ─────────────────────────────────────────────────────────────────────────────
// Helper class to demonstrate NullPointerException scenario
// ─────────────────────────────────────────────────────────────────────────────
class Student {
    private int id;
    private String name;
    private static int count = 1; // auto-incrementing ID counter

    public Student(String name) {
        this.id = count++;
        this.name = name;
    }

    /** Returns student details as a Map of {id → name} */
    public Map<Integer, String> getDetails() {
        return Map.of(this.id, this.name);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Main class showcasing different exception handling scenarios
// ─────────────────────────────────────────────────────────────────────────────
public class Basics {

    /**
     * Simple division — can throw ArithmeticException (divide by zero).
     * We intentionally do NOT handle it here; the caller handles it.
     */
    private static double divide(int a, int b) {
        return a / b;
    }

    /**
     * Demonstrates checked exception handling with manual resource cleanup.
     *
     * Problem: FileReader constructor throws FileNotFoundException (checked).
     * Solution:
     *   - Catch it, print a message, then re-throw to inform the caller.
     *   - Use finally to ALWAYS close the resource, even if an exception occurred.
     *
     * NOTE: fileReader is declared outside try so it's accessible in finally.
     *       If new FileReader() fails, fileReader stays null → handle that in finally too.
     *
     * Better Alternative: Use try-with-resources (see AutoClosing.java)
     *   try (FileReader fr = new FileReader("info.txt")) { ... }
     *   → AutoCloseable handles closing automatically, no need for finally block.
     */
    private static void fileData() throws IOException {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("info.txt");
            // ... process file data here ...
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            // Re-throw to propagate the exception to the caller (main)
            throw new FileNotFoundException(e.getMessage());
        } finally {
            // Always runs — ensures fileReader is closed even if exception occurred
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    System.out.println("Error closing fileReader: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Main method — entry point, wires together all exception scenarios.
     *
     * Declares `throws IOException, CustomException`:
     *   → Means we're propagating these checked exceptions instead of handling them here.
     *   → In production code, you'd handle them gracefully instead of propagating from main.
     */
    public static void main(String[] args) throws IOException, CustomException {

        // ── Scenario 1: Custom Exception (Insufficient Balance) ──────────────
        // CustomException is a checked exception we defined for domain logic errors.
        BankAccount myAcc = new BankAccount(1000);
        try {
            boolean withdrawSuccess = myAcc.withdraw(1200); // will throw CustomException (1200 > 1000)
            if (withdrawSuccess) {
                System.out.println("Rs.800 withdrawn successfully");
            }
        } catch (CustomException e) {
            System.out.println(e); // prints: Insufficient Balance
            return; // stop further execution if withdrawal fails
        }
        System.out.println(myAcc.getBalance()); // only reached if withdrawal succeeded

        // ── Scenario 2: Checked Exception — File I/O ─────────────────────────
        // fileData() throws IOException; if thrown, it bubbles up to main's signature.
        fileData();

        // ── Scenario 3: Multiple catch blocks — order matters! ────────────────
        // Specific exceptions MUST come before general ones.
        // Catching Exception first would make the specific catches unreachable (compile error).
        int[] numerators   = { 10, 20, 30, 40, 50, 60 };
        int[] denominators = {  5,  2, 30,  0,  5, 10 };

        for (int i = 0; i < numerators.length; i++) {
            try {
                System.out.println(divide(numerators[i], denominators[i])); // may throw ArithmeticException

                Student st1 = null;
                System.out.println(st1.getDetails()); // intentionally null → throws NullPointerException

            } catch (ArithmeticException e) {
                // Handles divide-by-zero (denominator = 0)
                System.out.println("Cannot divide by zero at index " + i);
                return;

            } catch (NullPointerException e) {
                // Handles calling a method on a null object reference
                System.out.println("Null Pointer Exception: " + e.getMessage());
                return;

            } catch (Exception e) {
                // Generic fallback — catches any other exception not handled above
                // IMPORTANT: This must always be the LAST catch block
                System.out.println("Unexpected exception: " + e.getMessage());
                return;

            } finally {
                // finally block ALWAYS executes, regardless of exception or return
                // Use it to release resources, log info, etc.
                System.out.println("Finally always executed");
            }
        }
    }
}
