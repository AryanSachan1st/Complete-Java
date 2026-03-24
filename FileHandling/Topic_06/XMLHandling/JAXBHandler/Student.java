package FileHandling.Topic_06.XMLHandling.JAXBHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Student {

    @XmlAttribute
    private int id;

    @XmlAttribute
    private String name;

    @XmlAttribute
    private int age;

    @XmlAttribute
    private String city;

    // no-arg constructor (required by JAXB)
    public Student() {}

    // constructor
    public Student(int id, String name, int age, String city) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
    }

    // getters
    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }
    public int getAge() {
        return this.age;
    }
    public String getCity() {
        return this.city;
    }

    // method
    public Map<String, String> getDetails() {
        
        Map<String, String> details = new LinkedHashMap<>(); // maintains insertion order
        details.put("id", String.valueOf(this.id));
        details.put("name", this.name);
        details.put("age", String.valueOf(this.age));
        details.put("city", this.city);

        return details;
    }
}
