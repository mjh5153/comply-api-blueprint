package com.init_spring_bean_mvn.demo.classobjectstaticinstancefieldsmethods;

public class Scan {
    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String scanId = "12345";
    private String name = "HIPAA Security Risk Assessment";
    private String type = "Compliance Scan";

    public String getName() {
        return name;
    }


    public Scan(String s, String s1, String s2) {
    }
}
