package com.init_spring_bean_mvn.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

record Scan(String scanId, String name, String type){};

record Purchase(String scanId, int delegateId, double price, int year, int dayOfYear){
    public LocalDate purchaseDate() {
    return LocalDate.ofYearDay(year, dayOfYear);
    }
};



public class Delegate {
    public static int lastId = 1;
    private String name;
    private int id;

    private List<Scan> scanList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Delegate(String name, List<Scan> scanList) {
        this.name = name;
        this.scanList = scanList;
        id = lastId++;
    }

    public Delegate(String name, Scan scan) {
        this(name, new ArrayList<>(List.of(scan)));
    }

    public Delegate(String name) {
        this(name, new ArrayList<>());
    }

    public String getName() {
        return this.name;
    }

    public void addScan(Scan scan) {
        scanList.add(scan);
    }

    @Override
    public String toString() {
        String[] courseNames = new String[scanList.size()];
        Arrays.setAll(courseNames, i -> scanList.get(i).name());
        return "[%d] %s:".formatted(id, String.join(", ", courseNames));
    }
}
