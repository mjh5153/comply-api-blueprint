package com.init_spring_bean_mvn.demo.bean;

public class Company {
    private String name;
    private int id;

    public Company(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
