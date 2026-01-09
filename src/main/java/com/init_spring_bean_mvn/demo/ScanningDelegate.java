package com.init_spring_bean_mvn.demo;

import java.util.Objects;

public class ScanningDelegate {
    private String lob;
    private String type;
    private int internalHash;


    public ScanningDelegate(String LOB, String type) {
        this.lob = LOB;
        this.type = type;
        this.internalHash = (LOB.equals("legal")) ? 11 : 12;
    }



    @Override
    public String toString() {
        return type + " of " + lob;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ScanningDelegate that = (ScanningDelegate) o;
        return lob.equals(that.lob) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = lob.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
