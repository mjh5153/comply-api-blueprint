package com.init_spring_bean_mvn.demo.worker;

public class DiscountComplianceAuditor extends DigitalEmployee{
    private String discountPolicies;
    private String billingRecords;

    public DiscountComplianceAuditor(String name, String birthDate, String hireDate, String discountPolicies, String billingRecords) {
        super(name, birthDate, hireDate);
        this.discountPolicies = discountPolicies;
        this.billingRecords = billingRecords;
    }

    public String getDiscountPolicies() {
        return discountPolicies;
    }

    public void setDiscountPolicies(String discountPolicies) {
        this.discountPolicies = discountPolicies;
    }

    public String getBillingRecords() {
        return billingRecords;
    }

    public void setBillingRecords(String billingRecords) {
        this.billingRecords = billingRecords;
    }
}
