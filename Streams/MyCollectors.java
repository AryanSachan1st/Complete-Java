package Streams;

import java.util.*;
import java.util.stream.*;

// Collectors: terminal operations that accumulate stream elements into a container
// (List, Set, Map, String, etc.) via Stream.collect(Collector)
public class MyCollectors {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie", "Alice", "Dave");
        List<Integer> nums = List.of(3, 7, 1, 9, 4, 2);

        // ── Collecting to a List ──────────────────────────────────────────────
        // toList() preserves encounter order and allows duplicates
        List<String> nameList = names.stream()
                .collect(Collectors.toList());
        System.out.println("List: " + nameList);

        // ── Collecting to a Set ───────────────────────────────────────────────
        // toSet() removes duplicates; order is NOT guaranteed
        Set<String> nameSet = names.stream()
                .collect(Collectors.toSet());
        System.out.println("Set (no duplicates): " + nameSet);

        // ── Collecting to a Specific Collection ───────────────────────────────
        // toCollection(Supplier) lets you choose any Collection implementation
        // Here we get a sorted, duplicate-free result via TreeSet
        TreeSet<String> sortedSet = names.stream()
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("TreeSet (sorted): " + sortedSet);

        // ── Joining Strings ───────────────────────────────────────────────────
        // joining(delimiter, prefix, suffix) concatenates all elements into one String
        String joined = names.stream()
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("Joined: " + joined);

        // ── Summarizing ───────────────────────────────────────────────────────
        // summarizingInt returns an IntSummaryStatistics object with:
        // count, sum, min, max, average — all computed in a single pass
        IntSummaryStatistics stats = nums.stream()
                .collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println("Stats → count=" + stats.getCount()
                + " sum=" + stats.getSum()
                + " min=" + stats.getMin()
                + " max=" + stats.getMax()
                + " avg=" + stats.getAverage()
            );

        // ── Grouping Elements — groupingBy ────────────────────────────────────
        // groupingBy(classifier) partitions elements into a Map<K, List<V>>
        // where the key is the result of applying the classifier function

        // 1) Simple grouping by string length
        Map<Integer, List<String>> byLength = names.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println("Grouped by length: " + byLength);

        // 2) Downstream collector: count elements in each group
        Map<Integer, Long> countByLength = names.stream()
                .collect(Collectors.groupingBy(String::length, Collectors.counting()));
        System.out.println("Count by length: " + countByLength);

        // 3) Collecting into a TreeMap so keys are sorted
        Map<Integer, List<String>> sortedGrouping = names.stream()
                .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList()));
        System.out.println("Sorted grouping: " + sortedGrouping);

        // 4) Joining within each group
        Map<Integer, String> joinedByLength = names.stream()
                .collect(Collectors.groupingBy(String::length, Collectors.joining(", ")));
        System.out.println("Joined by length: " + joinedByLength);

        // ── Partitioning Elements ─────────────────────────────────────────────
        // partitioningBy is a special groupingBy that only produces two groups:
        // true (predicate matches) and false (predicate doesn't match)
        Map<Boolean, List<Integer>> partitioned = nums.stream()
                .collect(Collectors.partitioningBy(n -> n > 4));
        System.out.println("Partition (>4): " + partitioned);
        // {false=[3, 1, 4, 2], true=[7, 9]}

        // ── Mapping and Collecting ─────────────────────────────────────────────
        // mapping(mapper, downstream) applies a transformation before collecting.
        // Useful as a downstream collector inside groupingBy / partitioningBy.
        // Here: group by name length, then keep only the uppercased names per group
        Map<Integer, List<String>> mappedGroup = names.stream()
                .collect(Collectors.groupingBy(
                        String::length,
                        Collectors.mapping(String::toUpperCase, Collectors.toList())
                ));
        System.out.println("Mapped grouping: " + mappedGroup);

        // toMap, ex used: create a map where key = element, value = element.length 
        List<String> fruits = Arrays.asList("Cherry", "Banana", "Mango", "Apple");
        Map<String, Integer> map =  fruits.stream().collect(Collectors.toMap(k -> k.toLowerCase(), v -> v.length(), (oldCount, currCount) -> oldCount + currCount)); // .toMap(key, value, function to handle duplicates becz, keys can not have dupes)
        System.out.println(map);
    }
}
