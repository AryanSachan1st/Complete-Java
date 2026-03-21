package Collections_Framework.ArrayList;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

// CopyOnWriteArrayList: Best when reads >> writes.
// On every write (add/remove), it creates a full copy of the internal array,
// applies the modification on the copy, then swaps the reference.
// Reads always iterate the stable snapshot — never the one being written to.
public class MyCopyOnWriteArrayList {

    // ─── DEMO 1: ArrayList — write while reading ──────────────────────────────
    static void arrayListDemo() {
        List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));

        try {
            for (String item : list) {           // iterator() captures a "modCount" stamp at creation

                System.out.println("Reading: " + item);

                if (item.equals("B")) {
                    list.add("X");               // ❌ mutates the list → modCount changes
                    // ArrayList's iterator checks modCount on every next() call.
                    // Since modCount no longer matches the stamp taken at iterator creation,
                    // it immediately throws ConcurrentModificationException (fail-fast behaviour).
                    // This is a safety guard — NOT a thread-safety mechanism.
                    // It fires even on a single thread if you modify the list mid-iteration.
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ArrayList → ConcurrentModificationException caught!");
            System.out.println("Reason: ArrayList's iterator is fail-fast. It tracks an internal");
            System.out.println("        modCount and throws if the list is structurally changed");
            System.out.println("        (add/remove) while iterating, even on a single thread.\n");
        }
    }

    // ─── DEMO 2: CopyOnWriteArrayList — write while reading ──────────────────
    static void cowArrayListDemo() {
        CopyOnWriteArrayList<String> cowList =
                new CopyOnWriteArrayList<>(Arrays.asList("A", "B", "C", "D"));

        // iterator() takes a snapshot of the current internal array at this moment.
        // All reads during this loop work on THAT snapshot — it never changes.
        for (String item : cowList) {

            System.out.println("Reading: " + item);

            if (item.equals("B")) {
                cowList.add("X");   // ✅ creates a NEW copy of the array, adds "X" to it,
                //    then atomically swaps the internal reference to the new array.
                // The running iterator still holds the OLD snapshot → no exception.
                System.out.println("  → Added 'X' safely during iteration.");
            }
        }

        // "X" is visible only AFTER the current iteration finishes,
        // because the iterator was snapshotted before the write happened.
        System.out.println("Final list: " + cowList);
    }

    public static void main(String[] args) {
        System.out.println("=== ArrayList (fail-fast) ===");
        arrayListDemo();

        System.out.println("=== CopyOnWriteArrayList (fail-safe / snapshot) ===");
        cowArrayListDemo();
    }
}

