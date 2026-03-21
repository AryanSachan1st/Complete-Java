package Collections_Framework.Map;

import java.util.*;

/*
 * ── LinkedHashMap INTERNALS ──────────────────────────────────────────────────
 *
 *  Parent : HashMap  (same bucket-array + chaining)
 *  Extra  : a DOUBLY-LINKED LIST wired through every Entry to track order.
 *
 *  Each node has two extra pointers on top of HashMap's Node:
 *      before (prev in list)  |  after (next in list)
 *  HashMap decides WHICH BUCKET; the linked list decides ITERATION ORDER.
 *
 *  CONSTRUCTOR:  LinkedHashMap(initialCapacity, loadFactor, accessOrder)
 *  ─────────────────────────────────────────────────────────────────────
 *  initialCapacity – buckets allocated upfront (default 16); pre-size to avoid rehash
 *  loadFactor      – resize when size > capacity × loadFactor (default 0.75)
 *  accessOrder     – false = insertion order (default)
 *                    true  = access  order  (key feature)
 *
 *  HOW accessOrder WORKS
 *  ─────────────────────
 *  false → each new entry appended at the TAIL; iteration = insertion sequence.
 *          Re-putting an existing key updates value but does NOT move the node.
 *
 *  true  → every get() / put()-on-existing-key moves that node to the TAIL.
 *          HEAD = Least-Recently-Used (LRU)   |   TAIL = Most-Recently-Used
 *          This makes it trivial to build an LRU cache:
 *          override removeEldestEntry() → return true when size > limit
 *          → LinkedHashMap auto-evicts the HEAD (the LRU entry) for you.
 *
 *  COST VS HashMap
 *  ───────────────
 *  Time  : same O(1) avg for put / get / remove
 *  Space : ~16 bytes more per entry (the two extra pointers)
 *
 *  KEY CHARACTERISTICS
 *  ───────────────────
 *  ✔ Null key/values
 *      Exactly ONE null key is allowed (same rule as HashMap).
 *      Any number of null values are fine.
 *
 *  ✔ Not thread-safe
 *      Concurrent writes corrupt the internal doubly-linked list.
 *      accessOrder=true is especially dangerous: even get() is a WRITE
 *      (it moves the node to the tail), so two simultaneous reads can corrupt it.
 *      Fix: Collections.synchronizedMap(new LinkedHashMap<>(...))
 *
 *  ✔ Guaranteed iteration order
 *      Unlike HashMap (whose order can change after a rehash), LinkedHashMap
 *      always iterates in either insertion or access order — your choice.
 *      entrySet(), keySet(), values() all honour the same order.
 *
 *  ✔ Faster iteration than HashMap
 *      HashMap must scan all bucket slots (most are empty).
 *      LinkedHashMap follows the doubly-linked list → visits only live entries.
 *      Difference is noticeable when capacity >> size.
 *
 *  ✔ Not sorted
 *      Order is positional (when you inserted / accessed), NOT alphabetical or
 *      numerical. For natural key ordering use TreeMap instead.
 *
 *  USE WHEN
 *  ────────
 *  ✔ Need HashMap speed + predictable iteration order
 *  ✔ Building an LRU cache (accessOrder = true)
 *  ✔ Preserving insertion order for JSON / config serialisation
 * ────────────────────────────────────────────────────────────────────────────
 */
public class MyLinkedHashMap {

    public static void main(String[] args) {

        // ── 1. INSERTION ORDER  (accessOrder = false) ────────────────────────
        LinkedHashMap<String, Integer> ins = new LinkedHashMap<>(8, 0.75f, false);   // cap=8, lf=0.75, insertion

        

        ins.put("Alice",   90);
        ins.put("Bob",     85);
        ins.put("Charlie", 78);
        ins.get("Alice");           // access Alice — order is UNCHANGED
        ins.put("Bob", 99);         // re-put Bob   — value updated, NOT moved

        System.out.println("── Insertion order ──");
        ins.forEach((k, v) -> System.out.println(k + " → " + v));
        // Alice→90, Bob→99, Charlie→78   (always original put() sequence)


        // ── 2. ACCESS ORDER  (accessOrder = true) ────────────────────────────
        LinkedHashMap<String, Integer> acc = new LinkedHashMap<>(8, 0.75f, true);    // cap=8, lf=0.75, access

        acc.put("A", 1);   // list: A
        acc.put("B", 2);   // list: A ↔ B
        acc.put("C", 3);   // list: A ↔ B ↔ C

        acc.get("A");      // A accessed → tail: B ↔ C ↔ A
        acc.get("B");      // B accessed → tail: C ↔ A ↔ B
        acc.put("C", 99);  // C re-put   → tail: A ↔ B ↔ C  (C is MRU)

        System.out.println("\n── Access order (LRU → MRU) ──");
        acc.forEach((k, v) -> System.out.println(k + " → " + v));
        // A → B → C   (A = oldest access, C = most recent)


        // ── 3. LRU CACHE — practical use of accessOrder = true ───────────────
        //  removeEldestEntry() is called after every put().
        //  Return true → LinkedHashMap removes the HEAD (LRU entry) automatically.
        //  Because accessOrder=true keeps MRU at the tail, the head is always the
        //  least recently used item — perfect eviction candidate.

        final int MAX = 3;

        LinkedHashMap<Integer, String> lru = new LinkedHashMap<>(MAX, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
                return size() > MAX;   // evict LRU when cache is full
            }
        };

        lru.put(1, "Page-1");  // [1]
        lru.put(2, "Page-2");  // [1, 2]
        lru.put(3, "Page-3");  // [1, 2, 3] ← full

        lru.get(1);            // 1 accessed → [2, 3, 1]  (2 is now LRU)
        lru.put(4, "Page-4"); // full → evict LRU (2) → [3, 1, 4]

        System.out.println("\n── LRU Cache ──");
        lru.forEach((k, v) -> System.out.println("key=" + k + "  " + v));
        System.out.println("key 2 evicted? " + !lru.containsKey(2));  // true
        System.out.println("key 1 kept?    " +  lru.containsKey(1));  // true


        // ── 4. COMMON API ────────────────────────────────────────────────────
        LinkedHashMap<String, Integer> m = new LinkedHashMap<>();
        m.put("X", 10);  m.put("Y", 20);  m.put("Z", 30);

        m.putIfAbsent("X", 99);                  // X exists → no change
        m.merge("Y", 5, Integer::sum);           // Y = 25
        m.compute("Z", (k, v) -> v * 2);         // Z = 60
        m.remove("X");
        m.remove("Y", 25);                       // conditional remove (matches → gone)

        System.out.println("\n── Final map ──");
        m.forEach((k, v) -> System.out.println(k + " → " + v)); // only Z → 60
    }
}
