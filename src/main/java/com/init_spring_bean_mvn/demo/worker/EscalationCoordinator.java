package com.init_spring_bean_mvn.demo.worker;

public class EscalationCoordinator extends DigitalEmployee{
    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    private String findings;

    public EscalationCoordinator(String name, String birthDate, String hireDate, String findings) {
        super(name, birthDate, hireDate);
        this.findings = findings;
    }

    public void packageFindings() {
        // Logic to package findings for escalation
    }

    public void escalate() {
        // Logic to escalate the issue to the appropriate team or individual
    }

    public void detect() {

    }
}
