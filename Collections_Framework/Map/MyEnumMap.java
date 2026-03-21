package Collections_Framework.Map;

import java.util.*;

/**
 * ======================= ENUM MAP INTERNAL WORKING =======================
 * 
 * 1. Introduction:
 *    - EnumMap is a specialized Map implementation for use with enum type keys.
 *    - All keys in an EnumMap must come from a single enum type that is
 *      specified when the map is created.
 *
 * 2. Internal Data Structure:
 *    - Internally, EnumMap is represented as two arrays of the same size:
 *      a) Object[] vals: Stores the values.
 *      b) K[] keyUniverse: Stores all possible keys of the specific enum.
 *    - The array size is exactly the number of constants in the enum.
 *
 * 3. Hashing and Indexing:
 *    - EnumMap does NOT use hashing (no hashCode() or collision resolution).
 *    - It uses the 'ordinal()' value of the enum constant as the array index.
 *    - E.g., if Day.Monday has ordinal 0, its value is stored at vals[0].
 *    - This makes lookup incredibly fast — O(1) direct array access.
 *
 * 4. Key Characteristics:
 *    - Performance: Faster than HashMap because there are no hash collisions,
 *      no rehashing, and it relies on simple array indexing.
 *    - Memory Efficiency: Very compact. It just allocates an array of the exact
 *      size of the enum. No Map.Entry nodes are created for each pair.
 *    - Ordering: Iteration order matches the natural order of the enum keys
 *      (the order in which the enum constants are declared).
 *    - Nulls: Null keys are NOT permitted (throws NullPointerException).
 *      Null values ARE permitted and internally wrapped in a special NULL token.
 * 
 * 5. Time Complexity:
 *    - put(), get(), remove(), containsKey() are all consistently O(1).
 * =========================================================================
 */
enum Day {
    Monday, Tuesday, Wednesday, Thrusday, Friday, Saturday, Sunday
}

public class MyEnumMap {
    public static void main(String[] args) {
        Map<Day, String> map = new EnumMap<>(Day.class);

        map.put(Day.Monday, "Gym");
        map.put(Day.Tuesday, "Java");
        map.put(Day.Wednesday, "Cooking");

        System.out.println(map.get(Day.Monday));
        System.out.println(map.get(Day.Saturday));
        System.out.println(map);
        System.out.println(Day.Thrusday.ordinal());
    }
}
