package com.init_spring_bean_mvn.demo.worker;

public class ContractVerificationAnalyst extends DigitalEmployee {

    public ContractVerificationAnalyst(String name, String birthDate, String hireDate, String terms, String billingRecords) {
        super(name, birthDate, hireDate);
        this.terms = terms;
        this.billingRecords = billingRecords;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getBillingRecords() {
        return billingRecords;
    }

    public void setBillingRecords(String billingRecords) {
        this.billingRecords = billingRecords;
    }

    private String terms;
    private String billingRecords;
}
