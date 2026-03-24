package FileHandling.Topic_06.YAMLHandling;

import java.util.LinkedHashMap;
import java.util.Map;

public class Student {
    private String name, city;
    private int age;

    // No-arg constructor is MANDATORY for SnakeYAML
    public Student() {};

    // setters (required by SnakeYAML to serialize/deserialize fields)
    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setCity(String city) {
        this.city = city;
    }
    
    // getters
    public String getName() {
        return this.name;
    }
    public int getAge() {
        return this.age;
    }
    public String getCity() {
        return this.city;
    }


    // methods
    public Map<String, String> getDetails() {
        Map<String, String> allDetails = new LinkedHashMap<>(); // insetion order

        allDetails.put("name", name);
        allDetails.put("age", String.valueOf(age));
        allDetails.put("city", city);

        return allDetails;
    }
}
