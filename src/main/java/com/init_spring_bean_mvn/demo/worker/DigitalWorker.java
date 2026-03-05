package com.init_spring_bean_mvn.demo.worker;

public class DigitalWorker {
    public DigitalWorker(String name, String birthDate, String endDate) {
        this.name = name;
        this.birthDate = birthDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    private String name;
    private String birthDate;
    private String endDate;

    public int getAge() {
        int currentYear = java.time.Year.now().getValue();
        int birthYear = Integer.parseInt(birthDate.substring(0, 4));
        return currentYear - birthYear;
    }

    public double collectMeteredUsage(double usage, double rate) {
        return usage * rate;
    }

    public void terminate(String endDate) {
        this.endDate = endDate; // subclasses might want to override for specific worker
    }

    @Override
    public String toString() {
        return "DigitalWorker{" +
                "name='" + name + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
