package Collections_Framework.Queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

// DelayQueue: A BlockingQueue where elements can only be taken AFTER their delay expires.
// Elements are sorted internally by delay (shortest delay = head of queue).
// Use case: Task schedulers, cache expiry, retry mechanisms with cooldowns.
//
// Hierarchy: Iterable → Collection → Queue → BlockingQueue → DelayQueue
// Elements must implement the Delayed interface.

public class DelayedQueue {
    public static void main(String[] args) throws InterruptedException {

        // DelayQueue only accepts elements that implement Delayed
        BlockingQueue<DelayedTask> delayedQueue = new DelayQueue<>();

        // Add tasks with different delays — internally sorted by soonest expiry
        delayedQueue.put(new DelayedTask("Task1", 5, TimeUnit.SECONDS));   // expires last among first two
        delayedQueue.put(new DelayedTask("Task2", 3, TimeUnit.SECONDS));   // expires first
        delayedQueue.put(new DelayedTask("Task3", 10, TimeUnit.SECONDS));  // expires last

        // take() BLOCKS until a task's delay expires, then returns it
        // Output order: Task2 → Task1 → Task3 (sorted by expiry time)
        while (!delayedQueue.isEmpty()) {
            DelayedTask task = delayedQueue.take();
            System.out.println("Executed: " + task.getTaskName() + " at " + System.currentTimeMillis() + "ms");
        }
    }
}

// Every element in a DelayQueue must implement the Delayed interface,
// which requires two methods: getDelay() and compareTo()
class DelayedTask implements Delayed {
    private String taskName;
    private long startTime; // absolute time (ms) when this task becomes eligible

    public DelayedTask(String taskName, long delay, TimeUnit unit) {
        this.taskName = taskName;
        // Convert relative delay → absolute future timestamp
        this.startTime = System.currentTimeMillis() + unit.toMillis(delay);
    }

    // compareTo() defines the ordering inside DelayQueue (min-heap internally)
    // The task with the smallest startTime sits at the head of the queue
    @Override
    public int compareTo(Delayed o) {
        if (this.startTime < ((DelayedTask) o).startTime) return -1;
        if (this.startTime > ((DelayedTask) o).startTime) return 1;
        return 0;
    }

    // getDelay() tells the queue how much time is LEFT before this task is eligible.
    // When getDelay() returns <= 0, the task can be taken from the queue.
    @Override
    public long getDelay(TimeUnit unit) {
        long remaining = startTime - System.currentTimeMillis();
        return unit.convert(remaining, TimeUnit.MILLISECONDS);
    }

    public String getTaskName() {
        return this.taskName;
    }
}