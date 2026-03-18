package com.init_spring_bean_mvn.demo.fileoperationsexpert;

import com.init_spring_bean_mvn.demo.recursecopy.RecurseCopy;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileOperationsExpert {
    public static void main (String[] args) {
        // rename file with ioa
//        File oldFile = new File("patients.json");
//        File newFile = new File("patients-activity.json");
//        if(oldFile.exists()) {
//            oldFile.renameTo(newFile); // not using output to renameTo method - possible rename failed; user permissions, network connectivity
//            // Path has renameTo file to work with nio and nio.2
//            System.out.println("File renamed successfully!");
//        } else {
//            System.out.println("File does not exist");
//        }

//        Path oldPath = Path.of("patients.json");
//        Path newPath = Path.of("files/patient-activity.json");
////        Path oldPath = oldFile.toPath();
////        Path newPath = newFile.toPath();
//            try {
//                Files.createDirectories(newPath.subpath(0, newPath.getNameCount() - 1));
//                Files.move(oldPath,newPath); // move not going to create sub directories - get nio.file.NoSuchFileException
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        // Patient name renamed back to original
        Path filedir = Path.of("files");
        Path fileRec = Path.of("resources");
        try {
            // Files.move(filedir, fileRec);
//            if(Files.exists(fileRec)) {
//                Files.delete(fileRec); // can't delete a directory if has content
//                // deleteIfExists() method also - same problem
//            }
            recurseDelete(fileRec);
            recurseCopy(filedir, fileRec);// performs shallow copy
            // Files.copy - replaced with recurseCopy
        } catch (IOException e) {
            e.printStackTrace();
        }

        // transferTo // reader interface and buffer
//        try(BufferedReader reader = new BufferedReader(new FileReader("files//patients-activity.json"))
//
//            PrintWriter writer = new PrintWriter("patients-backup.json")) {
//            reader.transferTo(writer);
//                    // files - files.copy- takes advantage of filesystemprovider
//            //transferTo
//            // InputStream != OutputStream Type - examples coming - java.net package
//        } catch (IOException ex){
//            throw new RuntimeException(ex);
//        }
/* Transferring output from console to file output
* Differences between abstract classes and Interfaces
* - Each method is public and abstract - does not contain an constructor
* -Interfaces - Can't do anything on its own  - contains methods without signature body's
* Abstract class - cannot instantiate
* - other classes inherit from it
* - always will be at least one method that hasn’t been completed: provide a guideline (a base class definition) from which derived classes will begin
* Differences: A class can only inherit from one abstract class at a time. However, a class may inherit from multiple interfaces. Interfaces are used to implement the concept of multiple inheritance in object oriented programming.

Because an abstract class is a real class, it can have access modifiers for its functions and properties, like for regular classes. Because an interface is not a class, it does not allow access modifiers.  Everything is considered public (open to everything) by default.

An interface is just an empty signature and does not contain a body (code). An abstract class can provide code, i.e., methods that have to be overridden.

Abstract classes are used when we require classes to share a similar behavior (or methods). However, if we need classes to share method signatures, and not the methods themselves, we should use interfaces.

* Interface: More time to add new methods to an interface. Code has to be rewritten for the interface and for all classes that refer to it include the new methods. It’s easier to add code to an abstract class, because we can use it as the default implementation. The program will still continue to run properly
    Because an interface is empty, it cannot have constants or fields. An abstract class can contain fields and constants definitions
*Interfaces can add to the existing functionality of a class. They are not necessarily integral to the identity of the classes that reference them.
*   --  Abstract classes, on the other hand, provide an identity to the classes that derive from them, as they inherit their behaviors from it
* * * -
* */
        String urlString = "https://api.census.gov/data/20/pop/charagegroups?get=NAME,POP&for=state:*";
        URI uri = URI.create(urlString); // url is always uri - sub cat : uniform resource names
        try ( var urlInputStream = uri.toURL().openStream()) {
            urlInputStream.transferTo(System.out);
        } catch(IOException e) {
            e.printStackTrace();
        }
        Path jsonPath = Path.of("PatientDataByAge.txt");
        try(var reader = new InputStreamReader(uri.toURL().openStream());
            var writer = Files.newBufferedWriter(jsonPath)) {
            reader.transferTo(writer);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try(var reader = new InputStreamReader(uri.toURL().openStream());
//            var writer = Files.newBufferedWriter(jsonPath)) {
            PrintWriter writer  = new PrintWriter("PatientDataByAge.txt")) {
            reader.transferTo(new Writer() { // abstract method so need to implement i.e below
                @Override
                public void write(char[] cbuf, int off, int len) throws IOException {
                    // Neds transformation
                    String jsonString = new String(cbuf, off, len);
                    jsonString = jsonString.replace('[', ' ').trim();
                    jsonString = jsonString.replace("\\]", "");
                    writer.write(jsonString);
                }

                @Override
                public void flush() throws IOException {
                    writer.flush();
                }

                @Override
                public void close() throws IOException {

                }
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }


    public static void recurseCopy(Path source, Path target) throws IOException { // code could fail in multiple ways
        Files.copy(source, target); // can't replace existing directory that has content
        if(Files.isDirectory(source)) {
            try (var children = Files.list(source)) {
                children.toList().forEach(p -> {
                    try {
                        FileOperationsExpert.recurseCopy(
                                p, target.resolve(p.getFileName())
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    public static void recurseDelete(Path target) throws IOException { // code could fail in multiple ways

        if(Files.isDirectory(target)) {
            try (var children = Files.list(target)) {
                children.toList().forEach(p -> {
                    try {
                        FileOperationsExpert.recurseDelete(
                                p
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        Files.delete(target); // if placed before if, would get errors saying directory not empty
    }
}
