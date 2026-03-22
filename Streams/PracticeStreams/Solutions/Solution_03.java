package Streams.PracticeStreams.Solutions;

import java.util.List;

public class Solution_03 {
    public static void main(String[] args) {
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        
        int evenSum = nums.stream().filter(x -> x % 2 == 0).reduce(0, Integer::sum);
        System.out.println(evenSum);
    }
}
