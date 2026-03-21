package Collections_Framework.Stack;

import java.util.*;

// ── JAVA STACK ────────────────────────────────────────────────────────────────
// WHY: Models LIFO (Last-In First-Out) — the last element added is removed first.
// USE CASES: Undo/Redo, browser back-navigation, DFS traversal,
//            expression evaluation, balanced parenthesis checking.
//
// ============================================================
//  PARENT CLASS / CLASS HIERARCHY
// ============================================================
//
//  java.lang.Object
//       └── java.util.AbstractCollection<E>
//               └── java.util.AbstractList<E>
//                       └── java.util.Vector<E>       ← parent
//                               └── java.util.Stack<E> ← our class
//
//  Stack EXTENDS Vector.
//  Vector itself implements List, Collection, Iterable,
//  RandomAccess, Cloneable, and Serializable.
//
//  Because Stack inherits from Vector:
//   • It is internally backed by a resizable Object[] array.
//   • Every method in Stack is SYNCHRONIZED (thread-safe),
//     just like Vector — but this also makes it SLOWER than
//     Deque-based alternatives in single-threaded code.
//
// INTERNAL IMPLEMENTATION (5 thin methods on top of Vector's array):
//   push(E)    → addElement(item)  → appends to end of array       O(1) amortised
//   pop()      → removes & returns last element (top of stack)      O(1)
//   peek()     → returns last element without removing              O(1)
//   empty()    → returns (size() == 0)                              O(1)
//   search(o)  → 1-based position from top, -1 if not found        O(n)
//   "Top of stack" = LAST index of the backing array (no extra pointer).
//   Growth policy: doubles array size when capacity is exceeded (inherited from Vector).
// ─────────────────────────────────────────────────────────────────────────────

public class MyStack {

    public static void main(String[] args) {

        // 1. CREATE & PUSH — add elements to the top
        Stack<Integer> stack = new Stack<>();
        stack.push(10); stack.push(20); stack.push(30); stack.push(40);
        System.out.println("After pushes : " + stack);          // [10, 20, 30, 40]

        // 2. PEEK — view top without removing
        System.out.println("Peek         : " + stack.peek());   // 40

        // 3. POP — remove and return top element
        System.out.println("Pop          : " + stack.pop());    // 40
        System.out.println("After pop    : " + stack);          // [10, 20, 30]

        // 4. EMPTY — check if stack has no elements
        System.out.println("Is empty?    : " + stack.empty());  // false

        // 5. SEARCH — 1-based distance from top (-1 if absent)
        System.out.println("Search 10    : " + stack.search(10)); // 3 (bottom)
        System.out.println("Search 30    : " + stack.search(30)); // 1 (top)
        System.out.println("Search 99    : " + stack.search(99)); // -1

        // 6. ITERATE — bottom → top
        System.out.print("Iterate      : ");
        for (int v : stack) System.out.print(v + " ");
        System.out.println();

        // ── USE CASE 1: Balanced Parenthesis Checker ──────────────────────────
        System.out.println("\n--- Balanced Parenthesis ---");
        System.out.println("\"({[]})\" → " + isBalanced("({[]})"));  // true
        System.out.println("\"({[})\"  → " + isBalanced("({[})"));   // false

        // ── USE CASE 2: Reverse a String ──────────────────────────────────────
        System.out.println("\n--- Reverse String ---");
        System.out.println(reverseString("Hello, Stack!"));           // !kcatS ,olleH

        // ── USE CASE 3: Undo Simulation ───────────────────────────────────────
        System.out.println("\n--- Undo Simulation ---");
        undoDemo();

        // ── CUSTOM STACK USING LINKED LIST ────────────────────────────────────
        System.out.println("\n--- Custom LinkedStack Demo ---");
        LinkedStack<Integer> ls = new LinkedStack<>();
        ls.push(1); ls.push(2); ls.push(3);
        System.out.println("Size   : " + ls.size());   // 3
        System.out.println("Peek   : " + ls.peek());   // 3
        System.out.println("Pop    : " + ls.pop());    // 3
        System.out.println("Pop    : " + ls.pop());    // 2
        System.out.println("Empty? : " + ls.isEmpty()); // false
        System.out.println("Pop    : " + ls.pop());    // 1
        System.out.println("Empty? : " + ls.isEmpty()); // true

        // ── MODERN ALTERNATIVE: ArrayDeque (faster, not synchronized) ─────────
        // Prefer ArrayDeque over Stack in single-threaded code (Java docs recommend it).
        System.out.println("\n--- ArrayDeque as Stack ---");
        Deque<String> ds = new ArrayDeque<>();
        ds.push("first"); ds.push("second"); ds.push("third");
        System.out.println("Peek : " + ds.peek());  // third
        System.out.println("Pop  : " + ds.pop());   // third
        System.out.println("Left : " + ds);         // [second, first]
    }

    // USE CASE 1: Push opening brackets; on closing bracket, pop & match.
    static boolean isBalanced(String expr) {
        Stack<Character> s = new Stack<>();
        for (char ch : expr.toCharArray()) {
            if ("({[".indexOf(ch) >= 0) { s.push(ch); }
            else if (")]}".indexOf(ch) >= 0) {
                if (s.empty()) return false;
                char t = s.pop();
                if (ch == ')' && t != '(') return false;
                if (ch == '}' && t != '{') return false;
                if (ch == ']' && t != '[') return false;
            }
        }
        return s.empty();
    }

    // USE CASE 2: Push all chars → pop gives reverse order.
    static String reverseString(String str) {
        Stack<Character> s = new Stack<>();
        for (char ch : str.toCharArray()) s.push(ch);
        StringBuilder sb = new StringBuilder();
        while (!s.empty()) sb.append(s.pop());
        return sb.toString();
    }

    // USE CASE 3: Each action is pushed; undo pops the last one.
    static void undoDemo() {
        Stack<String> history = new Stack<>();
        history.push("Typed 'Hello'");
        history.push("Typed ' World'");
        history.push("Bold applied");
        System.out.println("Current : " + history);
        System.out.println("Undo    : " + history.pop());
        System.out.println("After   : " + history);
    }
}

// ── CUSTOM STACK IMPLEMENTATION USING LINKED LIST ────────────────────────────
// WHY LINKED LIST instead of array?
//   • No fixed capacity — grows dynamically, no resizing/copying needed.
//   • push() and pop() are always O(1) — no shifting, no doubling.
//   • Memory is allocated per node, so no wasted slots like an array.
//
// STRUCTURE: Singly linked list where HEAD = TOP of the stack.
//   push → create new Node, point it to current head, update head.
//   pop  → save head data, move head to head.next, return saved data.
//
//  [top] → [node2] → [node1] → null
// ─────────────────────────────────────────────────────────────────────────────
class LinkedStack<T> {

    // Each node holds data + a reference to the node below it.
    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    private Node<T> head; // top of the stack
    private int size;

    // push — O(1): new node becomes the new head (top)
    public void push(T item) {
        Node<T> node = new Node<>(item);
        node.next = head;  // new node points to old top
        head = node;       // head now points to new top
        size++;
    }

    // pop — O(1): remove head, return its data
    public T pop() {
        if (isEmpty()) throw new EmptyStackException();
        T data = head.data;
        head = head.next;  // move head down to next node
        size--;
        return data;
    }

    // peek — O(1): read top without removing
    public T peek() {
        if (isEmpty()) throw new EmptyStackException();
        return head.data;
    }

    public boolean isEmpty() { return head == null; }
    public int size()        { return size; }
}