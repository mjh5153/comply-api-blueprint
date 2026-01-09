package com.init_spring_bean_mvn.demo.write;

import com.init_spring_bean_mvn.demo.Contact;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class WriteData {
    // Storing user data, logging app events to log file, storing config data, export data for exchange of info, support offline usage in file cache
    // can be loaded from local file and stored in file when user working remotely and data uploaded when connected again
    // Generating file products: reports
    // OutputStream class instead of inputStream, FileWriter, etc... managing multiple writes to single file from threads,
    // Opening file: different ways

    public static void main(String[] args) {
        // header

        String header = """
                   Time, Date,\
                """;

        Appointment appt = new Appointment("SPH", "Speech");
        Appointment apptHearing = new Appointment("HRG", "Hearing");

        List<Patient> patients = Stream.generate(() -> Patient.getRandomPatient(appt, apptHearing))
                .limit(Integer.MAX_VALUE)
                .toList();
//        System.out.println(header);
//        patients.forEach(p -> p.getEngagementRecords().forEach(System.out::println));
        Path path = Path.of("files/pacientes cuenta de cobro (Noviembre).csv");
//        try {
//            Files.writeString(path, header); // opens, writes, closes file: default java option
//            for(Patient patient: patients) {
//                Files.write(path, patient.getEngagementRecords(), StandardOpenOption.APPEND); // opens, writes, closes file
//                // if doesn't exist writes and creates new files, truncates each time, Use StandardOpenOption APPEND to change behavior
//            }
//        } catch (IOException io) {
//            io.printStackTrace();
//        }

        /* Methods good for log file but not for writing many records iterably */
//        try {
//            List<String> data = new ArrayList<>();
//            data.add(header);
//            for (Patient patient: patients) {
//                data.addAll(patient.getEngagementRecords());
//            }
//            Files.write(path, data); // incremental writes inefficient
//            // Solution: BufferedWriter method on Files class
//        } catch(IOException io) {
//            io.printStackTrace();
//        }

//        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("files/take.csv"))) {
//            writer.write(header);
//            // methods to overwrite characters and a newline mthod
//            writer.newLine();
//            for (Patient patient: patients) {
//                for(var record : patient.getEngagementRecords()) {
//                    writer.write(record);
//                }
//            }
//        } catch(IOException io) {
//            io.printStackTrace();
//        }

//        try (FileWriter fwriter = new FileWriter("files/take3.csv")) {
//            fwriter.write(header);
//            // methods to overwrite characters and a newline mthod
//            fwriter.write(System.lineSeparator());
//            for (Patient patient: patients) {
//                for(var record : patient.getEngagementRecords()) {
//                    fwriter.write(record);
//                    fwriter.write(System.lineSeparator()); // uses os new line , varies by os
//                }
//            }
//        } catch(IOException io) {
//            io.printStackTrace();
//        }

        try (PrintWriter pwriter = new PrintWriter("files/take4.csv")) {
            pwriter.println(header);
            // methods to overwrite characters and a newline mthod
            int count = 0;
            for (Patient patient: patients) {
                for(var record : patient.getEngagementRecords()) {
                    String[] recordData = record.split(",");
                    pwriter.printf("%-12d%-5s%-1s".formatted(
                            patient.getId(),
                            patient.getName(),
                            patient.getGender()
                    ));
                    pwriter.println(record);
                    //priter.format("") alternative to printf
                    count++;
                    if(count % 5 == 0) {
                        Thread.sleep(2000);
                        System.out.print(".");
                    }
                }
            }
        } catch(IOException io) {
            io.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // OVERLOAD FOR PRINTWRITER - AUTOFLUSH - default false - won't flush after every print or format method then
        // Temporary storge in writing files
        // Physical writes to disk when buffer flushed
        // Taking text stored in bugger, writing to output file, clearing buggers cache: size of bugger, speed of disk, data size, buffer flush ask different rates
        // Manually flush buffer - time sentigive data: flush omore frequently

    }
}
