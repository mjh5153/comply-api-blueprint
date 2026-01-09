package com.init_spring_bean_mvn.demo.multithreadingimpl;

import java.util.concurrent.TimeUnit;

public class RunnableInterfaceImpl {

    // Runnable is functional interface
    // function method or single access method is run method
    // Runnable has target for lambda expression
    // Any class can implement runnable to implement asynchronously

    // Challenge - ExecutorService classes - Any thread - SingleThreadExecutor, FixedThreadPool, CachedThreadPool or combo of these
    // - Add Executor service to main method -?> send orders to warehouse
    // - Add ExecutorService to ShoeWarehouse class -> FullFills orders
    // - Create and fill 15 shoe orders

    // Next Concurrency - Scheduling Tasks; Schedule Tasks with ScheduledExecutorService -
    public static void main (String[] args) {
        Runnable myRun = () -> {
            // goes in run method of Runnable
            // this is threads task
            for(int i = 1; i<=8; i++) {
                try {
                    System.out.println(i);
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        // Use in thread - create new thread instance and pass runnable function
        // extending threads: more control over threads behavior and properties
        // access to threads methods and fields directly from sub class
        // You can create a new thread for each task

        // Disadvantages: only extend one class in java - subclass can't extend other classes
        // tightly coupled to thread class, difficult to maintain
        // Implementing Runnable interface - can create threads in class that implements - passed to Thread constructor that accepts Runnable -
        // also pass anonymous class, lambda express or method
        // Advantages - extend any class and still implement Runnable, class is loosely coupled to Thread class - easier to maintain, anonymous classes, lambda expressions or method refs. to quickly describe behavior
        // Disadvantage - less control over threads behavior and properties - can't access threads methods in fields directly in run method
        // Next - interrupt thread and create dependencies in thread
        // Manipulate and communicate with Running Threads i.e. sleep - pause execution of thread for time
        // Can do in main thread or concurrent
        Thread mythread = new Thread(myRun);
        mythread.start();
    }
}
