package Streams.PracticeStreams.Solutions;

import java.util.stream.Stream;

public class Solution_13 {
    public static void main(String[] args) {
        int summation = Stream.iterate(1, x -> x + 1).filter(x -> x % 2 != 0).limit(5).map(x -> x * x).reduce(0, Integer::sum);
        
        System.out.println(summation);
    }
}
