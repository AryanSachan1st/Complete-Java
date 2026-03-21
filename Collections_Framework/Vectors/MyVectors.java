package Collections_Framework.Vectors;

import java.util.*;
import java.util.concurrent.*;

/*
 * ═══════════════════════════════════════════════════════════
 *  VECTOR vs ARRAYLIST vs LINKEDLIST — Key Differences
 * ═══════════════════════════════════════════════════════════
 *  | Feature           | Vector          | ArrayList       | LinkedList       |
 *  |-------------------|-----------------|-----------------|------------------|
 *  | Thread Safe       | ✅ Yes (sync)   | ❌ No           | ❌ No            |
 *  | Performance       | Slower          | Fast            | Fast add/remove  |
 *  | Growth Factor     | Doubles (2x)    | 1.5x            | Node-based       |
 *  | Legacy            | ✅ Yes (JDK 1.0)| ❌ No (JDK 1.2) | ❌ No (JDK 1.2)  |
 *  | Enumeration       | ✅ Yes          | ❌ No           | ❌ No            |
 *  | Null Elements     | ✅ Allowed      | ✅ Allowed      | ✅ Allowed       |
 *  | Random Access     | O(1)            | O(1)            | O(n)             |
 *
 * ═══════════════════════════════════════════════════════════
 *  INTERNAL IMPLEMENTATION
 * ═══════════════════════════════════════════════════════════
 *  - Backed by a dynamic Object[] array (like ArrayList).
 *  - Default initial capacity = 10.
 *  - Growth: when full, capacity DOUBLES (capacityIncrement=0).
 *    If capacityIncrement > 0, grows by that fixed amount instead.
 *  - Every public method is declared `synchronized` → only ONE
 *    thread can execute a method at a time (intrinsic lock on `this`).
 *
 * ═══════════════════════════════════════════════════════════
 *  SYNCHRONIZATION & THREAD SAFETY
 * ═══════════════════════════════════════════════════════════
 *  - Each method holds the monitor lock → safe for individual ops.
 *  - Compound actions (check-then-act) are NOT atomic:
 *      if (!v.isEmpty()) v.get(0); // still a race condition!
 *  - For true compound atomicity, wrap in synchronized(vector){...}
 *  - Modern alternative → CopyOnWriteArrayList (better read perf)
 *    or Collections.synchronizedList(new ArrayList<>())
 *
 * ═══════════════════════════════════════════════════════════
 *  PERFORMANCE
 * ═══════════════════════════════════════════════════════════
 *  - Every method acquires/releases a lock → overhead even in
 *    single-threaded apps — DON'T use Vector in single-threaded code.
 *  - ArrayList is ~2-3x faster than Vector in single-threaded scenarios.
 *  - If concurrency is needed, prefer CopyOnWriteArrayList or
 *    ConcurrentLinkedQueue depending on access patterns.
 *
 * ═══════════════════════════════════════════════════════════
 *  WHEN TO USE / NOT USE
 * ═══════════════════════════════════════════════════════════
 *  ✅ Use Vector when:
 *     - Maintaining legacy codebases that already use it.
 *     - Need a quick synchronized list (though better options exist).
 *  ❌ Avoid Vector when:
 *     - Writing new code (use ArrayList or CopyOnWriteArrayList).
 *     - Single-threaded — no benefit, only overhead.
 *     - High-concurrency — per-method sync is too coarse-grained.
 */

public class MyVectors {

    public static void main(String[] args) {

        // ── 1. CREATION ──────────────────────────────────────────
        Vector<String> v = new Vector<>();            // default capacity 10
        @SuppressWarnings("unused")
        Vector<Integer> v2 = new Vector<>(5);        // initial capacity 5
        @SuppressWarnings("unused")
        Vector<Integer> v3 = new Vector<>(5, 3);     // capacity=5, increment=3

        // ── 2. BASIC OPERATIONS ──────────────────────────────────
        v.add("Aryan");
        v.add("Sachan");
        v.add("Java");
        v.addElement("Legacy");          // old Vector-specific method
        v.add(1, "Inserted");            // insert at index 1

        System.out.println("Vector : " + v);
        System.out.println("Size   : " + v.size());
        System.out.println("Capacity: " + v.capacity()); // 10 (default)

        // ── 3. ACCESS ────────────────────────────────────────────
        System.out.println("get(0) : " + v.get(0));
        System.out.println("elementAt(2): " + v.elementAt(2)); // legacy method
        System.out.println("firstElement: " + v.firstElement());
        System.out.println("lastElement : " + v.lastElement());

        // ── 4. SEARCH ────────────────────────────────────────────
        System.out.println("contains 'Java': " + v.contains("Java"));
        System.out.println("indexOf 'Java' : " + v.indexOf("Java"));

        // ── 5. REMOVE ────────────────────────────────────────────
        v.remove("Legacy");              // by object
        v.remove(0);                     // by index
        System.out.println("After remove : " + v);

        // ── 6. ITERATION (Enumeration — legacy Vector feature) ───
        Vector<String> fruits = new Vector<>(Arrays.asList("Apple", "Mango", "Banana"));
        Enumeration<String> en = fruits.elements(); // legacy iterator
        System.out.print("Enumeration  : ");
        while (en.hasMoreElements()) System.out.print(en.nextElement() + " ");
        System.out.println();

        // ── 7. MODERN ITERATION (Iterator) ───────────────────────
        System.out.print("Iterator     : ");
        for (String f : fruits) System.out.print(f + " ");
        System.out.println();

        // ── 8. THREAD SAFETY DEMO ────────────────────────────────
        // Compound action — externally synchronized for atomicity
        synchronized (fruits) {
            if (!fruits.isEmpty()) {
                System.out.println("Safe first   : " + fruits.firstElement());
            }
        }

        // ── 9. MODERN ALTERNATIVES ───────────────────────────────
        // Single-threaded → use ArrayList
        @SuppressWarnings("unused")
        List<String> arrayList = new ArrayList<>();

        // Thread-safe (many reads, few writes) → CopyOnWriteArrayList
        @SuppressWarnings("unused")
        List<String> cowList = new CopyOnWriteArrayList<>();

        // Thread-safe (general) → synchronizedList wrapping ArrayList
        @SuppressWarnings("unused")
        List<String> synced = Collections.synchronizedList(new ArrayList<>());

        // ── 10. CAPACITY MANAGEMENT ──────────────────────────────
        Vector<Integer> cap = new Vector<>(2, 2); // start=2, increment=2
        cap.add(1); cap.add(2);
        System.out.println("Cap before grow: " + cap.capacity()); // 2
        cap.add(3); // triggers grow → capacity = 2+2 = 4
        System.out.println("Cap after grow : " + cap.capacity());  // 4
        cap.ensureCapacity(20);   // pre-allocate to avoid repeated grows
        cap.trimToSize();         // shrink backing array to current size
        System.out.println("Cap trimmed    : " + cap.capacity());  // 3
    }
}

/*
 * ═══════════════════════════════════════════════════════════
 *  QUICK SUMMARY
 * ═══════════════════════════════════════════════════════════
 *  Vector  → legacy, synchronized, doubles capacity, prefer to avoid.
 *  ArrayList → default choice, fast, not thread-safe.
 *  CopyOnWriteArrayList → best for concurrent read-heavy workloads.
 *  Collections.synchronizedList → easy thread-safe wrapper for ArrayList.
 */
