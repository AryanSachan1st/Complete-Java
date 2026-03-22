package Streams.PracticeStreams.Solutions;

import java.util.List;
import java.util.stream.Collectors;

public class Solution_15 {
    public static void main(String[] args) {
        List<String> fileNames = List.of("oops.c++", "generics.java", "file_handling.py", "javaErrors.js");

        List<String> javaFiles = fileNames.stream().filter(filename -> filename.contains(".java")).sorted().collect(Collectors.toList());

        System.out.println(javaFiles);
    }
}
