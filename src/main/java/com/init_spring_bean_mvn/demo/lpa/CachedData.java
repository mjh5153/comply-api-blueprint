package com.init_spring_bean_mvn.demo.lpa;

import com.init_spring_bean_mvn.demo.consupro.ConsuPro;
import com.init_spring_bean_mvn.demo.multithreadingdeadlockprev.DeadLockPrev;
import com.init_spring_bean_mvn.demo.multithreadingheapthread.ThreadColor;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CachedData {

    private volatile boolean flag = false;// volatile keyword used as modifier for class variables for fields -
    // indicator that variables value may be changed by multiple threads
    // ensures variable is always read from and written to main mem rather than thread-specific caches
    // Provides memory consistency for variables value across threads
    // Volatile has limited usage through -
    // 1. doesn't guarantee atomicity across threads
    // 2. When var is used to track state of shared resource - counter or flag

    // 2. var is used to communicate between threads
    /* When not to use!!!*/
    // 1. NOT when variable is used to single thread
    // 2. Variable used to store large amount of data
    // Solution - Synchronization - Next video 328.



    public void toggleFlag() {
        flag = !flag;
    }

    // accessor method
    public boolean isReady() {
        return flag;
    }



    public static void main(String[] args) {
        CachedData example = new CachedData();

        Thread writerThread = new Thread(() -> {
            try{
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            example.toggleFlag();
            System.out.println("A. Flag set to " + example.isReady());
        });

        Thread readerThread = new Thread(() -> {
            // reader waiting indefinitely
            while(!example.isReady()) {
                // Busy-wait until flag becomes true
            }
            System.out.println("B. Flag is " + example.isReady());
        });

        writerThread.start();
        readerThread.start(); // reader thread stuck in loop? memory inconsistency - to optimize perf of threads os may read from heap vars and mak copy of value in each threads storage cache
        // each trehad ahs ist sown small and fast mem storage and holds own shared resources value -
        //      one thread can modify shared var - may not be mad immediatly visible on heap
        // first updated on threads local cache -
        // os may not flush first threads changes to heap until thread has finished executing
    }

}
