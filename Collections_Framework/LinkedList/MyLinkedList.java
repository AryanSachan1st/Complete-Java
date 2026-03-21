package Collections_Framework.LinkedList;

import java.util.*;

/**
 * INTERNAL WORKING OF LinkedList<E>
 * ─────────────────────────────────
 * Backed by a DOUBLY-LINKED list of Node objects:
 *   Node { E item;  Node next;  Node prev; }
 *
 *   head ◄──► Node ◄──► Node ◄──► Node ◄──► tail
 *
 * • addFirst / addLast / removeFirst / removeLast → O(1)  (pointer swap only)
 * • get(i) / add(i,e) / remove(i)                → O(n)  (must traverse nodes)
 * • No array resizing — nodes are scattered in heap memory.
 * • Uses MORE memory than ArrayList (2 extra pointers per node).
 *
 * Implements List, Deque, Queue — so it can act as a list, stack, or queue.
 */
public class MyLinkedList {

    public static void main(String[] args) {

        // ── 1. CREATION ──────────────────────────────────────────────────────
        LinkedList<String> ll = new LinkedList<>();                      // empty
        LinkedList<String> fromCol = new LinkedList<>(List.of("P","Q","R")); // copy-constructor
        System.out.println("empty : " + ll + "  |  from collection : " + fromCol);


        // ── 2. ADDING ────────────────────────────────────────────────────────
        ll.add("Banana");              // addLast internally        O(1)
        ll.add("Cherry");
        ll.add(0, "Apple");            // insert at index           O(n)
        ll.addFirst("Avocado");        // update head pointer       O(1)
        ll.addLast("Date");            // update tail pointer       O(1)
        ll.addAll(List.of("Elderberry", "Fig")); // append collection       O(k)
        System.out.println("\nadd/addFirst/addLast/addAll : " + ll);


        // ── 3. ACCESSING ─────────────────────────────────────────────────────
        System.out.println("\nget(1)      : " + ll.get(1));       // O(n) — traverse
        System.out.println("getFirst()  : " + ll.getFirst());    // O(1) — head pointer
        System.out.println("getLast()   : " + ll.getLast());     // O(1) — tail pointer
        System.out.println("peek()      : " + ll.peek());        // head, null-safe     O(1)
        System.out.println("peekLast()  : " + ll.peekLast());    // tail, null-safe     O(1)
        System.out.println("size()      : " + ll.size());
        System.out.println("contains()  : " + ll.contains("Fig"));         // O(n) scan
        System.out.println("indexOf()   : " + ll.indexOf("Cherry"));       // O(n) scan


        // ── 4. UPDATING ──────────────────────────────────────────────────────
        // set() swaps the item inside an existing Node — no new Node created
        String old = ll.set(2, "Blueberry");  // O(n) traverse + O(1) swap
        System.out.println("\nset(2,'Blueberry') replaced : " + old + "  →  " + ll);


        // ── 5. REMOVING ──────────────────────────────────────────────────────
        ll.remove(0);                      // by index    O(n) traverse + O(1) unlink
        ll.remove("Elderberry");           // by value    O(n) scan    + O(1) unlink
        String h = ll.removeFirst();       // head        O(1)
        String t = ll.removeLast();        // tail        O(1)
        System.out.println("\nRemoved head=" + h + " tail=" + t + "  |  list : " + ll);

        ll.addAll(List.of("Alpha","Apricot","Ant"));
        ll.removeIf(s -> s.startsWith("A")); // removes ALL matching       O(n)
        System.out.println("removeIf(starts 'A') : " + ll);

        // poll() = removeFirst() but returns null when empty (queue-safe)
        System.out.println("poll()    : " + ll.poll() + "  →  " + ll);
        System.out.println("pollLast(): " + ll.pollLast() + "  →  " + ll);


        // ── 6. STACK  (LIFO — push/pop/peek use HEAD) ────────────────────────
        LinkedList<String> stack = new LinkedList<>();
        stack.push("Frame-1");  stack.push("Frame-2");  stack.push("Frame-3");
        System.out.println("\n[Stack] " + stack);
        System.out.println("pop()  : " + stack.pop() + "  peek() : " + stack.peek());


        // ── 7. QUEUE  (FIFO — offer at tail, poll from head) ─────────────────
        LinkedList<String> queue = new LinkedList<>();
        queue.offer("Task-A");  queue.offer("Task-B");  queue.offer("Task-C");
        System.out.println("\n[Queue] " + queue);
        System.out.println("poll() : " + queue.poll() + "  peek() : " + queue.peek());


        // ── 8. DEQUE  (offerFirst/offerLast + pollFirst/pollLast) ─────────────
        LinkedList<Integer> deque = new LinkedList<>();
        deque.offerFirst(2); deque.offerFirst(1); // [1,2]
        deque.offerLast(3);  deque.offerLast(4);  // [1,2,3,4]
        System.out.println("\n[Deque] " + deque
            + "  pollFirst=" + deque.pollFirst() + "  pollLast=" + deque.pollLast());


        // ── 9. ITERATING ─────────────────────────────────────────────────────
        LinkedList<String> itr = new LinkedList<>(List.of("Dog","Elephant","Fox"));

        System.out.print("\nfor-each   : "); 
        for (String s : itr) System.out.print(s + " ");

        System.out.print("\nforEach λ  : ");
        itr.forEach(s -> System.out.print(s + " "));

        System.out.print("\nDescending : ");
        Iterator<String> desc = itr.descendingIterator();   // uses prev pointers
        
        while (desc.hasNext()) System.out.print(desc.next() + " ");
        System.out.println();


        // ── 10. SORTING ──────────────────────────────────────────────────────
        LinkedList<String> s = new LinkedList<>(List.of("Mango","Apple","Kiwi","Banana"));
        Collections.sort(s);                                      // natural  O(n log n)
        System.out.println("\nSorted (natural) : " + s);
        s.sort((a, b) -> b.compareTo(a));                         // reverse
        System.out.println("Sorted (reverse) : " + s);


        // ── 11. MISC ─────────────────────────────────────────────────────────
        LinkedList<String> misc = new LinkedList<>(List.of("X","Y","Z","Y","X"));
        System.out.println("\nsubList(1,4) : " + misc.subList(1, 4));  // live view [Y,Z,Y]
        System.out.println("toArray()    : " + Arrays.toString(misc.toArray()));
        @SuppressWarnings("unchecked")
        LinkedList<String> cloned = (LinkedList<String>) misc.clone(); // shallow copy
        misc.clear();
        System.out.println("isEmpty()    : " + misc.isEmpty() + "  clone unaffected : " + cloned);
    }
}
