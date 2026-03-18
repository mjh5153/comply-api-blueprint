package com.init_spring_bean_mvn.demo.lpa;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Stream;

public class MainLists {
    public static void main(String[] args) {
        var threadMap = new ConcurrentSkipListMap<String, Long>();
        var persons = Stream.generate(Person::new)
                .limit(10000)
                .parallel()
                .peek((p) -> { // not best practice in real life - debugging code
                    var threadName = Thread.currentThread().getName()
                            .replace("ForkJoinPool.commonPool-worker-", "thread+");
                    threadMap.merge(threadName, 1L, Long::sum);
                })
                .toArray(Person[]::new);

        System.out.println(persons);
        // streams pipeline mechanizm may not execute in parallel
        // collect thread names and count total processes each executes

        System.out.println(threadMap);

        long total = 0;
        for(long count : threadMap.values()) {
            total += count;
        }
        System.out.println(total);

        // will see how data is allocated across threads
//        [Lcom.init_spring_bean_mvn.demo.lpa.Person;@3d646c37
//        {main=1920, thread+1=3200, thread+2=2560, thread+3=2320}
//        10000
        // dependant on os and that main thread is also taking data

        /* Next - 345: Building Robust Application: Thread-Safe Lists, Queues, ArrayBlockingQueue */

    }
}
