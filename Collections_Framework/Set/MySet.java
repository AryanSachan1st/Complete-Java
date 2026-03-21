package Collections_Framework.Set;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/*
 * ╔══════════════════════════════════════════════════════╗
 *  SET  — stores UNIQUE elements only (no duplicates)
 *
 *  Set  ←→  Map     (they are twins — one stores keys only,
 *                      the other stores key-value pairs)
 *
 *  HashSet         ←→  HashMap         (hash-table, O(1), unordered)
 *  LinkedHashSet   ←→  LinkedHashMap   (hash + doubly-linked list, insertion order)
 *  TreeSet         ←→  TreeMap         (Red-Black tree, sorted, O(log n))
 *  EnumSet         ←→  EnumMap         (bit-vector for enums, blazing fast)
 *
 *  Rule: internally every HashSet HOLDS a HashMap,
 *        every TreeSet HOLDS a TreeMap — Set is just "Map with dummy values".
 * ╚══════════════════════════════════════════════════════╝
 */
public class MySet {
    public static void main(String[] args) {

        // ── 1. HashSet ─────────────────────────────────────────────────
        // ↳ Like HashMap: uses hash-table → O(1) add/contains, NO order
        // ↳ Use-case: membership check, deduplication of large data
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Banana"); hashSet.add("Apple"); hashSet.add("Apple"); // duplicate ignored
        hashSet.add("Cherry");
        System.out.println("HashSet : " + hashSet);           // order not guaranteed

        // ── 2. LinkedHashSet ───────────────────────────────────────────
        // ↳ Like LinkedHashMap: hash-table + doubly-linked list → insertion order kept
        // ↳ Use-case: cache of recently seen unique items (LRU friendly)
        Set<String> linkedSet = new LinkedHashSet<>();
        linkedSet.add("Banana"); linkedSet.add("Apple"); linkedSet.add("Apple");
        linkedSet.add("Cherry");
        System.out.println("LinkedHashSet : " + linkedSet);   // [Banana, Apple, Cherry]

        // ── 3. TreeSet ─────────────────────────────────────────────────
        // ↳ Like TreeMap: Red-Black tree → always SORTED, O(log n)
        // ↳ Use-case: leaderboard, range queries, auto-sorted unique tags
        NavigableSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(40); treeSet.add(10); treeSet.add(30); treeSet.add(10); // dup ignored
        System.out.println("TreeSet (sorted) : " + treeSet); // [10, 30, 40]

        // TreeSet navigability (same API as TreeMap for keys)
        System.out.println("Floor  of 25 : " + treeSet.floor(25));   // 10
        System.out.println("Higher than 10: " + treeSet.higher(10));  // 30
        System.out.println("Subset 10-30  : " + treeSet.subSet(10, true, 30, true));

        // ── 4. EnumSet ─────────────────────────────────────────────────
        // ↳ Like EnumMap: backed by a bit-vector → FASTEST Set for enums
        // ↳ Use-case: permission flags, day-of-week selection, feature toggles
        enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }
        Set<Day> weekend = EnumSet.of(Day.SAT, Day.SUN);
        Set<Day> weekdays = EnumSet.complementOf((EnumSet<Day>) weekend);
        System.out.println("Weekend  : " + weekend);
        System.out.println("Weekdays : " + weekdays);

        // ── 5. Thread-Safe Options ────────────────────────────────────
        // Collections.synchronizedSet → wraps any set, LOCKS the whole set (slow)
        @SuppressWarnings("unused")
        Set<Integer> syncSet = Collections.synchronizedSet(new HashSet<>());

        // ConcurrentSkipListSet ↔ ConcurrentSkipListMap (sorted + thread-safe)
        // weakly consistent: allows updates during iteration (no ConcurrentModificationException)
        ConcurrentSkipListSet<Integer> cslSet = new ConcurrentSkipListSet<>();
        cslSet.add(3); cslSet.add(1); cslSet.add(2); cslSet.add(3); // dup ignored
        System.out.println("ConcurrentSkipListSet : " + cslSet);     // [1, 2, 3]

        // CopyOnWriteArraySet ↔ CopyOnWriteArrayList (write = copy entire array)
        // safe for iteration but EXPENSIVE writes — use only for tiny, read-heavy sets
        CopyOnWriteArraySet<Integer> cowaSet = new CopyOnWriteArraySet<>();
        cowaSet.add(10); cowaSet.add(20); cowaSet.add(10); // dup ignored
        System.out.println("CopyOnWriteArraySet   : " + cowaSet);

        // ── 6. Immutable Set ──────────────────────────────────────────
        // Set.of() → unmodifiable, throws IllegalArgumentException on duplicate keys
        Set<String> immutable = Set.of("A", "B", "C");
        System.out.println("Immutable Set : " + immutable);
        // immutable.add("D"); // ← throws UnsupportedOperationException

        // ── 7. Set Operations (very useful!) ──────────────────────────
        Set<Integer> s1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> s2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));

        Set<Integer> union = new HashSet<>(s1); union.addAll(s2);
        System.out.println("Union        : " + union);        // [1,2,3,4,5,6]

        Set<Integer> intersect = new HashSet<>(s1); intersect.retainAll(s2);
        System.out.println("Intersection : " + intersect);    // [3,4]

        Set<Integer> diff = new HashSet<>(s1); diff.removeAll(s2);
        System.out.println("Difference   : " + diff);         // [1,2]
    }
}

/*
 * QUICK COMPARISON TABLE
 * ┌─────────────────────┬────────────────┬──────────┬──────────┬───────────┐
 * │ Set                 │ Backed by      │ Order    │ Speed    │ Null?     │
 * ├─────────────────────┼────────────────┼──────────┼──────────┼───────────┤
 * │ HashSet             │ HashMap        │ None     │ O(1)     │ 1 allowed │
 * │ LinkedHashSet       │ LinkedHashMap  │ Insertion│ O(1)     │ 1 allowed │
 * │ TreeSet             │ TreeMap        │ Sorted   │ O(log n) │ No        │
 * │ EnumSet             │ Bit-vector     │ Enum ord.│ O(1)     │ No        │
 * │ ConcurrentSkipList  │ SkipList       │ Sorted   │ O(log n) │ No        │
 * │ CopyOnWriteArraySet │ COW Array      │ Insertion│ O(n)write│ allowed   │
 * └─────────────────────┴────────────────┴──────────┴──────────┴───────────┘
 */