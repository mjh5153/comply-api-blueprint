package com.init_spring_bean_mvn.demo.multithreadingdeadlockprev;

import java.util.Arrays;
import java.util.concurrent.Executors;

record Participant(String name, String searchingFor, LiveLock maze,
                   int[] startingPosition) {
    Participant {
        maze.moveLocation(startingPosition[0], startingPosition[1], name);
    }
}

    class ParticipantThread extends Thread{
        public final Participant participant;

        public ParticipantThread(Participant participant) {
            super(participant.name());
            this.participant = participant;
        }

        @Override
        public void run() {
            int[] lastSpot = participant.startingPosition();
            LiveLock maze = participant.maze();

            while (true) {
                int[] newSpot = maze.getNextLocation(lastSpot);
                try {
                    Thread.sleep(participant.name().equals("grace") ? 2900 : 500); // Not ideal for
                    // production code, Creating Lock makes game more challenging arguably
                    if (maze.searchCell(participant.searchingFor(), newSpot, lastSpot)) {
                        // true
                        System.out.printf("%s found %s at %s!%n",
                                participant.name(),
                                participant.searchingFor(),
                                Arrays.toString(newSpot));
                        break; // ONce person is found, thread can stop running and can break out of while loop
                    }
                    synchronized (maze) {
                        // adding block on
                        // move location is going to change maze values
                        maze.moveLocation(newSpot[0], newSpot[1], participant.name());
                        lastSpot = new int[]{newSpot[0], newSpot[1]};
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Reasert interrupt - if don't do this thread may not currently interrupt
                    return;
                   // e.printStackTrace();
                }
                System.out.println(participant.name() + " searching " + participant.maze());
            }
        }
    }





public class MazeRunner {
    public static void main(String[] args) {
// examples on internet: maze you navigate through - start at different positions but
        // move within three seconds and can't communicate one another i.e

        // Example : 4x4 maze,
        // One threads actions can effect anothers and prevent the thread from completing an action

        LiveLock maze = new LiveLock();
        Participant adam = new Participant("Adam", "Grace", maze, new int[]{3,3});
        Participant grace = new Participant("Grace", "Adam", maze, new int[]{1,1});
        System.out.println(maze);

        // Use threadpool from Executors to manage threads
        var executor = Executors.newCachedThreadPool();
        var adamsResults = executor.submit(new ParticipantThread(adam)); // returns future
        var graceResults = executor.submit( new ParticipantThread(grace)); // returns future

        while(!adamsResults.isDone() && !graceResults.isDone()) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(adamsResults.isDone()) {
                graceResults.cancel(true);
            } else if(graceResults.isDone()) {
                adamsResults.cancel(true);
            }

            // Expected output - adam and grace on the move and searching for each
            // call shutdown on executor
            executor.shutdown();
        }

        // run and thread working printing that adam is searching, adam moves to first cell
        // thread moving linearally - or way from each at same pace
        // LiveLock in action: threads aren't blocked, continually working, responding to mazes state from another thread in loop that may never end

        // can have without locking - comment out synchronized block

        /* HOW to avoid
        - Avoid having threads constantly checking each other states
         */
        // Use timeouts to prevent  threads from indefinitely waiting for each other
        // Use randomization to break symmetry between threads - For example; two threads trying
        // to acquire same resource, have randomization on which requires resources first

        // execute search by reandomizing search time - have each thread wait a different amount of time

        /* Next: Thread unable to obtain the resource it needs, to execute - Thread STARVATION
        * caused by other cuonccurrent threads being greedy
        * - Result -- App work being done but not all of it*/
    }
}
