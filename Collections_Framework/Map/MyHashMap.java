package Collections_Framework.Map;

import java.util.*;

/*
 * ── HOW HashMap WORKS INTERNALLY ────────────────────────────────────────────
 *
 *  Data structure : array of "buckets"  (Node<K,V>[] table)
 *  Default capacity : 16 slots  |  Load factor : 0.75
 *  if hashmap's size reaches cap * loadFactor size => resize (2x size) the hashmap
 *
 *  PUT  key → value
 *    1. hash(key)  →  spreads bits to reduce collisions
 *    2. index = hash & (capacity - 1)   (fast modulo via bit-mask)
 *    3. If bucket empty  → insert new Node
 *       If collision      → chain as linked-list (Java 8+: tree after 8 nodes)
 *
 *  GET  key
 *    1. Same hash + index formula
 *    2. Walk the chain, compare with equals()  →  O(1) avg, O(log n) worst
 *
 *  RESIZE  (rehash)
 *    When size > capacity × loadFactor → double capacity, rehash all entries
 *    Expensive, so pre-size if count is known: new HashMap<>(expectedSize / 0.75 + 1)
 *
 *  KEY CHARACTERISTICS
 *    ✔ Null key/values
 *        Exactly ONE null key is allowed (hashed to bucket 0).
 *        Any number of null values are fine.
 *        Hashtable (legacy) rejects both — HashMap is the modern replacement.
 *
 *    ✔ Not thread-safe
 *        Concurrent writes can corrupt the internal structure (infinite loop in
 *        Java 7 resize was a famous bug). Alternatives:
 *          • Collections.synchronizedMap(map)  – coarse lock, simple
 *          • ConcurrentHashMap               – segment-level lock, faster
 *
 *    ✔ No guaranteed iteration order
 *        Order can change after a rehash. If order matters:
 *          • LinkedHashMap  – preserves insertion order  (slight memory overhead)
 *          • TreeMap        – always sorted by key       (O(log n) ops, needs Comparable)
 *
 *    ✔ Performance: O(1) average, O(log n) worst-case
 *        Best case  : single entry per bucket (no collision)
 *        Worst case : many keys hash to same bucket → tree traversal (Java 8+)
 *        Degraded perf signals a bad hashCode() implementation on the key class.
 *
 *    ✔ equals() + hashCode() contract MUST be consistent
 *        If two objects are equal → they MUST have the same hashCode.
 *        Violating this causes silent data loss (entry stored but never found).
 *        Always override both together when using custom objects as keys.
 * ────────────────────────────────────────────────────────────────────────────
 */
public class MyHashMap {

    /*
     * ── BASIC COMPONENTS ─────────────────────────────────────────────────────
     *
     * 1. KEY
     * The thing you use to store and find a value.
     * Must be unique — if you put() the same key again, the old value is replaced.
     * Key is passed to the hash function to decide WHERE to store the entry.
     * Rule: if you use a custom object as a key, override equals() and hashCode().
     *
     * 2. VALUE
     * The data you want to store against a key.
     * Can be anything — a String, Integer, List, even another HashMap.
     * Duplicates are allowed (two different keys can store the same value).
     * A value can also be null.
     *
     * 3. BUCKET
     * Think of a bucket as a slot/box in an array.
     * HashMap internally keeps an array of 16 buckets by default.
     * Each bucket can hold one entry or a chain of entries (when keys collide).
     * The bucket index tells HashMap EXACTLY where to look — no searching needed.
     *
     * Bucket array visual (capacity = 8):
     * index: [0] [1] [2] [3] [4] [5] [6] [7]
     * null "Bob" null null "Alice" null null null
     * get("Alice") → compute index → 4 -> go directly to [4] → done in one step.
     *
     * 4. HASH FUNCTION
     * Converts the key into a number (hash code), then maps it to a bucket index.
     * Step 1 : hashCode = key.hashCode() e.g. "Alice" → 63378562
     * Step 2 : spread = hashCode ^ (hashCode >>> 16) (reduces clustering)
     * Step 3 : index = spread & (capacity - 1) e.g. spread & 15 → index 2
     * The result is always a valid bucket index, computed in constant time.
     * A good hash function spreads keys evenly so buckets don't pile up.
     *
     * examples:
     * hashCode(s1) % arraySize → bucket 5 → "Backend Dev"
     * hashCode(s2) % arraySize → bucket 2 → "Frontend Dev"
     * hashCode(s3) % arraySize → bucket 1 → "Researcher"
     *
     * ── HOW HashMap ACHIEVES O(1) TIME COMPLEXITY ────────────────────────────
     *
     * With arrays, finding a value at a known index is instant (one memory jump).
     * HashMap turns every key into an index using the hash function.
     * So instead of scanning all entries, it jumps straight to the right bucket.
     *
     * put("Alice", 90):
     * hash("Alice") → index 4 → store at table[4] (1 step)
     *
     * get("Alice"):
     * hash("Alice") → index 4 → read table[4] → return 90 (1 step)
     *
     * No loops. No comparisons across the whole map.
     * That is why both put() and get() run in O(1) on average.
     *
     * COLLISION → CHAINING → TREE (the full story)
     * ─────────────────────────────────────────────
     * Step 1 — Collision
     * Two different keys produce the same bucket index.
     * Example: "Aa" and "BB" both hash to the same index.
     * HashMap cannot just overwrite — they are different keys!
     *
     * Step 2 — Chaining (linked list)
     * HashMap links colliding entries in a singly linked list inside the bucket.
     * Bucket [3]: "Aa"(val=1) → "BB"(val=2) → null
     * To find "BB": go to bucket [3], then walk the list checking key with
     * equals().
     * With only 2–3 nodes, this is still very fast — nearly O(1) in practice.
     *
     * Step 3 — Treeification (Java 8+)
     * Problem: if many keys land in the same bucket (e.g. bad hashCode, or an
     * attacker deliberately crafts keys), the chain grows long — O(n) per lookup.
     * Fix: once a chain reaches 8 nodes, Java converts it to a Red-Black Tree.
     *
     * Linked list chain (slow when long):
     * [bucket 3] → A → B → C → D → E → F → G → H ← walk every node = O(n)
     *
     * Red-Black Tree (fast even when long):
     * D
     * / \
     * B F
     * / \ / \
     * A C E G ← binary search = O(log n)
     *
     * A Red-Black Tree is a self-balancing binary search tree.
     * At every step you go left or right — halving the remaining nodes to check.
     * 8 nodes in a tree → at most 3–4 comparisons. 1000 nodes → at most ~10.
     *
     * Step 4 — Back to list (untreeify)
     * If entries are removed and the tree shrinks to 6 nodes, it converts back
     * to a linked list to save memory (trees use more space per node).
     * ─────────────────────────────────────────────────────────────────────────
     */

    // ── NODE — the actual object stored inside each bucket ────────────────────
    // Every put() creates one Node. Every bucket slot either holds null or a Node.
    // When two keys collide, nodes are linked via the 'next' pointer forming a
    // chain.
    static class Node<K, V> {
        final int hash; // cached result of hash(key) — reused during resize
        final K key; // the key you passed to put(); never changes
        V value; // the value; replaced if same key is put() again
        Node<K, V> next; // points to next Node in the chain; null means end of chain

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next; // null for a fresh single entry, non-null on collision
        }
    }
    // ─────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) {

        // ── 1. BASIC OPERATIONS ──────────────────────────────────────────────
        HashMap<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 90); // hash("Alice") → bucket index → insert
        scores.put("Bob", 85);
        scores.put("Alice", 95); // same key → value OVERWRITTEN (not duplicated)
        scores.put(null, 0); // null key allowed; stored at bucket[0]

        System.out.println(scores.get("Alice")); // 95
        System.out.println(scores.get("Charlie")); // null (key absent)
        System.out.println(scores.getOrDefault("Dan", -1)); // -1 (safe fallback)

        // ── 2. CHECKING & CONDITIONAL PUT ───────────────────────────────────
        System.out.println(scores.containsKey("Bob")); // true
        System.out.println(scores.containsValue(85)); // true

        scores.putIfAbsent("Bob", 100); // Bob already exists → no change
        scores.putIfAbsent("Eve", 78); // Eve absent → inserted
        System.out.println(scores.get("Bob")); // 85 (unchanged)

        // ── 3. ITERATION (no guaranteed order) ──────────────────────────────
        for (Map.Entry<String, Integer> e : scores.entrySet())
            System.out.println(e.getKey() + " → " + e.getValue());

        scores.forEach((k, v) -> System.out.println(k + " = " + v)); // lambda style

        // ── 4. COMPUTE HELPERS ───────────────────────────────────────────────
        // merge: if key absent insert value, else apply function on old+new
        scores.merge("Alice", 5, Integer::sum); // 95 + 5 = 100
        // compute: always apply function (can return null to remove entry)
        scores.compute("Bob", (k, v) -> v == null ? 1 : v + 10); // 85 → 95

        // ── 5. REMOVAL ──────────────────────────────────────────────────────
        scores.remove("Eve"); // remove by key
        scores.remove("Bob", 99); // conditional remove (value must match)
        System.out.println(scores.containsKey("Bob")); // true; value was 95, not 99

        // ── 6. COLLISION DEMO (same bucket, different keys) ──────────────────
        // "Aa" and "BB" have the same hashCode in Java → collision → chained
        HashMap<String, Integer> collision = new HashMap<>();
        collision.put("Aa", 1);
        collision.put("BB", 2);
        System.out.println("Aa".hashCode() == "BB".hashCode()); // true → same bucket
        System.out.println(collision.get("Aa") + ", " + collision.get("BB")); // 1, 2

        // ── 7. PRE-SIZING TO AVOID REHASH ───────────────────────────────────
        // Storing 80 entries without triggering resize:
        HashMap<Integer, String> preSize = new HashMap<>(134, 0.6f); // 80 / 0.75 ≈ 134
        for (int i = 0; i < 80; i++)
            preSize.put(i, "v" + i);
        System.out.println("Size: " + preSize.size()); // 80
    }
}
