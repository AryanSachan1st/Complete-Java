package Collections_Framework.Map.ConcurrentMaps;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.Map;

/*
 * ConcurrentSkipListMap in Java
 * 
 * Hierarchy:
 * - Implements ConcurrentNavigableMap (which extends NavigableMap, SortedMap, Map)
 * - Inherits from AbstractMap
 * 
 * Internal Working of SkipList:
 * - A SkipList is a probabilistic data structure that allows O(log n) search, 
 *   insertion, and deletion complexity.
 * - It achieves this by maintaining a linked hierarchy of subsequences, with 
 *   each successive subsequence skipping over fewer elements.
 * - The lowest layer (level 0) is a standard sorted linked list containing all elements.
 * - Higher layers act as "express lanes" for faster traversal.
 * - Search starts at the highest layer, moves right until the next element is 
 *   greater than the target, then drops down a layer and repeats.
 * 
 * Key Characteristics of ConcurrentSkipListMap:
 * - Thread-Safe: Designed for high concurrency.
 * - Lock-Free: Uses lock-free operations using CAS (Compare-And-Swap).
 * - Sorted: Elements are sorted according to natural ordering or a Comparator.
 * - Nulls Not Allowed: Neither keys nor values can be null.
 * - Alternative to TreeMap: It's the concurrent equivalent of a TreeMap.
 */
public class MyConcurrentSkipListMap {
    public static void main(String[] args) {
        // Little Code Implementation
        ConcurrentSkipListMap<Integer, String> map = new ConcurrentSkipListMap<>();

        // Inserting elements
        map.put(3, "Apple");
        map.put(1, "Banana");
        map.put(4, "Cherry");
        map.put(2, "Date");

        // Simulating concurrent operations
        Runnable task1 = () -> map.put(5, "Elderberry");
        Runnable task2 = () -> map.remove(2);

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Iteration outputs elements in sorted order of keys
        System.out.println("Map Elements:");
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        // Useful NavigableMap features
        System.out.println("\nFirst Key: " + map.firstKey());
        System.out.println("Last Key: " + map.lastKey());
        System.out.println("Ceiling Key for 3: " + map.ceilingKey(3));
    }
}
