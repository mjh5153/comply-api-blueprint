package com.init_spring_bean_mvn.demo.consupro;

import com.init_spring_bean_mvn.demo.multithreadingdeadlockprev.DeadLockPrev;
import com.init_spring_bean_mvn.demo.multithreadingheapthread.ThreadColor;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MessageRepository { // Shared Resource
    private String message;
    private boolean hasMessage = false; // Producer can populate shared message,
    // true - Consumer can read it


    // 334. Explicit lock
    private final Lock lock = new ReentrantLock();
    public String read() { // current rhead is not owner of lock error - needs to be synchronized
        // Solution in Thread class - current thread and exception thrown
        // acquire lock
        if(lock.tryLock()) { // allows to figure out if better approach than blocking - now remove write message intrincinct lock


        try {


        while(!hasMessage) {
            try {
                //wait();
                Thread.sleep(500); // need to lock and unlock statement
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        hasMessage = false;
        // notifyAll(); // call outside loop and is to trigger another thread is waiting on
        } finally {
            lock.unlock(); // will always be executed even on exception - BP for locking
            // won't fix all deadlocks
            // Lock class gives options to chooose from before a thread gets lock and blocks
        }
        } else {
            System.out.println("** read blocked: " + lock);
            hasMessage = false;
        };
        return message; // returned to consume
    }
    //remove instrinc lock
    // remove synch, add lock.tryLock()
    public void write (String message) {
        try {
            if(lock.tryLock(3, TimeUnit.SECONDS)) { // throws checked exception
                while (hasMessage) {
                    try {
                        // wait(); // Always call wait in for loop - test whatever thread is waiting on and can proceed on task
                        // thread might not complete task it's set to do
                        // when message notified - loop will start at beginning
                        // Thread might not wake up by broadcast
                        Thread.sleep(500); // wait every half second instead of
                        // may not be acceptable solution if messages get skipped - could use overloaded tryLock
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        lock.unlock();
                    }

                }
            } else {
                System.out.println("** write blocked: " + lock); // Useful if managing many threads
                hasMessage = true;
            }
        } catch (InterruptedException e) { // posiilbe to interrupt the thread before times out
            throw new RuntimeException(e);
        }
        // no more message | consumed
        hasMessage = true;
        notifyAll(); // Still looping - no way to notify specific thread:
        // Call unless there are many threads set to do specific task
        this.message = message;
    }
}
//Producer
class MessageWriter implements Runnable {
    private MessageRepository outgoingMessage;

    private final String text = """
            Humpty Dumpty sat on a wall
            Humpty Dumpty had a great fall,
            All the king's horses and all the kings's men,
            Couldn't put Humpty together again.
            """;
    public MessageWriter(MessageRepository outgoingMessage) {
        this.outgoingMessage = outgoingMessage;
    }
    @Override
    public void run() {
        Random random = new Random();
        String[] lines = text.split("\n");

        for (int index = 0; index < lines.length; index++) {
            outgoingMessage.write(lines[index]);
            try {
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        outgoingMessage.write("Finished");
    }
}

class MessageReader implements Runnable {
    private MessageRepository incomingMessage;

    MessageReader(MessageRepository incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    @Override
    public void run() {
        Random random = new Random();
        String latestMessage = "";

        do {
            try {
                Thread.sleep(random.nextInt(500, 2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            latestMessage = incomingMessage.read();
        } while (!latestMessage.equals("Finished"));
    }
}

// DeadLock, LiveLock and Starvation can happen here
// - DeadLock in this case: Two or more threads access multiple shareable resources
// - single resource with multiple synched methods
// Thread A - Consumer - Can usually run with read method as try and won't go into while loop
//  flag false ? execute while loop !!!! waiting on hasMessage to change value -
//  Thread A acquired lock on shared resource  and thread b can't get lock - i.e blocked from doing anything
//  Class Deadlock - Solution: shutdown application or kill it manually: Could happen in reverse situation with Producer

class ColorThreadFactory implements ThreadFactory {

    private String threadName;


    private int colorValue = 6; // default is 0 - ansi default
    public ColorThreadFactory(ThreadColor color) {
        this.threadName = color.name();
    }

    public ColorThreadFactory() {
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        String name = threadName;
        if(name == null) {
            name = ThreadColor.values()[colorValue].name();
        }

        if(++colorValue > (ThreadColor.values().length -1)) {
            colorValue = 1;
        }
        thread.setName(name);
        return thread;
    }
}
public class ConsuPro {

    private static void countDown() {
        String threadName = Thread.currentThread().getName();
        var threadColor = ThreadColor.ANSI_RESET;
        try {
            threadColor = ThreadColor.valueOf(threadName.toUpperCase());
        } catch(IllegalArgumentException ignore) {
            // ignore error
        }
        String color = threadColor.color();
        for(int index=20; index>=0; index--) {
            System.out.println(color + " " + threadName.replace("ANSI_", "") + "  " + index);
        }
    }

    private static int sum(int start, int end, int delta, String colorString) {
        var threadColor = ThreadColor.ANSI_RESET;
        try{
            threadColor = ThreadColor.valueOf( "ANSI_" + colorString.toUpperCase());
        } catch(IllegalArgumentException ignore) {
            // user may pass bad color name, ignore error
        }

        String color = threadColor.color();
        int sum = 0;
        for(int i = start; i <= end; i+= delta) {
            sum+=i;
        }

        System.out.println(color + Thread.currentThread().getName() + ", " + colorString + "  " + sum);
        return sum;


    }

    public static void main(String[] args) {
        var multiExecutor = Executors.newCachedThreadPool();
        List<Callable<Integer>> taskList = List.of(
                () -> ConsuPro.sum(1,10,1, "red"),
                () -> ConsuPro.sum(10,100,10, "blue"),
                () -> ConsuPro.sum(2,200,2, "green")
        );
        try {
           // var results = multiExecutor.invokeAll(taskList); // Collection of callables and return is list of Futures

            var results = multiExecutor.invokeAny(taskList); // single value returned and integer is result
//            for (var result : results) {
//                System.out.println(result.get(500, TimeUnit.MILLISECONDS));
//            }
            System.out.println(results);
            } catch (InterruptedException | ExecutionException  e) { ///* TimeoutException */

                throw new RuntimeException(e);
        } finally {
            multiExecutor.shutdown();
        }
        }

    // Invoke any vs invokeAll
    // 1. at least one the first to complete                                | All tasks get executed
    // 2. Result of first task to complete , not a future                   | List of rsults, as futures, for all of the tasks, once they have all completed
    // 3. Use Cases: Need quick response back from one of several websites | Torlling multiple websites and requireing responses from all of them

    public static void cachedmain(String[] args) {
        var multiExecutor = Executors.newCachedThreadPool(); // return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
//        60L, TimeUnit.SECONDS, -- after 60 seconds cached threads will be dropped
//                new SynchronousQueue<Runnable>());
        try{ // Three distinct thread names = three thread process created and run - no order
//            multiExecutor.execute(
//                    () -> ConsuPro.sum(1,10,1, "red"));
//            multiExecutor.execute(
//                    () -> ConsuPro.sum(10,100,10, "blue"));
//            multiExecutor.execute(
//                    () -> ConsuPro.sum(2,200,2, "green"));
            // Try with submit - can run a Callable
            // Difference - Callable returns a value and get data back from running threads : two way communication
            //              - returns result of instance of Future Object
            //              - Future Interface represents a result of aynch computation , generic type, placeholder for result instance
            //              - get() can only call when call is complete, or the call will block, until it does complete, overloaded TimeUnit
            //              - Methods, cancel task, retriee result or check if computation was completed or cancelled
            //              - submit doesn't care if a return or not
            try {
                var redValue = multiExecutor.submit(
                        () -> ConsuPro.sum(1, 10, 1, "red"));
                var blueValue = multiExecutor.submit(
                        () -> ConsuPro.sum(10, 100, 10, "blue"));
                var greenValue = multiExecutor.submit(
                        () -> ConsuPro.sum(2, 200, 2, "green")); // Future commmuincate result to calling code
                try {
                    System.out.println(redValue.get(500, TimeUnit.SECONDS));
                    System.out.println(blueValue.get(500, TimeUnit.SECONDS));
                    System.out.println(greenValue.get(500, TimeUnit.SECONDS));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } finally {
                multiExecutor.shutdown();
            }
//            multiExecutor.execute(
//                    () -> ConsuPro.sum(1,10,1, "yellow"));
//            multiExecutor.execute(
//                    () -> ConsuPro.sum(10,100,10, "cyan"));
//            multiExecutor.execute(
//                    () -> ConsuPro.sum(2,200,2, "purple"));
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch(InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println("Next Tasks Will get executed");
        for(var color : new String[] {"red", "blue", "green", "yellow"}) {
            multiExecutor.execute(() -> ConsuPro.sum(1, 10, 1, color));
            // output shows 2 sets than wait then next set appearing to resuse threads but not shure because threads managed
        }
        } finally {
            multiExecutor.shutdown(); // threadpool stays around until something tells it to stop running
        }
    }
    // cleaner and easier to read and scalable, change one number can use a lot more threads; i.e. int count
    public static void fixedmain(String[] args) {
        int count = 3; // thread creation process gives Thread unique color - first three threads used for first 3 tasks and then reused for next three tasks
        // Only create amount of threads specificed
        // Threads - destorying threads and creating can be expensive
        // Thread pool mitigates cost, by keeping set of threads around for future work

        // Thread pool - Worker Threads - pre-created and kept alive through lifetime of app
        //              - Submitted Tasks - FIFO queue - Threads pop tasks from queue and executed in order submitted
        //              - Thread pool manager - allocates tasks to threads and ensures proper synchronization

        // Thread Pool classes: CachedThreadPool- Creates new threads as needed - could cause resource problems
        //                      ScheduledThreadPool -
        //                      WorkStealingPool
        //                      ForjoinPool - Type of WorkStealing Pool- allows us to control how to split tasks into subtask and then join back


        // Next - Advanced Thread Pools - Callable Submit and Future in Java

        var multiExec = Executors.newFixedThreadPool(count, new ColorThreadFactory()); // overloaded newFixedThreadPool
        // change ColorThreadFactory to be more flexible
        // need to execute exectutor passing the task to run
        for(int i = 0; i <count; i++) {
            multiExec.execute(ConsuPro::countDown);
        }
        multiExec.shutdown();
    }
    public static void singlemain(String[] args) {
            // var blueExecutor = Executors.newSingleThreadExecutor(); // creates threads as needed - Replace with ThreadFactoryInterface impl
        var blueExecutor = Executors.newSingleThreadExecutor(new ColorThreadFactory(ThreadColor.ANSI_BLUE));
        blueExecutor.execute(ConsuPro::countDown); // notice no mention of Threads
        // executor service needs shutdown() method -  pool-1-thread-1: java convention for naming threads in ExecutorService
        blueExecutor.shutdown();

        // Implement workflow for Factory impl
        boolean isDone = false;
        try {
            isDone = blueExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
// Copy above for yellow and red - Will see code get executed in order - If want all tasks to be asynchronous additional code needs to be added -
        // -- next video:
//        try {
//            isDone = yellowExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        //        try {
//            isDone = yellowExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        // Thread names created by Executor - ThreadFactoryInterface - Executor.NewThreadExecptor -
    }

    // Jobs of managing threads simplified - Executor service and pass it a lambda express for task to be run
    // -- need to call shutdown - maybe need to call await termination method
    // -- executor service allow you to stay focused on tasks that needs to be run rather than creation management
    public static void notmain (String[] args) {


        /* Implementing Single Threaded ExecutorService */
        // setup series of threads
        Thread blue = new Thread(
                ConsuPro::countDown, ThreadColor.ANSI_BLUE.name());


        Thread yellow = new Thread(
                ConsuPro::countDown, ThreadColor.ANSI_CYAN.name());


        Thread red = new Thread(
                ConsuPro::countDown, ThreadColor.ANSI_RED.name());
        
        // MORE OF WORKFLOW STRUCTURE WHERE RED AND YELLOW THREADS NEEDS WAIT - i.e ExecutorService solution
        blue.start();
        try {
            blue.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        yellow.start();
        try {
            yellow.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        red.start();
        try {
            red.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




        // Class produces data - PRoducer
        // consumes data - Consumer

        // IMPORTANT - commented out producer consumer code for ExecutorService lessons
//        MessageRepository mr = new MessageRepository();
//
//        // oass thread names to Thread consturctors
//        Thread reader = new Thread(new MessageReader(mr), "Reader");
//        Thread writer = new Thread(new MessageReader(mr), "Writer"); // will show which thread has lock
//
//        writer.setUncaughtExceptionHandler((thread, exc) -> {
//            System.out.println("Writer had exec");
//            if(reader.isAlive()) {
//                reader.interrupt();
//                System.out.println("Going to interrupt reader");
//            }
//        });
//
//        reader.setUncaughtExceptionHandler((thread, exc) -> {
//            System.out.println("Reader had exec");
//            if(writer.isAlive()) {
//                System.out.println("Interrupt Writer");
//                writer.interrupt();
//            }
//        });
//        // Still get exception but are handling it - need to fix read method -
//        reader.start();
//        writer.start();

    }

    // Lock hold count - hold counrt of lock counts , number times single thread, owner, acquired lock - when thread acquires lock for first time
    //  locks hodl count set to one, if lock is re-entrant and thread, reqcquires the same lock, locks's hold count will get incremented
    //      thread releases lock ? count decremented
    //          lock released when hold count becomes zero - include call to unlock method in inally clause of any code that will acquire lock even if re-entrant

    /*  Lock Summary ------------------- Lock Implementations Review
    //  Explicit Control - - when to acquire and release locks - easier to avoid deadlocks
    //  Timeouts - attempt to acquire lock without blocking indefinitely
    //  Interruptible Locking  - handle interruptions during acquisition more gracefully
    //  Improved Debugging methods let you query number of waiting threads and check if thread holds lock
    // java.util.concurrent - Concurrent Collections Classes for better management concurrent threads
    */

    /*
    Concurrenty - Managing threads with ExecutorService - manage createion and execution of threads
    -- Thread class - rudimentary control over thread, interrupt thread, join it to another, name thread, prioritize it, start each manually
        . complex and error prone - resource contention, thread createion overhead and scalability challenges
        - ExecutorService type - interface - several impls
                 - Abstract execution to level of tasks which need to be run - Thread Pools - reduce cost of creating new threads
                 - Efficient ssScaling - utilize multiple processor cores, Build-in Synch, reduce concurrency related error,
                 - Graceful shutdown - Prevents resource leaks
                 - Scheduled Implementations exist to further help with management workflows.
     */

}

/*
 *
 * Producer Consumer Shoe Fulfillment Warehouse challenge
 * Consumer code should fulfill process orders in FIFO order
 *  Producer should generate orders: order id, shoe type and quantity order - may use record,
 * and send the to shoe warehouse to be processed: product list, public static field, private list or orders, two methods receiveOrder FulfillORder,
 *  + receiveOrder - called by Producer thread - poll or loop indefineietly, check size of list, wait if list has reached maximum capacity,
 *  + fulfillOrder -> Called by Consumer thread: poll list - check list empty, wait in loop, until order added
 * Create min three types ; Order Shoe Warehouse and Main executable class
 * Both : invoke wait and notifyall appropriately
 * Main: create and start single PRoducer thread - gen 10 saes orders: cal receiveOrder on Shoe Warehouse for each
 * -- Confirm 10 orders fulfilled

 *  */

