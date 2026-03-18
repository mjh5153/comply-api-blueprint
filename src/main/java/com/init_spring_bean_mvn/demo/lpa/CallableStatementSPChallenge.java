package com.init_spring_bean_mvn.demo.lpa;

public class CallableStatementSPChallenge {

    // sp storefront db
    // sp four parameters - in, in, in, out
    // Teo input parameters
    // OrderDate, DATETime

    // java uses callable statement - timestamp for datetime java.sql.TimeStamp
    // translate string to java.sql.Timestamp - datateime formatteer and localdatetime

    // pass json string, reprsneting array of order details

    // output parameter - new order id, number of orders inserted

    // Need to delete data in storefrom - see helpful hint s of slide for sql

    // Use JsonBuilder template to build json - Used StringJoiner - Leverage for sp string parameter - could write own method

    // ofPattern("uuuu-MM-dd HH:mm:ss"); // complicated subject of using u over y paaterrn. Causes parsing to fail on one bad data
    // uses strict parsing

    // Timestamp timestamp = Timestamp. valueOf(localDateTime
}
