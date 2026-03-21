package Collections_Framework.Map;

import java.util.WeakHashMap;

/*
 * ═══════════════════════════════════════════════════════════
 *  WeakHashMap — Overview
 * ═══════════════════════════════════════════════════════════
 *  A Map implementation backed by a hash table where KEYS are
 *  stored as WeakReferences. When a key has no more strong
 *  references elsewhere, the GC can reclaim it and the entry
 *  is automatically removed from the map.
 *
 *  KEY CHARACTERISTICS:
 *  ┌─────────────────────────────────────────────────────┐
 *  │ 1. Weak Keys     – Keys are weakly referenced; GC   │
 *  │                    can collect them automatically.  │
 *  │ 2. Auto-cleanup  – Entries disappear when key obj   │
 *  │                    is garbage collected.            │
 *  │ 3. Null support  – Allows ONE null key & null vals. │
 *  │ 4. Not thread-safe – Must synchronize externally    │
 *  │                    or use Collections.synchronized  │
 *  │ 5. No ordering   – Iteration order is unpredictable.│
 *  │ 6. Size unstable – size() may shrink between calls. │
 *  └─────────────────────────────────────────────────────┘
 *
 *  INTERNAL STRUCTURE:
 *  – Backed by an array of Entry<K,V> buckets (like HashMap).
 *  – Each Entry wraps the key in a WeakReference + a
 *    ReferenceQueue. When GC collects a key, its Reference
 *    is enqueued; the map polls this queue on the next
 *    structural operation (put/get/size) to purge stale entries.
 *
 *  WHEN TO USE:
 *  – Caches where entries should vanish once the caller no
 *    longer holds the key object (avoids memory leaks).
 *  – Listener / callback registries (no need to deregister).
 *  – Metadata attached to objects without preventing GC.
 *  – Session-scoped data keyed on short-lived objects.
 *
 *  WHEN NOT TO USE:
 *  – When you need guaranteed persistence of entries.
 *  – When primitive wrappers are keys (auto-boxing creates
 *    short-lived objects → entries may vanish unexpectedly).
 *  – Multi-threaded environments without external sync.
 */

public class MyWeakHashMap {

    // ── Simple cache demo ──────────────────────────────────
    static WeakHashMap<String, String> cache = new WeakHashMap<>();

    static String computeIfAbsent(String key) {
        // If key was GC'd, cache.get() returns null → recompute
        return cache.computeIfAbsent(key, k -> "Result(" + k + ")");
    }

    public static void main(String[] args) throws InterruptedException {

        // 1. Basic put / get
        WeakHashMap<Object, String> map = new WeakHashMap<>();
        Object key1 = new Object();   // strong reference exists
        Object key2 = new Object();
        map.put(key1, "Value-1");
        map.put(key2, "Value-2");
        System.out.println("Before GC  → size: " + map.size()); // 2

        // 2. Drop strong reference → key2 becomes weakly reachable
        key2 = null;
        System.gc();                  // hint GC (not guaranteed)
        Thread.sleep(100);            // give GC a moment

        // Entry for key2 may now be purged automatically
        System.out.println("After GC   → size: " + map.size()); // likely 1

        // 3. key1 still alive → its entry survives
        System.out.println("key1 entry → " + map.get(key1));    // Value-1

        // 4. Null key support
        map.put(null, "NullKeyValue");
        System.out.println("Null key   → " + map.get(null));    // NullKeyValue

        // 5. Cache use-case
        String k = new String("report");   // intentionally non-interned
        System.out.println(computeIfAbsent(k));          // computes
        System.out.println(computeIfAbsent(k));          // cache hit
        k = null;
        System.gc();
        Thread.sleep(100);
        // After GC the cache entry is gone — no memory leak
        System.out.println("Cache size after GC → " + cache.size()); // 0 or 1
    }
}

