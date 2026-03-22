package Streams.PracticeStreams.Solutions;

import java.util.List;
import java.util.stream.Collectors;

public class Solution_01 {
    public static void main(String[] args) {
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> res = nums.stream().filter(x -> x % 3 == 0).collect(Collectors.toList());

        res.forEach(x -> System.out.print(x + " "));
    }
}
