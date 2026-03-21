package Collections_Framework.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
 * ─────────────────────────────────────────────────────────────────
 *  COMPARATOR IN JAVA
 * ─────────────────────────────────────────────────────────────────
 *  Comparator<T> is a functional interface (java.util.Comparator)
 *  used to define a custom ordering for objects.
 *
 *  compare(T o1, T o2) return value rules:
 *    negative → o1 comes BEFORE o2  (ascending)
 *    zero     → o1 and o2 are equal
 *    positive → o1 comes AFTER  o2  (descending)
 *
 *  Key difference from Comparable:
 *    • Comparable  → natural order defined INSIDE the class  (compareTo)
 *    • Comparator  → external / custom order, passed at sort time
 *
 *  Common ways to create a Comparator:
 *    1. Anonymous class
 *    2. Lambda expression  (Java 8+)
 *    3. Comparator.comparing() factory method (Java 8+)
 *    4. Method reference
 * ─────────────────────────────────────────────────────────────────
 */

// A simple Student class – does NOT implement Comparable intentionally,
// so all ordering is handled externally by Comparators.
class Student {
    String name;
    int age;
    double gpa;

    Student(String name, int age, double gpa) {
        this.name = name;
        this.age  = age;
        this.gpa  = gpa;
    }

    @Override
    public String toString() {
        return String.format("%-10s age=%-3d gpa=%.1f", name, age, gpa);
    }
}

public class MyComparators {

    // ── 1. Anonymous-class style Comparator (classic, verbose) ──
    static Comparator<Student> byNameAsc = new Comparator<Student>() {
        @Override
        public int compare(Student s1, Student s2) {
            return s1.name.compareTo(s2.name);   // A→Z
        }
    };

    // ── 2. Lambda style (Java 8+) – same logic, much shorter ──
    static Comparator<Student> byNameDesc = (s1, s2) -> s2.name.compareTo(s1.name); // Z→A

    // ── 3. Comparator.comparing() factory – cleanest form ──
    static Comparator<Student> byAgeAsc  = Comparator.comparingInt(s -> s.age);
    static Comparator<Student> byAgeDesc = Comparator.comparingInt((Student s) -> s.age).reversed();

    // ── 4. Chained / multi-key comparator ──
    //    Primary: GPA descending  |  Secondary: name ascending (tiebreaker)
    static Comparator<Student> byGpaDescThenName =
            Comparator.comparingDouble((Student s) -> s.gpa)
                      .reversed()
                      .thenComparing(s -> s.name);

    /*
     * ── 5. EXPLICIT +1 / -1 / 0 Custom Comparator ──────────────────────
     *
     *  Why write it this way?
     *  • Makes the three cases unmistakably clear for learners.
     *  • Full control – no subtraction tricks that can overflow for int.
     *  • When compare() returns 0 we explicitly handle a tiebreaker
     *    instead of leaving the order undefined.
     *
     *  Logic here:
     *    Primary   → GPA  descending  (+1 / -1)
     *    Tiebreaker→ Name ascending   (+1 / -1 / 0)
     * ────────────────────────────────────────────────────────────────────
     */
    static Comparator<Student> explicitComparator = new Comparator<Student>() {
        @Override
        public int compare(Student s1, Student s2) {

            // ── Primary key: GPA (higher GPA comes first → descending) ──
            if (s1.gpa > s2.gpa) return -1;   // s1 ranks higher → move s1 before s2
            if (s1.gpa < s2.gpa) return  1;   // s2 ranks higher → move s2 before s1

            // ── Tiebreaker (GPA == 0 case): sort by name A→Z ──
            // compareTo itself returns negative/zero/positive,
            // but we map it explicitly for clarity:
            int nameCmp = s1.name.compareTo(s2.name);
            if (nameCmp < 0) return -1;   // s1 name comes first alphabetically
            if (nameCmp > 0) return  1;   // s2 name comes first alphabetically
            return 0;                      // truly equal on both fields
        }
    };

    // ── Generic helper: sort any List with a given Comparator ──
    static <T> void sortAndPrint(List<T> list, Comparator<T> comp, String label) {
        List<T> copy = new ArrayList<>(list);   // don't mutate the original
        copy.sort(comp);                         // List.sort() uses the Comparator
        System.out.println("\n[ " + label + " ]");
        copy.forEach(System.out::println);
    }

    public static void main(String[] args) {

        List<Student> students = new ArrayList<>();
        students.add(new Student("Zara",  22, 3.5));
        students.add(new Student("Aryan", 20, 3.9));
        students.add(new Student("Mia",   21, 3.5));
        students.add(new Student("Bob",   23, 3.7));
        students.add(new Student("Alice", 20, 3.9));

        // ── Demo 1: Name A→Z ──
        sortAndPrint(students, byNameAsc, "Name Ascending (A→Z)");

        // ── Demo 2: Name Z→A ──
        sortAndPrint(students, byNameDesc, "Name Descending (Z→A)");

        // ── Demo 3: Age youngest first ──
        sortAndPrint(students, byAgeAsc, "Age Ascending (youngest first)");

        // ── Demo 4: Age oldest first ──
        sortAndPrint(students, byAgeDesc, "Age Descending (oldest first)");

        // ── Demo 5: GPA highest first, ties broken by name A→Z ──
        sortAndPrint(students, byGpaDescThenName, "GPA Desc → then Name Asc");

        // ── Demo 6: Same result using EXPLICIT +1/-1/0 comparator ──
        //    Aryan & Alice both have GPA 3.9 → tiebreaker kicks in → Alice before Aryan
        sortAndPrint(students, explicitComparator, "Explicit +1/-1/0 → GPA Desc, tie → Name Asc");

        // ── Demo 7: Inline one-liner with Collections.sort ──
        List<Integer> numbers = new ArrayList<>(List.of(5, 1, 8, 3, 9, 2));
        Collections.sort(numbers, Comparator.naturalOrder());   // ascending
        System.out.println("\n[ Numbers Ascending  ] " + numbers);

        Collections.sort(numbers, Comparator.reverseOrder());   // descending
        System.out.println("[ Numbers Descending ] " + numbers);
    }
}
