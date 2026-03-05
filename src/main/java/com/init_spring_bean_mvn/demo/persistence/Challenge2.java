package com.init_spring_bean_mvn.demo.persistence;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
// DDL
// Prepared
// Batch statements
// Transactions
// Error handling
// Rollbacks
// Commit
record OrderDetail(int orderDetailId, String itemDescription, int quantity) {
    public OrderDetail(String itemDescription, int qty) {
        this(-1, itemDescription, qty);
    }

    // record Order (String dateString) {
    // this(-1, dateString, new ArrayList<OrderDetail>());
    //}
    /*
    *  public void addDetail(String itemDescription, int qty) {
    * OrderDetail item = new OrderDetail(itemDescription, qty);
    *      orderDetails.add( new OrderDetail(itemDescription, qty));
    * */
}
public class Challenge2 {

        private static String USE_SCHEMA = "USE storefront";

        public static void main(String[] args) {
            var dataSource = new MysqlDataSource();
            dataSource.setServerName("localhost");
            dataSource.setPortNumber(3306);
            dataSource.setUser(System.getenv("MYSQL_USER"));
            dataSource.setPassword(System.getenv("MYSQL_PASS"));
            // statement using 2 not legacy conventions

            try (Connection conn = dataSource.getConnection()) {
               String alterString = "ALTER TABLE storefront.order_details ADD COLUMN quantity INT";
               Statement statement = conn.createStatement();
               statement.execute(alterString);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        /*
        * private static void addOrder(Connection conn, PreparedStatement psOrder,
        * PreparedStatement psDetaisl, Order order) throws SQLException {
        * try {
        * conn.setAutoCommit(false);
        * int orderId = -1;
        * psOrder.setString(1, order.dateString());
        * if(psOrder.executeUpdtae() ==1) {
        *  var rs = psOrder.getGeneratedKeys();
        *  if(rs.next()) {
        *   orderId = rs.getInt(1);
        * System.out.println("orderId = " + orderId);
        * if(orderId > =1) {
        * for(OrderDetail detail : order.orderDetails()) {
        *   psDetaisl.setInt(1, orderId);
        *   psDetaisl.setString(2, detail.itemDescription());
        *   psDetaisl.setInt(3, detail.quantity());
        *   psDetaisl.addBatch();
        *
        * }
        * int[] data= psDetail.executeBatch();
        * int rowsInserted = Arrays.stream(data).sum();
        * if(rowsInserted != order.details().size()) {
        *   throw new SQLException("Inserts don't match");
        *
        * }
        * }
        * }
        * }
        * conn.commit();
        * } catch(SQLException e) {
        * conn.rollback();
        * throw e;
        * } finally {
        * conn.setAutoCommit(true);
        * }
        * }
        * }
        * */
        /*
        // CALL from main method in try catch cluase - Will have error in data - Data trucation: Incorrect dtatetime value for column Order
        // 4th order has bad date - yyy-mm-dd hh:mm:ss sql error 1291 (22001)
        * private static void addOrders(Connection conn, List<ORder> orders) throws SQLException {
        * String orderSQL = "INSERT INTO storefront.order (order_date) VALUES (?)";
        * String detailsSQL = "INSERT INTO storefront.order_details (order_id, item_description, quantity) VALUES (?, ?, ?)";
        * try(PreparedStatement psOrder = conn.prepareStatement(orderSQL, Statement.RETURN_GENERATED_KEYS);
        *      PreparedStatement psDetails = conn.prepareStatement(detailsSQL);) {
        *
        * ) {
        *   orders.forEach((o -> {
        *
        *   try {
        * addOrder(conn, psOrder, psDetail, o);
        * } catch(SQLException e) {
        *
        * }
        * } catch(SQLException e) {
        * throw new RuntimeException(e);
        * }
        * */
        /*
        * call method before getting data connection and assign to order list variable
        * private static List<Order> readData() {
        *  List<Order> vals = new ArrayList<>();
        *   try(Scanner scanner = new Scanner(Path.of("src/main/resources/orders.txt"))) {
        *
        *    scanner.useDelimiter("[.\\n"]);
        *   var list = scanner.tokens().map(String::trim).toList();
        *
        *   for(int i = 0; i < list.size(); i++) {
        *       String value = list.get(i);
        *       if(value.equals("order")) {
        *          var date = list.get(++i);
        *           vals.add(new Order(date));
        *       } else if(value.equals("item")) {
        *
        *           var qty = Integer.parseInt(list.get(++i));
        *           var itemDesc = list.get(++i);
        *
        *           Order order = vals.get(vals.size() - 1);
        *           order.addDetail(itemDesc, qty);
        *   }
        * }
        * vals.forEach(System.out::println);
        * } catch(IOException e) {
        *
        *
        * */
}
