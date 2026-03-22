package Streams.PracticeStreams.Solutions;

import java.util.List;

public class Solution_02 {
    public static void main(String[] args) {
        List<String> names = List.of("Aryan", "Nirbhay", "Nikunj", "Neel", "Nitin", "Mukesh");
        // List<String> upperNames =
        // names.stream().map(String::toUpperCase).peek(System.out::println).toList();
        /*
         * Rule of thumb: peek() is mainly used for debugging in the middle of a
         * pipeline that already has a terminal operation. It's not meant to be the
         * final operation.
         */
        // System.out.println(upperNames.toString());

        names.stream().map(String::toUpperCase).forEach(System.out::println);
    }
}
