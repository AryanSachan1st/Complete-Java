package Collections_Framework.Queue;

import java.util.*;
import java.util.concurrent.*;

/*
 * HIERARCHY (Iterable → Queue leaf classes)
 * ------------------------------------------
 * Iterable  (java.lang)
 * └── Collection  (java.util)
 *     ├── Queue  (java.util)
 *     │   ├── Deque  (interface)
 *     │   │   ├── ArrayDeque          (class) ← preferred stack/queue, circular array
 *     │   │   └── LinkedList          (class) ← also implements List
 *     │   ├── PriorityQueue           (class) ← min/max-heap, not FIFO
 *     │   └── BlockingQueue  (interface, java.util.concurrent)
 *     │       ├── BlockingDeque  (interface)
 *     │       │   └── LinkedBlockingDeque     (class)
 *     │       ├── ArrayBlockingQueue          (class) ← bounded, single lock
 *     │       ├── LinkedBlockingQueue         (class) ← optionally bounded, two locks
 *     │       ├── PriorityBlockingQueue       (class) ← unbounded, min-heap
 *     │       ├── SynchronousQueue            (class) ← zero capacity, direct handoff
 *     │       ├── DelayQueue                  (class) ← elements released after delay
 *     │       └── TransferQueue  (interface)
 *     │           └── LinkedTransferQueue     (class) ← like SyncQ but non-blocking option
 *     └── AbstractQueue           (abstract class, bridging Queue ↔ AbstractCollection)
 *         └── ConcurrentLinkedQueue (class) ← lock-free, CAS-based, unbounded
 *
 * IMPORTANT METHODS (Two styles — throws exception vs returns null/false):
 * ┌─────────────┬────────────────────┬──────────────────────┐
 * │ Operation   │ Throws Exception   │ Returns null/false   │
 * ├─────────────┼────────────────────┼──────────────────────┤
 * │ Insert      │ add(e)             │ offer(e)             │
 * │ Remove Head │ remove()           │ poll()               │
 * │ Peek Head   │ element()          │ peek()               │
 * └─────────────┴────────────────────┴──────────────────────┘
 * Note: AbstractQueue is a separate branch
 */

public class MyQueue {
    public static void main(String[] args) {

        // ── 1. LinkedList as Queue ───────────────────────────────────────────
        // - Doubly linked list internally; implements both List and Deque
        // - Allows null elements; NOT thread-safe
        // - O(1) for add/remove at head/tail
        Queue<Integer> linkedQueue = new LinkedList<>();
        linkedQueue.offer(10);       // safe insert (returns false if full)
        linkedQueue.offer(20);
        linkedQueue.offer(30);
        System.out.println("LinkedList Queue: " + linkedQueue);
        System.out.println("Peek (head): "  + linkedQueue.peek());    // 10, no remove
        System.out.println("Poll (remove): " + linkedQueue.poll());   // removes 10
        System.out.println("After poll: "   + linkedQueue);


        // ── 2. PriorityQueue ─────────────────────────────────────────────────
        // - Min-heap by default (smallest element is always at head)
        // - Does NOT guarantee FIFO; ordering is by natural order or Comparator
        // - Does NOT allow null; NOT thread-safe
        // - O(log n) for add/remove; O(1) for peek
        Queue<Integer> pq = new PriorityQueue<>();
        pq.offer(50);
        pq.offer(10);
        pq.offer(30);
        System.out.println("\nPriorityQueue (min-heap):");
        while (!pq.isEmpty()) {
            System.out.print(pq.poll() + " ");   // prints: 10 30 50
        }

        // Max-heap using reverse order comparator
        Queue<Integer> maxPQ = new PriorityQueue<>(Collections.reverseOrder());
        maxPQ.offer(50); maxPQ.offer(10); maxPQ.offer(30);
        System.out.println("\nMax PriorityQueue: " + maxPQ.poll()); // 50


        // ── 3. ArrayDeque as Queue ───────────────────────────────────────────
        // - Resizable array-backed; implements Deque (circular double-ended queue)
        // - Faster than LinkedList for queue operations (no node overhead)
        // - Does NOT allow null; NOT thread-safe
        // - Preferred over LinkedList for queue/stack use (access/read from anywhere is optimal)
        Deque<String> arrayDeque = new ArrayDeque<>();
        arrayDeque.offer("A");         // add to tail
        arrayDeque.offer("B");
        arrayDeque.offerFirst("Z");    // add to head (Deque-specific)
        System.out.println("\nArrayDeque: " + arrayDeque);       // [Z, A, B]
        System.out.println("PeekLast: " + arrayDeque.peekLast()); // B
        System.out.println("PollFirst: " + arrayDeque.pollFirst()); // Z
        @SuppressWarnings("unused")
        Deque<Integer> intDq = new LinkedList<>(); // optimal when insertion/deletion in the middle are required


        // ── 4. ArrayBlockingQueue (Bounded BlockingQueue) ────────────────────
        // - Fixed-capacity, thread-safe queue (uses ReentrantLock internally)
        // - Blocks producer if full; blocks consumer if empty
        // - Used in producer-consumer pattern
        // - put() blocks; take() blocks | offer()/poll() with timeout don't block forever
        BlockingQueue<Integer> abq = new ArrayBlockingQueue<>(3); // capacity = 3 internal array size
        abq.offer(1);
        abq.offer(2);
        abq.offer(3);
        boolean added = abq.offer(4); // returns false — queue is full
        System.out.println("\nArrayBlockingQueue full, offer(4): " + added);
        System.out.println("ABQ: " + abq);


        // ── 5. LinkedBlockingQueue (Optionally Bounded BlockingQueue) ────────
        // - Linked-node based; optionally bounded (default: Integer.MAX_VALUE)
        // - Uses TWO separate locks (head lock & tail lock) → higher throughput than ABQ
        // - Preferred when producers and consumers operate at different rates
        BlockingQueue<String> lbq = new LinkedBlockingQueue<>(10);
        lbq.offer("Task-1");
        lbq.offer("Task-2");
        System.out.println("\nLinkedBlockingQueue: " + lbq);
        System.out.println("Head: " + lbq.poll());


        // ── 6. PriorityBlockingQueue ─────────────────────────────────────────
        // - Unbounded, thread-safe PriorityQueue
        // - Elements ordered by natural order or Comparator
        // - Blocks only on take() when empty (never blocks on put)
        BlockingQueue<Integer> pbq = new PriorityBlockingQueue<>();
        pbq.offer(40); pbq.offer(5); pbq.offer(20);
        System.out.println("\nPriorityBlockingQueue poll order: "
                + pbq.poll() + ", " + pbq.poll()); // 5, 20


        // ── 7. SynchronousQueue ──────────────────────────────────────────────
        // - Zero-capacity queue; each put() must wait for a take() and vice versa
        // - Acts as a direct handoff channel between threads
        // - Used in Executors.newCachedThreadPool()
        SynchronousQueue<String> sq = new SynchronousQueue<>();
        // sq.offer() returns false immediately if no consumer is waiting
        System.out.println("\nSynchronousQueue offer (no consumer): " + sq.offer("data"));


        // ── 8. ConcurrentLinkedQueue ─────────────────────────────────────────
        // - Unbounded, lock-free, thread-safe queue (uses CAS operations)
        // - Non-blocking; best for high concurrency with many threads
        // - Does NOT support blocking operations (no put/take)
        ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
        clq.offer("X"); clq.offer("Y"); clq.offer("Z");
        System.out.println("\nConcurrentLinkedQueue: " + clq);
        System.out.println("Poll: " + clq.poll()); // X
    }
}
