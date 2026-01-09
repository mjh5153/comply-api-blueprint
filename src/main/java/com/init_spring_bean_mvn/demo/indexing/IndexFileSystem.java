package com.init_spring_bean_mvn.demo.indexing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexFileSystem {
    public static void main (String[] args) {



        Path deepestFolder = Path.of("public", "assets", "icons");
        try {
            Files.createDirectories(deepestFolder);
            generateIndexFile(deepestFolder.getName(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // create copies
        for(int index=1; index<= deepestFolder.getNameCount(); index++) {
            Path indexedPath = deepestFolder.subpath(0, index).resolve("index.txt");
            Path backupPath = deepestFolder.subpath(0, index).resolve("indexCopy.txt");
            try {
                Files.copy(indexedPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // copy doesn't override existing file unless we specify i.e StandardCopyOption.REPLACE_EXISTING parameter

        // generate index..txt again
        try {
            generateIndexFile(deepestFolder.getName(0));

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        // private final static long serialVersionUID = 1L;
        // Serializable - What constitutes an Incompatible Change - Changing declared type of primitive field: int to long ( way primities are written (taken in certain bytes width )
        // deleting fields, non-static field to static or non-transient to transient == deleting field to class
        // moving class in hierarchy, change write and read after you serialized and other - see link
        // NOT all changes will invalidate - compatible: Adding fields - new field gets inititalized to default type, writeObject and readObject methods, Change access to field: public, package, protected,
        // change field to static to nonstatic or transient - full docs : /compatible-changes
        // serialization process - info of object type, field, and super type

        // Solution Start - add a field and set it to field value: BUT new field is wrong - set to zero, deserializeation code doesn't call constructor on class being serialized ( new code will not get updated )
        // make another change - ArrayList to LinkedList: same result - reconstructed object works with different list type

        // data stored in bytes, use version number - test value of localisted serializedVer == 1 ) ? stream.readInt() : stream.readLong()
        // why not change serialVersionUID ? Incompatible error
        // invalidate session data ? implement own versioning ( doesn't fix all errors - need transient field
    }

    public static void generateIndexFile(Path start) throws IOException {
        Path indexFile = start.resolve("index.txt");
        try (Stream<Path> contents = Files.find(start, Integer.MAX_VALUE, (path, attr) -> true)) { // includes absolute path of first item
            String fileContents = contents.map((path) -> path.toAbsolutePath().toString())
                    .collect(Collectors.joining(
                            System.lineSeparator(), "Directory Contents: " + System.lineSeparator(),
                            System.lineSeparator() + "Generated: " + LocalDateTime.now()

                    ));
            Files.writeString(indexFile, fileContents, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        }
                // using find instead of walk because beahvior could change
        // Needed to generate index.txt for each directory level
        try(Stream<Path> contents = Files.list(start)) {
            contents.filter(Files::isDirectory)
                    .toList()
                    .forEach(dir -> {
                        try {
                            generateIndexFile(dir);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
