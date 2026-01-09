package com.init_spring_bean_mvn.demo.lpa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class VisitorList {

    // demomstract to collect a stream of elements to this class
        // handles threading efficiently in multithreaded environment -
    // output - two visitors adding
    // poll method doesn't block if queue empty
    private static final CopyOnWriteArrayList<Person> masterList; // modified ? adding, updating, remvoings ? new copy underlying array created - modification persformed on new copy
    // useful when shared data accessed for reading
    // ensures ready threads aren't blocked by writers
    // expensive array add methods - Next : more thread problems - Deadlock other example and compare to liveLock and starvation
    // separate copy of array  ? aren't any synchronization issues betwenn reding and writigin threads
    // more efficient when traversal operations, vastly outnumber mutations i.e. 100s of thousands of users accessing coupons.
    static {
        masterList = Stream.generate(Person::new)
                .distinct()
                .limit(2500)
                .collect(CopyOnWriteArrayList::new, CopyOnWriteArrayList::add,
                         CopyOnWriteArrayList::addAll); // arguments are supplier, accumulater and combiner

    }
    private static final ArrayBlockingQueue<Person> newVisitors = new ArrayBlockingQueue<>(5);
    // Thread safe - different options to control and manage shared data being accessed
    // Removing single element from ArrayBlockingQueue: most methods get element at head of queue - first in
    // peek - check queue before attempting blocking element - see slide in 346
    // overloaded version of remove  is very different - does not block and returns boolean
    public static void main(String[] args) {
        // visitor gets coupon - used in store or applied to next entry fee
        Runnable producer = () -> {
            Person visitor = new Person();
            // System.out.println("Adding " + visitor);
            System.out.println("Queueing " + visitor);
            boolean queued = false;
            try {
                queued = newVisitors.offer(visitor, 5, TimeUnit.SECONDS);
                // want to send out alarm bell to alert or recover and kickoff consumer threads from this producer

                // for us - some visitors won't get coupons; could log all visitors to file -
                // - method called drainTo method to populate another collection

                // newVisitors.put(visitor);
               // queued = newVisitors.add(visitor);
            } catch(InterruptedException e) {
                System.out.println(newVisitors);
            }

            if(queued ) {
                System.out.println(newVisitors);

            } else {
                // handle error scenario when consumer threads missing - 1. Drain, 2. Write to file
                // File can be consulted and then coupons can be added to users manually - NEXT: Consumer tasks
                System.out.println(" Queue is Fukll, cannot add " + visitor);
                List<Person> tempList = new ArrayList<>();
                newVisitors.drainTo(tempList);
                List<String> lines = new ArrayList<>();
                tempList.forEach((customer)-> lines.add(customer.toString()));
                lines.add(visitor.toString());
                try {
                    Files.write(Path.of("Drained"), lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    throw new RuntimeException(e); //rethrow
                }
            }
        };

        // Set write arfer producer runnable

        Runnable consumer = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " Polling queue " + newVisitors.size());
            Person visitor  = null; // add timeout args to poll and throws checked exception
            try {
                // visitor = newVisitors.poll(5, TimeUnit.SECONDS);
                visitor = newVisitors.take(); // code will hang now
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(visitor != null) {
                System.out.println(threadName + " " + visitor);
                if(!masterList.contains(visitor)) {
                    masterList.add(visitor);
                    System.out.println("--> New Visitor gets Coupon! " + visitor);
                }
            }
            System.out.println(threadName + "--> done " + visitor);

        };
        // want many threads running ? rcreated consumer pool

        ScheduledExecutorService pe = Executors.newSingleThreadScheduledExecutor();
        pe.scheduleWithFixedDelay(producer, 0, 1, TimeUnit.SECONDS);

        ScheduledExecutorService pec = Executors.newSingleThreadScheduledExecutor();
        pec.scheduleWithFixedDelay(producer, 0, 1, TimeUnit.SECONDS);
        // producer code adds data to arrayblockingqueue
        for(int i = 0; i < 3; i++) {
            pec.scheduleAtFixedRate(consumer, 6, 3, TimeUnit.SECONDS);
        }

        while(true) {
            try {
                if (!pe.awaitTermination(10, TimeUnit.SECONDS)) // handle while consumer threads missing in actions
                    // timeout changed to 10 for adding consumerpool code, Delete Drained file from previous
                    break;
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        pe.shutdown();

        // Consumer pool
        while(true) {
            try {
                if (!pec.awaitTermination(3, TimeUnit.SECONDS)) // handle while consumer threads missing in actions
                    break;
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        pec.shutdown();
    }

    // ArrayBlockingQueue  - severla methods to choose from to add element to array - add, offer, offer () overload, put: blocks - yes
    // - see slide in 345 for more details
}
