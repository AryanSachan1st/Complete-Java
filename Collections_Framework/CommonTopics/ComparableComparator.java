package Collections_Framework.CommonTopics;

import java.util.*;

// ─────────────────────────────────────────────────────────────────
// COMPARABLE vs COMPARATOR
// ─────────────────────────────────────────────────────────────────
// Comparable  → "Natural Ordering" — the class defines its OWN sort logic.
//               Implemented inside the class itself (intrusive).
//               Single sort order: you can only define ONE natural order.
//               Method: compareTo(T other)  [java.lang.Comparable<T>]
//
// Comparator  → "Custom/External Ordering" — sorting logic lives OUTSIDE
//               the class (non-intrusive, plug-and-play).
//               Multiple sort orders: create as many Comparators as needed.
//               Method: compare(T o1, T o2)  [java.util.Comparator<T>]
// ─────────────────────────────────────────────────────────────────

// ══════════════════════════════════════════════════════════════════
// 1. COMPARABLE — Student sorts itself by marks (natural order)
// ══════════════════════════════════════════════════════════════════
class Student implements Comparable<Student> {

    String name;
    int    marks;
    int    age;

    Student(String name, int marks, int age) {
        this.name  = name;
        this.marks = marks;
        this.age   = age;
    }

    // compareTo is called internally by Collections.sort() / Arrays.sort()
    // Contract: return  negative → this < other
    //           return  0        → this == other
    //           return  positive → this > other
    @Override
    public int compareTo(Student other) {
        // Sort by marks ascending (natural order for Student)
        return Integer.compare(this.marks, other.marks);
        // Internally: Collections.sort() uses TimSort which repeatedly
        // calls compareTo() to decide element order.
    }

    @Override
    public String toString() {
        return name + "(marks=" + marks + ", age=" + age + ")";
    }
}

// ══════════════════════════════════════════════════════════════════
// 2. COMPARATOR — External, reusable sorting strategies
// ══════════════════════════════════════════════════════════════════

// Sort by name alphabetically
class SortByName implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        // compareTo on String returns negative/0/positive lexicographically
        return s1.name.compareTo(s2.name);
    }
}

// Sort by age ascending
class SortByAge implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) { // -ve: s1 < s2 | 0: s1 == s2 | +ive: s1 > s2
        return Integer.compare(s1.age, s2.age); // return s1.age - s2.age
    }
}

public class ComparableComparator {
    public static void main(String[] args) {

        List<Student> students = new ArrayList<>(Arrays.asList(
            new Student("Zara",  85, 22),
            new Student("Aryan", 92, 20),
            new Student("Mia",   78, 21),
            new Student("Dev",   92, 19)
        ));

        // ── Using Comparable (natural order → by marks) ─────────────
        // Collections.sort() checks if Student implements Comparable,
        // then calls compareTo() repeatedly during TimSort.
        Collections.sort(students);
        System.out.println("Comparable — by marks (asc): " + students);

        // ── Using Comparator — by name ───────────────────────────────
        // Overloaded sort(list, comparator); calls compare() instead.
        Collections.sort(students, new SortByName());
        System.out.println("Comparator — by name  (asc): " + students);

        // ── Using Comparator — by age ────────────────────────────────
        Collections.sort(students, new SortByAge());
        System.out.println("Comparator — by age   (asc): " + students);

        // ── Lambda Comparator (inline, Java 8+) ─────────────────────
        // Avoids creating a separate class; Comparator is a @FunctionalInterface
        students.sort((s1, s2) -> Integer.compare(s2.marks, s1.marks)); // desc
        System.out.println("Lambda     — by marks (desc): " + students);

        // ── Chained Comparator — tiebreaker ─────────────────────────
        // Comparator.comparing() builds a Comparator from a key extractor.
        // thenComparing() chains a secondary sort for ties.
        students.sort(Comparator.comparingInt((Student s) -> s.marks)
                                .thenComparing(s -> s.name));
        System.out.println("Chained    — marks then name: " + students);

        // ─────────────────────────────────────────────────────────────
        // KEY DIFFERENCE SUMMARY
        //  Comparable  → 1 fixed order, defined inside class (tight coupling)
        //  Comparator  → N flexible orders, defined outside (loose coupling)
        //  Use Comparable when the class "owns" its natural sort order.
        //  Use Comparator when you need multiple orderings or can't modify
        //  the class (e.g., third-party / legacy types).
        // ─────────────────────────────────────────────────────────────
    }
}
