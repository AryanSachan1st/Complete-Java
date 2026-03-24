package FileHandling.Topic_06.JSONHandling;

import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Student> students = List.of(
            new Student("KamalJeet", "Jhansi", 21, new int[] { 85, 92, 90 }),
            new Student("IndraJeet", "Haridwar", 22, new int[] { 75, 82, 92 }),
            new Student("Satya", "Kolkata", 22, new int[] { 85, 72, 80 }),
            new Student("Srikant", "Lucknow", 21, new int[] { 75, 92, 85 })
        );

        Path path = Path.of("FileHandling/Topic_06/JSONHandling/studentData.json");

        // Gson
        System.out.println("Gson Serialize");
        String gsonJson = GsonConvertor.serialize(students, path);
        System.out.println(gsonJson);

        System.out.println("\nGson Deserialize");
        List<Student> gsonStudents = GsonConvertor.deserialize(path, Student.class);
        if (gsonStudents != null) gsonStudents.forEach(System.out::println);

        // Jackson
        System.out.println("\nJackson Serialize");
        String jacksonJson = JacksonConvertor.serialize(students, path);
        System.out.println(jacksonJson);

        System.out.println("\nJackson Deserialize");
        List<Student> jacksonStudents = JacksonConvertor.deserialize(path, Student.class);
        if (jacksonStudents != null) jacksonStudents.forEach(System.out::println);
    }
}
