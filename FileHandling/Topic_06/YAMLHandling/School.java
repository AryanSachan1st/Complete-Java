package FileHandling.Topic_06.YAMLHandling;

import java.util.ArrayList;
import java.util.List;

public class School {
    private List<Student> students;

    public School() {};

    // setter
    public void setStudentsList(List<Student> students) {
        this.students = students;
    }

    // getter
    public List<Student> getStudentsList() {
        return new ArrayList<>(this.students);
    }
}
