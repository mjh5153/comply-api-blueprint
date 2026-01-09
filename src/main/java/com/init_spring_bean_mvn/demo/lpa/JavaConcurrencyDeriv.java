package com.init_spring_bean_mvn.demo.lpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

//RecursiveSumTask
class RecursiveSumTask extends RecursiveTask<Long> {
    private final long[] numbers;

    private final int start;

    private final int end;

    public RecursiveSumTask(long[] numbers, int start, int end, int division) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
        this.division = division;
    }

    private final int division;

    @Override
    protected Long compute() {
        if ((end - start) <= numbers.length / division) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                System.out.println("start  ");
                sum += numbers[i];
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            RecursiveSumTask leftTask = new RecursiveSumTask(numbers, start, mid, division);
            RecursiveSumTask rightTask = new RecursiveSumTask(numbers, mid, end, division);
            leftTask.fork();
            rightTask.fork();
            return leftTask.join() + rightTask.join(); // similar to calling into future instance, no checked exception
            // continue to spawn two recurive tasks until size of tasks within range of division
        }
    }
}

public class JavaConcurrencyDeriv {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int numbersLength = 100_000;
        long[] numbers = new Random().longs(numbersLength, 1, numbersLength).toArray();

        long sum = Arrays.stream(numbers).sum();
        System.out.println("sum = " + sum);
        // split summation tasks into small sets of data
        // WorkStealingThreadPool

        ForkJoinPool threadPool = ForkJoinPool.commonPool(); // starts with small number of threads and added removed (idle ) as needed
        //
        // will work same as Executors but numbers different, run a few times: pool and steal will run a few times
        //ExecutorService threadPool = Executors.newWorkStealingPool(4); // set to number of cpu's on you machine or less
        // ForkJoinPool threadPool = (ForkJoinPool) Executors.newWorkStealingPool(4);

        // Runtime class

        System.out.println("cpus:   = " + Runtime.getRuntime().availableProcessors());
        System.out.println("parallelism  = " + threadPool.getParallelism());
        System.out.println("Pool size = " + threadPool.getPoolSize());
        System.out.println("Steal size = " + threadPool.getStealCount()); // returns total number of completed tasks other than submitter


        // How split up task of 100000 numbers into smaller tasks
        List<Callable<Long>> tasks = new ArrayList<>();
        int taskNo = 10; // Create tasks
        int splitCount = numbersLength / taskNo;
        for (int i = 0; i < taskNo; i++) {
            int start = i* splitCount;
            int end = start + splitCount;
            tasks.add(() -> {
                long tasksum = 0;
                for( int j = start; j < end; j++) {
                    tasksum += (long) numbers[j];
                }
                return tasksum;
            });
        }
        // Submit task to threadpool and wait to complete
        List<Future<Long>> futures = threadPool.invokeAll(tasks);
        long taskSum = 0;
        for(Future<Long> future : futures) {
            taskSum += future.get();
        }

        System.out.println("Thread Pool Sum = " + taskSum);
        RecursiveTask<Long> task = new RecursiveSumTask(numbers, 0, numbersLength, 8);
        long forkJoinSum = threadPool.invoke(task);
        System.out.println("Recursive Pool Sum = " + forkJoinSum);

        threadPool.shutdown();
        // two types of paralleslism: Task par - divides program into smaller tasks that get executed concurrently
        // -- Each tasks runs on separate thread or processor core
        //  2. Data para - processes different parts of same data concurrently - to take advantage of parallel streams - Next
        System.out.println("Thread Pool Sum = " + threadPool.getClass().getName()); // threadPool class is reallly a ForkJoinPool
        // Java's imp of work stealing pool
        // based on fork-join or divide and conqueor algo of computing - breaks down problem into subtaks, executes parallely
        // see docs : /api/java.base/java/util/concurrent/ForkJoinPool.html - Steals work - threading pthis pool won't sit idol
        // if another thread has work in the queue -
        // static commPool() is avialable and approvriate for most applications -  - not explicityly submitted by specificed pool
        // -- Look at methods on ForkJoinPool - submit methods - overloaded ( runnable | callable | ForkJoinTask: abstract base class - thread-like but much lighter weight
        // huge number of taks and subtask may be hoted in pool ; RecurisveAction, Action, Task subclasses exists

        // Used for parallelism, concurrent execution of tasks
        // Each worker thread has own task queue
        // When worker thread finishes its own tasks, and its queue is empty, it can steal tasks from back of other worker threads queues
        // Helps to balance workload amount threads, reduces idle time and optimizes resource usage
    }
}
