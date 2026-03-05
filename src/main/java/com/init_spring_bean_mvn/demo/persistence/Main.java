package com.init_spring_bean_mvn.demo.persistence;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/* Sql database server behavior when Sql connection made from java program jdbc driver
* Query is parsed
* Syntax checked
* Execution Plan is Created
* Compiled statement is created
* Compiled ? Stored in a cache on database server
* Can be reused, without having to be recompiled each tim executed - stored also as special jdbc statement
* PreparedStatement in JDBC
* i.e Compiled reg exp by using Pattern.compile
* String is passed, get interpreted as an operation
* parsed, syntax  optimizations applied
* Con - overhead - use statement multiple times - pre-compile
* Execute same statement multiple times with parm value placeholders - db doesn't need to parse everytime exectued
* ? instead of value WHERE artist_name = ? - literals determined by type for use
* // pass values specifiying data types at that time - not interpretted as sql code - prevent help sql injection attacks
* Multiple parameters in sql string - different types
* placeholders is same - regardless of type of parameter
*  */
public class Main {
    private static String USE_SCHEMA = "USE storefront";

    public static void main(String[] args) {
        var dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(3306);
        dataSource.setUser(System.getenv("MYSQL_USER"));
        dataSource.setPassword(System.getenv("MYSQL_PASS"));
        // statement using 2 not legacy conventions

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            System.out.println(metaData.getSQLStateType());
            if(!checkSchema(conn)) {
                System.out.println("storefront schema does not exist.");
                setUpSchema(conn);
            }

            int newOrder = addOrder(conn, new String[] {"tos", "termsofservice", "USCA" });
            System.out.println("new order = " + newOrder);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static boolean checkSchema(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.execute(USE_SCHEMA);

        } catch (SQLException e) {
            e.printStackTrace();
            // code describing error
            // Consistent output streams more common in client server apps
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            if (conn.getMetaData().getDatabaseProductName().equals("MySQL") && e.getErrorCode() == 1049) {
                // MySQL specific error code for "Unknown database"

                    return false;
            } else throw e;
        }
        return true;
    }

    private static void setUpSchema(Connection conn) throws SQLException {

            String createSchemaSQL = "CREATE SCHEMA storefront";

            String createOrder = """
                        CREATE TABLE storefront.order (
                            order_id int NOT NULL AUTO_INCREMENT,
                            order_date DATETIME NOT NULL,
                            PRIMARY KEY (order_id)
                        )
                    """;
            String createOrderDetails = """
                        CREATE TABLE storefront.order_details (
                            order_detail_id int NOT NULL AUTO_INCREMENT,
                            item_description text,
                            order_id int NOT NULL,
                            PRIMARY KEY (order_detail_id),
                            KEY FK_ORDERID (order_id),
                            CONSTRAINT FK_ORDERID FOREIGN KEY (order_id)
                            REFERENCES storefront.order (order_id) ON DELETE CASCADE
                        )
                    """; // Delete parent also deletes child, treated like single unit when delete parent - delete order, details also gets deleted

            // difference of ddl and dml - ddl cant rollback but dml can be rolled back
            // Now have schema to u
            try(Statement statement = conn.createStatement()) {
                System.out.println("Creating storefront schema...");
                statement.execute(createSchemaSQL);
                if(checkSchema(conn)) {
                    statement.execute(createOrder);
                    System.out.println("Successfully Created Order");
                    statement.execute(createOrderDetails);
                    System.out.println("Successfully Created OrderDetails");
               }
            }

            System.out.println("storefront schema created or already exists.");

    }

    private static int addOrder(Connection conn, String[] items) throws SQLException {
        int orderId = -1;
        String insertOrder = "INSERT INTO storefront.order (order_date) VALUES ('%s')";
        String insertDetail = "INSERT INTO storefront.order_details " + "(order_id, item_description) VALUES (%d, %s)";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String orderDateTime = LocalDateTime.now().format(dtf);

        String formattedString = insertOrder.formatted(orderDateTime);
        System.out.println(formattedString);
        try (Statement statement = conn.createStatement()) {
            conn.setAutoCommit(false);
            int inserts = statement.executeUpdate(formattedString, Statement.RETURN_GENERATED_KEYS);
            if(inserts == 1) {
                var rs = statement.getGeneratedKeys();
                if(rs.next()) {
                    orderId = rs.getInt(1);
                }
            }
            int count = 0;
            for(var item : items) {
                formattedString = insertDetail.formatted(orderId,
                        statement.enquoteLiteral(item));
                System.out.println("formattedString = " + formattedString);
                inserts = statement.executeUpdate(formattedString);
                count += inserts;
            }
            if(count != items.length) {
                orderId = -1;
                System.out.println("num records doesn't equal items received");
                conn.rollback();
            } else {
                conn.commit();
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch(SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
        return orderId;
    }
}
