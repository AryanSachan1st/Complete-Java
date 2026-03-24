package FileHandling.Topic_06.XMLHandling.JAXBHandler;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "school")
@XmlAccessorType(XmlAccessType.FIELD)
public class School {

    @XmlElement(name = "student")
    private List<Student> students;

    // setter
    public void setStudents(List<Student> students) {
        this.students = students;
    }
    // getter
    public List<Student> getStudentsList() {
        return new ArrayList<>(this.students); // returning defensive copy
    }

    // method
    public void addStudentToList(Student student) {
        this.students.add(student);
    }
}
