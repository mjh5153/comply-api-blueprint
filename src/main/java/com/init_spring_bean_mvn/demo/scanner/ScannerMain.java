package com.init_spring_bean_mvn.demo.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.MatchResult;

public class ScannerMain {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(new File("files/ScanResults.csv"))) {
//            while(scanner.hasNextLine()) {
//                System.out.println(scanner.nextLine()); // can use tokens method to get stream of strings
//            }
//            System.out.println(scanner.delimiter());
//            scanner.useDelimiter("$");
//            scanner.tokens().forEach(System.out::println);

            //
//            scanner.findAll("[A-Za-z]{10,}").map(MatchResult::group).
//            distinct().sorted().forEach(System.out::println); // matchResults has group methods

            scanner.findAll("(.{15})(.{3})(.{12})(.{8})(.{2}).*").skip(1)
                    .map(m -> m.group(3).trim())
                    .distinct()
                    .sorted()
                    .toArray(String[]::new);
            // scanner uses ReadableByteChannel - NIO.2 enhancements
            // scanner with overloaded Path.of passed into new scanner
            // scanner will use nio.2 functionality with filereader or bufferfilereader when instantiating Scanner
        } catch (FileNotFoundException fnf) {
            throw new RuntimeException();
        }
        // file, path, string, readable, inputstream, inputbytechannel,
        //CharacterSet or CharacterSetName
    }
}
