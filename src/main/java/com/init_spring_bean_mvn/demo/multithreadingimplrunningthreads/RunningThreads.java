package com.init_spring_bean_mvn.demo.multithreadingimplrunningthreads;

public class RunningThreads {

    public static void main(String[] args) {
        System.out.println("Main thread running");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Thread thread = new Thread(() -> {
            String tname = Thread.currentThread().getName();
            System.out.println(tname + " should take 10 dots to run.");
            for( int i = 0; i < 10; i++) {
                System.out.print(". ");
                try {
                    Thread.sleep(500);
                    System.out.println(tname + " should take 10 dots to run.");
                } catch (InterruptedException e) {
                    System.out.println("\nWhoops!! " + tname + " interrupted.");
                    System.out.println("A1. State = " + Thread.currentThread().getState());
                    return;
                }
            }
            System.out.println("\n" + tname + " completed.");
        });

        System.out.println(thread.getName() + " starting");
        thread.start();

        System.out.println("Main thread would continue here");

        // Don't want thread to run until fist completed successfully
        Thread installThread = new Thread(() -> {
            try {
                for(int i = 0; i < 3; i++) {
                    Thread.sleep(250);
                    System.out.println("Installation Step");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
        }
        }, "InstallThread");


        // start at same time as original thrird thread
        Thread tm = new Thread(() -> {

            long now = System.currentTimeMillis();
            while (thread.isAlive()) { // negates what join does - won't be thread rnning to join
                //System.out.println("\nMain thread would continue here");
                try {
                    Thread.sleep(1000);
                    if (System.currentTimeMillis() - now > 8000) {
                        thread.interrupt();
                    }
                } catch (InterruptedException e) {
                    // Continue lecture: Call interrupt on current thread
                    Thread.currentThread().interrupt(); // fixes status being reset during thread processs
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        tm.start(); // required

        // any method that catches an interrupted error should re-interrupt itself ( sometimes can't throw exception )
        try {
            thread.join(); // make a dependent task - waiting for any ansynchronouse task sequence to finish
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // System.out.println("C. State = " + thread.getState());

        if(!thread.isInterrupted()) {
            installThread.start();
        } else {
            System.out.println("Previous thread interrupted, " +
                    installThread.getName() + " can't run. ");
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
        // Used to cancel download per user input

        // Thread states; New - not yet started
        // Runnable, Blocked, Waiting: waiting on another thread, Timed_Waiting: waiting for thread . sleep to end, Terminated: completes, interrupted by other means
        // No interrupted state: methods to check

        // join method - ex: downloading installation package only if download completed

        // creating third thread that interrupts first, takes longer than 3 seconds

        // Concurrent threads working in your objects heap memory
    }
}
