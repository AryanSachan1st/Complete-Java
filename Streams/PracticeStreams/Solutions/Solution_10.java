package Streams.PracticeStreams.Solutions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Solution_10 {
    public static void main(String[] args) {
        List<Integer> nums = List.of(1, 20, 3, 40, 50, 60, 7, 80, 9);

        Map<Boolean, List<Integer>> evenOdds = nums.stream().collect(Collectors.partitioningBy(num -> num % 2 == 0));

        System.out.println("Evens: " + evenOdds.get(true));
        System.out.println("Odds: " + evenOdds.get(false));
    }
}
