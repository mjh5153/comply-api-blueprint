package com.init_spring_bean_mvn.demo.multithreadingdeadlockprev;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StarvingThread {
    // Greedy thread acquires lock and in java locking isn't implicitly fair
    // Priority in java is a suggestion
    // ** Need more fair distribution of Thread
    // private static final Lock lock = new ReentrantLock();
    // Can put true for Reentrant to evently distribute;

    // Fair locks: all threads waiting to acquire lock will be giving equal chance to acquire lock
    // Monitor lock unfair,
    // Thread requrests access ? gets added to FIFO queue

    // Pros
    // 1. Prevent thread starvation, improve overall perfo of system
    // 2. system more predictable and easy to debug,
    // Cons: 1. threads frequrently competing for locks - negative effect on performance
            // 2. Can be more difficult to implement
    private static final Lock lock = new ReentrantLock(true);
    public static void main(String[] args) {
        Callable<Boolean> thread = () -> { // use because returns value to take advantage of returnAll method
            String threadName = Thread.currentThread().getName();
            int threadno = Integer.parseInt(threadName.replaceAll(".*-[1-9]*-.*-", ""));
            boolean keepGoing = true;
            while(keepGoing ) {

                lock.lock(); // acquire lock or block here

                // print which thread required lock, thread unique
                try {
                    System.out.printf("%d acquired the lock.%n", threadno );
                    TimeUnit.SECONDS.sleep(1);
                } catch(InterruptedException e) {
                    System.out.printf("Shutting down %d%n", threadno);
                    Thread.currentThread().interrupt(); // have thread shutdown smoothly
                    return false;
                } finally {
                    // be diligent about releasing lock
                    lock.unlock();
                }



            }
            return true;
        };

        var executor = Executors.newFixedThreadPool(3);
        try {
            var futures = executor.invokeAll(
                    List.of(thread, thread, thread),
                    10,
                    TimeUnit.SECONDS
            );
        } catch(InterruptedException e ) {
            throw new RuntimeException(e);
        }
        executor.shutdownNow();

        // Result thread has thread number AND required lock
    }
}
