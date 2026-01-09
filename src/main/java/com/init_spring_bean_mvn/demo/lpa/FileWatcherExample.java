package com.init_spring_bean_mvn.demo.lpa;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileWatcherExample {


    public static void main(String[] args) {
        WatchService ws = null; // lets a program listen for an event on file system
        try {
            ws = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path directory = Paths.get(".");
        WatchKey wk = null; // token - watchable objects registration with a watch service
        // event detected ? watchKey signalled a special state and can be polled
        // watchkey gets queued on watchservice - can be retrieved by invoking service
        // Can be revoked - Watchkey returns to ready state
        // events deteceted while key is in signalled state they are addto watchkey

        // watch key has list events that occurred while watch key is in signalled stated
        // WatchEvent object represents a specific change to watchable object
        try {
            wk = directory.register(ws,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        boolean keepGoing = true;
        // no output - polling for events in while loop
        while(keepGoing) {
            try {
                wk = ws.take(); // could also use poll method
                // ququeue? method gets element removing from queue
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            List<WatchEvent<?>> events = wk.pollEvents();

            for(WatchEvent<?> event : events) { // loop through all events from pollEvents return
                Path context = (Path) event.context();
                System.out.printf("Event type: %s - Context: %s%n", event.kind(), context);
                if(context.getFileName().toString().equals("Testing.txt") && event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("Shutting down watch service");
                    try {
                        ws.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    keepGoing = false;
                    break;
                }
            }
            wk.reset(); // reset watchkey - all data being processed

            /* Can use on dbs, message queues, shared memory regions and other resources as well */
        }
    }
}
