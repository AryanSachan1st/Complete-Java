package Streams;

import java.util.List;
import java.util.stream.Stream;

/*
>> A type of stream that enables parallel processing of elements
>> Allowing multiple threads to process different parts of the stream simultaneously
>> This can significantly improve performance for larger datasets
>> Workload is distributed across datasets 
*/

public class MyParallelStream {
    private static long factorial(int n) {
        int fact = 1;

        for (int i=2; i<=n; i++) {
            fact *= i;
        }

        return fact;
    }
    public static void main(String[] args) {
        List<Integer> counting = Stream.iterate(1, x -> x + 1).limit(20000).toList();

        // sequential stream
        long startTime = System.currentTimeMillis();
        @SuppressWarnings("unused")
        List<Long> factorialList = counting.stream().map(MyParallelStream::factorial).toList();
        long endTime = System.currentTimeMillis();
        System.out.println("Total Time spend using sequential stream: " + (endTime - startTime) + "ms");

        // parallel stream (only use for large dataset, and where every element is independent of other)
        startTime = System.currentTimeMillis();
        factorialList = counting.parallelStream().map(MyParallelStream::factorial).toList();
        endTime = System.currentTimeMillis();
        System.out.println("Total Time spend using parallel stream: " + (endTime - startTime) + "ms");
        

    }
}
