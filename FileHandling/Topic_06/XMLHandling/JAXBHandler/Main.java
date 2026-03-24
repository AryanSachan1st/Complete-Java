package FileHandling.Topic_06.XMLHandling.JAXBHandler;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        Student s1 = new Student(100, "Ritik", 22, "Udaipur");
        Student s2 = new Student(103, "Sanskriti", 21, "Rishikesh");
        Student s3 = new Student(101, "Karishma", 22, "Patna");
        Student s4 = new Student(105, "Kushagra", 22, "Kanpur");
        Student s5 = new Student(102, "R Sai Sudarshan", 21, "Chennai");

        students.add(s1);
        students.add(s2);
        students.add(s3);
        students.add(s4);
        students.add(s5);

        School school = new School();
        school.setStudents(students);

        Path path = Path.of("FileHandling/Topic_06/XMLHandling/JAXBHandler/schoolStudents.xml");

        // Root Object (school) -> XML file (path) [write]
        JAXBConvertor.marshal(School.class, school, path);

        // XML file (path) -> Root Object (school) [read]
        School school2 = JAXBConvertor.unmarshal(School.class, path);

        // Just printing
        List<Student> studentsList = school2.getStudentsList();
        studentsList.forEach((student) -> System.out.println(student.getDetails()));

        // Appending data to an XML file: read complete xml file -> get the root object -> add the new data to root obj -> write complete xml file again
    }
}
