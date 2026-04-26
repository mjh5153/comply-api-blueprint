package com.init_spring_bean_mvn.demo.datetimecalendars;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateTimeService {

    public static void main(String[] args) {
        // Bad practice -  Gregorian calendar

        // Good practice use java.time package - LocalDate, LocalTime, LocalDateTime, ZonedDateTime
        LocalDate date = LocalDate.now();
        LocalDate manualData = LocalDate.of(2024, 6, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = date.format(formatter); // throws illegal argument exception if the pattern is invalid
        System.out.println(formattedDate);

        ZonedDateTime zonedDateTime = ZonedDateTime.now(TimeZone.getTimeZone("America/New_York").toZoneId());
        System.out.println(zonedDateTime);

        // Outputs UTC time
        Instant.now();
        System.out.println(Instant.now());
    }
}
