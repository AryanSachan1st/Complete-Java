package Streams.PracticeStreams.Solutions;

import java.util.List;

public class Solution_07 {
    public static void main(String[] args) {
        List<Integer> nums = List.of(1, 4, 6, 3, 3, 7, 8, 7, 2, 1, 1);
        List<Integer> sortedUniqueNums = nums.stream().distinct().sorted().toList();
        System.out.println(sortedUniqueNums);
    }
}
