package Streams;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.*;

// Primitive Streams: IntStream, LongStream, DoubleStream
// Why? Avoid boxing/unboxing overhead of Stream<Integer> / Stream<Double>.
// Each has its own specialised terminal ops (sum, average, etc.)
public class MyPrimitiveStreams {
    public static void main(String[] args) {

        // ── Creating an IntStream ─────────────────────────────────────────────
        // IntStream.of(...)  →  like Stream.of but for ints (no boxing)
        // Used directly below in summaryStatistics example

        // ── range vs rangeClosed ──────────────────────────────────────────────
        // range(start, end)       → [start, end)   exclusive upper bound
        // rangeClosed(start, end) → [start, end]   inclusive upper bound

        System.out.print("range 1..5 : ");
        IntStream.range(1, 6).forEach(n -> System.out.print(n + " "));
        System.out.println();

        // ── Important terminal operations ─────────────────────────────────────
        int sum     = IntStream.rangeClosed(1, 5).sum();               // 15
        OptionalDouble avg = IntStream.rangeClosed(1, 5).average();   // 3.0
        OptionalInt  max  = IntStream.rangeClosed(1, 5).max();        // 5
        OptionalInt  min  = IntStream.rangeClosed(1, 5).min();        // 1
        long count        = IntStream.rangeClosed(1, 5).count();      // 5

        System.out.println("sum=" + sum + " avg=" + avg.getAsDouble()
                + " max=" + max.getAsInt() + " min=" + min.getAsInt()
                + " count=" + count);

        // summaryStatistics() — all five stats in one pass
        IntSummaryStatistics stats = IntStream.of(3, 7, 1, 9, 4).summaryStatistics();
        System.out.println("Stats: " + stats);
        // IntSummaryStatistics{count=5, sum=24, min=1, average=4.8, max=9}

        // ── .boxed() — back to Stream<Integer> ───────────────────────────────
        // Needed when you want to use Stream API (e.g. collect, sorted with Comparator)
        List<Integer> boxedList = IntStream.rangeClosed(1, 5)
                .boxed()                          // IntStream → Stream<Integer>
                .collect(Collectors.toList());
        System.out.println("Boxed list: " + boxedList); // [1, 2, 3, 4, 5]

        // ── mapToObj — IntStream → Stream<T> ─────────────────────────────────
        // Use when you want to produce objects from ints (e.g. strings)
        List<String> labels = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> "Item-" + i)       // IntStream → Stream<String>
                .collect(Collectors.toList());
        System.out.println("Labels: " + labels);  // [Item-1, Item-2, Item-3]

        // ── Conversions between primitive streams ─────────────────────────────
        // asLongStream()   → IntStream → LongStream  (widening, no data loss)
        // asDoubleStream() → IntStream → DoubleStream
        LongStream   longs   = IntStream.of(1, 2, 3).asLongStream();
        DoubleStream doubles = IntStream.of(1, 2, 3).asDoubleStream();

        System.out.println("LongStream sum   : " + longs.sum());    // 6
        System.out.println("DoubleStream avg : " + doubles.average().getAsDouble()); // 2.0

        // ── DoubleStream ──────────────────────────────────────────────────────
        // Similar API; has sum(), average(), min(), max(), summaryStatistics()
        double dSum = DoubleStream.of(1.5, 2.5, 3.0).sum();  // 7.0
        System.out.println("DoubleStream sum: " + dSum);

        // ── generate & iterate (infinite, must be limited) ────────────────────
        // generate(Supplier) — produces a constant or random stream
        IntStream.generate(() -> 1).limit(3).forEach(n -> System.out.print(n + " ")); // 1 1 1
        System.out.println();

        // iterate(seed, unaryOp) — like a for-loop
        IntStream.iterate(0, n -> n + 2).limit(5)
                .forEach(n -> System.out.print(n + " ")); // 0 2 4 6 8
        System.out.println();

        // ── flatMapToInt — flatten nested arrays into one IntStream ───────────
        List<int[]> arrays = List.of(new int[]{1, 2}, new int[]{3, 4});
        int flatSum = arrays.stream()
                .flatMapToInt(IntStream::of)  // Stream<int[]> → IntStream
                .sum();
        System.out.println("flatMapToInt sum: " + flatSum); // 10

        // ── Stream<Integer> → IntStream via mapToInt ──────────────────────────
        // Use mapToInt to go from an object stream to a primitive stream
        List<String> words = List.of("hello", "world", "java");
        int totalLen = words.stream()
                .mapToInt(String::length)   // Stream<String> → IntStream
                .sum();
        System.out.println("Total chars: " + totalLen); // 14
    }
}

