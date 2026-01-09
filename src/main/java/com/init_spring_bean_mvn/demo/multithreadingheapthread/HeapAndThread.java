package com.init_spring_bean_mvn.demo.multithreadingheapthread;

import java.util.concurrent.TimeUnit;

public class HeapAndThread {
    public static void main (String[] args) {
        ///  Types of memory threads have access to
        // Threads have own memory -> both have access to heap- have access to modify same object,
        // One thread changes an object on heap -> changes are visible to other threads

        //Fire up threads and observe what happens with these memory spaces
        // give separate instance for stop watches to ensure thread safety: caviats - field i could be static
        // --- immmutable objects could be used - make i final and make Thread-Safe
        // Thread sfatey- isn't compromised by execution of concurrent threads - correctness and consistency of program's output or visible state is unaffected by other threads
        // Atomic operations and immutable objects are examples of thread-safe code.
        // Real world - Shared resources need to be avaialbe to multple concurrent threads in real time - control access to resource - to protect from access of interleaving threads
        // - One thread finsih full unit of work - i.e.. Wait in Line
        StopWatch stopWatch = new StopWatch(TimeUnit.SECONDS);
        StopWatch purpleWatch = new StopWatch(TimeUnit.SECONDS);
        StopWatch redWatch = new StopWatch(TimeUnit.SECONDS);
        Thread green = new Thread(stopWatch::countDown, ThreadColor.ANSI_GREEN.name());
        Thread purple = new Thread(() -> purpleWatch.countDown(7), ThreadColor.ANSI_PURPLE.name());
        Thread red = new Thread(redWatch::countDown, ThreadColor.ANSI_RED.name());
        green.start();// start code concurrrently
        purple.start();
        red.start(); // made i in forloop field on stopwatch class?
    }
}


// 25 Thread

class StopWatch {
    // no modifer or package default

    //
    private TimeUnit timeUnit;
    private int i = 0;
    StopWatch(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
    public void countDown() {
        countDown(5); // When create threads, it will  demonstrate how to use method reference
    }
    public void countDown(int unitCount) {
        String threadName = Thread.currentThread().getName();

        ThreadColor threadColor = ThreadColor.ANSI_RESET;
        try{
            threadColor = ThreadColor.valueOf(threadName);

        } catch (IllegalArgumentException ignore) {

        }
        String color = threadColor.color();

        for(i = unitCount; i > 0; i--) { // all contdown from 7 though purple only setup,
            // sharing single stopwatch isntance amount 3 threads
            // each inisitalized at same time,
            // by time a second elapses, and thread goes to print, already have next
            // threads all changing field i and visible to all threads as progressing

            // Time not kept directly
            // Not atomic unit of work - operation split into much smaller pieces of task, each is another moment to thread to have moment of stage
            // Time Slicing - timesharing or time division
            // multiple threads or processes to share a single cpu for secution - available cpu time is sliced into small time intervales,
            // eah threads gets interval - attempt to make some progress, on tasks it has to do,
            // Task complete or not ? time slice ?  thread management system doesn't care,
            // TimeUp? yieldToOtherThread : WaitTurn
            // Threads sharing heap memory ? things can change during that wait
            // JMM - rules and behaviors for threads -> to help control and manage shared access to data and operations
            // 1. Understand problems: Atomicity of Operations - few are truly, problems with shared objs b/c of time slicing
            // 2. Synchronization- process of controlling threads access to shared resources
            try {
                timeUnit.sleep(1);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s%s Thread : i = %d%n", color, threadName, i);

            // Thread safety - Lists, Queues, and ArrayBlockingQueue - Last Lesson:
            // Next Mastering Concurrency and Multithreading: - 350 is Last section - showing next 5
            //      - 327: Advanced Concurrency: Interleaving, Atomicity, Memory Consistency

            // i in for loop: threads had no conflicts, adding instance variable -
            // for loop block - i = unitCount, i--: i= i-1, printf - Each thread can show up in boundary at any time - A
            //  thread can be halfway through the work in any one of these blocks, when it's time slice expires, and it then has to pause or suspend execution,
            //      to allow other threads to wake up and execute, means another active thread has an open door to same unit of work where paused thread is only parially done
            //              threads start and pause in same blocks as other threads - called interleaving - when multiple threads run concurrently - instructions can overlap or interleave in time
            //                  - exeution of threads happens arbitrarily
            // - Atomic action - Action happens all at once - happens completely or not at all: side effects never visible until action completes
            //  - Even simple: decrements and incremenats ren't all atomic nore primitive assignments: long and doulb emay not be atomic in all jvms - simple statements can even translate to multiple non-atomic steps by virtual machine
            //      - Opens door to interleaving threads
            // Prevention; shared objects with care - example stopWatch instance: could change time unit while running
            //  Fix: 1. Create 3 StopWatch instances


            //     X - 328: Implementing Synchronization: Synchronized Methods
            //     X - 329: Snchronized Blocks
            //     X - *330: Building a PRoducer-Consumer app - Deadlock Exploration*
            //     X - 332: Preventing Deadlocks - using wait and notify
            //       - 333: Challenge - Java Shoe Warehouse Fulfillment Center
            //       - 334: Explicit Locking in Java Concurrency: Exploring java.util.concurrent.locks
            //        - 335: Advanced Locking: Exploring the Benefits and Uses of ReentrantLock in Java
            //      - 336: Thread Management with ExecutorService: SingleThreadExecutorService
            //      - 337:
        }
    }
}
