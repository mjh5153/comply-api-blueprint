package com.init_spring_bean_mvn.demo.persistence;

import java.sql.*;
import java.util.Scanner;

public class MusicDML {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // can call getClass on any class to get a class instance

        } catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/scans?continueBatchOnError=false",// can provide parameters here to configure mysql
                System.getenv("MYSQL_USER"),
                System.getenv("MYSQL_PASS")
        );
             Statement statement = connection.createStatement();
        ) {
//            // Delegate
//            String delegate = "Joel";
//            // query =
//            String query = "SELECT * FROM scans where CompanyType = '%s'".formatted(delegate);
//                    boolean result = statement.execute(query);
//            System.out.println("result = " + result);
//
//            var rs = statement.getResultSet();
//            boolean found = (rs != null && rs.next()); // viable approach to test if query was found or not
//            System.out.println("Delegate was " + (found ? "found" : "not found"));
            Scanner scan = new Scanner(System.in);
            String input = "";
            String nextInput = "";
            if(scan.hasNext()) {
                input = scan.nextLine();
                System.out.println("input = " + input);
            }
            if(scan.hasNext()) {
                nextInput = scan.nextLine();
                System.out.println("nextinput = " + nextInput);
            }
            String tableName = "scans.Scans";
            String columnName = input;
            String columnValue = nextInput;

            // prevent users from deleting parent records and leaving child records around;
            boolean found = executeSelect(statement, tableName, columnName, columnValue);
            if(!found) {
                System.out.println("No records found for %s = %s".formatted(columnName, columnValue));
                insertRecord(statement, tableName, new String[] {columnName}, new String[] {columnValue});
                // if sql fails then we would get an exception in application and app would end
            } // ResultSet can get returend with no data but with metadata but not results if nothing found on query

            // important to manage transactions with use of commit and roll back -

            // Batch processing is often used to improved permance

        } catch(SQLException e) {
            throw new RuntimeException();
        }
    }

    // print records
    private static boolean printRecords(ResultSet rs)
 throws SQLException {
        boolean foundData = false;

        var meta = rs.getMetaData();
        System.out.println("======");

        for (int i = 1; i <= meta.getColumnCount(); i++) {
            System.out.printf("%-15s", meta.getColumnName(i).toUpperCase());
        }
        System.out.println();

        while(rs.next()) {
            for(int i = 1; i <=meta.getColumnCount(); i++) {
                System.out.printf("%-15s", rs.getString(i));

            }
            System.out.println();
            foundData = true;
        }

        return foundData;
    }

    private static boolean executeSelect(Statement statement, String table, String columnName, String columnValue) throws SQLException {
        String query = "SELECT * FROM %s WHERE %s = '%s'".formatted(table, columnName, columnValue);
        System.out.println("Execute Select query " + query);
        var result = statement.executeQuery(query);
        if(result != null) {
            return printRecords(result);
        }
        return false;
    }

    private static boolean insertRecord(Statement statement, String table, String[] columnName, String[] columnValue) throws SQLException {

        String colNames = String.join(",", columnName);
        String colValues = String.join("','", columnValue);
        // dml query
        String query = "INSERT INTO %s (%s) VALUES ('%s')".formatted(table, colNames, colValues);
        System.out.println(query);
        boolean insertResult = statement.execute(query);
        System.out.println("Insert result = " + insertResult);
        int recordsInserted = statement.getUpdateCount();
        System.out.println("Records inserted = " + recordsInserted);
        if(recordsInserted > 0) {
            executeSelect(statement, table, columnName[0], columnValue[0]);
            System.out.println("Record inserted successfully");
        }
        return statement.execute(query);
    }

    private static void deleteRecord() {
        /// Single transactional execute - commits - all or nothing commit
        // autocommit option - commit changes in db after statement - implicitely happen
        // Default connection object is in auto-commit mode
        // small changes - dangerous
        // Stored in tem location call redo log or jourcal file - persisted permanently to db - can review changes in temp location
        // Turned off autocommit if want series atatements to be in automic operation

        //Transactions: insers updates or deletes - ensure - db operations are atomic - all succeed of fail
        // Transaction fails - gets rolled back and no changes
        // jdbc - transaction is initialized when turn AUTOCOMMIT off
    }

    private static void deleteDelegate(Connection conn, Statement statement,
                                       String DelegateName, String CompanyType) throws SQLException {
    //
        try { // increase performance by batching statements
            System.out.println("AUTOCOMMIT = " + conn.getAutoCommit());
            conn.setAutoCommit(false);

        String deleteScans = """
                    DELETE FROM scans.Scans
                    WHERE scanId = (SELECT scandId from scans.Scans WHERE scan_name = '%s'
                """;

                // int deletedScans = statement.executeUpdate(deleteScans);

                String deleteSearches = """
                    DELETE FROM scans.searches
                    WHERE scanId = (SELECT scandId from scans.Scans WHERE scan_name = '%s'
                """;
        System.out.println("Deleted %d rows from scans.Scans = " + deleteSearches);
                // int deletedSearches = statement.executeUpdate(deleteSearches);

        // System.out.println("Deleted %d rows from scans.searches = " + deletedSearches);

        String deleteDelegate = """
                    DELETE FROM scans.delegates
                    WHERE DelegateName = '%s'
                """.formatted(DelegateName);

        statement.addBatch(deleteScans);
            statement.addBatch(deleteSearches);

            int[] results = statement.executeBatch(); // need to add for batch executions
            System.out.println("Deleted %d rows from scans.Scans = " + results[0]);
        //int deletedDelegates = statement.executeUpdate(deleteDelegate);
        //System.out.println("Deleted %d rows from scans.Delegate = " + deletedDelegates);
            // mysqlj driver by defaults executes all statements even on failure - see docs https://devmysql.com/doc/connector-j/en/connctor-j-connp-props-statements.html

        conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback(); // sets back to original state before transaction started
        }
        conn.setAutoCommit(true);
    }


}
