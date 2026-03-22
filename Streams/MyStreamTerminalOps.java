package Streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/*
Important Points to Remember

>> Terminal operations produce a result or a side-effect and consume the stream.
>> After a terminal op is called, the stream is closed and cannot be reused.
>> They trigger the actual execution of the entire pipeline (lazy evaluation ends here).
>> Some terminal ops short-circuit - they stop processing as soon as a result is found.
*/

public class MyStreamTerminalOps {
    public static void main(String[] args) {

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<String> names = Arrays.asList("Aryan", "Kushal", "Sheetal", "Kanika");

        // .forEach() ---> performs an action on each element; returns void
        list.stream().forEach(n -> System.out.print(n + " ")); // 1 2 3 4 5 6
        System.out.println();

        // .count() ---> returns the number of elements in the stream as a long
        long count = list.stream().filter(n -> n % 2 == 0).count(); // 3
        System.out.println(count);

        // .collect() ---> accumulates elements into a collection or other container
        List<Integer> evens = list.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList()); // [2, 4, 6]
        System.out.println(evens);

        // Collectors.joining() ---> concatenates stream elements into a single String
        String joined = names.stream()
        .collect(Collectors.joining(", ", "[", "]")); // (mid, start, end)
        System.out.println(joined); // [Aryan, Kushal, Sheetal, Kanika]

        // Collectors.groupingBy() ---> groups elements by a classifier into a Map<K, List<V>>
        Map<Integer, List<String>> byLength = names.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println(byLength); // {5=[Aryan], 6=[Kushal], 7=[Sheetal, Kanika]}

        // Collectors.toMap() ---> collects into a Map<K, V> using key and value mappers
        Map<String, Integer> nameLengths = names.stream()
            .collect(Collectors.toMap(s -> s, String::length)); // throws error if duplicate keys exists
        System.out.println(nameLengths); // {Aryan=5, Kushal=6, Sheetal=7, Kanika=6}

        // .reduce() ---> combines elements into a single value using an accumulator
        // reduce(identity, accumulator) ---> identity is the starting value
        int sum = list.stream().reduce(0, Integer::sum); // 21
        System.out.println(sum);

        Optional<Integer> product = list.stream().reduce((a, b) -> a * b); // 720
        System.out.println(product.orElse(0));

        // .min() / .max() ---> returns the min/max element wrapped in an Optional
        // using Comparable and Comparator
        Optional<Integer> min = list.stream().min(Integer::compareTo); // Optional[1]
        Optional<Integer> max = list.stream().max((a, b) -> Integer.compare(a, b)); // Optional[6]
        System.out.println(min.get() + " " + max.get());
        // The rule is simple: .min() with a reversed comparator gives you the max, and vice versa.
        // Note: trick to remember it: min will take the 1st value, max will take the last value. So if you sort it in ascending order: (1st -> min and last -> max)

        // .sorted() is an intermediate op that reorders the WHOLE stream (unlike min/max which return ONE element)
        List<Integer> asc  = list.stream().sorted().toList();                          // [1, 2, 3, 4, 5, 6]
        List<Integer> desc = list.stream().sorted((a, b) -> Integer.compare(b, a)).toList(); // [6, 5, 4, 3, 2, 1]
        List<Integer> desc2 = list.stream().sorted(Comparator.reverseOrder()).toList(); // cleaner way
        System.out.println(asc + " " + desc + " " + desc2);

        // short-circuits -- stops as soon as it finds an element
        // .findFirst() ---> returns the first element of the stream as an Optional
        Optional<Integer> first = list.stream().filter(n -> n > 3).findFirst(); // Optional[4]
        System.out.println(first.orElse(-1));

        // .findAny() ---> returns any element (useful in parallel streams; no order guarantee)
        Optional<Integer> any = list.parallelStream().filter(n -> n > 3).findAny();
        System.out.println(any.isPresent()); // true
        System.out.println(any.get()); // returns value held by Optional

        // .anyMatch(predicate) ---> short-circuit; true if at least one element matches
        boolean hasEven = list.stream().anyMatch(n -> n % 2 == 0); // true
        System.out.println(hasEven);

        // .allMatch(predicate) ---> short-circuit; true if ALL elements match
        boolean allPositive = list.stream().allMatch(n -> n > 0); // true
        System.out.println(allPositive);

        // .noneMatch(predicate) ---> short-circuit; true if NO element matches
        boolean noneNegative = list.stream().noneMatch(n -> n < 0); // true
        System.out.println(noneNegative);

        // .toArray() ---> collects stream elements into an Object[] (or typed array)
        Object[] arr = list.stream().filter(n -> n % 2 != 0).toArray(); // [1, 3, 5]
        System.out.println(Arrays.toString(arr));

        // .toList() ---> collects stream elements into a List<>, no need of .collect()
        List<Integer> sqList = list.stream().map(x -> x * x).toList();
        System.out.println(sqList); // unmodifiable

        // IntStream / LongStream / DoubleStream terminal aggregations
        // sum(), average(), min(), max() are only available on primitive streams
        int total  = list.stream().mapToInt(Integer::intValue).sum();     // 21
        double avg = list.stream().mapToInt(Integer::intValue).average().orElse(0); // 3.5
        System.out.println(total + " " + avg);

        // Example: Count occurrence of character 'l' in "Hello World"
        String sentence = "Hello World";
        System.out.println(sentence.chars().filter(x -> x == 'l').count());
    }
}
