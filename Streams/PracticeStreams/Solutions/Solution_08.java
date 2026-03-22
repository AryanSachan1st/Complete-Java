package Streams.PracticeStreams.Solutions;

import java.util.List;

public class Solution_08 {
    public static void main(String[] args) {
        List<String> names = List.of("Rihana", "Manikarnika", "Sudarshan", "Casy", "Shreyas");

        boolean matched = names.stream().anyMatch(name -> name.startsWith("R"));
        System.out.println("Matched: " + matched);
    }
}
