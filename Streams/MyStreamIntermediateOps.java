package Streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/*
Important Points to Remember

>> Streams are not data structures - they don't store data, they process it.
>> Streams are consumed once - you can't reuse a stream after a terminal operation.
>> Intermediate operations convert a stream into another stream
>> Lazy evaluation - intermediate operations don't execute until a terminal operation is invoked.
>> Non-mutating - they don't modify the original collection.
*/

public class MyStreamIntermediateOps {
    public static void main(String[] args) {

        // filter (takes a Predicate and put those values in the stream which follows (returns True) the Predicate Logic)
        List<Integer> list = Arrays.asList(1, 2, 3, 1, 4, 5, 6);
        Stream<Integer> stream1 = list.stream().filter(num -> num % 2 == 0); // no exec
        long evens = stream1.count(); // executed here (.count() --> terminal func)
        System.out.println(evens);

        // map (takes a Function and convert each element in something else)
        List<String> names = Arrays.asList("Aryan", "Kushal", "Sheetal", "Kushal", "Kanika");
        @SuppressWarnings("unused")
        Stream<String> stream2 = names.stream().map(String::toUpperCase); // will not execm becz no terminal function is called

        // .sorted() --> sorts in natural order
        @SuppressWarnings("unused")
        Stream<String> stream3 = names.stream().sorted();
        @SuppressWarnings("unused")
        Stream<String> stream4 = names.stream().sorted((a, b) -> a.length() - b.length());

        // .distinct() --> unique
        @SuppressWarnings("unused")
        Stream<Integer> stream5 = list.stream().filter(num -> num == 1).distinct();

        // .limit(n) ---> keeps only the first n elements
        @SuppressWarnings("unused")
        Stream<String> stream6 = names.stream().limit(3); // 1st 3
        @SuppressWarnings("unused")
        Stream<Integer> intStream = Stream.iterate(1, x -> x + 1).limit(30);

        // .skip(n) ---> skips the first n elements
        @SuppressWarnings("unused")
        Stream<Integer> stream7 = list.stream().skip(3); // skip(n) --> skip elements till index n-1

        // .flatMap() ---> flattens nested structures (Stream<List<T>> ---> Stream<T>)
        List<List<Integer>> nested = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList(4, 5, 6)
        );
        @SuppressWarnings("unused")
        Stream<Integer> stream8 = nested.stream().flatMap(eachList -> eachList.stream()); // [1, 2, 3, 4, 5, 6]
        // example: convert all the words into capitals and in one single stream
        List<String> greetings = List.of(
            "Hello there",
            "Good Morning",
            "Have a great day"
        );
        System.out.println(
            greetings.stream()
                .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
                .map(String::toUpperCase)
                .toList()
        );


        // .peek() ---> like map but for side-effects (debugging); returns the same stream
        // useful to inspect elements mid-pipeline without consuming the stream
        // @SuppressWarnings("unused")
        List<Integer> list9 = list.stream()
            .filter(n -> n % 2 == 0)
            .peek(n -> System.out.println("After filter: " + n)) // [2, 4, 6]
            .map(n -> n * 10)
            .toList();
        System.out.println("After map: " + list9);

        // .mapToInt() / .mapToLong() / .mapToDouble()
        // ---> converts Stream<T> to primitive specialised IntStream/LongStream/DoubleStream
        // avoids boxing/unboxing overhead; unlocks sum(), average(), min(), max()
        int total = list.stream().mapToInt(Integer::intValue).sum(); // 22
        System.out.println(total);

        double avg = list.stream().mapToDouble(Integer::doubleValue).average().orElse(0); // if average is empty, return 0
        System.out.println(avg);

        // .takeWhile(predicate) ---> keeps elements while predicate is true, stops at first false
        // (works predictably on ordered/sorted streams)
        @SuppressWarnings("unused")
        Stream<Integer> stream10 = Stream.of(1, 2, 3, 4, 5, 1)
            .takeWhile(n -> n < 4); // [1, 2, 3]  -- stops as soon as 4 is reached

        // .dropWhile(predicate) ---> drops elements while predicate is true, keeps the rest
        @SuppressWarnings("unused")
        Stream<Integer> stream11 = Stream.of(1, 2, 3, 4, 5)
            .dropWhile(n -> n < 4); // [4, 5]  -- drops 1,2,3 then keeps everything after
    }
}
