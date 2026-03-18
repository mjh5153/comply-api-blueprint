package com.init_spring_bean_mvn.demo.worker;

public class UsageVarianceAnalyst extends DigitalEmployee{
    private String logs;
    private String invoicedUsage;
    private double meterRate;

    public UsageVarianceAnalyst(String name, String birthDate, String hireDate, String logs, String invoicedUsage, double meterRate) {
        super(name, birthDate, hireDate);
        this.logs = logs;
        this.invoicedUsage = invoicedUsage;
        this.meterRate = meterRate;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public String getInvoicedUsage() {
        return invoicedUsage;
    }

    public void setInvoicedUsage(String invoicedUsage) {
        this.invoicedUsage = invoicedUsage;
    }

    public double getMeterRate() {
        return meterRate;
    }

    public void setMeterRate(double meterRate) {
        this.meterRate = meterRate;
    }
}
