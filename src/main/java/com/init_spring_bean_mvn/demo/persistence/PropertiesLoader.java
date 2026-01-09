package com.init_spring_bean_mvn.demo.persistence;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

// prevent sql injection: attacker attempts to manipulate input data,sent to apps database query
// Points: user input, login forms, checked against store crds, search forms,
// url perameters, dynamic sql queries, any input in web app that interacts with db
// Prevention- validat and sanitize user input
// Practice least Privilege Principle -
//  - Properterrrohandling andling
// use prepared stanments or parameterizequries

//Types of sql to use: ANSI SQL - compatible withmost databases
// Vendors may only implement standards minimually
// Avoid Vendor locking - Can make moving to different dbms

// ex: limit clause differences; ANSI - FETCH FIRST - mysql LIMIT clause
public class PropertiesLoader {
    public static void main(String[] args) {
        Properties props = new Properties();
        try{
            props.load(Files.newInputStream(Path.of("scans.properties"), StandardOpenOption.READ));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        //can use dataSource.setMaxRow(); Same result as below 2 but much simpler
        // String query = "SELECT * FROM scans";
        String query = "SELECT * FROM scans.scans";

// dbms agnostic but high complexity statement
//                  String query = """
//                WITH RankedRows AS ( SELECT *,
//                ROW_NUMBER() OVER (ORDER BY  scanId) AS row_num FROM scans.scans )
//                SELECT * FROM RankedRows
//                WHERE row_num <= 10
//                """;
        // now need a statement object - try with resources clause

        // See link - Oracle-Compliance-To-Core-SQL2011.html - Oracle Compliance to Core SqL
        var dataSource= new MysqlDataSource();
        dataSource.setServerName(props.getProperty("serverName"));
        dataSource.setPort(Integer.parseInt(props.getProperty("port")));
        dataSource.setDatabaseName(props.getProperty("databaseName"));

        try (var conn = dataSource.getConnection(
                props.getProperty("user"),
                props.getProperty("password"));

                Statement st = conn.createStatement();
        )
             {
                 ResultSet rs = st.executeQuery(query); // sqlDeveloper written in java, workbench written in c++,
                 // toad - java
                 var meta = rs.getMetaData();
                 for ( int ri = 1; ri <= meta.getColumnCount(); ri++) {
                     System.out.printf("%-15s", ri, meta.getColumnName(ri), meta.getColumnTypeName(ri)); // 15 character format
                     // methods: columnType, width to print table more flexible
                 }

                 // Code to return data in grid using java much like in mysql workbench returns
                 while(rs.next()) {
                     for ( int ri = 1; ri <= meta.getColumnCount(); ri++) {
                         System.out.printf("%-15s", ri, rs.getString(ri));
                     }
                     System.out.println();
                     // rs.getString("companyName");

                     // if don't know data: can use ResultSet.metadata object to get info about data
                 }
            System.out.println("Success! connected without entering un pw");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
