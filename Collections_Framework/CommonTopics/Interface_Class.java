package Collections_Framework.CommonTopics;

import java.util.*;

/*
 * ============================================================
 *   INTERFACE vs CLASS — Relationship in Collections Framework
 * ============================================================
 *
 * KEY IDEA:
 *   Interface  → defines WHAT operations are available (the contract)
 *   Class      → defines HOW those operations work (the implementation)
 *
 * WHOSE FUNCTIONS ARE USED?
 *   - The REFERENCE TYPE (interface/superclass) decides WHICH methods you can CALL.
 *   - The OBJECT TYPE (concrete class) decides WHICH implementation actually RUNS.
 *   - This is called Runtime Polymorphism (Dynamic Dispatch).
 *
 *   List<String> list = new ArrayList<>();
 *         ↑                    ↑
 *    Reference type         Object type
 *  (limits access to     (provides the actual
 *   List's methods)        method implementation)
 *
 * ============================================================
 *   Collections Framework Hierarchy (simplified)
 * ============================================================
 *
 *   Iterable
 *     └── Collection (interface)
 *           ├── List (interface)   → ArrayList, LinkedList, Vector
 *           ├── Set (interface)    → HashSet, TreeSet, LinkedHashSet
 *           └── Queue (interface) → PriorityQueue, ArrayDeque
 *
 *   Map (interface, separate hierarchy)
 *         └── HashMap, TreeMap, LinkedHashMap
 *
 * ============================================================
 */

public class Interface_Class {

    public static void main(String[] args) {

        // ----------------------------------------------------------
        // EXAMPLE 1: List (interface) ← ArrayList (class)
        // ----------------------------------------------------------
        // The variable is declared as List (interface).
        // So only methods defined in the List interface can be called.
        // But at runtime, ArrayList's version of add(), get() etc. runs.

        List<String> fruits = new ArrayList<>(); // ArrayList implements List

        fruits.add("Apple");       // List.add()  → ArrayList.add() runs
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.get(0);             // List.get()  → ArrayList.get() runs
        fruits.remove("Banana");   // List.remove() → ArrayList.remove() runs

        System.out.println("ArrayList via List ref: " + fruits);
        // Output: [Apple, Cherry]

        // ✅ You CAN call: add, get, remove, size, contains, iterator, etc.
        //    (all methods declared in List interface)
        // ❌ You CANNOT call: ensureCapacity(), trimToSize()
        //    (ArrayList-specific methods not in List interface)
        //    → To call those, cast: ((ArrayList<String>) fruits).trimToSize();

        // ----------------------------------------------------------
        // EXAMPLE 2: Same interface, Different class → Different behavior
        // ----------------------------------------------------------
        // Swapping ArrayList for LinkedList — no other code changes needed.
        // This is the POWER of programming to an interface.

        List<String> cities = new LinkedList<>(); // LinkedList also implements List

        cities.add("Delhi");
        cities.add("Mumbai");
        cities.add(0, "Pune");   // inserts at index 0

        System.out.println("LinkedList via List ref: " + cities);
        // Output: [Pune, Delhi, Mumbai]

        // ArrayList.get(i) → O(1) (direct index access, backed by array)
        // LinkedList.get(i) → O(n) (traverses nodes one by one)
        // Same method name, very different internal behavior!

        // ----------------------------------------------------------
        // EXAMPLE 3: Map (interface) ← HashMap vs TreeMap
        // ----------------------------------------------------------

        Map<String, Integer> scores = new HashMap<>(); // HashMap implements Map

        scores.put("Alice", 95);
        scores.put("Bob", 82);
        scores.put("Charlie", 88);

        System.out.println("HashMap (unordered): " + scores);
        // Output order is NOT guaranteed — HashMap uses hashing

        Map<String, Integer> sortedScores = new TreeMap<>(); // TreeMap implements Map

        sortedScores.put("Alice", 95);
        sortedScores.put("Bob", 82);
        sortedScores.put("Charlie", 88);

        System.out.println("TreeMap (sorted by key): " + sortedScores);
        // Output: {Alice=95, Bob=82, Charlie=88} — always sorted A→Z
        // TreeMap.put() internally uses a Red-Black Tree to keep keys sorted

        // ----------------------------------------------------------
        // EXAMPLE 4: Queue (interface) ← PriorityQueue (class)
        // ----------------------------------------------------------

        Queue<Integer> pq = new PriorityQueue<>(); // min-heap by default

        pq.offer(30);
        pq.offer(10);
        pq.offer(20);

        System.out.println("PriorityQueue poll order:");
        while (!pq.isEmpty()) {
            System.out.print(pq.poll() + " "); // poll() → Queue contract,
        }                                       // PriorityQueue gives smallest first
        System.out.println();
        // Output: 10 20 30  (NOT insertion order — PriorityQueue reorders internally)

        // ----------------------------------------------------------
        // SUMMARY TABLE
        // ----------------------------------------------------------
        /*
         *  Interface   | Concrete Class  | Key Behavior Difference
         *  ------------|-----------------|----------------------------------
         *  List        | ArrayList       | Fast random access O(1) via array
         *  List        | LinkedList      | Fast insert/delete O(1) via nodes
         *  Map         | HashMap         | Unordered, O(1) avg lookup
         *  Map         | TreeMap         | Sorted keys, O(log n) lookup
         *  Set         | HashSet         | Unordered unique elements
         *  Set         | TreeSet         | Sorted unique elements
         *  Queue       | PriorityQueue   | Retrieval by priority, not FIFO
         *
         * RULE OF THUMB:
         *   ✔ Declare variables using the INTERFACE type (List, Map, Set...)
         *   ✔ Instantiate using the CLASS that fits your performance needs
         *   ✔ The right implementation's code runs automatically at runtime
         */
    }
}

