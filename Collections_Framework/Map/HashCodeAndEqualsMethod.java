package Collections_Framework.Map;

import java.util.*;

/*
 * TOPIC: Why we override hashCode() and equals() for custom keys in HashMap
 *
 * RULE: HashMap uses TWO steps to find/store a key:
 *   Step 1 → hashCode()  : decides which BUCKET to go to
 *   Step 2 → equals()    : confirms the key is the SAME inside that bucket
 *
 * Without overriding: two objects with same data are treated as DIFFERENT keys
 *                     because Java compares memory addresses by default.
 * With overriding   : two objects with same data are treated as the SAME key.
 *
 * RESULT here: p1 and p3 have same name+id → same hashCode → equals() returns
 * true → HashMap sees them as ONE key. So map.size() = 2, not 3.
 */
public class HashCodeAndEqualsMethod {
    public static void main(String[] args) {
        HashMap<Person, String> map = new HashMap<>();
        Person p1 = new Person("Alice", 1);
        Person p2 = new Person("Bob", 2);
        Person p3 = new Person("Alice", 1); // same data as p1, different object

        map.put(p1, "Engineer");
        map.put(p2, "Designer");
        map.put(p3, "Manager"); // p3 == p1 logically → overwrites "Engineer" with "Manager"

        map.put(p1, "Engineer"); // hashcode1 --> index1
        map.put(p2, "Designer"); // hashcode2 --> index2
        map.put(p3, "Manager");  // hashcode1 --> index1 --> equals() --> replace

        System.out.println("HashMap Size: " + map.size()); // 2 (p1 and p3 are same key)
        System.out.println("Value for p1: " + map.get(p1)); // Manager
        System.out.println("Value for p3: " + map.get(p3)); // Manager (same bucket as p1)

        // String already overrides hashCode() and equals() → same string = same key
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("Shubham", 90); // hashcode1 --> index1
        map1.put("Neha", 92);    // hashcode2 --> index2
        map1.put("Shubham", 99); // hashcode1 --> index1 --> equals() --> replace
    }
}

// Custom class used as a HashMap KEY — must override hashCode() and equals()
class Person {
    private String name;
    private int id;

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public int getId()      { return id; }

    // Step 1: generates a bucket index based on field VALUES (not memory address)
    // Objects.hash() combines multiple fields into one hash code
    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    // Step 2: called when two keys land in the same bucket to check if they're equal
    // Checks: same object? → null? → same class? → same field values?
    @Override
    public boolean equals(Object obj) {
        if (this == obj)                  return true;  // exact same reference
        if (obj == null)                  return false; // nothing to compare
        if (getClass() != obj.getClass()) return false; // different types
        Person other = (Person) obj;
        return id == other.getId() && Objects.equals(name, other.getName());
    }

    // Makes System.out.println(person) show useful info instead of memory address
    @Override
    public String toString() {
        return "id: " + id + ", name: " + name;
    }
}