package com.init_spring_bean_mvn.demo.consupro;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class SchedulerMain {
        // next ForJoinPool and Streams - Derivatives of ExecutorService
    public static void main(String[] args) {
        var dtf = DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.MEDIUM,
                FormatStyle.LONG
        );

        Callable<ZonedDateTime> waitThenDoIt = () -> {
            ZonedDateTime zdt = null;
            try {
                TimeUnit.SECONDS.sleep(2);
                zdt = ZonedDateTime.now();
            } catch (InterruptedException e) {
                throw new InterruptedException();
            }
            return zdt;
        };

        // Create fixed thread pool to execute
        var threadPooll = Executors.newFixedThreadPool(2);
        List<Callable<ZonedDateTime>> list = Collections.nCopies(4, waitThenDoIt);
// Threadpoolsmanager and os determine how best to manage threads
        // Use SchedulerService to manage yourself
        try {
            List<Future<ZonedDateTime>> futureList = threadPooll.invokeAll(list);
            for(Future<ZonedDateTime> result : futureList) {
                try {
                    System.out.println(result.get(1, TimeUnit.SECONDS).format(dtf));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadPooll.shutdown();
        }

        Runnable dateTask = () -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(" a " + ZonedDateTime.now().format(dtf));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        // ScheduleAtFixedRate method: set delay to exact time
        System.out.println(ZonedDateTime.now().format(dtf));
        // ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(4); // argu is number of threads

        // show second set of 4 tasks created, each executed at different delays in 2 multiples
//        for(int i=0; i < 4; i++) {
//            executor.schedule(() -> System.out.println(ZonedDateTime.now().format(dtf)), 2 * (i+1), TimeUnit.SECONDS);
//        }
       // executor.schedule(() -> System.out.println(ZonedDateTime.now().format(dtf)), 2, TimeUnit.SECONDS);

        // has additional delay after initial delay parameter
        // scheduleWithFixedDelay returns ScheduledFuture - see in inferred type
        // var st = executor.scheduleWithFixedDelay(() -> System.out.println(ZonedDateTime.now().format(dtf)), 2, 2, TimeUnit.SECONDS);
        var st = executor.scheduleAtFixedRate(dateTask, 2, 2, TimeUnit.SECONDS);

        var st2 = executor.scheduleAtFixedRate(() -> System.out.println(" b " + ZonedDateTime.now().format(dtf)), 2, 2, TimeUnit.SECONDS);
        // still not scheduled every 2 seconds - scheduleManger scheduling and task takes more time
        // another thread could not pick up a task
        // Next task gets shceduled could be before previous completes and queued up
        // delayed rate method ? not scheduled until previous completes plus delay
        // can now check each stage of task
        // shutdown after certain time
        // Schedules next task after first finishes - time not sure of


        long time = System.currentTimeMillis();
        while(!st.isDone()) {
            try{
                TimeUnit.SECONDS.sleep(2);
                if((System.currentTimeMillis() - time) / 1000 > 10) {
                    st.cancel(true);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        executor.shutdown(); // shutting down before task ever hs changce to run, want to get data back
        //
    }

}
