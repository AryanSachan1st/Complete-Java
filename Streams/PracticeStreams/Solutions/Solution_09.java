package Streams.PracticeStreams.Solutions;

import java.util.Arrays;
import java.util.List;

public class Solution_09 {
    public static void main(String[] args) {
        List<String> greetings = List.of("Hello there", "Good Morning", "Have a Nice Day ahead");

        List<String> tokens = greetings.stream().flatMap(sentence -> Arrays.stream(sentence.split(" "))).toList();
        System.out.println(tokens);
    }
}
