package Collections_Framework.Map;

import java.util.IdentityHashMap;
import java.util.HashMap;
import java.util.Map;

/*
 * ═══════════════════════════════════════════════════════════
 *  IdentityHashMap — Overview
 * ═══════════════════════════════════════════════════════════
 *  A Map implementation that uses REFERENCE EQUALITY (==)
 *  instead of OBJECT EQUALITY (.equals()) when comparing keys.
 *  Internally it uses System.identityHashCode() instead of
 *  the key's own hashCode() method.
 *
 *  HashMap  → compares keys with key1.equals(key2)
 *  IdentityHashMap → compares keys with key1 == key2
 *
 *  KEY CHARACTERISTICS:
 *  ┌─────────────────────────────────────────────────────┐
 *  │ 1. Reference equality  – Uses == not .equals()      │
 *  │ 2. Identity hashCode   – Uses System.identityHash-  │
 *  │                          Code(), NOT key.hashCode() │
 *  │ 3. Two equal Strings   – Treated as DIFFERENT keys  │
 *  │                          if they are distinct objs. │
 *  │ 4. Allows null keys    – One null key is permitted.  │
 *  │ 5. Not thread-safe     – No built-in synchronization│
 *  │ 6. Linear-probe table  – Uses a flat Object[] array │
 *  │                          (no linked buckets/trees). │
 *  │ 7. No ordering         – Iteration order varies.    │
 *  └─────────────────────────────────────────────────────┘
 *
 *  INTERNAL STRUCTURE:
 *  – Backed by a single Object[] table of size 2*capacity.
 *  – Keys and values sit in adjacent pairs: table[2i]=key,
 *    table[2i+1]=value. Collisions resolved by linear probing.
 *  – Default initial capacity is 32 (16 key-value pairs).
 *
 *  WHEN TO USE:
 *  – Topology-preserving object graph transformations
 *    (serialization, deep-copy) — track visited objects by
 *    identity, not by value equality.
 *  – Proxy / interception frameworks that must distinguish
 *    two logically-equal but physically-different objects.
 *  – Cycle detection in object graphs.
 *
 *  WHEN NOT TO USE:
 *  – General-purpose key-value storage (use HashMap).
 *  – When String literals / interned keys are used naively
 *    (compiler may or may not intern them → unpredictable).
 */

public class MyIdentityHashMap {

    public static void main(String[] args) {

        // ── 1. The Core Difference: == vs .equals() ──────────
        String a = new String("hello");   // deliberately new object
        String b = new String("hello");   // same value, different object (diff heap loc)

        // HashMap: a.equals(b) → true → treated as ONE key
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put(a, 1);
        hashMap.put(b, 2);   // overwrites — same key by .equals()
        System.out.println("HashMap  size: " + hashMap.size());   // 1

        // both hashcodes will be different -> objects (in heap) are different
        System.out.println(System.identityHashCode(a));
        System.out.println(System.identityHashCode(b));

        // both hashcodes will be same -> both strings have same value
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());

        // IdentityHashMap: a == b → false → treated as TWO keys
        Map<String, Integer> idMap = new IdentityHashMap<>();
        idMap.put(a, 1);
        idMap.put(b, 2);     // distinct — different object references
        System.out.println("IdentityHashMap size: " + idMap.size()); // 2

        // ── 2. Retrieving values by reference ─────────────────
        System.out.println("Get 'a': " + idMap.get(a));  // 1
        System.out.println("Get 'b': " + idMap.get(b));  // 2

        // A third object with the same value is a DIFFERENT key
        String c = new String("hello");
        System.out.println("Get 'c': " + idMap.get(c));  // null (c != a, c != b)

        // ── 3. Null key support ───────────────────────────────
        idMap.put(null, 99);
        System.out.println("Null key: " + idMap.get(null)); // 99

        // ── 4. Object-graph traversal / cycle detection ───────
        // Track visited nodes by their identity (not value)
        IdentityHashMap<Object, Boolean> visited = new IdentityHashMap<>();
        Object node1 = new Object();
        Object node2 = new Object();
        visited.put(node1, true);
        visited.put(node2, true);

        System.out.println("node1 visited: " + visited.containsKey(node1)); // true
        System.out.println("new Object() visited: " + visited.containsKey(new Object())); // false

        // ── 5. Iterating entries ──────────────────────────────
        System.out.println("\nAll entries in IdentityHashMap:");
        for (Map.Entry<String, Integer> entry : idMap.entrySet()) {
            System.out.println("  Key@" + System.identityHashCode(entry.getKey()) + " = " + entry.getValue());
        }
    }
}
