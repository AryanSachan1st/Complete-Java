package Collections_Framework.Map.ConcurrentMaps;

import java.util.*;
import java.util.concurrent.*;

// ─────────────────────────────────────────────────────────────────────────────
// WHAT IS Hashtable?
// ─────────────────────────────────────────────────────────────────────────────
// Hashtable is one of Java's OLDEST data structures (since Java 1.0).
// It is a key-value store (like a Map) backed by a hash table internally.
// • Introduced before the Java Collections Framework existed.
// • Later retrofitted to implement the Map interface (Java 2 / JDK 1.2).
// • Every public method is synchronized on the Hashtable instance itself,
//   making it thread-safe but at a large performance cost.
// • Keys and values: neither can be null (unlike HashMap).
// • Hierarchy: Object → Dictionary<K,V> → Hashtable<K,V> → implements Map<K,V>
//   (Dictionary is an abstract class, now considered obsolete)

// ─────────────────────────────────────────────────────────────────────────────
// WHY Hashtable IS NOT PREFERRED ANYMORE
// ─────────────────────────────────────────────────────────────────────────────
// 1. Coarse-grained locking: Every method (get, put, remove) is synchronized
//    on the ENTIRE table object → only one thread can operate at a time.
// 2. Poor throughput: Under concurrent load, every thread blocks, creating a
//    bottleneck even for read-only operations.
// 3. Legacy class (Java 1.0): Predates the Collections Framework; not part of
//    the Map hierarchy by design — just retrofitted later.
// 4. Null not allowed: Neither keys nor values can be null (throws NPE).
// 5. Better alternatives exist:
//      - Collections.synchronizedMap(new HashMap<>())  → still coarse-lock
//      - ConcurrentHashMap                             → fine-grained, fast ✔

// ─────────────────────────────────────────────────────────────────────────────
// ConcurrentHashMap — HIERARCHY
// ─────────────────────────────────────────────────────────────────────────────
//   java.lang.Object
//     └── java.util.AbstractMap<K,V>          (implements Map<K,V>)
//           └── java.util.concurrent.ConcurrentHashMap<K,V>
//                 implements ConcurrentMap<K,V>      ← atomic ops (putIfAbsent, replace…)
//                 implements Serializable
//
// ConcurrentMap<K,V> extends Map<K,V> and adds atomic compound operations.
// Does NOT extend AbstractCollection, so it is NOT part of the Collection hierarchy.

// ─────────────────────────────────────────────────────────────────────────────
// ConcurrentHashMap — INTERNAL WORKING
// ─────────────────────────────────────────────────────────────────────────────
// Java 7: Divided into 16 Segments (each is a mini-HashMap with its own lock).
//         → Up to 16 threads can write concurrently (one per segment).
//
// Java 8+: Segments and SegmentLocks were completely removed for better performance.
// Instead, it uses a large array of Node<K,V> buckets (like a standard HashMap),
// achieving concurrency by locking individual buckets instead of large segments.
//
// Detailed put() execution:
//   1. Hashing: Spreads the hash bits to minimize index collisions.
//   2. Empty Bucket: If the target bucket is null, it uses CAS (Compare-And-Swap)
//      to insert the new Node lock-free. CAS is an atomic, hardware-level instruction.
//   3. Occupied Bucket: If a collision occurs, it uses 'synchronized' ONLY on the
//      FIRST node (the head) of that specific bucket. Other threads can still
//      write to different buckets concurrently without blocking!
//   4. Tree-ification: If a bucket's linked list grows past 8 nodes, it transforms
//      into a Red-Black Tree, boosting worst-case lookup from O(n) to O(log n).
//
// Reads (get):
//   • Reads are NEVER locked and are completely lock-free.
//   • Internal Node attributes ('val' and 'next') are marked as 'volatile', ensuring
//     changes by a writing thread are instantly visible to reading threads.
//
// Size Tracking:
//   • Updating a single 'size' variable across threads would cause massive contention.
//   • Instead, size is tracked using a distributed array of counters (similar to
//     LongAdder), combined when size() or mappingCount() is called.
//
// Concurrency Level (default 16): Kept for backward compatibility, but in Java 8+
// it is mostly ignored, serving only as a hint for initial table sizing.

// ─────────────────────────────────────────────────────────────────────────────
// KEY CHARACTERISTICS
// ─────────────────────────────────────────────────────────────────────────────
// ✔ Thread-safe without locking the whole map
// ✔ Reads are non-blocking (volatile reads)
// ✔ Writes lock only the affected bucket
// ✔ Supports atomic operations: putIfAbsent, replace, computeIfAbsent, merge
// ✔ Iteration is weakly consistent (won't throw ConcurrentModificationException)
// ✗ Does NOT allow null keys or null values (throws NullPointerException)
// ✗ No guaranteed ordering (use ConcurrentSkipListMap for sorted order)

public class MyConcurrentHashMap {
    public static void main(String[] args) {

        // ── Basic Usage ──────────────────────────────────────────────────────
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        map.put("Alice", 90);
        map.put("Bob",   85);
        map.put("Carol", 92);

        System.out.println("Map: " + map);                    // {Alice=90, Bob=85, Carol=92}
        System.out.println("Get Alice: " + map.get("Alice")); // 90

        // ── Atomic Operations ────────────────────────────────────────────────
        // putIfAbsent: inserts only if key is absent (atomic, no race condition)
        map.putIfAbsent("Alice", 0);          // ignored — Alice already exists
        map.putIfAbsent("Dave",  78);         // inserted

        // computeIfAbsent: compute value lazily if key missing
        map.computeIfAbsent("Eve", k -> k.length() * 10); // Eve=30

        // replace: atomic compare-and-replace
        boolean replaced = map.replace("Bob", 85, 99);    // CAS-style replace
        System.out.println("Bob replaced: " + replaced + " → " + map.get("Bob")); // true → 99

        // merge: add 5 to Carol's score; create entry if absent
        map.merge("Carol", 5, Integer::sum);              // Carol=97
        System.out.println("Carol after merge: " + map.get("Carol")); // 97

        // ── Iteration (weakly consistent — no ConcurrentModificationException)
        System.out.println("\nAll entries:");
        map.forEach((k, v) -> System.out.println("  " + k + " → " + v));

        // ── Bulk / Aggregation ───────────────────────────────────────────────
        // search: returns first non-null result across entries
        String topScorer = map.search(1, (k, v) -> v >= 95 ? k : null);
        System.out.println("Top scorer (≥95): " + topScorer);

        // reduce: sum all values in parallel (parallelism threshold = 1)
        int total = map.reduceValues(1, Integer::sum);
        System.out.println("Total score: " + total);

        // ── Size ────────────────────────────────────────────────────────────
        System.out.println("Size: " + map.size());   // approximate under high concurrency
        // mappingCount() preferred for large maps (returns long)
        System.out.println("Mapping count: " + map.mappingCount());

        // ── Null Restriction Demo ────────────────────────────────────────────
        try {
            map.put(null, 0);           // NullPointerException
        } catch (NullPointerException e) {
            System.out.println("Null key not allowed: " + e.getClass().getSimpleName());
        }

        // ── vs Hashtable (quick reminder) ────────────────────────────────────
        Hashtable<String, Integer> ht = new Hashtable<>();
        ht.put("X", 1);
        // ht is synchronized on 'this' for EVERY call → slow under concurrency
        // ConcurrentHashMap locks only per-bucket → far better throughput
        System.out.println("\nHashtable (legacy): " + ht);
        System.out.println("Prefer ConcurrentHashMap for concurrent workloads!");
    }
}
