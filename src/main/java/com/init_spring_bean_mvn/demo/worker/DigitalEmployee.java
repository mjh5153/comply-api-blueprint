package com.init_spring_bean_mvn.demo.worker;

public class DigitalEmployee extends DigitalWorker {

    private long employeeId;
    private String hireDate;


    public DigitalEmployee(String name, String birthDate, String endDate) {
        super(name, birthDate, endDate);
    }


}
