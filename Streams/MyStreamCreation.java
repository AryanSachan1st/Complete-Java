package Streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/*
>> Introduced in Java 8
>> Processes collection of data in a functional and declarative manner
>> Simplify Data Processing
>> Embrace Functional Programming
>> Improve Readability and Maintainability
>> Enable Easy Parallelism

>> Usecase: convert any collection of elements into stream just to perform functional programming where we can apply a lot of sequential and parallel aggregate operations

>> How to use?: source --> stream --> intermediate operations --> terminal operation 
*/

public class MyStreamCreation {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 3, 4, 5);

        // just an illustration of the flow (we will discuss each part in subsequent classes)
        long totalOdds = list.stream().filter(num -> num % 2 != 0).count(); // 1 liner
        System.out.println(totalOdds);

        // How to create streams?
        // from Collection-
        List<Integer> list1 = List.of(10, 20, 30, 40);
        Stream<Integer> stream1= list1.stream();
        System.out.println("listObject.stream()" + stream1);

        // from Arrays-
        String[] names = {"Aryan", "Rahul", "Nikunj", "Krishna"};
        Stream<String> stream2 = Arrays.stream(names);
        System.out.println("Arrays.stream(array): " + stream2);

        // using Stream.of()
        Stream<Integer> stream3 = Stream.of(1, 2, 3, 4);
        System.out.println("Stream.of(vals): " + stream3);

        // using Stream.generate() [ INF stream, use .limit() to avoid infinity]
        Stream<Integer> stream4 = Stream.generate(() -> 1).limit(50);
        System.out.println("Stream.generate(Supplier).limit(num): " + stream4);

        // using Stream.iterate(seed, Function [UnaryOperator])
        Stream<Integer> stream5 = Stream.iterate(1, x -> x + 1).limit(50); // total 50 items
        System.out.println("Stream.iterate(): " + stream5);
    }
}
