package Collections_Framework.Iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyItertor {
    public static void main(String[] args) {

        // ─────────────────────────────────────────────────────────────────
        // INTERNAL HIERARCHY OF ARRAYLIST
        // ─────────────────────────────────────────────────────────────────
        // Iterable (top-level interface)
        //   └── Collection
        //         └── List (interface)
        //               └── AbstractList (abstract class)
        //                     └── ArrayList (concrete class)
        //
        // ArrayList implements Iterable, which forces it to provide:
        //   → iterator()       returns a basic Iterator
        //   → listIterator()   returns a bidirectional ListIterator
        //
        // Internally ArrayList stores elements in: Object[] elementData
        // When iterator() is called, ArrayList returns an inner class
        // instance called "Itr" which holds an int cursor to track position.

        // ─────────────────────────────────────────────────────────────────
        // INTERNAL IMPLEMENTATION OF ITERATOR THROUGH ARRAYLIST
        // ─────────────────────────────────────────────────────────────────
        // ArrayList's inner class  Itr  roughly looks like:
        //
        //   class Itr implements Iterator<E> {
        //       int cursor;        // index of next element to return
        //       int lastRet = -1;  // index of last returned element
        //       int expectedModCount = modCount;  // snapshot of structural mod count
        //
        //       public boolean hasNext()  { return cursor != size; }
        //       public E next() {
        //           checkForConcurrentModification();   // ← key safety check
        //           int i = cursor++;
        //           return (E) elementData[i];
        //       }
        //       public void remove() {
        //           ArrayList.this.remove(lastRet);
        //           cursor = lastRet;
        //           lastRet = -1;
        //           expectedModCount = modCount;  // re-sync after safe removal
        //       }
        //   }

        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Mango");
        fruits.add("Cherry");

        Iterator<String> it = fruits.iterator();
        System.out.println("── Basic Iterator ──");
        while (it.hasNext()) {
            System.out.println(it.next());
        }

        // ─────────────────────────────────────────────────────────────────
        // RESOLVING CONCURRENTMODIFICATIONEXCEPTION USING ITERATOR
        // ─────────────────────────────────────────────────────────────────
        // If you add/remove from the list directly while iterating,
        // modCount != expectedModCount → ConcurrentModificationException.
        //
        // Safe fix: use iterator.remove() — it re-syncs expectedModCount.

        System.out.println("\n── Safe removal via Iterator ──");
        Iterator<String> safeIt = fruits.iterator();
        while (safeIt.hasNext()) {
            String fruit = safeIt.next();
            if (fruit.equals("Banana")) {
                safeIt.remove();   // ✅ safe — no CME
                // fruits.remove(fruit); ← ❌ would throw CME
            }
        }
        System.out.println("After removal: " + fruits); // [Apple, Mango, Cherry]

        // ─────────────────────────────────────────────────────────────────
        // LISTITERATOR — USE CASES & IMPORTANT METHODS
        // ─────────────────────────────────────────────────────────────────
        // ListIterator extends Iterator and supports:
        //   → BIDIRECTIONAL traversal (forward + backward)
        //   → add(), set() in addition to remove()
        //   → nextIndex() / previousIndex() to know current position
        //
        // Internally it is ArrayList's inner class "ListItr" extending "Itr".

        List<String> colors = new ArrayList<>();
        colors.add("Red");
        colors.add("Green");
        colors.add("Blue");

        ListIterator<String> listIt = colors.listIterator();

        System.out.println("\n── Forward traversal (ListIterator) ──");
        while (listIt.hasNext()) {
            int idx = listIt.nextIndex();
            String color = listIt.next();
            System.out.println("Index " + idx + " → " + color);

            if (color.equals("Green")) {
                listIt.set("Yellow");     // replace current element in-place
            }
            if (color.equals("Blue")) {
                listIt.add("Purple");     // insert after current element
            }
        }
        System.out.println("After set/add: " + colors); // [Red, Yellow, Blue, Purple]

        System.out.println("\n── Backward traversal (ListIterator) ──");
        while (listIt.hasPrevious()) {
            System.out.println(listIt.previousIndex() + " ← " + listIt.previous());
        }

        // ─────────────────────────────────────────────────────────────────
        // LISTITERATOR METHOD SUMMARY
        // ─────────────────────────────────────────────────────────────────
        // hasNext()        → true if forward traversal has more elements
        // next()           → returns next element, advances cursor
        // hasPrevious()    → true if backward traversal has more elements
        // previous()       → returns previous element, moves cursor back
        // nextIndex()      → index of element next() would return
        // previousIndex()  → index of element previous() would return
        // remove()         → removes the last element returned by next/previous
        // set(E e)         → replaces last element returned by next/previous
        // add(E e)         → inserts element immediately before next() position
    }
}