package com.init_spring_bean_mvn.demo.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe utility for writing concurrent request results to file.
 * Integrates concurrent request handling from PostConcurrentRequeststoServer
 * with Spring-based persistence layer.
 * Uses ReentrantLock to ensure thread-safe file operations during concurrent writes.
 */
public class ConcurrentFileWriter {

    private static final Lock _lock = new ReentrantLock();

    /**
     * Thread-safely write content to file
     * Acquires lock before writing to prevent concurrent write conflicts
     *
     * @param filePath Path to file
     * @param content Content to write
     * @throws IOException If file write operation fails
     */
    public static void writeAsync(Path filePath, String content) throws IOException {
        _lock.lock();
        try {
            // Ensure file exists
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            // Thread-safe append
            Files.writeString(filePath, content + System.lineSeparator(),
                    StandardOpenOption.APPEND);
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Thread-safely write multiple lines to file
     *
     * @param filePath Path to file
     * @param lines Lines to write
     * @throws IOException If file write operation fails
     */
    public static void writeAsyncBatch(Path filePath, Iterable<String> lines) throws IOException {
        _lock.lock();
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            for (String line : lines) {
                Files.writeString(filePath, line + System.lineSeparator(),
                        StandardOpenOption.APPEND);
            }
        } finally {
            _lock.unlock();
        }
    }
}


