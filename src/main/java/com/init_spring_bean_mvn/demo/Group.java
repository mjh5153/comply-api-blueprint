package com.init_spring_bean_mvn.demo;

import java.util.ArrayList;
import java.util.List;

public class Group<T> {
    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    int scanned = 0;
    int cleared = 0;
    int violations = 0;

    int considerations = 0;

    private final List<T> members = new ArrayList<>();

    public boolean addDelegate(T t) {
        if(members.contains(t)) {
            return false;
        } else {
            members.add(t);
            return true;
        }
    }

    public int numDelegates() {
        return this.members.size();
    }

    public void scan(Group systemResults, int ourResults, int scanResults) {
        if(ourResults > scanResults) {
            cleared++;
        } else if(ourResults == scanResults) {
            considerations++;
        } else {
            violations++;
        }
        scanned++;
        if(systemResults != null) {
            systemResults.scan(null, ourResults, scanResults);
        }
    }

    private void listDelegates() {
        for(T delegate : members) {
            System.out.println(" listing delegates " + delegate.getClass().getName());
        }
    }

    public int ranking() {
        return (cleared * 2) + considerations;
    }

}
