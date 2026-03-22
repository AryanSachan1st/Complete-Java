package Streams.PracticeStreams.Solutions;

import java.util.List;
import java.util.Optional;

public class Solution_12 {
    public static void main(String[] args) {
        List<String> sentences = List.of("Loading problem", "Successfully submitted", "End of File Error", "NullPointer Exception", "Invalid Return Type error");

        Optional<String> firstError = sentences.stream().filter(sentence -> sentence.toLowerCase().contains("error")).findFirst();

        firstError.ifPresent((x) -> System.out.println(x));
    }
}