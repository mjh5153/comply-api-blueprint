package com.init_spring_bean_mvn.demo.recursecopy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RecurseCopy {
    public static void main(String[] args) {

    }

    public static void recurseCopy(Path source, Path target) throws IOException { // code could fail in multiple ways
        Files.copy(source, target);
        if(Files.isDirectory(source)) {
            try (var children = Files.list(source)) {
                children.toList().forEach(p -> {
                    try {
                        RecurseCopy.recurseCopy(
                                p, target.resolve(p.getFileName())
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
