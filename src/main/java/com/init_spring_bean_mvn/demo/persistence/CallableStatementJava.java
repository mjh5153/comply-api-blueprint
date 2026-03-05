package com.init_spring_bean_mvn.demo.persistence;

import com.sun.jdi.PathSearchingVirtualMachine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Map;
import java.util.stream.Collectors;

public class CallableStatementJava {
    /*
    * Challenges working with db's via jdbc
    * Performance Tuning        Concurrency Control         SQL Injection
    * Transaction Management    Scalability                 Monitoring and Logging
    * Resource Leaks            Testing and Integration
    * Data Type Mapping         Error Handling              Vendor Lock-In
    *
    * Many issues on db server itself - dba's and sql devs
    * indeces, contraints, storeed procedures, monitoring logging code, queries
    * Consider addresses problems with server solutions - functions and routines in sql, or transaction or procedural languages
    * Store procedures - Procedure and function
    * Encapsulate logic on db server - call from jdbc into reusable models that can be call within db
    * improve performance, modularity and security
    * promciled queries, or segments of code that are storeds as another db - can be written in java, python or c wrapped
    *   in a PRocedure that can execute that code - External types of Stored Procedures
    *
    * CallableStatement interface in JDBC - used to call stored procedures - protect acess to db and restrice what clients aloud to do
    * can accept input parameters and return output parameters and result sets
    * java execcution and retrieval of results from these code segments with
    * first takes a parameterized string when get instance
    * Object referenced will already be compiled in db server
    * like method in java- can pass in data with parameters and you may or may or not get data back
    *
    * 1. Need access to Callable  Procedure download addAlbumSongs - Once exeute it will show up under stored Procedures folder in music db (scans )
    *-  parms - artistName TEXT, albumName TEXT, songTitles JSON (array of strings) - sql sp's don't currently support arrays but accept json
    *  - default input parameter - No Input parameters - can be set to IN, OUT, INOUT
    *
    * */

    private static final int ARTIST_COLUMN = 0;
    private static final int ALBUM_COLUMN = 1;
    private static final int SONG_COLUMN = 2;

    public static void main(String[] args) {
            Map<String, Map<String, String>> scans = null;
            try(var lines = Files.lines(Path.of("NewAlbums.csv"))) {
                scans = lines.map(s -> s.split(","))
                        .collect(Collectors.groupingBy(s -> s[ARTIST_COLUMN],
                        Collectors.groupingBy(s -> s[ALBUM_COLUMN],
                        Collectors.mapping(s -> s[SONG_COLUMN],
                        Collectors.joining("\",\"",
                                "[\"","\"]")))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scans.forEach((artist, artistAlbums) -> {
                artistAlbums.forEach((key, value) -> {
                    System.out.println(key + " : " + value);
                });
            });
            
            // paste code from Main of previous lecture - delete batchOnErrorFlag code - removed addDataFromFile call

        // CallableStatement cs = connection.prepareCall("{CALL addAlbumSongs(?, ?, ?)}"); pass string
        // "CALL procedurename music.addAlbum("?,?,?")  and ?, ?, ?" for parameters
        // set parameters - with index cs.setString(1, artistName); etc

        // get db back
        // cs.registerOutParameter(4, Types.INTEGER); // index 4 is out parameter - return new album id
        ///  RETRIEVE DATA FROM STATEMENT AFTER EXECUTED
        // cs.getInt(4, album);
        ///  notice statements 13 songs added for bob dylan and blonde on blonde
        // run cs.execute(); // to run stored procedure
        // mysql doesn't support arrays but can use json strings to represent arrays
        // SP takes 3 input parameters but didn't return data back

        // Next - work on getting results back from stored procedure

        // Callable Statements Input and Output Parameters
        // can have IN, OUT, INOUT parameters - type not specified - implicitely IN parameter -         Y, N
            // Out - Return values from SP to calling program -                                         Read: N, Write Y
            // INOUT - both read and write - Read Y, Write Y can modify values and return more values - Y, Y
        // JDBC - advantage - don't need to know syntax just concepts
        //  switch
        // get error parameter count is not registered as output parameter twice
        // other question mark in call to SP -- NOW works and data gets added, register out parameter to get data back from cs.execute


        // Execute function to get data back - mySqlFunctions and using with jdbc code
        // Use CallableStatement and jdbc escape sequences
        // control transaction management - enabling consistency with data
        // both stored in db and both can be executed as single unit ,
        // stored functions meant to return single value, integer, string, data resultset
        // is  Immutable - should produce same output and not modify in db
        // can use directly in sql expressions - SELECT myFunction(param1, param2) FROM dual;

        // Store Function - Data Validation, SP: Data Modification, ETL, Business Logic, Batch Processing
        // Data Conversion, Complex Expressions, Calculations
        //  SP: Transaction Processing

        // Called from java CallableStatement
        // diferences - question mark - for returned result - mysql functions only support IN Parameters - no parameter types
        // calcAlbumLength store function - return double - ERROR 1418 - function has none of deterministic, no sql, reads sql data in its declaration and binary logging is enabled
        // Is other option but won't get into course - set function to deterministic - always return same result for same input parameters
        // Data not expected to change - deterministic
        //* Call calcAlbumLength function*/

        ///CallableStatement csf = connection.prepareCall(
              ///  "{? = CALL scan.calcAlbumLength(?, ?)}"; // need curly brace for function call so it doesn't think it's calling a stored procedure
        // informs jdbc driver that first parameter is return value and other
        // more consistent and portable manner across db systems.
        // certains types not standard - date time and timespamp literlas
        // scalar functions such as numeric, string and data type voncersion
        //escxape chars for wildcards used in LIKE cluases
                // stored in procedures and functions
        // informs jdbc driver to prepare the code to execute a function
        // google jdbc exscape sequences for more information

        // CALL supported by most rdbms - some use different syntax

        // Summary: enhance code resusablity, inmproved performance, encapsulate complex logic, enhance security, modularity
        // centralize business logic, reduce network traffic, facilitate maintenance
//                csf.registerOutParameter(1, Types.DOUBLE);
//
//                albums.forEach((artist, albumMap) -> {
//                    albumMap.keySet().forEach((albumName) -> {
//                        try {
//                            csf.setString(2, albumName);
//                            csf.execute();
//                            double result = csf.getDouble(1);
//                            System.out.printf("Length of %s is %.1f%n",)
//                                    albumName, artist, result);
//                        }
//                    })
//                })
//        )
    }
}
