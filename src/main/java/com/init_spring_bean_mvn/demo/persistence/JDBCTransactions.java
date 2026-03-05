package com.init_spring_bean_mvn.demo.persistence;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class JDBCTransactions {
    private static String ANALYST_INSERT =
            "INSERT INTO scans.Scans (analyst_name) VALUES (?)";
    private static String SCANS_INSERT =
            "INSERT INTO scans.Scans (analyst_id, scans_name) VALUES (?, ?)";
    private static String COMPLIANCE_INSERT =
            "INSERT INTO scans.Scans (scanId, compliance) " +
                    "VALUES (?, ?)";

    public static void main(String[] args) {
        // data source will be used

        var dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("scans");

        try {
            dataSource.setContinueBatchOnError(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = dataSource.getConnection(
                System.getenv("MYSQL_USER"),
                System.getenv("MYSQL_PASS")
        );) {
            addDataFromFile(connection);
            String sql = "SELECT * FROM scans.Scans where scanId = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, 2);
            ResultSet rs = ps.executeQuery();
            // printRecords(rs); // sent to dbms and get scompiled at that point - PreparedStatement - contains precompiled
            // instanst, can optional contain parameters - ? - placeholders for data - call set method - setInt, setString
            // ensure user input is treated as data values and not executeable sql code
            // user of setString to set data value -
            // Series of set methods - setArray, SetBlob, long, double, etc..
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    private static int addAnalyst(PreparedStatement ps, Connection conn,
                                String analystName ) throws SQLException {
        int analystId = -1;
        ps.setString(1, analystName);
        int insertedCount = ps.executeUpdate();
        if(insertedCount > 0) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()) {
                analystId = generatedKeys.getInt(1);
                System.out.println("Auto-incremented ID: " + analystId);
            }
        }
        return analystId;
    }

    private static int addScans(PreparedStatement ps, Connection conn,
                                   int analystId, String scanName ) throws SQLException {
        int scanId = -1;
        ps.setInt(1, analystId);
        ps.setString(2, scanName);
        int insertedCount = ps.executeUpdate();
        if(insertedCount > 0) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()) {
                scanId = generatedKeys.getInt(1);
                System.out.println("Auto-incremented ID: " + scanId);
            }
        }
        return scanId;
    }

    private static void addScanResults(PreparedStatement ps, Connection conn,
                                      int scanId, int result, String field) throws SQLException {
        ps.setInt(1, scanId);
        ps.setInt(2, result);
        ps.addBatch();
     } // Next - batch Processing - addBatch() - add to batch of commands to be sent to dbms - executeBatch() - send batch to dbms for execution

    private static void addDataFromFile(Connection conn) throws SQLException {
        List<String> records = null;
        try {
            records = Files.readAllLines(Path.of("NewAlbums.csv"));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        String lastScan = null;
        String lastAnalyst = null;
        int analystId  = -1;
        int ScanId = -1;
        try (PreparedStatement psAnalyst = conn.prepareStatement(ANALYST_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement psScans = conn.prepareStatement(SCANS_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement psScanResults = conn.prepareStatement(COMPLIANCE_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            ) {
                conn.setAutoCommit(false);
                for(String record : records) {
                    String[] fields = record.split(",");
                    if(lastAnalyst == null || !lastAnalyst.equals(fields[0])) {
                        lastAnalyst = fields[0];
                        analystId = addAnalyst(psAnalyst, conn, lastAnalyst);

                    }
                    if(lastScan == null || !lastScan.equals(fields[1])) {

                        lastScan = fields[1];
                        ScanId = addScans(psScans, conn, analystId, lastScan);
                    }
                    System.out.println("fields 1, fields 2, fields 3 = " + fields[2] + " field = " + fields[1] + " " + fields[2] + " " + fields[3]);
                    addScanResults(psScanResults, conn, ScanId, Integer.parseInt(fields[2]), fields[3]);
                }
                int[] inserts = psScanResults.executeBatch();
                int totalInserts = Arrays.stream(inserts).sum(); // mini streams, sum inserts array and terminating with sum method
                System.out.printf("%d song records added %n", inserts.length);
                conn.commit();
                conn.setAutoCommit(true);

                // user - precompilation - parsing optimizing and parsing sql statement that can be efficiently exected by database server
            // Precompilation -
            // parameterized queries - dynamic data values and get injected at runtime
            // Efficient Resue  - Prepared statement can be executed multiple types subsittuting diffferent values
            // automatic type conversion - between java and sql datat ptes ensuring data compaitbaility
            // Easier to read and unstand as opposed to concatonented strings
            // Type safety - helps avoid datatype issues at runtime
            // flaws- need to execute up to 3 statements for single song in each album -
            // Didn't batch entire process ie 2000 round trips for 1000 albums and artists - batch processing can help with this - addBatch() and executeBatch() - send batch of commands to dbms for execution
                    // problem in second album , songs in first album wouldn't be inserted
            // Next solution - StoredProcedures - method on db server we can access that can be called from java code - precompiled and stored on dbms - can contain complex logic and multiple sql statements - can be called with parameters and return results - can help reduce network round trips and improve performance

            } catch(SQLException e) {
            conn.rollback();
             throw new RuntimeException(e);
        }
    }
}
