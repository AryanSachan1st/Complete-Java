package Collections_Framework.ArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * List<E>      → interface  (contract: defines WHAT methods exist)
 * ArrayList<E> → class      (implementation: defines HOW they work)
 *
 * INTERNAL WORKING OF ArrayList:
 *  - Backed by an Object[] with default initial capacity = 10.
 *  - When full, a new array ~1.5x the size is created and elements are copied.
 *  - get(index)  → O(1)  (direct array access)
 *  - add/remove in middle → O(n)  (elements must shift)
 *
 * Always declare the variable as the interface type:
 *   List<String> list = new ArrayList<>();   ← preferred (polymorphic)
 */
public class MyArrayList {

    public static void main(String[] args) {

        // ── 1. CREATION ──────────────────────────────────────────────────
        List<String> fruits = new ArrayList<>();          // default capacity 10
        System.out.println("Empty list: " + fruits);     // []


        // ── 2. Arrays.asList() — CREATION FROM RAW INPUTS ────────────────
        // Creates a FIXED-SIZE List backed directly by the given array.
        List<String> fixed = Arrays.asList("Red", "Green", "Blue");
        System.out.println("\nArrays.asList : " + fixed);  // [Red, Green, Blue]

        // ✅ set() works — the backing array allows value replacement
        fixed.set(0, "Crimson");
        System.out.println("After set(0)  : " + fixed);   // [Crimson, Green, Blue]

        // ❌ LIMITATION 1 — add() / remove() throw UnsupportedOperationException
        //    because the list size is fixed (it IS the array, cannot grow).
        try {
            fixed.add("Yellow");         // throws!
        } catch (UnsupportedOperationException e) {
            System.out.println("add() blocked : UnsupportedOperationException");
        }

        // ❌ LIMITATION 2 — changes to the original array reflect in the list
        //    (they share the same backing storage).
        String[] raw = {"One", "Two", "Three"};
        List<String> fromArr = Arrays.asList(raw);
        raw[1] = "CHANGED";
        System.out.println("Shared backing: " + fromArr); // [One, CHANGED, Three]

        // ✅ FIX — wrap in new ArrayList<>() for a fully mutable, independent copy
        List<String> mutable = new ArrayList<>(Arrays.asList("One", "Two", "Three"));
        mutable.add("Four");   // fine now
        mutable.remove("One"); // fine now
        System.out.println("Mutable copy  : " + mutable); // [Two, Three, Four]


        // ── 3. ADDING ────────────────────────────────────────────────────
        fruits.add("Apple");                  // append to end — O(1) amortised
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add(1, "Avocado");            // insert at index 1 — O(n) (shifts)
        fruits.addAll(List.of("Date", "Fig")); // append entire collection
        System.out.println("\nAfter add/addAll : " + fruits);
        // [Apple, Avocado, Banana, Cherry, Date, Fig]


        // ── 4. ACCESSING ─────────────────────────────────────────────────
        System.out.println("\nget(0)  : " + fruits.get(0));               // Apple
        System.out.println("size()  : " + fruits.size());                 // 6
        System.out.println("contains('Banana') : " + fruits.contains("Banana")); // true
        System.out.println("indexOf('Apple')   : " + fruits.indexOf("Apple"));   // 0


        // ── 5. UPDATING ──────────────────────────────────────────────────
        // set(index, newValue) replaces element and returns the OLD value — O(1)
        String old = fruits.set(2, "Blueberry");
        System.out.println("\nset(2,'Blueberry') replaced : " + old);
        System.out.println("List now : " + fruits);


        // ── 6. REMOVING ──────────────────────────────────────────────────
        fruits.remove(0);                        // by index — shifts remaining
        fruits.remove("Date");                   // by value — first occurrence
        fruits.removeIf(f -> f.startsWith("F")); // removes all matching (Java 8+)
        System.out.println("\nAfter removals : " + fruits);

        // ⚠ Integer list — index vs value ambiguity:
        List<Integer> nums = new ArrayList<>(List.of(10, 20, 30));
        nums.remove(1);                   // removes element AT index 1 (value 20)
        nums.remove(Integer.valueOf(30)); // removes VALUE 30 (use Integer.valueOf!), otherwise it will take 30 as integer
        System.out.println("Integer list after removes : " + nums); // [10]


        // ── 7. ITERATING ─────────────────────────────────────────────────
        System.out.print("\nfor-each : ");
        for (String f : fruits) System.out.print(f + "  ");

        System.out.print("\nforEach  : ");
        fruits.forEach(f -> System.out.print(f + "  "));
        System.out.println();


        // ── 8. SORTING ───────────────────────────────────────────────────
        fruits.add("Apple"); fruits.add("Zucchini");
        Collections.sort(fruits);                        // natural order
        System.out.println("\nSorted (natural)  : " + fruits);

        fruits.sort((a, b) -> b.compareTo(a));           // reverse order
        System.out.println("Sorted (reverse)  : " + fruits);

        fruits.sort((a, b) -> Integer.compare(a.length(), b.length())); // by length
        System.out.println("Sorted (length)   : " + fruits);


        // ── 9. SUBLIST & CLEAR ───────────────────────────────────────────
        // subList is a LIVE VIEW — mutations affect the original list
        List<String> sub = fruits.subList(0, 2);
        System.out.println("\nsubList(0,2) : " + sub);

        fruits.clear();
        System.out.println("After clear — isEmpty : " + fruits.isEmpty()); // true


        // ── 10. LIST → ARRAY CONVERSION ──────────────────────────────────
        List<String> toConvert = new ArrayList<>(Arrays.asList("X", "Y", "Z"));

        // (a) toArray() — returns Object[], loses type info, needs casting
        Object[] objArr = toConvert.toArray();
        System.out.println("\nObject[]  : " + Arrays.toString(objArr));

        // (b) toArray(T[] a) — preferred; returns a properly typed String[]
        //     Pass new String[0] — Java will allocate the right-sized array.
        String[] strArr = toConvert.toArray(new String[0]);
        System.out.println("String[]  : " + Arrays.toString(strArr));

        // (c) toArray(IntFunction) — method-reference style (Java 11+), cleanest
        String[] strArr2 = toConvert.toArray(String[]::new);
        System.out.println("String[]  : " + Arrays.toString(strArr2));

        // Reverse: from array back to mutable List — use new ArrayList<>(Arrays.asList(arr))
        List<String> backToList = new ArrayList<>(Arrays.asList(strArr));
        System.out.println("Back to List : " + backToList);


        // ── 11. POLYMORPHISM (power of coding to the interface) ──────────
        // Swapping implementation requires changing only ONE line:
        List<String> poly = new ArrayList<>();
        poly.add("Hello"); poly.add("World");
        printList(poly);

        poly = new java.util.LinkedList<>();  // swap! rest of code unchanged
        poly.add("Hello"); poly.add("World");
        printList(poly);
    }

    /** Accepts ANY List<String> — works with ArrayList, LinkedList, etc. */
    static void printList(List<String> list) {
        System.out.println("[" + list.getClass().getSimpleName() + "] " + list);
    }
}
