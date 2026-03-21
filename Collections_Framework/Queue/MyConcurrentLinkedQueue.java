package Collections_Framework.Queue;

import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * ─────────────────────────────────────────────────────────────────────────────
 *  ConcurrentLinkedQueue
 * ─────────────────────────────────────────────────────────────────────────────
 *  → A thread-safe, unbounded, non-blocking FIFO (First-In-First-Out) queue.
 *  → Built on a lock-free algorithm (Michael & Scott, 1996) using CAS
 *    (Compare-And-Swap) CPU instructions — no synchronized blocks needed.
 *
 *  Internal Structure:
 *  ┌──────┐    ┌──────┐    ┌──────┐    ┌──────┐
 *  │ "A"  │ →  │ "B"  │ →  │ "C"  │ →  │ "D"  │
 *  └──────┘    └──────┘    └──────┘    └──────┘
 *    head                               tail
 *
 *  → Each node holds a volatile 'item' and a volatile 'next' pointer.
 *  → Multiple threads can offer() (write) and poll() (read) simultaneously
 *    without blocking each other — unlike ArrayBlockingQueue.
 *
 *  Key Characteristics:
 *  ✔ Thread-safe without locks (CAS-based)
 *  ✔ Unbounded — grows dynamically (no capacity limit)
 *  ✔ Does NOT block — poll() returns null if empty (non-blocking)
 *  ✔ offer() never rejects elements (always returns true)
 *  ✗ size() is O(n) — traverses the whole list (avoid in hot paths)
 *  ✗ Not suitable when you need blocking behavior → use BlockingQueue instead
 *
 *  Use cases:
 *  → High-throughput task queues, event pipelines, producer-consumer scenarios
 *    where producers/consumers should never block each other.
 * ─────────────────────────────────────────────────────────────────────────────
 */

public class MyConcurrentLinkedQueue {

    // Shared queue — multiple producer & consumer threads operate on this
    static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    // ── Producer Thread: keeps adding tasks to the queue ──────────────────────
    static class Producer implements Runnable {
        private final String name;

        Producer(String name) { this.name = name; }

        @Override
        public void run() {
            for (int i = 1; i <= 4; i++) {
                String task = name + "-Task-" + i;
                queue.offer(task);   // offer() is thread-safe & non-blocking
                System.out.println("[WRITE] " + name + " added: " + task);
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }
    }

    // ── Consumer Thread: keeps polling tasks from the queue ───────────────────
    static class Consumer implements Runnable {
        private final String name;

        Consumer(String name) { this.name = name; }

        @Override
        public void run() {
            for (int i = 0; i < 4; i++) {
                // poll() retrieves AND removes head — returns null if empty (non-blocking)
                String task = queue.poll();
                if (task != null) {
                    System.out.println("[READ]  " + name + " processed: " + task);
                } else {
                    System.out.println("[READ]  " + name + " found empty queue, retrying...");
                }
                try { Thread.sleep(80); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // 2 producers writing, 2 consumers reading — all running concurrently
        Thread p1 = new Thread(new Producer("Producer-1"));
        Thread p2 = new Thread(new Producer("Producer-2"));
        Thread c1 = new Thread(new Consumer("Consumer-1"));
        Thread c2 = new Thread(new Consumer("Consumer-2"));

        p1.start(); p2.start();
        c1.start(); c2.start();

        // Wait for all threads to finish
        p1.join(); p2.join();
        c1.join(); c2.join();

        // Remaining unprocessed tasks (if any)
        System.out.println("\n── Remaining in queue: " + queue.size() + " ──");
        String leftover;
        while ((leftover = queue.poll()) != null) {
            System.out.println("  Leftover: " + leftover);
        }

        /*
         *  Why no data corruption?
         *  ─────────────────────────────────────────────────────────────────
         *  → offer() atomically swings the tail pointer to a new node via CAS.
         *  → poll() atomically reads and nulls out the head item via CAS.
         *  → If two threads race on the same pointer, only ONE CAS wins;
         *    the loser retries — no data is lost or duplicated.
         *
         *  ConcurrentLinkedQueue vs ArrayBlockingQueue (key difference):
         *  ┌─────────────────────────┬──────────────────────────┐
         *  │ ConcurrentLinkedQueue   │ ArrayBlockingQueue       │
         *  ├─────────────────────────┼──────────────────────────┤
         *  │ Non-blocking (CAS)      │ Blocking (locks)         │
         *  │ Unbounded               │ Bounded capacity         │
         *  │ poll() → null if empty  │ poll() blocks if empty   │
         *  └─────────────────────────┴──────────────────────────┘
         */
    }
}
