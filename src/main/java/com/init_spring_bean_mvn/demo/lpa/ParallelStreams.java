package com.init_spring_bean_mvn.demo.lpa;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

record Person(String fn, String ln, int age) {
    private final static String[] firsts =
            {"Able", "Bob", "Charlie", "Donna"};

    private final static String[] lasts =
            {"Norton", "OHare", "Hess", "Heredia"};

    public Person() {
        this(firsts[random.nextInt(firsts.length)],
                lasts[random.nextInt(lasts.length)],
                random.nextInt(18, 100));
    }

    private final static Random random = new Random();

    @Override
    public String toString() {
        return "%s, %s (%d)".formatted(ln, fn, age);
    }
}
public class ParallelStreams {

    // java 8, serial streams - perform operations on collections in parallesll - speding upd ata processing
    ///  advantages - improved perf on multi-core cpus
    //  simpliefied code for concurrent processing
    //  automatic workload distribution amount available threads
    //  Do work of splitting up tasks implicitely
    // HOW TO DO BENCHMARK TESTING
    // Results: Serial faster with smaller array - overhead associated with parallel streams: summation and average
    // -- may not see benefit until arrays get large enough,
    // -- need to synchronize operations to ensure results are correct - add overhead, especially in small arrays
    // -- Java's optimizations may not even use one
    // -- Order requirement may impact how parallel streams performed - NEXT: deep dive into Parallel Streams: Ordering, Reducing and Collecting in Java

    /* Sections Remaining in Concurrency
    * 343: Deep Dive into Parallel Sstreams - Not alwasy straight forward decision - good reasons not to use
    * - Order Matters - possible but not performant because of synch process and time to sort
    *
    * // Can be used with synchronized wrapper which you can get from Collections helper class -
    * // provide thread-safe option
    * // New code: use concurrent collections
    * 1. Lists_ two concurrent collection choices: ConcurrentLinkedQueue - frequent insertions and removals as producer-concumer or task scheduling
    * 2. CopyOnWriteArrayList: read-heav workload, infrequent modifications: config managment or readonly views
    * B. Array: Concurrent List options above useded - ArrayBlockQueue - Fixed-sized queue - blocks; 1. poll or remove element from empty queue,
    * // 2. Offer ? add an element to full qeue - FIFO queue
    * 344: Thread Sfety - Synchronized and Concurrent Collections
    ** 345: Building Robust Apps: Thread-Safe Lists, Queues, And ArrayBlockingQueue
    * 346: Implementing Consumer Tasks: Mastering ArrayBlockingQueue in Java Concurrency
    * 347: Thread Contention: Understanding and Avoiding Deadlock, livelock, Starvation*/
    public static void main(String[] args) {

        var persons = Stream.generate(Person::new)
                        .limit(10)
                                .sorted(Comparator.comparing(Person::ln))
                                        .toArray();

        for(var person : persons) {
            System.out.println(person);
        }

        System.out.println("----------------------");

        Stream.generate(Person::new)
                .limit(10) // infinite if no limit
                .parallel()// persons won't be sorted
                // .sorted(Comparator.comparing(Person::ln)) // redundant sorted call and not depend on sort order for parallel streams and replace with terminal operation
                //.forEach(System.out::println); // opersation does not guarantee to respect encounter order of stream - sacrifice benefit of parallelism
                .forEachOrdered(System.out::println); // ordered same as stream order not sorted
        // increase overhead of parallel processing - need metrics of pipeline with different dataset sizes as in last video
        // Cover reduction and collection

        // Reduction

        int sum = IntStream.range(1, 101) //exclusive upperbound
                .parallel()
                .reduce(0, Integer::sum);
        System.out.println("sum " + sum);

        // Reduction using strings
        String humptyDumpty = """
                Humpty Dumpty sat on a wall
                All the kings
                couldn't put humpty toogether again 
                """;

        System.out.println("--------------------");
        var words = new Scanner(humptyDumpty).tokens().toList();
        words.forEach(System.out::println);
        System.out.println("--------------------");

//        var backTogetherAgain = words.stream()
//                .parallel()
//                .reduce(new StringJoiner(" "), StringJoiner::add, StringJoiner::merge);// identity, acc, combiner

        // Thread safe
//        var backTogetherAgainCollect = words.parallelStream()
//                .collect(Collectors.joining(" "));

        var backTogetherAgainReduce = words.parallelStream() // output different spaces and different numbers
                .reduce("", (s1, s2) -> s1.concat(s2).concat(" ")); // run reduction in parallel
        // each thread will use identity to  create a new instance, start processing parts of data using acc and used to join other threads results

        // parallel stream works correctly - parallel
        // Collection getting map back
//        Map<String, Long> lastNameCounts = Stream.generate(Person::new)
//                .limit(10000)
//                .parallel()
//                .collect(Collectors.groupingBy(
//                        Person::ln,
//                        Collectors.counting()
//                ));

        Map<String, Long> lastNameCounts = Stream.generate(Person::new)
                .limit(10000)
                .parallel()
                .collect(Collectors.groupingByConcurrent( // change to this
                        Person::ln,
                        Collectors.counting()
                ));
        lastNameCounts.entrySet().forEach(System.out::println); // last name printed with counts of each for each stream

        // example : change state of instance not part of pipeline - side effects
        // setupt treeMap mmanually

        // stringJoiner not thread safe and errors caused by interweaving threads
        // serial stream
        var backTogetherAgainP = words.parallelStream()
                .reduce(new StringJoiner(" "), StringJoiner::add, StringJoiner::merge);// identity, acc, combiner
        System.out.println(backTogetherAgainP);
        // grouping by with parallel streams - merge method of combing multiple threads maps is very costly
        ///  groupingByConcurrent method is much more efficient - returns concurrent implementation of a Map: explained in next video
        long total = 0;
                for(long count : lastNameCounts.values()) {
                    total += count;
                }
                System.out.println("Total = " + total);
        System.out.println(lastNameCounts.getClass().getName()); // prints out HashMap as type - isn't thread-safe: lacks synchronization
        // other collection
        // two or more threads modifiying the asame hashmap can cause inifitie loops, inconsistant data
        // no guarantees of momory consistency, while iterating: concurrent modification exception when they are made or unpredictable behavior during the iteration process
        // java/util/HashMap.html - implementation is not synchronized - multiple htreads accessa hash map concurrently and at least one of threads
        // modifieds map structurally, must be synchronized externally
        // Changing value of assocated eky that na instance already contains is not a structual modification -
        // accoplished by synchronizing some object that naturally encapsulates the map -
        // wrront to be write a program that dependened on this exception for its correctness

        // Each parallel thread will have its own copy of hashmap instance
        // code merges threads together by key - maps: don't code parallel streams in way with groupingBy -
        // change to groupByConcurrent - now have ConcurrentHashMap type returned
        // Add or change data in map

        //Code will be unpredeiclble - ConcurrentModiferException
        // var lastCounts = new TreeMap<String, Long> ();
        var lastCounts = Collections.synchronizedMap(new TreeMap<String, Long> ()); // last names sorted and
        // Total has 10000! Specicalied type call SynchronizedMap -
        // running multiple times - always get right total and no exceptions

        //var lastCounts = new ConcurrentSkipListMap<String, Long>(); // One thread safe option

        // Usecease


        Stream.generate(Person::new)
                .limit(10000)
                .parallel()
                .forEach((person) -> lastCounts.merge(person.ln(), 1L, Long::sum));

        System.out.println(lastNameCounts.getClass().getName());

        total = 0;
        for(long count : lastCounts.values()) {
            total += count;
        }
        System.out.println(lastCounts.getClass().getName());
        // output - lastnames in order but total is less than 10000
        // many problems: concurrent modification problem and memory consistency errors - Non thread safe classes in multithreaded operation such as Parallel Streams

        // 1. Slide special map types - HashMap, TreeMap: not thread safe
        // Stream pipeline - groupingBy -> HashMap
        //  different map - specificy own in .collect(TreeMap) for example
        // Thread safe - ConcurrentHashMap,
        // Blocking - Collections$SynchronizedMap  - Other Threads will block
        // Concurrentvs Synchronized Wrapper - use locks - protect collection from concurrent access - single lock used to synchornize to entire map
        // Concurrent: mrore effiecient - fine-grained locking or non-blocking algorithems to enable to enable safe concurrent access without need
        // for heavy handed locking, meaning synchronized or single access locks

        // concurrent collections are recommended over synchronized collections in morst scenarios
        // maps used in parrallel streams to collect and aggregate data with large datasets
        // 2. Arrays and lists - Collects data in own array or list using erminal operations with Parallel Streams
        // Each parallel process collects elements first in own array or list - final part of process combines each threads data into single source


    }


    public static void oldmain(String[] args) {
        //int numbersLength = 100_000;
        int numbersLength = 100_000_000;
        long[] numbers = new Random().longs(numbersLength, 1, numbersLength).toArray();
        long start = System.nanoTime();
        long delta = 0;
        int iterations = 25; // calculations will take longer so lower from 100

        for (int i = 0; i < iterations; i++) {


            double averageSerial = Arrays.stream(numbers).average().orElseThrow();
            long elapsedSerial = System.nanoTime() - start;
            // System.out.printf("Ave = %.2f , elapsed = %d nanos or %.2f ms%n%n", averageSerial, elapsedSerial, elapsedSerial / 1000000.0);

            start = System.nanoTime();
            double averageSerialP = Arrays.stream(numbers).parallel().average().orElseThrow(); // java will use ForkJoinPool commonPool implicitely
            long elapsedSerialP = System.nanoTime() - start;
            // System.out.printf("Ave = %.2f , elapsed = %d nanos or %.2f ms%n%n", averageSerialP, elapsedSerialP, elapsedSerialP / 1000000.0);
            // elapsed time will be different based on os
            delta += (elapsedSerial - elapsedSerialP);

            System.out.printf("Parallel is [%d] nanos or [%.2f] ms faster on average %n", delta / iterations, delta / 1000000.0 / iterations);
            // Two ways to make stream parallel - //
            // 1. Include parallel intermediate operation: results are unexpected - not always faster, execute multople times to get average time elapsed time
        }
    }
}
