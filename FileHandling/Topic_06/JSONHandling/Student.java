package FileHandling.Topic_06.JSONHandling;

// Our POJO class for this illustration - Student
public class Student {
    private String name, city;
    private int age;
    private int[] scores;

    // Required by Jackson for deserialization
    public Student() {}

    public Student(String name, String city, int age, int[] scores) {
        this.name = name;
        this.city = city;
        this.age = age;
        this.scores = scores;
    }

    public String getName() {
        return this.name;
    }
    public String getCity() {
        return this.city;
    }
    public int getAge() {
        return this.age;
    }
    public int[] getScores() {
        return this.scores;
    }

    // Required by Jackson for deserialization (to set fields after no-args constructor)
    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setAge(int age) { this.age = age; }
    public void setScores(int[] scores) { this.scores = scores; }

    @Override
    public String toString() {
        return "Name: " + this.name + ", Age: " + this.age + ", City: " + this.city;
    }
}
