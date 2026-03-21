package Collections_Framework.Queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*
 ArrayBlockingQueue -- Bounded, thread-safe FIFO queue backed by a fixed-size circular array (circular array is used to avoid shifting during insrtion/deletion)
 >> Capacity fixed at construction time; cannot grow dynamically
 >> Internally uses a single ReentrantLock + two Conditions (notEmpty, notFull) for thread coordination
 >> put()  → blocks the thread when queue is FULL  (waits on notFull  condition)
 >> take() → blocks the thread when queue is EMPTY (waits on notEmpty condition)
 >> offer()/poll() with timeout → don't block forever; respect the given timeout
 >> Use case: classic Producer-Consumer pattern where you want back-pressure
 >> Uses a single lock for both enqueue and dequeue ops
 >> Cause problems if multiple threads are there
*/

// Producer: continuously generates items and puts them into the shared queue
class Producer implements Runnable {
    int value;
    BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Producer Produced: " + value);
                queue.put(value++); // blocks if queue is full (capacity reached)
                Thread.sleep(1000); // produces 1 item per second
            } catch (Exception e) {
                Thread.currentThread().interrupt(); // restore interrupted status
                System.out.println("Producer Interrupted: " + e.getMessage());
            }
        }
    }
}

// Consumer: continuously takes items from the shared queue and processes them
class Consumer implements Runnable {
    BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int value = queue.take(); // blocks if queue is empty (waits for producer)
                System.out.println("Consumer Consumed: " + value);
                Thread.sleep(2000); // consumes at half the rate of producer → queue fills up
            } catch (Exception e) {
                Thread.currentThread().interrupt(); // restore interrupted status
                System.out.println("Consumer Interrupted: " + e.getMessage());
            }
        }
    }
}

public class MyArrayBlockingQueue {
    public static void main(String[] args) {
        // shared queue with capacity 5 — producer blocks once 5 items are pending
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        Thread producer = new Thread(new Producer(queue));
        Thread consumer = new Thread(new Consumer(queue));

        // both threads share the same queue reference → safe coordination via blocking
        producer.start();
        consumer.start();
        // Producer runs at 1s/item, Consumer at 2s/item
        // → queue fills up to capacity 5, then producer blocks until consumer catches up
    }
}
