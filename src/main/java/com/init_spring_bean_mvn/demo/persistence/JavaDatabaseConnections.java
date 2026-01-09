package com.init_spring_bean_mvn.demo.persistence;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class JavaDatabaseConnections {

    // PreparedStatement
    // CallableStatement


    // databases like mysql have own procedural language to use instead of using sql code
    // MySql will be used - Free, beginners course with mysql, not hard install, popular industry stadndrd

    // Connect - Jdbc, Connection Strings to connect from java app to mysql db - can swap db connection string to swap db engine

    // mysql notes: default users can be changed

    // create new user for regular use as root user use has security problems
    // oo dbs, relations, nosql
    // same code reusable between dbs

    // find info in - java.sql package - api/java.sql/java/sql/package-summary.html
    // show's jdbc ~ java SE platform - many new features are options and variable of drivers features and supported version

    // Why? allow connection to db, execute sql statements - dml, crud, ddl
    // Execute Stored procedures or function store in db
    // retrieve and process results
    // handle db exceptions
    // version : JDBC 4.0
    // - javax.sql : serverside datasource access - Types : DriverManager
    // ResultSet

    // javax.sql: DataSource : Make connection with a driver | RowSet : Query Results
    private final static String CONN_STRING = "jdbc:mysql://localhost:3306/scans";
    // extracted .jar file to Downloads folder

    // Basic Connections
    public static void main(String[] args) {
        // un pw's, solicit from user
        String username = JOptionPane.showInputDialog(null, "Enter DB Username");
        JPasswordField pf = new JPasswordField(); // swing JPasswordField - can't see pw from console
        int okCxl = JOptionPane.showConfirmDialog(null, pf, "EnterDB Password", JOptionPane.OK_CANCEL_OPTION);
        final char[] password = (okCxl == JOptionPane.OK_OPTION ) ? pf.getPassword() : null;
        // make connection: how nad what connecting

        var ds = new MysqlDataSource(); // multi tier prod - jndi servoce = c;oemt = server a[[s = dpm
        // t meed or password
        // still need uname and password

        // ds.setURL(CONN_STRING);
        ds.setServerName("localhost");
        ds.setPort(3306);
        ds.setDatabaseName("scans");
        // try (Connection connection = DriverManager.getConnection(CONN_STRING, username, String.valueOf(password))) {
        try (Connection connection = ds.getConnection(username, String.valueOf(password))) {
            System.out.println("Success !");

            Arrays.fill(password, ' ');

        } catch(SQLException e) {
            e.printStackTrace();

            throw new RuntimeException(); // connection auto closed when this code completes
        }
    }
}
