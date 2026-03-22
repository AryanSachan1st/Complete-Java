package Streams.PracticeStreams.Solutions;

import java.util.List;
import java.util.stream.Collectors;

public class Solution_11 {
    public static void main(String[] args) {
        List<int[]> transcations = List.of(
            new int[] {1, 5100},
            new int[] {2, 6709},
            new int[] {3, 3450},
            new int[] {4, 8903},
            new int[] {5, 4000}
        );

        double averageT = transcations.stream().collect(Collectors.averagingDouble(t -> t[1]));
        System.out.println("Average: " + averageT);
    }
}
