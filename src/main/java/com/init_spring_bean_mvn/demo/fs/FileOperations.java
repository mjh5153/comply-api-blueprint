package com.init_spring_bean_mvn.demo.fs;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class FileOperations {
/*What happens when opening a file from Java application - 20 videos remaining */
    // entry made in file table or file control block table of OS to track file
    // File may be locked
    // File may be buffered by OS - memory allocated to cache all or part of file contents; optimize read and write operations
//--------------
    // Many methods in java make opening a file look like instantiating any other object
    // Open call not needed
    // Closing File = memory space free up storing file related data
        // Allows other precesses to access file
    // Implement AutoCloseable interface - helps with closing resources

    //IO, NIO, NIO.2 -
    // IO File InputStream, FileInputStream, OutputStream, BufferInput Streams
    // NIO - Buffer, Channel
    // NIO - Interface Path, java 1.7 - Files, FileSystem, FileSystems, Paths - makes easier delegating work to native system

    // 1. Java Exceptions
    // 2.

//    public static void main(String[] args) throws IOException { // most user friendly
//        String filename = "testing.csv";
//        Path path = Paths.get(filename);
//        List<String> lines = Files.readAllLines(path); // throws checked exception which anticipates issue occurring
//        // FileNotFoundException
//        // wrap statement or declare throws and specifying type
//    }
// Unchecked exeption is instance of RuntimeException or one of its classes - RuntimeException.html in oracle web docs

    // File System Concepts - Directory is file system container for other directories or files
    // 2. Path is directory or filename and may include info about parent directories or folders
    // 3. root directory is top-level directory in file system
    // 4. current working directory = directory in current process is working in or running from
    // 5. absolute path = root ( start with / or C:\ for windows
    // file classes en/java/javase/17/docs/api/java.base/java/nio/File.html#method-summary - gives many more options for reading files; readAllBytes, readString, write() methods overloading

    // Path - interface - Paths, Files - static methods , static methods return Path instance
    // File - get instance with File constructor and execute method - behavior is member on instance itself
    // NIO.2 - Need Path- factory method on Path, use static method on path
    public static void main(String[] args) { // most user friendly
        // NIO - path parameter and File static - anych file ops, File locking, region of locking, file metadata retrieval, symbolic link manipulation, syst notifications - changes on path can be made watchable to registered services
        // NIO2 - better performing, non blocking, multople threads, manage emory by ready into buffers through a FileChannel
                // read from and write to multiple buffers in single operation

        Path pathMastery = Path.of(""); // curr working directory -  ./demo
        System.out.println("cwd = " + pathMastery.toAbsolutePath());

        // reading data from file - readAllLines() - recommended way to read write file
        try(FileReader reader = new FileReader("files/ScanResults.csv")) { // has default butter size - characters in memory space while read from file
            // improve in data transfer
            int data;
            while((data = reader.read()) != -1) { // expensive if disk read - time and resource
                // reduce number of disk reads
                //cast data to char
                System.out.println((char) data);//
            }
        } catch(IOException exc) {
            exc.printStackTrace();
        }
        // InputStream - System.in -
        //  FileInputStream : read - inefficient because it's a hard disk read - need to wrap in BufferInputStream
        //  Can't be used in stream pipeline without first transforming it
        // Readers - bridge from byte streams to character streams
        // BufferedReader - large buffer than FileReader (character data or buffered array ) - Java states buffer large enough
        //  convenience methods from reading lines of test
        // NIO.2 reduces need to use these classes
        //  Benefits of buffer reads lines as opposed to FileReader

        System.out.println("-------------Buffer and File Reader-----------");
        try(BufferedReader bfr = new BufferedReader(new FileReader("files/ScanResults.csv"))) {
//            String line;
//            while((line = bfr.readLine()) != null) {
//                System.out.println(line);
//            }
            // Replace with
            bfr.lines().forEach((l) -> System.out.println(l));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Stream<Path> paths = Files.list(pathMastery)) { // still need catch clauses
            paths
                    .map(FileOperations::listDir) // needed to use Static listDir on this class FileOperations
                    .forEach(System.out::println);
            // prints datetime of file in Z - UTC date and time
        } // returns Stream DS of type path
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        // directory stream remains open until stream is exectued
            // readAllLines are both opening and closing automatically
            // always use try with resources for Streams
        /* Can do same operations with File instance as Files and path */
        useFile("files/testing.csv");
        usePath("files/pathfile.csv");

        // Flder contains files that dont exist? Files Class method - createDirectories()
        Path path = Path.of("files/testing.txt");
        printPathInfo(path);
        logStatement(path);
        System.out.println("curren wd = " + new File("").getAbsolutePath());
        String filename = "src/main/java/com/init_spring_bean_mvn/demo/fs/files/testing.csv"; // relative path used
        extraInfo(path);


        File file = new File("", filename);
        File file2 = new File(".", filename); // printed in root path - redundant name elements
        File file3 = new File(new File("").getAbsolutePath());
//        Path path = Paths.get(filename);
//        try {
//            List<String> lines = Files.readAllLines(path); // throws checked exception which anticipates issue occurring
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }// can use finally clause
        // FileNotFoundException
        // wrap statement or declare throws and specifying type
        // will throw error because file doesn't exist
        // linux command or mac - pwd

//        testFile2(null);
//        testFile(filename);

        Path path1  = Paths.get("fs/files/testing.csv"); // overloaded get methods
        System.out.println("I'm good to go " + path1);

        // 300. Practical File Operations in Java: Create, Delete, Write with File/Path
        //
        // 301 Next Video show different in NIO and legacy - Exploring Path Methods and File System Interfactions in Java NIO.2

        // File.listRoots() returns array of file instances
        for(File f : File.listRoots()) {
            System.out.println("I'm good to go " + f);
        }

        // File file = new File(filename);
        if(!file.exists()) {
            System.out.println("doesn't exist");
            // file handle != file resource - File Handle ref to file by os - abstract representation of - no data,
            // File Resource stored on disk - accessed by os and applications
            return;
        }
        System.out.println("I'm good to go ");

        System.out.println("------------------ ");
        // can use filter to just list files and not directoies
        try (Stream<Path> paths = Files.find(pathMastery, Integer.MAX_VALUE, (p, attr) ->
                // Files.isRegularFile(p)
                attr.isRegularFile() && attr.size() > 300 // all files > 300 bytes in size and All paths - search for any files modified in past week or meet certain size
                // could use attributes of path available via attr - already associated with current path in stream
        )) {
            // bi: takes 2 arguments, predicate: returns a boolean
            // Get same output as list: diff: walk is recurisve if specify second parameter::depth >1, still need catch clauses
            paths
                   // Can now be revmoed with find and lambda .filter(Files::isRegularFile) // Files has find - like walk but can pass condition as arg - more efficient
                    .map(FileOperations::listDir) // needed to use Static listDir on this class FileOperations
                    .forEach(System.out::println);
            // prints datetime of file in Z - UTC date and time
        } // returns Stream DS of type path
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Stream<Path> paths = Files.walk(pathMastery, 2)) { // Get same output as list: diff: walk is recurisve if specify second parameter::depth >1, still need catch clauses
            paths
                    .filter(Files::isRegularFile) // Files has find - like walk but can pass condition as arg - more efficient
                    .map(FileOperations::listDir) // needed to use Static listDir on this class FileOperations
                    .forEach(System.out::println);
            // prints datetime of file in Z - UTC date and time
        } // returns Stream DS of type path
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Directory Stream "); // more efficient File handling- Java nio.2 - provides iterable for directory's

        // could use path creation factory but will use path.resolve()
        path = path.resolve(".idea");
        System.out.println("--------Directory Stream---------- "); // more efficient File handling- Java nio.2 - provides iterable for directory's
        try( var dirs = Files.newDirectoryStream(path, p-> p.getFileName().toString().endsWith(".xml") && Files.isRegularFile(p) && Files.size(p) > 1000)) {
            // Elements return by iterator in no specific order, i.e
            // There is a walkFileTree method
            // newDirectoryStream: takes path paramter, iterable : forEach to get usually directory listing stream
            // overloaded version with string or glob- limited pattern language like regex but simpler - on oracle site which has patterns available
            // additional overloaded which takes function for lambda and returns a boolean and takes path
            dirs.forEach(d -> System.out.println(FileOperations.listDir(d)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // walk - short for walkFileTree
        // depth first - recursive visit al child elements before any siblings - see image in 303
        // bredtch first: neighbor
        // checanizm to accumultae info about all children - FileVisotr interface- entry points in walk
        // - before fter visiting directory, visiting file point and failer point
        // FileVistor Interface and SimpleFileVisitor - overriden- method args as data you can work with in the walk
        // - access to current path- directory or file
        // - return type - same enum value as in FileVisiResult enum- access to basic attrs on both visitFile preVisitDirectory methods

        //Immplemenation - add class

        System.out.println("-----------------------WalkFileTree and FileVisitor override---------");
        // Can keep track of state doing the walk i.e next in video
        Path startingPath = Path.of("."); // use .. for parent directory
        FileVisitor<Path> statsVisitor = new StatsVisitor(1);

        // need to override methods on SimpleFileVisitor to have method do anything
        try {
            // Depth first search
            // visit types that are files
            // Print directory names; pre or post visit of directory
            // KEEP TRACK OF STATE during walk
            Files.walkFileTree(startingPath, statsVisitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    private static class StatsVisitor extends SimpleFileVisitor<Path> { // can use default no args constructor in impl in main

        private int level;
        private Path initialPath = null;
        private Map<Path, Long> folderSizes = new LinkedHashMap<>(); // for maintaining insertion order
        private int initialCount;
        private int printLevel;

        private StatsVisitor(int printLevel) {
            this.printLevel = printLevel;
        }

        // increment when enter directory and decememnt when leave
        @Override // can auto generate boiler plate with ctrl o
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Objects.requireNonNull(file);
            Objects.requireNonNull(attrs);
            System.out.println(file.getFileName()); // prints files, next will post print and visit directories with preVisitDirectory override
            folderSizes.merge(file.getParent(), 0L, (o, l) -> o += attrs.size()); // bi function- old value, , new value// accum sizes to parent
            // didn't need merge, will always have 0 because of put and put this code in previsit method
            // could use computeIfPresent()
            // sizes not propogated to parents yet - include code in postVisit
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

            if(initialPath == null) {
                initialPath = dir;
                initialCount = dir.getNameCount();
            } else {
                int relativeLevel = dir.getNameCount() - initialCount;
                if(relativeLevel == 1) {
                    folderSizes.clear(); // large file tree ? more efficient to keep track of one folder at a time
                }
                folderSizes.put(dir, 0L);
            }
            Objects.requireNonNull(dir);
            Objects.requireNonNull(attrs);
            // indented - previsit method executed on previous path and same level of parent folder not files
            // Fix - add one to level in repeat
            // System.out.println("\t".repeat(level + 1) + dir.getFileName()); // prints files, next will post print and visit directories with preVisitDirectory override
            return FileVisitResult.CONTINUE;
            // better use case : sum of file size and want to include all parents childs size
        }

        // SimpleFileVisitor is a way to use only a few methods in FileVisitor
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Objects.requireNonNull(dir);
            if(dir.equals(initialPath)) {
                return FileVisitResult.TERMINATE; // on enum Or CONTINUE could work
            }

            int relativeLevel = dir.getNameCount() - initialCount;
            if(relativeLevel == 1) {


            folderSizes.forEach((key, value) -> {
                int level = key.getNameCount() - initialCount - 1;
                if (level < printLevel) {


                    System.out.printf("%s[%s] - %,d bytes %n", "\t".repeat(level), key.getFileName(), value);
                }
            });
            } else {
                long folderSize = folderSizes.get(dir);
                folderSizes.merge(dir.getParent(), 0L, (o, n) -> o += folderSize);
            }
            return FileVisitResult.CONTINUE;
        }
    }
    
    private static String listDir(Path path) {
        try {
            boolean isDir = Files.isDirectory(path);
            FileTime dateField = Files.getLastModifiedTime(path);
            LocalDateTime modDT = LocalDateTime.ofInstant(
                    dateField.toInstant(), ZoneId.systemDefault());
            return "%tD %tT %-5s %12s %s".formatted(modDT, modDT, (isDir ? "<DIR>" : ""), (isDir ? "" : Files.size(path)), path); // need to get size with ternary if the file type is a directory

             // now in main class add .map operation for stream paths: Added in main
        } catch (IOException e) {
            System.out.println(" something wrong" + path);
            // many ways to get infor about File
            return path.toString();
        }
    }
    private static void printPathInfo(Path path) {
        System.out.println("Path:  " + path);
        System.out.println("fileName =  " + path.getFileName());
        System.out.println("parent =  " + path.getParent());
        Path absolutePath = path.toAbsolutePath();
        System.out.println("Absolute Path: =" + absolutePath);
        System.out.println("Absolute Path root = " + absolutePath.getRoot());
        System.out.println("Root = " + path.getRoot());
        System.out.println("isAbsolute =  " + path.isAbsolute());

//        int index = 1;
//        var it = path.toAbsolutePath().iterator();
//        while(it.hasNext()) {
//            System.out.println(". ".repeat(index++ + " " + absolutePath.getName(i)));
//        }

        // Files.getAttribute - returns data as object which means you need to cast it to second column Class Name i.e FileTime, Long, Boolean
        // Can get a couple of these fields by specifically named methods on Files class
        int pathParts = absolutePath.getNameCount();
        for(int i= 0; i < pathParts; i++) {
            System.out.println(". ".repeat(i + 1) + " " + absolutePath.getName(i));
        }
    }

    private static void logStatement(Path path) {
        try {
            Path parent = path.getParent();
            if(!Files.exists(parent)) {
                    // Files.createDirectory(parent);
                Files.createDirectories(parent);
            }
            // CREATES AND APPENDS IN ONE LINE
            Files.writeString(path, Instant.now() + ": create compliance scan result\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void extraInfo(Path path) {
        try {
            var atts = Files.readAttributes(path, "*");
            // returns map - use forEach on entrySet
            atts.entrySet().forEach(System.out::println);
            System.out.println(Files.probeContentType(path)); // returns; content-type
            // --live data returned
            //lastAccessTime=2025-11-21T22:27:15.494751187Z
            //lastModifiedTime=2025-11-21T22:28:29.346590792Z
            //size=236
            //creationTime=2025-11-21T22:22:48Z
            //isSymbolicLink=false
            //isRegularFile=true
            //fileKey=(dev=1000004,ino=12964062638)
            //isOther=false
            //isDirectory=false
            //text/plain

            // next video - 302. Directory Listing Mastery: list, walk and find
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void useFile(String fileName) {

        // instanace file - legacy class
        File file = new File(fileName);
        boolean fileExists = file.exists();

        System.out.printf("File '%s' %s%n", fileName, fileExists ? "exists." : "does not exist.");

        if (fileExists) {
            fileExists = !file.delete();
        }

        if(!fileExists) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Created File: " + fileName);
            if(file.canWrite()) {
                System.out.println("Would write to file");
            }
        }
    }

    private static void usePath(String fileName) {

        // instanace file - legacy class
        Path path = Path.of(fileName); // jdk 11 Path.of(fileName)
        boolean fileExists = Files.exists(path);

        System.out.printf("File '%s' %s%n", fileName, fileExists ? "exists." : "does not exist.");

        if (fileExists) {
            try {
                Files.delete(path);
//                fileExists = !Files.delete(path);
                fileExists = false;
            } catch (IOException e) {
                e.printStackTrace(); // throws execption so doesn't return boolean - returns void
            }
        }

        if(!fileExists) {
            try {
                Files.createFile(path); // doesn't exist of Files
                // doesn't return boolean as createNewFile
                // LBYL approach
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Created File: " + fileName);
            if(Files.isWritable(path)) { // canWrite method not on files
                System.out.println("Would write to file");
                try {
                    Files.writeString(path, """
                            PRIVACY NOTICE
                            Last Updated: January 2025
                            
                            Acme Innovations (“we”, “our”, “us”) collects certain personal information when you use our website and mobile applications.
                            
                            1. INFORMATION WE COLLECT
                            We collect identifiers such as email address, device identifiers, and browsing activity on our website.
                            We also collect approximate geolocation for analytics purposes.
                            
                            2. HOW WE USE INFORMATION
                            We use personal information for internal analytics, improving our services, and advertising campaigns.
                            
                            3. SHARING INFORMATION
                            We may share personal information with our advertising partners to deliver personalized ads.
                            We do not provide a “Do Not Sell or Share My Personal Information” link on this site at this time.
                            
                            4. RIGHTS
                            You may request access or deletion of your information by emailing privacy@acmeinnovations.example.
                            
                            5. INTERNATIONAL TRANSFERS
                            We may transfer personal data outside your region to service providers.
                            (We do not specify safeguards, adequacy decisions, or transfer mechanisms.)
                            
                            6. CHILDREN
                            Our services are not intended for children under 13.
                            
                            7. CONTACT US
                            Contact our privacy team at privacy@acmeinnovations.example.
                            (No DPO specified.)
                            """);
                    Files.readAllLines(path).forEach(System.out::println);
                } catch (IOException e) {
                   System.out.println("Something went wrong in scan");
                }
            }
        }
    }

    private static void testFile(String filename) {

        Path path = Paths.get(filename);
        FileReader reader = null;
        try {
            reader = new FileReader(filename);
            // List<String> lines = Files.readAllLines(path); // throws checked exception which anticipates issue occurring
        } catch (IOException e) {
            throw new RuntimeException(); // bubbles up issue to calling function
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            // can use finally clause,
            System.out.println("Finally here ");
        }

        System.out.println("I'm good to go ");
    }
    private static void testFile2(String filename) {

        // multiple catch BP - delclare from most specific to general
        try (FileReader reader = new FileReader(filename)) { // comma delimted list of variabled, implement AutoCloaseable or Cloeable , used without finally block because all resources auto closed
            System.out.println("File with resources");
        } catch (FileNotFoundException e) { // opening resource
            System.out.println("File with resources'" + filename);
        } catch(NullPointerException | IllegalArgumentException badData) { // exceptions must be disjoint - Can't be of same parent class - will cause compiler error
            throw new RuntimeException(badData);
        } catch (IOException e) { // anytime including closing resource
            System.out.println("Something unexpected and unrelated");

            throw new RuntimeException(e);
        } // multiple catch clauses
        System.out.println("File exists and able to use with resources");
    }
    // Checked: Throwable, Exception, EOF, IO
    // Unchecked Error, RuntimeException, NullPointerException, IllegalArgumentExeption
    // important to second variation of catch clause - multiple targeted exception in single clause - separated by pipe |

    /* Java File operations open file in constructor and must be closed*/

    // Legacy(io) File FileReader - impls - AutoCloseable by parent Reader
    // In contract when you create instance of File you arent opening file - working with file handle to perform OS-like operations

}
