package FileHandling.Topic_06.YAMLHandling;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student();
        s1.setName("Prateek");
        s1.setAge(22);
        s1.setCity("Gurgaon");

        Student s2 = new Student();
        s2.setName("Sneha");
        s2.setAge(21);
        s2.setCity("Manglore");

        Student s3 = new Student();
        s3.setName("Aaradhya");
        s3.setAge(22);
        s3.setCity("Hyderabad");

        Student s4 = new Student();
        s4.setName("Rudra");
        s4.setAge(21);
        s4.setCity("Nasik");

        List<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);
        students.add(s3);
        students.add(s4);

        School school = new School();
        school.setStudentsList(students);

        Path path = Path.of("FileHandling/Topic_06/YAMLHandling/schoolStudents.yaml");

        // writing
        YAMLConvertor.writeToYAML(school, path);
        
        // reading
        School schoolObj2 = YAMLConvertor.readYAML(path, School.class);
        if (schoolObj2 == null) System.out.println("Reading YAML failed!");
        schoolObj2.getStudentsList().forEach((student) -> System.out.println(
            student.getDetails()
        ));
    }
}
