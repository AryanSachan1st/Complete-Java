package Collections_Framework.Map;

import java.util.*;

/**
 * Immutable Maps in Java
 * ----------------------
 * An immutable map is a map whose content cannot be modified after it is created.
 *
 * Characteristics:
 * 1. Read-Only: Any attempt to add, remove, or modify elements will throw an
 *    UnsupportedOperationException.
 * 2. Thread-Safe: Since they cannot change, they are inherently thread-safe and
 *    can be shared concurrently among multiple threads.
 * 3. Null Values: Modern immutable maps (like those from Map.of or Map.copyOf)
 *    do not allow null keys or values. Nulls throw NullPointerException.
 * 4. Memory Efficient: They consume less memory than their mutable counterparts.
 *
 * Ways to create Immutable Maps:
 * 1. Collections.unmodifiableMap(map): Wraps an existing map. Modifying the
 *    original map will reflect in the unmodifiable map.
 * 2. Map.of(k1, v1...): Max 10 key-value pairs. Returns true immutable map.
 * 3. Map.ofEntries(Map.entry...): For more than 10 pairs.
 * 4. Map.copyOf(map): Creates an immutable copy of an existing map (Java 10+).
 */
public class MyImmutableMap {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("John", 25);
        map.put("Jane", 30);
        System.out.println("Original HashMap: " + map);

        // 1. Using Collections.unmodifiableMap (View of original map)
        @SuppressWarnings("unused")
        Map<String, Integer> unmodifiableMap = Collections.unmodifiableMap(map);

        // 2. Using Map.of (Up to 10 entries allowed)
        @SuppressWarnings("unused")
        Map<String, Integer> mapOf = Map.of("John", 25, "Jane", 30);

        // 3. Using Map.ofEntries (For any number of entries)
        Map<String, Integer> immutableMap = Map.ofEntries(
            Map.entry("John", 25),
            Map.entry("Jane", 30),
            Map.entry("Jim", 35),
            Map.entry("Jill", 40),
            Map.entry("Jack", 45)
        );
        System.out.println("Map.ofEntries: " + immutableMap);

        // 4. Using Map.copyOf (Creates an independent immutable copy)
        @SuppressWarnings("unused")
        Map<String, Integer> copyMap = Map.copyOf(map);

        // Attempting to modify an immutable map will throw UnsupportedOperationException
        // immutableMap.put("Jake", 50); // Throws Exception
    }
}
