package Collections_Framework.Map;

import java.util.*;

// ─────────────────────────────────────────────────────────────────────────────
// HIERARCHY:  Map (interface)
//               └─ SortedMap (interface)   → adds sorted-key contract
//                    └─ NavigableMap (interface) → adds navigation methods
//                         └─ TreeMap (class)     → Red-Black Tree implementation
//
// TREEMAP — INTERNAL WORKING
//  • Backed by a self-balancing Red-Black Tree (not an array like HashMap).
//  • Every node stores: key, value, left-child, right-child, parent, color(R/B).
//  • On put()  → BST insert + recolor/rotate to maintain balance  → O(log n)
//  • On get()  → BST search left/right based on key comparison    → O(log n)
//  • On remove()→ BST delete + fix-up rotations                   → O(log n)
//  • Keys are always kept in SORTED order (in-order traversal of BST).
//  • No null keys allowed (compareTo / compare would throw NullPointerException).
//  • Null values ARE allowed.
//
// KEY FEATURES of TreeMap
//  ✔ Sorted keys (natural order OR custom Comparator)
//  ✔ O(log n) for put / get / remove (vs O(1) avg in HashMap)
//  ✔ Guaranteed iteration order (ascending by key)
//  ✔ Not thread-safe (use Collections.synchronizedSortedMap() if needed)
//  ✔ Implements NavigableMap → floor(), ceiling(), higher(), lower(), etc.
//
// KEY FEATURES of SortedMap (interface)
//  ✔ firstKey()          → smallest key in the map
//  ✔ lastKey()           → largest key in the map
//  ✔ headMap(toKey)      → sub-map with keys STRICTLY LESS than toKey
//  ✔ tailMap(fromKey)    → sub-map with keys >= fromKey
//  ✔ subMap(from, to)    → keys in range [from, to)
//  ✔ comparator()        → returns the Comparator used (null if natural order)
// ─────────────────────────────────────────────────────────────────────────────

public class MySortedMap {
    public static void main(String[] args) {

        // ── Example 1: String keys (natural order = lexicographic) ────────────
        // TreeMap uses String.compareTo() internally to place each key in the
        // correct position in the Red-Black Tree → output is alphabetically sorted.
        Map<String, Integer> map = new TreeMap<>();
        map.put("Aryan",   45);
        map.put("Aviral",  55);
        map.put("Ajinkya", 35);
        System.out.println(map); // {Ajinkya=35, Aryan=45, Aviral=55}

        // ── Example 2: Integer keys + SortedMap-specific methods ──────────────
        // Integer implements Comparable → natural order is ascending numeric.
        SortedMap<Integer, String> map2 = new TreeMap<>();
        map2.put(102, "Ranjan");
        map2.put(100, "Shivalik");
        map2.put(101, "Mahant");
        System.out.println(map2); // {100=Shivalik, 101=Mahant, 102=Ranjan}

        // headMap(toKey) — exclusive upper bound: keys < 102 → {100, 101}
        // Internally returns a view backed by the same tree, not a copy.
        System.out.println(map2.headMap(102)); // {100=Shivalik, 101=Mahant}

        // tailMap(fromKey) — inclusive lower bound: keys >= 100 → all three
        System.out.println(map2.tailMap(100)); // {100=Shivalik, 101=Mahant, 102=Ranjan}

        // ── Example 3: Custom object keys — MUST supply Comparator ────────────
        // TreeMap needs to compare keys to build its Red-Black Tree.
        // If key class does NOT implement Comparable → ClassCastException at runtime
        // unless a Comparator is passed to the constructor.
        // Here: lambda Comparator sorts Students by age (ascending).
        SortedMap<Student, String> map3 = new TreeMap<>((s1, s2) -> Integer.compare(s1.getAge(), s2.getAge()));

        Student s1 = new Student("Kajal",    21);
        Student s2 = new Student("Tripti",   20); // youngest → first in tree
        Student s3 = new Student("Samiksha", 22); // oldest   → last in tree

        map3.put(s1, "AI Generalist");
        map3.put(s2, "DevOps Expert");
        map3.put(s3, "System Designer");

        // Iteration is in ascending-age order (in-order traversal of R-B Tree)
        for (Map.Entry<Student, String> entry : map3.entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
        }

        // firstKey() → node with the smallest key (leftmost node in BST) → Tripti (age 20)
        System.out.println(map3.firstKey()); // Student{Tripti, 20}

        // lastKey()  → node with the largest key (rightmost node in BST) → Samiksha (age 22)
        System.out.println(map3.lastKey());  // Student{Samiksha, 22}
    }
}

// Student is used as a TreeMap KEY — no Comparable needed here because
// we provided an external Comparator to the TreeMap constructor instead.
class Student {
    private String name;
    private int    age;

    public Student(String name, int age) {
        this.name = name;
        this.age  = age;
    }

    public String getName() { return this.name; }
    public int    getAge()  { return this.age;  }

    @Override
    public String toString() { return name + "(age=" + age + ")"; }
}
