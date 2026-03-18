package com.init_spring_bean_mvn.demo.write;

import java.time.LocalDate;
import java.util.*;

public class Patient {

    record Purchase(String appointmentId, int patientId, double price, int year, int dayOfYear){
        public LocalDate purchaseDate() {
            return LocalDate.ofYearDay(year, dayOfYear);
        }
    };

        public static int lastId = 1;

    private Map<String, CourseEngagement> engagementMap;


    public void setName(String name) {
        this.name = name;
    }


    private String name;
        private int id;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String gender;


        private List<Appointment> appointmentList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Patient(String name, String gender, List<Appointment> appointmentList) {
            this.name = name;
            this.appointmentList = appointmentList;
            this.gender = gender;
            id = lastId++;

            engagementMap = new HashMap<>();

            for (Appointment appt : appointmentList) {
                engagementMap.put(appt.appointmentCode(),
                        new CourseEngagement(appt.appointmentCode(), 11, 2025,
                                "Speech"));
            }
        }

        public Patient(String name, String gender, com.init_spring_bean_mvn.demo.write.Appointment appointment) {
            this(name, gender, new ArrayList<>(List.of(appointment)));
        }

        public Patient(String name, String gender) {
            this(name, gender, new ArrayList<>());
        }

        public String getName() {
            return this.name;
        }

        public void addAppointment(Appointment appointment) {
            appointmentList.add(appointment);
        }

        public static int getRandomNumberInRange(int min, int max) {
            if (min >= max) {
                throw new IllegalArgumentException("max must be greater than min");
            }

            Random random = new Random();
            // nextInt(n) generates a random number between 0 (inclusive) and n (exclusive).
            // To include max, the range size is (max - min + 1).
            // Adding min shifts the result to the desired range.
            return random.nextInt(max - min) + min;
        }

        public static Patient getRandomPatient(Appointment... appointments) {

            Random random = new Random();
            String countryCode = List.of("AU", "CN", "GB", "IN","US")
                    .get(random.nextInt(5));
            String gender = List.of("M", "F", "U").get(random.nextInt(3));

            int minYear = 2015;
            int maxYear = LocalDate.now().getYear() + 1;

            String[] names = {"Martin Sierra, Isabella Bocanegra, Liliana DIAZ, Santiago Murcia" };
            int patientNameIndex = getRandomNumberInRange(0, names.length);
            Patient patient = new Patient(names[patientNameIndex], gender, List.of(appointments));


            int yearEnrolled = Patient.generateRandomYear(minYear, maxYear);
            List.of(appointments).forEach(c ->

                    patient.startVideo(c.appointmentCode(),
                            random.nextInt(1, c.getAppointmentCount()),
                            random.nextInt(1, 13),
                            random.nextInt(minYear, maxYear))
            );

        return patient;
    }
    public List<String> getEngagementRecords() {

        int i = 0;
        List<String> engagementData = new ArrayList<>();
        for (var engagement : engagementMap.values()) {
            engagementData.add("%s,%s".formatted(
                    id,
                    engagement));
        }
        return engagementData;
    }

    public static int generateRandomYear(int startYear, int endYear) {
        if (startYear > endYear) {
            throw new IllegalArgumentException("Start year cannot be greater than end year.");
        }
        Random random = new Random();
        // nextInt(bound) generates a number between 0 (inclusive) and bound (exclusive)
        // To include endYear, we add 1 to the range
        return random.nextInt((endYear - startYear + 1) + 1) + startYear;
    }
    public void startVideo(String courseCode, int lectureNumber, int month, int year) {

        var activity = engagementMap.get(courseCode);
        activity.recordLastActivity(lectureNumber, month, year);
    }
        @Override
        public String toString() {
            String[] appointmentNames = new String[appointmentList.size()];
            Arrays.setAll(appointmentNames, i -> appointmentList.get(i).title());
            return "[%d] %s:".formatted(id, String.join(", ", appointmentNames));
        }
    }
