package com.init_spring_bean_mvn.demo.write;


public record Appointment(String appointmentCode, String title) {

    public int getAppointmentCount() {
        return 15;
    }

    @Override
    public String toString() {
        return "%s %s".formatted(appointmentCode, title);
    }
}
