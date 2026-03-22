package Streams;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/*
Prerequisites to learn Streams: Java basics and Collection Framework
Java 8 introduces --> minimal code, functional programming, lambda expressions, Streams, Date and Time API
Note: lambda functions can only be used to implement functional interfaces
*/
@FunctionalInterface // an interface which has only one function
interface MathOperation {
    int operate(int a, int b);
}

class MobilePhone {
    private String name;

    public MobilePhone(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

public class StreamBasics {
    public static void main(String[] args) {
        // lambda expression -> remove access modifiers, return type, name of the function and put '->' between '() {}' => '() -> {}' [lambda expression]

        // lambda_1
        @SuppressWarnings("unused")
        Thread t1 = new Thread(() -> {
            System.out.println("Thread 1 definition");
        });

        // lambda_2
        @SuppressWarnings("unused")
        // implemented the logic for the iterface, [alternative of creating a class]
        MathOperation sumOperation_01 = (int a, int b) -> {return a + b;};
        MathOperation sumOperation_02 = (int a, int b) -> a + b; // cleaner

        int sumResult = sumOperation_02.operate(2, 4);
        System.out.println(sumResult);

        // Predicate --> Functional Interface: contains a function which will take any argument and will return a boolean value
        Predicate<Integer> EvenChecker = x -> x % 2 == 0; // interface implemented (storing the complete logic inside just a variable 'EvenChecker')

        boolean isEven = EvenChecker.test(3); // call that function through implementation 'Implementation.func(args)' & store the result in a variable
        System.out.println(isEven);

        Predicate<String> StartsWithLogic = str -> str.toLowerCase().startsWith("a");
        Predicate<String> EndsWithLogic = str -> str.toLowerCase().endsWith("n");

        boolean startsAndEnds = StartsWithLogic.and(EndsWithLogic).test("Aryan"); // logic1.and(logic2).test(arg)
        System.out.println(startsAndEnds);

        // Functions: similar to predicate but works for us, Function<T, R>: T (param), R (return type)
        Function<Character, Integer> findAscii = x -> (int) x;
        @SuppressWarnings("unused")
        UnaryOperator<Integer> findAscii_02 = x -> (int) x; // short-hand Function
        System.out.println("Ascii of A: " + findAscii.apply('A'));
        System.out.println("Ascii of Z: " + findAscii.apply('Z'));
        System.out.println("Ascii of a: " + findAscii.apply('a'));
        System.out.println("Ascii of z: " + findAscii.apply('z'));

        Function<Integer, Integer> doubleIt = x -> 2 * x;
        int doubledAsciiCodeOf_a = findAscii.andThen(doubleIt).apply('A'); // logic1.andThen(logic2).apply(arg)
        System.out.println("Doubled Ascii of A: " + doubledAsciiCodeOf_a);

        // returns the same data which is passed as parameter
        Function<Integer, Integer> identityOF = Function.identity();
        System.out.println("Identity of 24: " + identityOF.apply(24)); // returns 24

        // Consumer<T>: takes T arg, but returns void (nothing)
        Consumer<List<String>> display = list -> {
            for (String name: list) {
                System.out.print(name + " ");
            }
        };
        display.accept(List.of("aryan", "aditi", "vipul"));
        System.out.println();

        // Supplier<T>: takes nothing, but returns T
        Supplier<Integer> getRandomNumber = () -> {
            int randomInt = (int) Math.floor(Math.random() * 100) + 1;
            return randomInt;
        };

        System.out.println(getRandomNumber.get());
        System.out.println(getRandomNumber.get());
        System.out.println(getRandomNumber.get());

        // BiPredicate<T, U> (takes T, U returns boolean), BiConsumer<T, U> (takes T, U returns nothing), BiFunction<T, U, R> (takes T, U returns R)
        // BinaryOperator<T> (takes T, T returns T, short-hand)
        BiPredicate<Integer, Set<Integer>> isExistsInSet = (x, hashSet) -> {
            return hashSet.contains(x);
        };
        Set<Integer> set = new HashSet<>();
        set.addAll(List.of(1, 30, 4, 5));

        System.out.println("3 exists in set: " + isExistsInSet.test(3, set));

        // Method Reference (shortcut) --> use method without invoking & in place of lambda expression
        List<String> students = List.of("Kashish", "Rudra", "Archer", "Tharini"); // iterable item
        students.forEach(x -> System.out.println(x)); // iterable.forEach(Consumer)
        System.out.println();
        students.forEach(System.out::println); // Method referencing (class::method)
        System.out.println();

        // Constructor Reference (shortcut)
        // ex: take a list of names and return a list of MobilePhones [list --> streams --> map each name --> (for each name) create a new MobilePhone -> collect it --> convert streams back to list]
        List<String> names = List.of("Mob1", "Mob2", "Mob3");
        List<MobilePhone> phones = names.stream().map(MobilePhone::new).collect(Collectors.toList()); // (class::new)

        phones.forEach(phone -> System.out.println(phone.getName()));
    }
}