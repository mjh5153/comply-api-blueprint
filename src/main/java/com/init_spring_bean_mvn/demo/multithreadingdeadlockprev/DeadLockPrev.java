package com.init_spring_bean_mvn.demo.multithreadingdeadlockprev;

import java.io.File;

public class DeadLockPrev {

    public static void main (String[] args) {

        // blocked inside synchronized block - classic deadlock
        // threads need access to multiple resources in certain order
        // Another process needs to shutdown a thread for furthur
        // Organize locks into hierarchy and ensure threads acquire locks in certain order - csv then json
        // Can use try lock method on interface - fails? handle situation without DEADLOCK
        File ra = new File("inputData.csv");
        File rb = new File("outputData.json");

        Thread ta = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("trying to get src a " + threadName);
            synchronized (ra) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                System.out.println("Next - trying to get b ");
                    synchronized (rb) {
                        System.out.println("Next - lock to get b ");

                    }
                System.out.println("Next - lock released on b " );


            }
            System.out.println("Released " + threadName);

        }, "THREAD-A");
    // Can't use hierarchy ? don't use synchronized block as nesting locking mechanize, use explicity locks implementations instead of build in monitor
        Thread tb = new Thread(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("trying to get src a " + threadName);
            synchronized (ra) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Next - trying to get b ");
                synchronized (rb) {
                    System.out.println("Next - lock to get b ");

                }
                System.out.println("Next - lock released on b " );


            }
            System.out.println("Released " + threadName);
//            String threadName = Thread.currentThread().getName();
//            System.out.println("trying to get src b " + threadName);
//            synchronized (rb) {
//                System.out.println("has lock to get src b " + threadName);
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                System.out.println("Next - lock released on b " );
//                synchronized (ra) {
//                    System.out.println(threadName + "Next - lock released on a " );
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                System.out.println(threadName + "has - lock released on a csv" );
//
//
//            }
//            System.out.println(threadName +  " has - lock released on resource b (json " );

        }, "THREAD-B");

        ta.start();
        tb.start();

        try {
            ta.join();
            tb.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    // Deadlock:
    // LiveLock
    // Starvation

    // Object class wait, notify and notifyAll methods: used to manage some monitor lock situations - prevent rheads from blocking indefinietely
    // Because on Object - andy instance of a class can execute from synchronized method or statement

    // see Object.html#method-summary in java docs - docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#method-summary
    // causes current thread to put itself in wait set - puts current thread in wait queue for object it might have lock on - frees
    // up other threads to have access to object
    // thread calls wait will be dormant - relinguishes lock ;
    // 1. notified by other threads ( threads broadcast notify or notifyall method )
    // 2. interrupted thread ( overloaded version - timeout ? wakeup thread )
    // 3. Thread T is awakend spuriously - rare but need to code for it.

    // wait method: wakes up single thread: choice is arbitrary - NOT GOOD : thread who has lock needs to be notified
    //      could be thread unrelated
    // notifyAll: Will wake up all threads but can't require lock until notifyee release lock
    // any synchronized code that has loop needs to have wait method called our - read and write methods in MessageRepository

    // Purpose of Lock: controll access to shared resource by multiple threads
    // monitor lock easy but limitations
    //1. No way to test if intrinsic lock has bee acquired
    //2. no way to interrup blocked thread,
    // 3. no way to debug or examince instrinsic lock.
    // 4. exclusive lock

    // jdk5: java.util.concurrent package - additional solutions to prevent multi-threaded env locks
    // impls on locking and how to unblock blocked threads
    // methods:  lock(), lockInterruptibly(), newCondition() tryLock() tryLock(time, TimeUnit unit), unlock()

}
