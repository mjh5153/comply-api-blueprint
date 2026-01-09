package com.init_spring_bean_mvn.demo.multithreadingdeadlockprev;

record Delegate(String name, int enrolledYear, int delegateId)
    implements Comparable<Delegate> {
        @Override
        public int compareTo(Delegate o) {
            return this.delegateId - o.delegateId;
        }

        // Delegate class
    // not atomic
    }


public class AtomicClasses {

    /*
        Loose ends related to concurrent applications in
    java.util.concurrent.atomic classes lock free thread safe programming in single variables
    pros: significantly improve performance in concurrent applications,
    i.e especially in high throughput applications

    */

    // AtomicBoolean, AtomicInt, AtomicLong, AtomicReference

    // !IMPORTANT for concurrent applications

    public static void main(String[] args) {
        // create id generator for Delegate
        // name Tim
        // call idgenerator.getNextId()
        // create new student
        // add to set
        // return student
    }

    // create new executor
    // executor . invokeall( collections.nCopies(

    // kick off 10 threads but alternating between 7 and 10 students
    // nextInt not atomic and memory inconsistencies,
    // generator generated thread students but 3 might gain same id

    // Wrap in for loop to see more clearly
    // comment out code printing each student

    // Bad

    // Fix by synchronizing getNext method on Delegate - now all run cases has 10 stud
    // class calls AtomicInteger instead of prmitorive integer


    // use atomticInteger class for instanciating Delegate Class
    // can use getNext Method on aomtomicInteger - Thread Safe equates to Atomic

    // Results

    //2. Classes exist
    // a. Timer class: around since 1.3 - single threaded class scheduler

    // Create new class to demonstrate, local variable with type Timer, no args constructor
    // select java.util.
    // New background thread created when timer instantiated, need to override Timer class run method,
    // (dt formatter
    // Next call timer.sheduleAtFixedRate(task, 0-delay, 2000)
    // call thread sleep
    // cancel timer instance after

    // Run - thread name difference than default thread names

    // Compare to Executors.newSingleThreadedScheduledExecutor - see video 10 minutes for code

    // Need to execute cancel on timer to shutdown

    // timer docs say newSingleThreadedScheduledExecutor is same as Timer ( timer for one off task )
    // compared to 

}
