package com.init_spring_bean_mvn.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ContactData {
    private static final String phoneData = """
            John Doe, 2155550198
            Sophia Lane, 2675554432
            Sophia Lane, 2675554432
            Michael Torres, 4845551209
            Emily James, 6105550021
            """;
    // add comma delimited data for phone and email
    private static final String emailData = """
            John Doe, john.doe@example.com
            Sophia Lane, sophia.lane@example.org
            Sophia Lane, sophia.lane@example.org
            Michael Torres, michael.torres@example.net
            Emily James, emily.james@example.com
            """;

    public static List<Contact> getData(String type) {
        List<Contact> dataList = new ArrayList<>();
        Scanner scanner = new Scanner(type.equals("phone") ? phoneData : emailData);
        while(scanner.hasNext()) {
            String[] data = scanner.nextLine().split(",");
            Arrays.asList(data).replaceAll(String::trim);
            if(type.equals("phone")) {
                dataList.add(new Contact(data[0], Long.parseLong(data[1])));
            } else if(type.equals("email")) {
                dataList.add(new Contact(data[0], data[1]));
            }
        }
        return dataList;
    }

}
