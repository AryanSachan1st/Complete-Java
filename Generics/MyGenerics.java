package Generics;

import java.util.ArrayList;
import java.util.Arrays;

class MyNumber extends Number implements MyRunnable {
    private int num;

    public MyNumber(int num) {
        this.num = num;

    }

    @Override
    public int intValue() {
        return this.num;
    }

    @Override
    public long longValue() {
        return this.num;
    }

    @Override
    public float floatValue() {
        return this.num;
    }

    @Override
    public double doubleValue() {
        return this.num;
    }

    @Override
    public void run() {
        System.out.println(MyRunnable.runMessage);
    }
}

interface MyRunnable {
    static final String runMessage = "Custom Runnable Implemented";

    void run();
}

class Box<T extends Number & MyRunnable> {
    private T object;

    public Box(T object) {
        this.object = object;
    }

    public T getObject() {
        return this.object;
    }
} // <T extends Class & Interfaces>

class Box2<U> {
    U node;
    public <T> Box2(U node, T value) { // Generic Constructor
        this.node = node;
    } 
}

enum Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE;

    public <T extends Number> double apply(T a, T b) throws Exception {
        switch (this) {
            case ADD:
                return a.doubleValue() + b.doubleValue();
            case SUBTRACT:
                return a.doubleValue() - b.doubleValue();
            case MULTIPLY:
                return a.doubleValue() * b.doubleValue();
            case DIVIDE:
                if (b.doubleValue() == 0) {
                    throw new Exception("Can not divide by 0");
                }
                return a.doubleValue() / b.doubleValue();
            default:
                throw new AssertionError("Unknown Operation: " + this);
        }
    }
}

public class MyGenerics {
    public static void main(String[] args) {
        Box<MyNumber> box = new Box<>(new MyNumber(10));
        System.out.println(box.getObject().intValue());

        Integer[] nums = {1, 2, 3, 4, 5};
        String[] arr = {"Aryan", "Vipul", "Hitesh", "Abhishek"};

        print(nums);
        print(arr);

        try {
            System.out.println(Operation.ADD.apply(20, 4));
        } catch (Exception e) {
            System.out.println("Operation failed: " + e.getMessage());
        }
    }

    public static <T> void print(T[] arr) { // Generic methods
        System.out.println(Arrays.toString(arr));
    }

    public static void printList(ArrayList<?> list) { // Wildcard '?', we can use it when we do not need to know the datatype of data given [mainly used for READ ONLY ops]
        System.out.println(list.toString());
    }

    public static double calcSum(ArrayList<? extends Number> list) { // extends (upper bound) -> Number class
        double sum = 0;
        for (Number num: list) {
            sum += num.doubleValue();
        }
        return sum;
    }
    // super (lower bound) -> >= Integer class
    // With '? super Integer', the compiler only guarantees elements are 'Object' at read time
    // (the list could be ArrayList<Integer>, ArrayList<Number>, or ArrayList<Object>)
    // So we must read as Object and cast safely to Number.
    // NOTE: Use '? extends Number' if you only need to READ/sum values (see calcSum above).
    //       Use '? super Integer' when you need to WRITE/add Integer values into the list (PECS rule).
    public static double calcSum02(ArrayList<? super Integer> list) {
        double sum = 0;
        for (Object obj : list) { // must read as Object — that's the only safe type guaranteed
            if (obj instanceof Number) {
                sum += ((Number) obj).doubleValue();
            }
        }
        return sum;
    }
}

/*
 * ──────────────────────────────────────────────────────────────────────────────
 *  HOW THE COMPILER HANDLES GENERICS — TYPE ERASURE
 * ──────────────────────────────────────────────────────────────────────────────
 *
 *  1. TYPE ERASURE (Compile Time → Bytecode)
 *     Java generics exist ONLY at compile time. The compiler erases all type
 *     parameters before producing bytecode:
 *       • Unbounded  <T>              → replaced with  Object
 *       • Bounded    <T extends Foo>  → replaced with  Foo   (leftmost bound)
 *     So  Box<MyNumber>  in bytecode becomes just  Box  with  Object  field.
 *
 *  2. CAST INSERTION
 *     Wherever you call  box.getObject()  the compiler inserts an automatic
 *     checkcast instruction in bytecode so the JVM enforces the type at runtime.
 *       box.getObject()  →  (MyNumber) box.getObject()   [bytecode-level cast]
 *
 *  3. BRIDGE METHODS
 *     When a generic class is subclassed with a concrete type, the compiler
 *     generates a synthetic "bridge" method that overrides the raw Object-based
 *     method and delegates to the type-specific one — keeping polymorphism intact.
 *
 *  4. WHY NO  instanceof  for Generic Types?
 *     Because  <T>  is erased at runtime, you CANNOT write  obj instanceof T .
 *     The JVM has no idea what T was — only Object remains.
 *
 *  5. HEAP POLLUTION
 *     Mixing raw types with generics can cause ClassCastException at unexpected
 *     runtime points (not where the bad assignment was), because the cast check
 *     is deferred to where the value is actually used.
 *
 *  6. WHY GENERIC EXCEPTIONS DON'T WORK
 *     You CANNOT write:  class MyException<T> extends Exception  — illegal.
 *     The JVM matches exceptions by their exact runtime class (via instanceof).
 *     Because type erasure removes <T>, all MyException<T> variants collapse to
 *     the same raw class at runtime, making catch(MyException<String> e) vs
 *     catch(MyException<Integer> e) indistinguishable — so the compiler bans it.
 * ──────────────────────────────────────────────────────────────────────────────
 */
