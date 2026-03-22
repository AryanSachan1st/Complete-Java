package Streams.PracticeStreams.Solutions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Solution_05 {
    public static void main(String[] args) {
        List<String> words = List.of("Kanpur", "Patna", "Jhansi", "Chennai", "Goa", "Gaya");

        Map<Boolean, List<String>> res = words.stream().collect(Collectors.partitioningBy(x -> x.length() < 5));

        System.out.println("Strings having less than 5 characters: " + res.get(true));
        System.out.println("Total strings remaining: " + res.get(false).size());
    }
}
