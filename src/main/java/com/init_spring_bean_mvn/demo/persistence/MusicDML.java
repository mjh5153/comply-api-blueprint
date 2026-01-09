package com.init_spring_bean_mvn.demo.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MusicDML {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/scans",
                System.getenv("MYSQL_USER"),
                System.getenv("MYSQL_PASS")
        );
             Statement statement = connection.createStatement();
        ) {
            // Delegate
            String delegate = "Joel";
            // query =
            String query = "SELECT * FROM scans where CompanyType = '%s'".formatted(delegate);
                    boolean result = statement.execute(query);
            System.out.println("result = " + result);

            var rs = statement.getResultSet();
            boolean found = (rs != null && rs.next()); // viable approach to test if query was found or not
            System.out.println("Delegate was " + (found ? "found" : "not found"));

        } catch(SQLException e) {
            throw new RuntimeException();
        }
    }
}
